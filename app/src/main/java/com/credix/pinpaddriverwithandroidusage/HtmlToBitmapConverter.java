package com.credix.pinpaddriverwithandroidusage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class HtmlToBitmapConverter {

    public interface BitmapCallback {
        void onBitmapReady(Bitmap bitmap);
    }

    public static void convertHtmlToBitmap(Context context, String htmlContent, BitmapCallback callback) {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(() -> {
            WebView webView = new WebView(context);
            webView.getSettings().setJavaScriptEnabled(true);
            //webView.addJavascriptInterface(new WebAppBridge(), "android");

            webView.getSettings().setLoadWithOverviewMode(true);
            webView.getSettings().setUseWideViewPort(true);
            webView.setInitialScale(200); // משנה קנה מידה

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    new Handler().postDelayed(() -> {
                        // שינוי קנה מידה של התוכן עם JavaScript
                        //webView.evaluateJavascript("document.body.style.zoom = '1.5';", null);

                        webView.measure(
                                View.MeasureSpec.makeMeasureSpec(1350, View.MeasureSpec.EXACTLY), // מגדיל את הרוחב
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                        );
                        webView.layout(0, 0, webView.getMeasuredWidth(), webView.getMeasuredHeight());
                        webView.getSettings().setUseWideViewPort(true);
                        webView.getSettings().setLoadWithOverviewMode(true);
                        webView.getSettings().setBuiltInZoomControls(true);
                        webView.getSettings().setDisplayZoomControls(false);

                        if (webView.getMeasuredWidth() > 0 && webView.getMeasuredHeight() > 0) {
                            Bitmap bitmap = Bitmap.createBitmap(
                                    webView.getMeasuredWidth(),
                                    webView.getMeasuredHeight(),
                                    Bitmap.Config.ARGB_8888
                            );

                            Canvas canvas = new Canvas(bitmap);
                            webView.draw(canvas);

                            callback.onBitmapReady(bitmap);
                        } else {
                            callback.onBitmapReady(null);
                        }
                    }, 500);
                }
            });
            // הוספת meta tag כדי לוודא שהתוכן מתאים את עצמו
            String fullHtml = "<html><head>" +
                    "<meta name='viewport' content='width=device-width, initial-scale=1.0, maximum-scale=2.0, user-scalable=yes'>" +
                    "</head><body>" + htmlContent + "</body></html>";
            webView.loadDataWithBaseURL(null, fullHtml, "text/html", "UTF-8", null);
        });
    }
}
