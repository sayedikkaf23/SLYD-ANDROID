package chat.hola.com.app.wallet.transaction;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.wallet.wallet_detail.model.WalletBalanceData;

public interface TransactionContract {

    interface View extends BaseView{

        void showBalance(WalletBalanceData data);

        void updateData();
    }

    interface Presenter extends BasePresenter<TransactionContract.View>{

        void getWalletBalance();

        void getTransactionList();
    }

}
