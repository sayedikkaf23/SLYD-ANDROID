package chat.hola.com.app.profileScreen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Wallet implements Serializable {

    @SerializedName("tipsAmount")
    @Expose
    private Double tipsAmount;
    @SerializedName("buyPostAmount")
    @Expose
    private Double buyPostAmount;
    @SerializedName("totalAmount")
    @Expose
    private Double totalAmount;
    @SerializedName("giftAmount")
    @Expose
    private Double giftAmount;

    public Double getTipsAmount() {
        return tipsAmount;
    }

    public void setTipsAmount(Double tipsAmount) {
        this.tipsAmount = tipsAmount;
    }

    public Double getBuyPostAmount() {
        return buyPostAmount;
    }

    public void setBuyPostAmount(Double buyPostAmount) {
        this.buyPostAmount = buyPostAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getGiftAmount() {
        return giftAmount;
    }

    public void setGiftAmount(Double giftAmount) {
        this.giftAmount = giftAmount;
    }
}
