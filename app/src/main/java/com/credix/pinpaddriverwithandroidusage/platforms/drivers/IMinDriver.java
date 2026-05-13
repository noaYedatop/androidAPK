package com.credix.pinpaddriverwithandroidusage.platforms.drivers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.credix.pinpaddriverwithandroidusage.GPIOUtils;
import com.imin.library.IminSDKManager;
import com.imin.printerlib.IminPrintUtils;
import com.neostra.electronic.Electronic;
import com.neostra.electronic.ElectronicCallback;

import java.util.HashMap;

/**
 * נהג קופת iMin
 * <p>
 * מכיל את כל הלוגיקה הספציפית לקופת iMin:
 * - הדפסה דרך IminPrintUtils
 * - פתיחת מגירה דרך IminSDKManager או GPIO
 * - חיתוך נייר דרך partialCut
 * <p>
 * שאר הקוד באפליקציה לא יודע שזו קופת iMin —
 * הוא קורא רק לממשק IPlatformDriver.
 */
public class IMinDriver extends BaseDriver {

    private static final String TAG = "IMinDriver";

    private final IminPrintUtils mIminPrintUtils;
    private final Handler handler;

    // GPIO — לקופות iMin על Rockchip rk3288
    private final String fileName = GPIOUtils.Cash_3288;

    public IMinDriver(Context context) {
        super(context);
        mIminPrintUtils = IminPrintUtils.getInstance(context);
        handler = new Handler(Looper.getMainLooper());
        Log.i(TAG, "IMinDriver נוצר");
    }

    private Electronic mElectronic;
    private double iMinWeight = 0;

    private ElectronicCallback iMinCallback = new ElectronicCallback() {
        @Override
        public void electronicStatus(String weight, String status) {
            iMinWeight = Double.parseDouble(weight);
        }
    };

    public void connectScale() {
        try {
            mElectronic = new Electronic.Builder().setDevicePath("/dev/ttyS1").setBaudrate(9600).setReceiveCallback(iMinCallback).builder();
        } catch (Exception e) {
            Log.e(TAG, "connectScale נכשל");
        }
    }

    @Override
    public double getWeight() {
        if (mElectronic == null) {
            connectScale(); // מתחבר רק כשצריך, פעם אחת
        }
        return iMinWeight;
    }

    @Override
    public void tare() {
        if (mElectronic != null) mElectronic.removePeel();
    }

    @Override
    public void zeroScale() {
        if (mElectronic != null) mElectronic.turnZero();
    }

    // ────────────────────────────────
    // הדפסה
    // ────────────────────────────────

    @Override
    public void print(Bitmap bitmap, String barcode, Bitmap logo) throws Exception {
        // הדפסת לוגו
        if (logo != null) {
            int maxWidth = 350;
            if (logo.getWidth() > maxWidth) {
                int newHeight = (logo.getHeight() * maxWidth) / logo.getWidth();
                logo = Bitmap.createScaledBitmap(logo, maxWidth, newHeight, true);
            }
            Bitmap printLogo = prepareLogoForPrint(logo);
            mIminPrintUtils.printSingleBitmap(printLogo, 1);
        }

        // הדפסת גוף החשבונית
        mIminPrintUtils.printSingleBitmap(bitmap, 1);

        // הדפסת ברקוד
        if (barcode != null && barcode.length() > 1) {
            mIminPrintUtils.setBarCodeHeight(80);
            mIminPrintUtils.printBarCode(2, barcode, 1);
        }

        // הזנת נייר וחיתוך
        mIminPrintUtils.printAndFeedPaper(110);
        cutPaper();
    }

    @Override
    public void cutPaper() {
        mIminPrintUtils.partialCut();
    }

    @Override
    public void clearPrinterResources() {
        // iMin לא צריך ניקוי מיוחד מעבר לניקוי הבסיסי ב-BaseActivity
    }

    @Override
    public int getPrintWidth() {
        return -1; // MATCH_PARENT — iMin מדפיסה ברוחב מלא
    }

    // ────────────────────────────────
    // מגירה
    // ────────────────────────────────

    @Override
    public void openDrawer() {
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        String deviceName = Build.MODEL;

        Log.i(TAG, "פותחת מגירה | יצרן: " + manufacturer + " | דגם: " + deviceName);

        if (manufacturer.contains("rockchip") && deviceName.contains("rk3288")) {
            // פתיחה דרך GPIO — קופות ישנות על Rockchip rk3288
            Log.i(TAG, "פתיחת מגירה דרך GPIO");
            GPIOUtils.witchStatus((byte) 0x31, fileName);
            handler.postDelayed(() -> {
                GPIOUtils.witchStatus((byte) 0x30, fileName);
                Log.i(TAG, "פקודת סגירת מגירה נשלחה");
            }, 500);

        } else {
            // פתיחה דרך USB + IminSDKManager — קופות חדשות (APOLO וכו׳)
            Log.i(TAG, "פתיחת מגירה דרך USB + IminSDKManager");

            UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();

            UsbDevice usbDevice = null;
            for (UsbDevice device : deviceList.values()) {
                Log.d(TAG, "נמצא התקן USB: " + device.getDeviceName());
                usbDevice = device;
            }

            if (usbDevice != null) {
                int piFlags = PendingIntent.FLAG_UPDATE_CURRENT;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    piFlags |= PendingIntent.FLAG_IMMUTABLE;
                }
                PendingIntent permissionIntent = PendingIntent.getBroadcast(context, 0, new Intent("com.android.example.USB_PERMISSION"), piFlags);
                usbManager.requestPermission(usbDevice, permissionIntent);
            }

            // שליחת פקודת ESC/POS לפתיחת מגירה
            byte[] openCashDrawer = new byte[]{0x1B, 0x70, 0x00, 0x19, (byte) 0xFA, 0x0A};
            IminPrintUtils.getInstance(context).sendRAWData(openCashDrawer);

            IminSDKManager.opencashBox(context);
            Log.d(TAG, "פקודת פתיחת מגירה נשלחה ל-SDK");
        }
    }

    /**
     * ממיר לוגו לשחור-לבן לפני הדפסה.
     * נלקח כמו שהוא מ-BaseActivity הישנה.
     */
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
                bmp.setPixel(x, y, gray > 200 ? 0xFFFFFFFF : 0xFF000000);
            }
        }
        return bmp;
    }
    @Override
    public String prepareHtmlForPrint(String html, String barcode) {
        String result = html.replace("max-width: 150px!important;", "");
        result = result.replaceAll("width: 250px;", "width: 350px;max-width: 350px!important;");
        result = result.replace("margin-left: 5px", "margin-left: 15px");
        result = result.replaceAll("width:110px;", "width: 45%;");
        result = result.replace("font-size: 15;", "font-size: 39px !important;");
        result = result.replaceAll("width:120px;", "width: 45%;");
        result = result.replaceAll("width:70px;", "width: 10%;");
        result = result.replaceAll("max-width: 150px!important;width: 150px;", "width: 250px;");
        result = result.replaceAll("width: 250px;", "width: 750px;max-width:750px");

        if (barcode != null && !barcode.equals("0")) {
            // barcode נשמר ב-BaseActivity
        }
        return result;
    }

    @Override
    public int getAddedHeight() { return 300; }

    @Override
    public int getPrintFontSize() {
        return 27;
    }

}