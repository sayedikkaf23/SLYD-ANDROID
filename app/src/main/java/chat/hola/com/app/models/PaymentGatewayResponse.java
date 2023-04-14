package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * <h1></h1>
 * <p></p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 30 Jan 2020
 */
public class PaymentGatewayResponse implements Serializable {
    @SerializedName("message")
    @Expose
    String message;
    @SerializedName("count")
    @Expose
    Integer count;
    @SerializedName("data")
    @Expose
    List<PaymentGateway> paymentGateways;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<PaymentGateway> getPaymentGateways() {
        return paymentGateways;
    }

    public void setPaymentGateways(List<PaymentGateway> paymentGateways) {
        this.paymentGateways = paymentGateways;
    }
}
