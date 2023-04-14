package chat.hola.com.app.ecom.pdp.sellerbottomsheet;

import static chat.hola.com.app.Utilities.Constants.BOTTOM_SHEET_LOAD_DELAY;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import chat.hola.com.app.Utilities.LockableBottomSheetBehavior;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ItemSellerBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.kotlintestgradle.model.ecom.pdp.ReviewParameterData;
import java.util.ArrayList;
import java.util.Objects;
import javax.inject.Inject;

/**
 * shows the dialog which contains the seller rating and review
 */
public class SellerBottomSheetFragment extends BottomSheetDialogFragment {
  @Inject
  ViewModelProvider.Factory mViewModelFactory;
  private ItemSellerBottomSheetBinding mBinding;
  private SellerBottomSheetViewModel mSellerBottomSheetViewModel;
  private BottomSheetBehavior mBehavior;
  private String mSellerName;
  private String mSellerSince;
  private String mTermsAndConditions;
  private ArrayList<ReviewParameterData> mReviewParameterData = new ArrayList<>();
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
  public SellerBottomSheetFragment() {
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeViewModel();
    subscribeForClicked();
  }

  /**
   * initialization for the view mData
   */
  private void initializeViewModel() {
    mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
        R.layout.item_seller_bottom_sheet, null, false);
    mSellerBottomSheetViewModel = ViewModelProviders.of(this, mViewModelFactory).get(
        SellerBottomSheetViewModel.class);
    mBinding.setViewmodel(mSellerBottomSheetViewModel);
  }

  /**
   * subscribes for the cross icon clicked.
   */
  private void subscribeForClicked() {
    mSellerBottomSheetViewModel.onBackIconClick().observe(this, aBoolean -> {
      if (aBoolean) {
        if (getDialog() != null) {
          getDialog().dismiss();
        }
      }
    });
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
    if (mTermsAndConditions!=null&&!mTermsAndConditions.isEmpty()) {
      mBinding.vgSellerInfo.setVisibility(View.GONE);
      mBinding.vgTermsAndConditions.setVisibility(View.VISIBLE);
      mSellerBottomSheetViewModel.termsOrPrivacyData.set(mTermsAndConditions);
      return;
    }
    if (mSellerName != null) {
      mBinding.tvSellerName.setText(mSellerName);
    }
    mBinding.tvSellerSinceVal.setVisibility(
        TextUtils.isEmpty(mSellerSince) ? View.GONE : View.VISIBLE);
    mBinding.tvSelleSinceLabel.setVisibility(
        TextUtils.isEmpty(mSellerSince) ? View.GONE : View.VISIBLE);
    mBinding.tvSellerSinceVal.setText(!TextUtils.isEmpty(mSellerSince) ? mSellerSince : "");
    SellerBottomSheetAdapter sellerBottomSheetAdapter = new SellerBottomSheetAdapter(
        mReviewParameterData);
    mBinding.rvSellerRating.setAdapter(sellerBottomSheetAdapter);
  }

  /**
   * set the mData from pdp  activity
   *
   * @param sellerName seller name
   */
  public void setItemData(String sellerName, String sellerSince,
      ArrayList<ReviewParameterData> reviewParameterData) {
    this.mSellerName = sellerName;
    this.mSellerSince = sellerSince;
    if (mReviewParameterData != null) {
      this.mReviewParameterData.clear();
      this.mReviewParameterData.addAll(reviewParameterData);
    }
  }
  /**
   * set the mData from pdp  activity
   */
  public void setTermsAndConditions(String termsConditions) {
    mTermsAndConditions = termsConditions;
  }
}