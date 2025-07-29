package com.credix.pinpaddriverwithandroidusage;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

public class CustomLineHeightSpan extends ReplacementSpan {
    private final int extraHeight;

    public CustomLineHeightSpan(int extraHeight) {
        this.extraHeight = extraHeight;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        if (fm != null) {
            fm.ascent -= extraHeight;
            fm.top -= extraHeight;
        }
        return 0;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        // לא מציירים כלום כי המטרה היא רק להוסיף רווח
    }
}
