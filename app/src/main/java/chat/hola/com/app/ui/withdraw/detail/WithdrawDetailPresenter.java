package chat.hola.com.app.ui.withdraw.detail;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.WithdawStatusResponse;
import chat.hola.com.app.ui.withdraw.bankaccount.BankAccountActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


/**
 * <h1>{@link WithdrawDetailPresenter}</h1>
 *
 * <p>This is implemented presenter of {@link BankAccountActivity},
 * it will call apis and handles apis responses and
 * communicate between models and {@link BankAccountActivity}</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 March 2020
 */

public class WithdrawDetailPresenter implements WithdrawDetailContract.Presenter {

    @Inject
    public WithdrawDetailPresenter() {
    }

    WithdrawDetailContract.View view;

    @Inject
    HowdooService service;
    @Inject
    SessionApiCall sessionApiCall;
    @Inject
    SessionManager sessionManager;

    @Override
    public void attachView(WithdrawDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void withdawStatuses(String withdawId) {
        if (view != null) view.showLoader();
        service.withdawDetail(AppController.getInstance().getApiToken(), Constants.LANGUAGE, withdawId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<WithdawStatusResponse>>() {
                    @Override
                    public void onNext(Response<WithdawStatusResponse> response) {
                        if (view != null) view.hideLoader();

                        if (response.code() == 200) {
                            if (view != null)
                                view.withdawStatuses(response.body().getData().getWithdawStatuses());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null) view.hideLoader();
                    }

                    @Override
                    public void onComplete() {
                        if (view != null)
                            view.hideLoader();
                    }
                });
    }
}
