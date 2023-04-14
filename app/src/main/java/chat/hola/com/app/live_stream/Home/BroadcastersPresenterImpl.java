package chat.hola.com.app.live_stream.Home;

import androidx.annotation.Nullable;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.LiveStreamService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.live_stream.Observable.AllStreamsObservable;
import chat.hola.com.app.live_stream.ResponcePojo.AllStreamsData;
import chat.hola.com.app.live_stream.ResponcePojo.LiveBroadCasterResponse;
import chat.hola.com.app.manager.session.SessionManager;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by moda on 2/18/2019.
 */
public class BroadcastersPresenterImpl implements BroadcastersPresenterContract.BroadcastPresenter {

    @Inject
    SessionManager manager;
    @Inject
    LiveStreamService apiServices;
    @Inject
    Gson gson;
    @Nullable
    private BroadcastersPresenterContract.BroadcastView broadcasterView;

    @Inject
    public BroadcastersPresenterImpl() {
    }

    @Override
    public void callLiveBroadcaster() {
        Observable<Response<ResponseBody>> observable = apiServices.getLiveStreamers(AppController.getInstance().getApiToken(), Constants.LANGUAGE,0,20);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        int code = responseBodyResponse.code();

                        try {
                            if (code == 200) {

                                String response = responseBodyResponse.body().string();

                                LiveBroadCasterResponse liveBroadCaster = gson.fromJson(response, LiveBroadCasterResponse.class);


                                if (broadcasterView != null)
                                    broadcasterView.liveBroadCasterData(liveBroadCaster.getData().getStreams());

                            } else {
                                String responseError = responseBodyResponse.errorBody().string();
                                if (broadcasterView != null)
                                    broadcasterView.showAlert(new JSONObject(responseError).getString("message"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
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
    public void allStreamDataRxJava() {

        Observer<AllStreamsData> observer = new Observer<AllStreamsData>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(AllStreamsData dataStream) {

                if (broadcasterView != null)
                    broadcasterView.onAllStreamDataReceived(dataStream);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        AllStreamsObservable.getInstance().subscribe(observer);

    }

    @Override
    public void attachView(Object view) {
        broadcasterView = (BroadcastersPresenterContract.BroadcastView) view;
    }

    @Override
    public void detachView() {

        broadcasterView = null;
    }
}
