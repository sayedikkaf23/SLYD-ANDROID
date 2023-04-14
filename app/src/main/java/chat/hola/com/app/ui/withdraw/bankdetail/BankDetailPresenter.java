package chat.hola.com.app.ui.withdraw.bankdetail;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.models.StripeResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class BankDetailPresenter implements BankDetailContract.Presenter {

    private BankDetailContract.View mView;

    @Inject
    SessionManager sessionManager;
    @Inject
    HowdooService service;

    @Inject
    BankDetailPresenter() {
    }


    @Override
    public void deleteBank(StripeResponse.Data.ExternalAccounts.Account data) {
        mView.showLoader();
        Map<String, Object> map = new HashMap<>();
        map.put("accountId", data.getId());
        service.deleteBankAccount(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(Response<Error> response) {
                        mView.hideLoader();
                        if (response.code() == 200) {
                            mView.bankDeleted();
                        } else {
                            Gson gson = new GsonBuilder().create();
                            Error error = new Error();
                            try {
                                if (response.errorBody() != null) {
                                    error = gson.fromJson(response.errorBody().string(), Error.class);
                                    if (mView != null && error != null)
                                        mView.message(error.getMessage());
                                }
                            } catch (IOException ignored) {
                            }
                        }

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
    public void attach(BankDetailContract.View view) {
        mView = view;
    }

    @Override
    public void detach() {
        mView = null;
    }
}
