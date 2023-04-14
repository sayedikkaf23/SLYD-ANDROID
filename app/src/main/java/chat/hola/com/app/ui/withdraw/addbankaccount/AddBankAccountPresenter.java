package chat.hola.com.app.ui.withdraw.addbankaccount;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.ui.withdraw.model.bankfield.BankFieldResponse;
import chat.hola.com.app.ui.withdraw.model.bankfield.Field;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;


/**
 * <h1>{@link AddBankAccountPresenter}</h1>
 *
 * <p>This is implemented presenter of {@link AddBankAccountActivity},
 * it will call apis and handles apis responses and
 * communicate between models and {@link AddBankAccountActivity}</p>
 *
 * @author : Hardik Karkar
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 March 2020
 */

public class AddBankAccountPresenter implements AddBankAccountContract.Presenter {

    @Inject
    public AddBankAccountPresenter() {
    }

    AddBankAccountContract.View view;

    @Inject
    HowdooService service;
    @Inject
    SessionApiCall sessionApiCall;
    @Inject
    SessionManager sessionManager;

    @Override
    public void attachView(AddBankAccountContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void getBankFields(String paymentGateWayId) {
        view.showLoader();
        service.getBankFields(AppController.getInstance().getApiToken(), Constants.LANGUAGE, paymentGateWayId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<BankFieldResponse>>() {
                    @Override
                    public void onNext(Response<BankFieldResponse> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                assert response.body() != null;
                                view.showFields(response.body().getData().getFields());
                                break;
                            case 401:
                                if (view != null)
                                    view.sessionExpired();
                            default:
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

    @Override
    public void addBankAccount(List<Field> fieldList, String country) {

        Map<String, Object> map = new HashMap<>();

        map.put("country", country);
        map.put("bankData", fieldList);
        map.put("userId", AppController.getInstance().getUserId());

        view.showLoader();

        service.addBankAccount(AppController.getInstance().getApiToken(),
                Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                view.bankAccountAdded();
                                break;
                            case 401:
                                if (view != null)
                                    view.sessionExpired();
                                break;
                            default:
                                view.showMessage(response.message(), 0);
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

    @Override
    public void addBank(Map<String, String> params) {
        view.showLoader();
        service.addBank(AppController.getInstance().getApiToken(), Constants.LANGUAGE, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        view.hideLoader();
                        switch (response.code()) {
                            case 200:
                                view.bankAccountAdded();
                                break;
                            case 401:
                                if (view != null)
                                    view.sessionExpired();
                                break;
                            default:
                                Gson gson = new GsonBuilder().create();
                                Error error = new Error();
                                try {
                                    if (response.errorBody() != null) {
                                        error = gson.fromJson(response.errorBody().string(), Error.class);
                                        if (view != null && error != null)
                                            view.showMessage(error.getMessage(), -1);
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
                        view.hideLoader();
                    }
                });
    }
}
