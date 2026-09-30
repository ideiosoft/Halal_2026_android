package com.dapl.halaladvisor.Model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by DAT-Asset-128 on 22-07-2016.
 */
public class RestaurantBannerListModel {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("res_url")
    @Expose
    public String resUrl;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("r_status")
    @Expose
    public String rStatus;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
}
