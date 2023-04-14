package chat.hola.com.app.profileScreen.collection;

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.collections.model.CollectionData;
import chat.hola.com.app.models.PostUpdateData;

public interface CollectionContract {

    interface View extends BaseView {
        void setupRecyclerView();

        void isLoading(boolean flag);

        void showEmptyUi(boolean show);

        void loadData();

        /**
         * <p>Used to set updated saved status of post</p>
         *
         * @param saveData
         * */
        void updateSavedOnObserve(PostUpdateData saveData);

        void onSuccess(List<CollectionData> data, boolean isClear);
    }

    interface Presenter extends BasePresenter<View> {
        void init();

        public void getCollections(int offSet, int limit);

        void callApiOnScroll(int firstVisibleItemPosition, int childCount, int itemCount);
    }
}
