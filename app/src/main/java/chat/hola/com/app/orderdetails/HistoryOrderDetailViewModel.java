package chat.hola.com.app.orderdetails;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.kotlintestgradle.UseCaseHandler;
import com.kotlintestgradle.interactor.order.GetOrderDetailsUseCase;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import com.kotlintestgradle.model.orderdetails.OrderData;
import com.kotlintestgradle.model.orderdetails.pharmacy.PackingDetailItem;
import com.kotlintestgradle.model.orderhistory.OrderHistProductData;
import com.kotlintestgradle.model.orderhistory.OrderHistoryAccountingData;
import com.kotlintestgradle.model.orderhistory.PayByData;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.observers.DisposableSingleObserver;

import static chat.hola.com.app.Utilities.Constants.EIGHT;
import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.FIVE;
import static chat.hola.com.app.Utilities.Constants.FOUR;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_ORDER_TYPE;
import static chat.hola.com.app.Utilities.Constants.SIX;
import static chat.hola.com.app.Utilities.Constants.THREE;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static chat.hola.com.app.orderdetails.HistoryOrderDetailUiAction.BACK;
import static chat.hola.com.app.orderdetails.HistoryOrderDetailUiAction.BILLING_PHONE_NUM;
import static chat.hola.com.app.orderdetails.HistoryOrderDetailUiAction.DELIVERY_PHONE_NUM;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;
import static com.kotlintestgradle.data.utils.DataConstants.MASTER_ORDER_TYPE;


/**
 * view model class for history order detail.
 */
public class HistoryOrderDetailViewModel extends ViewModel {

  public ObservableField<String> deliveryAddressName = new ObservableField<>();
  public ObservableField<String> deliveryAddress = new ObservableField<>();
  public ObservableField<SpannableString> deliveryPhoneNum = new ObservableField<>();
  public ObservableField<String> billingAddressName = new ObservableField<>();
  public ObservableField<String> billingAddress = new ObservableField<>();
  public ObservableField<SpannableString> billingPhoneNum = new ObservableField<>();
  public ObservableField<String> cancelOrReorder = new ObservableField<>();
  public ObservableField<Boolean> progressVisible = new ObservableField<>(FALSE);
  public String mOrderId;
  public String currency;
  public MutableLiveData<String> tipValue = new MutableLiveData<>();
  private MutableLiveData<HistoryOrderDetailUiAction> mClick = new MutableLiveData<>();
  private MutableLiveData<ArrayList<OrderData>> mLiveData = new MutableLiveData<>();
  private MutableLiveData<ArrayList<String>> mPrescription = new MutableLiveData<>();
  private MutableLiveData<ArrayList<OrderHistProductData>> mLiveBoxesData = new MutableLiveData<>();
  private MutableLiveData<ArrayList<PackingDetailItem>> packingdetails = new MutableLiveData<>();
  private MutableLiveData<OrderHistoryAccountingData> mPriceDet = new MutableLiveData<>();
  private MutableLiveData<Integer> paymentMethod = new MutableLiveData<>();
  private GetOrderDetailsUseCase mGetOrderDetailUSeCase;
 // private ReorderUseCase mReorderUseCase;
  private UseCaseHandler mHandler;

  /**
   * constructor for history order detail view model.
   *
   * @param getOrderDetailUSeCase get order details use case.
   * @param handler               handler object.
   */
  @Inject
  HistoryOrderDetailViewModel(GetOrderDetailsUseCase getOrderDetailUSeCase,
      /*ReorderUseCase reorderUseCase,*/
      UseCaseHandler handler) {
 //   this.mReorderUseCase = reorderUseCase;
    this.mGetOrderDetailUSeCase = getOrderDetailUSeCase;
    this.mHandler = handler;
  }

