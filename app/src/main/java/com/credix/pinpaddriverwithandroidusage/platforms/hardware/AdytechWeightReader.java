package com.credix.pinpaddriverwithandroidusage.platforms.hardware;

import android.util.Log;

import com.semtom.weightlib.IReadWeightListener;
import com.semtom.weightlib.WeighUtils;

/**
 * AdytechWeightReader — ניהול משקל Adytech.
 *
 * Adytech עובד בצורה שונה — הוא מאזין ברקע ומעדכן ערך באופן רציף.
 * לכן הוא מנוהל בנפרד עם אתחול חד-פעמי ו-getLastWeight().
 *
 * שימוש:
 *   AdytechWeightReader.init(); // באתחול האפליקציה
 *   double weight = AdytechWeightReader.getLastWeight(); // בכל קריאת משקל
 *   AdytechWeightReader.tare(); // טרה
 *   AdytechWeightReader.zero(); // אפס
 *   AdytechWeightReader.release(); // בסגירת האפליקציה
 */
public class AdytechWeightReader {

    private static final String TAG = "AdytechWeightReader";

    private static WeighUtils weighUtils;
    private static IReadWeightListener listener;
    private static volatile double lastWeight = 0.0;
    private static volatile boolean isReady = false;

    // ────────────────────────────────────────────────────────────
    // אתחול — נקרא פעם אחת
    // ────────────────────────────────────────────────────────────

    public static synchronized void init() {
        if (isReady && weighUtils != null) return;

        try {
            Log.i(TAG, "מאתחל Adytech...");

            lastWeight = 0.0;

            weighUtils = WeighUtils.builder();

            listener = new IReadWeightListener() {
                @Override
                public void onReadingWeight(double weight, int status, int tare) {
                    lastWeight = weight;
                    Log.d(TAG, "משקל עדכני: " + weight + " status=" + status);
                }

                @Override
                public void onError(String msg) {
                    Log.e(TAG, "שגיאת משקל: " + msg);
                }

                @Override
                public void initValue(String value) {
                    Log.i(TAG, "initValue: " + value);
                }
            };

            weighUtils.open();
            weighUtils.bindWeightRead(listener);
            isReady = true;

            Log.i(TAG, "Adytech אותחל בהצלחה");

        } catch (Exception e) {
            Log.e(TAG, "אתחול Adytech נכשל", e);
            isReady = false;
            weighUtils = null;
            listener = null;
        }
    }

    // ────────────────────────────────────────────────────────────
    // קריאת משקל
    // ────────────────────────────────────────────────────────────

    /**
     * מחזיר את המשקל האחרון שהתקבל מהסנסור.
     * אם הסנסור לא מאותחל — מנסה לאתחל אוטומטית.
     */
    public static double getLastWeight() {
        if (!isReady) {
            Log.w(TAG, "Adytech לא מאותחל — מנסה לאתחל");
            init();
        }
        return lastWeight;
    }

    // ────────────────────────────────────────────────────────────
    // טרה ואפס
    // ────────────────────────────────────────────────────────────

    public static void tare() {
        try {
            if (weighUtils != null) {
                Log.i(TAG, "טרה");
                weighUtils.resetBalance_qp();
                lastWeight = 0.0;
            }
        } catch (Exception e) {
            Log.e(TAG, "טרה נכשל", e);
        }
    }

    public static void zero() {
        try {
            if (weighUtils != null) {
                Log.i(TAG, "אפס");
                weighUtils.resetBalance();
                lastWeight = 0.0;
            }
        } catch (Exception e) {
            Log.e(TAG, "אפס נכשל", e);
        }
    }

    // ────────────────────────────────────────────────────────────
    // שחרור — נקרא ב-onDestroy
    // ────────────────────────────────────────────────────────────

    public static synchronized void release() {
        try {
            if (weighUtils != null && listener != null) {
                weighUtils.unbindWeightRead(listener);
            }
        } catch (Exception e) {
            Log.e(TAG, "unbind נכשל", e);
        }
        try {
            if (weighUtils != null) {
                weighUtils.cwloseSerialPort();
            }
        } catch (Exception e) {
            Log.e(TAG, "סגירת פורט נכשלה", e);
        }

        weighUtils = null;
        listener = null;
        isReady = false;
        lastWeight = 0.0;
        Log.i(TAG, "Adytech שוחרר");
    }
}