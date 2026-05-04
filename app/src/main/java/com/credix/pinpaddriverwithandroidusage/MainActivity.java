package com.credix.pinpaddriverwithandroidusage;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Presentation;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.os.Parcel;
import android.os.Bundle;
import android.provider.Settings;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.content.ClipboardManager;
import android.content.ClipData;
import android.content.Context;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.device.DeviceManager;
import android.device.ScanManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.Typeface;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.media.MediaRouter;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.text.style.LeadingMarginSpan;
import android.text.style.LineHeightSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Pair;
import android.util.Printer;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
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
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
//import androidx.compose.ui.text.font.Font;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.provider.Settings;
import android.content.SharedPreferences;
import android.content.Context;
import androidx.core.content.ContextCompat;

import com.credix.pinpad.jni.PinPadAPI;
import com.credix.pinpad.jni.PinPadResponse;
import com.credix.pinpad.jni.PinPadSession;
import com.credix.pinpaddriverwithandroidusage.model.EnvPaymentItem;
import com.credix.pinpaddriverwithandroidusage.receiver.UsbDeviceReceiver;
import com.example.nexgolibrary.data.nexgo.device_config.NexgoDeviceConfig;
import com.example.nexgolibrary.data.nexgo.device_transaction.NexgoDeviceTransaction;
import com.example.nexgolibrary.domain.models.api.common.ClientDetails;
import com.example.nexgolibrary.utils.terminal.InitResCode;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
//import com.google.firebase.firestore.local.MemoryPersistence;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
//import com.google.zxing.Result;
import com.google.zxing.*;
//import com.google.zxing.common.BitMatrix; 17
//import com.google.zxing.oned.Code128Writer; 17
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.hoho.android.usbserial.driver.Ch34xSerialDriver;
import com.hoho.android.usbserial.driver.ProbeTable;
import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.neostra.electronic.Electronic;
import com.neostra.electronic.ElectronicCallback;
import com.nexgo.oaf.apiv3.DeviceEngine;
import com.nexgo.oaf.apiv3.SdkResult;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;
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
import com.rt.printerlibrary.factory.printer.PrinterFactory;
import com.rt.printerlibrary.setting.BarcodeSetting;
import com.rt.printerlibrary.setting.BitmapSetting;
import com.rt.printerlibrary.setting.CommonSetting;
//import com.urovo.sdk.scanner.InnerScannerImpl;
//import com.urovo.urovo.install.InstallManagerImpl;
import com.sun.jna.Pointer;
import com.wisepay.pinpad.Api;
import com.urovo.sdk.*;
//import com.urovo.sdk.system.SystemProviderImpl;

import com.caysn.autoreplyprint.*;
//import com.caysn.autoreplyprint.core.*;
//import com.caysn.autoreplyprint.support.*;

import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;

import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.eclipse.paho.mqttv5.client.MqttCallback;


import jpos.JposException;
import jpos.POSPrinter;
import jpos.POSPrinterConst;
import jpos.events.DataEvent;
import jpos.events.DataListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.math.BigInteger;
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
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android_serialport_api.SerialPort;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.credix.pinpaddriverwithandroidusage.R;
import com.semtom.weightlib.WeighUtils;
import com.semtom.weightlib.IReadWeightListener;

//import woyou.aidlservice.jiuiv5.IWoyouService;
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
//    public static String DOMAIN = "liv.yedatop";
//      public static String DOMAIN = Bu;


    /*SCREENS*/
//    public static final String MAIN_PATH = "https://kupa.yedatop.com";
//    public static final String MAIN_PATH = "https://office1.yedatop.com";
    public static final String MAIN_PATH = "https://"+DOMAIN;
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
    private boolean inProcess = false;
    private PinPadAPI dll;
    private Api apiDll;

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
            System.loadLibrary("serial_port");
            Log.d("SERIAL_SO", "loaded serial_port");
        } catch (Throwable e) {
            Log.e("SERIAL_SO", "failed to load serial_port", e);
        }

        try {
            System.loadLibrary("serialPort");
            Log.d("SERIAL_SO", "loaded serialPort");
        } catch (Throwable e) {
            Log.e("SERIAL_SO", "failed to load serialPort", e);
        }
    }

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
                    .setDevicePath("/dev/ttyS1").setBaudrate(9600)
                    .setReceiveCallback(iMinCallback)
                    .builder();
        }catch(Exception e){
            Log.e(TAG, "ERRRORRRRRRRRRRRR");
        }
    }
    public void testAllPorts() {
        String[] ports = {
                "/dev/ttyS0",
                "/dev/ttyS1",
                "/dev/ttyS2",
                "/dev/ttyS3",
                "/dev/ttyS4"
        };

        for (String port : ports) {
            try {
                Electronic testElectronic = new Electronic.Builder()
                        .setDevicePath(port)
                        .setBaudrate(9600)
                        .setReceiveCallback(new ElectronicCallback() {
                            @Override
                            public void electronicStatus(String weight, String status) {
                                Log.e("WEIGHT_TEST", "PORT " + port + " weight=" + weight + " status=" + status);
                            }
                        })
                        .builder();

                Log.e("WEIGHT_TEST", "Opened port " + port);

            } catch (Exception e) {
                Log.e("WEIGHT_TEST", "FAILED port " + port);
            }
        }
    }


    private double getFromIminWeight(){
        return iMinWeight;
    }

    //ניוטק איילון -------------------------------------------------------------------------------------
    private double getFromUsbWeightNewtech() {
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

    private static final Object SCALE_LOCK = new Object();
    private static UsbDeviceConnection sConn;
    private static UsbSerialPort sPort;
    private static Double sLastStable;    // משקל יציב אחרון
    private static long sLastStableAt;    // מתי נמדד
    private static final long FRESH_MS = 1200; // חלון "טריות"

    private double getFromUsbWeightScangle11() {
        // 0) אם יש ערך טרי—נחזיר מייד בלי USB
        synchronized (SCALE_LOCK) {
            if (sLastStable != null && (SystemClock.elapsedRealtime() - sLastStableAt) <= FRESH_MS) {
                return sLastStable;
            }
        }

        Context app = getApplicationContext();
        UsbManager manager = (UsbManager) app.getSystemService(Context.USB_SERVICE);
        if (manager == null) return -1;

        // 1) מאתרים דרייבר – קודם CH340 (1A86:7523), אחרת drivers.get(0)
        List<UsbSerialDriver> drivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (drivers.isEmpty()) return -1;

        UsbSerialDriver driver = null;
        for (UsbSerialDriver d : drivers) {
            UsbDevice dev = d.getDevice();
            if (dev.getVendorId() == 0x1A86 && dev.getProductId() == 0x7523) { // CH340
                driver = d; break;
            }
        }
        if (driver == null) driver = drivers.get(0);

        if (!manager.hasPermission(driver.getDevice())) return -1;

        // 2) פותחים פעם אחת ושומרים סטטית (למהירות בקריאות הבאות)
        synchronized (SCALE_LOCK) {
            try {
                if (sPort == null) {
                    sConn = manager.openDevice(driver.getDevice());
                    if (sConn == null) return -1;

                    sPort = driver.getPorts().get(0);
                    sPort.open(sConn);
                    try {
                        sPort.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
                    } catch (Exception e) {
                        // חלק מהמאזניים ב-7E1
                        sPort.setParameters(9600, 7, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_EVEN);
                    }
                    sPort.setDTR(true);
                    sPort.setRTS(true);

                    // Flush קצר מאוד (≈60–80ms)
                    byte[] tmp = new byte[256];
                    long flushUntil = SystemClock.elapsedRealtime() + 80;
                    while (SystemClock.elapsedRealtime() < flushUntil) {
                        int n = sPort.read(tmp, 20);
                        if (n <= 0) break;
                    }
                }
            } catch (Exception openErr) {
                Log.e("UDB", "USB open/config failed", openErr);
                safeCloseScale(); // נסגור סטטית אם נכשל
                return -1;
            }
        }

        // 3) קריאה מהירה עם יציבות קלה + עדכון קאש
        final int REQUIRED_CONSECUTIVE = 2;  // במקום 3 (מהיר יותר)
        final double TOL = 1.0;              // טולרנס 1g
        final int WARMUP_LINES = 1;          // זורקים שורה אחת
        final long MIN_GAP_MS = 40;          // מרווח מינ' בין דגימות
        final long DEADLINE_MS = 1200;       // דדליין קצר

        int warmupLeft = WARMUP_LINES;
        double prevVal = Double.NaN;
        int stableCount = 0;
        Double lastWeight = null;

        byte[] buf = new byte[256];
        StringBuilder lineBuf = new StringBuilder();
        long deadline = SystemClock.elapsedRealtime() + DEADLINE_MS;

        // (אופציונלי) שליחת פקודה – השאירי אם צריך; אם לא, אפשר לבטל כדי להרוויח ~80ms
        try {
            sPort.write("w\r\n".getBytes(StandardCharsets.US_ASCII), 150);
            try { Thread.sleep(60); } catch (InterruptedException ignored) {}
        } catch (Exception ignored) {}

        while (SystemClock.elapsedRealtime() < deadline) {
            int len;
            try {
                synchronized (SCALE_LOCK) {
                    if (sPort == null) return -1;
                    len = sPort.read(buf, 120);
                }
            } catch (Exception readErr) {
                Log.w("UDB", "USB read error, closing", readErr);
                safeCloseScale();
                return -1;
            }

            if (len <= 0) continue;

            String chunk = new String(buf, 0, len, StandardCharsets.US_ASCII);
            lineBuf.append(chunk);

            // צריכת שורות אחת-אחת
            int nl;
            while ((nl = indexOfNewline(lineBuf)) >= 0) {
                String line = consumeLine(lineBuf, nl);
                if (line.isEmpty()) continue;

                boolean stableFlag = line.startsWith("ST") || line.contains(" ST ");
                String num = line.replaceAll("[^0-9.\\-]", "");
                if (num.isEmpty() || ".".equals(num) || "-".equals(num)) continue;

                double w;
                try { w = Double.parseDouble(num); } catch (NumberFormatException e) { continue; }
                lastWeight = w;

                if (warmupLeft > 0) { warmupLeft--; continue; }

                if (stableFlag) {
                    // יציב מיידית לפי הדגל
                    synchronized (SCALE_LOCK) { sLastStable = w; sLastStableAt = SystemClock.elapsedRealtime(); }
                    return w;
                }

                long now = SystemClock.elapsedRealtime();
                // gap מינימלי לדגימה
                // (אם רוצים, אפשר לשמור lastTs ולהתחשב; בפועל 2 דגימות קרובות עובדות לרוב)
                if (Double.isNaN(prevVal) || Math.abs(w - prevVal) > TOL) {
                    prevVal = w; stableCount = 1;
                } else {
                    prevVal = w; stableCount++;
                }

                if (stableCount >= REQUIRED_CONSECUTIVE) {
                    synchronized (SCALE_LOCK) { sLastStable = w; sLastStableAt = now; }
                    return w;
                }
            }
        }

        // דדליין עבר: נחזיר מה שיש (אחרון), ונעדכן קאש אם נראה סביר
        if (lastWeight != null) {
            synchronized (SCALE_LOCK) { sLastStable = lastWeight; sLastStableAt = SystemClock.elapsedRealtime(); }
            return lastWeight;
        }
        return -1;
    }

    // ---- שתי עזרות זעירות בתוך אותה מחלקה (לא קלאס חדש) ----
    private static int indexOfNewline(CharSequence sb) {
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c == '\r' || c == '\n') return i;
        }
        return -1;
    }

    private static String consumeLine(StringBuilder sb, int nl) {
        String line = sb.substring(0, nl).trim();
        int c = nl;
        while (c < sb.length() && (sb.charAt(c) == '\r' || sb.charAt(c) == '\n')) c++;
        sb.delete(0, c);
        return line;
    }

    private static void safeCloseScale() {
        synchronized (SCALE_LOCK) {
            try { if (sPort != null) sPort.close(); } catch (Exception ignore) {}
            try { if (sConn != null) sConn.close(); } catch (Exception ignore) {}
            sPort = null; sConn = null;
        }
    }

    private double getFromUsbWeightScangle() {
        Context app = getApplicationContext(); // או requireContext().getApplicationContext() בפרגמנט
        UsbManager manager = (UsbManager) app.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> drivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (drivers.isEmpty()) return -1;
        UsbManager m = (UsbManager) getApplicationContext().getSystemService(Context.USB_SERVICE);
        for (UsbDevice d : m.getDeviceList().values()) {
            Log.i("USB", "VID=" + d.getVendorId() + " PID=" + d.getProductId()
                    + " devClass=" + d.getDeviceClass());
            for (int i=0;i<d.getInterfaceCount();i++) {
                Log.i("USB", " ifc " + i + " class=" + d.getInterface(i).getInterfaceClass());
            }
        }

        UsbSerialDriver driver = drivers.get(0);

// אם אין הרשאה – נבקש עכשיו ונחכה עד 1.2ש׳ (בלי רסיבר קבוע באפליקציה)
        if (!manager.hasPermission(driver.getDevice())) {
            final String ACTION_USB_PERMISSION = "com.yedatop.maytal.USB_PERMISSION";
            PendingIntent pi = PendingIntent.getBroadcast(
                    app, 0, new Intent(ACTION_USB_PERMISSION),
                    Build.VERSION.SDK_INT >= 31 ? PendingIntent.FLAG_MUTABLE : 0
            );

            final java.util.concurrent.CountDownLatch latch = new java.util.concurrent.CountDownLatch(1);
            BroadcastReceiver br = new BroadcastReceiver() {
                @Override public void onReceive(Context c, Intent intent) {
                    if (ACTION_USB_PERMISSION.equals(intent.getAction())) {
                        latch.countDown();
                    }
                }
            };

            IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);

            ContextCompat.registerReceiver(
                    getApplicationContext(),
                    br,
                    filter,
                    ContextCompat.RECEIVER_NOT_EXPORTED
            );
            try {
                manager.requestPermission(driver.getDevice(), pi);
                // מחכים עד 1.2 שנ׳ לאישור/דחייה מהמערכת
                latch.await(1200, java.util.concurrent.TimeUnit.MILLISECONDS);
            } catch (Exception ignore) {
            } finally {
                try { app.unregisterReceiver(br); } catch (Exception ignore) {}
            }

            // בדיקה שוב אחרי ההמתנה
            if (!manager.hasPermission(driver.getDevice())) {
                Log.w("USB", "אין הרשאה אחרי בקשה – חוזרים -1");
                return -1;
            }
        }

