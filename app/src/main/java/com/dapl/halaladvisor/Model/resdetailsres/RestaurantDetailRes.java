
package com.dapl.halaladvisor.Model.resdetailsres;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RestaurantDetailRes {

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("ostatus")
    @Expose
    private String ostatus;
    @SerializedName("avg_cost")
    @Expose
    private String avgCost;
    @SerializedName("contact")
    @Expose
    private String contact;
    @SerializedName("r_url")
    @Expose
    private String rUrl;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("order_now_link")
    @Expose
    private String orderNowLink;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("cusine_id")
    @Expose
    private String cusineId;
    @SerializedName("cash_and_card")
    @Expose
    private String cashAndCard;
    @SerializedName("delivary_type")
    @Expose
    private String delivaryType;
    @SerializedName("states_id")
    @Expose
    private String statesId;
    @SerializedName("city_id")
    @Expose
    private String cityId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("more_info_id")
    @Expose
    private String moreInfoId;
    @SerializedName("more_info")
    @Expose
    private List<MoreInfo> moreInfo = null;
    @SerializedName("cats_id")
    @Expose
    private String catsId;
    @SerializedName("catagory_name")
    @Expose
    private String catagoryName;
    @SerializedName("citys_id")
    @Expose
    private String citysId;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("s_id")
    @Expose
    private String sId;
    @SerializedName("state_name")
    @Expose
    private String stateName;
    @SerializedName("establishment_type")
    @Expose
    private String establishmentType;
    @SerializedName("avaragecost_type")
    @Expose
    private String avaragecostType;
    @SerializedName("avaragecost_range")
    @Expose
    private String avaragecostRange;
    @SerializedName("estb_type")
    @Expose
    private String estbType;
    @SerializedName("rest_avg_rating")
    @Expose
    private String restAvgRating;
    @SerializedName("total_rating")
    @Expose
    private String totalRating;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("reviews")
    @Expose
    private List<Review> reviews = null;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("open_hours")
    @Expose
    private List<OpenHour> openHours = null;
    @SerializedName("cuisines_name")
    @Expose
    private List<CuisinesName> cuisinesName = null;
    @SerializedName("status_resturent")
    @Expose
    private String statusResturent;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getOstatus() {
        return ostatus;
    }

    public void setOstatus(String ostatus) {
        this.ostatus = ostatus;
    }

    public String getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(String avgCost) {
        this.avgCost = avgCost;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getrUrl() {
        return rUrl;
    }

    public void setrUrl(String rUrl) {
        this.rUrl = rUrl;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOrderNowLink() {
        return orderNowLink;
    }

    public void setOrderNowLink(String orderNowLink) {
        this.orderNowLink = orderNowLink;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCusineId() {
        return cusineId;
    }

    public void setCusineId(String cusineId) {
        this.cusineId = cusineId;
    }

    public String getCashAndCard() {
        return cashAndCard;
    }

    public void setCashAndCard(String cashAndCard) {
        this.cashAndCard = cashAndCard;
    }

    public String getDelivaryType() {
        return delivaryType;
    }

    public void setDelivaryType(String delivaryType) {
        this.delivaryType = delivaryType;
    }

    public String getStatesId() {
        return statesId;
    }

    public void setStatesId(String statesId) {
        this.statesId = statesId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoreInfoId() {
        return moreInfoId;
    }

    public void setMoreInfoId(String moreInfoId) {
        this.moreInfoId = moreInfoId;
    }

    public List<MoreInfo> getMoreInfo() {
        return moreInfo;
    }

    public void setMoreInfo(List<MoreInfo> moreInfo) {
        this.moreInfo = moreInfo;
    }

    public String getCatsId() {
        return catsId;
    }

    public void setCatsId(String catsId) {
        this.catsId = catsId;
    }

    public String getCatagoryName() {
        return catagoryName;
    }

    public void setCatagoryName(String catagoryName) {
        this.catagoryName = catagoryName;
    }

    public String getCitysId() {
        return citysId;
    }

    public void setCitysId(String citysId) {
        this.citysId = citysId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getEstablishmentType() {
        return establishmentType;
    }

    public void setEstablishmentType(String establishmentType) {
        this.establishmentType = establishmentType;
    }

    public String getAvaragecostType() {
        return avaragecostType;
    }

    public void setAvaragecostType(String avaragecostType) {
        this.avaragecostType = avaragecostType;
    }

    public String getAvaragecostRange() {
        return avaragecostRange;
    }

    public void setAvaragecostRange(String avaragecostRange) {
        this.avaragecostRange = avaragecostRange;
    }

    public String getEstbType() {
        return estbType;
    }

    public void setEstbType(String estbType) {
        this.estbType = estbType;
    }

    public String getRestAvgRating() {
        return restAvgRating;
    }

    public void setRestAvgRating(String restAvgRating) {
        this.restAvgRating = restAvgRating;
    }

    public String getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<OpenHour> getOpenHours() {
        return openHours;
    }

    public void setOpenHours(List<OpenHour> openHours) {
        this.openHours = openHours;
    }

    public List<CuisinesName> getCuisinesName() {
        return cuisinesName;
    }

    public void setCuisinesName(List<CuisinesName> cuisinesName) {
        this.cuisinesName = cuisinesName;
    }

    public String getStatusResturent() {
        return statusResturent;
    }

    public void setStatusResturent(String statusResturent) {
        this.statusResturent = statusResturent;
    }

}
