package chat.hola.com.app.home.popular;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.EcomPythonService;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.HowdooServiceTrending;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.collections.model.CollectionResponse;
import chat.hola.com.app.collections.model.CreateCollectionResponse;
import chat.hola.com.app.comment.model.CommentResponse;
import chat.hola.com.app.home.contact.GetFriends;
import chat.hola.com.app.home.home.ProductsResponse;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.home.model.Posts;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.PostUpdateData;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.models.TransferResponse;
import chat.hola.com.app.models.ViewPost;
import chat.hola.com.app.models.ViewPostRequest;
import chat.hola.com.app.post.ReportReason;
import chat.hola.com.app.profileScreen.discover.contact.pojo.FollowResponse;
import chat.hola.com.app.profileScreen.followers.Model.FollowersResponse;
import chat.hola.com.app.socialDetail.model.PostResponse;
import com.ezcall.android.R;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;

public class PopularPresenter implements PopularContract.Presenter {

    private static final String TAG = PopularPresenter.class.getSimpleName();
    public static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public int page = 0;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    private boolean isCommentLoading = false;
    private boolean isCommentLastPage = false;
    public int commentPage = 0;

    private boolean isLikeLoading = false;
    private boolean isLikeLastPage = false;
    public int likePage = 0;

    //@Inject
    PopularContract.View mView;
    @Inject
    HowdooService service;
    @Inject
    EcomPythonService mEcomPythonService;
    @Inject
    HowdooServiceTrending serviceTrending;
    @Inject
    PopularModel model;
    @Inject
    PostObserver postObserver;
    @Inject
    SessionManager sessionManager;

    @Inject
    public PopularPresenter() {

    }

    /*
     * Bug Title: the user is shown as following in the home page even after we unfollow the user in the
     * Bug Id: DUBAND012
     * Fix Description: setup new observer to listen user action
     * Developer Name: Hardik
     * Fix Date: 6/4/2021
     * */
    public void subscribeSavedObserver() {
        postObserver.getSavedObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostUpdateData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        System.out.println(TAG + ":subscribe:" + d.isDisposed());
                    }

