package chat.hola.com.app.socialDetail;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.collections.model.CollectionData;
import chat.hola.com.app.comment.model.Comment;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.home.model.Data;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <h1>SocialDetailContract</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 23/3/18.
 */
public interface SocialDetailContract {

    interface View extends BaseView {


        /**
         * <p>dismisses the dialog</p>
         */
        void dismissDialog();

        /**
         * <p>add reports to the array list</p>
         *
         * @param data : list of report
         */
        void addToReportList(ArrayList<String> data);

        /**
         * <p>sets details to the screen</p>
         *
         * @param data : details
         */
        void setData(Data data);

        void liked(boolean b, boolean hasError, String postId);

        void deleted();

        void followers(List<Friend> data);

        void setDataList(List<Data> data, boolean entirelyNewList, boolean refreshRequest);
        void sendTipSuccess(Data data, String coin, int position);

        /**
         * Click on saved.
         *
         * @param position
         * @param bookMarked
         */
        void savedClick(int position, Boolean bookMarked);

        /**
         * Update view for post is saved or unsaved.
         *
         * @param pos
         * @param isSaved
         */
        void bookMarkPostResponse(int pos, boolean isSaved);

        /**
         * saved view click
         *
         * @param position
         * @param data
         */
        void savedViewClick(int position, Data data);

        /**
         * Click event of save to collection
         *
         * @param position
         * @param data
         */
        void saveToCollectionClick(int position, Data data);

        /**
         * Show progress in view
         *
         * @param b
         */
        void showProgress(boolean b);

        /**
         * post is successfully added to collection
         */
        void postAddedToCollection();

        /**
         * Fetched all Collection from API
         *
         * @param data
         */
        void collectionFetched(List<CollectionData> data);

        /**
         * When collection created successfully this method will be call.
         */
        void collectionCreated();

        void unFollowUser(String userId);

        void postUpdated(String postId, boolean allowComment, boolean allowDownload, boolean allowDuet, Map<String, Object> body);

        void setComments(List<Comment> data, boolean b);

        void commented(boolean b);

        void addToList(Comment comments,String postId);

        void clearLikeList(boolean b);

        void showLikes(List<chat.hola.com.app.profileScreen.followers.Model.Data> data);

        void noLikes(boolean b);

        void noComment(boolean b);

        void commentDeleted(int position, String commentId);

        void commentLiked(int position, String commentId, boolean isLike);

        void onPostPurchased(Data data, int position);

        void onUserSubscribed(Data data, int position);

        void insufficientBalance();

        void postReported(int position, String postId,boolean reportSuccessfull);
    }

    interface Presenter {

        /**
         * <p>gets details by postId </p>
         *
         * @param postId        :posts's Id
         * @param isJustForView : says its just to for view the post
         */
        void getPostById(String postId, boolean isJustForView);

        /**
         * <p>get list of report reasons</p>
         */
        void getReportReasons();

        void reportPost(int position,String postId, String reason, String message);

        void deletePost(String postId);

        void unlike(String postId);

        void like(String postId);

        void follow(String followingId);

        void unfollow(String followingId);


        /**
         * Saved post in bookmark By API.
         *
         * @param postId
         */
        void saveToBookmark(int pos, String postId);

        /**
         * Delete post from bookmark By API.
         *
         * @param postId
         */
        void deleteToBookmark(int pos, String postId);

        /**
         * Add Particular post to particular collection
         *
         * @param collectionId
         * @param postId
         */
        void addPostToCollection(String collectionId, String postId);

        /**
         * Fetch All Collections list
         */
        void getCollections();

        /**
         * Used to create new collection with one post.
         *
         * @param collectionName
         * @param collectionImage
         * @param postId
         */
        void createCollection(String collectionName, String collectionImage, String postId);

        void setPageNumber(int page);

        void deleteComment(int position, String postId, String commentId);

        void likeComment(int position, String commentId, boolean isLike);

        void sendTipRequest(Data data, String coin, String desc, int position);

        void payForPost(Data data, int position, boolean b);

        void subscribeProfile(Data data, int position);

        void subscribeStarUser(Data data, int position);
    }
}
