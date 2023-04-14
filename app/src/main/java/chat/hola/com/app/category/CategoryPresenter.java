package chat.hola.com.app.category;

import android.content.Intent;
import android.os.Handler;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.category.model.CategoryModel;
import chat.hola.com.app.category.model.ClickListner;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.post.model.CategoryResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * <h1>CategoryPresenter</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 28/3/18
 */

public class CategoryPresenter implements CategoryContract.Presenter, ClickListner {

    @Inject
    HowdooService service;
    @Inject
    CategoryContract.View view;
    @Inject
    CategoryModel model;
    @Inject
    NetworkConnector networkConnector;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    CategoryPresenter() {
    }

    @Override
    public void init() {
        view.applyFont();
        view.recyclerSetup();
    }

    /**
     * Returns list of categories and its data
     */
    @Override
    public void getCategory(String categoryId) {

        view.isLoadingData(true);
        service.getCategories(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<CategoryResponse>>() {
                    @Override
                    public void onNext(Response<CategoryResponse> response) {
                        switch (response.code()) {
                            case 200:
                                if (response.body().getData() != null) {
                                    model.setChannel(response.body().getData(), categoryId);// showCategory(response.body().getData());
                                }
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
                                                        getCategory(categoryId);
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
                        view.isLoadingData(false);

                    }

                    @Override
                    public void onError(Throwable e) {
                        view.isInternetAvailable(networkConnector.isConnected());
                        view.isLoadingData(false);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void onItemSelected(int position) {
        model.selectItem(position);
    }

    Intent getSelectedCategory() {
        return model.selectedCategory();
    }
}
