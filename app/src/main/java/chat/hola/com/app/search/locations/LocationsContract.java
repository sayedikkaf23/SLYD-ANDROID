package chat.hola.com.app.search.locations;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;

/**
 * Created by ankit on 24/2/18.
 */

public interface LocationsContract {

    interface View extends BaseView {
    }

    interface Presenter extends BasePresenter<LocationsContract.View> {
        void search(CharSequence charSequence);
    }
}
