package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Error implements Serializable {

    @SerializedName(value = "statusCode", alternate = "status")
    @Expose
    int statusCode;

    @SerializedName("following")
    @Expose
    boolean following;

    @SerializedName("isPrivate")
    @Expose
    boolean isPrivate;

    @SerializedName("message")
    @Expose
    String message;

    @SerializedName("verificationStatus")
    @Expose
    Integer verificationStatus;

    @SerializedName("data")
    @Expose
    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public Integer getVerificationStatus() {
        return verificationStatus;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class Data implements Serializable {
        @SerializedName("FinalAmount")
        @Expose
        String finalAmount;

        @SerializedName("TotalAmount")
        @Expose
        String totalAmount;

        @SerializedName("currency_convertion_change")
        @Expose
        String currencyConversationChange;

        @SerializedName("AppCommission")
        @Expose
        String appCommission;
        @SerializedName("otpId")
        @Expose
        String otpId;

        @SerializedName("otpExpiryTime")
        @Expose
        String otpExpiryTime;

        public String getOtpId() {
            return otpId;
        }

        public void setOtpId(String otpId) {
            this.otpId = otpId;
        }

        public String getOtpExpiryTime() {
            return otpExpiryTime;
        }

        public void setOtpExpiryTime(String otpExpiryTime) {
            this.otpExpiryTime = otpExpiryTime;
        }
        public String getFinalAmount() {
            return finalAmount;
        }

        public void setFinalAmount(String finalAmount) {
            this.finalAmount = finalAmount;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getCurrencyConversationChange() {
            return currencyConversationChange;
        }

        public void setCurrencyConversationChange(String currencyConversationChange) {
            this.currencyConversationChange = currencyConversationChange;
        }

        public String getAppCommission() {
            return appCommission;
        }

        public void setAppCommission(String appCommission) {
            this.appCommission = appCommission;
        }
    }
}
