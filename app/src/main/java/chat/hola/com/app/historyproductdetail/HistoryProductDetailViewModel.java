package chat.hola.com.app.historyproductdetail;

import android.util.Pair;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.ezcall.android.R;
import com.kotlintestgradle.UseCaseHandler;
import com.kotlintestgradle.interactor.order.GetOrderDetailsUseCase;
import com.kotlintestgradle.interactor.order.GetPackageDetailsUseCase;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;

import java.util.Objects;

import javax.inject.Inject;

import chat.hola.com.app.Utilities.BackButtonClickListener;
import io.reactivex.observers.DisposableSingleObserver;

import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_ORDER_TYPE;
import static chat.hola.com.app.Utilities.Constants.THREE;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static chat.hola.com.app.historyproductdetail.HistoryProductDetailUiAction.BACK;
import static chat.hola.com.app.historyproductdetail.HistoryProductDetailUiAction.DOWNLOAD_INVOICE;
import static chat.hola.com.app.historyproductdetail.HistoryProductDetailUiAction.RATE_NOW;
import static chat.hola.com.app.historyproductdetail.HistoryProductDetailUiAction.TRACKING;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;


/**
 * view model class for the HistoryProductDetail activity
 */
public class HistoryProductDetailViewModel extends ViewModel  {
  public ObservableField<String> deliveryAddressName = new ObservableField<>();
  public ObservableField<String> deliveryAddress = new ObservableField<>();
  public ObservableField<String> deliveryPhoneNum = new ObservableField<>();
  public ObservableField<String> billingAddressName = new ObservableField<>();
  public ObservableField<String> billingAddress = new ObservableField<>();
  public ObservableField<String> billingPhoneNum = new ObservableField<>();
  public ObservableField<String> trackingId = new ObservableField<>();
  public ObservableField<Boolean> progressVisible = new ObservableField<>(FALSE);
  public ObservableField<Boolean> returnProductVisible = new ObservableField<>(FALSE);
  public ObservableField<String> cancelOrReorder = new ObservableField<>();
  public String productOrderId = "";
  private MutableLiveData<HistoryProductDetailUiAction> mClick = new MutableLiveData<>();
  private GetPackageDetailsUseCase mGetPackageDetailsUseCase;
  private UseCaseHandler mHandler;
  private MutableLiveData<Pair<Boolean, Object>> mOrderDataMutableLiveData =
      new MutableLiveData<>();
 // private ReorderUseCase mReorderUseCase;
  private GetOrderDetailsUseCase mGetOrderDetailUSeCase;
  private int mStatus;

  /**
   * constructor for this class
   */
  @Inject
  public HistoryProductDetailViewModel(GetPackageDetailsUseCase packageDetailsUseCase,
     /* ReorderUseCase reorderUseCase,*/ GetOrderDetailsUseCase getOrderDetailUSeCase,
      UseCaseHandler handler) {
    this.mHandler = handler;
  //  this.mReorderUseCase = reorderUseCase;
    this.mGetPackageDetailsUseCase = packageDetailsUseCase;
    this.mGetOrderDetailUSeCase = getOrderDetailUSeCase;
  }

  /**
   * call the package details use case
   */
  void getPackageDetails(String productOrderId, String packageId) {
    progressVisible.set(TRUE);
    DisposableSingleObserver<GetPackageDetailsUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<GetPackageDetailsUseCase.ResponseValues>() {
          @Override
          public void onSuccess(GetPackageDetailsUseCase.ResponseValues responseValues) {
            progressVisible.set(FALSE);
            mOrderDataMutableLiveData.postValue(Pair.create(TRUE, responseValues.getData()));
            if (responseValues.getData().getProducts().get(ZERO) != null) {
              mStatus = Integer.parseInt(responseValues.getData().getProducts().get(
                  ZERO).getStatus().getStatus());
              if (mStatus == ONE || mStatus == TWO) {
                cancelOrReorder.set(
                    "Cancel");
              }/* else if (mStatus == THREE || mStatus == SEVEN) {
                cancelOrReorder.set(
                    ApplicationManager.getInstance().getString(R.string.historyReOrder));
              }
              if (mStatus == SEVEN) {
                returnProductVisible.set(TRUE);
              }*/
            }
            trackingId.set((responseValues.getData().getPartnerData().getTrackingId() != null
                && !responseValues.getData().getPartnerData().getTrackingId().isEmpty())
                ? String.format("%s (%s)",
                "Track Shipment",
                responseValues.getData().getPartnerData().getTrackingId())
                : String.format("%s", "Track Shipment"));
            if (responseValues.getData().getDeliveryAddress() != null) {
              AddressListItemData addressListItemData =
                  responseValues.getData().getDeliveryAddress();
              deliveryAddressName.set(addressListItemData.getName());
              deliveryAddress.set(String.format("%s%s,%s,%s", addressListItemData.getAddLine1(),
                  addressListItemData.getAddLine2(), addressListItemData.getLocality(),
                  addressListItemData.getPincode()));
              deliveryPhoneNum.set(String.format("%s:%s %s",
                 "Phone",
                  addressListItemData.getMobileNumberCode(),
                  addressListItemData.getMobileNumber()));
            }
            if (responseValues.getData().getBillingAddress() != null) {
              AddressListItemData addressListItemData =
                  responseValues.getData().getBillingAddress();
              billingAddressName.set(addressListItemData.getName());
              billingAddress.set(String.format("%s%s,%s,%s", addressListItemData.getAddLine1(),
                  addressListItemData.getAddLine2(), addressListItemData.getLocality(),
                  addressListItemData.getPincode()));
              billingPhoneNum.set(String.format("%s:%s %s",
                 "Phone",
                  addressListItemData.getMobileNumberCode(),
                  addressListItemData.getMobileNumber()));
            }
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
          }
        };
    mHandler.execute(mGetPackageDetailsUseCase,
        new GetPackageDetailsUseCase.RequestValues(productOrderId, packageId),
        disposableSingleObserver);
  }

