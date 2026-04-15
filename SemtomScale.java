package com.cashregister.shlic.cashregister.Scale;

import android.util.Log;

import com.neostra.serialport.SerialPort;
import com.semtom.weightlib.IReadWeightListener;
import com.semtom.weightlib.WeighUtils;

import java.io.IOException;

public class SemtomScale {
    private final String TAG = SemtomScale.class.getSimpleName();
    private final int STABLE_THRESHOLD = 5;
    private static SemtomScale semtomScale;
    private WeighUtils weighUtils;
    private volatile double weight = 0;
    private volatile boolean isStable = false;
    private ScaleListener scaleListener;
    private IReadWeightListener readWeightListener;
    private double lastWeight;
    private int stableCount = 0;

    private SemtomScale() {
        try {

            readWeightListener = new IReadWeightListener() {
                @Override
                public void onReadingWeight(double d, int status, int tare) {
                    weight = d;
                    if (scaleListener != null) {
                        scaleListener.onWeight(weight);
                    }
                }

                @Override
                public void onError(String msg) {
                    Log.e(TAG, "Error: " + msg);
                }

                @Override
                public void initValue(String value) {

                }
            };

            weighUtils = WeighUtils.builder();
            setMiniScreenLanguage();
            weighUtils.open();
            weighUtils.bindWeightRead(readWeightListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SemtomScale getInstance() {
        if (semtomScale == null) {
            semtomScale = new SemtomScale();
        }
        return semtomScale;
    }

    public boolean getStatus() {
        return weighUtils != null;
    }

    public double getWeight() {
        if (weight == lastWeight) {
            if (stableCount < 1000) {
                stableCount++;
            }
        } else {
            stableCount = 0;
            lastWeight = weight;
        }

        isStable = stableCount >= STABLE_THRESHOLD;
        if (isStable) {
        return weight;
        }
        return -1;
    }

    public void tara() {
        Log.i(TAG, "Tare()");
        if (weighUtils != null) {
            weighUtils.resetBalance_qp();
        }
    }

    public void zero() {
        Log.i(TAG, "Zero()");
        if (weighUtils != null) {
            weighUtils.resetBalance();
        }
    }

    public void setScaleListener(ScaleListener listener) {
        scaleListener = listener;
    }

    public ScaleListener getListener() {
        return this.scaleListener;
    }

    public void kill() {
        Log.i(TAG, "Kill()");
        weighUtils.cwloseSerialPort();
        weighUtils.unbindWeightRead(readWeightListener);
        weighUtils = null;
        scaleListener = null;
        semtomScale = null;
    }

    void setMiniScreenLanguage(){
        try {
            Log.wtf(TAG, "Setting mini screen language to English");
            SerialPort serialPort = new SerialPort(weighUtils.getPath(), weighUtils.getBaudrate(), 0);
            serialPort.getOutputStream().write(new byte[] {0x55, (byte)0xAB, 0x31}, 0, 3);
            serialPort.close();
        } catch (IOException e) {
            Log.e(TAG, "Error: ", e);
        }
    }
}
