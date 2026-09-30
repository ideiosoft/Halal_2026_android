package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class ThumbnailResource(
    @SerializedName("config_height")
    var configHeight: Int,
    @SerializedName("config_width")
    var configWidth: Int,
    @SerializedName("src")
    var src: String
)