
package com.dapl.halaladvisor.Model.fetchallreviews;

import java.util.List;
 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class FetchAllReviewsRes {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("baseUrl")
    @Expose
    private String baseUrl;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
