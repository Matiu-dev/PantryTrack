package pl.matiu.pantrytrack.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedEntity
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedRepository
import javax.inject.Inject

@HiltViewModel
class ProductScannerDialogViewModel @Inject constructor(private val productScannedRepository: ProductScannedRepository): ViewModel() {

    private var _dialogResult = MutableStateFlow<ProductScannerDialogResult>(ProductScannerDialogResult.Start)
    val dialogResult = _dialogResult.asStateFlow()

    fun setDialogResult(result: ProductScannerDialogResult) {
        _dialogResult.value = result
    }

    suspend fun saveScannedProduct(productScannedEntity: ProductScannedEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                productScannedRepository.addProduct(productScannedEntity)
            }
        }
    }

    suspend fun deleteScannedProduct(productScannedEntity: ProductScannedEntity) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                productScannedRepository.deleteProduct(productScannedEntity)
            }
        }
    }
}