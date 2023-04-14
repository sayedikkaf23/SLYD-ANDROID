package com.kotlintestgradle.data.repository.observable;

import android.annotation.SuppressLint;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.observables.ConnectableObservable;

public class DefaultAddressObservable {
  private static DefaultAddressObservable rxConnectable;
  private static ConnectableObservable<AddressListItemData> connectableObservable;
  private ObservableEmitter<AddressListItemData> emitor;

  @SuppressLint("CheckResult")
  public DefaultAddressObservable() {
    Observable<AddressListItemData> observable = Observable.create(
        new ObservableOnSubscribe<AddressListItemData>() {
          @Override
          public void subscribe(ObservableEmitter<AddressListItemData> e) throws Exception {
            emitor = e;
          }
        });
    connectableObservable = observable.publish();
    connectableObservable.share();
    connectableObservable.replay();
    connectableObservable.connect();
  }

  public static DefaultAddressObservable getInstance() {
    if (rxConnectable == null) {
      rxConnectable = new DefaultAddressObservable();
      return rxConnectable;
    } else {
      return rxConnectable;
    }
  }

  public static ConnectableObservable<AddressListItemData> getObservable() {
    return connectableObservable;
  }

  public void postData(AddressListItemData userData) {
    if (emitor != null && userData != null) {
      emitor.onNext(userData);
    }
  }
}
