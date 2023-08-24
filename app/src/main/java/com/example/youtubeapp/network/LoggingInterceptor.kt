package com.example.youtubeapp.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.Buffer


class LoggingInterceptor : Interceptor {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBody = request.body
        val requestBuffer = Buffer()
        requestBody?.writeTo(requestBuffer)

        // Log request details if needed
        // Log.e("Request URL", request.url.toString())
        // Log.e("Request Method", request.method)
        // Log.e("Request Headers", request.headers.toString())
        // Log.e("Request Body", requestBuffer.readString(Charset.forName("UTF-8")))

        val response = chain.proceed(request)
        val responseBody = response.body
        val responseBodyString = responseBody?.string()

        // Log response details
//        Log.e("Response Code", response.code.toString())
//        // Log.e("Response Headers", response.headers.toString())
//        //
//        Log.e("Response Body", responseBodyString!!)

        // Re-create the response body and return it to the caller
        return response.newBuilder()
            .body(responseBodyString?.toResponseBody(responseBody.contentType()))
            .build()
    }
}