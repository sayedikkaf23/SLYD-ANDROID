package com.kotlintestgradle.data.repository.observable;

import android.annotation.SuppressLint;
import com.kotlintestgradle.model.ecom.UserData;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observables.ConnectableObservable;

public class UserDataObservable {

  private static UserDataObservable rxConnectable;
  private static ConnectableObservable<UserData> connectableObservable;
  private ObservableEmitter<UserData> emitor;

  @SuppressLint("CheckResult")
  public UserDataObservable() {
    Observable<UserData> observable = Observable.create(new ObservableOnSubscribe<UserData>() {
      @Override
      public void subscribe(ObservableEmitter<UserData> e) throws Exception {
        emitor = e;
      }
    });
    connectableObservable = observable.publish();
    connectableObservable.share();
    connectableObservable.replay();
    connectableObservable.connect();
  }

  public static UserDataObservable getInstance() {
    if (rxConnectable == null) {
      rxConnectable = new UserDataObservable();
      return rxConnectable;
    } else {
      return rxConnectable;
    }
  }

  public static ConnectableObservable<UserData> getObservable() {
    return connectableObservable;
  }

  public void postData(UserData userData) {
    if (emitor != null && userData != null) {
      emitor.onNext(userData);
    }
  }
}
