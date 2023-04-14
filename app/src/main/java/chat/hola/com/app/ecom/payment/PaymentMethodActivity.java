package chat.hola.com.app.ecom.payment;

import static chat.hola.com.app.Utilities.Constants.ADDRESS_LIST_DATA;
import static chat.hola.com.app.Utilities.Constants.ADD_CARD_RC;
import static chat.hola.com.app.Utilities.Constants.ADD_MONEY_RC;
import static chat.hola.com.app.Utilities.Constants.AMOUNT;
import static chat.hola.com.app.Utilities.Constants.BRAND_LOGO;
import static chat.hola.com.app.Utilities.Constants.CARD_ID;
import static chat.hola.com.app.Utilities.Constants.CARD_NUMBER;
import static chat.hola.com.app.Utilities.Constants.CARD_PAYMENT_TYPE;
import static chat.hola.com.app.Utilities.Constants.CART_AMT;
import static chat.hola.com.app.Utilities.Constants.CASH_PAYMENT_TYPE;
import static chat.hola.com.app.Utilities.Constants.CONFIRM_ORDER;
import static chat.hola.com.app.Utilities.Constants.CURRENCY;
import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.FIVE;
import static chat.hola.com.app.Utilities.Constants.FOUR;
import static chat.hola.com.app.Utilities.Constants.IS_DELIVER_NOW;
import static chat.hola.com.app.Utilities.Constants.ORDER_SCREEN;
import static chat.hola.com.app.Utilities.Constants.PAYMENT;
import static chat.hola.com.app.Utilities.Constants.PAYMENT_BY_WALLET;
import static chat.hola.com.app.Utilities.Constants.PAYMENT_TYPE;
import static chat.hola.com.app.Utilities.Constants.THREE;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static chat.hola.com.app.Utilities.Constants.WALLET_AMT;
import static chat.hola.com.app.Utilities.Constants.WALLET_PAYMENT_TYPE;
import static chat.hola.com.app.Utilities.Constants.WALLET_PLUS_CARD;
import static chat.hola.com.app.Utilities.Constants.WALLET_PLUS_CASH;
import static chat.hola.com.app.Utilities.Constants.WHICH_DATE_SELECTED;
import static chat.hola.com.app.Utilities.Constants.WHICH_SLOT_SELECTED;
import static chat.hola.com.app.Utilities.Constants.ZERO;
import static com.appscrip.myapplication.utility.Constants.COMING_FROM;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.TWO;
import static com.appscrip.stripe.Constants.AUTHORIZATION;
import static com.appscrip.stripe.Constants.CARD;
import static com.appscrip.stripe.Constants.ENGLISH;
import static com.appscrip.stripe.Constants.LANGUAGE;
import static com.appscrip.stripe.Constants.USER_ID;
import static com.kotlintestgradle.data.utils.DataConstants.EMPTY_STRING;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.ecom.addresslist.PrescriptionModel;
import chat.hola.com.app.ecom.cart.EcomCartActivity;
import chat.hola.com.app.ecom.cart.SelectItem;
import com.appscrip.stripe.StripePaymentIntentActivity;
import com.ezcall.android.R;
import com.ezcall.android.databinding.ActivityPaymentMethodBinding;
import com.google.android.material.snackbar.Snackbar;
import com.kotlintestgradle.model.ecom.getaddress.AddressListItemData;
import com.kotlintestgradle.model.ecom.slots.DeliverySlot;
import dagger.android.support.DaggerAppCompatActivity;
import java.util.ArrayList;
import java.util.Objects;
import javax.inject.Inject;

/**
 * select the payment method like wallet,cards and cash delivery.
 */
public class PaymentMethodActivity extends DaggerAppCompatActivity implements SelectItem {
  ActivityPaymentMethodBinding mBinding;
  @Inject
  androidx.lifecycle.ViewModelProvider.Factory mViewModelFactory;
  private PaymentMethodViewModel mPaymentMethodViewModel;
  private ArrayList<SavedCardsData> mSavedCardsData = new ArrayList<>();
  private SavedCardsAdapter mSavedCardsAdapter;
  //private BusinessAccountAdapter mAdapter;
  private int mPosition;
  private String mRequestedTimePickup = "";
  private String mRequestedTime = "";
  private boolean mIsDeliverNow;
  private int mTotalStoreCount = ZERO;
  private ArrayList<DeliverySlot> mPickupSlotId = new ArrayList<>();
  private ArrayList<DeliverySlot> mDeliverySlotId = new ArrayList<>();
  private AddressListItemData mAddressListItemData;
  private ArrayList<PrescriptionModel> mPrescriptionList = new ArrayList<>();
  private String mDeliveryDisplayText = "";
  private int mWhichDay, mWhichSlot;
  String currency = " ";

