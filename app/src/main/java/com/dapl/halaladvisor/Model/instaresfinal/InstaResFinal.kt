package com.dapl.halaladvisor.Model.instaresfinal


import com.google.gson.annotations.SerializedName

data class InstaResFinal(
    @SerializedName("data")
    var `data`: List<Data>,
    @SerializedName("paging")
    var paging: Paging
)