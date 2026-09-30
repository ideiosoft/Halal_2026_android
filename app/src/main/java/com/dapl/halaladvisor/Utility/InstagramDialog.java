package com.dapl.halaladvisor.Utility;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dapl.halaladvisor.R;


public class InstagramDialog extends Dialog {

    static final float[] DIMENSIONS_LANDSCAPE = {460, 260};
    static final float[] DIMENSIONS_PORTRAIT = {450, 800};
    static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    static final int MARGIN = 0;
    static final int PADDING = 0;
    private static final String TAG = "Instagram-WebView";
    Context mContext;
    private String mUrl;
    private OAuthDialogListener mListener;
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private LinearLayout mContent;
    private TextView mTitle;

    public InstagramDialog(Context context, String url,
                           OAuthDialogListener listener) {
        super(context);
        mContext = context;
        mUrl = url;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mSpinner = new ProgressDialog(getContext());
//
//
//        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mSpinner.setMessage("Loading...");
//        mContent = new LinearLayout(getContext());
//        mContent.setOrientation(LinearLayout.VERTICAL);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_insta_web_view);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        mWebView = findViewById(R.id.instaWebView);

        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new OAuthWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        Log.e("murl", mUrl);
        mWebView.loadUrl(mUrl);
//        setUpTitle();
//        setUpWebView();
//
//        Display display = getWindow().getWindowManager().getDefaultDisplay();
//        final float scale = getContext().getResources().getDisplayMetrics().density;
//        float[] dimensions = (display.getWidth() < display.getHeight()) ? DIMENSIONS_PORTRAIT
//                : DIMENSIONS_LANDSCAPE;
//
//      /*  addContentView(mContent, new FrameLayout.LayoutParams(
//                (int) (dimensions[0] * scale + 0.5f), (int) (dimensions[1]
//                * scale + 0.5f))); */
//
//        addContentView(mContent, new FrameLayout.LayoutParams(
//                (int) (dimensions[0] * scale + 0.5f), (int) (dimensions[1]
//                * scale + 0.5f)));
//      //  CookieSyncManager.createInstance(getContext());
//      //  CookieManager cookieManager = CookieManager.getInstance();
//       // cookieManager.removeAllCookie();
    }

    private int getScale() {
        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width) / new Double(100);
        val = val * 100d;
        return val.intValue();
    }

    private void setUpTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mTitle = new TextView(getContext());
        mTitle.setText("Instagram");
        mTitle.setTextColor(Color.WHITE);
        mTitle.setTypeface(Typeface.DEFAULT_BOLD);
        mTitle.setBackgroundColor(Color.BLACK);
        mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
        mContent.addView(mTitle);
    }

    private void setUpWebView() {
        mWebView = new WebView(getContext());
        mWebView.setVerticalScrollBarEnabled(true);
        mWebView.setHorizontalScrollBarEnabled(true);
        mWebView.setWebViewClient(new OAuthWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.setPadding(0, 0, 0, 0);
        mWebView.setInitialScale(getScale());
        mWebView.loadUrl(mUrl);
        mWebView.setLayoutParams(FILL);
        mContent.addView(mWebView);
    }

    public interface OAuthDialogListener {
        public abstract void onComplete(String accessToken);

        public abstract void onError(String error);
    }

    private class OAuthWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.e(TAG, "Redirecting URL " + url);
         /*   String u="";
            try {
                Log.e("url temp", URLDecoder.decode(url, "UTF-8"));
                u=URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
            //   if (u.startsWith("https://l.instagram.com/?u=https://xperge.com")) {
            if (url.startsWith(InstagramApp.mCallbackUrl)) {
                String urls[] = url.split("=");
                mListener.onComplete(urls[1]);
                InstagramDialog.this.dismiss();
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            Log.d(TAG, "Page error: " + description);

            super.onReceivedError(view, errorCode, description, failingUrl);
            mListener.onError(description);
            InstagramDialog.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "Loading URL: " + url);

            super.onPageStarted(view, url, favicon);
//            mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            Log.d(TAG, "onPageFinished URL: " + url);
            //    mSpinner.dismiss();
        }

    }

}