package  com.dapl.halaladvisor.Utility;



import android.content.Context
import android.content.Intent
import android.renderscript.RenderScript
import android.util.Log
import android.widget.Toast
//import com.androidnetworking.AndroidNetworking
//import com.androidnetworking.common.Priority
//import com.androidnetworking.error.ANError
//import com.androidnetworking.interfaces.JSONObjectRequestListener
//import com.androidnetworking.interfaces.OkHttpResponseAndJSONObjectRequestListener
import com.dapl.halaladvisor.interfaces.OnImageUploadResponseEventListner
import com.dapl.halaladvisor.interfaces.OnResponseEventListner
import com.dapl.halaladvisor.interfaces.OnResponseInstaListner
import com.dapl.halaladvisor.interfaces.ProgressUploadListener
import com.facebook.AccessToken
import com.facebook.GraphRequest

import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
//
//class Server {
//
//    var prefManager: PrefManager? = null
//    private var mListener: OnResponseEventListner? = null //listener field
//    private var mInstaListener: OnResponseInstaListner? = null //listener field
//    private var mImageUploadListner: OnImageUploadResponseEventListner? =
//            null // image upload listner
//    private var progressUploadListener: ProgressUploadListener? =
//            null
//    var url: String? = null // api url
//    var context: Context? = null
//
//    //setting the listener
//    /**
//     * Constructor
//     * @param url
//     * @param callbackInterface class which defines the callback method
//     */
//    constructor(url: String, callbackInterface: OnResponseEventListner, context: Context?) {
//        this.url = url
//        this.mListener = callbackInterface
//        this.context = context
//        prefManager = PrefManager.getInstance(context)
//    }
//
//    constructor(
//            url: String,
//            callbackInterface: OnResponseEventListner,
//            progressUploadListener: ProgressUploadListener,
//            context: Context?
//    ) {
//        this.url = url
//        this.mListener = callbackInterface
//        this.progressUploadListener = progressUploadListener
//        this.context = context
//        prefManager = PrefManager.getInstance(context)
//    }
//
//    constructor(
//            url: String,
//            callbackInterface: OnResponseInstaListner,
//            context: Context?,
//            type: String
//    ) {
//        this.url = url
//        this.mInstaListener = callbackInterface
//        this.context = context
//        prefManager = PrefManager.getInstance(context)
//    }
//
//    constructor(mImageUploadListner: OnImageUploadResponseEventListner, context: Context?) {
//        this.mImageUploadListner = mImageUploadListner
//        this.context = context
//        prefManager = PrefManager.getInstance(context)
//
//    }
//
//    constructor(
//            mImageUploadListner: OnImageUploadResponseEventListner,
//            progressUploadListener: ProgressUploadListener,
//            context: Context?
//    ) {
//        this.mImageUploadListner = mImageUploadListner
//        this.progressUploadListener = progressUploadListener
//        this.context = context
//        prefManager = PrefManager.getInstance(context)
//
//    }
//
//
//    /*
//    * @object: login
//    * @params : authType,phone*/
//
//    fun apiCall(jsonKeyValue: JSONObject) {
//
//        Log.d("#######", jsonKeyValue.toString())
//
//        //ProgressDialogUtils.showDialog(context)
//        AndroidNetworking.post(this.url)
//                // .addBodyParameter(hashMap)
//                // .addHeaders("Authorization", auth)
//                .addJSONObjectBody(jsonKeyValue)
//                .setTag("apiCall")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
//                    override fun onResponse(okHttpResponse: Response?, response: JSONObject) {
//                        Log.d("response", "response :" + response.toString())
//                        // ProgressDialogUtils.dismissDialog(context)
//
//                        if (response.has("token")) {
//                            prefManager!!.savePreference(
//                                    "x-auth",
//                                    response.getJSONObject("token").get("accessToken")
//                            )
//                        }
//                        mListener!!.onResponse(response)
//
//                    }
//
//                    override fun onError(anError: ANError?) {
//                        //ProgressDialogUtils.dismissDialog(context)
//                        if (anError?.errorCode == 403) {
//                         //   handleForceLogout()
//                            return
//                        }
//
//                        mListener!!.onError(anError!!.errorCode.toString())
//                        Toast.makeText(
//                                context,
//                                "Error From Server, Try again later...",
//                                Toast.LENGTH_SHORT
//                        ).show()
//
//
//                        Log.e("MSG", anError?.toString())
//                        Log.e("MSG", anError.errorDetail)
//                        Log.e("MSG", anError.errorBody)
//
//                    }
//                })
//
//    }
//
//
//    fun getCurrentTime(jsonKeyValue: JSONObject, auth: String) {
//
//        Log.d("#######", jsonKeyValue.toString())
//
//        // ProgressDialogUtils.showDialog(context)
//        AndroidNetworking.post(this.url)
//                // .addBodyParameter(hashMap)
//                .addHeaders("Authorization", auth)
//                .addJSONObjectBody(jsonKeyValue)
//                .setTag("apiCall")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
//                    override fun onResponse(okHttpResponse: Response?, response: JSONObject) {
//                        Log.d("response", "response :" + response.toString())
//                        //           ProgressDialogUtils.dismissDialog(context)
//
//                        if (response.has("token")) {
//                            prefManager!!.savePreference(
//                                    "x-auth",
//                                    response.getJSONObject("token").get("accessToken")
//                            )
//                        }
//                        mListener!!.onResponse(response)
//
//                    }
//
//                    override fun onError(anError: ANError?) {
//                        //         ProgressDialogUtils.dismissDialog(context)
//                        if (anError?.errorCode == 403) {
//                            //handleForceLogout()
//                            return
//                        }
//
//                        mListener!!.onError(anError!!.errorCode.toString())
//                        Toast.makeText(
//                                context,
//                                "Error From Server, Try again later...",
//                                Toast.LENGTH_SHORT
//                        ).show()
//
//
//                        Log.e("MSG", anError?.toString())
//                        Log.e("MSG", anError.errorDetail)
//                        Log.e("MSG", anError.errorBody)
//
//                    }
//                })
//
//    }
//
//    fun apiCall(jsonKeyValue: JSONObject, position: Int) {
//
//        Log.d("#######", jsonKeyValue.toString())
//
//        ProgressDialogUtils.showDialog(context)
//        AndroidNetworking.post(this.url)
//                // .addBodyParameter(hashMap)
//                .addJSONObjectBody(jsonKeyValue)
//                .setTag("apiCall")
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
//                    override fun onResponse(okHttpResponse: Response?, response: JSONObject) {
//                        Log.d("response", "response :" + response.toString())
//                        ProgressDialogUtils.dismissDialog(context)
//
//                        if (response.has("token")) {
//                            prefManager!!.savePreference(
//                                    "x-auth",
//                                    response.getJSONObject("token").get("accessToken")
//                            )
//                        }
//                        response.put("item", position)
//                        mListener!!.onResponse(response)
//
//                    }
//
//                    override fun onError(anError: ANError?) {
//                        ProgressDialogUtils.dismissDialog(context)
//                        if (anError?.errorCode == 403) {
//                            //handleForceLogout()
//                            return
//                        }
//
//                        mListener!!.onError(anError!!.errorDetail)
//                        Toast.makeText(
//                                context,
//                                "Error From Server, Try again later...",
//                                Toast.LENGTH_SHORT
//                        ).show()
//
//
//                        Log.e("MSG", anError?.toString())
//                        Log.e("MSG", anError.errorDetail)
//                        Log.e("MSG", anError.errorBody)
//
//                    }
//                })
//
//    }
//
//
//
//
//
//
//    //signup
//    fun call(jsonKeyValue: JSONObject) {
//
//        prefManager = PrefManager.getInstance(context)
//        AndroidNetworking.post(url)
//                .addJSONObjectBody(jsonKeyValue)
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsOkHttpResponseAndJSONObject(object : OkHttpResponseAndJSONObjectRequestListener {
//                    override fun onResponse(okHttpResponse: Response?, response: JSONObject) {
//                        Log.d("HEADER", "Headers :" + okHttpResponse!!.headers())
//                        Log.d("HEADER", "Headers :" + response.toString())
//                        ProgressDialogUtils.dismissDialog(context)
//                        if (checkForceLogout(response)) {
//                            //handleForceLogout()
//
//                            return
//                        }
//                        if (response.has("message"))
//                            Toast.makeText(
//                                    context,
//                                    response.getString("message"),
//                                    Toast.LENGTH_SHORT
//                            ).show()
//                        //  prefManager!!.savePreference(AppConstants.XAUTHTOKEN, okHttpResponse.headers()[AppConstants.XAUTHTOKEN].toString())
//
//                        if (response.has("token")) {
//                            prefManager!!.savePreference(
//                                    "x-auth",
//                                    response.getJSONObject("token").get("accessToken")
//                            )
//                        }
//                        mListener!!.onResponse(response)
//
//                    }
//
//                    override fun onError(anError: ANError?) {
//                        ProgressDialogUtils.dismissDialog(context)
//                        if (anError?.errorCode == 403) {
//                            //handleForceLogout()
//                            return
//                        }
//
//                        mListener!!.onError(anError!!.errorDetail)
//                        /* Toast.makeText(
//                             context,
//                             "Something went wrong, Try again later...",
//                             Toast.LENGTH_SHORT
//                         ).show()*/
//                        Log.e("MSG", anError.errorDetail)
//
//                    }
//                })
//
//
//    }
//
//
//
//
//    fun checkForceLogout(jsonObj: JSONObject): Boolean {
//
//        if (jsonObj.has("code") && jsonObj.getString("code").equals("403")) {
//
//
//            return true
//        }
//
//        return false
//    }
//
////    fun handleForceLogout() {
////
////        /* logoutFromFacebook(object : LogoutFromFacebookListener {
////             override fun onLoggedOutFromFacebook() {*/
////        if (MainActivity.isMyServiceRunning(context!!)) {
////            MainActivity().stopAdvertisingService(context!!)
////        }
////        ProgressDialogUtils.dismissDialog(context)
////        val prefManager = PrefManager.getInstance(context)
////        val auth = prefManager.getPreference<String>(AppConstants.XAUTHTOKEN)
////        val DEVICE_TOKEN = prefManager.getPreference<String>(AppConstants.DEVICE_TOKEN, "")
////
////        MainActivity().logoutFromSever("Bearer " + auth, object : OnResponseEventListner {
////            override fun onResponse(jsonArray: JSONArray) {
////                Log.i("logout", jsonArray.toString())
////            }
////
////            override fun onError(errorString: String) {
////                Log.i("logout", errorString.toString())
////            }
////
////            override fun onResponse(jsonObject: JSONObject) {
////                Log.i("logout", jsonObject.toString())
////            }
////        }, context!!)
////        var temp_token: String? =
////                prefManager!!.getPreference<String>(AppConstants.FB_ACCESS_TOKEN, "abcde")
////
////        prefManager?.deleteAllPreference()
////        prefManager!!.savePreference(
////                AppConstants.FB_ACCESS_TOKEN,
////                temp_token!!
////        )
////        prefManager.savePreference(AppConstants.XAUTHTOKEN, auth)
////        prefManager.savePreference(AppConstants.DEVICE_TOKEN, DEVICE_TOKEN)
////
////        val intent = Intent(context, SplashActivity::class.java)
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
////        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
////        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
////        context!!.startActivity(intent)
////
////
////        /*}*/
////        /* })*/
////    }
//
//
//
//    /*
//          * Get insta posts api by gaurav
//          * */
//    fun getInstaPosts(urll: String) {
//        AndroidNetworking.get(urll)
//                .build()
//                .getAsJSONObject(object : JSONObjectRequestListener {
//                    override fun onResponse(response: JSONObject) {
//
//                        mInstaListener!!.onInstaResponse(response)
//                    }
//
//                    override fun onError(anError: ANError?) {
//
//
//                        Log.e("insta errror","--"+anError)
//                        if (anError?.errorCode == 403) {
//                        //    handleForceLogout()
//                            return
//                        }
//                        mInstaListener!!.onInstaError(anError!!.errorDetail)
//                    }
//                })
//    }
//}
//
