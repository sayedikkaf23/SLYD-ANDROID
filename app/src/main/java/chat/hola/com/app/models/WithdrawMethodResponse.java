package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WithdrawMethodResponse implements Serializable {
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {
        @SerializedName("data")
        @Expose
        private List<WithdrawMethod> withdrawMethods;

        public List<WithdrawMethod> getWithdrawMethods() {
            return withdrawMethods;
        }

        public void setWithdrawMethods(List<WithdrawMethod> withdrawMethods) {
            this.withdrawMethods = withdrawMethods;
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