// מכאן – פתיחה בטוחה: תופסים גם SecurityException אם המערכת עוד לא עדכנה מצב
        UsbDeviceConnection connection;
        try {
            connection = manager.openDevice(driver.getDevice());
        } catch (SecurityException se) {
            Log.e("UsbManager", "SecurityException ב-openDevice: אין הרשאה פעילה", se);
            return -1;
        }
        if (connection == null) return -1;


        UsbSerialPort port = null;
        try {
            port = driver.getPorts().get(0);
            port.open(connection);
            port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
            port.setDTR(true);
            port.setRTS(true);

            // ניקוי הבאפר
            byte[] flush = new byte[256];
            while (port.read(flush, 50) > 0) {
                Log.i("UDB", "Flush old data...");
            }

            // שליחת פקודה
            String command = "w\r\n";
            port.write(command.getBytes(StandardCharsets.US_ASCII), 200);

            // זמן תגובה
            Thread.sleep(150);

            byte[] buf = new byte[256];
            long deadline = SystemClock.elapsedRealtime() + 1200; // עד 1.2 שניות
            StringBuilder acc = new StringBuilder();

            double lastWeight = -1;

            while (SystemClock.elapsedRealtime() < deadline) {
                int len = port.read(buf, 150);
                if (len > 0) {
                    String raw = new String(buf, 0, len, StandardCharsets.US_ASCII);
                    Log.i("UDB", "RAW chunk: [" + raw + "]");
                    acc.append(raw);

                    // מפצלים לשורות
                    String[] lines = acc.toString().split("[\\r\\n]+");
                    for (String line : lines) {
                        line = line.trim();
                        if (line.isEmpty()) continue;
                        try {
                            double w = Double.parseDouble(line.replaceAll("[^0-9.\\-]", ""));
                            lastWeight = w;
                            Log.i("UDB", "Parsed weight: " + w);
                            return w; // מחזירים ברגע שיש משקל תקין
                        } catch (NumberFormatException ignore) {}
                    }
                }
            }

            return lastWeight;

        } catch (Exception e) {
            Log.e("UDB", "Exception during USB read", e);
            return -1;
        } finally {
            try { if (port != null) port.close(); } catch (Exception ignore) {}
            try { connection.close(); } catch (Exception ignore) {
                Log.e("UDB", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAש", ignore);
            }
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

    //    //BIP 30--------------------------------------------------------------------------------------------
//    private double getFromUsbWeightBip30(){
//
//        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
//        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
//        if (availableDrivers.isEmpty()) {
//            return -1;
//        }
//
//        // Open a connection to the first available driver.
//        UsbSerialDriver driver = availableDrivers.get(0);
//        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
//        if (connection == null) {
//            // add UsbManager.requestPermission(driver.getDevice(), ..) handling here
//            return -1;
//        }
//        try{
//            int usbSerialPort = 9600;
//            UsbSerialPort port = driver.getPorts().get(0); // Most devices have just one port (port 0)
//            port.open(connection);
//
//            port.setParameters(usbSerialPort, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
//
//            port.write("w\r\n".getBytes(), 250);
//            byte[] buf = new byte[2048];
//            int len = port.read(buf, 1250);
//            if (len < 3)
//                return -1;
//            Log.i("UDB, ",new String(buf));
//            String  weightStr = new String(buf).replace("\r","").replace("\n","").replace("g","");
//            double  weight  = new Double(weightStr);
//            return  weight;
//        }catch(Exception e){
//            return -1;
//        }
//    }
// Helper method to clear the buffer manually
    private void clearBuffer(UsbSerialPort port) {
        try {
            byte[] buffer = new byte[64];
            while (port.read(buffer, 100) > 0) {
                // Continue reading until buffer is empty
            }
        } catch (Exception e) {
            Log.e("USB Error", "Error clearing buffer", e);
        }
    }

    // Helper method to parse weight from different formats
    private double parseWeight(String weightStr) throws NumberFormatException {
        if (weightStr.startsWith(".")) {
            weightStr = "0" + weightStr; // Add leading zero if the string starts with a dot
        }
        return Double.parseDouble(weightStr);
    }
    //BIP 30--------------------------------------------------------------------------------------------
    private double getFromUsbWeightBip30() {
        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
        if (availableDrivers.isEmpty()) {
            Log.e("USB Error", "No USB drivers available");
            return -1;
        }

        // Open a connection to the first available driver.
        UsbSerialDriver driver = availableDrivers.get(0);
        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
        if (connection == null) {
            Log.e("USB Error", "Failed to open USB connection. Requesting permission.");
//            PendingIntent permissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
//            manager.requestPermission(driver.getDevice(), permissionIntent);
            return -199;
        }

        UsbSerialPort port = driver.getPorts().get(0); // Most devices have just one port (port 0)
        try {
            int baudRate = 9600;
            port.open(connection);
            port.setParameters(baudRate, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

            // Manually clear any previous data in the buffer
            clearBuffer(port);

            // Send the command to request weight measurement
            int writeTimeout = 1000; // Timeout for writing command
            port.write("w\r\n".getBytes(StandardCharsets.UTF_8), writeTimeout);
            Log.i("USB Command", "Sent: w");

            // Shorter wait for the device to process the command
            Thread.sleep(300); // Initial delay

            byte[] buffer = new byte[64];
            int totalLen = 0;
            StringBuilder responseBuilder = new StringBuilder();

            for (int attempt = 0; attempt < 3; attempt++) { // Try reading multiple times with fewer attempts
                int len = port.read(buffer, 1500); // Shorter timeout for reading response
                if (len > 0) {
                    totalLen += len;
                    responseBuilder.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
                    Log.i("USB Data", "Read attempt " + attempt + ": " + responseBuilder.toString());
                    if (responseBuilder.toString().contains("\n")) { // Assuming a newline terminates the response
                        break;
                    }
                }
                Thread.sleep(100); // Short delay between reads
            }

            if (totalLen < 1) {
                Log.e("USB Error", "Received insufficient data");
                return -1;
            }

            String response = responseBuilder.toString();
            Log.i("USB Data", "Final Response: " + response);

            // Check if response contains valid data
            if (response == null || response.trim().isEmpty()) {
                Log.e("USB Data", "Received null or empty response");
                return -1;
            }

            // Clean and parse the response string
            String weightStr = response.replace("\r", "").replace("\n", "").replace("g", "").trim();
            Log.i("Parsed Weight String", "Weight String: " + weightStr);

            if (weightStr.isEmpty()) {
                Log.e("Weight Error", "Weight string is empty after parsing");
                return -1;
            }

            try {
                double weight = parseWeight(weightStr);
                if (weight == 0.0) {
                    Log.e("Weight Error", "Weight is zero, possibly incorrect reading");
                    return -1;
                }
                return weight;
            } catch (NumberFormatException e) {
                Log.e("Parse Error", "Failed to parse weight: " + weightStr, e);
                return -1;
            }

        } catch (Exception e) {
            Log.e("USB Exception", "Error reading weight", e);
            return -1;
        } finally {
            try {
                if (port != null) {
                    port.close();
                }
            } catch (Exception e) {
                Log.e("USB Exception", "Error closing port", e);
            }
        }
    }

//    //CS600--------------------------------------------------------------------------------------------
//    private double getFromUsbWeightCS600(){
//
//        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
//        List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
//        if (availableDrivers.isEmpty()) {
//            return -1;
//        }
//
//        // Open a connection to the first available driver.
//        UsbSerialDriver driver = availableDrivers.get(0);
//        UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
//        if (connection == null) {
//            // add UsbManager.requestPermission(driver.getDevice(), ..) handling here
//            return -1;
//        }
//        try{
//            int usbSerialPort = 9600;
//            UsbSerialPort port = driver.getPorts().get(0); // Most devices have just one port (port 0)
//            port.open(connection);
//
//            port.setParameters(usbSerialPort, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
//
//
//            byte[] buf1 = new byte[2048];
//            int aa;
//            do{
//                aa=port.read(buf1,250);
//            }while (aa>0);
//            port.write("w\r\n".getBytes(), 250);//5050
//            byte[] buf = new byte[2048];
//            // Thread.sleep(1000);
//
//            int len = port.read(buf, 250);//3500
//            if (len < 3)
//                return -1;
//            Log.i("UDB, ",new String(buf));
//            String  weightStr = new String(buf).replace("\r","").replace("\n","").replace("g","");
//            String ss = weightStr.substring(0, 6);
//            double  weight  = new Double(ss);
//            return  weight;
//
//
//        }catch(Exception e){
//            return -1;
//        }
//    }

    // ==================== ADYTECH ====================

    private static final String ADYTECH_TAG = "ADYTECH";
    private static final int ADYTECH_STABLE_THRESHOLD = 2;
    private static final double ADYTECH_EPSILON = 0.005d;
    private static final long ADYTECH_STALE_MS = 4000L;

    private WeighUtils adytechWeighUtils;
    private IReadWeightListener adytechReadWeightListener;

    private volatile double adytechCurrentWeight = 0.0;
    private volatile boolean adytechReady = false;
    private volatile boolean adytechHasReading = false;
    private volatile long adytechLastUpdateTs = 0L;

    private double adytechLastWeight = Double.NaN;
    private int adytechStableCount = 0;

    private synchronized void initAdytechScaleIfNeeded() {
        if (adytechReady && adytechWeighUtils != null) {
            return;
        }

        try {
            Log.i(ADYTECH_TAG, "initAdytechScaleIfNeeded: start");

            adytechCurrentWeight = 0.0;
            adytechHasReading = false;
            adytechLastUpdateTs = 0L;
            adytechLastWeight = Double.NaN;
            adytechStableCount = 0;

            adytechWeighUtils = WeighUtils.builder();

            adytechReadWeightListener = new IReadWeightListener() {
                @Override
                public void onReadingWeight(double d, int status, int tare) {
                    adytechCurrentWeight = d;
                    adytechHasReading = true;
                    adytechLastUpdateTs = System.currentTimeMillis();

                    Log.d(ADYTECH_TAG, "onReadingWeight: weight=" + d
                            + ", status=" + status
                            + ", tare=" + tare
                            + ", ts=" + adytechLastUpdateTs);
                }

                @Override
                public void onError(String msg) {
                    Log.e(ADYTECH_TAG, "Scale error: " + msg);
                }

                @Override
                public void initValue(String value) {
                    Log.i(ADYTECH_TAG, "initValue: " + value);
                }
            };

            adytechWeighUtils.open();
            adytechWeighUtils.bindWeightRead(adytechReadWeightListener);

            adytechReady = true;

            Log.i(ADYTECH_TAG, "init success. path=" + adytechWeighUtils.getPath()
                    + ", baud=" + adytechWeighUtils.getBaudrate());

        } catch (Exception e) {
            Log.e(ADYTECH_TAG, "init failed", e);
            adytechReady = false;
            adytechWeighUtils = null;
            adytechReadWeightListener = null;
        }
    }

    private double getFromAdytechWeight() {
        ScaleManager scaleManager = ScaleManager.getInstance();
        return scaleManager.getLastWeight();
    }

    private void resetAdytechState() {
        adytechCurrentWeight = 0.0;
        adytechHasReading = false;
        adytechLastUpdateTs = 0L;
        adytechLastWeight = Double.NaN;
        adytechStableCount = 0;
    }

    private void adytechTare() {
        try {
            if (adytechWeighUtils != null) {
                Log.i(ADYTECH_TAG, "tare()");
                adytechWeighUtils.resetBalance_qp();
                resetAdytechState();
            }
        } catch (Exception e) {
            Log.e(ADYTECH_TAG, "adytechTare failed", e);
        }
    }

    private void adytechZero() {
        try {
            if (adytechWeighUtils != null) {
                Log.i(ADYTECH_TAG, "zero()");
                adytechWeighUtils.resetBalance();
                resetAdytechState();
            }
        } catch (Exception e) {
            Log.e(ADYTECH_TAG, "adytechZero failed", e);
        }
    }

    private synchronized void releaseAdytechScale() {
        try {
            if (adytechWeighUtils != null && adytechReadWeightListener != null) {
                adytechWeighUtils.unbindWeightRead(adytechReadWeightListener);
            }
        } catch (Exception e) {
            Log.e(ADYTECH_TAG, "unbind failed", e);
        }

        try {
            if (adytechWeighUtils != null) {
                adytechWeighUtils.cwloseSerialPort();
            }
        } catch (Exception e) {
            Log.e(ADYTECH_TAG, "close serial failed", e);
        }

        adytechWeighUtils = null;
        adytechReadWeightListener = null;
        adytechReady = false;
        resetAdytechState();
    }
    UsbSerialPort port;
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
            port = driver.getPorts().get(0); // Most devices have just one port (port 0)
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

    public double readWeightDirectlyFromUsb(Context context) {
        double weight = com.yedatop.util.USBWeightReader.readWeight(getApplicationContext());
        if (weight == -1) {
            Log.e("App", "Failed to read weight.");
        } else {
            Log.i("App", "Weight is: " + weight);
        }
        return weight;
    }





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
        if (!"liv.yedatop.com".equals(BuildConfig.DOMAIN) && !"peleliv2.yedatop".equals(BuildConfig.DOMAIN) && !"pos.pelecash.co.il".equals(BuildConfig.DOMAIN) && !"liv2.yedatop".equals(BuildConfig.DOMAIN) && !"mayafood.yedatop".equals(BuildConfig.DOMAIN) && !"dangot1.yedatop".equals(BuildConfig.DOMAIN) && !"dangot.yedatop".equals(BuildConfig.DOMAIN)  && !"dangotandroid.yedatop".equals(BuildConfig.DOMAIN)&& !"android.yedatop".equals(BuildConfig.DOMAIN) && input_barcode != null )//DANGOT UROVO
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
        try{
            mScanManager = new ScanManager();
            boolean powerOn = mScanManager.getScannerState();
        }catch(Exception e){}


    }

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat timeFormat =
            new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

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
        } catch (Exception ignored) {}
    }


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
//            input_barcode.requestFocus();
            // input_barcode.clearFocus();
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
                    " }else if (document.querySelector('.text.zicuy_txt.zicuy_txt3.border2') != undefined && $('.shovarzicuy').css('display') == 'block'){ \n" +
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

        Log.d("BarcodeInput", "Input finalized: " + s.toString());
        Log.i("afterTextChanged",s.toString());
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
        Log.i("beforeTextChanged",s.toString());

        input_barcode.removeTextChangedListener(this);
        this.s = s.toString();
        handler.postDelayed(beforeText, 350);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
        imm.hideSoftInputFromWindow(input_barcode.getWindowToken(), 0);
        //end for android with scanner

