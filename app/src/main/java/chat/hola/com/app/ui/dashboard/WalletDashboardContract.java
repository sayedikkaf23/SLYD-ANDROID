package chat.hola.com.app.ui.dashboard;

import java.util.List;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;
import chat.hola.com.app.models.WalletTransactionData;

/**
 * <h1>WalletDashboardContract</h1>
 * <p>contains presenter and view interfaces of {@link WalletDashboardActivity}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 05 Dec 2019
 */
public interface WalletDashboardContract {

    interface View extends BaseView {
        void loadBalance(String currency);

        void setData(List<WalletTransactionData> transactionDataList, String pageState, boolean isFirst);

        void moveNext(Integer verificationStatus);

        void noData();
    }

    interface Presenter extends BasePresenter<View> {
        void walletBalance();

        void transactions(String walletId, Integer type, boolean isFirst, String pageState);

        void callApiOnScroll(Integer type, boolean isFirst, String pageState, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        void kycVerification();
    }
}
