package com.credix.pinpaddriverwithandroidusage;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.device.ScanManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.credix.pinpad.jni.PinPadAPI;
import com.credix.pinpaddriverwithandroidusage.platforms.hardware.AdytechWeightReader;
import com.credix.pinpaddriverwithandroidusage.receiver.UsbDeviceReceiver;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.rt.printerlibrary.bean.Position;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.cmd.PinFactory;
import com.rt.printerlibrary.enumerate.BarcodeStringPosition;
import com.rt.printerlibrary.enumerate.BarcodeType;
import com.rt.printerlibrary.enumerate.BmpPrintMode;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.enumerate.EscBarcodePrintOritention;
import com.rt.printerlibrary.exception.SdkException;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.setting.BarcodeSetting;
import com.rt.printerlibrary.setting.BitmapSetting;
import com.rt.printerlibrary.setting.CommonSetting;
import com.wisepay.pinpad.Api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Driver;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

//import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, TextWatcher, ZXingScannerView.ResultHandler {
    BaseApp baseApp;
    public static final String TAG = "JS LOG";
    public static Boolean _isCancelTransaction = false;
    private static final int REQUEST_PERMISSION_MULTIPLE = 911;
    public long lastCreditPayTime = 0;

    private boolean useFocus = false;

    private String scannedText = "";


    private FirebaseAnalytics mFirebaseAnalytics;
    public static String DOMAIN = BuildConfig.DOMAIN;
//    public static String DOMAIN = "liv.yedatop";
//      public static String DOMAIN = Bu;


    /*SCREENS*/
//    public static final String MAIN_PATH = "https://kupa.yedatop.com";
//    public static final String MAIN_PATH = "https://office1.yedatop.com";
    public static final String MAIN_PATH = "https://" + DOMAIN;
    private static final String[] BLOCKED_SCREENS = new String[]{"modules/stock/cashbox_fe/"};


    private Bitmap logo = null;
    private boolean logoDownloaded = false;
    private boolean jsInterfaceLoaded = false;
    Pattern p = Pattern.compile("(src=)\"(.*?)\"");


    //Views
    private Button btnFunctions, btnPrint, btnDummyInvoice;
    private EditText etBarcode;
    private RecyclerView rv;

    public Boolean focusKeyboard = true;

    private AdapterRows adapter;
//    private ArrayList<ArrayList<String>> data = new ArrayList<>();


    private final Handler mHideHandler = new Handler();

    private View mContentView;

    private View mControlsView;
    public WebView webView;

    private boolean mVisible;

    private int fontSize = 25; // was 30

    private UsbDeviceReceiver mUsbReceiver;

    public int running;
    public EditText input_barcode;
    private boolean isProcessing = false; // Flag to prevent overlapping processing
    private boolean isScanning = false;
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;

    public static final int INPUT_FILE_REQUEST_CODE = 1;
    private View reloadView;
    private View reloadViewTXT;
    private ImageView img;
    public boolean inProcess = false;
    public PinPadAPI dll;
    public Api apiDll;

    private Vector<Driver> mDrivers;

    private ScanManager mScanManager = null;

    private Api wisepayDll;

    private MyPresentation mPresentation;
    private List<Product> productList; // Assuming this is your list of products
    private VideoView videoView;
    private ScrollView scrollView;
    private TableLayout tableLayout;
    public String type_present_global;
    public String username_for_path;

    static {
        try {
            System.loadLibrary("serialPort");
            Log.d("SERIAL_SO", "loaded serialPort");
        } catch (Throwable e) {
            Log.e("SERIAL_SO", "failed to load serialPort", e);
        }
    }

    private boolean urovo_power_On;
    private ZXingScannerView barcode_scanner;


    //    public Bitmap generateBarcode(String barcodeData, int width, int height) {
