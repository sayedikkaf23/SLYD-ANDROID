package chat.hola.com.app.home.activity.youTab.channelrequesters.model;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.manager.session.SessionManager;
import retrofit2.http.Query;

/**
 * <h1>SocialDetailModel</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 5/3/2018.
 */

public class SocialDetailModel {

    @Inject
    SessionManager sessionManager;

    @Inject
    SocialDetailModel() {

    }

    public Map<String, Object> getParams() {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", AppController.getInstance().getUserId());
        map.put("ip", sessionManager.getIpAdress());
        map.put("city", sessionManager.getCity());
        map.put("country", sessionManager.getCountry());
        map.put("latitude", sessionManager.getLatitude());
        map.put("longitude", sessionManager.getLongitude());
        return map;
    }

    public Map<String, String> getParams(String postId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", AppController.getInstance().getUserId());
        map.put("ip", sessionManager.getIpAdress());
        map.put("city", sessionManager.getCity());
        map.put("country", sessionManager.getCountry());
        map.put("postId", postId);
        return map;
    }

    public Map<String, String> getReasonParams(String postId, String reason, String message) {
        Map<String, String> map = new HashMap<>();
        map.put("postId", postId);
        map.put("message", message);
        map.put("reason", reason);
        return map;
    }


    public Map<String, String> getParams1(String postId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", AppController.getInstance().getUserId());
        map.put("postId", postId);
        return map;
    }
}
