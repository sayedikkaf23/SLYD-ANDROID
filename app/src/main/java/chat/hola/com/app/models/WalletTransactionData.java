package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WalletTransactionData implements Serializable {

    @SerializedName("walletid")
    @Expose
    private String walletId;
    @SerializedName("txntimestamp")
    @Expose
    private String txnTimeStamp;
    @SerializedName("txnid")
    @Expose
    private String txnId;
    @SerializedName("amount")
    @Expose
    private String amount;
    @SerializedName("closingbal")
    @Expose
    private String closingbal;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("initiatedby")
    @Expose
    private String initiatedBy;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("openingbal")
    @Expose
    private String openingBalance;
    @SerializedName("trigger")
    @Expose
    private String trigger;
    @SerializedName("txntype")
    @Expose
    private Integer txnType;
    @SerializedName("txntypetext")
    @Expose
    private String txnTypeText;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol = "";
    @SerializedName(value = "to", alternate = "user_to")
    @Expose
    private String to;
    @SerializedName(value = "from", alternate = "user_from")
    @Expose
    private String from;
    @SerializedName("senderImage")
    @Expose
    private String senderImage;
    @SerializedName("receiverImage")
    @Expose
    private String receiverImage;
    @SerializedName("txn_fee")
    @Expose
    private String txn_fee;
    @SerializedName("doc_type")
    @Expose
    private String doc_type;
    @SerializedName("bank")
    @Expose
    private String bank;
    @SerializedName("rechargemode")
    @Expose
    private String rechargemode;

    @SerializedName("receiverUserName")
    @Expose
    private String receiverUserName;
    @SerializedName("senderUserName")
    @Expose
    private String senderUserName;
    @SerializedName("fromAmount")
    @Expose
    private String fromAmount;
    @SerializedName("toAmount")
    @Expose
    private String toAmount;
    @SerializedName("transferCommission")
    @Expose
    private String transferCommission;
    @SerializedName("toCurrencySymbol")
    @Expose
    private String toCurrencySymbol;
    @SerializedName("toCurrency")
    @Expose
    private String toCurrency;
    @SerializedName("toUserId")
    @Expose
    private String toUserId;
    @SerializedName("fromCurrencySymbol")
    @Expose
    private String fromCurrencySymbol;
    @SerializedName("fromCurrency")
    @Expose
    private String fromCurrency;
    @SerializedName("fromUserId")
    @Expose
    private String fromUserId;
    @SerializedName(value = "transactionId", alternate = "transactionid")
    @Expose
    private String transactionId;

    public String getToCurrencySymbol() {
        return toCurrencySymbol;
    }

    public void setToCurrencySymbol(String toCurrencySymbol) {
        this.toCurrencySymbol = toCurrencySymbol;
    }

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getFromCurrencySymbol() {
        return fromCurrencySymbol;
    }

    public void setFromCurrencySymbol(String fromCurrencySymbol) {
        this.fromCurrencySymbol = fromCurrencySymbol;
    }

    public String getFromCurrency() {
        return fromCurrency;
    }

    public void setFromCurrency(String fromCurrency) {
        this.fromCurrency = fromCurrency;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getReceiverUserName() {
        return receiverUserName;
    }

    public void setReceiverUserName(String receiverUserName) {
        this.receiverUserName = receiverUserName;
    }

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }

    public String getFromAmount() {
        return fromAmount;
    }

    public void setFromAmount(String fromAmount) {
        this.fromAmount = fromAmount;
    }

    public String getToAmount() {
        return toAmount;
    }

    public void setToAmount(String toAmount) {
        this.toAmount = toAmount;
    }

    public String getTransferCommission() {
        return transferCommission;
    }

    public void setTransferCommission(String transferCommission) {
        this.transferCommission = transferCommission;
    }

    public String getTxn_fee() {
        return txn_fee;
    }

    public void setTxn_fee(String txn_fee) {
        this.txn_fee = txn_fee;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getRechargemode() {
        return rechargemode;
    }

    public void setRechargemode(String rechargemode) {
        this.rechargemode = rechargemode;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSenderImage() {
        return senderImage;
    }

    public void setSenderImage(String senderImage) {
        this.senderImage = senderImage;
    }

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getTxnTimeStamp() {
        return txnTimeStamp;
    }

    public void setTxnTimeStamp(String txnTimeStamp) {
        this.txnTimeStamp = txnTimeStamp;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getClosingbal() {
        return closingbal;
    }

    public void setClosingbal(String closingbal) {
        this.closingbal = closingbal;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void setInitiatedBy(String initiatedBy) {
        this.initiatedBy = initiatedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public Integer getTxnType() {
        return txnType;
    }

    public void setTxnType(Integer txnType) {
        this.txnType = txnType;
    }

    public String getTxnTypeText() {
        return txnTypeText;
    }

    public void setTxnTypeText(String txnTypeText) {
        this.txnTypeText = txnTypeText;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
