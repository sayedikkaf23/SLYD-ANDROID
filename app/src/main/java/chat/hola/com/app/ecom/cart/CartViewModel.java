package chat.hola.com.app.ecom.cart;

import static chat.hola.com.app.Utilities.Constants.DEFAULT_LAT_LANG;
import static chat.hola.com.app.Utilities.Constants.DELIVERY_TYPE;
import static chat.hola.com.app.Utilities.Constants.DOLLAR_SIGN;
import static chat.hola.com.app.Utilities.Constants.ERROR;
import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.MULTI_STORE;
import static chat.hola.com.app.Utilities.Constants.RETAILER;
import static chat.hola.com.app.Utilities.Constants.SIX;
import static chat.hola.com.app.Utilities.Constants.STORE_TYPE;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.VALUE_ZERO;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static chat.hola.com.app.ecom.cart.CartUiAction.BILLING_ADDRESS;
import static chat.hola.com.app.ecom.cart.CartUiAction.CHANGE_ADDRESS;
import static chat.hola.com.app.ecom.cart.CartUiAction.CHANGE_PAYMENT;
import static chat.hola.com.app.ecom.cart.CartUiAction.CLEAR;
import static chat.hola.com.app.ecom.cart.CartUiAction.CONTACT_LESS_DELIVERY;
import static chat.hola.com.app.ecom.cart.CartUiAction.CONTINUE_SHOPPING;
import static chat.hola.com.app.ecom.cart.CartUiAction.DELIVERY_SCHEDULE_CHANGE;
import static chat.hola.com.app.ecom.cart.CartUiAction.DISABLE_BACK;
import static chat.hola.com.app.ecom.cart.CartUiAction.EMPTY_CART;
import static chat.hola.com.app.ecom.cart.CartUiAction.ENABLE_BACK;
import static chat.hola.com.app.ecom.cart.CartUiAction.EXTRA_NOTE;
import static chat.hola.com.app.ecom.cart.CartUiAction.LOGIN;
import static chat.hola.com.app.ecom.cart.CartUiAction.LOGIN_RESULT;
import static chat.hola.com.app.ecom.cart.CartUiAction.MANAGE_ADDRESS;
import static chat.hola.com.app.ecom.cart.CartUiAction.ON_SUCCESS;
import static chat.hola.com.app.ecom.cart.CartUiAction.PRESCRIPTION_UPLOAD;
import static chat.hola.com.app.ecom.cart.CartUiAction.PROMO_CODE;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.kotlintestgradle.data.utils.DataConstants.EMPTY_STRING;

import android.view.View;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.BackButtonClickListener;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.ecom.addresslist.PrescriptionModel;
import chat.hola.com.app.manager.session.SessionManager;
import com.ezcall.android.R;
import com.jio.consumer.domain.interactor.user.handler.UserHandler;
import com.kotlintestgradle.UseCaseHandler;
import com.kotlintestgradle.interactor.ecom.AddressHandler;
import com.kotlintestgradle.interactor.ecom.ApplyPromoCodesUseCase;
import com.kotlintestgradle.interactor.ecom.GetCartProductsUseCase;
import com.kotlintestgradle.interactor.ecom.IpAddressToLocationUseCase;
import com.kotlintestgradle.interactor.ecom.PlaceOrderUseCase;
import com.kotlintestgradle.interactor.ecom.cart.UpdateCartUseCase;
import com.kotlintestgradle.model.ecom.getcart.CartData;
import com.kotlintestgradle.model.ecom.promocode.ApplyPromoCodeData;
import com.kotlintestgradle.model.ecom.promocode.ProductPromoInput;
import com.kotlintestgradle.model.ecom.slots.DeliverySlot;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;

