package chat.hola.com.app.home.social;

import android.os.Handler;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.connection.NetworkStateHolder;
import chat.hola.com.app.Networking.observer.NetworkObserver;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.collections.model.CollectionResponse;
import chat.hola.com.app.collections.model.CreateCollectionResponse;
import chat.hola.com.app.home.contact.GetFriends;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.home.model.Posts;
import chat.hola.com.app.home.social.model.ClickListner;
import chat.hola.com.app.home.social.model.SocialModel;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.post.ReportReason;
import com.ezcall.android.R;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h1>AddContactActivity</h1>
 *
 * @author 3Embed
 * @since 4/5/2018.
 */

public class SocialPresenter
    implements SocialContract.Presenter, ClickListner, SwipeRefreshLayout.OnRefreshListener {
  public static final String TAG = "AddContactPresenter";
  static final int PAGE_SIZE = Constants.PAGE_SIZE;
  private boolean isLoading = false;
  private boolean isLastPage = false;
  public static int page = 0;

  @Nullable
  SocialContract.View view;
  @Inject
  SocialModel model;
  @Inject
  HowdooService service;
  @Inject
  NetworkObserver networkObserver;
  @Inject
  NetworkStateHolder networkStateHolder;
  @Inject
  PostObserver postObserver;
  @Inject
  NetworkConnector networkConnector;
  @Inject
  SessionManager sessionManager;
  @Inject
  SessionApiCall sessionApiCall;

  public List<Data> arrayList = new ArrayList<>();
  private boolean observerAlreadyAdded = false;

  @Inject
  SocialPresenter() {
  }

  @Override
  public void callSocialApi(int offset, int limit, boolean load) {
    isLoading = true;
    if (view != null) {
      view.isLoading(load);
    }

    service.getPosts(AppController.getInstance().getApiToken(), Constants.LANGUAGE, offset, limit)
        //                .repeatWhen(objectObservable -> objectObservable.delay(10, TimeUnit.SECONDS))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<Posts>>() {
          @Override
          public void onNext(Response<Posts> response) {
            switch (response.code()) {
              case 200:
                isLastPage = response.body().getData().size() < PAGE_SIZE;
                model.setData(response.body().getData(), offset == 0);
                isLoading = false;
                if (view != null) {
                  view.showEmptyUi(false);
                  view.setData(response.body().getData(), offset == 0);
                  view.isLoading(false);
                }
                break;
              case 401:
                if (view != null) view.sessionExpired();
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
                            callSocialApi(offset, limit, load);
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

              case 204:
                model.clearData();
                if (view != null) view.showEmptyUi(true);
                break;
            }
          }

          @Override
          public void onError(Throwable e) {
            isLoading = false;
            if (view != null) {
              view.isInternetAvailable(networkConnector.isConnected());
              view.isLoading(false);
            }
          }

          @Override
          public void onComplete() {
            isLoading = false;
            if (view != null) {
              view.isLoading(false);
            }
          }
        });
  }

  @Override
  public void attachView(SocialContract.View view) {
    this.view = view;
  }

  @Override
  public void detachView() {
    this.view = null;
  }

  @Override
  public void onItemClick(int position, View v) {
    assert view != null;
    view.onItemClick(position);
  }

  @Override
  public void onUserClick(int position) {
    assert view != null;
    view.onUserClick(position);
  }

  @Override
  public void onUserClick(String userId) {
    assert view != null;
    view.openProfile(userId);
  }

  @Override
  public void onChannelClick(int position) {
    view.onChannelClick(model.getChannelId(position));
  }

  @Override
  public void viewAllComments(String postId) {
    assert view != null;
    view.viewAllComments(postId);
  }

  @Override
  public void send(int position) {
    assert view != null;
    view.send(position);
  }

  @Override
  public void share(int position) {
    assert view != null;
    view.share(position);
  }

  @Override
  public void onLikeClicked(int position, boolean liked) {
    assert view != null;
    view.like(position, liked);
  }

  @Override
  public void onLikerClick(Data data) {
    if (view != null) view.onLikeClick(data);
  }

  @Override
  public void onViewClick(Data data) {
    if (view != null) view.onViewClick(data);
  }

  @Override
  public void onReport(Data data) {
    if (view != null) {
      view.report(data);
    }
  }

  @Override
  public void onEdit(Data data) {
    if (view != null) {
      view.edit(data);
    }
  }

  @Override
  public void onDelete(Data data) {
    if (view != null) {
      view.delete(data);
    }
  }

  @Override
  public void onActionButtonClick(String title, String url) {
    if (view != null) view.onActionButtonClick(title, url);
  }

  @Override
  public void prefetchImage(int position) {
  }

  @Override
  public SocialPresenter getPresenter() {
    return this;
  }

  @Override
  public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoading && !isLastPage) {
      if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
          && firstVisibleItemPosition >= 0
          && totalItemCount >= PAGE_SIZE) {
        page++;
        callSocialApi(PAGE_SIZE * page, PAGE_SIZE, false);
      }
    }
  }

  @Override
  public void onRefresh() {
    Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        model.arrayList.clear();
        page = 0;
        callSocialApi(PAGE_SIZE * page, PAGE_SIZE, true);
        getCollections();
      }
    }, 1000);
  }

  @Override
  public void postObserver() {
    if (!observerAlreadyAdded) {
      observerAlreadyAdded = true;
      postObserver.getObservable()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new DisposableObserver<Boolean>() {
            @Override
            public void onNext(Boolean flag) {
              Handler handler = new Handler();
              handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                  model.arrayList.clear();
                  page = 0;
                  callSocialApi(PAGE_SIZE * page, PAGE_SIZE, true);
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
    }
  }

  @Override
  public void getFriends() {
    service.getFollowersFollowee(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<GetFriends>>() {
          @Override
          public void onNext(Response<GetFriends> response) {
            switch (response.code()) {
              case 200:
                if (view != null) view.friends(response.body().getData());
                break;
              case 204:
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
                            getFriends();
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
              case 401:
                if (view != null) view.sessionExpired();
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
  public void deletePost(String postId) {
    service.deletePost(AppController.getInstance().getApiToken(), Constants.LANGUAGE, postId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<ResponseBody>>() {
          @Override
          public void onNext(Response<ResponseBody> response) {
            switch (response.code()) {
              case 200:
                if (view != null) {
                  view.showMessage(null, R.string.deleted);
                }
                onRefresh();
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
  public void getReportReasons() {
    service.getReportReason(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<ReportReason>>() {
          @Override
          public void onNext(Response<ReportReason> response) {
            switch (response.code()) {
              case 200:
                if (view != null) {
                  view.addToReportList(response.body().getData());
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

  public List<Data> getDataList() {
    return model.getAllData();
  }

  public void dislike(String postId) {
    Map<String, String> params = new HashMap<>();
    params.put("userId", AppController.getInstance().getUserId());
    params.put("postId", postId);
    service.unlike(AppController.getInstance().getApiToken(), Constants.LANGUAGE, params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<ResponseBody>>() {
          @Override
          public void onNext(Response<ResponseBody> response) {
            switch (response.code()) {
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
                            dislike(postId);
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

  public void like(String postId, SessionManager sessionManager) {
    Map<String, String> map = new HashMap<>();
    map.put("userId", AppController.getInstance().getUserId());
    map.put("ip", sessionManager.getIpAdress());
    map.put("city", sessionManager.getCity());
    map.put("country", sessionManager.getCountry());
    map.put("postId", postId);

    service.like(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<ResponseBody>>() {
          @Override
          public void onNext(Response<ResponseBody> response) {
            switch (response.code()) {
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
                            like(postId, sessionManager);
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

  public void reportPost(String postId, String message, String reason) {
    Map<String, String> map = new HashMap<>();
    map.put("postId", postId);
    map.put("message", message);
    map.put("reason", reason);
    service.reportPost(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<ResponseBody>>() {

          @Override
          public void onNext(Response<ResponseBody> response) {
            switch (response.code()) {
              case 200:
                if (view != null) {
                  view.showMessage(null, R.string.reported_post);
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
                            reportPost(postId, message, reason);
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
  public void savedClick(int position, Boolean bookMarked) {
    assert view != null;
    view.savedClick(position, bookMarked);
  }

  @Override
  public void savedViewClick(int position, Data data) {
    assert view != null;
    view.savedViewClick(position, data);
  }

  @Override
  public void onSaveToCollectionClick(int position, Data data) {
    assert view != null;
    view.saveToCollectionClick(position, data);
  }

  @Override
  public void savedLongCick(int position, Boolean bookMarked) {

  }

  @Override
  public void saveToBookmark(int pos, String postId) {
    assert view != null;
    view.showProgress(true);
    Map<String, Object> params = new HashMap<>();
    params.put("postId", postId);
    service.postBookmark(AppController.getInstance().getApiToken(), Constants.LANGUAGE, params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<ResponseBody>>() {
          @Override
          public void onNext(Response<ResponseBody> response) {
            view.showProgress(false);
            switch (response.code()) {
              case 200:
                assert view != null;
                view.bookMarkPostResponse(pos, true);
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
                        view.showProgress(false);
                      }

                      @Override
                      public void onComplete() {
                        view.showProgress(false);
                      }
                    });
                sessionApiCall.getNewSession(service, sessionObserver);
                break;
            }
          }

          @Override
          public void onError(Throwable e) {
            view.showProgress(false);
          }

          @Override
          public void onComplete() {
            view.showProgress(false);
          }
        });
  }

  @Override
  public void deleteToBookmark(int pos, String postId) {
    //Map<String, String> params = new HashMap<>();
    //params.put("postId", postId);
    view.showProgress(true);
    service.deleteToBookmark(AppController.getInstance().getApiToken(), Constants.LANGUAGE, postId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<ResponseBody>>() {
          @Override
          public void onNext(Response<ResponseBody> response) {
            view.showProgress(false);
            switch (response.code()) {
              case 200:
                assert view != null;
                view.bookMarkPostResponse(pos, false);
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
                        view.showProgress(false);
                      }

                      @Override
                      public void onComplete() {
                        view.showProgress(false);
                      }
                    });
                sessionApiCall.getNewSession(service, sessionObserver);
                break;
            }
          }

          @Override
          public void onError(Throwable e) {
            view.showProgress(false);
          }

          @Override
          public void onComplete() {
            view.showProgress(false);
          }
        });
  }

  @Override
  public void addPostToCollection(String collectionId, String postId) {
    view.showProgress(true);
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
            view.showProgress(false);
            switch (response.code()) {
              case 200:
                view.postAddedToCollection();
                break;
            case 502:
                if(view!=null) view.showMessage("",R.string.alredy_added_in_collection);
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
                        if (view != null) view.showProgress(false);
                      }

                      @Override
                      public void onComplete() {
                        if (view != null) view.showProgress(false);
                      }
                    });
                sessionApiCall.getNewSession(service, sessionObserver);
                break;
            }
          }

          @Override
          public void onError(Throwable e) {
            if (view != null) view.showProgress(false);
          }

          @Override
          public void onComplete() {
            if (view != null) view.showProgress(false);
          }
        });
  }

  @Override
  public void getCollections() {
    service.getCollections(AppController.getInstance().getApiToken(), Constants.LANGUAGE, 0, 100)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<CollectionResponse>>() {
          @Override
          public void onNext(Response<CollectionResponse> response) {
            if (view != null) view.showProgress(false);
            switch (response.code()) {
              case 200:
                if (view != null) view.collectionFetched(response.body().getData());
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
            if (view != null) view.showProgress(false);
          }

          @Override
          public void onComplete() {
            if (view != null) view.showProgress(false);
          }
        });
  }

  @Override
  public void createCollection(String collectionName, String collectionImage, String postId) {
    if (view != null) view.showProgress(true);
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
            if (view != null) view.showProgress(false);
            switch (response.code()) {
              case 200:
                if (view != null) view.collectionCreated();
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
                        if (view != null) view.showProgress(false);
                      }

                      @Override
                      public void onComplete() {
                        if (view != null) view.showProgress(false);
                      }
                    });
                sessionApiCall.getNewSession(service, sessionObserver);
                break;
            }
          }

          @Override
          public void onError(Throwable e) {
            if (view != null) view.showProgress(false);
          }

          @Override
          public void onComplete() {
            if (view != null) view.showProgress(false);
          }
        });
  }
}
