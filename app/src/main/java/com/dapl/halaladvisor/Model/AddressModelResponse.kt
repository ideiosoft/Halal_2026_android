package com.dapl.halaladvisor.Model

data class AddressModelResponse(
    val plus_code: PlusCode,
    val results: List<ResultX>,
    val status: String
)