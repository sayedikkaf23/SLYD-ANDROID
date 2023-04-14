package chat.hola.com.app.search_user;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.contact.Friend;

/**
 * <h1>BlockUserContract</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public interface SearchUserContract {

    interface View extends BaseView {
        void dataAvailable(boolean flag);

        void add(Friend user);

        void onUserSelected(Friend user);
    }

    interface Presenter {

        void search(String text, int skip, int limit);

        void stareSearch(String text, int skip, int limit);

        void friendSearch(String text, int skip, int limit);

        void friendRequestSearch(String text, int skip, int limit);

        void topStareSearch(int category, int skip, int limit);

        void callApiOnScroll(String call, String text, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        SearchUserAdapter.ClickListner getPresenter();

        void myStars(int skip, int limit);

        void stars(String type, int skip, int limit);
    }
}
