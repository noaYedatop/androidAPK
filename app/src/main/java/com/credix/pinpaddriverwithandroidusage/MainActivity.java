package com.credix.pinpaddriverwithandroidusage;

import static android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS;
import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;

import static POSSDK.POSSDK.CutPartAfterFeed;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.device.ScanManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.credix.pinpad.jni.PinPadAPI;
import com.credix.pinpad.jni.PinPadResponse;
import com.credix.pinpad.jni.PinPadSession;
import com.credix.pinpaddriverwithandroidusage.model.EnvPaymentItem;
import com.credix.pinpaddriverwithandroidusage.receiver.UsbDeviceReceiver;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
//import com.google.zxing.Result;
import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.neostra.electronic.Electronic;
import com.neostra.electronic.ElectronicCallback;
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
//import com.urovo.sdk.scanner.InnerScannerImpl;
import com.wisepay.pinpad.Api;
import POSAPI.*;
//import com.urovo.sdk.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.sql.Driver;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import POSSDK.POSSDK;
import android_serialport_api.SerialPort;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener , TextWatcher , ZXingScannerView.ResultHandler {
    BaseApp baseApp;
    public static final String TAG = "JS LOG";
    private static Boolean _isCancelTransaction = false;
    private static final int REQUEST_PERMISSION_MULTIPLE = 911;
    private long lastCreditPayTime = 0;

    private boolean useFocus = false;

    private String scannedText = "";


    private FirebaseAnalytics mFirebaseAnalytics;
    public static String DOMAIN = BuildConfig.DOMAIN;
//    public static String DOMAIN = "liv";
//      public static String DOMAIN = Bu;


    /*SCREENS*/
//    public static final String MAIN_PATH = "https://kupa.yedatop.com";
//    public static final String MAIN_PATH = "https://office1.yedatop.com";
    public static final String MAIN_PATH = "https://"+DOMAIN+".yedatop.com";
    //public static final String MAIN_PATH = "https://dangot.yedatop.com";

    private static final String[] BLOCKED_SCREENS = new String[]{
            "modules/stock/cashbox_fe/"
    };


    private Bitmap logo = null;
    private boolean logoDownloaded = false;
    private boolean jsInterfaceLoaded = false;
    Pattern p = Pattern.compile("(src=)\"(.*?)\"");


    //Views
    private Button btnFunctions, btnPrint, btnDummyInvoice;
    private EditText etBarcode;
    private RecyclerView rv;

    private Boolean focusKeyboard = true;

    private AdapterRows adapter;
//    private ArrayList<ArrayList<String>> data = new ArrayList<>();


    private final Handler mHideHandler = new Handler();

    private View mContentView;

    private View mControlsView;
    public WebView webView;
    private boolean mVisible;

    private int fontSize = 25; // was 30

    private UsbDeviceReceiver mUsbReceiver;

    private int running;
    private EditText input_barcode;
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
    private boolean inProcess = false;
    private PinPadAPI dll;
    private Api apiDll;

    private Vector<Driver> mDrivers;

    private ScanManager mScanManager = null;

    private Api wisepayDll;


    private ElectronicCallback iMinCallback = new ElectronicCallback() {
        @Override
        public void electronicStatus(String weight, String status) {
            iMinWeight = Double.parseDouble(weight);
        }
    };


    private double iMinWeight = 0;
    private boolean urovo_power_On;
    private ZXingScannerView barcode_scanner;

    private void connectImin(){
        try {
            mElectronic = new Electronic.Builder()
                    .setDevicePath("/dev/ttyS4").setBaudrate(9600)
                    .setReceiveCallback(iMinCallback)
                    .builder();
//            current_platform = PLATFORMS.IMIN;
        }catch(Exception e){}
    }
    private double getFromIminWeight(){
        return iMinWeight;
    }

//ניוטק איילון -------------------------------------------------------------------------------------
    private double getFromUsbWeightNewtech(){

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            return -1;
        }

        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            // add UsbManager.requestPermission(driver.getDevice(), ..) handling here
            return -1;
        }
        try{
            int usbSerialPort = 9600;
            UsbSerialPort port = driver.getPorts().get(0); // Most devices have just one port (port 0)
            port.open(connection);

            port.setParameters(usbSerialPort, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

            port.write("w\r\n".getBytes(), 250);
            Thread.sleep(250, 0);
            byte[] buf = new byte[2048];

            int len = port.read(buf, 1250);
            if (len < 3)
                return -1;
            Log.i("UDB",new String(buf));
            String  weightStr = new String(buf);//.replace("\r","").replace("\n","").replace("g","");
            //if(len>=5){
                String s = weightStr.substring(0,7);
            //}
            double  weight  = new Double(s);
            return  weight;
        }catch(Exception e){
            return -1;
        }
    }
    private SerialPort mSerialPort = null;
    private OutputStream mOutputStream = null;
    private SerialPort openSerialPort(File device, int baudrate, int stopBits, int dataBits, int parity, boolean flowCon){
        //closeSerialPort();
        try {
            mSerialPort = new SerialPort(device, baudrate, stopBits);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return mSerialPort;
    }

    private SerialPort getttyS1() throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Open the serial port */
            return openSerialPort(new File("/dev/ttyS1"), 9600, 1,8,0, false);
        }
        return mSerialPort;
    }

    private OutputStream getOutputStream(){
        if(mSerialPort == null){
            return null;
        }
        if(mOutputStream == null) {
            try {
                mOutputStream = mSerialPort.getOutputStream();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return mOutputStream;
        }
        return mOutputStream;
    }

    private double getFromUsbWeightBip30Dangot(){//DANGOT ANDROID
//
//        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
//        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
//        if (availableDrivers.isEmpty()) {
//            return -1;
//        }


        // Open a connection to the first available driver.
//        UsbSerialDriver driver = availableDrivers.get(0);
//        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
//        if (connection == null) {
//            // add UsbManager.requestPermission(driver.getDevice(), ..) handling here
//            return -1;
//        }
        try{
            int usbSerialPort = 9600;
//            UsbSerialPort port = driver.getPorts().get(0); // Most devices have just one port (port 0)
//            port.open(connection);
//
//            port.setParameters(usbSerialPort, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            getttyS1();
            OutputStream mos = getOutputStream();
//            port.write("w\r\n".getBytes(), 250);
//            byte[] buf = new byte[2048];
//            int len = port.read(buf, 1250);
//            if (len < 3)
//                return -1;
            //Log.i("UDB, ",new String(buf));
            ByteArrayOutputStream bb = (ByteArrayOutputStream)mos;
            byte[] bytrm = bb.toByteArray();

            String  weightStr = new String(bytrm, StandardCharsets.UTF_8).replace("\r","").replace("\n","").replace("g","");
            double  weight  = new Double(weightStr);
            return  weight;
        }catch(Exception e){
            return -1;
        }
    }

    //BIP 30--------------------------------------------------------------------------------------------
    private double getFromUsbWeightBip30(){

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            return -1;
        }

        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            // add UsbManager.requestPermission(driver.getDevice(), ..) handling here
            return -1;
        }
        try{
            int usbSerialPort = 9600;
            UsbSerialPort port = driver.getPorts().get(0); // Most devices have just one port (port 0)
            port.open(connection);

            port.setParameters(usbSerialPort, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

            port.write("w\r\n".getBytes(), 250);
            byte[] buf = new byte[2048];
            int len = port.read(buf, 1250);
            if (len < 3)
                return -1;
            Log.i("UDB, ",new String(buf));
            String  weightStr = new String(buf).replace("\r","").replace("\n","").replace("g","");
            double  weight  = new Double(weightStr);
            return  weight;
        }catch(Exception e){
            return -1;
        }
    }

    //CS600--------------------------------------------------------------------------------------------
    private double getFromUsbWeightCS600(){

        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            return -1;
        }

        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            // add UsbManager.requestPermission(driver.getDevice(), ..) handling here
            return -1;
        }
        try{
            int usbSerialPort = 9600;
            UsbSerialPort port = driver.getPorts().get(0); // Most devices have just one port (port 0)
            port.open(connection);

            port.setParameters(usbSerialPort, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);


            byte[] buf1 = new byte[2048];
            int aa;
            do{
                aa=port.read(buf1,250);
            }while (aa>0);
            port.write("w\r\n".getBytes(), 250);//5050
            byte[] buf = new byte[2048];
            // Thread.sleep(1000);

            int len = port.read(buf, 250);//3500
            if (len < 3)
                return -1;
            Log.i("UDB, ",new String(buf));
            String  weightStr = new String(buf).replace("\r","").replace("\n","").replace("g","");
            String ss = weightStr.substring(0, 6);
            double  weight  = new Double(ss);
            return  weight;


        }catch(Exception e){
            return -1;
        }
    }

    public Bitmap generateBarcode(String barcodeData, int width, int height) {
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try {
            Code128Writer writer = new Code128Writer();
            BitMatrix bitMatrix = writer.encode(barcodeData, BarcodeFormat.CODE_128, width, height, hints);

            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                int offset = y * width;
                for (int x = 0; x < width; x++) {
                    pixels[offset + x] = bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE;
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            return bitmap;

        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
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
        if (BuildConfig.DOMAIN != "liv" && BuildConfig.DOMAIN != "dangot1" && BuildConfig.DOMAIN != "dangot"  && BuildConfig.DOMAIN != "dangotandroid" && input_barcode != null )//DANGOT UROVO
            input_barcode.requestFocus();
        dll = null;
        lastCreditPayTime = 0;

        initScan();
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
        try{
            mScanManager = new ScanManager();
            boolean powerOn = mScanManager.getScannerState();
        }catch(Exception e){}


    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    /*

    UROVO function


     */

    @JavascriptInterface
    public void repotUrovo(){

        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        final MainActivity activity = this;
        try {

            json.put("jsonrpc","2.0");
            json.put("method","doPeriodic");
            json.put("id",1);
            jsonArray.put("ashrait");
            jsonArray.put("xml");

            json.put("params",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String f_reqeustString = json.toString();

        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();

                RequestBody body = RequestBody.create(JSON, f_reqeustString); // new
                Request request = new Request.Builder()
                        .url("http://127.0.0.1:8081/SPICy")
                        .post(body)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
//                    response.body().close();
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("RESULT ===>", result.toString());
                    if (result == null)
                    {
                        result = new JSONObject();
                        result.put("success",false);
                    }else {


                        result = result.optJSONObject("result");

                        JSONObject resp = new JSONObject();
                        resp.put("code",60000);
                        JSONObject xml = new JSONObject();
                        xml.put("report",result.optString("report"));
                        resp.put("data",xml);

                         final String zz    = resp.toString();
                        Log.i("zz ==>   " , zz);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String load = "javascript:(function(data) {" +
                                "angular.element(document.querySelector('#credit_appr')).scope().getUrovoTransactionsReportResult(JSON.stringify(data));"+
                            "})("+zz+")";
                                webView.loadUrl(load);
//                                webView.loadUrl("javascript:angular.element(document.querySelector('#credit_appr')).scope().getUrovoTransactionsReportResult(JSON.stringify(data))");
                            }
                        });

                    }


                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    try{
                        JSONObject resp = new JSONObject();
                        resp.put("code",60001);
                        JSONObject xml = new JSONObject();
                        resp.put("data","");
                        final String zz = resp.toString();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                String load = "javascript:(function(data) {" +
                                        "angular.element(document.querySelector('#credit_appr')).scope().getUrovoTransactionsReportResult(JSON.stringify(data));"+
                                        "})("+zz+")";
                                webView.loadUrl(load);
//                                webView.loadUrl("javascript:angular.element(document.querySelector('#credit_appr')).scope().getUrovoTransactionsReportResult(JSON.stringify(data))");
                            }
                        });
                    }catch (Exception ee){}



                }
            }
        });
        worker.start();
    }


    @JavascriptInterface
    public void getUrovoStatus(){

        JSONObject json = new JSONObject();
        JSONArray jsonArray = new JSONArray();

        final MainActivity activity = this;
        try {

            json.put("jsonrpc","2.0");
            json.put("method","getStatus");
            json.put("id",1);
            jsonArray.put("ashrait");
            json.put("params",jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String f_reqeustString = json.toString();;

        Thread worker = new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();

                RequestBody body = RequestBody.create(JSON, f_reqeustString); // new
                Request request = new Request.Builder()
                        .url("http://127.0.0.1:8081/SPICy")
                        .post(body)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
//                    response.body().close();
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("RESULT STATUS ===>", result.toString());
                    if (result == null)
                    {
                        result = new JSONObject();
                        result.put("success",false);
                    }else {
                        if (result.optString("result").indexOf("ashraitReady") > -1){
                            Log.i(TAG,"READY TO CHARGE "+result.optString("result"));
                        }else{

                            Intent it = new Intent("intent.my.action");
                            it.setComponent(new ComponentName("il.co.modularity.agamento", "il.co.modularity.agamento.views.main.MainActivity"));
                            it.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent launch_back = new Intent(activity, MainActivity.class);
                                    if(DOMAIN == "dangot1"){
                                        launch_back.setComponent(new ComponentName("Synqpos", "com.credix.pinpaddriverwithandroidusage.MainActivity"));
                                    }
                                    else {
                                        launch_back.setComponent(new ComponentName("com.yedatop.maytal", "com.credix.pinpaddriverwithandroidusage.MainActivity"));
                                    }
                                    launch_back.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    getApplicationContext().startActivity(launch_back);
                                }
                            },2000);
                            getApplicationContext().startActivity(it);



                            Log.i(TAG,"NOT READY TO CHARGE "+result.optString("result"));
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
                            if(DOMAIN == "dangot1"){
                                launch_back.setComponent(new ComponentName("Synqpos", "com.credix.pinpaddriverwithandroidusage.MainActivity"));
                            }
                            else {
                                launch_back.setComponent(new ComponentName("com.yedatop.maytal", "com.credix.pinpaddriverwithandroidusage.MainActivity"));
                            }
                            launch_back.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);

                           // launch_back.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            getApplicationContext().startActivity(launch_back);

                        }
                    },2000);
                    try {
                        getApplicationContext().startActivity(it);

                    }catch (Exception ex){}
                    // e.printStackTrace();
                }
            }
        });
        worker.start();
        //startActivity(intent);
    }

    @JavascriptInterface
    public void startUrovoPayment(String amount,String approvalNumber, int pay_num,double pay_first,int ashray_f_credit,
                                  int moto,  String cardNumber,String expDate, String cvv, String cardHolder_id){

        int tranType = 1; // Regular
        double amt = Double.parseDouble(amount);
        if (amt < 0)
        {
            tranType = 53; // Regund
            amt = Math.abs(amt);
        }
//            int amountAsAgorot = (int)(amt * 100);
        int amountAsAgorot = (int)(Math.round(amt * 100));
        int posEntryMode = 40;
        if(moto != 0){
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
            data.put("amount",amountAsAgorot);
            data.put("vuid",todayAsString);
            /*
1 - Regular
3 - Forced Transaction
6 - Cash Back
7 - Cash
30 - Balance
53 - Refund
             */
            data.put("tranType",tranType);
            data.put("creditTerms",1);
            if (pay_num > 1) {
                data.put("payments", pay_num);
                first_payment = (int)(Math.round(pay_first * 100));
                paymentType = "CREDIT_TERMS_PAYMENTS";

                data.put("firstPaymentAmount", first_payment);
                data.put("otherPaymentAmount", (amountAsAgorot - first_payment) / (pay_num-1));
            }
            if (ashray_f_credit > 1){
                paymentType = "CREDIT_TERMS_CREDIT";
                data.put("creditPayments",ashray_f_credit);

            }

            if (cardNumber.isEmpty()){
                data.put("cardNumber",cardNumber);
                data.put("expDate",expDate);
                data.put("cvv",cvv);
                data.put("cardHolderID",cardHolder_id);
            }
//            data.put("payments",5);
            data.put("tranCode",1);
            data.put("posEntryMode",posEntryMode);
            data.put("currency","376");
            json.put("jsonrpc","2.0");
            json.put("method","doTransaction");
            json.put("id",approvalNumber);
            jsonArray.put("ashrait");
            jsonArray.put(data);
            json.put("params",jsonArray);
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
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(60, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .build();

                RequestBody body = RequestBody.create(JSON, f_reqeustString); // new
                Request request = new Request.Builder()
                            .url("http://127.0.0.1:8081/SPICy")
                        .post(body)
                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
//                    response.body().close();
                    JSONObject result = new JSONObject(response.body().string());
                    Log.i("RESULT ===>", result.toString());
                    if (result == null)
                    {
                        result = new JSONObject();
                        result.put("success",false);
                    }else {
                        result = result.optJSONObject("result");
                        result.put("success", true);
                    }
                    final JSONObject urovo_result =  MainActivity.this.buildPaymentResponse(result,f_payment_type,f_first_payment,f_number_payment );

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl("javascript:angular.element(document.querySelector('#credit_appr')).scope().end_ashray('"+urovo_result.toString()+"')");
                        }
                    });
                    Intent launch_back = new Intent(activity, MainActivity.class);
                    if(DOMAIN == "dangot1"){
                        it.setComponent(new ComponentName("Synqpos", "com.credix.pinpaddriverwithandroidusage.MainActivity"));
                    }
                    else {
                        it.setComponent(new ComponentName("com.yedatop.maytal", "com.credix.pinpaddriverwithandroidusage.MainActivity"));
                    }
                    launch_back.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
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


    public String getManpik(int issuer, int manpic){
        if (manpic == 0 )
            return "TOURIST";

        switch (issuer) {
            case 0:
                return "TOURIST";
            case 1:  return "ISRACARD";
            case 2: // VISA_CAL
                    if (manpic == 2)
                        return "VISA_CAL";
                    if (manpic == 6)
                        return "LEUMICARD";
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
    private String getTransactionTypeValue(int tt){
        switch (tt){

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
    private String getTransactionType(int tt){
        switch (tt){

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
                    case 0: response.put("code", 60000); break;
//                    case 2:
//                    case 4:
//                    case 6:
//                    case 8:
                    default: response.put("code", 60002);
                }


                JSONObject details = new JSONObject();
                //CREDIT_TERMS_REGULAR //CREDIT_TERMS_IMMEDIATE //CREDIT_TERMS_CREDIT
                details.put("cardNumber", result.optString("cardNumber", "-1"));
                details.put("expDate", result.optString("expDate", "-1"));
                details.put("amount", result.optDouble("amount") );
                details.put("voucherNum", result.optString("rrn", "-1"));
                details.put("issuerAuthNum", result.optString("issuerAuthNum", "-1"));

                details.put("cardBrandValue", result.optInt("mutag", -1));

                int mutag = result.optInt("mutag", -1);
                int manpik = result.optInt("manpik", -1);

                details.put("cardIssuer", getManpik(mutag,manpik));

                details.put("creditTerms", type);

               // details.put("panEntryMode","PAN_ENTRY_MODE_MOTO");
                details.put("posEntryMode", result.optInt("posEntryMode"));
                details.put("creditTermsValue",getTransactionType(result.optInt("tranType")));
                details.put("transactionType",getTransactionType(result.optInt("j")));
                if (type == "CREDIT_TERMS_PAYMENTS") {
                    details.put("firstPayment", f_payment);
                    details.put("noPayments", n_payment);
                }
                if(type == "CREDIT_TERMS_CREDIT"){}

                JSONObject res = new JSONObject();
                res.put("txnDetails", details);
                response.put("data",res);

//                response.put("txnDetails", details);
            }
            return response;
        }catch (Exception e){
            return response;
        }
    }
    private final Runnable beforeText = new Runnable() {
        @Override
        public void run() {
            input_barcode.clearFocus();
            //webView.loadUrl("javascript:setBarcode('"+s.toString()+"')");
            String input = input_barcode.getText().toString();
            webView.loadUrl("javascript:(function setBarcode(text){\n" +
                    "debugger;"+
                    " if (document.activeElement.id == 'search_prod') {\n" +
                    "$('#search_prod')[0].value = text;\n" +
                    "$('#search_prod')[0].click();\n" +
                    "    }\n" +
                    "else if ($('.keyboard').css('display') != 'none' || document.activeElement.id =='search_keyboard2'){ \n" +
                    "setTimeout(function(){"+
                    "console.log('yayy '+text);"+
                    "$('search_keyboard2').value = text ;\n" +
                    "$('.keyboard_result')[0].value = text;\n" +
                    "},1);"+
                    "\n" +
                    " }else if (document.querySelector('.text.zicuy_txt.zicuy_txt3.border2') != undefined){ \n" +
                    "document.querySelector('.text.zicuy_txt.zicuy_txt3.border2').value = text}\n"+
                    "else if (document.activeElement.id == '' || document.activeElement.id == ' '){ \n" +
                    "$('#search_prod')[0].value = text;\n" +
                    "$('#search_prod')[0].click();\n" +
                    "    }\n" +
                    "    })('"+input.toString()+"')");

//                webView.loadUrl("javascript:setBarcode('"+s.toString()+"')");

            input_barcode.setText("");
            input_barcode.addTextChangedListener(MainActivity.this);
            Log.i("beforeTextChanged",input.toString());
        }
    };
    @Override
    public void afterTextChanged(Editable s) {
        Log.i("afterTextChanged",s.toString());
    }
    private String s;
    @Override
    public void beforeTextChanged(final CharSequence s, int start,
                                  int count, int after) {
        input_barcode.clearFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
        hideSoftKeyboard(getWindow().getCurrentFocus());
            input_barcode.removeTextChangedListener(this);
        this.s = s.toString();
        if (DOMAIN == "dangotandroid" || DOMAIN == "dangotandroid1") {
            handler.postDelayed(beforeText, 1000);
        }
        else {
            handler.postDelayed(beforeText, 350);//350}
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
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
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.hideSystemUI(getWindow());
        super.onCreate(savedInstanceState);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                //WindowManager.LayoutParams.FLAG_SECURE);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "MainActivity");
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle);

//        WebView.setWebContentsDebuggingEnabled(true);

        Utils.printInputLanguages(this);

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().sendUnsentReports();


        baseApp = (BaseApp) getApplication();


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if(BuildConfig.DOMAIN != "liv" && BuildConfig.DOMAIN != "dangot" && BuildConfig.DOMAIN != "dangot1"   && BuildConfig.DOMAIN != "dangotandroid" && BuildConfig.DOMAIN != "dangotandroid1" && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&  input_barcode != null) {//DANGOT
            WebView.enableSlowWholeDocumentDraw();
            input_barcode.addTextChangedListener(this);

        }

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        if (DOMAIN == "dangot1" || DOMAIN == "dangotandroid" || DOMAIN == "dangotandroid1") {
            setContentView(R.layout.activity_main_login);
        }
        else{
            setContentView(R.layout.activity_main);
        }

        input_barcode = findViewById(R.id.input_barcode);
        input_barcode.addTextChangedListener(this);

        barcode_scanner = (ZXingScannerView) findViewById(R.id.barcode_scanner);

        img = (ImageView)findViewById(R.id.img);
        reloadView = findViewById(R.id.reload);
        reloadViewTXT = findViewById(R.id.reload_txt);
        reloadView.setOnClickListener(this);
        receiptWebView = findViewById(R.id.webReciept);


        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        btnFunctions = findViewById(R.id.btnFunctions);
        btnPrint = findViewById(R.id.btnTestPrint);
        btnDummyInvoice = findViewById(R.id.btnDummyInvoice);
        etBarcode = findViewById(R.id.etBarcode);
//        buildBarcodeReader();

        btnFunctions.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btnDummyInvoice.setOnClickListener(this);
        findViewById(R.id.btnDummyPayment).setOnClickListener(this);


        connectImin();


        initWebView();
        if (BuildConfig.DOMAIN != "liv"  && BuildConfig.DOMAIN != "dangot1" && BuildConfig.DOMAIN != "dangot" && BuildConfig.DOMAIN != "dangotandroid" && BuildConfig.DOMAIN != "dangotandroid1") {//DANGOT
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            setListenerToRootView();
        }
        else{
           // if (getPlatform() == PLATFORMS.UROVO) {
//                getUrovoStatus();
           // }

        }
        disableKeyboard();
        hasAllPermissions();
        initPrinter();
        registerPrinter(this);



    }

    private final Runnable disableKeyboard = new Runnable() {
        @Override
        public void run() {
            MainActivity.this.disableKeyboard();
        }
    };
    public void disableKeyboard(){
        this.webView.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
        this.webView.setFocusable(false);
        this.webView.setFocusableInTouchMode(false);
        this.hideSoftKeyboard(null);
    }
    private final Runnable enableKeyboard = new Runnable() {
        @Override
        public void run() {
            MainActivity.this.enableKeyboard();
        }
    };
    public void enableKeyboard(){
        this.webView.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
        this.webView.setFocusable(true);
        this.webView.setFocusableInTouchMode(true);
       // inputMethodManager.showSoftInputFromInputMethod(MainActivity.this.getCurrentFocus().getWindowToken(), 0);
      //this.inputMethodManager.showSoftInputFromInputMethod(MainActivity.this.getCurrentFocus().getWindowToken(), SHOW_FORCED);

    }

    @JavascriptInterface
    public String openPinPad(String com, String wisepayCode) {
        String address = String.format("COM{0}", com);
        if (com.length() > 2)
        {
            address = com;
        }
        byte[] response = new byte[10];
        wisepayDll.Version(response, 10);
        wisepayDll.SetDebug(true);

        int ret1 = wisepayDll.Open2(address, wisepayCode);
        wisepayDll.SetDebug(true);
        if (ret1 != 0)
        {
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
    public void setListenerToRootView() {
            final View parentView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);

//            final View activityRootView = getWindow().getDecorView().findViewById(android.R.id.content);
            parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private boolean alreadyOpen;
            private final int defaultKeyboardHeightDP = 100;
            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
            private final Rect rect = new Rect();


            @Override
            public void onGlobalLayout() {


                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
                parentView.getWindowVisibleDisplayFrame(rect);
                int heightDiff = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
                Log.i(TAG,"softkeyboard heightDiff "+heightDiff);

                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == alreadyOpen) {
                    Log.i("Keyboard state", "Ignoring global layout change...");
                    return;
                }
                alreadyOpen = isShown;


                if (heightDiff > 5) { // 99% of the time the height diff will be due to a keyboard.
                   hideSoftKeyboard(null);
                } else if (isOpened == true) {
                }
            }
        });
    }
    private void initWebView() {
        etBarcode.setCursorVisible(false);
        input_barcode.setCursorVisible(false);

        if (webView != null){
            webView.reload();
        }
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        final MyJsInterface jsInterface = new MyJsInterface(MainActivity.this);

        webView = (WebView) this.findViewById(R.id.fullscreen_content);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            WebView.enableSlowWholeDocumentDraw();
        }

        webView.addJavascriptInterface(jsInterface, "android");
        webView.addJavascriptInterface(jsInterface, "android2");


        webView.getSettings().setJavaScriptEnabled(true);
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

        if (BuildConfig.DOMAIN != "liv" && BuildConfig.DOMAIN != "dangot1" && BuildConfig.DOMAIN != "dangot" && BuildConfig.DOMAIN != "dangotandroid")//DANGOT
        {
            webView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

//                if (focusKeyboard) {
                    MainActivity.this.hideSoftKeyboard(null);


                        input_barcode.requestFocus();

                    Utils.hideSystemUI(getWindow());
//                }


                    return false;
                }
            });
        }else{
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
//                            webView.setFocusable(true);
//                            webView.setFocusableInTouchMode(true);
            }
        };
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.toLowerCase().contains("login")){
                    openLogin(url);
                }else if (url.toLowerCase().contains("main2") || url.equalsIgnoreCase("https://"+DOMAIN+".yedatop.com/modules/stock") || url.toLowerCase().contains("basket") || url.toLowerCase().contains("listing") || url.toLowerCase().contains("rep") ||url.toLowerCase().contains("shaon")||url.toLowerCase().contains("stock/cp") ) {
//                    openInBrowser(url);
                    openBackOffice(url);
                    return true;
                }
                else{
                    MainActivity.this.runOnUiThread(closeKeyboard);
                }

                view.loadUrl(url);
                return true;
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
                Log.i("WebView","onReceivedError "+error.toString() );
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
//                showLoadingError();
                Log.i("WebView","onReceivedHttpError "+errorResponse.toString() );
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

            public boolean onShowFileChooser(
                    WebView webView, ValueCallback<Uri[]> filePathCallback,
                    WebChromeClient.FileChooserParams fileChooserParams) {
                if(mFilePathCallback != null) {
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
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));
                    } else {
                        takePictureIntent = null;
                    }
                }

                Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
                contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
                contentSelectionIntent.setType("image/*");

                Intent[] intentArray;
                if(takePictureIntent != null) {
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
                File storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File imageFile = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );
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
                MainActivity.this.startActivityForResult(Intent.createChooser(i,"File Chooser"), FILECHOOSER_RESULTCODE);

            }

            // For Android 3.0+
            public void openFileChooser( ValueCallback uploadMsg, String acceptType ) {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("*/*");
                MainActivity.this.startActivityForResult(
                        Intent.createChooser(i, "File Browser"),
                        FILECHOOSER_RESULTCODE);
            }

            //For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture){
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                MainActivity.this.startActivityForResult( Intent.createChooser( i, "File Chooser" ), MainActivity.FILECHOOSER_RESULTCODE );

            }
        });

       /* webView.setOnTouchListener((view, motionEvent) -> {
            // hideSoftKeyboard(view);
            return false;
        });
*///DANGOT1

        if(DOMAIN == "dangot1") {
            String sn = "0";

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                sn = Build.getSerial();
            }

            webView.loadUrl("https://" + DOMAIN + ".yedatop.com/modules/stock/cashbox_fe?dangot=1&sn=" + sn);
        }
        else {
            webView.loadUrl("https://"+DOMAIN+".yedatop.com/modules/stock/cashbox_fe?dangot=1");
        }
        //webView.loadUrl("https://dangot.yedatop.com/modules/stock/cashbox_fe?dangot=1");
