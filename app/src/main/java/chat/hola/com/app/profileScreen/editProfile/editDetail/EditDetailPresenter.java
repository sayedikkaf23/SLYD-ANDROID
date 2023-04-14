package chat.hola.com.app.profileScreen.editProfile.editDetail;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.models.Error;
import chat.hola.com.app.profileScreen.editProfile.model.EditProfileResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class EditDetailPresenter implements EditDetailContract.Presenter {

    private EditDetailContract.View view;

    @Inject
    HowdooService service;

    @Inject
    public EditDetailPresenter() {
    }

    @Override
    public void updateDetail(Map<String, Object> map) {
        view.showLoader();
        service.editProfile(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<EditProfileResponse>>() {
                    @Override
                    public void onNext(@NotNull Response<EditProfileResponse> editProfileResponse) {
                        view.hideLoader();
                        if (editProfileResponse.code() == 200) {
                            view.success();
                        }
                    }

                    @Override
                    public void onError(@NotNull Throwable e) {
                        view.hideLoader();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void verifyIsEmailRegistered(String email, boolean isBusiness) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("type", "1");

        service.emailPhoneVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(@NotNull Response<Error> response) {
                        if (isBusiness)
                            view.businessEmailAvailable(response.code() == 204);
                        else
                            view.emailAvailable(response.code() == 204);
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
    public void verifyIsPhoneRegistered(String countryCode, String phone, boolean isBusiness) {
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
                        if (isBusiness)
                            view.businessPhoneAvailable(response.code() == 204);
                        else
                            view.phoneAvailable(response.code() == 204);
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
    public void verifyBusinessEmail(String email) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("type", "1");

        service.sendVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(@NotNull Response<Error> response) {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            view.businessEmailVerificationSent(response.body().getMessage());
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
    public void verifyBusinessPhone(String countryCode, String mobile) {
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("countryCode", countryCode);
        map.put("type", "2");

        service.sendVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(@NotNull Response<Error> response) {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            view.businessPhoneVerificationSent(response.body().getData().getOtpId());
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
    public void verifyEmail(String email) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", email);
        map.put("type", "1");

        service.profileEmailPhoneVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(@NotNull Response<Error> response) {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            view.emailVerificationSent(response.body().getMessage());
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
    public void verifyPhone(String countryCode, String mobile) {
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", mobile);
        map.put("countryCode", countryCode);
        map.put("type", "2");

        service.profileEmailPhoneVerification(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Error>>() {
                    @Override
                    public void onNext(@NotNull Response<Error> response) {
                        if (response.code() == 200) {
                            assert response.body() != null;
                            view.phoneVerificationSent(response.body().getData().getOtpId());
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
    public void attach(EditDetailContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        view = null;
    }
}
