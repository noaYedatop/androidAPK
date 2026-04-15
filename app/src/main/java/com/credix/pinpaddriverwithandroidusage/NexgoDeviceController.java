package com.credix.pinpaddriverwithandroidusage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;

import com.example.nexgolibrary.R;
import com.example.nexgolibrary.data.DeviceController;
import com.example.nexgolibrary.domain.models.transaction.TransactionReceipt;
import com.example.nexgolibrary.utils.GeneralRetCode;
import com.example.nexgolibrary.utils.device.DeviceBeepLevel;
import com.example.nexgolibrary.utils.device.DeviceLedColor;
import com.example.nexgolibrary.utils.transaction.TransactionReceiptType;
import com.nexgo.oaf.apiv3.APIProxy;
import com.nexgo.oaf.apiv3.DeviceEngine;
import com.nexgo.oaf.apiv3.SdkResult;
import com.nexgo.oaf.apiv3.device.printer.AlignEnum;
import com.nexgo.oaf.apiv3.device.printer.GrayLevelEnum;
import com.nexgo.oaf.apiv3.device.printer.Printer;
import com.nexgo.oaf.apiv3.device.usbserial.UsbSerial;

import java.util.concurrent.Executors;

public class NexgoDeviceController implements DeviceController {
    private DeviceEngine deviceEngine;
    private Printer printer;
    private Context appContext;

    public NexgoDeviceController(Context appContext) {
        this.appContext = appContext;
        this.deviceEngine = APIProxy.getDeviceEngine(appContext);
        this.printer = (deviceEngine != null) ? deviceEngine.getPrinter() : null;
        if (deviceEngine == null) {
            Log.e("Printer", "DeviceEngine is null. Cannot initialize printer.");
        }
        int printerStatus = printer.getStatus();
        Log.d("Printer", "Printer Status: " + printerStatus);
        if (printerStatus != SdkResult.Success) {
            Log.e("Printer", "Printer is not ready, status: " + printerStatus);
        }
    }

