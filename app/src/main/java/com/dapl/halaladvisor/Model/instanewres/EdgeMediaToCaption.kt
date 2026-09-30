package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class EdgeMediaToCaption(
    @SerializedName("edges")
    var edges: List<EdgeX>
)