//        Map<EncodeHintType, Object> hints = new HashMap<>();
//        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
//        try {
//            Code128Writer writer = new Code128Writer();
//            BitMatrix bitMatrix = writer.encode(barcodeData, BarcodeFormat.CODE_128, width, height, hints);
//
//            int[] pixels = new int[width * height];
//            for (int y = 0; y < height; y++) {
//                int offset = y * width;
//                for (int x = 0; x < width; x++) {
//                    pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
//                }
//            }
//
//            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
//
//            return bitmap;
//
//        } catch (WriterException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            // Handle the home button press here
            Toast.makeText(this, "Home button pressed", Toast.LENGTH_SHORT).show();
            // Return true to indicate that you have handled the event
            return true;
        }
        // For other key presses, call the super method
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Utils.hideSystemUI(getWindow());
    }

    @Override
    protected void onResume() {
        super.onResume();
        inProcess = false;
        if (!isExcludedDomain() && input_barcode != null)
            input_barcode.requestFocus();
        dll = null;
        lastCreditPayTime = 0;

        Log.e("INIT", "before scan");
        initScan();
        Log.e("INIT", "after scan");

/*
        mScanManager = new ScanManager();

        urovo_power_On = mScanManager.openScanner();//mScanManager.getScannerState();
        if (!urovo_power_On) {
            urovo_power_On = mScanManager.openScanner();
        }
        if(urovo_power_On){
            mScanManager.startDecode();
            mScanManager.setTriggerMode(Triggering.CONTINUOUS);


            //mScanManager.addReadActionListener(readerListener);
        }*/
    }

    private void initScan() {
        try {
            mScanManager = new ScanManager();
            boolean powerOn = mScanManager.getScannerState();
        } catch (Exception e) {
        }


    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    @JavascriptInterface
    public void writeloggg(String tag, String message) {
        try {
            File logsDir = new File(getFilesDir(), "logs");
            if (!logsDir.exists()) {
                logsDir.mkdirs();
            }

            String today = dateFormat.format(new Date());
            File logFile = new File(logsDir, "log_" + today + ".txt");

            String time = timeFormat.format(new Date());
            String line = "[" + time + "][" + tag + "] " + message + "\n";

            FileWriter writer = new FileWriter(logFile, true);
            writer.append(line);
            writer.close();
        } catch (Exception ignored) {
        }
    }


    /*
    UROVO function
     */
    @JavascriptInterface
    public void repotUrovo() {

        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        final MainActivity activity = this;
        try {

            json.put("jsonrpc", "2.0");
            json.put("method", "doPeriodic");
            json.put("id", 1);
            jsonArray.put("ashrait");
            jsonArray.put("xml");

            json.put("params", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String f_reqeustString = json.toString();

        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(30, TimeUnit.SECONDS).writeTimeout(10, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();

                RequestBody body = RequestBody.create(JSON, f_reqeustString); // new
                Request request = new Request.Builder().url("http://127.0.0.1:8081/SPICy").post(body).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
//                    response.body().close();
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("RESULT ===>", result.toString());
                    if (result == null) {
                        result = new JSONObject();
                        result.put("success", false);
                    } else {


                        result = result.optJSONObject("result");

                        JSONObject resp = new JSONObject();
                        resp.put("code", 60000);
                        JSONObject xml = new JSONObject();
                        xml.put("report", result.optString("report"));
                        resp.put("data", xml);

                        final String zz = resp.toString();
                        Log.i("zz ==>   ", zz);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String load = "javascript:(function(data) {" + "angular.element(document.querySelector('#credit_appr')).scope().getUrovoTransactionsReportResult(JSON.stringify(data));" + "})(" + zz + ")";
                                webView.loadUrl(load);
//                                webView.loadUrl("javascript:angular.element(document.querySelector('#credit_appr')).scope().getUrovoTransactionsReportResult(JSON.stringify(data))");
                            }
                        });

                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    try {
                        JSONObject resp = new JSONObject();
                        resp.put("code", 60001);
                        JSONObject xml = new JSONObject();
                        resp.put("data", "");
                        final String zz = resp.toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String load = "javascript:(function(data) {" + "angular.element(document.querySelector('#credit_appr')).scope().getUrovoTransactionsReportResult(JSON.stringify(data));" + "})(" + zz + ")";
                                webView.loadUrl(load);
//                                webView.loadUrl("javascript:angular.element(document.querySelector('#credit_appr')).scope().getUrovoTransactionsReportResult(JSON.stringify(data))");
                            }
                        });
                    } catch (Exception ee) {
                    }


                }
            }
        });
        worker.start();
    }


    @JavascriptInterface
    public void getUrovoStatus() {

        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        final MainActivity activity = this;
        try {

            json.put("jsonrpc", "2.0");
            json.put("method", "getStatus");
            json.put("id", 1);
            jsonArray.put("ashrait");
            json.put("params", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String f_reqeustString = json.toString();
        ;

        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();

                RequestBody body = RequestBody.create(JSON, f_reqeustString); // new
                Request request = new Request.Builder().url("http://127.0.0.1:8081/SPICy").post(body).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
//                    response.body().close();
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("RESULT STATUS ===>", result.toString());
                    if (result == null) {
                        result = new JSONObject();
                        result.put("success", false);
                    } else {
                        if (result.optString("result").indexOf("ashraitReady") > -1) {
                            Log.i(TAG, "READY TO CHARGE " + result.optString("result"));
                        } else {

                            Intent it = new Intent("intent.my.action");
                            it.setComponent(new ComponentName("il.co.modularity.agamento", "il.co.modularity.agamento.views.main.MainActivity"));
                            it.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent launch_back = new Intent(activity, MainActivity.class);

                                    launch_back.setComponent(new ComponentName("com.yedatop.maytal", "com.credix.pinpaddriverwithandroidusage.MainActivity"));

                                    launch_back.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    getApplicationContext().startActivity(launch_back);
                                }
                            }, 2000);
                            getApplicationContext().startActivity(it);


                            Log.i(TAG, "NOT READY TO CHARGE " + result.optString("result"));
                        }
                    }


                } catch (IOException | JSONException e) {
                    Intent it = new Intent("intent.my.action");
                    it.setComponent(new ComponentName("il.co.modularity.agamento", "il.co.modularity.agamento.views.main.MainActivity"));
                    it.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent launch_back = new Intent(activity, MainActivity.class);

                            launch_back.setComponent(new ComponentName("com.yedatop.maytal", "com.credix.pinpaddriverwithandroidusage.MainActivity"));

                            launch_back.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                            // launch_back.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            getApplicationContext().startActivity(launch_back);

                        }
                    }, 2000);
                    try {
                        getApplicationContext().startActivity(it);

                    } catch (Exception ex) {
                    }
                    // e.printStackTrace();
                }
            }
        });
        worker.start();
        //startActivity(intent);
    }

    @JavascriptInterface
    public void startUrovoPayment(String amount, String approvalNumber, int pay_num, double pay_first, int ashray_f_credit, int moto, String cardNumber, String expDate, String cvv, String cardHolder_id) {

        int tranType = 1; // Regular
        double amt = Double.parseDouble(amount);
        if (amt < 0) {
            tranType = 53; // Regund
            amt = Math.abs(amt);
        }
//            int amountAsAgorot = (int)(amt * 100);
        int amountAsAgorot = (int) (Math.round(amt * 100));
        int posEntryMode = 40;
        if (moto != 0) {
            posEntryMode = moto;
        }
        String pattern = "MM/dd/yyyy HH:mm:ss";
        DateFormat df = new SimpleDateFormat(pattern);
        Date currentTime = Calendar.getInstance().getTime();
        String todayAsString = df.format(currentTime);

        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject data = new JSONObject();
        String reqeustString = "{}";
        String paymentType = "CREDIT_TERMS_REGULAR"; //CREDIT_TERMS_IMMEDIATE //CREDIT_TERMS_CREDIT\n"
        int first_payment = 0;

        final MainActivity activity = this;
        try {
            data.put("amount", amountAsAgorot);
            data.put("vuid", todayAsString);
            /*
1 - Regular
3 - Forced Transaction
6 - Cash Back
7 - Cash
30 - Balance
53 - Refund
             */
            data.put("tranType", tranType);
            data.put("creditTerms", 1);
            if (pay_num > 1) {
                data.put("payments", pay_num);
                first_payment = (int) (Math.round(pay_first * 100));
                paymentType = "CREDIT_TERMS_PAYMENTS";

                data.put("firstPaymentAmount", first_payment);
                data.put("otherPaymentAmount", (amountAsAgorot - first_payment) / (pay_num - 1));
            }
            if (ashray_f_credit > 1) {
                paymentType = "CREDIT_TERMS_CREDIT";
                data.put("creditPayments", ashray_f_credit);

            }

            if (cardNumber.isEmpty()) {
                data.put("cardNumber", cardNumber);
                data.put("expDate", expDate);
                data.put("cvv", cvv);
                data.put("cardHolderID", cardHolder_id);
            }
//            data.put("payments",5);
            data.put("tranCode", 1);
            data.put("posEntryMode", posEntryMode);
            data.put("currency", "376");
            json.put("jsonrpc", "2.0");
            json.put("method", "doTransaction");
            json.put("id", approvalNumber);
            jsonArray.put("ashrait");
            jsonArray.put(data);
            json.put("params", jsonArray);
            reqeustString = json.toString();
            Log.d("reqeustString#######===", reqeustString);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String f_reqeustString = reqeustString;
        Intent it = new Intent("intent.my.action");
        it.setComponent(new ComponentName("il.co.modularity.agamento", "il.co.modularity.agamento.views.main.MainActivity"));
        it.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        final String f_payment_type = paymentType;
        final int f_number_payment = pay_num;
        final double f_first_payment = first_payment;


        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).build();

                RequestBody body = RequestBody.create(JSON, f_reqeustString); // new
                Request request = new Request.Builder().url("http://127.0.0.1:8081/SPICy").post(body).build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
//                    response.body().close();
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("RESULT ===>", result.toString());
                    if (result == null) {
                        result = new JSONObject();
                        result.put("success", false);
                    } else {
                        result = result.optJSONObject("result");
                        result.put("success", true);
                    }
                    final JSONObject urovo_result = MainActivity.this.buildPaymentResponse(result, f_payment_type, f_first_payment, f_number_payment);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:angular.element(document.querySelector('#credit_appr')).scope().end_ashray('" + urovo_result.toString() + "')");
                        }
                    });
                    Intent launch_back = new Intent(activity, MainActivity.class);
                    it.setComponent(new ComponentName("com.yedatop.maytal", "com.credix.pinpaddriverwithandroidusage.MainActivity"));

                    launch_back.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    getApplicationContext().startActivity(launch_back);

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }
//                Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                Intent intent = new Intent(activity, MainActivity.class);
//                startActivity(intent);
            }
        });
        worker.start();
        //startActivity(intent);
        getApplicationContext().startActivity(it);
    }


    public String getManpik(int issuer, int manpic) {
        if (manpic == 0) return "TOURIST";

        switch (issuer) {
            case 0:
                return "TOURIST";
            case 1:
                return "ISRACARD";
            case 2: // VISA_CAL
                if (manpic == 2) return "VISA_CAL";
                if (manpic == 6) return "LEUMICARD";
            case 3:
                return "DINERS";
            case 4:
                return "AMEX";
            case 5:
                return "ISRACARD";
            case 6: //JCB
            case 7: // DISCOVER
            case 8: // MAESTRO
                return "TOURIST";

        }
        return "OTHER";
    }

    private String getTransactionTypeValue(int tt) {
        switch (tt) {

            case 0:
                //TT_Informative = 0
                //Informative - Registeres for general knowledge - עסקה אינפורמטיבית

            case 1://       TT_Regular = 1
//                Regular - עסקת חיוב רגילה
                return "עסקת חיוב";
            case 2://    TT_Unload = 2
                //Unload - עסקת פריקה לכרטיס נטען

            case 3:      //   TT_Forced = 3
                return "עסקה מאולצת";
//                Forced - Transaction was forced to be approved offline - עסקה מאולצת

            case 4://TT_CashBack = 6
                return "עסקה עם מזומן";

            case 7: //TT_Cash = 7
                return "עסקת מזומן";
//                Cash - עסקת מזומן

            case 11: //TT_StandingOrder = 11
//                Standing Order - הוראת קבע, אתחול או תשלום תור”ן

            case 30:  //  TT_BalanceCheck = 30
                return "בירור יתרה בכרטיס נטען";
//                Balance Check - בירור יתרה לכרטיס נטען

            case 53: //TT_Refund = 53
                return "עסקת זיכוי";
//                Refund - עסקת זיכוי

            case 55://TT_Charge = 55

//                Charge - עסקת טעינה לכרטיס נטען
        }
        return "עסקה לא ברורה";
    }

    private String getTransactionType(int tt) {
        switch (tt) {

            case 0:
                //TT_Informative = 0
                //Informative - Registeres for general knowledge - עסקה אינפורמטיבית

            case 1://       TT_Regular = 1
//                Regular - עסקת חיוב רגילה
                return "עסקת חיוב";
            case 2://    TT_Unload = 2
                //Unload - עסקת פריקה לכרטיס נטען

            case 3:      //   TT_Forced = 3
                return "עסקה מאולצת";
//                Forced - Transaction was forced to be approved offline - עסקה מאולצת

            case 4://TT_CashBack = 6
                return "עסקה עם מזומן";

            case 7: //TT_Cash = 7
                return "עסקת מזומן";
//                Cash - עסקת מזומן

            case 11: //TT_StandingOrder = 11
//                Standing Order - הוראת קבע, אתחול או תשלום תור”ן

            case 30:  //  TT_BalanceCheck = 30
                return "בירור יתרה בכרטיס נטען";
//                Balance Check - בירור יתרה לכרטיס נטען

            case 53: //TT_Refund = 53
                return "TRANSACTION_TYPE_REFUND";
//                Refund - עסקת זיכוי

            case 55://TT_Charge = 55

//                Charge - עסקת טעינה לכרטיס נטען
        }
        return "עסקה לא ברורה";
    }

    public JSONObject buildPaymentResponse(JSONObject result, String type, double f_payment, int n_payment) throws JSONException {
        //angular.element(document.querySelector('#credit_appr')).scope().end_ashray()
        JSONObject response = new JSONObject();

        try {


            if (!result.optBoolean("success")) {
                JSONObject e = new JSONObject();
                e.put("connection towisepay failed", "true");
                response.put("error", e);
            } else {
                //60002 - not complete
                //60018 - not performed
                //60000 - success
                int code = result.optInt("statusCode", 60002);
                int re = result.optInt("authCodeManpik");

                response.put("code", code == 0 ? 60000 : 60018);
                switch (code) {
                    case 0:
                        response.put("code", 60000);
                        break;
//                    case 2:
//                    case 4:
//                    case 6:
//                    case 8:
                    default:
                        response.put("code", 60002);
                }


                JSONObject details = new JSONObject();
                //CREDIT_TERMS_REGULAR //CREDIT_TERMS_IMMEDIATE //CREDIT_TERMS_CREDIT
                details.put("cardNumber", result.optString("cardNumber", "-1"));
                details.put("expDate", result.optString("expDate", "-1"));
                details.put("amount", result.optDouble("amount"));
                details.put("voucherNum", result.optString("rrn", "-1"));
                details.put("issuerAuthNum", result.optString("issuerAuthNum", "-1"));

                details.put("cardBrandValue", result.optInt("mutag", -1));

                int mutag = result.optInt("mutag", -1);
                int manpik = result.optInt("manpik", -1);

                details.put("cardIssuer", getManpik(mutag, manpik));

                details.put("creditTerms", type);

                // details.put("panEntryMode","PAN_ENTRY_MODE_MOTO");
                details.put("posEntryMode", result.optInt("posEntryMode"));
                details.put("creditTermsValue", getTransactionType(result.optInt("tranType")));
                details.put("transactionType", getTransactionType(result.optInt("j")));
                if (type == "CREDIT_TERMS_PAYMENTS") {
                    details.put("firstPayment", f_payment);
                    details.put("noPayments", n_payment);
                }
                if (type == "CREDIT_TERMS_CREDIT") {
                }

                JSONObject res = new JSONObject();
                res.put("txnDetails", details);
                response.put("data", res);

//                response.put("txnDetails", details);
            }
            return response;
        } catch (Exception e) {
            return response;
        }
    }

    private final Runnable beforeText = new Runnable() {
        @Override
        public void run() {
//            input_barcode.requestFocus();
            // input_barcode.clearFocus();
            //webView.loadUrl("javascript:setBarcode('"+s.toString()+"')");
            String input = input_barcode.getText().toString();

            webView.loadUrl("javascript:(function setBarcode(text){\n" + "debugger;" + " if (document.activeElement.id == 'search_prod') {\n" + "$('#search_prod')[0].value = text;\n" + "$('#search_prod')[0].click();\n" + "    }\n" + "else if ($('.keyboard').css('display') != 'none' || document.activeElement.id =='search_keyboard2'){ \n" + "setTimeout(function(){" + "console.log('yayy '+text);" + "$('search_keyboard2').value = text ;\n" + "$('.keyboard_result')[0].value = text;\n" + "},1);" + "\n" + " }else if (document.querySelector('.text.zicuy_txt.zicuy_txt3.border2') != undefined && $('.shovarzicuy').css('display') == 'block'){ \n" + "document.querySelector('.text.zicuy_txt.zicuy_txt3.border2').value = text}\n" + "else if (document.activeElement.id == '' || document.activeElement.id == ' '){ \n" + "$('#search_prod')[0].value = text;\n" + "$('#search_prod')[0].click();\n" + "    }\n" + "    })('" + input.toString() + "')");

//                webView.loadUrl("javascript:setBarcode('"+s.toString()+"')");

            input_barcode.setText("");
            input_barcode.addTextChangedListener(MainActivity.this);
            Log.i("beforeTextChanged", input.toString());

        }
    };

    @Override
    public void afterTextChanged(Editable s) {

        Log.d("BarcodeInput", "Input finalized: " + s.toString());
        Log.i("afterTextChanged", s.toString());
    }

    private String s;
