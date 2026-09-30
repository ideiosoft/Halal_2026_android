package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class PageInfoXXX(
    @SerializedName("end_cursor")
    var endCursor: Any,
    @SerializedName("has_next_page")
    var hasNextPage: Boolean
)