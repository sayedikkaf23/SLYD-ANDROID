package chat.hola.com.app.AddContact;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.contact.Friend;

/**
 * <h1>AddContactContract</h1>
 *
 * @author 3Embed
 * @since 24/2/2018.
 */

public interface AddContactContract {

    interface View extends BaseView {

        void finishActivity();

        void showData(List<Friend> data);

        void noData();
    }

    interface Presenter extends BasePresenter<View> {

    }
}
