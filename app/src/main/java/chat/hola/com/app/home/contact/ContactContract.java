package chat.hola.com.app.home.contact;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;

/**
 * <h1>ContactContract</h1>
 *
 * @author 3Embed
 * @since 13/2/2019.
 */

public interface ContactContract {

    interface View extends BaseView {

        void openChatForItem(Friend friend,int position);

        void noContent();

        void loadingCompleted();

        void onUserSelected(Friend data);

        void friendRequests(boolean b);

        void onFriendRequestSelected(Friend friend);

        void newFriendCount(int count);

        void scroll(boolean empty);

        void isFollowing(int pos, int status);
    }

    interface Presenter extends BasePresenter<View> {

        void newFriends();

        void friends();

        void searchFriends(String character);
    }
}
