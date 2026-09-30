package com.dapl.halaladvisor.Model

import com.dapl.halaladvisor.Model.newww.CuisinesName

data class Result(
    val address: String,
    val avg_cost: String,
    val cat_id: String,
    val cost_sign: String,
    val cuisines_name: List<CuisinesName>,
    val cusine_id: String,
    val delivery_tag: String,
    val distance: String,
    val id: String,
    val image: String,
    val is_featured: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val offer_price: Any,
    val order_now_link: String,
    val ostatus: String,
    val status_resturent: String,
    val pick_up: String,
    val pre_order: String,
    val r_contact: String,
    val r_id: String,
    val r_rating: String,
    val r_url: String,
    var res_fav: Int,
    val type: Any
)