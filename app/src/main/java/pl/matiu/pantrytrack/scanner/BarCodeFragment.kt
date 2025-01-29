package pl.matiu.pantrytrack.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import kotlinx.coroutines.launch
import pl.matiu.pantrytrack.databinding.FragmentBarCodeBinding

class BarCodeFragment : Fragment() {

    private var _binding: FragmentBarCodeBinding? = null
    private val binding get() = _binding!!
    private val CAMERA_PERMISSION_REQUEST_CODE = 1001
    var zXingScanner: DecoratedBarcodeView? = null
    inline val navigator get() = findNavController()

    private lateinit var barCodeDialogViewModel: BarCodeDialogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBarCodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        barCodeDialogViewModel = ViewModelProvider(requireActivity())[(BarCodeDialogViewModel::class.java)]

        zXingScanner = binding.barCodeView

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            startCamera()
        }

        val decoder = DefaultDecoderFactory(
            listOf(
                BarcodeFormat.EAN_13,
                BarcodeFormat.CODE_128,
                BarcodeFormat.QR_CODE
            )
        )
        zXingScanner?.decoderFactory = decoder

        val capture = CaptureManager(requireActivity(), zXingScanner)
        capture.initializeFromIntent(requireActivity().intent, savedInstanceState)

        zXingScanner?.decodeSingle { result ->
            showDialog(result.toString())
        }

        selectObservers()
    }

    private fun selectObservers() {
        lifecycleScope.launch {
            barCodeDialogViewModel.dialogResult.collect { status ->
                when(status) {
                    BarCodeDialogResult.Start -> {
                        Toast.makeText(requireContext(), "Dialog started", Toast.LENGTH_SHORT).show()
                    }
                    BarCodeDialogResult.Cancelled -> {
                        barCodeDialogViewModel.setDialogResult(result = BarCodeDialogResult.Start)
                        navigator.navigate(BarCodeFragmentDirections.toFirstFragmentPage(eanCode = ""))

                        Toast.makeText(requireContext(), "Dialog cancelled", Toast.LENGTH_SHORT).show()
                    }
                    is BarCodeDialogResult.Success -> {
                        barCodeDialogViewModel.setDialogResult(result = BarCodeDialogResult.Start)
                        navigator.navigate(
                            BarCodeFragmentDirections.toFirstFragmentPage(
                                eanCode = status.product.name,
                                amount = status.product.amount.toString(),
                                price = status.product.price.toString()
                            )
                        )

                        Toast.makeText(requireContext(), "Dialog accepted", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showDialog(name: String) {
        val dialog = BarCodeDialogFragment(name)
        dialog.show(parentFragmentManager, "barcodefragment")
    }

    private fun startCamera() {
        zXingScanner?.resume()
    }

    override fun onPause() {
        super.onPause()

        zXingScanner?.pause()
    }
}