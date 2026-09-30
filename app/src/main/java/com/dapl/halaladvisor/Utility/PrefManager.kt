package  com.dapl.halaladvisor.Utility;

import android.content.Context
import android.content.SharedPreferences

class PrefManager private constructor(context: Context?) {
    private val sharedPreferences: SharedPreferences


    init {
        val prefsFile = context?.packageName
        sharedPreferences = context!!.getSharedPreferences(prefsFile, Context.MODE_PRIVATE)
        sharedPreferences.edit().apply()
    }


    fun deletePreference(key: String) {
        if (sharedPreferences.contains(key)) {
            sharedPreferences.edit().remove(key).apply()
        }
    }

    fun deleteAllPreference() {
        try {
            sharedPreferences.edit().clear().apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun savePreference(key: String, value: Any) {
        deletePreference(key)
        if (value is Boolean) {
            sharedPreferences.edit().putBoolean(key, value).apply()
        } else if (value is Int) {
            sharedPreferences.edit().putInt(key, value).apply()
        } else if (value is Float) {
            sharedPreferences.edit().putFloat(key, value).apply()
        } else if (value is Long) {
            sharedPreferences.edit().putLong(key, value).apply()
        } else if (value is String) {
            sharedPreferences.edit().putString(key, value).apply()
        } else if (value is Enum<*>) {
            sharedPreferences.edit().putString(key, value.toString()).apply()
        }
        else if (value is HashSet<*>) {
            sharedPreferences.edit().putStringSet(key, value as MutableSet<String>?).commit()
        }
        /*else if (value != null) {
            throw new RuntimeException("Attempting to save non-primitive preference");
        }*/

    }

    fun <T> getPreference(key: String): T {
        return sharedPreferences.all[key] as T
    }

    fun <T> getPreference(key: String, defValue: T): T {
        val returnValue = sharedPreferences.all[key] as T
        return returnValue ?: defValue
    }

    fun isPreferenceExists(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    companion object {
        private var sInstance: PrefManager? = null

        @Synchronized
        fun getInstance(context: Context?): PrefManager {
            if (sInstance == null) {
                sInstance = PrefManager(context)
            }
            return sInstance as PrefManager
        }
    }
}