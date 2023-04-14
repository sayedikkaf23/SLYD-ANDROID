package chat.hola.com.app.search.people;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.contact.Friend;

/**
 * Created by ankit on 24/2/18.
 */

public interface PeopleContract {

    interface View extends BaseView {
        void showData(List<Friend> data);

        void isFollowing(int pos, int status);

        void noData();
    }

    interface Presenter extends BasePresenter<PeopleContract.View> {
        void search(CharSequence charSequence);

        void follow(int pos, String followingId);

        void unfollow(int pos, String followingId);
    }
}
