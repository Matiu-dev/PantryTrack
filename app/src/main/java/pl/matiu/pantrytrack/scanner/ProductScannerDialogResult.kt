package pl.matiu.pantrytrack.scanner

import android.graphics.Bitmap
import pl.matiu.pantrytrack.product.Product

sealed class ProductScannerDialogResult {
    data object Start: ProductScannerDialogResult()
    data object Cancelled: ProductScannerDialogResult()
    data class Success(val name: String, val imagePhoto: Bitmap): ProductScannerDialogResult()
}