package pl.matiu.pantrytrack.fragments.barcodeScannerFragment

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import pl.matiu.pantrytrack.fragments.product.Product

class BarCodeDialogFragment(val name: String): DialogFragment() {

    private lateinit var barCodeDialogViewModel: BarCodeDialogViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        barCodeDialogViewModel = ViewModelProvider(requireActivity())[(BarCodeDialogViewModel::class.java)]

        return activity?.let {
            val amountTextView = TextView(it)
            amountTextView.text = "Amount"
            val priceTextView = TextView(it)
            priceTextView.text = "Price"

            val amountEditText = EditText(it)
            val priceEditText = EditText(it)

            val layout = LinearLayout(it)
            layout.orientation = LinearLayout.VERTICAL
            layout.addView(amountTextView)
            layout.addView(amountEditText)
            layout.addView(priceTextView)
            layout.addView(priceEditText)

            val builder = AlertDialog.Builder(it)
            builder.setMessage("Czy chcesz dodać ten produkt?")
                .setView(layout)

                .setPositiveButton("Dodaj") { dialog, id ->
                    barCodeDialogViewModel.setDialogResult(
                        BarCodeDialogResult.Success(
                            Product(
                                name = name,
                                amount = amountEditText.text.toString().toInt(),
                                price = priceEditText.text.toString().toDouble()
                            )
                        )
                    )
                }
                .setNegativeButton("Anuluj") { dialog, id ->
                    barCodeDialogViewModel.setDialogResult(BarCodeDialogResult.Cancelled)
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}