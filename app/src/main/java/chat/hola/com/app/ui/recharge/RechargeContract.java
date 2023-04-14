package chat.hola.com.app.ui.recharge;

import java.util.List;
import java.util.Map;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;
import chat.hola.com.app.models.RechargeResponse;
import chat.hola.com.app.models.TransferResponse;
import chat.hola.com.app.models.WithdrawSuccess;
import chat.hola.com.app.profileScreen.model.Data;
import retrofit2.Response;

/**
 * <h1>RechargeContract</h1>
 * <p>contains presenter and view interfaces of {@link RechargeActivity}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 17 Dec 2019
 */
public interface RechargeContract {

    interface View extends BaseView {
        void transfered(Response<TransferResponse> response);

        void recharged(RechargeResponse.Recharge data);

        void setSuggestedAmount(String currency_symbol, List<String> amounts);

        void withdrawSuccess(WithdrawSuccess.Data data);

        void withdraw(String amount, String currency);

        void recharge(String pgLinkId);
    }

    interface Presenter extends BasePresenter<View> {

        void suggestedAmount();

        void withdraw(Map<String, Object> map);

        void withdrawalAmount(String amount, boolean isForWithdraw);

        void methods(String currencyCountryCode);

        void validateTransfer(Data user, String amount,String toCurrency);
    }
}
