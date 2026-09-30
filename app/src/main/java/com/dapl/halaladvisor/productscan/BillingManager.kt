package com.dapl.halaladvisor.productscan

import android.app.Activity
import android.content.Context
import android.util.Log
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams

/**
 * Google Play Billing client for the Premium subscription.
 *
 * Wraps connection, product-details lookup and the purchase flow for the
 * single subscription product [PREMIUM_PRODUCT_ID]. Every step is logged
 * under [TAG] so the purchase funnel can be traced in logcat.
 */
class BillingManager(
    private val context: Context,
    private val callback: Callback
) : PurchasesUpdatedListener, BillingClientStateListener {

    interface Callback {
        /** A premium purchase completed (and was acknowledged) successfully. */
        fun onPremiumPurchased(purchase: Purchase)

        /** Product details became available; carries the localized price text. */
        fun onPriceReady(formattedPrice: String) {}

        /** The user cancelled the Google Play purchase sheet. */
        fun onPurchaseCancelled() {}

        /** Any billing error worth surfacing to the user. */
        fun onBillingError(message: String) {}
    }

    private var premiumDetails: ProductDetails? = null

    private val billingClient: BillingClient = BillingClient.newBuilder(context)
        .setListener(this)
        .enablePendingPurchases(
            PendingPurchasesParams.newBuilder()
                .enableOneTimeProducts()
                .build()
        )
        .build()

    /** Open the connection to Google Play. Call before launching a purchase. */
    fun startConnection() {
        Log.d(TAG, "startConnection: connecting to Google Play Billing…")
        if (billingClient.isReady) {
            Log.d(TAG, "startConnection: client already ready, querying product details")
            queryProductDetails()
            return
        }
        billingClient.startConnection(this)
    }

    override fun onBillingSetupFinished(billingResult: BillingResult) {
        Log.d(
            TAG,
            "onBillingSetupFinished: code=${billingResult.responseCode} " +
                "msg=${billingResult.debugMessage}"
        )
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
            queryProductDetails()
            queryExistingPurchases()
        } else {
            callback.onBillingError("Billing unavailable (${billingResult.responseCode})")
        }
    }

    override fun onBillingServiceDisconnected() {
        Log.w(TAG, "onBillingServiceDisconnected: connection lost")
    }

    /** Look up [PREMIUM_PRODUCT_ID] so we have a [ProductDetails] to purchase. */
    private fun queryProductDetails() {
        Log.d(TAG, "queryProductDetails: querying product '$PREMIUM_PRODUCT_ID' (SUBS)")
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                listOf(
                    QueryProductDetailsParams.Product.newBuilder()
                        .setProductId(PREMIUM_PRODUCT_ID)
                        .setProductType(BillingClient.ProductType.SUBS)
                        .build()
                )
            )
            .build()

        billingClient.queryProductDetailsAsync(params) { billingResult, queryResult ->
            val products = queryResult.productDetailsList
            Log.d(
                TAG,
                "queryProductDetails result: code=${billingResult.responseCode} " +
                    "count=${products.size}"
            )
            if (billingResult.responseCode != BillingClient.BillingResponseCode.OK) {
                callback.onBillingError("Could not load plan (${billingResult.responseCode})")
                return@queryProductDetailsAsync
            }
            val details = products.firstOrNull { it.productId == PREMIUM_PRODUCT_ID }
            if (details == null) {
                Log.e(TAG, "queryProductDetails: '$PREMIUM_PRODUCT_ID' not found in Play Console")
                callback.onBillingError("Premium plan is not available right now")
                return@queryProductDetailsAsync
            }
            premiumDetails = details
            val price = details.subscriptionOfferDetails
                ?.firstOrNull()
                ?.pricingPhases
                ?.pricingPhaseList
                ?.firstOrNull()
                ?.formattedPrice
            Log.d(TAG, "queryProductDetails: loaded '$PREMIUM_PRODUCT_ID', price=$price")
            price?.let { callback.onPriceReady(it) }
        }
    }

    /** Launch the Google Play purchase sheet for the premium subscription. */
    fun launchPurchaseFlow(activity: Activity) {
        val details = premiumDetails
        if (details == null) {
            Log.w(TAG, "launchPurchaseFlow: product details not ready yet, retrying query")
            callback.onBillingError("Still loading the plan, please try again")
            startConnection()
            return
        }
        val offerToken = details.subscriptionOfferDetails?.firstOrNull()?.offerToken
        if (offerToken == null) {
            Log.e(TAG, "launchPurchaseFlow: no subscription offer token for '$PREMIUM_PRODUCT_ID'")
            callback.onBillingError("Premium plan is misconfigured")
            return
        }

        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(details)
                        .setOfferToken(offerToken)
                        .build()
                )
            )
            .build()

        Log.d(TAG, "launchPurchaseFlow: launching billing flow for '$PREMIUM_PRODUCT_ID'")
        val result = billingClient.launchBillingFlow(activity, flowParams)
        Log.d(
            TAG,
            "launchPurchaseFlow: launch result code=${result.responseCode} " +
                "msg=${result.debugMessage}"
        )
    }

    override fun onPurchasesUpdated(
        billingResult: BillingResult,
        purchases: MutableList<Purchase>?
    ) {
        Log.d(
            TAG,
            "onPurchasesUpdated: code=${billingResult.responseCode} " +
                "purchases=${purchases?.size ?: 0}"
        )
        when (billingResult.responseCode) {
            BillingClient.BillingResponseCode.OK -> {
                purchases?.forEach { handlePurchase(it) }
            }
            BillingClient.BillingResponseCode.USER_CANCELED -> {
                Log.d(TAG, "onPurchasesUpdated: user cancelled")
                callback.onPurchaseCancelled()
            }
            BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                Log.d(TAG, "onPurchasesUpdated: item already owned")
                callback.onBillingError("You already own Premium")
            }
            else -> {
                Log.e(TAG, "onPurchasesUpdated: error ${billingResult.debugMessage}")
                callback.onBillingError("Purchase failed (${billingResult.responseCode})")
            }
        }
    }

    /** Restore/check any subscriptions the user already owns. */
    private fun queryExistingPurchases() {
        val params = com.android.billingclient.api.QueryPurchasesParams.newBuilder()
            .setProductType(BillingClient.ProductType.SUBS)
            .build()
        billingClient.queryPurchasesAsync(params) { billingResult, purchases ->
            Log.d(
                TAG,
                "queryExistingPurchases: code=${billingResult.responseCode} " +
                    "owned=${purchases.size}"
            )
            purchases.forEach { handlePurchase(it) }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        Log.d(
            TAG,
            "handlePurchase: products=${purchase.products} " +
                "state=${purchase.purchaseState} acknowledged=${purchase.isAcknowledged}"
        )
        if (!purchase.products.contains(PREMIUM_PRODUCT_ID)) {
            Log.d(TAG, "handlePurchase: ignoring unrelated product ${purchase.products}")
            return
        }
        if (purchase.purchaseState != Purchase.PurchaseState.PURCHASED) {
            Log.d(TAG, "handlePurchase: not in PURCHASED state, skipping")
            return
        }

        if (!purchase.isAcknowledged) {
            val ackParams = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.acknowledgePurchase(ackParams) { ackResult ->
                Log.d(TAG, "acknowledgePurchase: code=${ackResult.responseCode}")
                if (ackResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    callback.onPremiumPurchased(purchase)
                } else {
                    callback.onBillingError("Could not confirm purchase")
                }
            }
        } else {
            Log.d(TAG, "handlePurchase: already acknowledged, premium active")
            callback.onPremiumPurchased(purchase)
        }
    }

    /** Release the connection. Call from the owner's onDestroy. */
    fun release() {
        Log.d(TAG, "release: ending billing connection")
        if (billingClient.isReady) {
            billingClient.endConnection()
        }
    }

    companion object {
        private const val TAG = "BILLING_MANAGER"

        /** Play Console product id for the monthly Premium subscription. */
        const val PREMIUM_PRODUCT_ID = "premium_1.99"
    }
}
