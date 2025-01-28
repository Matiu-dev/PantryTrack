package pl.matiu.pantrytrack

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import pl.matiu.pantrytrack.adapter.ProductAdapter
import pl.matiu.pantrytrack.databinding.FragmentFirstBinding
import pl.matiu.pantrytrack.model.Product
import pl.matiu.pantrytrack.viewModel.FirstFragmentViewModel

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private lateinit var productViewModel: FirstFragmentViewModel
    private lateinit var adapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView

    private val args: FirstFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productViewModel = ViewModelProvider(requireActivity())[(FirstFragmentViewModel::class.java)]

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ProductAdapter(emptyList())

        if(args.eanCode.isNotEmpty()) {
            Toast.makeText(requireContext(), args.eanCode, Toast.LENGTH_SHORT).show()
            productViewModel.addProduct(Product(args.eanCode, args.price.toDouble(), args.amount.toInt()))
        }

        selectListeners()
        selectObservers()
    }

    private fun selectListeners() {
        binding.buttonBarcodeScanner.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_BarCodeFragment)
        }
    }

    private fun selectObservers() {
        lifecycleScope.launch {
            productViewModel.productList.collect { products ->
                products?.let {
                    Log.d("products", it.toString())
                    adapter.updateProducts(it)
                    recyclerView.adapter = adapter
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}