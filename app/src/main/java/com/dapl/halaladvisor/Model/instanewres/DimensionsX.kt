package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class DimensionsX(
    @SerializedName("height")
    var height: Int,
    @SerializedName("width")
    var width: Int
)