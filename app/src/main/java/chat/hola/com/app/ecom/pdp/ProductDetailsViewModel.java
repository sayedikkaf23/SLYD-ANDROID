package chat.hola.com.app.ecom.pdp;

import static chat.hola.com.app.Utilities.Constants.ADD_CART;
import static chat.hola.com.app.Utilities.Constants.ALL_DETAILS_CLICKED;
import static chat.hola.com.app.Utilities.Constants.ALL_OFFERS;
import static chat.hola.com.app.Utilities.Constants.DEFAULT_LAT_LANG;
import static chat.hola.com.app.Utilities.Constants.DELETE_CART;
import static chat.hola.com.app.Utilities.Constants.ECOM_FLOW;
import static chat.hola.com.app.Utilities.Constants.ERROR_MSG;
import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.FIVE;
import static chat.hola.com.app.Utilities.Constants.FOUR;
import static chat.hola.com.app.Utilities.Constants.GET_ATTRIBUTES;
import static chat.hola.com.app.Utilities.Constants.GET_ATTRIBUTES_RATING;
import static chat.hola.com.app.Utilities.Constants.GET_DESCRIPTION;
import static chat.hola.com.app.Utilities.Constants.GET_HILIGHTS;
import static chat.hola.com.app.Utilities.Constants.GET_OFFER_DATA;
import static chat.hola.com.app.Utilities.Constants.GET_PRESCRIPTION_REQUIRED;
import static chat.hola.com.app.Utilities.Constants.GET_RATINGS;
import static chat.hola.com.app.Utilities.Constants.GET_SELLERS;
import static chat.hola.com.app.Utilities.Constants.GET_VARIENT_DATA;
import static chat.hola.com.app.Utilities.Constants.INSERT;
import static chat.hola.com.app.Utilities.Constants.LIKE_DISLIKE;
import static chat.hola.com.app.Utilities.Constants.LINK;
import static chat.hola.com.app.Utilities.Constants.MULTI_STORE;
import static chat.hola.com.app.Utilities.Constants.OUT_OF_STOCK_CLICKED;
import static chat.hola.com.app.Utilities.Constants.RATING_CLICKED;
import static chat.hola.com.app.Utilities.Constants.RETAILER;
import static chat.hola.com.app.Utilities.Constants.REVIEW;
import static chat.hola.com.app.Utilities.Constants.REVIEW_CLICKED;
import static chat.hola.com.app.Utilities.Constants.REVIEW_DATA;
import static chat.hola.com.app.Utilities.Constants.REVIEW_IMAGES;
import static chat.hola.com.app.Utilities.Constants.SIX;
import static chat.hola.com.app.Utilities.Constants.SUCCESS_MSG;
import static chat.hola.com.app.Utilities.Constants.THREE;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.UPDATE_CART;
import static chat.hola.com.app.Utilities.Constants.VALUE_ZERO;
import static chat.hola.com.app.Utilities.Constants.VIEW_PAGER_IMAGES;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static chat.hola.com.app.Utilities.Utilities.isEmptyArray;
import static chat.hola.com.app.ecom.pdp.PdpUiAction.CROSS_ICON;
import static chat.hola.com.app.ecom.pdp.PdpUiAction.GOTO_CART;
import static chat.hola.com.app.ecom.pdp.PdpUiAction.LOGIN;
import static chat.hola.com.app.ecom.pdp.PdpUiAction.MORE;
import static chat.hola.com.app.ecom.pdp.PdpUiAction.OPEN_LOGIN;
import static chat.hola.com.app.ecom.pdp.PdpUiAction.RATING_AND_REVIEWS;
import static chat.hola.com.app.ecom.pdp.PdpUiAction.SIZE_CHART;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;
import static com.kotlintestgradle.data.utils.DataConstants.EMPTY_STRING;

import android.annotation.SuppressLint;
import android.net.Uri;
import androidx.core.util.Pair;
import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.Utilities.model.Product;
import chat.hola.com.app.manager.session.SessionManager;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.jio.consumer.domain.interactor.user.handler.UserHandler;
import com.kotlintestgradle.UseCaseHandler;
import com.kotlintestgradle.interactor.ecom.AddProductToWishListUseCase;
import com.kotlintestgradle.interactor.ecom.CartHandler;
import com.kotlintestgradle.interactor.ecom.DeleteWishListProductUseCase;
import com.kotlintestgradle.interactor.ecom.GetSellerListUseCase;
import com.kotlintestgradle.interactor.ecom.GetSimilarProductsUseCase;
import com.kotlintestgradle.interactor.ecom.LikeDisLikeReviewUseCase;
import com.kotlintestgradle.interactor.ecom.cart.AddProductToCartUseCase;
import com.kotlintestgradle.interactor.ecom.cart.NotifyProductAvailabilityUseCase;
import com.kotlintestgradle.interactor.ecom.cart.PdpUseCase;
import com.kotlintestgradle.interactor.ecom.cart.UpdateCartUseCase;
import com.kotlintestgradle.model.ecom.common.FinalPriceListData;
import com.kotlintestgradle.model.ecom.pdp.AttributesData;
import com.kotlintestgradle.model.ecom.pdp.PdpOfferData;
import com.kotlintestgradle.model.ecom.pdp.ProductModel;
import com.kotlintestgradle.model.ecom.pdp.ReviewData;
import com.kotlintestgradle.model.ecom.pdp.SupplierData;
import com.kotlintestgradle.model.ecom.pdp.VariantsData;
import com.kotlintestgradle.model.ecom.pdp.substitutes.DataItems;
import com.kotlintestgradle.model.ecom.similarproducts.SimilarProductsData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import javax.inject.Inject;

/**
 * view model class for the product detail activity
 */
