package chat.hola.com.app.search.tags;

import android.os.Handler;
import androidx.annotation.Nullable;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.search.tags.module.TagsResponse;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import retrofit2.Response;

/**
 * Created by ankit on 24/2/18.
 */

public class TagsPresenter implements TagsContract.Presenter {

  @Nullable
  private TagsContract.View view;
  @Inject
  HowdooService service;

  private SessionApiCall sessionApiCall = new SessionApiCall();

  @Inject
  TagsPresenter() {
  }

  @Override
  public void attachView(TagsContract.View view) {
    this.view = view;
  }

  @Override
  public void detachView() {

  }

  @Override
  public void search(CharSequence charSequence) {
    service.searchTags(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
        charSequence.toString())
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Response<TagsResponse>>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(Response<TagsResponse> response) {

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

          }

          @Override
          public void onComplete() {
          }
        });
  }
}
