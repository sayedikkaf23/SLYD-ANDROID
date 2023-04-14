package chat.hola.com.app.profileScreen.profile_story;

import android.os.Handler;
import androidx.annotation.Nullable;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.stories.model.StoryResponse;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import retrofit2.Response;

public class ProfileStoryPresenter implements ProfileStoryContract.Presenter {

  private static final String TAG = ProfileStoryPresenter.class.getSimpleName();
  static final int PAGE_SIZE = Constants.PAGE_SIZE;
  private boolean isLoading = false;
  private boolean isLastPage = false;
  public static int page = 0;

  @Inject
  public ProfileStoryPresenter() {
  }

  @Nullable
  ProfileStoryContract.View view;
  @Inject
  HowdooService service;
  @Inject
  NetworkConnector networkConnector;
  private SessionApiCall sessionApiCall = new SessionApiCall();

  @Override
  public void init() {
    if (view != null) view.setupRecyclerView();
  }

  @Override
  public void loadData(int skip, int limit, String userId) {
    isLoading = true;
    if (view != null) {
      view.isLoading(true);
    }
    if (skip == 0) {
      isLastPage = false;
      page=0;
    }
    service.getUserStoriesHistory(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
        userId)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<Response<StoryResponse>>() {
          @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(Response<StoryResponse> response) {
            isLoading = false;
            try {
              switch (response.code()) {
                case 200:
                  if (response.body() != null
                      && response.body().getData().size() > 0
                      && response.body().getData().get(0).getPosts().size() > 0
                      && view != null) {
                    isLastPage = response.body().getData().get(0).getPosts().size() < PAGE_SIZE;
                    view.showData(response.body().getData().get(0).getPosts(), true);
                    view.isLoading(false);
                  } else {
                    if (view != null) view.noData();
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
                              loadData(skip, limit, userId);
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
        loadData(page * PAGE_SIZE, PAGE_SIZE, userId);
      }
    }
  }

  @Override
  public void attachView(ProfileStoryContract.View view) {
    this.view = view;
  }

  @Override
  public void detachView() {
    this.view = null;
  }
}
