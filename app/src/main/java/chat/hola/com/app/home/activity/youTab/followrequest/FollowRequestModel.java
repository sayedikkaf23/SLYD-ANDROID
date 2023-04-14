package chat.hola.com.app.home.activity.youTab.followrequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * <h1>ChannelRequestModel</h1>
 *
 * @author DELL
 * @since 4/28/2018.
 */

public class FollowRequestModel {

    @Inject
    List<ReuestData> data;
    @Inject
    FollowRequestAdapter adapter;

    @Inject
    FollowRequestModel() {
    }

    public void setData(List<ReuestData> reuestData) {
        data.addAll(reuestData);
        adapter.notifyDataSetChanged();
    }

    public String getUserId(int position) {
        return data.get(position).getId();
    }

    public Map<String, Object> getParams(String userId, int  status) {
        Map<String, Object> map = new HashMap<>();
        map.put("targetId", userId);
        map.put("status", status);
        return map;
    }

    public void accepted(int position) {
        try {
            this.data.remove(position);
            adapter.notifyDataSetChanged();
        } catch (Exception ignored) {
        }
    }

    public void clearList() {
        this.data.clear();
    }
}
