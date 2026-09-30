package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class EdgeOwnerToTimelineMedia(
    @SerializedName("count")
    var count: Int,
    @SerializedName("edges")
    var edges: ArrayList<EdgeXX>,
    @SerializedName("page_info")
    var pageInfo: PageInfoXX
)