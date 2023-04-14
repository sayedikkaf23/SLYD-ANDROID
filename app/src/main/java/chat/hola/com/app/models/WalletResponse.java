package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <h1>WalletResponse</h1>
 * <p></p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 05 Feb 2020
 */
public class WalletResponse implements Serializable {
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
        @SerializedName("coinWallet")
        @Expose
        private Wallet coinWallet;
        @SerializedName("walletData")
        @Expose
        private List<Wallet> walletData = null;
        @SerializedName("walletEarningData")
        @Expose
        private List<WalletEarning> walletEarningData = null;

        public Wallet getCoinWallet() {
            return coinWallet;
        }

        public void setCoinWallet(Wallet coinWallet) {
            this.coinWallet = coinWallet;
        }

        public List<Wallet> getWalletData() {
            return walletData;
        }

        public void setWalletData(List<Wallet> walletData) {
            this.walletData = walletData;
        }

        public List<WalletEarning> getWalletEarningData() {
            return walletEarningData;
        }

        public void setWalletEarningData(List<WalletEarning> walletEarningData) {
            this.walletEarningData = walletEarningData;
        }

        public class Wallet implements Serializable {
            @SerializedName("userid")
            @Expose
            private String userid;
            @SerializedName("usertype")
            @Expose
            private String usertype;
            @SerializedName("currency")
            @Expose
            private String currency;
            @SerializedName("createddate")
            @Expose
            private String createddate;
            @SerializedName("balance")
            @Expose
            private String balance;
            @SerializedName("status")
            @Expose
            private Integer status;
            @SerializedName("statustext")
            @Expose
            private String statustext;
            @SerializedName("walletid")
            @Expose
            private String walletid;
            @SerializedName("currency_symbol")
            @Expose
            private String currencySymbol = "";

            public String getCurrencySymbol() {
                return currencySymbol;
            }

            public void setCurrencySymbol(String currencySymbol) {
                this.currencySymbol = currencySymbol;
            }

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getUsertype() {
                return usertype;
            }

            public void setUsertype(String usertype) {
                this.usertype = usertype;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getCreateddate() {
                return createddate;
            }

            public void setCreateddate(String createddate) {
                this.createddate = createddate;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

            public String getStatustext() {
                return statustext;
            }

            public void setStatustext(String statustext) {
                this.statustext = statustext;
            }

            public String getWalletid() {
                return walletid;
            }

            public void setWalletid(String walletid) {
                this.walletid = walletid;
            }
        }

        public class WalletEarning implements Serializable {
            @SerializedName("userid")
            @Expose
            private String userid;
            @SerializedName("usertype")
            @Expose
            private String usertype;
            @SerializedName("createddate")
            @Expose
            private String createddate;
            @SerializedName("balance")
            @Expose
            private String balance;
            @SerializedName("status")
            @Expose
            private Integer status;
            @SerializedName("statustext")
            @Expose
            private String statustext;
            @SerializedName("walletearningid")
            @Expose
            private String walletearningid;

            public String getUserid() {
                return userid;
            }

            public void setUserid(String userid) {
                this.userid = userid;
            }

            public String getUsertype() {
                return usertype;
            }

            public void setUsertype(String usertype) {
                this.usertype = usertype;
            }

            public String getCreateddate() {
                return createddate;
            }

            public void setCreateddate(String createddate) {
                this.createddate = createddate;
            }

            public String getBalance() {
                return balance;
            }

            public void setBalance(String balance) {
                this.balance = balance;
            }

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

            public String getStatustext() {
                return statustext;
            }

            public void setStatustext(String statustext) {
                this.statustext = statustext;
            }

            public String getWalletearningid() {
                return walletearningid;
            }

            public void setWalletearningid(String walletearningid) {
                this.walletearningid = walletearningid;
            }

        }
    }
}
