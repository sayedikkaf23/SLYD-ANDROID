package chat.hola.com.app.ecom.pdp;

import static android.view.View.GONE;
import static chat.hola.com.app.Utilities.Constants.ALL_DETAILS_CLICKED;
import static chat.hola.com.app.Utilities.Constants.ALL_OFFERS;
import static chat.hola.com.app.Utilities.Constants.ALL_PREVIEW_CODE;
import static chat.hola.com.app.Utilities.Constants.ALL_REVIEWS_CODE;
import static chat.hola.com.app.Utilities.Constants.ATTRIBUTES_BOTTOM_SHEET_TAG;
import static chat.hola.com.app.Utilities.Constants.ERROR_MSG;
import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.FIFTY;
import static chat.hola.com.app.Utilities.Constants.FIVE;
import static chat.hola.com.app.Utilities.Constants.FIVE_HUNDRED;
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
import static chat.hola.com.app.Utilities.Constants.LIKE_DISLIKE;
import static chat.hola.com.app.Utilities.Constants.ONE_FIFTY;
import static chat.hola.com.app.Utilities.Constants.OUT_OF_STOCK_CLICKED;
import static chat.hola.com.app.Utilities.Constants.POINT_EIGHT;
import static chat.hola.com.app.Utilities.Constants.POINT_SIX;
import static chat.hola.com.app.Utilities.Constants.POINT_THREE;
import static chat.hola.com.app.Utilities.Constants.POINT_ZERO;
import static chat.hola.com.app.Utilities.Constants.POSITION;
import static chat.hola.com.app.Utilities.Constants.PRICE;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_COLOUR;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_IMAGE;
import static chat.hola.com.app.Utilities.Constants.PRODUCT_NAME;
import static chat.hola.com.app.Utilities.Constants.RATING_CLICKED;
import static chat.hola.com.app.Utilities.Constants.REQUEST_CODE;
import static chat.hola.com.app.Utilities.Constants.REVIEW;
import static chat.hola.com.app.Utilities.Constants.REVIEW_CLICKED;
import static chat.hola.com.app.Utilities.Constants.REVIEW_DATA;
import static chat.hola.com.app.Utilities.Constants.REVIEW_IMAGES;
import static chat.hola.com.app.Utilities.Constants.SELLER_BOTTOM_SHEET_TAG;
import static chat.hola.com.app.Utilities.Constants.SHOPPING_STATUS;
import static chat.hola.com.app.Utilities.Constants.SUCCESS_MSG;
import static chat.hola.com.app.Utilities.Constants.TEN;
import static chat.hola.com.app.Utilities.Constants.TEXT_PLAIN;
import static chat.hola.com.app.Utilities.Constants.THREE;
import static chat.hola.com.app.Utilities.Constants.THREE_FIFTY;
import static chat.hola.com.app.Utilities.Constants.TITLE;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.TWO_FIFTY;
import static chat.hola.com.app.Utilities.Constants.URL;
import static chat.hola.com.app.Utilities.Constants.USER_REVIEW_DATA;
import static chat.hola.com.app.Utilities.Constants.VIEW_PAGER_IMAGES;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static chat.hola.com.app.Utilities.Utilities.isEmptyArray;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;
import static com.kotlintestgradle.data.utils.DataConstants.EMPTY_STRING;
import static com.kotlintestgradle.remote.RemoteConstants.PARENT_PRODUCT_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PRODUCT_ID;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.util.Pair;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.viewpager.widget.ViewPager;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.PreviewImageActivity;
import chat.hola.com.app.Utilities.LocationManagerUtil;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.camera.filter.SpacesItemDecoration;
import chat.hola.com.app.ecom.allreviews.ReviewImgClick;
import chat.hola.com.app.ecom.cart.EcomCartActivity;
import chat.hola.com.app.ecom.home.GridListProductsAdapter;
import chat.hola.com.app.ecom.pdp.adapters.AllOffersAdapter;
import chat.hola.com.app.ecom.pdp.adapters.HighLightsAdapter;
import chat.hola.com.app.ecom.pdp.adapters.PdpVariantsAdapter;
import chat.hola.com.app.ecom.pdp.adapters.ProductRatingOnAdapter;
import chat.hola.com.app.ecom.pdp.adapters.ProductReviewImagesAdapter;
import chat.hola.com.app.ecom.pdp.adapters.ProductsViewPagerAdapter;
import chat.hola.com.app.ecom.pdp.adapters.RatingDistributionsAdapter;
import chat.hola.com.app.ecom.pdp.adapters.ReviewsAdapter;
import chat.hola.com.app.ecom.pdp.adapters.SpecificationsAdapter;
import chat.hola.com.app.ecom.pdp.adapters.ViewMoreSellersAdapter;
import chat.hola.com.app.ecom.pdp.attributebottomsheet.AttributesBottomSheetFragment;
import chat.hola.com.app.ecom.pdp.sellerbottomsheet.SellerBottomSheetFragment;
import chat.hola.com.app.ecom.review.ReviewProductActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.ui.dialog.CustomDialogUtilBuilder;
import chat.hola.com.app.webScreen.WebActivity;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ActivityProductDetailsBinding;
import com.google.android.material.snackbar.Snackbar;
import com.kotlintestgradle.interactor.ecom.cart.PdpUseCase;
import com.kotlintestgradle.model.ecom.common.ImageData;
import com.kotlintestgradle.model.ecom.pdp.AttributeRatingData;
import com.kotlintestgradle.model.ecom.pdp.AttributesData;
import com.kotlintestgradle.model.ecom.pdp.InnerAttributesData;
import com.kotlintestgradle.model.ecom.pdp.PdpOfferData;
import com.kotlintestgradle.model.ecom.pdp.Rating;
import com.kotlintestgradle.model.ecom.pdp.ReviewParameterData;
import com.kotlintestgradle.model.ecom.pdp.SupplierData;
import com.kotlintestgradle.model.ecom.pdp.UserReviewData;
import com.kotlintestgradle.model.ecom.pdp.VariantsData;
import com.kotlintestgradle.model.ecom.similarproducts.SimilarProductsData;
import dagger.android.support.DaggerAppCompatActivity;
import java.util.ArrayList;
import java.util.Collections;
import javax.inject.Inject;

