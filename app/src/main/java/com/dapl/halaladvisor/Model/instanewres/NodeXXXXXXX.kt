package com.dapl.halaladvisor.Model.instanewres


import com.google.gson.annotations.SerializedName

data class NodeXXXXXXX(
    @SerializedName("full_name")
    var fullName: String,
    @SerializedName("id")
    var id: String,
    @SerializedName("is_private")
    var isPrivate: Boolean,
    @SerializedName("is_verified")
    var isVerified: Boolean,
    @SerializedName("profile_pic_url")
    var profilePicUrl: String,
    @SerializedName("username")
    var username: String
)