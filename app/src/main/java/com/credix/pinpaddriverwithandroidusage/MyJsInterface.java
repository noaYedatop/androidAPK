package com.credix.pinpaddriverwithandroidusage;

import static com.credix.pinpaddriverwithandroidusage.Utils.openDrawer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.core.content.FileProvider;

import com.credix.pinpad.jni.PinPadAPI;
import com.credix.pinpad.jni.PinPadResponse;
import com.credix.pinpad.jni.PinPadSession;
import com.credix.pinpaddriverwithandroidusage.model.EnvPaymentItem;
import com.credix.pinpaddriverwithandroidusage.platforms.hardware.WeightManager;
import com.example.nexgolibrary.data.nexgo.device_config.NexgoDeviceConfig;
import com.example.nexgolibrary.data.nexgo.device_transaction.NexgoDeviceTransaction;
import com.example.nexgolibrary.domain.models.api.common.ClientDetails;
import com.example.nexgolibrary.utils.terminal.InitResCode;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.wisepay.pinpad.Api;

import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import jpos.events.DataEvent;
import jpos.events.DataListener;

public class MyJsInterface implements DataListener {

    private Context context;
    private BaseActivity baseActivity;   // תמיד קיים
    private MainActivity activity;       // null כשנקרא מ-BackOffice/Login

    public MyJsInterface(Context context, BaseActivity baseActivity) {
        this.context = context;
        this.baseActivity = baseActivity;
        this.activity = (baseActivity instanceof MainActivity) ? (MainActivity) baseActivity : null;
    }

    // ────────────────────────────────────────────────────────────
    // עזרים — BaseActivity (תמיד זמין)
    // ────────────────────────────────────────────────────────────
    private void runOnUiThread(Runnable r) { baseActivity.runOnUiThread(r); }
    private Context getApplicationContext() { return baseActivity.getApplicationContext(); }
    private BaseActivity.PLATFORMS getPlatform() { return baseActivity.getPlatform(); }
    private void loadWebView() { baseActivity.loadWebView(); }
    private void hideSoftKeyboard() { baseActivity.hideSoftKeyboard(null); }

    // ────────────────────────────────────────────────────────────
    // עזרים — MainActivity בלבד (nullable)
    // ────────────────────────────────────────────────────────────
    private void load(String html) { if (activity != null) activity.load(html); }
    private void registerBroadcastReceiver() { if (activity != null) activity.registerBroadcastReceiver(); }
    private void unregisterBroadcastReceiver() { if (activity != null) activity.unregisterBroadcastReceiver(); }
    private void startUrovoPayment(String amount, String approvalNumber, int pay_num, double pay_first, int ashray_f_credit, int moto, String cardNumber, String expDate, String cvv, String cardHolder_id) {
        if (activity != null) activity.startUrovoPayment(amount, approvalNumber, pay_num, pay_first, ashray_f_credit, moto, cardNumber, expDate, cvv, cardHolder_id);
    }
    private void repotUrovo() { if (activity != null) activity.repotUrovo(); }
    private void getUrovoStatus() { if (activity != null) activity.getUrovoStatus(); }

    // ────────────────────────────────────────────────────────────
    // JavaScript Interface
    // ────────────────────────────────────────────────────────────

    @JavascriptInterface
    public String getSerialNumber() { return "0"; }

    @JavascriptInterface
    public void disableKeyboard() {
        baseActivity.handler.post(() -> {
            InputMethodManager imm = (InputMethodManager) baseActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            WebView wv = (activity != null) ? activity.webView : null;
            if (imm != null && wv != null) {
                imm.hideSoftInputFromWindow(wv.getWindowToken(), 0);
                wv.clearFocus();
            }
        });
    }

    @JavascriptInterface
    public void enableKeyboard() {
        Log.d("enableKeyboard", "Trying to show keyboard...");
        if (activity != null) activity.handler.postDelayed(activity.enableKeyboardRunnable, 5);
    }

    @JavascriptInterface
    public void hideKeyboard() {
        Log.i("hideKeyboard", "hideKeyboard");
        hideSoftKeyboard();
    }

    @JavascriptInterface
    public void openCreditCardPayment() {
        if (activity != null) activity.focusKeyboard = false;
    }

    @JavascriptInterface
    public void closeCreditCartPayment() {
        if (activity != null) activity.focusKeyboard = true;
    }

    @JavascriptInterface
    public void height(int _height) {
        baseActivity.height = _height;
    }

    private final Runnable closeKeyboard = () -> baseActivity.hideSoftKeyboard(null);

    @JavascriptInterface
    public void printPrinterJsonStructure(String data, String barcode) {
        Log.d(MainActivity.TAG, data);
    }

    @JavascriptInterface
    public void open_drw() throws InterruptedException {
        runOnUiThread(() -> Utils.openDrawer(baseActivity.rtPrinter));
    }

    @JavascriptInterface
    public void print_invoice3(String s) {
        try {
            s = s.replace(
                    "<table style=\"font-family: Courier New;font-size: 9px;font-weight: bold;direction: rtl;max-width: 150px!important;width: 150px;margin-left: 20px;\">",
                    "<table style=\"font-family: Courier New;font-size: 15px;font-weight: bold;direction: rtl;width: 250px;margin-left: 5px;\">");
            print_invoice3(s, null, 1);
        } catch (JSONException e) { e.printStackTrace(); }
    }

