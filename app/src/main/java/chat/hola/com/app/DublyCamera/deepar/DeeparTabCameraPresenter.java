package chat.hola.com.app.DublyCamera.deepar;

import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.deepar.ar.AREffect;
import chat.hola.com.app.DublyCamera.deepar.ar.AROperations;
import chat.hola.com.app.Networking.LiveStreamService;
import chat.hola.com.app.Utilities.Constants;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.ResponseBody;
import org.json.JSONObject;
import retrofit2.Response;

import static chat.hola.com.app.DublyCamera.deepar.ar.AROperations.SLOT_MASKS;

public class DeeparTabCameraPresenter implements DeeparTabCameraContract.Presenter {

  @Inject
  public DeeparTabCameraPresenter() {
  }

  @Inject
  DeeparTabCameraContract.View view;

  @Inject
  LiveStreamService apiServices;

  @Override
  public void startLiveBroadcastApi(String streamId, String streamType, String thumbnail,
      String streamName) {
    Map<String, Object> map = new HashMap<>();
    map.put("id", streamId);
    map.put("type", streamType);
    map.put("streamName", streamName);
    map.put("thumbnail", thumbnail);
    map.put("record", false);
    map.put("detection", false);
    map.put("duration", 0);

    Observable<Response<ResponseBody>> observable =
        apiServices.postApiCallStream(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
            map);
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

  private AROperations arOperations;

  @Override
  public void initialize(AROperations arOperations) {
    this.arOperations = arOperations;
  }

  @Override
  public void applyFilter(String slot, String path) {
    arOperations.applyFilter(slot, path);
  }

  @Override
  public void clearFilters() {

    arOperations.clearAllFilters();
  }

  @Override
  public void applyBeautifyOptions(boolean beautificationApplied) {
    arOperations.applyBeautifyOptions(beautificationApplied);
  }
}
