package chat.hola.com.app.Utilities;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.ResponseBody;

public class Error {

    public static String getErrorMessage(ResponseBody errorBody) {
        Gson gson = new Gson();
        chat.hola.com.app.models.Error error = null;
        try {
            error = gson.fromJson(errorBody.string(), chat.hola.com.app.models.Error.class);
            return error.getMessage();
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
