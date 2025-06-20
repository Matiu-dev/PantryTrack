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
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedEntity
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedRepository
import javax.inject.Inject

@HiltViewModel
class FirstFragmentViewModel @Inject constructor(private val productRepository: ProductRepository,
    private val productScannedRepository: ProductScannedRepository): ViewModel() {

    private var _productList = MutableStateFlow<List<Product>?>(emptyList())
    val productList = _productList.asStateFlow()

    private var _scannedProductList = MutableStateFlow<List<ProductScannedEntity>?>(emptyList())
    val scannedProductList = _scannedProductList.asStateFlow()

    init {
        Log.d("products", "products view model init")
        addInitialProducts()
        addInitialScannedProducts()
    }


    private fun addInitialProducts() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
//                delay(3000)
                _productList.value = productRepository.getProducts()
            }
        }
    }

    fun addProduct(product: Product) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
//                delay(3000)

                productRepository.addProduct(product)
                _productList.value = productRepository.getProducts()
            }
        }
    }

    fun deleteScannedProducts(productScannedEntity: ProductScannedEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
//                delay(3000)
                productScannedRepository.deleteProduct(productScannedEntity)
                _scannedProductList.value = _scannedProductList.value?.filter { it.productId != productScannedEntity.productId }
            }
        }
    }

    private fun addInitialScannedProducts() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
//                delay(3000)
                _scannedProductList.value = productScannedRepository.getProducts()
            }
        }
    }
}