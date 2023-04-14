package chat.hola.com.app.ui.withdraw.model.bankaccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Data implements Serializable {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("userId")
    @Expose
    private String userId;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("bankData")
    @Expose
    private BankData bankData;
    @SerializedName("userTypeCode")
    @Expose
    private Integer userTypeCode;
    @SerializedName("userTypeString")
    @Expose
    private String userTypeString;

    private Boolean isSelected = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BankData getBankData() {
        return bankData;
    }

    public void setBankData(BankData bankData) {
        this.bankData = bankData;
    }

    public Integer getUserTypeCode() {
        return userTypeCode;
    }

    public void setUserTypeCode(Integer userTypeCode) {
        this.userTypeCode = userTypeCode;
    }

    public String getUserTypeString() {
        return userTypeString;
    }

    public void setUserTypeString(String userTypeString) {
        this.userTypeString = userTypeString;
    }

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}