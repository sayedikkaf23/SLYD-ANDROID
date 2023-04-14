package chat.hola.com.app.home.comment;

import java.util.List;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.comment.model.ClickListner;
import chat.hola.com.app.comment.model.Comment;
import chat.hola.com.app.hastag.Hash_tag_people_pojo;

public interface CommentFragmentContract {
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

        void selectItem(int position, boolean isSelected);

        String getUserId(int position);

        int getTotalComments();

        void setData(List<Comment> data);

        void clearList();

        void addToList(Comment comments);

        void commentDeleted(int position, String commentId);

        void commentLiked(int position, String commentId, boolean isLike);
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

        void bindView(View view);

        void unbindView();
    }
}
