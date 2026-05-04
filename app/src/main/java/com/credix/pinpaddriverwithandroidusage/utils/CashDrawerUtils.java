//package com.credix.pinpaddriverwithandroidusage.utils;
//
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.hardware.usb.UsbDevice;
//import android.hardware.usb.UsbManager;
//import android.os.Build;
//import android.util.Log;
//
//import com.credix.pinpaddriverwithandroidusage.BaseActivity;
//import com.credix.pinpaddriverwithandroidusage.GPIOUtils;
//import com.credix.pinpaddriverwithandroidusage.Utils;
//import com.imin.library.IminSDKManager;
//import com.imin.printerlib.IminPrintUtils;
//import com.imin.printerlib.port.UsbPrinter;
//import com.rt.printerlibrary.printer.RTPrinter;
//
//import java.io.File;
//import java.util.HashMap;
//
//public class CashDrawerUtils {
//
//    public static void openDrawer(RTPrinter rtPrinter) {
//        openDrawer(null, rtPrinter, null, null, null);
//    }
//
//    public static void openDrawer(Context context, RTPrinter rtPrinter) {
//        openDrawer(context, rtPrinter, null, null, null);
//    }
//
//    public static void openDrawer(
//            Context context,
//            RTPrinter rtPrinter,
//            BaseActivity.PLATFORMS platform,
//            //Handler handler,
//            String fileName
//    ) {
//        // המימוש המלא
//
//        //Utils.openDrawer(rtPrinter); // adytech
//
//        //Log.i("DrawerDebug", "Trying to open drawer on platform: " + getPlatform());
//        Log.i("DrawerDebug", "Manufacturer: " + Build.MANUFACTURER + ", Device: " + Build.MODEL);
//        //IminSDKManager.opencashBox(this);
//        String manufacturer = Build.MANUFACTURER.toLowerCase();
//        String deviceName = Build.MODEL;
//        //IminSDKManager.opencashBox();
//
//       if (getPlatform() == BaseActivity.PLATFORMS.IMIN || (getPlatform() == BaseActivity.PLATFORMS.iPOS && deviceName.contains("rk3568_r"))){
//            if(manufacturer.contains("rockchip") && deviceName.contains("rk3288")){
//                Log.i("DrawerDebug", "Before sending open drawer command");
//                GPIOUtils.witchStatus((byte) 0x31, fileName);
//                Log.i("DrawerDebug", "Command sent");
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        GPIOUtils.witchStatus((byte) 0x30, fileName);
//                        Log.i("DrawerDebug", "Drawer close command sent after delay");
//                    }
//                }, 500);
//                isDrawerOpen = true;
//                File portFile = new File(fileName);
//                Log.i("DrawerDebug", "Port file exists: " + portFile.exists());
//
//            }
//            else{
//                UsbDevice usbDevice = null;//APOLO צריך את הבלוק הזה אז לשים הערה את התנאי הקודם שלא ייכנס לשם
//                UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
//                HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
//                for (UsbDevice device : deviceList.values()) {
//                    Log.d("USB", "Found device: " + device.getDeviceName());
//                    // פה אתה בודק אם זה המדפסת שלך
//                     usbDevice = device; // זה המדפסת
//
//                }
//                int piFlags = PendingIntent.FLAG_UPDATE_CURRENT;
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                    piFlags |= PendingIntent.FLAG_IMMUTABLE;
//                }
//
//                PendingIntent permissionIntent = PendingIntent.getBroadcast(
//                        context,
//                        0,
//                        new Intent("com.android.example.USB_PERMISSION"),
//                        piFlags
//                );
//                usbManager.requestPermission(usbDevice, permissionIntent);
//                UsbPrinter printer = new UsbPrinter(context);
//                boolean isOpen = printer.isOpen();
//                if (isOpen) {
//                    Log.d("USB", "Printer connection opened");
//                } else {
//                    Log.e("USB", "Failed to open printer connection");
//                }
//                byte[] openCashDrawer = new byte[]{
//                        0x1B, 0x70, 0x00, 0x19, (byte) 0xFA, 0x0A  // הוספתי \n בסוף
//                };
//                // שליחת פקודת ESC/POS דרך ה־SDK
//                IminPrintUtils.getInstance(this).sendRAWData(openCashDrawer);
//
//                //IminSDKManager.opencashBox();
//                IminSDKManager.opencashBox(this);
//                Log.d("CashBox", ">>> הפקודה נשלחה ל-SDK");
//            }
//            //CashDrawerOpener.openCashDrawer(getApplicationContext());
////        }else if (getPlatform() == PLATFORMS.SUNMI){
////            sunmi.aidlUtil.sendRawData(new byte [] {0x10, 0x14, 0x00, 0x00,0x00});
//        }
//        //else if (getPlatform() == PLATFORMS.iPOS || getPlatform() == PLATFORMS.UROVO){// בשביל המשולבת והטאבלט
//        else if (getPlatform() == BaseActivity.PLATFORMS.UROVO){
//
//        }
//        else if (getPlatform() == BaseActivity.PLATFORMS.iPOS){
//
//            if (manufacturer.contains("samsung") || manufacturer.contains("sprd")
//                || Objects.equals(deviceName, "N6") || deviceName == "N6" ) {
//
//            }
//            else {
//                Utils.openDrawer(rtPrinter); // Assuming Rockchip has a drawer
//            }
//        }
//        else{
//             Utils.openDrawer(rtPrinter);
//        }
//    }
//}
