package chat.hola.com.app.search;

import chat.hola.com.app.Utilities.BaseView;

/**
 * Created by ankit on 24/2/18.
 */

public interface SearchContract {

    interface View extends BaseView {
        void stop();
    }

    interface Presenter {
        void stop();


    }

}
