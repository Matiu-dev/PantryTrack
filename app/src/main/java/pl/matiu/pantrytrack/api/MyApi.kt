package pl.matiu.pantrytrack.api

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Streaming


interface MyApi {

    @GET("/getModel")
    @Streaming
    suspend fun getModel(@Query("modelName") modelName: String): Response<ResponseBody>

}