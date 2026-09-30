package com.dapl.halaladvisor.Model;

public class AddressObject {

    private boolean history;
    private String userKeywordAddressData;
    private String fullAddress;
    private String searchKey;
    private String elevation;
    private String median_income;
    private String latitude;
    private String postcode;
    private String type;
    private String time_zone;
    private String urban_area;
    private String population;
    private String area_sq_km;
    private String name;
    private String local_government_area;
    private String id;
    private String state;
    private String state_code;
    private String region;
    private String longitude;

    public boolean isHistory() {
        return history;
    }

    public void setHistory(boolean history) {
        this.history = history;
    }

    public String getUserKeywordAddressData() {
        return userKeywordAddressData;
    }

    public void setUserKeywordAddressData(String userKeywordAddressData) {
        this.userKeywordAddressData = userKeywordAddressData;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getSearchKey() {
        return searchKey;
    }

    public void setSearchKey(String searchKey) {
        this.searchKey = searchKey;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public String getMedian_income() {
        return median_income;
    }

    public void setMedian_income(String median_income) {
        this.median_income = median_income;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime_zone() {
        return time_zone;
    }

    public void setTime_zone(String time_zone) {
        this.time_zone = time_zone;
    }

    public String getUrban_area() {
        return urban_area;
    }

    public void setUrban_area(String urban_area) {
        this.urban_area = urban_area;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getArea_sq_km() {
        return area_sq_km;
    }

    public void setArea_sq_km(String area_sq_km) {
        this.area_sq_km = area_sq_km;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocal_government_area() {
        return local_government_area;
    }

    public void setLocal_government_area(String local_government_area) {
        this.local_government_area = local_government_area;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState_code() {
        return state_code;
    }

    public void setState_code(String state_code) {
        this.state_code = state_code;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "ClassPojo [elevation = " + elevation + ", median_income = " + median_income + ", latitude = " + latitude + ", postcode = " + postcode + ", type = " + type + ", time_zone = " + time_zone + ", urban_area = " + urban_area + ", population = " + population + ", area_sq_km = " + area_sq_km + ", name = " + name + ", local_government_area = " + local_government_area + ", id = " + id + ", state = " + state + ", state_code = " + state_code + ", region = " + region + ", longitude = " + longitude + "]";
    }
}
