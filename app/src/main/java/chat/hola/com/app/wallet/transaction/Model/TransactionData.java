package chat.hola.com.app.wallet.transaction.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransactionData implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("txnId")
    @Expose
    private String txnId;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("txnType")
    @Expose
    private String txnType;
    @SerializedName("txnTypeCode")
    @Expose
    private Long txnTypeCode;
    @SerializedName("trigger")
    @Expose
    private String trigger;
    @SerializedName("currency")
    @Expose
    private String currency;
    @SerializedName("currencySymbol")
    @Expose
    private String currencySymbol;
    @SerializedName("coinType")
    @Expose
    private String coinType;
    @SerializedName("coinOpeingBalance")
    @Expose
    private Double coinOpeingBalance;
    @SerializedName("cost")
    @Expose
    private Double cost;
    @SerializedName("coinAmount")
    @Expose
    private Double coinAmount;
    @SerializedName("coinClosingBalance")
    @Expose
    private Double coinClosingBalance;
    @SerializedName("paymentType")
    @Expose
    private String paymentType;
    @SerializedName("timestamp")
    @Expose
    private Long timestamp;
    @SerializedName("paymentTxnId")
    @Expose
    private String paymentTxnId;
    @SerializedName("initatedBy")
    @Expose
    private String initatedBy;
    @SerializedName("transctionTime")
    @Expose
    private Long transctionTime;
    @SerializedName("transctionDate")
    @Expose
    private Long transctionDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public Long getTxnTypeCode() {
        return txnTypeCode;
    }

    public void setTxnTypeCode(Long txnTypeCode) {
        this.txnTypeCode = txnTypeCode;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCoinType() {
        return coinType;
    }

    public void setCoinType(String coinType) {
        this.coinType = coinType;
    }

    public Double getCoinOpeingBalance() {
        return coinOpeingBalance;
    }

    public void setCoinOpeingBalance(Double coinOpeingBalance) {
        this.coinOpeingBalance = coinOpeingBalance;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(Double coinAmount) {
        this.coinAmount = coinAmount;
    }

    public Double getCoinClosingBalance() {
        return coinClosingBalance;
    }

    public void setCoinClosingBalance(Double coinClosingBalance) {
        this.coinClosingBalance = coinClosingBalance;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPaymentTxnId() {
        return paymentTxnId;
    }

    public void setPaymentTxnId(String paymentTxnId) {
        this.paymentTxnId = paymentTxnId;
    }

    public String getInitatedBy() {
        return initatedBy;
    }

    public void setInitatedBy(String initatedBy) {
        this.initatedBy = initatedBy;
    }

    public Long getTransctionTime() {
        return transctionTime;
    }

    public void setTransctionTime(Long transctionTime) {
        this.transctionTime = transctionTime;
    }

    public Long getTransctionDate() {
        return transctionDate;
    }

    public void setTransctionDate(Long transctionDate) {
        this.transctionDate = transctionDate;
    }
}
