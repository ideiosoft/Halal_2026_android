package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class PageInfoXX(
    @SerializedName("end_cursor")
    var endCursor: String,
    @SerializedName("has_next_page")
    var hasNextPage: Boolean
)