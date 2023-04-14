package chat.hola.com.app.blockUser;

import android.os.Handler;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.blockUser.model.ClickListner;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h1>BlockUserPresenter</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public class BlockUserPresenter implements BlockUserContract.Presenter, ClickListner {
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    HowdooService service;
    @Inject
    BlockUserContract.View view;
    @Inject
    BlockUserModel model;
    @Inject
    NetworkConnector networkConnector;

    @Inject
    BlockUserPresenter() {
    }


    @Override
    public void getBlockedUsers(int offset, int limit) {
        isLoading = true;
        service.getBlockedUser(AppController.getInstance().getApiToken(), Constants.LANGUAGE, offset, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<chat.hola.com.app.blockUser.model.Response>>() {
                    @Override
                    public void onNext(Response<chat.hola.com.app.blockUser.model.Response> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    isLastPage = response.body().getData().size() < PAGE_SIZE;
                                    if (offset == 0) model.clearList();
                                    model.setData(response.body().getData());
                                    view.noContent(false);
                                    break;
                                case 204:
                                    view.noContent(true);
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
                                                            getBlockedUsers(offset, limit);
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
                        } catch (Exception ignored) {
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
    public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                page++;
                getBlockedUsers(PAGE_SIZE * page, PAGE_SIZE);
            }
        }
    }

    @Override
    public ClickListner getPresenter() {
        return this;
    }

    @Override
    public void unblock(int position) {
        Map<String, String> map = new HashMap<>();
        map.put("reason", "");
        map.put("targetId", model.getUserId(position));
        map.put("type", "unblock");

        service.block(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    model.updateData(position);
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
                                                            unblock(position);
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
                        } catch (Exception ignored) {
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
    public void onUserClick(int position) {
        view.openProfile(model.getUserId(position));
    }

    @Override
    public void itemSelect(int position, boolean isSelected) {
//        model.selectItem(position, isSelected);
    }

    @Override
    public void btnUnblock(int position) {
        view.confirm(position, model.getUserName(position));
    }


}