/**
 * <h1>This class is used to show the details of the particular product.</h1>
 */
public class ProductDetailsActivity extends DaggerAppCompatActivity implements
    PdpVariantsClickListener, PopupMenu.OnMenuItemClickListener, ReviewsMenuClick,
    ReviewImgClick,
    CustomDialogUtilBuilder.DialogOutOfStockNotifyListener, ViewMoreSellersOnClick,
    OfferItemOnClick, /*HtmlAttributesAdapter.OnAttributesClickListener,*/
     GridListProductsAdapter.AddButtonClickListener,
    LocationManagerUtil.FusedLocationListener {
  @Inject
  ViewModelProvider.Factory mViewModelFactory;
  @Inject
  AttributesBottomSheetFragment mAttributesBottomSheetFragment;
  @Inject
  SellerBottomSheetFragment mSellerBottomSheetFragment;
  @Inject
  SessionManager manager;
  private ProductDetailsViewModel mProductDetailsViewModel;
  private ActivityProductDetailsBinding mBinding;
  private ArrayList<ImageData> mMobileImages = new ArrayList<>();
  private ArrayList<String> mReviewImages = new ArrayList<>();
  private ProductsViewPagerAdapter mProductsViewPagerAdapter;
  private HighLightsAdapter mHighLightsAdapter;
  private SpecificationsAdapter mSpecificationsAdapter;
  private ProductRatingOnAdapter mProductRatingOnAdapter;
  private ProductReviewImagesAdapter mProductReviewImagesAdapter;
  private RatingDistributionsAdapter mRatingDistributionsAdapter;
  private ReviewsAdapter mReviewsAdapter;
  private PdpOfferData mOffer;
  private ArrayList<String> mHighLightsList = new ArrayList<>();
  private ArrayList<InnerAttributesData> mSpecialitiesList = new ArrayList<>();
  private ArrayList<AttributesData> mAttributesData = new ArrayList<>();
  private ArrayList<AttributeRatingData> mAttributeRatingData = new ArrayList<>();
  private ArrayList<UserReviewData> mUserReviewDataArrayList = new ArrayList<>();
  private ArrayList<Rating> mUserRatingData = new ArrayList<>();
  private ArrayList<VariantsData> mVariantsData = new ArrayList<>();
  private ArrayList<SupplierData> mSupplierArrayList = new ArrayList<>();
  private ArrayList<PdpOfferData> mAllOffersArrayList = new ArrayList<>();
  private ViewMoreSellersAdapter mViewMoreSellersAdapter;
  private AllOffersAdapter mAllOffersAdapter;
  private int mDotsCount;
  private ImageView[] mDots;
  private String mParentProductId, mProductId, mReviewId;
  private PdpVariantsAdapter mPdpVariantsAdapter;
  private int mPosition;
  private UserReviewData mUserReviewData;
  private int mReviewPos, mAvailableCount, mCartCount;
  private String mUnitId, mStoreId;
  private PdpOfferData mOffersListData;
  private double mLat, mLan;
  private String mDescription = "";
  private boolean mIsPrescriptionRequired = false;
  private int moreIndex = ONE, mLineCount;
  private boolean shoppingStatus = false;
  private boolean isSimilarItemsCalled = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    requestWindowFeature(Window.FEATURE_NO_TITLE);
    super.onCreate(savedInstanceState);
    initializeView();
    initializeViewModel();
    initializeViewPager();
    initializeHighLightsAdapter();
    initializeSpecificationsAdapter();
    initializeRatingDistributionAdapter();
    initializeProductReviewImagesAdapter();
    initializeReviewsAdapter();
    initializeRatingOnAdapter();
    initializeVariantsAdapter();
    initializeOffersAdapter();
    initializeViewMoreSellersAdapter();
    subscribeBackButton();
    subscribeVariantData();
    subscribeSimilarItems();
    subscribeLiveData();
    subscribeHtmlAttributes();
    subscribeSubstituteAttributes();
    subscribeToShareLink();
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      mParentProductId = bundle.getString(PARENT_PRODUCT_ID);
      mProductId = bundle.getString(PRODUCT_ID);
     /* if (!LocationManagerUtil.isGpsEnabled(this)) {
        mProductDetailsViewModel.callProductDetailsApi(mProductId, mParentProductId,
            DEFAULT_LAT_LANG, DEFAULT_LAT_LANG);
      } else {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
          LocationManagerUtil.getFuseLocation(this, this::onSuccess);
        } else {
          mProductDetailsViewModel.callProductDetailsApi(mProductId, mParentProductId,
              DEFAULT_LAT_LANG, DEFAULT_LAT_LANG);
        }
      }*/
      mLat = manager.getLatitude();
      mLan = manager.getLongitude();
      mProductDetailsViewModel.callProductDetailsApi(mProductId,
          mParentProductId, mLat, mLan);
    }

  //  mProductDetailsViewModel.callSimilarProductsApi(mParentProductId, TEN);
  }

  /**
   * subscribe to similar items response
   */
  private void subscribeSimilarItems() {
    mProductDetailsViewModel.getSimilarProductsList().observe(this, objectPair -> {
      switch ((String) objectPair.first) {
        case SUCCESS_MSG:
          mBinding.tvPdpAlsoViewed.setVisibility(View.VISIBLE);
          mBinding.rvPdpAlsoViewed.setVisibility(View.VISIBLE);
          SimilarProductsData data = (SimilarProductsData) objectPair.second;
          if (data != null && !isEmptyArray(data.getProducts())) {
            GridListProductsAdapter adapter = new GridListProductsAdapter(data.getProducts(),
                this, !AppController.getInstance().isGuest());
            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.dp_7);
            mBinding.rvPdpAlsoViewed.addItemDecoration(
                new SpacesItemDecoration(spacingInPixels));
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, TWO);
            mBinding.rvPdpAlsoViewed.setLayoutManager(gridLayoutManager);
            mBinding.rvPdpAlsoViewed.setAdapter(adapter);
          }
          break;

        case ERROR_MSG:
          break;
      }

    });
  }

  /**
   * subscribe to share link
   */
  private void subscribeToShareLink() {
    mProductDetailsViewModel.getShareLink().observe(this, shortLink -> {
      if (shortLink == null) {
        Toast.makeText(ProductDetailsActivity.this,
            getResources().getString(R.string.errorGeneratingLink), Toast.LENGTH_SHORT).show();
      } else {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType(TEXT_PLAIN);
        intent.putExtra(Intent.EXTRA_SUBJECT,
            AppController.getInstance().getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
        startActivity(intent);
      }
    });
  }


  /**
   * initialize adapter for view more sellers
   */
  private void initializeViewMoreSellersAdapter() {
    mViewMoreSellersAdapter = new ViewMoreSellersAdapter(mSupplierArrayList, this);
    mBinding.rvViewSellersDetails.setAdapter(mViewMoreSellersAdapter);
  }

  /**
   * initialize adapter for view more sellers
   */
  private void initializeOffersAdapter() {
    mAllOffersAdapter = new AllOffersAdapter(mAllOffersArrayList, this::onOfferItemClick);
    mBinding.rvPdpAllOffers.setAdapter(mAllOffersAdapter);
  }

  /**
   * open the login activity screen
   */
  private void startBoardingAct() {
    AppController.getInstance().openSignInDialog(this);
  }

  private void startAllDetailsAct(String title) {
   /* Intent intent = new Intent(this, AllDetailsActivity.class);
    intent.putExtra(SPECIFICATIONS, title);
    intent.putExtra(ATTRIBUTES_DATA, mAttributesData);
    intent.putExtra(HIGHLIGHT_DATA, mHighLightsList);
    intent.putExtra(HTML_ATTRIBUTES_DATA, mProductDetailsViewModel.mHtmlAttributesData);
    intent.putExtra(HTML_ATTRIBUTES_HEADING, mProductDetailsViewModel.mHtmlAttributesHeading);
    intent.putExtra(ITEM_NAME, mBinding.tvPdpProductName.getText().toString());
    intent.putExtra(PRODUCT_IMAGE, mProductDetailsViewModel.mPrimaryImage);
    intent.putExtra(PRODUCT_COLOUR, mProductDetailsViewModel.mColor);
    intent.putExtra(PRODUCT_PRICE, mBinding.tvPdpProductPrice.getText().toString());
    intent.putExtra(OFFER_PRICE, mProductDetailsViewModel.mOfferPrice);
    startActivity(intent);*/
  }

  /**
   * start activity for review product activity
   */
  private void startRateProductAct() {
    Intent intent = new Intent(this, ReviewProductActivity.class);
    intent.putExtra(PRODUCT_IMAGE, mProductDetailsViewModel.mPrimaryImage);
    intent.putExtra(PRODUCT_COLOUR, mProductDetailsViewModel.mColor);
    intent.putExtra(PRODUCT_NAME, mBinding.tvPdpProductName.getText().toString());
    intent.putExtra(PRICE, mBinding.tvPdpProductPrice.getText().toString());
    intent.putExtra(PARENT_PRODUCT_ID, mParentProductId);
    intent.putExtra(PRODUCT_ID, mProductId);
    startActivity(intent);
  }

  /**
   * sets the adapter class for variants mData
   */
  private void initializeVariantsAdapter() {
    mPdpVariantsAdapter = new PdpVariantsAdapter(mVariantsData, this);
    mBinding.rvPdpVariants.setAdapter(mPdpVariantsAdapter);
    mBinding.dummyView10.setVisibility(isEmptyArray(mVariantsData) ? GONE : View.VISIBLE);
  }

  /**
   * open the allReviewsActivity
   */
  private void startAllReviewsAct(Integer penCount) {
   /* Intent intent = new Intent(this, AllReviewsActivity.class);
    intent.putExtra(RATING_DATA, mUserRatingData);
    intent.putExtra(PARENT_PRODUCT_ID, mParentProductId);
    intent.putExtra(RATING, mBinding.tvPdpCurrentRateVal.getText().toString());
    intent.putExtra(TOTALRATING_AND_REVIEWS,
        mBinding.tvPdpRatingsReviewDetails.getText().toString());
    intent.putExtra(PEN_COUNT, penCount);
    startActivityForResult(intent, ALL_REVIEWS_CODE);*/
  }

  /**
   * start the review image activity
   *
   * @param pos       position of the review
   * @param reviewPos in that review position position of the review image.
   */
  private void startPreviewImgActivity(int pos, int reviewPos) {
    mUserReviewData = mUserReviewDataArrayList.get(reviewPos);
    this.mReviewPos = reviewPos;
    Intent intent = new Intent(this, PreviewImageActivity.class);
    intent.putExtra(POSITION, pos);
    intent.putExtra(USER_REVIEW_DATA, mUserReviewData);
    startActivityForResult(intent, ALL_PREVIEW_CODE);
  }

  /**
   * sets the adapter class for review mData
   */
  private void initializeReviewsAdapter() {
    mReviewsAdapter = new ReviewsAdapter(mUserReviewDataArrayList, this, this, TRUE);
    mBinding.rvPdpProductReviews.setAdapter(mReviewsAdapter);
  }

  /**
   * sets the adapter class rating mData
   */
  private void initializeRatingDistributionAdapter() {
    mRatingDistributionsAdapter = new RatingDistributionsAdapter(mUserRatingData);
    mBinding.rvPdpRatingDistribution.setAdapter(mRatingDistributionsAdapter);
  }

  /**
   * sets the adapter for  user ratings
   */
  private void initializeRatingOnAdapter() {
    mProductRatingOnAdapter = new ProductRatingOnAdapter(mAttributeRatingData);
    mBinding.rvPdpRatingOn.setAdapter(mProductRatingOnAdapter);
  }

  /**
   * sets the adapter class for review images
   */
  private void initializeProductReviewImagesAdapter() {
    mProductReviewImagesAdapter = new ProductReviewImagesAdapter(mReviewImages);
    mBinding.rvPdpProductReviewsImages.setAdapter(mProductReviewImagesAdapter);
  }

  /**
   * Initialising the View using Data Binding
   */
  private void initializeView() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_product_details);
    mProductDetailsViewModel = ViewModelProviders.of(this, mViewModelFactory).get(
        ProductDetailsViewModel.class);
    DisplayMetrics displayMetrics = new DisplayMetrics();
    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    mBinding.vPPdpProductImages.requestFocus();
    mProductDetailsViewModel.progressVisible.set(TRUE);
    mBinding.vPPdpProductImages.getLayoutParams().height = (int) (displayMetrics.widthPixels*(0.75));
    initializeDefaultStore();
  }

  /**
   * initialize default store
   */
  private void initializeDefaultStore() {
    mBinding.tvPharmacyProductDetails.setVisibility(GONE);
    mBinding.rvPharmacyProductDetailsInfo.setVisibility(GONE);
    mBinding.dummyViewPharmacyProductDetails.setVisibility(GONE);
    mBinding.tvPharmacySubstitutesHeading.setVisibility(GONE);
    mBinding.tvPharmacySubstitutesViewAll.setVisibility(GONE);
    mBinding.rvPharmacySubstitutesInfo.setVisibility(GONE);
    mBinding.dummyViewPharmacySubstitutesDetails.setVisibility(GONE);
    mBinding.tvDisclaimerHeading.setVisibility(GONE);
    mBinding.tvDisclaimerInfo.setVisibility(GONE);
    mBinding.tvPharmacyMedQuantityInfo.setVisibility(GONE);
    mBinding.tvPharmacyMfgTagText.setVisibility(GONE);
    mBinding.tvPdpPharmacyBrandName.setVisibility(GONE);
    mBinding.dummyViewDisclaimer.setVisibility(GONE);
    mBinding.include.ivLike.setVisibility(View.VISIBLE);
    mBinding.include.ivAddToList.setVisibility(View.GONE);
  }

  /**
   * <p>This method is used initialize the viewModel class for this activity.</p>
   */
  private void initializeViewModel() {
    mBinding.setViewmodel(mProductDetailsViewModel);
    mProductDetailsViewModel.getDeviceDetails(Utilities.getIpAddress(this));
    // EcomUtil.generateDeepLink(getIntent(), this);
    setupNestedScrollView();
    mProductDetailsViewModel.onCartUpdate();
    mProductDetailsViewModel.progress.set(TRUE);
  }

  /**
   * subscribe to HTML attributes response
   */
  private void subscribeHtmlAttributes() {
  }

  /**
   * subscribe to substitute attributes response
   */
  private void subscribeSubstituteAttributes() {

  }

  /**
   * viewpager to show the products
   */
  private void initializeViewPager() {
    mProductsViewPagerAdapter = new ProductsViewPagerAdapter(this, mMobileImages);
    mBinding.vPPdpProductImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
      }

      @Override
      public void onPageSelected(int position) {
        Utilities.setViewPagerSelectedDot(mBinding.vPPdpProductImages.getContext(), mDots,
            mDotsCount, position);
      }

      @Override
      public void onPageScrollStateChanged(int state) {
      }
    });
  }

  /**
   * this will show the out of stock report dialog
   */
  private void showOutOfStockReportDialog(String userEmail) {
    CustomDialogUtilBuilder.CustomDialogBuilder customDialogUtilBuilder =
        new CustomDialogUtilBuilder.CustomDialogBuilder(this, this, THREE);
    customDialogUtilBuilder.setMailId(userEmail);
    customDialogUtilBuilder.buildCustomDialog();
  }

  /**
   * initialize the highlights adapter.
   */
  private void initializeHighLightsAdapter() {
    mHighLightsAdapter = new HighLightsAdapter(mHighLightsList);
    mBinding.rvPdpHighlights.setAdapter(mHighLightsAdapter);
  }

  /**
   * initializes the specifications adapter.
   */
  private void initializeSpecificationsAdapter() {
    mSpecificationsAdapter = new SpecificationsAdapter(mSpecialitiesList, TRUE);
    mBinding.rvPdpSpecifications.setAdapter(mSpecificationsAdapter);
  }

  /**
   * set the dot count for selected item in the viewpager..
   */
  private void setUiPageViewController() {
    mBinding.llPdpProductImagePosition.removeAllViews();
    mDotsCount = mProductsViewPagerAdapter.getCount();
    mDots = new ImageView[mDotsCount];
    if (mDotsCount > ZERO) {
      Utilities.setViewPagerDots(this, mDots, mDotsCount, mBinding.llPdpProductImagePosition);
    }
  }

  /**
   * it shows the mError message
   *
   * @param error mError message.
   */
  public void onError(String error) {
    Snackbar.make(mBinding.clPdp, error, Snackbar.LENGTH_SHORT).show();
  }

  /**
   * subscribe for the back button on click
   */
  private void subscribeBackButton() {
    mProductDetailsViewModel.onClick().observe(this, result -> {
      switch (result) {
        case CROSS_ICON:
          finish();
          break;
        case OPEN_LOGIN:
          startBoardingAct();
          break;
        case SIZE_CHART:
          startWebViewAct();
          break;
        case SELLER_INFORMATION:
          break;
        case GOTO_CART:
          startGoToCartAct();
          break;
        case RATING_AND_REVIEWS:
          mBinding.nsPdp.smoothScrollTo(ZERO, (int) mBinding.tvPdpRateOption.getY() - FIFTY);
          break;
        case MORE:
          moreIndex += ONE;
          setLinesCount(mDescription, TRUE);
          break;
        case LOGIN:
          startBoardingAct();
          break;
        case OPEN_SHEET:
          onVariantClick(mVariantsData, mUnitId);
          break;
      }
    });
  }

  /*subscribes the live data*/
  public void subscribeLiveData() {
    mProductDetailsViewModel.getLiveData().observe(this, new Observer<Pair<String, Object>>() {
      @Override
      public void onChanged(Pair<String, Object> integerObjectPair) {
        if (integerObjectPair.first != null && integerObjectPair.second != null) {
          switch (integerObjectPair.first) {
            case REVIEW:
              openSellerBottomSheet((ArrayList<ReviewParameterData>) integerObjectPair.second);
              break;
            case REVIEW_CLICKED:
              startAllReviewsAct((Integer) integerObjectPair.second);
              break;
            case ALL_DETAILS_CLICKED:
              startAllDetailsAct((String) integerObjectPair.second);
              break;
            case RATING_CLICKED:
              if ((Boolean) integerObjectPair.second) {
                if (!AppController.getInstance().isGuest()) {
                  startRateProductAct();
                } else {
                  startBoardingAct();
                }
              }
              break;
            case LIKE_DISLIKE:
              UserReviewData userReviewData = mUserReviewDataArrayList.get(mPosition);
              if ((boolean) integerObjectPair.second) {
                userReviewData.setLikes(userReviewData.getLikes() + ONE);
                if (userReviewData.getDisLikeSel()) {
                  userReviewData.setDisLikes(userReviewData.getDisLikes() - ONE);
                  userReviewData.setDisLikeSel(FALSE);
                }
                userReviewData.setLikeSel(TRUE);
              } else {
                userReviewData.setDisLikes(userReviewData.getDisLikes() + ONE);
                if (userReviewData.getLikeSel()) {
                  userReviewData.setLikes(userReviewData.getLikes() - ONE);
                  userReviewData.setLikeSel(FALSE);
                }
                userReviewData.setDisLikeSel(TRUE);
              }
              mReviewsAdapter.notifyItemChanged(mPosition, userReviewData);
              break;
            case ERROR_MSG:
              onError((String) integerObjectPair.second);
              break;
            case VIEW_PAGER_IMAGES:
              mBinding.vPPdpProductImages.setAdapter(null);
              mMobileImages.clear();
              mMobileImages.addAll((ArrayList<ImageData>) integerObjectPair.second);
              mBinding.vPPdpProductImages.setAdapter(mProductsViewPagerAdapter);
              mProductsViewPagerAdapter.notifyDataSetChanged();
              setUiPageViewController();
              break;
            case REVIEW_IMAGES:
              mReviewImages.clear();
              mReviewImages.addAll((ArrayList<String>) integerObjectPair.second);
              mProductReviewImagesAdapter.notifyDataSetChanged();
              break;
            case REVIEW_DATA:
              mUserReviewDataArrayList.clear();
              mUserReviewDataArrayList.addAll((ArrayList<UserReviewData>) integerObjectPair.second);
              mReviewsAdapter.notifyDataSetChanged();
              break;
            case GET_RATINGS:
              mUserRatingData.clear();
              mUserRatingData.addAll((ArrayList<Rating>) integerObjectPair.second);
              mRatingDistributionsAdapter.notifyDataSetChanged();
              break;
            case GET_HILIGHTS:
              mHighLightsList.clear();
              mHighLightsList.addAll((ArrayList<String>) integerObjectPair.second);
              mHighLightsAdapter.notifyDataSetChanged();
              break;
            case GET_ATTRIBUTES:
              mSpecialitiesList.clear();
              mAttributesData.clear();
              mAttributesData.addAll((ArrayList<AttributesData>) integerObjectPair.second);
              if (mAttributesData != null && mAttributesData.size() > ZERO) {
                mSpecialitiesList.addAll(mAttributesData.get(ZERO).getInnerAttributes());
                mSpecificationsAdapter.notifyDataSetChanged();
              }
              break;
            case GET_PRESCRIPTION_REQUIRED:
              mBinding.tvPrescriptionText.setVisibility(((Boolean) integerObjectPair.second)
                  ? View.VISIBLE : GONE);
              break;
            case GET_ATTRIBUTES_RATING:
              mAttributeRatingData.clear();
              mAttributeRatingData.addAll(
                  (ArrayList<AttributeRatingData>) integerObjectPair.second);
              mProductRatingOnAdapter.notifyDataSetChanged();
              break;
            case GET_VARIENT_DATA:
              if (integerObjectPair.second != null) {
                mVariantsData.clear();
                mVariantsData.addAll((ArrayList<VariantsData>) integerObjectPair.second);
                mPdpVariantsAdapter.notifyDataSetChanged();
              }
              break;
            case GET_OFFER_DATA:
              if (integerObjectPair.second != null) {
                mOffer = (PdpOfferData) integerObjectPair.second;
              }
              break;
            case OUT_OF_STOCK_CLICKED:
              showOutOfStockReportDialog(
                  (Boolean) integerObjectPair.second ? manager.getEmail() : "");
              break;
            case GET_SELLERS:
              mSupplierArrayList.clear();
              ArrayList<SupplierData> supplierData = new ArrayList<>(
                  (ArrayList<SupplierData>) integerObjectPair.second);
              Collections.sort(supplierData,
                  (finalList, temp) -> temp.getFinalPriceList().getFinalPrice().compareTo(
                      finalList.getFinalPriceList().getFinalPrice()));
              Collections.reverse(supplierData);
              mSupplierArrayList.addAll(supplierData);
              Utilities.printLog("exe" + "mSupplierArrayList" + mSupplierArrayList.size());
              mViewMoreSellersAdapter.notifyDataSetChanged();
              mBinding.rvViewSellersDetails.setVisibility(
                  mSupplierArrayList.size() > ONE ? View.VISIBLE : GONE);
              break;
            case GET_DESCRIPTION:
              mDescription = (String) integerObjectPair.second;
              if (mDescription != null && !mDescription.isEmpty()) {
                mBinding.tvPdpProductDescription.setText(mDescription);
                Utilities.printLog("exe" + "description" + mDescription);
                mBinding.tvPdpProductDescription.post(() -> {
                  // Use lineCount here
                  mLineCount = mBinding.tvPdpProductDescription.getLineCount();
                  Utilities.printLog("exe" + "lineCount" + mLineCount);
                  setLinesCount(mDescription, FALSE);
                  if (mLineCount <= FOUR) {
                    mBinding.tvPdpProductDesMore.setVisibility(View.GONE);
                  }
                });
              } else {
                mBinding.tvPdpProductDescription.setVisibility(GONE);
                mBinding.tvPdpProductDesMore.setVisibility(GONE);
                mBinding.tvPdpDescriptionTxt.setVisibility(GONE);
              }
              break;
            case ALL_OFFERS:
              mAllOffersArrayList.clear();
              mAllOffersArrayList.addAll((ArrayList<PdpOfferData>) integerObjectPair.second);
              mAllOffersAdapter.notifyDataSetChanged();
              break;
          }
        }
      }
    });
  }

  /**
   * subscribes to variant data
   */
  private void subscribeVariantData() {
    mProductDetailsViewModel.getVariants().observe(this, integerObjectHashMap -> {
      mUnitId = (String) integerObjectHashMap.get(ONE);
      mStoreId = (String) integerObjectHashMap.get(TWO);
      ArrayList<VariantsData> variantsData = (ArrayList<VariantsData>) integerObjectHashMap.get(
          THREE);
      mOffersListData = (PdpOfferData) integerObjectHashMap.get(FOUR);
      mAvailableCount = (Integer) integerObjectHashMap.get(FIVE);
      // mCartCount=(Integer)integerObjectHashMap.get(SIX);
      mAvailableCount = (Integer) integerObjectHashMap.get(FIVE);
      if (variantsData != null) {
        mVariantsData.clear();
        mVariantsData.addAll(variantsData);
        mPdpVariantsAdapter.notifyDataSetChanged();
      }
    });
    mProductDetailsViewModel.getIsShoppingList().observe(this, aBoolean -> {
      // mBinding.include.ivAddToList.setSelected(aBoolean);
    });
  }

  /**
   * send the mData to the viewModel to set the mData.
   *
   * @param responseValues response for the pdp.
   */
  public void getProductData(PdpUseCase.ResponseValues responseValues, String productId) {
    mProductDetailsViewModel.mProductId = productId;
    mProductDetailsViewModel.parsePdpResponseValues(responseValues);
  }

  /**
   * this method is used to show the add to cart button status
   *
   * @param isAddToCart used to show the addTocart button status
   */
  public void addToCartStatus(boolean isAddToCart) {
    mProductDetailsViewModel.addToCartTxt.set(
        isAddToCart ? AppController.getInstance().getString(R.string.pdpAddGoCart)
            : AppController.getInstance().getString(R.string.pdpAddToCart));
  }

  /**
   * this method is used to set the total cart count from bottom sheet fragment
   */
  public void setCartCount(String productId) {
    mProductDetailsViewModel.setCartStatus(productId);
  }

  @Override
  public void onVariantClick(ArrayList<VariantsData> variantsData, String unitId) {
    if (variantsData != null && variantsData.size() >= ONE) {
      if (!mAttributesBottomSheetFragment.isAdded()) {
        mAttributesBottomSheetFragment.setItemData(mBinding.tvPdpProductPrice.getText().toString(),
            mProductDetailsViewModel.mOfferPrice,
            mBinding.tvPdpProductName.getText().toString(),
            variantsData, mProductId, mParentProductId, unitId, mStoreId, mAvailableCount,
            mProductDetailsViewModel.productOutOfStock.get(), mCartCount, mOffersListData, mOffer);
        mAttributesBottomSheetFragment.show(getSupportFragmentManager(),
            ATTRIBUTES_BOTTOM_SHEET_TAG);
      }
    }
  }

  /**
   * this method  will open the seller bottom sheet
   */
  private void openSellerBottomSheet(ArrayList<ReviewParameterData> reviewParameterData) {
    if (!mSellerBottomSheetFragment.isAdded()) {
      mSellerBottomSheetFragment.setItemData(mBinding.tvPdpSellerName.getText().toString(),
          mProductDetailsViewModel.getSellerSince(),
          reviewParameterData);
      mSellerBottomSheetFragment.show(getSupportFragmentManager(),
          SELLER_BOTTOM_SHEET_TAG);
    }
  }

  /**
   * subscribe to short link
   */
  private void subscribeShortLink() {
    mProductDetailsViewModel.getShortLink().observe(this, shortLink -> {
      Intent intent = new Intent(Intent.ACTION_SEND);
      intent.setType(TEXT_PLAIN);
      intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
      intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
      startActivity(intent);
    });
  }

  /**
   * this method  will open the seller bottom sheet
   */
  private void openTermsAndConBottomSheet(String termsAndConditions) {
    if (!mSellerBottomSheetFragment.isAdded()) {
      mSellerBottomSheetFragment.setTermsAndConditions(termsAndConditions);
      mSellerBottomSheetFragment.show(getSupportFragmentManager(),
          SELLER_BOTTOM_SHEET_TAG);
    }
  }

  /**
   * start the web view activity
   */
  private void startWebViewAct() {
    Intent intent = new Intent(this, WebActivity.class);
    intent.putExtra(URL, BuildConfig.SIZE_CHART_URL + mParentProductId);
    intent.putExtra(TITLE, getResources().getString(R.string.pdpSizeChart));
    startActivity(intent);
  }

  /**
   * listener for menu item click for review abort.
   *
   * @param item menu item references
   * @return returns boolean
   */
  @Override
  public boolean onMenuItemClick(MenuItem item) {
    if (item.getItemId() == R.id.flagAsAbusive) {
      if (!AppController.getInstance().isGuest()) {
        mProductDetailsViewModel.callReportReviewApi(mReviewId);
      } else {
        startBoardingAct();
      }
      return true;
    }
    return false;
  }

  /**
   * menu item click call back
   */
  @Override
  public void onMenuClick(int likeOrMenu, int pos, AppCompatImageView optionMenu,
      boolean likeOrDislike) {
    if (AppController.getInstance().isGuest()) {
      startBoardingAct();
      return;
    }
    UserReviewData reviewData = mUserReviewDataArrayList.get(pos);
    this.mReviewId = reviewData.getReviewId();
    this.mPosition = pos;
    switch (likeOrMenu) {
      case ONE:
        PopupMenu popup = new PopupMenu(this, optionMenu, Gravity.TOP);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.item_reviews_menu);
        popup.show();
        break;
      case TWO:
        mProductDetailsViewModel.callLikeOrDislikeApi(mReviewId, likeOrDislike);
        break;
    }
  }

  /**
   * listens when we click on the review image
   */
  @Override
  public void onImgClick(int pos, int reviewPos) {
    startPreviewImgActivity(pos, reviewPos);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case REQUEST_CODE:
        if (data != null) {
          boolean isAdded = data.getBooleanExtra(SHOPPING_STATUS, false);
          shoppingStatus = isAdded;
          mProductDetailsViewModel.setShippingStatus(isAdded);
        }
        break;
      case ALL_REVIEWS_CODE:
        if (data != null && resultCode == RESULT_OK) {
          ArrayList<UserReviewData> userReviewData = data.getParcelableArrayListExtra(
              USER_REVIEW_DATA);
          mUserReviewDataArrayList.clear();
          mUserReviewDataArrayList.addAll(userReviewData);
          mReviewsAdapter.notifyDataSetChanged();
        }
        break;
      case ALL_PREVIEW_CODE:
        if (data != null && resultCode == RESULT_OK) {
          mUserReviewData = data.getParcelableExtra(USER_REVIEW_DATA);
          mUserReviewDataArrayList.remove(mReviewPos);
          mUserReviewDataArrayList.add(mReviewPos, mUserReviewData);
          mReviewsAdapter.notifyItemChanged(mReviewPos);
        }
    }
  }

  /**
   * set up the nested scroll view listener
   */
  private void setupNestedScrollView() {
    mBinding.include.tvPdpBrandName.setAlpha(POINT_ZERO);
    mBinding.nsPdp.setOnScrollChangeListener(
        (NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
          if (scrollY < ONE_FIFTY) {
            mBinding.include.tvPdpBrandName.setAlpha(POINT_ZERO);
          } else if (scrollY > ONE_FIFTY && scrollY < TWO_FIFTY) {
            mBinding.include.tvPdpBrandName.setAlpha(POINT_THREE);
          } else if (scrollY > TWO_FIFTY && scrollY < THREE_FIFTY) {
            mBinding.include.tvPdpBrandName.setAlpha(POINT_SIX);
          } else if (scrollY > THREE_FIFTY && scrollY < FIVE_HUNDRED) {
            mBinding.include.tvPdpBrandName.setAlpha(POINT_EIGHT);
          } else {
              if (!isSimilarItemsCalled)
               // mProductDetailsViewModel.callSimilarProductsApi(mParentProductId, FIVE);
              isSimilarItemsCalled = true;
              mBinding.include.tvPdpBrandName.setAlpha(ONE);
            }
        });
  }

  @Override
  public void onNotifyMail(String mail) {
    if (!mail.isEmpty()) {
      Utilities.printLog("eMail" + mail);
      mProductDetailsViewModel.callNotifyProductApi(mail, mParentProductId);
    } else {
      onError(getResources().getString(R.string.pdpPleaseEnterMail));
    }
  }

  /**
   * it will open the cart activity
   */
  private void startGoToCartAct() {
    if (!AppController.getInstance().isGuest()) {
      Intent intent = new Intent(this, EcomCartActivity.class);
      startActivity(intent);
    } else {
      AppController.getInstance().openSignInDialog(this);
    }
  }

  @Override
  public void onOfferItemClick(String value) {
    if (value != null && !value.isEmpty()) {
      openTermsAndConBottomSheet(value);
    }
  }

  @Override
  public void onSuccess(double latitude, double longitude) {
    mLat = latitude;
    mLan = longitude;
    Location location = new Location("currentLocation");
    location.setLatitude(mLat);
    location.setLongitude(mLan);
    manager.setCurrentLocation(location);
    mProductDetailsViewModel.callProductDetailsApi(mProductId,
        mParentProductId, mLat, mLan);
  }

  /**
   * get the count of lines
   */
  private void setLinesCount(String description, boolean isClicked) {
    mBinding.tvPdpProductDescription.setMaxLines(!isClicked ? (FOUR * moreIndex) : mLineCount);
    mBinding.tvPdpProductDescription.setEllipsize(
        mLineCount > FOUR * moreIndex ? TextUtils.TruncateAt.END : null);
    mBinding.tvPdpProductDesMore.setVisibility(
        (description != null && !description.isEmpty()) ? View.VISIBLE : View.GONE);
    if (isClicked && description != null && !description.isEmpty()) {
      mBinding.tvPdpProductDesMore.setText(EMPTY_STRING);
    }
  }

  @Override
  public void onClick(int pos) {
    SupplierData supplierData = mSupplierArrayList.get(pos);
    if (supplierData != null) {
      mProductDetailsViewModel.callProductDetailsApi(supplierData.getSupplier().getProductId(),
          mParentProductId, mLat, mLan);
    }
  }

 /* @Override
  public void onAttributesClicked(InnerAttributesData data) {
  }*/

