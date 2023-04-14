package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RechargeResponse implements Serializable {
    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("data")
    @Expose
    Recharge data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Recharge getData() {
        return data;
    }

    public void setData(Recharge data) {
        this.data = data;
    }

    public class Recharge implements Serializable {
        @SerializedName("amount")
        @Expose
        String amount;
        @SerializedName("txnId")
        @Expose
        String txnId;
        @SerializedName("rechargeMode")
        @Expose
        String rechargeMode;
        @SerializedName("bank")
        @Expose
        String bank;
        @SerializedName(value = "timeStamp",alternate = "txnTimestamp")
        @Expose
        String timeStamp;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getTxnId() {
            return txnId;
        }

        public void setTxnId(String txnId) {
            this.txnId = txnId;
        }

        public String getRechargeMode() {
            return rechargeMode;
        }

        public void setRechargeMode(String rechargeMode) {
            this.rechargeMode = rechargeMode;
        }

        public String getBank() {
            return bank;
        }

        public void setBank(String bank) {
            this.bank = bank;
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }
    }
}
