package chat.hola.com.app.ui.dashboard;

import android.os.Handler;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.models.TransactionResponse;
import chat.hola.com.app.models.WalletResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * <h1>WalletDashboardPresenter</h1>
 *
 * <p>This is implemented presenter of {@link WalletDashboardActivity},
 * it will call apis and handles apis responses and
 * communicate between models and {@link WalletDashboardActivity}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 05 Dec 2019
 */
public class WalletDashboardPresenter implements WalletDashboardContract.Presenter {

    private WalletDashboardContract.View view;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int PAGE_SIZE = Constants.PAGE_SIZE;

    @Inject
    HowdooService service;
    @Inject
    SessionManager sessionManager;
    private SessionApiCall sessionApiCall = new SessionApiCall();


    @Inject
    public WalletDashboardPresenter() {

    }

    @Override
    public void attach(WalletDashboardContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void walletBalance() {
        service.walletBalance(AppController.getInstance().getApiToken(), Constants.LANGUAGE, AppController.getInstance().getUserId(), Constants.APP_TYPE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<WalletResponse>>() {
                    @Override
                    public void onNext(Response<WalletResponse> response) {
                        if (response.code() == 200) {
                            try {
                                assert response.body() != null;
                                if (view != null && response.body().getData() != null && !response.body().getData().getWalletData().isEmpty()) {
                                    for (WalletResponse.Data.Wallet wallet : response.body().getData().getWalletData()) {
                                        if (wallet.getCurrency().equals(sessionManager.getCurrency())) {
                                            sessionManager.setWalletId(wallet.getWalletid());
                                            sessionManager.setWalletBalance(wallet.getBalance());
                                            view.loadBalance(wallet.getCurrencySymbol());
                                        }
                                    }
                                }

                                transactions(sessionManager.getWalletId(), 0, true, null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void transactions(String walletId, Integer type, boolean isFirst, String pageState) {
        isLoading = true;
        if (view != null) view.showLoader();
        service.transactions(AppController.getInstance().getApiToken(), Constants.LANGUAGE, walletId, type, pageState, 20)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<TransactionResponse>>() {
                    @Override
                    public void onNext(Response<TransactionResponse> response) {
                        isLoading = false;

                        if (view != null) view.hideLoader();
                        if (response.code() == 200) {
                            try {
                                assert response.body() != null;
                                if (view != null) {
                                    isLastPage = response.body().getPageState() == null;
                                    view.setData(response.body().getTransactionDataList(), response.body().getPageState(), isFirst);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else if (response.code()==204){
                            if (view != null) {
                                view.noData();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoading = false;
                        if (view != null) view.hideLoader();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void callApiOnScroll(Integer type, boolean isFirst, String pageState, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                transactions(sessionManager.getWalletId(), type, isFirst, pageState);
            }
        }
    }

    @Override
    public void kycVerification() {
        view.showLoader();
        service.kycVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                //applied for kyc
                                view.moveNext(response.body().getVerificationStatus());
                                break;
                            case 404:
                                //not applied for kyc
                                view.moveNext(-1);
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable().subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        kycVerification();
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onComplete() {
                                            }
                                        });
                                sessionApiCall.getNewSession(service, sessionObserver);
                                break;
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
}