//        start for dangot and liv APOLO, not working in android with scanner rachel
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
//        if (DOMAIN == "dangotandroid" || DOMAIN == "dangotandroid1") {
//            handler.postDelayed(beforeText, 1000);
//        }
//        else {
//            handler.postDelayed(beforeText, 350);//350}
//        }
//        end for dangot and liv, not working in android with scanner
    }


    void hideSoftKeyboardd(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            boolean result = imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            Log.d("KeyboardHide", "Hide keyboard result: " + result);
        }
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
        releaseAdytechScale();
    }
    private NexgoJavaWrapper nexgoJavaWrapper;

    private NexgoLibraryEventsListener nexgoListener;
    private TextView transactionTextView, pinTextView;

    private void nexgoCancelTransaction(NexgoJavaWrapper nexgoJavaWrapper) {
        nexgoJavaWrapper.cancelTransaction();
    }

    private NexgoDeviceController nexgoDeviceController;
    private String deviceId;  // 👈 משתנה גלובלי

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("DOMAIN_TEST", "DOMAIN=" + DOMAIN);
        Log.e("DOMAIN_TEST", "== dangot1 ? " + (DOMAIN == "dangot1.yedatop"));
        Log.e("DOMAIN_TEST", "equals dangot1 ? " + "dangot1.yedatop".equals(DOMAIN));
        if(!"liv.yedatop.com".equals(BuildConfig.DOMAIN) && !"liv1.yedatop".equals(BuildConfig.DOMAIN)&& !"mayafood.yedatop".equals(BuildConfig.DOMAIN) && !"peleliv2.yedatop".equals(BuildConfig.DOMAIN) && !"pos.pelecash.co.il".equals(BuildConfig.DOMAIN) && !"liv2.yedatop".equals(BuildConfig.DOMAIN)) {
            updateLocale(this);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Utils.hideSystemUI(getWindow());
        super.onCreate(savedInstanceState);
        //
        // .init();
        //setupTechnicianAccess();

        ScaleManager.getInstance();
        if("android.yedatop.com".equals(BuildConfig.DOMAIN) || "office1.yedatop.com".equals(BuildConfig.DOMAIN)){
            deviceId = Settings.Secure.getString(
                    getContentResolver(),
                    Settings.Secure.ANDROID_ID
            );
        } else {
            //לשים בהערה אם לא נקסגו וכן אנדרואיד
            nexgoDeviceController = new NexgoDeviceController(this); // יש לך context פה for pelecard
            deviceId = nexgoDeviceController.getDeviceSerial();//for android "0". pelecard getserial
        }
        Log.i("DEVICE_ID" ,deviceId );
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        // getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
        //WindowManager.LayoutParams.FLAG_SECURE);

        Utils.printInputLanguages(this);
        //for debug in chrome

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        FirebaseCrashlytics.getInstance().sendUnsentReports();

        baseApp = (BaseApp) getApplication();
        if(!"liv.yedatop.com".equals(BuildConfig.DOMAIN) && !"liv1.yedatop".equals(BuildConfig.DOMAIN) && !"mayafood.yedatop".equals(BuildConfig.DOMAIN) && !"peleliv2.yedatop".equals(BuildConfig.DOMAIN) && !"pos.pelecash.co.il".equals(BuildConfig.DOMAIN) && !"liv2.yedatop".equals(BuildConfig.DOMAIN) && BuildConfig.DOMAIN != "dangot.yedatop" && BuildConfig.DOMAIN != "dangot1.yedatop"){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        }

        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_UNCHANGED
                        | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        );

        boolean isExcludedDomain =
                "liv.yedatop.com".equals(BuildConfig.DOMAIN) ||
                        "liv1.yedatop".equals(BuildConfig.DOMAIN) ||
                        "peleliv2.yedatop".equals(BuildConfig.DOMAIN) ||
                        "pos.pelecash.co.il".equals(BuildConfig.DOMAIN) ||
                        "liv2.yedatop".equals(BuildConfig.DOMAIN) ||
                        "mayafood.yedatop".equals(BuildConfig.DOMAIN) ||
                        "dangot.yedatop".equals(BuildConfig.DOMAIN) ||
                        "dangot1.yedatop".equals(BuildConfig.DOMAIN) ||
                        "dangotandroid.yedatop".equals(BuildConfig.DOMAIN) ||
                        "dangotandroid1.yedatop".equals(BuildConfig.DOMAIN);

        if (!isExcludedDomain
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && input_barcode != null) { // DANGOT            WebView.enableSlowWholeDocumentDraw();
            input_barcode.addTextChangedListener(this);
        }
        else{
            // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE | WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
        }
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        if ("dangot1.yedatop".equals(DOMAIN)
                || "dangotandroid.yedatop".equals(DOMAIN)
                || "dangotandroid1.yedatop".equals(DOMAIN)) {
            setContentView(R.layout.activity_main_login);
        }
        else{
            Log.e("BOOT", "DOMAIN=" + DOMAIN + " BuildConfig.DOMAIN=" + BuildConfig.DOMAIN);
            Log.e("BOOT", "layout set, before findViewById");
            setContentView(R.layout.activity_main);
        }
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


        img = (ImageView)findViewById(R.id.img);
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
        //testAllPorts();
        initWebView();
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            Log.e("DEVICE_OVERLAY", "FORCE SHOW FROM ONCREATE");
//            addDeviceIdNativeOverlay();
//        }, 800);
        boolean isExcludedDomainn =
                "liv.yedatop.com".equals(BuildConfig.DOMAIN) ||
                        "liv1.yedatop".equals(BuildConfig.DOMAIN) ||
                        "peleliv2.yedatop".equals(BuildConfig.DOMAIN) ||
                        "pos.pelecash.co.il".equals(BuildConfig.DOMAIN) ||
                        "liv2.yedatop".equals(BuildConfig.DOMAIN) ||
                        "mayafood.yedatop".equals(BuildConfig.DOMAIN) ||
                        "dangot1".equals(BuildConfig.DOMAIN) ||
                        "dangot".equals(BuildConfig.DOMAIN) ||
                        "dangotandroid".equals(BuildConfig.DOMAIN) ||
                        "dangotandroid1".equals(BuildConfig.DOMAIN);

        if (!isExcludedDomainn) { // DANGOT            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            setListenerToRootView();
        }
        else{
            if ("liv.yedatop.com".equals(BuildConfig.DOMAIN) ||
                    "liv1.yedatop".equals(BuildConfig.DOMAIN) ||
                    "peleliv2.yedatop".equals(BuildConfig.DOMAIN) ||
                    "pos.pelecash.co.il".equals(BuildConfig.DOMAIN) ||
                    "liv2.yedatop".equals(BuildConfig.DOMAIN) ||
                    "mayafood.yedatop".equals(BuildConfig.DOMAIN)) {
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
        Log.e("INIT", "after printer");Log.e("INIT", "before registerPrinter");
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
        } catch (Exception ignored) {}
    }

    private void addDeviceIdNativeOverlay() {
        try {
            String idToShow = deviceId;

            if (idToShow == null || idToShow.trim().isEmpty()) {
                idToShow = Settings.Secure.getString(
                        getContentResolver(),
                        Settings.Secure.ANDROID_ID
                );
            }

            if (deviceIdOverlay != null) {
                try {
                    ((ViewGroup) deviceIdOverlay.getParent()).removeView(deviceIdOverlay);
                } catch (Exception ignored) {}
                deviceIdOverlay = null;
            }

            LinearLayout card = new LinearLayout(this);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setGravity(Gravity.CENTER);
            card.setPadding(16, 12, 16, 12); // היה 24/18
            card.setElevation(99999f);

            GradientDrawable bg = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{
                            Color.parseColor("#1E3A8A"),
                            Color.parseColor("#2563EB"),
                            Color.parseColor("#06B6D4")
                    }
            );
            bg.setCornerRadius(20);
            bg.setAlpha(230); // קצת שקוף
            card.setBackground(bg);

            TextView title = new TextView(this);
            title.setText("מזהה קופה לטכנאי");
            title.setTextColor(Color.WHITE);
            title.setTextSize(15);
            title.setTypeface(null, Typeface.BOLD);
            title.setGravity(Gravity.CENTER);

            TextView idText = new TextView(this);
            idText.setText(idToShow);
            idText.setTextColor(Color.WHITE);
            idText.setTextSize(14);
            idText.setTypeface(null, Typeface.BOLD);
            idText.setGravity(Gravity.CENTER);
            idText.setPadding(0, 4, 0, 6); // קטן יותר

            ImageView barcodeView = new ImageView(this);
            Bitmap barcode = generateBarcode(idToShow, 420, 100);

            if (barcode != null) {
                barcodeView.setImageBitmap(barcode);
                barcodeView.setBackgroundColor(Color.WHITE);
                barcodeView.setPadding(6, 6, 6, 6); // היה 10

                LinearLayout.LayoutParams barcodeParams = new LinearLayout.LayoutParams(
                        320,
                        80
                );
                barcodeView.setLayoutParams(barcodeParams);
            }

            card.addView(title);
            card.addView(idText);

            if (barcode != null) {
                card.addView(barcodeView);
            }

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            params.bottomMargin = 45;

            addContentView(card, params);

            deviceIdOverlay = card;
            card.bringToFront();

            final String finalId = idToShow;

            card.setOnClickListener(v -> {
                ClipboardManager clipboard =
                        (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                ClipData clip = ClipData.newPlainText("Device ID", finalId);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(this, "המזהה הועתק", Toast.LENGTH_SHORT).show();
            });

        } catch (Exception e) {
            Log.e("DEVICE_OVERLAY", "failed to show overlay", e);
            Toast.makeText(this, "שגיאה בהצגת מזהה", Toast.LENGTH_LONG).show();
        }
    }
    private Bitmap generateBarcode(String data, int width, int height) {
        try {
            BitMatrix bitMatrix = new Code128Writer().encode(
                    data,
                    BarcodeFormat.CODE_128,
                    width,
                    height
            );

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

            if (techClickCount >= 7) {
                techClickCount = 0;
                showDeviceIdDialog();
                return true;
            }
        }

        return super.dispatchTouchEvent(ev);
    }

    private void showDeviceIdDialog() {
        String deviceId = Settings.Secure.getString(
                getContentResolver(),
                Settings.Secure.ANDROID_ID
        );

        View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_device_id, null);

        TextView deviceIdText = view.findViewById(R.id.deviceIdText);
        Button copyButton = view.findViewById(R.id.copyButton);

        deviceIdText.setText(deviceId);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        copyButton.setOnClickListener(v -> {
            ClipboardManager clipboard =
                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

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

    private void registerBroadcastReceiver() {
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

    private void unregisterBroadcastReceiver() {
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
                String topText=message;
                if(BuildConfig.DOMAIN == "liv2.yedatop" || BuildConfig.DOMAIN == "peleliv2.yedatop" || BuildConfig.DOMAIN == "pos.pelecash.co.il"){
                    if ("הכנס / הצג / העבר כרטיס".equals(topText)) {
                        topText = "הכנס / הצמד / העבר";
                    }
                    else if ("כרטיס זוהה, המתן לאישור".equals(topText)) {
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


                    }
                    else if ("הכנס / הצג כרטיס".equals(topText)) {
                        topText = "הכנס / הצמד / העבר";
                    }

                    else {
                        topText = message;
                    }
                }
                else{
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
                if(BuildConfig.DOMAIN == "liv2.yedatop" || BuildConfig.DOMAIN == "peleliv2.yedatop" || BuildConfig.DOMAIN == "pos.pelecash.co.il"){
                    transactionTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                    drawable.setColor(Color.parseColor("#097999"));
                    transactionTextView.setGravity(Gravity.CENTER);
                    transactionTextView.setTextColor(Color.parseColor("#B5D1FF"));
                    transactionTextView.setPadding(0, 100, 0, 150);
                    if ("מבצע חיוב...".equals(topText)) {
                        transactionTextView.setPadding(0, 10, 0, 300);
                    }
                }
                else{
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
            ActivityCompat.requestPermissions(this,
                    permissionsNeeded.toArray(new String[0]),
                    REQUEST_PERMISSION_MULTIPLE);
        }
    }
    private void checkStoragePermission2() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            List<String> permissionsNeeded = new ArrayList<>();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_MEDIA_IMAGES);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_MEDIA_VIDEO);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.READ_MEDIA_AUDIO);
            }

            // POST_NOTIFICATIONS נוספה באנדרואיד 13 (API 33)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionsNeeded.add(Manifest.permission.POST_NOTIFICATIONS);
            }

            if (!permissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(this,
                        permissionsNeeded.toArray(new String[0]),
                        REQUEST_PERMISSION_MULTIPLE);
            }

        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_MULTIPLE);
            }
        }
    }

