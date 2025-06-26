package pl.matiu.pantrytrack.scanner

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import pl.matiu.pantrytrack.R
import pl.matiu.pantrytrack.barcodeScanner.BarCodeDialogResult
import pl.matiu.pantrytrack.barcodeScanner.BarCodeFragmentDirections
import pl.matiu.pantrytrack.databinding.FragmentFirstBinding
import pl.matiu.pantrytrack.databinding.ProductScannerPhotoBinding
import pl.matiu.pantrytrack.machineLearning.classifyImage
import pl.matiu.pantrytrack.product.Product

class ProductScannerDialogFragment(val myPhoto: Bitmap): DialogFragment() {

    private lateinit var productScannerDialogViewModel: ProductScannerDialogViewModel
    inline val navigator get() = findNavController()

    private var _binding: ProductScannerPhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        productScannerDialogViewModel =
            ViewModelProvider(requireActivity())[(ProductScannerDialogViewModel::class.java)]

        _binding = ProductScannerPhotoBinding.inflate(LayoutInflater.from(context))
        binding.productPhoto.setImageBitmap(myPhoto)
        //tutaj
        binding.productNameEditText.setText(classifyImage(myPhoto, requireContext()))

        setListeners()

        return activity?.let {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Czy chcesz dodać ten produkt?")
                .setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setListeners() {
        binding.acceptButton.setOnClickListener {
            dismiss()
            productScannerDialogViewModel.setDialogResult(
                ProductScannerDialogResult.Success(
                    name = binding.productNameEditText.text.toString(),
                    imagePhoto = myPhoto
                )
            )
        }

        binding.cancelButton.setOnClickListener{
            dismiss()
            productScannerDialogViewModel.setDialogResult(ProductScannerDialogResult.Cancelled)
        }
    }

}