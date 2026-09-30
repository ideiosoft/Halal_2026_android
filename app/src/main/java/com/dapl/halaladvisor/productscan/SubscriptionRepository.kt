package com.dapl.halaladvisor.productscan

import android.content.Context
import android.util.Log
import com.dapl.halaladvisor.Utility.PrefUtils
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * App-scoped cache for the user's subscription status.
 *
 * Premium users skip the plan-selection screen, the scan limit, and interstitial ads.
 * Warmed once on the home screen so those decisions are made without a network round-trip.
 */
object SubscriptionRepository {

    @Volatile
    var cached: SubscriptionData? = null
        private set

    private val disposables = CompositeDisposable()

    fun isPremium(): Boolean = cached?.isPremium == true

    private fun userId(context: Context): String? =
        PrefUtils.getProfileObject(context)?.u_id?.takeIf { it.isNotBlank() }

    /** Warm the cache from the home screen. Silent — failures are logged only. */
    fun prefetch(context: Context) {
        val userId = userId(context) ?: return
        disposables.add(
            ScanUsageApiClient.api.subscriptionStatus(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.success) response.data?.let { cached = it }
                }, { error ->
                    Log.e("SUBSCRIPTION_API", "subscription_status failed: ${error.message}")
                })
        )
    }

    /**
     * Activate Premium on the backend after a successful Google Play purchase,
     * then re-check subscription_status to confirm the user is actually premium.
     *
     * 1. POST activate_subscription { u_id, purchaseToken, productId, expiryDate }
     * 2. On success → GET subscription_status/{u_id}
     * 3. Cache the result; [onResult] receives true only if the backend reports premium.
     *
     * [onResult] is delivered on the main thread.
     */
    fun activateAndRefresh(
        context: Context,
        purchaseToken: String,
        productId: String,
        expiryDate: String,
        onResult: (isPremium: Boolean, message: String?) -> Unit
    ) {
        val userIdStr = userId(context)
        val userId = userIdStr?.toLongOrNull()
        if (userIdStr == null || userId == null) {
            Log.e("SUBSCRIPTION_API", "activate_subscription: missing/invalid u_id")
            onResult(false, "Missing user id")
            return
        }

        val request = ActivateSubscriptionRequest(
            uId = userId,
            purchaseToken = purchaseToken,
            productId = productId,
            expiryDate = expiryDate,
            platform = "google"
        )
        Log.d("SUBSCRIPTION_API", "activate_subscription: posting $request")
        disposables.add(
            ScanUsageApiClient.api.activateSubscription(request)
                .subscribeOn(Schedulers.io())
                .flatMap { activateResponse ->
                    if (!activateResponse.success) {
                        Single.error(
                            IllegalStateException(activateResponse.message ?: "Activation failed")
                        )
                    } else {
                        Log.d("SUBSCRIPTION_API", "activate_subscription ok, checking status")
                        ScanUsageApiClient.api.subscriptionStatus(userIdStr)
                    }
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ statusResponse ->
                    val data = statusResponse.data
                    if (statusResponse.success && data != null) cached = data
                    val premium = data?.isPremium == true
                    Log.d("SUBSCRIPTION_API", "subscription_status: isPremium=$premium")
                    onResult(premium, null)
                }, { error ->
                    Log.e("SUBSCRIPTION_API", "activate flow failed: ${error.message}")
                    onResult(false, error.message)
                })
        )
    }
}
