package com.dapl.halaladvisor.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.dapl.halaladvisor.R;
import com.dapl.halaladvisor.extra.AllConstants;
import com.dapl.halaladvisor.extra.ConnectionDetector;

import java.net.URISyntaxException;

import dmax.dialog.SpotsDialog;

public class AboutWebScreen extends AppCompatActivity {

    Context context;
    WebView myWebView;
    TextView tvHeading;
    Boolean isInternetPresent = false;
    // Connection detector class
    ConnectionDetector cd;
    String webURL = "";

    //custom dialog
    AlertDialog dialog;
    private ProgressDialog pd;
    /*private Toolbar mToolbar;
    private TextView toolbar_header_name, toolbar_header_name2, toolbar_tv_add;*/
    private ImageView toolbar_tag_icon, ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutweb);

        context = this;

        initToolbarViews();

        cd = new ConnectionDetector(AboutWebScreen.this);
        Log.e("Direction", webURL);
        tvHeading = findViewById(R.id.tvHeading);
        myWebView = findViewById(R.id.myWebView);


//        enableAd();
        checkStatus();


        getDataFromIntent();
    }




    public  void getDataFromIntent(){
        if (getIntent().hasExtra("about")){
            webURL = getIntent().getStringExtra("about");
            tvHeading.setText(getIntent().getStringExtra("type"));

            myWebView.loadUrl(webURL);
            WebSettings webSettings = myWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            if (Build.VERSION.SDK_INT < 8) {

             } else {
                myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
            }

            myWebView.setWebViewClient(new MyWebViewClient());
            myWebView.getSettings().setJavaScriptEnabled(true);
            myWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            myWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
            myWebView.getSettings().setAllowFileAccess(true);


            myWebView.getSettings().setUseWideViewPort(false);
            myWebView.setWebChromeClient(new MyWebChromeClient());
            myWebView.setWebViewClient(new WebViewClient());

            // these settings speed up page load into the webview
            myWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            myWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

            myWebView.setWebViewClient(new WebViewClient() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {


                    Log.e("url interanal","---"+request.getUrl());
                    if (request.getUrl().toString().startsWith("intent://") &&
                            request.getUrl().toString() .contains("com.facebook.orca")) {
                        try {
                            Context context = view.getContext();
                            Intent intent = null;
                            try {
                                intent = Intent.parseUri(request.getUrl().toString(), Intent.URI_INTENT_SCHEME);
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }

                            if (intent != null) {
                                view.stopLoading();

                                PackageManager packageManager = context.getPackageManager();
                                ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                                if (info != null) {
                                    context.startActivity(intent);
                                } else {
                                    String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                              String      packageee = intent.getStringExtra("package");

                                    Log.e( "packageee", "Can't resolve intent://" +packageee );
                                    view.loadUrl(fallbackUrl);
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.facebook.orca")));
                                    } catch (android.content.ActivityNotFoundException anfe) {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.facebook.orca")));
                                    }
                                    // or call external broswer
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
//                    context.startActivity(browserIntent);
                                }

                                return true;
                            }
                        } catch (Exception e) {
                            Log.e( "shdfjkashfas", "Can't resolve intent://" );
                        }

                    }
                    else   if (request.getUrl().toString().startsWith("intent://")) {
                        try {
                            Context context = view.getContext();
                            Intent intent = null;
                            try {
                                intent = Intent.parseUri(request.getUrl().toString(), Intent.URI_INTENT_SCHEME);
                            } catch (URISyntaxException e) {
                                e.printStackTrace();
                            }

                            if (intent != null) {
                                view.stopLoading();

                                PackageManager packageManager = context.getPackageManager();
                                ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                                if (info != null) {
                                    context.startActivity(intent);
                                } else {
                                    String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                                    view.loadUrl(fallbackUrl);

                                    // or call external broswer
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
//                    context.startActivity(browserIntent);
                                }

                                return true;
                            }
                        } catch (Exception e) {
                            Log.e( "shdfjkashfas", "Can't resolve intent://" );
                        }

                    }else {

                        if (Uri.parse(String.valueOf(request.getUrl())).getHost().equals(webURL)) {
                /*pd = new ProgressDialog(DirectionActivity.this);
                pd.setMessage("Please wait ...");
                pd.show();*/
                            dialog = new SpotsDialog(AboutWebScreen.this, R.style.CustomDialog);
                            dialog.setCancelable(false);
                            return false;
                        }


                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(request.getUrl().toString()));
                        startActivity(intent);
                    }
                    return false;
                }
            });
        }
    }
    /*Initialize all views for toolbar*/
    private void initToolbarViews() {


        ivBack = findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


/*

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        toolbar_header_name = mToolbar.findViewById(R.id.tvAddress);
        //  toolbar_header_name2 = (TextView) mToolbar.findViewById(R.id.toolbar_header_name2);
        //  toolbar_tv_add = (TextView) mToolbar.findViewById(R.id.toolbar_tv_add);
        // toolbar_tag_icon = (ImageView) mToolbar.findViewById(R.id.toolbar_tag_icon);
        toolbar_header_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        toolbar_header_name.setText("GET DIRECTIONS");
        toolbar_header_name.setTextSize(18);


        toolbar_header_name.setVisibility(View.VISIBLE);

*/

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
            showAlertDialog(AboutWebScreen.this, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

    }

    // TODO Auto-generated method stub
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

Log.e("url--","---"+url);

            if (url.toString().startsWith("intent://") &&
                    url.toString().toString() .contains("com.facebook.orca")) {
                try {
                    Context context = view.getContext();
                    Intent intent = null;
                    try {
                        intent = Intent.parseUri(url.toString(), Intent.URI_INTENT_SCHEME);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    if (intent != null) {
                        view.stopLoading();

                        PackageManager packageManager = context.getPackageManager();
                        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        if (info != null) {
                            context.startActivity(intent);
                        } else {
                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                            String      packageee = intent.getStringExtra("package");

                            Log.e( "packageee", "Can't resolve intent://" +packageee );
                            view.loadUrl(fallbackUrl);
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.facebook.orca")));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.facebook.orca")));
                            }
                            // or call external broswer
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
//                    context.startActivity(browserIntent);
                        }

                        return true;
                    }
                } catch (Exception e) {
                    Log.e( "shdfjkashfas", "Can't resolve intent://" );
                }

            }
            else  if (url.startsWith("intent://")) {
                try {
                    Context context = view.getContext();
                    Intent intent = null;
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    if (intent != null) {
                        view.stopLoading();

                        PackageManager packageManager = context.getPackageManager();
                        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        if (info != null) {
                            context.startActivity(intent);
                        } else {
                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                            view.loadUrl(fallbackUrl);

                            // or call external broswer
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
//                    context.startActivity(browserIntent);
                        }

                        return true;
                    }
                } catch (Exception e) {
                    Log.e( "shdfjkashfas", "Can't resolve intent://" );
                }

            }else {

                if (Uri.parse(String.valueOf(url.toString())).getHost().equals(webURL)) {
                /*pd = new ProgressDialog(DirectionActivity.this);
                pd.setMessage("Please wait ...");
                pd.show();*/
                    dialog = new SpotsDialog(AboutWebScreen.this, R.style.CustomDialog);
                    dialog.setCancelable(false);
                    return false;
                }


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.toString()));
                startActivity(intent);
            }

       /*     if (url.startsWith("intent://")) {
                try {
                    Context context = view.getContext();
                    Intent intent = null;
                    try {
                        intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }

                    if (intent != null) {
                        view.stopLoading();

                        PackageManager packageManager = context.getPackageManager();
                        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        if (info != null) {
                            context.startActivity(intent);
                        } else {
                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                            view.loadUrl(fallbackUrl);

                            // or call external broswer
//                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
//                    context.startActivity(browserIntent);
                        }

                        return true;
                    }
                } catch (Exception e) {
                    Log.e( "shdfjkashfas", "Can't resolve intent://" );
                }

                }else {

                if (Uri.parse(url).getHost().equals(webURL)) {
                *//*pd = new ProgressDialog(DirectionActivity.this);
                pd.setMessage("Please wait ...");
                pd.show();*//*
                    dialog = new SpotsDialog(AboutWebScreen.this, R.style.CustomDialog);
                    dialog.setCancelable(false);
                    return false;
                }


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }


*/




            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (dialog != null) {
                dialog.dismiss();
            }
        }

    }

    class MyWebChromeClient extends WebChromeClient {

        ProgressDialog pDialog;
        private Context context;

        public MyWebChromeClient() {
            // TODO Auto-generated constructor stub
            /*pDialog = new ProgressDialog(DirectionActivity.this, R.style.CustomDialog); //Use getActivity(); for Fragment
            pDialog.setMessage("Loading...");
            pDialog.setCancelable(false);
            pDialog.setProgress(0);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);*/
            //set custom dialog
            context = AboutWebScreen.this;
            dialog = new SpotsDialog(AboutWebScreen.this, R.style.CustomDialog);
            dialog.setCancelable(false);

        }

        public void onProgressChanged(WebView view, int progress) {
            if (!((Activity) context).isFinishing()) {
                if (progress < 100 && !dialog.isShowing()) {
                    dialog.show();
                }
//            dialog.setProgress(progress);
                if (progress == 100) {
                    dialog.dismiss();
                }
            }
        }

    }
}