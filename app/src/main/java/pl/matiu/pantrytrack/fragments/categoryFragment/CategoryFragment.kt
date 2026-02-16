package pl.matiu.pantrytrack.fragments.categoryFragment

import android.os.Bundle
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

        lifecycleScope.launch {
            categoryViewModel.getCategoriesAndModels()
        }

        categoryRecyclerView = binding.categoryRecyclerView
        categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        categoryAdapter = CategoryAdapter(categories = emptyList()) { categoryClicked ->
            categoryViewModel.onCategoryClicked(categoryName = categoryClicked, context = requireContext(), navController = navigator)
        }

        selectListeners()
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            categoryViewModel.category.collect { categories ->
                val categoriesAdapter = CategoryAdapter(categories = categories) { categoryClicked ->
                    categoryViewModel.onCategoryClicked(categoryName = categoryClicked, context = requireContext(), navController = navigator)
                }
                categoryRecyclerView.adapter = categoriesAdapter
            }
        }
    }

    private fun selectListeners() {

        binding.addCategoryButton.setOnClickListener {
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = CategoryDialogFragment()
        dialog.show(parentFragmentManager, "categorydialogfragment")
    }
}