//    @Override
//    public void beforeTextChangedracel(final CharSequence s, int start, int count, int after) {
//        input_barcode.removeTextChangedListener(this);
//        this.s = s.toString();
//        handler.postDelayed(beforeText, 350);// for all platform
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
//    }

    @Override
    public void beforeTextChanged(final CharSequence s, int start, int count, int after) {
        if (input_barcode == null || input_barcode.getWindowToken() == null) {
            return; // מונע קריסה אם ה-View לא קיים
        }

        //start for android with scanner rachel
        Log.d("BarcodeInput", "Input finalized: " + s.toString());
        Log.i("beforeTextChanged", s.toString());

        input_barcode.removeTextChangedListener(this);
        this.s = s.toString();
        handler.postDelayed(beforeText, 350);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
        imm.hideSoftInputFromWindow(input_barcode.getWindowToken(), 0);
        //end for android with scanner

//        start for liv APOLO, not working in android with scanner rachel
//        input_barcode.clearFocus();
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        View currentFocus = getWindow().getCurrentFocus();
//        if (currentFocus != null && currentFocus.getWindowToken() != null) {
//            imm.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
//        }
////        imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
//        hideSoftKeyboard(getWindow().getCurrentFocus());
//        input_barcode.removeTextChangedListener(this);
//        this.s = s.toString();
//
//         handler.postDelayed(beforeText, 350);//350}
//
//        end for liv, not working in android with scanner
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.d("BarcodeInput", "Input changed: " + s.toString());
        etBarcode.setCursorVisible(false);
        input_barcode.setCursorVisible(false);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       /* try {
            mScanManager.removeReadActionListener(readerListener);
        }catch(Exception e){}
    */
        unregisterBroadcastReceiver();

        // ביטול הרישום של ה-NexgoListener
        if (nexgoListener != null) {
            nexgoListener.unregisterListener();
        }
        AdytechWeightReader.release();
    }

    public NexgoJavaWrapper nexgoJavaWrapper;

    private NexgoLibraryEventsListener nexgoListener;
    private TextView transactionTextView, pinTextView;

    private void nexgoCancelTransaction(NexgoJavaWrapper nexgoJavaWrapper) {
        nexgoJavaWrapper.cancelTransaction();
    }

    private NexgoDeviceController nexgoDeviceController;
    private String deviceId;  // 👈 משתנה גלובלי

    private boolean isExcludedDomain() {
        return "liv.yedatop.com".equals(BuildConfig.DOMAIN) ||
                "liv1.yedatop".equals(BuildConfig.DOMAIN) ||
                "peleliv2.yedatop".equals(BuildConfig.DOMAIN) ||
                "pos.pelecash.co.il".equals(BuildConfig.DOMAIN) ||
                "liv2.yedatop".equals(BuildConfig.DOMAIN) ||
                "mayafood.yedatop".equals(BuildConfig.DOMAIN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("DOMAIN_TEST", "DOMAIN=" + DOMAIN);
        if (!"liv.yedatop.com".equals(BuildConfig.DOMAIN) && !"liv1.yedatop".equals(BuildConfig.DOMAIN) && !"mayafood.yedatop".equals(BuildConfig.DOMAIN) && !"peleliv2.yedatop".equals(BuildConfig.DOMAIN) && !"pos.pelecash.co.il".equals(BuildConfig.DOMAIN) && !"liv2.yedatop".equals(BuildConfig.DOMAIN)) {
            updateLocale(this);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.hideSystemUI(getWindow());
        super.onCreate(savedInstanceState);
        //
        // .init();
        //setupTechnicianAccess();

        try {
            ScaleManager.getInstance();
        } catch (Throwable e) {
            Log.e("SCALE", "ScaleManager failed - continuing without scale", e);
        }

        if ("android.yedatop.com".equals(BuildConfig.DOMAIN) || "office1.yedatop.com".equals(BuildConfig.DOMAIN)) {
            deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        } else {
            //לשים בהערה אם לא נקסגו וכן אנדרואיד
            nexgoDeviceController = new NexgoDeviceController(this); // יש לך context פה for pelecard
            deviceId = nexgoDeviceController.getDeviceSerial();//for android "0". pelecard getserial
        }
        Log.i("DEVICE_ID", deviceId);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
        //WindowManager.LayoutParams.FLAG_SECURE);

        Utils.printInputLanguages(this);
        //for debug in chrome

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().sendUnsentReports();

        baseApp = (BaseApp) getApplication();
        if (!"liv.yedatop.com".equals(BuildConfig.DOMAIN) && !"liv1.yedatop".equals(BuildConfig.DOMAIN) && !"mayafood.yedatop".equals(BuildConfig.DOMAIN) && !"peleliv2.yedatop".equals(BuildConfig.DOMAIN) && !"pos.pelecash.co.il".equals(BuildConfig.DOMAIN) && !"liv2.yedatop".equals(BuildConfig.DOMAIN)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        if (!isExcludedDomain() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && input_barcode != null) {
            WebView.enableSlowWholeDocumentDraw();
            input_barcode.addTextChangedListener(this);
        } else {
            // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
        }
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        Log.e("BOOT", "DOMAIN=" + DOMAIN + " BuildConfig.DOMAIN=" + BuildConfig.DOMAIN);
        Log.e("BOOT", "layout set, before findViewById");
        setContentView(R.layout.activity_main);

        input_barcode = findViewById(R.id.input_barcode);
        Log.e("BOOT", "initWebView: webView=" + (webView != null));
        input_barcode.addTextChangedListener(this);
        input_barcode.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // אם איבד פוקוס – נחזיר אותו תוך זמן קצר
                new Handler().postDelayed(() -> {
                    input_barcode.requestFocus();
                }, 100);
            }
        });
        barcode_scanner = (ZXingScannerView) findViewById(R.id.barcode_scanner);


        img = (ImageView) findViewById(R.id.img);
        reloadView = findViewById(R.id.reload);
        reloadViewTXT = findViewById(R.id.reload_txt);
        reloadView.setOnClickListener(this);
        receiptWebView = findViewById(R.id.webReciept);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);
