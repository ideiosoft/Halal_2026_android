package com.dapl.halaladvisor.interfaces;

/**
 * Created by DAT-Asset-128 on 11-07-2016.
 */
public interface MultipartWithDataUploadCallback {

    public void onSuccess(String response);

    public void onFailure(String response);
}
