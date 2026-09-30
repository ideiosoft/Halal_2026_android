package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class EdgeMediaCollections(
    @SerializedName("count")
    var count: Int,
    @SerializedName("edges")
    var edges: List<Any>,
    @SerializedName("page_info")
    var pageInfo: PageInfoX
)