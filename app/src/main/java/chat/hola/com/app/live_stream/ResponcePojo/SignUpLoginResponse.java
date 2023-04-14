package chat.hola.com.app.live_stream.ResponcePojo;

import java.io.Serializable;

/**
 * Created by moda on 11/21/2018.
 */
public class SignUpLoginResponse implements Serializable
{
    private String message;
    private SignUpLoginData data;

    public String getMessage() {
        return message;
    }

    public SignUpLoginData getData() {
        return data;
    }
}
