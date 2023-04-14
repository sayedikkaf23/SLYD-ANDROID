package chat.hola.com.app.wallet.transaction.credit;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.models.WalletTransactionData;
import chat.hola.com.app.wallet.transaction.Model.TransactionData;

public interface CreditContract {

    interface View extends BaseView{

        void recyclerViewSetup();

//        void updateList(List<TransactionData> data,boolean isFirst);

        void isDataLoading(boolean b);

        void setData(List<WalletTransactionData> transactionDataList, String pageState, boolean isFirst);

        void noData();
    }

    interface Presenter extends BasePresenter<CreditContract.View>{
        void init();

        void getTransactionList(String walletId, Integer type, boolean isFirst, String pageState);
//        void getTransactionList(int page, int size, int type);

        //        void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);
        void callApiOnScroll(Integer type, boolean isFirst, String pageState, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

    }
}
