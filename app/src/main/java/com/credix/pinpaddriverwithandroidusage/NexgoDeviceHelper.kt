@file:JvmName("NexgoDeviceHelper") // שינוי השם של הקובץ ש-Java מזהה

package com.credix.pinpaddriverwithandroidusage

import android.util.Log
import com.example.nexgolibrary.data.nexgo.device_config.NexgoDeviceConfig
import com.example.nexgolibrary.data.nexgo.device_controller.ControlerUtils.createReceipt
import com.example.nexgolibrary.data.nexgo.device_transaction.NexgoDeviceTransaction
import com.example.nexgolibrary.domain.models.api.common.ClientDetails
import com.example.nexgolibrary.utils.terminal.InitResCode
import com.example.nexgolibrary.domain.models.transaction.NexgoTransaction;
import com.example.nexgolibrary.domain.models.transaction.TelephoneTransaction
import com.example.nexgolibrary.domain.models.transaction.TransactionReceipt
import com.example.nexgolibrary.utils.transaction.TransactionReceiptType
import com.example.nexgolibrary.utils.transaction.TransactionResCode
import com.nexgo.oaf.apiv3.SdkResult
import kotlinx.coroutines.runBlocking

object NexgoDeviceHelper {
    @JvmStatic
    fun initDeviceConfigSync(nexgoDeviceConfig: NexgoDeviceConfig, clientDetails: ClientDetails): InitResCode {
        return runBlocking {
            nexgoDeviceConfig.initDeviceConfiguration(clientDetails)
        }
    }

    @JvmStatic
    fun startTransactionSync(nexgoDeviceTransaction: NexgoDeviceTransaction, nexgoTransaction: NexgoTransaction, clientDetails: ClientDetails): TransactionResCode {
        return runBlocking {
            try {
                val result = nexgoDeviceTransaction.startTransaction(nexgoTransaction, clientDetails)
                Log.d("Transaction", "Transaction finished with result: $result")
                result
            } catch (e: Exception) {
                Log.e("Transaction", "Transaction failed", e)
                TransactionResCode.TRANSACTION_ERROR // החזרת קוד שגיאה במקום קריסה
            }
        }
    }

    @JvmStatic
    fun startManualTransactionSync(nexgoDeviceTransaction: NexgoDeviceTransaction, nexgoTransaction: NexgoTransaction, clientDetails: ClientDetails, telephoneTransaction: TelephoneTransaction): TransactionResCode {
        return runBlocking {
            try {
                val result = nexgoDeviceTransaction.makeManualTransaction(clientDetails, nexgoTransaction, telephoneTransaction)
                Log.d("Transaction", "Transaction finished with result: $result")
                result
            } catch (e: Exception) {
                Log.e("Transaction", "Transaction failed", e)
                TransactionResCode.TRANSACTION_ERROR // החזרת קוד שגיאה במקום קריסה
            }
        }
    }
//    private fun print(receiptData: TransactionReceipt, printType: TransactionReceiptType ){
//        printer?.let{ it->
//            createReceipt(receiptData, , it, printType)
//            it.startPrint(true) { retCode ->
//                if(retCode != SdkResult.Success){
////Log
//                }
//            }
//        }
//    }

    @JvmStatic
    fun cancelTransactionSync(nexgoDeviceTransaction: NexgoDeviceTransaction) {
        return runBlocking {
            try {
                nexgoDeviceTransaction.cancelTransaction()
                Log.i("cancelTransaction cancelTransaction ","cancelTransaction - HERE");
                TransactionResCode.TRANSACTION_CANCELED // החזרת קוד שגיאה במקום קריסה
            } catch (e: Exception) {
                Log.e("cancelTransaction", "cancelTransaction failed", e)
                TransactionResCode.TRANSACTION_CANCELED // החזרת קוד שגיאה במקום קריסה
            }
        }
    }

}

