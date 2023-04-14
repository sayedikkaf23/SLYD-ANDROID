package chat.hola.com.app.wallet.transaction.all;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.TransactionResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class AllPresenter implements AllContract.Presenter {

    static final String TAG = AllPresenter.class.getSimpleName();
    final int PAGE_SIZE = 50;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;
    private SessionApiCall sessionApiCall = new SessionApiCall();

    @Inject
    HowdooService service;
    @Inject
    SessionManager sessionManager;

    @Inject
    public AllPresenter() {
    }

    //@Inject
    AllContract.View view;

    @Override
    public void attachView(AllContract.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void init() {
        page = 0;
        if (view != null) view.recyclerViewSetup();
    }

    @Override
    public void callApiOnScroll(Integer type, boolean isFirst, String pageState, int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                getTransactionList(sessionManager.getCoinWalletId(), type, isFirst, pageState);
            }
        }
    }

    @Override
    public void getTransactionList(String walletId, Integer type, boolean isFirst, String pageState) {
        isLoading = true;
        if (view != null) view.isDataLoading(true);
        service.transactions(AppController.getInstance().getApiToken(), Constants.LANGUAGE, walletId, type, pageState, 20)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<TransactionResponse>>() {
                    @Override
                    public void onNext(Response<TransactionResponse> response) {
                        isLoading = false;

                        if (view != null) view.isDataLoading(false);
                        if (response.code() == 200) {
                            try {
                                assert response.body() != null;
                                if (view != null) {
                                    isLastPage = response.body().getPageState() == null;
                                    view.setData(response.body().getTransactionDataList(), response.body().getPageState(), isFirst);
                                }
                            } catch (Exception e) {
                                if (view!=null)
                                    view.noData();
                                e.printStackTrace();
                            }
                        } else {
                            if (view != null)
                                view.noData();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        isLoading = false;
                        if (view != null) view.isDataLoading(false);
                        if (view!=null)
                            view.noData();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
