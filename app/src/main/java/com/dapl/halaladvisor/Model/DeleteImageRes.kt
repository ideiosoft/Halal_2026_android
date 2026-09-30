package com.dapl.halaladvisor.Model


import com.google.gson.annotations.SerializedName

data class DeleteImageRes(
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: Boolean
)