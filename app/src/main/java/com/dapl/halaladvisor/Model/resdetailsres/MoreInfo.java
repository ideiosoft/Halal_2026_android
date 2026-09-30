
package com.dapl.halaladvisor.Model.resdetailsres;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class MoreInfo {

    @SerializedName("more_info")
    @Expose
    private String moreInfo;

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

}