/*holds the logic of the cart page*/
public class CartViewModel extends ViewModel implements BackButtonClickListener {
  public ObservableField<Boolean> progressVisible = new ObservableField<>(FALSE);
  public ObservableField<Boolean> changeVisible = new ObservableField<>(FALSE);
  public ObservableField<Boolean> billingAddVisible = new ObservableField<>(FALSE);
  public ObservableField<Boolean> addAddressVisible = new ObservableField<>(FALSE);
  public ObservableField<Boolean> getCartprogress = new ObservableField<>(FALSE);
  public ObservableField<Boolean> cartAmtGroup = new ObservableField<>(TRUE);
  public ObservableField<Boolean> paymentGroup = new ObservableField<>(FALSE);
  public ObservableField<Boolean> userNameVisibility = new ObservableField<>();
  public ObservableField<String> userName = new ObservableField<>();
  public ObservableField<String> userAddress = new ObservableField<>();
  public ObservableField<String> billingUserName = new ObservableField<>();
  public ObservableField<String> promoCodeError = new ObservableField<>();
  public ObservableField<String> billingUserAddress = new ObservableField<>();
  public String mCartId;
  String addressId;
  String billingAddressId;
  String cardId;
  String requestedTimePickup = "", cityId = "", countryId = "", cityName = "", countryName = "";
  String requestedTime = "";
  String currencySymbol = DOLLAR_SIGN;
  MutableLiveData<String> tipAmount = new MutableLiveData<>(EMPTY_STRING);
  int whichDay, whichSlot;
  boolean isDeliverNow;
  ArrayList<DeliverySlot> pickupSlotId = new ArrayList<>();
  ArrayList<DeliverySlot> deliverySlotId = new ArrayList<>();
  boolean contactLessDelivery = false;
  String contactLessDeliveryReason = EMPTY_STRING;
  String tip = VALUE_ZERO;
  String extraNote = EMPTY_STRING;
  boolean mIsDeliveryScheduled = false;
  String mDeliveryDateAndTime = "";
  String coupon = "";
  String couponId = "";
  int paymentType;
  boolean payByWallet, isPayByWallet;
  boolean mConfirmOrder;
  @Inject
  AddressHandler mAddressHandler;
  private double mDiscount = 0.0;
  private UseCaseHandler mHandler;
  private GetCartProductsUseCase mGetCartProductsUseCase;
  private IpAddressToLocationUseCase mIpAddressToLocationUseCase;
  private MutableLiveData<String> mValidateOnErrorLiveData = new MutableLiveData<>();
  private MutableLiveData<CartData> mCartDataMutableLiveData = new MutableLiveData<>();
  private MutableLiveData<Boolean> mLocation = new MutableLiveData<>();
  private MutableLiveData<String> mAddressData = new MutableLiveData<>();
  private MutableLiveData<ArrayList<String>> mTipsData = new MutableLiveData<>();
  MutableLiveData<CartUiAction> mCartUiAction = new MutableLiveData<>();
  private MutableLiveData<ApplyPromoCodeData> mApplyPromoCodeData = new MutableLiveData<>();
  private UpdateCartUseCase mUpdateCartUseCase;
  private PlaceOrderUseCase mPlaceOrderUseCase;
   private ApplyPromoCodesUseCase mApplyPromoCodesUseCase;
  String mIpAddress = "";
  String storeCategoryId = EMPTY_STRING;
  @Inject
  SessionManager mSessionManager;
  @Inject
  UserHandler mUserInfoHandler;

  @Inject
  public CartViewModel(UseCaseHandler handler, GetCartProductsUseCase getCartProductsUseCase,
       IpAddressToLocationUseCase ipAddressToLocationUseCase, UpdateCartUseCase updateCartUseCase,
      ApplyPromoCodesUseCase applyPromoCodesUseCase,
      PlaceOrderUseCase placeOrderUseCase) {
    this.mHandler = handler;
    this.mGetCartProductsUseCase = getCartProductsUseCase;
    this.mIpAddressToLocationUseCase = ipAddressToLocationUseCase;
    this.mUpdateCartUseCase = updateCartUseCase;
    this.mPlaceOrderUseCase = placeOrderUseCase;
    this.mApplyPromoCodesUseCase = applyPromoCodesUseCase;
    promoCodeError.set(
        AppController.getInstance().getResources().getString(R.string.selectPromoCode));
  }