    @JavascriptInterface
    public void print_invoice(final String s, final String barcode, int seconds) {}

    @JavascriptInterface
    public void print_invoice2(final String s, final String barcode, int seconds) {}

    @JavascriptInterface
    public void present_sett(String username, String type_present) {
        activity.username_for_path = username;
        activity.type_present_global = type_present;
        runOnUiThread(() -> {
            if ("1".equals(activity.type_present_global)) baseActivity.showPicture(activity.username_for_path);
            else if ("2".equals(activity.type_present_global)) baseActivity.showVideo(activity.username_for_path);
            else if ("3".equals(activity.type_present_global)) baseActivity.showLogo(activity.username_for_path);
        });
    }

    @JavascriptInterface
    public void presentProduct(String title, double amount, String price, int index, String totalAmount) {
        double price2 = Double.parseDouble(price);
        double totalAmount2 = Double.parseDouble(totalAmount);
        runOnUiThread(() -> {
            if ("3".equals(activity.type_present_global))
                baseActivity.startProductPresentation(activity.username_for_path, title, amount, price2, index, totalAmount2);
        });
    }

    @JavascriptInterface
    public void removeProd(int index, String totalAmount) {
        double totalAmount2 = Double.parseDouble(totalAmount);
        runOnUiThread(() -> {
            if ("3".equals(activity.type_present_global))
                baseActivity.removeProductFromPresentation(index, totalAmount2);
        });
        if (activity != null && activity.input_barcode != null) activity.input_barcode.requestFocus();
    }

    @JavascriptInterface
    public void plusCountProd(int index, double price, String totalAmount) {
        double totalAmount2 = Double.parseDouble(totalAmount);
        runOnUiThread(() -> { if ("3".equals(activity.type_present_global)) baseActivity.plusCountProdFromPresentation(index, price, totalAmount2); });
    }

    @JavascriptInterface
    public void minusCountProd(int index, double price, String totalAmount) {
        double totalAmount2 = Double.parseDouble(totalAmount);
        runOnUiThread(() -> { if ("3".equals(activity.type_present_global)) baseActivity.minusCountProdFromPresentation(index, price, totalAmount2); });
    }

    @JavascriptInterface
    public void changeCountProd(int index, int count, double totalprice, String totalAmount) {
        double totalAmount2 = Double.parseDouble(totalAmount);
        runOnUiThread(() -> { if ("3".equals(activity.type_present_global)) baseActivity.changeCountProdFromPresentation(index, count, totalprice, totalAmount2); });
    }

    @JavascriptInterface
    public void addAnachaProd(int index, int count, String totalprice, String anacha, String totalAmount) {
        double totalprice2 = Double.parseDouble(totalprice);
        double anacha2 = Double.parseDouble(anacha);
        double totalAmount2 = Double.parseDouble(totalAmount);
        runOnUiThread(() -> { if ("3".equals(activity.type_present_global)) baseActivity.addAnachaProdFromPresentation(index, count, totalprice2, anacha2, totalAmount2); });
    }

    @JavascriptInterface
    public void addDiscountProd(int index, String discountName, String discount, String totalAmount) {
        double discount2 = Double.parseDouble(discount);
        double totalAmount2 = Double.parseDouble(totalAmount);
        runOnUiThread(() -> { if ("3".equals(activity.type_present_global)) baseActivity.addDiscountProdFromPresentation(index, discountName, discount2, totalAmount2); });
    }

    @JavascriptInterface
    public void addAnachaGeneral(String totalprice) {
        double totalprice2 = Double.parseDouble(totalprice);
        runOnUiThread(() -> { if ("3".equals(activity.type_present_global)) baseActivity.addAnachaGeneralFromPresentation(totalprice2); });
    }

    @JavascriptInterface
    public void changeTotalPrice(double totalprice) {
        runOnUiThread(() -> { if ("3".equals(activity.type_present_global)) baseActivity.changeTotalPriceProduct(totalprice); });
    }

    @JavascriptInterface
    public void stopPresentProduct() {
        runOnUiThread(() -> { if ("3".equals(activity.type_present_global)) baseActivity.stopProducrpresentation(); });
    }

    @JavascriptInterface
    public void tara() { baseActivity.driver.tare(); }

    @JavascriptInterface
    public void clear_weight() { baseActivity.driver.zeroScale(); }

    @JavascriptInterface
    public double getChooseWeight(String typeOfWeight2) {
        return WeightManager.getWeight(getApplicationContext(), Integer.parseInt(typeOfWeight2), baseActivity.driver.getWeight());
    }

    @JavascriptInterface
    public void unitPrice(String unitPrice) {}

    @JavascriptInterface
    public void writeLogToConsole(String text) { Log.i("FROM JS: ", text); }

    @JavascriptInterface
    public void writelog_this(String journal, String text) {
        Context ctx = getApplicationContext();
        File directory = new File(ctx.getExternalFilesDir(null), "Logs");
        if (!directory.exists()) {
            if (!directory.mkdirs()) { Log.e("LogManager", "Failed to create: " + directory.getAbsolutePath()); return; }
        }
        File logFile = new File(directory, journal + ".txt");
        String currentTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        try {
            if (!logFile.exists()) logFile.createNewFile();
            FileWriter writer = new FileWriter(logFile, true);
            writer.write(currentTime + " : " + text + "\n");
            writer.close();
        } catch (IOException e) { Log.e("LogManager", "Error: ", e); }
    }

