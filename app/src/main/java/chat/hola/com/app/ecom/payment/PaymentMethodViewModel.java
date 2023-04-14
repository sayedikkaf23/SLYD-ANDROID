package chat.hola.com.app.ecom.payment;

import static chat.hola.com.app.Utilities.Constants.FALSE;
import static chat.hola.com.app.Utilities.Constants.TRUE;
import static com.appscrip.stripe.Constants.ENGLISH;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.BackButtonClickListener;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.session.SessionManager;
import com.appscrip.stripe.AccountsDelegate;
import com.appscrip.stripe.UserAccounts;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kotlintestgradle.UseCaseHandler;
import com.kotlintestgradle.data.utils.DataConstants;
import java.util.ArrayList;
import javax.inject.Inject;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

/**
 * provides the logical part of payment screen
 */
public class PaymentMethodViewModel extends ViewModel implements
    BackButtonClickListener {
  public androidx.databinding.ObservableField<Boolean>
      payOnDeliveryState = new androidx.databinding.ObservableField<>(FALSE);
  public androidx.databinding.ObservableField<Boolean>
      payFromWallet = new androidx.databinding.ObservableField<>(FALSE);
  public androidx.databinding.ObservableField<Boolean>
      btnEnabled = new androidx.databinding.ObservableField<>(FALSE);
  public androidx.databinding.ObservableField<Boolean>
      progressVisible = new androidx.databinding.ObservableField<>(FALSE);
  boolean mIsChecked;
  float amtToBePaid;
  String toCurrency;
  String comingFrom = DataConstants.EMPTY_STRING;
  float walletBal;
  private MutableLiveData<PaymentUiAction> mClick = new MutableLiveData<>();
  private ArrayList<SavedCardsData> mSavedCardsData = new ArrayList<>();
  private MutableLiveData<String> mWalletData = new MutableLiveData<>();
  private MutableLiveData<ArrayList<SavedCardsData>> mSavedCardsLiveData = new MutableLiveData<>();
  private MutableLiveData<Boolean> mPaymentDisSelectData = new MutableLiveData<>();
  private UseCaseHandler mHandler;
  /*  private WalletAddMoneyUseCase mWalletAddMoneyUseCase;
    private GetCorporateAccountsUseCase mAccountUseCase;
    private TransactionEstimateUseCase mTransactionEstimateUseCase;*/
  private boolean mIsToCallWallet = TRUE;
  private SessionManager sessionManager;

  @Inject
  public PaymentMethodViewModel(UseCaseHandler useCaseHandler, SessionManager sessionManager
      /*WalletAddMoneyUseCase walletAddMoneyUseCase,*/
                               /* TransactionEstimateUseCase transactionEstimateUseCase,
                                GetCorporateAccountsUseCase accountsUseCase*/) {
    /*    mWalletAddMoneyUseCase = walletAddMoneyUseCase;*/
/*
    mTransactionEstimateUseCase = transactionEstimateUseCase;
*/
    mHandler = useCaseHandler;
/*
    mAccountUseCase = accountsUseCase;
*/
    this.sessionManager = sessionManager;
  }

  /**
   * call the add wallet amount  api
   */
  void callAddMoneyToWalletApi(String cardId, String currency, String amount) {
    progressVisible.set(TRUE);
    /*DisposableSingleObserver<WalletAddMoneyUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<WalletAddMoneyUseCase.ResponseValues>() {
          @Override
          public void onSuccess(WalletAddMoneyUseCase.ResponseValues responseValues) {
            mIsToCallWallet = FALSE;
            callGetWalletApi();
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
            Utilities.printLog("wallet Fail" + e.getMessage());
          }
        };
    mHandler.execute(mWalletAddMoneyUseCase,
        new WalletAddMoneyUseCase.RequestValues(cardId, currency, amount),
        disposableSingleObserver);*/
  }

  /**
   * call the get wallet amount  api
   */
  void callGetWalletApi() {
    String balance = sessionManager.getCurrencySymbol() + " " + sessionManager.getWalletBalance() + "";
    mWalletData.setValue(balance);

    if (mIsToCallWallet) {
      getCards();
    } else {
      progressVisible.set(FALSE);
    }
  }

  /**
   * call the get wallet amount  api
   */
  private void callGetWalletEstimateApi(String fromCurrency, String toCurrency, float amount) {
    progressVisible.set(TRUE);
  /*  DisposableSingleObserver<TransactionEstimateUseCase.ResponseValues> disposableSingleObserver =
        new DisposableSingleObserver<TransactionEstimateUseCase.ResponseValues>() {
          @Override
          public void onSuccess(TransactionEstimateUseCase.ResponseValues responseValues) {
            progressVisible.set(FALSE);
            Utilities.printLog(
                "exe" + "wallet Sucess" + responseValues.getData().getEstimateAmount());
            if (responseValues.getData() != null) {
              walletBal =
                  Float.parseFloat(responseValues.getData().getEstimateAmount());
              String balance = String.format("%s %s",
                  toCurrency,
                  String.format("%.2f", walletBal));
              mWalletData.setValue(balance);
            }
          }

          @Override
          public void onError(Throwable e) {
            progressVisible.set(FALSE);
            Utilities.printLog("wallet Fail" + e.getMessage());
          }
        };
    mHandler.execute(mTransactionEstimateUseCase,
        new TransactionEstimateUseCase.RequestValues(fromCurrency, toCurrency, amount),
        disposableSingleObserver);*/
  }

  /**
   * used to get the cards
   */
  void getCards() {
    progressVisible.set(TRUE);
    UserAccounts accounts = new UserAccounts(AppController.getInstance());
    accounts.getCards(AppController.getInstance().getApiToken(),
        ENGLISH, AppController.getInstance().getUserId(), new AccountsDelegate() {
          @Override
          public void onSuccess(@NotNull Object successData) {
            progressVisible.set(FALSE);
            JSONArray jsonArray = (JSONArray) successData;
            Gson gson = new Gson();
            java.lang.reflect.Type listType = new TypeToken<ArrayList<SavedCardsData>>() {
            }.getType();
            mSavedCardsData = gson.fromJson(jsonArray.toString(), listType);
            mSavedCardsLiveData.postValue(mSavedCardsData);
          }

          @Override
          public void onFailure(@NotNull String failure) {
            progressVisible.set(FALSE);
            Utilities.printLog("exe" + "failure" + failure);
          }
        });
  }

  @Override
  public void backButtonClickListener(android.view.View view) {
    mClick.postValue(PaymentUiAction.BACK);
  }

  /**
   * notify wallet data comes
   *
   * @return mutable live data of strings
   */
  MutableLiveData<String> getWalletData() {
    return mWalletData;
  }


  /**
   * listens change address clicked.
   */
  public void continueClick() {
    if (payFromWallet.get() && payOnDeliveryState.get()) {
      mClick.postValue(PaymentUiAction.WALLET_PLUS_CASH);
    } else if (payFromWallet.get() && mIsChecked) {
      mClick.postValue(PaymentUiAction.WALLET_PLUS_CARD);
    } else if (mIsChecked) {
      mClick.postValue(PaymentUiAction.CARD);
    } else if (payFromWallet.get()) {
      mClick.postValue(PaymentUiAction.WALLET);
    } else if (payOnDeliveryState.get()) {
      mClick.postValue(PaymentUiAction.CASH);
    } else {
      mClick.postValue(PaymentUiAction.EMPTY);
    }
  }

  /**
   * set the  button status on select the card
   */
  void selectCardButtonStatus() {
    if (walletBal < amtToBePaid) {
      btnEnabled.set(payFromWallet.get()
          && payOnDeliveryState.get() ? TRUE : FALSE);
    } else {
      btnEnabled.set(payFromWallet.get()
          || payOnDeliveryState.get() ? TRUE : FALSE);
    }
  }

  /**
   * listens add money clicked.
   */
  public void addMoneyClick() {
    mClick.postValue(PaymentUiAction.ADD_MONEY);
  }

  /**
   * listens add money clicked.
   */
  public void addCardClick() {
    mClick.postValue(PaymentUiAction.ADD_CARD);
  }

  /**
   * listens change address clicked.
   */
  public void payOnDeliveryClick() {
    if (mIsChecked) {
      mPaymentDisSelectData.setValue(TRUE);
      if (amtToBePaid < walletBal) {
        mClick.setValue(PaymentUiAction.ENABLE_CARDS);
      }
    }
    payOnDeliveryState.set(payOnDeliveryState.get() ? FALSE : TRUE);
    if (amtToBePaid > walletBal) {
      mClick.setValue(PaymentUiAction.ENABLE_CARDS);
    }
    btnEnabled.set(payOnDeliveryState.get());
  }

  /**
   * listens change address clicked.
   */
  public void walletClick() {
    if (mIsChecked) {
      if (amtToBePaid < walletBal) {
        mPaymentDisSelectData.setValue(TRUE);
      }
    }
    if (amtToBePaid < walletBal) {
      payOnDeliveryState.set(FALSE);
    }
    android.util.Log.d("PAYAEMY", amtToBePaid + "");
    payFromWallet.set(payFromWallet.get() ? FALSE : TRUE);
    if (payFromWallet.get()) {
      mClick.setValue(amtToBePaid < walletBal ? PaymentUiAction.DISABLE : PaymentUiAction.ENABLE);
    } else {
      mClick.setValue(PaymentUiAction.ENABLE);
    }
    if (amtToBePaid > walletBal) {
      if (payOnDeliveryState.get()) {
        btnEnabled.set(TRUE);
      }
    }
  }

  /**
   * notify saved cards data comes
   *
   * @return mutable live data of saved cards
   */
  MutableLiveData<ArrayList<SavedCardsData>> getSavedCardsLIveData() {
    return mSavedCardsLiveData;
  }

  /**
   * notify saved cards data comes
   *
   * @return mutable live data of saved cards
   */
  MutableLiveData<Boolean> getDisSelectLIveData() {
    return mPaymentDisSelectData;
  }

  /**
   * returns live data clicking.
   */
  MutableLiveData<PaymentUiAction> onClick() {
    return mClick;
  }


  /*notify UI when add account button clicked*/
  public void onAddAccountClick() {
    mClick.postValue(PaymentUiAction.ADD_ACCOUNT);
  }

  /*notify UI when personal button clicked*/
  public void onPersonalButtonClick() {
    mClick.postValue(PaymentUiAction.PERSONAL);
  }

  /*notify UI when business button clicked*/
  public void onBusinessButtonClick() {
    mClick.postValue(PaymentUiAction.BUSINESS);
  }
}