package com.dapl.halaladvisor.Utility;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.UUID;

public class LogUtils {

    public static void printf(String message) {
        System.out.println(message);
    }
    public static String getDeviceID(Context context) {
        String deviceID = "";
        try {

            String deviceName = getDeviceName();
            String macid = getWifiMacID(context);
            String android_id = getAndroid_ID(context);

            UUID deviceUuid = new UUID(android_id.hashCode(), ((long) macid.hashCode() << 32) | deviceName.hashCode());
            deviceID = deviceUuid.toString();
            //deviceID = android_id+"_"+deviceName+"_"+macid;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceID;
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }



    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }


    public static String getWifiMacID(Context context) {
        String m_wlanMacAdd = "";

        try {

            WifiManager m_wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            m_wlanMacAdd = m_wm.getConnectionInfo().getMacAddress();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return m_wlanMacAdd;
    }


    public static String getAndroid_ID(Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        return android_id;
    }

}
