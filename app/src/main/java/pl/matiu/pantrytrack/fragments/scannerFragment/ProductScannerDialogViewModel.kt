package pl.matiu.pantrytrack.fragments.scannerFragment

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.matiu.pantrytrack.api.ApiRepository
import pl.matiu.pantrytrack.configuration.FlavorConfig
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsEntity
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsRepository
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedEntity
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProductScannerDialogViewModel @Inject constructor(
    private val productScannedRepository: ProductScannedRepository,
    private val productDetailsRepository: ProductDetailsRepository,
    private val apiRepository: ApiRepository
): ViewModel() {

    private var _dialogResult = MutableStateFlow<ProductScannerDialogResult>(ProductScannerDialogResult.Start)
    val dialogResult = _dialogResult.asStateFlow()

    private var _productDetails = MutableStateFlow<List<ProductDetailsEntity>>(emptyList())
    val productDetails = _productDetails.asStateFlow()

    private var _targetQuantity = MutableStateFlow<Int?>(0)
    val targetQuantity = _targetQuantity.asStateFlow()
    private val _modelFile = MutableStateFlow<File?>(null)
    val modelFile = _modelFile.asStateFlow()

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

    suspend fun updateTargetQuantity(productDetailsId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _targetQuantity.value = productScannedRepository.getProductByProductDetailsId(productDetailsId = productDetailsId)?.targetQuantity
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
                    scannedProduct.quantity +=1
                    scannedProduct.targetQuantity = productScannedEntity.targetQuantity
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
                    scannedProduct.quantity -=1
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
        return productScannedRepository.getProductByProductDetailsId(productDetailsId = productScannedEntity.productDetailsId)
    }

    fun loadModel(modelName: String, context: Context) {
        if(FlavorConfig.isLocalServer) {
            viewModelScope.launch(Dispatchers.IO) {
                _modelFile.value = apiRepository.readModel(modelName = modelName)
            }
        }

        if(FlavorConfig.isExternalServer) {
            viewModelScope.launch(Dispatchers.IO) {
                _modelFile.value = apiRepository.readModel(modelName = modelName)
            }
        }

    }
}