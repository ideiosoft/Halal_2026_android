package com.dapl.halaladvisor.productscan

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ScanUsageApi {

    @GET("api/scan_usage/{userId}")
    fun getScanUsage(
        @Path("userId") userId: String
    ): Single<ScanUsageResponse>

    @POST("api/increment_scan")
    fun incrementScan(
        @Body body: IncrementScanRequest
    ): Single<IncrementScanResponse>

    @POST("api/history_add")
    fun historyAdd(
        @Body body: HistoryAddRequest
    ): Single<HistoryAddResponse>

    @GET("api/history_list/{userId}")
    fun historyList(
        @Path("userId") userId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Single<HistoryListResponse>

    @GET("api/history_summary/{userId}")
    fun historySummary(
        @Path("userId") userId: String
    ): Single<HistorySummaryResponse>

    @POST("api/activate_subscription")
    fun activateSubscription(
        @Body body: ActivateSubscriptionRequest
    ): Single<ActivateSubscriptionResponse>

    @GET("api/subscription_status/{userId}")
    fun subscriptionStatus(
        @Path("userId") userId: String
    ): Single<SubscriptionStatusResponse>
}
