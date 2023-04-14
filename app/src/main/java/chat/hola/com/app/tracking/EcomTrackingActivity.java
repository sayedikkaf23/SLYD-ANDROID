package chat.hola.com.app.tracking;

import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.ezcall.android.R;
import com.ezcall.android.databinding.ActivityEcomTrackingBinding;
import com.kotlintestgradle.model.tracking.TrackingItemData;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

import static chat.hola.com.app.Utilities.Constants.BOX_COUNT;
import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.IS_SPIT_PRODUCT;
import static chat.hola.com.app.Utilities.Constants.PACKAGE_ID;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.kotlintestgradle.remote.RemoteConstants.PRODUCT_ORDER_ID;
import static com.kotlintestgradle.remote.RemoteConstants.REASON;

/**
 * activity class for the tracking for the particular activity.
 */
public class EcomTrackingActivity extends DaggerAppCompatActivity {
  @Inject
  ViewModelProvider.Factory mViewModelFactory;
  private ActivityEcomTrackingBinding mActivityEcomTrackingBinding;
  private EcomTrackingViewModel mEcomTrackingViewModel;
  private ArrayList<TrackingItemData> mTrackingItemData = new ArrayList<>();
  private EcomTrackAdapter mEcomTrackAdapter;
  private boolean viewStatus;
  private String mReason;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeView();
    initializeViewModel();
    subScribeToTrackingData();
    subScribeToBack();
  }

  /**
   * Initialising the View using Data Binding.
   */
  private void initializeView() {
    mActivityEcomTrackingBinding = DataBindingUtil.setContentView(this,
        R.layout.activity_ecom_tracking);
  }

  /**
   * <p>This method is used initialize the viewModel class for this activity.</p>
   */
  private void initializeViewModel() {
    mEcomTrackingViewModel = ViewModelProviders.of(this, mViewModelFactory).get(
        EcomTrackingViewModel.class);
    mActivityEcomTrackingBinding.setViewModel(mEcomTrackingViewModel);
    mActivityEcomTrackingBinding.incHeader.tvCenterCategoryName.setText(
        getResources().getString(R.string.tracking));
    mEcomTrackAdapter = new EcomTrackAdapter(mTrackingItemData);
    mActivityEcomTrackingBinding.rvDeliveryStatus.setAdapter(mEcomTrackAdapter);
    Bundle bundle = getIntent().getExtras();
    if (bundle != null) {
      boolean splitOrder = bundle.getBoolean(IS_SPIT_PRODUCT, FALSE);
      mReason = bundle.getString(REASON);
      mActivityEcomTrackingBinding.clBox.setVisibility(splitOrder ? View.VISIBLE : View.GONE);
      mActivityEcomTrackingBinding.tvBoxCount.setText(bundle.getString(BOX_COUNT));
      if (bundle.getString(PACKAGE_ID) != null && !bundle.getString(PACKAGE_ID).isEmpty()) {
        mActivityEcomTrackingBinding.tvPackageId.setText(
            String.format("%s: %s", getResources().getString(R.string.packageId),
                bundle.getString(PACKAGE_ID)));
      } else {
        mActivityEcomTrackingBinding.tvPackageId.setVisibility(View.GONE);
      }
      mEcomTrackingViewModel.callGetDeliveryStatusApi(
          bundle.getString(PRODUCT_ORDER_ID));
    }

  }

  /**
   * used to set the track status
   */
  private void setStatus() {
    for (int i = 0; i < mTrackingItemData.size(); i++) {
      TrackingItemData trackingItemData = mTrackingItemData.get(i);
      if (trackingItemData.getFormatedDate().isEmpty()) {
        int j = i - ONE;
        if (j > ZERO && !viewStatus) {
          viewStatus = TRUE;
          TrackingItemData trackingPreviousItemData = mTrackingItemData.get(j);
          trackingPreviousItemData.setViewStatus2(TRUE);
        }
        trackingItemData.setViewStatus1(TRUE);
        trackingItemData.setViewStatus2(TRUE);
      }
    }
    mEcomTrackAdapter.setReason(mReason);
    mEcomTrackAdapter.notifyDataSetChanged();
  }

  /**
   * subscribe for tracking data.
   */
  private void subScribeToTrackingData() {
    mEcomTrackingViewModel.onGetTrackingData().observe(this,
        trackingItemData -> {
          mTrackingItemData.clear();
          mTrackingItemData.addAll(trackingItemData);
          setStatus();
        });
  }

  /**
   * subscribe for back icon click.
   */
  private void subScribeToBack() {
    mEcomTrackingViewModel.onBackClicked().observe(this, value -> finish());
  }
}
