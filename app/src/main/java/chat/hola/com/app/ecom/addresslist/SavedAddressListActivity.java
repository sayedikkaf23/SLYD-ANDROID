package chat.hola.com.app.ecom.addresslist;

import static chat.hola.com.app.Utilities.Constants.ACTION;
import static chat.hola.com.app.Utilities.Constants.ADDRESS;
import static chat.hola.com.app.Utilities.Constants.ADDRESS_LIST_DATA;
import static chat.hola.com.app.Utilities.Constants.ADDRESS_SCREEN;
import static chat.hola.com.app.Utilities.Constants.CANCEL;
import static chat.hola.com.app.Utilities.Constants.CART_AMT;
import static chat.hola.com.app.Utilities.Constants.CART_ID;
import static chat.hola.com.app.Utilities.Constants.CART_ITEM;
import static chat.hola.com.app.Utilities.Constants.CART_SCREEN;
import static chat.hola.com.app.Utilities.Constants.DELIVERY_DISPLAY_TEXT;
import static chat.hola.com.app.Utilities.Constants.DELIVERY_SCREEN;
import static chat.hola.com.app.Utilities.Constants.DELIVERY_SLOT_ID;
import static chat.hola.com.app.Utilities.Constants.EDIT_TYPE;
import static chat.hola.com.app.Utilities.Constants.ESTIMATED_PRODUCT_PRICE;
import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.FROM;
import static chat.hola.com.app.Utilities.Constants.IS_DEFAULT;
import static chat.hola.com.app.Utilities.Constants.IS_DELIVER_NOW;
import static chat.hola.com.app.Utilities.Constants.IS_FROM_CART;
import static chat.hola.com.app.Utilities.Constants.IS_SLOTS_AVAILABLE;
import static chat.hola.com.app.Utilities.Constants.NONE;
import static chat.hola.com.app.Utilities.Constants.PICKUP_SLOT_ID;
import static chat.hola.com.app.Utilities.Constants.PRESCRIPTION_SCREEN;
import static chat.hola.com.app.Utilities.Constants.PROFILE_SCREEN;
import static chat.hola.com.app.Utilities.Constants.REQUESTED_TIME;
import static chat.hola.com.app.Utilities.Constants.REQUESTED_TIME_PICKUP;
import static chat.hola.com.app.Utilities.Constants.RIDE_ADDRESS_REQUEST;
import static chat.hola.com.app.Utilities.Constants.RIDE_ADDRESS_SELECTION;
import static chat.hola.com.app.Utilities.Constants.SELECTED_PRESCRIPTION;
import static chat.hola.com.app.Utilities.Constants.STORE_COUNT;
import static chat.hola.com.app.Utilities.Constants.TOTAL_STORE_COUNT;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.WHICH_DATE_SELECTED;
import static chat.hola.com.app.Utilities.Constants.WHICH_SLOT_SELECTED;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static chat.hola.com.app.Utilities.Utilities.isEmptyArray;
import static com.appscrip.myapplication.utility.Constants.COMING_FROM;
import static com.kotlintestgradle.data.utils.DataConstants.EMPTY_STRING;
import static com.kotlintestgradle.remote.util.Constants.STORE_CATEGORY_ID;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import chat.hola.com.app.ecom.addresslist.manageaddress.AddAddressActivity;
import chat.hola.com.app.ui.dialog.CustomDialogUtil;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ActivitySavedAddressListBinding;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import com.kotlintestgradle.model.ecom.getcart.CartSellerData;
import com.kotlintestgradle.model.ecom.slots.DeliverySlot;
import dagger.android.support.DaggerAppCompatActivity;
import java.util.ArrayList;
import javax.inject.Inject;

/*
 * Purpose – This class Holds list of Address added by user.
 * @author 3Embed
 * Created on Dec 10, 2019
 * Modified on
 */
