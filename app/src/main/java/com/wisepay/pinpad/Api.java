//package com.credix.pinpad.jni;
package com.wisepay.pinpad;

public class Api {

    // On Linux use: "WisepayPinPadDriver_jni" - no 'lib' prefix
    // On Windows use: "libWisepayPinPadDriver_jni"
    static {
        System.loadLibrary("WisepayPinPadDriver_jni");
    }
    // Native private method declaration
    private native void nativeVersion(byte[] response, int responseLength);

    private native void nativeSetDebug(boolean verbose);

    private native int nativeOpen2(String address, String pinCode);

    private native int nativeClose();

    private native int nativeShutdown();

    private native int nativeTxn(String txnType,
                                 int amount,
                                 String currency,
                                 String creditTerms,
                                 int noPayments,
                                 int firstPayment,
                                 int notFirstPayment,
                                 int creditNoPayments,
                                 String indexPayment,
                                 int cashbackAmount,
                                 String authNum,
                                 String motoPanEntry,
                                 boolean printVoucher,
                                 String context,
                                 String userData);

    private native int nativeCancelTxn(String voucherNum,
                                       int amount,
                                       String currency,
                                       String motoPanEntry,
                                       boolean printVoucher,
                                       String context,
                                       String userData);

    private native int nativeCancelTxnByUID(String UID,
                                            int amount,
                                            String currency,
                                            String motoPanEntry,
                                            boolean printVoucher,
                                            String context,
                                            String userData);

    private native int nativeCancelTxnNoValidate(String UID,
                                                 String voucherNum,
                                                 boolean printVoucher,
                                                 String context,
                                                 String userData);

    private native int nativeJ2(String motoPanEntry,
                                String context);

    private native int nativeJ2Ex(String motoPanEntry,
                                  int amount,
                                  String currency,
                                  String txnType,
                                  String creditTerms,
                                  int noPayments,
                                  int firstPayment,
                                  int notFirstPayment,
                                  int creditNoPayments,
                                  String indexPayment,
                                  int cashbackAmount,
                                  String context,
                                  String userData);

    private native int nativeJ5(int amount,
                                String currency,
                                String creditTerms,
                                int noPayments,
                                int firstPayment,
                                int notFirstPayment,
                                int creditNoPayments,
                                String indxPayment,
                                String motoPanEntry,
                                boolean printVoucher,
                                String context,
                                String userData);

    private native int nativeSettleJ5(String authNum,
                                      int amount,
                                      String currency,
                                      String motoPanEntry,
                                      boolean printVoucher,
                                      String context,
                                      String userData);

    private native int nativeCancelJ5(String authNum,
                                      int amount,
                                      String currency,
                                      String motoPanEntry,
                                      boolean printVoucher,
                                      String context,
                                      String userData);

    private native int nativeCheckBalance(String motoPanEntry,
                                          boolean printVoucher,
                                          String context,
                                          String userData);

    private native int nativeDepositTxns(String reportFormat,
                                         boolean printVoucher,
                                         String context);

    private native int nativeDepositTxns2(String reportFormat,
                                          boolean printVoucher,
                                          String context);

    private native int nativeLastDeposit(String reportFormat,
                                         boolean printVoucher,
                                         String context);

    private native int nativeLastDeposit2(String reportFormat,
                                          boolean printVoucher,
                                          String context);

    private native int nativeLastTxnDetails(boolean printVoucher,
                                            String context);

    private native int nativeReadCard(String displayMsg,
                                      String context);

    private native int nativeGetInfo(String context);

    private native int nativeIsResponseReady(int msTimeout);

    private native void nativeGetResponse(byte[] response, int responseLength);

    private native void nativeCancelOngoingTxn();

    private native int nativePrintLine(String line,
                                       String fontSize,
                                       String alignment,
                                       boolean underline,
                                       boolean reverse);

    private native int nativePrintParagraph(String paragraphInJsonFormat);

    private native int nativeFeedLine(int numOfLines);

    private native int nativeMessageBox(String header,
                                        String message,
                                        String icon,
                                        String footerStyle,
                                        int timeout,
                                        String context);

    private native int nativeSetLocale(String locale);

    private native int nativeQueryByppContext(String ppContext,
                                              String context);

    private native int nativeTxnStart(String panEntryMode,
                                      int amount,
                                      String currency,
                                      String txnType,
                                      String context);