/*  @Override
  public void onSubstitutesClicked(DataItems item) {
    mProductDetailsViewModel.callProductDetailsApi(item.getChildProductId(),
        item.getParentProductId(), mLat, mLan);
  }*/

  @Override
  public void onItemAdded(String parentProductId, String productId, String unitId, String storeId,
      int action,
      int newQuantity, int position) {
    mProductDetailsViewModel.suggestedParentProductId = parentProductId;
    mProductDetailsViewModel.suggestedProductId = productId;
    mProductDetailsViewModel.suggestedUnitId = unitId;
    mProductDetailsViewModel.suggestedStoreId = storeId;
    mProductDetailsViewModel.callUpdateCartApi(action, newQuantity, TRUE);
  }

  @Override
  public void onItemNotified(String parentProductId, String productId, String unitId) {
    mProductDetailsViewModel.suggestedParentProductId = parentProductId;
    mProductDetailsViewModel.suggestedProductId = productId;
    String email = !AppController.getInstance().isGuest() ? manager.getEmail() : "";
    CustomDialogUtilBuilder.CustomDialogBuilder customDialogUtilBuilder =
        new CustomDialogUtilBuilder.CustomDialogBuilder(this, this, THREE);
    customDialogUtilBuilder.setMailId(
        TextUtils.isEmpty(email) ? "" : manager.getEmail());
    customDialogUtilBuilder.buildCustomDialog();
  }
}