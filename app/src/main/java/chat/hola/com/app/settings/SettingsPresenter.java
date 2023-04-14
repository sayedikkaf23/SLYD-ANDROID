package chat.hola.com.app.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Login;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.notification.UserIdUpdateHandler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h1>SettingsPresenter</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 24/2/18.
 */

public class SettingsPresenter implements SettingsContract.Presenter {

    @Inject
    HowdooService service;
    @Inject
    SettingsContract.View mView;
    @Inject
    SessionManager sessionManager;
    private SessionApiCall sessionApiCall = new SessionApiCall();
    CouchDbController db;

    @Inject
    SettingsPresenter() {
        db = AppController.getInstance().getDbController();
    }

    @Override
    public void init() {
        mView.applyFont();
    }

    @Override
    public void logout() {
        mView.showLoader();
        service.logout(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                mView.logout();
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
                                                        logout();
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
                        mView.hideLoader();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void deleteAccount(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setMessage("Are you sure want to Delete Account");
        dialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        deleteUserAccount();
                    }
                });
        dialog.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();
    }

    @Override
    public void switchBusiness(boolean isBusiness, String businessCategoryId) {
        Map<String, Object> params = new HashMap<>();
        params.put("businessCategoryId", businessCategoryId);
        params.put("isActiveBusinessProfile", isBusiness);
        params.put("businessUniqueId", sessionManager.getBusinessUniqueId());
        service.switchBusiness(AppController.getInstance().getApiToken(), Constants.LANGUAGE, params)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Login>>() {
                    @Override
                    public void onNext(Response<Login> response) {
                        switch (response.code()) {
                            case 200:
                                setData(response.body().getResponse(), isBusiness);
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
                                                        switchBusiness(isBusiness, businessCategoryId);
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
                        Log.i("error", "");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, int RESULT_OK) {
//        if (requestCode == 111) {
//            if (resultCode == RESULT_OK) {
//                // Get the invitation IDs of all sent messages
//                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
//            } else {
//                mView.showMessage(null, R.string.inviteFailedMsg);
//            }
//        }
    }

    private void setData(Login.LoginResponse response, boolean isBusiness) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("userImageUrl", response.getProfilePic() == null ? "" : response.getProfilePic());
            map.put("userName", response.getUserName());
            map.put("firstName", response.getFirstName());
            map.put("lastName", response.getLastName());
            map.put("userId", response.getUserId());
            map.put("private", response.get_private());
            map.put("socialStatus", "");
            map.put("userIdentifier", response.getUserName());
            map.put("apiToken", response.getToken());
            sessionManager.setRefreshToken(response.getRefreshToken());

            AppController.getInstance().getSharedPreferences().edit().putString("token", response.getToken()).apply();
            map.put("userLoginType", 1);
            map.put("excludedFilterIds", new ArrayList<Integer>());
            if (!db.checkUserDocExists(AppController.getInstance().getIndexDocId(), response.getUserId())) {
                String userDocId = db.createUserInformationDocument(map);
                db.addToIndexDocument(AppController.getInstance().getIndexDocId(), response.getUserId(), userDocId);

            } else {
                db.updateUserDetails(db.getUserDocId(response.getUserId(), AppController.getInstance().getIndexDocId()), map);
            }

            db.updateIndexDocumentOnSignIn(AppController.getInstance().getIndexDocId(), response.getUserId(), 1, true);


            String name = response.getFirstName();

            if (response.getLastName() != null && !response.getLastName().isEmpty()) {
                name = name + " " + response.getLastName();
            }

            AppController.getInstance()
                    .setSignedIn(true, response.getUserId(), name, response.getUserName(),
                            response.getProfilePic(), 1, response.getAccountId(), response.getProjectId(),
                            response.getKeysetId(), response.getLicenseKey(), response.getRtcAppId(),
                            response.getArFiltersAppId(), response.getGroupCallStreamId());
            AppController.getInstance().setSignStatusChanged(true);

            String topic = "/topics/" + response.getUserId();
            FirebaseMessaging.getInstance().subscribeToTopic(topic);
            OneSignal.setExternalUserId(response.getOneSignalId(), new UserIdUpdateHandler());

            sessionManager.setUserName(response.getUserName());
            sessionManager.setFirstName(response.getFirstName());
            sessionManager.setLastsName(response.getLastName());
//            sessionManager.setFacebookAccessToken(AccessToken.getCurrentAccessToken());
            sessionManager.setUserProfilePic(response.getProfilePic(), true);

//            LiveStream stream = response.getStream();
            String countryCode = response.getCountryCode();
            String phone = response.getPhoneNumber();

            //  sessionManager.setFcmTopic(stream.getFcmTopic());
            sessionManager.setUserId(response.getUserId());
            sessionManager.setEmail(response.getEmail());
            sessionManager.setMobileNumber(phone.replace(countryCode, ""));
            sessionManager.setFirstName(response.getFirstName());
            sessionManager.setCountryCode(countryCode);

            mView.hideLoader();
            mView.gotoProfile(isBusiness);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void postSubscription(double amount, double youAmount, double appAmount) {
        Map<String, Object> map = new HashMap<>();
        map.put("amount", amount);
        map.put("appWillGet", appAmount);
        map.put("userWillGet", youAmount);
        service.postSubscription(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            if (mView != null) mView.onSuccessSubscriptionAdded();
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

    private void deleteUserAccount() {
        mView.showLoader();
        service.deleteAccount(AppController.getInstance().getApiToken())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                mView.delete();
                                break;
                            case 204:
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        deleteUserAccount();
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
