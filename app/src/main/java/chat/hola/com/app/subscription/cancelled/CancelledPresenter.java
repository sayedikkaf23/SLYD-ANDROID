package chat.hola.com.app.subscription.cancelled;

import android.util.Pair;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.subscription.model.SubscriptionListRes;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class CancelledPresenter implements CancelledContract.Presenter{

    @Inject
    public CancelledPresenter() {
    }

    //@Inject
    CancelledContract.View view;
    @Inject
    HowdooService service;
    @Inject
    SessionManager sessionManager;
    @Inject
    PostObserver postObserver;

    public static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    public static int page = 0;

    @Override
    public void attach(CancelledContract.View view) {
        this.view = view;
    }

    @Override
    public void detach() {
        view = null;
    }

    @Override
    public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0 && totalItemCount >= PAGE_SIZE) {
                page++;
                getSubscriptions(PAGE_SIZE * page, PAGE_SIZE);
            }
        }
    }

    @Override
    public void getSubscriptions(int offset, int limit) {
        isLoading = true;
        if(view!=null)view.showLoader();
        service.getSubscriptions(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                3,offset,limit)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<SubscriptionListRes>>() {
                    @Override
                    public void onNext(@NonNull Response<SubscriptionListRes> response) {
                        isLoading = false;
                        if(view!=null)view.hideLoader();
                        switch (response.code()){
                            case 200:
                                if (response.body() != null && response.body().getData()!=null) {
                                    isLastPage = response.body().getData().size() < PAGE_SIZE;
                                    if (view != null)
                                        view.showData(response.body().getData(),offset==0);
                                }
                                break;
                            case 204:
                                if(!isLastPage && view != null)
                                    view.showEmpty(true);
                                break;
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoading = false;
                        if(view!=null)view.hideLoader();
                    }

                    @Override
                    public void onComplete() {
                        isLoading = false;
                        if(view!=null)view.hideLoader();
                    }
                });
    }

    @Override
    public void subscribeStarUser(boolean isChecked, String id) {
        if(view!=null)view.showLoader();
        Map<String,Object> map = new HashMap<>();
        map.put("userIdToFollow",id);
        service.subscribeStarUSer(AppController.getInstance().getApiToken(), Constants.LANGUAGE,map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<ResponseBody>>() {
                    @Override
                    public void onNext(Response<ResponseBody> response) {
                        if(view!=null)view.hideLoader();
                        switch (response.code()){
                            case 200:
                                if(view!=null)view.onSuccessSubscribe(isChecked);
                                postObserver.postPaidPostsObservableEmitter(new Pair("isFor","1"));
                                break;
                            case 403:
                                /*INSUFFICIENT BALANCE*/
                                if(view!=null)view.insufficientBalance();
                                break;
                            case 405:
                                if(view!=null)view.message(chat.hola.com.app.Utilities.Error.getErrorMessage(response.errorBody()));
                                break;
                            default:
                                if(view!=null)view.onSuccessSubscribe(false);
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(view!=null)view.hideLoader();
                    }

                    @Override
                    public void onComplete() {
                        if(view!=null)view.hideLoader();
                    }
                });
    }
}
