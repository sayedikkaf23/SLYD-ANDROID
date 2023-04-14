package chat.hola.com.app.wallet.wallet_detail;

import android.os.Handler;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.wallet.wallet_detail.model.WalletBalanceMain;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class WalletPresenter implements WalletContract.Presenter {
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    public WalletPresenter() {
    }

    @Inject
    HowdooService service;
    @Inject
    WalletContract.View view;

    @Override
    public void attachView(WalletContract.View view) {

    }

    @Override
    public void detachView() {

    }

    @Override
    public void getWalletBalance() {

    }
}
