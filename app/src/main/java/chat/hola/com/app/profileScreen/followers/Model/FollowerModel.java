package chat.hola.com.app.profileScreen.followers.Model;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.profileScreen.followers.FollowerAdapter;

/**
 * Created by ankit on 6/4/18.
 */

public class FollowerModel {

    private ArrayList<Data> arrayList;
    private FollowerAdapter followerAdapter;

    @Inject
    FollowerModel(ArrayList<Data> arrayList , FollowerAdapter adapter){
        this.arrayList = arrayList;
        this.followerAdapter = adapter;
    }


    public void showFollowers(List<Data> followers) {
//        if(followers.size() < PAGE_SIZE)
//            isLastPage = true;
        //presenter check then

        this.arrayList.addAll(followers);
        followerAdapter.setData(this.arrayList);
    }


    public void showFollowees(List<Data> followees) {
//        if(followees.size() < PAGE_SIZE)
//            isLastPage = true;
        //presenter check then

        this.arrayList.addAll(followees);
        followerAdapter.setData(this.arrayList);
    }

}
