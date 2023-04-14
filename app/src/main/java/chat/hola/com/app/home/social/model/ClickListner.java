package chat.hola.com.app.home.social.model;

import android.view.View;

import chat.hola.com.app.home.model.Data;

/**
 * <h1>ClickListner</h1>
 *
 * @author 3Embed
 * @since 24/2/2018.
 */

public interface ClickListner {
    void onItemClick(int position, View view);

    void onUserClick(int position);

    void onUserClick(String userId);

    void onChannelClick(int position);

    void viewAllComments(String postId);

    void send(int position);

    void share(int position);

    void onLikeClicked(int position, boolean liked);

    void onLikerClick(Data data);

    void onViewClick(Data data);

    void onReport(Data data);

    void onEdit(Data data);

    void onDelete(Data data);

    void onActionButtonClick(String title,String s);

    /**
     * Click on saved icon.
     * @param position
     * @param bookMarked
     */
    void savedClick(int position, Boolean bookMarked);

    /**
     * saved view click event
     * @param position
     * @param data
     */
    void savedViewClick(int position, Data data);

    /**
     * On save to collection click
     * @param position
     * @param data
     */
    void onSaveToCollectionClick(int position, Data data);

    /**
     * Long click on saved icon
     * @param position
     * @param bookMarked
     */
    void savedLongCick(int position, Boolean bookMarked);
}
