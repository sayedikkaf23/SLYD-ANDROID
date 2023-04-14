package chat.hola.com.app.Networking;

import com.ezcall.android.BuildConfig;

import chat.hola.com.app.AppController;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CallApiServiceGenerator {

    private static final OkHttpClient httpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(AppController.getInstance().getApplicationContext());

    private static final Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl(BuildConfig.CALLING_BASE)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}
