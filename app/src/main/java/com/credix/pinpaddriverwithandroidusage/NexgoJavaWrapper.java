package com.credix.pinpaddriverwithandroidusage;

import android.util.Log;
import android.webkit.JavascriptInterface;

import com.example.nexgolibrary.data.nexgo.device_config.NexgoDeviceConfig;
import com.example.nexgolibrary.data.nexgo.device_transaction.NexgoDeviceTransaction;
import com.example.nexgolibrary.domain.models.api.common.ClientDetails;
import com.example.nexgolibrary.domain.models.transaction.TelephoneTransaction;
import com.example.nexgolibrary.domain.models.transaction.TransactionResultUser;
import com.example.nexgolibrary.utils.terminal.InitResCode;
import com.example.nexgolibrary.domain.models.transaction.NexgoTransaction;
import com.example.nexgolibrary.utils.transaction.TranType;
import com.example.nexgolibrary.utils.transaction.TransactionResCode;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class NexgoJavaWrapper {

    private final NexgoDeviceConfig nexgoDeviceConfig;
    private final NexgoDeviceTransaction nexgoDeviceTransaction;

    public NexgoJavaWrapper(NexgoDeviceConfig config, NexgoDeviceTransaction transaction) {
        this.nexgoDeviceConfig = config;
        this.nexgoDeviceTransaction = transaction;
    }

    public CompletableFuture<String> initNexgo(String username, String password, String terminalNum) {
        JSONObject nexgoInitResult = new JSONObject();
        return CompletableFuture.supplyAsync(() -> {
            ClientDetails clientDetails = new ClientDetails(username, password, terminalNum);
            try {
                InitResCode result = NexgoDeviceHelper.initDeviceConfigSync(nexgoDeviceConfig, clientDetails);
                nexgoInitResult.put("returnValue", result.name());
                return nexgoInitResult.toString();
            } catch (Exception e) {
                Log.e("NexgoPayment", "Error during initialization", e);
                try {
                    nexgoInitResult.put("result", InitResCode.INIT_GATEWAY_FAILED.name());
                } catch (JSONException ex) {
                    throw new RuntimeException(ex);
                }
                return nexgoInitResult.toString();
            }
        });
    }

    public CompletableFuture<String> startNexgoPayment(String userName, String password, String terminal, String kupaNum, String amount, String currency,
                                                       String tranType, String creditTerms, String paymentsNumber, String firstPayment, String fixedPayment,
                                                       String isManual, String credit, String cardNum, String exDate, String cvv, String id) {
        Log.i("startNexgoPayment startNexgoPayment ", "startNexgoPayment - HERE");
        double amountDouble = Double.parseDouble(amount);
        long tAmount = (long) Math.abs(amountDouble * 100);
        TranType tTranType = tranType.equals("REGULAR") ? TranType.REGULAR : TranType.REFUND;
        int tCreditTerms = creditTerms.equals("REGULAR") ? 1 : 0;
        if (creditTerms.equals("PLUS30")){
            tCreditTerms = 2;
        }
        int tPaymentsNumber = (paymentsNumber != null && !paymentsNumber.isEmpty()) ? Integer.valueOf(paymentsNumber) : null;
        long tFirstPayment = (firstPayment != null && !firstPayment.isEmpty()) ? Long.parseLong(firstPayment) * 100 : 0L;
        long tFixedPayment = (fixedPayment != null && !fixedPayment.isEmpty()) ? Long.parseLong(fixedPayment) * 100 : 0L;
        int tCredit = (credit != null && !credit.isEmpty()) ? Integer.valueOf(credit) : null;

        if(tPaymentsNumber > 0){
            tCreditTerms = 8;//payments code
            if(tFirstPayment == 0L){
                tFirstPayment = tAmount / tPaymentsNumber;
                long itra = tAmount - tFirstPayment;
                tFixedPayment = itra / (tPaymentsNumber - 1);
            } else {
                tFixedPayment = (tAmount - tFirstPayment) / (tPaymentsNumber - 1);
            }
            tPaymentsNumber = tPaymentsNumber -1;
        }
        if(tCredit > 0){
            tCreditTerms = 6;
            tPaymentsNumber = tCredit;
            tFixedPayment = tAmount / tCredit;
        }
        long ttFirstPayment = tFirstPayment;
        long ttFixedPayment = tFixedPayment;
        int ttCreditTerms = tCreditTerms;
        int ttPaymentsNumber = tPaymentsNumber;
        JSONObject nexgoResult = new JSONObject();
        return CompletableFuture.supplyAsync(() -> {
            TransactionResCode result;
            try {
                NexgoTransaction nexgoTransaction = new NexgoTransaction("", kupaNum, tAmount, 0L,
                        tTranType, false, ttCreditTerms, ttPaymentsNumber, ttFirstPayment, ttFixedPayment,
                    "376", currency, null, null, "", null, null);
                Log.i("TRANSACTIONNNNNNN", nexgoTransaction.toString());
                ClientDetails clientDetails = new ClientDetails(userName, password, terminal);
                if(Objects.equals(isManual, "1")){
                    TelephoneTransaction telephoneTransaction = new TelephoneTransaction(cardNum ,exDate, cvv, id, "");
                    result = NexgoDeviceHelper.startManualTransactionSync(nexgoDeviceTransaction, nexgoTransaction, clientDetails, telephoneTransaction);
                } else {
                    result = NexgoDeviceHelper.startTransactionSync(nexgoDeviceTransaction, nexgoTransaction, clientDetails);
                }
                if(result.name().equals("TRANSACTION_OK") && nexgoDeviceTransaction.getFinishedTransactionResult().getStatus() == 0) {
                    return buildTransactionRes(nexgoDeviceTransaction.getFinishedTransactionResult());
                } else if(result.name().equals("TRANSACTION_OK") &&  nexgoDeviceTransaction.getFinishedTransactionResult().getStatus() != 0){
                    try {
                        nexgoResult.put("returnValue", "TRANSACTION_FAILED");
                        nexgoResult.put("codeDes", nexgoDeviceTransaction.getFinishedTransactionResult().getAshStatusDes());
                        nexgoResult.put("codeError", nexgoDeviceTransaction.getFinishedTransactionResult().getStatus());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    return nexgoResult.toString();
                }
                if(result.name().equals("TRANSACTION_CANCELED")) {
                        try {
                            nexgoResult.put("returnValue", "TRANSACTION_CANCELED");
                            nexgoResult.put("codeDes", nexgoDeviceTransaction.getFinishedTransactionResult().getAshStatusDes());
                            nexgoResult.put("codeError", nexgoDeviceTransaction.getFinishedTransactionResult().getStatus());
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    return nexgoResult.toString();
                } else {
                    try {
                        nexgoResult.put("returnValue", "TRANSACTION_ERROR");
                        nexgoResult.put("codeDes", nexgoDeviceTransaction.getFinishedTransactionResult().getAshStatusDes());
                        nexgoResult.put("codeError", nexgoDeviceTransaction.getFinishedTransactionResult().getStatus());

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    return nexgoResult.toString();
                }
            } catch (Exception e) {
                Log.e("NexgoPayment Transaction", "Error during transaction", e);
                try {
                    nexgoResult.put("returnValue", "TRANSACTION_ERROR");
                    nexgoResult.put("codeDes", nexgoDeviceTransaction.getFinishedTransactionResult().getAshStatusDes());
                    nexgoResult.put("codeError", nexgoDeviceTransaction.getFinishedTransactionResult().getStatus());
                } catch (JSONException e1) {
                    throw new RuntimeException(e1);
                }
                return nexgoResult.toString();
            }
        });
    }

    public void cancelTransaction(){
        NexgoDeviceHelper.cancelTransactionSync(nexgoDeviceTransaction);
    }
    private String buildTransactionRes(TransactionResultUser resDet) throws JSONException {
        JSONObject nexgoResult = new JSONObject();
        String amt = String.valueOf(Float.parseFloat(resDet.getAmount()) / 100);
        if(resDet.getTranType().equals("53")){
            amt = String.valueOf((Float.parseFloat(resDet.getAmount()) / 100) * -1 );
        }
        nexgoResult.put("returnValue", "TRANSACTION_OK");
        nexgoResult.put("amount", amt);
        nexgoResult.put("cardNumber", resDet.getPan());
        nexgoResult.put("cardExpirationDate", resDet.getExpirationDate());
        nexgoResult.put("cardBrand", resDet.getBrand());
        nexgoResult.put("creditTerms", resDet.getCreditTerms());
        nexgoResult.put("approvalNumber", resDet.getCompRetailerNum());
        nexgoResult.put("tranType", resDet.getTranType());
        String firstPay = String.valueOf(Float.parseFloat(resDet.getFirstPayment()) / 100);
        nexgoResult.put("firstPaymentAmount", firstPay);
        nexgoResult.put("numberOfPayments", resDet.getNoPayments());
        nexgoResult.put("creditCardCompany", resDet.getCardName());

        return nexgoResult.toString();
    }
}
