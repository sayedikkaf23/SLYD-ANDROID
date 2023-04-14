package chat.hola.com.app.ui.paymentgateway;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.PaymentGatewayResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * <h1>RechargePresenter</h1>
 *
 * <p>This is implemented presenter of {@link PaymentGatewaysActivity},
 * it will call apis and handles apis responses and
 * communicate between models and {@link  PaymentGatewaysActivity}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 17 Dec 2019
 */
public class PaymentGatewaysPresenter implements PaymentGatewaysContract.Presenter {

    private PaymentGatewaysContract.View view;

    @Inject
    HowdooService service;
    @Inject
    SessionManager sessionManager;

    @Inject
    public PaymentGatewaysPresenter() {

    }

    @Override
    public void attach(PaymentGatewaysContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void getData() {
        view.showLoader();
        service.paymentGateways(AppController.getInstance().getApiToken(), Constants.LANGUAGE, Constants.CURRENCY_CODE, "1", 20, 0)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<PaymentGatewayResponse>>() {
                    @Override
                    public void onNext(Response<PaymentGatewayResponse> response) {
                        view.hideLoader();
                        if (response.code() == 200) {
                            view.setData(response.body().getPaymentGateways());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoader();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
