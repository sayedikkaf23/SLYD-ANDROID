package chat.hola.com.app.ui.withdraw.method;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.WalletResponse;
import chat.hola.com.app.models.WithdrawMethodResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class WithdrawMethodPresenter implements WithdrawMethodContract.Presenter {

    private WithdrawMethodContract.View mView;

    @Inject
    SessionManager sessionManager;
    @Inject
    HowdooService service;

    @Inject
    WithdrawMethodPresenter() {
    }

    @Override
    public void attach(WithdrawMethodContract.View view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }

    @Override
    public void methods(String currencyCountryCode) {
        mView.showLoader();
        service.paymentMethods(AppController.getInstance().getApiToken(), Constants.LANGUAGE, currencyCountryCode, 0, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<WithdrawMethodResponse>>() {
                    @Override
                    public void onNext(Response<WithdrawMethodResponse> response) {
                        mView.hideLoader();
                        if (response.code() == 200)
                            mView.methods(response.body().getData().getWithdrawMethods());
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoader();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void walletBalance() {
        mView.showLoader();
        service.walletBalance(AppController.getInstance().getApiToken(), Constants.LANGUAGE, AppController.getInstance().getUserId(), Constants.APP_TYPE)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<WalletResponse>>() {
                    @Override
                    public void onNext(Response<WalletResponse> response) {
                        mView.hideLoader();
                        if (response.code() == 200) {
                            try {
                                assert response.body() != null;
                                for (WalletResponse.Data.Wallet wallet : response.body().getData().getWalletData()) {
                                    if (wallet.getCurrency().equals(sessionManager.getCurrency())) {
                                        mView.loadBalance(wallet.getCurrencySymbol());
                                        sessionManager.setWalletId(wallet.getWalletid());
                                        sessionManager.setWalletBalance(wallet.getBalance());
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.hideLoader();
                    }

                    @Override
                    public void onComplete() {
                        mView.hideLoader();
                    }
                });
    }
}
