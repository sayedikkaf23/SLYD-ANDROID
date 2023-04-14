package chat.hola.com.app.profileScreen.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ankit on 2/4/18.
 */

public class Email implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("verified")
    @Expose
    private Integer verified;

    @SerializedName("verificationToken")
    @Expose
    private String verificationToken;

    @SerializedName("isVisible")
    @Expose
    private Integer isVisible;

    public String getId() {
        return id;
    }

    public Integer getVerified() {
        return verified;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setVerified(Integer verified) {
        this.verified = verified;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public Integer getVisible() {
        return isVisible;
    }

    public void setVisible(Integer visible) {
        isVisible = visible;
    }
}
