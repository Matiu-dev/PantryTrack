package pl.matiu.pantrytrack.fragments.categoryFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.matiu.pantrytrack.fragments.scannerFragment.ProductScannerDialogResult
import pl.matiu.pantrytrack.productDatabase.category.CategoryEntity
import pl.matiu.pantrytrack.productDatabase.category.CategoryRepository
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsEntity
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(val categoryRepository: CategoryRepository): ViewModel() {

    private var _category = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val category = _category.asStateFlow()

    init {
        getCategories()
    }

     fun getCategories() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _category.value = categoryRepository.getCategories()
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
}