package chat.hola.com.app.profileScreen.business.form.verify;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.models.Error;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class BusinessFormVerifyPresenter implements BusinessFormVerifyContract.Presenter {

    private BusinessFormVerifyContract.View view;

    @Inject
    HowdooService service;

    @Inject
    public BusinessFormVerifyPresenter() {
    }

    @Override
    public void verifyIsEmailRegistered(String email) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("type", "1");

        service.emailPhoneVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(@NotNull Response<Error> response) {
                        view.businessEmailAvailable(response.code() == 204);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void verifyIsUserNameRegistered(String userName) {
        service.verifyIsUserNameRegistered(Utilities.getThings(), userName, Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(@NotNull Response<ResponseBody> response) {
                        switch (response.code()) {

                            case 200:
                                view.userNameAvailable(false);
                                break;
                            case 204:
                                view.userNameAvailable(true);
                                break;
                            case 406:
                                break;
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void verifyIsPhoneRegistered(String countryCode, String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", phone);
        map.put("countryCode", countryCode);
        map.put("type", "2");

        service.emailPhoneVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(@NotNull Response<Error> response) {
                        view.businessPhoneAvailable(response.code() == 204);
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void attach(BusinessFormVerifyContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        view = null;
    }
}
