package chat.hola.com.app.live_stream.Home;

import com.google.gson.Gson;

import javax.inject.Inject;

import chat.hola.com.app.Networking.LiveStreamService;
import chat.hola.com.app.manager.session.SessionManager;
import io.reactivex.annotations.Nullable;

public class ProfilePresenterImpl implements ProfilePresenterContract.ProfilePresenter {
    @Inject
    LiveStreamService apiServices;
    @Inject
    Gson gson;
    @Inject
    SessionManager manager;

    @Nullable
    ProfilePresenterContract.ProfileView view;

    @Inject
    public ProfilePresenterImpl() {
    }

    @Override
    public void attachView(Object view) {
        this.view = (ProfilePresenterContract.ProfileView) view;
    }

    @Override
    public void detachView() {

        view = null;
    }

}
