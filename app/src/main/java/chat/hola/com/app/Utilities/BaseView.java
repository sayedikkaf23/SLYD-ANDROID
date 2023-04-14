package chat.hola.com.app.Utilities;

import chat.hola.com.app.models.InternetErrorView;

/**
 * Created by ankit on 21/2/18.
 */

public interface BaseView extends InternetErrorView.ErrorListner {

    void showMessage(String msg, int msgId);

    void sessionExpired();

    void isInternetAvailable(boolean flag);

    void userBlocked();
}
