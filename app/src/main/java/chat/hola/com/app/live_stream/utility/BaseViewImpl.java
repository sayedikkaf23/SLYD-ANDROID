package chat.hola.com.app.live_stream.utility;

/**
 * Created by moda on 11/21/2018.
 */
public interface BaseViewImpl
{
    void showProgress();
    void hideProgress();
    void apiError(String errorMsg);
    void onNetworkRetry(boolean isNetwork, String thisApi);

}
