package com.credix.pinpaddriverwithandroidusage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaRouter;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.credix.pinpaddriverwithandroidusage.WebInterface.BackOfficeInterface;
import com.google.firebase.analytics.FirebaseAnalytics;

public class LoginActivity extends BaseActivity {
    private WebView wv,receiptWebView;
    private BackOfficeInterface backOfficeInterface;
    private String url;
    private View reloadView;
    public View progress;
    private FirebaseAnalytics mFirebaseAnalytics;
    private Display displays[];
    private MediaRouter mMediaRouter;


    private static final int MY_CAMERA_REQUEST_CODE = 100;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.hideSystemUI(getWindow());

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Utils.printInputLanguages(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main_login);

        setContentView(R.layout.activity_main);


        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }



        progress = findViewById(R.id.progress);

        reloadView = findViewById(R.id.reload);
        wv = (WebView) this.findViewById(R.id.fullscreen_content);

        Intent intent = getIntent();
        url = intent.getStringExtra("URL");



        wv.requestFocus(View.FOCUS_DOWN);

        wv.getSettings().setJavaScriptEnabled(true);

        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv.getSettings().setSupportMultipleWindows(true);
        wv.setFocusable(true);
        wv.setFocusableInTouchMode(true);





        wv.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                reloadView.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
//                redirect(url);
            }



            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.endsWith("modules/stock/cashbox_fe"))
                {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();


                }else
                {
                    view.loadUrl(url);
                }

                return true;
            }
        });

        wv.loadUrl(url);

    }


}
