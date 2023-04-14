package chat.hola.com.app.Networking.callback;


import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.SocketTimeoutException;

import chat.hola.com.app.Networking.callback.BaseResponse;
import chat.hola.com.app.Utilities.BaseView;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.Response;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 05 August 2019
 */
public abstract class CallbackWrapper <T extends Response<BaseResponse>> extends DisposableObserver<T>{
    //BaseView is just a reference of a View in MVP
    private WeakReference<BaseView> weakReference;

    public CallbackWrapper(BaseView view) {
        this.weakReference = new WeakReference<>(view);
    }

    protected abstract void onSuccess(T t);

    @Override
    public void onNext(T t) {
        switch (t.code())
        {
            case 200:
                onSuccess(t);
                break;

        }

    }

    @Override
    public void onError(Throwable e) {
        BaseView view = weakReference.get();
        if (e instanceof HttpException) {
            ResponseBody responseBody = ((HttpException)e).response().errorBody();
//            view.onUnknownError(getErrorMessage(responseBody));
        } else if (e instanceof SocketTimeoutException) {
//            view.onTimeout();
        } else if (e instanceof IOException) {
//            view.onNetworkError();
        } else {
//            view.onUnknownError(e.getMessage());
        }
    }

    @Override
    public void onComplete() {

    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
