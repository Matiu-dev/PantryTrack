package pl.matiu.pantrytrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import pl.matiu.pantrytrack.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val args: FirstFragmentArgs by navArgs()
    inline val navigator get() = findNavController()

    var products = mutableListOf(
        Product("Produkt 1", 11.1, 11),
        Product("Produkt 2", 12.1, 12),
        Product("Produkt 3", 13.1, 13),
        Product("Produkt 4", 14.1, 14),
        Product("Produkt 5", 15.1, 15)
    )
    private var productAdapter: ProductAdapter ?= null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(args.eanCode != "") {
            products.add(Product(args.eanCode, 11.1, 11))
        }

        productAdapter = ProductAdapter(products = products)

        selectListeners()

        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = productAdapter
    }

    private fun selectListeners() {
        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }

        binding.buttonBarcodeScanner.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_BarCodeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}