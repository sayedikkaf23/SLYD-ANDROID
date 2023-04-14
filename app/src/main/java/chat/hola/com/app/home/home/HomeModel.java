package chat.hola.com.app.home.home;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.manager.session.SessionManager;

public class HomeModel {

    @Inject
    SessionManager sessionManager;

    @Inject
    HomeModel() {

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
