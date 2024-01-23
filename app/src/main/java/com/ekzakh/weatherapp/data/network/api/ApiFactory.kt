package com.ekzakh.weatherapp.data.network.api

import com.ekzakh.weatherapp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {
    private val BASE_URL = "https://api.weatherapi.com/v1/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                },
        )
        .addInterceptor { chain ->
            val originalRequest = chain.request()
            val newUrl = originalRequest.url
                .newBuilder()
                .addQueryParameter("key", BuildConfig.WEATHER_API_KEY)
                .build()
            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()
            chain.proceed(newRequest)
        }
        .build()

    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T : Any> create(apiService: Class<T>): T = builder.create(apiService)
}
