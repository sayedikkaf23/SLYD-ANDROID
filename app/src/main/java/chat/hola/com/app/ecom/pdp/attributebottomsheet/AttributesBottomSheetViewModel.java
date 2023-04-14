package chat.hola.com.app.ecom.pdp.attributebottomsheet;

import static chat.hola.com.app.Utilities.Constants.ADD_CART;
import static chat.hola.com.app.Utilities.Constants.DEFAULT_LAT_LANG;
import static chat.hola.com.app.Utilities.Constants.DELETE_CART;
import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.MULTI_STORE;
import static chat.hola.com.app.Utilities.Constants.RETAILER;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.UPDATE_CART;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static chat.hola.com.app.ecom.pdp.attributebottomsheet.PdpBottomSheetUiAction.CROSS_ICON;
import static chat.hola.com.app.ecom.pdp.attributebottomsheet.PdpBottomSheetUiAction.GOTO_CART;
import static chat.hola.com.app.ecom.pdp.attributebottomsheet.PdpBottomSheetUiAction.GOTO_CART_STATUS;
import static chat.hola.com.app.ecom.pdp.attributebottomsheet.PdpBottomSheetUiAction.GOTO_LOGIN;
import static chat.hola.com.app.ecom.pdp.attributebottomsheet.PdpBottomSheetUiAction.NOTIFY_ME;
import static chat.hola.com.app.ecom.pdp.attributebottomsheet.PdpBottomSheetUiAction.QUANTITY;
import static com.appscrip.myapplication.utility.Constants.ONE;

import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.session.SessionManager;
import com.ezcall.android.R;
import com.kotlintestgradle.UseCaseHandler;
import com.kotlintestgradle.interactor.ecom.CartHandler;
import com.kotlintestgradle.interactor.ecom.cart.AddProductToCartUseCase;
import com.kotlintestgradle.interactor.ecom.cart.NotifyProductAvailabilityUseCase;
import com.kotlintestgradle.interactor.ecom.cart.PdpUseCase;
import com.kotlintestgradle.interactor.ecom.cart.UpdateCartUseCase;
import com.kotlintestgradle.model.ecom.common.FinalPriceListData;
import com.kotlintestgradle.model.ecom.pdp.PdpOfferData;
import com.kotlintestgradle.model.ecom.pdp.ProductModel;
import com.kotlintestgradle.model.ecom.pdp.VariantsData;
import io.reactivex.observers.DisposableSingleObserver;
import java.util.ArrayList;
import java.util.Objects;
import javax.inject.Inject;

/**
 * view mData class for the pdp bottom sheet.
 */
public class AttributesBottomSheetViewModel extends ViewModel {
  public ObservableField<String> productPrice = new ObservableField<>();
  public ObservableField<Boolean> productAddToCart = new ObservableField<>(FALSE);
  public ObservableField<String> addToCartTxt = new ObservableField<>();
  public ObservableField<String> productActualPrice = new ObservableField<>();
  public ObservableField<Boolean> progressVisible = new ObservableField<>(FALSE);
  public ObservableField<String> productOfferPer = new ObservableField<>();
  public ObservableField<Boolean> offerViews = new ObservableField<>(FALSE);
  public ObservableField<String> productName = new ObservableField<>();
  public ObservableField<String> imageUrl = new ObservableField<>();
  public ObservableField<String> currentQuantity = new ObservableField<>();
  public ObservableField<Boolean> productCartOptions = new ObservableField<>(TRUE);
  public ObservableField<Boolean> productOutOfStock = new ObservableField<>(FALSE);
  @Inject
  CartHandler mCartHandler;
  private UseCaseHandler mHandler;
  private MutableLiveData<PdpBottomSheetUiAction> mUiAction = new MutableLiveData<>();
  private MutableLiveData<String> mValidateOnErrorLiveData = new MutableLiveData<>();
  private MutableLiveData<ArrayList<VariantsData>> mVariantsData =
      new MutableLiveData<>();
  private MutableLiveData<Boolean> mCartCountData = new MutableLiveData<>();
  private MutableLiveData<PdpUseCase.ResponseValues> mPdpResponseValues =
      new MutableLiveData<>();
  private PdpUseCase mPdpUseCase;
  private AddProductToCartUseCase mAddProductToCartUseCase;
  private NotifyProductAvailabilityUseCase mNotifyProductAvailabilityUseCase;
  private UpdateCartUseCase mUpdateCartUseCase;
  String mProductId = "", mParentProductId = "";
  private String mUnitId, mStoreId;
  private String mDeviceId = "";
  private String mDeviceIpAddress = "";
  private PdpOfferData mOffersListData;
  private int mAvailableCount;
  private boolean mIsOutOfStock;
  private int mCartCount;
  PdpOfferData offer;
  private SessionManager sessionManager;

