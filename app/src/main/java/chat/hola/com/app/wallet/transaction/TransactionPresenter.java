package chat.hola.com.app.wallet.transaction;

import android.os.Handler;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.wallet.transaction.Model.TransactionModel;
import chat.hola.com.app.wallet.wallet_detail.model.WalletBalanceMain;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class TransactionPresenter implements TransactionContract.Presenter {

    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    public TransactionPresenter() {
    }


    @Inject
    TransactionContract.View view;
    @Inject
    TransactionModel model;
    @Inject
    HowdooService service;

    @Override
    public void attachView(TransactionContract.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void getWalletBalance() {

    }

    @Override
    public void getTransactionList() {

    }

}
