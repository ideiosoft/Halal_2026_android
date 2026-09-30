package com.dapl.halaladvisor.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NearByResturent {

    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("status_resturent")
    @Expose
    private String status_resturent;
    @SerializedName("res_fav")
    @Expose
    private Integer res_fav;
    @SerializedName("r_id")
    @Expose
    private String rId;
    @SerializedName("cat_id")
    @Expose
    private String catId;
    @SerializedName("latitude")
    @Expose
    private String latitude;
    @SerializedName("longitude")
    @Expose
    private String longitude;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("order_now_link")
    @Expose
    private String orderNowLink;
    @SerializedName("r_url")
    @Expose
    private String rUrl;
    @SerializedName("cusine_id")
    @Expose
    private String cusineId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("avg_cost")
    @Expose
    private String avgCost;
    @SerializedName("pre_order")
    @Expose
    private String preOrder;
    @SerializedName("pick_up")
    @Expose
    private String pickUp;
    @SerializedName("delivery_tag")
    @Expose
    private String deliveryTag;
    @SerializedName("ostatus")
    @Expose
    private String ostatus;
    @SerializedName("r_rating")
    @Expose
    private String rRating;
    @SerializedName("is_featured")
    @Expose
    private Object isFeatured;
    @SerializedName("type")
    @Expose
    private Object type;
    @SerializedName("offer_price")
    @Expose
    private Object offerPrice;
    @SerializedName("r_contact")
    @Expose
    private String rContact;
    @SerializedName("distance")
    @Expose
    private String distance;

    public String getStatus_resturent() {
        return status_resturent;
    }

    public void setStatus_resturent(String status_resturent) {
        this.status_resturent = status_resturent;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
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

    public String getOrderNowLink() {
        return orderNowLink;
    }

    public void setOrderNowLink(String orderNowLink) {
        this.orderNowLink = orderNowLink;
    }

    public String getrUrl() {
        return rUrl;
    }

    public void setrUrl(String rUrl) {
        this.rUrl = rUrl;
    }

    public Integer getRes_fav() {
        return res_fav;
    }

    public void setRes_fav(Integer res_fav) {
        this.res_fav = res_fav;
    }

    public String getCusineId() {
        return cusineId;
    }

    public void setCusineId(String cusineId) {
        this.cusineId = cusineId;
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

    public String getAvgCost() {
        return avgCost;
    }

    public void setAvgCost(String avgCost) {
        this.avgCost = avgCost;
    }

    public String getPreOrder() {
        return preOrder;
    }

    public void setPreOrder(String preOrder) {
        this.preOrder = preOrder;
    }

    public String getPickUp() {
        return pickUp;
    }

    public void setPickUp(String pickUp) {
        this.pickUp = pickUp;
    }

    public String getDeliveryTag() {
        return deliveryTag;
    }

    public void setDeliveryTag(String deliveryTag) {
        this.deliveryTag = deliveryTag;
    }

    public String getOstatus() {
        return ostatus;
    }

    public void setOstatus(String ostatus) {
        this.ostatus = ostatus;
    }

    public String getrRating() {
        return rRating;
    }

    public void setrRating(String rRating) {
        this.rRating = rRating;
    }

    public Object getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Object isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    public Object getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(Object offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getrContact() {
        return rContact;
    }

    public void setrContact(String rContact) {
        this.rContact = rContact;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}