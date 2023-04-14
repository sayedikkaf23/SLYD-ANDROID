package chat.hola.com.app.dagger;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Named;
import javax.inject.Singleton;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.DublyService;
import chat.hola.com.app.Networking.EcomPythonService;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.HowdooServiceTrending;
import chat.hola.com.app.Networking.LiveStreamService;
import chat.hola.com.app.Networking.UnsafeOkHttpClient;
import chat.hola.com.app.Utilities.ApiOnServer;
import com.ezcall.android.BuildConfig;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <h1>NetworkModule</h1>
 *
 * @author Shaktinh Jadeja
 * @since 21/2/18.
 */

@Module
public class NetworkModule {

    @Provides
    @Singleton
    @Named("app")
    Retrofit retrofit() {

        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(
                AppController.getInstance().getApplicationContext());

        return new Retrofit.Builder().baseUrl(ApiOnServer.TYPE + ApiOnServer.HOST_API)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @Named("trending")
    Retrofit retrofit1() {

        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(
                AppController.getInstance().getApplicationContext());

        return new Retrofit.Builder().baseUrl(ApiOnServer.TYPE + ApiOnServer.TRENDING_HOST_API)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @Named("dubly")
    Retrofit retrofit2() {

        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(
                AppController.getInstance().getApplicationContext());
        return new Retrofit.Builder().baseUrl(ApiOnServer.TYPE + ApiOnServer.HOST_API)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @Named("liveStream")
    Retrofit retrofit4() {

        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(
                AppController.getInstance().getApplicationContext());

        return new Retrofit.Builder().baseUrl(
                ApiOnServer.TYPE + ApiOnServer.LIVE_STREAM_BASE)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    @Named("ecomm")
    Retrofit ecomRetrofit() {

        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(
            AppController.getInstance().getApplicationContext());

        return new Retrofit.Builder().baseUrl(BuildConfig.CALLING_BASE)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    }

    @Provides
    @Singleton
    HowdooService howdooService(@Named("app") Retrofit retrofit) {
        return retrofit.create(HowdooService.class);
    }

    @Provides
    @Singleton
    HowdooServiceTrending howdooServiceTrending(@Named("trending") Retrofit retrofit) {
        return retrofit.create(HowdooServiceTrending.class);
    }

    @Provides
    @Singleton
    DublyService dublyService(@Named("dubly") Retrofit retrofit) {
        return retrofit.create(DublyService.class);
    }

    @Provides
    @Singleton
    LiveStreamService liveStreamService(@Named("liveStream") Retrofit retrofit) {
        return retrofit.create(LiveStreamService.class);
    }

    @Provides
    @Singleton
    EcomPythonService ecomStreamService(  @Named("ecomm") Retrofit retrofit) {
        return retrofit.create(EcomPythonService.class);
    }
}
