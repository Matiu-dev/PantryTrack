package pl.matiu.pantrytrack.fragments.shoppingListFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.matiu.pantrytrack.R
import pl.matiu.pantrytrack.databinding.FragmentProductScannerBinding

class ShoppingListFragment : Fragment() {

    private var _binding: FragmentProductScannerBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false)
    }

}