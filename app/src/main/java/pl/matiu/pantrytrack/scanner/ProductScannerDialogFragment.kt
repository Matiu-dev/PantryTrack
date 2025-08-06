package pl.matiu.pantrytrack.scanner

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import pl.matiu.pantrytrack.databinding.ProductScannerPhotoBinding
import pl.matiu.pantrytrack.machineLearning.classifyImage2

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
        binding.productNameEditText.setText(classifyImage2(myPhoto, requireContext()))

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