package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GuestTokenResponse {

@SerializedName("message")
@Expose
private String message;
@SerializedName("data")
@Expose
private GuestTokenData data;

public String getMessage() {
return message;
}

public void setMessage(String message) {
this.message = message;
}

public GuestTokenData getData() {
return data;
}

public void setData(GuestTokenData data) {
this.data = data;
}

}
