
package com.dapl.halaladvisor.Model.offerredeemres;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class OtherDetails {

    @SerializedName("offer_price")
    @Expose
    private String offerPrice;
    @SerializedName("r_name")
    @Expose
    private String rName;

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }

}
