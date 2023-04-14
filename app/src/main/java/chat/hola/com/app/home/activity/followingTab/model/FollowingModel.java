package chat.hola.com.app.home.activity.followingTab.model;

import java.util.ArrayList;

import javax.inject.Inject;

import chat.hola.com.app.home.activity.followingTab.FollowingAdapter;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 5/14/2018.
 */

public class FollowingModel {

//    @Named("followingList")
//    @Inject
//    ArrayList<Following> followings;

    @Inject
    FollowingAdapter adapter;

    @Inject
    public FollowingModel() {
    }

    public void setData(ArrayList<Following> data) {
        adapter.setData(data);
    }
}
