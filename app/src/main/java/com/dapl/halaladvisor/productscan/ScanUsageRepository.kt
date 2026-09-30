package com.dapl.halaladvisor.productscan

import android.content.Context
import android.util.Log
import com.dapl.halaladvisor.Utility.PrefUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * App-scoped cache for the user's monthly scan usage.
 *
 * The network fetch is warmed once on the home screen so the scanner can render the correct
 * state synchronously from [cached] (no UI flicker), then optionally refresh in the background.
 */
object ScanUsageRepository {

    @Volatile
    var cached: ScanUsageData? = null
        private set

    private val disposables = CompositeDisposable()

    private fun userId(context: Context): String? =
        PrefUtils.getProfileObject(context)?.u_id?.takeIf { it.isNotBlank() }

    /** Warm the cache from the home screen. Silent — failures are logged only. */
    fun prefetch(context: Context) {
        val userId = userId(context) ?: return
        disposables.add(
            ScanUsageApiClient.api.getScanUsage(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.success) response.data?.let { cached = it }
                }, { error ->
                    Log.e("SCAN_USAGE_API", "prefetch failed: ${error.message}")
                })
        )
    }

    /**
     * Refresh the cache and deliver the latest data to [onResult] on the main thread.
     * Caller owns nothing here; pass a callback that guards against a destroyed view.
     */
    fun refresh(context: Context, onResult: (ScanUsageData) -> Unit) {
        val userId = userId(context) ?: return
        disposables.add(
            ScanUsageApiClient.api.getScanUsage(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.success) response.data?.let {
                        cached = it
                        onResult(it)
                    }
                }, { error ->
                    Log.e("SCAN_USAGE_API", "refresh failed: ${error.message}")
                })
        )
    }

    /** Keep the cache in sync when other places (e.g. increment_scan) get fresh counts. */
    fun update(data: ScanUsageData) {
        cached = data
    }
}
