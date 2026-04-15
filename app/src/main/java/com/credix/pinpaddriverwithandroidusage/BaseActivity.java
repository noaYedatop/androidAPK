package com.credix.pinpaddriverwithandroidusage;

import static com.credix.pinpaddriverwithandroidusage.MainActivity.DOMAIN;
import static com.credix.pinpaddriverwithandroidusage.MainActivity.TAG;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.app.Presentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.device.DeviceManager;
import android.device.PrinterManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.hardware.display.DisplayManager;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.media.MediaRouter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

//import com.ahmedelsayed.sunmiprinterutill.PrintMe;
import com.imin.library.IminSDKManager;
import com.imin.printerlib.IminPrintUtils;
import com.imin.printerlib.port.UsbPrinter;
import com.neostra.electronic.Electronic;
import com.rt.printerlibrary.bean.UsbConfigBean;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.connect.PrinterInterface;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.factory.connect.PIFactory;
import com.rt.printerlibrary.factory.connect.UsbFactory;
import com.rt.printerlibrary.factory.printer.PinPrinterFactory;
import com.rt.printerlibrary.printer.RTPrinter;

import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import com.credix.pinpaddriverwithandroidusage.GPIOUtils;


public class BaseActivity extends AppCompatActivity {

    //public PrintMe sunmi;
    public UsbManager mUsbManager;
    public UsbDevice mUsbDevice;
    private MyPresentation mPresentation;
    private MyPresentation mPresentationScreeen;
    private List<Product> productList;
    private Display[] displays;
    public WebView addedReceiptWebView;
    public static RTPrinter rtPrinter = null;
    public Handler handler = new Handler();
    public Handler cutterHandler = new Handler();
    public IminPrintUtils mIminPrintUtils;
    public JSONArray data = new JSONArray();
    public FrameLayout receiptWebView;

    private final int REQUEST_CODE =11324;
    private PinPrinterFactory printerFactory;
    public InputMethodManager inputMethodManager;

    public PrinterManager mPrinterManager;
    DeviceManager mDevice;

    private int height;


    private final static int PRNSTS_ERR = -256;            //Common error
    private final static int PRNSTS_ERR_DRIVER = -257;

    Electronic mElectronic;
    public enum PLATFORMS {iPOS,IMIN,SUNMI, UROVO};

//    public PLATFORMS current_platform = PLATFORMS.iPOS;

    public String barcode = "";
    public Bitmap barcodeBitmap = null;
    public Bitmap logo;
    private Context context;

    public boolean isDrawerOpen = false;
    public String fileName = GPIOUtils.Cash_3288; // Example GPIO file

//    public boolean dontOpenCashDrawer = false;
//
//    public void setDontOpenCashDrawer(boolean value) {
//        this.dontOpenCashDrawer = value;
//    }
//
//    public boolean consumeDontOpenCashDrawer() {
//        boolean v = this.dontOpenCashDrawer;
//        this.dontOpenCashDrawer = false;   // חד-פעמי: מאפס אחרי שימוש
//        return v;
//    }


    @TargetApi(Build.VERSION_CODES.O)
    private void disableAutofill() {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1) {
//            WebView.enableSlowWholeDocumentDraw();
            getWindow().getDecorView().setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS);

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Log.v(TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(updateLocale(newBase));
    }

   public static Context updateLocale(Context context) {
        // קבלת שפת ברירת המחדל ששמורה בהגדרות
        String languageCode = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
                .getString("My_Lang", "he");  // ברירת מחדל: עברית

        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //AppLogger.init(this);
        Log.e("WHICH_ACTIVITY", "onCreate of: " + getClass().getName());
        StrictMode.allowThreadDiskReads();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        super.onCreate(savedInstanceState);
        disableAutofill();
        //loadLocale();
        inputMethodManager = (InputMethodManager) BaseActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);

        //sunmi = new PrintMe(this);

