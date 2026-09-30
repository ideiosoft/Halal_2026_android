package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("has_public_page")
    var hasPublicPage: Boolean,
    @SerializedName("id")
    var id: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("slug")
    var slug: String
)