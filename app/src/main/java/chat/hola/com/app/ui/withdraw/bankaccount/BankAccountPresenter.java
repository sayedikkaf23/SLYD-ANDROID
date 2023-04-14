package chat.hola.com.app.ui.withdraw.bankaccount;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.StripeResponse;
import chat.hola.com.app.ui.withdraw.model.bankaccount.BankAccountResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


/**
 * <h1>{@link BankAccountPresenter}</h1>
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

public class BankAccountPresenter implements BankAccountContract.Presenter {

    @Inject
    public BankAccountPresenter() {
    }

    BankAccountContract.View view;

    @Inject
    HowdooService service;
    @Inject
    SessionApiCall sessionApiCall;
    @Inject
    SessionManager sessionManager;

    @Override
    public void attachView(BankAccountContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void getBankAccounts() {
        if (view != null) view.showLoader();
        service.getBanks(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                AppController.getInstance().getUserId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<BankAccountResponse>>() {
                    @Override
                    public void onNext(Response<BankAccountResponse> response) {
                        if (view != null) view.hideLoader();

                        switch (response.code()) {
                            case 200:
                                assert response.body() != null;
                                break;
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

    public void getStripeAccount() {
        if (view != null) view.showLoader();
        service.getStripeAccount(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<StripeResponse>>() {
                    @Override
                    public void onNext(Response<StripeResponse> response) {
                        if (view != null) view.hideLoader();

                        switch (response.code()) {
                            case 200:
                                String status = response.body().getData().getIndividual().getVerification().getStatus();
                                if (status.toLowerCase().equals("verified")) {
                                    if (view != null) view.setStatus(status, 1);
                                } else {
                                    if (view != null) view.setStatus("Unverified", 2);
                                }
                                if (view != null)
                                    view.setList(response.body().getData().getId(),response.body().getData().getExternal_accounts().getAccounts(), response.body().getFee());
                                break;
                            case 404:
                                if (view != null)
                                    view.setStatus("Create Account", 0);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null)
                            view.hideLoader();
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) view.hideLoader();
                    }
                });
    }
}
