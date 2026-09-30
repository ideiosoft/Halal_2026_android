package com.dapl.halaladvisor.productscan

import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.converter.gson.GsonConverterFactory
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object WoolworthsApiClient {

    private const val BASE_URL = "https://www.woolworths.com.au/"

    private val client = OkHttpClient.Builder()
        .build()

    val api: WoolworthsApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(WoolworthsApiService::class.java)
    }
}

interface WoolworthsApiService {

    @GET("apis/ui/Search/products")
    fun searchProduct(
        @Query("searchTerm") searchTerm: String
    ): Single<WoolworthsResponse>
}