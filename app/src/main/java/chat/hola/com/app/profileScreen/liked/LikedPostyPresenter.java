package chat.hola.com.app.profileScreen.liked;

import android.os.Handler;
import android.util.Log;
import androidx.annotation.Nullable;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.stories.StoriesPresenter;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.PostUpdateData;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.story.model.ProfilePostRequest;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by ankit on 23/2/18.
 */

public class LikedPostyPresenter implements LikedPostContract.Presenter {

  private static final String TAG = StoriesPresenter.class.getSimpleName();
  static final int PAGE_SIZE = Constants.PAGE_SIZE;
  private boolean isLoading = false;
  private boolean isLastPage = false;
  public static int page = 0;
  private SessionApiCall sessionApiCall = new SessionApiCall();
  @Nullable
  LikedPostContract.View view;
  @Inject
  HowdooService service;
  @Inject
  NetworkConnector networkConnector;
  @Inject
  PostObserver postObserver;

  @Inject
  LikedPostyPresenter() {
  }

  @Override
  public void init() {
    if (view != null) view.setupRecyclerView();
  }

  void likeUnlikeObserver() {
    postObserver.getLikeUnlikeObservable()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<PostUpdateData>() {
          @Override
          public void onNext(PostUpdateData likeUnlike) {
            if (view != null) {
              view.updateListOnObserve(likeUnlike);
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
  public void loadData(int skip, int limit, boolean isLiked) {
    isLoading = true;
    if (view != null) {
      view.isLoading(true);
    }

    if (skip == 0) {
      isLastPage = false;
      page = 0;
    }
    // my liked posts
    likedPosts(AppController.getInstance().getUserId(), skip, limit);
  }

  @Override
  public void loadMemberData(String userId, int skip, int limit, boolean isLiked) {
    isLoading = true;
    if (view != null) {
      view.isLoading(true);
    }
    if (skip == 0) {
      isLastPage = false;
      page = 0;
    }
    //liked post
    likedPosts(userId, skip, limit);
  }

  private void likedPosts(String userId, int skip, int limit) {

    service.getLikedPosts(AppController.getInstance().getApiToken(), Constants.LANGUAGE, userId,
        skip, limit)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Response<ProfilePostRequest>>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(Response<ProfilePostRequest> response) {
            isLoading = false;
            if (view != null) view.isLoading(false);

            try {
              switch (response.code()) {
                case 200:
                  if (response.body() != null && response.body().getData() != null) {
                    isLastPage = response.body().getData().size() < PAGE_SIZE;
                    if (response.body().getData().size() > 0 && view != null) {
                      view.showData(response.body().getData(), skip == 0);
                    } else {
                      if (view != null) view.noData();
                    }
                  }
                  break;
                case 204:

                  isLastPage = true;
                  if (view != null) view.noData();
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
                              likedPosts(userId, skip, limit);
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
            if (view != null) {
              view.isInternetAvailable(networkConnector.isConnected());
              view.showMessage(e.getMessage(), 0);
              view.isLoading(false);
            }
          }

          @Override
          public void onComplete() {
            isLoading = false;
            if (view != null) view.isLoading(false);
          }
        });
  }

  @Override
  public void unlike(String postId) {
    Map<String, String> map = new HashMap<>();
    map.put("userId", AppController.getInstance().getUserId());
    map.put("postId", postId);
    service.unlike(AppController.getInstance().getApiToken(), "en", map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Response<ResponseBody>>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

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
            }
          }

          @Override
          public void onError(Throwable e) {
            Log.e(TAG, "unlike failed!!");
          }

          @Override
          public void onComplete() {
          }
        });
  }

  @Override
  public void like(String postId, SessionManager sessionManager) {
    Map<String, String> map = new HashMap<>();
    map.put("userId", AppController.getInstance().getUserId());
    map.put("ip", sessionManager.getIpAdress());
    map.put("city", sessionManager.getCity());
    map.put("country", sessionManager.getCountry());
    map.put("postId", postId);
    service.like(AppController.getInstance().getApiToken(), "en", map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Response<ResponseBody>>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

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
            Log.w(TAG, "like failed!!");
          }

          @Override
          public void onComplete() {
          }
        });
  }

  @Override
  public void attachView(LikedPostContract.View view) {
    this.view = view;
  }

  @Override
  public void detachView() {
    this.view = null;
  }

  public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount, String userId, boolean isLiked) {
    if (!isLoading && !isLastPage) {
      if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
          && firstVisibleItemPosition >= 0
          && totalItemCount >= PAGE_SIZE) {
        page++;
        if (userId != null && !userId.equals("")) {
          if (userId.equals(AppController.getInstance().getUserId())) {
            loadData(page * PAGE_SIZE, PAGE_SIZE, isLiked);
          } else {
            loadMemberData(userId, page * PAGE_SIZE, PAGE_SIZE, isLiked);
          }
        }
      }
    }
  }

    public void initObservers() {
        postObserver.getSavedObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostUpdateData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PostUpdateData postUpdateData) {
                        if(view!=null){
                            view.updateSavedOnObserve(postUpdateData);
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
