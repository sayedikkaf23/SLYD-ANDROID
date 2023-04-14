package chat.hola.com.app.wallet.wallet_detail;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.wallet.wallet_detail.model.WalletBalanceData;

public interface WalletContract {

    interface View extends BaseView{

        void showBalance(WalletBalanceData data);
    }

    interface Presenter extends BasePresenter<WalletContract.View>{

        void getWalletBalance();
    }

}
