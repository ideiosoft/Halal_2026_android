package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class Edge(
    @SerializedName("node")
    var node: Node
)