//        webView.postUrl("https://office1.yedatop.com/modules/stock/rep_tazmech_print.php?&simple=1&sDate=01/02/2021&eDate=18/02/2021",("zedmode=1&journum=104").getBytes());
    }
    private void injectCSS() {
        try {
            InputStream inputStream = getAssets().open("custom.css");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
            webView.loadUrl("javascript:(function() {" +
                    "var parent = document.getElementsByTagName('head').item(0);" +
                    "var style = document.createElement('style');" +
                    "style.type = 'text/css';" +
                    // Tell the browser to BASE64-decode the string into your script !!!
                    "style.innerHTML = window.atob('" + encoded + "');" +
                    "parent.appendChild(style)" +
                    "})()");
        } catch (Exception e) {
            e.printStackTrace();
        }
        ;
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
    private void callJavaScript(String methodName, Object... params) {
        Log.d(TAG, "callJavaScript " + methodName);
        Log.d(TAG, params.toString());


        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("javascript:try{");
        stringBuilder.append(methodName);
        stringBuilder.append("(");
        for (Object param : params) {
            if (param instanceof String) {
                stringBuilder.append("'");
                stringBuilder.append(param);
                stringBuilder.append("'");
            }
            stringBuilder.append(",");
        }
        stringBuilder.append(")}catch(error){Android.onError(error.message);}");
        webView.loadUrl(stringBuilder.toString());
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.reload:
            {
                initWebView();
            }
            case R.id.btnTestPrint:
                //Utils.printTest(this, webView,rtPrinter);
                break;
            case R.id.btnDummyInvoice:
                JsonObject res = Utils.dummyInvoiceData();

              //  print_i_machine(res.get("invoice").toString(),res.get("barcode").toString(),1);
                break;
            case R.id.btnFunctions:
             //   startActivity(new Intent(MainActivity.this, FunctionActivity.class));
                break;
            case R.id.btnDummyPayment:
                // pinPadTest();

                webView.loadUrl("javascript:alert(getLogoUrl())");
                webView.loadUrl("javascript:alert(getLogoUrl)");

                webView.evaluateJavascript("getLogoUrl()", value -> {
                    Log.d(TAG, "" + value);
                });
                break;
        }
    }
    public static int countWordsUsingSplit(String input)
    { if (input == null || input.isEmpty()) { return 0; }
        return input.split("src=", -1).length-1;
    }
    private void load(final String html/*,String barcode*/){

        data.put(html);
        if (data.length() == 1){
            if (addedReceiptWebView == null){

            }else{

            }
            addedReceiptWebView.loadData(html, "text/html; charset=UTF-8","utf-8");
        }
        //addedReceiptWebView.loadData(html, "text/html; charset=UTF-8","utf-8");
    }
    @Override
    public void handleResult(Result result) {
        barcode_scanner.stopCamera();
        barcode_scanner.setVisibility(View.GONE);
        input_barcode.setText(result.getText());
    }
    public class MyJsInterface {
        private Context context;

        public MyJsInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public String getSerialNumber(){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                return Build.getSerial();
            }
            return "0";
        }
        @JavascriptInterface
        public void disableKeyboard(){

            handler.postDelayed(disableKeyboard,10);
        }
        @JavascriptInterface
        public void enableKeyboard() {
            handler.postDelayed(enableKeyboard,10);
//            MainActivity.this.webView.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
//            MainActivity.this.webView.setFocusable(true);
//            MainActivity.this.webView.setFocusableInTouchMode(true);
        }


        @JavascriptInterface
        public  void hideKeyboard(){
            Log.i("hideKeyboard","hideKeyboard");
            hideSoftKeyboard();
        }

        @JavascriptInterface
        public void openCreditCardPayment() {

            focusKeyboard = false;

        }
        @JavascriptInterface
        public void closeCreditCartPayment() {

            focusKeyboard = true;

        }

        @JavascriptInterface
        public void hideSoftKeyboard() {

            handler.postDelayed(closeKeyboard,300);

        }

        private final Runnable closeKeyboard = new Runnable() {
            @Override
            public void run() {
                MainActivity.this.hideSoftKeyboard(null);
            }
        };

        @JavascriptInterface
        public void printPrinterJsonStructure(String data, String barcode) {
            Log.d(TAG, data);
        }




        @JavascriptInterface
        public void open_drw(){
            openDrawer();
        }


        @JavascriptInterface
        public void print_invoice3( String s){
            try {
                s = s.replace("<table style=\"font-family: Courier New;font-size: 9px;font-weight: bold;direction: rtl;max-width: 150px!important;width: 150px;margin-left: 20px;\">", "<table style=\"font-family: Courier New;font-size: 15px;font-weight: bold;direction: rtl;width: 250px;margin-left: 5px;\">");
                print_invoice3(s, null,1);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        @JavascriptInterface
        public void print_invoice( final String s, final String barcode, int seconds){

        }
        @JavascriptInterface
        public void print_invoice2( final String s, final String barcode, int seconds){

        }
        @JavascriptInterface
        public void tara(){
            if (getPlatform() ==  PLATFORMS.IMIN) {
                mElectronic.removePeel();
            }else if (getPlatform() == PLATFORMS.UROVO){
                //open scanner;
                barcode_scanner.setVisibility(View.VISIBLE);
                barcode_scanner.setResultHandler(MainActivity.this); // Register ourselves as a handler for scan results.
                barcode_scanner.startCamera();


                // Programmatically initialize the scanner view

            }
        }

        @JavascriptInterface
        public void clear_weight(){
            if (getPlatform() == PLATFORMS.IMIN) {
                mElectronic.turnZero();
            }
        }

        @JavascriptInterface
        public double  getWeightBip30(){
            if(DOMAIN == "dangotandroid" || DOMAIN == "dangotandroid1"){
                return  MainActivity.this.getFromUsbWeightBip30Dangot();
            }
            else if (getPlatform() ==  PLATFORMS.iPOS || getPlatform() == PLATFORMS.SUNMI) {
                return MainActivity.this.getFromUsbWeightBip30();
            }
            else if (getPlatform() == PLATFORMS.IMIN){
                return MainActivity.this.getFromIminWeight();
            }
            return 0;
        }

        @JavascriptInterface
        public double  getWeightCS600(){
            if (getPlatform() ==  PLATFORMS.iPOS || getPlatform() ==  PLATFORMS.SUNMI)
                return  MainActivity.this.getFromUsbWeightCS600();
            else if (getPlatform() == PLATFORMS.IMIN){
                return MainActivity.this.getFromIminWeight();
            }
            return 0;
        }

        @JavascriptInterface
        public double  getWeightNewtech(){
            if (getPlatform() ==  PLATFORMS.iPOS || getPlatform() ==  PLATFORMS.SUNMI)
                return  MainActivity.this.getFromUsbWeightNewtech();
            else if (getPlatform() == PLATFORMS.IMIN){
                return MainActivity.this.getFromIminWeight();
            }
            return 0;
        }

        @JavascriptInterface
        public double  getWeight(){
            if (getPlatform() ==  PLATFORMS.iPOS || getPlatform() ==  PLATFORMS.SUNMI)
                return  MainActivity.this.getFromUsbWeightCS600();
            else if (getPlatform() == PLATFORMS.IMIN){
                return MainActivity.this.getFromIminWeight();
            }
            return 0;
        }
        @JavascriptInterface
        public void print_invoice_bon(String s, final String net) throws JSONException {
            POSInterfaceAPI interface_net = new POSNetAPI();
            //s="123456";
            POSSDK pos_sdk = new POSSDK(interface_net);
            int error_code = 0;
            String[] a_arr;
            int POSPORT = 9100;
            //int STATEPORT = 4000;
            error_code = interface_net.OpenDevice(net, POSPORT);
            if(error_code == 1001){

            }
            else {
                running = 0;
                byte[] send_buf = new byte[0];
                s = s.replace("[", "");
                s = s.replace("]", "");
                s = s.replace("\"", "");
                a_arr=s.split("[,]");

                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                String jsonString = gson.toJson(a_arr);
                jsonString = jsonString.replace("[", "");
                jsonString = jsonString.replace("]", "");
                jsonString = jsonString.replace("\"", "");
                jsonString = jsonString.replace(",", "");

                Bitmap c_image = pos_sdk.imageCreateRasterBitmap(jsonString,23,1);
                error_code = pos_sdk.imageStandardModePrint (c_image, 33, 10, 640);
                error_code = pos_sdk.systemCutPaper(CutPartAfterFeed, 80);
                interface_net = null;
            }
        }
        public Bitmap barcodeBitmap = null;
            /**
             *
             * Print HTML Invoice
             * @param s
             * @param barcode
             * @param seconds
             * @throws JSONException
             */
        @JavascriptInterface
        public void print_invoice3(final String s, final String barcode, int seconds) throws JSONException {
            running = 0;

            final String htmlTemp = s;


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (addedReceiptWebView == null){
                        loadWebView();
                    }
                    int font_size = 27;//30;

                    String _htmlTemp;
                    _htmlTemp = htmlTemp.replace("max-width: 150px!important;","");

                    if (getPlatform() == PLATFORMS.IMIN || getPlatform() == PLATFORMS.SUNMI ){
                        _htmlTemp = _htmlTemp.replaceAll("width: 250px;","");

                        _htmlTemp = htmlTemp.replace("margin-left: 5px","margin-left: 15px");
//                        _htmlTemp = _htmlTemp.replace(";width: 250px;",";width: 550px;");

                        _htmlTemp = _htmlTemp.replaceAll("width:110px;","width: 45%;");
                        _htmlTemp = _htmlTemp.replaceAll("width:120px;","width: 45%;");
                        _htmlTemp = _htmlTemp.replaceAll("width:70px;","width: 10%;");

                        Matcher m = p.matcher(_htmlTemp);

                        if (countWordsUsingSplit(_htmlTemp) == 2) {
                            // get the first
                            m.find() ;
                            downloadLogo(m.group().replace("src=\"","").replace("\"",""));
                        }
                        _htmlTemp = _htmlTemp+"<style type=\"text/css\">\timg{visibility: hidden;\theight: 0px;}\tbody{height: 0px;}</style>";

                      //  _htmlTemp = _htmlTemp.replace("max-width:170px;width:170px;","max-width:500px;width:450px;");
//                        _htmlTemp = _htmlTemp.replace("max-width:170px;width:170px;", "max-width:600px;width:600px;");
                        if (barcode != "0") {
                            MainActivity.this.barcode = barcode;
                            if(DOMAIN == "dangotandroid" || DOMAIN == "dangotandroid1"){

                            }
                        }
                    }else if (getPlatform() == PLATFORMS.UROVO){
                        font_size = 24;
                        if (barcode != "0")
                            MainActivity.this.barcode = barcode;

                        _htmlTemp = _htmlTemp+"<style type=\"text/css\">\timg{visibility: hidden;\theight: 0px;}\tbody{height: 0px;}</style>";

                        Matcher m = p.matcher(_htmlTemp);

                        if (countWordsUsingSplit(_htmlTemp) == 2) {
                            // get the first
                            m.find() ;
                            downloadLogo(m.group().replace("src=\"","").replace("\"",""));
                        }


                    }
                    else {
                        _htmlTemp = _htmlTemp.replace("max-width:170px;width:170px;", "max-width:350px;width:350px;");
                    }
                    if (getPlatform() == PLATFORMS.UROVO){
                        load("<html style=\"height: fit-content;\">" +
                                "<head>\n" +
                                "\t<style>\n" +
                                "\t\t   body{\n" +
                                "margin: unset;"+
                                "\t\t\tpadding: 0 !important;\n" +
                                "\t\t}\n" +
                                "\t\tbody > table{\n" +
                                "max-width:unset !important;"+
                                " font-size: "+font_size+"px !important;" +
                                "width: 93% !important;"+
                                "margin-left: 15px !important;;"+
                                "margin-right: 15px !important;;"+
                                "\t\t}\n" +
                                "\t</style>\n" +
                                "\t<script>\n console.log('height = Running 1');" +
                                "\t(function () {\n  console.log('height = Running2'); " +
                                "\tsetTimeout(function(){  console.log('height = Running3'); " +
                                "\t if (android != undefined) {\n "+
                                "\t android.height(document.getElementsByTagName(\"table\")[0].clientHeight );\n" +
                                "\t}\n" +
                                "},100);"+
                                "\t})();\n" +
                                "\t </script>\n" +
                                "</head>" +
                                "<body>"+_htmlTemp+"</body></html>");
                    }
                    else{
                        if(DOMAIN == "dangotandroid" || DOMAIN == "dangotandroid1") {
                            MainActivity.this.barcodeBitmap = generateBarcode(barcode, 340, 100);
                        }
                        load("<html style=\"height: fit-content;\">" +
                                "<head>\n" +
                                "\t<style>\n" +
                                "\t\t   body{\n" +
                                "margin: unset;"+
                                "\t\t\tpadding: 0 !important;\n" +
                                "\t\t}\n" +
                                "\t\tbody > table{\n" +
                                "max-width:unset !important;"+
                                " font-size: "+font_size+"px !important;" +
                                "width: 93% !important;"+
                                "margin-left: 15px !important;;"+
                                "margin-right: 15px !important;;"+
                                "\t\t}\n" +
                                "\t</style>\n" +
                                "\t<script>\n console.log('height = Running 1');" +
                                "\t(function () {\n  console.log('height = Running2'); " +
                                "\tsetTimeout(function(){  console.log('height = Running3'); " +
                                "\t if (android != undefined) {\n "+
                                "\t android.height(document.getElementsByTagName(\"table\")[0].clientHeight+300 );\n" +
                                "\t}\n" +
                                "},100);"+
                                "\t})();\n" +
                                "\t </script>\n" +
                                "</head>" +
                                "<body>"+_htmlTemp +
                                "\n" +"</body></html>");

                    }
                }
            });
        }

        @JavascriptInterface
        public void onData(String value) {
            //.. do something with the data
            Log.d(TAG, "************ " + value + "***********");
        }

        @JavascriptInterface
        public String swipeCard (Object input){

            return "nothing";
        }


        JSONObject credixResult = new JSONObject();
        //        OR -
        @JavascriptInterface
        public String startPayCredit(String itra) {
            Log.e(TAG, "startPayCredit");
            EnvPaymentItem paymentItem;

            if (getPlatform() == PLATFORMS.UROVO){
                paymentItem = new Gson().fromJson(itra, EnvPaymentItem.class);
                int pay_num = ((String)paymentItem.pay_num).isEmpty()? 0 : Integer.parseInt((String)paymentItem.pay_num);
                int ashray_f_credit = ((String)paymentItem.ashray_f_credit).isEmpty()? 0 : Integer.parseInt((String)paymentItem.ashray_f_credit);
                int moto = ((String)paymentItem.moto).isEmpty()? 0 :Integer.parseInt((String)paymentItem.moto);
                double pay_first = ((String)paymentItem.pay_num).isEmpty()? 0 : Double.parseDouble((String)paymentItem.pay_first);

                startUrovoPayment(Double.toString(paymentItem.amount),paymentItem.aproval_num_f,pay_num,pay_first,ashray_f_credit,moto,  "","","","");
                return "{\"code\":1}";
            }

            if (inProcess) return "{\"error\": \"fail get response\"}";

            try {
                long dif = System.currentTimeMillis() - lastCreditPayTime;
                if (dif < 1000 || dll != null) {
                    Log.e(TAG, "too dense `startP`ayCredit calls, last call was " + dif + " millis ago");
                    return credixResult.toString();
                }
                credixResult = new JSONObject();
                try {
                     paymentItem = new Gson().fromJson(itra, EnvPaymentItem.class);
                }catch(JsonSyntaxException ex){
                    Log.e(TAG, "No Input");
                    return "";

                }
                if (paymentItem.IpayPassword == null || paymentItem.IpayUserName == null){
                    credixResult = new JSONObject();
                    credixResult.put("error","fail get response");
                    return  credixResult.toString();
                }

                dll = new PinPadAPI();
                inProcess = true;
                int res;
//                if ( BuildConfig.DEBUG){
//                    //res = dll.open("192.168.1.117", paymentItem.IpayUserName, paymentItem.IpayPassword);
                    res = dll.open(paymentItem.IpayCom/*"192.168.0.110"*/, paymentItem.IpayUserName, paymentItem.IpayPassword);


//                }else {
                    res = dll.open(paymentItem.IpayCom/*"192.168.0.110"*/, paymentItem.IpayUserName, paymentItem.IpayPassword);
//                }


                JSONObject params = new JSONObject();



                String transactionType = "CHARGE";
                double amt = paymentItem.amount;
                if (amt < 0) {
                    transactionType = "CREDIT";
                    amt = Math.abs(amt);
                }

                String creditTerms  = "REGULAR_CREDIT";
                double numberOfPayments = 1;
                double pay_num = 0;
                double firstPaymentAmount = -1;
                double ashray_f_credit = 0;

                if (paymentItem.pay_num!= null){
                    if  (((String)paymentItem.pay_num).isEmpty() )
                        pay_num = 1;
                    else
                        pay_num =  Double.parseDouble((String) paymentItem.pay_num);
                }


                if (paymentItem.pay_first!= null){
                    if  (((String)paymentItem.pay_first).isEmpty()) {
//                        firstPaymentAmount = amt;
                    }else
                        firstPaymentAmount =  Double.parseDouble((String) paymentItem.pay_first);
                }
                if (paymentItem.ashray_f_credit!= null){
                    if  (((String)paymentItem.ashray_f_credit).isEmpty() )
                        ashray_f_credit = 0;
                    else
                        ashray_f_credit =  Double.parseDouble((String) paymentItem.ashray_f_credit);
                }

                double paymentAmount = paymentItem.amount;
                if (pay_num > 1) {
                    creditTerms = "PAYMENTS";
                    numberOfPayments = pay_num;
                    if (firstPaymentAmount > 0)
                    {
                        paymentAmount = (amt - firstPaymentAmount) / (pay_num - 1);
                        params.put("firstPaymentAmount", firstPaymentAmount);
                        params.put("paymentAmount", paymentAmount);

                    }
                    else
                    {
                        firstPaymentAmount =  Math.round(amt / pay_num);
                        params.put("firstPaymentAmount", firstPaymentAmount);
                        paymentAmount = (amt - firstPaymentAmount) / (pay_num - 1);
                        params.put("paymentAmount", paymentAmount);
                    }
                    numberOfPayments -= 1;
                    params.put("numberOfPayments", numberOfPayments);

                }
                if (ashray_f_credit > 0)
                {
                    creditTerms = "FIXED_INSTALMENT_CREDIT";
                    numberOfPayments = ashray_f_credit;
                    firstPaymentAmount = 0;
                    paymentAmount = 0;
                    params.put("firstPaymentAmount", firstPaymentAmount);
                    params.put("numberOfPayments", numberOfPayments);
                }

                JSONObject data = new JSONObject();
                params.put("amount", amt);
                params.put("creditTerms", creditTerms);

                params.put("currency", "ILS");
                params.put("printVoucher", false);
                params.put("requestType", "QUERY_EXECUTION_IN_ACCORDANCE_WITH_TERMINAL");
                params.put("transactionType", transactionType);
                data.put("params", params);

                // Send regular transaction
                PinPadSession requestSession = new PinPadSession();
                String type = "transaction";
                //String data = "{\"params\":{\"amount\":1.5,\"creditTerms\":\"REGULAR_CREDIT\",\"currency\":\"ILS\",\"printVoucher\":false,\"requestType\":\"QUERY_EXECUTION_IN_ACCORDANCE_WITH_TERMINAL\",\"transactionType\":\"CHARGE\"}}";

                int ret = dll.sendRequest(requestSession, type, data.toString());


                for (int i = 0; i < 60; i++) {
                    // Get transaction response
                    ret = dll.isResponseReady(requestSession, 1000);
                    if (ret > 0) {
                        break;
                    }
                }


                if (ret == 0) {
                    // Cancel transaction
                    PinPadSession cancelSession = new PinPadSession();
                    type = "cancel";
                    data = null;
                    // ret = dll.sendRequest(cancelSession, type, data.toString());
                    ret = dll.sendRequest(cancelSession, type, null);

                    // Get cancel response
                    ret = dll.isResponseReady(cancelSession, 1000);
                    if (ret > 0) {
                        PinPadResponse response = new PinPadResponse();
                        dll.getResponse(cancelSession, response, ret);
                        inProcess = false;
                        JSONObject js = new JSONObject(response.get());
                        if (js.optInt("code") == 60000){
                            credixResult.put("error", "עסקה בוטלה");
                            credixResult.put("code", "");

                            JSONObject inner = new JSONObject();
                            inner.put("message", "עסקה בוטלה");
                            inner.put("code", js.optString(""));

                            credixResult.put("data", inner);
                            dll.close();
                            dll = null;
                            return  credixResult.toString();
                        }
                        dll.close();
                        dll = null;
                        lastCreditPayTime = System.currentTimeMillis();
                        focusKeyboard = true;
                        return response.get();

//                        System.out.println(response);
                    }

                    ret = dll.isResponseReady(requestSession, 1000);
                }

                // Get transaction response
                if (ret > 0) {
                    PinPadResponse response = new PinPadResponse();
                    dll.getResponse(requestSession, response, ret);
                    inProcess = false;
                    try{
                        JSONObject js = new JSONObject(response.get());
                        if (js.optInt("code") > 60000   ){
                            credixResult.put("error","fail get response");
                            credixResult.put("code",js.optInt("code"));

                            JSONObject inner = new JSONObject();

                            if (js.has("data")){
                                inner.put("message", js.optJSONObject("data").optString("message") + " "+
                                        js.optJSONObject("data").optString("solek","") +" "+ js.optJSONObject("data").optString("solekTelNum",""));
                                inner.put("code",  "");
                            }else {
                                inner.put("message", js.optString("msg"));
                                inner.put("code", js.optString("1"));
                            }
                            credixResult.put("data",inner);
                            dll.close();
                            dll = null;
                            lastCreditPayTime = System.currentTimeMillis();
                            focusKeyboard = true;
                            return credixResult.toString();
//                            return "{\"error\": \"fail get response\",\"data\":{\"message\": "+js.optString("msg")+"}}";
                        }
                    }catch (Exception ex){
                        dll.close();
                        dll = null;
                        Log.i(TAG,"crashed");
                        ex.printStackTrace();
                    }
                    dll.close();
                    dll = null;
                    lastCreditPayTime = System.currentTimeMillis();
                    focusKeyboard = true;
                    return response.get();
                    // OR -  SHOULD WE PRINT SDOMETHING HERE
                }
                else if (ret < 0){
                    inProcess = false;

                    credixResult.put("error","fail get response");
                    credixResult.put("code","60002");

                    JSONObject inner = new JSONObject();
                    inner.put("message","ארעה שגיאה");
                    inner.put("code",-1);

                    credixResult.put("data",inner);
                    dll.close();
                    dll = null;
                    lastCreditPayTime = System.currentTimeMillis();
                    return credixResult.toString();
                }

                // if small than 0 return the ret
                lastCreditPayTime = System.currentTimeMillis();
                dll.close();
                dll = null;
            } catch (Exception e) {
                inProcess = false;

                e.printStackTrace();
            }
            lastCreditPayTime = System.currentTimeMillis();
            inProcess = false;
            dll = null;
            return "";
        }

