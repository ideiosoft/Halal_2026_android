package com.dapl.halaladvisor.Model

data class AddProductRequest(
    val barcode: String,
    val product_name: String,
    val brand: String,
    val category: String,
    val country: String,
    val image_url: String,
    val ingredients: String
)