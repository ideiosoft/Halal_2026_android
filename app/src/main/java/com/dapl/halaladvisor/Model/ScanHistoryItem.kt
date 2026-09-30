package com.dapl.halaladvisor.Model

data class ScanHistoryItem(
    val id: String,
    val barcode: String,
    val productName: String,
    val brandName: String,
    val ingredientsSummary: String,
    val status: String, // HALAL, HARAM, MUSHBOOH
    val dateLabel: String, // e.g. "Today", "Yesterday", "25 Jun"
    val timeLabel: String  // e.g. "10:42 AM"
)
