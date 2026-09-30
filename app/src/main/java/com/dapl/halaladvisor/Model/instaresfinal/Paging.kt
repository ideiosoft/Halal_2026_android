package com.dapl.halaladvisor.Model.instaresfinal


import com.google.gson.annotations.SerializedName

data class Paging(
    @SerializedName("cursors")
    var cursors: Cursors,
    @SerializedName("next")
    var next: String
)