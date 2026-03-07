package pl.matiu.pantrytrack.fragments.shoppingListFragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pl.matiu.pantrytrack.fragments.firstfragment.FirstFragmentProductModel
import pl.matiu.pantrytrack.fragments.scannerFragment.ProductScannerDialogResult
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsEntity
import pl.matiu.pantrytrack.productDatabase.productDetails.ProductDetailsRepository
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedEntity
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedRepository
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor(
    private val productScannedRepository: ProductScannedRepository,
    private val productDetailsRepository: ProductDetailsRepository
): ViewModel() {

    private var _productList = MutableStateFlow<List<FirstFragmentProductModel>>(emptyList())
    val productList = _productList.asStateFlow()

    init {
        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val pL: List<ProductScannedEntity> = productScannedRepository.getProducts()
            val pD: List<ProductDetailsEntity> = productDetailsRepository.getProductDetails()

            _productList .value= pL.map {
                fromProductScannedEntityToFirstFragmentProductModel(it, pD)
            }.filter { it.targetQuantity > it.quantity }
        }
    }

    private fun fromProductScannedEntityToFirstFragmentProductModel(productScannedEntity: ProductScannedEntity, productDetails: List<ProductDetailsEntity>): FirstFragmentProductModel {
        return FirstFragmentProductModel(
            productId = productScannedEntity.productId,
            productName = productDetails.filter { it.productDetailsId == productScannedEntity.productDetailsId }[0].productName,
            quantity = productScannedEntity.quantity,
            productDetailsId = productScannedEntity.productDetailsId,
            scannedPhoto = productScannedEntity.scannedPhoto,
            targetQuantity = productScannedEntity.targetQuantity
        )

    }
}