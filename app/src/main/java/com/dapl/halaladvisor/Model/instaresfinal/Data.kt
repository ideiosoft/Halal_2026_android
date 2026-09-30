package com.dapl.halaladvisor.Model.instaresfinal


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("caption")
    var caption: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("media_type")
    var mediaType: String,
    @SerializedName("media_url")
    var mediaUrl: String,
    @SerializedName("permalink")
    var permalink: String,
    @SerializedName("timestamp")
    var timestamp: String,
    @SerializedName("username")
    var username: String
)