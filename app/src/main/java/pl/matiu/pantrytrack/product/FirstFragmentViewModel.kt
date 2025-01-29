package pl.matiu.pantrytrack.product

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FirstFragmentViewModel: ViewModel() {

    private var _productList = MutableStateFlow<List<Product>?>(emptyList())
    val productList = _productList.asStateFlow()

    init {
        Log.d("products", "products view model init")
        _productList.value = _productList.value?.plus(
            listOf(
                Product("1", 11.1, 1),
                Product("2", 22.2, 2),
                Product("3", 33.3, 3),
            )
        )
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                delay(3000)
                _productList.value = _productList.value?.plus(product)
            }
        }
    }
}