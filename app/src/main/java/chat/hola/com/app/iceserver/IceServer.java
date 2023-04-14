package chat.hola.com.app.iceserver;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class IceServer {

    @SerializedName("urls")
    @Expose
    private List<String> urls = null;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("credential")
    @Expose
    private String credential;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

}
