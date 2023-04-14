package chat.hola.com.app.comment;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.comment.model.ClickListner;
import chat.hola.com.app.hastag.Hash_tag_people_pojo;

/**
 * <h1>BlockUserContract</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */

public interface CommentContract {

    interface View extends BaseView {

        /**
         * call when comment is completed
         *
         * @param flag : true - comment completed, false- not completed
         */
        void commented(boolean flag);

        /**
         * redirects to  @{@link chat.hola.com.app.profileScreen.ProfileActivity})
         *
         * @param userId : userId to get user details
         */
        void openProfile(String userId);

        /**
         * set hashtag to edit text
         *
         * @param tag : hashtag data
         */
        void setTag(Hash_tag_people_pojo tag);

        /**
         * set user tag to edit text
         *
         * @param tag : user tag data
         */
        void setUser(Hash_tag_people_pojo tag);

        void showProgress(boolean show);
    }

    interface Presenter {

        /**
         * adds comment to the list
         *
         * @param postId  : userId of data
         * @param comment : comment text
         */
        void addComment(String postId, String comment);


        /**
         * gets lis of comments of particular post
         *
         * @param postId : userId of comment
         */
        void getComments(String postId, int offset, int limit);


        /**
         * perform the click actions
         */
        ClickListner getPresenter();

        /**
         * search hashTag
         */
        void searchHashTag(String hashTag);

        /**
         * search userTag
         */
        void searchUserTag(String userTag);

        void callApiOnScroll(String postId, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        int getTotal();
    }
}
