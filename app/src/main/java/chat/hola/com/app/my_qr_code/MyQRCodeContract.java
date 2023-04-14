package chat.hola.com.app.my_qr_code;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.profileScreen.model.Data;

public interface MyQRCodeContract {

    interface View extends BaseView{

        void initData(Data profileData);
    }

    interface Presenter extends BasePresenter<MyQRCodeContract.View>{

        void parseIntent(Data profileData);
    }

}
