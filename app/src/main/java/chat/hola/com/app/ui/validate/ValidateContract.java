package chat.hola.com.app.ui.validate;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;
import chat.hola.com.app.ui.kyc.KycActivity;

/**
 * <h1>KycContract</h1>
 * <p>contains presenter and view interfaces of {@link KycActivity}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 Dec 2019
 */
public interface ValidateContract {

    interface View extends BaseView {
        void moveNext();

        void showUI(String message);
    }

    interface Presenter extends BasePresenter<View> {

        void checkKyc();
    }
}