//        public InnerScannerImpl mInnerScanner = null;
//        @JavascriptInterface
//        public void scanBarcoide(){
//
//        }

        @JavascriptInterface
        public void getUrovoStatusForCreditUsers(){

            getUrovoStatus();

        }

        @JavascriptInterface
        public String openPinPad(String com, String wisepayCode) {
            Api apiDll = new Api();
            String address = String.format("COM{0}", com);
            if (com.length() > 2)
            {
                address = com;
            }
            byte[] response = new byte[10];
            apiDll.Version(response, 10);
            apiDll.SetDebug(true);

            int ret1 = apiDll.Open2(address, wisepayCode);
            apiDll.SetDebug(true);
            if (ret1 != 0)
            {
                return "{\"error\": \"connection to wisepay failed " + "\" }";
            }
            return "ok";
        }
        @JavascriptInterface
        public String getUrovoTransactionsReport(){
            repotUrovo();
            return  "";
        }
        @JavascriptInterface
        public String getTransactionsReport(String com, String wisepayCode)
        {
            if (BuildConfig.DOMAIN == "liv1" || BuildConfig.DOMAIN == "liv"){
                    return "";
                }else{
                apiDll = new Api();
            String address = String.format("COM{0}", com);
            if (com.length() > 2)
            {
                address = com;
            }
            apiDll.SetDebug(true);
            //if (WisepayPinPad_Open2(address, "4633") != 0)//papa
            if (apiDll.Open2(address, wisepayCode) != 0)
            {
                return "{\"error\": \"connection towisepay failed " + "\" }";
            }
            int result = apiDll.DepositTxns2("REPORT_TYPE_XML", false, "deposit - context");

            String myRes = ReadReply(result, apiDll);

            if(myRes == "error"){
                return "{\"error\": \"fail send request\"}";
            }
            else{
                if (apiDll.Close() != 0)
                { String reportResponse = null;
//            if (!GetResponse(ref reportResponse, true))
//            {
//                return "{\"error\": \"fail send request\"}";
//            }
//            else
//            {
//                if (apiDll.Close() != 0)
//                {
//                    System.out.println("Failed to Got Response pinpad");
//                }
//                reportResponse = reportResponse.ToString();
//                byte[] bytes = Encoding.Default.GetBytes(reportResponse);
//                reportResponse = Encoding.UTF8.GetString(bytes);
//                return reportResponse;
//            }
                    Log.i(TAG,"Failed to Got Response pinpad");
                    //return "{\"error\": \"close\"}";
                }
            }
                return myRes;

            }
//
        }

        class MyResponse {
            // public member variable
            public String response;

            // default constructor
            public MyResponse()
            {
                response = null;
            }
        }

        private String ReadReply(int result, Api jniApi) {
            String response = "";
            if (result == 0) {
                int ret = 0;
                while (ret == 0) {
                    // Get transaction response
                    ret = jniApi.IsResponseReady(1000);
                    if (ret > 0) {
                        break;
                    }
                }
                if (ret > 0) {
                    byte[] nativeResponse = new byte[ret];
                    jniApi.GetResponse(nativeResponse, ret);
                    response = new String(nativeResponse,0,ret-1); // last byte is null termination
                    System.out.println(response);
                }
                else{
                    response = "error";
                }
            }
            return response;
        }

        //for liv
        @JavascriptInterface
        public String startPayCredit(String amount, String username, String password, int pay_num, double pay_first, int ashray_f_credit,int moto,  String com, String approvalNumber, String motoPanEntry)
        {
            return startPayCredit_both(amount,username,password,pay_num,pay_first,ashray_f_credit,moto,com,approvalNumber,motoPanEntry);
        }

        //for android
        @JavascriptInterface
        public String startPayCredit(String amount, String username, String password, int pay_num, double pay_first, int ashray_f_credit, String com, String approvalNumber, String motoPanEntry)
        {
            return startPayCredit_both(amount,username,password,pay_num,pay_first,ashray_f_credit,0,com,approvalNumber,motoPanEntry);
        }

        @JavascriptInterface
        public String startPayCredit_both(String amount, String username, String password, int pay_num, double pay_first, int ashray_f_credit,int moto,  String com, String approvalNumber, String motoPanEntry)
        {
            if (getPlatform() == PLATFORMS.UROVO){
                startUrovoPayment(amount,approvalNumber,pay_num,pay_first,ashray_f_credit,moto,  "","","","");
            }
            String transactionType = "TRANSACTION_TYPE_DEBIT";
            double amt = Double.parseDouble(amount);
            if (amt < 0)
            {
                transactionType = "TRANSACTION_TYPE_REFUND";
                amt = Math.abs(amt);
            }
//            int amountAsAgorot = (int)(amt * 100);
            int amountAsAgorot = (int)(Math.round(amt * 100));
            String creditTerms = "CREDIT_TERMS_REGULAR";
            int numberOfPayments = 1;
            double firstPaymentAmount = 0;
            double paymentAmount = 0;
            if (pay_num > 1)//tashlumim
            {
                creditTerms = "CREDIT_TERMS_PAYMENTS";
                numberOfPayments = pay_num;
                firstPaymentAmount = pay_first;
                if (pay_first < 1)
                {
                    firstPaymentAmount = amt - Math.round(amt / pay_num) * ((int)(pay_num) - 1);
                    paymentAmount = Math.round(amt / pay_num);
                }
                else
                {
                    firstPaymentAmount = pay_first;
                    paymentAmount = Math.round((amt - firstPaymentAmount) / (pay_num - 1));
                    firstPaymentAmount = amt - paymentAmount * (pay_num - 1);
                }
                numberOfPayments -= 1;
            }
            if (ashray_f_credit > 0)
            {
                creditTerms = "CREDIT_TERMS_CREDIT";
                numberOfPayments = ashray_f_credit;
                firstPaymentAmount = 0;
                paymentAmount = 0;
            }
            StringBuilder sb = new StringBuilder();
            String context = null;
            Pair<String, String> cmd = new Pair<String, String>("transaction", sb.toString());
            firstPaymentAmount = firstPaymentAmount * 100;
            int firstPaymentAmountAsInt = (int)(firstPaymentAmount);
            if(motoPanEntry != null) {
                context = "moto-context";
            }
            else {
                context = "sale - context";
            }
            apiDll = new Api();
            int result = apiDll.Txn(transactionType, amountAsAgorot, "ILS", creditTerms, numberOfPayments, firstPaymentAmountAsInt, (int)(paymentAmount * 100), numberOfPayments, "INDEX_PAYMENT_NONE", 0, approvalNumber, motoPanEntry, false, context, null);

            String myRes = ReadReply(result, apiDll);

            if(myRes == "error"){
                return "{\"error\": \"fail send request\"}";
            }
            else{
                if (apiDll.Close() != 0)
                {
                    Log.i(TAG,"Failed to Got Response pinpad");
                    //return "{\"error\": \"close\"}";
                }
                return myRes;
            }
        }
        private Boolean GetResponse(MyResponse response, Boolean allowCancel)
        {
            apiDll = new Api();
            _isCancelTransaction = false;
            int ret;
            Boolean error = false;
            do
            {
                ret = apiDll.IsResponseReady(250);
                if (ret < 0)
                {
                    Log.i(TAG, "[WisepayPinPad_IsResponseReady] failed, code = " + ret);
                    error = true;
                    break;
                }
                if (ret == 0)
                {
                    String msg = "[WisepayPinPad_IsResponseReady] Pending for response";
                    if (allowCancel)
                        msg += " (press ctrl+c to cancel)";
                    Log.i(TAG, msg);
                    continue;
                }
                BigInteger replyLenght = BigInteger.valueOf(ret);
                apiDll.GetResponse(replyLenght.toByteArray(), ret);
                Log.i(TAG, "[WisepayPinPad_GetResponse] Got Response: ");
                response.response = replyLenght.toString();
                Log.i(TAG, response.response);
                break;
            } while (!(_isCancelTransaction && allowCancel));
            if (error)
                return false;
            return true;
        }

        @JavascriptInterface
        public String startPayCredit(String amount, String username, String password, int pay_num, double pay_first, int ashray_f_credit, String com, String approvalNumber) {
            PinPadAPI dll = new PinPadAPI();
            Integer ret;

            String address = String.format("COM%s", com);
            if (com.length() > 2) address = com;

            int res = dll.open(address, username, password);
            if (res != 0) {
                return "{\"error\": \"fail com " + res + "\" }";
            }

            String transactionType = "CHARGE";
            double amt = Double.parseDouble(amount);
            if (amt < 0) {
                transactionType = "CREDIT";
                amt = Math.abs(amt);
            }

            String creditTerms = "REGULAR_CREDIT";
            int numberOfPayments = 1;
            double firstPaymentAmount = 0;
            double paymentAmount = 0;

            return "{\"error\": \"error\"}";
        }


        @JavascriptInterface
        public String start_credix(String amount, String username, String password, int pay_num,
                                   double pay_first, int ashray_f_credit, String com, String approvalNumber) {
            PinPadAPI dll = new PinPadAPI();
            Integer ret;

            String address = String.format("COM%s", com);
            if (com.length() > 2) address = com;

            int res = dll.open(address, username, password);
            if (res != 0) {
                return "{\"error\": \"fail com " + res + "\" }";
            }

            String transactionType = "CHARGE";
            double amt = Double.parseDouble(amount);
            if (amt < 0) {
                transactionType = "CREDIT";
                amt = Math.abs(amt);
            }

            String creditTerms = "REGULAR_CREDIT";
            int numberOfPayments = 1;
            double firstPaymentAmount = 0;
            double paymentAmount = 0;


            return "{\"error\": \"error\"}";
        }

        @JavascriptInterface
        public void swipeCard() {
            Log.d(TAG, "swipeCard");
        }

        @JavascriptInterface
        public void onLogoLoaded(final String logoUrl) {
            Log.d(TAG, "on logo loaded: " + logoUrl);
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_MULTIPLE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "Permission was granted!");
            } else {
                Log.e(TAG, "Permission was not granted!");
                Toast.makeText(MainActivity.this, "Permission must be granted", Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
            }
        }
    }
    /* NOT MINE */
    private IWoyouService woyouService;
    private ServiceConnection connService = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
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
    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
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

    private void pinPrint(Bitmap mBitmap)  {

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
    private void escPrint(Bitmap mBitmap, int mBmpPrintWidth)  {

        final int bmpPrintWidth = (mBmpPrintWidth > 72 )?72:mBmpPrintWidth;

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

    private void print_i_machine_barcode(Bitmap logo,Bitmap img,String barcode, int mBmpPrintWidth) {
        CmdFactory cmdFactory = new EscFactory();
        Cmd escCmd = cmdFactory.create();

        if (logo != null)
            escCmd = Utils.addBitmap(escCmd,logo,mBmpPrintWidth);
        escCmd = Utils.addBitmap(escCmd,img,mBmpPrintWidth);

        escCmd.append(escCmd.getHeaderCmd());


        CommonSetting commonSetting = new CommonSetting();
        commonSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
        escCmd.append(escCmd.getCommonSettingCmd(commonSetting));

        BarcodeSetting barcodeSetting = new BarcodeSetting();
        barcodeSetting.setBarcodeStringPosition(BarcodeStringPosition.BELOW_BARCODE);
        barcodeSetting.setEscBarcodePrintOritention(EscBarcodePrintOritention.Rotate0);
        barcodeSetting.setHeightInDot(96);//accept value:1~255
        barcodeSetting.setBarcodeWidth(3);//accept value:2~6
        barcodeSetting.setPosition(new Position(200,20));
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
        if (img.getVisibility() == View.VISIBLE)
            img.setVisibility(View.GONE);
        if (webView.getVisibility() == View.GONE)
            webView.setVisibility(View.VISIBLE);
        else if (webView.canGoBack()){
           // webView.goBack();
        }else
        {
            //super.onBackPressed();
        }
    }
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }

        Uri[] results = null;

        // Check that the response is a good one
        if(resultCode == Activity.RESULT_OK) {
            if(data == null) {
                // If there is not data, then we may have taken a photo
                if(mCameraPhotoPath != null) {
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
    private  void openBackOffice(String url){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        Intent intent = new Intent(this, BackOfficeActivity.class);
        intent.putExtra("URL",url);
        startActivity(intent);

    }
    private  void openLogin(String url){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("URL",url);
        startActivity(intent);
        finish();

    }
}
