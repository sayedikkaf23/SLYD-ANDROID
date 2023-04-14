package chat.hola.com.app.transfer_to_friend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.models.TransferResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class TransferPresenter implements TransferContract.Presenter {

    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    public TransferPresenter() {
    }

    @Inject
    SessionManager sessionManager;
    @Inject
    TransferContract.View view;
    @Inject
    HowdooService service;

    @Override
    public void attachView(TransferContract.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void validateTransfer(String userId, String userName, String amount, String toCurrency) {
        view.showProgress(true);
        Map<String, Object> map = new HashMap<>();
        map.put("fromCurrency", sessionManager.getCurrency());
        map.put("fromUserId", AppController.getInstance().getUserId());
        map.put("fromUserType", Constants.APP_TYPE);
        map.put("toCurrency", toCurrency);
        map.put("toUserId", userId);
        map.put("toUserType", Constants.APP_TYPE);
        map.put("amount", amount);
        service.validateTransfer(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        view.showProgress(false);
                        if (response.code() == 200)
                            view.showConvertedAmount(response.body().getData());
                        else {
                            Gson gson = new GsonBuilder().create();
                            Error error = new Error();
                            try {
                                if (response.errorBody() != null) {
                                    error = gson.fromJson(response.errorBody().string(), Error.class);
                                    if (view != null && error != null)
                                        view.alert(error.getMessage());
                                }
                            } catch (IOException ignored) {
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showProgress(false);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void transfer(String userId, String userName, String amount, String toCurrency, String note) {
        view.showProgress(true);
        Map<String, Object> map = new HashMap<>();
        map.put("fromUserName", AppController.getInstance().getUserName());
        map.put("fromCurrency", sessionManager.getCurrency());
        map.put("fromUserId", AppController.getInstance().getUserId());
        map.put("fromUserType", Constants.APP_TYPE);
        map.put("toUserName", userName);
        map.put("toCurrency", toCurrency);
        map.put("toUserId", userId);
        map.put("toUserType", Constants.APP_TYPE);
        map.put("amount", amount);
        map.put("notes", note);
        service.transfer(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<TransferResponse>>() {
                    @Override
                    public void onNext(Response<TransferResponse> response) {
                        view.showProgress(false);
                        if (response.code() == 200)
                            view.transferInitiatedSuccess(response.body().getData().getTransactionId(), response.body().getData().getTransactionTime());
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.showProgress(false);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