  /**
   * This method is using to get all product in cart from API
   */
  void getCartProducts() {
    getCartprogress.set(TRUE);
    DisposableSingleObserver<GetCartProductsUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<GetCartProductsUseCase.ResponseValues>() {
          @Override
          public void onSuccess(GetCartProductsUseCase.ResponseValues responseValues) {
            Utilities.printLog("GetCart Success" + responseValues.getData().getSubTotal());
            getCartprogress.set(FALSE);
            progressVisible.set(FALSE);
            if (responseValues.getData() != null) {
              mCartId = responseValues.getData().getId();
              if (responseValues.getData().getTotalDiscount() != null) {
                mDiscount = Double.parseDouble(responseValues.getData().getTotalDiscount());
              }
              if (responseValues.getData().getStoreCategoryId() != null) {
                storeCategoryId = responseValues.getData().getStoreCategoryId();
              }
            }
            mCartDataMutableLiveData.setValue(responseValues.getData());
            mLocation.setValue(TRUE);
          }

          @Override
          public void onError(Throwable e) {
            mCartUiAction.postValue(EMPTY_CART);
            Utilities.printLog("GetCart Fail" + e.toString());
            mLocation.setValue(TRUE);
            getCartprogress.set(FALSE);
            progressVisible.set(FALSE);
          }
        };
    mHandler.execute(
        mGetCartProductsUseCase,
        new GetCartProductsUseCase.RequestValues(false,AppController.getInstance().getUserId(),
            mSessionManager.getCurrencySymbol(),mSessionManager.getCurrency()),
        disposableSingleObserver);
  }

  /**
   * This method is used to call the apply promo  from API
   */
  void applyPromoCodeApi(String currencySymbol, float totalPurchaseValue,
      float deliveryFee, String currencyName,
      ArrayList<ProductPromoInput> productPromoInputArrayList) {
    progressVisible.set(TRUE);
    DisposableSingleObserver<ApplyPromoCodesUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<ApplyPromoCodesUseCase.ResponseValues>() {
          @Override
          public void onSuccess(ApplyPromoCodesUseCase.ResponseValues responseValues) {
            progressVisible.set(FALSE);
            if (responseValues.getData() != null) {
              mApplyPromoCodeData.postValue(responseValues.getData());
            }
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
            try {
              JSONObject jsonObject = new JSONObject(e.getMessage());
              if (jsonObject.has(ERROR)) {
                coupon = "";
                promoCodeError.set(jsonObject.getString(ERROR));
              }
            } catch (JSONException ex) {
              ex.printStackTrace();
            }
          }
        };
    mHandler.execute(
        mApplyPromoCodesUseCase,
        new ApplyPromoCodesUseCase.RequestValues(cityId, countryId, countryName, cityName, paymentType, coupon, couponId,
            currencySymbol, totalPurchaseValue, mCartId, deliveryFee, isPayByWallet, currencyName, productPromoInputArrayList,
            mSessionManager.getEmail()),
        disposableSingleObserver);
  }

  /** This method is using for subscribing to User data */
  void onUserDataUpdate() {
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    compositeDisposable.add(
        mUserInfoHandler
            .getUserDataObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(userData -> {
              if (userData.getName() == null) {
                userName.set("");
              }
              addAddressVisible.set(userData.getName() == null ? TRUE : FALSE);
              changeVisible.set(userData.getName() == null ? FALSE : TRUE);
              Utilities.printLog("userName" + userData.getName());
            }));
  }

