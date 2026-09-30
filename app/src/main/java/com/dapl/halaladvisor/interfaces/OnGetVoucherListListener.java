package com.dapl.halaladvisor.interfaces;

import org.json.JSONObject;


public interface OnGetVoucherListListener {

    void onGetVoucherListListenerSuccess(JSONObject obj);

    void onGetVoucherListListenerFailed();
}
