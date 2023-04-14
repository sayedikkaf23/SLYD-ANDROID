package chat.hola.com.app.stars;

import chat.hola.com.app.home.contact.Friend;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;

public class StarModel {
  @Inject
  List<Friend> friendList;
  @Inject
  StarAdapter adapter;
  Set<String> initialCharacters = new HashSet<>();

  @Inject
  StarModel() {
  }

  void clearList() {
    initialCharacters.clear();
    this.friendList.clear();
  }

  void setFriendList(List<Friend> list) {
    try {

      for (int i = 0; i < list.size(); i++) {

        String initialCharacter =
            Character.toString(list.get(i).getFirstName().charAt(0)).toUpperCase();
        if (!initialCharacters.contains(initialCharacter)) {
          initialCharacters.add(initialCharacter);

          Friend friend = new Friend();
          friend.setTitle(initialCharacter);
          friend.setFirstName(initialCharacter);
          friend.setIsTitle(true);
          friendList.add(friend);
        }
          friendList.add(list.get(i));
      }

      Collections.sort(friendList,
          (obj1, obj2) -> obj1.getFirstName().compareToIgnoreCase(obj2.getFirstName()));

      adapter.refreshData(friendList);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public Friend getFriend(int position) {
    return friendList.get(position);
  }
}
