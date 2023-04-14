package chat.hola.com.app.profileScreen.business.category;


import android.os.Handler;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.model.BusinessCategory;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * <h1>BusinessCategoryPresenter</h1>
 *
 * @author Shaktisinh Jaeja
 * @version 1.0
 * @since 16 August 2019
 */
public class BusinessCategoryPresenter implements BusinessCategoryContact.Presenter, BusinessCategoryAdapter.ClickListner {

    @Inject
    HowdooService service;
    @Inject
    BusinessCategoryModel model;
    @Inject
    BusinessCategoryContact.View view;

    private SessionApiCall sessionApiCall;

    @Inject
    BusinessCategoryPresenter() {
        sessionApiCall = new SessionApiCall();
    }

    @Override
    public void businessCategories() {
        service.businessCategories(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<BusinessCategory>>() {
                    @Override
                    public void onNext(Response<BusinessCategory> response) {
                        switch (response.code()) {
                            case 200:
                                view.success();
                                model.setCategories(response.body().getData());
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
                                                        businessCategories();
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                view.failed();
                                            }

                                            @Override
                                            public void onComplete() {
                                            }
                                        });
                                sessionApiCall.getNewSession(sessionObserver);
                                break;
                            default:
                                view.failed();
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.failed();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public BusinessCategoryAdapter.ClickListner getPresenter() {
        return this;
    }

    @Override
    public void onItemSelect(BusinessCategory.Data businessCategory) {
        view.selectCategory(businessCategory);
    }
}