public class ProductDetailsViewModel extends ViewModel {
  public ObservableBoolean progressVisible = new ObservableBoolean(FALSE);
  public ObservableField<Boolean> progress = new ObservableField<>(FALSE);
  public ObservableField<Boolean> otherSellers = new ObservableField<>(FALSE);
  public ObservableField<String> otherSellersCount = new ObservableField<>();
  public ObservableField<Boolean> allDetHighLights = new ObservableField<>(FALSE);
  public ObservableField<Boolean> productCartOptions = new ObservableField<>(TRUE);
  public ObservableField<Boolean> productAddToCart = new ObservableField<>(FALSE);
  public ObservableField<Boolean> productOutOfStock = new ObservableField<>(FALSE);
  public ObservableField<Boolean> reviewsCountVisible = new ObservableField<>(TRUE);
  public ObservableField<Boolean> offerViews = new ObservableField<>(FALSE);
  public ObservableField<Boolean> prescriptionRequired = new ObservableField<>(FALSE);
  public ObservableField<Boolean> isQuantityAvailable = new ObservableField<>(FALSE);
  public MutableLiveData<Boolean> isManufacturerAvailable = new MutableLiveData<>(FALSE);
  public ObservableField<Boolean> offerMsgViews = new ObservableField<>(TRUE);
  public ObservableField<Boolean> allDetSpecifications = new ObservableField<>(FALSE);
  public ObservableField<Boolean> productOnOffer = new ObservableField<>(FALSE);
  public ObservableField<Boolean> sizeChartVisible = new ObservableField<>(TRUE);
  public ObservableField<Boolean> productHighLights = new ObservableField<>(FALSE);
  public ObservableField<Boolean> sellerInForm = new ObservableField<>(FALSE);
  public ObservableField<Boolean> highLightsVisibility = new ObservableField<>(TRUE);
  public ObservableField<Boolean> productSpecifications = new ObservableField<>(FALSE);
  public ObservableBoolean cartCountVisibility = new ObservableBoolean(FALSE);
  public ObservableField<Boolean> availableOffers = new ObservableField<>(TRUE);
  public ObservableField<Integer> productAddedFav = new ObservableField<>();
  public ObservableField<String> productName = new ObservableField<>();
  public ObservableField<String> totalReviews = new ObservableField<>();
  public ObservableField<String> sellerName = new ObservableField<>();
  public ObservableField<String> manufacturerName = new ObservableField<>();
  public ObservableField<String> productSpecificationTitle = new ObservableField<>();
  public ObservableField<String> avgRating = new ObservableField<>();
  public ObservableField<String> sellerRating = new ObservableField<>();
  public ObservableField<String> totalRating = new ObservableField<>();
  public ObservableField<String> productPrice = new ObservableField<>();
  public ObservableField<String> parentProductId = new ObservableField<>();
  public ObservableField<String> totalStarRating = new ObservableField<>();
  public ObservableField<String> totalRatingAndReviews = new ObservableField<>();
  public ObservableField<String> productActualPrice = new ObservableField<>();
  public ObservableField<String> brandName = new ObservableField<>();
  public ObservableField<String> cartCount = new ObservableField<>();
  public ObservableField<String> productOfferPer = new ObservableField<>();
  public ObservableField<String> addToCartTxt = new ObservableField<>();
  public ObservableField<String> currentQuantity = new ObservableField<>();
  public ObservableField<String> medicineQuantity = new ObservableField<>();
  public ObservableField<String> substituteHeading = new ObservableField<>();
  private PdpOfferData mOffer;
  private String mShareLink = BuildConfig.APP_LINK;
  @Inject
  public UserHandler mUserInfoHandler;
  String mPrimaryImage, mColor;
  @Inject
  CartHandler mCartHandler;
  @Inject
  SessionManager mSessionManager;
  String mOfferPrice = "";
  private PdpUseCase mPdpUseCase;
  private String mDeviceIpAddress = "";
  /* private ReportReviewUseCase mReportReviewUseCase;*/
  private NotifyProductAvailabilityUseCase mNotifyProductAvailabilityUseCase;
  private LikeDisLikeReviewUseCase mLikeDisLikeReviewUseCase;
  String suggestedParentProductId = EMPTY_STRING;
  String suggestedProductId = EMPTY_STRING;
  String suggestedUnitId = EMPTY_STRING;
  String suggestedStoreId = VALUE_ZERO;
  private DeleteWishListProductUseCase mRemoveWishListUseCase;
  private AddProductToWishListUseCase mAddProductToWishListUseCase;
  private UpdateCartUseCase mUpdateCartUseCase;
  private UseCaseHandler handler;
  private MutableLiveData<PdpUiAction> mClick = new MutableLiveData<>();
  private MutableLiveData<Pair<String, Object>> mLiveData =
      new MutableLiveData<>();
  private MutableLiveData<HashMap<Integer, Object>> mVariants =
      new MutableLiveData<>();
  private MutableLiveData<Uri> mShortLink = new MutableLiveData<>();
  private MutableLiveData<Uri> shareLink = new MutableLiveData<>();
  private MutableLiveData<Pair<String, Object>> mSimilarProducts = new MutableLiveData<>();
  String mProductId, mParentProductId, mHtmlAttributesHeading;
  private ReviewData mReviewData;
  private int mSellerCount, mAvailableCount;
  private MutableLiveData<ArrayList<AttributesData>> mHtmlAttributes = new MutableLiveData<>();
  private MutableLiveData<ArrayList<DataItems>> mDifferentSubstitute = new MutableLiveData<>();
  private SupplierData mSupplierData;
  private String mSellerSince;
  private String mUnitId, mStoreId;
  private PdpOfferData mPdpOfferData;
  private AddProductToCartUseCase mAddProductToCartUseCase;
  private GetSellerListUseCase mGetSellerListUseCase;
  private GetSimilarProductsUseCase mGetSimilarProductsUseCase;
  private int mVarientSize;
  private Product mProduct;
  private MutableLiveData<Boolean> mIsShoppingList = new MutableLiveData<>();

