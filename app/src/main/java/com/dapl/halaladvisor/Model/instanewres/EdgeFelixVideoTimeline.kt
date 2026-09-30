package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class EdgeFelixVideoTimeline(
    @SerializedName("count")
    var count: Int,
    @SerializedName("edges")
    var edges: List<Edge>,
    @SerializedName("page_info")
    var pageInfo: PageInfo
)