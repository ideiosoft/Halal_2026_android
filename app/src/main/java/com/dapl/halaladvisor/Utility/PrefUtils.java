package com.dapl.halaladvisor.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dapl.halaladvisor.Model.Credential;
import com.dapl.halaladvisor.Model.LastSearchLocationObject;
import com.dapl.halaladvisor.Model.ProfileDetailObject;
import com.dapl.halaladvisor.Model.UserDataModel;
import com.dapl.halaladvisor.Model.UserLatLong;
import com.dapl.halaladvisor.Model.profileupdateres.Userdetails;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

public class PrefUtils {
    private static String profile = "profile";
    private static String sites = "sites";

    private static String attendanceListResponse = "attendanceListResponse";

    private static String jobListResponse = "jobListResponse";

    private static String selectedJobObj = "selectedJobObj";
    private static String lastLocationUpdateTime = "lastLocationUpdateTime";
    private static String leaveTypeResponse = "leaveTypeResponse";


    public static void saveProfile(Context context, String profileJsonObject) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString(profile, profileJsonObject).apply();

    }


    public static UserDataModel getProfileObject(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String profileStr = sp.getString(profile, null);

        UserDataModel profileObject;

        if (profileStr != null) {


            profileObject = (UserDataModel) getObjectFromJson(profileStr, UserDataModel.class);


        } else {

            profileObject = null;
        }


        return profileObject;

    }


    public static String getJsonFromObject(Object object, Class<?> classType) {
        String jsonText = "";
        if (object != null) {
            Gson gson = new Gson();
            jsonText = gson.toJson(object, classType);
        }
        return jsonText;
    }

    public static Object getObjectFromJson(String jsonText, Class<?> classType) {
        Object object = null;
        if (jsonText != null) {
            Gson gson = new Gson();
            object = gson.fromJson(jsonText, classType);
        }
        return object;
    }

    public void saveUserData(Userdetails data, Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        String da = new Gson().toJson(data);

        editor.putString("data", da);
        editor.commit();
        editor.apply();


    }

    public Userdetails getData(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String da = sp.getString("data", "");
        if (da.isEmpty()) {
            return null;
        } else {
            return new Gson().fromJson(da, Userdetails.class);
        }

    }

    public static void saveCredential(Context context, String object) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("cred", object).apply();
    }

    public static Credential getCredential(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String profileStr = sp.getString("cred", null);

        Credential credential;

        if (profileStr != null) {


            credential = (Credential) getObjectFromJson(profileStr, Credential.class);


        } else {

            credential = null;
        }


        return credential;

    }


    public static void saveUserLatLong(Context context, String str) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("latLng", str).apply();
    }

    public static UserLatLong getUserLatLong(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String str = sp.getString("latLng", null);

        UserLatLong userLatLong;

        if (str != null) {


            userLatLong = (UserLatLong) getObjectFromJson(str, UserLatLong.class);


        } else {

            userLatLong = null;
        }


        return userLatLong;

    }

    public static void saveReviewStatus(Context context, String str) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("reviewStatus", str).apply();
    }

    public static String getReviewStatus(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String str = sp.getString("reviewStatus", "");


        return str;

    }

    public static void saveLastSearchedLocation(Context context, LastSearchLocationObject locationObject) {

        try {

            String jsn = getJsonFromObject(locationObject, LastSearchLocationObject.class).trim();

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

            sp.edit().putString("lasT_loC", jsn).apply();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static LastSearchLocationObject getLastSearchedLocation(Context context) {
        LastSearchLocationObject locationObject = null;
        try {

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            String jsn = sp.getString("lasT_loC", null);

            if (jsn != null) {

                locationObject = (LastSearchLocationObject) getObjectFromJson(jsn, LastSearchLocationObject.class);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return locationObject;

    }

    public static void saveReviewArrayFromReviewListScreen(Context context, String toString) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("allReview", toString).apply();

    }

    public static void saveReviewArrayFromDetailScreen(Context context, String toString) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("allReviewDetail", toString).apply();

    }

    public static void setProfile(Context context, String toString) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("photo", toString).apply();

    }

    public static String getProfile(Context context) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return sp.getString("photo", null);

    }

    public static String getAllReviewsFromReviewListScreen(Context context) {


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        return sp.getString("allReview", null);


    }

    public static String getAllReviewsFromDetailScreen(Context context) {


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);


        return sp.getString("allReviewDetail", null);


    }

    public static void saveProfileDetail(Context context, String jsnObj) {


        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("profileDetail", jsnObj.trim()).apply();


    }


    public static ProfileDetailObject getProfileDetailObject(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        String str = sp.getString("profileDetail", null);

        ProfileDetailObject detailObject;

        if (str != null) {


            detailObject = (ProfileDetailObject) getObjectFromJson(str, ProfileDetailObject.class);


        } else {

            detailObject = null;
        }


        return detailObject;

    }

    public static void removeAllSavedData(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().clear().apply();
    }

    public static void setFreePlanSelected(Context context, boolean selected) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("freePlanSelected", selected).apply();
    }

    public static boolean isFreePlanSelected(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("freePlanSelected", false);
    }

    /** Marks that the free/premium plan picker has been shown once (first app launch). */
    public static void setPlanSelectionSeen(Context context, boolean seen) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("planSelectionSeen", seen).apply();
    }

    /** True once the plan picker has been shown; used so it only appears the very first time. */
    public static boolean isPlanSelectionSeen(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getBoolean("planSelectionSeen", false);
    }

    public static void saveFirebaseToken(Context context, String fcmToken) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("fcm_token", fcmToken).apply();
    }

    public static String getFirebaseToken(Context context) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("fcm_token", null);
    }

    public static void saveActivityOpenFromNotification(Context context, boolean b) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean("isOpenFromNoti", b).apply();
    }

    public static void saveResId(Context context, String b) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("res_iddd", b).apply();
    }

    public static String getResId(Context context) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        return sp.getString("res_iddd", null);
    }
    public static boolean isActivityOpenFromNotification(Context context) {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        return sp.getBoolean("isOpenFromNoti", false);
    }

    public static void insertToAddressHistory(Context context, JSONObject jsonObject) {

        try {
            JSONArray array = getAddressHistory(context);
            array.put(0, jsonObject);

            if (array.length() > 15) {

            }

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            sp.edit().putString("addressHistory", array.toString()).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static JSONArray getAddressHistory(Context context) {
        JSONArray array = new JSONArray();
        try {
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
            String str = sp.getString("addressHistory", null);

            if (str != null) {
                array = new JSONArray(str);
            } else {
                array = new JSONArray();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return array;
    }
}
