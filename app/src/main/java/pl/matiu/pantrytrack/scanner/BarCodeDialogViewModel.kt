package pl.matiu.pantrytrack.scanner

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class BarCodeDialogViewModel: ViewModel() {

    private var _dialogResult = MutableStateFlow<BarCodeDialogResult>(BarCodeDialogResult.Start)
    val dialogResult = _dialogResult.asStateFlow()

    fun setDialogResult(result: BarCodeDialogResult) {
        _dialogResult.value = result
    }
}