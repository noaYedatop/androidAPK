package com.credix.pinpaddriverwithandroidusage.WebInterface;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.credix.pinpaddriverwithandroidusage.BackOfficeActivity;
import com.credix.pinpaddriverwithandroidusage.BaseActivity;
import com.credix.pinpaddriverwithandroidusage.MainActivity;
import com.credix.pinpaddriverwithandroidusage.R;
import com.credix.pinpaddriverwithandroidusage.Utils;
import com.rt.printerlibrary.cmd.Cmd;
import com.rt.printerlibrary.cmd.EscFactory;
import com.rt.printerlibrary.factory.cmd.CmdFactory;

import org.json.JSONException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

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
            lp.setMargins(0,0,100,0);
            addedReceiptWebView.setLayoutParams(lp);
            frameLauout.addView(addedReceiptWebView,new FrameLayout.LayoutParams( FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            addedReceiptWebView.setInitialScale(getScale());


            WebSettings settings = addedReceiptWebView.getSettings();
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
    private void injectCSS() {
        try {

            if (addedReceiptWebView == null){
                ((Activity)context).runOnUiThread(loadWebView);
            }
           // addedReceiptWebView.lay
            addedReceiptWebView.loadUrl("javascript:(function() {" +
                    "document.getElementsByClassName('wrapper')[0].style.marginRight='auto';"+
                    "document.getElementsByClassName('wrapper')[0].style.paddingRight='15px';"+
                    "document.getElementsByTagName('frame')[0].style.marginRight='auto';"+
                    "document.getElementsByTagName('frame')[0].style.paddingRight='25px';"+
                    "document.head.insertAdjacentHTML('beforeend', `<style>body{font-size:17px}</style>`)"+
                    "document.head.insertAdjacentHTML('beforeend', `<style>td{font-size:17px}</style>`)"+
                    "document.head.insertAdjacentHTML('beforeend', `<style>b{font-size:17px}</style>`)"+

                    "var iframe = document.getElementsByTagName('frame')[0];"+
                    "   var style = document.createElement('style');"+
                    "    style.textContent = 'tr, td, div {font-size: 20px;}';"+
                    "    iframe.contentDocument.head.appendChild(style);"+

                    "})()");

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void print_invoice3(final String s) throws JSONException {
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


            addedReceiptWebView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            addedReceiptWebView.layout(0, 0, addedReceiptWebView.getMeasuredWidth(), addedReceiptWebView.getMeasuredHeight());
//            Bitmap bitmap;
//            if(((BaseActivity)context).getPlatform() != BaseActivity.PLATFORMS.UROVO){
//                 bitmap = Bitmap.createBitmap(addedReceiptWebView.getWidth(), addedReceiptWebView.getHeight()+150, Bitmap.Config.ARGB_8888);
//            }
//            else{
//                 bitmap = Bitmap.createBitmap(addedReceiptWebView.getWidth(), addedReceiptWebView.getHeight(), Bitmap.Config.ARGB_8888);
//            }
            Bitmap bitmap = Bitmap.createBitmap(addedReceiptWebView.getWidth(), addedReceiptWebView.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            int iHeight = bitmap.getHeight();
            //bitmap.setHeight(iHeight-600);
            canvas.drawBitmap(bitmap, 5, 0, paint);
            addedReceiptWebView.draw(canvas);

            if(((BaseActivity)context).getPlatform() == BaseActivity.PLATFORMS.IMIN){
                ((BaseActivity)context).mIminPrintUtils.printSingleBitmap(bitmap);
                ((BaseActivity)context).mIminPrintUtils.printAndFeedPaper(100);


                ((BaseActivity)context).mIminPrintUtils.release();

                ((BaseActivity)context).mIminPrintUtils.partialCut();
                ((BackOfficeActivity) context).progress.setVisibility(View.GONE);

                ((BaseActivity)context).clearPrinterResources();

            } else if ( ((BaseActivity)context).getPlatform() == BaseActivity.PLATFORMS.SUNMI) {
                ((BaseActivity)context).sunmi.sendImageToPrinter(bitmap);
                ((BaseActivity)context).clearPrinterResources();
            }
            else if ( ((BaseActivity)context).getPlatform() == BaseActivity.PLATFORMS.UROVO) {
                ((BaseActivity)context).mPrinterManager.drawBitmap(bitmap, 0, 0);
                ((BaseActivity)context).mPrinterManager.printPage(0);
                ((BaseActivity)context).mPrinterManager.clearPage();
                ((BaseActivity)context).clearPrinterResources();
                ((BackOfficeActivity) context).progress.setVisibility(View.GONE);
            }
            else{

                CmdFactory cmdFactory = new EscFactory();
                Cmd escCmd = cmdFactory.create();

                escCmd = Utils.addBitmap(escCmd, bitmap, bitmap.getWidth());


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