    private boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    // ────────────────────────────────────────────────────────────
    // MQTT
    // ────────────────────────────────────────────────────────────
    private MqttClient mqttClient;

    private boolean isMqttClientConnected() {
        try { if (mqttClient != null) return mqttClient.isConnected(); } catch (Throwable tr) { tr.printStackTrace(); }
        return false;
    }

    private MqttCallback mqttCallback = new MqttCallback() {
        @Override public void disconnected(MqttDisconnectResponse r) { System.out.println("disconnected " + r); }
        @Override public void mqttErrorOccurred(MqttException e) { System.out.println("mqttErrorOccurred " + e); }
        @Override public void messageArrived(String topic, MqttMessage message) throws Exception { System.out.println("Message published"); }
        @Override public void deliveryComplete(IMqttToken token) { System.out.println("deliveryComplete " + token); }
        @Override public void connectComplete(boolean reconnect, String serverURI) { System.out.println("connectComplete " + serverURI); }
        @Override public void authPacketArrived(int reasonCode, MqttProperties properties) { System.out.println("authPacketArrived " + properties); }
    };

    private boolean publishDataAndUpdateUI(String topic, byte[] data, int qos, String TicketID) {
        if (topic == null || topic.isEmpty()) return false;
        System.out.println("Publish topic " + topic + " qos " + qos + " ticket " + TicketID);
        return true;
    }

