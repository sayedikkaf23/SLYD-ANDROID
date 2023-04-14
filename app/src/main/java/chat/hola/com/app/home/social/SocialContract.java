package chat.hola.com.app.home.social;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.collections.model.CollectionData;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.home.model.Data;

/**
 * <h1>AddContactContract</h1>
 *
 * @author 3Embed
 * @since 24/2/2018.
 */

public interface SocialContract {

    interface View extends BaseView {
        void onActionButtonClick(String title, String url);

        void isLoading(boolean flag);

        void onItemClick(int position);

        void onUserClick(int position);

        void showEmptyUi(boolean show);

        void onChannelClick(String channelId);

        void setData(List<Data> data, boolean clear);

        void viewAllComments(String postId);

        void send(int position);

        void share(int position);

        void like(int position, boolean liked);

        void friends(List<Friend> friends);

        void openProfile(String userId);

        void onViewClick(Data data);

        void onLikeClick(Data data);

        void edit(Data data);

        void delete(Data data);

        void report(Data data);

        void addToReportList(ArrayList<String> data);

        /**
         * Click on saved.
         * @param position
         * @param bookMarked
         */
        void savedClick(int position, Boolean bookMarked);

        /**
         * Update view for post is saved or unsaved.
         * @param pos
         * @param isSaved
         */
        void bookMarkPostResponse(int pos, boolean isSaved);

        /**
         * saved view click
         * @param position
         * @param data
         */
        void savedViewClick(int position, Data data);

        /**
         * Click event of save to collection
         * @param position
         * @param data
         */
        void saveToCollectionClick(int position, Data data);

        /**
         * Show progress in view
         * @param b
         */
        void showProgress(boolean b);

        /**
         * post is successfully added to collection
         */
        void postAddedToCollection();

        /**
         * Fetched all Collection from API
         * @param data
         */
        void collectionFetched(List<CollectionData> data);

        /**
         * When collection created successfully this method will be call.
         */
        void collectionCreated();
    }

    interface Presenter extends BasePresenter<View> {
        void callSocialApi(int offset, int limit, boolean load);

        void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);

        void prefetchImage(int position);

        SocialPresenter getPresenter();

        void postObserver();

        void getFriends();

        void deletePost(String postId);

        void getReportReasons();


        /**
         * Saved post in bookmark By API.
         * @param postId
         */
        void saveToBookmark(int pos,String postId);

        /**
         * Delete post from bookmark By API.
         * @param postId
         */
        void deleteToBookmark(int pos,String postId);

        /**
         * Add Particular post to particular collection
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
         * @param collectionName
         * @param collectionImage
         * @param postId
         */
        void createCollection(String collectionName, String collectionImage, String postId);

    }
}
