package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class WithdrawAmountCheckResponse implements Serializable {
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

        @SerializedName("appCommission")
        @Expose
        private String appCommission;
        @SerializedName("withdrawAmount")
        @Expose
        private String withdrawAmount;
        @SerializedName("totalAmount")
        @Expose
        private String totalAmount;
        @SerializedName("withdrawChargeInPer")
        @Expose
        private String withdrawChargeInPer;

        public String getAppCommission() {
            return appCommission;
        }

        public void setAppCommission(String appCommission) {
            this.appCommission = appCommission;
        }

        public String getWithdrawAmount() {
            return withdrawAmount;
        }

        public void setWithdrawAmount(String withdrawAmount) {
            this.withdrawAmount = withdrawAmount;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getWithdrawChargeInPer() {
            return withdrawChargeInPer;
        }

        public void setWithdrawChargeInPer(String withdrawChargeInPer) {
            this.withdrawChargeInPer = withdrawChargeInPer;
        }

    }
}