  /**
   * constructor for this mData
   *
   * @param pdpUseCase pdp use case
   * @param handler    nodeApiHandler for this attribute bottom sheet.
   */
  @Inject
  public AttributesBottomSheetViewModel(AddProductToCartUseCase addProductToCartUseCase,
      UpdateCartUseCase updateCartUseCase,
      NotifyProductAvailabilityUseCase notifyProductAvailabilityUseCase,
      PdpUseCase pdpUseCase, UseCaseHandler handler, SessionManager sessionManager) {
    this.mPdpUseCase = pdpUseCase;
    this.mAddProductToCartUseCase = addProductToCartUseCase;
    this.mNotifyProductAvailabilityUseCase = notifyProductAvailabilityUseCase;
    this.mUpdateCartUseCase = updateCartUseCase;
    this.mHandler = handler;
    this.sessionManager = sessionManager;
  }

  /**
   * for getting device details
   *
   * @param deviceId        [Id of device]
   * @param deviceIpAddress [IP of device]
   */
  void getDeviceDetails(String deviceId, String deviceIpAddress) {
    this.mDeviceId = deviceId;
    this.mDeviceIpAddress = deviceIpAddress;
  }

  /**
   * for getting the input data
   *
   * @param storeId store id
   * @param unitId  unit id
   */
  void getInputData(String storeId, String unitId, String productId, String parentProductId,
      int availableCount, boolean isOutOfStock, int cartCount,
      PdpOfferData offersListData) {
    this.mStoreId = storeId;
    this.mUnitId = unitId;
    this.mProductId = productId;
    this.mParentProductId = parentProductId;
    this.mOffersListData = offersListData;
    this.mAvailableCount = availableCount;
    this.mIsOutOfStock = isOutOfStock;
    this.mCartCount = cartCount;
    showCartStatus();
  }

  /**
   * call the product details api
   *
   * @param productId       product id
   * @param parentProductId parent product id
   */
  void callProductDetailsApi(String productId, String parentProductId) {
    progressVisible.set(TRUE);
    this.mProductId = productId;
    this.mParentProductId = parentProductId;
    DisposableSingleObserver<PdpUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<PdpUseCase.ResponseValues>() {
          @Override
          public void onSuccess(PdpUseCase.ResponseValues responseValues) {
            Utilities.printLog("Pdp succ");
            progressVisible.set(FALSE);
            ProductModel productModel = responseValues.getData().getProductData().getData().get(
                ZERO);
            if (productModel != null) {
              mUnitId = productModel.getUnitId();
              mStoreId = productModel.getSupplier().getId();
              productOutOfStock.set(productModel.isOutOfStock());
              productCartOptions.set(!productModel.isOutOfStock());
              offer = productModel.getOffers();
              if (productModel.getProductName() != null) {
                productName.set(productModel.getProductName());
              }
              FinalPriceListData finalPriceListData = productModel.getFinalPriceList();
              if (finalPriceListData.getFinalPrice() != null) {
                productPrice.set(
                    String.format("%s %s", productModel.getCurrencySymbol(),
                        finalPriceListData.getFinalPrice()));
              }
              if (finalPriceListData.getBasePrice() != null) {
                productActualPrice.set(String.format("%s %s", productModel.getCurrencySymbol(),
                    finalPriceListData.getBasePrice()));
              }
              if (finalPriceListData.getDiscountPercentage() != null && Integer.parseInt(
                  finalPriceListData.getDiscountPercentage()) != ZERO) {
                productOfferPer.set(
                    String.format("(%s %s%% %s)",
                        AppController.getInstance().getString(R.string.pdpOnUpto),
                        finalPriceListData.getDiscountPercentage(),
                        AppController.getInstance().getString(R.string.pdpOff)));
                offerViews.set(TRUE);
              } else {
                offerViews.set((finalPriceListData.getDiscountPrice() != null
                    && !finalPriceListData.getDiscountPrice().isEmpty()
                    && !finalPriceListData.getDiscountPrice().equals("" + ZERO)));
                if (finalPriceListData.getDiscountPrice() != null
                    && !finalPriceListData.getDiscountPrice().isEmpty()
                    && !finalPriceListData.getDiscountPrice().equals("" + ZERO)) {
                  productOfferPer.set(
                      String.format("(%s %s %s)",
                          AppController.getInstance().getString(R.string.pdpFlat),
                          finalPriceListData.getDiscountPrice(),
                          AppController.getInstance().getString(R.string.pdpOff)));
                }
              }
              if (productModel.getVariantsData() != null
                  && productModel.getVariantsData().size() > ZERO) {
                mVariantsData.setValue(productModel.getVariantsData());
              }
              showCartStatus();
            }
            mPdpResponseValues.setValue(responseValues);
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
            mValidateOnErrorLiveData.postValue(e.getMessage());
            Utilities.printLog("Pdp Fail" + e.getMessage());
          }
        };
    mHandler.execute(mPdpUseCase,
        new PdpUseCase.RequestValues(productId, parentProductId, sessionManager.getLatitude(),
            sessionManager.getLongitude(), sessionManager.getIpAdress(), sessionManager.getCity(),
            sessionManager.getCountry()),
        disposableSingleObserver);
  }

