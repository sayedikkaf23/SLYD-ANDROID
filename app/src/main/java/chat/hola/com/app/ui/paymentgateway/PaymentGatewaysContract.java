package chat.hola.com.app.ui.paymentgateway;

import java.util.List;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;
import chat.hola.com.app.models.PaymentGateway;
import chat.hola.com.app.ui.recharge.RechargeActivity;

/**
 * <h1>RechargeContract</h1>
 * <p>contains presenter and view interfaces of {@link RechargeActivity}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 17 Dec 2019
 */
public interface PaymentGatewaysContract {

    interface View extends BaseView {
        void setData(List<PaymentGateway> paymentGateways);
    }

    interface Presenter extends BasePresenter<View> {
        void getData();
    }
}