//noa


        btnFunctions = findViewById(R.id.btnFunctions);
        btnPrint = findViewById(R.id.btnTestPrint);
        btnDummyInvoice = findViewById(R.id.btnDummyInvoice);
        etBarcode = findViewById(R.id.etBarcode);

        btnFunctions.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btnDummyInvoice.setOnClickListener(this);
        findViewById(R.id.btnDummyPayment).setOnClickListener(this);

        //connectImin();//IMIN!!!!! remove for adytech!!!
        initWebView();
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            Log.e("DEVICE_OVERLAY", "FORCE SHOW FROM ONCREATE");
//            addDeviceIdNativeOverlay();
//        }, 800);

        if (!isExcludedDomain()) { // DANGOT            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            setListenerToRootView();
        } else {
            if ("liv.yedatop.com".equals(BuildConfig.DOMAIN) || "liv1.yedatop".equals(BuildConfig.DOMAIN) || "peleliv2.yedatop".equals(BuildConfig.DOMAIN) || "pos.pelecash.co.il".equals(BuildConfig.DOMAIN) || "liv2.yedatop".equals(BuildConfig.DOMAIN) || "mayafood.yedatop".equals(BuildConfig.DOMAIN)) {
                // קריאה לפונקציה (דוגמא)

                unregisterBroadcastReceiver();
                transactionTextView = findViewById(R.id.transaction_message_text);
                nexgoListener = new NexgoLibraryEventsListener(this);
            }
            // if (getPlatform() == PLATFORMS.UROVO) {
            // getUrovoStatus();
            // }
        }
        checkStoragePermission();
        disableKeyboard();
        hasAllPermissions();


        Log.e("INIT", "before printer");
        initPrinter();
        Log.e("INIT", "after printer");
        Log.e("INIT", "before registerPrinter");
        registerPrinter(this);//rrr
        Log.e("INIT", "after registerPrinter");

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("zicuy")) {
            String zicuynum = intent.getStringExtra("num_zicuy");
            // Assuming you determine the URL based on the received "zicuy" value
            String url = getUrlBasedOnZicuy(zicuynum);
            // Now load this URL in a WebView or handle it as needed
            loadUrl(url);
        }
    }

    private final Handler overlayHandler = new Handler(Looper.getMainLooper());
    private Runnable showDeviceOverlayRunnable;

    private View deviceIdOverlay;

    private void removeDeviceIdNativeOverlay() {
        try {
            if (deviceIdOverlay != null && deviceIdOverlay.getParent() != null) {
                ((ViewGroup) deviceIdOverlay.getParent()).removeView(deviceIdOverlay);
            }
            deviceIdOverlay = null;
        } catch (Exception ignored) {
        }
    }

    private Bitmap generateQR(String data, int size) {
        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size);

            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    private void showDeviceIdCopyDialog(String id) {
        LinearLayout layout = new LinearLayout(MainActivity.this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 20, 30, 10);
        layout.setGravity(Gravity.CENTER_HORIZONTAL);

        EditText editText = new EditText(MainActivity.this);
        editText.setText(id);
        editText.setSelectAllOnFocus(true);
        editText.setTextIsSelectable(true);
        editText.setSingleLine(true);
        editText.setTextSize(18);

        ImageView qrView = new ImageView(MainActivity.this);
        Bitmap qr = generateQR(id, 350);
        if (qr != null) {
            qrView.setImageBitmap(qr);
            qrView.setPadding(0, 20, 0, 10);
            layout.addView(qrView, new LinearLayout.LayoutParams(350, 350));
        }

        layout.addView(editText);

        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("מזהה קופה").setView(layout).setPositiveButton("העתק", (d, which) -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            ClipData clip = ClipData.newPlainText("Device ID", id);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(MainActivity.this, "הועתק באנדרואיד", Toast.LENGTH_SHORT).show();
        }).setNegativeButton("סגור", null).create();

        dialog.setOnShowListener(d -> {
            editText.requestFocus();
            editText.selectAll();
        });

        dialog.show();
    }

    private void addDeviceIdNativeOverlay() {
        try {
            String idToShow = deviceId;

            if (idToShow == null || idToShow.trim().isEmpty()) {
                idToShow = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            }

            if (deviceIdOverlay != null) {
                try {
                    ((ViewGroup) deviceIdOverlay.getParent()).removeView(deviceIdOverlay);
                } catch (Exception ignored) {
                }
                deviceIdOverlay = null;
            }

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setGravity(Gravity.CENTER);
            card.setPadding(16, 12, 16, 12); // היה 24/18
            card.setElevation(99999f);

            GradientDrawable bg = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, new int[]{Color.parseColor("#1E3A8A"), Color.parseColor("#2563EB"), Color.parseColor("#06B6D4")});
            bg.setAlpha(230); // קצת שקוף
            bg.setCornerRadius(24);
            card.setBackground(bg);

            TextView title = new TextView(this);
            title.setText("מזהה קופה לטכנאי");
            title.setTextColor(Color.WHITE);
            title.setTextSize(15);
            title.setTypeface(null, Typeface.BOLD);
            title.setGravity(Gravity.CENTER);
            card.addView(title);

            ImageView qrView = new ImageView(this);
            Bitmap qr = generateQR(idToShow, 180);

            if (qr != null) {
                qrView.setImageBitmap(qr);
                qrView.setBackgroundColor(Color.WHITE);
                qrView.setPadding(8, 8, 8, 8);
                qrView.setElevation(8f);

                LinearLayout.LayoutParams qrParams = new LinearLayout.LayoutParams(180, 180);
                qrParams.topMargin = 8;
                qrView.setLayoutParams(qrParams);

                card.addView(qrView);
            }

            TextView idText = new TextView(this);
            idText.setText(idToShow);
            idText.setTextColor(Color.WHITE);
            idText.setTextSize(14);
            idText.setGravity(Gravity.CENTER);
            idText.setPadding(0, 6, 0, 0);
            idText.setTypeface(null, Typeface.BOLD);
            card.addView(idText);

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            params.bottomMargin = 45;

            addContentView(card, params);

            deviceIdOverlay = card;
            card.bringToFront();

            //final String finalId = idToShow;
            //card.setOnClickListener(v -> showDeviceIdCopyDialog(finalId));

        } catch (Exception e) {
            Log.e("DEVICE_OVERLAY", "failed to show overlay", e);
            Toast.makeText(this, "שגיאה בהצגת מזהה", Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap generateBarcode(String data, int width, int height) {
        try {
            BitMatrix bitMatrix = new Code128Writer().encode(data, BarcodeFormat.CODE_128, width, height);

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }

            return bitmap;
        } catch (Exception e) {
            Log.e("BARCODE", "failed to generate barcode", e);
            return null;
        }
    }

    private int techClickCount = 0;
    private long lastTechClickTime = 0;

    @Override
    public boolean dispatchTouchEvent(android.view.MotionEvent ev) {
        if (!isOnLoginScreen) {
            return super.dispatchTouchEvent(ev);
        }
        if (ev.getAction() == android.view.MotionEvent.ACTION_UP) {

            long now = System.currentTimeMillis();

            // אם עברו יותר מ-2 שניות בין לחיצות - איפוס
            if (now - lastTechClickTime > 2000) {
                techClickCount = 0;
            }

            lastTechClickTime = now;
            techClickCount++;

//            if (techClickCount >= 7) {
//                techClickCount = 0;
//                showDeviceIdDialog();
//                return true;
//            }
        }

        return super.dispatchTouchEvent(ev);
    }

    private void showDeviceIdDialog() {
        String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_device_id, null);

        TextView deviceIdText = view.findViewById(R.id.deviceIdText);
        Button copyButton = view.findViewById(R.id.copyButton);

        deviceIdText.setText(deviceId);

        AlertDialog dialog = new AlertDialog.Builder(this).setView(view).create();

        copyButton.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            ClipData clip = ClipData.newPlainText("Device ID", deviceId);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, "המזהה הועתק", Toast.LENGTH_SHORT).show();
        });

        dialog.show();
    }

    private final BroadcastReceiver commonReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("NexgoDebug", "BroadcastReceiver received an event!");

            if (intent != null && "common-payment-event".equals(intent.getAction())) {
                Log.i("NexgoDebug", "Received broadcast in commonReceiver");
                Bundle extras = intent.getExtras();
                if (extras == null || extras.isEmpty()) {
                    Log.w("NexgoDebug", "No extras in intent");
                    return;
                }

                // הדפסה של כל המפתחות והערכים
                for (String key : extras.keySet()) {
                    Object value = extras.get(key);
                    String type = (value != null) ? value.getClass().getName() : "null";
                    Log.d("NexgoDebug", "Extra key: " + key + " | type: " + type + " | value: " + String.valueOf(value));
                }
                // לוגים על הודעות טרנזקציה
                String message = intent.getStringExtra("transaction_message");
                if (message != null) {
                    Log.i("NexgoDebug", "Transaction message received: " + message);
                    updateTransactionMessage(message);
                } else {
                    Log.w("NexgoDebug", "No transaction message received");
                }

//                // לוגים על אירועי PIN
//                String pinEvent = intent.getStringExtra("pin_event");
//                if (pinEvent != null) {
//                    Log.i("NexgoDebug", "Pin event received: " + pinEvent);
//                    updatePinEvent(pinEvent);
//                } else {
//                    Log.w("NexgoDebug", "No pin event received");
//                }
            } else {
                Log.e("NexgoDebug", "Received an unexpected broadcast action");
            }
        }
    };

    private boolean isReceiverRegistered = false;

    public void registerBroadcastReceiver() {
        if (!isReceiverRegistered) {
            Log.d("NexgoDebug", "Registering BroadcastReceiver...");
            IntentFilter filter = new IntentFilter("common-payment-event");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                isReceiverRegistered = true;
                registerReceiver(commonReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
            } else {
                isReceiverRegistered = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    registerReceiver(commonReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
                }
            }
            Log.d("NexgoDebug", "BroadcastReceiver registered.");
        } else {
            Log.d("NexgoDebug", "BroadcastReceiver already registered.");
        }
    }

    public void unregisterBroadcastReceiver() {
        if (transactionTextView != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //ViewGroup parent = (ViewGroup) transactionTextView.getParent();
                    //ViewGroup parent = (ViewGroup) findViewById(R.id.fullscreen_content); // מזהה את ה-Layout שמכיל את הכל
                    LinearLayout transactionLayout = findViewById(R.id.transaction_layout);
                    if (transactionLayout != null) {
                        ViewGroup parent = (ViewGroup) transactionLayout.getParent();
                        if (parent != null) {
                            transactionLayout.setVisibility(View.GONE); // ✅ מסתיר במקום להסיר
                            Log.d("NexgoDebug", "transaction_layout removed from parent");
                        } else {
                            transactionLayout.setVisibility(View.GONE); //
                            Log.d("NexgoDebug", "transaction_layout set to GONE");
                        }

//                    if (parent != null) {
//                        //parent.removeView(transactionTextView);
//                        parent.removeView(transactionLayout); // ✅ מסיר את כל ה-Layout
//
//                        Log.d("NexgoDebug", "transactionTextView removed from parent");
//                    } else {
//                        transactionTextView.setVisibility(View.GONE);
//                        Log.d("NexgoDebug", "transactionTextView set to GONE");
//                    }
                    }
                }
            });
            Log.d("NexgoDebug", "transactionTextView set to GONE");
        }

        if (isReceiverRegistered) {
            Log.d("NexgoDebug", "Unregistering BroadcastReceiver...");
            unregisterReceiver(commonReceiver);
            isReceiverRegistered = false;
            Log.d("NexgoDebug", "BroadcastReceiver unregistered.");
        } else {
            Log.d("NexgoDebug", "BroadcastReceiver was not registered.");
        }
    }

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;

    ClickableSpan clickableSpan = new ClickableSpan() {
        @Override
        public void onClick(@NonNull View widget) {
            nexgoJavaWrapper.cancelTransaction();
        }

        @Override
        public void updateDrawState(@NonNull TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(true); // קו תחתי כמו קישור
            ds.setColor(Color.RED); // צביעת הטקסט בצבע כחול כדי שיהיה ברור שזה לינק
        }
    };

    private void updateTransactionMessage(String message) {
        if (transactionTextView == null) {
            Log.e("NexgoDebug", "transactionTextView is NULL!");
            return;
        } else {
            Log.d("NexgoDebug", "transactionTextView visibility: " + transactionTextView.getVisibility());
            transactionTextView.bringToFront();
            transactionTextView.requestLayout();
            runOnUiThread(() -> {
                // קבלת ה-Layout, ImageView ו-TextView
                LinearLayout transactionLayout = findViewById(R.id.transaction_layout);
                //ImageView transactionIcon = findViewById(R.id.transaction_icon);
                TextView transactionTextView = findViewById(R.id.transaction_message_text);

                if (transactionLayout.getParent() == null) {
                    ViewGroup parent = findViewById(R.id.fullscreen_content);
                    parent.addView(transactionLayout);
                }
                String topText = message;
                if (BuildConfig.DOMAIN == "liv2.yedatop" || BuildConfig.DOMAIN == "peleliv2.yedatop" || BuildConfig.DOMAIN == "pos.pelecash.co.il") {
                    if ("הכנס / הצג / העבר כרטיס".equals(topText)) {
                        topText = "הכנס / הצמד / העבר";
                    } else if ("כרטיס זוהה, המתן לאישור".equals(topText)) {
                        topText = "מבצע חיוב...";
//                    EditText amountInput = findViewById(R.id.transaction_amount_input);
//                    CircularProgressIndicator spinner = findViewById(R.id.loadingSpinner);
//                    FrameLayout spinnerContainer = findViewById(R.id.spinnerContainer);
//
//                        //כשאת רוצה להציג טעינה:
//                    spinner.setIndicatorColor(
//                            ContextCompat.getColor(this, R.color.spinner_color1),
//                            ContextCompat.getColor(this, R.color.spinner_color2)
//                    );


                    } else if ("הכנס / הצג כרטיס".equals(topText)) {
                        topText = "הכנס / הצמד / העבר";
                    } else {
                        topText = message;
                    }
                } else {
                    topText = message;
                }
                // הגדרת טקסט

//                String linkText = "ביטול עסקה";
//                SpannableString spannableString = new SpannableString(topText + "\n\n" + linkText);
//
//                // הוספת לינק על "ביטול עסקה"
//                ClickableSpan clickableSpan = new ClickableSpan() {
//                    @Override
//                    public void onClick(@NonNull View widget) {
//                        nexgoCancelTransaction(nexgoJavaWrapper);
//                    }
//
//                    @Override
//                    public void updateDrawState(@NonNull TextPaint ds) {
//                        super.updateDrawState(ds);
//                        ds.setUnderlineText(true);
//                        ds.setColor(Color.RED);
//                    }
//                };
//
//                // הוספת clickableSpan רק על "ביטול עסקה"
//                spannableString.setSpan(clickableSpan, topText.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                GradientDrawable drawable = new GradientDrawable();
                drawable.setShape(GradientDrawable.RECTANGLE);
                drawable.setCornerRadius(12f); // 32dp פינות עגולות
                if (BuildConfig.DOMAIN == "liv2.yedatop" || BuildConfig.DOMAIN == "peleliv2.yedatop" || BuildConfig.DOMAIN == "pos.pelecash.co.il") {
                    transactionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    drawable.setColor(Color.parseColor("#097999"));
                    transactionTextView.setGravity(Gravity.CENTER);
                    transactionTextView.setTextColor(Color.parseColor("#B5D1FF"));
                    transactionTextView.setPadding(0, 100, 0, 150);
                    if ("מבצע חיוב...".equals(topText)) {
                        transactionTextView.setPadding(0, 10, 0, 300);
                    }
                } else {
                    transactionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                    drawable.setColor(Color.parseColor("#F44336")); // צבע אדום
                    transactionTextView.setTextColor(Color.BLACK);
                    transactionTextView.setPadding(0, 100, 0, 50);
                }


                Button transactionButton = findViewById(R.id.transaction_button);
                transactionButton.setBackground(drawable);

                // הוספת מאזין ללחיצות על הכפתור
                transactionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // קריאה לפונקציה הקיימת שלך בעת לחיצה
                        nexgoCancelTransaction(nexgoJavaWrapper);
                    }
                });

                transactionButton.setTranslationY(200); // להוריד פחות מלמעלה
                if ("מבצע חיוב...".equals(topText)) {
                    transactionButton.setVisibility(View.GONE);
                }
                // עדכון ה-TextView
                transactionTextView.setText(topText);
                transactionTextView.setMovementMethod(LinkMovementMethod.getInstance());
                transactionTextView.setTypeface(null, Typeface.BOLD);
                transactionTextView.setGravity(Gravity.CENTER);
                transactionTextView.setTranslationY(-300); // מזיז את הטקסט 50 פיקסלים למעלה
                // הצגת ה-Layout
                transactionLayout.setVisibility(View.VISIBLE);
            });
        }
    }

    private void updatePinEvent(String pinEvent) {
        Log.i("MainActivity", "Pin Event: " + pinEvent);
        runOnUiThread(() -> pinTextView.setText("PIN: " + pinEvent));
    }

    private void checkStoragePermission() {
        List<String> permissionsNeeded = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED)
                permissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED)
                permissionsNeeded.add(Manifest.permission.READ_MEDIA_VIDEO);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED)
                permissionsNeeded.add(Manifest.permission.READ_MEDIA_AUDIO);

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED)
                permissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                permissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            permissionsNeeded.add(Manifest.permission.CAMERA);

        if (!permissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toArray(new String[0]), REQUEST_PERMISSION_MULTIPLE);
        }
    }

    private void processBarcodeInput(String newText) {
        if (isProcessing) {
            return; // Skip if already processing
        }

        if (newText == null || newText.trim().isEmpty()) {
            return; // Ignore empty or null input
        }

        isProcessing = true; // Set processing flag

        // Store and clear the scanned text
        scannedText = newText.trim();
        input_barcode.setText("");

        // Hide the soft keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }

        // Delay processing to ensure input is properly registered
        handler.postDelayed(() -> {
            processScannedText(scannedText);
            isProcessing = false; // Reset processing flag
        }, 2350);
    }

    private void processScannedText(String text) {
        // Handle the scanned text (e.g., interact with WebView)
        webView.loadUrl("javascript:(function setBarcode(text) { " + "if (document.activeElement.id == 'search_prod') { " + "$('#search_prod')[0].value = text; " + "$('#search_prod')[0].click(); " + "} " + "else if ($('.keyboard').css('display') != 'none' || document.activeElement.id == 'search_keyboard2') { " + "setTimeout(function() { " + "console.log('yayy ' + text); " + "$('#search_keyboard2').val(text); " + "$('.keyboard_result')[0].value = text; " + "}, 1); " + "} " + "else if (document.querySelector('.text.zicuy_txt.zicuy_txt3.border2') != undefined) { " + "document.querySelector('.text.zicuy_txt.zicuy_txt3.border2').value = text; " + "} " + "else if (document.activeElement.id == '' || document.activeElement.id == ' ') { " + "$('#search_prod')[0].value = text; " + "$('#search_prod')[0].click(); " + "} " + "})('" + text + "')");
    }

    private String getUrlBasedOnZicuy(String zicuy_num) {
        // Implement logic to determine the URL based on the zicuy value
        return "https://liv.yedatop.com/modules/stock/cashbox_fe/index.php?zicuy=1&num_zicuy=" + zicuy_num;  // Example URL
    }

    private void loadUrl(String url) {
        // WebView webView = findViewById(R.id.webview);
        webView.loadUrl(url);
        //webView.getSettings().setJavaScriptEnabled(true);

    }


    private final Runnable disableKeyboard = new Runnable() {
        @Override
        public void run() {
            MainActivity.this.disableKeyboard();
        }
    };

    public void disableKeyboard() {
        // this.webView = new WebView(this);
        this.webView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        this.webView.setFocusable(false);
        this.webView.setFocusableInTouchMode(false);
        this.hideSoftKeyboard(null);
    }

    private final Runnable enableKeyboard = new Runnable() {
        @Override
        public void run() {
            Log.d("DEBUG", "enableKeyboard() Runnable executed");
            MainActivity.this.enableKeyboard();
        }
    };

    public void enableKeyboard() {
        Log.d("DEBUG", "enableKeyboard() called");

        if (this.webView != null) {
            Log.d("DEBUG", "webView is NOT null");

            this.webView.getSettings().setSaveFormData(false);
            this.webView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            this.webView.setFocusable(true);
            this.webView.setFocusableInTouchMode(true);
            this.webView.requestFocus();
            boolean focusResult = this.webView.requestFocus();
            Log.d("DEBUG", "webView.requestFocus() result: " + focusResult);
            Log.d("DEBUG", "webView.hasFocus(): " + this.webView.hasFocus());
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                String currentIme = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
                Log.d("DEBUG", "Current Input Method: " + currentIme);

                Log.d("DEBUG", "InputMethodManager is NOT null");

                boolean shown = imm.showSoftInput(this.webView, InputMethodManager.SHOW_IMPLICIT);
                Log.d("DEBUG", "imm.showSoftInput() result: " + shown);
            }
        }
