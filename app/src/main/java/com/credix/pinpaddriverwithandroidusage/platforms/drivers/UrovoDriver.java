package com.credix.pinpaddriverwithandroidusage.platforms.drivers;

import android.content.Context;
import android.device.PrinterManager;
import android.graphics.Bitmap;
import android.util.Log;

/**
 * נהג קופת Urovo
 *
 * מכיל את כל הלוגיקה הספציפית לקופת Urovo:
 * - הדפסה דרך PrinterManager
 * - פתיחת מגירה — Urovo לא פותחת מגירה
 * - חיתוך נייר דרך Utils
 */
public class UrovoDriver extends BaseDriver {

    private static final String TAG = "UrovoDriver";

    private final PrinterManager mPrinterManager;

    private final static int PRNSTS_ERR        = -256;
    private final static int PRNSTS_ERR_DRIVER = -257;

    public UrovoDriver(Context context) {
        super(context);
        mPrinterManager = new PrinterManager();
        try {
            mPrinterManager.open();
            Log.i(TAG, "UrovoDriver נוצר, PrinterManager פתוח");
        } catch (Throwable t) {
            Log.e(TAG, "PrinterManager.open נכשל", t);
        }
    }


    // ────────────────────────────────
    // הדפסה
    // ────────────────────────────────

    @Override
    public void print(Bitmap bitmap, String barcode, Bitmap logo) {
        try {
            mPrinterManager.open();
            mPrinterManager.setupPage(-1, -1);

            // הדפסת לוגו
            if (logo != null) {
                if (logo.getWidth() > 350) {
                    logo = Bitmap.createScaledBitmap(logo, 350, 150, false);
                }
                mPrinterManager.drawBitmap(logo, 0, 0);
                mPrinterManager.printPage(0);
            }

            // הדפסת גוף החשבונית
            mPrinterManager.drawBitmap(bitmap, 0, 0);
            mPrinterManager.printPage(0);

            // הדפסת ברקוד
            if (barcode != null && barcode.length() > 1) {
                mPrinterManager.clearPage();
                mPrinterManager.setupPage(-1, -1);
                mPrinterManager.drawBarcode(barcode, 40, 80, 20, 2, 50, 0);
                mPrinterManager.printPage(0);
            }

            mPrinterManager.paperFeed(150);
            mPrinterManager.clearPage();

        } catch (Exception e) {
            Log.e(TAG, "שגיאה בהדפסה", e);
        }
    }

    @Override
    public void cutPaper() {
        // Urovo לא חותכת נייר — אין פעולה
        Log.i(TAG, "cutPaper — Urovo לא חותכת נייר");
    }

    @Override
    public void clearPrinterResources() {
        try {
            mPrinterManager.clearPage();
            mPrinterManager.prn_close();
            mPrinterManager.close();
            Log.i(TAG, "clearPrinterResources — PrinterManager נסגר");
        } catch (Exception e) {
            Log.e(TAG, "שגיאה בסגירת PrinterManager", e);
        }
    }

    @Override
    public int getPrintWidth() {
        return 350; // Urovo מדפיסה ברוחב 350
    }

    // ────────────────────────────────
    // מגירה
    // ────────────────────────────────

    @Override
    public void openDrawer() {
        // Urovo לא פותחת מגירה
        Log.i(TAG, "openDrawer — Urovo לא פותחת מגירה");
    }


    // ────────────────────────────────
    // סריקה — Urovo משתמשת בלייזר מובנה
    // ────────────────────────────────

    @Override
    public void startScan() {
        // הלוגיקה של ZXingScannerView תועבר לכאן מ-MainActivity
        Log.w(TAG, "startScan — יש להעביר לוגיקה מ-MainActivity");
    }

    @Override
    public String prepareHtmlForPrint(String html, String barcode) {
        String result = html.replace("max-width: 150px!important;", "");
        result = result + "<style type=\"text/css\">\timg{visibility: hidden;\theight: 0px;}\tbody{height: 0px;}</style>";
        return result;
    }

    @Override
    public int getAddedHeight() { return 0; }

    @Override
    public int getPrintFontSize() {
        return 24;
    }
}