  /** This method is using for subscribing to User cart data */
  void onCartUpdate() {
    Utilities.printLog("onCartUpdate");
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    compositeDisposable.add(
        mUserInfoHandler
            .getCartDataObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(cartData -> {
              Utilities.printLog("onCartUpdate" + cartData.getAction());
              if (cartData.getAction() == ZERO) {
                mCartUiAction.postValue(EMPTY_CART);
              } else if (cartData.getAction() != SIX) {
                getCartProducts();
              }
            }));
  }

  /**
   * This method is using to get all product in cart from API
   */
  void getAddress(String ipAddress) {
    Utilities.printLog("getAddress " + ipAddress);
    DisposableSingleObserver<IpAddressToLocationUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<IpAddressToLocationUseCase.ResponseValues>() {
          @Override
          public void onSuccess(IpAddressToLocationUseCase.ResponseValues responseValues) {
            Utilities.printLog("getAddress Success");
            mAddressData.postValue(String.format("%s %s", responseValues.getData().getPostal(),
                responseValues.getData().getRegion()));
          }

          @Override
          public void onError(Throwable e) {
            Utilities.printLog("getAddress Fail" + e.getMessage());
          }
        };
    mHandler.execute(
        mIpAddressToLocationUseCase,
        new IpAddressToLocationUseCase.RequestValues(ipAddress,mSessionManager.getCurrencySymbol(),mSessionManager.getCurrency()),
        disposableSingleObserver);
  }

  /**
   * listens add address clicked.
   */
  public void addAddress() {
    mCartUiAction.postValue(CHANGE_ADDRESS);
  }

  /**
   * listens add address clicked.
   */
  public void billingAddress() {
    mCartUiAction.postValue(BILLING_ADDRESS);
  }

  /**
   * listens add address clicked.
   */
  public void changePaymentMethod() {
    mCartUiAction.postValue(CHANGE_PAYMENT);
  }

  /**
   * listens clear promo code clicked.
   */
  public void clearPromoCode() {
    mCartUiAction.postValue(CLEAR);
  }

  /**
   * listens clear promo code clicked.
   */
  public void promoCode() {
    mCartUiAction.postValue(PROMO_CODE);
  }

  /**
   * listens change address clicked.
   */
  public void changeAddress() {
    mCartUiAction.postValue(AppController.getInstance().isGuest() ? LOGIN_RESULT : CHANGE_ADDRESS);
  }

  /**
   * listens change address clicked.
   */
  public void placeOrder() {
    if (!AppController.getInstance().isGuest()) {
      if (mConfirmOrder) {
        mCartUiAction.setValue(DISABLE_BACK);
        callPlaceOrderApi(mCartId, addressId, billingAddressId, mDiscount, cardId, paymentType,
            payByWallet);
      } else {
        mCartUiAction.postValue(MANAGE_ADDRESS);
      }
    } else {
      mCartUiAction.postValue(LOGIN);
    }
  }

  /**
   * listens add address clicked.
   */
  public void continueShopping() {
    mCartUiAction.postValue(CONTINUE_SHOPPING);
  }

  /**
   * listens prescription upload clicked.
   */
  public void prescriptionUpload() {
    mCartUiAction.postValue(PRESCRIPTION_UPLOAD);
  }

  /**
   * listens delivery schedule clicked.
   */
  public void deliveryScheduleChange() {
    mCartUiAction.postValue(DELIVERY_SCHEDULE_CHANGE);
  }

  /**
   * listens contactless delivery clicked.
   */
  public void contactlessDeliveryChange() {
    mCartUiAction.postValue(CONTACT_LESS_DELIVERY);
  }

  /**
   * listens contactless delivery clicked.
   */
  public void extraNoteChange() {
    mCartUiAction.postValue(EXTRA_NOTE);
  }

