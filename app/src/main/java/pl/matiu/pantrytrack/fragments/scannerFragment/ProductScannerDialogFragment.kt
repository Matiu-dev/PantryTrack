package pl.matiu.pantrytrack.fragments.scannerFragment

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import pl.matiu.pantrytrack.R
import pl.matiu.pantrytrack.databinding.ProductScannerPhotoBinding
import pl.matiu.pantrytrack.machineLearning.classifyImage2
import pl.matiu.pantrytrack.product.FirstFragment
import pl.matiu.pantrytrack.productDatabase.productDetails.Type

class ProductScannerDialogFragment(val myPhoto: Bitmap): DialogFragment() {

    private lateinit var productScannerDialogViewModel: ProductScannerDialogViewModel
    inline val navigator get() = findNavController()

    private var _binding: ProductScannerPhotoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val type: Type = when(FirstFragment().readFromSharedPrefs(requireContext())) {
            "DAIRY" -> Type.DAIRY
            else -> Type.NONE
        }

        productScannerDialogViewModel =
            ViewModelProvider(requireActivity())[(ProductScannerDialogViewModel::class.java)]

        _binding = ProductScannerPhotoBinding.inflate(LayoutInflater.from(context))
        binding.productPhoto.setImageBitmap(myPhoto)
        binding.productNameEditText.setText(classifyImage2(myPhoto, type = type, requireContext())?.productName)

        val productDetailsAdapter = ArrayAdapter<String>(
            requireContext(),
            R.layout.spinner_item,
            mutableListOf()
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.productCategorySpinner.adapter = productDetailsAdapter
        classifyImage2(myPhoto, type = type,requireContext())?.productId?.let {
            binding.productCategorySpinner.setSelection(it)
        }


        setListeners()
        setObservers(productDetailsAdapter)

        return activity?.let {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Czy chcesz dodać ten produkt?")
                .setView(binding.root)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setObservers(productDetailsAdapter: ArrayAdapter<String>) {
        lifecycleScope.launch {
            productScannerDialogViewModel.productDetails.collect { productDetails ->
                val items = productDetails.map {
                    when(it.productDetailsId.toString()) {
                        "0" -> "Homogenizowany"
                        "1" -> "Skyr"
                        "2" -> "Wiejski"
                        else -> "empty"
                    }
                }

                productDetailsAdapter.clear()
                productDetailsAdapter.addAll(items)
                productDetailsAdapter.notifyDataSetChanged()

            }
        }
    }

    private fun setListeners() {
        binding.acceptButton.setOnClickListener {
            dismiss()
            productScannerDialogViewModel.setDialogResult(
                ProductScannerDialogResult.SuccessAdd(
                    name = binding.productNameEditText.text.toString(),
                    imagePhoto = myPhoto,
                    productDetailsId = binding.productCategorySpinner.selectedItemPosition
                )
            )
        }

        binding.deleteButton.setOnClickListener {
            dismiss()
            productScannerDialogViewModel.setDialogResult(
                ProductScannerDialogResult.SuccessDelete(
                    name = binding.productNameEditText.text.toString(),
                    imagePhoto = myPhoto,
                    productDetailsId = binding.productCategorySpinner.selectedItemPosition
                )
            )
        }

        binding.cancelButton.setOnClickListener{
            dismiss()
            productScannerDialogViewModel.setDialogResult(ProductScannerDialogResult.Cancelled)
        }
    }

}