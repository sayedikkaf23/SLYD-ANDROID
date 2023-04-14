package chat.hola.com.app.search_user;

import android.os.Handler;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.HowdooServiceTrending;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.contact.GetFriendRequest;
import chat.hola.com.app.home.contact.GetFriends;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.discover.contact.pojo.FollowResponse;
import chat.hola.com.app.search.model.SearchResponse;
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

public class SearchUserPresenter implements SearchUserContract.Presenter, SearchUserAdapter.ClickListner {
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;
    @Inject
    HowdooService service;
    @Inject
    HowdooServiceTrending trending;
    @Inject
    SearchUserContract.View view;
    @Inject
    SearchUserModel model;
    SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    SearchUserPresenter() {
    }

    @Override
    public void search(String text, int offset, int limit) {
        isLoading = true;
        service.search(AppController.getInstance().getApiToken(), Constants.LANGUAGE, text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<SearchResponse>>() {
                    @Override
                    public void onNext(Response<SearchResponse> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    isLastPage = response.body().getData().size() < PAGE_SIZE;
                                    model.clearData();
                                    model.setUser(response.body().getData());
                                    view.dataAvailable(!response.body().getData().isEmpty());

                                    break;
                              case 204:
                                view.dataAvailable(false);
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
                                                            search(text, offset, limit);
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
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    @Override
    public void friendSearch(String text, int offset, int limit) {
        isLoading = true;
        service.searchFriends(AppController.getInstance().getApiToken(), Constants.LANGUAGE, text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<GetFriends>>() {
                    @Override
                    public void onNext(Response<GetFriends> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    isLastPage = response.body().getData().size() < PAGE_SIZE;
                                    model.clearData();
                                    model.setUser(response.body().getData());
                                    view.dataAvailable(!response.body().getData().isEmpty());
                                    break;
                              case 204:
                                view.dataAvailable(false);
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
                                                            friendSearch(text, offset, limit);
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
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    @Override
    public void friendRequestSearch(String text, int offset, int limit) {
        isLoading = true;
        service.getFriendRequestsSearch(AppController.getInstance().getApiToken(), Constants.LANGUAGE, text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<GetFriendRequest>>() {
                    @Override
                    public void onNext(Response<GetFriendRequest> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    isLastPage = response.body().getData().getFriendRequests().size() < PAGE_SIZE;
                                    model.clearData();
                                    model.setUser(response.body().getData().getFriendRequests());
                                    view.dataAvailable(!response.body().getData().getFriendRequests().isEmpty());
                                    break;
                                case 401:
                                    view.sessionExpired();
                                    break;

                                case 204:
                                    view.dataAvailable(false);
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
                                                            friendRequestSearch(text, offset, limit);
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
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    @Override
    public void stareSearch(String text, int skip, int limit) {
        isLoading = true;
        service.searchStar(AppController.getInstance().getApiToken(), Constants.LANGUAGE, text, skip, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<SearchResponse>>() {
                    @Override
                    public void onNext(Response<SearchResponse> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    isLastPage = response.body().getData().size() < PAGE_SIZE;
                                    model.clearData();
                                    model.setUser(response.body().getData());
                                    view.dataAvailable(!response.body().getData().isEmpty());
                                    break;

                              case 204:
                                view.dataAvailable(false);
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
                                                            stareSearch(text, skip, limit);
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
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    @Override
    public void topStareSearch(int category, int skip, int limit) {
        isLoading = true;
        service.searchTopStar(AppController.getInstance().getApiToken(), Constants.LANGUAGE, category, skip, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<SearchResponse>>() {
                    @Override
                    public void onNext(Response<SearchResponse> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    isLastPage = response.body().getData().size() < PAGE_SIZE;
                                    model.clearData();
                                    model.setUser(response.body().getData());
                                    view.dataAvailable(!response.body().getData().isEmpty());
                                    break;
                              case 204:
                                view.dataAvailable(false);
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
                                                            topStareSearch(category, skip, limit);
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
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    @Override
    public void callApiOnScroll(String call, String text, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {

    }

    @Override
    public SearchUserAdapter.ClickListner getPresenter() {
        return this;
    }

    @Override
    public void myStars(int skip, int limit) {
        isLoading = true;
        service.myStars(AppController.getInstance().getApiToken(), Constants.LANGUAGE, skip, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<SearchResponse>>() {
                    @Override
                    public void onNext(Response<SearchResponse> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    isLastPage = response.body().getData().size() < PAGE_SIZE;
                                    model.clearData();
                                    model.setUser(response.body().getData());
                                    view.dataAvailable(!response.body().getData().isEmpty());
                                    break;
                              case 204:
                                view.dataAvailable(false);
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
                                                            myStars(skip, limit);
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
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    @Override
    public void stars(String type, int skip, int limit) {
        trending.getTopStars(AppController.getInstance().getApiToken(), Constants.LANGUAGE, type, skip, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<SearchResponse>>() {
                    @Override
                    public void onNext(Response<SearchResponse> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    isLastPage = response.body().getData().size() < PAGE_SIZE;
                                    model.clearData();
                                    model.setUser(response.body().getData());
                                    view.dataAvailable(!response.body().getData().isEmpty());
                                    break;
                              case 204:
                                view.dataAvailable(false);
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
                                                            stars(type, skip, limit);
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
                        e.printStackTrace();

                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                    }
                });
    }

    @Override
    public void add(int position) {
        view.add(model.getUser(position));
    }

    @Override
    public void view(int position) {

    }

    @Override
    public void onUserSelected(int position) {
        view.onUserSelected(model.getUser(position));
    }

    @Override
    public void follow(int position, boolean isFollowing, String id) {
        if (isFollowing)
            unfollowPeople(position, id);
        else
            followPeople(position, id);
    }

    private void followPeople(int position, String id) {
        Map<String,Object> map = new HashMap<>();
        map.put("followingId",id);
        service.follow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> response) {
                        switch (response.code()) {
                            case 200:
                                model.updateFolow(position, response.body().getUserId(), response.body().getStatus());
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
                                                        followPeople(position, id);
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

    private void unfollowPeople(int position, String id) {
        Map<String,Object> map = new HashMap<>();
        map.put("followingId",id);
        service.unfollow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> response) {
                        switch (response.code()) {
                            case 200:
                                model.updateFolow(position, response.body().getUserId(), response.body().getStatus());
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
                                                        unfollowPeople(position, id);
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
}
