package chat.hola.com.app.preview;

import android.os.Handler;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.stories.model.ViewerResponse;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h1>PreviewPresenter</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 4/24/2018.
 */

public class PreviewPresenter implements PreviewContract.Presenter {

    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    HowdooService service;
    @Inject
    PreviewContract.View view;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Inject
    PreviewPresenter() {

    }

    public static int page = 0;
    public static final int pageSize = Constants.PAGE_SIZE;


    @Override
    public void viewStory(String storyId) {
        service.viewStory(AppController.getInstance().getApiToken(), Constants.LANGUAGE, storyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
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
                                                        viewStory(storyId);
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

                    }
                });
    }

    @Override
    public void viewerListScroll(String storyId, int visibleItemCount, int firstVisibleItemPosition, int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= pageSize){
                page++;
                getViewerList(storyId,page*pageSize,pageSize);
            }
        }
    }

    @Override
    public void getViewerList(String storyId, int skip, int limit) {
        isLoading = true;
        service.getStoryViewerList(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                storyId,skip,limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ViewerResponse>>() {
                    @Override
                    public void onNext(Response<ViewerResponse> response) {
                        isLoading = false;
                        switch (response.code()) {
                            case 200:
                                if(view!=null && response.body().getData()!=null) {
                                    isLastPage = response.body().getData().size() < pageSize;
                                    view.updateViewerList(storyId, response.body().getData(), skip == 0);
                                }
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
                                                        getViewerList(storyId,skip,limit);
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
                        e.printStackTrace();
                        isLoading = false;
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }
}
