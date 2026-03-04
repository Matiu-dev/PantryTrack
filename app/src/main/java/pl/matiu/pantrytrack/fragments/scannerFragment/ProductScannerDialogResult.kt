package pl.matiu.pantrytrack.fragments.scannerFragment

import android.graphics.Bitmap

sealed class ProductScannerDialogResult {
    data object Start: ProductScannerDialogResult()
    data object Cancelled: ProductScannerDialogResult()
    data class SuccessAdd(val name: String, val imagePhoto: Bitmap, val productDetailsId: Int, val quantity: Int = 0, val targetQuantity: Int = 1): ProductScannerDialogResult()
    data class SuccessDelete(val name: String, val imagePhoto: Bitmap, val productDetailsId: Int, val targetQuantity: Int = 1): ProductScannerDialogResult()
}