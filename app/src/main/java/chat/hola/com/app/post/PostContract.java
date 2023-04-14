package chat.hola.com.app.post;

import java.util.List;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.hastag.Hash_tag_people_pojo;
import chat.hola.com.app.post.model.CategoryData;
import chat.hola.com.app.post.model.ChannelData;

/**
 * Created by ankit on 21/2/18.
 */

public interface PostContract {

    interface View extends BaseView {
        void applyFont();

        void displayMedia();

        void onBackPress();

        void setTag(Hash_tag_people_pojo response);

        void setUser(Hash_tag_people_pojo response);

        void attacheCategory(List<CategoryData> data);

        void attacheChannels(List<ChannelData> data);

        void showCategory(List<CategoryData> data);

        void updated();

        void onSuccessCoinAmount(List<String> data);
    }

    interface Presenter {
        void init(String path, String type);

        void message(String s);

        void onBackPress();

        void searchHashTag(String hashTag);

        void getChannels();

        void getCategories();

        void getCategory();

        void searchUserTag(String username);

        void getSuggestedCoinAmount();
    }
}
