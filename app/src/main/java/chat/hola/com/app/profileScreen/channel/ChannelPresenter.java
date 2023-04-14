package chat.hola.com.app.profileScreen.channel;

import android.os.Handler;
import androidx.annotation.Nullable;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.PostUpdateData;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.channel.Model.ChannelResponse;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h>ChannelPresenter</h>
 *
 * @author 3Embed.
 * @since 23/2/18.
 */

public class ChannelPresenter implements ChannelContract.Presenter {

  private static final String TAG = ChannelPresenter.class.getSimpleName();
  private SessionApiCall sessionApiCall = new SessionApiCall();

  @Nullable
  ChannelContract.View view;
  @Inject
  HowdooService service;
  @Inject
  PostObserver postObserver;

  @Inject
  ChannelPresenter() {
  }

  @Override
  public void init() {
    if (view != null) view.setupRecyclerView();
  }

  @Override
  public void getChannelData(int skip, int limit, String userId) {
    if (view != null) view.isLoading(true);
    service.getChannelPost(AppController.getInstance().getApiToken(), "en",
        userId == null ? AppController.getInstance().getUserId() : userId, skip, limit)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<ChannelResponse>>() {
          @Override
          public void onNext(Response<ChannelResponse> channelResponse) {
            if (view != null) view.isLoading(false);
            switch (channelResponse.code()) {
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
                            getChannelData(skip, limit, userId);
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
              case 200:
                if (view != null) {
                  if( channelResponse.body()  != null){

                  view.showChannelData(channelResponse.body().getData());}
                }
                break;
              case 401:
                if (view != null) view.sessionExpired();
                break;
              case 204:
                if (view != null) view.noData();
                break;
              default:
                if (view != null) view.showEmptyUi(true);
                break;
            }
          }

          @Override
          public void onError(Throwable e) {

            if (view != null) {
              view.isLoading(false);
            }
          }

          @Override
          public void onComplete() {
            if (view != null) {
              view.isLoading(false);
            }
          }
        });
  }

  @Override
  public void deleteChannel(String channelId) {
    if (view != null) view.isLoading(true);
    service.deleteChannel(AppController.getInstance().getApiToken(), "en", channelId)
        .subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<ResponseBody>>() {
          @Override
          public void onNext(Response<ResponseBody> channelResponse) {
            if (view != null) view.isLoading(false);
            switch (channelResponse.code()) {
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
                            deleteChannel(channelId);
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
              case 200:
                if (view != null) view.channelDeleted();
                break;
              case 401:
                if (view != null) view.sessionExpired();
                break;
            }
          }

          @Override
          public void onError(Throwable e) {
            if (view != null) {
              view.isLoading(false);
            }
          }

          @Override
          public void onComplete() {
            if (view != null) {
              view.isLoading(false);
            }
          }
        });
  }

    @Override
    public void subscribeObservers() {

        postObserver.getObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if(view!=null)
                            view.reload();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        postObserver.getUpdateObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PostUpdateData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(PostUpdateData postUpdateData) {
                        if(view!=null)
                            view.reload();

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
  public void attachView(ChannelContract.View view) {
    this.view = view;
  }

  @Override
  public void detachView() {
    this.view = null;
  }
}