  /**
   * constructor for this mData
   *
   * @param pdpUseCase pdp use case
   * @param handler    handler for this product detail view model.
   */
  @Inject
  ProductDetailsViewModel(PdpUseCase pdpUseCase, /*ReportReviewUseCase reportReviewUseCase,*/
      LikeDisLikeReviewUseCase likeDisLikeReviewUseCase,
      AddProductToWishListUseCase addProductToWishListUseCase,
      DeleteWishListProductUseCase deleteWishListProductUseCase,
      UpdateCartUseCase updateCartUseCase,
      NotifyProductAvailabilityUseCase notifyProductAvailabilityUseCase,
      AddProductToCartUseCase addProductToCartUseCase, GetSellerListUseCase getSellerListUseCase,
      UseCaseHandler handler, GetSimilarProductsUseCase getSimilarProductsUseCase) {
    this.mPdpUseCase = pdpUseCase;
    this.mUpdateCartUseCase = updateCartUseCase;
    /*this.mReportReviewUseCase = reportReviewUseCase;*/
    this.mLikeDisLikeReviewUseCase = likeDisLikeReviewUseCase;
    this.mAddProductToWishListUseCase = addProductToWishListUseCase;
    this.mRemoveWishListUseCase = deleteWishListProductUseCase;
    this.mNotifyProductAvailabilityUseCase = notifyProductAvailabilityUseCase;
    this.mAddProductToCartUseCase = addProductToCartUseCase;
    this.mGetSellerListUseCase = getSellerListUseCase;
    this.mGetSimilarProductsUseCase = getSimilarProductsUseCase;
    this.handler = handler;
  }

  /**
   * for getting device details
   *
   * @param deviceIpAddress [IP of device]
   */
  void getDeviceDetails(String deviceIpAddress) {
    this.mDeviceIpAddress = deviceIpAddress;
  }

  /**
   * call the product details api
   *
   * @param productId       product id
   * @param parentProductId parent product id
   */
  void callProductDetailsApi(String productId, String parentProductId, double lat, double lan) {
    progressVisible.set(TRUE);
    productCartOptions.set(FALSE);
    mProductId = productId;
    mParentProductId = parentProductId;
    DisposableSingleObserver<PdpUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<PdpUseCase.ResponseValues>() {
          @Override
          public void onSuccess(PdpUseCase.ResponseValues responseValues) {
            mSellerCount = ZERO;
            parsePdpResponseValues(responseValues);
            Utilities.printLog("http" + "mSellerCount" + mSellerCount);
            if (mSellerCount > ONE) {
              callGetSellersApi(mProductId, mParentProductId);
            } else {
              progressVisible.set(FALSE);
            }
            mIsShoppingList.postValue(
                responseValues.getData().getProductData().getData().get(ZERO).isShoppingList());
            setProductDetails(responseValues.getData().getProductData().getData().get(ZERO));
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
            productCartOptions.set(TRUE);
            mLiveData.postValue(Pair.create(ERROR_MSG, (Object) e.getMessage()));
            Utilities.printLog("Pdp Fail" + e.getMessage());
          }
        };
    handler.execute(mPdpUseCase, new PdpUseCase.RequestValues(productId, parentProductId, lat, lan,
            mSessionManager.getIpAdress(), mSessionManager.getCity(), mSessionManager.getCountry()),
        disposableSingleObserver);
  }

  /** This method is using for subscribing to User cart data */
  void onCartUpdate() {
    Utilities.printLog("onCartUpdatePdp");
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    compositeDisposable.add(
        mUserInfoHandler
            .getCartDataObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(cartData -> {
              switch (cartData.getAction()) {
                case ADD_CART:
                  if (mProductId != null && mProductId.equals(cartData.getProductId())) {
                    addToCartTxt.set(
                        AppController.getInstance().getString(R.string.pdpAddGoCart));
                    productAddToCart.set(TRUE);
                    currentQuantity.set(String.valueOf(cartData.getNewQuantity()));
                  }
                  setCartStatus(mProductId);
                  break;
                case UPDATE_CART:
                  if (mProductId != null && mProductId.equals(cartData.getProductId())) {
                    addToCartTxt.set(
                        AppController.getInstance().getString(R.string.pdpAddGoCart));
                    productAddToCart.set(TRUE);
                    currentQuantity.set(String.valueOf(cartData.getNewQuantity()));
                  }
                  break;
                case DELETE_CART:
                  if (mProductId != null && mProductId.equals(cartData.getProductId())) {
                    addToCartTxt.set(
                        AppController.getInstance().getString(R.string.pdpAddToCart));
                    productAddToCart.set(FALSE);
                  }
                  setCartStatus(mProductId);
                  break;
                default:
                  break;
              }
            }));
  }

