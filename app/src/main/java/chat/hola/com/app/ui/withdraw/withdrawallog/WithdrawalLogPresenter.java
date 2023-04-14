package chat.hola.com.app.ui.withdraw.withdrawallog;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.StripeResponse;
import chat.hola.com.app.models.WalletResponse;
import chat.hola.com.app.models.WithdrawLog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * <h1>{@link WithdrawalLogPresenter}</h1>
 *
 * <p>This is implemented presenter of {@link WithdrawalLogActivity},
 * it will call apis and handles apis responses and
 * communicate between models and {@link WithdrawalLogActivity}</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 March 2020
 */
public class WithdrawalLogPresenter implements WithdrawalLogContract.Presenter {

    @Inject
    public WithdrawalLogPresenter() {
    }

    WithdrawalLogContract.View view;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int PAGE_SIZE = Constants.PAGE_SIZE;

    @Inject
    HowdooService service;
    @Inject
    SessionManager sessionManager;

    @Override
    public void attachView(WithdrawalLogContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void walletBalance() {
        view.showLoader();
        service.walletBalance(AppController.getInstance().getApiToken(), Constants.LANGUAGE, AppController.getInstance().getUserId(), Constants.APP_TYPE)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<WalletResponse>>() {
                    @Override
                    public void onNext(Response<WalletResponse> response) {
                        view.hideLoader();
                        if (response.code() == 200) {
                            try {
                                assert response.body() != null;

                                for (WalletResponse.Data.Wallet wallet : response.body().getData().getWalletData()) {
                                    if (wallet.getCurrency().equals(sessionManager.getCurrency())) {
                                        view.loadBalance(wallet.getCurrencySymbol());
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
                        view.hideLoader();
                    }

                    @Override
                    public void onComplete() {
                        view.hideLoader();
                    }
                });
    }

    @Override
    public void withdrawLogs(String pageState) {
        view.showLoader();
        service.withdrawLogs(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                AppController.getInstance().getUserId(), Constants.APP_TYPE, pageState, 100)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<WithdrawLog>>() {
                    @Override
                    public void onNext(Response<WithdrawLog> response) {
                        view.hideLoader();
                        isLoading = false;
                        if (response.code() == 200) {
                            isLastPage = response.body().getData().getPageState() == null;
                            view.showWithdrawLogs(response.body().getData().getData(), response.body().getData().getPageState());
                        } else {
                            view.empty();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoader();
                        isLoading = false;
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void callApiOnScroll(boolean b, String pageState, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                withdrawLogs(pageState);
            }
        }
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
