package chat.hola.com.app.search.channel;

import android.os.Handler;
import android.util.Log;
import androidx.annotation.Nullable;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.search.channel.module.ChannelResponse;
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
 * Created by ankit on 24/2/18.
 */

public class ChannelPresenter implements ChannelContract.Presenter {

  @Nullable
  private ChannelContract.View view;
  @Inject
  HowdooService service;
  private SessionApiCall sessionApiCall = new SessionApiCall();

  @Inject
  ChannelPresenter() {
  }

  @Override
  public void attachView(ChannelContract.View view) {
    this.view = view;
  }

  @Override
  public void detachView() {
  }

  @Override
  public void search(CharSequence charSequence) {
    service.searchChannel(AppController.getInstance().getApiToken(), "en", charSequence.toString())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Response<ChannelResponse>>() {
          @Override
          public void onSubscribe(Disposable d) {
          }

          @Override
          public void onNext(Response<ChannelResponse> response) {

            switch (response.code()) {
              case 200:
                if (response.body().getData() != null && response.body().getData().size() > 0) {
                  view.showData(response.body().getData());
                }
                break;
              case 204:
                view.noData();
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
                            search(charSequence);
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
            Log.e("search", "onError: " + e.getMessage());
          }

          @Override
          public void onComplete() {
          }
        });
  }

  @Override
  public void subscribeChannel(String channelId) {
    Map<String, Object> map = new HashMap<>();
    map.put("channelId", channelId);
    service.subscribeChannel(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
        .subscribeOn(Schedulers.newThread())
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
                            subscribeChannel(channelId);
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
            this.dispose();
          }
        });
  }

  @Override
  public void unSubscribeChannel(String channelId) {
    Map<String, Object> map = new HashMap<>();
    map.put("channelId", channelId);
    service.unSubscribeChannel(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
        .subscribeOn(Schedulers.newThread())
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
                            unSubscribeChannel(channelId);
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
            this.dispose();
          }
        });
  }
}
