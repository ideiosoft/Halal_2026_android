package com.dapl.halaladvisor.productscan

import com.google.gson.annotations.SerializedName

data class ScanUsageResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("data") val data: ScanUsageData? = null
)

data class ScanUsageData(
    @SerializedName("usedScans") val usedScans: Int = 0,
    @SerializedName("maxScans") val maxScans: Int = 0,
    @SerializedName("remainingScans") val remainingScans: Int = 0,
    @SerializedName("resetDate") val resetDate: String? = null
)

data class IncrementScanRequest(
    @SerializedName("u_id") val uId: Long
)

data class IncrementScanResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: ScanUsageData? = null
)

data class HistoryAddRequest(
    @SerializedName("u_id") val uId: Long,
    @SerializedName("barcode") val barcode: String,
    @SerializedName("productName") val productName: String,
    @SerializedName("brand") val brand: String,
    @SerializedName("halalStatus") val halalStatus: String
)

data class HistoryAddResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String? = null
)

data class HistoryListResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("data") val data: HistoryListData? = null
)

data class HistoryListData(
    @SerializedName("items") val items: List<HistoryItem>? = null,
    @SerializedName("pagination") val pagination: HistoryPagination? = null
)

data class HistoryItem(
    @SerializedName("id") val id: String? = null,
    @SerializedName("user_id") val userId: String? = null,
    @SerializedName("barcode") val barcode: String? = null,
    @SerializedName("product_name") val productName: String? = null,
    @SerializedName("brand") val brand: String? = null,
    @SerializedName("halal_status") val halalStatus: String? = null,
    @SerializedName("scanned_at") val scannedAt: String? = null
)

data class HistoryPagination(
    @SerializedName("page") val page: Int = 1,
    @SerializedName("limit") val limit: Int = 20,
    @SerializedName("totalItems") val totalItems: Int = 0,
    @SerializedName("totalPages") val totalPages: Int = 0
)

data class HistorySummaryResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("data") val data: HistorySummaryData? = null
)

data class HistorySummaryData(
    @SerializedName("totalScans") val totalScans: Int = 0,
    @SerializedName("halalCount") val halalCount: Int = 0,
    @SerializedName("haramCount") val haramCount: Int = 0,
    @SerializedName("mushboohCount") val mushboohCount: Int = 0
)

data class ActivateSubscriptionRequest(
    @SerializedName("u_id") val uId: Long,
    @SerializedName("purchaseToken") val purchaseToken: String,
    @SerializedName("productId") val productId: String,
    @SerializedName("expiryDate") val expiryDate: String,
    @SerializedName("platform") val platform: String = "google"
)

data class ActivateSubscriptionResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String? = null
)

data class SubscriptionStatusResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("data") val data: SubscriptionData? = null
)

data class SubscriptionData(
    @SerializedName("isPremium") val isPremium: Boolean = false,
    @SerializedName("planName") val planName: String? = null,
    @SerializedName("expiryDate") val expiryDate: String? = null,
    @SerializedName("autoRenew") val autoRenew: Boolean = false
)
