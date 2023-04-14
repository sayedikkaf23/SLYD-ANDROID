package chat.hola.com.app.search.tags;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.search.tags.module.Tags;

/**
 * Created by ankit on 24/2/18.
 */

public interface TagsContract {

    interface View extends BaseView {

        void showData(List<Tags> data);

        void noData();
    }

    interface Presenter extends BasePresenter<TagsContract.View> {
        void search(CharSequence charSequence);

    }
}