//
//        this.webView.getSettings().setSaveFormData(false);
//        this.webView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
//        this.webView.setFocusable(true);
//        this.webView.setFocusableInTouchMode(true);
        // inputMethodManager.showSoftInputFromInputMethod(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
        //this.inputMethodManager.showSoftInputFromInputMethod(MainActivity.this.getCurrentFocus().getWindowToken(), SHOW_FORCED);

    }

    @JavascriptInterface
    public String openPinPad(String com, String wisepayCode) {
        String address = String.format("COM{0}", com);
        if (com.length() > 2) {
            address = com;
        }
        byte[] response = new byte[10];
        wisepayDll.Version(response, 10);
        wisepayDll.SetDebug(true);

        int ret1 = wisepayDll.Open2(address, wisepayCode);
        wisepayDll.SetDebug(true);
        if (ret1 != 0) {
            return "{\"error\": \"connection to wisepay failed " + "\" }";
        }
        return "ok";
    }

    private boolean hasAllPermissions() {

        int storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<String>();

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }


        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_PERMISSION_MULTIPLE);
            return false;
        }

        return true;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    boolean isOpened = false;
//    public void setListenerToRootView() {
//            final View parentView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
//
////            final View activityRootView = getWindow().getDecorView().findViewById(android.R.id.content);
//            parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            private boolean alreadyOpen;
//            private final int defaultKeyboardHeightDP = 100;
//            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
//            private final Rect rect = new Rect();
//
//
//            @Override
//            public void onGlobalLayout() {
//
//
//                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
//                parentView.getWindowVisibleDisplayFrame(rect);
//                int heightDiff = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
//                Log.i(TAG,"softkeyboard heightDiff "+heightDiff);
//
//                boolean isShown = heightDiff >= estimatedKeyboardHeight;
//
//                if (isShown == alreadyOpen) {
//                    Log.i("Keyboard state", "Ignoring global layout change...");
//                    return;
//                }
//                alreadyOpen = isShown;
//
//
//                if (heightDiff > 5) { // 99% of the time the height diff will be due to a keyboard.
//                   hideSoftKeyboard(null);
//                } else if (isOpened == true) {
//                }
//            }
//        });
//    }

    public void setListenerToRootView() {
        final View parentView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);

        ViewCompat.setOnApplyWindowInsetsListener(parentView, (v, insets) -> {
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
            boolean isKeyboardVisible = imeInsets.bottom > 0;

            Log.i(TAG, "Keyboard visible: " + isKeyboardVisible);

            if (isKeyboardVisible) {
                hideSoftKeyboard(null); // תפעילי את מה שאת צריכה כשהמקלדת פתוחה
            } else {
                // כשהמקלדת סגורה
            }

            return insets;
        });
    }

    private boolean isOnLoginScreen = false;

    private void initWebView() {
        etBarcode.setCursorVisible(false);
        input_barcode.setCursorVisible(false);

        if (webView != null) {
            webView.reload();
        }
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        final MyJsInterface jsInterface = new MyJsInterface(MainActivity.this, MainActivity.this);

        webView = (WebView) this.findViewById(R.id.fullscreen_content);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();///iti
        }

        webView.addJavascriptInterface(jsInterface, "android");
        webView.addJavascriptInterface(jsInterface, "android2");


        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);//iti
        //WebSettings webSetting = webView.getSettings();
        //webView.getSettings().setMixedContentMode(webSetting.MIXED_CONTENT_ALWAYS_ALLOW);
        //Log.d("WEBVIEW", "URL: " + url)
        //webSetting.setLoadsImagesAutomatically(true);
        //webSetting.setBlockNetworkImage(false);
        //webView.addJavascriptInterface(new WebAppBridge(), "android");

        // webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        webView.setHapticFeedbackEnabled(false);

        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);

        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.getSettings().setDomStorageEnabled(true);

        if (BuildConfig.DOMAIN != "liv.yedatop.com" && BuildConfig.DOMAIN != "peleliv2.yedatop" && BuildConfig.DOMAIN != "pos.pelecash.co.il" && BuildConfig.DOMAIN != "liv2.yedatop" && BuildConfig.DOMAIN != "mayafood.yedatop") {
            webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

//                if (focusKeyboard) {
                    MainActivity.this.hideSoftKeyboard(null);
//                    disableKeyboard();
                    input_barcode.requestFocus();

                    Utils.hideSystemUI(getWindow());
//                }


                    return false;
                }
            });
        } else {
            webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if (MainActivity.this.inputMethodManager.isAcceptingText())
                        MainActivity.this.disableKeyboard();

                    // MainActivity.this.hideSoftKeyboard(null);

                    return false;
                }
            });
        }


        final Runnable closeKeyboard = new Runnable() {
            @Override
            public void run() {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//123
                webView.setFocusable(true);
                webView.setFocusableInTouchMode(true);
            }
        };
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                try {
                    if (request == null || request.getUrl() == null) return false;
                    return handleUrl(view, request.getUrl().toString());
                } catch (Throwable t) {
                    Log.e("WEB_DEBUG", "Crash in shouldOverrideUrlLoading(request)", t);
                    return false;
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    return handleUrl(view, url);
                } catch (Throwable t) {
                    Log.e("WEB_DEBUG", "Crash in shouldOverrideUrlLoading(url)", t);
                    return false;
                }
            }

            private boolean handleUrl(WebView view, String url) {
                // 🔥 חסימת HTTP והמרה ל-HTTPS כדי למנוע ERR_CLEARTEXT_NOT_PERMITTED
                if (url.startsWith("http://")) {
                    String httpsUrl = "https://" + url.substring("http://".length());
                    Log.e("WEB_DEBUG", "FORCE HTTPS: " + url + " -> " + httpsUrl);
                    view.loadUrl(httpsUrl);
                    return true; // עצרנו את ה-HTTP
                }
                if (url == null || url.trim().isEmpty()) return false;

                Log.e("WEB_DEBUG", "URL = " + url);

                // ✅ טיפול בסכמות שלא שייכות ל-WebView רגיל
                String lower = url.toLowerCase(java.util.Locale.ROOT);

                if (lower.startsWith("tel:") || lower.startsWith("mailto:") || lower.startsWith("sms:")) {
                    try {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(i);
                        return true;
                    } catch (Throwable t) {
                        Log.e("WEB_DEBUG", "Failed to open external scheme: " + url, t);
                        return true; // כבר ניסינו לטפל
                    }
                }

                if (lower.startsWith("intent:")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        startActivity(intent);
                        return true;
                    } catch (Throwable t) {
                        Log.e("WEB_DEBUG", "Failed to handle intent url: " + url, t);
                        return true;
                    }
                }

                // ✅ הגנה על DOMAIN כדי שלא יפיל
                String domainSafe = (DOMAIN == null) ? "" : DOMAIN.trim();

                // אם זה "login"
                if (lower.contains("login") || lower.contains("logink")) {
                    Log.e("WEB_DEBUG", "LOGIN IN SAME WEBVIEW: " + url);
                    view.loadUrl(url);
                    return true;
                }

                // אם זה מסכים של BackOffice
                boolean isBackOffice = lower.contains("main2") || (!domainSafe.isEmpty() && url.equalsIgnoreCase("https://" + domainSafe + ".com/modules/stock")) || lower.contains("basket") || lower.contains("listing") || lower.contains("rep") || lower.contains("shaon") || lower.contains("stock/cp");

                if (isBackOffice) {
                    try {
                        openBackOffice(url);
                    } catch (Throwable t) {
                        Log.e("WEB_DEBUG", "openBackOffice crashed for url=" + url, t);
                    }
                    return true;
                }

                // ✅ אחרת לא עוקפים: נותנים ל-WebView לטפל
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.e("WEB_DEBUGGGG", "PAGE START: " + url);
                Log.e("DEVICE_OVERLAY", "PAGE START: " + url);

            }

            private void openInBrowser(String url) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed(); // Ignore SSL certificate errors
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

