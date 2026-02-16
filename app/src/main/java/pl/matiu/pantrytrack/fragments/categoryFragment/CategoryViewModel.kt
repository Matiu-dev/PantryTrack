package pl.matiu.pantrytrack.fragments.categoryFragment

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.matiu.pantrytrack.MainApplication
import pl.matiu.pantrytrack.api.ApiRepository
import pl.matiu.pantrytrack.configuration.FlavorConfig
import pl.matiu.pantrytrack.fragments.scannerFragment.ProductScannerDialogResult
import pl.matiu.pantrytrack.productDatabase.category.CategoryEntity
import pl.matiu.pantrytrack.productDatabase.category.CategoryRepository
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsEntity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

@HiltViewModel
class CategoryViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    val categoryRepository: CategoryRepository,
    val apiRepository: ApiRepository): ViewModel() {
    private var _category = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val category = _category.asStateFlow()



    init {

    }

    fun getCategoriesAndModels() {
        viewModelScope.launch(Dispatchers.IO) {
            getCategories()
            getModels(context = context)
        }
    }

    suspend fun getCategories() {
         _category.value = categoryRepository.getCategories()
    }

    suspend fun getModels(context: Context) {
        if(FlavorConfig.isLocalServer) {
            for (category in _category.value ) {
                apiRepository.saveModelLocally(category.categoryName)
            }
        }

        if(FlavorConfig.isExternalServer) {
            for (category in _category.value ) {
                apiRepository.downloadModels(category.categoryName)
            }
        }

    }

    fun saveCategories(categoryName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                categoryRepository.saveCategories(CategoryEntity(categoryName = categoryName))
            }
        }
    }

    fun onCategoryClicked(categoryName: String, navController: NavController, context: Context) {

        if(FlavorConfig.isLocalServer) {//tutaj
            if(apiRepository.saveModelLocally(categoryName) != null) {
                Toast.makeText(context, "Istnieje model dla tej kategorii", Toast.LENGTH_SHORT).show()
                navController.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(categoryName))
            } else {
                Toast.makeText(context, "Nie ma modelu dla tej kategorii", Toast.LENGTH_SHORT).show()
            }
        }

        if(FlavorConfig.isExternalServer) {
            if(apiRepository.readModel(categoryName) != null) {
                Toast.makeText(context, "Istnieje model dla tej kategorii", Toast.LENGTH_SHORT).show()
                navController.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(categoryName))
            } else {
                Toast.makeText(context, "Nie ma modelu dla tej kategorii", Toast.LENGTH_SHORT).show()
            }
        }

    }
}