package com.dapl.halaladvisor.Model.cuisine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CuisineModelNew {
    @SerializedName("Connection")
    @Expose
    public String connection;
    @SerializedName("Status")
    @Expose
    public String status;
    @SerializedName("Message")
    @Expose
    public String message;
    @SerializedName("Data")
    @Expose
    public List<Datum> data = null;

    public class Datum {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("cuisines_name")
        @Expose
        public String cuisinesName;
        @SerializedName("cuisine_image")
        @Expose
        public String cuisineImage;

    }
}
