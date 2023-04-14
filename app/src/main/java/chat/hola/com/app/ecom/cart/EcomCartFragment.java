package chat.hola.com.app.ecom.cart;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_OK;
import static chat.hola.com.app.Utilities.Constants.ADDRESS_LIST_DATA;
import static chat.hola.com.app.Utilities.Constants.APPLY_PROMO;
import static chat.hola.com.app.Utilities.Constants.ATTRIBUTES_DATA;
import static chat.hola.com.app.Utilities.Constants.BILLING_ADDRESS_REQUEST;
import static chat.hola.com.app.Utilities.Constants.CARD;
import static chat.hola.com.app.Utilities.Constants.CARD_ID;
import static chat.hola.com.app.Utilities.Constants.CARD_NUMBER;
import static chat.hola.com.app.Utilities.Constants.CART_AMT;
import static chat.hola.com.app.Utilities.Constants.CART_ID;
import static chat.hola.com.app.Utilities.Constants.CART_ITEM;
import static chat.hola.com.app.Utilities.Constants.CART_SCREEN;
import static chat.hola.com.app.Utilities.Constants.CENTRAL_DRIVER;
import static chat.hola.com.app.Utilities.Constants.CHANGE_ADDRESS_REQUEST;
import static chat.hola.com.app.Utilities.Constants.CONFIRM_ORDER;
import static chat.hola.com.app.Utilities.Constants.CONTACT_LESS_DELIVERY_REQ_CODE;
import static chat.hola.com.app.Utilities.Constants.CURRENCY;
import static chat.hola.com.app.Utilities.Constants.DELIVERY_DISPLAY_TEXT;
import static chat.hola.com.app.Utilities.Constants.DELIVERY_SLOT_ID;
import static chat.hola.com.app.Utilities.Constants.EXTRA_NOTES;
import static chat.hola.com.app.Utilities.Constants.EXTRA_NOTES_REQ_CODE;
import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.FIFTEEN;
import static chat.hola.com.app.Utilities.Constants.FIVE;
import static chat.hola.com.app.Utilities.Constants.FOUR;
import static chat.hola.com.app.Utilities.Constants.IS_DELIVER_NOW;
import static chat.hola.com.app.Utilities.Constants.IS_FROM_CART;
import static chat.hola.com.app.Utilities.Constants.IS_SLOTS_AVAILABLE;
import static chat.hola.com.app.Utilities.Constants.LOGIN;
import static chat.hola.com.app.Utilities.Constants.LOGIN_RC;
import static chat.hola.com.app.Utilities.Constants.MANGE_ADDRESS_REQUEST;
import static chat.hola.com.app.Utilities.Constants.NONE;
import static chat.hola.com.app.Utilities.Constants.ORDER_SCREEN;
import static chat.hola.com.app.Utilities.Constants.PAYMENT;
import static chat.hola.com.app.Utilities.Constants.PAYMENT_BY_WALLET;
import static chat.hola.com.app.Utilities.Constants.PAYMENT_METHOD_REQ_CODE;
import static chat.hola.com.app.Utilities.Constants.PAYMENT_TYPE;
import static chat.hola.com.app.Utilities.Constants.PAYMENT_TYPE_CARD;
import static chat.hola.com.app.Utilities.Constants.PICKUP_SLOT_ID;
import static chat.hola.com.app.Utilities.Constants.PROMO_CODE_ID;
import static chat.hola.com.app.Utilities.Constants.RADIO_BUTTON_ID;
import static chat.hola.com.app.Utilities.Constants.REQUESTED_TIME;
import static chat.hola.com.app.Utilities.Constants.REQUESTED_TIME_PICKUP;
import static chat.hola.com.app.Utilities.Constants.REQUEST_CODE_PERMISSION_MULTIPLE;
import static chat.hola.com.app.Utilities.Constants.SCHEDULE_DELIVERY_REQ_CODE;
import static chat.hola.com.app.Utilities.Constants.SELECTED_CONTACTLESS_DELIVERY_TYPE;
import static chat.hola.com.app.Utilities.Constants.SELECTED_CONTACTLESS_DELIVERY_TYPE_ID;
import static chat.hola.com.app.Utilities.Constants.SELECTED_PRESCRIPTION;
import static chat.hola.com.app.Utilities.Constants.SIXTEEN_F;
import static chat.hola.com.app.Utilities.Constants.SPECIFICATIONS;
import static chat.hola.com.app.Utilities.Constants.STORE_COUNT;
import static chat.hola.com.app.Utilities.Constants.TEN;
import static chat.hola.com.app.Utilities.Constants.THIRTY;
import static chat.hola.com.app.Utilities.Constants.THOUSAND;
import static chat.hola.com.app.Utilities.Constants.THREE;
import static chat.hola.com.app.Utilities.Constants.TIP_AMOUNT;
import static chat.hola.com.app.Utilities.Constants.TIP_CURRENCY;
import static chat.hola.com.app.Utilities.Constants.TOTAL_STORE_COUNT;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.TWENTY;
import static chat.hola.com.app.Utilities.Constants.Transaction.AMOUNT;
import static chat.hola.com.app.Utilities.Constants.VALUE_ZERO;
import static chat.hola.com.app.Utilities.Constants.WALLET_AMT;
import static chat.hola.com.app.Utilities.Constants.WALLET_PLUS_CARD;
import static chat.hola.com.app.Utilities.Constants.WHICH_DATE_SELECTED;
import static chat.hola.com.app.Utilities.Constants.WHICH_SLOT_SELECTED;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static chat.hola.com.app.Utilities.DateAndTimeUtil.getCurrentDate;
import static chat.hola.com.app.Utilities.Utilities.getTodayDate;
import static chat.hola.com.app.Utilities.Utilities.isEmptyArray;
import static chat.hola.com.app.ecom.cart.CartType.ECOM_CART;
import static chat.hola.com.app.ecom.cart.CartUiAction.CROSS_CLICK;
import static com.appscrip.myapplication.utility.Constants.COMING_FROM;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;
import static com.facebook.share.internal.ShareConstants.PROMO_CODE;
import static com.kotlintestgradle.data.utils.DataConstants.CITY_ID;
import static com.kotlintestgradle.data.utils.DataConstants.ECOM_TYPE;
import static com.kotlintestgradle.data.utils.DataConstants.EMPTY_STRING;
import static com.kotlintestgradle.remote.RemoteConstants.COUNTRY_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PARENT_PRODUCT_ID;
import static com.kotlintestgradle.remote.RemoteConstants.PRODUCT_ID;
import static com.kotlintestgradle.remote.RemoteConstants.STORE_ID;
import static com.kotlintestgradle.remote.util.Constants.STORE_CATEGORY_ID;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.CustomTypefaceSpan;
import chat.hola.com.app.Utilities.LocationManagerUtil;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.ecom.addresslist.SavedAddressListActivity;
import chat.hola.com.app.ecom.payment.PaymentMethodActivity;
import chat.hola.com.app.ecom.pdp.ProductDetailsActivity;
import chat.hola.com.app.ecom.pdp.attributebottomsheet.PiecesPopUpAdapter;
import chat.hola.com.app.ecom.pdp.attributebottomsheet.QuantityItemClick;
import chat.hola.com.app.home.LandingActivity;
import com.bumptech.glide.Glide;
import com.ezcall.android.R;
import com.ezcall.android.databinding.FragmentCartScreenBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.kotlintestgradle.data.utils.DataConstants;
import com.kotlintestgradle.interactor.ecom.AddressHandler;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import com.kotlintestgradle.model.ecom.getcart.CartAccoutingData;
import com.kotlintestgradle.model.ecom.getcart.CartAttributesData;
import com.kotlintestgradle.model.ecom.getcart.CartData;
import com.kotlintestgradle.model.ecom.getcart.CartProductItemData;
import com.kotlintestgradle.model.ecom.getcart.CartSellerData;
import com.kotlintestgradle.model.ecom.getcart.CartTaxData;
import com.kotlintestgradle.model.ecom.promocode.ApplyPromoCodeData;
import com.kotlintestgradle.model.ecom.promocode.ProductPromoInput;
import com.kotlintestgradle.model.ecom.slots.DeliverySlot;
import dagger.android.support.DaggerFragment;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;

