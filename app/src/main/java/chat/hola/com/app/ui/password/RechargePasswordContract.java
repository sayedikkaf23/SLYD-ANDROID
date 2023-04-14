package chat.hola.com.app.ui.password;

import java.util.Map;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;
import chat.hola.com.app.models.TransferResponse;
import chat.hola.com.app.models.WithdrawSuccess;

/**
 * <h1>RechargePasswordContract</h1>
 * <p>contains presenter and view interfaces of {@link RechargePasswordActivity}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 17 Dec 2019
 */
public interface RechargePasswordContract {

    interface View extends BaseView {
        void transferred(TransferResponse.Data data);

        void passwordMatched();

        void withdrawSuccess(WithdrawSuccess.Data data);

        void startTimer();
    }

    interface Presenter extends BasePresenter<View> {

        void checkPassword(String password);

        void transferTo(String receiverUid, String receiverUserName, String reason, String amount);

        void withdraw(Map<String,Object> map);

        void requestOtp();

        void verifyOtp(String verificationCode);
    }
}
