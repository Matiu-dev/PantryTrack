package pl.matiu.pantrytrack.fragments.categoryFragment

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.navigation.fragment.findNavController
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pl.matiu.pantrytrack.R
import pl.matiu.pantrytrack.databinding.CategoryDialogBinding
import androidx.core.graphics.drawable.toDrawable

class CategoryDialogFragment(): DialogFragment() {
    private lateinit var categoryViewModel: CategoryViewModel
    inline val navigator get() = findNavController()

    private var _binding: CategoryDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        categoryViewModel =
            ViewModelProvider(requireActivity())[(CategoryViewModel::class.java)]

        _binding = CategoryDialogBinding.inflate(LayoutInflater.from(context))

        setListeners()

        return activity?.let {
            val builder = AlertDialog.Builder(requireContext())

            builder.setMessage("Wpisz nazwę kategorii")
                .setView(binding.root)

            val dialog = builder.create()

            val color = ContextCompat.getColor(requireContext(), R.color.gray)
            dialog.window?.setBackgroundDrawable(color.toDrawable())

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun setObservers() {
        lifecycleScope.launch {

        }
    }

    private fun setListeners() {
        binding.categoryButton.setOnClickListener {
            dismiss()
            categoryViewModel.saveCategories(binding.categoryEditText.text.toString())
            navigator.navigate( CategoryFragmentDirections.actionCategoryFragmentSelf())
        }
    }
}