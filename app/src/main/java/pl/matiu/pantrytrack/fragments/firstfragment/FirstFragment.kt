package pl.matiu.pantrytrack.fragments.firstfragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import pl.matiu.pantrytrack.R
import pl.matiu.pantrytrack.databinding.FragmentFirstBinding
import pl.matiu.pantrytrack.machineLearning.classifyImage2
import pl.matiu.pantrytrack.fragments.product.FirstFragmentViewModel
import pl.matiu.pantrytrack.fragments.product.Product
import pl.matiu.pantrytrack.fragments.product.ScannedProductAdapter
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.byteArrayToBitmap
import pl.matiu.pantrytrack.sharedPrefs.SharedPrefs
import java.io.File

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

//        requireActivity().title = "test"
        val toolbar = requireActivity().findViewById<MaterialToolbar>(R.id.toolbar)
        toolbar.title = args.type

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
            productViewModel.addProduct(
                Product(
                    args.eanCode,
                    args.price.toDouble(),
                    args.amount.toInt()
                )
            )
            resetArgs()
        }

        if(args.type != "") {
            activity?.let { SharedPrefs().saveType(it.applicationContext, args.type) }
            productViewModel.addInitialScannedProductsByType(args.type)
        } else {
            activity?.let {
                SharedPrefs().readType(it.applicationContext)?.let { type ->
                    productViewModel.addInitialScannedProductsByType(type)
                }
            }
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
        binding.imageView.setOnClickListener {
            imagePicker.launch("image/*")
        }

        binding.scanButton.setOnClickListener {
            navigator.navigate(R.id.action_FirstFragment_to_ScannerFragment)
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
            productViewModel.productNameClicked.collect { productName ->
                binding.resultText.text = productName
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
                            productViewModel.getProductNameByProductDetailsId(product.productDetailsId)
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
        val type: String = SharedPrefs().readType(requireContext()).toString()
        val modelFile: File? = productViewModel.getModel("$type.tflite")

        uri?.let {
            binding.imageView.setImageURI(it)
            val bitmap = binding.imageView.drawable.toBitmap()
            modelFile?.let {
                binding.resultText.text = classifyImage2(bitmap, modelFile = modelFile)?.productName
            }
        }
    }

//    fun saveToSharedPrefs(context: Context, type: String) {
//        val prefs = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
//        prefs.edit().putString("type", type).apply()
//    }

//    fun readFromSharedPrefs(context: Context): String? {
//        val prefs = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
//        return prefs.getString("type", null)
//    }

}