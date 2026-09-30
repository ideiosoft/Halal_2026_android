package com.dapl.halaladvisor.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.SplashActivity;
import com.dapl.halaladvisor.Utility.PrefUtils;
import com.dapl.halaladvisor.extra.AllConstants;
import com.dapl.halaladvisor.extra.ConnectionDetector;

public class DroidWebViewActivity extends AppCompatActivity {
    WebView myWebView;
    private ProgressDialog pd;
    Boolean isInternetPresent = false;
    private ImageView progressBar;
    private ImageView ivBack;
    Context context;
    ConnectionDetector cd;
    String webURL = AllConstants.webUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_droid_web_view);
        context=this;
        cd = new ConnectionDetector(DroidWebViewActivity.this);
        progressBar = (ImageView) findViewById(R.id.progressBar);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        myWebView = (WebView) findViewById(R.id.myWebView);
        Log.e("weburl",webURL+"");

      //  myWebView.loadUrl("https://supernashbrothers.bopple.app/10391/menu");
        WebSettings webSettings = myWebView.getSettings();

        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        WebView.setWebContentsDebuggingEnabled(true);

        webSettings.setLoadsImagesAutomatically(true);
        myWebView.setWebViewClient(new MyWebViewClient());
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
       // myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        myWebView.getSettings().setAllowFileAccess(true);

        myWebView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
   //   myWebView.getSettings().setAppCacheMaxSize(1024 * 8);
        //  myWebView.getSettings().setAppCacheEnabled(true);

        myWebView.getSettings().setUseWideViewPort(false);
        myWebView.setWebChromeClient(new MyWebChromeClient());
        myWebView.setWebViewClient(new WebViewClient()  );
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(0);
            myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            myWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            myWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        // these settings speed up page load into the webview
        myWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        Glide.with(this)
                .load(R.drawable.custom_loader)
                .into(progressBar);
        //enableAd();
        checkStatus();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }



        myWebView.loadUrl(webURL);

    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost().equals(webURL)) {
                /*pd = new ProgressDialog(DroidWebViewActivity.this);
                pd.setMessage("Please wait ...");
                pd.show();*/
                return false;
            }


            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            /*if (pd != null) {
                pd.dismiss();
            }*/
            progressBar.setVisibility(View.GONE);
        }


    }

    class MyWebChromeClient extends WebChromeClient {

        ProgressDialog pDialog;

        public MyWebChromeClient() {
            // TODO Auto-generated constructor stub
            /*pDialog = new ProgressDialog(DroidWebViewActivity.this); //Use getActivity(); for Fragment
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setProgress(0);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);*/
            progressBar.setVisibility(View.VISIBLE);
        }

        public void onProgressChanged(WebView view, int progress) {
            if (progress < 100 && /*!pDialog.isShowing()*/!progressBar.isShown()) {
//                pDialog.show();
                progressBar.setVisibility(View.VISIBLE);
            }
//            pDialog.setProgress(progress);
            if (progress == 100) {
//                pDialog.dismiss();
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack(); // Go to previous page
            return true;
        }
        // Use this as else part
        return super.onKeyDown(keyCode, event);
    }

    void checkStatus() {

        isInternetPresent = cd.isConnectingToInternet();

        // check for Internet status
        if (isInternetPresent) {
            // Internet Connection is Present
            // make HTTP requests
//        showAlertDialog(getActivity(), "Internet Connection",
//                "You have internet connection", true);
        } else {
            // Internet connection is not present
            // Ask user to connect to Internet
            showAlertDialog(context, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        backFunction();
    }

    private void backFunction() {

        if (PrefUtils.isActivityOpenFromNotification(context)) {
            startActivity(new Intent(context, SplashActivity.class));
            finish();
        } else {

            finish();
        }


    }

    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting alert dialog icon
        alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}