package com.dapl.halaladvisor.Model.fetchreview


import com.google.gson.annotations.SerializedName

data class FetchReviewRes(
    @SerializedName("message")
    var message: String,
    @SerializedName("result")
    var result: Result,
    @SerializedName("status")
    var status: Boolean
)