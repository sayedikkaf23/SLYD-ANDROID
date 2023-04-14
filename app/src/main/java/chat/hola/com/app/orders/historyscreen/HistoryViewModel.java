package chat.hola.com.app.orders.historyscreen;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import chat.hola.com.app.Utilities.Utilities;
import com.appscrip.myapplication.utility.Utility;
import com.kotlintestgradle.UseCaseHandler;
import com.kotlintestgradle.interactor.order.GetOrderHistoryUseCase;
import com.kotlintestgradle.model.orderhistory.OrderHistoryItemData;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.TEN;
import static chat.hola.com.app.Utilities.Constants.TRUE;


/**
 * view model class for history view model
 */
public class HistoryViewModel extends ViewModel {
  public ObservableField<Boolean> progressVisible = new ObservableField<>(FALSE);
  public ObservableField<Boolean> itemLoadingVisible = new ObservableField<>(FALSE);

  public ArrayList<OrderHistoryItemData> mOrderHistoryItemData = new ArrayList<>();
  private MutableLiveData<String> mSearchData = new MutableLiveData<>();
  private GetOrderHistoryUseCase mGetOrderHistoryUseCase;
  private UseCaseHandler mHandler;
  private MutableLiveData<ArrayList<OrderHistoryItemData>> mHistoryItemDataMutableLiveData =
      new MutableLiveData<>();
  private ArrayList<OrderHistoryItemData> mFilterHistoryItemData = new ArrayList<>();
  private MutableLiveData<Integer> mCountLivaData = new MutableLiveData<>();
  private MutableLiveData<Boolean> mApplyFilter = new MutableLiveData<>();

  @Inject
  public HistoryViewModel(GetOrderHistoryUseCase getOrderHistoryUseCase, UseCaseHandler handler) {
    this.mGetOrderHistoryUseCase = getOrderHistoryUseCase;
    this.mHandler = handler;
  }

  /** This method is using for subscribing to User history data */
  public void onHistoryUpdate(int storeType) {
    CompositeDisposable compositeDisposable = new CompositeDisposable();

  }

  /**
   * get the history of orders
   *
   * @param skip  skip
   * @param limit limit
   */
  public void callGetHistoryApi(int skip, int limit, int status, String search, String timeStamp,int storeType) {
    progressVisible.set(TRUE);
    DisposableSingleObserver<GetOrderHistoryUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<GetOrderHistoryUseCase.ResponseValues>() {
          @Override
          public void onSuccess(GetOrderHistoryUseCase.ResponseValues responseValues) {
            progressVisible.set(FALSE);
            itemLoadingVisible.set(FALSE);
            mCountLivaData.setValue(responseValues.getData().getCount());
            if (responseValues.getData() != null) {
              if (!timeStamp.isEmpty()) {
                if (limit > TEN) {
                  mFilterHistoryItemData.addAll(responseValues.getData().getData());
                  mHistoryItemDataMutableLiveData.setValue(mFilterHistoryItemData);
                } else {
                  mFilterHistoryItemData.clear();
                  mFilterHistoryItemData.addAll(responseValues.getData().getData());
                  mHistoryItemDataMutableLiveData.setValue(mFilterHistoryItemData);
                }
              } else if (search.isEmpty()) {
                if (limit > TEN) {
                  mOrderHistoryItemData.addAll(responseValues.getData().getData());
                } else {
                  mOrderHistoryItemData.clear();
                  mOrderHistoryItemData.addAll(responseValues.getData().getData());
                }
                mHistoryItemDataMutableLiveData.setValue(mOrderHistoryItemData);
              } else {
                if (limit > TEN) {
                  mFilterHistoryItemData.addAll(responseValues.getData().getData());
                  mHistoryItemDataMutableLiveData.setValue(mFilterHistoryItemData);
                } else {
                  mFilterHistoryItemData.clear();
                  mFilterHistoryItemData.addAll(responseValues.getData().getData());
                  mHistoryItemDataMutableLiveData.setValue(mFilterHistoryItemData);
                }
              }
            }
          }

          @Override
          public void onError(Throwable e) {
            Utilities.printLog("errro for history "+e.getLocalizedMessage());
            progressVisible.set(FALSE);
            itemLoadingVisible.set(FALSE);
          }
        };
    mHandler.execute(mGetOrderHistoryUseCase,
        new GetOrderHistoryUseCase.RequestValues(limit, skip, status, 0, storeType,
            0, search, timeStamp),
        disposableSingleObserver);
  }

  public void myOrdersClicked() {
    mApplyFilter.postValue(TRUE);
  }

  /**
   * <p>this method is used listen when cross icon clicked.</p>
   */
  public void onCrossIconClicked() {
    mSearchData.postValue("");
  }

  /**
   * This method is using to get the history Mutable mData
   *
   * @return history mutable mData
   */
 public MutableLiveData<ArrayList<OrderHistoryItemData>> getOrderHistoryLiveData() {
    return mHistoryItemDataMutableLiveData;
  }

  /**
   * This method is using to get the history Mutable mData
   *
   * @return history mutable mData
   */
  public MutableLiveData<Boolean> applyFilter() {
    return mApplyFilter;
  }

  /**
   * This method is using to get the history Mutable mData
   *
   * @return history mutable mData
   */
  public MutableLiveData<Integer> getOrderHistoryCount() {
    return mCountLivaData;
  }

  /**
   * returns live data clicking.
   */
  public MutableLiveData<String> searchData() {
    return mSearchData;
  }
}
