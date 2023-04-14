package chat.hola.com.app.tracking;

import android.view.View;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.kotlintestgradle.UseCaseHandler;
import com.kotlintestgradle.interactor.order.TrackingOrderUseCase;
import com.kotlintestgradle.model.tracking.TrackingItemData;

import java.util.ArrayList;

import javax.inject.Inject;

import chat.hola.com.app.orders.BackButtonClickListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.TRUE;


/**
 * view model class for the tracking activity.
 */
public class EcomTrackingViewModel extends ViewModel {
  public ObservableField<Boolean> progressVisible = new ObservableField<>(FALSE);

  private MutableLiveData<ArrayList<TrackingItemData>> mListMutableLiveData =
          new MutableLiveData<>();
  private TrackingOrderUseCase mTrackingOrderUseCase;
  private UseCaseHandler mHandler;
  private MutableLiveData<Boolean> mBack = new MutableLiveData<>();

  @Inject
  public EcomTrackingViewModel(TrackingOrderUseCase trackingOrderUseCase,
                               UseCaseHandler useCaseHandler) {
    this.mTrackingOrderUseCase = trackingOrderUseCase;
    this.mHandler = useCaseHandler;
  }

/*  *//** This method is using for subscribing to User tracking data *//*
  void onTrackingUpdate() {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    compositeDisposable.add(
            userInfoHandler
                    .getTrackingObservable()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(trackingData -> {
                      if (trackingData != null) {
                        if (trackingData.getAction() != null) {
                          int action = Integer.parseInt(trackingData.getAction());
                          if (action == THREE) {
                            if (trackingData.getProduct_order_id() != null) {
                              callGetDeliveryStatusApi(trackingData.getProduct_order_id());
                            }
                          }
                        }
                      }
                    }));
  }*/

  /**
   * call the get reasons order  api
   */
  void callGetDeliveryStatusApi(String productOrderId) {
    progressVisible.set(TRUE);
    DisposableSingleObserver<TrackingOrderUseCase.ResponseValues> disposableSingleObserver =
            new DisposableSingleObserver<TrackingOrderUseCase.ResponseValues>() {
              @Override
              public void onSuccess(TrackingOrderUseCase.ResponseValues responseValues) {
                progressVisible.set(FALSE);
                if (responseValues.getData() != null) {
                  mListMutableLiveData.postValue(responseValues.getData().getOrderStatus());
                }
              }

              @Override
              public void onError(Throwable e) {
                progressVisible.set(FALSE);
              }
            };
    mHandler.execute(mTrackingOrderUseCase,
            new TrackingOrderUseCase.RequestValues(productOrderId),
            disposableSingleObserver);
  }

  /**
   * subscribe to  click on views.
   */
  MutableLiveData<ArrayList<TrackingItemData>> onGetTrackingData() {
    return mListMutableLiveData;
  }

  /**
   * subscribe to  click on views.
   */
  MutableLiveData<Boolean> onBackClicked() {
    return mBack;
  }


}
