package com.credix.pinpaddriverwithandroidusage;

import android.app.Application;
import android.content.Context;
import android.view.Display;

import com.rt.printerlibrary.printer.RTPrinter;
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
    }

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
