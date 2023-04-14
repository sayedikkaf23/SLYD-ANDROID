package chat.hola.com.app.ecom.wishlist.sort;

import static chat.hola.com.app.Utilities.Constants.BOTTOM_SHEET_LOAD_DELAY;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_HIDDEN;
import static com.kotlintestgradle.remote.RemoteConstants.CATEGORY_NAME;
import static com.kotlintestgradle.remote.RemoteConstants.SEARCH_QUERY;
import static com.kotlintestgradle.remote.RemoteConstants.SUB_CATEGORY_NAME;
import static com.kotlintestgradle.remote.RemoteConstants.SUB_SUB_CATEGORY_NAME;

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
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.ecom.wishlist.WishListActivity;
import chat.hola.com.app.ecom.wishlist.WishListViewModel;
import com.ezcall.android.R;
import com.ezcall.android.databinding.FragmentEcomSortBottomsheetBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.Arrays;
import java.util.Objects;
import javax.inject.Inject;

/*
 * Purpose – This class Holds Ui Product Sorting.
 * @author 3Embed
 * Created on Dec 05, 2019
 * Modified on
 */
public class SortBottomSheet extends BottomSheetDialogFragment implements
    SortTypeListAdapter.OnSortItemClicked {
  @Inject
  ViewModelProvider.Factory mViewModelFactory;
  private FragmentEcomSortBottomsheetBinding mBinding;
  private BottomSheetBehavior mBehavior;
  private SortViewModel mViewModel;
  private String mCatName, mSubCatName, mSearchQuery;
  private String mSubSubCatName = "";
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
            case BottomSheetBehavior.STATE_HALF_EXPANDED:
              break;
          }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
      };

  @Inject
  public SortBottomSheet() {
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeViewModel();
  }

  /**
   * This method is using to initialization of basic resources
   */
  private void initializeViewModel() {
    mBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
        R.layout.fragment_ecom_sort_bottomsheet, null, false);
    mBinding.ivCloseBtn.setOnClickListener(view -> dismiss());
  /*  if (getActivity() instanceof ProductListingActivity) {
      mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()),
          mViewModelFactory).get(
          ProductListViewModel.class);
    } else*/ if (getActivity() instanceof WishListActivity) {
      mViewModel = ViewModelProviders.of(requireActivity(),
          mViewModelFactory).get(
          WishListViewModel.class);
    }
    mViewModel.setDisposableSingleObserver();
    if (getArguments() != null) {
      Bundle bundle = getArguments();
      if (TextUtils.isEmpty(bundle.getString(SEARCH_QUERY))) {
        mCatName = bundle.getString(CATEGORY_NAME);
        mSubCatName = bundle.getString(SUB_CATEGORY_NAME);
        mSubSubCatName = bundle.getString(SUB_SUB_CATEGORY_NAME);
      } else {
        mSearchQuery = bundle.getString(SEARCH_QUERY);
      }
    }
    String[] myResArray = getResources().getStringArray(R.array.sort_types);
    SortTypeListAdapter adapter = new SortTypeListAdapter(Arrays.asList(myResArray), this);
    mBinding.rvSortList.setAdapter(adapter);
  }

  @Override
  public void onResume() {
    super.onResume();
    setHeight();
  }

  /**
   * Set height of the bottomSheet dynamically based mData loaded
   */
  private void setHeight() {
    DisplayMetrics displayMetrics = new DisplayMetrics();
    requireActivity().getWindowManager().getDefaultDisplay().getMetrics(
        displayMetrics);
    new Handler().postDelayed(() -> mBehavior.setPeekHeight(
        mBinding.clSortMain.getHeight()), BOTTOM_SHEET_LOAD_DELAY);
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
   * This method is using to initialization of View parameters
   *
   * @param rootView rootView for get view elements
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
  }

  @Override
  public void onSortItemLClickedListener(int type) {
    if (!TextUtils.isEmpty(mCatName)) {
      Utilities.printLog("exe" + "mCatName" + mCatName);
      mViewModel.getSubCatFilterProduct(mCatName, mSubCatName, mSubSubCatName, type,
          String.valueOf(ONE), "");
    } else if (!TextUtils.isEmpty(mSearchQuery)) {
      mViewModel.getSearchProduct(mSearchQuery, type);
    } else {
      mViewModel.wishLIstSort(type);
    }
    dismiss();
  }
}