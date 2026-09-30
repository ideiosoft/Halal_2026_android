package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class DashInfoXX(
    @SerializedName("is_dash_eligible")
    var isDashEligible: Boolean,
    @SerializedName("number_of_qualities")
    var numberOfQualities: Int,
    @SerializedName("video_dash_manifest")
    var videoDashManifest: Any
)