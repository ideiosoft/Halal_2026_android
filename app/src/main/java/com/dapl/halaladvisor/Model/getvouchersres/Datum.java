
package com.dapl.halaladvisor.Model.getvouchersres;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class Datum {

    @SerializedName("offer_id")
    @Expose
    private String offerId;
    @SerializedName("offer_type")
    @Expose
    private String offerType;
    @SerializedName("rest_id")
    @Expose
    private String restId;
    @SerializedName("rest_name")
    @Expose
    private String restName;
    @SerializedName("tagline")
    @Expose
    private String tagline;
    @SerializedName("offer_description")
    @Expose
    private String offerDescription;
    @SerializedName("rest_lat")
    @Expose
    private String restLat;
    @SerializedName("rest_long")
    @Expose
    private String restLong;
    @SerializedName("offer_price")
    @Expose
    private String offerPrice;
    @SerializedName("offer_img")
    @Expose
    private String offerImg;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("current_date")
    @Expose
    private String currentDate;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public String getRestId() {
        return restId;
    }

    public void setRestId(String restId) {
        this.restId = restId;
    }

    public String getRestName() {
        return restName;
    }

    public void setRestName(String restName) {
        this.restName = restName;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getOfferDescription() {
        return offerDescription;
    }

    public void setOfferDescription(String offerDescription) {
        this.offerDescription = offerDescription;
    }

    public String getRestLat() {
        return restLat;
    }

    public void setRestLat(String restLat) {
        this.restLat = restLat;
    }

    public String getRestLong() {
        return restLong;
    }

    public void setRestLong(String restLong) {
        this.restLong = restLong;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getOfferImg() {
        return offerImg;
    }

    public void setOfferImg(String offerImg) {
        this.offerImg = offerImg;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

}
