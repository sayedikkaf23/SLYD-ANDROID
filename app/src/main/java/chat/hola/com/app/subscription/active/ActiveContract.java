package chat.hola.com.app.subscription.active;

import java.util.List;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;
import chat.hola.com.app.subscription.model.SubData;

public interface ActiveContract {

    interface View extends BaseView {

        void showData(List<SubData> data, boolean clear);

        void onSuccessSubscribe(boolean isChecked);

        void insufficientBalance();

        void refresh();

        void showEmpty(boolean b);
    }

    interface Presenter extends BasePresenter<View> {

        void getSubscriptions(int offset, int limit);

        void subscribeStarUser(boolean isChecked, String id);

        void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);
    }
}
