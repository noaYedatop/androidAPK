package com.credix.pinpaddriverwithandroidusage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.credix.pinpaddriverwithandroidusage.WebInterface.BackOfficeInterface;
import com.google.firebase.analytics.FirebaseAnalytics;

public class BackOfficeActivity extends BaseActivity {
    private WebView wv,addedReceiptWebView;
    private BackOfficeInterface backOfficeInterface;
    private String url;
    private View reloadView;
    public View progress;
    private Handler handler = new Handler();
    private RelativeLayout root_view;
    private WebView newWebView;
    private InputMethodManager inputMethodManager;
    private FirebaseAnalytics mFirebaseAnalytics;

    private FrameLayout receiptWebView;




    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            BackOfficeActivity.this.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Utils.hideSystemUI(getWindow());
                }
            },3000);
            return false;
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.hideSystemUI(getWindow());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "BackOfficeActivity");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "BackOfficeActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);



        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root_view = this.findViewById(R.id.root_view);
        progress = findViewById(R.id.progress);


        reloadView = findViewById(R.id.reload);
        receiptWebView = (FrameLayout) findViewById(R.id.webReciept);
        wv = (WebView) this.findViewById(R.id.fullscreen_content);
        wv.clearCache(true);
        wv.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        Intent intent = getIntent();
        url = intent.getStringExtra("URL");


        inputMethodManager = (InputMethodManager) BackOfficeActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);

        hideKeyboard();
        wv.setOnTouchListener(touchListener);


        wv.requestFocus(View.FOCUS_DOWN);

        wv.getSettings().setJavaScriptEnabled(true);

        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setSupportMultipleWindows(true);
        wv.setFocusable(true);
        wv.setFocusableInTouchMode(true);



        wv.setWebChromeClient(wv_chromClient);


        wv.setWebViewClient(wv_webClient);

        wv.loadUrl(url);
        initPrinter();
        registerPrinter(this);


        backOfficeInterface = new BackOfficeInterface(this,wv,/*MainActivity.rtPrinter,*/ receiptWebView );

        wv.addJavascriptInterface(backOfficeInterface, "android");
        wv.addJavascriptInterface(backOfficeInterface, "android2");

        BackOfficeActivity.this.runOnUiThread(closeKeyboard);
        handler.postDelayed(closeKeyboard,200);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        backOfficeInterface = null;
        receiptWebView.removeAllViews();
        root_view.removeAllViews();
        addedReceiptWebView = null;
    }

    final Runnable closeKeyboard = new Runnable() {
        @Override
        public void run() {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        }
    };
    WebViewClient wv_webClient = new WebViewClient() {


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            BackOfficeActivity.this.hideKeyboard();
            reloadView.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
//                redirect(url);
        }



        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.endsWith("modules/stock/cashbox_fe"))
            {
                BackOfficeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BackOfficeActivity.this.finish();
                    }
                });

            }else
            {
                view.loadUrl(url);
            }

            return true;
        }
    };
    WebViewClient newWV_WebClient = new WebViewClient(){

        private boolean pageStarted;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pageStarted = true;
            BackOfficeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.setVisibility(View.VISIBLE);

                }
            });
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            BackOfficeActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (pageStarted != true && newWebView.canGoBack()){
                        newWebView.goBack();
                    }else{
                        pageStarted = false;
                    }

                    progress.setVisibility(View.GONE);
                }
            });

        }

        @Override
        public boolean shouldOverrideUrlLoading (WebView view, String url) {
//                        handleWebViewLinks(url); // you can get your target url here
            Log.i("WINDOW","PSTH "+url);
            if (url.contains("/blank.htm")) {
                BackOfficeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT);
                        view.setLayoutParams(params);
                        root_view.removeView(newWebView);
                        root_view.addView(newWebView);
                    }
                });

                return false;
            }
            if (url.contains("about:blank")) {
                wv.loadUrl(view.getUrl());
                return true;
            }
            wv.loadUrl(url);
            return true; // return false if you want the load to continue
        }

    };
    WebChromeClient wv_chromClient = new WebChromeClient() {

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
//                String url = view.getUrl();
//                wv.loadUrl(url);
            newWebView = new WebView(getApplicationContext()); // pass
            newWebView.clearCache(true);
            newWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            WebSettings webSettings = newWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            newWebView.addJavascriptInterface(BackOfficeActivity.this.backOfficeInterface, "android");


            newWebView.setWebViewClient(newWV_WebClient);

            WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
            transport.setWebView(newWebView);
            resultMsg.sendToTarget();
//
            return true;

        }
    };

    private void hideKeyboard(){
        try{
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(BackOfficeActivity.this.getCurrentFocus().getWindowToken(), 0);
            }
        }catch(Exception e){}

    }

    @Override
    public void onBackPressed(){
        BackOfficeActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (newWebView != null){
                     if (newWebView.canGoBack()) {
                        newWebView.goBack();
//                        root_view.removeView(newWebView);
//                        newWebView = null;
                    }else{
                        root_view.removeView(newWebView);


                        if (newWebView.getUrl()== null){
                            if (wv.canGoBack()) {
                                String path = wv.getUrl();
                                WebBackForwardList list = wv.copyBackForwardList();
                                int index = list.getCurrentIndex();
                                for (int i = list.getSize() - 1; i >= 0; i--) {
                                    String historyurl = list.getItemAtIndex(i).getUrl();
                                    if (!historyurl.contains(path)) {
                                        wv.goBackOrForward(i - index);
                                        break;
                                    }
                                }
                            }
                        }
                        newWebView = null;
                    }
                }else{
                    if (wv.canGoBack()) {
                    String path = wv.getUrl();
                    WebBackForwardList list = wv.copyBackForwardList();
                    int index = list.getCurrentIndex();
                    for (int i = index ; i >= 0; i--) {
                        String historyurl = list.getItemAtIndex(i).getUrl();
                        if (!historyurl.contains(path)) {
                            wv.goBackOrForward(i - index);
                            break;
                        }
                    }
//                    if (wv.canGoBack()) {
//                        wv.goBack();
                    }else
                    {
                        BackOfficeActivity.this.finish();

                    }
                }

                /*if (wv.canGoBack()) {

                    progress.setVisibility(View.VISIBLE);
                    if (false && shouldGoBackTwice()){
                        wv.goBackOrForward(-2);
                    }
                    else
                        wv.goBack();
                }
                else
                {
                }*/
            }
        });
    }
}
