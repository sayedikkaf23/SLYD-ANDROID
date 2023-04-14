package chat.hola.com.app.home.activity.followingTab.model;

import android.view.View;

/**
 * Created by DELL on 4/17/2018.
 */
public interface ClickListner {
    void onUserClicked(String userId);

    void onMediaClick(int position, View view);
}
