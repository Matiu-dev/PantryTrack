package pl.matiu.pantrytrack.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.matiu.pantrytrack.model.Product

class FirstFragmentViewModel: ViewModel() {

    private var _productList = MutableStateFlow<List<Product>?>(emptyList())
    val productList = _productList.asStateFlow()


    fun addProduct(product: Product) {
        Log.d("products", "adding new product ${_productList.value?.size}")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                delay(3000)
                val currentList = _productList.value ?: emptyList()
                val updatedList = currentList + product
                _productList.value = updatedList
            }
        }
    }
}