package com.credix.pinpaddriverwithandroidusage;

import android.app.Presentation;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BackPanel extends Presentation {

    WebView mv;
    RelativeLayout layout;
//    Product_List_Adapter adapter;
    ListView listView;
    TextView SumDisplay;
    TextView WeightDisplay;
    RelativeLayout RL;
    RelativeLayout LastView = null;
    TableLayout table;
    final DecimalFormat decimalF = new DecimalFormat("0.000");
    final DecimalFormat priceF = new DecimalFormat("0.00");

    public BackPanel(Context outerContext, Display display, WebView mv, RelativeLayout layout) {
        super(outerContext, display);
        this.mv = mv;
        this.layout = layout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout);
        Log.e("BOOT", "setContentView = layout " + layout);  // או activity_main_login בהתאם
//        SumDisplay = layout.findViewById(R.id.SumDisplay);
//
//        if(Utils.isImin() && !SystemDataHandler.isBackPanelPictureMode())
//            layout.findViewById(R.id.HideDisplay).setVisibility(View.INVISIBLE);
//
//        if(Utils.isImin() && SystemDataHandler.isBackPanelPictureMode()) {
//            TextView display = layout.findViewById(R.id.KiloDisplayPanel);
//            if(display != null) {
//                display.setVisibility(View.VISIBLE);
//                WeightDisplay = display;
//            }
//        }
//        else {
//            TextView display = layout.findViewById(R.id.KiloDisplayPanel);
//            if(display != null)
//                display .setVisibility(View.GONE);
//            WeightDisplay = layout.findViewById(R.id.KiloDisplay);
//        }
//        table = layout.findViewById(R.id.ProductsTable);
//
//        listView = layout.findViewById(R.id.List);
//
//        adapter = new Product_List_Adapter(mv, R.layout.product_huge_view, mv.bill.Products, true);
//
//        listView.setAdapter(adapter);
//
//        adapter.setNotifyOnChange(true);

        HandleBackImagesShow();

    }

    public void UpdateListAdapter() {
//        adapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(0);
    }

    public void SetAdapter() {
//        adapter = new Product_List_Adapter(mv, R.layout.product_huge_view, mv.bill.Products, true);
//        listView.setAdapter(adapter);
    }

    public void SetSumDisplay(String str) {
        SumDisplay.setText(str);
    }

    public void SetWeightDisplay(String str){
        if(WeightDisplay != null) {
//            Utils.runOnUI(mv, new Listener() {
//                @Override
//                public void OnCall(Object data) {
//                    WeightDisplay.setText(str);
//                }
//            });
        }
    }

