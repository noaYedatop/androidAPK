package com.credix.pinpaddriverwithandroidusage;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.enumerate.BmpPrintMode;
import com.rt.printerlibrary.enumerate.CommonEnum;
import com.rt.printerlibrary.exception.SdkException;
import com.rt.printerlibrary.factory.cmd.CmdFactory;
import com.rt.printerlibrary.printer.RTPrinter;
import com.rt.printerlibrary.setting.BitmapSetting;
import com.rt.printerlibrary.setting.CommonSetting;

import java.util.List;


public class Utils {

    public static void printInputLanguages(Context ctx) {
        boolean updateKeyboard = true;
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> ims = imm.getEnabledInputMethodList();

        for (InputMethodInfo method : ims) {
            List<InputMethodSubtype> submethods = imm.getEnabledInputMethodSubtypeList(method, true);
            for (InputMethodSubtype submethod : submethods) {
                if (submethod.getMode().equals("keyboard")) {
                    String currentLocale = submethod.getLocale();

                    if (!currentLocale.contains("zh_CN")) updateKeyboard = false;
                    Log.i("Login`", "Available input method locale: " + currentLocale);
                }
            }
        }
        if (updateKeyboard) {
            Toast.makeText(ctx, "יש לעדכן הגדרות שפה", Toast.LENGTH_LONG).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ctx, "יש לעדכן הגדרות שפה מקלדת וירטואלית מוגדרת לא תקין", Toast.LENGTH_LONG).show();

                }
            }, 5000);
        }
    }
    public static void hideSystemUI(Window window) {
        View decorView = window.getDecorView();/*.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );*/
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public static Cmd addBitmap(Cmd cmd, Bitmap mBitmap, int mBmpPrintWidth)  {

        final int bmpPrintWidth = (mBmpPrintWidth > 72 )?72:mBmpPrintWidth;

//        CmdFactory cmdFactory = new EscFactory();
//        Cmd cmd = cmdFactory.create();
        cmd.append(cmd.getHeaderCmd());

        CommonSetting commonSetting = new CommonSetting();
        commonSetting.setAlign(CommonEnum.ALIGN_MIDDLE);
        cmd.append(cmd.getCommonSettingCmd(commonSetting));

        BitmapSetting bitmapSetting = new BitmapSetting();

        bitmapSetting.setBmpPrintMode(BmpPrintMode.MODE_MULTI_COLOR);
//        bitmapSetting.setBimtapLimitWidth(bmpPrintWidth * 7);
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


        return cmd;
    }

    public static JsonObject dummyInvoiceData() {
        JsonObject res = new JsonObject();

        try {
            String s = "[\"             Yarden             \",\"25/11/2018                14:15\",\"     חשבונית מס קבלה - מקור     \",\"           004550021           \",\"ברקוד         תיאור        סה\\\"כ\",\"700000000     שוארמה         30\",\"700000000     נקניקיות       49\",\"2             שניצל          49\",\"        סכום ביניים:     128.00\",\"        התקבל במזומן:       128\",\"        חייב מע\\\"מ:       109.40\",\"        סה\\\"כ מע\\\"מ:        18.60\",\"        סה\\\"כ שולם:       128.00\",\"         מס' פריטים: 3         \",\"       מספר קופה: 638          \",\"         תודה ולהתראות         \"]";
            String barcode = "0000004550021";
            res.addProperty("invoice", s);
            res.addProperty("barcode", barcode);
        }
      catch (Exception ex)
      {

      }

        return res;

       /* try {
            print_invoice2(s, barcode, 0);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
    }

    public static void cutPaper(RTPrinter rtPrinter) {
//        AidlUtil.getInstance().cutPaper();
        CmdFactory cmdFactory = new EscFactory();
        Cmd cmd = cmdFactory.create();
        cmd.append(cmd.getAllCutCmd());
        rtPrinter.writeMsg(cmd.getAppendCmds());

    }

    public static void openDrawer(RTPrinter rtPrinter){
        CmdFactory cmdFactory = new EscFactory();
        Cmd cmd = cmdFactory.create();
        cmd.append(cmd.getOpenMoneyBoxCmd());
        rtPrinter.writeMsg(cmd.getAppendCmds());
    }

}
