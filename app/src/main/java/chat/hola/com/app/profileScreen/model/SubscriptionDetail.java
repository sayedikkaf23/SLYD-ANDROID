package chat.hola.com.app.profileScreen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubscriptionDetail implements Serializable {

    @SerializedName("amount")
    @Expose
    private Double amount;
    @SerializedName("create")
    @Expose
    private String create;
    @SerializedName("appWillGet")
    @Expose
    private Double appWillGet;
    @SerializedName("userWillGet")
    @Expose
    private Double userWillGet;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public Double getAppWillGet() {
        return appWillGet;
    }

    public void setAppWillGet(Double appWillGet) {
        this.appWillGet = appWillGet;
    }

    public Double getUserWillGet() {
        return userWillGet;
    }

    public void setUserWillGet(Double userWillGet) {
        this.userWillGet = userWillGet;
    }

}
