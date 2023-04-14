package chat.hola.com.app.ui.withdraw.model.bankaccount;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BankData implements Serializable {

    @SerializedName("Bank Code")
    @Expose
    private String bankCode;
    @SerializedName("Account Number")
    @Expose
    private String accountNumber;

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

}