        mDevice = new DeviceManager();
        mPrinterManager = new PrinterManager();
        try {
            Log.e("BOOTMMMMM", "2 mPrinterManager.open OK");
            mPrinterManager.open();
            Log.e("BOOTMMMM", "mPrinterManager.open OK");
            Log.e("PRINTER", "open OK");
        } catch (Throwable t) {
            Log.e("PRINTER", "open FAILED - continue without printer", t);
            // הכי חשוב: לא לעשות שום שימוש במדפסת אחרי זה

        }

        //presenet product

        context = this; // Save context reference for later use
        productList = new ArrayList<>();
        context = this;

    }

    public void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, resources.getDisplayMetrics());

        // שמירת השפה ב-SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", languageCode);
        editor.apply();

        // שימוש ב-Handler כדי למנוע בעיות קריסה
        new Handler(Looper.getMainLooper()).postDelayed(() -> recreate(), 200);
    }

    // פונקציה שטוענת את השפה השמורה
    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "he"); // ברירת מחדל: עברית
        setLocale(language);
    }

    public void showVideo(String name){

        if (mPresentationScreeen == null || !mPresentationScreeen.isShowing()) {
            MediaRouter mediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
            if (mediaRouter != null) {
                MediaRouter.RouteInfo route = mediaRouter.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_VIDEO);
                if (route != null) {
                    Display presentationDisplay = route.getPresentationDisplay();
                    if (presentationDisplay != null) {
                        mPresentationScreeen = new MyPresentation(this, presentationDisplay);
                        mPresentationScreeen.show();
                    }
                }
            }
        }
        if (mPresentationScreeen != null) {
            mPresentationScreeen.displayVidoe(name);
        }
    }

    public void showPicture(String name){
        if (mPresentationScreeen == null || !mPresentationScreeen.isShowing()) {
            MediaRouter mediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
            if (mediaRouter != null) {
                MediaRouter.RouteInfo route = mediaRouter.getSelectedRoute(MediaRouter.ROUTE_TYPE_LIVE_VIDEO);
                if (route != null) {
                    Display presentationDisplay = route.getPresentationDisplay();
                    if (presentationDisplay != null) {
                        mPresentationScreeen = new MyPresentation(this, presentationDisplay);
                        mPresentationScreeen.show();
                    }
                }
            }
        }

        if (mPresentationScreeen != null) {
            mPresentationScreeen.displayPicture(name);
        }
    }

    public void showLogo(String name){
        MediaRouter mediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
        if (mediaRouter != null) {
            MediaRouter.RouteInfo route = mediaRouter.getSelectedRoute(1);
            if (route != null) {
                Display presentationDisplay = route.getPresentationDisplay();
                if (presentationDisplay != null) {
                    if (mPresentationScreeen == null || !mPresentationScreeen.isShowing()) {
                        mPresentationScreeen = new MyPresentation(this, presentationDisplay, productList);
                        mPresentationScreeen.show();
                        mPresentationScreeen.displayLogo(name);

                    } else {
                        mPresentationScreeen.displayLogo(name);
                    }
                } else {
                    // Handle case where presentationDisplay is null
                }
            } else {
                // Handle case where route is null
            }
        } else {
            // Handle case where mediaRouter is null
        }
    }

    public void startProductPresentation(String name,String prod, double amount, Double price,int index,double totalAmount) {
        MediaRouter mediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
        if (mediaRouter != null) {
            MediaRouter.RouteInfo route = mediaRouter.getSelectedRoute(1);
            if (route != null) {
                Display presentationDisplay = route.getPresentationDisplay();

                if (presentationDisplay != null) {
                    productList.add(new Product(prod, amount, price,index,0,0,""));

                    if (mPresentationScreeen == null || !mPresentationScreeen.isShowing()) {
                        mPresentationScreeen = new MyPresentation(this, presentationDisplay, productList);
                        mPresentationScreeen.show();
                        mPresentationScreeen.updateProducts(productList,totalAmount);

                    } else {
                        mPresentationScreeen.updateProducts(productList,totalAmount); // Update products
                    }
                } else {
                    // Handle case where presentationDisplay is null
                }
            } else {
                // Handle case where route is null
            }
        } else {
            // Handle case where mediaRouter is null
        }
    }

        public void removeProductFromPresentation(int index, double totalprice) {
        if (mPresentationScreeen != null) {
            mPresentationScreeen.removeProduct(index,totalprice);
        }
    }

    public void plusCountProdFromPresentation(int index,double price, double totalprice) {
        if (mPresentationScreeen != null) {
            mPresentationScreeen.plusCountProdFromPresentation(index,price,totalprice);
        }
    }

    public void minusCountProdFromPresentation(int index,double price, double totalprice) {
        if (mPresentationScreeen != null) {
            mPresentationScreeen.minusCountProdFromPresentation(index,price,totalprice);
        }
    }

    public void changeCountProdFromPresentation(int index,int count, double totalpriceprod, double totalprice) {
        if (mPresentationScreeen != null) {
            mPresentationScreeen.changeCountProdFromPresentation(index,count,totalpriceprod,totalprice);
        }
    }
    public void addAnachaProdFromPresentation(int index,int count, double totalpriceprod,double anacha,double totalprice) {
        if (mPresentationScreeen != null) {
            mPresentationScreeen.addAnachaProdFromPresentation(index,count,totalpriceprod,anacha,totalprice);
        }
    }
    public void addDiscountProdFromPresentation(int index,String discountName,double discount ,double totalprice) {
        if (mPresentationScreeen != null) {
            mPresentationScreeen.addDiscountProdFromPresentation(index,discountName,discount,totalprice);
        }
    }
    public void addAnachaGeneralFromPresentation(double totalprice) {
        if (mPresentationScreeen != null) {
            mPresentationScreeen.addAnachaGeneralFromPresentation(totalprice);
        }
    }

    public void changeTotalPriceProduct(double totalprice) {
        if (mPresentationScreeen != null) {
            mPresentationScreeen.changeTotalPriceProduct(totalprice);
        }
    }


    public void stopProducrpresentation(){
        if (mPresentationScreeen != null) {
            mPresentationScreeen.clearDisplayedProducts();
        }
    }
    private boolean checkPermission() {
        boolean isAndroid14 = android.os.Build.VERSION.SDK_INT >= 34;
        if(! isAndroid14) {
            int result = ContextCompat.checkSelfPermission(BaseActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
            if (result == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        DisplayManager dm = (DisplayManager) getSystemService(DISPLAY_SERVICE);
        displays = dm.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);

        updateContents();

        if (mPresentation != null)
            mPresentation.setOnDismissListener(mOnDismissListener);

        if (Build.VERSION.SDK_INT >= 23) {

            if (checkPermission())
            {
                // Code for above or equal 23 API Oriented Device
                // Your Permission granted already .Do next code
            } else {
                boolean isAndroid14 = android.os.Build.VERSION.SDK_INT >= 34;
                if(! isAndroid14) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE);
                }
            }
        }

    }

    /**
     * Listens for when presentations are dismissed.
     */
    private final DialogInterface.OnDismissListener mOnDismissListener =
            new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (dialog == mPresentation) {
                        Log.i(TAG, "Presentation was dismissed.");
                        mPresentation = null;
                        updateContents();
                    }
                }
            };

    public void registerPrinter(Context mContext){
        mIminPrintUtils = IminPrintUtils.getInstance(BaseActivity.this);
        mIminPrintUtils.initPrinter(IminPrintUtils.PrintConnectType.USB);
        mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            if (device.getVendorId() == 1659 || device.getVendorId() == 4070 || device.getVendorId() == 8137 )
            {
                mUsbDevice = device;
            }

        }

        if (mUsbDevice == null) {
            Log.e("PrinterSetup", "BBBBBBBBBB No suitable USB device found. deviceList=" + deviceList.size());
            return;
        }

        int piFlags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            piFlags |= PendingIntent.FLAG_IMMUTABLE; // חובה באנדרואיד 12+
        }

        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(
                BaseActivity.this,

                0,
                new Intent(BaseActivity.this.getApplicationInfo().packageName),
                0
                //piFlags
        );
        UsbConfigBean configObj = new UsbConfigBean(BaseApp.getInstance(), mUsbDevice, mPermissionIntent);

        connectUSB(configObj);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getPlatform() == PLATFORMS.IMIN){
//                    current_platform = PLATFORMS.IMIN;
                }
            }
        },800);



    }

    private boolean checkIfMprinter(){
        try{
            if (mPrinterManager.getStatus()  != PRNSTS_ERR && mPrinterManager.getStatus() != PRNSTS_ERR_DRIVER)
                return  true;
            else
                return false;
        }catch(Exception e){
            return false;
        }
    }


    public PLATFORMS getPlatform() {
         if (mIminPrintUtils != null && mIminPrintUtils.getPrinterStatus(IminPrintUtils.PrintConnectType.USB) != -1 && mIminPrintUtils.getUsbPrinter() != null) {
            return PLATFORMS.IMIN;
//        } else if (sunmi.aidlUtil.isConnect()) {
//            return PLATFORMS.SUNMI;
        }else if(checkIfMprinter()){
            return PLATFORMS.UROVO;
        }
        else {
            return PLATFORMS.iPOS;
        }

    }

    public void connectUSB(UsbConfigBean usbConfigBean) {
        if (rtPrinter == null) {          // גיבוי
            initPrinter();
            if (rtPrinter == null) return;
        }
        UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        Map<String, UsbDevice> devices = mUsbManager.getDeviceList();
        PIFactory piFactory = new UsbFactory();
        PrinterInterface printerInterface = piFactory.create();
        printerInterface.setConfigObject(usbConfigBean);
        rtPrinter.setPrinterInterface(printerInterface);
        if (usbConfigBean.usbDevice!= null && mUsbManager.hasPermission(usbConfigBean.usbDevice)) {
            try {
                rtPrinter.connect(usbConfigBean);
                BaseApp.instance.setRtPrinter(rtPrinter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (usbConfigBean.usbDevice !=null){
            mUsbManager.requestPermission(usbConfigBean.usbDevice, usbConfigBean.pendingIntent);
        }
    }


    private void updateContents() {
        if(displays.length == 0) return;;

        if (mPresentation == null ){
            mPresentation = new MyPresentation(this,displays[0]);
            // mPresentation = new MyPresentation(this, presentationDisplay, productList);

        }
        try {
            mPresentation.show();
        } catch (WindowManager.InvalidDisplayException ex) {
            Log.w(TAG, "Couldn't show presentation!  Display was removed in "
                    + "the meantime.", ex);
            mPresentation = null;
        }
    }


    @Override
    protected void onStop() {
        // Be sure to call the super class.
        super.onStop();

        // Dismiss the presentation when the activity is not visible.
        if (mPresentation != null) {
            Log.i(TAG, "Dismissing presentation because the activity is no longer visible.");
            mPresentation.dismiss();
            mPresentation = null;
        }
    }

    private boolean isPrinterAvailable() {
        String manufacturer = android.os.Build.MANUFACTURER.toLowerCase();
        String model = android.os.Build.MODEL.toLowerCase();

        if(manufacturer.contains("rockchip") && model.contains("rk3288")){
            return false;
        }
        return true;
    }
    public void initPrinter() {
//        if (!isPrinterAvailable()) {
//            Log.w("PrinterInit", "No printer available. Skipping printer initialization.");
//            return;
//        }
        //BaseApp.instance.setCurrentCmdType(BaseEnum.CMD_PIN);
        BaseApp.instance.setCurrentCmdType(BaseEnum.CMD_ESC);


        printerFactory = new PinPrinterFactory();
        rtPrinter = printerFactory.create();

    }

    public void print() {
        Log.i("POS", "print() called");

        if (rtPrinter == null){
            Log.i("POS", "rtPrinter == null → calling initPrinter()");
            initPrinter();
        }
        boolean skipDrawer = false;//consumeDontOpenCashDrawer();

        if (!skipDrawer) {
            openDrawer();
        }

        Log.i("POS", "openDrawer() called, scheduling printerRunnable");

        handler.postDelayed(printerRunnable, 50);
    }



    Runnable printerRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i("POS", "mIminPrintUtils.printHtml() sent");
            try{
                 int added_height = 30;
                if(barcodeBitmap != null){
                    added_height = -250;
                }
                else if (getPlatform() == PLATFORMS.UROVO){
                    added_height = 00;
                }else if (getPlatform() == PLATFORMS.iPOS){
                    //added_height = 50;//לא ברור למה זה
                    //added_height = 0;
                    added_height = -200;//adytech
                }
                else if (getPlatform() == PLATFORMS.IMIN){
                    added_height = -300;
                }
                addedReceiptWebView.measure(View.MeasureSpec.makeMeasureSpec(
                        View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//                int height = addedReceiptWebView.getContentHeight() > 850?addedReceiptWebView.getMeasuredHeight(): addedReceiptWebView.getContentHeight();



                addedReceiptWebView.layout(0, 0, addedReceiptWebView.getMeasuredWidth(), height+added_height);

                Bitmap bitmap = Bitmap.createBitmap(addedReceiptWebView.getWidth(), height+added_height, Bitmap.Config.ARGB_8888);



                Canvas canvas = new Canvas(bitmap);
                Paint paint = new Paint();
                int iHeight = bitmap.getHeight();
                canvas.drawBitmap(bitmap, 0, iHeight, paint);
                addedReceiptWebView.draw(canvas);



                if(getPlatform() == PLATFORMS.IMIN){//) || getPlatform() == PLATFORMS.iPOS/* mIminPrintUtils.getPrinterStatus(IminPrintUtils.PrintConnectType.USB ) != -1 && mIminPrintUtils.getUsbPrinter() != null*/){

//                    try {
//                        URL url = new URL("https://android.yedatop.com/officefiles/papa/_MlaitekPro/panda.jpg300.jpg");
//                        logo = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    if (logo != null){
//                        if (logo.getWidth() < 350)
//                            logo = Bitmap.createScaledBitmap(logo, 250, 150, false);
//
//                        mIminPrintUtils.printSingleBitmap(logo,1);
//                        mIminPrintUtils.printAndFeedPaper(100);
//                    }
                    if (logo != null) {
                        int maxWidth = 350;

                        if (logo.getWidth() > maxWidth) {
                            int newWidth = maxWidth;
                            int newHeight = (logo.getHeight() * newWidth) / logo.getWidth();
                            logo = Bitmap.createScaledBitmap(logo, newWidth, newHeight, true);
                        }
                        Bitmap printLogo = prepareLogoForPrint(logo);
                        mIminPrintUtils.printSingleBitmap(printLogo, 1);
//                        mIminPrintUtils.printSingleBitmap(logo, 1);
                    }
                    mIminPrintUtils.printSingleBitmap(bitmap, 1);
                    if (barcode.length() > 1) {
                        if(barcodeBitmap == null) {
//                            mIminPrintUtils.printAndFeedPaper(0);
                            mIminPrintUtils.setBarCodeHeight(80);

//                            mIminPrintUtils.printBarCode(73, barcode, 1);
                            //connectType = 0;
                            mIminPrintUtils.printBarCode(2, barcode, 1);
                        } else {
                            mIminPrintUtils.printSingleBitmap(barcodeBitmap, 1);
                        }
                    }
                    //mIminPrintUtils.printAndFeedPaper(255); //קופהקטנה כמו טאבלט יחדAPOLO
                    //mIminPrintUtils.printAndFeedPaper(50);//קופהקטנה כמו טאבלט יחדAPOLO
                    mIminPrintUtils.printAndFeedPaper(110);
                    CutPaper();
                    if (data.length() > 1) {

                        handler.postDelayed(printAnother, 600);
                    }else{
                        clearPrinterResources();
                    }
                }else
                    if (getPlatform() == PLATFORMS.UROVO){
                    mPrinterManager.open();
                    mPrinterManager.setupPage(-1, -1);
                    if (logo != null){
                        if (logo.getWidth() > 350)
                            logo = Bitmap.createScaledBitmap(logo, 350, 150, false);
                        mPrinterManager.drawBitmap(logo, 0, 0);
                        mPrinterManager.printPage(0);  //Execution printing

                    }

                    mPrinterManager.drawBitmap(bitmap, 0, 0);
                    mPrinterManager.printPage(0);  //Execution printing


                    if (barcode.length() > 1) {
                        mPrinterManager.clearPage();
                        mPrinterManager.setupPage(-1, -1);
                        mPrinterManager.drawBarcode(barcode, 40, 80, 20, 2, 50, 0);
                        mPrinterManager.printPage(0);  //Execution printing

                    }

                    mPrinterManager.paperFeed(150);
                    mPrinterManager.clearPage();

                    if (data.length() > 1) {

                        handler.postDelayed(printAnother, 600);
                    }else{

                        clearPrinterResources();

                    }
                }
//                else if (getPlatform() == PLATFORMS.SUNMI) {
//                    if (logo != null){
//                        if (logo.getWidth() < 350)
//                            logo = Bitmap.createScaledBitmap(logo, 350, 150, false);
//
//                        sunmi.sendImageToPrinter(logo);
//                        sunmi.aidlUtil.print3Line();
//                    }
//
//                    sunmi.sendImageToPrinter(bitmap);
//
//                    if (barcode.length() > 1){
//                        sunmi.aidlUtil.printBarCode(barcode,8 /*code 128*/,80,2,0);
//                        sunmi.aidlUtil.print3Line();
//                    }
//                    sunmi.aidlUtil.print3Line();
//                    CutPaper();
//
//                    if (data.length() > 1) {
//
//                        handler.postDelayed(printAnother, 600);
//                    }else{
//
//                        clearPrinterResources();
//
//                    }
//                }
                else
                {

                    CmdFactory cmdFactory = new EscFactory();
                    Cmd escCmd = cmdFactory.create();

                    escCmd = Utils.addBitmap(escCmd, bitmap, bitmap.getWidth() + 20);

                    escCmd.append(escCmd.getHeaderCmd());
                    rtPrinter.writeMsg(escCmd.getAppendCmds());

                    CutPaper();

                    if (data.length() > 1) {

                        handler.postDelayed(printAnother, 500);
                    } else {
                        clearPrinterResources();
                        escCmd.clear();
                        escCmd = null;
                        canvas = null;
                        paint = null;
                    }
                }
            }
            catch(Exception e){}

        }
    };
    private Bitmap prepareLogoForPrint(Bitmap src) {
        Bitmap bmp = src.copy(Bitmap.Config.ARGB_8888, true);

        int width = bmp.getWidth();
        int height = bmp.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pixel = bmp.getPixel(x, y);

                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;

                int gray = (r + g + b) / 3;

                if (gray > 200) {
                    bmp.setPixel(x, y, 0xFFFFFFFF);
                } else {
                    bmp.setPixel(x, y, 0xFF000000);
                }
            }
        }

        return bmp;
    }

    public void openDrawer() {
        //Utils.openDrawer(rtPrinter); // adytech
        Log.i("DrawerDebug", "Trying to open drawer on platform: " + getPlatform());
        Log.i("DrawerDebug", "Manufacturer: " + Build.MANUFACTURER + ", Device: " + Build.MODEL);
        //IminSDKManager.opencashBox(this);
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        String deviceName = Build.MODEL;
        //IminSDKManager.opencashBox();

       if (getPlatform() == PLATFORMS.IMIN || (getPlatform() == PLATFORMS.iPOS && deviceName.contains("rk3568_r"))){
            if(manufacturer.contains("rockchip") && deviceName.contains("rk3288")){
                Log.i("DrawerDebug", "Before sending open drawer command");
                GPIOUtils.witchStatus((byte) 0x31, fileName);
                Log.i("DrawerDebug", "Command sent");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        GPIOUtils.witchStatus((byte) 0x30, fileName);
                        Log.i("DrawerDebug", "Drawer close command sent after delay");
                    }
                }, 500);
                isDrawerOpen = true;
                File portFile = new File(fileName);
                Log.i("DrawerDebug", "Port file exists: " + portFile.exists());

            }
            else{
                UsbDevice usbDevice = null;//APOLO צריך את הבלוק הזה אז לשים הערה את התנאי הקודם שלא ייכנס לשם
                UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
                HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
                for (UsbDevice device : deviceList.values()) {
                    Log.d("USB", "Found device: " + device.getDeviceName());
                    // פה אתה בודק אם זה המדפסת שלך
                     usbDevice = device; // זה המדפסת

                }
                int piFlags = PendingIntent.FLAG_UPDATE_CURRENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    piFlags |= PendingIntent.FLAG_IMMUTABLE;
                }

                PendingIntent permissionIntent = PendingIntent.getBroadcast(
                        context,
                        0,
                        new Intent("com.android.example.USB_PERMISSION"),
                        piFlags
                );
                usbManager.requestPermission(usbDevice, permissionIntent);
                UsbPrinter printer = new UsbPrinter(context);
                boolean isOpen = printer.isOpen();
                if (isOpen) {
                    Log.d("USB", "Printer connection opened");
                } else {
                    Log.e("USB", "Failed to open printer connection");
                }
                byte[] openCashDrawer = new byte[]{
                        0x1B, 0x70, 0x00, 0x19, (byte) 0xFA, 0x0A  // הוספתי \n בסוף
                };
                // שליחת פקודת ESC/POS דרך ה־SDK
                IminPrintUtils.getInstance(this).sendRAWData(openCashDrawer);

                //IminSDKManager.opencashBox();
                IminSDKManager.opencashBox(this);
                Log.d("CashBox", ">>> הפקודה נשלחה ל-SDK");
            }
            //CashDrawerOpener.openCashDrawer(getApplicationContext());
