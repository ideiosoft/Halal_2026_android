package com.dapl.halaladvisor.interfaces;

import org.json.JSONObject;

/**
 * TODO: Created by Tanay Mondal on 26-09-2016
 */
public interface OnProfileEditResult {
    void onProfileEditSuccess(JSONObject object);

    void onProfileEditFailed();
}
