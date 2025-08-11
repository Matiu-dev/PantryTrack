package pl.matiu.pantrytrack.scannerFragment

import android.graphics.Bitmap

sealed class ProductScannerDialogResult {
    data object Start: ProductScannerDialogResult()
    data object Cancelled: ProductScannerDialogResult()
    data class Success(val name: String, val imagePhoto: Bitmap, val productDetailsId: Int): ProductScannerDialogResult()
}