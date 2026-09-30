package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("id")
    var id: String,
    @SerializedName("username")
    var username: String
)