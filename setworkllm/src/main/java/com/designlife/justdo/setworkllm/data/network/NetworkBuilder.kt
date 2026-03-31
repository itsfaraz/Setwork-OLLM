package com.designlife.justdo.setworkllm.data.network

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

internal object NetworkBuilder {

    lateinit var okHttpClient: OkHttpClient

    fun instance(
        context: Context,
        baseURL: String,
    ): Retrofit {

        val interceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS) // ✅ IMPORTANT: not infinite
            .writeTimeout(15, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true) // ✅ auto retry
            .addInterceptor(interceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun clearNetwork() {
        if (::okHttpClient.isInitialized){
            // Cancel all ongoing API calls
            NetworkBuilder.okHttpClient.dispatcher.cancelAll()

            // Kill all dead connections
            NetworkBuilder.okHttpClient.connectionPool.evictAll()
        }
    }
}