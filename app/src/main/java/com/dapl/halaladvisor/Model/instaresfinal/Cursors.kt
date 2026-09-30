package com.dapl.halaladvisor.Model.instaresfinal


import com.google.gson.annotations.SerializedName

data class Cursors(
    @SerializedName("after")
    var after: String,
    @SerializedName("before")
    var before: String
)