package chat.hola.com.app.ui.withdraw.method;

import java.util.List;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;
import chat.hola.com.app.models.WithdrawMethod;

public interface WithdrawMethodContract {
    interface View extends BaseView {

        void methods(List<WithdrawMethod> cards);

        /**
         * <h1>To load balance in view</h1>
         *
         * @param : currencySymbol
         */
        void loadBalance(String currencySymbol);
    }

    interface Presenter extends BasePresenter<View> {
        void methods(String currencyCountryCode);

        /**
         * <h1>This method used fetch current wallet balance</h1>
         */
        void walletBalance();
    }
}
