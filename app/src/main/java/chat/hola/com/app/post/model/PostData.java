package chat.hola.com.app.post.model;

/**
 * Created by DELL on 3/26/2018.
 */

public class PostData {
    private String id;
    private String userId;
    private String data;
    private int status;
    private boolean fbShare = false;
    private boolean twitterShare = false;
    private boolean instaShare =false;

    private boolean merged;

    public boolean isMerged() {
        return merged;
    }

    public void setMerged(boolean merged) {
        this.merged = merged;
    }

    public void setFbShare(boolean fbShare) {
        this.fbShare = fbShare;
    }

    public void setTwitterShare(boolean twitterShare) {
        this.twitterShare = twitterShare;
    }

    public boolean isFbShare() {
        return fbShare;
    }

    public boolean isTwitterShare() {
        return twitterShare;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isInstaShare() {
        return instaShare;
    }

    public void setInstaShare(boolean instaShare) {
        this.instaShare = instaShare;
    }
}
