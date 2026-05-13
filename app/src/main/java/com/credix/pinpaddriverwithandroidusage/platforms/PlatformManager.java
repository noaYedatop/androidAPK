package com.credix.pinpaddriverwithandroidusage.platforms;

import android.content.Context;
import android.util.Log;

import com.credix.pinpaddriverwithandroidusage.platforms.drivers.IMinDriver;
import com.credix.pinpaddriverwithandroidusage.platforms.drivers.IPosDriver;
import com.credix.pinpaddriverwithandroidusage.platforms.drivers.UrovoDriver;

/**
 * מנהל פלטפורמה.
 *
 * אחראי על החלטה אחת בלבד: איזה נהג להחזיר.
 * כל שאר הקוד באפליקציה עובד עם IPlatformDriver בלבד — לא יודע מי מאחוריו.
 *
 * שימוש:
 *   IPlatformDriver driver = PlatformManager.getDriver(this, getPlatform());
 *
 * להוסיף קופה חדשה:
 *   1. ליצור קובץ חדש ב- platforms/drivers/
 *   2. להוסיף case אחד פה למטה
 *   זהו — לא לגעת בשום קובץ אחר.
 */
public class PlatformManager {

    private static final String TAG = "PlatformManager";

    /**
     * סוגי הפלטפורמות הנתמכות.
     * מועבר מ-BaseActivity שמזהה את הפלטפורמה לפי חומרה.
     */
    public enum Platform {
        IMIN,
        UROVO,
        IPOS,
        SUNMI
    }

    /**
     * מחזירה את הנהג המתאים לפלטפורמה.
     *
     * @param context הקונטקסט של האקטיביטי
     * @param platform הפלטפורמה שזוהתה על ידי BaseActivity
     * @return נהג שמממש את IPlatformDriver
     */
    public static IPlatformDriver getDriver(Context context, Platform platform) {
        Log.i(TAG, "יוצר נהג לפלטפורמה: " + platform);

        switch (platform) {
            case IMIN:
                return new IMinDriver(context);

            case UROVO:
                return new UrovoDriver(context);

            case SUNMI:
                // כרגע SUNMI משתמש באותו נהג כמו iPOS
                // כשיהיה צורך — ליצור SunmiDriver נפרד ולשנות כאן בלבד
                return new IPosDriver(context);

            case IPOS:
            default:
                return new IPosDriver(context);
        }
    }
}