//    private void checkStoragePermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            // אם זה Android 13 ומעלה, יש להשתמש בהרשאות החדשות
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED &&
//                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO, Manifest.permission.READ_MEDIA_AUDIO},
//                        REQUEST_PERMISSION_MULTIPLE);
//            }
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.POST_NOTIFICATIONS},
//                        REQUEST_PERMISSION_MULTIPLE);
//            }
//        } else {
//            // אם הגרסה היא לפני 13, תשתמש בהרשאות הישנות
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//            }
//        }
//
//    }

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
        webView.loadUrl("javascript:(function setBarcode(text) { " +
                "if (document.activeElement.id == 'search_prod') { " +
                "$('#search_prod')[0].value = text; " +
                "$('#search_prod')[0].click(); " +
                "} " +
                "else if ($('.keyboard').css('display') != 'none' || document.activeElement.id == 'search_keyboard2') { " +
                "setTimeout(function() { " +
                "console.log('yayy ' + text); " +
                "$('#search_keyboard2').val(text); " +
                "$('.keyboard_result')[0].value = text; " +
                "}, 1); " +
                "} " +
                "else if (document.querySelector('.text.zicuy_txt.zicuy_txt3.border2') != undefined) { " +
                "document.querySelector('.text.zicuy_txt.zicuy_txt3.border2').value = text; " +
                "} " +
                "else if (document.activeElement.id == '' || document.activeElement.id == ' ') { " +
                "$('#search_prod')[0].value = text; " +
                "$('#search_prod')[0].click(); " +
                "} " +
                "})('" + text + "')");
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
    private String constructUrlBasedOnZicuy(String zicuy) {
        // This method is just a placeholder - customize as needed!
        return "https://liv.yedatop.com/modules/stock/cashbox_fe/index.php";
    }

    private final Runnable disableKeyboard = new Runnable() {
        @Override
        public void run() {
            MainActivity.this.disableKeyboard();
        }
    };
    public void disableKeyboard(){
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
    public void enableKeyboard(){
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

        if (webView != null){
            webView.reload();
        }
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.

        final MyJsInterface jsInterface = new MyJsInterface(MainActivity.this);

        webView = (WebView) this.findViewById(R.id.fullscreen_content);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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

        if (BuildConfig.DOMAIN != "liv.yedatop.com" && BuildConfig.DOMAIN != "peleliv2.yedatop" && BuildConfig.DOMAIN != "pos.pelecash.co.il" && BuildConfig.DOMAIN != "liv2.yedatop" && BuildConfig.DOMAIN != "mayafood.yedatop" && BuildConfig.DOMAIN != "dangot1.yedatop" && BuildConfig.DOMAIN != "dangot.yedatop" && BuildConfig.DOMAIN != "dangotandroid.yedatop")//DANGOT
        {
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
                boolean isBackOffice =
                        lower.contains("main2")
                                || (!domainSafe.isEmpty() && url.equalsIgnoreCase("https://" + domainSafe + ".com/modules/stock"))
                                || lower.contains("basket")
                                || lower.contains("listing")
                                || lower.contains("rep")
                                || lower.contains("shaon")
                                || lower.contains("stock/cp");

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

//        if(DOMAIN == "dangot1") {
//            String sn = "0";
//
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                sn = Build.getSerial();
//            }
//
//            webView.loadUrl("https://" + DOMAIN + ".yedatop.com/modules/stock/cashbox_fe?dangot=1&sn=" + sn);
//        }
//        else {



//for pelecard and serial number

        Log.d("NEXGO_SN", "sn=" + deviceId);
        webView.loadUrl("https://"+DOMAIN+"/modules/stock/cashbox_fe?dangot=1&snMode=serial&sn="+deviceId);
        //String finalUrl = "https://" + DOMAIN + "/modules/stock/cashbox_fe?dangot=1&sn=" + sn;
//Log.e("URL_CHECK", "finalUrl=" + finalUrl);
//webView.loadUrl(finalUrl);
//        Log.d("NEXGO_SN", "url=" + "https://"+DOMAIN+".com/modules/stock/cashbox_fe?dangot=1&sn="+Uri.encode(sn));
//             webView.loadUrl("https://"+DOMAIN+".com/modules/stock/cashbox_fe?dangot=1&runner=1");//for runner
        //}
        //end for pelecard


        //webView.loadUrl("https://dangot.yedatop.com/modules/stock/cashbox_fe?dangot=1");
//        webView.postUrl("https://office1.yedatop.com/modules/stock/rep_tazmech_print.php?&simple=1&sDate=01/02/2021&eDate=18/02/2021",("zedmode=1&journum=104").getBytes());
    }

    private String getSystemProperty(String key) {
        try {
            Class<?> sp = Class.forName("android.os.SystemProperties");
            return (String) sp.getMethod("get", String.class).invoke(null, key);
        } catch (Throwable t) {
            return null;
        }
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
    public class MyJsInterface implements DataListener {
        private Context context;

        public MyJsInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public String getSerialNumber(){
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                return Build.getSerial();
//            }
            return "0";
        }
        @JavascriptInterface
        public void disableKeyboard() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null && webView != null) {
                        imm.hideSoftInputFromWindow(webView.getWindowToken(), 0);
                        webView.clearFocus(); // שחרור הפוקוס מה־WebView
                    }
                }
            });
        }
        //        public void disableKeyboard(){
