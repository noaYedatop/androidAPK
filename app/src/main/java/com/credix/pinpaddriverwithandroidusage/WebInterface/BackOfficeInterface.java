package com.credix.pinpaddriverwithandroidusage.WebInterface;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.credix.pinpaddriverwithandroidusage.BackOfficeActivity;
import com.credix.pinpaddriverwithandroidusage.BaseActivity;
import com.credix.pinpaddriverwithandroidusage.HtmlToBitmapConverter;
import com.credix.pinpaddriverwithandroidusage.MainActivity;
import com.credix.pinpaddriverwithandroidusage.NexgoDeviceController;
import com.credix.pinpaddriverwithandroidusage.R;
import com.credix.pinpaddriverwithandroidusage.Utils;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.factory.cmd.CmdFactory;

import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions;
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence;

import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BackOfficeInterface {
    static final String TAG = "BackOfficeInterface";
    private WebView addedReceiptWebView;
//    private final RTPrinter rtPrinter;
    private Context context;
    private android.os.Handler handler = new android.os.Handler();
    private WebView calling ;
    private  FrameLayout frameLauout;
    EditText emailET;
    public BackOfficeInterface(Context context,WebView _calling,/* RTPrinter _rtPrinter,*/ FrameLayout frameLauout) {
        this.context = context;
        calling = _calling;
        this.frameLauout = frameLauout;
//        receiptWebView = _receiptWebView;
//        rtPrinter = _rtPrinter;

    }

    AlertDialog alertDialog;


    OkHttpClient client = new OkHttpClient();

    HttpUrl.Builder urlBuilder; //= HttpUrl.parse(MainActivity.MAIN_PATH+"/modules/stock/email_report_setting.php").newBuilder();

    @JavascriptInterface
    public  void hideKeyboard(){
        Log.i("hideKeyboard","hideKeyboard");
    }

    @JavascriptInterface
    public void backoffice_sendInnerEmail(String email){
        if (alertDialog != null) return;
        if (email == null || email.isEmpty()){
            noEmailAlert();
        }else {

            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    calling.loadUrl("javascript:sendEmailReport(1);");

                }
            });        }

    }

    private void noEmailAlert() {
        LayoutInflater inflater = (LayoutInflater)
                ((Activity)context).getSystemService(LAYOUT_INFLATER_SERVICE);

        final View view = inflater.inflate(R.layout.set_email_dialog, null);
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("כתובת דואר אלקטרוני אינה מוגדרת ");
        alertDialog.setCancelable(false);
        alertDialog.setView(view);
        emailET = (EditText) view.findViewById(R.id.email);
        emailET.setEnabled(false);
        emailET.setVisibility(View.GONE);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", emailDialogueApprove);

    }

    @JavascriptInterface
    public void backoffice_sendEmail(String email){
        if (alertDialog != null) return;
        if (email == null || email.isEmpty()){
           this.setEmail();
        }else {
            this.approveEmail(email);
        }

    }

    private  DialogInterface.OnClickListener emailDialogueApprove = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            alertDialog = null;
            ((Activity) context).runOnUiThread(emailReportHandler);
        }
    };
    private void approveEmail(String email){
            LayoutInflater inflater = (LayoutInflater)
                    ((Activity)context).getSystemService(LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.set_email_dialog, null);
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("אשר שליחת דוא״ל אל: ");
            alertDialog.setCancelable(false);
            alertDialog.setView(view);
            emailET = (EditText) view.findViewById(R.id.email);
            emailET.setEnabled(false);
            emailET.setText(email);

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", emailDialogueApprove);


            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",dialogueCancel);
            alertDialog.show();

    }

    DialogInterface.OnClickListener dialogueCancel =  new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            alertDialog.dismiss();
            alertDialog = null;
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setEmail();
                }
            });

        }
    };

    private int getScale(){
        Display display = ((WindowManager) this.context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(1300);
        val = val * 100d;
        return val.intValue();
    }

    Runnable loadWebView = new Runnable() {
        @Override
        public void run() {
            loadWebView();
        }
    };
    private  void loadWebView(){

        try{
            addedReceiptWebView = new WebView(this.context);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
            lp.setMargins(0,0,0,0);
            addedReceiptWebView.setLayoutParams(lp);
            frameLauout.addView(addedReceiptWebView,new FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            addedReceiptWebView.setInitialScale(100);
            addedReceiptWebView.getSettings().setUseWideViewPort(true);
            addedReceiptWebView.getSettings().setLoadWithOverviewMode(false);
            addedReceiptWebView.getSettings().setTextZoom(100); // 100=רגיל, נסי 150-250


            WebSettings settings = addedReceiptWebView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setDomStorageEnabled(true);
            settings.setLoadWithOverviewMode(true);
            settings.setUseWideViewPort(true);

            settings.setDefaultTextEncodingName("utf-8");

            addedReceiptWebView.enableSlowWholeDocumentDraw();
            addedReceiptWebView.setHorizontalScrollBarEnabled(false);
            addedReceiptWebView.setVerticalScrollBarEnabled(false);

            addedReceiptWebView.setWebViewClient(wv_client);

        }catch (Exception e){
            e.printStackTrace();
        }

    }



    // Inject CSS method: read style.css from assets folder
// Append stylesheet to document head
//    private void injectCSS() {
//        try {
//
//            if (addedReceiptWebView == null){
//                ((Activity)context).runOnUiThread(loadWebView);
//            }
//           // addedReceiptWebView.lay
//            addedReceiptWebView.loadUrl("javascript:(function() {" +
//                    "document.getElementsByClassName('wrapper')[0].style.marginRight='auto';"+
//                    "document.getElementsByClassName('wrapper')[0].style.paddingRight='15px';"+
//                    "document.getElementsByTagName('frame')[0].style.marginRight='auto';"+
//                    "document.getElementsByTagName('frame')[0].style.paddingRight='25px';"+
//                    "document.head.insertAdjacentHTML('beforeend', `<style>body{font-size:17px}</style>`)"+
//                    "document.head.insertAdjacentHTML('beforeend', `<style>td{font-size:17px}</style>`)"+
//                    "document.head.insertAdjacentHTML('beforeend', `<style>b{font-size:17px}</style>`)"+
//
//                    "var iframe = document.getElementsByTagName('frame')[0];"+
//                    "   var style = document.createElement('style');"+
//                    "    style.textContent = 'tr, td, div {font-size: 20px;}';"+
//                    "    iframe.contentDocument.head.appendChild(style);"+
//
//                    "})()");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    private int getPrinterDotsWidth() {
        // 80mm:
        return 576;
        // 58mm:
        // return 384;
    }
    private void injectCSS() {
        if (addedReceiptWebView == null) return;

        ((Activity) context).runOnUiThread(() -> {
            int W = getPrinterDotsWidth(); // 576

            String js =
                    "javascript:(function(){" +
                            "  function apply(doc){" +
                            "    try{" +
                            "      var old = doc.querySelector('meta[name=viewport]'); if(old) old.remove();" +
                            "      var meta = doc.createElement('meta');" +
                            "      meta.name='viewport';" +
                            "      meta.content='width=" + W + ", initial-scale=1.0, maximum-scale=1.0, user-scalable=no';" +
                            "      doc.head.appendChild(meta);" +
                            "    }catch(e){}" +

                            "    var css = `" +
                            "      *{ box-sizing:border-box !important; }" +
                            "      html,body{ margin:0 !important; padding:0 !important; background:#fff !important; }" +
                            "      body{ direction:rtl !important; text-align:right !important; }" +

                            "      .wrapper,.container,.content,#content,main,section,article{" +
                            "        width:100% !important; margin:0 !important; padding:0 !important;" +
                            "      }" +

                            "      table{ width:100% !important; table-layout:auto" +
                            " !important; border-collapse:collapse !important; }" +

                            "      table, table * {" +
                            "        font-size: 50px !important;" +
                            "        line-height: 1.25 !important;" +
                            "      }" +

                            "      th, th * { font-weight:700 !important; }" +
                            "      .wrapper, .wrapper * { font-size:50px !important; }" +
                            "td, th {\n" +
                            "  white-space: nowrap !important;\n" +
                            "}"+
                            "      td, span, span * { font-size:50px !important; }" +
                            "      b,strong{ font-size:inherit !important; }" +
                            "    `;" +

                            "    var style = doc.getElementById('print_style_injected');" +
                            "    if(!style){ style = doc.createElement('style'); style.id='print_style_injected'; doc.head.appendChild(style); }" +
                            "    style.innerHTML = css;" +

                            // ✅ זה חייב להיות *בתוך* apply(doc) כדי ש-doc יהיה מוגדר
                            "    try{" +
                            "      var targets = doc.querySelectorAll('table, table *, .wrapper, .wrapper *, th, td, span, p, strong, b');" +
                            "      for (var i=0;i<targets.length;i++){" +
                            "        targets[i].style.setProperty('font-size','50px','important');" +
                            "        targets[i].style.setProperty('line-height','1.25','important');" +
                            "      }" +
                            "    }catch(e){}" +

                            "  }" +

                            "  apply(document);" +
                            "  var frames=document.querySelectorAll('iframe,frame');" +
                            "  for(var i=0;i<frames.length;i++){" +
                            "    try{ if(frames[i].contentDocument) apply(frames[i].contentDocument); }catch(e){}" +
                            "  }" +
                            "})();";


            String code = js.replace("javascript:", "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                addedReceiptWebView.evaluateJavascript(code, null);
            } else {
                addedReceiptWebView.loadUrl(js);
            }
        });
    }


    private void setEmail(){
        LayoutInflater inflater = (LayoutInflater)
                ((Activity)context).getSystemService(LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.set_email_dialog, null);
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("הכנס כתובת אימייל למשלוח");
        alertDialog.setCancelable(false);
        alertDialog.setView(view);

        emailET = (EditText) view.findViewById(R.id.email);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String _inputEmail = emailET.getText().toString();
                alertDialog = null;
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        oldUrl = calling.getUrl();
                        calling.loadUrl(MainActivity.MAIN_PATH+"/modules/stock/email_report_setting.php?email="+_inputEmail);
                        handler.postDelayed(urlLoading,150);
                        handler.postDelayed(emailReportHandler,400);

                    }
                });
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", dialogueOnClick);
        alertDialog.show();
    }
    private  String oldUrl;

    private final  Runnable emailReportHandler = new Runnable() {
        @Override
        public void run() {
            calling.loadUrl("javascript:sendEmailReport(1);");
        }
    };
    private final Runnable urlLoading = new Runnable() {
        @Override
        public void run() {
            calling.stopLoading();
            calling.loadUrl(oldUrl);
        }
    };

    DialogInterface.OnClickListener dialogueOnClick = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    };
    @JavascriptInterface
    public void goBack(){
        ((Activity)context).onBackPressed();
    }

    @JavascriptInterface
    public void print_back_office(int id, String url)throws JSONException{
//        url="";
        print(url);
    }
    private MqttClient mqttClient;
    private MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void disconnected(MqttDisconnectResponse disconnectResponse) {
            String msgstr = "disconnected " + disconnectResponse.toString();
            System.out.println(msgstr);
        }

        @Override
        public void mqttErrorOccurred(MqttException exception) {
            String msgstr = "mqttErrorOccurred " + exception.toString();
            System.out.println("from CALLBACK: " + msgstr);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            //The original data is stored in the file, the amount of data is too large
            String msgstr = topic + "\n" + message.toString() + "\n";
            System.out.println("Message published");
            //  handleRecvMessage(message.toString());
        }

        @Override
        public void deliveryComplete(IMqttToken token) {
            String msgstr = "deliveryComplete " + token.toString();
            System.out.println(msgstr);
        }

        @Override
        public void connectComplete(boolean reconnect, String serverURI) {
            String msgstr = "connectComplete " + serverURI;
            System.out.println(msgstr);
        }

        @Override
        public void authPacketArrived(int reasonCode, MqttProperties properties) {
            String msgstr = "authPacketArrived " + properties;
            System.out.println(msgstr);
        }
    };
    @JavascriptInterface
    public void print_invoice_bon(String s, final String net, String server, String userName, String password) throws JSONException {
        try{
            // initializePrinter();
            String serverURI = server;
            MemoryPersistence persistence = new MemoryPersistence();
            mqttClient = new MqttClient(serverURI, net,persistence);
            MqttConnectionOptions connOpts = new MqttConnectionOptions();
            connOpts.setCleanStart(true);
            connOpts.setSessionExpiryInterval(0xffffffffl);
            connOpts.setUserName(userName);
            connOpts.setPassword(password.getBytes(StandardCharsets.UTF_8));
            mqttClient.setCallback(mqttCallback);
            mqttClient.connect(connOpts);
            byte[] init = new byte[] {0x1B, 0x40};
            String charsetName = "CP862";
            s = s.replace("\"","");
            s= s.replace("[","");
            s= s.replace("]","");
            String[] lines = s.split(",");
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            outputStream.write(0x0A);outputStream.write(0x0A);outputStream.write(0x0A);outputStream.write(0x0A);outputStream.write(0x0A); outputStream.write(0x0A);outputStream.write(0x0A);
            int x =0;
            for (String line : lines) {
                byte[] text = line.getBytes(charsetName);
                int paddingLength = (40 - text.length) / 2; // Assuming 48 characters is the width of your printer
                outputStream.write(init);
                outputStream.write(text);
                if(x!=4) {
                    for (int i = 0; i < paddingLength; i++) {
                        outputStream.write(0x20); // Write spaces for padding
                    }
                }if(x==4) {
                    for (int i = 0; i < 20; i++) {
                        outputStream.write(0x20); // Write spaces for padding
                    }
                }
                outputStream.write(0x0A); // Line feed after each lin
                x++;
            }
            outputStream.write(0x0A);outputStream.write(0x0A); outputStream.write(0x0A);outputStream.write(0x0A);outputStream.write(0x0A); outputStream.write(0x0A);outputStream.write(0x0A);
            byte[] printData = outputStream.toByteArray();
            String topic = net; // Replace with the topic the printer is subscribed to
            // String message = "Hello, printer123456!";
            mqttClient.publish(topic, printData, 1, true);

            mqttClient.disconnect();
        } catch (Throwable tr) {
            tr.printStackTrace();
        }

    }


    /**
     *
     * Print HTML Invoice
     * @param s
     * @param barcode
     * @param seconds
     * @throws JSONException
     */
    int running = 0;

    @JavascriptInterface
    public void pairPinpadSynqpay(String ip, String serial) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"jsonrpc\": \"2.0\",\n    \"method\": \"pair\",\n    \"id\": \"23fda5af-2c2e-4467-9a46-73b79e0b6bee\",\n    \"params\": {\n        \"serialNumber\": \""+serial+"\"\n    }\n}");
        Request request = new Request.Builder()
                .url("http://"+ip+":8000/synqpay")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("apiKey", "")
                .build();
        Response response = client.newCall(request).execute();
        Log.i("COOOOD", response.toString());
    }


    @JavascriptInterface
    public String authPinpadSynqpay(String ip, String code) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"jsonrpc\": \"2.0\",\n    \"method\": \"authenticate\",\n    \"id\": \"1234\",\n    \"params\": {\n        \"otp\": \""+code+"\"\n    }\n}");
        Request request = new Request.Builder()
                .url("http://"+ip+":8000/synqpay")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("apiKey", "")
                .build();
        Response response = client.newCall(request).execute();
        JSONObject result = new JSONObject(response.body().string());
        result = result.optJSONObject("result");
        String apiKey = result.optString("apiKey");
        Log.i("APIIIIII ", apiKey);
        return apiKey;
    }
        @JavascriptInterface
    public void  print_invoice3(final String s) throws JSONException {
        print(MainActivity.MAIN_PATH+s);

        //     print_i_machine(s,barcode,seconds);

    }

    @JavascriptInterface
    public void print_z(final String s, String z) throws JSONException {
        if (z == null || z == "0"){
            print(MainActivity.MAIN_PATH+s);
        }else
            print(MainActivity.MAIN_PATH+s,z);

        //     print_i_machine(s,barcode,seconds);

    }
    private void print(String path){
        print( path,null);
    }
    private String z, path;
    private void print(String path, String z){
        running = 0;
        this.z =  z;
        this.path = path;

        if (addedReceiptWebView == null)
        {
            ((Activity)context).runOnUiThread(loadWebView);

        }else {
            addedReceiptWebView.setHorizontalScrollBarEnabled(false);
            addedReceiptWebView.setVerticalScrollBarEnabled(false);
        }
        ((Activity)context).runOnUiThread(printRunnable);
    }

    private final WebViewClient wv_client  = new WebViewClient(){
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            running = Math.max(running, 1);
        }



        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String urlNewString) {
            running++;
            webView.loadUrl(urlNewString);
            return true;
        }


        @Override
        public void onPageFinished(WebView view, String url) {

            super.onPageFinished(view, url);


            injectCSS();
//            addedReceiptWebView.zoomBy(1.05f);


            if (--running == 0 && view.getProgress() == 100) {

                handler.postDelayed(printer,500);

            }
        }
    };

    Runnable printer = new Runnable() {
        @Override
        public void run() {

            int W = getPrinterDotsWidth(); // 576
            int wSpec = View.MeasureSpec.makeMeasureSpec(W, View.MeasureSpec.EXACTLY);
            int hSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

            addedReceiptWebView.measure(wSpec, hSpec);
            addedReceiptWebView.layout(0, 0, W, addedReceiptWebView.getMeasuredHeight());

            Bitmap bitmap = Bitmap.createBitmap(W, addedReceiptWebView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            addedReceiptWebView.draw(canvas);






//            addedReceiptWebView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
//                                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

//            addedReceiptWebView.layout(0, 0, addedReceiptWebView.getMeasuredWidth(), addedReceiptWebView.getMeasuredHeight());

//            Bitmap bitmap;
//            if(((BaseActivity)context).getPlatform() != BaseActivity.PLATFORMS.UROVO){
//                 bitmap = Bitmap.createBitmap(addedReceiptWebView.getWidth(), addedReceiptWebView.getHeight()+150, Bitmap.Config.ARGB_8888);
//            }
//            else{
//                 bitmap = Bitmap.createBitmap(addedReceiptWebView.getWidth(), addedReceiptWebView.getHeight(), Bitmap.Config.ARGB_8888);
//            }
//            Bitmap bitmap = Bitmap.createBitmap(addedReceiptWebView.getWidth(), addedReceiptWebView.getHeight(), Bitmap.Config.ARGB_8888);

//            Canvas canvas = new Canvas(bitmap);
//            Paint paint = new Paint();
//            int iHeight = bitmap.getHeight();
//            //bitmap.setHeight(iHeight-600);
//            canvas.drawBitmap(bitmap, 5, 0, paint);
//            addedReceiptWebView.draw(canvas);

            if(((BaseActivity)context).getPlatform() == BaseActivity.PLATFORMS.IMIN){
                ((BaseActivity)context).mIminPrintUtils.printSingleBitmap(bitmap);
                ((BaseActivity)context).mIminPrintUtils.printAndFeedPaper(100);


                ((BaseActivity)context).mIminPrintUtils.release();

                ((BaseActivity)context).mIminPrintUtils.partialCut();
                ((BackOfficeActivity) context).progress.setVisibility(View.GONE);

                ((BaseActivity)context).clearPrinterResources();

//            } else if ( ((BaseActivity)context).getPlatform() == BaseActivity.PLATFORMS.SUNMI) {
//                ((BaseActivity)context).sunmi.sendImageToPrinter(bitmap);
//                ((BaseActivity)context).clearPrinterResources();
            }
            else if ( ((BaseActivity)context).getPlatform() == BaseActivity.PLATFORMS.UROVO) {
                ((BaseActivity)context).mPrinterManager.drawBitmap(bitmap, 0, 0);
                ((BaseActivity)context).mPrinterManager.printPage(0);
                ((BaseActivity)context).mPrinterManager.clearPage();
                ((BaseActivity)context).clearPrinterResources();
                ((BackOfficeActivity) context).progress.setVisibility(View.GONE);
            }
            else if(((BaseActivity)context).getPlatform() == BaseActivity.PLATFORMS.iPOS) {
                String manufacturer = Build.MANUFACTURER.toLowerCase();
                if (manufacturer.contains("sprd")) {
                    if (bitmap != null) {
                        Log.d("Bitmap", "Bitmap מוכן להדפסה!");
                        String deviceName = Build.MODEL;
                        if (!deviceName.equals("N6")) {  // שים לב להשוואה נכונה
                            Log.d("PRINT", "נשלח להדפסה!");
                            NexgoDeviceController nexgoDeviceController = new NexgoDeviceController(context);
                            nexgoDeviceController.makeBitmapPrint(bitmap);
                            new android.os.Handler().postDelayed(() -> {
                                ((BackOfficeActivity) context).progress.setVisibility(View.GONE);
                            }, 4000); // 4000 = 4 שניות (אפשר גם 3000 או מה שמתאים בפועל)

                        }
                    } else {
                        Log.e("Bitmap", "ה-Bitmap שהועבר היה null");
                    }
                }
                else{
                    CmdFactory cmdFactory = new EscFactory();
                    Cmd escCmd = cmdFactory.create();

                    int Wi = getPrinterDotsWidth();
                    escCmd = Utils.addBitmap(escCmd, bitmap, Wi);


                    escCmd.append(escCmd.getHeaderCmd());

                    BaseActivity.rtPrinter.writeMsg(escCmd.getAppendCmds());
                    Utils.cutPaper( BaseActivity.rtPrinter);
                    ((BackOfficeActivity) context).progress.setVisibility(View.GONE);

                    ((BaseActivity)context).clearPrinterResources();
                }
            }

            else{

                CmdFactory cmdFactory = new EscFactory();
                Cmd escCmd = cmdFactory.create();

                int Wi = getPrinterDotsWidth();
                escCmd = Utils.addBitmap(escCmd, bitmap, Wi);


                escCmd.append(escCmd.getHeaderCmd());

                BaseActivity.rtPrinter.writeMsg(escCmd.getAppendCmds());
                Utils.cutPaper( BaseActivity.rtPrinter);
                ((BackOfficeActivity) context).progress.setVisibility(View.GONE);

                ((BaseActivity)context).clearPrinterResources();

            }


        }
    };
    private final Runnable printRunnable = new Runnable() {
        @Override
        public void run() {
            ((BackOfficeActivity)context).progress.setVisibility(View.VISIBLE);

            if (addedReceiptWebView == null){
                ((Activity)context).runOnUiThread(loadWebView);
            }


            if (z == null || z == "0") {
                addedReceiptWebView.loadUrl(path);
            }else{
                addedReceiptWebView.postUrl(path,("zedmode=1&journum="+z).getBytes());
                //webView.postUrl("https://office1.yedatop.com/modules/stock/rep_tazmech_print.php?&simple=1&sDate=01/02/2021&eDate=18/02/2021",("zedmode=1&journum=104").getBytes());


            }

        }
    };
}
