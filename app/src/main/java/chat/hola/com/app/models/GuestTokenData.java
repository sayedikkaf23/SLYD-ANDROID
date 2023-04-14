package chat.hola.com.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GuestTokenData {

@SerializedName("accessExpireAt")
@Expose
private Long accessExpireAt;
@SerializedName("accessToken")
@Expose
private String accessToken;
@SerializedName("refreshToken")
@Expose
private String refreshToken;

public Long getAccessExpireAt() {
return accessExpireAt;
}

public void setAccessExpireAt(Long accessExpireAt) {
this.accessExpireAt = accessExpireAt;
}

public String getAccessToken() {
return accessToken;
}

public void setAccessToken(String accessToken) {
this.accessToken = accessToken;
}

public String getRefreshToken() {
return refreshToken;
}

public void setRefreshToken(String refreshToken) {
this.refreshToken = refreshToken;
}

}
