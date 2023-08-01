package com.credix.pinpaddriverwithandroidusage.wv;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;

public class WV extends WebView {
    private String TAG = "WebView";

    public WV(Context context) {
        super(context);
    }

    public WV(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WV(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WV(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onCheckIsTextEditor() {
        Log.d(TAG, "onCheckIsTextEditor");
        InputMethodManager imm = (InputMethodManager) getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindowToken(),InputMethodManager.HIDE_IMPLICIT_ONLY);
        return false;
    }


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new BaseInputConnection(this, false); //this is needed for #dispatchKeyEvent() to be notified.
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean isDispached = super.dispatchKeyEvent(event);

        if(event.getAction() == KeyEvent.ACTION_UP){
            Log.d("anton","dispatchKeyEvent="+event.getKeyCode());
            switch (event.getKeyCode())
            {
                case KeyEvent.KEYCODE_ENTER:
                    InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
                    break;
            }
        }

        return isDispached;

    }
}