  /**
   * <p>calls update cart API</p>
   */
  void callUpdateCartApi(int action, int newQuantity, boolean isSuggestedItem) {
    progress.set(TRUE);
    DisposableSingleObserver<UpdateCartUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<UpdateCartUseCase.ResponseValues>() {
          @Override
          public void onSuccess(UpdateCartUseCase.ResponseValues responseValues) {
            Utilities.printLog("UpdateCartCart Succ" + responseValues.getData().getMessage());
            progress.set(FALSE);
            if (action == ADD_CART) {
              cartCount.set(cartCount.get() != null && Integer.parseInt(
                  Objects.requireNonNull(cartCount.get())) < ONE
                  ? String.valueOf(ONE)
                  : String.valueOf(
                      Integer.parseInt(Objects.requireNonNull(cartCount.get())) + ONE));
              cartCountVisibility.set(TRUE);
            } else if (action != UPDATE_CART) {
              cartCount.set(Integer.parseInt(
                  Objects.requireNonNull(cartCount.get())) > ZERO ? String.valueOf(
                  Integer.parseInt(Objects.requireNonNull(cartCount.get())) - ONE)
                  : String.valueOf(ONE));
              if (Integer.parseInt(Objects.requireNonNull(cartCount.get())) == ZERO) {
                cartCountVisibility.set(FALSE);
              }
            }
            if (!isSuggestedItem) {
              addToCartTxt.set(
                  (action == ADD_CART || action == UPDATE_CART)
                      ? AppController.getInstance().getString(
                      R.string.pdpAddGoCart)
                      : AppController.getInstance().getString(R.string.pdpAddToCart));
              productAddToCart.set((action == ADD_CART || action == UPDATE_CART) ? TRUE : FALSE);
              if (action == ADD_CART || action == UPDATE_CART) {
                currentQuantity.set(String.valueOf(newQuantity));
              } else {
                cartCount.set(Integer.parseInt(
                    Objects.requireNonNull(cartCount.get())) > ZERO ? String.valueOf(
                    Integer.parseInt(Objects.requireNonNull(cartCount.get())) - ONE)
                    : String.valueOf(ONE));
                if (Integer.parseInt(Objects.requireNonNull(cartCount.get())) == ZERO) {
                  cartCountVisibility.set(FALSE);
                }
                productCartOptions.set(TRUE);
              }
              suggestedProductId = EMPTY_STRING;
            }
          }

          @Override
          public void onError(Throwable e) {
            progress.set(FALSE);
            mLiveData.postValue(Pair.create(ERROR_MSG, (Object) e.getMessage()));
            Utilities.printLog("UpdateCartCart Fail" + e.getMessage());
            suggestedProductId = EMPTY_STRING;
          }
        };
    handler.execute(mUpdateCartUseCase,
        new UpdateCartUseCase.RequestValues(RETAILER, isSuggestedItem
            ? suggestedParentProductId : mParentProductId,
            isSuggestedItem ? suggestedProductId : mProductId,
            isSuggestedItem ? suggestedUnitId : mUnitId,
            isSuggestedItem ? suggestedStoreId : mStoreId,
            MULTI_STORE, "", "", "", newQuantity,
            ONE, action, MULTI_STORE, mDeviceIpAddress,
            DEFAULT_LAT_LANG, DEFAULT_LAT_LANG, mOffer, mSessionManager.getCurrencySymbol(),
            mSessionManager.getCurrency()),
        disposableSingleObserver);
  }

  /**
   * This method is using to get product detail
   *
   * @return product data
   */
  Product getProduct() {
    return mProduct;
  }

  /*sets the product details*/
  private void setProductDetails(ProductModel model) {
    if (!isEmptyArray(model.getImages())) {
      mProduct = new Product(model.getProductName(), model.getParentProductId(),
          model.getChildProductId(), model.getUnitId(), model.getFinalPriceList().getBasePrice(),
          model.getFinalPriceList().getFinalPrice(), model.isOutOfStock(),
          model.getImages().get(ZERO).getMedium(), model.getCurrencySymbol(),
          model.getCurrency(), model.getBrandName(), model.getCatName(), INSERT);
      mProduct.setStoreId(model.getSupplier() != null ? model.getSupplier().getId() : "");
    }
  }

  /**
   * call the product details api
   *
   * @param reviewId review id
   */
  void callReportReviewApi(String reviewId) {
    /*progress.set(TRUE);
    DisposableSingleObserver<ReportReviewUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<ReportReviewUseCase.ResponseValues>() {
          @Override
          public void onSuccess(ReportReviewUseCase.ResponseValues responseValues) {
            progress.set(FALSE);
            Utilities.printLog("REview Succ");
            mLiveData.postValue(
                Pair.create(ERROR_MSG, (Object) responseValues.getData().getMessage()));
          }

          @Override
          public void onError(Throwable e) {
            progress.set(FALSE);
            mLiveData.postValue(Pair.create(ERROR_MSG, (Object) e.getMessage()));
            Utilities.printLog("REview Fail" + e.getMessage());
          }
        };
    handler.execute(mReportReviewUseCase, new ReportReviewUseCase.RequestValues(reviewId),
        disposableSingleObserver);*/
  }

  /**
   * call the notify  product  api
   */
  void callNotifyProductApi(String email, String parentProductId) {
    progress.set(TRUE);
    DisposableSingleObserver<NotifyProductAvailabilityUseCase.ResponseValues>
        disposableSingleObserver =
        new DisposableSingleObserver<NotifyProductAvailabilityUseCase.ResponseValues>() {
          @Override
          public void onSuccess(NotifyProductAvailabilityUseCase.ResponseValues responseValues) {
            progress.set(FALSE);
            Utilities.printLog("Notify Succ");
            mLiveData.postValue(
                Pair.create(ERROR_MSG, (Object) responseValues.getData().getMessage()));
          }

          @Override
          public void onError(Throwable e) {
            progress.set(FALSE);
            mLiveData.postValue(Pair.create(ERROR_MSG, (Object) e.getMessage()));
            Utilities.printLog("Notify Fail" + e.getMessage());
          }
        };
    handler.execute(mNotifyProductAvailabilityUseCase,
        new NotifyProductAvailabilityUseCase.RequestValues(
            suggestedProductId.isEmpty() ? mProductId : suggestedProductId, email,
            suggestedProductId.isEmpty() ? parentProductId : suggestedParentProductId),
        disposableSingleObserver);
  }

  /**
   * call the LikeOrDislike api
   *
   * @param reviewId review id
   */
  public void callLikeOrDislikeApi(String reviewId, boolean likeOrDislike) {
    progress.set(TRUE);
    DisposableSingleObserver<LikeDisLikeReviewUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<LikeDisLikeReviewUseCase.ResponseValues>() {
          @Override
          public void onSuccess(LikeDisLikeReviewUseCase.ResponseValues respconseValues) {
            progress.set(FALSE);
            Utilities.printLog("LIke Succ");
            mLiveData.postValue(Pair.create(LIKE_DISLIKE, likeOrDislike));
          }

          @Override
          public void onError(Throwable e) {
            progress.set(FALSE);
            mLiveData.postValue(Pair.create(ERROR_MSG, (Object) e.getMessage()));
            Utilities.printLog("Like Fail" + e.getMessage());
          }
        };
    if (likeOrDislike) {
      handler.execute(mLikeDisLikeReviewUseCase,
          new LikeDisLikeReviewUseCase.RequestValues(reviewId, likeOrDislike),
          disposableSingleObserver);
    } else {
      handler.execute(mLikeDisLikeReviewUseCase,
          new LikeDisLikeReviewUseCase.RequestValues(TRUE, reviewId),
          disposableSingleObserver);
    }
  }