//    public void UpdateProductsList(ArrayList<Product> products){
//        if(table != null)
//            table.removeAllViews();
//        int counter = 0;
//        boolean IMin = Utils.isImin() && !SystemDataHandler.isBackPanelPictureMode();
//
//        if(!IMin){
//            WeightDisplay.setVisibility(View.GONE);
//        }
//        else {
//            WeightDisplay.setVisibility(View.VISIBLE);
//        }
//
//        for(Product product: products){
//            RelativeLayout layout = (RelativeLayout) mv.getLayoutInflater().inflate(R.layout.activity_back_panel_row_min, null);
//
//            final TableRow row = layout.findViewById(R.id.row);
//            TableRow Seperator = layout.findViewById(R.id.Seperator);
//            layout.removeAllViews();
//
//            TextView AmountDisplay = row.findViewById(R.id.AmountCol);
//            TextView NameDisplay = row.findViewById(R.id.NameCol);
//            TextView PricePerKiloDisplay = row.findViewById(R.id.PricePerWeightCol);
//            TextView WeightDisplay = row.findViewById(R.id.WeightCol);
//            TextView SumCol = row.findViewById(R.id.SumCol);
//
//
//            if(product.Multy)
//                AmountDisplay.setText(String.valueOf(product.Multiply));
//            else
//                AmountDisplay.setText("1");
//            if(product.Name != null)
//                NameDisplay.setText(product.Name);
//            else
//                WeightDisplay.setText("-");
//            PricePerKiloDisplay.setText(priceF.format(product.Price));
//
//            if(!Utils.isImin() && !SystemDataHandler.IsScaleConnected()) {
//                WeightDisplay.setVisibility(View.GONE);
//            }
//            else {
//                if (product.ByWeight)
//                    WeightDisplay.setText(decimalF.format(product.CurrWeight));
//                else
//                    WeightDisplay.setText("-");
//            }
//            SumCol.setText(String.valueOf(priceF.format(product.CalcPrice())));
//
//            if(counter % 2 != 0)
//                row.setBackgroundResource(R.color.Grey);
//            else
//                row.setBackgroundResource(R.color.White);
//            table.addView(Seperator);
//            table.addView(row);
//            counter++;
//        }
//    }

    public void ClearProductsList(){
        if(table != null)
            table.removeAllViews();
    }

    public void ShowGoodByeView() {

//        mv.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                View GoodBytView = mv.getLayoutInflater().inflate(R.layout.show_message_view, null);
//                RelativeLayout Main = GoodBytView.findViewById(R.id.Main);
//
//                RL = GoodBytView.findViewById(R.id.RL);
//
//                Main.removeAllViews();
//
//                ((TextView) RL.findViewById(R.id.Title)).setText(SystemDataHandler.GetCustomGoodByeMsg(mv));
//
//                layout.addView(RL);
//
//                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) RL.getLayoutParams();
//
//                lp.addRule(RelativeLayout.CENTER_IN_PARENT);
//
//                RL.setLayoutParams(lp);
//            }
//        });

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        mv.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//                layout.removeView(RL);
//
//            }
//        });

    }

    int ImageIndex = 0;

    void HandleBackImagesShow() {
//        final ArrayList<Bitmap> images = SystemDataHandler.GetBackPanelImages(mv);
//        if (images.size() <= 0)
//            return;

//        final ImageView image_Display = layout.findViewById(R.id.ImageShowView);
//
//
//        if(image_Display != null) {
//            image_Display.setVisibility(View.VISIBLE);
//
//
//            final int duration = SystemDataHandler.GetBackPanelImageShowDuration(mv);
//
//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    while (true) {
//                        mv.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                image_Display.setImageBitmap(images.get(ImageIndex % images.size()));
//                                ImageIndex++;
//                            }
//                        });
//
//                        try {
//                            Thread.sleep(duration * 1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            });
//
//            thread.start();
//        }
    }



    public void ShowView(RelativeLayout RL) {
        LastView = RL;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) LastView.getLayoutParams();
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        LastView.setLayoutParams(lp);
        layout.addView(LastView);
    }

    public void RemoveView() {
        if (LastView != null)
            layout.removeView(LastView);
        LastView = null;
    }

    public static final class AppLogger {

        public enum Level { DEBUG, INFO, WARN, ERROR }

        private static final String TAG = "AppLogger";
        private static final String FILE_NAME = "app_logs.txt";
        private static final int MAX_BUFFER_LINES = 2000;

        private static final ArrayDeque<String> memoryBuffer = new ArrayDeque<>();
        private static volatile Context appContext;

        private AppLogger() {}

        public static void init(Context context) {
            appContext = context.getApplicationContext();
            log(Level.INFO, "Logger initialized", null, null);
        }

        public static void d(String message) { log(Level.DEBUG, message, null, null); }
        public static void i(String message) { log(Level.INFO, message, null, null); }
        public static void w(String message) { log(Level.WARN, message, null, null); }
        public static void e(String message, Throwable t) { log(Level.ERROR, message, null, t); }

        public static void log(Level level, String message, Object data, Throwable throwable) {
            String line = buildLine(level, message, data, throwable);

            // 1) Logcat
            switch (level) {
                case DEBUG: Log.d(TAG, line); break;
                case INFO:  Log.i(TAG, line); break;
                case WARN:  Log.w(TAG, line); break;
                case ERROR: Log.e(TAG, line); break;
            }

            // 2) Memory buffer (for the viewer screen)
            synchronized (memoryBuffer) {
                memoryBuffer.addLast(line);
                while (memoryBuffer.size() > MAX_BUFFER_LINES) {
                    memoryBuffer.removeFirst();
                }
            }

            // 3) Persist to file
            Context ctx = appContext;
            if (ctx == null) return;

            File f = new File(ctx.getFilesDir(), FILE_NAME);
            FileWriter fw = null;
            try {
                fw = new FileWriter(f, true);
                fw.append(line).append('\n');
            } catch (Exception e) {
                Log.e(TAG, "Failed to write log file", e);
            } finally {
                if (fw != null) {
                    try { fw.close(); } catch (IOException ignored) {}
                }
            }
        }

        public static List<String> getLogsSnapshot() {
            synchronized (memoryBuffer) {
                return new ArrayList<>(memoryBuffer);
            }
        }

        public static void clearLogs() {
            synchronized (memoryBuffer) {
                memoryBuffer.clear();
            }
            Context ctx = appContext;
            if (ctx == null) return;
            File f = new File(ctx.getFilesDir(), FILE_NAME);
            FileWriter fw = null;
            try {
                fw = new FileWriter(f, false);
                fw.write("");
            } catch (Exception e) {
                Log.e(TAG, "Failed to clear log file", e);
            } finally {
                if (fw != null) {
                    try { fw.close(); } catch (IOException ignored) {}
                }
            }
        }

        public static File getLogFile(Context context) {
            return new File(context.getFilesDir(), FILE_NAME);
        }

        private static String buildLine(Level level, String message, Object data, Throwable throwable) {
            String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).format(new Date());
            String dataPart = (data != null) ? (" | data=" + String.valueOf(data)) : "";
            String errPart = (throwable != null) ? (" | ex=" + throwable.getClass().getSimpleName() + ": " + throwable.getMessage()) : "";
            return ts + " [" + level.name() + "] " + message + dataPart + errPart;
        }
    }
}
