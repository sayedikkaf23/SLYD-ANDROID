package chat.hola.com.app.ecom.pdp.attributebottomsheet;

import static chat.hola.com.app.Utilities.Constants.BOTTOM_SHEET_LOAD_DELAY;
import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.THREE;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.LockableBottomSheetBehavior;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.ecom.cart.EcomCartActivity;
import chat.hola.com.app.ecom.pdp.ProductDetailsActivity;
import chat.hola.com.app.ui.dialog.CustomDialogUtilBuilder;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemAttributesBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kotlintestgradle.interactor.ecom.CartHandler;
import com.kotlintestgradle.model.ecom.pdp.PdpOfferData;
import com.kotlintestgradle.model.ecom.pdp.VariantsData;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import javax.inject.Inject;

/**
 * bottom sheet fragment for different attributes for the product.
 */
public class AttributesBottomSheetFragment
    extends BottomSheetDialogFragment implements QuantityItemClick, ProductAttributesClick,
    CustomDialogUtilBuilder.DialogOutOfStockNotifyListener {
  @Inject
  ViewModelProvider.Factory mViewModelFactory;
  @Inject
  CartHandler mCartHandler;
  private ListPopupWindow mPopupWindow;
  private ItemAttributesBottomSheetBinding mBinding;
  private AttributesBottomSheetViewModel mAttributesBottomSheetViewModel;
  private String mProductPrice, mProductName, mOfferPrice, mUnitId, mStoreId;
  private ArrayList<VariantsData> mVariantsData = new ArrayList<>();
  private VariantsAdapter mVariantsAdapter;
  private String mParentProductId, mProductId;
  private int mAvailableCount;
  private PdpOfferData mOffers;
  private boolean mIsOutOfStock;
  private PdpOfferData mOffersListData;
  private int mCartCount;
  private BottomSheetBehavior mBehavior;
  private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
      new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
          switch (newState) {
            case BottomSheetBehavior.STATE_COLLAPSED:
              break;
            case BottomSheetBehavior.STATE_SETTLING:
              break;
            case BottomSheetBehavior.STATE_EXPANDED:
              break;
            case STATE_HIDDEN:
              dismiss();
              break;
            case BottomSheetBehavior.STATE_DRAGGING:
              mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
              break;
          }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
      };

  /**
   * constructor for the attributes.
   */
  @Inject
  public AttributesBottomSheetFragment() {
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeViewModel();
    subscribeForClicked();
    subscribeVariantData();
    subscribeOnError();
    subscribeCartCount();
    subscribeProductDetailData();
  }

  /**
   * subscribes to variant mData
   */
  private void subscribeVariantData() {
    mAttributesBottomSheetViewModel.onGetVariants().observe(this,
        variantsData -> {
          if (variantsData != null) {
            mVariantsData.clear();
            mVariantsData.addAll(variantsData);
            mVariantsAdapter.notifyDataSetChanged();
            setVariantsData(mVariantsData);
          }
        });
  }

  /**
   * initialization for the view mData
   */
  private void initializeViewModel() {
    mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
        R.layout.item_attributes_bottom_sheet, null, false);
    mAttributesBottomSheetViewModel = ViewModelProviders.of(this, mViewModelFactory).get(
        AttributesBottomSheetViewModel.class);
    mBinding.setViewmodel(mAttributesBottomSheetViewModel);
    mAttributesBottomSheetViewModel.offer = mOffers;
  }

  @Override
  public void onResume() {
    super.onResume();
    setHeight();
  }

  /**
   * setting the height for the bottom sheet
   */
  private void setHeight() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    requireActivity().getWindowManager().getDefaultDisplay().getMetrics(
        displayMetrics);
    new Handler().postDelayed(() -> mBehavior.setPeekHeight(
        mBinding.clBottomSheet.getHeight()), BOTTOM_SHEET_LOAD_DELAY);
  }

  @SuppressLint("RestrictedApi")
  @Override
  public void setupDialog(@NonNull Dialog dialog, int style) {
    super.setupDialog(dialog, style);
    dialog.setContentView(mBinding.getRoot());
    FrameLayout bottomSheet = Objects.requireNonNull(dialog.getWindow()).findViewById(
        R.id.design_bottom_sheet);
    bottomSheet.setBackgroundResource(R.drawable.top_radius_white_bag);
    initViews(mBinding.getRoot());
  }

  /**
   * initialization of the views
   *
   * @param rootView view object
   */
  private void initViews(View rootView) {
    CoordinatorLayout.LayoutParams params =
        (CoordinatorLayout.LayoutParams) ((View) rootView.getParent()).getLayoutParams();
    mBehavior = new LockableBottomSheetBehavior();
    params.setBehavior(mBehavior);
    ((View) rootView.getParent()).setLayoutParams(params);
    if (mBehavior != null) {
      mBehavior.addBottomSheetCallback(mBottomSheetBehaviorCallback);
    }
    mAttributesBottomSheetViewModel.productPrice.set(mProductPrice);
    mAttributesBottomSheetViewModel.productName.set(mProductName);
    if (mOfferPrice != null && !mOfferPrice.isEmpty()) {
      mAttributesBottomSheetViewModel.offerViews.set(TRUE);
      mAttributesBottomSheetViewModel.productActualPrice.set(mOfferPrice);
    }
    mVariantsAdapter = new VariantsAdapter(mVariantsData, this);
    mBinding.rvPdpVariantItems.setAdapter(mVariantsAdapter);
    setVariantsData(mVariantsData);
    String[] quantities = getResources().getStringArray(R.array.product_quantiy);
    ArrayList<String> productQuantity = new ArrayList<>(Arrays.asList(quantities));
    PiecesPopUpAdapter piecesPopUpAdapter = new PiecesPopUpAdapter(productQuantity, this);
    mPopupWindow = new ListPopupWindow(requireActivity());
    mPopupWindow.setAdapter(piecesPopUpAdapter);
    mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    mPopupWindow.setDropDownGravity(Gravity.BOTTOM);
    mPopupWindow.setOverlapAnchor(FALSE);
    mBinding.tvProductQuantity.setText(quantities[ZERO]);
    mPopupWindow.setAnchorView(mBinding.tvProductQuantity);
    mAttributesBottomSheetViewModel.getDeviceDetails(Utilities.getDeviceId(getActivity()),
        Utilities.getIpAddress(getActivity()));
    mAttributesBottomSheetViewModel.getInputData(mStoreId, mUnitId, mProductId, mParentProductId,
        mAvailableCount, mIsOutOfStock, mCartCount,
        mOffersListData);
  }

  /**
   * setting the primary image primary color name
   */
  private void setVariantsData(ArrayList<VariantsData> variantsData) {
    if (variantsData.size() > ZERO) {
      for (int i = 0; i < variantsData.size(); i++) {
        boolean isValid =  (variantsData.get(i).getName().equals(getResources().getString(R.string.pdpColors)));
        if (isValid) {
          if (variantsData.get(i).getSizeData() != null && variantsData.get(i).getSizeData().size()
              > ZERO) {
            for (int j = 0; j < variantsData.get(i).getSizeData().size(); j++) {
              if (variantsData.get(i).getSizeData().get(j).getIsPrimary()) {
                mAttributesBottomSheetViewModel.imageUrl.set(variantsData.get(i).getSizeData().get(
                    j).getImage());
                this.mProductId = variantsData.get(i).getSizeData().get(j).getChildProductId();
                new Thread(() -> mAttributesBottomSheetViewModel.addToCartTxt.set(
                    (mCartHandler.isProductExistInCart(mProductId))
                        ? AppController.getInstance().getString(R.string.pdpAddGoCart)
                        : AppController.getInstance().getString(
                            R.string.pdpAddToCart))).start();
                break;
              }
            }
          }
        }
      }
    }
  }

  /**
   * subscribes for the clicked.
   */
  private void subscribeForClicked() {
    mAttributesBottomSheetViewModel.onClick().observe(this, pdpBottomSheetUiAction -> {
      switch (pdpBottomSheetUiAction) {
        case CROSS_ICON:
          if (getDialog() != null) {
            getDialog().dismiss();
          }
          break;
        case QUANTITY:
          if (!mPopupWindow.isShowing()) {
            mPopupWindow.show();
          }
          break;
        case GOTO_CART:
          startGoToCartAct();
          if (getDialog() != null) {
            getDialog().dismiss();
          }
          break;
        case ADD_TO_CART_STATUS:
          if (getActivity() != null) {
            ((ProductDetailsActivity) getActivity()).addToCartStatus(TRUE);
          }
          break;
        case GOTO_CART_STATUS:
          if (getActivity() != null) {
            ((ProductDetailsActivity) getActivity()).addToCartStatus(FALSE);
          }
          break;
        case NOTIFY_ME:
          showOutOfStockReportDialog();
          break;

        case GOTO_LOGIN:
          launchLoginScreen();
        default:
          break;
      }
    });
  }

  /**
   * launch the login screen
   */
  private void launchLoginScreen() {
    if (AppController.getInstance().isGuest()) {
      AppController.getInstance().openSignInDialog(this.getContext());
    }
  }

  /**
   * this method is used to set the total cart count from bottom sheet fragment
   */
  private void setCartCount() {
    ((ProductDetailsActivity) requireActivity()).setCartCount(mProductId);
  }

  /**
   * this will show the out of stock report dialog
   */
  private void showOutOfStockReportDialog() {
    CustomDialogUtilBuilder.CustomDialogBuilder customDialogUtilBuilder =
        new CustomDialogUtilBuilder.CustomDialogBuilder(getActivity(), this, THREE);
    customDialogUtilBuilder.buildCustomDialog();
  }

  /**
   * it will open the cart activity
   */
  private void startGoToCartAct() {
    Intent intent = new Intent(getActivity(), EcomCartActivity.class);
    startActivity(intent);
  }

  /**
   * subscribe for onError
   */
  private void subscribeOnError() {
    mAttributesBottomSheetViewModel.onError().observe(this, this::onError);
  }

  /**
   * subscribe for cartCount
   */
  private void subscribeCartCount() {
    mAttributesBottomSheetViewModel.onCartCount().observe(this, new Observer<Boolean>() {
      @Override
      public void onChanged(Boolean value) {
        setCartCount();
      }
    });
  }

  /**
   * it will show snack bar message
   *
   * @param error string mError message
   */
  public void onError(String error) {
    Log.d("exe", "error" + error);
    Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
  }

  /**
   * set the height for the bottom sheet
   */
  public void setData() {
    setHeight();
  }

  /**
   * set the mData from pdp  activity
   *
   * @param productPrice    product price
   * @param productName     product name
   * @param variantsData    variants dat
   * @param parentProductId parent product id
   */
  public void setItemData(String productPrice, String offerPrice, String productName,
      ArrayList<VariantsData> variantsData, String productId, String parentProductId, String unitId,
      String storeId, int availableCount, boolean isOutOfStock, int cartCount,
      PdpOfferData offersListData, PdpOfferData offers) {
    this.mProductPrice = productPrice;
    this.mProductName = productName;
    this.mOfferPrice = offerPrice;
    this.mProductId = productId;
    this.mParentProductId = parentProductId;
    this.mUnitId = unitId;
    this.mStoreId = storeId;
    this.mAvailableCount = availableCount;
    this.mIsOutOfStock = isOutOfStock;
    this.mCartCount = cartCount;
    this.mOffersListData = offersListData;
    this.mOffers = offers;
    if (variantsData != null) {
      this.mVariantsData.clear();
      this.mVariantsData.addAll(variantsData);
    }
  }

  /**
   * listens when quantity pop up window clicked
   *
   * @param value string value
   */
  @Override
  public void onItemClick(String value) {
    if (mPopupWindow.isShowing()) {
      mPopupWindow.dismiss();
    }
    mBinding.tvProductQuantity.setText(value);
  }

  /**
   * listens when product variant  clicked
   *
   * @param productId product id
   */
  @Override
  public void onClick(String productId) {
    mAttributesBottomSheetViewModel.callProductDetailsApi(productId, mParentProductId);
  }

  /**
   * subscribes to product details response value
   */
  private void subscribeProductDetailData() {
    mAttributesBottomSheetViewModel.onGetResponseValues().observe(this,
        responseValues -> {
          if (responseValues != null) {
            if (getActivity() != null) {
              ((ProductDetailsActivity) getActivity()).getProductData(responseValues,
                      mAttributesBottomSheetViewModel.mProductId);
            }
          }
        });
  }

  @Override
  public void onNotifyMail(String mail) {
    if (!mail.isEmpty()) {
      Utilities.printLog("eMail" + mail);
      mAttributesBottomSheetViewModel.callNotifyProductApi(mail, mParentProductId);
    } else {
      onError(getResources().getString(R.string.pdpPleaseEnterMail));
    }
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    Log.d("exe", "onDestroy: bottom sheet ");
  }
}