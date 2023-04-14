package chat.hola.com.app.profileScreen.discover.contact;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.connection.ContactHolder;
import chat.hola.com.app.Networking.observer.ContactObserver;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.discover.contact.pojo.ContactRequest;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contacts;
import chat.hola.com.app.profileScreen.discover.contact.pojo.FollowResponse;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h>ContactPresenter</h>
 *
 * @author 3Embed.
 * @since 02/3/18.
 */

public class ContactPresenter implements ContactContract.Presenter {

    private static final String TAG = ContactPresenter.class.getSimpleName();
    private ContactContract.View view;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    ContactModel contactModel;
    @Inject
    HowdooService service;
    @Inject
    ContactHolder contactHolder;
    @Inject
    ContactObserver contactObserver;
    @Inject
    SessionManager sessionManager;

    @Inject
    public ContactPresenter(Context context) {
    }

    @Override
    public void attachView(ContactContract.View view) {
        this.view = view;
        init();
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void init() {
        if (view != null) {
            view.initialization();
            view.applyFont();
            view.initPostRecycler();
        }
    }

    @Override
    public void addContacts() {
        view.showContacts(contactModel.addContact());
        view.loading(false);
    }

    @Override
    public void fetchContact() {
        observeIt();
        contactModel.contactSync();
    }

    private void observeIt() {
        contactObserver.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ContactHolder>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ContactHolder contactHolder) {
                        contactSync(contactHolder.getContactRequest());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void requestContactsPermission() {
        //  view.requestContactsPermission();
    }

    /**
     * <p>When MQTT responds </p>
     *
     * @param object : contains contact list, event name and other details
     */
    @Override
    public void mqttResponse(JSONObject object) {
        boolean response = false;

        try {
            if (object.getString(Constants.Mqtt.EVENT_NAME).equals(MqttEvents.ContactSync.value + "/" + AppController.getInstance().getUserId())) {
                response = contactModel.saveContactsFromApiResponse(object);

                Contacts contacts = new Gson().fromJson(object.toString(), Contacts.class);

                if (view != null) {
                    //   view.followedAll(contacts.getIsFollowedAll());
                    view.showContacts(contacts.getContacts());
                    view.loading(false);
                }

                AppController.getInstance().setContactSynced(true);
                AppController.getInstance().registerContactsObserver();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (response)
            view.postToBus();
    }

    @Override
    public void followAll(List<String> strings) {
        Map<String, List<String>> map = new HashMap<>();
        map.put("followeeId", strings);
        service.followAll(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if (view != null) {
                            switch (response.code()) {
                                case 200:

                                    view.followedAll(true);
                                    break;
                                case 401:
                                    view.sessionExpired();
                                    break;
                                case 406:
                                    SessionObserver sessionObserver = new SessionObserver();
                                    sessionObserver.getObservable().subscribeOn(Schedulers.io())
                                            .observeOn(AndroidSchedulers.mainThread())
                                            .subscribe(new DisposableObserver<Boolean>() {
                                                @Override
                                                public void onNext(Boolean flag) {
                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            followAll(strings);
                                                        }
                                                    }, 1000);
                                                }

                                                @Override
                                                public void onError(Throwable e) {

                                                }

                                                @Override
                                                public void onComplete() {
                                                }
                                            });
                                    sessionApiCall.getNewSession(service, sessionObserver);
                                    break;

                                default:
                                    view.followedAll(false);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "failed to followAll!!: " + e.getMessage());
                        view.followedAll(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * <p>It sends all device contacts to server</p>
     *
     * @param request : @ContactRequest which contains list of contact details
     */
    @Override
    public void contactSync(ContactRequest request) {
        AppController.getInstance().subscribeToTopic(MqttEvents.ContactSync.value + "/" + AppController.getInstance().getUserId(), 1);

        service.postUser(sessionManager.getGuestToken(), AppController.getInstance().getApiToken(), request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        switch (response.code()) {
                            case 401:
                                view.sessionExpired();
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable().subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        contactSync(request);
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onComplete() {
                                            }
                                        });
                                sessionApiCall.getNewSession(service, sessionObserver);
                                break;

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (view != null) {
                            //  view.showMessage(e.getMessage(), 0);
                            view.loading(false);
                        }
                    }

                    @Override
                    public void onComplete() {
                        if (view != null)
                            view.loading(false);
                    }
                });
    }

    /**
     * <p>It follows user</p>
     *
     * @param pos          : position of data list, which we requires to pass after result
     * @param followingId: user id to whom user wants to follow
     */
    @Override
    public void follow(final int pos, String followingId) {
        Map<String,Object> map = new HashMap<>();
        map.put("followingId",followingId);
        service.follow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> response) {
                        switch (response.code()) {
                            case 200:
                                view.isFollowing(pos, true, response.body().getStatus(), response.body().getIsPrivate(), response.body().getIsAllFollow());
                                contactModel.updateFolow(response.body().getUserId(), response.body().getStatus());
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable().subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        follow(pos, followingId);
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onComplete() {
                                            }
                                        });
                                sessionApiCall.getNewSession(service, sessionObserver);
                                break;

                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //  view.showMessage(e.getMessage(), 0);
                        // view.isFollowing(pos, false, false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    /**
     * <p>It unfollows user</p>
     *
     * @param pos          : position of data list, which we requires to pass after result
     * @param followingId: user id to whom user wants to unfollow
     */
    @Override
    public void unfollow(final int pos, String followingId) {
        Map<String,Object> map = new HashMap<>();
        map.put("followingId",followingId);
        service.unfollow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<FollowResponse>>() {
                    @Override
                    public void onNext(Response<FollowResponse> response) {
                        switch (response.code()) {
                            case 200:
                                view.isFollowing(pos, false, 0, response.body().getIsPrivate(), response.body().getIsAllFollow());
                                contactModel.updateFolow(response.body().getUserId(), response.body().getStatus());
                                break;
                            case 406:
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable().subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        unfollow(pos, followingId);
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onComplete() {
                                            }
                                        });
                                sessionApiCall.getNewSession(service, sessionObserver);
                                break;

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //   view.showMessage(e.getMessage(), 0);
//                        view.isFollowing(pos, true, false);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public String getUserId(int pos) {
        return contactModel.getUserId(pos);
    }
}
