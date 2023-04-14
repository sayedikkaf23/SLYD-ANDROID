package chat.hola.com.app.music;

import java.util.List;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.dubly.Dub;
import chat.hola.com.app.home.model.Data;

/**
 * <h1>HashtagContract</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 1/4/2018.
 */

public interface MusicContract {

    interface View extends BaseView {

        void loading(boolean isLoading);

        void showDetail(int position, List<Data> data);

        void setData(Long totalPosts, Dub musicData);

        void setImage(String image);
    }

    interface Presenter {

        void getMusicData(String musicId, int skip, int limit);

        void callApiOnScroll(String musicId, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount, String from);

        MusicAdapter.ClickListner getPresenter();

        void favorite(String musicId, boolean favorite);

        void getHashtag(String hashtag, int i, int pageSize, String from);

        void getCategoryPost(String categoryId, int i, int pageSize);
    }
}
