package com.example.data.remote.interceptors

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class ConnectivityInterceptor @Inject constructor() : Interceptor {
    @Throws(Exception::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        try {
            val response = chain.proceed(request)
            val bodyString = response.body()!!.string()
            return response.newBuilder()
                .body(ResponseBody.create(response.body()?.contentType(), bodyString))
                .build()
        } catch (e: Exception) {
            val message: String = when (e) {
                is HttpException -> {
                    "Server is unreachable, please try again later."
                }
                is IOException -> {
                    "Timeout - Please check your internet connection"
                }
                else -> {
                    "Unknown exception: ${e.stackTrace}"
                }
            }
            return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create(null, ""))
                .code(400)
                .message(message)
                .build()
        }
    }
}
