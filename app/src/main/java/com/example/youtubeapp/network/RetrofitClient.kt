package com.example.youtubeapp.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.0.105:3000/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor()) // Add the custom interceptor here
        .build()


    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()).client(okHttpClient)
        .build()

}