    // ────────────────────────────────────────────────────────────
    // Bitmap utilities
    // ────────────────────────────────────────────────────────────
    public Bitmap convertToMonochrome(Bitmap src) {
        Bitmap bmpMonochrome = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ALPHA_8);
        Canvas canvas = new Canvas(bmpMonochrome);
        ColorMatrix ma = new ColorMatrix(); ma.setSaturation(0);
        Paint paint = new Paint(); paint.setColorFilter(new ColorMatrixColorFilter(ma));
        canvas.drawBitmap(src, 0, 0, paint);
        return bmpMonochrome;
    }

    public void sendBitmapOverMQTT(byte[] imageData, String serverURI, String net) {
        try {
            mqttClient = new MqttClient(serverURI, net, new MemoryPersistence());
            MqttConnectionOptions options = new MqttConnectionOptions(); options.setCleanStart(true);
            mqttClient.connect(options);
            MqttMessage message = new MqttMessage(imageData); message.setQos(1);
            mqttClient.publish(net, message);
            mqttClient.disconnect();
        } catch (MqttException e) { e.printStackTrace(); }
    }

    public void printDataUsingOPOS(byte[] data) {
        POSPrinter printer = null;
        try {
            printer = new POSPrinter(); printer.open("POSPrinter"); printer.claim(1000);
            printer.setDeviceEnabled(true);
            printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, new String(data));
            printer.close();
        } catch (JposException e) { e.printStackTrace();
        } finally { if (printer != null) { try { printer.close(); } catch (JposException e) { e.printStackTrace(); } } }
    }

    private POSPrinter printer;

    private String convertBonToHtml(String s) {
        StringBuilder html = new StringBuilder("<html dir='rtl'><body style='text-align:right; font-size:18px;'>");
        for (String line : s.split(",")) html.append("<div>").append(line).append("</div>");
        html.append("</body></html>");
        return html.toString();
    }

    private void renderHtmlToBitmap(final String html, final Consumer<Bitmap> onDone) {
        new Handler(Looper.getMainLooper()).post(() -> {
            WebView webView = new WebView(context);
            webView.setVisibility(View.INVISIBLE);
            webView.setLayoutParams(new ViewGroup.LayoutParams(384, ViewGroup.LayoutParams.WRAP_CONTENT));
            webView.setWebViewClient(new WebViewClient() {
                public void onPageFinished(WebView view, String url) {
                    view.measure(View.MeasureSpec.makeMeasureSpec(384, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                    Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                    view.draw(new Canvas(bitmap));
                    onDone.accept(bitmap);
                }
            });
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        });
    }

    // ────────────────────────────────────────────────────────────
    // מדפסת מרוחקת ענן עם PRN
    // ────────────────────────────────────────────────────────────
    @JavascriptInterface
    public void print_invoice_bon2(String s, final String net, String server, String userName, String password) throws JSONException {
        try {
            mqttClient = new MqttClient(server, net, new MemoryPersistence());
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            connOpts.setCleanStart(true); connOpts.setSessionExpiryInterval(0xffffffffl);
            connOpts.setUserName(userName); connOpts.setPassword(password.getBytes(StandardCharsets.UTF_8));
            mqttClient.setCallback(mqttCallback); mqttClient.connect(connOpts);
            byte[] init = new byte[]{0x1B, 0x40};
            s = s.replace("\"", "").replace("[", "").replace("]", "");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int x = 0;
            for (String line : s.split(",")) {
                byte[] text = line.getBytes("CP862");
                int paddingLength = (40 - text.length) / 2;
                outputStream.write(init); outputStream.write(text);
                for (int i = 0; i < (x == 4 ? 20 : paddingLength); i++) outputStream.write(0x20);
                outputStream.write(0x0A); x++;
            }
            for (int i = 0; i < 7; i++) outputStream.write(0x0A);
            outputStream.write(new byte[]{0x1D, 0x56, 0x00});
            mqttClient.publish(net, outputStream.toByteArray(), 1, true);
            mqttClient.disconnect();
        } catch (Throwable tr) { tr.printStackTrace(); }
    }

    @JavascriptInterface
    public void print_invoice_bon_new_printer(String s, final String net, String server, String userName, String password) {
        try {
            SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            String deviceId = prefs.getString("device_id", null);
            if (deviceId == null || deviceId.isEmpty()) {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                if (deviceId == null || deviceId.isEmpty()) deviceId = java.util.UUID.randomUUID().toString();
                prefs.edit().putString("device_id", deviceId).apply();
            }
            MqttClient localMqttClient = new MqttClient(server, net + "-" + deviceId, new MemoryPersistence());
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            connOpts.setCleanStart(true); connOpts.setAutomaticReconnect(false);
            connOpts.setSessionExpiryInterval(0xffffffffL);
            connOpts.setUserName(userName); connOpts.setPassword(password.getBytes(StandardCharsets.UTF_8));
            localMqttClient.connect(connOpts);
            final String ss = s;
            ((Activity) context).runOnUiThread(() -> {
                String _s = ss.replace("max-width: 150px!important;width: 150px;margin-left: 20px;", "width:120%;margin: 0; text-align: center;");
                final String html = "<html><head><meta name=\"viewport\" content=\"width=120%, initial-scale=1.0\"><head>\n\t<style>\n\t\t   body{\n\t\t\twidth: 120%;\n\t\t}\n\t\tbody > table{\nfont-size: 50px !important;width: 120% !important;\t\t}\n\t</style>\n</head><body>" + _s + "\n</body></html>";
                WebView webView = new WebView(context);
                webView.setVisibility(View.GONE);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        view.postDelayed(() -> {
                            try {
                                int viewWidth = 576;
                                view.measure(View.MeasureSpec.makeMeasureSpec(viewWidth, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(30000, View.MeasureSpec.AT_MOST));
                                view.layout(0, 0, viewWidth, view.getMeasuredHeight());
                                Bitmap bitmap = Bitmap.createBitmap(viewWidth, view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                                view.draw(new Canvas(bitmap));
                                MqttMessage message = new MqttMessage(convertBitmapToEscPos(bitmap));
                                message.setQos(1); message.setRetained(false);
                                localMqttClient.publish(net, message);
                                if (localMqttClient.isConnected()) localMqttClient.disconnect();
                                ((ViewGroup) ((Activity) context).findViewById(android.R.id.content)).removeView(webView);
                            } catch (Exception e) { e.printStackTrace(); }
                        }, 500);
                    }
                });
                ViewGroup root = ((Activity) context).findViewById(android.R.id.content);
                root.addView(webView);
                webView.getSettings().setUseWideViewPort(true);
                webView.getSettings().setLoadWithOverviewMode(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setInitialScale(100);
                webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
            });
        } catch (Exception e) { e.printStackTrace(); }
    }

    public Bitmap toMonochrome(Bitmap original) {
        Bitmap bwBitmap = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bwBitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix(); cm.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(original, 0, 0, paint);
        return bwBitmap;
    }

    public byte[] convertBitmapToEscPos(Bitmap bitmap) {
        int width = bitmap.getWidth(), height = bitmap.getHeight();
        Bitmap monoBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(monoBitmap);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix(); cm.setSaturation(0);
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        int bytesPerRow = (width + 7) / 8;
        byte[] imageData = new byte[bytesPerRow * height];
        int index = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x += 8) {
                byte b = 0;
                for (int i = 0; i < 8; i++) {
                    if (x + i < width) {
                        int pixel = monoBitmap.getPixel(x + i, y);
                        int gray = (((pixel >> 16) & 0xFF) + ((pixel >> 8) & 0xFF) + (pixel & 0xFF)) / 3;
                        if (gray < 128) b |= (1 << (7 - i));
                    }
                }
                imageData[index++] = b;
            }
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(0x1D); output.write('v'); output.write('0'); output.write(0x00);
        output.write(bytesPerRow % 256); output.write(bytesPerRow / 256);
        output.write(height % 256); output.write(height / 256);
        try {
            output.write(imageData);
            for (int i = 0; i < 5; i++) output.write(0x0A);
            output.write(new byte[]{0x1D, 0x56, 0x00});
        } catch (IOException e) { e.printStackTrace(); }
        return output.toByteArray();
    }

    public Bitmap barcodeBitmap = null;

    @JavascriptInterface
    public void setDontOpenCashDrawer(boolean value) {}

    @JavascriptInterface
    public boolean getDontOpenCashDrawer() { return true; }

    // ────────────────────────────────────────────────────────────
    // print_invoice3
    // ────────────────────────────────────────────────────────────
    @JavascriptInterface
    public void print_invoice3(final String s, final String barcode, int seconds) throws JSONException {
        if (activity != null) activity.running = 0;
        final String htmlTemp = s;
        runOnUiThread(() -> {
            if (baseActivity.addedReceiptWebView == null) loadWebView();
            String _htmlTemp = baseActivity.driver.prepareHtmlForPrint(htmlTemp, barcode);
            int font_size = baseActivity.driver.getPrintFontSize();

            if (getPlatform() == BaseActivity.PLATFORMS.IMIN || getPlatform() == BaseActivity.PLATFORMS.SUNMI) {
                if (barcode != null && !barcode.equals("0")) baseActivity.barcode = barcode;
            } else if (getPlatform() == BaseActivity.PLATFORMS.UROVO) {
                if (!"0".equals(barcode)) baseActivity.barcode = barcode;
            }

            String maxWidth = (getPlatform() == BaseActivity.PLATFORMS.IMIN) ? "max-width:650px !important;" : "max-width:unset !important;";
            String width = (getPlatform() == BaseActivity.PLATFORMS.IMIN) ? "width: 97% !important;" : "width: 93% !important;";
            String margins = (getPlatform() == BaseActivity.PLATFORMS.IMIN) ? "" : "margin-left: 15px !important;;margin-right: 15px !important;;";

            load("<html style=\"height: fit-content;\">" +
                    "<head>\n\t<style>\n\t\t   body{\nmargin: unset;\t\t\tpadding: 0 !important;\n\t\t}\n\t\tbody > table{\n" +
                    maxWidth + " font-size: " + font_size + "px !important;" + width + margins +
                    "\t\t}\n\t</style>\n" +
                    "\t<script>\n console.log('height = Running 1');" +
                    "\t(function () {\n  console.log('height = Running2'); " +
                    "\tsetTimeout(function(){  console.log('height = Running3'); " +
                    "\t if (android != undefined) {\n " +
                    "\t android.height(document.getElementsByTagName(\"table\")[0].clientHeight+" + baseActivity.driver.getAddedHeight() + " );\n" +
                    "\t}\n},100);\t})();\n\t </script>\n</head>" +
                    "<body>" + _htmlTemp + "\n</body></html>");
        });
    }

    @JavascriptInterface
    public void onData(String value) { Log.d(MainActivity.TAG, "************ " + value + "***********"); }

    @JavascriptInterface
    public String swipeCard(Object input) { return "nothing"; }

    JSONObject credixResult = new JSONObject();
    private NexgoDeviceTransaction nexgoDeviceTransaction;
    private NexgoLibraryEventsListener nexgoLibraryEventsListener;

    @JavascriptInterface
    public String initNexgo(String userName, String password, String terminal) {
        try {
            ClientDetails clientDetails = new ClientDetails(userName, password, terminal);
            NexgoDeviceConfig nexgoDeviceConfig = new NexgoDeviceConfig(getApplicationContext());
            InitResCode result = NexgoDeviceHelper.initDeviceConfigSync(nexgoDeviceConfig, clientDetails);
            return "{\"returnValue\":\"" + result.name() + "\"}";
        } catch (Exception e) {
            Log.e("NexgoInit", "Error", e);
            return "{\"returnValue\":\"INIT_FAILED\"}";
        }
    }

    @JavascriptInterface
    public String startNexgoPayment(String userName, String password, String terminal, String kupaNum,
                                    String amount, String currency, String tranType, String creditTerms,
                                    String paymentsNumber, String firstPayment, String fixedPayment,
                                    String isManual, String credit, String cardNum, String exDate,
                                    String cvv, String id, String masof) {
        if (activity == null) return "{\"error\":\"not MainActivity\"}";
        registerBroadcastReceiver();
        AtomicReference<String> resultMessage = new AtomicReference<>("");
        nexgoLibraryEventsListener = new NexgoLibraryEventsListener(activity);
        NexgoDeviceConfig nexgoDeviceConfig = new NexgoDeviceConfig(getApplicationContext());
        nexgoDeviceTransaction = new NexgoDeviceTransaction(activity);
        activity.nexgoJavaWrapper = new NexgoJavaWrapper(nexgoDeviceConfig, nexgoDeviceTransaction);
        CompletableFuture<Void> future = activity.nexgoJavaWrapper.startNexgoPayment(
                        userName, password, terminal, kupaNum, amount, currency,
                        tranType, creditTerms, paymentsNumber, firstPayment, fixedPayment,
                        isManual, credit, cardNum, exDate, cvv, id)
                .thenAccept(result -> {
                    Log.d("NexgoDebug", "Transaction result: " + result);
                    unregisterBroadcastReceiver();
                    resultMessage.set(result);
                })
                .exceptionally(e -> {
                    Log.e("NexgoDebug", "Error", e);
                    unregisterBroadcastReceiver();
                    resultMessage.set("שגיאה במהלך תהליך התשלום");
                    return null;
                });
        try { future.get(); } catch (Exception e) { Log.e("NexgoDebug", "Error waiting", e); }
        return resultMessage.get();
    }

    @JavascriptInterface
    public String pelelivyCredit(String itra) {
        if (activity == null) return "{\"error\":\"not MainActivity\"}";
        Log.e(MainActivity.TAG, "pelelivyCredit");
        EnvPaymentItem paymentItem;

        if (getPlatform() == BaseActivity.PLATFORMS.UROVO) {
            paymentItem = new Gson().fromJson(itra, EnvPaymentItem.class);
            int pay_num = ((String) paymentItem.pay_num).isEmpty() ? 0 : Integer.parseInt((String) paymentItem.pay_num);
            int ashray_f_credit = ((String) paymentItem.ashray_f_credit).isEmpty() ? 0 : Integer.parseInt((String) paymentItem.ashray_f_credit);
            int moto = ((String) paymentItem.moto).isEmpty() ? 0 : Integer.parseInt((String) paymentItem.moto);
            double pay_first = ((String) paymentItem.pay_num).isEmpty() ? 0 : Double.parseDouble((String) paymentItem.pay_first);
            startUrovoPayment(Double.toString(paymentItem.amount), paymentItem.aproval_num_f, pay_num, pay_first, ashray_f_credit, moto, "", "", "", "");
            return "{\"code\":1}";
        }

        if (activity.inProcess) return "{\"error\": \"fail get response\"}";

        try {
            long dif = System.currentTimeMillis() - activity.lastCreditPayTime;
            if (dif < 1000 || activity.dll != null) {
                Log.e(MainActivity.TAG, "too dense calls");
                return credixResult.toString();
            }
            credixResult = new JSONObject();
            try { paymentItem = new Gson().fromJson(itra, EnvPaymentItem.class); }
            catch (JsonSyntaxException ex) { Log.e(MainActivity.TAG, "No Input"); return ""; }

            if (paymentItem.IpayPassword == null || paymentItem.IpayUserName == null) {
                credixResult = new JSONObject();
                credixResult.put("error", "fail get response");
                return credixResult.toString();
            }

            activity.dll = new PinPadAPI();
            activity.inProcess = true;
            activity.dll.open(paymentItem.IpayCom, paymentItem.IpayUserName, paymentItem.IpayPassword);

            JSONObject params = new JSONObject();
            String transactionType = "CHARGE";
            double amt = paymentItem.amount;
            if (amt < 0) { transactionType = "CREDIT"; amt = Math.abs(amt); }

            String creditTerms = "REGULAR_CREDIT";
            double numberOfPayments = 1, pay_num = 0, firstPaymentAmount = -1, ashray_f_credit = 0;

            if (paymentItem.pay_num != null) {
                pay_num = ((String) paymentItem.pay_num).isEmpty() ? 1 : Double.parseDouble((String) paymentItem.pay_num);
            }
            if (paymentItem.pay_first != null && !((String) paymentItem.pay_first).isEmpty()) {
                firstPaymentAmount = Double.parseDouble((String) paymentItem.pay_first);
            }
            if (paymentItem.ashray_f_credit != null && !((String) paymentItem.ashray_f_credit).isEmpty()) {
                ashray_f_credit = Double.parseDouble((String) paymentItem.ashray_f_credit);
            }

            double paymentAmount = paymentItem.amount;
            if (pay_num > 1) {
                creditTerms = "PAYMENTS"; numberOfPayments = pay_num;
                if (firstPaymentAmount > 0) {
                    paymentAmount = (amt - firstPaymentAmount) / (pay_num - 1);
                    params.put("firstPaymentAmount", firstPaymentAmount); params.put("paymentAmount", paymentAmount);
                } else {
                    firstPaymentAmount = Math.round(amt / pay_num);
                    paymentAmount = (amt - firstPaymentAmount) / (pay_num - 1);
                    params.put("firstPaymentAmount", firstPaymentAmount); params.put("paymentAmount", paymentAmount);
                }
                numberOfPayments -= 1; params.put("numberOfPayments", numberOfPayments);
            }
            if (ashray_f_credit > 0) {
                creditTerms = "FIXED_INSTALMENT_CREDIT"; numberOfPayments = ashray_f_credit;
                firstPaymentAmount = 0; paymentAmount = 0;
                params.put("firstPaymentAmount", firstPaymentAmount); params.put("numberOfPayments", numberOfPayments);
            }

            JSONObject data = new JSONObject();
            params.put("amount", amt); params.put("creditTerms", creditTerms);
            params.put("currency", "ILS"); params.put("printVoucher", false);
            params.put("requestType", "QUERY_EXECUTION_IN_ACCORDANCE_WITH_TERMINAL");
            params.put("transactionType", transactionType);
            data.put("params", params);

            PinPadSession requestSession = new PinPadSession();
            int ret = activity.dll.sendRequest(requestSession, "transaction", data.toString());

            for (int i = 0; i < 60; i++) {
                ret = activity.dll.isResponseReady(requestSession, 1000);
                if (ret > 0) break;
            }

            if (ret == 0) {
                PinPadSession cancelSession = new PinPadSession();
                ret = activity.dll.sendRequest(cancelSession, "cancel", null);
                ret = activity.dll.isResponseReady(cancelSession, 1000);
                if (ret > 0) {
                    PinPadResponse response = new PinPadResponse();
                    activity.dll.getResponse(cancelSession, response, ret);
                    activity.inProcess = false;
                    JSONObject js = new JSONObject(response.get());
                    if (js.optInt("code") == 60000) {
                        credixResult.put("error", "עסקה בוטלה"); credixResult.put("code", "");
                        JSONObject inner = new JSONObject();
                        inner.put("message", "עסקה בוטלה"); inner.put("code", js.optString(""));
                        credixResult.put("data", inner);
                        activity.dll.close(); activity.dll = null;
                        return credixResult.toString();
                    }
                    activity.dll.close(); activity.dll = null;
                    activity.lastCreditPayTime = System.currentTimeMillis();
                    activity.focusKeyboard = true;
                    return response.get();
                }
                ret = activity.dll.isResponseReady(requestSession, 1000);
            }

            if (ret > 0) {
                PinPadResponse response = new PinPadResponse();
                activity.dll.getResponse(requestSession, response, ret);
                activity.inProcess = false;
                try {
                    JSONObject js = new JSONObject(response.get());
                    if (js.optInt("code") > 60000) {
                        credixResult.put("error", "fail get response"); credixResult.put("code", js.optInt("code"));
                        JSONObject inner = new JSONObject();
                        if (js.has("data")) {
                            inner.put("message", js.optJSONObject("data").optString("message") + " " +
                                    js.optJSONObject("data").optString("solek", "") + " " +
                                    js.optJSONObject("data").optString("solekTelNum", ""));
                            inner.put("code", "");
                        } else { inner.put("message", js.optString("msg")); inner.put("code", js.optString("1")); }
                        credixResult.put("data", inner);
                        activity.dll.close(); activity.dll = null;
                        activity.lastCreditPayTime = System.currentTimeMillis();
                        activity.focusKeyboard = true;
                        return credixResult.toString();
                    }
                } catch (Exception ex) {
                    activity.dll.close(); activity.dll = null;
                    Log.i(MainActivity.TAG, "crashed"); ex.printStackTrace();
                }
                activity.dll.close(); activity.dll = null;
                activity.lastCreditPayTime = System.currentTimeMillis();
                activity.focusKeyboard = true;
                return response.get();
            } else if (ret < 0) {
                activity.inProcess = false;
                credixResult.put("error", "fail get response"); credixResult.put("code", "60002");
                JSONObject inner = new JSONObject(); inner.put("message", "ארעה שגיאה"); inner.put("code", -1);
                credixResult.put("data", inner);
                activity.dll.close(); activity.dll = null;
                activity.lastCreditPayTime = System.currentTimeMillis();
                return credixResult.toString();
            }

            activity.lastCreditPayTime = System.currentTimeMillis();
            activity.dll.close(); activity.dll = null;
        } catch (Exception e) { activity.inProcess = false; e.printStackTrace(); }
        activity.lastCreditPayTime = System.currentTimeMillis();
        activity.inProcess = false;
        activity.dll = null;
        return "";
    }

    @JavascriptInterface
    public void getUrovoStatusForCreditUsers() { getUrovoStatus(); }

    @JavascriptInterface
    public String openPinPad(String com, String wisepayCode) {
        Api apiDll = new Api();
        String address = com.length() > 2 ? com : String.format("COM{0}", com);
        byte[] response = new byte[10];
        apiDll.Version(response, 10); apiDll.SetDebug(true);
        int ret1 = apiDll.Open2(address, wisepayCode);
        if (ret1 != 0) return "{\"error\": \"connection to wisepay failed\"}";
        return "ok";
    }

    @JavascriptInterface
    public String getUrovoTransactionsReport() { repotUrovo(); return ""; }

    @JavascriptInterface
    public String getTransactionsReport(String com, String wisepayCode) {
        if (activity == null) return "";
        if (BuildConfig.DOMAIN.equals("liv1.yedatop") || BuildConfig.DOMAIN.equals("liv.yedatop.com") ||
                BuildConfig.DOMAIN.equals("peleliv2.yedatop") || BuildConfig.DOMAIN.equals("pos.pelecash.co.il") ||
                BuildConfig.DOMAIN.equals("liv2.yedatop")) return "";
        activity.apiDll = new Api();
        String address = com.length() > 2 ? com : String.format("COM{0}", com);
        activity.apiDll.SetDebug(true);
        if (activity.apiDll.Open2(address, wisepayCode) != 0) return "{\"error\": \"connection towisepay failed\"}";
        int result = activity.apiDll.DepositTxns2("REPORT_TYPE_XML", false, "deposit - context");
        String myRes = ReadReply(result, activity.apiDll);
        if ("error".equals(myRes)) return "{\"error\": \"fail send request\"}";
        if (activity.apiDll.Close() != 0) Log.i(MainActivity.TAG, "Failed to Got Response pinpad");
        return myRes;
    }

    @Override
    public void dataOccurred(DataEvent dataEvent) {}

    class MyResponse {
        public String response;
        public MyResponse() { response = null; }
    }

    private String ReadReply(int result, Api jniApi) {
        if (result != 0) return "";
        int ret = 0;
        while (ret == 0) { ret = jniApi.IsResponseReady(1000); if (ret > 0) break; }
        if (ret > 0) {
            byte[] nativeResponse = new byte[ret];
            jniApi.GetResponse(nativeResponse, ret);
            String response = new String(nativeResponse, 0, ret - 1);
            System.out.println(response);
            return response;
        }
        return "error";
    }

    // for liv
    @JavascriptInterface
    public String pelelivyCredit(String amount, String username, String password, int pay_num, double pay_first,
                                 int ashray_f_credit, int moto, String com, String approvalNumber, String motoPanEntry) {
        return startPayCredit_both(amount, username, password, pay_num, pay_first, ashray_f_credit, moto, com, approvalNumber, motoPanEntry);
    }

    // for android
    @JavascriptInterface
    public String startPayCredit(String amount, String username, String password, int pay_num, double pay_first,
                                 int ashray_f_credit, String com, String approvalNumber, String motoPanEntry) {
        return startPayCredit_both(amount, username, password, pay_num, pay_first, ashray_f_credit, 0, com, approvalNumber, motoPanEntry);
    }

    @JavascriptInterface
    public String startPayCredit_both(String amount, String username, String password, int pay_num, double pay_first,
                                      int ashray_f_credit, int moto, String com, String approvalNumber, String motoPanEntry) {
        if (getPlatform() == BaseActivity.PLATFORMS.UROVO) {
            startUrovoPayment(amount, approvalNumber, pay_num, pay_first, ashray_f_credit, moto, "", "", "", "");
        }
        String transactionType = "TRANSACTION_TYPE_DEBIT";
        double amt = Double.parseDouble(amount);
        if (amt < 0) { transactionType = "TRANSACTION_TYPE_REFUND"; amt = Math.abs(amt); }
        int amountAsAgorot = (int) Math.round(amt * 100);
        String creditTerms = "CREDIT_TERMS_REGULAR";
        int numberOfPayments = 1;
        double firstPaymentAmount = 0, paymentAmount = 0;
        if (pay_num > 1) {
            creditTerms = "CREDIT_TERMS_PAYMENTS"; numberOfPayments = pay_num; firstPaymentAmount = pay_first;
            if (pay_first < 1) {
                firstPaymentAmount = Math.round(firstPaymentAmount * 100.0) / 100.0;
                paymentAmount = Math.round(amt / pay_num);
            } else {
                paymentAmount = Math.round((amt - firstPaymentAmount) / (pay_num - 1));
                firstPaymentAmount = amt - paymentAmount * (pay_num - 1);
            }
            numberOfPayments -= 1;
        }
        if (ashray_f_credit > 0) {
            creditTerms = "CREDIT_TERMS_CREDIT"; numberOfPayments = ashray_f_credit;
            firstPaymentAmount = 0; paymentAmount = 0;
        }
        String ctx = motoPanEntry != null ? "moto-context" : "sale - context";
        int firstPaymentAmountAsInt = (int) (firstPaymentAmount * 100);
        if (activity == null) return "{\"error\":\"not MainActivity\"}";
        activity.apiDll = new Api();
        int result = activity.apiDll.Txn(transactionType, amountAsAgorot, "ILS", creditTerms, numberOfPayments,
                firstPaymentAmountAsInt, (int) (paymentAmount * 100), numberOfPayments,
                "INDEX_PAYMENT_NONE", 0, approvalNumber, motoPanEntry, false, ctx, null);
        String myRes = ReadReply(result, activity.apiDll);
        if ("error".equals(myRes)) return "{\"error\": \"fail send request\"}";
        if (activity.apiDll.Close() != 0) Log.i(MainActivity.TAG, "Failed to Got Response pinpad");
        return myRes;
    }

    private Boolean GetResponse(MyResponse response, Boolean allowCancel) {
        if (activity == null) return false;
        activity.apiDll = new Api();
        MainActivity._isCancelTransaction = false;
        Boolean error = false;
        int ret;
        do {
            ret = activity.apiDll.IsResponseReady(250);
            if (ret < 0) { Log.i(MainActivity.TAG, "IsResponseReady failed: " + ret); error = true; break; }
            if (ret == 0) { Log.i(MainActivity.TAG, "Pending" + (allowCancel ? " (ctrl+c to cancel)" : "")); continue; }
            BigInteger replyLenght = BigInteger.valueOf(ret);
            activity.apiDll.GetResponse(replyLenght.toByteArray(), ret);
            response.response = replyLenght.toString();
            Log.i(MainActivity.TAG, response.response);
            break;
        } while (!(MainActivity._isCancelTransaction && allowCancel));
        return !error;
    }

    @JavascriptInterface
    public String startPayCredit(String amount, String username, String password, int pay_num, double pay_first,
                                 int ashray_f_credit, String com, String approvalNumber) {
        PinPadAPI dll = new PinPadAPI();
        String address = com.length() > 2 ? com : String.format("COM%s", com);
        int res = dll.open(address, username, password);
        if (res != 0) return "{\"error\": \"fail com " + res + "\" }";
        return "{\"error\": \"error\"}";
    }

    @JavascriptInterface
    public String start_credix(String amount, String username, String password, int pay_num, double pay_first,
                               int ashray_f_credit, String com, String approvalNumber) {
        PinPadAPI dll = new PinPadAPI();
        String address = com.length() > 2 ? com : String.format("COM%s", com);
        int res = dll.open(address, username, password);
        if (res != 0) return "{\"error\": \"fail com " + res + "\" }";
        return "{\"error\": \"error\"}";
    }

    @JavascriptInterface
    public void swipeCard() { Log.d(MainActivity.TAG, "swipeCard"); }

    @JavascriptInterface
    public void onLogoLoaded(final String logoUrl) { Log.d(MainActivity.TAG, "on logo loaded: " + logoUrl); }
}