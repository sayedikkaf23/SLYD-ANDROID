package chat.hola.com.app.post.model;

/**
 * Created by DELL on 3/26/2018.
 */

public class UploadHolder {
    private String Id;
    private boolean isSuccess;
    private String msg;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