//                showLoadingError();
                Log.i("WebView", "onReceivedError " + error.toString());
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
//                showLoadingError();
                Log.i("WebView", "onReceivedHttpError " + errorResponse.toString());
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                reloadView.setVisibility(View.GONE);
//                injectCSS();
                // load logo url
                if (!jsInterfaceLoaded) {
                    /*jsInterfaceLoaded = true;
                    webView.removeJavascriptInterface("android");
                    webView.addJavascriptInterface(jsInterface, "android");*/
                }

//                webView.loadUrl("javascript:(function setBarcode(text){\n" +
//                        " if (document.activeElement.id == 'search_prod') {\n" +
//                        "$('#search_prod')[0].value = text;\n" +
//                        "$('#search_prod')[0].click();\n" +
//                        "    }\n" +
//                        "else if (document.activeElement.incremental =='search_keyboard2'){ \n" +
//                        "$('.keyboard_result')[0].value = text;\n" +
//                        "\n" +
//                        " } \n" +
//                        "    })");
                if (url != null && url.toLowerCase().contains("logink")) {
//                    new AlertDialog.Builder(MainActivity.this)
//                            .setTitle("מזהה קופה")
//                            .setMessage(deviceId)
//                            .setPositiveButton("סגור", null)
//                            .show();
                }
                String lower = url == null ? "" : url.toLowerCase();

                isOnLoginScreen = lower.contains("/login") || lower.contains("/logink");

                Log.e("PAGE_FINISHED", lower);

                if (isOnLoginScreen) {
                    addDeviceIdNativeOverlay();
                } else {
                    removeDeviceIdNativeOverlay();
                }
            }
        });
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        webView.setLongClickable(false);
        webView.setWebChromeClient(new WebChromeClient() {


            public void onSelectionStart(WebView view) {
                // By default we cancel the selection again, thus disabling
                // text selection unless the chrome client supports it.
                // view.notifySelectDialogDismissed();
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                Log.d(TAG, "JS ALERT: " + message);
                return true;
            }

            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
                if (mFilePathCallback != null) {
                    mFilePathCallback.onReceiveValue(null);
                }
                mFilePathCallback = filePathCallback;

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(MainActivity.this.getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                        takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
                    } catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.e(TAG, "Unable to create Image File", ex);
                    }

                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if (takePictureIntent != null) {
                    intentArray = new Intent[]{takePictureIntent};
                } else {
                    intentArray = new Intent[0];
                }

                Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
                chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
                chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

                startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

                return true;
            }

            private File createImageFile() throws IOException {
                // Create an image file name
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                File imageFile = File.createTempFile(imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */);
                return imageFile;
            }

            //The undocumented magic method override
            //Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {

                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);

            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                MainActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), MainActivity.FILECHOOSER_RESULTCODE);

            }
        });

       /* webView.setOnTouchListener((view, motionEvent) -> {
            // hideSoftKeyboard(view);
            return false;
        });
*///
// for pelecard and serial number

        Log.d("NEXGO_SN", "sn=" + deviceId);
        webView.loadUrl("https://" + DOMAIN + "/modules/stock/cashbox_fe?dangot=1&snMode=serial&sn=" + deviceId);
        //String finalUrl = "https://" + DOMAIN + "/modules/stock/cashbox_fe?dangot=1&sn=" + sn;
