package pl.matiu.pantrytrack.scannerFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsDao
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsEntity
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsRepository
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedEntity
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedRepository
import javax.inject.Inject

@HiltViewModel
class ProductScannerDialogViewModel @Inject constructor(
    private val productScannedRepository: ProductScannedRepository,
    private val productDetailsRepository: ProductDetailsRepository
): ViewModel() {

    private var _dialogResult = MutableStateFlow<ProductScannerDialogResult>(ProductScannerDialogResult.Start)
    val dialogResult = _dialogResult.asStateFlow()

    private var _productDetails = MutableStateFlow<List<ProductDetailsEntity>>(emptyList())
    val productDetails = _productDetails.asStateFlow()

    init {
        getProductDetails()
    }

    private fun getProductDetails() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _productDetails.value = productDetailsRepository.getProductDetails()
            }
        }
    }


    fun setDialogResult(result: ProductScannerDialogResult) {
        _dialogResult.value = result
    }

    suspend fun saveScannedProduct(productScannedEntity: ProductScannedEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {

                var scannedProduct = checkIfScannedProductExist(productScannedEntity = productScannedEntity)

                if(scannedProduct != null) {
                    scannedProduct.amount +=1
                    productScannedRepository.updateProduct(scannedProduct)
                } else {
                    productScannedRepository.addProduct(productScannedEntity)
                }
            }
        }
    }

    suspend fun deleteScannedProduct(productScannedEntity: ProductScannedEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                var scannedProduct = checkIfScannedProductExist(productScannedEntity = productScannedEntity)

                if(scannedProduct != null) {
                    scannedProduct.amount -=1
                    productScannedRepository.updateProduct(scannedProduct)
                }
            }
        }
    }

    suspend fun saveProductDetails(productDetailsEntity: ProductDetailsEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                productDetailsRepository.addProductDetails(productDetailsEntity)
            }
        }
    }

    suspend fun checkIfScannedProductExist(productScannedEntity: ProductScannedEntity): ProductScannedEntity? {
        return productScannedRepository.getProductByName(productScannedName = productScannedEntity.name)
    }
}