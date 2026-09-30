package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class EdgeMutualFollowedBy(
    @SerializedName("count")
    var count: Int,
    @SerializedName("edges")
    var edges: List<Any>
)