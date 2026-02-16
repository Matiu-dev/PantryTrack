package pl.matiu.pantrytrack.fragments.firstfragment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.matiu.pantrytrack.api.ApiRepository
import pl.matiu.pantrytrack.fragments.product.Product
import pl.matiu.pantrytrack.productDatabase.ProductRepository
import pl.matiu.pantrytrack.productDatabase.productDetails.Energy
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsEntity
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsRepository
import pl.matiu.pantrytrack.productDatabase.productDetails.Type
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedEntity
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FirstFragmentViewModel @Inject constructor(private val productRepository: ProductRepository,
                                                 private val productScannedRepository: ProductScannedRepository,
                                                 private val productDetailsRepository: ProductDetailsRepository,
                                                 val apiRepository: ApiRepository
): ViewModel() {

    private var _productList = MutableStateFlow<List<Product>?>(emptyList())
    val productList = _productList.asStateFlow()

    private var _productNameClicked = MutableStateFlow<String?>(null)
    val productNameClicked = _productNameClicked.asStateFlow()

    private var _scannedProductList = MutableStateFlow<List<FirstFragmentProductModel>?>(emptyList())
    val scannedProductList = _scannedProductList.asStateFlow()

    init {
        Log.d("products", "products view model init")
        addInitialProducts()
//        addInitialScannedProducts()
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
                    type = Type.DAIRY,
                    productName = "Homogenizowany"
                )

                val productDetailsSkyr = ProductDetailsEntity(
                    productDetailsId = 1,
                    numberOfProtein = 9.6,
                    numberOfFat = 0.0,
                    numberOfCarbohydrate = 10.0,
                    numberOfSalt = 0.06,
                    energy = Energy(78, "kcal"),
                    type = Type.DAIRY,
                    productName = "Skyr"
                )

                val productDetailsWiejski = ProductDetailsEntity(
                    productDetailsId = 2,
                    numberOfProtein = 11.0,
                    numberOfFat = 5.0,
                    numberOfCarbohydrate = 2.0,
                    numberOfSalt = 0.7,
                    energy = Energy(97, "kcal"),
                    type = Type.DAIRY,
                    productName = "Wiejski"
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

    fun deleteScannedProducts(firstFragmentProductModel: FirstFragmentProductModel) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
//                delay(3000)
                val scannedProduct = productScannedRepository.getProductByProductId(firstFragmentProductModel.productId)
                scannedProduct?.let {
                    productScannedRepository.deleteProduct(scannedProduct)
                }
                _scannedProductList.value =
                    _scannedProductList.value?.filter { it.productId != scannedProduct?.productId }
            }
        }
    }

     fun addInitialScannedProducts() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
//                delay(3000)
//                _scannedProductList.value = productScannedRepository.getProducts()
            }
        }
    }

    fun addInitialScannedProductsByType(type: String) {
        viewModelScope.launch {

            var scannedProducts =
                withContext(Dispatchers.IO) {
                    productScannedRepository.getProducts()
                }
            var productDetails =
                withContext(Dispatchers.IO) {
                    productDetailsRepository.getProductDetails()
                }

//            val filtered = scannedProducts.filter {
//                val categoryName = it.categoryName
////                id in productDetails.indices && productDetails[id].type.toString() == type
//                categoryName in type
//            }

            val returnList = mutableListOf<FirstFragmentProductModel>()

            for(sP in scannedProducts) {
                if(sP.categoryName == type) {
                    var productName: String? = null

                    for(pD in productDetails) {
                        if(sP.productDetailsId == pD.productDetailsId) {
                            productName = pD.productName
                        }
                    }

                    productName?.let {
                        returnList.add(
                            FirstFragmentProductModel(
                                productName = productName,
                                amount = sP.amount,
                                scannedPhoto = sP.scannedPhoto,
                                productDetailsId = sP.productDetailsId,
                                productId = sP.productId
                            )
                        )
                    }
                }
            }

            _scannedProductList.value = returnList
        }
    }

    fun getModel(categoryName: String): File? {
        return apiRepository.readModel(modelName = categoryName)
    }

    fun getProductNameByProductDetailsId(productDetailsId: Int) {

        viewModelScope.launch(Dispatchers.IO) {
            _productNameClicked.value = productDetailsRepository.getProductNameByProductDetailsId(detailsId = productDetailsId)
        }
    }

}