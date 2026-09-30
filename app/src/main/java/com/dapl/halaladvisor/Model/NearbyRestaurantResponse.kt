package com.dapl.halaladvisor.Model

data class NearbyRestaurantResponse(
    val message: String,
    val result: List<Result>,
    val status: String,
    val total_records: Int
)