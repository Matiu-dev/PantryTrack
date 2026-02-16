package pl.matiu.pantrytrack.api

import android.content.Context
import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.BufferedReader
import java.io.DataInput
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStreamReader
import javax.inject.Inject

class ApiRepository @Inject constructor(private val myApi: MyApi, private val context: Context) {

    suspend fun downloadModels(modelName: String){
        val apiReturn = myApi.getModel(modelName).body()

        apiReturn?.let { value ->
            saveModel(value, modelName)
        }
    }

    fun saveModelLocally(modelName: String): File? {
        val file = File(context.filesDir, "$modelName.tflite")

        if (file.exists()) return file

        return try {
            context.assets.open(modelName + ".tflite").use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file
        } catch (e: FileNotFoundException) {
            null
        }
    }

    fun saveModel(body: ResponseBody?, modelName: String) {

        val file = File(context.filesDir, "$modelName.tflite")

        body?.byteStream().use { input ->
            file.outputStream().use { output ->
                input?.copyTo(output)
            }
        }
    }

    fun readModel(modelName: String): File? {
        val file = File(context.filesDir, "$modelName.tflite")

        if(!file.exists()) {
            return null
        }

        return file
    }
}