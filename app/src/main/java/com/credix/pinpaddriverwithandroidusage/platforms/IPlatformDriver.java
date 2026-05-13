package com.credix.pinpaddriverwithandroidusage.platforms;

import android.graphics.Bitmap;

import java.io.UnsupportedEncodingException;

/**
 * ממשק אחיד לכל סוגי הקופות.
 * כל נהג פלטפורמה חייב לממש את כל הפונקציות האלה.
 * MainActivity ו-BaseActivity לא יודעות מי מממש — הן קוראות רק לממשק הזה.
 */
public interface IPlatformDriver {

    // ────────────────────────────────
    // הדפסה
    // ────────────────────────────────

    /**
     * מדפיסה חשבונית.
     * @param bitmap תמונת ה-HTML המומרת
     * @param barcode ברקוד להדפסה, או מחרוזת ריקה אם אין
     * @param logo לוגו להדפסה, או null אם אין
     */
    void print(Bitmap bitmap, String barcode, Bitmap logo) throws Exception;

    /**
     * חותכת נייר בסיום הדפסה.
     */
    void cutPaper();

    /**
     * מנקה משאבי הדפסה לאחר סיום.
     */
    void clearPrinterResources();

    /**
     * מחזירה את רוחב תצוגת ה-WebView להדפסה בפיקסלים.
     */
    int getPrintWidth();

    /**
     * מחזירה גובה נוסף לחישוב גובה הביטמפ.
     * ערך שלילי = קיצור, ערך חיובי = הרחבה.
     */
    int getAddedHeight();

    // ────────────────────────────────
    // מגירה
    // ────────────────────────────────

    /**
     * פותחת מגירת מזומן.
     */
    void openDrawer();

    // ────────────────────────────────
    // משקל
    // ────────────────────────────────

    /**
     * קוראת משקל נוכחי.
     * @return משקל בגרמים, או -1 אם אין חיבור
     */
    double getWeight();

    /**
     * מאפסת משקל (טרה).
     */
    void tare();

    /**
     * מאפסת לאפס.
     */
    void zeroScale();

    // ────────────────────────────────
    // סריקה
    // ────────────────────────────────

    /**
     * מפעילה סריקת ברקוד.
     * התוצאה מוחזרת דרך הממשק הקיים של האפליקציה.
     */
    void startScan();

    // ────────────────────────────────
    // תשלום
    // ────────────────────────────────

    /**
     * מבצעת חיוב אשראי.
     * @param request פרטי העסקה בפורמט JSON
     * @return תוצאת העסקה בפורמט JSON
     */
    String pay(String request);

    /**
     * שולפת דוח עסקאות.
     * @return דוח בפורמט JSON
     */
    String getPaymentReport();

    // מכין את ה-HTML לפני טעינה ל-WebView
    String prepareHtmlForPrint(String html, String barcode);

    // מחזיר גודל פונט לפי פלטפורמה
    int getPrintFontSize();
}
