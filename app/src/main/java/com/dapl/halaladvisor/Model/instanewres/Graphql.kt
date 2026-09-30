package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class Graphql(
    @SerializedName("user")
    var user: User
)