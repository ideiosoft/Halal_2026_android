//package com.dapl.halaladvisor.productscan
//
//import io.reactivex.Single
//import retrofit2.Response
//import retrofit2.http.GET
//import retrofit2.http.Path
//


package com.dapl.halaladvisor.productscan

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenFoodApi {

    @GET("product/{barcode}.json")
    fun getProduct(
        @Path("barcode") barcode: String,

        @Query("fields") fields: String = "product_name," + "brands," + "categories," + "countries," + "ingredients_text," + "image_front_url," + "image_ingredients_url," + "image_nutrition_url," + "selected_images," + "ingredients," + "additives_tags"

    ): Single<ProductResponse>
}


interface OpenFoodSearchApi {

    @GET("cgi/search.pl")
    fun searchProduct(
        @Query("search_terms") searchTerms: String,
        @Query("search_simple") searchSimple: Int = 1,
        @Query("action") action: String = "process",
        @Query("json") json: Int = 1,
        @Query("page") page: Int = 3,
        @Query("page_size") pageSize: Int = 500,

        @Query("fields") fields: String = "product_name,ingredients_text,brands,image_front_url,categories,countries,code"
    ): Single<SearchResponse>
}


interface GoUpcApi {

    @GET("api/v1/code/{barcode}")
    fun getProduct(
        @Path("barcode") barcode: String, @Query("key") apiKey: String
    ): Single<GoUpcResponse>
}