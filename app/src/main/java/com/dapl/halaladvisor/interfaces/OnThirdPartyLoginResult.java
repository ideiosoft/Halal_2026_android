package com.dapl.halaladvisor.interfaces;

import org.json.JSONObject;

/**
 * TODO: Created by Tanay Mondal on 20-09-2016
 */
public interface OnThirdPartyLoginResult {
    void onThirdPartyLoginSuccess(JSONObject object);

    void onThirdPartyLoginError(String string);
}
