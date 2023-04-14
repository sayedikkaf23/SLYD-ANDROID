package chat.hola.com.app.home.activity.youTab.channelrequesters.model;

/**
 * Created by DELL on 4/17/2018.
 */
public interface ClickListner {
    void onRequestAction(int position, boolean flag);

    void onUserClicked(int position);

    void onChannelClick(int position);
}
