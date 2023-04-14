package chat.hola.com.app.music;

import android.os.Handler;

import com.ezcall.android.R;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.HowdooServiceTrending;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.post.ReportReason;
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

public class MusicPresenter implements MusicContract.Presenter, MusicAdapter.ClickListner {
    private static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;
    @Inject
    HowdooService service;
    @Inject
    MusicContract.View view;
    @Inject
    MusicModel model;
    @Inject
    HowdooServiceTrending serviceTrending;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    MusicPresenter() {
    }


    @Override
    public void getMusicData(String musicId, int skip, int limit) {
        isLoading = true;
        service.getPostByMusicId(AppController.getInstance().getApiToken(), Constants.LANGUAGE, musicId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<chat.hola.com.app.music.Response>>() {
                    @Override
                    public void onNext(Response<chat.hola.com.app.music.Response> postsResponse) {

                        switch (postsResponse.code()) {
                            case 200:
                                assert postsResponse.body() != null;
                                isLastPage = postsResponse.body().getData().size() < PAGE_SIZE;
                                model.setData(postsResponse.body().getData(), skip == 0);
                                view.setData(postsResponse.body().getTotalPosts(), postsResponse.body().getMusicData());
                                break;
                            case 201:
                                view.showMessage(null, R.string.nodata);
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
                                                        getMusicData(musicId, skip, limit);
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
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                        view.loading(false);
                    }
                });
    }

    @Override
    public void callApiOnScroll(String postId, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount, String from) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                page++;
                if (MusicActivity.musicId != null)
                    getMusicData(postId, PAGE_SIZE * page, PAGE_SIZE);
                else if (MusicActivity.categoryId != null)
                    getCategoryPost(postId, PAGE_SIZE * page, PAGE_SIZE);
                else
                    getHashtag(postId, PAGE_SIZE * page, PAGE_SIZE, from);

            }
        }
    }

    @Override
    public MusicAdapter.ClickListner getPresenter() {
        return this;
    }

    @Override
    public void favorite(String musicId, boolean favorite) {
        Map<String, Object> map = new HashMap<>();
        map.put("musicId", musicId);
        map.put("isFavourite", favorite);
        service.favourite(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ReportReason>>() {
                    @Override
                    public void onNext(Response<ReportReason> reportReasonResponse) {

                        switch (reportReasonResponse.code()) {
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
                                                        favorite(musicId, favorite);
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getHashtag(String hashtag, int skip, int limit, String from) {
        if (from != null && !from.isEmpty()) {
            service.getPostByHashtags(AppController.getInstance().getApiToken(), Constants.LANGUAGE, hashtag, skip, limit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Response<chat.hola.com.app.music.Response>>() {
                        @Override
                        public void onNext(Response<chat.hola.com.app.music.Response> postsResponse) {
                            switch (postsResponse.code()) {
                                case 200:
                                    assert postsResponse.body() != null;
                                    isLastPage = postsResponse.body().getData().size() < PAGE_SIZE;
                                    model.setData(postsResponse.body().getData(), skip == 0);
                                    view.setImage(postsResponse.body().getImage());
                                    view.setData(postsResponse.body().getTotalPosts(), postsResponse.body().getMusicData());
                                    break;
                                case 201:
                                    view.showMessage(null, R.string.nodata);
                                    isLoading = false;
                                    view.loading(false);
                                    break;
                                case 401:
                                    view.sessionExpired();
                                    isLoading = false;
                                    view.loading(false);
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
                                                            getHashtag(hashtag, skip, limit, from);
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
                            isLoading = false;
                            view.loading(false);
                        }

                        @Override
                        public void onComplete() {
                            isLoading = false;
                            view.loading(false);
                        }
                    });
        } else {
            serviceTrending.getPostByHashTag(AppController.getInstance().getApiToken(), Constants.LANGUAGE, hashtag, skip, limit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Response<chat.hola.com.app.music.Response>>() {
                        @Override
                        public void onNext(Response<chat.hola.com.app.music.Response> postsResponse) {
                            switch (postsResponse.code()) {
                                case 200:
                                    assert postsResponse.body() != null;
                                    isLastPage = postsResponse.body().getData().size() < PAGE_SIZE;
                                    model.setData(postsResponse.body().getData(), skip == 0);
                                    view.setImage(postsResponse.body().getImage());
                                    view.setData(postsResponse.body().getTotalPosts(), postsResponse.body().getMusicData());
                                    break;
                                case 201:
                                    view.showMessage(null, R.string.nodata);
                                    isLoading = false;
                                    view.loading(false);
                                    break;
                                case 401:
                                    view.sessionExpired();
                                    isLoading = false;
                                    view.loading(false);
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
                                                            getHashtag(hashtag, skip, limit, from);
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
                            isLoading = false;
                            view.loading(false);
                        }

                        @Override
                        public void onComplete() {
                            isLoading = false;
                            view.loading(false);
                        }
                    });
        }
    }

    @Override
    public void getCategoryPost(String categoryId, int skip, int limit) {
        serviceTrending.getPostByCategory(AppController.getInstance().getApiToken(), Constants.LANGUAGE, categoryId, skip, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<chat.hola.com.app.music.Response>>() {
                    @Override
                    public void onNext(Response<chat.hola.com.app.music.Response> postsResponse) {
                        switch (postsResponse.code()) {
                            case 200:
                                assert postsResponse.body() != null;
                                isLastPage = postsResponse.body().getData().size() < PAGE_SIZE;
                                model.setData(postsResponse.body().getData(), skip == 0);
                                view.setImage(postsResponse.body().getImage());
                                view.setData(postsResponse.body().getTotalPosts(), postsResponse.body().getMusicData());
                                break;
                            case 201:
                                view.showMessage(null, R.string.nodata);
                                isLoading = false;
                                view.loading(false);
                                break;
                            case 401:
                                view.sessionExpired();
                                isLoading = false;
                                view.loading(false);
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
                                                        getCategoryPost(categoryId, skip, limit);
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
                        isLoading = false;
                        view.loading(false);
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                        view.loading(false);
                    }
                });
    }

    @Override
    public void onItemSelected(int position) {
        view.showDetail(position, model.getData());
    }
}
