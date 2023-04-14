package chat.hola.com.app.ui.withdraw.withdrawallog;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.models.WithdrawLog;

/**
 * <h1>{@link WithdrawalLogContract}</h1>
 * <p>contains presenter and view interfaces of {@link WithdrawalLogActivity}</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 March 2020
 */

public interface WithdrawalLogContract {

    interface View extends BaseView {

        /**
         * <h1>To load balance in view</h1>
         *
         * @param : currencySymbol
         */
        void loadBalance(String currencySymbol);

        /**
         * <h1>To show loading view</h1>
         */
        void showLoader();

        /**
         * <h1>To hide loading view</h1>
         */
        void hideLoader();

        void showWithdrawLogs(List<WithdrawLog.DataResponse.Data> data, String pageState);

        void setStatus(String status, int i);

        void empty();
    }

    interface Presenter extends BasePresenter<View> {
        /**
         * <h1>This method used fetch current wallet balance</h1>
         */
        void walletBalance();

        void withdrawLogs(String pageState);

        void callApiOnScroll(boolean b, String pageState, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);
    }
}
