package pl.matiu.pantrytrack.scanner

sealed class BarCodeDialogResult {
    data object Start: BarCodeDialogResult()
    data object Cancelled: BarCodeDialogResult()
    data class Success(val name: String): BarCodeDialogResult()
}