@ActivityScoped
public class EcomCartFragment extends DaggerFragment implements QuantityClick, QuantityItemClick,
    SelectItem, CompoundButton.OnCheckedChangeListener {
  @Inject
  ViewModelProvider.Factory mViewModelFactory;
  @Inject
  AddressHandler mAddressHandler;
  private CartViewModel mViewModel;
  private FragmentCartScreenBinding mBinding;
  private ListPopupWindow mPopupWindow;
  private ArrayList<CartProductItemData> mCartProductItemData = new ArrayList<>();
  private ArrayList<CartSellerData> mCartSellersData = new ArrayList<>();
  private CartAdapter mCartAdapter;
  private int mPosition;
  private BottomSheetDialogFragment mTipDialog;
  private boolean mConfirmOrder;
  private String mTipAmount = EMPTY_STRING;
  private int mRadioButtonId = RADIO_BUTTON_ID;
  private CartData mCartData;
  private int mOtherTipButtonId = NONE;
  private ArrayList<String> mTips = new ArrayList<>();
  private ApplyPromoCodeData mApplyPromoCodeData;
  private int mPaymentType, mTotalStoreCount;
  private ArrayList<CartProductItemData> mItemWithPrescription = new ArrayList<>();
  private ArrayList<CartProductItemData> mItemWithoutPrescription = new ArrayList<>();


  @Inject
  public EcomCartFragment() {
  }

  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart_screen, container, false);
    initialize();
    subscribeLocationLiveData();
    subscribeAddressLiveData();
    subscribeUiAction();
    subscribeOnPromoData();
    subscribeOnError();
    return mBinding.getRoot();
  }

  /**
   * Does Initialization of basic activity resources
   */
  private void initialize() {
    mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(CartViewModel.class);
    mBinding.setViewModel(mViewModel);//used to connect mBinding to viewModel

    mBinding.clPharmaHeader.setVisibility(View.VISIBLE);
    mBinding.clCartHeader.setVisibility(View.GONE);
    mBinding.vgContactLess.setVisibility(View.GONE);
    if (getActivity() != null) {
      mBinding.includeCartHeader.ivBackBtn.setVisibility(
          /*getActivity() instanceof HomeActivity ? View.GONE :*/ View.VISIBLE);
    }
    mBinding.includeCartHeader.ivBackBtn.setOnClickListener(view ->{
      ((EcomCartActivity)getActivity()).finish();
    });
    mBinding.rvCartList.setVisibility(View.VISIBLE);
    if (getArguments() != null) {
      mConfirmOrder = getArguments().getBoolean(CONFIRM_ORDER, FALSE);
      mViewModel.mConfirmOrder = mConfirmOrder;
      mViewModel.cardId = getArguments().getString(CARD_ID);
      mViewModel.paymentType = getArguments().getInt(PAYMENT_TYPE);
      int pk = getArguments().getInt(PAYMENT_TYPE);
      mViewModel.isPayByWallet = getArguments().getBoolean(PAYMENT_BY_WALLET);
      mViewModel.payByWallet = getArguments().getBoolean(PAYMENT_BY_WALLET);
      float totalAmt = getArguments().getFloat(AMOUNT);
      setPaymentMethod(totalAmt);
      mBinding.includeCartHeader.tvTitle.setText(
          mConfirmOrder ? getResources().getString(R.string.confirmOrder)
              : getResources().getString(R.string.myCart));
      mViewModel.cartAmtGroup.set(!mConfirmOrder);
      mViewModel.paymentGroup.set(mConfirmOrder);
      mBinding.btnPlaceOrder.setText(mConfirmOrder
          ? getResources().getString(R.string.confirmAndPlaceOrder)
          : getResources().getString(R.string.cartPlaceOrder));
      if (mConfirmOrder) {
        mBinding.includeCartHeader.ivBackBtn.setImageResource((R.drawable.all_back));
        mBinding.tvPayMentMethod.setVisibility(View.VISIBLE);
        mBinding.tvPmChange.setVisibility(View.VISIBLE);
        mBinding.viewBillingAddDivider.setVisibility(View.VISIBLE);
        mBinding.vgApplyPromoCode.setVisibility(View.VISIBLE);
        if (!AppController.getInstance().isGuest()) {
          mBinding.vgBillingAddress.setVisibility(View.VISIBLE);
          mViewModel.billingAddVisible.set(TRUE);
        }
      } else {
        mBinding.vgContactLess.setVisibility(View.GONE);
        mBinding.vgExtraNote.setVisibility(View.GONE);
        mBinding.tvCartDeliverTitle.setTextSize(SIXTEEN_F);
      }

    }
    Thread thread = new Thread() {
      @Override
      public void run() {
        if (Utilities.isNetworkAvailable(AppController.getInstance())) {
          mViewModel.mIpAddress = Utilities.getIpAddress(getContext());
        } else {
          mViewModel.mCartUiAction.postValue(CROSS_CLICK);
        }
      }
    };
    thread.start();
    //mBinding.tvCartAddress.setText(mUserInfoHandler.getDineAddress());
    mViewModel.changeVisible.set(!AppController.getInstance().isGuest() ? TRUE : FALSE);
    mViewModel.addAddressVisible.set(!AppController.getInstance().isGuest()? FALSE : TRUE);
    mViewModel.getCartDataMutableLiveData().observe(requireActivity(),
        cartData -> {
          if (!isEmptyArray(cartData.getSellers())) {
            mCartData = cartData;
            CartAccoutingData accounting = cartData.getAccounting();
            if (accounting != null) {
              mViewModel.currencySymbol = cartData.getCurrencySymbol();
              if (accounting.getSubTotal() != null) {
                mBinding.accoutingDetails.tvCartSubTotalAmt.setText(
                    String.format("%s%s", cartData.getCurrencySymbol(),
                        String.format("%.2f", Float.parseFloat(accounting.getUnitPrice()))));
              }
              if (accounting.getDeliveryFee() != null) {
                mBinding.accoutingDetails.tvCartShippingAmt.setText(
                    (accounting.getDeliveryFee().equals(String.valueOf(ZERO)))
                        ? getResources().getString(R.string.free) :
                        String.format("%s%s", cartData.getCurrencySymbol(),
                            accounting.getDeliveryFee()));
              }
              mBinding.accoutingDetails.tvCartTotalSavingsAmt.setText(
                  String.format("%s%s", cartData.getCurrencySymbol(),
                      String.format("%.2f", ((float) accounting.getOfferDiscount()))));
              if (accounting.getFinalTotal() != null && !accounting.getFinalTotal().isEmpty()) {
                mBinding.accoutingDetails.tvCartOrderTotalAmt.setText(
                    String.format("%s%s", cartData.getCurrencySymbol(),
                        String.format("%.2f", Float.parseFloat(accounting.getFinalTotal()))));
                mBinding.tvCartAmount.setText(
                    String.format("%s%s", cartData.getCurrencySymbol(),
                        String.format("%.2f", Float.parseFloat(accounting.getFinalTotal()))));
              }
            }
            Utilities.printLog("exe" + "mCartData" + mCartData.getTaxData().size());
            if (mCartData.getAccounting().getTax().size() > ZERO) {
              setTaxData(mCartData);
            } else {
              mBinding.accoutingDetails.tvTaxes.setVisibility(View.GONE);
            }
            mCartProductItemData.clear();
            mBinding.clEmptyCart.setVisibility(View.GONE);
              mBinding.viewCartPayment.setVisibility(View.VISIBLE);
              mBinding.clDineDelivery.setVisibility(View.GONE);
              mBinding.clItemHeader.setVisibility(View.GONE);
              mBinding.clCartFooter.setVisibility(View.VISIBLE);

            if (cartData.getSellers() != null) {
              mCartSellersData.clear();
              for (int i = ZERO; i < cartData.getSellers().size(); i++) {
                mCartProductItemData.addAll(cartData.getSellers().get(i).getProducts());
                mCartSellersData.add(cartData.getSellers().get(i));
              }
              if (!mConfirmOrder) {
                mBinding.includeCartHeader.tvTitle.setText(
                    String.format("%s( %d )", getResources().getString(R.string.cart),
                        mCartProductItemData.size()));
              }
            }

            int withPrescriptionSize =
                mItemWithPrescription == null ? ZERO : mItemWithPrescription.size();
            int withoutPrescriptionSize =
                mItemWithoutPrescription == null ? ZERO : mItemWithoutPrescription.size();

            if (mConfirmOrder) {
              mBinding.vgPrescriptionUpload.setVisibility(
                  mItemWithPrescription.size() > ZERO ? View.VISIBLE : View.GONE);
            }

            mCartAdapter = new CartAdapter(mCartProductItemData, this,
                this::onSelectItem, ECOM_CART, mConfirmOrder, withPrescriptionSize,
                withoutPrescriptionSize);
            mBinding.rvCartList.setAdapter(mCartAdapter);
            mBinding.rvCartList.setVisibility(View.VISIBLE);
            Utilities.printLog("exe" + "mCartProductItemData" + mCartProductItemData.size());
            mBinding.clEmptyCart.setVisibility(
                mCartProductItemData.size() > ZERO ? View.GONE : View.VISIBLE);
              mBinding.clCartFooter.setVisibility(
                  mCartProductItemData.size() > ZERO ? View.VISIBLE : View.GONE);

          } else {
            mBinding.includeCartHeader.tvTitle.setText(
                String.format("%s( %d )", getResources().getString(R.string.cart), ZERO));
            mBinding.clEmptyCart.setVisibility(View.VISIBLE);
            mBinding.clCartFooter.setVisibility(View.GONE);
          }
        });
    mViewModel.getCartProducts();
    mViewModel.onUserDataUpdate();
    mViewModel.onCartUpdate();
    mBinding.cbDeliveryAddress.setOnCheckedChangeListener(
        (buttonView, isChecked) -> {
          mViewModel.billingAddVisible.set(!isChecked);
          mBinding.tvCartBillingName.setTextColor(
              isChecked ? getResources().getColor(R.color.colorSilverChalice)
                  : getResources().getColor(R.color.color_black));
          mBinding.tvCartBillingAddress.setTextColor(
              isChecked ? getResources().getColor(R.color.colorSilverChalice)
                  : getResources().getColor(R.color.color_black));
          if (isChecked) {
            mBinding.tvCartBillingName.setText(mBinding.tvCartDeliverName.getText().toString());
            mBinding.tvCartBillingAddress.setText(mBinding.tvCartDelAddress.getText().toString());
          }
        });
  }

  /**
   * update the total amount
   *
   * @param amount String
   */
  private void updateTotalAmount(String amount) {
    try {
      float tip = Float.parseFloat(amount);
      float cartAmount = Float.parseFloat(mCartData.getAccounting().getFinalTotal());
      float total = cartAmount + tip;
      mBinding.accoutingDetails.tvCartOrderTotalAmt.setText(
          String.format("%s %.2f", mViewModel.currencySymbol, total));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * subscribe to tips data
   */
  private void subscribeTipData() {
    mViewModel.getTipLiveData().observe(getViewLifecycleOwner(), tips -> {
      if (tips != null && tips.size() > ZERO) {
        mBinding.vgTip.setVisibility(View.VISIBLE);
        tips.add(tips.size(), getResources().getString(R.string.other));
        mTips.clear();
        mTips.addAll(tips);
        initializeTips();
      } else {
        mBinding.vgTip.setVisibility(View.GONE);
      }
    });
  }

  /**
   * add the time slots at run time
   */
  private void initializeTips() {
    if (mBinding.rgTipAmount.getChildCount() > ZERO) {
      mBinding.rgTipAmount.removeAllViews();
    }
    for (String tipAmount : mTips) {
      boolean isOtherTipAmount = tipAmount.equals(getResources().getString(R.string.other));
      AppCompatRadioButton radioButton = getRadioButton(isOtherTipAmount);
      radioButton.setText(isOtherTipAmount ? tipAmount : String.format("%s %.2f",
          mViewModel.currencySymbol, Float.parseFloat(tipAmount)));
      radioButton.setId(mRadioButtonId++);
      if (isOtherTipAmount) {
        mOtherTipButtonId = mRadioButtonId;
      }
      radioButton.setOnCheckedChangeListener(this);
      RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(
          RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
      params.setMargins(TWENTY, FIFTEEN, TWENTY, ZERO);
      mBinding.rgTipAmount.addView(radioButton, params);
    }
  }

  @Override
  public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
    for (int i = ZERO; i < mBinding.rgTipAmount.getChildCount(); i++) {
      if (mBinding.rgTipAmount.getChildAt(i) instanceof AppCompatRadioButton) {
        AppCompatRadioButton radioButton = (AppCompatRadioButton) mBinding.rgTipAmount.getChildAt(
            i);
        if (radioButton.getId() == buttonView.getId() && radioButton.isChecked()) {
          if (i + ONE == mBinding.rgTipAmount.getChildCount()) {
            if (!Objects.equals(mTipAmount, EMPTY_STRING)) {
              Bundle args = new Bundle();
              args.putString(TIP_AMOUNT, mTipAmount);
              args.putString(TIP_CURRENCY, mViewModel.currencySymbol);
              mTipDialog.setArguments(args);
            } else {
              Bundle args = new Bundle();
              args.putString(TIP_CURRENCY, mViewModel.currencySymbol);
              mTipDialog.setArguments(args);
            }
            if (!mTipDialog.isVisible()) {
              mTipDialog.show(getChildFragmentManager(), mTipDialog.getTag());
            }
          } else {
            try {
              if (isValidAmount(radioButton.getText().toString().trim().substring(ONE).trim())) {
                mViewModel.tip = radioButton.getText().toString().trim().substring(ONE).trim();
                mViewModel.tipAmount.setValue(
                    radioButton.getText().toString().trim().substring(ONE).trim());
              } else {
                String[] s = (radioButton.getText().toString().trim().substring(ONE).trim()).split(
                    "\\s+");
                String amount = s[s.length - ONE];
                mViewModel.tip = amount;
                mViewModel.tipAmount.postValue(amount);
              }
            } catch (NumberFormatException e) {
              try {
                String[] s = (radioButton.getText().toString().trim().substring(ONE).trim()).split(
                    "\\s+");
                String amount = s[s.length - ONE];
                mViewModel.tip = amount;
                mViewModel.tipAmount.postValue(amount);
                boolean isValid = isValidAmount(amount);
                mBinding.accoutingDetails.tvCartTip.setVisibility(
                    isValid ? View.VISIBLE : View.GONE);
                mBinding.accoutingDetails.tvCartTipAmt.setVisibility(
                    isValid ? View.VISIBLE : View.GONE);
                mBinding.accoutingDetails.tvCartTipAmt.setText(
                    String.format("%s %.2f", mViewModel.currencySymbol,
                        Float.parseFloat((amount.isEmpty() ? VALUE_ZERO : amount))));
                if (isValid) {
                  updateTotalAmount((amount.trim().isEmpty() ? VALUE_ZERO : amount.trim()));
                }
              } catch (Exception e1) {
                e1.printStackTrace();
              }
            }
          }
          return;
        }
      }
    }
  }

  /**
   * returns a ready made radio button
   *
   * @return AppCompatRadioButton
   */
  private AppCompatRadioButton getRadioButton(boolean isOtherTipAmount) {
    AppCompatRadioButton radioButton = new AppCompatRadioButton(getActivity());
    radioButton.setPadding(THIRTY, TEN, THIRTY, TEN);
    radioButton.setButtonDrawable(null);
    radioButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.add_button_selector));
    if (isOtherTipAmount) {
      radioButton.setSingleLine();
      radioButton.setPadding(THIRTY, TEN, THIRTY, TEN);
      radioButton.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null,
          ContextCompat.getDrawable(getActivity(),
              R.drawable.ic_edit_pen), null);
      radioButton.setCompoundDrawablePadding(TEN);
    }
    radioButton.setTextAppearance(getActivity(), R.style.Text_14sp_tipToggle_PoppinsMed);
    return radioButton;
  }

  /**
   * checks if entered amount is valid or not
   *
   * @param amount String
   * @return boolean
   */
  private boolean isValidAmount(String amount) {
    try {
      float value = Float.parseFloat(amount.trim());
      return value > ZERO;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  /*subscribes the location live data */
  private void subscribeLocationLiveData() {
    mViewModel.getLocationLiveData().observe(getActivity(), aBoolean -> {
      if (aBoolean) {
        if (!AppController.getInstance().isGuest()) {
          Thread t = new Thread() {
            public void run() {
              if (mAddressHandler.isAddressListEmpty()) {
                //permission for location
                if (mCartProductItemData.size() > ZERO) {
                  checkPerMission();
                }
              } else {
                if (mAddressHandler.getDefaultAddress().getName() != null
                    && !mAddressHandler.getDefaultAddress().getName().isEmpty()) {
                  String name = mAddressHandler.getDefaultAddress().getName();
                  requireActivity().runOnUiThread(() -> {
                    mBinding.tvCartDeliverName.setVisibility(View.VISIBLE);
                    mBinding.tvCartDeliverName.setText(name);
                    mBinding.tvCartBillingName.setText(name);
                  });
                } else {
                  requireActivity().runOnUiThread(
                      () -> mBinding.tvCartDeliverName.setVisibility(View.GONE));
                  requireActivity().runOnUiThread(
                      () -> mBinding.tvCartBillingName.setVisibility(View.GONE));
                }
                String addLine1 = mAddressHandler.getDefaultAddress().getAddLine1();
                String landMark = mAddressHandler.getDefaultAddress().getLandmark();
                String city = mAddressHandler.getDefaultAddress().getCity();
                String pinCOde = mAddressHandler.getDefaultAddress().getPincode();
                String addID = mAddressHandler.getDefaultAddress().getId();
                String addressType = mAddressHandler.getDefaultAddress().getTaggedAs();
                String phoneNum = String.format("+%s %s",
                    mAddressHandler.getDefaultAddress().getMobileNumberCode(),
                    mAddressHandler.getDefaultAddress().getMobileNumber());
                mViewModel.cityId = mAddressHandler.getDefaultAddress().getCityId();
                mViewModel.countryId = mAddressHandler.getDefaultAddress().getCountryId();
                getActivity().runOnUiThread(() -> {
                  mBinding.tvCartDelAddress.setText(
                      String.format("%s%s,%s,%s",
                          addLine1,
                          landMark,
                          city,
                          String.format("%s\n%s:%s", pinCOde,
                              getResources().getString(R.string.phoneNumber), phoneNum)));
                  mViewModel.addressId = addID;
                  mBinding.tvCartDeliverType.setText(String.format("%s", addressType));
                  mBinding.tvCartDeliverType.setVisibility(
                      mBinding.tvCartDeliverType.getText().toString().trim().isEmpty()
                          ? View.INVISIBLE : View.VISIBLE);
                  mBinding.tvCartBillingAddress.setText(
                      String.format("%s%s,%s,%s",
                          addLine1,
                          landMark,
                          city,
                          String.format("%s\n%s:%s", pinCOde,
                              getResources().getString(R.string.phoneNumber), phoneNum)));
                  mViewModel.billingAddressId = addID;
                  mBinding.cbDeliveryAddress.setChecked(TRUE);
                });
              }
            }
          };
          t.start();
        } else {
          mBinding.vgBillingAddress.setVisibility(View.GONE);
          mBinding.tvCartDeliverName.setVisibility(View.GONE);
          if (mCartProductItemData.size() > ZERO) {
            checkPerMission();
          }
        }
      }
    });
  }

  @Override
  public void onResume() {
    super.onResume();
    Utilities.printLog("exe" + "onResume");
  }

  @Override
  public void onHiddenChanged(boolean hidden) {
    if (!hidden) {
      //do when hidden
      mViewModel.getCartProducts();
    }
  }

  /**
   * subscribe to location data
   */
  private void subscribeAddressLiveData() {
    mViewModel.getAddressData().observe(requireActivity(),
        location -> mBinding.tvCartDelAddress.setText(location));
  }

  /**
   * subscribe to location data
   */
  private void subscribeUiAction() {
    mViewModel.uiAction().observe(requireActivity(), cartUiAction -> {
      switch (cartUiAction) {
        case LOGIN_RESULT:
          loginActivity();
          break;
        case LOGIN:
          doLogin();
          break;
        case PAYMENT:
        case CHANGE_PAYMENT:
          paymentActivity();
          break;
        case MANAGE_ADDRESS:
              manageAddress(FALSE);
          break;
        case CHANGE_ADDRESS:
          changeAddress();
          break;
        case BILLING_ADDRESS:
          addBillingAddress();
          break;
        case CROSS_CLICK:
          if (getActivity() != null) {
            getActivity().finish();
          }
          break;
        case CONTINUE_SHOPPING:
            getActivity().finish();
          break;

        case DELIVERY_SCHEDULE_CHANGE:
          break;

        case EXTRA_NOTE:
          break;

        case CONTACT_LESS_DELIVERY:
          break;

        case EMPTY_CART:
          mBinding.includeCartHeader.tvTitle.setText(
              String.format("%s( %d )", getResources().getString(R.string.cart), ZERO));
          mBinding.clEmptyCart.setVisibility(View.VISIBLE);
          mBinding.clCartFooter.setVisibility(View.GONE);
          break;
        case ON_SUCCESS:
          onSuccess();
          break;
        case SUB_TOTAL:
          break;
        case ENABLE_BACK:
          mBinding.includeCartHeader.ivBackBtn.setEnabled(TRUE);
          if (getActivity() instanceof EcomCartActivity) {
            EcomCartActivity ecomCartActivity = (EcomCartActivity) getActivity();
            ecomCartActivity.allowBack(TRUE);
          }
          break;
        case DISABLE_BACK:
          mBinding.includeCartHeader.ivBackBtn.setEnabled(FALSE);
          if (getActivity() instanceof EcomCartActivity) {
            EcomCartActivity ecomCartActivity = (EcomCartActivity) getActivity();
            ecomCartActivity.allowBack(FALSE);
          }
          break;
        case CLEAR:
          mViewModel.coupon = "";
          mViewModel.couponId = "";
          mBinding.tvPromoCode.setText(getResources().getString(R.string.selectPromoCode));
          mBinding.accoutingDetails.vgPromotionsApplied.setVisibility(View.GONE);
          float finalTotalAmt = Float.parseFloat(mCartData.getAccounting().getFinalTotal());
          mBinding.accoutingDetails.tvCartOrderTotalAmt.setText(
              String.format("%s%s", mCartData.getCurrencySymbol(),
                  String.format("%.2f", finalTotalAmt)));
          float yourSavingsAmt = (float) mCartData.getAccounting().getOfferDiscount();
          mBinding.accoutingDetails.tvCartTotalSavingsAmt.setText(
              String.format("%s%s", mCartData.getCurrencySymbol(),
                  String.format("%.2f", yourSavingsAmt)));
          if (mPaymentType == WALLET_PLUS_CARD) {
            float walletAmt = getArguments().getFloat(WALLET_AMT);
            mBinding.tvCardNumberAmt.setText(
                String.format("%s %s", mCartData.getCurrencySymbol(),
                    String.format("%.2f",
                        (Float.parseFloat(mCartData.getAccounting().getFinalTotal())
                            - walletAmt))));
          } else if (mPaymentType == CARD) {
            mBinding.tvCardNumberAmt.setText(
                String.format("%s%s", mCartData.getCurrencySymbol(),
                    String.format("%.2f", finalTotalAmt)));
          }
          break;
        case PROMO_CODE:
          promoCodeAct(mViewModel.countryId, mViewModel.cityId, mViewModel.mCartId, getStoreId());
          break;
        default:
          break;
      }
    });
  }

  /**
   * it will show the layout to schedule the delivery
   */
  private void scheduleDelivery(boolean isOrderScreen) {
  }

  private int getTotalSellersCount() {
    int totalStoreCount = ZERO;
    boolean isCentralDriverIncluded = false;
    for (CartSellerData item : mCartSellersData) {
      int driverType = item.getDriverTypeId();
      if (driverType != CENTRAL_DRIVER) {
        totalStoreCount++;
      } else if (!isCentralDriverIncluded) {
        totalStoreCount++;
        isCentralDriverIncluded = TRUE;
      }
    }
    return totalStoreCount;
  }

  /**
   * set the tax data
   *
   * @param cartData cart data object from server.
   */
  private void setTaxData(CartData cartData) {
    LinearLayout linearLayout = mBinding.accoutingDetails.cartAddTaxLl;
    ArrayList<CartTaxData> taxData = cartData.getAccounting().getTax();
    linearLayout.removeAllViews();
    LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(
        Context.LAYOUT_INFLATER_SERVICE);
    View viewInflater;
    if (taxData != null && taxData.size() > ZERO) {
      for (int i = ZERO; i < taxData.size(); i++) {
        viewInflater = layoutInflater.inflate(R.layout.tax_row, null);
        TextView taxTxtTv = viewInflater.findViewById(R.id.taxTxtTv);
        TextView taxValueTv = viewInflater.findViewById(R.id.taxValueTv);
        taxTxtTv.setText(taxData.get(i).getTaxName());
        taxValueTv.setText(
            String.format("%s %s", cartData.getCurrencySymbol(), taxData.get(i).getTotalValue()));
        linearLayout.addView(viewInflater);
      }
    }
  }

  /**
   * check permission camera,read external storage
   */
  private void checkPerMission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ((requireActivity().checkSelfPermission(
        ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED))) {
      requestPermissions(new String[]{ACCESS_FINE_LOCATION},
          REQUEST_CODE_PERMISSION_MULTIPLE);
    } else {
      //permission granted.
      accessLocation();
    }
  }

  /**
   * getLocation data
   */
  private void accessLocation() {
    mBinding.tvCartDelAddress.setText(LocationManagerUtil.getCurrentLocation(getActivity()));
    Thread t = new Thread() {
      public void run() {
        String name = mAddressHandler.getDefaultAddress().getName();
        String deliveryType = mAddressHandler.getDefaultAddress().getTaggedAs();
        String cartBillingName = mAddressHandler.getDefaultAddress().getName();
        if (mAddressHandler.getDefaultAddress().getName() != null
            && !mAddressHandler.getDefaultAddress().getName().isEmpty()) {
          new Handler(Looper.getMainLooper()).post(() -> {
            mBinding.tvCartDeliverName.setText(name);
            mBinding.tvCartDeliverType.setText(deliveryType);
            mBinding.tvCartBillingName.setText(cartBillingName);
          });
        } else {
          new Handler(Looper.getMainLooper()).post(() -> {
            requireActivity().runOnUiThread(
                () -> mBinding.tvCartDeliverName.setVisibility(View.GONE));
            requireActivity().runOnUiThread(
                () -> mBinding.tvCartDeliverType.setVisibility(View.GONE));
            requireActivity().runOnUiThread(
                () -> mBinding.tvCartBillingName.setVisibility(View.GONE));
          });
        }
        Utilities.printLog("address" + "isAddressListEmpty()"
            + mAddressHandler.isAddressListEmpty()
            + mAddressHandler.getDefaultAddress().getName());
        mViewModel.addressId = mAddressHandler.getDefaultAddress().getId();
        mViewModel.billingAddressId = mAddressHandler.getDefaultAddress().getId();
        mViewModel.cityId = mAddressHandler.getDefaultAddress().getCityId();
        mViewModel.countryId = mAddressHandler.getDefaultAddress().getCountryId();
      }
    };
    t.start();
  }

  @Override
  public void onItemClick(AppCompatTextView appCompatTextView, String unitName, int pos,
      int action) {
    mPosition = pos;
    if (appCompatTextView != null && unitName != null) {
      CartProductItemData cartProductItemData = mCartProductItemData.get(mPosition);
      ArrayList<String> unitList = new ArrayList<>();
      for (int i = ONE; i <= cartProductItemData.getAvailableQuantity(); i++) {
        unitList.add(String.format("%d %s", i, unitName));
      }
      PiecesPopUpAdapter piecesPopUpAdapter = new PiecesPopUpAdapter(unitList, this);
      mPopupWindow = new ListPopupWindow(requireActivity());
      mPopupWindow.setAdapter(piecesPopUpAdapter);
      mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
      mPopupWindow.setDropDownGravity(Gravity.BOTTOM);
      mPopupWindow.setOverlapAnchor(FALSE);
      mPopupWindow.setAnchorView(appCompatTextView);
      if (!mPopupWindow.isShowing()) {
        mPopupWindow.show();
        mCartProductItemData.get(pos).setEnable(FALSE);
        mCartAdapter.notifyItemChanged(pos);
      }
      mPopupWindow.setOnDismissListener(() -> {
        mCartProductItemData.get(pos).setEnable(TRUE);
        mCartAdapter.notifyItemChanged(pos);
      });
    } else {
      switch (action) {
        case THREE:
          mViewModel.callUpdateCartApi(action, ZERO,
              mCartProductItemData.get(mPosition).getCentralProductId(),
              mCartProductItemData.get(mPosition).getId(),
              mCartProductItemData.get(mPosition).getUnitId(),
              mCartProductItemData.get(mPosition).getStoreId());
          break;
        case FOUR:
          startAllDetailsAct(mCartProductItemData.get(mPosition).getAttributes());
          break;
      }
    }
  }

  @Override
  public void onItemClick(String value) {
    Utilities.printLog("exe" + "value" + value);
    if (mPopupWindow.isShowing()) {
      mPopupWindow.dismiss();
      String[] quantity = value.split("\\s+");
      mCartProductItemData.get(mPosition).getQuantity().setValue(quantity[ZERO]);
      mCartProductItemData.get(mPosition).getQuantity().setUnit(quantity[ONE]);
      mCartAdapter.notifyItemChanged(mPosition);
      mViewModel.callUpdateCartApi(TWO, Integer.parseInt(quantity[ZERO]),
          mCartProductItemData.get(mPosition).getCentralProductId(),
          mCartProductItemData.get(mPosition).getId(),
          mCartProductItemData.get(mPosition).getUnitId(),
          mCartProductItemData.get(mPosition).getStoreId());
    }
  }

  /**
   * start all details activity
   */
  private void startAllDetailsAct(ArrayList<CartAttributesData> cartAttributesData) {
   /* Intent intent = new Intent(getActivity(), AllDetailsActivity.class);
    intent.putExtra(IS_FROM_CART, TRUE);
    intent.putExtra(SPECIFICATIONS, getResources().getString(R.string.customizations));
    intent.putExtra(ATTRIBUTES_DATA, cartAttributesData);
    startActivity(intent);*/
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
      @NotNull String[] permissions, @NotNull int[] grantResults) {
    switch (requestCode) {
      case REQUEST_CODE_PERMISSION_MULTIPLE: {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ((
            requireActivity().checkSelfPermission(
                ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED))) {
          Thread thread = new Thread() {
            @Override
            public void run() {
              if (!mAddressHandler.isAddressListEmpty()) {
                if (mAddressHandler.getDefaultAddress().getName() != null
                    && !mAddressHandler.getDefaultAddress().getName().isEmpty()) {
                  new Handler(Looper.getMainLooper()).post(() -> {
                    mBinding.tvCartDeliverName.setText(
                        mAddressHandler.getDefaultAddress().getName());
                    mBinding.tvCartBillingName.setText(
                        mAddressHandler.getDefaultAddress().getName());
                  });
                } else {
                  new Handler(Looper.getMainLooper()).post(() -> {
                    requireActivity().runOnUiThread(
                        () -> mBinding.tvCartDeliverName.setVisibility(View.GONE));
                    requireActivity().runOnUiThread(
                        () -> mBinding.tvCartBillingName.setVisibility(View.GONE));
                  });
                }
                mViewModel.addressId = mAddressHandler.getDefaultAddress().getId();
                mViewModel.billingAddressId = mAddressHandler.getDefaultAddress().getId();
                mViewModel.cityId = mAddressHandler.getDefaultAddress().getCityId();
                mViewModel.countryId = mAddressHandler.getDefaultAddress().getCountryId();
              }
              mViewModel.getAddress(Utilities.getIpAddress(getContext()));
            }
          };
          thread.start();
        } else {
          accessLocation();
        }
        break;
      }
    }
  }

  /**
   * start promo code activity
   */
  private void promoCodeAct(String countryId, String cityId, String cartId,
      String storeId) {
  /*  Intent intent = new Intent(getActivity(), PromoCodesActivity.class);
    intent.putExtra(COUNTRY_ID, countryId);
    intent.putExtra(CITY_ID, cityId);
    intent.putExtra(CART_ID, cartId);
    intent.putExtra(STORE_ID, storeId);
    startActivityForResult(intent, APPLY_PROMO);*/
  }

  /**
   * start manage address activity
   */
  public void manageAddress(boolean isScheduleBookingAllowed) {
    String totalAmtToBePaid = String.format("%s,%s", mCartData.getCurrencyCode(),
        mCartData.getAccounting().getFinalTotal());
    Intent intent = new Intent(getActivity(), (SavedAddressListActivity.class));
    intent.putExtra(IS_FROM_CART, TRUE);
    intent.putExtra(TOTAL_STORE_COUNT, getTotalSellersCount());
    intent.putExtra(IS_DELIVER_NOW, TRUE);
    intent.putExtra(STORE_CATEGORY_ID, mViewModel.storeCategoryId);
    intent.putExtra(IS_SLOTS_AVAILABLE, isScheduleBookingAllowed);
    intent.putExtra(REQUESTED_TIME_PICKUP, getCurrentDate());
    intent.putExtra(STORE_COUNT, ZERO);
    intent.putExtra(REQUESTED_TIME, getCurrentDate());
    intent.putParcelableArrayListExtra(CART_ITEM, mCartSellersData);
    intent.putExtra(DELIVERY_DISPLAY_TEXT, getTodayDate());
    intent.putParcelableArrayListExtra(PICKUP_SLOT_ID, getEmptySlotId());
    intent.putParcelableArrayListExtra(DELIVERY_SLOT_ID, getEmptySlotId());
    intent.putExtra(CART_AMT, totalAmtToBePaid);
    intent.putExtra(CART_ID, mViewModel.mCartId);
    intent.putExtra(COMING_FROM, CART_SCREEN);
    startActivity(intent);
  }

  /**
   * start manage address activity
   */
  public void changeAddress() {
    Intent intent = new Intent(getActivity(), SavedAddressListActivity.class);
    intent.putExtra(IS_FROM_CART, TRUE);
    startActivityForResult(intent, CHANGE_ADDRESS_REQUEST);
  }

  /**
   * set pickup slot Ids to default zero if slot selection is unavailable
   *
   * @return ArrayList
   */
  private ArrayList<DeliverySlot> getEmptySlotId() {
    return new ArrayList<DeliverySlot>();
  }

  /**
   * start manage address activity
   */
  public void addBillingAddress() {
    Intent intent = new Intent(getActivity(), SavedAddressListActivity.class);
    intent.putExtra(IS_FROM_CART, TRUE);
    startActivityForResult(intent, BILLING_ADDRESS_REQUEST);
  }

  /**
   * start manage address activity
   */
  private void loginActivity() {
//    startActivityForResult(new Intent(getActivity(), EcomLoginActivity.class),
//        LOGIN_RC);
    AppController.getInstance().openSignInDialog(getActivity());
  }

  /**
   * start manage address activity
   */
  private void doLogin() {
    //startActivityForResult(new Intent(getActivity(), EcomLoginActivity.class), LOGIN);
    AppController.getInstance().openSignInDialog(getActivity());
  }

  /**
   * start payment activity
   */
  private void paymentActivity() {
    String totalAmtToBePaid =
        String.format("%s,%s", mCartData.getCurrencyCode(),
            mCartData.getAccounting().getFinalTotal());
    Intent intent = new Intent(getActivity(), PaymentMethodActivity.class);
    intent.putExtra(CART_AMT, totalAmtToBePaid);
    intent.putExtra(COMING_FROM, mConfirmOrder ? ORDER_SCREEN : CART_SCREEN);
    if (mConfirmOrder) {
      startActivityForResult(intent, PAYMENT_METHOD_REQ_CODE);
    } else {
      startActivity(intent);
    }
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == RESULT_OK && data != null) {
      switch (requestCode) {
        case MANGE_ADDRESS_REQUEST:
          AddressListItemData addressListItemData = data.getParcelableExtra(ADDRESS_LIST_DATA);
          String phoneNum = String.format("+%s %s",
              addressListItemData.getMobileNumberCode(),
              addressListItemData.getMobileNumber());
          mBinding.tvCartDelAddress.setText(
              String.format("%s%s,%s,%s", addressListItemData.getAddLine1(),
                  addressListItemData.getLandmark(), addressListItemData.getCity(),
                  String.format("%s\n%s:%s", addressListItemData.getPincode(),
                      getResources().getString(
                          R.string.phoneNumber), phoneNum)));
          mBinding.tvCartDeliverType.setText(
              String.format("%s", addressListItemData.getTaggedAs()));
          mBinding.tvCartDeliverName.setVisibility(View.VISIBLE);
          mViewModel.addressId = addressListItemData.getId();
          mBinding.tvCartDeliverName.setText(addressListItemData.getName());
          mViewModel.changeVisible.set(TRUE);
          mViewModel.addAddressVisible.set(FALSE);
          paymentActivity();
          break;
        case CHANGE_ADDRESS_REQUEST:
          AddressListItemData changeAddressListItemData = data.getParcelableExtra(
              ADDRESS_LIST_DATA);
          String changePhoneNum = String.format("+%s %s",
              changeAddressListItemData.getMobileNumberCode(),
              changeAddressListItemData.getMobileNumber());
          mBinding.tvCartDelAddress.setText(
              String.format("%s%s,%s,%s", changeAddressListItemData.getAddLine1(),
                  changeAddressListItemData.getLandmark(), changeAddressListItemData.getCity(),
                  changeAddressListItemData.getPincode() + "\n" + getResources().getString(
                      R.string.phoneNumber) + ":" + changePhoneNum));
          mBinding.tvCartDeliverType.setText(
              String.format("%s", changeAddressListItemData.getTaggedAs()));
          mBinding.tvCartDeliverName.setVisibility(View.VISIBLE);
          mViewModel.addressId = changeAddressListItemData.getId();
          mBinding.tvCartDeliverName.setText(changeAddressListItemData.getName());
          mViewModel.changeVisible.set(TRUE);
          mViewModel.addAddressVisible.set(FALSE);
          break;
        case BILLING_ADDRESS_REQUEST:
          AddressListItemData billingAddressListItemData = data.getParcelableExtra(
              ADDRESS_LIST_DATA);
          String billingAddPhoneNum = String.format("+%s %s",
              billingAddressListItemData.getMobileNumberCode(),
              billingAddressListItemData.getMobileNumber());
          mBinding.tvCartBillingAddress.setText(
              String.format("%s%s,%s,%s", billingAddressListItemData.getAddLine1(),
                  billingAddressListItemData.getLandmark(), billingAddressListItemData.getCity(),
                  String.format("%s\n%s:%s", billingAddressListItemData.getPincode(),
                      getResources().getString(
                          R.string.phoneNumber), billingAddPhoneNum)));
          mBinding.tvCartBillingName.setVisibility(View.VISIBLE);
          mViewModel.billingAddressId = billingAddressListItemData.getId();
          mViewModel.cityId = billingAddressListItemData.getCityId();
          mViewModel.countryId = billingAddressListItemData.getCountryId();
          mBinding.tvCartBillingName.setText(billingAddressListItemData.getName());
          mViewModel.billingAddVisible.set(TRUE);
          break;
        case LOGIN_RC:
          changeAddress();
          break;
        case LOGIN:
          mViewModel.placeOrder();
          break;
        case APPLY_PROMO:
          String promoCode = data.getStringExtra(PROMO_CODE);
          String promoCodeId = data.getStringExtra(PROMO_CODE_ID);
          mBinding.tvPromoCode.setText(promoCode);
          mViewModel.coupon = promoCode;
          mViewModel.couponId = promoCodeId;
          mViewModel.applyPromoCodeApi(mCartData.getCurrencySymbol(),
              Float.parseFloat(mCartData.getAccounting().getUnitPrice()),
              Float.parseFloat(mCartData.getAccounting().getDeliveryFee()),
              mCartData.getCurrencyCode(), getProductPromoList());
          Utilities.printLog("exe" + "promoCode" + promoCode);
          break;

        case SCHEDULE_DELIVERY_REQ_CODE:
          if (data.getStringExtra(DELIVERY_DISPLAY_TEXT) != null) {
            mViewModel.mDeliveryDateAndTime = data.getStringExtra(DELIVERY_DISPLAY_TEXT);
            mViewModel.mIsDeliveryScheduled = true;
            mViewModel.requestedTime = data.getStringExtra(REQUESTED_TIME);
            mViewModel.isDeliverNow = data.getBooleanExtra(IS_DELIVER_NOW, FALSE);
            mViewModel.whichDay = data.getIntExtra(WHICH_DATE_SELECTED, ZERO);
            mViewModel.whichSlot = data.getIntExtra(WHICH_SLOT_SELECTED, ZERO);
            mViewModel.requestedTimePickup = data.getStringExtra(REQUESTED_TIME_PICKUP);
            mViewModel.pickupSlotId = data.getParcelableArrayListExtra(PICKUP_SLOT_ID);
            mViewModel.deliverySlotId = data.getParcelableArrayListExtra(DELIVERY_SLOT_ID);
            mBinding.tvDeliveryScheduleInfo.setText(data.getStringExtra(DELIVERY_DISPLAY_TEXT));
          }
          break;

        case CONTACT_LESS_DELIVERY_REQ_CODE:
          if (data.getStringExtra(SELECTED_CONTACTLESS_DELIVERY_TYPE) != null) {
            mBinding.tvContactLessDeliveryInfo.setText(
                data.getStringExtra(SELECTED_CONTACTLESS_DELIVERY_TYPE));
            mViewModel.contactLessDeliveryReason = data.getStringExtra(
                SELECTED_CONTACTLESS_DELIVERY_TYPE_ID);
            mViewModel.contactLessDelivery = TRUE;
          }
          break;

        case EXTRA_NOTES_REQ_CODE:
          if (data.getStringExtra(EXTRA_NOTES) != null) {
            String notes = data.getStringExtra(EXTRA_NOTES);
            boolean isValidNote = !notes.trim().isEmpty()
                && !notes.equals(getResources().getString(R.string.extraNotesText));
            mViewModel.extraNote = isValidNote ? data.getStringExtra(EXTRA_NOTES) : EMPTY_STRING;
            mBinding.tvDineExtraNote.setText(isValidNote
                ? data.getStringExtra(EXTRA_NOTES)
                : getResources().getString(R.string.extraNotesText));
            mBinding.tvExtraNoteText.setText(isValidNote
                ? data.getStringExtra(EXTRA_NOTES)
                : getResources().getString(R.string.extraNotesText));
            mBinding.tvExtraNoteText.setTextColor(ContextCompat.getColor(getContext(),
                (isValidNote ? R.color.color_black : R.color.colorSilverWhite)));
            mBinding.tvDineExtraNote.setTextColor(ContextCompat.getColor(getContext(),
                (isValidNote ? R.color.color_black : R.color.colorSilverWhite)));
          }
          break;

        case PAYMENT_METHOD_REQ_CODE:
          mViewModel.cardId = data.getStringExtra(CARD_ID);
          mViewModel.paymentType = data.getIntExtra(PAYMENT_TYPE, ZERO);
          mViewModel.payByWallet = data.getBooleanExtra(PAYMENT_BY_WALLET, FALSE);
          mViewModel.isPayByWallet = data.getBooleanExtra(PAYMENT_BY_WALLET, FALSE);
          float totalAmt = data.getFloatExtra(AMOUNT, 0f);
          int paymentType = data.getIntExtra(PAYMENT, ZERO);
          float walletAmt = data.getFloatExtra(WALLET_AMT, 0f);
          String currency = data.getStringExtra(CURRENCY);
          String cardNum = data.getStringExtra(CARD_NUMBER);
          setPaymentMethod(totalAmt, paymentType, walletAmt, currency, cardNum);
          if ((mViewModel.paymentType == PAYMENT_TYPE_CARD || mViewModel.payByWallet)
              && mTotalStoreCount == ONE) {
            mBinding.vgTip.setVisibility(View.VISIBLE);
            if (mTips != null && mTips.size() > ZERO) {
              initializeTips();
            } else {
              //mViewModel.callGetTipApi();
              subscribeTipData();
            }
          } else {
            mBinding.vgTip.setVisibility(View.GONE);
          }
          break;

        default:
          break;
      }
    }
  }

  /**
   * subscribe for onError
   */
  private void subscribeOnPromoData() {
    mViewModel.getPromoDataMutableLiveData().observe(getActivity(),
        applyPromoCodeData -> {
          mApplyPromoCodeData = applyPromoCodeData;
          mBinding.accoutingDetails.vgPromotionsApplied.setVisibility(View.VISIBLE);
          mBinding.accoutingDetails.tvCartPromotionsAmt.setText(
              String.format("-%s%s", mCartData.getCurrencySymbol(),
                  String.format("%.2f", mApplyPromoCodeData.getReducedAmt())));
          float totalAmt = Float.parseFloat(mCartData.getAccounting().getFinalTotal());
          float finalTotalAmt = totalAmt - mApplyPromoCodeData.getReducedAmt();
          mBinding.accoutingDetails.tvCartOrderTotalAmt.setText(
              String.format("%s%s", mCartData.getCurrencySymbol(),
                  String.format("%.2f", finalTotalAmt)));
          float yourSavingsAmt = ((float) mCartData.getAccounting().getOfferDiscount())
              + mApplyPromoCodeData.getReducedAmt();
          mBinding.accoutingDetails.tvCartTotalSavingsAmt.setText(
              String.format("%s%s", mCartData.getCurrencySymbol(),
                  String.format("%.2f", yourSavingsAmt)));
          if (mPaymentType == WALLET_PLUS_CARD) {
            float walletAmt = getArguments().getFloat(WALLET_AMT);
            mBinding.tvCardNumberAmt.setText(
                String.format("%s %s", mCartData.getCurrencySymbol(),
                    String.format("%.2f",
                        (totalAmt - walletAmt - mApplyPromoCodeData.getReducedAmt()))));
          } else if (mPaymentType == CARD) {
            mBinding.tvCardNumberAmt.setText(
                String.format("%s%s", mCartData.getCurrencySymbol(),
                    String.format("%.2f", finalTotalAmt)));
          }
        });
  }

  /**
   * subscribe for onError
   */
  private void subscribeOnError() {
    mViewModel.onError().observe(getActivity(), this::onError);
  }

  /**
   * it will show snack bar message
   *
   * @param error string mError message
   */
  public void onError(String error) {
    Log.d("exe", "error" + error);
    Snackbar.make(mBinding.clCart, error, Snackbar.LENGTH_SHORT).show();
  }

  /**
   * <p>This method is used to show the success dialog for 2 seconds.</p>
   */
  public void onSuccess() {
    Utilities.hideSoftKeyboard(mBinding.clCart);
    mBinding.successCheckOut.sucecssTv.setText(
        getResources().getString(R.string.confirmOrderlSuccess));
    mBinding.successCheckOut.tvDesc.setVisibility(View.GONE);
    mBinding.includeCartHeader.clActionBar.setVisibility(View.GONE);
    mBinding.clCartFooter.setVisibility(View.GONE);
    mBinding.successCheckOut.clSuccess.setVisibility(View.VISIBLE);
    ((Animatable) mBinding.successCheckOut.successTick.getDrawable()).start();
    CountDownTimer countDownTimer = new CountDownTimer(THOUSAND * TWO, THOUSAND) {
      @Override
      public void onTick(long millisUntilFinished) {
      }

      @Override
      public void onFinish() {
          Intent intent = new Intent(getActivity(), LandingActivity.class);
          TaskStackBuilder.create(getActivity())
              .addParentStack(LandingActivity.class)
              .addNextIntent(intent)
              .startActivities();


      }
    };
    countDownTimer.start();
  }

  /**
   * This method used set the payment method
   */
  private void setPaymentMethod(float totalAmt) {
    mPaymentType = getArguments().getInt(PAYMENT);
    float walletAmt = getArguments().getFloat(WALLET_AMT);
    String currency = getArguments().getString(CURRENCY);
    String cardNum = getArguments().getString(CARD_NUMBER);
    if (mConfirmOrder) {
      switch (mPaymentType) {
        case ONE:
          mBinding.vgWallet.setVisibility(View.VISIBLE);
          mBinding.vgCardMethod.setVisibility(View.VISIBLE);
          mBinding.tvCashOnDel.setVisibility(View.GONE);
          mBinding.tvCardNumber.setText(
              String.format("%s(%s %s)", getResources().getString(R.string.card),
                  getResources().getString(R.string.ending), cardNum));
          mBinding.tvCardNumberAmt.setText(
              String.format("%s %s", currency, String.format("%.2f", (totalAmt - walletAmt))));
          if (walletAmt > totalAmt) {
            mBinding.vgCardMethod.setVisibility(View.GONE);
          }
          mBinding.tvWalletAmt.setText(
              String.format("%s %s", currency, String.format("%.2f", walletAmt)));
          break;
        case TWO:
          mBinding.vgWallet.setVisibility(View.VISIBLE);
          mBinding.tvCashOnDel.setVisibility(View.VISIBLE);
          mBinding.vgCardMethod.setVisibility(View.GONE);
          mBinding.tvWalletAmt.setText(
              String.format("%s %s", currency, String.format("%.2f", walletAmt)));
          break;
        case THREE:
          mBinding.vgCardMethod.setVisibility(View.GONE);
          mBinding.vgWallet.setVisibility(View.VISIBLE);
          mBinding.tvCashOnDel.setVisibility(View.GONE);
          mBinding.tvWalletAmt.setText(
              String.format("%s %s", currency, String.format("%.2f", walletAmt)));
          break;
        case FOUR:
          mBinding.vgCardMethod.setVisibility(View.VISIBLE);
          mBinding.vgWallet.setVisibility(View.GONE);
          mBinding.tvCashOnDel.setVisibility(View.GONE);
          mBinding.tvCardNumber.setText(
              String.format("%s(%s %s)", getResources().getString(R.string.card),
                  getResources().getString(R.string.ending), cardNum));
          mBinding.tvCardNumberAmt.setText(
              String.format("%s %s", currency, String.format("%.2f", totalAmt)));
          break;
        case FIVE:
          mBinding.tvCashOnDel.setVisibility(View.VISIBLE);
          mBinding.vgCardMethod.setVisibility(View.GONE);
          mBinding.vgWallet.setVisibility(View.GONE);
          break;
      }
    }
  }

  /**
   * This method used set the payment method
   */
  private void setPaymentMethod(float totalAmt, int paymentType, float walletAmt, String currency,
      String cardNum) {
    if (mConfirmOrder) {
      switch (paymentType) {
        case ONE:
          mBinding.vgWallet.setVisibility(View.VISIBLE);
          mBinding.vgCardMethod.setVisibility(View.VISIBLE);
          mBinding.tvCashOnDel.setVisibility(View.GONE);
          mBinding.tvCardNumber.setText(
              String.format("%s(%s %s)", getResources().getString(R.string.card),
                  getResources().getString(R.string.ending), cardNum));
          mBinding.tvCardNumberAmt.setText(
              String.format("%s %s", currency, String.format("%.2f", (totalAmt - walletAmt))));
          if (walletAmt > totalAmt) {
            mBinding.vgCardMethod.setVisibility(View.GONE);
          }
          mBinding.tvWalletAmt.setText(
              String.format("%s %s", currency, String.format("%.2f", walletAmt)));
          break;
        case TWO:
          mBinding.vgWallet.setVisibility(View.VISIBLE);
          mBinding.tvCashOnDel.setVisibility(View.VISIBLE);
          mBinding.vgCardMethod.setVisibility(View.GONE);
          mBinding.tvWalletAmt.setText(
              String.format("%s %s", currency, String.format("%.2f", walletAmt)));
          break;
        case THREE:
          mBinding.vgCardMethod.setVisibility(View.GONE);
          mBinding.vgWallet.setVisibility(View.VISIBLE);
          mBinding.tvCashOnDel.setVisibility(View.GONE);
          mBinding.tvWalletAmt.setText(
              String.format("%s %s", currency, String.format("%.2f", walletAmt)));
          break;
        case FOUR:
          mBinding.vgCardMethod.setVisibility(View.VISIBLE);
          mBinding.vgWallet.setVisibility(View.GONE);
          mBinding.tvCashOnDel.setVisibility(View.GONE);
          mBinding.tvCardNumber.setText(
              String.format("%s(%s %s)", getResources().getString(R.string.card),
                  getResources().getString(R.string.ending), cardNum));
          mBinding.tvCardNumberAmt.setText(
              String.format("%s %s", currency, String.format("%.2f", totalAmt)));
          break;
        case FIVE:
          mBinding.tvCashOnDel.setVisibility(View.VISIBLE);
          mBinding.vgCardMethod.setVisibility(View.GONE);
          mBinding.vgWallet.setVisibility(View.GONE);
          break;
      }
    }
  }

  @Override
  public void onSelectItem(int position) {
    Intent intent = new Intent(getActivity(), ProductDetailsActivity.class);
    intent.putExtra(PARENT_PRODUCT_ID, mCartProductItemData.get(position).getCentralProductId());
    intent.putExtra(PRODUCT_ID, mCartProductItemData.get(position).getId());
    startActivity(intent);
  }

  /**
   * this method used to get the product details for promo.
   *
   * @return return the product list.
   */
  private ArrayList<ProductPromoInput> getProductPromoList() {
    ArrayList<ProductPromoInput> productPromoInputs = new ArrayList<>();
    if (!isEmptyArray(mCartProductItemData)) {
      for (CartProductItemData details : mCartProductItemData) {
        if (details.getId() != null) {
          ProductPromoInput productPromoInput = new ProductPromoInput();
          productPromoInput.setProduct_id(details.getId());
          productPromoInput.setName(details.getName());
          productPromoInput.setBrand_name(details.getBrandName());
          productPromoInput.setPrice(Float.parseFloat(details.getAccounting().getFinalTotal()));
          productPromoInput.setDelivery_fee(
              Float.parseFloat(details.getDeliveryDetails().getDeliveryFee()));
          productPromoInput.setCentralproduct_id(details.getCentralProductId());
          productPromoInput.setUnitPrice(Float.parseFloat(details.getAccounting().getUnitPrice()));
          productPromoInput.setTaxAmount(Float.parseFloat(details.getAccounting().getTaxAmount()));
          productPromoInput.setCategory_id(null);
          productPromoInput.setTaxRequests(null);
          productPromoInput.setCityId(mViewModel.cityId);
          ArrayList<String> storeIdList = new ArrayList<>();
          storeIdList.add(details.getStoreId());
          productPromoInput.setStore_id(storeIdList);
          productPromoInput.setQuantityData(details.getQuantity());
          productPromoInputs.add(productPromoInput);
        }
        Utilities.printLog("exe" + "storeId" + details.getStoreId());
      }
    }
    return productPromoInputs;
  }

  String getStoreId() {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < mCartProductItemData.size(); i++) {
      if (mCartProductItemData.get(i).getStoreId() != null) {
        stringBuilder.append(mCartProductItemData.get(i).getStoreId()).append(",");
      }
    }
    String storeIdValue = "";
    if (stringBuilder.toString().length() > ZERO) {
      storeIdValue = stringBuilder.toString().substring(ZERO,
          stringBuilder.toString().length() - ONE);
    }
    return storeIdValue;
  }
}