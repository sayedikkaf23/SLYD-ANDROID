package chat.hola.com.app.friends;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.home.contact.GetFriendRequest;

/**
 * <h1>FriendsModel</h1>
 *
 * @author 3Embed
 * @since 4/10/2018.
 */

class FriendsModel {

    @Inject
    List<Friend> friends;
    @Inject
    FriendsAdapter adapter;


    @Inject
    FriendsModel() {
    }


    public void setData(GetFriendRequest.Data data) {
        if (data != null) {
            try {
                friends.clear();
                if (data.getFriendRequests().size() > 0) {
                    Friend friend2 = new Friend();
                    friend2.setTitle("FRIEND REQUESTS");
                    friend2.setFirstName("A");
                    friend2.setIsTitle(true);
                    friends.add(friend2);
                    for (Friend friend : data.getFriendRequests()) {
                        friend.setFriendRequest(true);
                        friends.add(friend);
                    }
                    Collections.sort(friends, (obj1, obj2) -> obj1.getFirstName().compareToIgnoreCase(obj2.getFirstName()));
                }

                List<Friend> list = new ArrayList<>();
                if (data.getSentFriendRequest().size() > 0) {
                    Friend friend2 = new Friend();
                    friend2.setTitle("REQUEST I SENT");
                    friend2.setFirstName("A");
                    friend2.setIsTitle(true);
                    list.add(friend2);
                    list.addAll(data.getSentFriendRequest());
                    Collections.sort(list, (obj1, obj2) -> obj1.getFirstName().compareToIgnoreCase(obj2.getFirstName()));
                    friends.addAll(list);
                }
                adapter.setData(friends);
            } catch (Exception e) {
                e.printStackTrace();
                clearList();
            }
        }
    }

    public void clearList() {
        this.friends.clear();
    }

    public String getUserId(int position) {
        return friends.get(position).getId();
    }

    public Friend getUser(int position) {
        return friends.get(position);
    }
}
