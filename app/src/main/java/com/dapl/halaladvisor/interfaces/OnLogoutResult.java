package com.dapl.halaladvisor.interfaces;

import org.json.JSONObject;

/**
 * Created by DAT-Asset-131 on 21-06-2016.
 */
public interface OnLogoutResult {

    void onLogoutSuccess(JSONObject obj);
    void onLogoutError(String error);

}
