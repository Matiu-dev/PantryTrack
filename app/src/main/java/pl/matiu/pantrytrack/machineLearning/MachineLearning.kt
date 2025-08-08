package pl.matiu.pantrytrack.machineLearning

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.TensorOperator
import org.tensorflow.lite.support.common.TensorProcessor
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.ResizeOp.ResizeMethod
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun classifyImage(bitmap: Bitmap, context: Context): String {
    val image = TensorImage.fromBitmap(bitmap)

    val classifier = ImageClassifier.createFromFile(context, "model.tflite")
    val results: List<Classifications> = classifier.classify(image)


    val topResult = results.firstOrNull()?.categories?.maxByOrNull { it.score }?.index

    var maxScore = 0.0F
    results.firstOrNull()?.categories?.filter { it.index == topResult}?.map { maxScore= it.score }

    Log.d("maxScore", "maxScore ${maxScore.toString()}")

    when(topResult) {
        ProductClassEnum.HOMOGENIZOWANYPIATNICA.productNumber -> {
            if(maxScore > 0.9) {
//                binding.resultText.text = "Rozponany produkt: Homogenizowany Piatnica z dokładnością ${maxScore}"
                return "Homogenizowany Piatnica ${maxScore}"
            } else {
//                binding.resultText.text = "Nie rozpoznano, za mała dokładność - ${maxScore}"
            }
        }

        ProductClassEnum.SKYRPIATNICA.productNumber -> {
            if(maxScore > 0.9) {
//                binding.resultText.text = "Rozponany produkt: SKYR Piatnica z dokładnością ${maxScore}"
                return "SKYR Piatnica ${maxScore}"
            } else {
//                binding.resultText.text = "Nie rozpoznano, za mała dokładność - ${maxScore}"
            }
        }

        ProductClassEnum.WIEJSKIPIATNICA.productNumber -> {
            if(maxScore > 0.9) {
//                binding.resultText.text = "Rozponany produkt: Wiejski Piatnica z dokładnością ${maxScore}"
                return "Wiejski Piatnica ${maxScore}"
            } else {
//                binding.resultText.text = "Nie rozpoznano, za mała dokładność - ${maxScore}"
            }
        }
    }
    return "Nie rozpoznano $maxScore"
}

fun classifyImage2(bitmap: Bitmap, context: Context): String {
    val image = TensorImage.fromBitmap(bitmap)

    val classifier = ImageClassifier.createFromFile(context, "model_with_metadata17.tflite")
    val results: List<Classifications> = classifier.classify(image)


    val topResult = results.firstOrNull()?.categories?.maxByOrNull { it.score }?.index

    var maxScore = 0.0F
    results.firstOrNull()?.categories?.filter { it.index == topResult}?.map { maxScore= it.score }

    Log.d("maxScore", "maxScore ${maxScore.toString()}")

    when(topResult) {
        ProductClassEnum2.HOMOGENIZOWANYPIATNICA.productNumber -> {
            if(maxScore > 0.9) {
//                binding.resultText.text = "Rozponany produkt: Homogenizowany Piatnica z dokładnością ${maxScore}"
                return "Homogenizowany Piatnica ${maxScore}"
            } else {
//                binding.resultText.text = "Nie rozpoznano, za mała dokładność - ${maxScore}"
            }
        }

        ProductClassEnum2.SKYRPIATNICA.productNumber -> {
            if(maxScore > 0.9) {
//                binding.resultText.text = "Rozponany produkt: SKYR Piatnica z dokładnością ${maxScore}"
                return "SKYR Piatnica ${maxScore}"
            } else {
//                binding.resultText.text = "Nie rozpoznano, za mała dokładność - ${maxScore}"
            }
        }

        ProductClassEnum2.WIEJSKIPIATNICA.productNumber -> {
            if(maxScore > 0.9) {
//                binding.resultText.text = "Rozponany produkt: Wiejski Piatnica z dokładnością ${maxScore}"
                return "Wiejski Piatnica ${maxScore}"
            } else {
//                binding.resultText.text = "Nie rozpoznano, za mała dokładność - ${maxScore}"
            }
        }
    }
    return "Nie rozpoznano $maxScore"
}