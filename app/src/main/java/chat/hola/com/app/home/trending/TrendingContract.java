package chat.hola.com.app.home.trending;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.home.trending.model.Header;

/**
 * Created by ankit on 21/2/18.
 */

public interface TrendingContract {

    interface View extends BaseView {
        void initHeaderRecycler();

        void initContentRecycler();

        void showHeader(ArrayList<Header> headers);

        void showContent(ArrayList<Data> trendings);

        void onPostClick(List<Data> items, int position, android.view.View view);

        void isLoading(boolean flag);

        void isContentAvailable(boolean flag);

        void setHashTags(StringBuilder tags);

        void setCategoryId(String categoryId, String categoryName);

        void onStarCLick();

        void onLiveStreamCLick();
    }

    interface Presenter extends BasePresenter<TrendingContract.View> {
        void init();

        void loadHeader();

        void loadContent(int skip, int limit);
    }

}
