package chat.hola.com.app.blockUser;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.blockUser.model.ClickListner;

/**
 * <h1>BlockUserContract</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public interface BlockUserContract {

    interface View extends BaseView {


        /**
         * redirects to  @{@link chat.hola.com.app.profileScreen.ProfileActivity})
         *
         * @param userId : userId to get user details
         */
        void openProfile(String userId);


        void confirm(int position, String name);

        void noContent(boolean show);
    }

    interface Presenter {

        /**
         * gets lis of comments of particular post
         */
        void getBlockedUsers(int offset, int limit);

        void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        ClickListner getPresenter();

        void unblock(int position);
    }
}