    private native int nativeTxnStartEx(String motoPanEntry,
                                        int amount,
                                        String currency,
                                        String txnType,
                                        String creditTerms,
                                        int noPayments,
                                        int firstPayment,
                                        int notFirstPayment,
                                        int creditNoPayments,
                                        String indexPayment,
                                        int cashbackAmount,
                                        String context,
                                        String userData);

    private native int nativeTxnEnd(String pendingTxnId,
                                    String txnType,
                                    int amount,
                                    String currency,
                                    String creditTerms,
                                    int noPayments,
                                    int firstPayment,
                                    int notFirstPayment,
                                    int creditNoPayments,
                                    String indexPayment,
                                    int cashbackAmount,
                                    String authNum,
                                    boolean printVoucher,
                                    boolean userConfirm,
                                    String context,
                                    String userData);

    private native int nativeCancelTwoPhasePendingTxn(String pendingTxnId);

    public void Version(byte[] response, int responseLength) {
        nativeVersion(response, responseLength);
    }

    public void SetDebug(boolean verbose) {
        nativeSetDebug(verbose);
    }

    public Integer Open2(String address, String pinCode) {
        return nativeOpen2(address, pinCode);
    }

    public Integer Close() {
        return nativeClose();
    }

    public Integer Shutdown() {
        return nativeShutdown();
    }

    public Integer Txn(String txnType,
                       Integer amount,
                       String currency,
                       String creditTerms,
                       Integer noPayments,
                       Integer firstPayment,
                       Integer notFirstPayment,
                       Integer creditNoPayments,
                       String indexPayment,
                       Integer cashbackAmount,
                       String authNum,
                       String motoPanEntry,
                       boolean printVoucher,
                       String context,
                       String userData) {

        return nativeTxn(txnType, amount, currency, creditTerms, noPayments, firstPayment, notFirstPayment,
                creditNoPayments, indexPayment, cashbackAmount, authNum,
                motoPanEntry, printVoucher, context, userData);
    }

    public Integer CancelTxn(String voucherNum,
                             Integer amount,
                             String currency,
                             String motoPanEntry,
                             boolean printVoucher,
                             String context,
                             String userData) {

        return nativeCancelTxn(voucherNum, amount, currency,
                motoPanEntry, printVoucher, context, userData);
    }

    public Integer CancelTxnByUID(String UID,
                                  Integer amount,
                                  String currency,
                                  String motoPanEntry,
                                  boolean printVoucher,
                                  String context,
                                  String userData) {

        return nativeCancelTxnByUID(UID, amount, currency,
                motoPanEntry, printVoucher, context, userData);
    }

    public Integer J5(int amount,
                      String currency,
                      String creditTerms,
                      Integer noPayments,
                      Integer firstPayment,
                      Integer notFirstPayment,
                      Integer creditNoPayments,
                      String indxPayment,
                      String motoPanEntry,
                      boolean printVoucher,
                      String context,
                      String userData) {

        return nativeJ5(amount, currency, creditTerms, noPayments, firstPayment, notFirstPayment,
                creditNoPayments, indxPayment,
                motoPanEntry, printVoucher, context, userData);
    }

    public Integer SettleJ5(String authNum,
                            Integer amount,
                            String currency,
                            String motoPanEntry,
                            boolean printVoucher,
                            String context,
                            String userData) {

        return nativeSettleJ5(authNum, amount, currency,
                motoPanEntry, printVoucher, context, userData);
    }

    public Integer CancelJ5(String authNum,
                            Integer amount,
                            String currency,
                            String motoPanEntry,
                            boolean printVoucher,
                            String context,
                            String userData) {

        return nativeCancelJ5(authNum, amount, currency,
                motoPanEntry, printVoucher, context, userData);
    }

    public Integer CheckBalance(String motoPanEntry,
                                boolean printVoucher,
                                String context,
                                String userData) {

        return nativeCheckBalance(motoPanEntry, printVoucher,
                context, userData);
    }

    public Integer DepositTxns(String reportFormat,
                               boolean printVoucher,
                               String context) {

        return nativeDepositTxns(reportFormat, printVoucher, context);
    }

    public Integer DepositTxns2(String reportFormat,
                                boolean printVoucher,
                                String context) {

        return nativeDepositTxns2(reportFormat, printVoucher, context);
    }