//        }else if (getPlatform() == PLATFORMS.SUNMI){
//            sunmi.aidlUtil.sendRawData(new byte [] {0x10, 0x14, 0x00, 0x00,0x00});
        }
        //else if (getPlatform() == PLATFORMS.iPOS || getPlatform() == PLATFORMS.UROVO){// בשביל המשולבת והטאבלט
        else if (getPlatform() == PLATFORMS.UROVO){

        }
        else if (getPlatform() == PLATFORMS.iPOS){

            if (manufacturer.contains("samsung") || manufacturer.contains("sprd")
                || Objects.equals(deviceName, "N6") || deviceName == "N6" ) {

            }
            else {
                Utils.openDrawer(rtPrinter); // Assuming Rockchip has a drawer
            }
        }
        else{
             Utils.openDrawer(rtPrinter);
        }
    }



    public void CutPaper(){
        if (getPlatform() == PLATFORMS.IMIN){
            mIminPrintUtils.partialCut();
//        }else if (getPlatform() == PLATFORMS.SUNMI){
//            sunmi.aidlUtil.sendRawData(new byte [] {0x1d, 0x56, 0x42, 0x00});
        }else{
            Utils.cutPaper(rtPrinter);
        }

    }
    public void clearPrinterResources(){
        barcode = "";
        data.remove(0);
        if (receiptWebView != null) {
            receiptWebView.removeAllViews();
            addedReceiptWebView.destroy();
            addedReceiptWebView = null;
        }
        if (getPlatform() == PLATFORMS.UROVO){
            mPrinterManager.clearPage();
            mPrinterManager.prn_close();
            mPrinterManager.close();
        }
    }

    public void downloadLogo(String _url){
        long t0 = SystemClock.elapsedRealtime();

        if (logo != null) return;;
        try {
            URL url = new URL(_url);
            logo = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            Log.e("LOGO", "downloadLogo OK in " + (SystemClock.elapsedRealtime()-t0) + "ms url=" + _url);

        } catch(IOException e) {
            System.out.println(e);
            Log.e("LOGO", "downloadLogo FAILED url=" + _url, e);

        }

    }

    public Runnable printAnother = new Runnable() {
        @Override
        public void run() {
            data.remove(0);
            BaseActivity.this.runOnUiThread(receiptPrinterRunnable);
        }

    };

    Runnable receiptPrinterRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i("POS", "printerRunnable running");
            if (addedReceiptWebView == null){
                loadWebView();
            }else{

            }
            addedReceiptWebView.loadData(data.optString(0), "text/html; charset=UTF-8", "utf-8");

        }
    };

    public  void loadWebView(){

        addedReceiptWebView = new WebView(getApplicationContext());
        addedReceiptWebView.addJavascriptInterface(jsInterface, "android");
        addedReceiptWebView.getSettings().setJavaScriptEnabled(true);
        FrameLayout.LayoutParams lp;
        if (getPlatform() == PLATFORMS.UROVO) {
            lp = new FrameLayout.LayoutParams(350, FrameLayout.LayoutParams.MATCH_PARENT);
        }else{
            lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        }
        lp.setMargins(0,0,0,0);// for imin machine
      //  lp.setMargins(0,0,100,0);
        addedReceiptWebView.setLayoutParams(lp);
        receiptWebView.addView(addedReceiptWebView,lp);

        addedReceiptWebView.setInitialScale(getScale());

        WebSettings settings = addedReceiptWebView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

        addedReceiptWebView.enableSlowWholeDocumentDraw();
        addedReceiptWebView.setHorizontalScrollBarEnabled(false);
        addedReceiptWebView.setVerticalScrollBarEnabled(false);


        addedReceiptWebView.setWebChromeClient(receiptChromClient);
        addedReceiptWebView.setWebViewClient(receiptWebViewClient);


    }

    public class MyJsInterface {
        private Context context;


        public MyJsInterface(Context context) {
            this.context = context;
        }

        @JavascriptInterface
        public void height(int _height) {
            height = (int)(_height*1) ;
        }
    }
    final MyJsInterface jsInterface = new MyJsInterface(BaseActivity.this);


    private int getScale(){
       /* Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        int width = display.getWidth();

        Double val = new Double(width)/new Double(getPlatform() ==PLATFORMS.UROVO ?500:1300);
        val = val * 100d;
        return val.intValue();*/
        return 100;
    }


    WebChromeClient receiptChromClient  = new WebChromeClient(){
        public void onProgressChanged(WebView view, int progress) {
            if (progress == 100) {
            }
        }
    };
    WebViewClient receiptWebViewClient = new WebViewClient(){


        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);


        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String urlNewString) {
            Log.e("WEB_DEBUG", "Loading MAIN_PATH = " + urlNewString);
            webView.loadUrl(urlNewString);
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (view.getProgress() == 100) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        print();
                    }
                },300);
//                print();
                /*try {

                    view.evaluateJavascript("document.getElementsByTagName(\"table\")", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            print();
                        }
                    });
                }catch(Exception e) {
                    print();
                }*/


                BaseActivity.this.hideSoftKeyboard(view);

            }
        }
    };


    public void hideSoftKeyboard(View v) {
        if (BaseActivity.this.getCurrentFocus() == null) return;

        inputMethodManager.hideSoftInputFromWindow(BaseActivity.this.getCurrentFocus().getWindowToken(), 0);
    }
}
