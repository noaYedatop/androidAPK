package com.credix.pinpaddriverwithandroidusage;

import static android.content.Intent.getIntent;
import static androidx.core.app.ActivityCompat.recreate;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.view.Display;

import com.rt.printerlibrary.printer.RTPrinter;

import java.util.Locale;

/**
 * Created by Administrator on 2017/4/27.
 */
public class BaseApp extends Application {
    private boolean isAidl,is_i_machine;

    public static BaseApp instance = null;
    private RTPrinter rtPrinter;

    @BaseEnum.CmdType
    private int currentCmdType = BaseEnum.CMD_PIN;//默认为针打

    @BaseEnum.ConnectType
    private int currentConnectType = BaseEnum.NONE;//默认未连接

    public static Context getInstance() {
        return instance;
    }


    private MyPresentation mPresentation = null;

    public boolean isAidl() {
        return isAidl;
    }
    public boolean is_i_machine() {
        return is_i_machine;
    }
    public void setAidl(boolean aidl) {
        isAidl = aidl;
    }
    Display[] displays;
    @Override
    public void onCreate() {
        super.onCreate();
        isAidl = false;
        is_i_machine = true;
        instance = this;
        setLocale("he");
    }
    public void setLocale(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        Context context = getApplicationContext();
        context = context.createConfigurationContext(config);

        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
    }

    public void saveLanguageToPreferences(String languageCode) {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("language", languageCode);
        editor.apply();
    }

    // קריאה לשפה מ-SharedPreferences
    public String getLanguageFromPreferences() {
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        return preferences.getString("language", "he");  // ברירת מחדל: עברית
    }
//    public void setLocale(String languageCode) {
//        // הגדרת השפה החדשה
//        Locale locale = new Locale(languageCode);
//        Locale.setDefault(locale);
//
//        // עדכון ה-Configuration עם השפה החדשה
//        Configuration config = new Configuration();
//        config.setLocale(locale);
//
//        // עדכון ה-Context עם השפה החדשה
//        Context context = getApplicationContext();
//        context = context.createConfigurationContext(config);
//
//        // עדכון ה-Resources של האפליקציה עם השפה החדשה
//        getResources().updateConfiguration(config, getResources().getDisplayMetrics());
//    }

    public RTPrinter getRtPrinter() {
        return rtPrinter;
    }

    public void setRtPrinter(RTPrinter rtPrinter) {
        this.rtPrinter = rtPrinter;
    }

    @BaseEnum.CmdType
    public int getCurrentCmdType() {
        return currentCmdType;
    }

    public void setCurrentCmdType(@BaseEnum.CmdType int currentCmdType) {
        this.currentCmdType = currentCmdType;
    }

    @BaseEnum.ConnectType
    public int getCurrentConnectType() {
        return currentConnectType;
    }

    public void setCurrentConnectType(@BaseEnum.ConnectType int currentConnectType) {
        this.currentConnectType = currentConnectType;
    }
}
