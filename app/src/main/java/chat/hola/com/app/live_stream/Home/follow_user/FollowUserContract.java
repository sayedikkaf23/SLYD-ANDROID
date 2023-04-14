package chat.hola.com.app.live_stream.Home.follow_user;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;

public interface FollowUserContract {

    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter<View> {
        void follow(String userId);

        void unFollow(String userId);
    }
}
