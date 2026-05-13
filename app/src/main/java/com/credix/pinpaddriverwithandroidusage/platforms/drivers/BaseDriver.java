package com.credix.pinpaddriverwithandroidusage.platforms.drivers;

import android.content.Context;
import android.util.Log;

import com.credix.pinpaddriverwithandroidusage.platforms.IPlatformDriver;

/**
 * נהג בסיס — מכיל מימוש ברירת מחדל לפונקציות שרוב הקופות לא מממשות.
 *
 * כל נהג ספציפי יורש מכאן ומממש רק את מה שרלוונטי אליו.
 * כך אין צורך לממש פונקציות ריקות בכל נהג.
 *
 * לדוגמה: קופת נקסגו אין לה משקל — לא מממשת getWeight, מקבלת -1 מכאן.
 */
public abstract class BaseDriver implements IPlatformDriver {

    protected final Context context;
    protected static final String TAG = "PlatformDriver";

    public BaseDriver(Context context) {
        this.context = context;
    }

    // ────────────────────────────────
    // ברירות מחדל — כל נהג מחליף מה שרלוונטי אליו
    // ────────────────────────────────

    @Override
    public double getWeight() {
        Log.w(TAG, "getWeight לא נתמך בפלטפורמה זו");
        return -1;
    }

    @Override
    public void tare() {
        Log.w(TAG, "tare לא נתמך בפלטפורמה זו");
    }

    @Override
    public void zeroScale() {
        Log.w(TAG, "zeroScale לא נתמך בפלטפורמה זו");
    }

    @Override
    public void startScan() {
        Log.w(TAG, "startScan לא נתמך בפלטפורמה זו");
    }

    @Override
    public String pay(String request) {
        Log.w(TAG, "pay לא נתמך בפלטפורמה זו");
        return "{\"error\": \"תשלום לא נתמך בפלטפורמה זו\"}";
    }

    @Override
    public String getPaymentReport() {
        Log.w(TAG, "getPaymentReport לא נתמך בפלטפורמה זו");
        return "{\"error\": \"דוח לא נתמך בפלטפורמה זו\"}";
    }

    @Override
    public void clearPrinterResources() {
        // ברירת מחדל ריקה — כל נהג מוסיף מה שצריך
    }

    @Override
    public int getPrintWidth() {
        return 576; // רוחב ברירת מחדל
    }

    @Override
    public int getAddedHeight() {
        return -200; // ברירת מחדל
    }
}