  /**
   * call the product details api
   *
   * @param orderId product id
   */
  void callGetHistoryApi(String orderId) {
    progressVisible.set(TRUE);
    DisposableSingleObserver<GetOrderDetailsUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<GetOrderDetailsUseCase.ResponseValues>() {
          @Override
          public void onSuccess(GetOrderDetailsUseCase.ResponseValues responseValues) {
            progressVisible.set(FALSE);
            mOrderDataMutableLiveData.postValue(Pair.create(FALSE, responseValues.getData()));
            if (responseValues.getData().getProducts() != null
                && responseValues.getData().getProducts().size() > ZERO) {
              int status = Integer.parseInt(responseValues.getData().getProducts().get(
                  ZERO).getStatus().getStatus());
              if (status == ONE || status == TWO) {
                cancelOrReorder.set("Cancel");
              }/* else if (status == THREE || status == 7) {
                cancelOrReorder.set(
                    ApplicationManager.getInstance().getString(R.string.historyReOrder));
              }*/
           /*   if (status == SEVEN) {
                returnProductVisible.set(TRUE);
              }*/
            }
            if (responseValues.getData().getDeliveryAddress() != null) {
              AddressListItemData addressListItemData =
                  responseValues.getData().getDeliveryAddress();
              deliveryAddressName.set(addressListItemData.getName());
              deliveryAddress.set(String.format("%s%s,%s,%s", addressListItemData.getAddLine1(),
                  addressListItemData.getAddLine2(), addressListItemData.getLocality(),
                  addressListItemData.getPincode()));
              deliveryPhoneNum.set(String.format("%s:%s %s",
                  "Phone",
                  addressListItemData.getMobileNumberCode(),
                  addressListItemData.getMobileNumber()));
            }
            if (responseValues.getData().getBillingAddress() != null) {
              AddressListItemData addressListItemData =
                  responseValues.getData().getBillingAddress();
              billingAddressName.set(addressListItemData.getName());
              billingAddress.set(String.format("%s%s,%s,%s", addressListItemData.getAddLine1(),
                  addressListItemData.getAddLine2(), addressListItemData.getLocality(),
                  addressListItemData.getPincode()));
              billingPhoneNum.set(String.format("%s:%s %s",
                  "Phone",
                  addressListItemData.getMobileNumberCode(),
                  addressListItemData.getMobileNumber()));
            }
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
          //  EcomUtil.printLog("History Fail" + e.getMessage());
          }
        };
    mHandler.execute(mGetOrderDetailUSeCase,
        new GetOrderDetailsUseCase.RequestValues(PRODUCT_ORDER_TYPE, orderId, ZERO),
        disposableSingleObserver);
  }

/*  *//**
   * call the reorder  api
   *
   * @param orderId product id
   *//*
  private void callReorderApi(String orderId) {
    progressVisible.set(TRUE);
    DisposableSingleObserver<ReorderUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<ReorderUseCase.ResponseValues>() {
          @Override
          public void onSuccess(ReorderUseCase.ResponseValues responseValues) {
            progressVisible.set(FALSE);
            mClick.postValue(REORDER);
            EcomUtil.printLog("exe" + "History Succ");
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
            EcomUtil.printLog("History Fail" + e.getMessage());
          }
        };
    mHandler.execute(mReorderUseCase,
        new ReorderUseCase.RequestValues(PRODUCT_ORDER_TYPE, orderId),
        disposableSingleObserver);
  }*/

  /**
   * listen when cancel order clicked.
   */
 /* public void cancelOrReorderClicked() {
    if (Objects.requireNonNull(cancelOrReorder.get()).equals(
        ApplicationManager.getInstance().getString(R.string.historyReOrder))) {
      callReorderApi(productOrderId);
    } else {
      mClick.postValue(CANCEL_ORDER);
    }
  }*/

  /**
   * listen click on rate now.
   */
  public void rateNow() {
    mClick.postValue(RATE_NOW);
  }

  /**
   * listen click on rate now.
   */
  public void downLoadInvoice() {
    mClick.postValue(DOWNLOAD_INVOICE);
  }

  /**
   * listen click on rate now.
   */
  public void tracking() {
    mClick.postValue(TRACKING);
  }

  /**
   * subscribe to  click on views.
   */
  public MutableLiveData<HistoryProductDetailUiAction> onClick() {
    return mClick;
  }

  /**
   * subscribe to  click on views.
   */
  public MutableLiveData<Pair<Boolean, Object>> getData() {
    return mOrderDataMutableLiveData;
  }

}
