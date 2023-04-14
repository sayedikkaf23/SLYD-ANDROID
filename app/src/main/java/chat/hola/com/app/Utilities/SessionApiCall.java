package chat.hola.com.app.Utilities;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Login;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * <h1>SessionApiCall</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 05 August 2019
 */

public class SessionApiCall {
    public void getNewSession(HowdooService service, SessionObserver sessionObserver) {
        SessionManager sessionManager = new SessionManager(AppController.getInstance().getBaseContext());
        service.newSessionToken(AppController.getInstance().getApiToken(), sessionManager.getRefreshToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Login>>() {
                    @Override
                    public void onNext(Response<Login> response) {
                        if (response.code() == 200) {

                            Map<String, Object> map = new HashMap<>();
                            map.put("userLoginType", 1);
                            map.put("userName", AppController.getInstance().getUserName());
                            map.put("userId", AppController.getInstance().getUserId());
                            map.put("userIdentifier", AppController.getInstance().getUserIdentifier());
                            map.put("apiToken", response.body().getResponse().getToken());

                            AppController.getInstance().setApiToken(response.body().getResponse().getToken());
                            AppController.getInstance().getDbController().updateUserDetails(AppController.getInstance().getDbController().getUserDocId(AppController.getInstance().getUserId(), AppController.getInstance().getIndexDocId()), map);
                            AppController.getInstance().getSharedPreferences().edit().putString("token", response.body().getResponse().getToken()).apply();

                            sessionObserver.postData(true);
                        } else {
                            sessionManager.sessionExpired(AppController.getInstance().getBaseContext());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        sessionObserver.postData(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void getNewSession(SessionObserver sessionObserver) {
        SessionManager sessionManager = new SessionManager(AppController.getInstance().getBaseContext());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, ApiOnServer.NEW_TOKEN, null, response -> {

            String token;
            try {
                token = response.getJSONObject("data").getString("accessToken");
            } catch (JSONException e) {
                token = "";
                e.printStackTrace();
            }
            Map<String, Object> map = new HashMap<>();
            map.put("userLoginType", 1);
            map.put("userName", AppController.getInstance().getUserName());
            map.put("userId", AppController.getInstance().getUserId());
            map.put("userIdentifier", AppController.getInstance().getUserIdentifier());
            map.put("apiToken", token);

            AppController.getInstance().setApiToken(token);
            AppController.getInstance().getDbController().updateUserDetails(AppController.getInstance().getDbController().getUserDocId(AppController.getInstance().getUserId(), AppController.getInstance().getIndexDocId()), map);
            AppController.getInstance().getSharedPreferences().edit().putString("token", token).apply();

            sessionObserver.postData(true);
        }, error -> {
            sessionManager.sessionExpired(AppController.getInstance().getBaseContext());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", AppController.getInstance().getApiToken());
                headers.put("refreshtoken", sessionManager.getRefreshToken());
                headers.put("lang", Constants.LANGUAGE);

                return headers;
            }
        };
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(jsonObjReq, "newToken");
    }
}
