package chat.hola.com.app.live_stream.Home.follow_user;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.discover.contact.pojo.FollowResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FollowUserPresenter implements FollowUserContract.Presenter {

    private FollowUserContract.View view;

    @Inject
    HowdooService service;
    @Inject
    SessionManager sessionManager;

    @Inject
    FollowUserPresenter() {

    }

    @Override
    public void follow(String userId) {
        view.showLoader();
        Map<String, Object> map = new HashMap<>();
        map.put("followingId", userId);
        service.follow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> followResponseResponse) {
                        view.hideLoader();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoader();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void unFollow(String userId) {
        view.showLoader();
        Map<String, Object> map = new HashMap<>();
        map.put("followingId", userId);
        service.unfollow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> followResponseResponse) {
                        view.hideLoader();
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.hideLoader();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void attach(FollowUserContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        view = null;
    }
}