  /**
   * call to add to wishList api
   *
   * @param productId product id
   */
  public void callAddWishListApi(String productId) {
    progress.set(TRUE);
    DisposableSingleObserver<AddProductToWishListUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<AddProductToWishListUseCase.ResponseValues>() {
          @Override
          public void onSuccess(AddProductToWishListUseCase.ResponseValues responseValues) {
            progress.set(FALSE);
            mLiveData.postValue(Pair.create(ERROR_MSG, responseValues.getData().getMessage()));
            productAddedFav.set(R.drawable.fav_icon_selected);
            Utilities.printLog("wishList Succ");
          }

          @Override
          public void onError(Throwable e) {
            progress.set(FALSE);
            mLiveData.postValue(Pair.create(ERROR_MSG, e.getMessage()));
            Utilities.printLog("wishList Fail" + e.getMessage());
          }
        };
    handler.execute(mAddProductToWishListUseCase,
        new AddProductToWishListUseCase.RequestValues(productId, mDeviceIpAddress,
            DEFAULT_LAT_LANG,
            DEFAULT_LAT_LANG, "", "", AppController.getInstance().getUserId()),
        disposableSingleObserver);
  }

  public MutableLiveData<Pair<String, Object>> subscribeAddFavouriteProduct() {
    return mLiveData;
  }

  /**
   * call to remove from  wishList api
   *
   * @param productId product id
   */
  private void callDeleteWishListApi(String productId) {
    progress.set(TRUE);
    DisposableSingleObserver<DeleteWishListProductUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<DeleteWishListProductUseCase.ResponseValues>() {
          @Override
          public void onSuccess(DeleteWishListProductUseCase.ResponseValues responseValues) {
            progress.set(FALSE);
            mLiveData.postValue(Pair.create(ERROR_MSG, responseValues.getData().getMessage()));
            productAddedFav.set(R.drawable.fav_icon);
            Utilities.printLog("wishList Succ");
          }

          @Override
          public void onError(Throwable e) {
            progress.set(FALSE);
            mLiveData.postValue(Pair.create(ERROR_MSG, e.getMessage()));
            Utilities.printLog("wishList Fail" + e.getMessage());
          }
        };
    handler.execute(mRemoveWishListUseCase,
        new DeleteWishListProductUseCase.RequestValues(productId, mDeviceIpAddress,
            DEFAULT_LAT_LANG,
            DEFAULT_LAT_LANG, "", ""),
        disposableSingleObserver);
  }

  /**
   * listens click on share
   */
  public void goToCart() {
    mClick.postValue(GOTO_CART);
  }

