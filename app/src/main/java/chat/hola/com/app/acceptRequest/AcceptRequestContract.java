package chat.hola.com.app.acceptRequest;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;

/**
 * <h1>AddContactContract</h1>
 *
 * @author 3Embed
 * @since 24/2/2018.
 */

public interface AcceptRequestContract {

    interface View extends BaseView {

        void finishActivity();

        void enable(boolean enable);
    }

    interface Presenter extends BasePresenter<View> {
        void accept(String targetUserId);

        void reject(String targetUserId);

        void block(String targetUserId);

        void send(String targetUserId, String message);
    }
}
