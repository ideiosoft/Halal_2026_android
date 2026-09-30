package com.dapl.halaladvisor.interfaces;

import org.json.JSONObject;

/**
 * Created by DAT-Asset-131 on 21-06-2016.
 */
public interface OnSearchResult {

    void onSearchSuccess(JSONObject obj);
    void onSearchError(String error);

}
