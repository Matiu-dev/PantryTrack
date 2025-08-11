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
import pl.matiu.pantrytrack.productDatabase.productDetails.Energy
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsEntity
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsRepository
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedEntity
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedRepository
import javax.inject.Inject

@HiltViewModel
class FirstFragmentViewModel @Inject constructor(private val productRepository: ProductRepository,
    private val productScannedRepository: ProductScannedRepository,
    private val productDetailsRepository: ProductDetailsRepository): ViewModel() {

    private var _productList = MutableStateFlow<List<Product>?>(emptyList())
    val productList = _productList.asStateFlow()

    private var _scannedProductList = MutableStateFlow<List<ProductScannedEntity>?>(emptyList())
    val scannedProductList = _scannedProductList.asStateFlow()

    init {
        Log.d("products", "products view model init")
        addInitialProducts()
        addInitialScannedProducts()
        createProductDetails()
    }

    private fun createProductDetails() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.d("product details", "adding")
                val productDetailsHomogenizowany = ProductDetailsEntity(
                    productDetailsId = 0,
                    numberOfProtein = 7.2,
                    numberOfFat = 6.3,
                    numberOfCarbohydrate = 13.0,
                    numberOfSalt = 0.2,
                    energy = Energy(138, "kcal"),
                )

                val productDetailsSkyr = ProductDetailsEntity(
                    productDetailsId = 1,
                    numberOfProtein = 9.6,
                    numberOfFat = 0.0,
                    numberOfCarbohydrate = 10.0,
                    numberOfSalt = 0.06,
                    energy = Energy(78, "kcal"),
                )

                val productDetailsWiejski = ProductDetailsEntity(
                    productDetailsId = 2,
                    numberOfProtein = 11.0,
                    numberOfFat = 5.0,
                    numberOfCarbohydrate = 2.0,
                    numberOfSalt = 0.7,
                    energy = Energy(97, "kcal"),
                )

                productDetailsRepository.deleteAllData()
                productDetailsRepository.addProductDetails(productDetailsHomogenizowany)
                productDetailsRepository.addProductDetails(productDetailsSkyr)
                productDetailsRepository.addProductDetails(productDetailsWiejski)

            }
        }
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