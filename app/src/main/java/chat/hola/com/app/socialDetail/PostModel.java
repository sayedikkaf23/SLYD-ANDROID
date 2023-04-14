package chat.hola.com.app.socialDetail;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.manager.session.SessionManager;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 8/20/2018.
 */
public class PostModel {

    @Inject
    SessionManager sessionManager;

    @Inject
    PostModel() {

    }

    public Map<String, String> getParams(String postId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", AppController.getInstance().getUserId());
        map.put("ip",sessionManager.getIpAdress());
        map.put("city",sessionManager.getCity());
        map.put("country",sessionManager.getCountry());
        map.put("postId", postId);
        return map;
    }


    public Map<String, String> getParams1(String postId) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", AppController.getInstance().getUserId());
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
}