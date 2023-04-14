package chat.hola.com.app.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TransferResponse implements Serializable {
    //    {"message":"Success.","data":{"fromTxnId":"313c9eda-d523-40a3-9ace-c1cd72d7afb3","fromTxnTime":1583405224346}}
    @SerializedName("message")
    String message;

    @SerializedName("data")
    Data data;

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
        @SerializedName(value = "transactionId",alternate = "fromTxnId")
        String transactionId;

        @SerializedName(value = "txntimestamp",alternate = "fromTxnTime")
        String transactionTime;

        @SerializedName("toAmount")
        String toAmount;

        @SerializedName("fromAmount")
        String fromAmount;

        @SerializedName("transferCommission")
        String transferCommission;

        public String getToAmount() {
            return toAmount;
        }

        public void setToAmount(String toAmount) {
            this.toAmount = toAmount;
        }

        public String getFromAmount() {
            return fromAmount;
        }

        public void setFromAmount(String fromAmount) {
            this.fromAmount = fromAmount;
        }

        public String getTransferCommission() {
            return transferCommission;
        }

        public void setTransferCommission(String transferCommission) {
            this.transferCommission = transferCommission;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getTransactionTime() {
            return transactionTime;
        }

        public void setTransactionTime(String transactionTime) {
            this.transactionTime = transactionTime;
        }
    }

}

