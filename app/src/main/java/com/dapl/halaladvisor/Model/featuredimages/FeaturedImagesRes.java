
package com.dapl.halaladvisor.Model.featuredimages;

import java.util.List;
 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class FeaturedImagesRes {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;
    @SerializedName("baseUrl")
    @Expose
    private String baseUrl;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

}