    public Integer LastDeposit(String reportFormat,
                               boolean printVoucher,
                               String context) {

        return nativeLastDeposit(reportFormat, printVoucher, context);
    }

    public Integer LastDeposit2(String reportFormat,
                                boolean printVoucher,
                                String context) {

        return nativeLastDeposit2(reportFormat, printVoucher, context);
    }

    public Integer LastTxnDetails(boolean printVoucher, String context) {

        return nativeLastTxnDetails(printVoucher, context);
    }

    public Integer ReadCard(String displayMsg, String context) {

        return nativeReadCard(displayMsg, context);
    }

    public Integer GetInfo(String context) {

        return nativeGetInfo(context);
    }

    public Integer IsResponseReady(Integer msTimeout) {

        return nativeIsResponseReady(msTimeout);
    }

    public void GetResponse(byte[] response, Integer responseLength) {

        nativeGetResponse(response, responseLength);
    }

    public void CancelOngoingTxn() {

        nativeCancelOngoingTxn();
    }

    public int PrintLine(String line,
                         String fontSize,
                         String alignment,
                         boolean underline,
                         boolean reverse) {
        return nativePrintLine(line, fontSize, alignment, underline, reverse);
    }

    public int PringParagraph(String paragraphInJsonFormat) {

        return nativePrintParagraph(paragraphInJsonFormat);
    }

    public int FeedLine(int numOfLines) {

        return nativeFeedLine(numOfLines);
    }

    public int MessageBox(String header,
                          String message,
                          String icon,
                          String footerStyle,
                          int timeout,
                          String context) {

        return nativeMessageBox(header, message, icon, footerStyle, timeout, context);
    }

    public int J2(String motoPanEntry,
                  String context) {

        return nativeJ2(motoPanEntry, context);
    }

    public int J2Ex(String motoPanEntry,
                    int amount,
                    String currency,
                    String txnType,
                    String creditTerms,
                    int noPayments,
                    int firstPayment,
                    int notFirstPayment,
                    int creditNoPayments,
                    String indexPayment,
                    int cashbackAmount,
                    String context,
                    String userData) {

        return nativeJ2Ex(motoPanEntry, amount, currency, txnType, creditTerms, noPayments, firstPayment,
                notFirstPayment, creditNoPayments, indexPayment, cashbackAmount, context, userData);
    }

    public int CancelTxnNoValidate(String UID,
                                   String voucherNum,
                                   boolean printVoucher,
                                   String context,
                                   String userData) {

        return nativeCancelTxnNoValidate(UID, voucherNum, printVoucher, context, userData);
    }

    public  int SetLocale(String locale) {

        return nativeSetLocale(locale);
    }

    public int QueryByppContext(String ppContext, String context) {

        return nativeQueryByppContext(ppContext, context);
    }

    public int TxnStart(String panEntryMode,
                        int amount,
                        String currency,
                        String txnType,
                        String context){
        return nativeTxnStart(panEntryMode, amount, currency, txnType, context);
    }

    public int TxnStartEx(String motoPanEntry,
                          int amount,
                          String currency,
                          String txnType,
                          String creditTerms,
                          int noPayments,
                          int firstPayment,
                          int notFirstPayment,
                          int creditNoPayments,
                          String indexPayment,
                          int cashbackAmount,
                          String context,
                          String userData) {

        return nativeTxnStartEx(motoPanEntry, amount, currency, txnType, creditTerms, noPayments,
                firstPayment, notFirstPayment, creditNoPayments,
                indexPayment, cashbackAmount, context, userData);
    }

    public int TxnEnd(String pendingTxnId,
                      String txnType,
                      int amount,
                      String currency,
                      String creditTerms,
                      int noPayments,
                      int firstPayment,
                      int notFirstPayment,
                      int creditNoPayments,
                      String indexPayment,
                      int cashbackAmount,
                      String authNum,
                      boolean printVoucher,
                      boolean userConfirm,
                      String context,
                      String userData) {
        return nativeTxnEnd(pendingTxnId, txnType, amount, currency, creditTerms, noPayments, firstPayment, notFirstPayment,
                creditNoPayments, indexPayment, cashbackAmount, authNum, printVoucher, userConfirm, context, userData);
    }

    public int CancelTwoPhasePendingTxn(String pendingTxnId) {

        return nativeCancelTwoPhasePendingTxn(pendingTxnId);
    }
}