    @Override
    public GeneralRetCode makePrint(TransactionReceipt transactionReceipt, String timeBetweenPrint, TransactionReceiptType printType) {
        if (printer != null) {
            printer.initPrinter();
        }
        assert printer != null;
        printer.appendPrnStr("ברוכים הבאים!! ", 22, AlignEnum.RIGHT, false);
        printer.startPrint(true, result -> Log.d("Printer", "Print result: " + result));

        Typeface hebrewTypeface = ResourcesCompat.getFont(appContext, R.font.inter_thin);
        Typeface defaultTypeface = Typeface.DEFAULT_BOLD;

        if (printer != null) {
            printer.setTypeface(hebrewTypeface);
            printer.setTypeface(defaultTypeface);
        }

        Executors.newSingleThreadExecutor().execute(() -> {
            switch (printType) {
                case COSTUMER:
                case SELLER:
                    //printReceipt(receiptData, printType);
                    break;
                case COSTUMER_SELLER:
                    //printReceipt(receiptData, TransactionReceiptType.COSTUMER);
                    try {
                        Thread.sleep(Long.parseLong(timeBetweenPrint));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    //printReceipt(receiptData, TransactionReceiptType.SELLER);
                    break;
                case NONE:
                    break;
            }
        });

        return GeneralRetCode.SUCCESS;
    }

    public GeneralRetCode makeBitmapPrint(Bitmap img){
        if (printer != null) {
            printer.initPrinter();
        }
        int status = printer.getStatus();
        Log.d("Printer", "Printer status: " + status);
        if (status != 0) {
            Log.e("Printer", "Printer is not ready. Status: " + status);
            return GeneralRetCode.ERROR;
        }

        assert printer != null;
        printer.setLetterSpacing(2);
//        printer.se(5);  // מגדיל רזולוציה במדפסת
        printer.setGray(GrayLevelEnum.LEVEL_1);  // מבטיח שקווים שחורים יהיו ברורים
        printer.appendImage(img, AlignEnum.CENTER);
        printer.startPrint(true, result -> Log.d("Printer", "Print result: " + result));

        Executors.newSingleThreadExecutor().execute(() -> {
            switch (TransactionReceiptType.COSTUMER_SELLER) {
                case COSTUMER:
                case SELLER:
                    //printReceipt(receiptData, printType);
                    break;
                case COSTUMER_SELLER:
                    //printReceipt(receiptData, TransactionReceiptType.COSTUMER);
                    try {
                        Thread.sleep(Long.parseLong("20"));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    //printReceipt(receiptData, TransactionReceiptType.SELLER);
                    break;
                case NONE:
                    break;
            }
        });

        return GeneralRetCode.SUCCESS;
    }
    private void printReceipt(TransactionReceipt receiptData, TransactionReceiptType printType) {
        if (printer != null) {
            //ControlerUtils.createReceipt(receiptData, appContext, printer, printType);
            printer.startPrint(true, retCode -> {
                if (retCode != SdkResult.Success) {
                    Log.d("print", "print failed: " + retCode);
                }
            });
        }
    }

    public static void createReceipt(TransactionReceipt receiptData, Context context, Printer printer, TransactionReceiptType printType) {

        // פונקציה פנימית להוספת שדה למדפסת אם הוא לא ריק
//        if (value != null && !value.isEmpty()) {
//            String label = context.getString(labelResId);
//            String formattedString = swap ? value + " " + label : label + " " + value;
//            printer.appendPrnStr(formattedString, 22, AlignEnum.RIGHT, false);
//        }

//        List<Triple<Integer, String, Boolean>> generalFields = new ArrayList<>();
//        generalFields.add(new Triple<>(R.string.terminal_name, receiptData.getTerminalName(), false));
//        generalFields.add(new Triple<>(R.string.terminal_number, receiptData.getTerminalNumber(), false));
//        generalFields.add(new Triple<>(R.string.app_version, receiptData.getAppVersion(), false));
//        generalFields.add(new Triple<>(R.string.merchant_id, receiptData.getMerchantID(), false));
//        generalFields.add(new Triple<>(R.string.date_time, receiptData.getDateTime(), false));
//        generalFields.add(new Triple<>(R.string.card_name, receiptData.getCardName(), false));
//        generalFields.add(new Triple<>(R.string.card_number, receiptData.getCardSeqNumber(), false));
//        generalFields.add(new Triple<>(R.string.receipt_number, receiptData.getReceiptNumber(), false));
//        generalFields.add(new Triple<>(R.string.uid, receiptData.getUid(), true));
//        generalFields.add(new Triple<>(R.string.rrn, receiptData.getRrn(), true));
//        generalFields.add(new Triple<>(R.string.tran_type, mapToTranType(receiptData.getTranType()), false));
//        generalFields.add(new Triple<>(R.string.auth_issuer_number, receiptData.getAuthManpikNo(), false));
//        generalFields.add(new Triple<>(R.string.authorize_by, receiptData.getAuthorizeBy(), false));
//        generalFields.add(new Triple<>(R.string.entry_mode, mapToEntryMode(receiptData.getPanEntryMode()), false));
//        generalFields.add(new Triple<>(R.string.credit_terms, mapToCreditTerms(receiptData.getCreditTerms()), false));
//        generalFields.add(new Triple<>(R.string.amount, receiptData.getAmount(), false));
//        generalFields.add(new Triple<>(R.string.currency, receiptData.getCurrrency(), false));
//        generalFields.add(new Triple<>(R.string.amount_after_conversion, receiptData.getAmountAfterConvert(), false));
//        generalFields.add(new Triple<>(R.string.currency_conversion, receiptData.getCurrencyConversion(), false));
//        generalFields.add(new Triple<>(R.string.conversion_rate, receiptData.getConversionRate(), false));
//        generalFields.add(new Triple<>(R.string.conversion_factor, receiptData.getConversionFactor(), false));
//        generalFields.add(new Triple<>(R.string.more_payments, receiptData.getMorePayments(), false));
//        generalFields.add(new Triple<>(R.string.another_payment, receiptData.getAnotherPayment(), false));
//        generalFields.add(new Triple<>(R.string.charge_on_time, receiptData.getChargeOnTime(), false));
//        generalFields.add(new Triple<>(R.string.tip, receiptData.getTip(), false));
//        generalFields.add(new Triple<>(R.string.net_amount, receiptData.getNetAmount(), false));
//        generalFields.add(new Triple<>(R.string.discount_amount, receiptData.getDiscountAmount(), false));
//        generalFields.add(new Triple<>(R.string.discount_unit_number, receiptData.getDiscountUnitNumber(), false));
//        generalFields.add(new Triple<>(R.string.cash_amount, receiptData.getCashAmount(), false));
//        generalFields.add(new Triple<>(R.string.total_amount, receiptData.getTotalAmount(), false));
//
//        if (!"0".equals(receiptData.getPaymentsNumber())) {
//            generalFields.add(new Triple<>(R.string.payments_number, receiptData.getPaymentsNumber(), false));
//        }
//        if (!"0".equals(receiptData.getFirstPayment()) && !"0.00".equals(receiptData.getFirstPayment())) {
//            generalFields.add(new Triple<>(R.string.first_payment, receiptData.getFirstPayment(), false));
//        }
//
//        List<Triple<Integer, String, Boolean>> sellerFields = new ArrayList<>();
//        sellerFields.add(new Triple<>(R.string.company_message, receiptData.getCompanyMessage(), false));
//        sellerFields.add(new Triple<>(R.string.aid, receiptData.getAid(), false));
//        sellerFields.add(new Triple<>(R.string.verified_by_pin, receiptData.getVerifiedByPin(), false));
//        sellerFields.add(new Triple<>(R.string.tsi, receiptData.getTsi(), true));
//        sellerFields.add(new Triple<>(R.string.arc, receiptData.getArc(), true));
//        sellerFields.add(new Triple<>(R.string.tvr, receiptData.getTvr(), true));
//        sellerFields.add(new Triple<>(R.string.atc, receiptData.getAtc(), true));
//        sellerFields.add(new Triple<>(R.string.costumer_signature, "_____", false));
//        sellerFields.add(new Triple<>(R.string.costumer_phone_number, "_____", false));
//
//        for (Triple<Integer, String, Boolean> field : generalFields) {
//            appendIfNotEmpty(context, printer, field.getFirst(), field.getSecond(), field.getThird());
//        }
//
//        if (printType == TransactionReceiptType.SELLER) {
//            for (Triple<Integer, String, Boolean> field : sellerFields) {
//                appendIfNotEmpty(context, printer, field.getFirst(), field.getSecond(), field.getThird());
//            }
//        }
    }

    @NonNull
    @Override
    public GeneralRetCode closeAllLed() {
        return null;
    }

    @NonNull
    @Override
    public GeneralRetCode controlBeeper(@NonNull DeviceBeepLevel deviceBeepLevel) {
        return null;
    }

    @NonNull
    @Override
    public GeneralRetCode controlLed(@NonNull DeviceLedColor deviceLedColor, boolean b) {
        return null;
    }

    @NonNull
    @Override
    public String getDeviceSerial() {
        //return "0";
        try {
            Object info = deviceEngine.getDeviceInfo();
            if (info == null) return null;

            // נסה מתודות נפוצות בלי לדעת שם מדויק
            String[] candidates = {
                    "getSerialNo", "getSerialNumber", "getSN", "getSn", "getDeviceSn",
                    "getTerminalSn", "getTerminalSerial", "getDeviceSerial", "getSNumber"
            };

            for (String mName : candidates) {
                try {
                    java.lang.reflect.Method m = info.getClass().getMethod(mName);
                    Object v = m.invoke(info);
                    if (v != null) {
                        String s = String.valueOf(v).trim();
                        if (!s.isEmpty() && !"null".equalsIgnoreCase(s) && !"unknown".equalsIgnoreCase(s)) {
                            return s;
                        }
                    }
                } catch (Throwable ignore) {}
            }

            // אם לא מצא – תדפיסי את כל המתודות כדי שנדע מה לקרוא
            for (java.lang.reflect.Method m : info.getClass().getMethods()) {
                if (m.getParameterTypes().length == 0 && m.getReturnType() == String.class) {
                    try {
                        Object v = m.invoke(info);
                        if (v != null) {
                            String s = String.valueOf(v).trim();
                            if (!s.isEmpty()) {
                                android.util.Log.d("NEXGO_DEVICEINFO", m.getName() + " -> " + s);
                            }
                        }
                    } catch (Throwable ignore) {}
                }
            }

            return null;
        } catch (Throwable t) {
            android.util.Log.e("NEXGO_DEVICEINFO", "failed", t);
            return null;
        }

    }

    @Override
    public void enableKioskMode() {

    }

    @Override
    public void disableKioskMode() {

    }
}
