package chat.hola.com.app.stars;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.home.contact.Friend;

public interface StarContact {
    interface View extends BaseView {

        void noContent();

        void loadingCompleted();

        void onUserSelected(Friend data);

    }

    interface Presenter {

        void stars(int offset, int limit);

        Friend getFriend(int position);

        void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);
    }
}
