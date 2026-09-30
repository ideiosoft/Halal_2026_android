
package com.dapl.halaladvisor.Model.getofferres;

import java.util.List;
 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class GetOfferListingRes {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
