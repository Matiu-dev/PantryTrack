package pl.matiu.pantrytrack.fragments.shoppingListFragment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pl.matiu.pantrytrack.fragments.scannerFragment.ProductScannerDialogResult
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedRepository
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel @Inject constructor( private val productScannedRepository: ProductScannedRepository): ViewModel() {

    private var _dialogResult = MutableStateFlow<ProductScannerDialogResult>(ProductScannerDialogResult.Start)
    val dialogResult = _dialogResult.asStateFlow()

    init {

    }
}