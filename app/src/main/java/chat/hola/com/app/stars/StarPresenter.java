package chat.hola.com.app.stars;

import android.os.Handler;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.home.contact.GetFriends;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import retrofit2.Response;

public class StarPresenter implements StarContact.Presenter {

  private final int PAGE_SIZE = Constants.PAGE_SIZE;
  private boolean isLoading = false;
  private boolean isLastPage = false;
  public static int page = 0;
  private SessionApiCall sessionApiCall = new SessionApiCall();

  @Inject
  StarModel model;
  @Inject
  HowdooService service;
  @Inject
  StarContact.View view;

  @Inject
  StarPresenter() {
  }

  @Override
  public void stars(int offset, int limit) {
    if (offset == 0) {

      page = 0;
      isLastPage = false;
    }
    isLoading = true;
    service.stars(AppController.getInstance().getApiToken(), Constants.LANGUAGE, offset, limit)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<GetFriends>>() {
          @Override
          public void onNext(Response<GetFriends> response) {
            isLoading = false;
            switch (response.code()) {
              case 200:
                if (offset == 0) model.clearList();
                isLastPage = response.body().getData().size() < PAGE_SIZE;
                model.setFriendList(response.body().getData());
                break;
              case 204:
                isLastPage = true;
                if (view != null) view.noContent();
                break;
              case 401:
                if (view != null) view.sessionExpired();
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
                            stars(offset, limit);
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
          }

          @Override
          public void onComplete() {
            if (view != null) view.loadingCompleted();
            isLoading = false;
          }
        });
  }

  @Override
  public Friend getFriend(int position) {
    return model.getFriend(position);
  }

  @Override
  public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount,
      int totalItemCount) {
    if (!isLoading && !isLastPage) {
      if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
          && firstVisibleItemPosition >= 0
          && totalItemCount >= PAGE_SIZE) {
        page++;
        stars(PAGE_SIZE * page, PAGE_SIZE);
      }
    }
  }
}
