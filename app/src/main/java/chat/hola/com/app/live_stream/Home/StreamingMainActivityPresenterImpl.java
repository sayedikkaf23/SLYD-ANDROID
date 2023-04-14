package chat.hola.com.app.live_stream.Home;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.LiveStreamService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.live_stream.LiveStream.LiveStreamBroadcaster.LiveBroadCastPresenterImpl;
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
public class StreamingMainActivityPresenterImpl implements StreamingMainActivityPresenterContract.presenterMain {

    private static final String TAG = LiveBroadCastPresenterImpl.class.getSimpleName();

    @Inject
    StreamingMainActivityPresenterContract.ViewMain viewMain;


    @Inject
    SessionManager manager;
    @Inject
    LiveStreamService apiServices;
    @Inject
    Gson gson;

    @Inject
    public StreamingMainActivityPresenterImpl() {
    }


    @Override
    public void onFragmentTransition(String TAG, FragmentManager fragmentManager, Fragment fragmentLiveBroadcasters, Fragment fragmentProfile, int frameId) {

        FragmentTransaction fTransaction = fragmentManager.beginTransaction();
try{
        switch (TAG) {
            case "HOME":

                if (fragmentProfile.isAdded())
                    fTransaction.hide(fragmentProfile);
                if (fragmentLiveBroadcasters.isAdded()) {
                    fTransaction.show(fragmentLiveBroadcasters);
                } else {
                    fTransaction.add(frameId, fragmentLiveBroadcasters, TAG);
                }
                break;


            case "PROFILE":

                if (fragmentLiveBroadcasters.isAdded())
                    fTransaction.hide(fragmentLiveBroadcasters);
                if (fragmentProfile.isAdded()) {
                    fTransaction.show(fragmentProfile);
                } else {
                    fTransaction.add(frameId, fragmentProfile, TAG);
                }
                break;

        }
        fTransaction.commit();
}catch(IllegalStateException e){e.printStackTrace();}
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
                                viewMain.onSuccess(new JSONObject(resp).getJSONObject("data").getString("streamId"));
                            } else {
                                viewMain.onError(responseBodyResponse.errorBody().string());
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
