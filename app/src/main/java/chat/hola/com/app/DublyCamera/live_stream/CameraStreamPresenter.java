package chat.hola.com.app.DublyCamera.live_stream;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.LiveStreamService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.manager.session.SessionManager;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * Created by moda on 11/20/2018.
 */
public class CameraStreamPresenter implements CameraStreamContract.Presenter {


    @Inject
    CameraStreamContract.View view;
    @Inject
    SessionManager manager;
    @Inject
    LiveStreamService apiServices;

    @Inject
    public CameraStreamPresenter() {
    }

    @Override
    public void startLiveBroadcastApi(String streamId, String streamType, String thumbnail, String streamName) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", streamId);
        map.put("type", streamType);
        map.put("streamName", streamName);
        map.put("thumbnail", thumbnail);
        map.put("record", false);
        map.put("detection", false);
        map.put("duration", 0);

        Observable<Response<ResponseBody>> observable = apiServices.postApiCallStream(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> responseBodyResponse) {
                        int code = responseBodyResponse.code();
                        try {
                            if (code == 200) {
                                String resp = responseBodyResponse.body().string();
                                view.onSuccess(new JSONObject(resp).getJSONObject("data").getString("streamId"));
                            } else {
                                view.onError(responseBodyResponse.errorBody().string());
                            }
                        } catch (Exception e) {
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
}
