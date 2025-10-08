package pl.matiu.pantrytrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.matiu.pantrytrack.databinding.FragmentCategoryBinding
import pl.matiu.pantrytrack.databinding.FragmentSecondBinding
import pl.matiu.pantrytrack.productDatabase.productDetails.Type
import pl.matiu.pantrytrack.scannerFragment.ProductScannerFragmentDirections


class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!
    inline val navigator get() = findNavController()

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

        selectListeners()
    }

    private fun selectListeners() {
        binding.dairyButton.setOnClickListener {
            navigator.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(type = Type.DAIRY.toString()))
        }

        binding.breadButton.setOnClickListener {
            navigator.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(type = Type.BREAD.toString()))
        }

        binding.meatButton.setOnClickListener {
            navigator.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(type = Type.MEAT.toString()))
        }

        binding.fruitButton.setOnClickListener {
            navigator.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(type = Type.FRUIT.toString()))
        }

        binding.vegetableButton.setOnClickListener {
            navigator.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(type = Type.VEGETABLE.toString()))
        }

        binding.drinkButton.setOnClickListener {
            navigator.navigate( CategoryFragmentDirections.fromCategoryToFirstFragmentPage(type = Type.DRINK.toString()))
        }
    }

}