//
//            handler.postDelayed(disableKeyboard,10);
//        }
//        @JavascriptInterface
//        public void longPressDiv() {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                stopLockTask(); // Stop lock task without user confirmation
//            }
////            MainActivity.this.webView.setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
////            MainActivity.this.webView.setFocusable(true);
////            MainActivity.this.webView.setFocusableInTouchMode(true);
//        }

        @JavascriptInterface
        public void enableKeyboard() {
            Log.d("enableKeyboard", "Trying to show keyboard...");

            handler.postDelayed(enableKeyboard,5);
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
        public void open_drw() throws InterruptedException {
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
        public void present_sett(String username,String type_present){
            username_for_path = username;
            type_present_global =type_present;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type_present_global.equals("1")){
                        showPicture(username_for_path);
                    }
                    else if(type_present_global.equals("2")) {
                        showVideo(username_for_path);
                    }
                    else if(type_present_global.equals("3")) {
                        showLogo(username_for_path);
                    }
                }
            });
        }

        @JavascriptInterface
        public void presentProduct(String title,double amount, String price,int index,String totalAmount){
            double price2 = Double.parseDouble(price);
            double totalAmount2 =Double.parseDouble(totalAmount);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type_present_global.equals("3")) {
                        startProductPresentation(username_for_path,title, amount, price2, index,totalAmount2);
                    }
                }
            });
        }

        @JavascriptInterface
        public void removeProd(int index,String totalAmount){
            double totalAmount2 =Double.parseDouble(totalAmount);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type_present_global.equals("3")) {
                        removeProductFromPresentation(index,totalAmount2);
                    }
                }
            });
            input_barcode.requestFocus();
        }

        @JavascriptInterface
        public void plusCountProd(int index,double price,String totalAmount){
            double totalAmount2 =Double.parseDouble(totalAmount);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type_present_global.equals("3")) {
                        plusCountProdFromPresentation(index, price,totalAmount2);
                    }
                }
            });
        }

        @JavascriptInterface
        public void minusCountProd(int index,double price,String totalAmount){
            double totalAmount2 =Double.parseDouble(totalAmount);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type_present_global.equals("3")) {
                        minusCountProdFromPresentation(index, price,totalAmount2);
                    }
                }
            });
        }

        @JavascriptInterface
        public void changeCountProd(int index, int count, double totalprice,String totalAmount){
            double totalAmount2 =Double.parseDouble(totalAmount);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type_present_global.equals("3")) {
                        changeCountProdFromPresentation(index, count, totalprice,totalAmount2);
                    }
                }
            });
        }

        @JavascriptInterface
        public void addAnachaProd(int index, int count, String totalprice,String anacha,String totalAmount){
            double totalprice2 = Double.parseDouble(totalprice);
            double anacha2 = Double.parseDouble(anacha);
            double totalAmount2 =Double.parseDouble(totalAmount);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type_present_global.equals("3")) {
                        addAnachaProdFromPresentation(index, count, totalprice2,anacha2,totalAmount2);
                    }
                }
            });
        }
        @JavascriptInterface
        public void addDiscountProd(int index,String discountName,String discount,String totalAmount){
            double discount2 = Double.parseDouble(discount);
            double totalAmount2 =Double.parseDouble(totalAmount);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type_present_global.equals("3")) {
                        addDiscountProdFromPresentation(index,discountName ,discount2,totalAmount2);
                    }
                }
            });
        }

        @JavascriptInterface
        public void addAnachaGeneral(String totalprice){
            double totalprice2 = Double.parseDouble(totalprice);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type_present_global.equals("3")) {
                        addAnachaGeneralFromPresentation(totalprice2);
                    }
                }
            });
        }

        @JavascriptInterface
        public void changeTotalPrice(double totalprice){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type_present_global.equals("3")) {
                        changeTotalPriceProduct(totalprice);
                    }
                }
            });
        }

        @JavascriptInterface
        public void stopPresentProduct(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(type_present_global.equals("3")) {
                        stopProducrpresentation();
                    }
                }
            });
        }

        @JavascriptInterface
        public void tara() {
            if (getPlatform() == PLATFORMS.IMIN) {
                mElectronic.removePeel();
            } else if (getPlatform() == PLATFORMS.UROVO) {
                barcode_scanner.setVisibility(View.VISIBLE);
                barcode_scanner.setResultHandler(MainActivity.this);
                barcode_scanner.startCamera();
            } else if (getPlatform() == PLATFORMS.iPOS || getPlatform() == PLATFORMS.SUNMI) {
                adytechTare();
            }
        }

        @JavascriptInterface
        public void clear_weight() {
            if (getPlatform() == PLATFORMS.IMIN) {
                mElectronic.turnZero();
            } else if (getPlatform() == PLATFORMS.iPOS || getPlatform() == PLATFORMS.SUNMI) {
                adytechZero();
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
                return  MainActivity.this.getFromUsbWeightBip30();
            else if (getPlatform() == PLATFORMS.IMIN){
                return MainActivity.this.getFromIminWeight();
            }
            return 0;
        }

        @JavascriptInterface
        public double  getChooseWeight(String typeOfWeight2){
            int typeOfWeight = Integer.parseInt(typeOfWeight2);
            Log.e(TAG, "typeOfWeight"+typeOfWeight);
            if (getPlatform() ==  PLATFORMS.iPOS || getPlatform() ==  PLATFORMS.SUNMI) {
                if (typeOfWeight == 0) {
                    Log.e(TAG, "getFromUsbWeightCS600" + typeOfWeight);
                    return MainActivity.this.getFromUsbWeightCS600();
                } else if (typeOfWeight == 1) {
                    Log.e(TAG, "getFromUsbWeightBip30" + typeOfWeight);
                    return MainActivity.this.getFromUsbWeightBip30();
                } else if (typeOfWeight == 2) {
                    return MainActivity.this.getFromUsbWeightNewtech();
                } else if (typeOfWeight == 3) {
                    return MainActivity.this.getFromAdytechWeight();
                }
            }
            else if (getPlatform() == PLATFORMS.IMIN){
//                String manufacturer = Build.MANUFACTURER.toLowerCase();
//                if (manufacturer.contains("rockchip")) {
//                    return MainActivity.this.getFromUsbWeightScangle();
//                }
//                return MainActivity.this.readWeightDirectlyFromUsb(context);
                 return MainActivity.this.getFromIminWeight();
            }
            return 0;
        }
        private void resetAdytechState() {
            adytechCurrentWeight = 0.0;
            adytechLastWeight = Double.NaN;
            adytechStableCount = 0;
        }
        private void adytechTare() {
            try {
                if (adytechWeighUtils != null) {
                    adytechWeighUtils.resetBalance_qp();
                    resetAdytechState();
                }
            } catch (Exception e) {
                Log.e(ADYTECH_TAG, "adytechTare failed", e);
            }
        }

        private void adytechZero() {
            try {
                initAdytechScaleIfNeeded();
                if (adytechWeighUtils != null) {
                    adytechWeighUtils.resetBalance();
                    resetAdytechState();
                }
            } catch (Exception e) {
                Log.e(ADYTECH_TAG, "adytechZero failed", e);
            }
        }
        @JavascriptInterface
        public double  getWeight(){
            if (getPlatform() ==  PLATFORMS.iPOS || getPlatform() ==  PLATFORMS.SUNMI){
                //  return  MainActivity.this.getFromUsbWeightCS600();
                return  MainActivity.this.getFromUsbWeightBip30();
            } else if (getPlatform() == PLATFORMS.IMIN){
                return MainActivity.this.getFromIminWeight();
            }
            return 0;
        }

        @JavascriptInterface
        public void unitPrice(String unitPrice){ //for customer screen in normal android
//            UsbManager manager = (UsbManager) getSystemService
//                    (Context.USB_SERVICE);
//            List<UsbSerialDriver> availableDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
//            if (availableDrivers.isEmpty()) {
//                //return -1
//            }
//
//            // Open a connection to the first available driver.
//            UsbSerialDriver driver = availableDrivers.get(0);
//            UsbDeviceConnection connection = manager.openDevice(driver.getDevice());
//            if (connection == null) {
//                // add UsbManager.requestPermission(driver.getDevice(), ..) handling here
//               // return -1;
//            }
//            try {
//                String UP = "P" + unitPrice + "\r\n";
//                int usbSerialPort = 9600;
//                port = driver.getPorts().get(0);
//                port.open(connection);
//                port.setParameters(usbSerialPort, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
//                port.write(UP.getBytes(), 250);//5050
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//
//            }
        }

//        @JavascriptInterface
//        public void writelog(String journal, String text){
//            // Check if external storage is available
//            if (isExternalStorageWritable()) {
//                File directory = new File(Environment.getExternalStorageDirectory(), "Logs");
//                if (!directory.exists()) {
//                    directory.mkdirs();
//                }
//                File logFile = new File(directory, journal + ".txt");
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//                String currentTime = sdf.format(new Date());
//                try {
//                    if (!logFile.exists()) {
//                        logFile.createNewFile();
//                    }
//                    FileWriter writer = new FileWriter(logFile, true);
//                    writer.write(currentTime + " : " + text + "\n");
//                    writer.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        @JavascriptInterface
        public void writeLogToConsole(String text){
            Log.i("FROM JS: " ,text);
        }

        @JavascriptInterface
        public void writelog_this( String journal, String text) {
            Context context = getApplicationContext();

            // יצירת תיקיית Logs באחסון הפרטי של האפליקציה
            File directory = new File(context.getExternalFilesDir(null), "Logs");
            if (!directory.exists()) {
                if (!directory.mkdirs()) {
                    Log.e("LogManager", "Failed to create directory: " + directory.getAbsolutePath());
                    return;
                } else {
                    Log.i("LogManager", "Directory created successfully: " + directory.getAbsolutePath());
                }
            }

            // יצירת הקובץ
            File logFile = new File(directory, journal + ".txt");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            String currentTime = sdf.format(new Date());

            try {
                if (!logFile.exists()) {
                    if (logFile.createNewFile()) {
                        Log.i("LogManager", "New log file created: " + logFile.getAbsolutePath());
                    } else {
                        Log.e("LogManager", "Failed to create log file: " + logFile.getAbsolutePath());
                        return;
                    }
                }

                // כתיבת טקסט לתוך הקובץ
                FileWriter writer = new FileWriter(logFile, true);
                writer.write(currentTime + " : " + text + "\n");
                writer.close();

                Log.i("LogManager", "Log written to: " + logFile.getAbsolutePath());
            } catch (IOException e) {
                Log.e("LogManager", "Error writing log file: ", e);
            }
        }


//        public void writelog( String journal, String text) {
//
//            Context context = getApplicationContext();
//
//            // יצירת תיקיית Logs באחסון הפרטי של האפליקציה
//
//            File directory = new File(context.getExternalFilesDir(null), "Logs");
//            if (!directory.exists()) {
//                if (!directory.mkdirs()) {
//                    Log.e("LogManager", "Failed to create directory: " + directory.getAbsolutePath());
//                    return;
//                }
//            }
//
//            // יצירת הקובץ
//            File logFile = new File(directory, journal + ".txt");
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//            String currentTime = sdf.format(new Date());
//
//            try {
//                // יצירת קובץ חדש אם הוא לא קיים
//                if (!logFile.exists()) {
//                    logFile.createNewFile();
//                }
//
//                // כתיבה לקובץ
//                FileWriter writer = new FileWriter(logFile, true);
//                writer.write(currentTime + " : " + text + "\n");
//                writer.close();
//
//                Log.i("LogManager", "Log written to: " + logFile.getAbsolutePath());
//            } catch (IOException e) {
//                Log.e("LogManager", "Error writing log file: ", e);
//            }
//            if (logFile.exists()) {
//                Log.i("LogManager", "File successfully created at: " + logFile.getAbsolutePath());
//            } else {
//                Log.e("LogManager", "File creation failed at: " + logFile.getAbsolutePath());
//            }
//
//            // פתיחת הקובץ באפליקציה חיצונית
//            openLogFile(context, logFile);
//        }

        private void openLogFile(Context context, File logFile) {
            if (logFile.exists()) {
                try {
                    Uri uri = FileProvider.getUriForFile(
                            context,
                            context.getPackageName() , // סמכות ללא .fileprovider
                            logFile
                    );

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(uri, "text/plain");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(intent);
                } catch (IllegalArgumentException e) {
                    Log.e("FileProvider", "Invalid FileProvider URI", e);
                }
            } else {
                Log.e("LogManager", "File does not exist: " + logFile.getAbsolutePath());
            }
        }


        private boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state);
        }

        private MqttClient mqttClient;
        // private TextField textFieldServerAddress, textFieldServerPort, textFieldUserName, textFieldPassword;

        private boolean subscribeTopic(String topic, int qos) {
            try {
                mqttClient.subscribe(topic, qos);
                return true;
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
            return false;
        }
//        private String generateTicketID(int i, int copies) {
//            if (checkBoxManualSetTicketID.isSelected())
//                return textFieldManualSetTicketID.getText().trim();
//            else
//                return (i + 1) + "/" + copies + " " + mqttClientID + " " + Utils.GetTimestamp();
//        }

        private boolean isMqttClientConnected() {
            try {
                if (mqttClient != null) return mqttClient.isConnected();
            } catch (Throwable tr) {
                tr.printStackTrace();
            }
            return false;
        }

        private MqttCallback mqttCallback = new MqttCallback() {
            @Override
            public void disconnected(MqttDisconnectResponse disconnectResponse) {
                String msgstr = "disconnected " + disconnectResponse.toString();
                System.out.println(msgstr);
            }

            @Override
            public void mqttErrorOccurred(MqttException exception) {
                String msgstr = "mqttErrorOccurred " + exception.toString();
                System.out.println("from CALLBACK: " + msgstr);
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                //The original data is stored in the file, the amount of data is too large
                String msgstr = topic + "\n" + message.toString() + "\n";
                System.out.println("Message published");
                //  handleRecvMessage(message.toString());
            }

            @Override
            public void deliveryComplete(IMqttToken token) {
                String msgstr = "deliveryComplete " + token.toString();
                System.out.println(msgstr);
            }

            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                String msgstr = "connectComplete " + serverURI;
                System.out.println(msgstr);
            }

            @Override
            public void authPacketArrived(int reasonCode, MqttProperties properties) {
                String msgstr = "authPacketArrived " + properties;
                System.out.println(msgstr);
            }
        };
        private String generateTicketID(int i, int copies) {

            return (i + 1) + "/" + copies + " " + "PrnCC18BF3BC51F" + " " ;
        }
        private boolean publishDataAndUpdateUI(String topic, byte[] data, int qos, String TicketID) {
            if ((topic == null) || (topic.isEmpty())) {
                return false;
            }
            System.out.println("Publish topic " + topic + " qos " + qos + " ticket " + TicketID);
            //System.out.println(Utils.GetHexString(data));

//            if (publishTopic(topic, data, qos)) {
//                TicketInfoRow ticketInfoRow = getTicketInfoRow(TicketID);
//                ticketInfoRow.setAppSended(Utils.GetTimestamp());
//                return true;
//            }
            return true;
        }


        public Bitmap drawTextToBitmap(String s) {
            // Create a mutable bitmap
            Bitmap bitmap = Bitmap.createBitmap(10, 20, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            // Initialize a new Paint instance to draw the text
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.BLACK); // Text color
            paint.setTextSize(2); // Text size
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            // Optional: Set background color
            canvas.drawColor(Color.WHITE);
            // Calculate the positions to make the text center aligned
            String text = "nkdvdbv dkjvndsjkvnc sdcdsijcsd ";
            float textWidth = paint.measureText(text);
            float x = (bitmap.getWidth() - textWidth) / 2;
            float y = (bitmap.getHeight() - paint.ascent() - paint.descent()) / 2;
            // Draw the text
            canvas.drawText(text, x, y, paint);
            return bitmap;
        }

        public byte[] bitmapToByteArray(Bitmap bitmap) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // Compress to PNG format at 100% quality
            byte[] byteArray = stream.toByteArray();
            return byteArray;
        }
        public byte[] bitmapToMonochromeBytes(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x += 8) {
                    int value = 0;
                    for (int bit = 0; bit < 8; bit++) {
                        if (x + bit < width) {
                            int pixel = bitmap.getPixel(x + bit, y);
                            int luminance = (int) (0.299 * Color.red(pixel) + 0.587 * Color.green(pixel) + 0.114 * Color.blue(pixel));
                            if (luminance < 128) {
                                value |= (1 << (7 - bit));
                            }
                        }
                    }
                    byteArrayOutputStream.write(value);
                }
            }
            return byteArrayOutputStream.toByteArray();
        }
        public  Bitmap convertToMonochrome(Bitmap src) {
            Bitmap bmpMonochrome = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ALPHA_8);
            Canvas canvas = new Canvas(bmpMonochrome);
            ColorMatrix ma = new ColorMatrix();
            ma.setSaturation(0);
            Paint paint = new Paint();
            paint.setColorFilter(new ColorMatrixColorFilter(ma));
            canvas.drawBitmap(src, 0, 0, paint);
            return bmpMonochrome;
        }
        public byte[] rasterizeBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x += 8) {
                    int byteValue = 0;
                    for (int bit = 0; bit < 8; bit++) {
                        if (x + bit < width) {
                            int pixel = bitmap.getPixel(x + bit, y);
                            if (Color.red(pixel) < 128) {  // Assuming black text on a white background
                                byteValue |= (1 << (7 - bit));
                            }
                        }
                    }
                    byteArrayOutputStream.write(byteValue);
                }
            }
            return byteArrayOutputStream.toByteArray();
        }
        public byte[] bitmapToBytes(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            for (int y = 0; y < height; y++) {
                int byteValue = 0;
                for (int x = 0; x < width; x++) {
                    if (x % 8 == 0 && x != 0) {
                        outputStream.write(byteValue);
                        byteValue = 0;
                    }
                    int color = bitmap.getPixel(x, y);
                    int luminance = (Color.red(color) + Color.green(color) + Color.blue(color)) / 3;
                    if (luminance < 128) {
                        byteValue |= (1 << (7 - (x % 8)));  // Set bit to 1
                    }
                }
                outputStream.write(byteValue); // write last byte
            }
            return outputStream.toByteArray();
        }
        public void sendBitmapOverMQTT(byte[] imageData, String serverURI, String net) {
            try {
                MemoryPersistence persistence = new MemoryPersistence();
                mqttClient = new MqttClient(serverURI, net,persistence);
                MqttConnectionOptions options = new MqttConnectionOptions();
                options.setCleanStart(true);
                mqttClient.connect(options);

                MqttMessage message = new MqttMessage(imageData);
                message.setQos(1);  // At least once delivery
                mqttClient.publish(net, message);

                mqttClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        public byte[] convertBitmapToPrinterBytes(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            // Each line requires a width divisible by 8 for byte alignment
            int byteWidth = (width + 7) / 8;
            byte[] rowBytes = new byte[byteWidth];

            for (int y = 0; y < height; y++) {
                int byteIndex = 0;
                for (int x = 0; x < width; x++) {
                    if ((x % 8) == 0) {
                        rowBytes[byteIndex] = 0;  // Initialize byte
                    }

                    int pixel = bitmap.getPixel(x, y);
                    // Assuming white is 0xFF and black is 0x00, adjust threshold as necessary
                    if ((pixel & 0xFF) < 128) {  // Check the blue component or calculate luminance
                        rowBytes[byteIndex] |= (1 << (7 - (x % 8)));
                    }

                    if ((x % 8) == 7 || x == (width - 1)) {
                        outputStream.write(rowBytes[byteIndex]);
                        byteIndex++;
                    }
                }
                // Line feed after each row
                outputStream.write(0x0A);
            }

            return outputStream.toByteArray();
        }
        public void printDataUsingOPOS(byte[] data) {
            POSPrinter printer = null;
            try {
                printer = new POSPrinter();
                printer.open("POSPrinter");
                printer.claim(1000);
                printer.setDeviceEnabled(true);
                printer.printNormal(POSPrinterConst.PTR_S_RECEIPT, new String(data));

                printer.close();
            } catch (JposException e) {
                e.printStackTrace();
            } finally {
                if (printer != null) {
                    try {
                        printer.close();
                    } catch (JposException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        private  POSPrinter printer;



        private String convertBonToHtml(String s) {
            String[] lines = s.split(",");
            StringBuilder html = new StringBuilder();
            html.append("<html dir='rtl'><body style='text-align:right; font-size:18px;'>");
            for (String line : lines) {
                html.append("<div>").append(line).append("</div>");
            }
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
                        view.measure(View.MeasureSpec.makeMeasureSpec(384, View.MeasureSpec.EXACTLY),
                                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
                        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        view.draw(canvas);
                        onDone.accept(bitmap);
                    }
                });
                webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
            });
        }




        //מדפסת מרוחקת ענן עם PRN
        @JavascriptInterface
        public void print_invoice_bon2(String s, final String net, String server, String userName, String password) throws JSONException {
            try{
                String serverURI = server;
                MemoryPersistence persistence = new MemoryPersistence();
                mqttClient = new MqttClient(serverURI, net,persistence);
                MqttConnectionOptions connOpts = new MqttConnectionOptions();
                connOpts.setCleanStart(true);
                connOpts.setSessionExpiryInterval(0xffffffffl);
                connOpts.setUserName(userName);
                connOpts.setPassword(password.getBytes(StandardCharsets.UTF_8));
                mqttClient.setCallback(mqttCallback);
                mqttClient.connect(connOpts);
                byte[] init = new byte[] {0x1B, 0x40};
                String charsetName = "CP862";
                //String charsetName = "UTF-8";
                s = s.replace("\"","");
                s= s.replace("[","");
                s= s.replace("]","");
//                String htmlFormatted = convertBonToHtml(s);

                String[] lines = s.split(",");
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int x =0;
                for (String line : lines) {
                    byte[] text = line.getBytes(charsetName);
                    int paddingLength = (40 - text.length) / 2; // Assuming 48 characters is the width of your printer
                    outputStream.write(init);
                    outputStream.write(text);
                    if(x!=4) {
                        for (int i = 0; i < paddingLength; i++) {
                            outputStream.write(0x20); // Write spaces for padding
                        }
                    }if(x==4) {
                        for (int i = 0; i < 20; i++) {
                            outputStream.write(0x20); // Write spaces for padding
                        }
                    }
                    outputStream.write(0x0A); // Line feed after each lin
                    x++;

                }
                outputStream.write(0x0A);outputStream.write(0x0A); outputStream.write(0x0A);outputStream.write(0x0A);outputStream.write(0x0A); outputStream.write(0x0A);outputStream.write(0x0A);
                outputStream.write(new byte[]{0x1D, 0x56, 0x00}); // פקודת חיתוך מלא
                byte[] printData = outputStream.toByteArray();
                String topic = net; // Replace with the topic the printer is subscribed to
                // String message = "Hello, printer123456!";
                mqttClient.publish(topic, printData, 1, true);

                mqttClient.disconnect();
            } catch (Throwable tr) {
                tr.printStackTrace();
            }

        }

        @JavascriptInterface
        public void print_invoice_bon_new_printer(String s, final String net, String server, String userName, String password) {
            try {
                // התחברות ל־MQTT
                SharedPreferences prefs = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                String deviceId = prefs.getString("device_id", null);
                if (deviceId == null || deviceId.isEmpty()) {
                    deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
                    if (deviceId == null || deviceId.isEmpty()) deviceId = java.util.UUID.randomUUID().toString();
                    prefs.edit().putString("device_id", deviceId).apply();
                }
                String clientId = net + "-" + deviceId;
                String serverURI = server;
                MemoryPersistence persistence = new MemoryPersistence();
//                mqttClient = new MqttClient(serverURI, net, persistence);
                MqttClient localMqttClient = new MqttClient(serverURI, clientId, persistence);

                MqttConnectionOptions connOpts = new MqttConnectionOptions();
                connOpts.setCleanStart(true);
                connOpts.setAutomaticReconnect(false);
                connOpts.setSessionExpiryInterval(0xffffffffL);
                connOpts.setUserName(userName);
                connOpts.setPassword(password.getBytes(StandardCharsets.UTF_8));
                localMqttClient.connect(connOpts);
                DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                float density = metrics.density;
// נגדיר פונט בסיסי שמתאים למדפסת 384 נקודות ברזולוציה 160dpi
                int baseFontSize = 100; // נקודת מוצא
                float scaleFactor = density; // או metrics.densityDpi / 160f

                int fontSize = (int)(baseFontSize / scaleFactor);
                final String ss=s;
                ((Activity) context).runOnUiThread(() -> {
                    String _s =ss;
                    String _s2;
                    _s = ss.replace("max-width: 150px!important;width: 150px;margin-left: 20px;","width:120%;margin: 0; text-align: center;");
                    final String html = "<html>" +
                            "<head>" +
                            "<meta name=\"viewport\" content=\"width=120%, initial-scale=1.0\">" +
                            "<head>\n" +
                            "\t<style>\n" +
                            "\t\t   body{\n" +
                            "\t\t\twidth: 120%;\n" +
                            "\t\t}\n" +
                            "\t\tbody > table{\n" +
                            "font-size: 50px !important;" +
                            "width: 120% !important;"+
                            "\t\t}\n" +
                            "\t</style>\n" +
                            "</head>" +
                            "<body>"+_s +
                            "\n" +"</body></html>";

                    WebView webView = new WebView(context);
                    webView.setVisibility(View.GONE);
                    webView.getSettings().setJavaScriptEnabled(true);
                    //webView.addJavascriptInterface(new WebAppBridge(), "android");

                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageFinished(WebView view, String url) {
                            view.postDelayed(() -> {
                                try {
                                    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
                                    float scale = metrics.density;
                                    int printerDotsWidth = 576; // או 576
                                    int viewWidth = printerDotsWidth;

                                    int height = 30000;
                                    view.measure(
                                            View.MeasureSpec.makeMeasureSpec(viewWidth, View.MeasureSpec.EXACTLY),
                                            View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.AT_MOST)
                                    );
                                    view.layout(0, 0, viewWidth, view.getMeasuredHeight());

                                    Bitmap bitmap = Bitmap.createBitmap(viewWidth, view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
                                    Canvas canvas = new Canvas(bitmap);
                                    view.draw(canvas);

                                    Log.i("DEBUG", "Bitmap size: " + bitmap.getWidth() + "x" + bitmap.getHeight());

                                    byte[] escposData = convertBitmapToEscPos(bitmap);
//                                    MqttMessage clearMessage = new MqttMessage(new byte[0]);
//                                    clearMessage.setQos(0);
//                                    clearMessage.setRetained(true);
//                                    localMqttClient.publish(net, clearMessage);
//                                    Log.i("MQTT", "Cleared retained message on topic " + net);

                                    MqttMessage message = new MqttMessage(escposData);
                                    message.setQos(1); // QoS הכי פשוט — שליחה חד־פעמית
                                    message.setRetained(false); // לא לשמור ב־Broker!
                                    localMqttClient.publish(net, message);
                                    Log.i("MQTT", "Publish sent");

                                    // שליחת פקודת RESET כדי לנקות את ה־Buffer
//                                    byte[] resetPrinter = new byte[] { 0x1B, 0x40 }; // ESC @ → Reset
//                                    MqttMessage resetMessage = new MqttMessage(resetPrinter);
//                                    resetMessage.setQos(0);
//                                    resetMessage.setRetained(false);
//                                    localMqttClient.publish(net, resetMessage);

//                                    localMqttClient.publish(net, new MqttMessage(escposData));
                                    if (localMqttClient.isConnected()) {
                                        localMqttClient.disconnect();
                                        Log.i("MQTT", "Disconnected after publish and reset");
                                    }
//                                    Thread.sleep(200);
                                    ViewGroup root = ((Activity) context).findViewById(android.R.id.content);
                                    root.removeView(webView);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }, 500);
                        }
                    });

                    ViewGroup root = ((Activity) context).findViewById(android.R.id.content);
                    root.addView(webView);
                    webView.getSettings().setUseWideViewPort(true);
                    webView.getSettings().setLoadWithOverviewMode(true);
                    webView.getSettings().setJavaScriptEnabled(true);

                    webView.setInitialScale(100); // חשוב!

                    webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }



        public Bitmap toMonochrome(Bitmap original) {
            Bitmap bwBitmap = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bwBitmap);
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(filter);
            canvas.drawBitmap(original, 0, 0, paint);
            return bwBitmap;
        }


        public byte[] convertBitmapToEscPos(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            // המר ל־1 ביט בשחור־לבן
            Bitmap monoBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(monoBitmap);
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(filter);
            canvas.drawBitmap(bitmap, 0, 0, paint);

            // המר לפורמט byte[]
            int bytesPerRow = (width + 7) / 8;
            byte[] imageData = new byte[bytesPerRow * height];
            int index = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x += 8) {
                    byte b = 0;
                    for (int i = 0; i < 8; i++) {
                        if (x + i < width) {
                            int pixel = monoBitmap.getPixel(x + i, y);
                            int r = (pixel >> 16) & 0xFF;
                            int g = (pixel >> 8) & 0xFF;
                            int bVal = pixel & 0xFF;
                            int gray = (r + g + bVal) / 3;
                            if (gray < 128) {
                                b |= (1 << (7 - i));
                            }
                        }
                    }
                    imageData[index++] = b;
                }
            }

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            // GS v 0: הדפסה של bitmap
            output.write(0x1D); // GS
            output.write('v');
            output.write('0');
            output.write(0x00); // רגיל

            output.write(bytesPerRow % 256); // xL
            output.write(bytesPerRow / 256); // xH
            output.write(height % 256);      // yL
            output.write(height / 256);      // yH

            try {
                output.write(imageData);
                output.write(0x0A);output.write(0x0A);output.write(0x0A);output.write(0x0A);output.write(0x0A);
                output.write(new byte[]{0x1D, 0x56, 0x00});
            } catch (IOException e) {
                e.printStackTrace(); // או Log.e(...)
            }
            Log.i("DEBUG", "ESC/POS total bytes = " + output.size());
            Log.i("DEBUG", "Bitmap: " + width + "x" + height);

            return output.toByteArray();
        }


        //מדפסת מרוחקת ללא ענן, עם כבל ו IP
        //  @JavascriptInterface
//        public void print_invoice_bonsssss_(String s, final String net) throws JSONException {
//
//            POSInterfaceAPI interface_net = new POSNetAPI();
//            //s="123456";
//            POSSDK pos_sdk = new POSSDK(interface_net);
//            int error_code = 0;
//            String[] a_arr;
//            int POSPORT = 9100;
//            //int STATEPORT = 4000;
//            error_code = interface_net.OpenDevice(net, POSPORT);
//            if(error_code == 1001){
//
//            }
//            else {
//                running = 0;
//                byte[] send_buf = new byte[0];
//                s = s.replace("[", "");
//                s = s.replace("]", "");
//                s = s.replace("\"", "");
//                a_arr=s.split("[,]");
//
//                Gson gson = new GsonBuilder().setPrettyPrinting().create();
//                String jsonString = gson.toJson(a_arr);
//                jsonString = jsonString.replace("[", "");
//                jsonString = jsonString.replace("]", "");
//                jsonString = jsonString.replace("\"", "");
//                jsonString = jsonString.replace(",", "");
//
//                Bitmap c_image = pos_sdk.imageCreateRasterBitmap(jsonString,23,1);
//                error_code = pos_sdk.imageStandardModePrint (c_image, 33, 10, 640);
//                error_code = pos_sdk.systemCutPaper(CutPartAfterFeed, 80);
//                interface_net = null;
//            }
//        }
        public Bitmap barcodeBitmap = null;
        /**
         * Set whether to prevent opening the cash drawer.
         * @param value true to prevent opening, false otherwise.
         */
        @JavascriptInterface
        public void setDontOpenCashDrawer(boolean value) {
            //dontOpenCashDrawer = value;
        }

        @JavascriptInterface
        public boolean getDontOpenCashDrawer() {
            return true;//dontOpenCashDrawer;
        }

        private void resetDontOpenCashDrawer() {
            //dontOpenCashDrawer = false;
        }


        @JavascriptInterface
        public void print_invoice3(final String s, final String barcode, int seconds) throws JSONException {
            running = 0;
            final String htmlTemp = s;
            String manufacturer = Build.MANUFACTURER.toLowerCase();
//            NEXGO
//            if(getPlatform() == PLATFORMS.iPOS && manufacturer.contains("sprd")) {
//
//                if (manufacturer.contains("sprd")) {
//                    String tmp = htmlTemp.replace("max-width:170px;width:170px;","max-width:600px; width:600px;");//15px not 35
//                    String tmp1 = tmp.replace("max-width: 150px!important;width: 650px;","max-width:600px; width:600px;");//15px not 35
//                    String cleanedHtml = tmp1.replaceAll("<img[^>]+src=\"https://liv\\.yedatop\\.com/modules/stock/cashbox_fe/inc/barcode\\.php\\?height=20&barcode=[0-9]+\"[^>]*>", "");
//                    if (!("pos.pelecash.co.il".equals(BuildConfig.DOMAIN)) && barcode != "0")
//                        MainActivity.this.barcode = barcode;
//                    NexgoDeviceController nexgoDeviceController = new NexgoDeviceController(context);
//                    HtmlToBitmapConverter.convertHtmlToBitmap(context, cleanedHtml, bitmap -> {
//                        if (bitmap != null) {
//                            Log.d("Bitmap", "Bitmap נוצר בהצלחה!");
//                            // ניתן לשלוח למדפסת
//                            String deviceName = Build.MODEL;
//                            if(deviceName != "N6" && deviceName != "N6"){
//                                Log.d("PRINT", "נשלח להדפסה!");
//                                nexgoDeviceController.makeBitmapPrint(bitmap);
//                            }
//                        } else {
//                            Log.e("Bitmap", "שגיאה ביצירת ה-Bitmap");
//                        }
//                    });
//                }
//            }
//             NEXGO END

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
                        _htmlTemp = _htmlTemp.replaceAll("width: 250px;","width: 350px;max-width: 350px!important;");

                        _htmlTemp = htmlTemp.replace("margin-left: 5px","margin-left: 15px");//15px not 35
//                        _htmlTemp = _htmlTemp.replace(";width: 250px;",";width: 550px;");

                        _htmlTemp = _htmlTemp.replaceAll("width:110px;","width: 45%;");
                        _htmlTemp = _htmlTemp.replace("font-size: 15;","font-size: 39px !important;");//for citaq machine
                        _htmlTemp = _htmlTemp.replaceAll("width:120px;","width: 45%;");
                        _htmlTemp = _htmlTemp.replaceAll("width:70px;","width: 10%;");
                        _htmlTemp = _htmlTemp.replaceAll("max-width: 150px!important;width: 150px;","width: 250px;");
                        _htmlTemp = _htmlTemp.replaceAll("width: 250px;","width: 750px;max-width:750px");

                        Matcher m = p.matcher(_htmlTemp);

                        if (countWordsUsingSplit(_htmlTemp) == 2 || countWordsUsingSplit(_htmlTemp) == 1) {
                            List<String> srcList = new ArrayList<>();

                            while (m.find()) {
                                String src = m.group();
                                srcList.add(src);
                            }
                            if (srcList.size() == 2) {

                                // יש 2 → להוריד את שניהם
                                for (String src : srcList) {
                                    String cleanSrc = src.replace("src=\"","").replace("\"","");
                                    downloadLogo(cleanSrc);
                                    _htmlTemp = _htmlTemp.replace(src, "");
                                }

                            } else if (srcList.size() == 1) {

                                // יש רק אחד → רק לאפס (למחוק מה־html)
                                String src = srcList.get(0);
                                _htmlTemp = _htmlTemp.replace(src, "");
                            }
//                            // get the first
//                            m.find() ;
//                            String src = m.group();
//                            downloadLogo(m.group().replace("src=\"","").replace("\"",""));
//                            _htmlTemp = _htmlTemp.replace(src, "");
                        }
                        //  _htmlTemp = _htmlTemp.replace("max-width:170px;width:170px;", "max-width:480px;width:350px;");

//                        _htmlTemp = _htmlTemp+"<style type=\"text/css\">\timg{visibility: hidden;\theight: 0px;}\tbody{height: 0px;}</style>";

                        //  _htmlTemp = _htmlTemp.replace("max-width:170px;width:170px;","max-width:500px;width:450px;");
//                        _htmlTemp = _htmlTemp.replace("max-width:170px;width:170px;", "max-width:600px;width:600px;");
                        if (!barcode.equals("0")) {
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
                        //  _htmlTemp = _htmlTemp.replace("max-width:170px;width:170px;", "max-width:480px;width:350px;");
//                          _htmlTemp = _htmlTemp.replace("width: 250px;", "width: 350px;");
//                        _htmlTemp = _htmlTemp.replace("max-width:170px;width:170px;", "max-width:550px;width:550px;");//for chaklaot
//                        _htmlTemp = _htmlTemp.replaceAll("width:110px;","width: 45%;");
                        _htmlTemp = _htmlTemp.replace("font-size: 15;","font-size: 39px !important;");//for citaq machine
//                        _htmlTemp = _htmlTemp.replaceAll("width:120px;","width: 45%;");
//                        _htmlTemp = _htmlTemp.replaceAll("width:70px;","width: 10%;");
//                        _htmlTemp = _htmlTemp.replaceAll("max-width: 150px!important;width: 150px;","width: 250px;");
//                        _htmlTemp = _htmlTemp.replaceAll("width: 250px;","width: 750px;max-width:750px");
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
                    else  if (getPlatform() == PLATFORMS.IMIN) {
                        load("<html style=\"height: fit-content;\">" +
                                "<head>\n" +
                                "\t<style>\n" +
                                "\t\t   body{\n" +
                                "margin: unset;"+
                                "\t\t\tpadding: 0 !important;\n" +
                                "\t\t}\n" +
                                "\t\tbody > table{\n" +
                                "max-width:650px !important;"+
                                " font-size: "+font_size+"px !important;" +
                                "width: 97% !important;"+
//                                "margin-left: 35px !important;;"+//15
//                                "margin-right: 35px !important;;"+//15
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
                    else{
//                        if(DOMAIN == "dangotandroid" || DOMAIN == "dangotandroid1") {
//                            MainActivity.this.barcodeBitmap = generateBarcode(barcode, 340, 100);
//                        }
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
                                "\t android.height(document.getElementsByTagName(\"table\")[0].clientHeight+220 );\n" +//+300
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

        private Activity activity = MainActivity.this;
        private NexgoDeviceTransaction nexgoDeviceTransaction;
        private NexgoLibraryEventsListener nexgoLibraryEventsListener;

        @JavascriptInterface
        public String initNexgo(String userName, String password, String terminal) {
            try {
                ClientDetails clientDetails = new ClientDetails(userName, password, terminal);
                NexgoDeviceConfig nexgoDeviceConfig = new NexgoDeviceConfig(getApplicationContext());

                // הרץ את הפונקציה suspend דרך runBlocking בצורה סינכרונית
                InitResCode result = NexgoDeviceHelper.initDeviceConfigSync(nexgoDeviceConfig, clientDetails);

                // מחזיר JSON string (שיהיה תקין לקריאה ב-JavaScript)
                return "{\"returnValue\":\"" + result.name() + "\"}";

            } catch (Exception e) {
                Log.e("NexgoInit", "Error initializing Nexgo", e);
                return "{\"returnValue\":\"INIT_FAILED\"}";
            }
        }

        @JavascriptInterface
        public String startNexgoPayment(String userName, String password, String terminal, String kupaNum, String amount, String currency,
                                        String tranType, String creditTerms, String paymentsNumber, String firstPayment, String fixedPayment, String isManual, String credit,
                                        String cardNum, String exDate, String cvv, String id,String masof){
            Log.i("startNexgoPayment-startNexgoPayment" , "startNexgoPayment  -  HERE");
            double amountValue = Double.parseDouble(amount);
            String formattedAmount = String.format("%.2f", amountValue);
            if ("liv2.yedatop".equals(BuildConfig.DOMAIN) || "peleliv2.yedatop".equals(BuildConfig.DOMAIN) || "pos.pelecash.co.il".equals(BuildConfig.DOMAIN)) {

                Runnable ui = new Runnable() {
                    @Override public void run() {
//                        EditText editText = findViewById(R.id.transaction_amount_input);
//                        editText.setVisibility(View.VISIBLE);
//
//                        CircularProgressIndicator spinner = findViewById(R.id.loadingSpinner);
//                        spinner.setVisibility(View.GONE);
//
//                        FrameLayout spinnerContainer = findViewById(R.id.spinnerContainer);
//                        spinnerContainer.setVisibility(View.GONE);
//
//                        editText.setText("₪" + formattedAmount);
//
//                        TextView tvDate = findViewById(R.id.tvDate);
//                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm   dd.MM.yyyy ", Locale.getDefault());
//                        tvDate.setText(sdf.format(new Date()));
//
//                        TextView tvMispar = findViewById(R.id.tvMispar);
//                        tvMispar.setText(" מסוף:" + masof);
                    }
                };

                // חשוב: אם אנחנו לא על ה-UI thread — להעביר לשם
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    ui.run();
                } else {
                    runOnUiThread(ui);
                }
            }

            // ... כל השאר נשאר אותו דבר ...
            // registerBroadcastReceiver();
            // CompletableFuture...
            // future.get();
            // return resultMessage.get();

            registerBroadcastReceiver();
            AtomicReference<String> resultMessage = new AtomicReference<>("");
            nexgoLibraryEventsListener = new NexgoLibraryEventsListener(activity);
            NexgoDeviceConfig nexgoDeviceConfig = new NexgoDeviceConfig(getApplicationContext());
            nexgoDeviceTransaction = new NexgoDeviceTransaction(activity);
            nexgoJavaWrapper = new NexgoJavaWrapper(nexgoDeviceConfig, nexgoDeviceTransaction);
            CompletableFuture<Void> future = nexgoJavaWrapper.startNexgoPayment(userName, password, terminal, kupaNum, amount, currency,
                            tranType, creditTerms, paymentsNumber, firstPayment, fixedPayment, isManual, credit, cardNum, exDate, cvv, id)
                    .thenAccept(result -> {
                        if(result.toString().equals("TRANSACTION_ERROR")){

                        }
                        Log.d("NexgoDebug", "Transaction result received: " + result);
                        //nexgoLibraryEventsListener.onEventReceiveTransactionsMessages("תוצאה תשלום:" + result);
                        unregisterBroadcastReceiver();
                        Log.i("MainActivity", "תוצאההה: " + result);
                        resultMessage.set(result);
                    })
                    .exceptionally(e -> {
                        Log.e("NexgoDebug", "Error during Nexgo payment process", e);
                        unregisterBroadcastReceiver(); // ביטול הרשמה גם במקרה של שגיאה
                        resultMessage.set("שגיאה במהלך תהליך התשלום");
                        return null;
                    });
            // המתן לסיום התהליך האסינכרוני
            try {
                future.get(); // מחכה שהתהליך יסתיים
            } catch (Exception e) {
                Log.e("NexgoDebug", "Error waiting for result", e);
            }
            return resultMessage.get();  // החזרת התוצאה שנשמרה
        }

        @JavascriptInterface
        public String pelelivyCredit(String itra) {
            Log.e(TAG, "pelelivyCredit");
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
            if (BuildConfig.DOMAIN == "liv1.yedatop" || BuildConfig.DOMAIN == "liv.yedatop.com" || BuildConfig.DOMAIN == "peleliv2.yedatop" || BuildConfig.DOMAIN == "pos.pelecash.co.il" || BuildConfig.DOMAIN == "liv2.yedatop" ){
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

        @Override
        public void dataOccurred(DataEvent dataEvent) {

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
        public String pelelivyCredit(String amount, String username, String password, int pay_num, double pay_first, int ashray_f_credit,int moto,  String com, String approvalNumber, String motoPanEntry)
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
                    firstPaymentAmount = Math.round(firstPaymentAmount * 100.0) / 100.0;//rachel יום חמישי
                    paymentAmount = Math.round(amt / pay_num);
                }
                else
                {
                    firstPaymentAmount = pay_first;
                    paymentAmount = Math.round((amt - firstPaymentAmount) / (pay_num - 1));
                    firstPaymentAmount = amt - paymentAmount * (pay_num - 1);
                }
                numberOfPayments -= 1;
                double newAmnt = paymentAmount * numberOfPayments + firstPaymentAmount;
//                if(newAmnt != amt){
//                    double absAmnt = Math.abs(newAmnt-amt);
//                     if(newAmnt < amt) {
//                         firstPaymentAmount += absAmnt;
//                     } else if(newAmnt > amt){
//                         firstPaymentAmount -= absAmnt;
//                     }
//                }
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
        super.onBackPressed();
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
        intent.putExtra("present_type", type_present_global);
        intent.putExtra("present_username", username_for_path);
        startActivity(intent);

    }
    private  void openLogin(String url){
//        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        Log.e("NAV", "openLogin url=" + url);

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("URL",url);
        startActivity(intent);
        finish();

    }
}