  /**
   * call the product details api.
   *
   * @param orderId product id
   */
  public void callGetHistoryApi(String orderId, boolean splitOrder) {
    progressVisible.set(TRUE);
    String orderType = splitOrder ? PRODUCT_ORDER_TYPE : MASTER_ORDER_TYPE;
    DisposableSingleObserver<GetOrderDetailsUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<GetOrderDetailsUseCase.ResponseValues>() {
          @Override
          public void onSuccess(GetOrderDetailsUseCase.ResponseValues responseValues) {
            progressVisible.set(FALSE);
                for (int i =0 ; i<responseValues.getData().getStoreOrders().size();i++){
                  if (responseValues.getData().getPackingDetails()!=null &&
                          responseValues.getData().getPackingDetails().size()>0){
                    for (int j = 0; j<responseValues.getData().getPackingDetails().size();j++){
                      ArrayList<OrderHistProductData> data = new ArrayList<>();

                      for (int k=0;k<responseValues.getData().getPackingDetails().get(j).getProductOrderIds().size();k++){

                      for (int l=0;l<responseValues.getData().getStoreOrders().get(i).getProducts().size();l++){

                        if (responseValues.getData().getPackingDetails().get(j).getProductOrderIds().get(k).equals(
                                responseValues.getData().getStoreOrders().get(i).getProducts().get(l).getProductOrderId()
                        )){

                          data.add(responseValues.getData().getStoreOrders().get(i).getProducts().get(l));
                        }
                      }
                      }

                      responseValues.getData().getPackingDetails().get(j).setProducts(data);
                      Log.d("packingDetails",responseValues.getData().getPackingDetails().get(j).getProducts().size()+"");

                    }

                  }
                }
                if (responseValues.getData().getPackingDetails()!=null && responseValues.getData().getPackingDetails().size()>0){
                  packingdetails.setValue(responseValues.getData().getPackingDetails());
                }
            if (splitOrder) {
              mLiveBoxesData.setValue(responseValues.getData().getProducts());
            } else {
              mLiveData.setValue(responseValues.getData().getStoreOrders());
            }
            if (responseValues.getData().getDeliveryAddress() != null) {
              AddressListItemData addressListItemData =
                  responseValues.getData().getDeliveryAddress();
              deliveryAddressName.set(addressListItemData.getName());
              deliveryAddress.set(String.format("%s%s,%s,%s", addressListItemData.getAddLine1(),
                  addressListItemData.getAddLine2(), addressListItemData.getLocality(),
                  addressListItemData.getPincode()));
              deliveryPhoneNum.set(getSpannableString(String.format("%s:%s %s",
                      "Phone",
                      addressListItemData.getMobileNumberCode(),
                      addressListItemData.getMobileNumber())));
            }
            if (responseValues.getData().getBillingAddress() != null) {
              AddressListItemData addressListItemData =
                  responseValues.getData().getBillingAddress();
              billingAddressName.set(addressListItemData.getName());
              billingAddress.set(String.format("%s%s,%s,%s", addressListItemData.getAddLine1(),
                  addressListItemData.getAddLine2(), addressListItemData.getLocality(),
                  addressListItemData.getPincode()));
              billingPhoneNum.set(getSpannableString(String.format("%s:%s %s",
                  "Phone",
                  addressListItemData.getMobileNumberCode(),
                  addressListItemData.getMobileNumber())));
            }
            mPriceDet.setValue(responseValues.getData().getAccounting());
            if (responseValues.getData().getStatus() != null) {
              int status = Integer.parseInt(responseValues.getData().getStatus().getStatus());
              if (status == ONE || status == TWO) {
                cancelOrReorder.set(
                    "cancel Order");
              }
            }
            int payment = ZERO;
            double walletAmt = ZERO;
            double cardAmt = ZERO;
            double cashAmt = ZERO;
            PayByData payByData = responseValues.getData().getAccounting().getPayBy();
            walletAmt = Float.parseFloat(payByData.getWallet());
            cardAmt = Float.parseFloat(payByData.getCard());
            cashAmt = Float.parseFloat(payByData.getCash());
            if (walletAmt > ZERO && cardAmt > ZERO) {
              payment = ONE;
            } else if (walletAmt > ZERO && cashAmt > ZERO) {
              payment = TWO;
            } else if (walletAmt > ZERO) {
              payment = THREE;
            } else if (cardAmt > ZERO) {
              payment = FOUR;
            } else if (cashAmt > ZERO) {
              payment = FIVE;
            }
            paymentMethod.setValue(payment);

          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
          }
        };
    mHandler.execute(mGetOrderDetailUSeCase,
        new GetOrderDetailsUseCase.RequestValues(orderType, orderId, EIGHT),
        disposableSingleObserver);
  }

  public SpannableString getSpannableString(String phone) {
    SpannableString spannable = new SpannableString(phone);
    spannable.setSpan(new ForegroundColorSpan(Color.BLUE),
            SIX, spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    return spannable;
  }

/*  *//**
   * call the reorder  api
   *
   * @param orderId product id
   *//*
  public void callReorderApi(String orderId) {
    progressVisible.set(TRUE);
    DisposableSingleObserver<ReorderUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<ReorderUseCase.ResponseValues>() {
          @Override
          public void onSuccess(ReorderUseCase.ResponseValues responseValues) {
            progressVisible.set(FALSE);
            mClick.postValue(BACK);
            EcomUtil.printLog("History Succ");
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
            EcomUtil.printLog("History Fail" + e.getMessage());
          }
        };
    mHandler.execute(mReorderUseCase,
        new ReorderUseCase.RequestValues(MASTER_ORDER_TYPE, orderId),
        disposableSingleObserver);
  }*/

  /**
   * listen when cancel order clicked.
   */
  public void cancelOrReorderClicked() {
  }

  /**
   * called when delivery phone click
   */
  public void onDeliveryPhoneNumClick() {
    mClick.postValue(DELIVERY_PHONE_NUM);
  }

  /**
   * called when billing phone click
   */
  public void onBillingPhoneNumClick() {
    mClick.postValue(BILLING_PHONE_NUM);
  }

  /**
   * subscribe to on click
   */
  public MutableLiveData<HistoryOrderDetailUiAction> onClick() {
    return mClick;
  }

  /**
   * subscribe to on history product data
   */
  public MutableLiveData<ArrayList<OrderData>> onHistoryProductsData() {
    return mLiveData;
  }

  public MutableLiveData<ArrayList<PackingDetailItem>> onpackingdetails() {
    return packingdetails;
  }

  /**
   * subscribe to on history product boxes data
   */
  public MutableLiveData<ArrayList<OrderHistProductData>> onHistoryBoxData() {
    return mLiveBoxesData;
  }

  /**
   * notify for price details
   */
  public MutableLiveData<OrderHistoryAccountingData> getPriceDet() {
    return mPriceDet;
  }

  /**
   * notify for prescription details
   */
  public MutableLiveData<ArrayList<String>> getPrescriptionPreview() {
    return mPrescription;
  }

  /**
   * notify for payment method
   */
 public MutableLiveData<Integer> getPaymentMethod() {
    return paymentMethod;
  }


}
