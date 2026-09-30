package com.dapl.halaladvisor.Model.getgalleryimagesres


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("g_status")
    var gStatus: String,
    @SerializedName("gal_id")
    var galId: String,
    @SerializedName("gal_image_name")
    var galImageName: String,
    @SerializedName("r_id")
    var rId: String
)