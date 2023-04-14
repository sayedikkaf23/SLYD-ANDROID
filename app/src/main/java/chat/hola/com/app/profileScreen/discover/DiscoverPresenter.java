package chat.hola.com.app.profileScreen.discover;

import android.content.Context;
import android.os.Handler;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.HowdooServiceTrending;
import chat.hola.com.app.Networking.connection.ContactHolder;
import chat.hola.com.app.Networking.observer.ContactObserver;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.post.ReportReason;
import chat.hola.com.app.profileScreen.discover.contact.ContactAdapter;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contact;
import chat.hola.com.app.profileScreen.discover.contact.pojo.ContactRequest;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contacts;
import chat.hola.com.app.profileScreen.discover.contact.pojo.FollowResponse;
import com.google.gson.Gson;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

/**
 * <h>DiscoverPresenter</h>
 *
 * @author 3Embed.
 * @since 2/3/18.
 */

public class DiscoverPresenter implements DiscoverContract.Presenter, ContactAdapter.ClickListner, ContactAdapter.FilterListener {
  private static final String TAG = DiscoverPresenter.class.getSimpleName();
  @Inject
  ContactModel contactModel;
  @Inject
  DiscoverContract.View view;
  @Inject
  NetworkConnector networkConnector;
  @Inject
  ContactObserver contactObserver;
  @Inject
  HowdooService service;
  @Inject
  HowdooServiceTrending contactSyncService;
  private SessionApiCall sessionApiCall = new SessionApiCall();
    private ContactHolder contactHolder;

  @Inject
  public DiscoverPresenter() {
  }

  @Override
  public void init(Context context) {
    view.applyFont();
    contactModel.getAllCountries(context);
  }

  @Override
  public void changeText(String text) {
    view.changeSkipText(text);
  }

  public void fetchContact() {
    observeIt();
    contactModel.contactSync();
  }

  /**
   * <p>When MQTT responds </p>
   *
   * @param object : contains contact list, event name and other details
   */
  public void mqttResponse(JSONObject object) {
    boolean response = false;

    try {
      if (object.getString(Constants.Mqtt.EVENT_NAME)
          .equals(MqttEvents.ContactSync.value + "/" + AppController.getInstance().getUserId())) {
        //                response = contactModel.saveContactsFromApiResponse(object);

        Contacts contacts = new Gson().fromJson(object.toString(), Contacts.class);

        if (view != null) {
          //   view.followedAll(contacts.getIsFollowedAll());
          view.showContacts(contacts.getContacts(), true);
        }

        AppController.getInstance().setContactSynced(true);
        AppController.getInstance().registerContactsObserver();
        response = true;
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }

    if (response) view.postToBus();
  }

  private void observeIt() {
    contactObserver.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Observer<ContactHolder>() {

            @Override
          public void onSubscribe(Disposable d) {

          }

          @Override
          public void onNext(ContactHolder holder) {
                contactHolder = holder;
                page = 0;
            contactSync(contactHolder.getContactRequest(), PAGE_SIZE * page, PAGE_SIZE);
          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onComplete() {

          }
        });
  }

    public static final int PAGE_SIZE = 100;
    public static int page = 0;
    private boolean isLastPage = false;

    @Override
    public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
//        if (!isLoading && !isLastPage) {
//            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
//                    && firstVisibleItemPosition >= 0
//                    && totalItemCount >= PAGE_SIZE) {
//                page++;
//                contactSync(contactHolder.getContactRequest(), PAGE_SIZE * page, PAGE_SIZE);
//            }
//        }
    }

  public void contactSync(ContactRequest request, int skip, int limit) {
    AppController.getInstance()
        .subscribeToTopic(
            MqttEvents.ContactSync.value + "/" + AppController.getInstance().getUserId(), 1);
    contactSyncService.contactSync(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
            skip,
            limit,
            request)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<Contacts>>() {
          @Override
          public void onNext(Response<Contacts> response) {
            switch (response.code()) {
              case 200:
                  if (response.body() != null) {

                      isLastPage = response.body().isLastPage();

                      if (view != null) {
                          view.showContacts(response.body().getContacts(),skip==0);
                      }

                      if(!isLastPage) {
                          //page++;
                          //contactSync(contactHolder.getContactRequest(),page*PAGE_SIZE,PAGE_SIZE);
                      }else{
                          page = 0;
                          AppController.getInstance().setContactSynced(true);
                      }
                  }

                AppController.getInstance().registerContactsObserver();
                break;
              case 401:
                view.sessionExpired();
                view.empty(true);
                break;
              case 406:
                SessionObserver sessionObserver = new SessionObserver();
                sessionObserver.getObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Boolean>() {
                      @Override
                      public void onNext(Boolean flag) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                          @Override
                          public void run() {
                            contactSync(request, PAGE_SIZE * page, PAGE_SIZE);
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
                sessionApiCall.getNewSession(sessionObserver);
                break;
            }
          }

          @Override
          public void onError(Throwable e) {

            //view.empty(true);
          }

          @Override
          public void onComplete() {
          }
        });
  }

  @Override
  public void add(int position) {
    view.add(position);
  }

  public void addAsFriend(Contact data) {
    Map<String, Object> map = new HashMap<>();
    map.put("targetUserId", data.getId());
    map.put("message", "friend");
    service.addFriend(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<ReportReason>>() {
          @Override
          public void onNext(Response<ReportReason> reportReasonResponse) {
            switch (reportReasonResponse.code()) {
              case 200:
                assert view != null;
                view.reload();
                break;
              case 406:
                SessionObserver sessionObserver = new SessionObserver();
                sessionObserver.getObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Boolean>() {
                      @Override
                      public void onNext(Boolean flag) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                          @Override
                          public void run() {
                            addAsFriend(data);
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
                sessionApiCall.getNewSession(sessionObserver);
            }
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
  public void view(int position) {
    view.onUserSelected(position);
  }

  @Override
  public void onUserSelected(int position) {
    view.onUserSelected(position);
  }

  @Override
  public void onFollow(String userId, boolean follow, int position) {
    if (follow) {
      follow(position, userId);
    } else {
      unfollow(position, userId);
    }
  }


    public void follow(final int pos, String followingId) {
    Map<String, Object> map = new HashMap<>();
    map.put("followingId", followingId);
    service.follow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<FollowResponse>>() {
          @Override
          public void onNext(Response<FollowResponse> response) {
            if (view != null) {
              switch (response.code()) {
                case 200:
                  view.isFollowing(pos, 2);// private
                  break;
                case 201:
                  view.isFollowing(pos, 1);
                  break;
                case 406:
                  SessionObserver sessionObserver = new SessionObserver();
                  sessionObserver.getObservable()
                      .subscribeOn(Schedulers.io())
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
          }

          @Override
          public void onError(Throwable e) {

          }

          @Override
          public void onComplete() {

          }
        });
  }

  public void unfollow(final int pos, String followingId) {
    Map<String, Object> map = new HashMap<>();
    map.put("followingId", followingId);
    service.unfollow(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<FollowResponse>>() {
          @Override
          public void onNext(Response<FollowResponse> response) {

            switch (response.code()) {
              case 200:
                if (view != null) {
                  view.isFollowing(pos, 0);
                }
                break;
              case 406:
                SessionObserver sessionObserver = new SessionObserver();
                sessionObserver.getObservable()
                    .subscribeOn(Schedulers.io())
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
          }

          @Override
          public void onComplete() {
          }
        });
  }

    @Override
    public void onFilter(int count) {
        view.onFilter(count);
    }
}
