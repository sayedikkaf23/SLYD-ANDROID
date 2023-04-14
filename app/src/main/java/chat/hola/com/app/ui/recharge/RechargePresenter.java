package chat.hola.com.app.ui.recharge;

import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Card;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.models.RechargeResponse;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.models.SuggestedAmountResponse;
import chat.hola.com.app.models.TransferResponse;
import chat.hola.com.app.models.WalletResponse;
import chat.hola.com.app.models.WithdrawAmountCheckResponse;
import chat.hola.com.app.models.WithdrawMethodResponse;
import chat.hola.com.app.models.WithdrawSuccess;
import chat.hola.com.app.profileScreen.model.Data;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * <h1>RechargePresenter</h1>
 *
 * <p>This is implemented presenter of {@link RechargeActivity},
 * it will call apis and handles apis responses and
 * communicate between models and {@link  RechargeActivity}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 17 Dec 2019
 */
public class RechargePresenter implements RechargeContract.Presenter {
    private SessionApiCall sessionApiCall = new SessionApiCall();
    private RechargeContract.View view;

    @Inject
    HowdooService service;
    @Inject
    SessionManager sessionManager;

    @Inject
    public RechargePresenter() {

    }

    @Override
    public void attach(RechargeContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        this.view = null;
    }

    @Override
    public void suggestedAmount() {String country =
            sessionManager.getCountryName() != null && !sessionManager.getCountryName().isEmpty()
                    ? sessionManager.getCountryName() : "India";
        service.suggestedAmount(AppController.getInstance().getApiToken(), Constants.LANGUAGE, country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<SuggestedAmountResponse>>() {
                    @Override
                    public void onNext(@NotNull Response<SuggestedAmountResponse> response) {
                        view.hideLoader();
                        if (response.code() == 200) {
                            view.setSuggestedAmount(response.body().getData().get(0).getCurrency_symbol(), response.body().getData().get(0).getAmount());
                        } else {
                            Gson gson = new GsonBuilder().create();
                            SuggestedAmountResponse error = new SuggestedAmountResponse();
                            try {
                                if (response.errorBody() != null) {
                                    error = gson.fromJson(response.errorBody().string(), SuggestedAmountResponse.class);
                                    if (view != null && error != null)
                                        view.message(error.getMessage());
                                }
                            } catch (IOException ignored) {
                            }
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

    public void recharge(Card card, String amount, String pgLinkId) {
        view.showLoader();
        Map<String, Object> map = new HashMap<>();
        map.put("paymentMethod", card.getId());
        map.put("type", 1);
        map.put("postal_code", "90011");
        map.put("amount", amount);
        map.put("currency", sessionManager.getCurrency());
        map.put("pgLinkId", pgLinkId);
        service.recharge(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<RechargeResponse>>() {
                    @Override
                    public void onNext(Response<RechargeResponse> response) {
                        view.hideLoader();
                        if (response.code() == 200) {
                            view.recharged(response.body().getData());
                        } else {
                            Gson gson = new GsonBuilder().create();
                            RechargeResponse error = new RechargeResponse();
                            try {
                                if (response.errorBody() != null) {
                                    error = gson.fromJson(response.errorBody().string(), RechargeResponse.class);
                                    if (view != null && error != null)
                                        view.message(error.getMessage());
                                }
                            } catch (IOException ignored) {
                            }
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

    @Override
    public void withdraw(Map<String, Object> map) {
        view.showLoader();
        service.withdraw(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<WithdrawSuccess>>() {
                    @Override
                    public void onNext(Response<WithdrawSuccess> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                view.withdrawSuccess(response.body().getData());
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
                                                        withdraw(map);
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
                                sessionApiCall.getNewSession(sessionObserver);
                                break;

                            default:
                                Gson gson = new GsonBuilder().create();
                                Error error = new Error();
                                try {
                                    if (response.errorBody() != null) {
                                        error = gson.fromJson(response.errorBody().string(), Error.class);
                                        if (view != null && error != null)
                                            view.message(error.getMessage());
                                    }
                                } catch (IOException ignored) {
                                }
                                break;
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

    @Override
    public void withdrawalAmount(String amount, boolean isForWithdraw) {
        // as per backend dev hardcoding USD
        service.withdrawAmount(AppController.getInstance().getApiToken(), Constants.LANGUAGE, sessionManager.getWalletId(), "USD", "1", amount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<WithdrawAmountCheckResponse>>() {
                    @Override
                    public void onNext(Response<WithdrawAmountCheckResponse> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                view.withdraw(String.valueOf(response.body().getData().getWithdrawAmount()), sessionManager.getCurrency());
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
                                                        withdrawalAmount(amount, false);
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
                                sessionApiCall.getNewSession(sessionObserver);
                                break;

                            default:
                                Gson gson = new GsonBuilder().create();
                                Error error = new Error();
                                try {
                                    if (response.errorBody() != null) {
                                        error = gson.fromJson(response.errorBody().string(), Error.class);
                                        if (view != null && error != null)
                                            view.message(error.getMessage());
                                    }
                                } catch (Exception ignored) {
                                }
                                break;
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

    @Override
    public void methods(String currencyCountryCode) {
        view.showLoader();
        service.paymentMethods(AppController.getInstance().getApiToken(), Constants.LANGUAGE, currencyCountryCode, 0, 20)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<WithdrawMethodResponse>>() {
                    @Override
                    public void onNext(Response<WithdrawMethodResponse> response) {
                        view.hideLoader();
                        if (response.code() == 200)
                            view.recharge(response.body().getData().getWithdrawMethods().get(0).get_id());
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

    @Override
    public void validateTransfer(Data user, String amount, String toCurrency) {
        view.showLoader();
        Map<String, Object> map = new HashMap<>();
        map.put("fromCurrency", sessionManager.getCurrency());
        map.put("fromUserId", AppController.getInstance().getUserId());
        map.put("fromUserType", Constants.APP_TYPE);
        map.put("toCurrency", toCurrency);
        map.put("toUserId", user.getId());
        map.put("toUserType", Constants.APP_TYPE);
        map.put("amount", amount);
        service.validateTransfer(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        view.hideLoader();
                        if (response.code() == 200)
                            transfer(user, amount, toCurrency);
                        else {
                            Gson gson = new GsonBuilder().create();
                            Error error = new Error();
                            try {
                                if (response.errorBody() != null) {
                                    error = gson.fromJson(response.errorBody().string(), Error.class);
                                    if (view != null && error != null)
                                        view.message(error.getMessage());
                                }
                            } catch (IOException ignored) {
                            }
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

    private void transfer(Data user, String amount, String toCurrency) {
        view.showLoader();
        Map<String, Object> map = new HashMap<>();
        map.put("fromUserName", AppController.getInstance().getUserName());
        map.put("fromCurrency", sessionManager.getCurrency());
        map.put("fromUserId", AppController.getInstance().getUserId());
        map.put("fromUserType", Constants.APP_TYPE);
        map.put("toUserName", user.getUserName());
        map.put("toCurrency", toCurrency);
        map.put("toUserId", user.getId());
        map.put("toUserType", Constants.APP_TYPE);
        map.put("amount", amount);
        service.transfer(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<TransferResponse>>() {
                    @Override
                    public void onNext(Response<TransferResponse> response) {
                        view.hideLoader();
                        if (response.code() == 200)
                            view.transfered(response);
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