                    @Override
                    public void onNext(PostUpdateData postUpdateData) {
                        if (mView != null && !postUpdateData.getFrom().equals(Constants.PostUpdate.POPULAR_FRAGMENT)) {
                            mView.updateSavedOnObserve(postUpdateData);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println(TAG + ":" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {
                        System.out.println(TAG + "On Complete");
                    }
                });

        postObserver.getPostEditedObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostUpdateData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PostUpdateData postUpdateData) {
                        if (mView != null) {
                            mView.postUpdated(postUpdateData.getPostId(), postUpdateData.isAllowComment(),
                                    postUpdateData.isAllowDownload(), postUpdateData.isAllowDuet(),postUpdateData.getBody());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        postObserver.getFollowObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Pair<Boolean, String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Pair<Boolean, String> pair) {
                        if(mView!=null)
                            mView.unFollowUser(pair.second);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void getPostById(String postId, boolean isJustForView) {
        if (AppController.getInstance().isGuest()) return;
        Map<String, Object> params = model.getParams();
        service.postDetail(AppController.getInstance().getApiToken(), Constants.LANGUAGE, postId,
                String.valueOf(params.get("city")), String.valueOf(params.get("country")),
                String.valueOf(params.get("ip")),
                Double.parseDouble(String.valueOf(params.get("latitude"))),
                Double.parseDouble(String.valueOf(params.get("longitude"))))

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<PostResponse>>() {
                    @Override
                    public void onNext(Response<PostResponse> response) {
                        switch (response.code()) {
                            case 200:
                                if (!isJustForView) {
                                    postObserver.postData(true);
                                    if (mView != null) mView.setData(response.body().getData());
                                }
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
                                                        getPostById(postId, isJustForView);
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
                        Log.i(TAG, "onError: ");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void getReportReasons() {
        if (AppController.getInstance().isGuest()) return;
        service.getReportReason(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ReportReason>>() {
                    @Override
                    public void onNext(Response<ReportReason> response) {
                        switch (response.code()) {
                            case 200:
                                if (mView != null) mView.addToReportList(response.body().getData());
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
                                                        getReportReasons();
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
    public void like(String postId) {
        service.like(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                model.getParams(postId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                if (mView != null) mView.liked(true, false, postId);
                                //postObserver.postData(true);
                                postObserver.postLikeUnlikeObject(new PostUpdateData(true, postId));
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
                                                        like(postId);
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
                            default:
                                if (mView != null) mView.liked(true, true, postId);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) mView.liked(true, true, postId);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void unlike(String postId) {
        service.unlike(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                model.getParams1(postId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                if (mView != null) mView.liked(false, false, postId);
                                postObserver.postData(true);
                                postObserver.postLikeUnlikeObject(new PostUpdateData(false, postId));
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
                                                        unlike(postId);
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
                            default:
                                if (mView != null) mView.liked(false, true, postId);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) mView.liked(false, true, postId);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void deletePost(String postId) {
        service.deletePost(AppController.getInstance().getApiToken(), Constants.LANGUAGE, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 200:
                                if (mView != null) {
                                    mView.deleted(postId);
                                    mView.showMessage(null, R.string.deleted);
                                }
                                postObserver.postData(true);
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
                                                        deletePost(postId);
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
    public void reportPost(int position,String postId, String reason, String message) {
        service.reportPost(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                model.getReasonParams(postId, reason, message))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {

                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                          case 200:{
                            if (mView != null) {
                              mView.showMessage(null, R.string.reported_post);
                              mView.postReported(position,postId,true);
                            }break;}
                          case 409:{
                            if (mView != null) {
                              mView.showMessage(null, R.string.already_reported_post);
                              mView.postReported(position,postId,false);
                            }break;}
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
                                                        reportPost(position,postId, reason, message);
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
    public void follow(String followingId) {
        Map<String, Object> map = new HashMap<>();
        map.put("followingId", followingId);
        service.follow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> response) {
                        switch (response.code()) {
                            case 200:
                                postObserver.postData(true);
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
                                                        follow(followingId);
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
    public void unfollow(String followingId) {
        Map<String, Object> map = new HashMap<>();
        map.put("followingId", followingId);
        service.unfollow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> response) {
                        switch (response.code()) {
                            case 200:
                                if (mView != null) mView.unFollowUser(followingId);
                                postObserver.postData(true);
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
                                                        unfollow(followingId);
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
    public void saveToBookmark(int pos, String postId) {
        if (mView != null) mView.showProgress(true);
        Map<String, Object> params = new HashMap<>();
        params.put("postId", postId);
        service.postBookmark(AppController.getInstance().getApiToken(), Constants.LANGUAGE, params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (mView != null) mView.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                assert mView != null;
                                mView.bookMarkPostResponse(pos, true);
                                postObserver.postSavedUpdate(new PostUpdateData(true, postId, Constants.PostUpdate.POPULAR_FRAGMENT));
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
                                                        saveToBookmark(pos, postId);
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                if (mView != null) mView.showProgress(false);
                                            }

                                            @Override
                                            public void onComplete() {
                                                if (mView != null) mView.showProgress(false);
                                            }
                                        });
                                sessionApiCall.getNewSession(service, sessionObserver);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) mView.showProgress(false);
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) mView.showProgress(false);
                    }
                });
    }

    @Override
    public void deleteToBookmark(int pos, String postId) {
        if (mView != null) mView.showProgress(true);
        service.deleteToBookmark(AppController.getInstance().getApiToken(), Constants.LANGUAGE, postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (mView != null) mView.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                assert mView != null;
                                mView.bookMarkPostResponse(pos, false);
                                postObserver.postSavedUpdate(new PostUpdateData(false, postId, Constants.PostUpdate.POPULAR_FRAGMENT));
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
                                                        deleteToBookmark(pos, postId);
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                if (mView != null) mView.showProgress(false);
                                            }

                                            @Override
                                            public void onComplete() {
                                                if (mView != null) mView.showProgress(false);
                                            }
                                        });
                                sessionApiCall.getNewSession(service, sessionObserver);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) mView.showProgress(false);
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) mView.showProgress(false);
                    }
                });
    }

    @Override
    public void createCollection(String collectionName, String collectionImage, String postId) {
        if (mView != null) mView.showProgress(true);
        Map<String, Object> map = new HashMap<>();
        ArrayList<String> postIds = new ArrayList<>();
        postIds.add(postId);
        map.put("collectionName", collectionName);
        map.put("coverImage", collectionImage);
        map.put("postId", postIds);
        service.createCollection(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<CreateCollectionResponse>>() {
                    @Override
                    public void onNext(Response<CreateCollectionResponse> response) {
                        if (mView != null) mView.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                if (mView != null) mView.collectionCreated();
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
                                                        createCollection(collectionName, collectionImage, postId);
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                if (mView != null) {
                                                    mView.showProgress(false);
                                                }
                                            }

                                            @Override
                                            public void onComplete() {
                                                if (mView != null) mView.showProgress(false);
                                            }
                                        });
                                sessionApiCall.getNewSession(service, sessionObserver);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) {
                            mView.showProgress(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) {
                            mView.showProgress(false);
                        }
                    }
                });
    }

    @Override
    public void addPostToCollection(String collectionId, String postId) {
        if (mView != null) mView.showProgress(true);
        Map<String, Object> map = new HashMap<>();
        ArrayList<String> postIds = new ArrayList<>();
        postIds.add(postId);
        map.put("postIds", postIds);
        map.put("collectionId", collectionId);
        service.addToCollection(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (mView != null) mView.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                if (mView != null) mView.postAddedToCollection();
                                break;
                            case 502:
                                if (mView != null)
                                    mView.showMessage("", R.string.alredy_added_in_collection);
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
                                                        addPostToCollection(collectionId, postId);
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                if (mView != null) mView.showProgress(false);
                                            }

                                            @Override
                                            public void onComplete() {
                                                if (mView != null) mView.showProgress(false);
                                            }
                                        });
                                sessionApiCall.getNewSession(service, sessionObserver);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) mView.showProgress(false);
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) mView.showProgress(false);
                    }
                });
    }

    @Override
    public void callProductsAPi(ArrayList<String> productIds) {
        if (mView != null) mView.showProgress(true);
        Map<String, Object> map = new HashMap<>();
        map.put("productIds", productIds);
        mEcomPythonService.getProducts(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ProductsResponse>>() {
                    @Override
                    public void onNext(Response<ProductsResponse> response) {
                        if (mView != null) mView.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                if (mView != null) mView.showProducts(response.body().getData().getData());
                                break;
                            case 502:
                                if (mView != null)
                                    mView.showMessage("", R.string.alredy_added_in_collection);
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
                                                      callProductsAPi(productIds);
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                if (mView != null) mView.showProgress(false);
                                            }

                                            @Override
                                            public void onComplete() {
                                                if (mView != null) mView.showProgress(false);
                                            }
                                        });
                                sessionApiCall.getNewSession(service, sessionObserver);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (mView != null) mView.showProgress(false);
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) mView.showProgress(false);
                    }
                });
    }

    @Override
    public void getCollections() {
        if (AppController.getInstance().isGuest()) return;
        service.getCollections(AppController.getInstance().getApiToken(), Constants.LANGUAGE, 0, 100)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<CollectionResponse>>() {
                    @Override
                    public void onNext(Response<CollectionResponse> response) {
                        if (mView != null) mView.showProgress(false);
                        switch (response.code()) {
                            case 200:
                                if (mView != null)
                                    mView.collectionFetched(response.body().getData());
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
                                                        getCollections();
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
                        if (mView != null) mView.showProgress(false);
                    }

                    @Override
                    public void onComplete() {
                        if (mView != null) mView.showProgress(false);
                    }
                });
    }

    /*
     * Bug Title: not able to forward posts as it says add Friends to send even though I am following users
     * Bug Id: #2698
     * Fix Desc: calling from resume for refresh
     * Fix Dev: Hardik
     * Fix Date: 22/6/21
     * */
    public void getFollowUsers() {
        if (AppController.getInstance().isGuest()) return;
        service.getFollowersFollowee(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<GetFriends>>() {
                    @Override
                    public void onNext(Response<GetFriends> response) {
                        switch (response.code()) {
                            case 200:
                                if (mView != null) mView.followers(response.body().getData());
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
                                                        getFollowUsers();
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

    public void callSocialApi(int offset, int limit, boolean entirelyNewList,
                              boolean refreshRequest) {

        if (entirelyNewList) {
            page = 0;
        }
        if (mView != null) {


            if (refreshRequest || entirelyNewList)
                mView.showProgress(true);
        }

        isLoading = true;
        if (!AppController.getInstance().isGuest()) {
            serviceTrending.getPopularPosts(AppController.getInstance().getApiToken(), Constants.LANGUAGE, offset, limit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Response<Posts>>() {
                        @Override
                        public void onNext(Response<Posts> response) {
                            isLoading = false;
                            if (mView != null) mView.showProgress(false);
                            switch (response.code()) {
                                case 200:
                                    if (!refreshRequest) {
                                        assert response.body() != null;
                                        isLastPage = response.body().getData().size() < PAGE_SIZE;
                                    }

                                    if (mView != null) {
                                        mView.setDataList(response.body().getData(), entirelyNewList, refreshRequest);
                                    }
                                    break;
                                case 204:
                                    isLastPage = true;
                                    if (entirelyNewList) {
                                        assert mView != null;
                                        mView.showEmpty(true);
                                    }
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
                                                            callSocialApi(offset, limit, entirelyNewList, refreshRequest);
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
                            isLoading = false;
                            if (mView != null) mView.showProgress(false);
                        }

                        @Override
                        public void onComplete() {
                            isLoading = false;
                            if (mView != null) mView.showProgress(false);
                        }
                    });

        } else {
            serviceTrending.getGuestPosts(sessionManager.getGuestToken(), Constants.LANGUAGE, offset, limit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Response<Posts>>() {
                        @Override
                        public void onNext(Response<Posts> response) {
                            isLoading = false;
                            if (mView != null) mView.showProgress(false);
                            switch (response.code()) {
                                case 200:
                                    if (!refreshRequest) {
                                        assert response.body() != null;
                                        isLastPage = response.body().getData().size() < PAGE_SIZE;
                                    }

                                    if (mView != null) {
                                        mView.setDataList(response.body().getData(), entirelyNewList, refreshRequest);
                                    }
                                    break;
                                case 204:
                                    isLastPage = true;
                                    if (entirelyNewList) {
                                        assert mView != null;
                                        mView.showEmpty(true);
                                    }
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
                                                            callSocialApi(offset, limit, entirelyNewList, refreshRequest);
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
                            isLoading = false;
                            if (mView != null) mView.showProgress(false);
                        }

                        @Override
                        public void onComplete() {
                            isLoading = false;
                            if (mView != null) mView.showProgress(false);
                        }
                    });
        }

    }

    public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount,
                                int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                page++;
                callSocialApi(PAGE_SIZE * page, PAGE_SIZE, false, false);
            }
        }
    }

    @Override
    public void attachView(PopularContract.View view) {
        this.mView = view;
    }

    @Override
    public void detachView() {
        this.mView = null;
    }

    public void getComments(String postId, int offset, int limit) {
        isCommentLoading = true;

        if (offset == 0) {
            commentPage = 0;
            isCommentLastPage = false;
        }

        service.getComment(AppController.getInstance().getApiToken(), Constants.LANGUAGE, postId, offset, limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<chat.hola.com.app.comment.model.Response>>() {
                    @Override
                    public void onNext(Response<chat.hola.com.app.comment.model.Response> response) {
                        isCommentLoading = false;
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body() != null) {
                                        isCommentLastPage = response.body().getData().size() < PAGE_SIZE;
                                        mView.setComments(response.body().getData(), offset == 0);
                                        mView.noComment(response.body().getData().isEmpty());
                                    }
                                    break;
                                case 204:
                                    mView.noComment(true);
                                    isCommentLastPage = true;
                                    break;
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isCommentLoading = false;
                    }

                    @Override
                    public void onComplete() {
                        isCommentLoading = false;
                    }
                });
    }

    public void addComment(String postId, String comment) {
        service.addComment(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                getParamsAddComment(postId, comment))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<CommentResponse>>() {
                    @Override
                    public void onNext(Response<CommentResponse> response) {
                        switch (response.code()) {
                            case 200:
                                mView.addToList(response.body().getComments(),postId);
                                mView.commented(true);
                                break;
                            case 401:
                                mView.sessionExpired();
                                mView.commented(false);
                                break;

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.commented(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    Map<String, Object> getParamsAddComment(String postId, String comment) {
        Map<String, Object> map = new HashMap<>();
        if (!TextUtils.isEmpty(comment)) {
            map.put("comment", comment);
            map.put("postId", postId);

            //hashtag
            String regexPattern = "(#\\w+)";
            Pattern p = Pattern.compile(regexPattern);
            Matcher m = p.matcher(comment);
            StringBuilder hashtag = new StringBuilder();
            while (m.find()) {
                hashtag.append(",").append(m.group(1));
            }
            map.put("hashTags", hashtag.toString());

            //userTag
            String regexPattern1 = "(@\\w+)";
            Pattern p1 = Pattern.compile(regexPattern1);
            Matcher m1 = p1.matcher(comment);
            int i = 0;
            ArrayList<String> strings = new ArrayList<>();
            String userTag[] = new String[i + 1];
            while (m1.find()) {
                strings.add(m1.group(1).replace("@", ""));
            }
            map.put("userTags", strings);

        }
        map.put("postedBy", AppController.getInstance().getUserId());
        map.put("ip", sessionManager.getIpAdress());
        map.put("city", sessionManager.getCity());
        map.put("country", sessionManager.getCountry());
        return map;
    }

    public void likers(String postId, int skip, int limit) {
        isLikeLoading = true;
        service.likers(AppController.getInstance().getApiToken(), Constants.LANGUAGE, postId, skip,
                limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowersResponse>>() {
                    @Override
                    public void onNext(Response<FollowersResponse> followersResponse) {
                        isLikeLoading = false;
                        switch (followersResponse.code()) {
                            case 204:
                                mView.clearLikeList(true);
                                mView.noLikes(true);
                                break;
                            case 200:
                                mView.noLikes(followersResponse.body().getData().isEmpty());
                                mView.clearLikeList(skip == 0);
                                isLikeLastPage = followersResponse.body().getData().size() < PAGE_SIZE;
                                mView.showLikes(followersResponse.body().getData());
                                break;
                            case 401:
                                mView.sessionExpired();
                                break;

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLikeLoading = false;
                    }

                    @Override
                    public void onComplete() {
                        isLikeLoading = false;
                    }
                });
    }

    public void searchLikers(String postId, int skip, int limit, String query) {
        service.searchLikers(AppController.getInstance().getApiToken(), Constants.LANGUAGE, postId,
                query, skip, limit)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowersResponse>>() {
                    @Override
                    public void onNext(Response<FollowersResponse> followersResponse) {
                        switch (followersResponse.code()) {
                            case 200:
                                isLastPage = followersResponse.body().getData().size() < PAGE_SIZE;
                                mView.clearLikeList(skip == 0);
                                mView.showLikes(followersResponse.body().getData());
                                break;
                            case 204:
                                mView.clearLikeList(true);
                                break;
                            case 401:
                                mView.sessionExpired();
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

    public void updatePostViewedToServer(ArrayList<ViewPost> viewPost) {

        if (viewPost.isEmpty())
            return;

        ViewPostRequest request = new ViewPostRequest();
        request.setViewPosts(viewPost);
        serviceTrending.viewPost(AppController.getInstance().getApiToken(), Constants.LANGUAGE, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (response.code() == 200) {
                            clearViewPostList();
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

    ArrayList<ViewPost> viewPost = new ArrayList<>();

    public ArrayList<ViewPost> getViewPost() {
        return viewPost;
    }

    public void addToViewPostList(Data data) {
        //Log.d(TAG, "view post: "+data.getPostId());
        if (!AppController.getInstance().getDbController()
                .isPostAlreadyViewed(data.getPostId(), data.getUserId())) {

            viewPost.add(new ViewPost(sessionManager.getCity(),
                    sessionManager.getCountry(),
                    String.valueOf(sessionManager.getLatitude()),
                    String.valueOf(sessionManager.getLongitude()),
                    data.getPostId(),
                    String.valueOf(data.getMediaType1()),
                    "" + 30,
                    AppController.getInstance().getUserId(),
                    "" + 30
            ));

            Map<String, Object> map = new HashMap<>();
            map.put("city", sessionManager.getCity());
            map.put("country", sessionManager.getCountry());
            map.put("latitude", sessionManager.getLatitude());
            map.put("longitude", sessionManager.getLongitude());
            map.put("postId", data.getPostId());
            map.put("mediaType", data.getMediaType1());
            map.put("videoDuration", 30);
            map.put("viewerId", data.getUserId());
            map.put("watchedDuration", 30);

            AppController.getInstance().getDbController().addViewPost(map);
        }

        if (viewPost.size() >= PAGE_SIZE)
            updatePostViewedToServer(viewPost);
    }

    public void clearViewPostList() {
        viewPost.clear();
    }

    @Override
    public void deleteComment(int position, String postId, String commentId) {
        ArrayList<String> commentIds = new ArrayList<>();
        commentIds.add(commentId);
        Map<String,Object> map = new HashMap<>();
        map.put("postId",postId);
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
                            mView.commentDeleted(position,commentId);
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
    public void likeComment(int position, String commentId, boolean isLike) {
        Map<String,Object> map = new HashMap<>();
        map.put("isNewLike", isLike);
        map.put("commentId",commentId);
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
                      if (response.code() == 200) {
                        mView.commentLiked(position, commentId, isLike);
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

  /**
   * For paid posts
   */
  @Override
  public void payForPost(Data data, int position, boolean b) {
    Map<String, Object> map = new HashMap<>();
    map.put("postId", data.getPostId());
    service.buyPost(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<ResponseBody>>() {
          @Override
          public void onNext(@NotNull Response<ResponseBody> response) {
            switch (response.code()) {
              case 200:
                data.setPurchased(true);
                data.setThumbnailUrl1(Utilities.getPurchaseMediaUrl(data.getThumbnailUrl1()));
                if (data.getMediaType1() == 0) {
                  data.setImageUrl1(Utilities.getPurchaseMediaUrl(data.getImageUrl1()));
                }
                if (mView != null) mView.onPostPurchased(data, position);
                  postObserver.postPaidPostsObservableEmitter(new Pair("isFor","1"));
                break;
              case 403:
                /*INSUFFICIENT BALANCE*/
                if (mView != null) mView.insufficientBalance();
                break;
              case 405:
                if (mView != null) {
                  mView.showMessage(
                      chat.hola.com.app.Utilities.Error.getErrorMessage(response.errorBody()), 0);
                }
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
  public void subscribeProfile(Data data, int position) {
    Map<String, Object> map = new HashMap<>();
    map.put("userIdToFollow", data.getUserId());
    service.subscribeStarUSer(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<ResponseBody>>() {
          @Override
          public void onNext(Response<ResponseBody> response) {
            switch (response.code()) {
              case 200:{
                if (mView != null) {
                  data.setPurchased(true);
                  mView.onUserSubscribed(data, position);
                    postObserver.postPaidPostsObservableEmitter(new Pair("isFor","1"));  }
                break;}
              case 403:
                /*INSUFFICIENT BALANCE*/
                if (mView != null) mView.insufficientBalance();
                break;
              case 405:
                if (mView != null) {
                  mView.showMessage(
                      chat.hola.com.app.Utilities.Error.getErrorMessage(response.errorBody()), 0);
                }
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
  public void subscribeStarUser(Data data, int position) {
    Map<String, Object> map = new HashMap<>();
    map.put("userIdToFollow", data.getUserId());
    service.subscribeStarUSer(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<ResponseBody>>() {
          @Override
          public void onNext(Response<ResponseBody> response) {
            switch (response.code()) {
              case 200:{
                if (mView != null) {
                  data.setPurchased(true);
                  mView.onUserSubscribed(data, position);
                    postObserver.postPaidPostsObservableEmitter(new Pair("isFor","1"));  }
                break;}
              case 403:
                /*INSUFFICIENT BALANCE*/
                if (mView != null) mView.insufficientBalance();
                break;
              case 405:
                if (mView != null) {
                  mView.showMessage(
                      chat.hola.com.app.Utilities.Error.getErrorMessage(response.errorBody()), 0);
                }
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
  public void sendTipRequest(Data data, String coin, String desc, int position) {
    Map<String, Object> map = new HashMap<>();
    map.put("postId", data.getPostId());
    map.put("type", "Tip");
    map.put("senderId", AppController.getInstance().getUserId());
    map.put("senderName", AppController.getInstance().getUserName());
    map.put("senderType", "user");
    map.put("coins", coin);
    map.put("receiverId", data.getUserId());
    map.put("receiverType", "user");
    map.put("receiverName", data.getUsername());
    map.put("notes", desc);
    map.put("description", desc);
    service.transferCoin(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<TransferResponse>>() {
          @Override
          public void onNext(Response<TransferResponse> response) {
            switch (response.code()) {
              case 200:
                if (mView != null) {
                  mView.sendTipSuccess(data,coin,position);
                }
                break;
              case 403:
                /*INSUFFICIENT BALANCE*/
                if (mView != null) mView.insufficientBalance();
                break;
              case 405:
                if (mView != null) {
                  mView.showMessage(
                      chat.hola.com.app.Utilities.Error.getErrorMessage(response.errorBody()), 0);
                }
              case 401:
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
