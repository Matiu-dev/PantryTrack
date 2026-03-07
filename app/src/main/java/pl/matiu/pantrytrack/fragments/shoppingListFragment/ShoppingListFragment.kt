package pl.matiu.pantrytrack.fragments.shoppingListFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.matiu.pantrytrack.R
import pl.matiu.pantrytrack.databinding.FragmentFirstBinding
import pl.matiu.pantrytrack.databinding.FragmentProductScannerBinding
import pl.matiu.pantrytrack.databinding.FragmentShoppingListBinding
import pl.matiu.pantrytrack.fragments.firstfragment.FirstFragmentViewModel
import pl.matiu.pantrytrack.fragments.product.ScannedProductAdapter
import kotlin.getValue

@AndroidEntryPoint
class ShoppingListFragment : Fragment() {

    private var _binding: FragmentShoppingListBinding? = null
    private val binding get() = _binding!!
    private val shoppingListViewModel: ShoppingListViewModel by viewModels()

    private lateinit var shoppingListAdapter: ShoppingListAdapter
    private lateinit var shoppingListRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShoppingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shoppingListRecyclerView = binding.scannedProductRecyclerView
        shoppingListRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        shoppingListAdapter = ShoppingListAdapter(
            emptyList(),
        )

        selectObservers()
    }

    fun selectObservers() {
        lifecycleScope.launch {
            shoppingListViewModel.productList.collect { shoppingList ->
                val shoppingListAdapter = ShoppingListAdapter(shoppingList)
                shoppingListRecyclerView.adapter = shoppingListAdapter
            }
        }
    }
}