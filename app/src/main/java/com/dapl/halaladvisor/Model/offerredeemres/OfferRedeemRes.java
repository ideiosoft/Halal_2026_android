
package com.dapl.halaladvisor.Model.offerredeemres;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class OfferRedeemRes {

    @SerializedName("status")
    @Expose
    private boolean status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("other_details")
    @Expose
    private OtherDetails otherDetails;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OtherDetails getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(OtherDetails otherDetails) {
        this.otherDetails = otherDetails;
    }

}
