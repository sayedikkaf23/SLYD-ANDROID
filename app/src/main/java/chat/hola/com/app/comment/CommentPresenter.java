package chat.hola.com.app.comment;

import android.os.Handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.comment.model.ClickListner;
import chat.hola.com.app.comment.model.Comment;
import chat.hola.com.app.comment.model.CommentResponse;
import chat.hola.com.app.hastag.Hash_tag_people_pojo;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import javax.inject.Inject;

import retrofit2.Response;

/**
 * <h1>BlockUserPresenter</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public class CommentPresenter implements CommentContract.Presenter, ClickListner {
    static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    HowdooService service;
    @Inject
    CommentContract.View view;
    @Inject
    CommentModel model;
    @Inject
    NetworkConnector networkConnector;


    @Inject
    CommentPresenter() {
    }

    @Override
    public void addComment(String postId, String comment) {
        service.addComment(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                model.getParamsAddComment(postId, comment))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<CommentResponse>>() {
                    @Override
                    public void onNext(Response<CommentResponse> response) {
                        switch (response.code()) {
                            case 200:
                                model.addToList(response.body().getComments());
                                view.commented(true);

                                break;
                            case 401:
                                view.sessionExpired();
                                view.commented(false);
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        addComment(postId, comment);
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
                        view.isInternetAvailable(networkConnector.isConnected());
                        view.commented(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getComments(String postId, int offset, int limit) {
        isLoading = true;
        view.showProgress(true);

        if (offset == 0) {
            page = 0;
            isLastPage = false;
        }

        service.getComment(AppController.getInstance().getApiToken(), Constants.LANGUAGE, postId,
                offset, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<chat.hola.com.app.comment.model.Response>>() {
                    @Override
                    public void onNext(Response<chat.hola.com.app.comment.model.Response> response) {
                        isLoading = false;
                        try {
                            view.showProgress(false);
                            switch (response.code()) {
                                case 200:

                                    if (response.body() != null) {
                                        isLastPage = response.body().getData().size() < PAGE_SIZE;
                                        if (offset == 0) model.clearList();
                                        model.setData(response.body().getData());
                                    }
                                    break;
                                case 204:

                                    isLastPage = true;
                                    break;
                                case 401:
                                    view.sessionExpired();
                                    break;
                                case 406:
                                    SessionObserver sessionObserver = new SessionObserver();
                                    sessionObserver.getObservable()
                                            .subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new DisposableObserver<Boolean>() {
                                                @Override
                                                public void onNext(Boolean flag) {
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            getComments(postId, offset, limit);
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
                        isLoading = false;
                        view.showProgress(false);
                        view.isInternetAvailable(networkConnector.isConnected());
                    }

                    @Override
                    public void onComplete() {
                        view.showProgress(false);
                        isLoading = false;
                    }
                });
    }

    @Override
    public void searchHashTag(String hashTag) {
        service.checkHashTag(AppController.getInstance().getApiToken(), Constants.LANGUAGE, hashTag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Hash_tag_people_pojo>>() {
                    @Override
                    public void onNext(retrofit2.Response<Hash_tag_people_pojo> response) {

                        switch (response.code()) {
                            case 200:
                                view.setTag(response.body());
                                break;
                            case 401:
                                view.sessionExpired();
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        searchUserTag(hashTag);
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
                        view.isInternetAvailable(networkConnector.isConnected());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void searchUserTag(String userTag) {

        service.checkUserTag(AppController.getInstance().getApiToken(), Constants.LANGUAGE, userTag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Hash_tag_people_pojo>>() {
                    @Override
                    public void onNext(retrofit2.Response<Hash_tag_people_pojo> response) {
                        switch (response.code()) {

                            case 200:
                                view.setUser(response.body());
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        searchUserTag(userTag);
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
                        view.isInternetAvailable(networkConnector.isConnected());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void callApiOnScroll(String postId, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                page++;
                getComments(postId, PAGE_SIZE * page, PAGE_SIZE);
            }
        }
    }

    @Override
    public int getTotal() {
        return model.getTotalComments();
    }

    @Override
    public ClickListner getPresenter() {
        return this;
    }

    @Override
    public void onUserClick(int position,String s) {
        view.openProfile(model.getUserId(position));
    }

    @Override
    public void itemSelect(int position, boolean isSelected) {
        model.selectItem(position, isSelected);
    }

    @Override
    public void onDeleteComment(int position, Comment comment) {
        ArrayList<String> commentIds = new ArrayList<>();
        commentIds.add(comment.getId());
        Map<String,Object> map = new HashMap<>();
        map.put("postId",comment.getPostId());
        map.put("ArrayOfIds",commentIds);
        service.deleteComment(AppController.getInstance().getApiToken(),
                Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CommentResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<CommentResponse> response) {
                        if(response.code()==200){
                            model.commentDeleted(position,comment.getId());
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
    public void onLikeComment(int position, Comment comment, boolean isLike) {
        Map<String,Object> map = new HashMap<>();
        map.put("isNewLike", isLike);
        map.put("commentId",comment.getId());
        service.likeComment(AppController.getInstance().getApiToken(),
                Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<CommentResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<CommentResponse> response) {
                        if(response.code()==200){
                            model.commentLiked(position,comment.getId(),isLike);
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
