package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WithdrawSuccess implements Serializable {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }


    public class Data implements Serializable {

        @SerializedName("txnId")
        @Expose
        private String txnId;
        @SerializedName("createdAt")
        @Expose
        private String createdAt;
        @SerializedName("accountNumber")
        @Expose
        private String accountNumber;
        @SerializedName("withdrawAmount")
        @Expose
        private String withdrawalAmount;
        @SerializedName("fee")
        @Expose
        private String fee;
        @SerializedName("bank")
        @Expose
        private String bank;

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getTxnId() {
            return txnId;
        }

        public void setTxnId(String txnId) {
            this.txnId = txnId;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getAccountNumber() {
            return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        public String getWithdrawalAmount() {
            return withdrawalAmount;
        }

        public void setWithdrawalAmount(String withdrawalAmount) {
            this.withdrawalAmount = withdrawalAmount;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }
    }
}
