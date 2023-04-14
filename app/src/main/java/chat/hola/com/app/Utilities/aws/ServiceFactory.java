package chat.hola.com.app.Utilities.aws;

import android.content.Context;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.UnsafeOkHttpClient;
import chat.hola.com.app.Utilities.ApiOnServer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceFactory {
    private static final String BASE_URL = ApiOnServer.TYPE + ApiOnServer.HOST_API;

    private static OkHttpClient httpClient =
            UnsafeOkHttpClient.getUnsafeOkHttpClient(AppController.getInstance().getApplicationContext());




    /**
     * Creates a retrofit service from an arbitrary class (clazz)
     *
     * @param clazz Java interface of the retrofit service
     * @return retrofit service with defined endpoint
     */
    public static <T> T createRetrofitService(final Class<T> clazz) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(clazz);
    }

}
