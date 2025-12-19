package pl.matiu.pantrytrack.fragments.categoryFragment

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import pl.matiu.pantrytrack.databinding.FragmentCategoryBinding
import pl.matiu.pantrytrack.fragments.scannerFragment.ProductScannerDialogFragment
import pl.matiu.pantrytrack.fragments.scannerFragment.ProductScannerDialogViewModel
import pl.matiu.pantrytrack.product.ScannedProductAdapter
import pl.matiu.pantrytrack.productDatabase.productDetails.Type

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    inline val navigator get() = findNavController()

    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryViewModel: CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryViewModel = ViewModelProvider(requireActivity())[(CategoryViewModel::class.java)]

        categoryRecyclerView = binding.categoryRecyclerView
        categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        categoryAdapter = CategoryAdapter(categories = emptyList(), navController = navigator, context = requireContext())

        selectListeners()
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            categoryViewModel.category.collect { categories ->
                val categoriesAdapter = CategoryAdapter(categories, navigator, context = requireContext())
                categoryRecyclerView.adapter = categoriesAdapter
            }
        }
    }

    private fun selectListeners() {

        binding.addCategoryButton.setOnClickListener {
            showDialog()
        }

//        binding.dairyButton.setOnClickListener {
//            navigator.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(type = Type.DAIRY.toString()))
//        }

//        binding.breadButton.setOnClickListener {
//            navigator.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(type = Type.BREAD.toString()))
//        }
//
//        binding.meatButton.setOnClickListener {
//            navigator.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(type = Type.MEAT.toString()))
//        }
//
//        binding.fruitButton.setOnClickListener {
//            navigator.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(type = Type.FRUIT.toString()))
//        }
//
//        binding.vegetableButton.setOnClickListener {
//            navigator.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(type = Type.VEGETABLE.toString()))
//        }
//
//        binding.drinkButton.setOnClickListener {
//            navigator.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(type = Type.DRINK.toString()))
//        }
    }

    private fun showDialog() {
        val dialog = CategoryDialogFragment()
        dialog.show(parentFragmentManager, "categorydialogfragment")
    }
}