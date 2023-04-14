package chat.hola.com.app.home.contact;

import android.os.Handler;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.models.UserObserver;
import chat.hola.com.app.profileScreen.discover.contact.pojo.FollowResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;
import retrofit2.Response;

/**
 * <h1>AddContactActivity</h1>
 *
 * @author 3Embed
 * @since 4/5/2018.
 */

public class ContactPresenter
    implements ContactAdapter.ClickListner, FriendRequestAdapter.ClickListner,
    ContactContract.Presenter {

  @Inject
  ContactFriendModel model;
  @Inject
  HowdooService service;
  @Inject
  UserObserver userObserver;
  private ContactContract.View view;
  private SessionApiCall sessionApiCall = new SessionApiCall();
  public static int count = 0;

  @Inject
  ContactPresenter() {
  }

  public void observe() {
    userObserver.getObservable()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Integer>() {
          @Override
          public void onNext(Integer action) {
            switch (action) {
              case 1:
                //accept
                newFriends();
                friends();
                break;
              case 2:
                //reject
                newFriends();
                break;
              case 3:
                //block
                friends();
                break;
              case 4:
                //request sent
                newFriends();
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
  public void newFriends() {
    service.getFriendRequests(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<GetFriendRequest>>() {
          @Override
          public void onNext(Response<GetFriendRequest> response) {

            switch (response.code()) {
              case 200:
                count = response.body().getData().getFriendRequests().size();
                //                                assert response.body() != null;
                //                                model.setFriendRequestList(response.body().getData().getFriendRequests());
                if (view != null) {
                  view.friendRequests(true);
                  view.newFriendCount(response.body().getData().getFriendRequests().size());
                }
                break;
              case 204:
                if (view != null) {
                  view.friendRequests(false);
                  view.newFriendCount(0);
                }
                break;
              case 401:
                if (view != null) view.sessionExpired();
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
                            newFriends();
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
            if (view != null) view.friendRequests(false);
          }

          @Override
          public void onComplete() {
            if (view != null) view.loadingCompleted();
          }
        });
  }

  @Override
  public void friends() {

    service.getFollowersFollowee(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<GetFriends>>() {
          @Override
          public void onNext(Response<GetFriends> response) {

            switch (response.code()) {
              case 200:
                model.clearList();

                //                                if (view != null) {
                //                                    assert response.body() != null;
                //                                    view.scroll(response.body().getData() == null || response.body().getData().isEmpty());
                //                                }

                if( response.body()  != null){
                if (response.body().getData() != null) {

                  model.setFriendList(response.body().getData());
                  model.saveFriendsToDb(response.body().getData());
                }}

                if (!AppController.getInstance().getChatSynced()) {

                  try {
                    JSONObject object = new JSONObject();

                    object.put("eventName", "SyncChats");

                    AppController.getBus().post(object);
                  } catch (JSONException e) {
                    e.printStackTrace();
                  }
                }
                if (!AppController.getInstance().isFriendsFetched()) {
                  AppController.getInstance().setFriendsFetched(true);
                }

                break;
              case 204:
                if (view != null) view.noContent();

                if (!AppController.getInstance().getChatSynced()) {

                  try {
                    JSONObject object = new JSONObject();

                    object.put("eventName", "SyncChats");

                    AppController.getBus().post(object);
                  } catch (JSONException e) {
                    e.printStackTrace();
                  }
                }

                if (!AppController.getInstance().isFriendsFetched()) {
                  AppController.getInstance().setFriendsFetched(true);
                }
                break;
              case 401:
                if (view != null) view.sessionExpired();
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
                            friends();
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

          }

          @Override
          public void onComplete() {
            if (view != null) view.loadingCompleted();
          }
        });
  }

  @Override
  public void searchFriends(String character) {
    service.searchFriends(AppController.getInstance().getApiToken(), Constants.LANGUAGE, character)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<GetFriends>>() {
          @Override
          public void onNext(Response<GetFriends> response) {

            switch (response.code()) {
              case 200:
                //                                model.clearList();
                //                                assert response.body() != null;
                //                                model.setFriendList(response.body().getData());
                break;
              case 204:
                if (view != null) view.noContent();
                break;
              case 401:
                if (view != null) view.sessionExpired();
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
                            searchFriends(character);
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

          }

          @Override
          public void onComplete() {
            if (view != null) view.loadingCompleted();
          }
        });
  }

  @Override
  public void attachView(ContactContract.View view) {
    this.view = view;
  }

  @Override
  public void detachView() {
    this.view = null;
  }

  @Override
  public void onItemSelect(int position) {
    if (view != null) view.openChatForItem(model.getFriend(position), position);
  }

  @Override
  public void onUserSelected(int position) {
    if (view != null) view.onUserSelected(model.getFriend(position));
  }

  @Override
  public void onFollow(String id, boolean follow, int position) {
    if (follow) {
      follow(position, id);
    } else {
      unfollow(position, id);
    }
  }

  public ContactAdapter.ClickListner getPresenter() {
    return this;
  }

  @Override
  public void onFriendRequestSelected(int position) {
    if (view != null) view.onFriendRequestSelected(model.getRequestedFriend(position));
  }

  public void stars() {
    service.stars(AppController.getInstance().getApiToken(), Constants.LANGUAGE, 0, 100)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableObserver<Response<GetFriends>>() {
          @Override
          public void onNext(Response<GetFriends> response) {

            switch (response.code()) {
              case 200:
                model.clearList();
                if( response.body()  != null){
                model.setFriendList(response.body().getData());}
                break;
              case 204:
                if (view != null) view.noContent();
                break;
              case 401:
                if (view != null) view.sessionExpired();
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
                            stars();
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

          }

          @Override
          public void onComplete() {
            if (view != null) view.loadingCompleted();
          }
        });
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
}
