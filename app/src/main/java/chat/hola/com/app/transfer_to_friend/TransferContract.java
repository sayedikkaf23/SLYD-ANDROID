package chat.hola.com.app.transfer_to_friend;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.models.Error;

public interface TransferContract {

    interface View extends BaseView {

        void showProgress(boolean b);

        void transferInitiatedSuccess(String transactionId, String id);

        void confirm();

        void showConvertedAmount(Error.Data amount);

        void alert(String message);
    }

    interface Presenter extends BasePresenter<TransferContract.View> {
        void validateTransfer(String userId, String userName, String amount, String toCurrency);

        void transfer(String userId, String userName, String amount, String toCurrency, String note);
    }
}
