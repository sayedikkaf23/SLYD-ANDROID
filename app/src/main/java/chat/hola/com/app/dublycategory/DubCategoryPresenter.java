package chat.hola.com.app.dublycategory;

import android.os.Handler;
import android.util.Log;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.DublyService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.dublycategory.modules.CategoryClickListner;
import chat.hola.com.app.dublycategory.modules.DubCategoryResponse;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * <h1>SearchUserPresenter</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public class DubCategoryPresenter implements DubCategoryContract.Presenter, CategoryClickListner {
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    DublyService service;
    @Inject
    DubCategoryContract.View view;
    @Inject
    DubCategoryModel model;
    @Inject
    NetworkConnector networkConnector;

    @Inject
    DubCategoryPresenter() {
    }


    @Override
    public CategoryClickListner getCategoryPresenter() {
        return this;
    }

    @Override
    public void getCategories(boolean b) {
        service.getDubCategories(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<DubCategoryResponse>>() {
                    @Override
                    public void onNext(Response<DubCategoryResponse> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    isLastPage = response.body().getDubs().size() < PAGE_SIZE;
                                    model.setCategoryData(response.body().getDubs(), b);
                                    view.categories(response.body().getDubs());
                                    break;
                                case 401:
                                    view.sessionExpired();
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
                                                            getCategories(b);
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
                        } catch (Exception ignored) {
                            Log.i("Exception", ignored.toString());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.isInternetAvailable(networkConnector.isConnected());
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }


    @Override
    public void onItemClick(String id, String name) {
        view.getList(id, name);
    }
}
