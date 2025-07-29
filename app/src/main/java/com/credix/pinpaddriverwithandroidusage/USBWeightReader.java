package com.yedatop.util;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.*;
import android.util.Log;

import java.util.Arrays;
import java.util.HashMap;

public class USBWeightReader {

    private static final String TAG = "USBWeightReader";
    private static final String ACTION_USB_PERMISSION = "com.yedatop.USB_PERMISSION";

    private static final int TARGET_VENDOR_ID = 8401;
    private static final int TARGET_PRODUCT_ID = 28680;

    public static double readWeight(Context context) {
        Log.d(TAG, "Starting readWeight (no command mode)");

        UsbManager usbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        if (usbManager == null) {
            Log.e(TAG, "UsbManager is null");
            return -1;
        }

        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        for (UsbDevice device : deviceList.values()) {
            Log.d(TAG, "Found device: VID=" + device.getVendorId() + " PID=" + device.getProductId());

            if (device.getVendorId() == TARGET_VENDOR_ID && device.getProductId() == TARGET_PRODUCT_ID) {
                Log.d(TAG, "Target device matched");

                if (!usbManager.hasPermission(device)) {
                    Log.w(TAG, "No permission for device. Requesting...");
                    PendingIntent permissionIntent = PendingIntent.getBroadcast(
                            context, 0,
                            new Intent(ACTION_USB_PERMISSION),
                            PendingIntent.FLAG_IMMUTABLE);
                    usbManager.requestPermission(device, permissionIntent);
                    return -1;
                }

                UsbInterface usbInterface = device.getInterface(0);
                UsbEndpoint epIn = null;
                UsbEndpoint epOut = null;

                for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
                    UsbEndpoint ep = usbInterface.getEndpoint(i);
                    Log.d(TAG, "Endpoint[" + i + "]: direction=" + ep.getDirection() + ", type=" + ep.getType());

                    if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                        if (ep.getDirection() == UsbConstants.USB_DIR_IN) {
                            epIn = ep;
                        } else if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                            epOut = ep;
                        }
                    }
                }

                if (epIn == null) {
                    Log.e(TAG, "No IN endpoint found");
                    return -1;
                }

                UsbDeviceConnection connection = usbManager.openDevice(device);
                if (connection == null || !connection.claimInterface(usbInterface, true)) {
                    Log.e(TAG, "Failed to open connection or claim interface");
                    return -1;
                }

                // ✨ שליחת פקודת שקילה
                if (epOut != null) {
                    byte[] command = "SI\r\n".getBytes();
                    int writeResult = connection.bulkTransfer(epOut, command, command.length, 1000);
                    Log.d(TAG, "Write result: " + writeResult);
                } else {
                    Log.w(TAG, "No OUT endpoint found, skipping command send.");
                }

                byte[] buffer = new byte[64];
                Log.d(TAG, "Listening for auto-sent weight...");
                int readResult = connection.bulkTransfer(epIn, buffer, buffer.length, 5000);
                Log.d(TAG, "Read result: " + readResult);

                connection.releaseInterface(usbInterface);
                connection.close();

                if (readResult > 0) {
                    String raw = new String(buffer, 0, readResult);
                    Log.d(TAG, "Raw data: " + raw);
                    String cleaned = raw.replaceAll("[^0-9.\\-]", "");
                    Log.d(TAG, "Cleaned result: " + cleaned);

                    try {
                        double weight = Double.parseDouble(cleaned);
                        Log.i(TAG, "Weight parsed: " + weight);
                        return weight;
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Parse error: " + cleaned);
                    }
                } else {
                    Log.e(TAG, "No data received in passive mode");
                    Log.d(TAG, "Buffer contents: " + Arrays.toString(Arrays.copyOf(buffer, 10)));
                }
            }
        }

        Log.w(TAG, "Target device not found");
        return -1;
    }
}
