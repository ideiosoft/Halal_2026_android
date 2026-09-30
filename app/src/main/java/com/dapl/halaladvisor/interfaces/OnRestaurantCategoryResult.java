package com.dapl.halaladvisor.interfaces;

import org.json.JSONObject;

/**
 * Created by DAT-Asset-128 on 07-09-2016.
 */
public interface OnRestaurantCategoryResult {

    void OnRestaurantCategorySuccess(JSONObject obj);
    void OnRestaurantCategoryError(String error);
}
