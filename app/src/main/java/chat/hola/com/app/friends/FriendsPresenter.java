package chat.hola.com.app.friends;

import android.os.Handler;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.contact.GetFriendRequest;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * <h1>BlockUserPresenter</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public class FriendsPresenter implements FriendsContract.Presenter, FriendsAdapter.ClickListner {
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;
    SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    HowdooService service;
    @Inject
    FriendsContract.View view;
    @Inject
    FriendsModel model;


    @Inject
    FriendsPresenter() {
    }


    @Override
    public void friends(int offset, int limit) {
        view.loading(true);
        service.getFriendRequests(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<GetFriendRequest>>() {
                    @Override
                    public void onNext(Response<GetFriendRequest> response) {

                        switch (response.code()) {
                            case 200:
                              if( response.body()  != null){
                                model.setData(response.body().getData());}
                                view.loading(false);
                                break;
                            case 204:
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
                                                        friends(offset, limit);
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
                        view.loading(false);
                    }

                    @Override
                    public void onComplete() {
                        view.loading(false);
                    }
                });
    }

    @Override
    public void friendsSearch(String text, int offset, int limit) {
        view.loading(true);
        service.getFriendRequestsSearch(AppController.getInstance().getApiToken(), Constants.LANGUAGE, text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<GetFriendRequest>>() {
                    @Override
                    public void onNext(Response<GetFriendRequest> response) {

                        switch (response.code()) {
                            case 200:
                              if( response.body()  != null){
                                model.setData(response.body().getData());}
                                view.loading(false);
                                break;
                            case 204:
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
                                                        friendsSearch(text, offset, limit);
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
                        view.loading(false);
                    }

                    @Override
                    public void onComplete() {
                        view.loading(false);
                    }
                });
    }

    @Override
    public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                page++;
                friends(PAGE_SIZE * page, PAGE_SIZE);
            }
        }
    }

    @Override
    public FriendsAdapter.ClickListner getPresenter() {
        return this;
    }

    @Override
    public void openRequest(int position) {
        view.openRequest(model.getUser(position));
    }

    @Override
    public void openProfile(int position) {
        view.openProfile(model.getUser(position));
    }
}
