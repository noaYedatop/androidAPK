package com.credix.pinpaddriverwithandroidusage;

import android.webkit.JavascriptInterface;

public class WebAppBridge {

    // אם אצלך journal_id מגיע כמספר לפעמים:
    @JavascriptInterface
    public void writelog(String journal_id, String desc) {
        //AppLogger.log("WEB | " + journal_id + " | " + desc);
    }
}

