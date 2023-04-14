package chat.hola.com.app.star_configuration;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;

public interface StarConfigContract {

    interface View extends BaseView{

    }

    interface Presenter extends BasePresenter<StarConfigContract.View>{

        void makeChatVisibility(boolean isChecked);
    }
}
