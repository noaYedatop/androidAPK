package com.credix.pinpaddriverwithandroidusage;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.nexgolibrary.common.NexgoEventManager;
import com.example.nexgolibrary.common.NexgoEventsListener;
import com.example.nexgolibrary.utils.transaction.PinEvent;

public class NexgoLibraryEventsListener implements NexgoEventsListener {
    private Context context;
    public NexgoLibraryEventsListener(Context context) {
        this.context = context;
        NexgoEventManager.INSTANCE.registerListener(this);
    }
    @Override
    public void onEventReceiveTransactionsMessages(String msg) {
        Log.i("onEventReceiveTransactionsMessages: " , msg);
        onNewTransactionMessage(msg);
    }

    @Override
    public void onEventPin(PinEvent pinEvent) {
        Log.i("onEventPin: " , pinEvent.name());

//        switch (pinEvent) {
//            case PIN_FIRST_TIME:
//                onNewPin("PIN_SCREEN_NAVIGATE");
//                break;
//            case PIN_ENTRY_COMPLETED:
//                onNewPin("PIN_ENTERED_EVENT");
//                break;
//            case PIN_ENTRY_DIGIT:
//                onNewPin("PIN_DIGIT_EVENT");
//                break;
//            case PIN_ENTRY_CLEAR:
//                onNewPin("PIN_CLEAR_DIGITS_EVENT");
//                break;
//            default:
//                break;
//        }
    }

    private void onNewTransactionMessage(String msg) {
        Log.i("onNewTransactionMessage - ", msg);
        Intent intent = new Intent("common-payment-event");
        intent.putExtra("transaction_message", msg);
        context.sendBroadcast(intent);
    }

    private void onNewPin(String pinEvent) {
        Log.i("onNewPin - ", pinEvent);
        Intent intent = new Intent("common-payment-event");
        intent.putExtra("pin_event", pinEvent);
        context.sendBroadcast(intent);
    }

    public void unregisterListener() {
        NexgoEventManager.INSTANCE.unregisterListener();
    }
}

//    @Override
//    public void onEventPin(@NonNull PinEvent pinEvent) {
//        Log.i("onEventPin" , pinEvent.toString());
//        switch (pinEvent) {
//            case PIN_FIRST_TIME:
//                onNewPin("PIN_SCREEN_NAVIGATE");
//                break;
//            case PIN_ENTRY_COMPLETED:
//                onNewPin("PIN_ENTERED_EVENT");
//                break;
//            case PIN_ENTRY_DIGIT:
//                onNewPin("PIN_DIGIT_EVENT");
//                break;
//            case PIN_ENTRY_CLEAR:
//                onNewPin("PIN_CLEAR_DIGITS_EVENT");
//                break;
//            default:
//                // Do nothing
//                break;
//        }
//    }
//
//    @Override
//    public void onEventReceiveTransactionsMessages(@NonNull String s) {
//        Log.i("On onEventReceiveTransactionsMessage Transaction" , s);
//    }
//
//    private void onNewPin(String msg){
//        Intent intent = new Intent("common-payment-event");
//        intent.putExtra("transaction_event", msg);
//        context.sendBroadcast(intent);
//    }
//
//    private void onNewTransactionsMessages(String pinEvent){
//        Intent intent = new Intent("common-payment-event");
//        intent.putExtra("pin_event", pinEvent);
//        context.sendBroadcast(intent);
//    }
//}
