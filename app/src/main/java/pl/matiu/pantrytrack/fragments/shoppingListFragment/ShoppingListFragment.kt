package pl.matiu.pantrytrack.fragments.shoppingListFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pl.matiu.pantrytrack.R
import pl.matiu.pantrytrack.databinding.FragmentProductScannerBinding
import pl.matiu.pantrytrack.fragments.firstfragment.FirstFragmentViewModel
import kotlin.getValue

class ShoppingListFragment : Fragment() {

    private var _binding: FragmentProductScannerBinding? = null
    private val shoppingListViewModel: ShoppingListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        selectObservers()
    }

    fun selectObservers() {
        lifecycleScope.launch {
            
        }
    }
}