package com.credix.pinpaddriverwithandroidusage.platforms.hardware;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * WeightManager — כל לוגיקת קריאת משקל במקום אחד.
 *
 * סוג המשקל נקבע רק לפי הגדרת מנהל המשתמשים בשרת.
 * הפלטפורמה (iMin/iPOS) לא קובעת — רק ההגדרה.
 *
 * שימוש מ-MainActivity:
 *   double weight = WeightManager.getWeight(context, typeOfWeight, iminWeight);
 *
 * מיפוי סוגי משקל (תואם למנהל המשתמשים):
 *   0 = CS600
 *   1 = Bip30
 *   2 = Newtech איילון
 *   3 = Adytech משולב
 *   4 = iMin משולב (משקל פנימי מובנה)
 */
public class WeightManager {

    private static final String TAG = "WeightManager";

    // קבועים — תואמים בדיוק למנהל המשתמשים בשרת
    public static final int TYPE_CS600        = 0;
    public static final int TYPE_BIP30        = 1;
    public static final int TYPE_NEWTECH      = 2;
    public static final int TYPE_ADYTECH      = 3;
    public static final int TYPE_IMIN_BUILTIN = 4;

    /**
     * קורא משקל לפי סוג שהוגדר במנהל המשתמשים.
     *
     * @param context      קונטקסט
     * @param typeOfWeight סוג המשקל (0-4) שהגיע מה-JavaScript
     * @param iminWeight   הערך הנוכחי ממשקל iMin פנימי (רלוונטי רק ל-type=4)
     * @return משקל בגרמים, או -1 אם נכשל
     */
    public static double getWeight(Context context, int typeOfWeight, double iminWeight) {
        Log.i(TAG, "getWeight: type=" + typeOfWeight);

        switch (typeOfWeight) {
            case TYPE_CS600:        return readCs600(context);
            case TYPE_BIP30:        return readBip30(context);
            case TYPE_NEWTECH:      return readNewtech(context);
            case TYPE_ADYTECH:      return AdytechWeightReader.getLastWeight();
            case TYPE_IMIN_BUILTIN: return iminWeight;
            default:
                Log.w(TAG, "סוג משקל לא מוכר: " + typeOfWeight);
                return -1;
        }
    }

    // ════════════════════════════════════════════════════════════
    // CS600
    // ════════════════════════════════════════════════════════════

    private static double readCs600(Context context) {
        UsbSerialPort port = null;
        UsbDeviceConnection connection = null;
        try {
            UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            List<UsbSerialDriver> drivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
            if (drivers.isEmpty()) return -1;

            UsbSerialDriver driver = drivers.get(0);
            connection = manager.openDevice(driver.getDevice());
            if (connection == null) return -1;

            port = driver.getPorts().get(0);
            port.open(connection);
            port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

            byte[] flush = new byte[2048];
            int aa;
            do { aa = port.read(flush, 250); } while (aa > 0);

            port.write("w\r\n".getBytes(), 250);

            byte[] buf = new byte[2048];
            int len = port.read(buf, 250);
            if (len < 3) return -1;

            String weightStr = new String(buf)
                    .replace("\r", "").replace("\n", "").replace("g", "").trim();
            String ss = weightStr.substring(0, Math.min(6, weightStr.length()));
            return Double.parseDouble(ss.trim());

        } catch (Exception e) {
            Log.e(TAG, "CS600 שגיאה", e);
            return -1;
        } finally {
            safeClose(port, connection);
        }
    }

    // ════════════════════════════════════════════════════════════
    // Bip30
    // ════════════════════════════════════════════════════════════

    private static double readBip30(Context context) {
        UsbSerialPort port = null;
        UsbDeviceConnection connection = null;
        try {
            UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            List<UsbSerialDriver> drivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
            if (drivers.isEmpty()) return -1;

            UsbSerialDriver driver = drivers.get(0);
            connection = manager.openDevice(driver.getDevice());
            if (connection == null) return -199;

            port = driver.getPorts().get(0);
            port.open(connection);
            port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

            clearBuffer(port);
            port.write("w\r\n".getBytes(StandardCharsets.UTF_8), 1000);
            Thread.sleep(300);

            byte[] buffer = new byte[64];
            int totalLen = 0;
            StringBuilder response = new StringBuilder();

            for (int attempt = 0; attempt < 3; attempt++) {
                int len = port.read(buffer, 1500);
                if (len > 0) {
                    totalLen += len;
                    response.append(new String(buffer, 0, len, StandardCharsets.UTF_8));
                    if (response.toString().contains("\n")) break;
                }
                Thread.sleep(100);
            }

            if (totalLen < 1) return -1;

            String weightStr = response.toString()
                    .replace("\r", "").replace("\n", "").replace("g", "").trim();
            if (weightStr.isEmpty()) return -1;

            double weight = parseWeight(weightStr);
            return (weight == 0.0) ? -1 : weight;

        } catch (Exception e) {
            Log.e(TAG, "Bip30 שגיאה", e);
            return -1;
        } finally {
            safeClose(port, connection);
        }
    }

    // ════════════════════════════════════════════════════════════
    // Newtech איילון
    // ════════════════════════════════════════════════════════════

    private static double readNewtech(Context context) {
        UsbSerialPort port = null;
        UsbDeviceConnection connection = null;
        try {
            UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
            List<UsbSerialDriver> drivers = UsbSerialProber.getDefaultProber().findAllDrivers(manager);
            if (drivers.isEmpty()) return -1;

            UsbSerialDriver driver = drivers.get(0);
            connection = manager.openDevice(driver.getDevice());
            if (connection == null) return -1;

            port = driver.getPorts().get(0);
            port.open(connection);
            port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);

            port.write("w\r\n".getBytes(), 250);
            Thread.sleep(250);

            byte[] buf = new byte[2048];
            int len = port.read(buf, 1250);
            if (len < 3) return -1;

            String weightStr = new String(buf).trim();
            String s = weightStr.substring(0, Math.min(7, weightStr.length()));
            return Double.parseDouble(s.trim());

        } catch (Exception e) {
            Log.e(TAG, "Newtech שגיאה", e);
            return -1;
        } finally {
            safeClose(port, connection);
        }
    }

    // ════════════════════════════════════════════════════════════
    // עזר משותף
    // ════════════════════════════════════════════════════════════

    private static void clearBuffer(UsbSerialPort port) {
        try {
            byte[] buffer = new byte[64];
            while (port.read(buffer, 100) > 0) { /* ריקון */ }
        } catch (Exception e) {
            Log.w(TAG, "clearBuffer שגיאה", e);
        }
    }

    private static double parseWeight(String weightStr) {
        if (weightStr.startsWith(".")) weightStr = "0" + weightStr;
        return Double.parseDouble(weightStr);
    }

    private static void safeClose(UsbSerialPort port, UsbDeviceConnection connection) {
        try { if (port != null) port.close(); } catch (Exception ignore) {}
        try { if (connection != null) connection.close(); } catch (Exception ignore) {}
    }
}