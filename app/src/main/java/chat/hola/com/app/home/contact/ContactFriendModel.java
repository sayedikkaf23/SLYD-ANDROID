package chat.hola.com.app.home.contact;

import chat.hola.com.app.AppController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;

/**
 * <h1>ContactFriendModel</h1>
 *
 * @author 3Embed
 * @since 4/5/2018.
 */

public class ContactFriendModel {

  @Inject
  List<Friend> friendList;
  @Inject
  List<Friend> requestedFriend;
  @Inject
  ContactAdapter contactAdapter;
  @Inject
  FriendRequestAdapter friendRequestAdapter;

  @Inject
  ContactFriendModel() {
  }

  void clearList() {
    this.friendList.clear();
  }

  void setFriendList(List<Friend> list) {
    try {

      Set<String> temp = new HashSet<>();
      for (int i = 0; i < list.size(); i++) {
        String obj1 = Character.toString(list.get(i).getFirstName().charAt(0)).toUpperCase();
        String obj2;
        try {
          obj2 = Character.toString(list.get(i + 1).getFirstName().charAt(0)).toUpperCase();
        } catch (Exception e) {
          obj2 = "#";
        }

        if (!obj1.equalsIgnoreCase(obj2)) {
          temp.add(obj1);
        }
      }

      for (String title : temp) {
        Friend friend = new Friend();
        friend.setTitle(title);
        friend.setFirstName(title);
        friend.setIsTitle(true);
        friendList.add(friend);
      }

      friendList.addAll(list);
      Collections.sort(friendList,
          (obj1, obj2) -> obj1.getFirstName().compareToIgnoreCase(obj2.getFirstName()));
      contactAdapter.refreshData(friendList);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  String getFriendId(int position) {
    return friendList.get(position).getId();
  }

  public void setFriendRequestList(List<Friend> data) {
    requestedFriend.clear();
    requestedFriend.addAll(data);
    friendRequestAdapter.refreshData(requestedFriend);
  }

  public Friend getFriend(int position) {
    return friendList.get(position);
  }

  public Friend getRequestedFriend(int position) {
    return requestedFriend.get(position);
  }

  public void saveFriendsToDb(List<Friend> data) {

    try {

      //        Log.d("log1","friends data-->"+data.size());

      ArrayList<Map<String, Object>> friends = new ArrayList<>();

      for (Friend f : data) {

        Map<String, Object> friend = new HashMap<>();

        friend.put("userId", f.getId());
        friend.put("userName", f.getUserName());
        friend.put("countryCode", f.getCountryCode());
        friend.put("number", f.getNumber());
        friend.put("profilePic", f.getProfilePic());
        friend.put("firstName", f.getFirstName());
        friend.put("lastName", f.getLastName());
        friend.put("socialStatus", f.getStatus());
        friend.put("isStar", f.isStar());
        friend.put("starRequest", f.getStarData());
        friend.put("private", f.getPrivate());
        friend.put("friendStatusCode", f.getFriendStatusCode());
        friend.put("followStatus", f.getFollowStatus());
        friend.put("timestamp", f.getTimestamp());
        friend.put("message", f.getMessage());
        friend.put("isChatEnable",f.isChatEnable());

        //            Log.d("log1","friends data-->"+f.isStar());

        friends.add(friend);

        friend = null;
      }

//      AppController.getInstance()
//          .getDbController()
//          .insertFriendsInfo(friends, AppController.getInstance().getFriendsDocId());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
