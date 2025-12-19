package pl.matiu.pantrytrack.fragments.scannerFragment

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import pl.matiu.pantrytrack.databinding.FragmentProductScannerBinding
import pl.matiu.pantrytrack.fragments.barcodeScannerFragment.BarCodeFragmentDirections
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.ProductScannedEntity
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.bitmapToByteArray
import pl.matiu.pantrytrack.productDatabase.scannedProductPhoto.resizeBitmap
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class ProductScannerFragment : Fragment() {

    private var _binding: FragmentProductScannerBinding? = null
    private val binding get() = _binding!!
    private var imageCapture: ImageCapture? = null
    private lateinit var cameraExecutor: ExecutorService
    inline val navigator get() = findNavController()
    private lateinit var productScannerDialogViewModel: ProductScannerDialogViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductScannerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productScannerDialogViewModel = ViewModelProvider(requireActivity())[(ProductScannerDialogViewModel::class.java)]

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            requestPermissions()
        }

        setupListeners()
        setupObservers()

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun setupListeners() {
        binding.imageCaptureButton.setOnClickListener { takePhoto() }
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            productScannerDialogViewModel.dialogResult.collect { status ->
                when(status) {
                    is ProductScannerDialogResult.Start -> {
                        Toast.makeText(requireContext(), "Dialog started", Toast.LENGTH_SHORT).show()
                    }
                    is ProductScannerDialogResult.Cancelled -> {
                        productScannerDialogViewModel.setDialogResult(result = ProductScannerDialogResult.Start)

                        navigator.navigate(ProductScannerFragmentDirections.toFirstFragmentPage(eanCode = ""))

                        Toast.makeText(requireContext(), "Dialog cancelled", Toast.LENGTH_SHORT).show()
                    }
                    is ProductScannerDialogResult.SuccessAdd -> {
                        val resized = resizeBitmap(status.imagePhoto, 800, 800)
                        Log.d("BitmapSize", "ByteArray size: ${bitmapToByteArray(resized).size} bytes (${bitmapToByteArray(resized).size / 1024} KB)")

                        //check if product exist
                        val productScannedEntity = ProductScannedEntity(
                            name = status.name,
                            productDetailsId = status.productDetailsId,
                            scannedPhoto = bitmapToByteArray(resized),
                            amount = 1
                        )


                        productScannerDialogViewModel.saveScannedProduct(productScannedEntity =
                            productScannedEntity
                        )

                        productScannerDialogViewModel.setDialogResult(result = ProductScannerDialogResult.Start)

                        navigator.navigate(
                            BarCodeFragmentDirections.toFirstFragmentPage(eanCode = ""))

                        Toast.makeText(requireContext(), "Dialog accepted", Toast.LENGTH_SHORT).show()
                    }
                    is ProductScannerDialogResult.SuccessDelete -> {
                        val resized = resizeBitmap(status.imagePhoto, 800, 800)
                        Log.d("BitmapSize", "ByteArray size: ${bitmapToByteArray(resized).size} bytes (${bitmapToByteArray(resized).size / 1024} KB)")

                        val productScannedEntity = ProductScannedEntity(
                            name = status.name,
                            productDetailsId = status.productDetailsId,
                            scannedPhoto = bitmapToByteArray(resized),
                            amount = 1
                        )

                        productScannerDialogViewModel.deleteScannedProduct(productScannedEntity =
                            productScannedEntity
                        )

                        productScannerDialogViewModel.setDialogResult(result = ProductScannerDialogResult.Start)

                        navigator.navigate(
                            BarCodeFragmentDirections.toFirstFragmentPage(eanCode = ""))

                        Toast.makeText(requireContext(), "Dialog accepted", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val bitmap = imageProxyToBitmap(image)

                    showDialog(bitmap)

                    image.close()
                }
            }
        )
    }

    private fun imageProxyToBitmap(imageProxy: ImageProxy): Bitmap {
        val planeProxy = imageProxy.planes[0]
        val buffer = planeProxy.buffer
        val bytes = ByteArray(buffer.remaining())
        buffer.get(bytes)

        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }


    private fun startCamera() {

        imageCapture = ImageCapture.Builder().build()

        val cameraProviderFuture = requireContext().applicationContext?.let {
            ProcessCameraProvider.getInstance(
                it
            )
        }

        cameraProviderFuture?.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture)

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(requireContext().applicationContext!!))


    }

    private fun requestPermissions() {
        activityResultLauncher.launch(REQUIRED_PERMISSIONS)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        requireContext().applicationContext?.let { it1 ->
            ContextCompat.checkSelfPermission(
                it1, it)
        } == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if (!permissionGranted) {
                Toast.makeText(requireContext().applicationContext,
                    "Permission request denied",
                    Toast.LENGTH_SHORT).show()
            } else {
                startCamera()
            }
        }

    private fun showDialog(photo: Bitmap) {
        val dialog = ProductScannerDialogFragment(myPhoto = photo)
        dialog.show(parentFragmentManager, "productscannerdialogfragment")
    }
}