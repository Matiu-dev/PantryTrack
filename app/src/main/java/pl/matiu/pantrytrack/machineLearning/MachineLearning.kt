package pl.matiu.pantrytrack.machineLearning

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

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

fun classifyImage2(bitmap: Bitmap, context: Context): ClassificationReturnValue? {
    val image = TensorImage.fromBitmap(bitmap)

    val classifier = ImageClassifier.createFromFile(context, "model_with_metadata_mobile_net_v7.tflite")
    val results: List<Classifications> = classifier.classify(image)

    val topResult = results.firstOrNull()?.categories?.maxByOrNull { it.score }?.index

    var maxScore = 0.0F
    results.firstOrNull()?.categories?.filter { it.index == topResult}?.map { maxScore= it.score }

    Log.d("maxScore", "maxScore ${maxScore.toString()}")

    when(topResult) {
        ProductClassEnum2.HOMOGENIZOWANYPIATNICA.productNumber -> {
            if(maxScore > 0.9) {
                return ClassificationReturnValue(
                    productName = "Homogenizowany Piatnica",
                    classificationValue = maxScore,
                    productId = 0
                )
            } else {
                return ClassificationReturnValue(
                    productName = "Możliwe: Homogenizowany Piatnica",
                    classificationValue = maxScore,
                    productId = 0
                )
            }
        }

        ProductClassEnum2.SKYRPIATNICA.productNumber -> {
            if(maxScore > 0.9) {
                return ClassificationReturnValue(
                    productName = "SKYR Piatnica",
                    classificationValue = maxScore,
                    productId = 1
                )
            } else {
                return ClassificationReturnValue(
                    productName = "Możliwe: SKYR Piatnica",
                    classificationValue = maxScore,
                    productId = 1
                )
            }
        }

        ProductClassEnum2.WIEJSKIPIATNICA.productNumber -> {
            if(maxScore > 0.9) {
                return ClassificationReturnValue(
                    productName = "Wiejski Piatnica",
                    classificationValue = maxScore,
                    productId = 2
                )
            } else {
                return ClassificationReturnValue(
                    productName = "Możliwe: Wiejski Piatnica",
                    classificationValue = maxScore,
                    productId = 2
                )
            }
        }
    }

    return null
}