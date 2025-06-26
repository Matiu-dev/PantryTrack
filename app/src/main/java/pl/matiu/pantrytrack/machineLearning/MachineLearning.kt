package pl.matiu.pantrytrack.machineLearning

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import pl.matiu.pantrytrack.ProductClassEnum

fun classifyImage(bitmap: Bitmap, context: Context): String {
    val image = TensorImage.fromBitmap(bitmap)

    val classifier = ImageClassifier.createFromFile(context, "model.tflite")
    val results: List<Classifications> = classifier.classify(image)


    val topResult = results.firstOrNull()?.categories?.maxByOrNull { it.score }?.index

    var maxScore = 0.0F
    results.firstOrNull()?.categories?.filter { it.index == topResult}?.map { maxScore= it.score }

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
    return "Nie rozpoznano"
}