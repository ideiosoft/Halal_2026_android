package com.dapl.halaladvisor.productscan

import android.content.Context
import android.util.Log
import com.dapl.halaladvisor.Model.ScanHistoryItem
import com.dapl.halaladvisor.Utility.PrefUtils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Locale

/** Fetches the user's scan history (list + summary) from the admin API. */
object HistoryRepository {

    private val disposables = CompositeDisposable()

    private fun userId(context: Context): String? =
        PrefUtils.getProfileObject(context)?.u_id?.takeIf { it.isNotBlank() }

    /** Fetches a page of history items mapped to [ScanHistoryItem]. Failures deliver an empty list. */
    fun fetchList(context: Context, page: Int = 1, limit: Int = 20, onResult: (List<ScanHistoryItem>) -> Unit) {
        val userId = userId(context) ?: run { onResult(emptyList()); return }
        disposables.add(
            ScanUsageApiClient.api.historyList(userId, page, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val items = response.data?.items.orEmpty().map { it.toScanHistoryItem() }
                    onResult(items)
                }, { error ->
                    Log.e("HISTORY_API", "history_list failed: ${error.message}")
                    onResult(emptyList())
                })
        )
    }

    /** Fetches the aggregate counts. Failures are logged only. */
    fun fetchSummary(context: Context, onResult: (HistorySummaryData) -> Unit) {
        val userId = userId(context) ?: return
        disposables.add(
            ScanUsageApiClient.api.historySummary(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    if (response.success) response.data?.let(onResult)
                }, { error ->
                    Log.e("HISTORY_API", "history_summary failed: ${error.message}")
                })
        )
    }
}

private fun normalizeStatus(raw: String?): String {
    val s = raw?.trim()?.uppercase().orEmpty()
    return when {
        s.contains("NON-HALAL") || s.contains("NON HALAL") || s.contains("NOT HALAL") ||
            s.contains("HARAM") || s.contains("PORK") || s.contains("ALCOHOL") -> "HARAM"
        s.contains("HALAL") -> "HALAL"
        else -> "MUSHBOOH"
    }
}

private fun ingredientsSummaryFor(status: String): String = when (status) {
    "HALAL" -> "No non-halal ingredients"
    "HARAM" -> "Haram ingredients"
    else -> "Potential haram ingredients"
}

/** Parsed date+time split into a relative day label ("Today"/"Yesterday"/"25 Jun") and a time. */
private fun formatScannedAt(raw: String?): Pair<String, String> {
    if (raw.isNullOrBlank()) return "" to ""
    return try {
        val parser = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
        val date = parser.parse(raw) ?: return raw to ""
        val timeLabel = SimpleDateFormat("h:mm a", Locale.US).format(date)

        val day = java.util.Calendar.getInstance().apply { time = date }
        val now = java.util.Calendar.getInstance()
        fun sameDay(a: java.util.Calendar, b: java.util.Calendar) =
            a.get(java.util.Calendar.YEAR) == b.get(java.util.Calendar.YEAR) &&
                a.get(java.util.Calendar.DAY_OF_YEAR) == b.get(java.util.Calendar.DAY_OF_YEAR)
        val yesterday = (now.clone() as java.util.Calendar).apply { add(java.util.Calendar.DAY_OF_YEAR, -1) }

        val dateLabel = when {
            sameDay(day, now) -> "Today"
            sameDay(day, yesterday) -> "Yesterday"
            else -> SimpleDateFormat("d MMM", Locale.US).format(date)
        }
        dateLabel to timeLabel
    } catch (e: Exception) {
        raw to ""
    }
}

fun HistoryItem.toScanHistoryItem(): ScanHistoryItem {
    val status = normalizeStatus(halalStatus)
    val (dateLabel, timeLabel) = formatScannedAt(scannedAt)
    return ScanHistoryItem(
        id = id ?: "",
        barcode = barcode ?: "",
        productName = productName ?: "Unknown",
        brandName = brand ?: "",
        ingredientsSummary = ingredientsSummaryFor(status),
        status = status,
        dateLabel = dateLabel,
        timeLabel = timeLabel
    )
}
