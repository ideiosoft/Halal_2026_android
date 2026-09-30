package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class SharingFrictionInfo(
    @SerializedName("bloks_app_url")
    var bloksAppUrl: Any,
    @SerializedName("should_have_sharing_friction")
    var shouldHaveSharingFriction: Boolean
)