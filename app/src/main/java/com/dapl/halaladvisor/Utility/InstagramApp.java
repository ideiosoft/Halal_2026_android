package com.dapl.halaladvisor.Utility;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class InstagramApp {
    private InstagramSession mSession;
    private InstagramDialog mDialog;
    private OAuthAuthenticationListener mListener;
    private ProgressDialog mProgress;
    public static String mAuthUrl;
    private String mTokenUrl;
    private String mAccessToken;
    private Context mCtx;

    private String mClientId;
    private String type;
    private String mClientSecret;

    private static int WHAT_FINALIZE = 0;
    private static int WHAT_ERROR = 1;
    private static int WHAT_FETCH_INFO = 2;

    /**
     * Callback url, as set in 'Manage OAuth Costumers' page
     * (https://developer.github.com/)
     */

    public static String mCallbackUrl = "";
    public static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    public static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String API_URL = "https://api.instagram.com/v1";

    private static final String TAG = "InstagramAPI";









    public InstagramApp(Context context, String clientId, String clientSecret,
                        String callbackUrl) {

        mClientId = clientId;
        mClientSecret = clientSecret;
        mCtx = context;
        mSession = new InstagramSession(context);
        mAccessToken = mSession.getAccessToken();
        mCallbackUrl = callbackUrl;



        mTokenUrl = TOKEN_URL + "?app_id=" + clientId + "&app_secret="
                + clientSecret + "&redirect_uri=" + mCallbackUrl
                + "&grant_type=ig_exchange_token"+ "&access_token={short-lived-access-token}";
        mAuthUrl = AUTH_URL
                + "?app_id="
                + clientId
                + "&redirect_uri="
                + mCallbackUrl
                + "&response_type=code&scope=user_profile,user_media";

        InstagramDialog.OAuthDialogListener listener = new InstagramDialog.OAuthDialogListener() {
            @Override
            public void onComplete(String code) {

                Log.e("code",code);
                getAccessToken(code.substring(0,code.length()-2));
            }

            @Override
            public void onError(String error)
            {
                mListener.onFail("Authorization failed");
            }
        };



        mDialog = new InstagramDialog(context, mAuthUrl, listener);

        mProgress = new ProgressDialog(context);
        mProgress.setCancelable(false);
    }

    public   void getAccessToken(final String code) {
        mProgress.setMessage("Getting access token ...");
        if(type!=null) {
            if (type.equalsIgnoreCase("show")) {
                mProgress.show();
            }

        }
        new Thread() {
            @Override
            public void run() {
                Log.e(TAG, "Getting access token");
                int what = WHAT_FETCH_INFO;
                try {
                    URL url = new URL(TOKEN_URL);
                    // URL url = new URL(mTokenUrl + "&code=" + code);
                    Log.e(TAG, "Opening Token URL " + url.toString());
                    HttpsURLConnection urlConnection = (HttpsURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);

                    urlConnection.connect();
                    OutputStreamWriter writer = new OutputStreamWriter(
                            urlConnection.getOutputStream());
                    writer.write("app_id=" + mClientId + "&app_secret="
                            + mClientSecret + "&grant_type=authorization_code"
                            + "&redirect_uri=" + mCallbackUrl + "&code=" + code);
                    writer.flush();
                    String response = streamToString(urlConnection
                            .getInputStream());
                    Log.e(TAG, "response " + response);
                    JSONObject jsonObj =   new JSONObject(response)
                            ;

                    mAccessToken = jsonObj.getString("access_token");


                    // mAccessToken = jsonObj.getString("user_id");
                    // Log.i(TAG, "Got access token: " + mAccessToken);

                    mSession.storeAccessToken(mAccessToken, jsonObj.getString("user_id"));

                } catch (Exception ex) {
                    Log.e("exception",""+ex.toString());
                    what = WHAT_ERROR;
                    ex.printStackTrace();
                }

                mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
            }
        }.start();
    }
    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return sslSocketFactory;
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            return null;
        }

    }
   /* private void fetchUserName() {
        mProgress.setMessage("Finalizing ...");

        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Fetching user info");
                int what = WHAT_FINALIZE;
                try {
                    URL url = new URL("https://graph.instagram.com/me/media?fields=id,media_type,media_url,thumbnail_url,timestamp,username,permalink,caption&access_token=" + mAccessToken);
                    Log.d(TAG, "Opening URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();
                    String response = streamToString(urlConnection
                            .getInputStream());
                    System.out.println(response);

                    Log.e(TAG, "response " + response);
                    JSONObject jsonObj = (JSONObject) new JSONTokener(response)
                            .nextValue();
                    String name = jsonObj.getJSONObject("data").getString(
                            "full_name");
                    // String bio =
                    // jsonObj.getJSONObject("data").getString("bio");
                    Log.i(TAG, "Got name: " + name);
                } catch (Exception ex) {
                    what = WHAT_ERROR;
                    ex.printStackTrace();
                }

                mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
            }
        }.start();

    }*/

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_ERROR) {
                if(type!=null)
                {
                    if(type.equalsIgnoreCase("show"))
                    {
                        mProgress.dismiss();}}
                if (msg.arg1 == 1) {
                    mListener.onFail("Failed to get access token");
                } else if (msg.arg1 == 2) {
                    mListener.onFail("Failed to get user information");
                }
            } else if (msg.what == WHAT_FETCH_INFO) {
                if(type!=null)
                {
                    if(type.equalsIgnoreCase("show")) {
                        mProgress.dismiss();
                    }}
                mListener.onSuccess();
                // fetchUserName();
            } else {
                if(type!=null)
                {
                    if(type.equalsIgnoreCase("show"))
                    {
                        mProgress.dismiss();}}
                // mListener.onSuccess();
            }
        }
    };

    public boolean hasAccessToken() {
        return (mAccessToken == null) ? false : true;
    }

    public void setListener(OAuthAuthenticationListener listener) {
        mListener = listener;
    }

    // getting username
    public String getUserName() {
        return mSession.getUsername();
    }

    // getting user id
    public String getId() {
        return mSession.getId();
    }

    // getting username
    public String getName() {
        return mSession.getName();
    }

    // getting user image
    public String getUserPicture() {
        return mSession.getUserImage();
    }

    // getting accesstoken
    public String getAccessToken() {
        return mSession.getAccessToken();
    }

    public void authorizeTemp() {
        // Intent webAuthIntent = new Intent(Intent.ACTION_VIEW);
        // webAuthIntent.setData(Uri.parse(AUTH_URL));
        // mCtx.startActivity(webAuthIntent);
        mDialog.show();
    }
    public void authorize(String type) {
        this.type=type;
        // Intent webAuthIntent = new Intent(Intent.ACTION_VIEW);
        // webAuthIntent.setData(Uri.parse(AUTH_URL));
        // mCtx.startActivity(webAuthIntent);

        mDialog.show();
    }

    private String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }

    public void resetAccessToken() {
        if (mAccessToken != null) {
            mSession.resetAccessToken();
            mAccessToken = null;
        }
    }

    public interface OAuthAuthenticationListener {
        public abstract void onSuccess();

        public abstract void onFail(String error);
    }
}