  /**
   * pdp response values
   */
  @SuppressLint("DefaultLocale")
  void parsePdpResponseValues(PdpUseCase.ResponseValues responseValues) {
    ProductModel productModel = responseValues.getData().getProductData().getData().get(ZERO);
    if (productModel != null) {
      parentProductId.set(productModel.getParentProductId());
      mShareLink = productModel.getShareLink();
      productOutOfStock.set(productModel.isOutOfStock());
      productCartOptions.set(!productModel.isOutOfStock());
      mOffer = productModel.getOffers();
      mAvailableCount = productModel.getAvailableQuantity();
      mLiveData.setValue(Pair.create(VIEW_PAGER_IMAGES, productModel.getImages()));
      if (productModel.getProductName() != null) {
        productName.set(productModel.getProductName());
      }
      productAddedFav.set(
          productModel.isFavorite() ? R.drawable.fav_icon_selected : R.drawable.fav_icon);
      FinalPriceListData finalPriceListData = productModel.getFinalPriceList();
      if (finalPriceListData.getFinalPrice() != null) {
        productPrice.set(
            String.format("%s%s", productModel.getCurrencySymbol(),
                String.format("%.2f", Float.parseFloat(finalPriceListData.getFinalPrice()))));
      }
      if (finalPriceListData.getBasePrice() != null) {
        productActualPrice.set(
            String.format("%.2f", Float.parseFloat(finalPriceListData.getBasePrice())));
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
      if (offerViews.get()) {
        mOfferPrice = productActualPrice.get();
      }
      if (productModel.getAllOffers() != null && productModel.getAllOffers().size() > ZERO) {
        mLiveData.postValue(Pair.create(ALL_OFFERS, productModel.getAllOffers()));
      } else {
        availableOffers.set(FALSE);
      }

      brandName.set(
          (productModel.getBrandName() == null ? EMPTY_STRING : productModel.getBrandName()));
      mSellerCount = productModel.getSellerCount();
      mSupplierData = productModel.getSupplier();
      sellerName.set(mSupplierData.getSupplierName());
      mSellerSince = mSupplierData.getSellerSince();
      if (mSupplierData.getRating() != null) {
        sellerRating.set((String.format("%.1f", Float.parseFloat(mSupplierData.getRating()))));
      }
      Utilities.printLog("visible" + (mSupplierData.getReviewParameter() != null
          && mSupplierData.getReviewParameter().size() > ZERO));
      sellerInForm.set((mSupplierData.getReviewParameter() != null
          && mSupplierData.getReviewParameter().size() > ZERO));
      prescriptionRequired.set(productModel.isPrescriptionRequired());
      mLiveData.setValue(Pair.create(GET_PRESCRIPTION_REQUIRED, prescriptionRequired.get()));
      if (productModel.getMouDataUnit() != null && !productModel.getMouDataUnit().isEmpty()) {
        isQuantityAvailable.set(Boolean.TRUE);
        medicineQuantity.set(productModel.getMouDataUnit());
      } else {
        isQuantityAvailable.set(FALSE);
      }
      if (productModel.getManufacturerName() != null) {
        isManufacturerAvailable.setValue(
            (productModel.getManufacturerName().isEmpty()) ? FALSE : TRUE);
        manufacturerName.set(productModel.getManufacturerName());
      }
      if (productModel.getHighlight() != null && productModel.getHighlight().size() > ZERO) {
        productHighLights.set(TRUE);
        allDetHighLights.set(TRUE);
        mLiveData.setValue(Pair.create(GET_HILIGHTS, productModel.getHighlight()));
      } else {
        highLightsVisibility.set(FALSE);
      }
      if (productModel.getAttributes() != null && productModel.getAttributes().size() > ZERO) {
        if (productModel.getAttributes().get(ZERO).getInnerAttributes().size() > ZERO) {
          productSpecifications.set(TRUE);
          allDetSpecifications.set(FALSE);
          productSpecificationTitle.set(productModel.getAttributes().get(ZERO).getName());
          mLiveData.setValue(Pair.create(GET_ATTRIBUTES, productModel.getAttributes()));
        }
      }
      mLiveData.setValue(Pair.create(GET_DESCRIPTION, productModel.getDetailDesc()));
      if (productModel.getVariantsData() != null) {
        setVariantsData(productModel.getVariantsData());
        if (!isEmptyArray(productModel.getVariantsData())
            && !isEmptyArray(productModel.getVariantsData().get(ZERO).getSizeData())) {
          mPrimaryImage = productModel.getVariantsData().get(ZERO).getSizeData().get(
              ZERO).getImage();
        }
        if (mPrimaryImage == null && !isEmptyArray(productModel.getImages())) {
          mPrimaryImage = productModel.getImages().get(ZERO).getSmall();
        }
        mVarientSize = productModel.getVariantsData().size();
        mLiveData.setValue(Pair.create(GET_VARIENT_DATA, productModel.getVariantsData()));
        mLiveData.setValue(Pair.create(GET_OFFER_DATA, productModel.getOffers()));
        HashMap<Integer, Object> map = new HashMap<>();
        map.put(ONE, mUnitId);
        map.put(TWO, mSupplierData.getId());
        map.put(THREE, productModel.getVariantsData());
        map.put(FOUR, productModel.getOffers());
        map.put(FIVE, productModel.getAvailableQuantity());
        map.put(SIX, cartCount.get());
        mVariants.postValue(map);
        mStoreId = mSupplierData.getId();
        mPdpOfferData = productModel.getOffers();
      } else {
        if (!isEmptyArray(productModel.getImages())) {
          mPrimaryImage = productModel.getImages().get(ZERO).getSmall();
        }
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
        setCartStatus(mProductId);
        Utilities.printLog("getQuantity" + mCartHandler.getQuantity(mProductId) + "currentQuantity"
            + currentQuantity.get() + "exist" + mCartHandler.isProductExistInCart(mProductId)
            + "totalCartItems" + mCartHandler.totalCartItems());
      }).start();
      if (productModel.getSizeChartData() != null) {
        sizeChartVisible.set(productModel.getSizeChartData().size() > ZERO);
      }
      if (offerViews.get() != null) {
        productOnOffer.set(offerViews.get());
      }
    }
    mReviewData = responseValues.getData().getReview();
    if (mReviewData != null) {
      if (mReviewData.getPenCount() != ZERO) {
        totalReviews.set(
            String.format("%s %d %s",
                AppController.getInstance().getString(R.string.reviewProductAll),
                mReviewData.getPenCount(), AppController.getInstance().getString(
                    R.string.pdpReviews)));
      } else {
        reviewsCountVisible.set(FALSE);
      }
      totalStarRating.set(String.format("%.1f", mReviewData.getTotalStarRating()));
      avgRating.set(String.format("%.1f", mReviewData.getTotalStarRating()));
      totalRating.set(String.format("%s %s", mReviewData.getTotalNoOfRatings(),
          AppController.getInstance().getString(
              R.string.pdpRatings)));
      totalRatingAndReviews.set(
          String.format("%s %s %s\n%d %s", mReviewData.getTotalNoOfRatings(),
              AppController.getInstance().getString(
                  R.string.pdpRatings), AppController.getInstance().getString(R.string.and),
              mReviewData.getTotalNoOfReviews(),
              AppController.getInstance().getString(R.string.pdpReviews)));
      if (mReviewData.getImages() != null && mReviewData.getImages().size() > ZERO) {
        mLiveData.setValue(Pair.create(REVIEW_IMAGES, mReviewData.getImages()));
      }
      if (mReviewData.getRatings() != null && mReviewData.getRatings().size() > ZERO) {
        mLiveData.setValue(Pair.create(GET_RATINGS, mReviewData.getRatings()));
      }
      if (mReviewData.getAttributeRating() != null
          && mReviewData.getAttributeRating().size() > ZERO) {
        mLiveData.setValue(Pair.create(GET_ATTRIBUTES_RATING, mReviewData.getAttributeRating()));
      }
      if (mReviewData.getUserReviews() != null && mReviewData.getUserReviews().size() > ZERO) {
        mLiveData.setValue(Pair.create(REVIEW_DATA, mReviewData.getUserReviews()));
      }
    }
  }

  /*actions for click event for seller*/
  public void onSellerInformation() {
//    if (mSupplierData.getReviewParameter() != null
//        && mSupplierData.getReviewParameter().size() > ZERO) {
    mLiveData.postValue(Pair.create(REVIEW, mSupplierData.getReviewParameter()));
//    } else {
//      mLiveData.postValue(Pair.create(ERROR_MSG,
//          ApplicationManager.getInstance().getString(R.string.somethingWentWrong)));
//    }
  }

  /**
   * actions for add to cart click
   */
  public void addItemToCart() {
    new Thread(() -> {
      if (mAvailableCount != Integer.parseInt(Objects.requireNonNull(currentQuantity.get()))) {
        if (mCartHandler.isProductExistInCart(mProductId)) {
          int newQuantity = Integer.parseInt(Objects.requireNonNull(currentQuantity.get())) + ONE;
          callUpdateCartApi(UPDATE_CART, newQuantity, FALSE);
        }
      } else {
        mLiveData.postValue(Pair.create(ERROR_MSG,
            AppController.getInstance().getString(R.string.pdpAddToCartWarning)));
      }
    }).start();
  }

