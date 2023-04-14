package chat.hola.com.app.home.xclusive;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.collections.model.CollectionData;
import chat.hola.com.app.comment.model.Comment;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.home.model.User;
import chat.hola.com.app.models.PostUpdateData;

import com.kotlintestgradle.remote.model.response.ecom.homeSubCategory.CategoryDetails;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <h1>{@link XclusiveContract}</h1>
 */

public interface XclusiveContract {

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

        void deleted(String postId);

        void followers(List<Friend> data);

        void setDataList(List<Data> data, boolean entirelyNewList, boolean refreshRequest);

        /*Used to show progress while background data operation*/
        void showProgress(boolean show);

        /*To show empty UI*/
        void showEmpty(boolean show);

        void bookMarkPostResponse(int pos, boolean bookMarked);

        void collectionCreated();

        void postAddedToCollection();

        void collectionFetched(List<CollectionData> data);
        void sendTip(int position);

        void sendTipSuccess(Data data, String coin, int position);

        void insufficientBalance();
        //void sendTipRequest(Data data, String coin, String desc);


        /**
         * <p>Used to update saved status of post from subscribe observer</p>
         */
        void updateSavedOnObserve(PostUpdateData savedData);

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

        void setSuggestedUsers(ArrayList<User> users);

        void showProducts(ArrayList<CategoryDetails> products);

        void onPostPurchased(Data data, int position);

        void onUserSubscribed(Data data, int position);

        void postReported(int position, String postId,boolean reportSuccessfull);
    }

    interface Presenter extends BasePresenter<View> {

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

        void reportPost(int position, String postId, String reason, String message);

        void deletePost(String postId);

        void unlike(String postId);

        void like(String postId);

        void follow(String followingId);

        void unfollow(String followingId);

        void saveToBookmark(int position, String postId);

        void deleteToBookmark(int position, String postId);

        void createCollection(String trim, String collectionImage, String addToCollectionPostId);

        void addPostToCollection(String id, String id1);

        void getCollections();

        void deleteComment(int position, String postId, String commentId);

        void likeComment(int position, String commentId, boolean isLike);

        void suggestUser();

        void callProductsAPi(ArrayList<String> productIds);

        void sendTipRequest(Data data, String coin, String desc, int position);

        void payForPost(Data data, int position, boolean b);

        void subscribeProfile(Data data, int position);

        void subscribeStarUser(Data data, int position);
    }
}
