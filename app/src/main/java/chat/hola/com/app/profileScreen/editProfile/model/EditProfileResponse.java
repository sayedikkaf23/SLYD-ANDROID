package chat.hola.com.app.profileScreen.editProfile.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ankit on 19/3/18.
 */

public class EditProfileResponse {

    @SerializedName("code")
    @Expose
    String code;

    @SerializedName("message")
    @Expose
    String message;

    public void setCode(String code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