public class SavedAddressListActivity extends DaggerAppCompatActivity implements
    AddressListAdapter.OnAddressItemClickListener, CustomDialogUtil.SimpleAlertDialogClickHandler {
  @Inject
  ViewModelProvider.Factory mViewModelFactory;
  private ActivitySavedAddressListBinding mBinding;
  private SavedAddressListViewModel mViewModel;
  private String mAddressId = EMPTY_STRING, mCartId = EMPTY_STRING;
  private int mTotalStoreCount = ZERO;
  private ArrayList<CartSellerData> mCartSellersData = new ArrayList<>();
  private String mComingFrom = EMPTY_STRING;
  private String mTotalAmount = EMPTY_STRING;
  private AddressListItemData mAddressData;
  private ArrayList<AddressListItemData> mAddressListItemData = new ArrayList<>();
  private ArrayList<PrescriptionModel> mPrescriptionList = new ArrayList<>();
  private AddressListAdapter mAdapter;
  private boolean mIsDeliverNow = FALSE;
  private boolean mIsSlotsAvailable = FALSE;
  private String mRequestedTimePickup = "";
  private String mRequestedTime = "";
  private ArrayList<DeliverySlot> mPickUpSlotId = new ArrayList<>();
  private ArrayList<DeliverySlot> mDeliverySlotId = new ArrayList<>();
  private String mDeliveryDisplayText = "";
  private int mWhichDay, mWhichSlot, mStoreTypeCount;

  private boolean mIsFromRide = false;
  private boolean mIsFromCart = false;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mBinding = DataBindingUtil.setContentView(
        this,
        R.layout.activity_saved_address_list);
    initialization();
  }

  /**
   * This method is using for resource initialization
   */
  private void initialization() {
    mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(
        SavedAddressListViewModel.class);
    mBinding.setViewModel(mViewModel);
    mViewModel.getAddressList();
    mIsFromCart = getIntent().getBooleanExtra(IS_FROM_CART, FALSE);
    if (getIntent() != null && getIntent().getStringExtra(COMING_FROM) != null) {
      mComingFrom = getIntent().getStringExtra(COMING_FROM);
      if (mComingFrom != null && mComingFrom.equals(PROFILE_SCREEN)) {
        mBinding.btnContinue.setVisibility(View.GONE);
      }
    }
    if (getIntent() != null && getIntent().getStringExtra(CART_AMT) != null) {
      mTotalAmount = getIntent().getStringExtra(CART_AMT);
    }
    if (getIntent() != null && getIntent().getStringExtra(CART_ID) != null) {
      mCartId = getIntent().getStringExtra(CART_ID);
    }
    if (getIntent() != null && getIntent().getStringExtra(STORE_CATEGORY_ID) != null) {
      mViewModel.storeCategoryId = getIntent().getStringExtra(STORE_CATEGORY_ID);
    }
    if (getIntent() != null && getIntent().getStringExtra(ESTIMATED_PRODUCT_PRICE) != null) {
      mViewModel.estimatedProductPrice = getIntent().getStringExtra(ESTIMATED_PRODUCT_PRICE);
    }
    if (getIntent() != null && !isEmptyArray(getIntent().getParcelableArrayListExtra(CART_ITEM))) {
      mCartSellersData = getIntent().getParcelableArrayListExtra(CART_ITEM);
    }
    if (getIntent() != null && getIntent().getStringExtra(FROM) != null
        && getIntent().getStringExtra(FROM).equals(RIDE_ADDRESS_SELECTION)) {
      mIsFromRide = true;
      mBinding.btnContinue.setText(getString(R.string.confirm));
      mBinding.btnContinue.setTextColor(getResources().getColor(R.color.purple));
    }
    setUpScreen();
    if (getIntent() != null) {
      mTotalStoreCount = getIntent().getIntExtra(TOTAL_STORE_COUNT, ZERO);
    }
    if (getIntent() != null && getIntent().getParcelableArrayListExtra(SELECTED_PRESCRIPTION)
        != null) {
      mPrescriptionList.clear();
      mPrescriptionList.addAll(getIntent().getParcelableArrayListExtra(SELECTED_PRESCRIPTION));
    }
    if (getIntent() != null) {
      mWhichDay = getIntent().getIntExtra(WHICH_DATE_SELECTED, ZERO);
      mWhichSlot = getIntent().getIntExtra(WHICH_SLOT_SELECTED, ZERO);
    }
    if (getIntent() != null && getIntent().getStringExtra(COMING_FROM) != null) {
      mComingFrom = getIntent().getStringExtra(COMING_FROM);
      mIsSlotsAvailable = getIntent().getBooleanExtra(IS_SLOTS_AVAILABLE, FALSE);
    }
    if (getIntent() != null) {
      mStoreTypeCount = getIntent().getIntExtra(STORE_COUNT, ZERO);
    }
    if (getIntent() != null && getIntent().getStringExtra(DELIVERY_DISPLAY_TEXT) != null) {
      mIsDeliverNow = getIntent().getBooleanExtra(IS_DELIVER_NOW, mIsDeliverNow);
      mRequestedTimePickup = getIntent().getStringExtra(REQUESTED_TIME_PICKUP);
      mRequestedTime = getIntent().getStringExtra(REQUESTED_TIME);
      mPickUpSlotId = getIntent().getParcelableArrayListExtra(PICKUP_SLOT_ID);
      mDeliverySlotId = getIntent().getParcelableArrayListExtra(DELIVERY_SLOT_ID);
      mDeliveryDisplayText = getIntent().getStringExtra(DELIVERY_DISPLAY_TEXT);
    }
    mBinding.tvTitle.setText(mIsFromCart ? getString(R.string.confirmAddressTitle)
        : getString(R.string.manageAddressTitle));
    mIsFromRide = false;
    mIsFromCart = true;

    mViewModel.getUiActionListener().observe(this, action -> {
      switch (action) {
        case BACK:
          finish();
          break;

        case DELIVERY_AVAILABLE:
          launchScheduleDelivery();
          break;

        case DELIVERY_NOT_AVAILABLE:
          CustomDialogUtil.basicAlertDialog(this, this,
              getResources().getString(R.string.alert),
              getResources().getString(R.string.selectDiffDeliveryAddress), CANCEL);
          break;
      }
    });
    mViewModel.getAddressListLiveData().observe(this,
        addressListItemData -> {
          mAddressListItemData.clear();
          mAddressListItemData.addAll(addressListItemData);
          mAdapter = new AddressListAdapter(mAddressListItemData, this, mIsFromCart,
              mViewModel.lastSelectedPosition, mIsFromRide);
          mBinding.rvAddressList.setAdapter(mAdapter);
          mAdapter.notifyDataSetChanged();
          if (mViewModel.lastSelectedPosition != NONE
              && mViewModel.lastSelectedPosition < mAddressListItemData.size()) {
            mBinding.rvAddressList.scrollToPosition(mViewModel.lastSelectedPosition);
          }
        });
    mViewModel.getAddressListData().observe(this, indexValue -> {
      if (indexValue != null) {
        if (mIsFromCart) {
          selectAddress(mAddressListItemData.get(indexValue));
        }
      }
    });
    mBinding.btnAddAddress.setOnClickListener(
        view -> {
          launchAddAddressActivity();
        });

    mBinding.tvAddAddress.setOnClickListener(
        view -> {
          launchAddAddressActivity();
        });

    mBinding.btnContinue.setOnClickListener(v -> {
      Intent intent = null;
      switch (mComingFrom) {
        case CART_SCREEN:
        case PRESCRIPTION_SCREEN:
          //mViewModel.calculateDeliveryFee(mCartId, mAddressId);
          launchScheduleDelivery();
          break;

        case DELIVERY_SCREEN:
          intent = new Intent(SavedAddressListActivity.this, chat.hola.com.app.ecom.payment.PaymentMethodActivity.class);
          intent.putExtra(ADDRESS_LIST_DATA, mAddressData);
          intent.putExtra(CART_AMT, mTotalAmount);
          intent.putParcelableArrayListExtra(SELECTED_PRESCRIPTION, mPrescriptionList);
          intent.putExtra(IS_DELIVER_NOW, mIsDeliverNow);
          intent.putExtra(REQUESTED_TIME_PICKUP, mRequestedTimePickup);
          intent.putExtra(REQUESTED_TIME, mRequestedTime);
          intent.putExtra(IS_FROM_CART, TRUE);
          intent.putExtra(TOTAL_STORE_COUNT, mTotalStoreCount);
          intent.putExtra(COMING_FROM, ADDRESS_SCREEN);
          intent.putExtra(WHICH_DATE_SELECTED, mWhichDay);
          intent.putExtra(WHICH_SLOT_SELECTED, mWhichSlot);
          intent.putParcelableArrayListExtra(PICKUP_SLOT_ID, mPickUpSlotId);
          intent.putParcelableArrayListExtra(DELIVERY_SLOT_ID, mDeliverySlotId);
          intent.putExtra(DELIVERY_DISPLAY_TEXT, mDeliveryDisplayText);
          startActivity(intent);
          break;

        default:
          Intent data = new Intent();
          data.putExtra(ADDRESS_LIST_DATA, mAddressData);
          setResult(RESULT_OK, data);
          finish();
          break;
      }
    });
  }

  /**
   * it will either launch ScheduleDeliveryActivity or PaymentMethodActivity
   * based on availability of slots
   */
  private void launchScheduleDelivery() {
    Intent intent = new Intent(SavedAddressListActivity.this, chat.hola.com.app.ecom.payment.PaymentMethodActivity.class);
    intent.putExtra(ADDRESS_LIST_DATA, mAddressData);
    intent.putExtra(CART_AMT, mTotalAmount);
    intent.putParcelableArrayListExtra(SELECTED_PRESCRIPTION, mPrescriptionList);
    intent.putExtra(IS_DELIVER_NOW, mIsDeliverNow);
    intent.putExtra(STORE_COUNT, mStoreTypeCount);
    intent.putExtra(IS_SLOTS_AVAILABLE, mIsSlotsAvailable);
    intent.putExtra(REQUESTED_TIME_PICKUP, mRequestedTimePickup);
    intent.putExtra(REQUESTED_TIME, mRequestedTime);
    intent.putExtra(IS_FROM_CART, TRUE);
    intent.putExtra(TOTAL_STORE_COUNT, mTotalStoreCount);
    intent.putExtra(COMING_FROM, ADDRESS_SCREEN);
    intent.putParcelableArrayListExtra(CART_ITEM, mCartSellersData);
    intent.putExtra(WHICH_DATE_SELECTED, mWhichDay);
    intent.putExtra(WHICH_SLOT_SELECTED, mWhichSlot);
    intent.putParcelableArrayListExtra(PICKUP_SLOT_ID, mPickUpSlotId);
    intent.putParcelableArrayListExtra(DELIVERY_SLOT_ID, mDeliverySlotId);
    intent.putExtra(DELIVERY_DISPLAY_TEXT, mDeliveryDisplayText);
    startActivity(intent);
  }

  /**
   * launch add address activity
   */
  private void launchAddAddressActivity() {
    mBinding.btnAddAddress.setEnabled(false);
    Intent intentAddressSelection = new Intent(this, AddAddressActivity.class);
    startActivity(intentAddressSelection);
  }


  /**
   * set up screen as per delivery
   */
  private void setUpScreen() {
    mBinding.tvAddAddress.setVisibility(View.VISIBLE);
    mBinding.tvSavedAddresses.setVisibility(View.VISIBLE);
    mBinding.btnAddAddress.setVisibility(View.GONE);
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    mBinding.btnAddAddress.setEnabled(true);
    mBinding.tvAddAddress.setEnabled(true);
    mViewModel.getAddressList();
  }

  @Override
  public void onDeleteClickListener(String addressId) {
    this.mAddressId = addressId;
    CustomDialogUtil.basicAlertDialog(this, this,
        getString(R.string.alert), getString(R.string.deleteAddressWarning), NONE);
  }

  @Override
  public void onEditClickListener(AddressListItemData addressListItemData) {
    Intent intent = new Intent(this, AddAddressActivity.class);
    intent.putExtra(ACTION, EDIT_TYPE);
    intent.putExtra(ADDRESS, addressListItemData);
    intent.putExtra(IS_DEFAULT, mViewModel.getIsEmptyValue());
    startActivity(intent);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RIDE_ADDRESS_REQUEST && resultCode == RESULT_OK && data != null) {
      mViewModel.getAddressList();
    }
  }

  @Override
  public void makeAddressDefault(String addressId, String prevDefAddressId,
      int lastSelectedPosition) {
    mViewModel.makeAddressAsDefault(addressId, prevDefAddressId, lastSelectedPosition);
  }

  @Override
  public void selectAddress(AddressListItemData addressListItemData) {
    mBinding.btnContinue.setEnabled(TRUE);
    mAddressId = addressListItemData.getId();
    this.mAddressData = addressListItemData;
  }

  @Override
  public void selectRideAddress(AddressListItemData addressListItemData) {
    mBinding.btnContinue.setEnabled(TRUE);
    this.mAddressData = addressListItemData;
  }

  @Override
  public void onOkClickListener(int type) {
    if (type != CANCEL) {
      mViewModel.deleteAddress(mAddressId);
    }
  }
}
