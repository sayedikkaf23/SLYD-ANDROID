package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StripeResponse implements Serializable {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    Data data;
    @SerializedName("fee")
    @Expose
    String fee;

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

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
        @SerializedName("id")
        @Expose
        String id;

        @SerializedName("external_accounts")
        @Expose
        ExternalAccounts external_accounts;

        @SerializedName("individual")
        @Expose
        Individual individual;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public ExternalAccounts getExternal_accounts() {
            return external_accounts;
        }

        public void setExternal_accounts(ExternalAccounts external_accounts) {
            this.external_accounts = external_accounts;
        }

        public Individual getIndividual() {
            return individual;
        }

        public void setIndividual(Individual individual) {
            this.individual = individual;
        }

        public class Individual implements Serializable {
            @SerializedName("verification")
            @Expose
            Verification verification;

            public Verification getVerification() {
                return verification;
            }

            public void setVerification(Verification verification) {
                this.verification = verification;
            }

            public class Verification implements Serializable {
                @SerializedName("status")
                @Expose
                String status;

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }
            }
        }

        public class ExternalAccounts implements Serializable {
            @SerializedName("data")
            @Expose
            List<Account> accounts;

            public List<Account> getAccounts() {
                return accounts;
            }

            public void setAccounts(List<Account> accounts) {
                this.accounts = accounts;
            }

            public class Account implements Serializable {
                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("object")
                @Expose
                private String object;
                @SerializedName("account")
                @Expose
                private String account;
                @SerializedName("account_holder_name")
                @Expose
                private String accountHolderName;
                @SerializedName("account_holder_type")
                @Expose
                private String accountHolderType;
                @SerializedName("bank_name")
                @Expose
                private String bankName;
                @SerializedName("country")
                @Expose
                private String country;
                @SerializedName("currency")
                @Expose
                private String currency;
                @SerializedName("default_for_currency")
                @Expose
                private Boolean defaultForCurrency;
                @SerializedName("fingerprint")
                @Expose
                private String fingerprint;
                @SerializedName("last4")
                @Expose
                private String last4;
                @SerializedName("routing_number")
                @Expose
                private String routingNumber;
                @SerializedName("status")
                @Expose
                private String status;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getObject() {
                    return object;
                }

                public void setObject(String object) {
                    this.object = object;
                }

                public String getAccount() {
                    return account;
                }

                public void setAccount(String account) {
                    this.account = account;
                }

                public String getAccountHolderName() {
                    return accountHolderName;
                }

                public void setAccountHolderName(String accountHolderName) {
                    this.accountHolderName = accountHolderName;
                }

                public String getAccountHolderType() {
                    return accountHolderType;
                }

                public void setAccountHolderType(String accountHolderType) {
                    this.accountHolderType = accountHolderType;
                }

                public String getBankName() {
                    return bankName;
                }

                public void setBankName(String bankName) {
                    this.bankName = bankName;
                }

                public String getCountry() {
                    return country;
                }

                public void setCountry(String country) {
                    this.country = country;
                }

                public String getCurrency() {
                    return currency;
                }

                public void setCurrency(String currency) {
                    this.currency = currency;
                }

                public Boolean getDefaultForCurrency() {
                    return defaultForCurrency;
                }

                public void setDefaultForCurrency(Boolean defaultForCurrency) {
                    this.defaultForCurrency = defaultForCurrency;
                }

                public String getFingerprint() {
                    return fingerprint;
                }

                public void setFingerprint(String fingerprint) {
                    this.fingerprint = fingerprint;
                }

                public String getLast4() {
                    return last4;
                }

                public void setLast4(String last4) {
                    this.last4 = last4;
                }

                public String getRoutingNumber() {
                    return routingNumber;
                }

                public void setRoutingNumber(String routingNumber) {
                    this.routingNumber = routingNumber;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }
            }
        }
    }
}
