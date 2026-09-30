package com.dapl.halaladvisor.Model.fetchreview


import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("id")
    var id: String,
    @SerializedName("review_images")
    var reviewImages: String
)