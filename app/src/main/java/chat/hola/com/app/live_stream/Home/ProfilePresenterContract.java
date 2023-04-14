package chat.hola.com.app.live_stream.Home;

public class ProfilePresenterContract {


    interface ProfilePresenter extends BasePresenter {


    }

    interface ProfileView {
        void signOutResponse();
        void showFailedSignoutAlert(String message);
    }

}