  /**
   * actions for add to add to cart click
   */
  public void addToCart() {
    if (!AppController.getInstance().isGuest()) {
      if (addToCartTxt.get() != null && addToCartTxt.get().equals(
          AppController.getInstance().getString(R.string.pdpAddToCart))) {
        callUpdateCartApi(ADD_CART, ADD_CART);
      } else {
        mUiAction.postValue(GOTO_CART);
      }
    } else {
      mUiAction.postValue(GOTO_LOGIN);
    }
  }

  /**
   * actions for add to cart click
   */
  public void addItemToCart() {
    new Thread(() -> {
      Utilities.printLog(
          "mAvailableCount" + mAvailableCount + "mCartHandler.getQuantity(mProductId)"
              + mCartHandler.getQuantity(mProductId));
      if (mAvailableCount != Integer.parseInt(Objects.requireNonNull(currentQuantity.get()))) {
        if (mCartHandler.isProductExistInCart(mProductId)) {
          int newQuantity = Integer.parseInt(Objects.requireNonNull(currentQuantity.get())) + ONE;
          callUpdateCartApi(UPDATE_CART, newQuantity);
        }
      } else {
        mValidateOnErrorLiveData.postValue(
            AppController.getInstance().getString(R.string.pdpAddToCartWarning));
      }
    }).start();
  }

  /**
   * actions for add to cart click
   */
  public void removeItemFromCart() {
    new Thread(() -> {
      if (Integer.parseInt(Objects.requireNonNull(currentQuantity.get())) != ONE) {
        int newQuantity = Integer.parseInt(Objects.requireNonNull(currentQuantity.get())) - ONE;
        callUpdateCartApi(UPDATE_CART, newQuantity);
      } else {
        callUpdateCartApi(DELETE_CART, ZERO);
      }
    }).start();
  }

  /**
   * <p>calls update cart API</p>
   */
  private void callUpdateCartApi(int action, int newQuantity) {
    progressVisible.set(TRUE);
    DisposableSingleObserver<UpdateCartUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<UpdateCartUseCase.ResponseValues>() {
          @Override
          public void onSuccess(UpdateCartUseCase.ResponseValues responseValues) {
            Utilities.printLog("UpdateCartCart Succ" + responseValues.getData().getMessage());
            progressVisible.set(FALSE);
            if (action == ADD_CART) {
              mCartCountData.setValue(TRUE);
            }
            addToCartTxt.set(
                (action == ADD_CART || action == UPDATE_CART)
                    ? AppController.getInstance().getString(
                    R.string.pdpAddGoCart)
                    : AppController.getInstance().getString(R.string.pdpAddToCart));
            productAddToCart.set((action == ADD_CART || action == UPDATE_CART) ? TRUE : FALSE);
            if (action == ADD_CART || action == UPDATE_CART) {
              currentQuantity.set(String.valueOf(newQuantity));
            } else {
              mUiAction.setValue(GOTO_CART_STATUS);
              mCartCountData.setValue(TRUE);
              productCartOptions.set(TRUE);
            }
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
            mValidateOnErrorLiveData.postValue(e.getMessage());
            Utilities.printLog("UpdateCartCart Fail" + e.getMessage());
          }
        };
    mHandler.execute(mUpdateCartUseCase,
        new UpdateCartUseCase.RequestValues(RETAILER, mParentProductId, mProductId, mUnitId,
            mStoreId, MULTI_STORE,
            "", "", "", newQuantity, ONE, action, MULTI_STORE, mDeviceIpAddress, DEFAULT_LAT_LANG,
            DEFAULT_LAT_LANG, offer,sessionManager.getCurrencySymbol(),sessionManager.getCurrency()),
        disposableSingleObserver);
  }

