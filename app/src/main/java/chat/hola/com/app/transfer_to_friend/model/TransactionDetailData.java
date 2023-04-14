package chat.hola.com.app.transfer_to_friend.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransactionDetailData implements Serializable {

    @SerializedName("messageId")
    @Expose
    private String messageId;
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    @SerializedName("txnId")
    @Expose
    private String txnId;
    @SerializedName("transferStatus")
    @Expose
    private Integer transferStatus;
    @SerializedName("messageCreatedDate")
    @Expose
    private Double messageCreatedDate;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("amount")
    @Expose
    private Double amount;

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public Integer getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(Integer transferStatus) {
        this.transferStatus = transferStatus;
    }

    public Double getMessageCreatedDate() {
        return messageCreatedDate;
    }

    public void setMessageCreatedDate(Double messageCreatedDate) {
        this.messageCreatedDate = messageCreatedDate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
