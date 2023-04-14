package chat.hola.com.app.profileScreen.live_stream;

import android.os.Handler;
import androidx.annotation.Nullable;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.stories.model.StoryData;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class LiveStreamHistoryPresenter implements LiveStreamHistoryContract.Presenter {

  private static final String TAG = LiveStreamHistoryPresenter.class.getSimpleName();
  static final int PAGE_SIZE = Constants.PAGE_SIZE;
  private boolean isLoading = false;
  private boolean isLastPage = false;
  public static int page = 0;

  @Inject
  public LiveStreamHistoryPresenter() {
  }

  @Nullable
  LiveStreamHistoryContract.View view;
  @Inject
  HowdooService service;

  private SessionApiCall sessionApiCall = new SessionApiCall();

  @Override
  public void init() {
    if (view != null) view.setupRecyclerView();
  }

  @Override
  public void loadData(String userId, int offset, int limit) {
    isLoading = true;
    if (offset == 0) {
      isLastPage = false;
      page=0;
    }

    if (view != null) {
      view.isLoading(true);
    }
    service.streamHistory(AppController.getInstance().getApiToken(), Constants.LANGUAGE, userId,
        offset, limit)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<StoryData>>() {
          @Override
          public void onNext(Response<StoryData> response) {
            isLoading = false;
            if (view != null) {
              view.isLoading(false);
            }
            switch (response.code()) {
              case 200:
                if (view != null) {
                  assert response.body() != null;
                  isLastPage = response.body().getPosts().size() < PAGE_SIZE;
                  view.showData(response.body().getPosts(), offset == 0);
                }
                break;

              case 204:
                isLastPage = true;
                if (view != null) {
                  view.noData();
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
                            loadData(userId, offset, limit);
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
            if (view != null) {
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
  public void callApiOnScroll(String userId, int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoading && !isLastPage) {
      if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
          && firstVisibleItemPosition >= 0
          && totalItemCount >= PAGE_SIZE) {
        page++;
        loadData(userId, page * PAGE_SIZE, PAGE_SIZE);
      }
    }
  }

  public void deleteStream(String streamId) {
    service.deleteLiveStream(AppController.getInstance().getApiToken(), Constants.LANGUAGE, streamId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new DisposableObserver<Response<ResponseBody>>() {
              @Override
              public void onNext(Response<ResponseBody> response) {
                switch (response.code()) {
                  case 200:
                    if (view != null) {
                      view.isLoading(false);
                    }
                    if (view != null) {
                      view.streamDeleteSuccess(true);
                    }
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
                                    deleteStream(streamId);
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
  public void attachView(LiveStreamHistoryContract.View view) {
    this.view = view;
  }

  @Override
  public void detachView() {
    this.view = null;
  }
}
