
package com.dapl.halaladvisor.Model.getofferres;

 import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class Datum {

    @SerializedName("offer_id")
    @Expose
    private String offerId;

     public String getR_name() {
         return r_name;
     }

     public void setR_name(String r_name) {
         this.r_name = r_name;
     }

     @SerializedName("r_name")
    @Expose
    private String r_name;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("resturant_id")
    @Expose
    private String resturantId;
    @SerializedName("tagline")
    @Expose
    private String tagline;
    @SerializedName("image_path")
    @Expose
    private String imagePath;
    @SerializedName("start_date")
    @Expose
    private String startDate;
    @SerializedName("end_date")
    @Expose
    private String endDate;
    @SerializedName("offer_price")
    @Expose
    private String offerPrice;
    @SerializedName("offer_desc")
    @Expose
    private String offerDesc;
    @SerializedName("fine_print")
    @Expose
    private String finePrint;
    @SerializedName("limited_time")
    @Expose
    private String limitedTime;
    @SerializedName("more_info")
    @Expose
    private String moreInfo;
    @SerializedName("offer_deal")
    @Expose
    private String offerDeal;
    @SerializedName("how_to_redeem")
    @Expose
    private String howToRedeem;
    @SerializedName("is_featured")
    @Expose
    private String isFeatured;
    @SerializedName("type")
    @Expose
    private Object type;

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getResturantId() {
        return resturantId;
    }

    public void setResturantId(String resturantId) {
        this.resturantId = resturantId;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
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

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getOfferDesc() {
        return offerDesc;
    }

    public void setOfferDesc(String offerDesc) {
        this.offerDesc = offerDesc;
    }

    public String getFinePrint() {
        return finePrint;
    }

    public void setFinePrint(String finePrint) {
        this.finePrint = finePrint;
    }

    public String getLimitedTime() {
        return limitedTime;
    }

    public void setLimitedTime(String limitedTime) {
        this.limitedTime = limitedTime;
    }

    public String getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(String moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getOfferDeal() {
        return offerDeal;
    }

    public void setOfferDeal(String offerDeal) {
        this.offerDeal = offerDeal;
    }

    public String getHowToRedeem() {
        return howToRedeem;
    }

    public void setHowToRedeem(String howToRedeem) {
        this.howToRedeem = howToRedeem;
    }

    public String getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(String isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

}
