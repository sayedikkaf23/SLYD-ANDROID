package chat.hola.com.app.acceptRequest;

import android.content.Context;
import android.os.Handler;

import com.ezcall.android.R;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.models.UserObserver;
import chat.hola.com.app.post.ReportReason;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h1>AddContactActivity</h1>
 *
 * @author 3Embed
 * @since 4/5/2018.
 */

public class AcceptRequestPresenter implements AcceptRequestContract.Presenter {
    public static final String TAG = "AddContactPresenter";
    private SessionApiCall sessionApiCall = new SessionApiCall();
    @Nullable
    AcceptRequestContract.View view;
    @Inject
    HowdooService service;
    @Inject
    UserObserver userObserver;


    @Inject
    AcceptRequestPresenter(Context context) {
    }

    @Override
    public void attachView(AcceptRequestContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void accept(String targetUserId) {
        Map<String, Object> map = new HashMap<>();
        map.put("targetUserId", targetUserId);
        map.put("status", 1);
        service.requestResponse(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ReportReason>>() {
                    @Override
                    public void onNext(Response<ReportReason> reportReasonResponse) {
                        switch (reportReasonResponse.code()) {
                            case 200:
                                userObserver.postData(1);
                                if( view != null){
                                view.showMessage(null, R.string.request_accepted);
                                view.finishActivity();}
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
                                                        accept(targetUserId);
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

                    }

                    @Override
                    public void onComplete() {
                      if( view != null){
                        view.enable(true);}
                    }
                });
    }

    @Override
    public void reject(String targetUserId) {
        Map<String, Object> map = new HashMap<>();
        map.put("targetUserId", targetUserId);
        map.put("status", 2);
        service.requestResponse(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ReportReason>>() {
                    @Override
                    public void onNext(Response<ReportReason> reportReasonResponse) {
                        switch (reportReasonResponse.code()) {
                            case 200:
                                userObserver.postData(2);
                              if( view != null){
                                view.showMessage(null, R.string.request_rejected);
                                view.finishActivity();}
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
                                                        reject(targetUserId);
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

                    }

                    @Override
                    public void onComplete() {
                      if( view != null){
                        view.enable(true);}
                    }
                });
    }

    @Override
    public void block(String targetUserId) {
        Map<String, String> map = new HashMap<>();
        map.put("reason", "block");
        map.put("targetId", targetUserId);
        map.put("type", "block");
        service.block(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                userObserver.postData(3);
                              if( view != null){
                                view.showMessage(null, R.string.user_blocked);
                                view.finishActivity();}
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
                                                        block(targetUserId);
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

                    }

                    @Override
                    public void onComplete() {
                      if( view != null){
                        view.enable(true);}
                    }
                });
    }

    @Override
    public void send(String targetUserId, String message) {
        Map<String, Object> map = new HashMap<>();
        map.put("targetUserId", targetUserId);
        map.put("message", message);
        service.addFriend(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ReportReason>>() {
                    @Override
                    public void onNext(Response<ReportReason> reportReasonResponse) {
                        switch (reportReasonResponse.code()) {
                            case 200:
                                userObserver.postData(4);
                              if( view != null){
                                view.showMessage(null, R.string.request_sent);
                                view.finishActivity();}
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
                                                        send(targetUserId, message);
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

                    }

                    @Override
                    public void onComplete() {
                      if( view != null){
                        view.enable(true);}
                    }
                });
    }

    public void hideMyPost(boolean isChecked, String userId) {
        //TODO hide my post
    }
}