  /**
   * actions for add to cart click
   */
  public void removeItemFromCart() {
    new Thread(() -> {
      if (currentQuantity != null && currentQuantity.get() != null) {
        int newQuantity = Integer.parseInt(Objects.requireNonNull(currentQuantity.get()));
        callUpdateCartApi((newQuantity != ONE ? UPDATE_CART : DELETE_CART),
            (newQuantity != ONE ? newQuantity - ONE : ZERO), FALSE);
      }
    }).start();
  }

  /**
   * actions for add to add to cart click
   */
  public void addToCart() {
    if (AppController.getInstance().isGuest()) {
      mClick.setValue(OPEN_LOGIN);
    } else {
      if (addToCartTxt.get() != null && addToCartTxt.get().equals(
          AppController.getInstance().getString(R.string.pdpAddToCart))) {
        if (mVarientSize >= ONE) {
          mClick.setValue(PdpUiAction.OPEN_SHEET);
        } else {
          callUpdateCartApi(ADD_CART, ADD_CART, FALSE);
        }
      } else {
        mClick.postValue(GOTO_CART);
      }
    }
  }

  /**
   * actions for rating and reviews
   */
  public void ratingAndReviews() {
    mClick.postValue(RATING_AND_REVIEWS);
  }

  /**
   * actions for rating and reviews
   */
  public void more() {
    mClick.postValue(MORE);
  }

  /**
   * call to add to wishList api
   *
   * @param parentProductId product id
   */
  private void callGetSellersApi(String productId, String parentProductId) {
    progressVisible.set(TRUE);
    DisposableSingleObserver<GetSellerListUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<GetSellerListUseCase.ResponseValues>() {
          @Override
          public void onSuccess(GetSellerListUseCase.ResponseValues responseValues) {
            progressVisible.set(FALSE);
            if (responseValues.getData().getData() != null
                && responseValues.getData().getData().size() > ZERO) {
              String currency = responseValues.getData().getData().get(ZERO).getCurrencySymbol();
              responseValues.getData().getData().get(ZERO).getSupplier().get(ZERO).setCurrencySymbol(currency);
              mLiveData.setValue(Pair.create(GET_SELLERS,
                  responseValues.getData().getData().get(ZERO).getSupplier()));
              if (responseValues.getData().getData().get(ZERO).getSupplier() != null
                  && responseValues.getData().getData().get(ZERO).getSupplier().size() > ZERO) {
                otherSellers.set(TRUE);
                otherSellersCount.set(String.format("%s(%d)",
                    AppController.getInstance().getString(R.string.allOtherSellers),
                    responseValues.getData().getData().get(ZERO).getSupplier().size()));
              }
            }
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
              mLiveData.postValue(Pair.create(ERROR_MSG, e.getMessage()));
            Utilities.printLog("allReviews Fail" + e.getMessage());
          }
        };
    handler.execute(mGetSellerListUseCase,
        new GetSellerListUseCase.RequestValues(productId, parentProductId,
            String.valueOf(RETAILER)),
        disposableSingleObserver);
  }

  /**
   * call to get similar products
   *
   * @param itemId product id
   * @param limit  no of products to fetch
   */
  void callSimilarProductsApi(String itemId, int limit) {
    DisposableSingleObserver<GetSimilarProductsUseCase.ResponseValues> disposableSingleObserver =
            new DisposableSingleObserver<GetSimilarProductsUseCase.ResponseValues>() {
              @Override
              public void onSuccess(GetSimilarProductsUseCase.ResponseValues responseValues) {
                Utilities.printLog("exe similar products Pass");
                if (responseValues.getData() != null) {
                  mSimilarProducts.postValue(Pair.create(SUCCESS_MSG,
                          ((SimilarProductsData) responseValues.getData())));
                }
              }

              @Override
              public void onError(Throwable e) {
                mSimilarProducts.postValue(Pair.create(ERROR_MSG, e.getMessage()));
                Utilities.printLog("exe similar products Fail" + e.getMessage());
              }
            };
    handler.execute(mGetSimilarProductsUseCase,
            new GetSimilarProductsUseCase.RequestValues(itemId, limit),
            disposableSingleObserver);
  }

  /**
   * listens click on reviews
   */
  public void onReviewsClicked() {
    mLiveData.postValue(Pair.create(REVIEW_CLICKED, mReviewData.getPenCount()));
  }

  /**
   * listens when onclick clicked
   */
  public void onAllDetailsClicked() {
    mLiveData.setValue(
        Pair.create(ALL_DETAILS_CLICKED, AppController.getInstance().getResources().getString(
            R.string.pdpHighlightsTitle)));
  }

  /**
   * listens when onclick clicked
   */
  public void onAllDetailClicked() {
    mLiveData.setValue(
        Pair.create(ALL_DETAILS_CLICKED, AppController.getInstance().getResources().getString(
            R.string.productDetails)));
  }

  /**
   * listen when out of stock clicked
   */
  public void outOfStockClicked() {
    mLiveData.postValue(
        Pair.create(OUT_OF_STOCK_CLICKED, !AppController.getInstance().isGuest() ? TRUE : FALSE));
  }

  /**
   * listen when onclick clicked
   */
  public void onAllDetSpecificationClicked() {
    mLiveData.setValue(
        Pair.create(ALL_DETAILS_CLICKED, productSpecificationTitle.get()));
  }

  /**
   * listen when onclick clicked
   */
  public void onRatingClicked() {
    mLiveData.postValue(Pair.create(RATING_CLICKED, TRUE));
  }

  /**
   * notify when backButton clicked
   */
  public void onBackButtonClicked() {
    mClick.postValue(CROSS_ICON);
  }

  /**
   * notify when addToWishList clicked
   */
  public void onWishListClicked() {
    if (!AppController.getInstance().isGuest()) {
      if (productAddedFav != null) {
        if (productAddedFav.get() == R.drawable.fav_icon) {
          callAddWishListApi(mProductId);
        } else {
          callDeleteWishListApi(mProductId);
        }
      }
    } else {
      mClick.setValue(LOGIN);
    }
  }