//Log.e("URL_CHECK", "finalUrl=" + finalUrl);
//webView.loadUrl(finalUrl);
//        Log.d("NEXGO_SN", "url=" + "https://"+DOMAIN+".com/modules/stock/cashbox_fe?dangot=1&sn="+Uri.encode(sn));
//             webView.loadUrl("https://"+DOMAIN+".com/modules/stock/cashbox_fe?dangot=1&runner=1");//for runner

        //end for pelecard


        //webView.loadUrl("https://dangot.yedatop.com/modules/stock/cashbox_fe?dangot=1");
//        webView.postUrl("https://office1.yedatop.com/modules/stock/rep_tazmech_print.php?&simple=1&sDate=01/02/2021&eDate=18/02/2021",("zedmode=1&journum=104").getBytes());
    }

    private final Runnable txt_reload = new Runnable() {
        @Override
        public void run() {
            reloadViewTXT.setVisibility(View.VISIBLE);
        }
    };

    private void showLoadingError() {
        MainActivity.this.runOnUiThread(txt_reload);
    }

    @Override
    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.reload:
//            {
//                initWebView();
//            }
//            case R.id.btnTestPrint:
//                //Utils.printTest(this, webView,rtPrinter);
//                break;
//            case R.id.btnDummyInvoice:
//                JsonObject res = Utils.dummyInvoiceData();
//
//              //  print_i_machine(res.get("invoice").toString(),res.get("barcode").toString(),1);
//                break;
//            case R.id.btnFunctions:
//             //   startActivity(new Intent(MainActivity.this, FunctionActivity.class));
//                break;
//            case R.id.btnDummyPayment:
//                // pinPadTest();
//
//                webView.loadUrl("javascript:alert(getLogoUrl())");
//                webView.loadUrl("javascript:alert(getLogoUrl)");
//
//                webView.evaluateJavascript("getLogoUrl()", value -> {
//                    Log.d(TAG, "" + value);
//                });
//                break;
//        }
    }

    public static int countWordsUsingSplit(String input) {
        if (input == null || input.isEmpty()) {
            return 0;
        }
        return input.split("src=", -1).length - 1;
    }

    public void load(final String html/*,String barcode*/) {
        data.put(html);
        if (data.length() == 1) {
            if (addedReceiptWebView == null) {

            } else {

            }
            addedReceiptWebView.loadData(html, "text/html; charset=UTF-8", "utf-8");
        }
        //addedReceiptWebView.loadData(html, "text/html; charset=UTF-8","utf-8");
    }

    @Override
    public void handleResult(Result result) {
        barcode_scanner.stopCamera();

        barcode_scanner.setVisibility(View.GONE);
        input_barcode.setText(result.getText());
    }
    public final Runnable enableKeyboardRunnable = new Runnable() {
        @Override
        public void run() {
            enableKeyboard();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_MULTIPLE) {
            boolean allGranted = true;

            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int result = grantResults[i];

                if (result == PackageManager.PERMISSION_GRANTED) {
                    Log.d("Permissions", "✔ Granted: " + permission);
                } else {
                    allGranted = false;
                    Log.e("Permissions", "✖ Denied: " + permission);

                    // בדיקה אם המשתמש סימן "Don't Ask Again"
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        Log.w("Permissions", "⚠️ Don't Ask Again was selected for: " + permission);
                        //showSettingsDialog();
                    }
                }
            }

            if (!allGranted) {
                Toast.makeText(this, "חלק מההרשאות סורבו. חלק מהפונקציות לא יעבדו.", Toast.LENGTH_LONG).show();
            }
        }
    }

    //    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.d("DEBUG", "API Level: " + Build.VERSION.SDK_INT);
