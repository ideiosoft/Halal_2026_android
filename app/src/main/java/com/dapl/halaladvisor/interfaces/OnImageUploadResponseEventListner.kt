package com.dapl.halaladvisor.interfaces
import org.json.JSONObject

interface OnImageUploadResponseEventListner {

    //method which have parameters
    fun onImageUploadedSucess(jsonObject: JSONObject)

    fun onImageUploadError(errorString: String)


}