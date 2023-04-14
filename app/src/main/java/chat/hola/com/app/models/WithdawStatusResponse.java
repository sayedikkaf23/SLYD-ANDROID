package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WithdawStatusResponse implements Serializable {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("data")
    @Expose
    Data data;

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
        @SerializedName("history")
        @Expose
        List<WithdawStatus> withdawStatuses;

        public List<WithdawStatus> getWithdawStatuses() {
            return withdawStatuses;
        }

        public void setWithdawStatuses(List<WithdawStatus> withdawStatuses) {
            this.withdawStatuses = withdawStatuses;
        }
    }
}
