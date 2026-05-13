package com.credix.pinpaddriverwithandroidusage.platforms.drivers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;

import com.credix.pinpaddriverwithandroidusage.Utils;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.printer.RTPrinter;

import java.util.Objects;

/**
 * נהג קופת iPOS
 *
 * ברירת המחדל — מכסה את כל הקופות שאינן iMin או Urovo:
 * - Samsung, Nexgo, Sunmi, Dangot ועוד
 *
 * הדפסה דרך RTPrinter עם פקודות ESC/POS.
 * פתיחת מגירה דרך Utils.openDrawer — חוץ מדגמים ספציפיים שאין להם מגירה.
 */
public class IPosDriver extends BaseDriver {

    private static final String TAG = "IPosDriver";

    private final RTPrinter rtPrinter;

    public IPosDriver(Context context) {
        super(context);
        // rtPrinter מגיע מ-BaseActivity שכבר אתחלה אותו
        rtPrinter = com.credix.pinpaddriverwithandroidusage.BaseActivity.rtPrinter;
        Log.i(TAG, "IPosDriver נוצר");
    }


    // ────────────────────────────────
    // הדפסה
    // ────────────────────────────────

    @Override
    public void print(Bitmap bitmap, String barcode, Bitmap logo) {
        try {
            CmdFactory cmdFactory = new EscFactory();
            Cmd escCmd = cmdFactory.create();

            escCmd = Utils.addBitmap(escCmd, bitmap, bitmap.getWidth() + 20);
            escCmd.append(escCmd.getHeaderCmd());
            rtPrinter.writeMsg(escCmd.getAppendCmds());

            cutPaper();

        } catch (Exception e) {
            Log.e(TAG, "שגיאה בהדפסה", e);
        }
    }

    @Override
    public void cutPaper() {
        Utils.cutPaper(rtPrinter);
    }

    @Override
    public void clearPrinterResources() {
        // iPOS לא צריך ניקוי מיוחד מעבר לניקוי הבסיסי ב-BaseActivity
    }

    @Override
    public int getPrintWidth() {
        return -1; // MATCH_PARENT
    }

    // ────────────────────────────────
    // מגירה
    // ────────────────────────────────

    @Override
    public void openDrawer() {
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        String deviceName   = Build.MODEL;

        Log.i(TAG, "פותחת מגירה | יצרן: " + manufacturer + " | דגם: " + deviceName);

        // דגמים שאין להם מגירה — לא עושים כלום
        boolean noDrawer = manufacturer.contains("samsung")
                || manufacturer.contains("sprd")
                || Objects.equals(deviceName, "N6");

        if (noDrawer) {
            Log.i(TAG, "דגם ללא מגירה — מדלגים");
            return;
        }

        // כל שאר הדגמים — פתיחה דרך Utils
        Utils.openDrawer(rtPrinter);
    }

    @Override
    public String prepareHtmlForPrint(String html, String barcode) {
        String result = html.replace("max-width: 150px!important;", "");
        result = result.replace("font-size: 15;", "font-size: 39px !important;");
        return result;
    }

    @Override
    public int getAddedHeight() { return 220; }

    @Override
    public int getPrintFontSize() {
        return 27;
    }
}