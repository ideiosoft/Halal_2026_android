

package com.dapl.halaladvisor.productscan

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object GoUpcApiClient {

    private const val BASE_URL = "https://go-upc.com/"
    private const val TAG = "GO_UPC_API"

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d(TAG, message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    val api: GoUpcApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(
                retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create()
            )
            .build()
            .create(GoUpcApi::class.java)
    }


}
object OpenFoodProductClient {

    private const val BASE_URL = "https://au.openfoodfacts.org/api/v0/"

    val api: OpenFoodApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
            .build()
            .create(OpenFoodApi::class.java)
    }
}
object OpenFoodSearchClient {

    private const val BASE_URL = "https://au.openfoodfacts.org/"

    private val okHttpClient by lazy {

        OkHttpClient.Builder()
            .connectTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)

            // 🔥 Optional: Retry interceptor (3 attempts)
            .addInterceptor { chain ->
                var request = chain.request()
                var response = chain.proceed(request)

                var tryCount = 0
                val maxLimit = 3

                while (!response.isSuccessful && tryCount < maxLimit) {
                    tryCount++
                    response.close()
                    response = chain.proceed(request)
                }

                response
            }

            .build()
    }

    val api: OpenFoodSearchApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // ✅ attach client
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
            .build()
            .create(OpenFoodSearchApi::class.java)
    }
}
object ScanUsageApiClient {

    private const val BASE_URL = "https://admin.halaladvisor.com.au/"
    private const val TAG = "SCAN_USAGE_API"

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        Log.d(TAG, message)
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()

    val api: ScanUsageApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
            .build()
            .create(ScanUsageApi::class.java)
    }
}