package pl.matiu.pantrytrack.product

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
import pl.matiu.pantrytrack.productDatabase.ProductRepository
import javax.inject.Inject

@HiltViewModel
class FirstFragmentViewModel @Inject constructor(private val productRepository: ProductRepository): ViewModel() {

    private var _productList = MutableStateFlow<List<Product>?>(emptyList())
    val productList = _productList.asStateFlow()

    init {
        Log.d("products", "products view model init")
        addInitialProducts()
    }


    private fun addInitialProducts() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                delay(3000)
                _productList.value = productRepository.getProducts()
            }
        }


    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                delay(3000)

                productRepository.addProduct(product)
                _productList.value = productRepository.getProducts()
            }
        }
    }
}