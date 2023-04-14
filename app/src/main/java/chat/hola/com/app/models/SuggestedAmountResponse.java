package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuggestedAmountResponse implements Serializable {

    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    List<Data> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public class Data implements Serializable {
        @SerializedName("commonType")
        @Expose
        String commonType;
        @SerializedName("country_id")
        @Expose
        String country_id;
        @SerializedName("country_name")
        @Expose
        String country_name;
        @SerializedName("created_at")
        @Expose
        String created_at;
        @SerializedName("currency_code")
        @Expose
        String currency_code;
        @SerializedName("currency_name")
        @Expose
        String currency_name;
        @SerializedName("currency_symbol")
        @Expose
        String currency_symbol;
        @SerializedName("filter_text")
        @Expose
        String filter_text;
        @SerializedName("amount")
        @Expose
        List<String> amount;

        public String getCommonType() {
            return commonType;
        }

        public void setCommonType(String commonType) {
            this.commonType = commonType;
        }

        public String getCountry_id() {
            return country_id;
        }

        public void setCountry_id(String country_id) {
            this.country_id = country_id;
        }

        public String getCountry_name() {
            return country_name;
        }

        public void setCountry_name(String country_name) {
            this.country_name = country_name;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getCurrency_code() {
            return currency_code;
        }

        public void setCurrency_code(String currency_code) {
            this.currency_code = currency_code;
        }

        public String getCurrency_name() {
            return currency_name;
        }

        public void setCurrency_name(String currency_name) {
            this.currency_name = currency_name;
        }

        public String getCurrency_symbol() {
            return currency_symbol;
        }

        public void setCurrency_symbol(String currency_symbol) {
            this.currency_symbol = currency_symbol;
        }

        public String getFilter_text() {
            return filter_text;
        }

        public void setFilter_text(String filter_text) {
            this.filter_text = filter_text;
        }

        public List<String> getAmount() {
            return amount;
        }

        public void setAmount(List<String> amount) {
            this.amount = amount;
        }
    }
}