  /**
   * <p>calls update cart API</p>
   */
  void callUpdateCartApi(int action, int newQuantity, String parentProductId, String productId,
      String unitId, String storeId) {
    progressVisible.set(TRUE);
    DisposableSingleObserver<UpdateCartUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<UpdateCartUseCase.ResponseValues>() {
          @Override
          public void onSuccess(UpdateCartUseCase.ResponseValues responseValues) {
            Utilities.printLog(
                "UpdateCartCart Succ" + responseValues.getData().getMessage() + "newQuantity"
                    + newQuantity);
            //  getCartProducts();
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
            mValidateOnErrorLiveData.setValue(e.getMessage());
            Utilities.printLog("UpdateCartCart Fail" + e.getMessage());
          }
        };
    mHandler.execute(mUpdateCartUseCase,
        new UpdateCartUseCase.RequestValues(RETAILER, parentProductId, productId, unitId,
            storeId, MULTI_STORE,
            "", "", "", newQuantity, ONE, action, MULTI_STORE, mIpAddress, DEFAULT_LAT_LANG,
            DEFAULT_LAT_LANG,mSessionManager.getCurrencySymbol(),mSessionManager.getCurrency()),
        disposableSingleObserver);
  }

  /**
   * <p>calls place order API</p>
   */
  private void callPlaceOrderApi(String cartId, String addressId, String billingAddressId,
      double discount, String cardId,
      int paymentType, boolean payByWallet) {
    Utilities.printLog("exe" + "addressId  " + addressId + "billingAddressId  " + billingAddressId);
    progressVisible.set(TRUE);
    DisposableSingleObserver<PlaceOrderUseCase.ResponseValues> disposableSingleObserver =
            new DisposableSingleObserver<PlaceOrderUseCase.ResponseValues>() {
              @Override
              public void onSuccess(PlaceOrderUseCase.ResponseValues responseValues) {
                progressVisible.set(FALSE);
                mCartUiAction.postValue(ON_SUCCESS);
              }

              @Override
              public void onError(Throwable e) {
                progressVisible.set(FALSE);
                mCartUiAction.setValue(ENABLE_BACK);
                mValidateOnErrorLiveData.setValue(e.getMessage());
                Utilities.printLog("UpdateCartCart Fail" + e.getMessage());
              }
            };
              mHandler.execute(mPlaceOrderUseCase,
                      new PlaceOrderUseCase.RequestValues(cartId, addressId, billingAddressId, paymentType,
                              cardId, payByWallet,
                              coupon, couponId, discount, DEFAULT_LAT_LANG, DEFAULT_LAT_LANG, mIpAddress, "", STORE_TYPE,
                              DELIVERY_TYPE,mSessionManager.getUserId(),mSessionManager.getCurrencySymbol(),
                          mSessionManager.getCurrency()),
                      disposableSingleObserver);
  }

  /**
   * This method is using to get the cart Mutable mData
   *
   * @return cart mutable mData
   */
  MutableLiveData<CartData> getCartDataMutableLiveData() {
    return mCartDataMutableLiveData;
  }

  /**
   * This method is using to get tip value
   *
   * @return MutableLiveData<ArrayList<String>>
   */
  MutableLiveData<ArrayList<String>> getTipLiveData() {
    return mTipsData;
  }

  /**
   * This method is using to get the cart Mutable mData
   *
   * @return cart mutable mData
   */
  MutableLiveData<ApplyPromoCodeData> getPromoDataMutableLiveData() {
    return mApplyPromoCodeData;
  }

  /**
   * This method is using to get the location data
   */
  MutableLiveData<Boolean> getLocationLiveData() {
    return mLocation;
  }

  MutableLiveData<String> getAddressData() {
    return mAddressData;
  }

  MutableLiveData<CartUiAction> uiAction() {
    return mCartUiAction;
  }

  /*
   * notify when onError comes
   */
  public MutableLiveData<String> onError() {
    return mValidateOnErrorLiveData;
  }

  @Override
  public void backButtonClickListener(View view) {
    mCartUiAction.postValue(CartUiAction.CROSS_CLICK);
  }
}