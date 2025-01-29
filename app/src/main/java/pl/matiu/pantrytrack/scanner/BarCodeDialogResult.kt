package pl.matiu.pantrytrack.scanner

import pl.matiu.pantrytrack.product.Product

sealed class BarCodeDialogResult {
    data object Start: BarCodeDialogResult()
    data object Cancelled: BarCodeDialogResult()
    data class Success(val product: Product): BarCodeDialogResult()
}