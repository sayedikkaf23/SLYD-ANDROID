package chat.hola.com.app.search_user;

import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.home.contact.Friend;

/**
 * <h1>SearchUserModel</h1>
 *
 * @author 3Embed
 * @since 4/10/2018.
 */

class SearchUserModel {

    @Inject
    List<Friend> searchData;
    @Inject
    SearchUserAdapter adapter;


    @Inject
    SearchUserModel() {
    }

    public void clearData() {
        searchData.clear();
    }

    public void setUser(List<Friend> users) {
        searchData = users;
        adapter.setData(searchData);
    }

    public Friend getUser(int position) {
        return searchData.get(position);
    }

    public void updateFolow(int position, String userId, Integer status) {
        searchData.get(position).setFollowStatus(status);
        adapter.notifyDataSetChanged();
    }
}
