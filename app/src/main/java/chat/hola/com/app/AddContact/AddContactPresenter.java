package chat.hola.com.app.AddContact;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.search.model.SearchResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * <h1>AddContactActivity</h1>
 *
 * @author 3Embed
 * @since 4/5/2018.
 */

public class AddContactPresenter implements AddContactContract.Presenter {
    public static final String TAG = "AddContactPresenter";

    @Nullable
    AddContactContract.View view;
    @Inject
    HowdooService service;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    AddContactPresenter(Context context) {
    }

    @Override
    public void attachView(AddContactContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    public void search(String text) {
        service.search(AppController.getInstance().getApiToken(), "en", text)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<SearchResponse>>() {

                    @Override
                    public void onNext(Response<SearchResponse> response) {

                        switch (response.code()) {
                            case 200:
                                if (response.body().getData() != null && response.body().getData().size() > 0) {
                                    if (view != null)
                                        view.showData(response.body().getData());
                                }
                                break;
                            case 204:
                                if (view != null) {
                                    view.noData();
                                }
                                break;
                            case 401:
                                if (view != null) {
                                    view.sessionExpired();
                                }
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable().subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        search(text);
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
