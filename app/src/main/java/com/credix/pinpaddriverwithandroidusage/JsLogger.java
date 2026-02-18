package com.credix.pinpaddriverwithandroidusage;
import android.content.Context;
import android.webkit.JavascriptInterface;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class JsLogger {

    private final Context context;
    private final SimpleDateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat timeFormat =
            new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    public JsLogger(Context context) {
        this.context = context;
    }

    @JavascriptInterface
    public void writelog(String tag, String message) {
        try {
            File logsDir = new File(context.getFilesDir(), "logs");
            if (!logsDir.exists()) {
                logsDir.mkdirs();
            }

            String today = dateFormat.format(new Date());
            File logFile = new File(logsDir, "log_" + today + ".txt");

            String time = timeFormat.format(new Date());
            String line = "[" + time + "][" + tag + "] " + message + "\n";

            FileWriter writer = new FileWriter(logFile, true);
            writer.append(line);
            writer.close();

        } catch (Exception ignored) {
            // בכוונה לא זורקים שגיאה – לוג לא אמור להפיל אפליקציה
        }
    }
}
