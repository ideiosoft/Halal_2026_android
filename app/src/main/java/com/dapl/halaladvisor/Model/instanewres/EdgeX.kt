package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class EdgeX(
    @SerializedName("node")
    var node: NodeX
)