  /**
   * notify when size chart clicked
   */
  public void onSizeChartClicked() {
    mClick.postValue(SIZE_CHART);
  }

  /**
   * returns live data clicking.
   */
  MutableLiveData<PdpUiAction> onClick() {
    return mClick;
  }

  /**
   * returns the variants array list
   */
  MutableLiveData<HashMap<Integer, Object>> getVariants() {
    return mVariants;
  }

  /**
   * setting the primary image primary color name
   */
  private void setVariantsData(ArrayList<VariantsData> variantsData) {
    if (variantsData != null && variantsData.size() > ZERO) {
      StringBuilder colorQuantity = new StringBuilder();
      colorQuantity.delete(ZERO, colorQuantity.length());
      for (int i = 0; i < variantsData.size(); i++) {
        if (variantsData.get(i) != null && variantsData.get(i).getName() != null
            && variantsData.get(i).getName().equals(
            AppController.getInstance().getResources().getString(R.string.pdpColors))) {
          if (variantsData.get(i).getSizeData() != null && variantsData.get(i).getSizeData().size()
              > ZERO) {
            for (int j = 0; j < variantsData.get(i).getSizeData().size(); j++) {
              if (variantsData.get(i).getSizeData().get(j).getIsPrimary()) {
                mPrimaryImage = variantsData.get(i).getSizeData().get(j).getImage();
                colorQuantity.append(variantsData.get(i).getSizeData().get(j).getName()).append(
                    ",");
                this.mProductId = variantsData.get(i).getSizeData().get(j).getChildProductId();
                this.mUnitId = variantsData.get(i).getSizeData().get(j).getUnitId();
                Utilities.printLog("mProductId" + mProductId);
                break;
              }
            }
          }
        } else {
          for (int j = 0; j < variantsData.get(i).getSizeData().size(); j++) {
            if (variantsData.get(i).getSizeData().get(j).getIsPrimary()) {
              colorQuantity.append(variantsData.get(i).getSizeData().get(j).getName()).append(",");
              break;
            }
          }
        }
      }
      if (!colorQuantity.toString().isEmpty()) {
        mColor = colorQuantity.toString().substring(ZERO, colorQuantity.toString().length() - ONE);
      }
    }
  }

  /**
   * manufacturer tag visibility
   *
   * @return MutableLiveData
   */
  MutableLiveData<Boolean> getManufacturerAvailability() {
    return isManufacturerAvailable;
  }

  /**
   * this method will set the cart total cart count which we added.
   */
  void setCartStatus(String productId) {
    new Thread(() -> {
      if (mCartHandler.totalCartItems(ECOM_FLOW) > ZERO) {
        cartCount.set((mCartHandler.totalCartItems(ECOM_FLOW) != ZERO) ? String.valueOf(
            mCartHandler.totalCartItems(ECOM_FLOW))
            : String.valueOf(ONE));
      } else {
        cartCount.set(String.valueOf(ZERO));
      }
      cartCountVisibility.set(mCartHandler.totalCartItems(ECOM_FLOW) > ZERO ? TRUE : FALSE);
    }).start();
  }

  /**
   * generating the deep with respective params
   */
  private void shareDeepLink() {
    progress.set(TRUE);
//     https://shoppd.page.link/?link=https://shoppd.net/&apn=com.customer
//     .fivecanale&afl=https://shoppd.net/
    String longLink =
        String.format("%s%s%s", BuildConfig.DYNAMIC_LINK, LINK, mShareLink);
    Utilities.printLog("exe" + "longLink" + longLink);
    FirebaseDynamicLinks.getInstance().createDynamicLink()
        .setLongLink(Uri.parse(longLink))
        .setAndroidParameters(
            new DynamicLink.AndroidParameters.Builder(BuildConfig.APPLICATION_ID)
                .build())
        .buildShortDynamicLink()
        .addOnCompleteListener(task -> {
          progress.set(FALSE);
          if (task.isSuccessful()) {
            shareLink.setValue(Objects.requireNonNull(task.getResult()).getShortLink());
          } else {
            shareLink.setValue(null);
          }
        });
  }

  MutableLiveData<Uri> getShareLink() {
    return shareLink;
  }

  /**
   * return the short link to share
   *
   * @return MutableLiveData<Uri>
   */
  MutableLiveData<Uri> getShortLink() {
    return mShortLink;
  }

  /**
   * This method using to get Seller experience
   *
   * @return seller since val
   */
  String getSellerSince() {
    return mSellerSince;
  }

  /**
   * get html attributes, if available
   *
   * @return MutableLiveData<ArrayList < AttributesData>>
   */
  MutableLiveData<ArrayList<AttributesData>> getHtmlAttributes() {
    return mHtmlAttributes;
  }

  /**
   * get different substitutes, if available
   *
   * @return MutableLiveData<ArrayList < AttributesData>>
   */
  MutableLiveData<ArrayList<DataItems>> getDifferentSubstitutes() {
    return mDifferentSubstitute;
  }

  MutableLiveData<Pair<String, Object>> getLiveData() {
    return mLiveData;
  }

  /**
   * This method is using to set newly added shopping id
   */
  void setShippingStatus(Boolean isAdded) {
    mIsShoppingList.postValue(isAdded);
  }


  /**
   * This method is using to get shopping list live data
   *
   * @return shopping list data
   */
  MutableLiveData<Boolean> getIsShoppingList() {
    return mIsShoppingList;
  }

  /**
   * This method is using to get shopping list live data
   *
   * @return shopping list data
   */
  MutableLiveData<Pair<String, Object>> getSimilarProductsList() {
    return mSimilarProducts;
  }

  /**
   * listens click on share
   */
  public void onClickShare() {
    if (Utilities.isNetworkAvailable(AppController.getInstance())) {
      shareDeepLink();
    } else {
      mLiveData.postValue(
          Pair.create(ERROR_MSG,
              AppController.getInstance().getString(R.string.internetIssue)));
    }
  }
}