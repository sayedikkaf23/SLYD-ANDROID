package chat.hola.com.app.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 19 July 2019
 */
public class LiveStream implements Serializable {
    @SerializedName("id")
    @Expose(serialize = false, deserialize = true)
    String id;

    @SerializedName("email")
    @Expose(serialize = false, deserialize = true)
    String email;

    @SerializedName("firstName")
    @Expose(serialize = false, deserialize = true)
    String firstName;

    @SerializedName("lastName")
    @Expose(serialize = false, deserialize = true)
    String lastName;

    @SerializedName("fcmTopic")
    @Expose(serialize = false, deserialize = true)
    String fcmTopic;

    @SerializedName("mqttTopic")
    @Expose(serialize = false, deserialize = true)
    String mqttTopic;

    @SerializedName("authToken")
    @Expose(serialize = false, deserialize = true)
    String authToken;

    @SerializedName("phone")
    @Expose(serialize = false, deserialize = true)
    Phone phone;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFcmTopic() {
        return fcmTopic;
    }

    public void setFcmTopic(String fcmTopic) {
        this.fcmTopic = fcmTopic;
    }

    public String getMqttTopic() {
        return mqttTopic;
    }

    public void setMqttTopic(String mqttTopic) {
        this.mqttTopic = mqttTopic;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Phone getPhone() {
        return phone;
    }

    public void setPhone(Phone phone) {
        this.phone = phone;
    }
}
