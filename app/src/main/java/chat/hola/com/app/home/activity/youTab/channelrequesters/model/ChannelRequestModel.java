package chat.hola.com.app.home.activity.youTab.channelrequesters.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.home.activity.youTab.model.RequestedChannels;
import chat.hola.com.app.home.activity.youTab.model.RequestedUserList;

/**
 * <h1>ChannelRequestModel</h1>
 *
 * @author DELL
 * @since 4/28/2018.
 */

public class ChannelRequestModel {

    @Inject
    List<DataList> data;
    @Inject
    ChannelRequesterAdapter adapter;

    @Inject
    ChannelRequestModel() {
    }

    public void setData(List<RequestedChannels> channelsData) {
        for (RequestedChannels channels : channelsData) {
            String channelId = channels.getId();
            String channelName = channels.getChannelName();
            Boolean _private = channels.getPrivate();
            String channelImageUrl = channels.getChannelImageUrl();
            Double channelCreatedOn = channels.getChannelCreatedOn();

//            this.data.add(new DataList(channels.getId(), channels.getChannelName(), channels.getPrivate(), channels.getChannelImageUrl(), channels.getChannelCreatedOn(), true));
            for (RequestedUserList userList : channels.getRequestedUserList()) {
                DataList list = new DataList();
                list.setChannelId(channelId);
                list.setChannelName(channelName);
                list.set_private(_private);
                list.setChannelImageUrl(channelImageUrl);
                list.setChannelCreatedOn(channelCreatedOn);

                list.setUserId(userList.getId());
                list.setCountryCode(userList.getCountryCode());
                list.setNumber(userList.getNumber());
                list.setProfilePic(userList.getProfilePic());
                list.setUserName(userList.getUserName());
                list.setRequestedTimestamp(userList.getRequestedTimestamp());

                data.add(list);
//                this.data.add(new DataList(channels.getId(), userList.getId(), userList.getCountryCode(), userList.getNumber(), userList.getProfilePic(), userList.getUserName(), userList.getRequestedTimestamp(), false));
            }

        }
        adapter.notifyDataSetChanged();
    }

    public String getChannelId(int position) {
        return data.get(position).getChannelId();
    }

    public String getUserId(int position) {
        return data.get(position).getUserId();
    }

    public Map<String, Object> getParams(String channelId, String userId, boolean flag) {
        Map<String, Object> map = new HashMap<>();
        map.put("channelId", channelId);
        map.put("userId", userId);
        map.put("isAccepted", flag);
        return map;
    }

    public void accpted(int position) {
        try {
            this.data.remove(position);
            adapter.notifyDataSetChanged();
        } catch (Exception ignored) {
        }
    }
}
