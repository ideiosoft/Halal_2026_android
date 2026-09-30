package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class NodeXXXX(
    @SerializedName("user")
    var user: UserX,
    @SerializedName("x")
    var x: Int,
    @SerializedName("y")
    var y: Int
)