  /**
   * call the notify  product  api
   */
  void callNotifyProductApi(String email, String parentProductId) {
    progressVisible.set(TRUE);
    DisposableSingleObserver<NotifyProductAvailabilityUseCase.ResponseValues>
        disposableSingleObserver =
        new DisposableSingleObserver<NotifyProductAvailabilityUseCase.ResponseValues>() {
          @Override
          public void onSuccess(NotifyProductAvailabilityUseCase.ResponseValues responseValues) {
            progressVisible.set(FALSE);
            Utilities.printLog("Notify Succ");
            mValidateOnErrorLiveData.postValue(
                responseValues.getData().getMessage());
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
            mValidateOnErrorLiveData.postValue(e.getMessage());
            Utilities.printLog("Notify Fail" + e.getMessage());
          }
        };
    mHandler.execute(mNotifyProductAvailabilityUseCase,
        new NotifyProductAvailabilityUseCase.RequestValues(mProductId, email, parentProductId),
        disposableSingleObserver);
  }

  /**
   * this method will show the cart button status for this bottom sheet
   */
  private void showCartStatus() {
    if (mIsOutOfStock) {
      productOutOfStock.set(mIsOutOfStock);
      productCartOptions.set(!mIsOutOfStock);
      return;
    }
    new Thread(() -> {
      addToCartTxt.set((mCartHandler.isProductExistInCart(mProductId))
          ? AppController.getInstance().getString(R.string.pdpAddGoCart)
          : AppController.getInstance().getString(R.string.pdpAddToCart));
      productAddToCart.set((addToCartTxt.get() != null && Objects.requireNonNull(
          addToCartTxt.get()).equals(
          AppController.getInstance().getString(R.string.pdpAddGoCart))) ? TRUE : FALSE);
      if (mCartHandler.isProductExistInCart(mProductId)) {
        currentQuantity.set((mCartHandler.getQuantity(mProductId) != ZERO) ? String.valueOf(
            mCartHandler.getQuantity(mProductId)) : String.valueOf(ONE));
      } else {
        currentQuantity.set(String.valueOf(ZERO));
      }
      Utilities.printLog("getQuantity" + mCartHandler.getQuantity(mProductId) + "currentQuantity"
          + currentQuantity.get() + "exist" + mCartHandler.isProductExistInCart(mProductId));
    }).start();
  }

  /**
   * notify when cross icon clicked.
   */
  public void onCrossClicked() {
    mUiAction.postValue(CROSS_ICON);
  }

  /**
   * notify when quantity icon clicked.
   */
  public void onQuantityClicked() {
    mUiAction.postValue(QUANTITY);
  }

  /**
   * listen when out of stock clicked
   */
  public void outOfStockClicked() {
    mUiAction.postValue(NOTIFY_ME);
  }

  /*
   * notify when clicking action happened
   */
  MutableLiveData<PdpBottomSheetUiAction> onClick() {
    return mUiAction;
  }

  /*
   * notify activity when variant mData came
   */
  MutableLiveData<ArrayList<VariantsData>> onGetVariants() {
    return mVariantsData;
  }

  /*
   * notify activity when variant mData came
   */
  MutableLiveData<PdpUseCase.ResponseValues> onGetResponseValues() {
    return mPdpResponseValues;
  }

  /*
   * notify when onError comes
   */
  public MutableLiveData<String> onError() {
    return mValidateOnErrorLiveData;
  }

  /*
   * notify when cart count comes
   */
  MutableLiveData<Boolean> onCartCount() {
    return mCartCountData;
  }
}