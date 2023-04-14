package chat.hola.com.app.my_qr_code;

import javax.inject.Inject;

import chat.hola.com.app.profileScreen.model.Data;

public class MyQRCodePrensenter implements MyQRCodeContract.Presenter {

    @Inject
    MyQRCodeContract.View view;

    @Inject
    public MyQRCodePrensenter() {
    }

    @Override
    public void attachView(MyQRCodeContract.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void parseIntent(Data profileData) {
        view.initData(profileData);
    }
}
