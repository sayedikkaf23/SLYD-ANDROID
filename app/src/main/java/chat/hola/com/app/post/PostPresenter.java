package chat.hola.com.app.post;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.SocialShare;
import chat.hola.com.app.hastag.Hash_tag_people_pojo;
import chat.hola.com.app.models.CoinAmountResponse;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.PostUpdateData;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.post.model.CategoryResponse;
import chat.hola.com.app.post.model.ChannelResponse;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by ankit on 23/2/18.
 */

public class PostPresenter implements PostContract.Presenter {

  private static final String TAG = PostPresenter.class.getSimpleName();

  private Context context;
  private String path;
  private String type;
  private CompositeDisposable compositeDisposable;
  private SessionApiCall sessionApiCall = new SessionApiCall();
  @Inject
  PostObserver postObserver;
  @Inject
  PostContract.View view;
  @Inject
  SocialShare socialShare;
  @Inject
  HowdooService service;
  @Inject
  PostActivity postActivity;

  @Inject
  PostPresenter(Context context) {
    this.context = context;
    compositeDisposable = new CompositeDisposable();
  }

  @Override
  public void init(String path, String type) {
    this.path = path;
    this.type = type;
    view.displayMedia();
    view.applyFont();
  }

  @Override
  public void message(String message) {
    view.showMessage(message, 0);
  }

  @Override
  public void onBackPress() {
    view.onBackPress();
  }

  @Override
  public void getCategory() {
    //view.isLoadingData(true);
    service.getCategories(AppController.getInstance().getApiToken(), "en")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<CategoryResponse>>() {
          @Override
          public void onNext(Response<CategoryResponse> response) {
            switch (response.code()) {
              case 200:
                if (response.body().getData() != null) view.showCategory(response.body().getData());
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
                            getCategory();
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

            //view.isLoadingData(false);
          }

          @Override
          public void onComplete() {
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
                            searchHashTag(hashTag);
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
            e.printStackTrace();
          }

          @Override
          public void onComplete() {
          }
        });
  }

  private void informUsers(final String msg) {
    new Handler(Looper.getMainLooper()).post(new Runnable() {
      @Override
      public void run() {
        view.showMessage(msg, 0);
      }
    });
  }

  public void disposeObservable() {
    compositeDisposable.clear();
  }

  @Override
  public void getCategories() {
    service.getCategories(AppController.getInstance().getApiToken(), "en")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<CategoryResponse>>() {
          @Override
          public void onNext(Response<CategoryResponse> response) {
            switch (response.code()) {
              case 200:
                if (response.body().getData() != null) {
                  view.attacheCategory(response.body().getData());
                }
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
                            getCategories();
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
  public void getChannels() {
    service.getChannels(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Response<ChannelResponse>>() {
          @Override
          public void onSubscribe(Disposable d) {
            compositeDisposable.add(d);
          }

          @Override
          public void onNext(Response<ChannelResponse> response) {
            switch (response.code()) {
              case 200:
                if (response.body().getData() != null) {
                  view.attacheChannels(response.body().getData());
                }
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
                            getChannels();
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

  public void updatePost(Map<String, Object> map) {
    service.updatePost(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<ResponseBody>>() {
          @Override
          public void onNext(Response<ResponseBody> response) {
            switch (response.code()) {
              case 200:
                postObserver.postData(true);
                postObserver.postEditUpdate(new PostUpdateData(true,null));
                /**
                 * For comments and download
                 */
                postObserver.postEditedUpdate(new PostUpdateData((boolean)(map.get("allowComment")),(boolean)(map.get("allowDownload")),(boolean)(map.get("allowDuet")),(String)(map.get("postId")),map));
                view.updated();
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
                            updatePost(map);
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
    public void getSuggestedCoinAmount() {
        service.getCoinAmount(AppController.getInstance().getApiToken(),Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<CoinAmountResponse>>() {
                    @Override
                    public void onNext(Response<CoinAmountResponse> response) {
                        switch (response.code()){
                            case 200:
                                if(view!=null)
                                    view.onSuccessCoinAmount(response.body().getData());
                                break;
                            case 401:
                                break;
                            default:
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
