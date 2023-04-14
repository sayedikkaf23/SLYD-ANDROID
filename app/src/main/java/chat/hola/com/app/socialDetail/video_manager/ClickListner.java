package chat.hola.com.app.socialDetail.video_manager;

import android.widget.ImageButton;

import chat.hola.com.app.home.model.Data;
import java.util.ArrayList;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 12/1/2018.
 */
public interface ClickListner {
    void like(boolean isChecked, String postId);

    void follow(boolean isChecked, String postId);

    void comment(Data data, String postId, int position, String commentCount, boolean commentsEnabled);

    void send(int position);

    void profile(String postId, boolean isChannel, boolean isBusiness);

    void category(String categoryId, String categoryName);

    void channel(String channelId, String channelName);

    void music(String id, String name, String path, String artist);

    void showCustomShareSheet(Data data, int position, long duration);

    void openMenu(Data data, ImageButton ibMenu);

    void view(Data data);

    void location(Data data);

    void openLikers(Data data);

    void openViewers(Data data);

    void onActionButtonClick(String businessButtonText, String businessUrl);

    void savedClick(int position, Boolean bookMarked);

    void savedLongCick(int position, Boolean bookMarked);

    void savedViewClick(int position, Data data);

    void onSaveToCollectionClick(int position, Data data);

    void showAds();

    void callProductsApi(ArrayList<String> productIds);
    void sendTip(int position);

    void payForPost(Data data, int position, boolean isConfirm);

    void subscribeProfile(Data data, int position, boolean isConfirm);
//    void setImage(ImageView mCover);
}