  @Override
  protected void onCreate(android.os.Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initializeView();
    subScribeWalletData();
    subScribeSavedCards();
    subscribeSavedCardsUnSelect();
    subscribeOnClick();
    mPaymentMethodViewModel.callGetWalletApi();
  }

  @Override
  protected void onResume() {
    super.onResume();
    changeUI(true);
  }

  /**
   * Initialising the View using Data Binding
   */
  @SuppressLint("DefaultLocale")
  private void initializeView() {
    mBinding = DataBindingUtil.setContentView(this, R.layout.activity_payment_method);
    mPaymentMethodViewModel = ViewModelProviders.of(this, mViewModelFactory).get(
        PaymentMethodViewModel.class);
    mBinding.setViewmodel(mPaymentMethodViewModel);
    //mAdapter = new BusinessAccountAdapter(this, mAccountList);
    //mBinding.rvBusinessAccounts.setAdapter(mAdapter);
    mSavedCardsAdapter = new SavedCardsAdapter(this::onSelectItem, mSavedCardsData, TRUE);
    mBinding.rvSavedCards.setAdapter(mSavedCardsAdapter);
    if (getIntent() != null && getIntent().getStringExtra(COMING_FROM) != null) {
      mPaymentMethodViewModel.comingFrom = getIntent().getStringExtra(COMING_FROM);
    }
    mBinding.groupCorporate.setVisibility(android.view.View.GONE);
    mBinding.includePaymentMethodHeader.tvTitle.setText(
        getResources().getString(R.string.allPaymentMethod));
    String[] cartAmount = getCartAmount(getIntent().getStringExtra(CART_AMT));
    mBinding.tvPmAmt.setText(
        String.format("%s %s", cartAmount[ZERO],
            String.format("%.2f",
                Float.parseFloat(cartAmount[ONE]))));
    mPaymentMethodViewModel.amtToBePaid = Float.parseFloat(cartAmount[ONE]);
    mPaymentMethodViewModel.toCurrency = cartAmount[ZERO];
    android.util.Log.d("PAYAEMY", mPaymentMethodViewModel.amtToBePaid + "");

    if (getIntent() != null) {
      mWhichDay = getIntent().getIntExtra(WHICH_DATE_SELECTED, ZERO);
      mWhichSlot = getIntent().getIntExtra(WHICH_SLOT_SELECTED, ZERO);
    }
    mBinding.groupCorporate.setVisibility(android.view.View.GONE);
    if (getIntent().getStringExtra(CART_AMT) != null) {
      String[] cartAmt = getCartAmount(getIntent().getStringExtra(CART_AMT));
      mBinding.tvPmAmt.setText(
          String.format("%s %s", cartAmt[ZERO],
              String.format("%.2f", Float.parseFloat(cartAmt[ONE]))));
      mPaymentMethodViewModel.amtToBePaid = Float.parseFloat(Objects.requireNonNull(cartAmt[ONE]));
      android.util.Log.d("PAYAEMY", mPaymentMethodViewModel.amtToBePaid + "");
    }

    mBinding.includePaymentMethodHeader.ivBackBtn.setImageResource(
        (com.ezcall.android.R.drawable.all_back));
    if (getIntent() != null && getIntent().getParcelableExtra(ADDRESS_LIST_DATA) != null) {
      mAddressListItemData = getIntent().getParcelableExtra(ADDRESS_LIST_DATA);
    }
    mBinding.includePaymentMethodHeader.ivBackBtn.setOnClickListener(v -> {
      onBackPressed();
    });
  }

