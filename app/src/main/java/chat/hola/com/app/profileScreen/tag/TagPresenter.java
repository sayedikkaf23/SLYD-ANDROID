package chat.hola.com.app.profileScreen.tag;

import android.os.Handler;
import android.util.Log;
import androidx.annotation.Nullable;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.stories.StoriesPresenter;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.PostUpdateData;
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

public class TagPresenter implements TagContract.Presenter {

  private static final String TAG = StoriesPresenter.class.getSimpleName();
  static final int PAGE_SIZE = Constants.PAGE_SIZE;
  private boolean isLoading = false;
  private boolean isLastPage = false;
  public static int page = 0;
  private SessionApiCall sessionApiCall = new SessionApiCall();

  @Nullable
  TagContract.View view;
  @Inject
  HowdooService service;
  @Inject
  NetworkConnector networkConnector;
  @Inject
  PostObserver postObserver;

  @Inject
  TagPresenter() {
  }

  @Override
  public void init() {
    if (view != null) view.setupRecyclerView();
  }

  @Override
  public void loadData(int skip, int limit, boolean isLiked) {
    isLoading = true;
    if (view != null) {
      view.isLoading(true);
    }
    if (skip == 0) {
      page = 0;
      isLastPage = false;
    }
    if (isLiked) {
      // my tagged posts
      taggedPosts(AppController.getInstance().getUserId(), skip, limit);
    } else {
      ///my posts
      service.getProfilePost(AppController.getInstance().getApiToken(), Constants.LANGUAGE, skip,
          limit)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Observer<Response<ProfilePostRequest>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Response<ProfilePostRequest> response) {
              isLoading = false;
              try {
                if (response.code() == 200) {

                  if (response.body() != null && response.body().getData() != null) {
                    isLastPage = response.body().getData().size() < PAGE_SIZE;
                    if (response.body().getData().size() > 0 && view != null) {
                      view.showData(response.body().getData(), skip == 0);
                      view.isLoading(false);
                    } else {
                      if (view != null) view.noData();
                    }
                  }
                } else if (response.code() == 204) {

                  isLastPage = true;
                  if (view != null) view.noData();
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
            }
          });
    }
  }

  @Override
  public void loadMemberData(String userId, int skip, int limit, boolean isLiked) {
    isLoading = true;
    if (view != null) {
      view.isLoading(true);
    }
    if (skip == 0) {
      page = 0;
      isLastPage = false;
    }
    if (isLiked) {
      //tagged post
      taggedPosts(userId, skip, limit);
    } else {
      //post
      service.getMemberPosts(AppController.getInstance().getApiToken(), Constants.LANGUAGE, userId,
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
                              loadMemberData(userId, skip, limit, isLiked);
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
              if (view != null) view.isLoading(false);
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
            }
          });
    }
  }

  public void taggedPosts(String userName, int skip, int limit) {
    isLoading = true;
    if (view != null) {
      view.isLoading(true);
    }
    if (skip == 0) {
      page = 0;
      isLastPage = false;
    }
    service.getTaggedPosts(AppController.getInstance().getApiToken(), Constants.LANGUAGE, userName,
        skip, limit)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Response<ProfilePostRequest>>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(Response<ProfilePostRequest> response) {
            try {
              isLoading = false;
              switch (response.code()) {
                case 200:
                  if (response.body() != null && response.body().getData() != null) {
                    isLastPage = response.body().getData().size() < PAGE_SIZE;
                    if (response.body().getData().size() > 0 && view != null) {
                      view.showData(response.body().getData(), skip == 0);
                      view.isLoading(false);
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
                              taggedPosts(userName, skip, limit);
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
            if(view!=null)view.isLoading(false);
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
  public void attachView(TagContract.View view) {
    this.view = view;
  }

  @Override
  public void detachView() {
    this.view = null;
  }

  public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount, String userName, boolean isLiked) {
    if (!isLoading && !isLastPage) {
      if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
          && firstVisibleItemPosition >= 0
          && totalItemCount >= PAGE_SIZE) {
        page++;
        taggedPosts(userName, page * PAGE_SIZE, PAGE_SIZE);
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

        postObserver.getTagPostsObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostUpdateData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PostUpdateData postUpdateData) {
                        if(view!=null){
                            view.viewAppear();
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
