package pl.matiu.pantrytrack.fragments.barcodeScannerFragment

import pl.matiu.pantrytrack.fragments.product.Product

sealed class BarCodeDialogResult {
    data object Start: BarCodeDialogResult()
    data object Cancelled: BarCodeDialogResult()
    data class Success(val product: Product): BarCodeDialogResult()
}