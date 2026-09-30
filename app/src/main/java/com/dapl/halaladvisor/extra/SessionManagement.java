package com.dapl.halaladvisor.extra;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import com.dapl.halaladvisor.Activity.LogInActivity;
import com.dapl.halaladvisor.Activity.RegistrationActivity;

import java.util.HashMap;


/**
 * Created by DAT-Asset-131 on 21-06-2016.
 */

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Shared Preferences
    SharedPreferences pref_token;

    // Editor for Device Token
    Editor editor_token;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    public static final String PREF_NAME = "QuickRestaurent";

    // Sharedpref file name
    public static final String PREF_NAME_TOKEN = "QuickRestaurentToken";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    /*// User name (make variable public to access from outside)
    public static final String KEY_LASTNAME = "lastName";*/

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // User Id (make variable public to access from outside)
    public static final String KEY_UID = "id";

    // Mobile No (make variable public to access from outside)
    public static final String KEY_USER_IMAGE = "image";

    // Device TOKEN (make variable public to access from outside)
    public static final String KEY_DEVICE_TOKEN = "device_token";

    // New Device Token (make variable public to access from outside)
    public static final String KEY_NEW_DEVICE_TOKEN = "new_device_token";

    // User Mobile (make variable public to access from outside)
    public static final String KEY_MOBILE = "mobile";

    // User Location (make variable public to access from outside)
    public static final String KEY_LOCATION = "location";

    // User City (make variable public to access from outside)
    /*public static final String KEY_CITY = "city";

    // User Province (make variable public to access from outside)
    public static final String KEY_PROVINCE = "province";*/


    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        this._context = context;
        pref_token = _context.getSharedPreferences(PREF_NAME_TOKEN, PRIVATE_MODE);
        editor_token = pref_token.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String email, String fname, String userId, String image, String mobile, String location){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing id in pref
        editor.putString(KEY_UID, userId);

        // Storing name in pref
        editor.putString(KEY_NAME, fname);

        // Storing name in pref
        editor.putString(KEY_USER_IMAGE, image);

        // Storing email in pref
        editor.putString(KEY_EMAIL, email);


        // Storing mobile in pref
        editor.putString(KEY_MOBILE, mobile);

        // Storing location in pref
        editor.putString(KEY_LOCATION, location);

        // Storing address 2 in pref
        /*editor.putString(KEY_ADDRESS_TWO, address2);

        // Storing address 2 in pref
        editor.putString(KEY_CATEGORY, category);

        // Storing address 2 in pref
        editor.putString(KEY_CITY, city);

        // Storing address 2 in pref
        editor.putString(KEY_PROVINCE, province);*/

        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LogInActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }


    public void setInfoStatus(boolean infoStatus){
        editor.putBoolean("infoStatus",infoStatus);
        editor.commit();
    }

    public boolean getInfStatus() {
        return pref.getBoolean("infoStatus",false);
    }





    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        // user id
        user.put(KEY_UID, pref.getString(KEY_UID, null));

        // user name
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user name
        user.put(KEY_USER_IMAGE, pref.getString(KEY_USER_IMAGE, null));

        // user email id
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user device token
        user.put(KEY_DEVICE_TOKEN, pref_token.getString(KEY_DEVICE_TOKEN, null));

        // user mobile
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));

        // location
        user.put(KEY_LOCATION, pref.getString(KEY_LOCATION, null));

        // category
        /*user.put(KEY_CATEGORY, pref.getString(KEY_CATEGORY, null));

        // city
        user.put(KEY_CITY, pref.getString(KEY_CITY, null));

        // province
        user.put(KEY_PROVINCE, pref.getString(KEY_PROVINCE, null));*/

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, RegistrationActivity.class);

        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }


    /*
    * Sets Device Token (First time)
    * */
    public void setNewDeviceToken(String device_token){
        // Storing email in pref
        editor_token.putString(KEY_DEVICE_TOKEN, device_token);
        editor_token.commit();
    }

    /*
    * Updates Device Token (First time) if the token gets changed
    * */
    public void updateDeviceToken(String token){

        // Storing name in pref
        editor_token.putString(KEY_NEW_DEVICE_TOKEN, token);

        // Storing name in pref
        editor_token.putString(KEY_DEVICE_TOKEN, token);

        // commit changes
        editor_token.commit();
    }

    /*
    * For Getting Device Token
    * */
    public String getDeviceToken(){
        return pref_token.getString(KEY_DEVICE_TOKEN, null);
    }
}
