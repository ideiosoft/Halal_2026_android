package com.dapl.halaladvisor.Model.fetchreview


import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("date_of_post")
    var dateOfPost: String,
    @SerializedName("images")
    var images: List<Image>,
    @SerializedName("rating")
    var rating: String,
    @SerializedName("rating_id")
    var ratingId: String,
    @SerializedName("res_id")
    var resId: String,
    @SerializedName("review")
    var review: String,
    @SerializedName("review_image_id")
    var reviewImageId: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("u_id")
    var uId: String
)