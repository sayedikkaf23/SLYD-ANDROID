package chat.hola.com.app.poststatus;

import android.content.Context;
import android.os.Handler;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.cloudinary.android.policy.TimeWindow;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.stories.model.StoryPost;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by embed on 15/12/18.
 */

public class PostStatusPresenter implements PostStatusPresenterImpl.PostStatusPresent, UploadCallback {

    @Inject
    HowdooService service;

    @Inject
    PostStatusPresenterImpl.PostStatusPresenterView view;
    SessionApiCall sessionApiCall = new SessionApiCall();


    @Inject
    public PostStatusPresenter() {
    }


    @Override
    public void attachView(Object view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void postStatus(Context mContext, String absolutePath, int type, boolean isPrivate, String bgcolor, String statusMsg, String fontType) {

        view.showProgress();

        MediaManager.get().upload(absolutePath)
                .option(Constants.Post.FOLDER, Constants.Cloudinary.stories)
                .option(Constants.Post.RESOURCE_TYPE, "image")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {

                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        Map<String, Object> parameters = new HashMap<>();
                        //parameters.put("type", resultData.get("resource_type").equals("video") ? "2" : "1");
                        parameters.put("type", type);
                        parameters.put("urlPath", resultData.get(Constants.Post.URL));
                        parameters.put("thumbnail", resultData.get(Constants.Post.URL));
                        parameters.put("isPrivate", isPrivate);

                        if (bgcolor != null)
                            parameters.put("backgroundColor", bgcolor);
                        if (statusMsg != null)
                            parameters.put("statusMessage", statusMsg);
                        if (fontType != null)
                            parameters.put("fontType", fontType);


                        service.postStory(AppController.getInstance().getApiToken(), Constants.LANGUAGE, parameters)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DisposableObserver<Response<StoryPost>>() {
                                    @Override
                                    public void onNext(Response<StoryPost> response) {
                                        switch (response.code()) {
                                            case 200:
                                                view.hideProgress();
                                                view.onPostStatusSuccess();
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
                                                                        postStatus(mContext, absolutePath, type, isPrivate, bgcolor, statusMsg, fontType);
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
                                        view.hideProgress();
                                    }

                                    @Override
                                    public void onComplete() {
                                        view.hideProgress();
                                    }
                                });
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        view.hideProgress();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {
                        view.hideProgress();
                    }
                })
                .constrain(TimeWindow.immediate())
                .dispatch();

        /*Bundle bundle = new Bundle();
        bundle.putString("path", absolutePath);
        bundle.putString("backGroundColor",bgcolor);
        bundle.putString("statusMessage",statusMsg);
        bundle.putString("fontType",fontType);
        bundle.putInt("type", type);
        bundle.putBoolean("isPrivate", isPrivate);
        bundle.putString("duration", "");
        Intent intent = new Intent(mContext, StoryService.class);
        intent.putExtra("data", bundle);
        mContext.startService(intent);*/

    }

    @Override
    public void onStart(String requestId) {

    }

    @Override
    public void onProgress(String requestId, long bytes, long totalBytes) {

    }

    @Override
    public void onSuccess(String requestId, Map resultData) {


    }

    @Override
    public void onError(String requestId, ErrorInfo error) {

    }

    @Override
    public void onReschedule(String requestId, ErrorInfo error) {

    }
}