  /**
   * return the cart amount
   *
   * @param extra String
   * @return String[]
   */
  private String[] getCartAmount(String extra) {
    String[] input = new String[]{EMPTY_STRING, EMPTY_STRING};
    try {
      input = extra.split(",");
      if (input.length < TWO) {
        try {
          input = extra.split("\\s+");
        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    } catch (Exception e) {
      try {
        input = extra.split("\\s+");
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    }
    return input;
  }

  /**
   * subscribe to saved cards
   */
  private void subScribeSavedCards() {
    mPaymentMethodViewModel.getSavedCardsLIveData().observe(this,
        savedCardsData -> {
          mSavedCardsData.clear();
          mSavedCardsData.addAll(savedCardsData);
          mSavedCardsAdapter.notifyDataSetChanged();
        });
  }

  /**
   * subscribe to wallet data
   */
  private void subScribeWalletData() {
    mPaymentMethodViewModel.getWalletData().observe(this, walletBal -> {
      mBinding.tvWalletAmt.setText(walletBal);
    });
  }

  /**
   * subscribe for the back button on click
   */
  private void subscribeOnClick() {
    mPaymentMethodViewModel.onClick().observe(this, paymentUiAction -> {
      switch (paymentUiAction) {
        case BACK:
          onBackPressed();
          break;
        case ADD_CARD:
          startStripePaymentAct();
          break;
        case ADD_MONEY:
      /*    Intent addMoneyIntent = new Intent(this, EcomAddMoneyActivity.class);
          startActivityForResult(addMoneyIntent, ADD_MONEY_RC);*/
          break;
        case WALLET_PLUS_CARD:
          Utilities.PAYMENT_TYPE_VALUE = WALLET_PLUS_CARD;
          startGoToCartAct(mSavedCardsData.get(mPosition).getId(), CARD_PAYMENT_TYPE, TRUE, ONE);
          break;
        case WALLET_PLUS_CASH:
          Utilities.PAYMENT_TYPE_VALUE = WALLET_PLUS_CASH;
          startGoToCartAct("", CASH_PAYMENT_TYPE, TRUE, TWO);
          break;
        case WALLET:
          Utilities.PAYMENT_TYPE_VALUE = WALLET_PAYMENT_TYPE;
          startGoToCartAct("", TWO, TRUE, THREE);
          break;
        case CARD:
          Utilities.PAYMENT_TYPE_VALUE = CARD_PAYMENT_TYPE;
          startGoToCartAct(mSavedCardsData.get(mPosition).getId(), CARD_PAYMENT_TYPE, FALSE,
              FOUR);
          break;
        case CASH:
          Utilities.PAYMENT_TYPE_VALUE = CASH_PAYMENT_TYPE;
          startGoToCartAct("", CASH_PAYMENT_TYPE, FALSE, FIVE);
          break;
        case EMPTY:
          Toast.makeText(this, R.string.selectThePaymentType, Toast.LENGTH_SHORT).show();
          break;
        case DISABLE:
          disableViews();
          break;
        case ENABLE:
          enableViews();
          break;
        case DISABLE_CARDS:
          disableCardViews();
          break;
        case ENABLE_CARDS:
          enableCardViews();
          break;
        case BUSINESS:
          changeUI(false);
          break;
        case PERSONAL:
          changeUI(true);
          break;
      }
    });
  }

  /*change UI for Business and Personal Account*/
  private void changeUI(boolean b) {
    mBinding.groupPersonal.setVisibility(b ? android.view.View.VISIBLE : android.view.View.GONE);
    mBinding.btnPmContinue.setVisibility(b ? android.view.View.VISIBLE : android.view.View.GONE);
    mBinding.groupBusiness.setVisibility(!b ? android.view.View.VISIBLE : android.view.View.GONE);
    mBinding.rvBusinessAccounts.setVisibility(android.view.View.GONE);
    mBinding.tvBusinessAccount.setVisibility(android.view.View.GONE);
    mBinding.groupBusinessAccount.setVisibility(android.view.View.GONE);
    mBinding.btnAddAccount.setVisibility(!b ? android.view.View.VISIBLE : android.view.View.GONE);
    mBinding.tvPersonal.setSelected(b);
    mBinding.tvBusiness.setSelected(!b);
    mBinding.tvBusiness.setTextColor(b ? getResources().getColor(R.color.colorAccount) :
        getResources().getColor(R.color.white));
    mBinding.tvPersonal.setTextColor(b ? getResources().getColor(R.color.white) :
        getResources().getColor(R.color.colorAccount));
  }

  /**
   * <p>This method is used to show the snackBar message.</p>
   */
  private void showErrorMsg(String msg) {
    Snackbar.make(mBinding.clPaymentMethod, msg, Snackbar.LENGTH_SHORT).show();
  }

  /**
   * it will open the cart activity
   */
  private void startGoToCartAct(String cardId, int paymentType, boolean payByWallet, int payment) {
    Intent intent = new Intent(this, EcomCartActivity.class);
    android.os.Bundle bundle = new android.os.Bundle();
    if (mSavedCardsData != null && mSavedCardsData.size() > ZERO) {
      bundle.putString(CARD_ID, cardId);
      bundle.putString(BRAND_LOGO, mSavedCardsData.get(mPosition).getBrand());
      bundle.putString(CARD_NUMBER, mSavedCardsData.get(mPosition).getLast4());
    }
    bundle.putFloat(WALLET_AMT, mPaymentMethodViewModel.walletBal);
    bundle.putFloat(AMOUNT, mPaymentMethodViewModel.amtToBePaid);
    bundle.putInt(PAYMENT_TYPE, paymentType);
    bundle.putInt(WHICH_DATE_SELECTED, mWhichDay);
    bundle.putInt(WHICH_SLOT_SELECTED, mWhichSlot);
    bundle.putBoolean(IS_DELIVER_NOW, mIsDeliverNow);
    bundle.putInt(PAYMENT, payment);

    if (getIntent().getStringExtra(CART_AMT) != null) {
      bundle.putString(CURRENCY, getIntent().getStringExtra(CART_AMT).split(",")[ZERO]);
    }
    bundle.putBoolean(PAYMENT_BY_WALLET, payByWallet);
    bundle.putBoolean(CONFIRM_ORDER, TRUE);
    bundle.putParcelable(ADDRESS_LIST_DATA, mAddressListItemData);
    intent.putExtras(bundle);
    switch (mPaymentMethodViewModel.comingFrom) {
      case ORDER_SCREEN:
        setResult(RESULT_OK, intent);
        finish();
        break;
      default:
        startActivity(intent);
        break;
    }
  }

  /**
   * start payment act
   */
  private void startStripePaymentAct() {
    Intent intent = new Intent(this, StripePaymentIntentActivity.class);
    intent.putExtra(USER_ID, AppController.getInstance().getUserId());
    intent.putExtra(LANGUAGE, ENGLISH);
    intent.putExtra(AUTHORIZATION, AppController.getInstance().getApiToken());
    startActivityForResult(intent, ADD_CARD_RC);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode,
      @androidx.annotation.Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (data != null) {
      if (requestCode == ADD_MONEY_RC) {
        if (resultCode == Activity.RESULT_OK) {
          String cardId = data.getStringExtra(CARD_ID);
          String amt = data.getStringExtra(AMOUNT);
          String currency = data.getStringExtra(CURRENCY);
          mPaymentMethodViewModel.callAddMoneyToWalletApi(cardId, currency, amt);
          mPaymentMethodViewModel.getCards();
        }
      } else if (requestCode == ADD_CARD_RC) {
        if (resultCode == RESULT_OK) {
          String cardToken = data.getStringExtra(CARD);
          Utilities.printLog("exe" + "cardToken" + cardToken);
          mPaymentMethodViewModel.getCards();
        }
      }
    }
  }

  @Override
  public void onSelectItem(int position) {
    mPosition = position;
    SavedCardsData savedCardsData = mSavedCardsData.get(position);
    Utilities.CARD_BRAND = savedCardsData.getBrand();
    Utilities.SELECTED_CARD_ID = savedCardsData.getId();
    Utilities.SELECTED_CARD_LAST_FOUR_NO = savedCardsData.getLast4();
    mPaymentMethodViewModel.mIsChecked = savedCardsData.isChecked();
    if (mPaymentMethodViewModel.mIsChecked) {
      mPaymentMethodViewModel.payOnDeliveryState.set(FALSE);
      mPaymentMethodViewModel.btnEnabled.set(TRUE);
    } else {
      mPaymentMethodViewModel.selectCardButtonStatus();
    }
  }

  /**
   * subscribe selected card management
   */
  private void subscribeSavedCardsUnSelect() {
    mPaymentMethodViewModel.getDisSelectLIveData().observe(this, aBoolean -> {
      SavedCardsData savedCardsData = mSavedCardsData.get(mPosition);
      savedCardsData.setChecked(FALSE);
      mSavedCardsAdapter.notifyItemChanged(mPosition);
      mPaymentMethodViewModel.mIsChecked = FALSE;
    });
  }

  /**
   * disable views.
   */
  private void disableViews() {
    mSavedCardsAdapter.enabled(FALSE);
    mSavedCardsAdapter.notifyDataSetChanged();
    mBinding.tvPMPayOnDel.setEnabled(FALSE);
  }

  /**
   * disable views.
   */
  private void disableCardViews() {
    mSavedCardsAdapter.enabled(FALSE);
    mSavedCardsAdapter.notifyDataSetChanged();
  }

  /**
   * disable card views.
   */
  private void enableCardViews() {
    mSavedCardsAdapter.enabled(TRUE);
    mSavedCardsAdapter.notifyDataSetChanged();
  }

  /**
   * enable card views.
   */
  private void enableViews() {
    mSavedCardsAdapter.enabled(TRUE);
    mSavedCardsAdapter.notifyDataSetChanged();
    mBinding.tvPMPayOnDel.setEnabled(TRUE);
  }
}
