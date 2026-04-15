package com.credix.pinpaddriverwithandroidusage;

import android.util.Log;

import com.neostra.serialport.SerialPort;
import com.semtom.weightlib.IReadWeightListener;
import com.semtom.weightlib.WeighUtils;

import java.io.IOException;

public class ScaleManager {

    private volatile boolean initialized = false;
    private volatile boolean firstReadingReceived = false;
    private static final String TAG = "SCALE_DEBUG";

    private static ScaleManager ScaleManager;
    private WeighUtils weighUtils;

    private volatile double weight = 0;
    private volatile long lastWeightTs = 0;
    private volatile long lastConsumedTs = 0;

    private ScaleListener scaleListener;
    private IReadWeightListener readWeightListener;

    private ScaleManager() {
        Log.i(TAG, "ScaleManager constructor START");

        try {
            readWeightListener = new IReadWeightListener() {
                @Override
                public void onReadingWeight(double d, int status, int tare) {
                    long now = System.currentTimeMillis();

                    Log.d(TAG, "onReadingWeight() CALLED");
                    Log.d(TAG, "Raw weight received = " + d +
                            " | status = " + status +
                            " | tare = " + tare +
                            " | ts = " + now);

                    weight = d;
                    lastWeightTs = now;
                    firstReadingReceived = true;

                    Log.d(TAG, "Weight updated -> " + weight + ", lastWeightTs=" + lastWeightTs);

                    if (scaleListener != null) {
                        scaleListener.onWeight(weight);
                    } else {
                        Log.w(TAG, "scaleListener is NULL - no listener notified");
                    }
                }

                @Override
                public void onError(String msg) {
                    Log.e(TAG, "Scale ERROR received from library: " + msg);
                }

                @Override
                public void initValue(String value) {
                    Log.d(TAG, "initValue() called with value = " + value);
                }
            };

            weighUtils = WeighUtils.builder();

            Log.i(TAG, "Opening weighUtils connection");
            weighUtils.open();

            Thread.sleep(300);

            Log.i(TAG, "Binding weight listener");
            weighUtils.bindWeightRead(readWeightListener);

            Thread.sleep(300);

            Log.i(TAG, "Setting mini screen language");
            //setMiniScreenLanguage();

            initialized = true;
            Log.i(TAG, "ScaleManager constructor FINISHED, initialized=true");

        } catch (Exception e) {
            Log.e(TAG, "Exception during ScaleManager init", e);
        }
    }
    public boolean isInitialized() {
        return initialized;
    }

    public boolean hasFirstReading() {
        return firstReadingReceived;
    }
    public static ScaleManager getInstance() {
        Log.d(TAG, "getInstance() called");

        if (ScaleManager == null) {
            Log.i(TAG, "Creating new ScaleManager instance");
            ScaleManager = new ScaleManager();
        } else {
            Log.d(TAG, "Returning existing ScaleManager instance");
        }

        return ScaleManager;
    }

    public boolean getStatus() {
        boolean status = weighUtils != null;
        Log.d(TAG, "getStatus() -> weighUtils != null = " + status);
        return status;
    }

    public double getLastWeight() {
        Log.d(TAG, "getLastWeight() -> " + weight);
        return weight;
    }

    public long getLastWeightTs() {
        Log.d(TAG, "getLastWeightTs() -> " + lastWeightTs);
        return lastWeightTs;
    }

    public long getLastConsumedTs() {
        Log.d(TAG, "getLastConsumedTs() -> " + lastConsumedTs);
        return lastConsumedTs;
    }

    public void setLastConsumedTs(long ts) {
        Log.d(TAG, "setLastConsumedTs() -> " + ts);
        lastConsumedTs = ts;
    }

    public void tara() {
        Log.i(TAG, "Tare() called");

        if (weighUtils != null) {
            Log.i(TAG, "Sending tare command to scale");
            weighUtils.resetBalance_qp();
        } else {
            Log.e(TAG, "Tare FAILED - weighUtils is NULL");
        }
    }

    public void zero() {
        Log.i(TAG, "Zero() called");

        if (weighUtils != null) {
            Log.i(TAG, "Sending zero command to scale");
            weighUtils.resetBalance();
        } else {
            Log.e(TAG, "Zero FAILED - weighUtils is NULL");
        }
    }

    public void setScaleListener(ScaleListener listener) {
        Log.i(TAG, "setScaleListener() called -> listener = " + listener);
        scaleListener = listener;
    }

    public ScaleListener getListener() {
        Log.d(TAG, "getListener() called");
        return this.scaleListener;
    }

    public void kill() {
        Log.w(TAG, "kill() called - closing scale connection");

        if (weighUtils != null) {
            Log.i(TAG, "Closing serial port");
            weighUtils.cwloseSerialPort();

            Log.i(TAG, "Unbinding weight listener");
            weighUtils.unbindWeightRead(readWeightListener);
        } else {
            Log.w(TAG, "kill() called but weighUtils already NULL");
        }

        weighUtils = null;
        scaleListener = null;
        ScaleManager = null;

        Log.i(TAG, "ScaleManager destroyed");
    }

    void setMiniScreenLanguage() {
        try {
            Log.i(TAG, "setMiniScreenLanguage() START");

            String path = weighUtils.getPath();
            int baud = weighUtils.getBaudrate();

            Log.d(TAG, "Serial path = " + path + " | baudrate = " + baud);

            SerialPort serialPort = new SerialPort(path, baud, 0);

            byte[] command = new byte[]{0x55, (byte) 0xAB, 0x31};

            Log.d(TAG, "Sending language command to scale");
            serialPort.getOutputStream().write(command, 0, 3);
            Log.d(TAG, "Command sent successfully");

            serialPort.close();
            Log.i(TAG, "Serial port closed");

        } catch (IOException e) {
            Log.e(TAG, "Error while setting mini screen language", e);
        }
    }
}