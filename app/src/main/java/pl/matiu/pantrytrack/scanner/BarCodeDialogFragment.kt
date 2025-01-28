package pl.matiu.pantrytrack.scanner

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider

class BarCodeDialogFragment(val name: String): DialogFragment() {

    private lateinit var barCodeDialogViewModel: BarCodeDialogViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        barCodeDialogViewModel = ViewModelProvider(requireActivity())[(BarCodeDialogViewModel::class.java)]

        return activity?.let {

            val editText = EditText(it)

            val builder = AlertDialog.Builder(it)
            builder.setMessage("Czy chcesz dodać ten produkt?")
                .setView(editText)
                .setPositiveButton("Dodaj") { dialog, id ->
                    barCodeDialogViewModel.setDialogResult(BarCodeDialogResult.Success(name = name))
                }
                .setNegativeButton("Anuluj") { dialog, id ->
                    barCodeDialogViewModel.setDialogResult(BarCodeDialogResult.Cancelled)
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}