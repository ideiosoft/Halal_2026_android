package com.dapl.halaladvisor.Activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dapl.halaladvisor.Adapter.ScanHistoryAdapter
import com.dapl.halaladvisor.Model.ScanHistoryItem
import com.dapl.halaladvisor.R
import com.dapl.halaladvisor.productscan.HistoryRepository

class ActivityScanHistory : AppCompatActivity() {

    private lateinit var rvScanHistory: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var contentScroll: View
    private lateinit var edtSearch: EditText
    private lateinit var txtNoProduct: TextView

    private lateinit var adapter: ScanHistoryAdapter
    private var allItems = listOf<ScanHistoryItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_history)

        adapter = ScanHistoryAdapter(emptyList())

        val btnBack = findViewById<ImageView>(R.id.btnBack)
        rvScanHistory = findViewById(R.id.rvScanHistory)
        progressBar = findViewById(R.id.progressBar)
        contentScroll = findViewById(R.id.contentScroll)
        edtSearch = findViewById(R.id.edtSearch)
        txtNoProduct = findViewById(R.id.txtNoProduct)
        val tabLayout = findViewById<com.google.android.material.tabs.TabLayout>(R.id.tabLayout)

        tabLayout.getTabAt(1)?.select()

        btnBack.setOnClickListener {
            finish()
        }

        rvScanHistory.layoutManager = LinearLayoutManager(this)
        rvScanHistory.adapter = adapter

        setupSearch()

        loadHistory()
        loadSummary()
    }

    /** Filters the loaded history locally as the user types. */
    private fun setupSearch() {
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                applyFilter()
            }
        })
    }

    /** Applies the current search text to [allItems] and shows the empty state when nothing matches. */
    private fun applyFilter() {
        val query = edtSearch.text?.toString()?.trim().orEmpty()
        val filtered = if (query.isEmpty()) {
            allItems
        } else {
            allItems.filter {
                it.productName.contains(query, ignoreCase = true) ||
                    it.brandName.contains(query, ignoreCase = true)
            }
        }
        adapter.updateItems(filtered)
        txtNoProduct.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        rvScanHistory.visibility = if (filtered.isEmpty()) View.GONE else View.VISIBLE
    }

    private fun loadHistory() {
        // Show the loader until the list is ready, then reveal the content.
        progressBar.visibility = View.VISIBLE
        contentScroll.visibility = View.GONE
        // Complete list (request a large page so the full history is shown)
        HistoryRepository.fetchList(this, page = 1, limit = 100) { items ->
            if (isFinishing || isDestroyed) return@fetchList
            allItems = items
            // Re-apply any text already typed in the search box.
            applyFilter()
            progressBar.visibility = View.GONE
            contentScroll.visibility = View.VISIBLE
        }
    }

    private fun loadSummary() {
        HistoryRepository.fetchSummary(this) { summary ->
            if (isFinishing || isDestroyed) return@fetchSummary
            findViewById<TextView>(R.id.txtTotalCount).text = summary.totalScans.toString()
            findViewById<TextView>(R.id.txtHalalCount).text = summary.halalCount.toString()
            findViewById<TextView>(R.id.txtHaramCount).text = summary.haramCount.toString()
            findViewById<TextView>(R.id.txtMushboohCount).text = summary.mushboohCount.toString()
        }
    }
}
