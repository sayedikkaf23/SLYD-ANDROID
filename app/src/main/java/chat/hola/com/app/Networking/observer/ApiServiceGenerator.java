package chat.hola.com.app.Networking.observer;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.UnsafeOkHttpClient;
import chat.hola.com.app.Utilities.ApiOnServer;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServiceGenerator {

    private static final OkHttpClient httpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(AppController.getInstance().getApplicationContext());

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(ApiOnServer.TYPE + "" + ApiOnServer.HOST_API)
            .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}
