package chat.hola.com.app.friends;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.contact.Friend;

/**
 * <h1>BlockUserContract</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public interface FriendsContract {

    interface View extends BaseView {
        void openProfile(Friend friend);

        void loading(boolean b);

        void openRequest(Friend user);
    }

    interface Presenter {

        void friends(int offset, int limit);

        void friendsSearch(String text, int offset, int limit);

        void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        FriendsAdapter.ClickListner getPresenter();
    }
}
