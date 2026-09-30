package com.dapl.halaladvisor.Model.getgalleryimagesres


import com.google.gson.annotations.SerializedName

data class GetGalleryImagesRes(
    @SerializedName("message")
    var message: String,
    @SerializedName("result")
    var result: List<Result>,
    @SerializedName("status")
    var status: Boolean
)