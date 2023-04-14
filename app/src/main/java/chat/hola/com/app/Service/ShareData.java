package chat.hola.com.app.Service;

import java.io.Serializable;

/**
 * Created by ankit on 19/4/18.
 */

public class ShareData implements Serializable {

    private String caption;

    private String mediaPath;

    private String type;

    private boolean facebookShare;

    private boolean twitterShare;

    public ShareData() {
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setFacebookShare(boolean facebookShare) {
        this.facebookShare = facebookShare;
    }

    public void setTwitterShare(boolean twitterShare) {
        this.twitterShare = twitterShare;
    }

    public String getCaption() {
        return caption;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public String getType() {
        return type;
    }

    public boolean isFacebookShare() {
        return facebookShare;
    }

    public boolean isTwitterShare() {
        return twitterShare;
    }
}
