package pl.matiu.pantrytrack.product

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import pl.matiu.pantrytrack.machineLearning.ProductClassEnum
import pl.matiu.pantrytrack.R
import pl.matiu.pantrytrack.databinding.FragmentFirstBinding
import pl.matiu.pantrytrack.machineLearning.classifyImage
import pl.matiu.pantrytrack.machineLearning.classifyImage2
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.byteArrayToBitmap

@AndroidEntryPoint
class FirstFragment : Fragment(R.layout.fragment_first) {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    private val productViewModel: FirstFragmentViewModel by viewModels()

//    private lateinit var adapter: ProductAdapter
//    private lateinit var recyclerView: RecyclerView

    private lateinit var scannedProductAdapter: ScannedProductAdapter
    private lateinit var scannedProductRecyclerView: RecyclerView

    private val args: FirstFragmentArgs by navArgs()

    private inline val navigator get() = findNavController()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        scannedProductRecyclerView = binding.scannedProductRecyclerView
        scannedProductRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        scannedProductAdapter = ScannedProductAdapter(
            emptyList(),
            onItemClick = {},
            deleteItemClick = {}
        )

//        recyclerView = binding.recyclerView
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        adapter = ProductAdapter(emptyList())

        if(args.eanCode.isNotEmpty()) {
            Toast.makeText(requireContext(), args.eanCode, Toast.LENGTH_SHORT).show()
            productViewModel.addProduct(Product(args.eanCode, args.price.toDouble(), args.amount.toInt()))
            resetArgs()
        }

        selectListeners()
        selectObservers()
    }

    private fun resetArgs() {
        val currentDestination = navigator.currentDestination?.id
        if(currentDestination == R.id.FirstFragment) {
            val action = FirstFragmentDirections.actionFirstFragmentSelf(eanCode = "", amount = "", price = "")
            navigator.navigate(action)
        }
    }

    private fun selectListeners() {
        binding.buttonBarcodeScanner.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_BarCodeFragment)
        }

        binding.buttonProductScanner.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_ScannerFragment)
        }

        binding.imageView.setOnClickListener {
            imagePicker.launch("image/*")
        }
    }

    private fun selectObservers() {
        lifecycleScope.launch {
            productViewModel.productList.collect { products ->
                products?.let {
                    Log.d("products", it.toString())
//                    adapter.updateProducts(it)
//                    recyclerView.adapter = adapter
                }
            }
        }

        lifecycleScope.launch {
            productViewModel.scannedProductList.collect { scannedProduct ->
                scannedProduct?.let {
                    Log.d("scannedProducts", it.toString())

                    val scannedProductAdapter = ScannedProductAdapter(
                        it,
                        onItemClick = { product ->
                            binding.imageView.setImageBitmap(byteArrayToBitmap(product.scannedPhoto))
                            binding.resultText.text = product.name
                        },
                        deleteItemClick = { product ->
                            productViewModel.deleteScannedProducts(product)
                        }
                    )

                    scannedProductRecyclerView.adapter = scannedProductAdapter
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            binding.imageView.setImageURI(it)
            val bitmap = binding.imageView.drawable.toBitmap()
            binding.resultText.text = classifyImage2(bitmap, context = requireContext())
        }
    }


}