//
//        if (requestCode == REQUEST_PERMISSION_MULTIPLE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.d(TAG, "Permission was granted!");
//            } else {
//                Log.e(TAG, "Permission was not granted!");
//                Toast.makeText(MainActivity.this, "Permission must be granted", Toast.LENGTH_SHORT).show();
//                MainActivity.this.finish();
//            }
//        }
//    }
    /* NOT MINE */
    //private IWoyouService woyouService;
    private ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            //woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //woyouService = IWoyouService.Stub.asInterface(service);
        }
    };
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;
    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    private final Runnable mHideRunnable = () -> hide();

    private void pinPrint(Bitmap mBitmap) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                CmdFactory cmdFactory = new PinFactory();
                Cmd cmd = cmdFactory.create();

                CommonSetting commonSetting = new CommonSetting();
                commonSetting.setPageHigh(22, CommonEnum.PAGE_HIGH_UNIT_TYPE_INCH);

                BitmapSetting bitmapSetting = new BitmapSetting();
                bitmapSetting.setBimtapLimitWidth(mBitmap.getWidth());

                try {
                    cmd.append(cmd.getBitmapCmd(bitmapSetting, mBitmap));
                } catch (SdkException e) {
                    e.printStackTrace();
                }
                cmd.append(cmd.getLFCRCmd());

                cmd.append(cmd.getEndCmd());

                rtPrinter.writeMsg(cmd.getAppendCmds());
            }
        }).start();


    }

    private void escPrint(Bitmap mBitmap, int mBmpPrintWidth) {

        final int bmpPrintWidth = (mBmpPrintWidth > 72) ? 72 : mBmpPrintWidth;

        CmdFactory cmdFactory = new EscFactory();
        Cmd cmd = cmdFactory.create();
        cmd.append(cmd.getHeaderCmd());

        CommonSetting commonSetting = new CommonSetting();
        commonSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
        cmd.append(cmd.getCommonSettingCmd(commonSetting));

        BitmapSetting bitmapSetting = new BitmapSetting();

        bitmapSetting.setBmpPrintMode(BmpPrintMode.MODE_SINGLE_COLOR);

        bitmapSetting.setBimtapLimitWidth(bmpPrintWidth * 8);
        try {
            cmd.append(cmd.getBitmapCmd(bitmapSetting, mBitmap));
        } catch (SdkException e) {
            e.printStackTrace();
        }
        cmd.append(cmd.getLFCRCmd());
        cmd.append(cmd.getLFCRCmd());
        cmd.append(cmd.getLFCRCmd());
        cmd.append(cmd.getLFCRCmd());
        cmd.append(cmd.getLFCRCmd());
        cmd.append(cmd.getLFCRCmd());


        if (rtPrinter != null) {
            rtPrinter.writeMsg(cmd.getAppendCmds());//Sync Write
        }
    }

    private void print_i_machine_barcode(Bitmap logo, Bitmap img, String barcode, int mBmpPrintWidth) {
        CmdFactory cmdFactory = new EscFactory();
        Cmd escCmd = cmdFactory.create();

        if (logo != null) escCmd = Utils.addBitmap(escCmd, logo, mBmpPrintWidth);
        escCmd = Utils.addBitmap(escCmd, img, mBmpPrintWidth);

        escCmd.append(escCmd.getHeaderCmd());


        CommonSetting commonSetting = new CommonSetting();
        commonSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
        escCmd.append(escCmd.getCommonSettingCmd(commonSetting));

        BarcodeSetting barcodeSetting = new BarcodeSetting();
        barcodeSetting.setBarcodeStringPosition(BarcodeStringPosition.BELOW_BARCODE);
        barcodeSetting.setEscBarcodePrintOritention(EscBarcodePrintOritention.Rotate0);
        barcodeSetting.setHeightInDot(96);//accept value:1~255
        barcodeSetting.setBarcodeWidth(3);//accept value:2~6
        barcodeSetting.setPosition(new Position(200, 20));
        barcodeSetting.setQrcodeDotSize(5);//accept value: Esc(1~15), Tsc(1~10)
        try {
            escCmd.append(escCmd.getBarcodeCmd(BarcodeType.CODE128, barcodeSetting, barcode));
        } catch (SdkException e) {
            e.printStackTrace();
        }
        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());
        escCmd.append(escCmd.getLFCRCmd());


        rtPrinter.writeMsg(escCmd.getAppendCmds());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (img.getVisibility() == View.VISIBLE) img.setVisibility(View.GONE);
        if (webView.getVisibility() == View.GONE) webView.setVisibility(View.VISIBLE);
        else if (webView.canGoBack()) {
            // webView.goBack();
        } else {
            //super.onBackPressed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Uri[] results = null;

        // Check that the response is a good one
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                // If there is not data, then we may have taken a photo
                if (mCameraPhotoPath != null) {
                    results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                }
            } else {
                String dataString = data.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }

        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
        return;
    }

    private void openBackOffice(String url) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        Intent intent = new Intent(this, BackOfficeActivity.class);
        intent.putExtra("URL", url);
        intent.putExtra("present_type", type_present_global);
        intent.putExtra("present_username", username_for_path);
        startActivity(intent);

    }

    private void openLogin(String url) {
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Log.e("NAV", "openLogin url=" + url);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);
        finish();
    }
}