package pl.matiu.pantrytrack.scannerFragment

import android.graphics.Bitmap

sealed class ProductScannerDialogResult {
    data object Start: ProductScannerDialogResult()
    data object Cancelled: ProductScannerDialogResult()
    data class SuccessAdd(val name: String, val imagePhoto: Bitmap, val productDetailsId: Int, val amount: Int = 0): ProductScannerDialogResult()
    data class SuccessDelete(val name: String, val imagePhoto: Bitmap, val productDetailsId: Int): ProductScannerDialogResult()
}