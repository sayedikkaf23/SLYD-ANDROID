package chat.hola.com.app.dagger;


import android.app.ProgressDialog;
import android.content.Context;

import com.google.gson.Gson;
import com.kotlintestgradle.UseCaseHandler;
import com.kotlintestgradle.data.repository.ecom.AddressHandlerRepositoryImpl;
import com.kotlintestgradle.data.repository.ecom.CartHandlerRepositoryImpl;
import com.kotlintestgradle.interactor.ecom.AddressHandler;
import com.kotlintestgradle.interactor.ecom.AddressHandlerImpl;
import com.kotlintestgradle.interactor.ecom.CartHandler;
import com.kotlintestgradle.interactor.ecom.CartHandlerImpl;
import com.kotlintestgradle.repository.AddressHandlerRepository;
import com.kotlintestgradle.repository.CartHandlerRepository;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import javax.inject.Singleton;

import androidx.recyclerview.widget.LinearLayoutManager;

import chat.hola.com.app.Database.PostDb;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Networking.connection.ContactHolder;
import chat.hola.com.app.Networking.connection.NetworkStateHolder;
import chat.hola.com.app.Networking.observer.ContactObserver;
import chat.hola.com.app.Networking.observer.NetworkObserver;
import chat.hola.com.app.Networking.observer.SocialShareObserver;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.twitterManager.TwitterShareManager;
import chat.hola.com.app.home.stories.model.StoryObserver;
import chat.hola.com.app.live_stream.pubsub.MQTTManager;
import chat.hola.com.app.live_stream.utility.AlertProgress;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.ConnectionObserver;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.PostObserver;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.models.SocialObserver;
import chat.hola.com.app.models.UserObserver;
import chat.hola.com.app.post.model.UploadObserver;
import dagger.Module;
import dagger.Provides;

//import com.facebookmanager.com.FacebookManager;

/**
 * <h1>AppUtilModule</h1>
 *
 * @author 3eMBED
 * @version 1.0
 * @since 20/2/18.
 */

////@Singleton
@Module
public class AppUtilModule {
    @Singleton
    @Provides
    TypefaceManager typefaceManager(Context context) {
        return new TypefaceManager(context);
    }

    @Singleton
    @Provides
    Bus provideBus(Context context) {
        return new Bus(ThreadEnforcer.ANY);
    }

    @Singleton
    @Provides
    TwitterShareManager provideTwitterShareManager() {
        return new TwitterShareManager();
    }

    @Singleton
    @Provides
    UploadObserver uploadObserver() {
        return new UploadObserver();
    }

    @Singleton
    @Provides
    StoryObserver storyObserver() {
        return new StoryObserver();
    }

    @Singleton
    @Provides
    PostDb postdb(Context context) {
        return new PostDb(context);
    }

    @Provides
    @Singleton
    NetworkObserver getNetWorkObserver() {
        return new NetworkObserver();
    }

    @Provides
    @Singleton
    NetworkStateHolder getNetWorHolder() {
        return new NetworkStateHolder();
    }

    @Provides
    @Singleton
    SessionManager getSessionManager(Context context) {
        return new SessionManager(context);
    }

    @Provides
    @Singleton
    ProgressDialog getProgressDialog(Context context) {
        return new ProgressDialog(context);
    }

    @Provides
    @Singleton
    Gson getGson() {
        return new Gson();
    }

    @Provides
    @Singleton
    ContactObserver getContactObserver() {
        return new ContactObserver();
    }

    @Provides
    @Singleton
    ContactHolder getContactHolder() {
        return new ContactHolder();
    }

    @Provides
    @Singleton
    LinearLayoutManager linearLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    @Singleton
    @Provides
    PostObserver postObserver() {
        return new PostObserver();
    }

    @Singleton
    @Provides
    ConnectionObserver connectionObserver() {
        return new ConnectionObserver();
    }

    @Singleton
    @Provides
    NetworkConnector networkConnector() {
        return new NetworkConnector();
    }

    @Provides
    @Singleton
    BlockDialog bockDialog(Context context) {
        return new BlockDialog(context);
    }

    @Singleton
    @Provides
    UserObserver userObserver() {
        return new UserObserver();
    }

    @Provides
    @Singleton
    MQTTManager mqttManager(Context context, SessionManager sessionManager, Gson gson) {
        return new MQTTManager(context, sessionManager, gson);
    }

    @Provides
    @Singleton
    SessionApiCall sessionApiCall() {
        return new SessionApiCall();
    }

    @Singleton
    @Provides
    SessionObserver sessionObserver() {
        return new SessionObserver();
    }

    @Singleton
    @Provides
    SocialObserver socialShareObserver() {
        return new SocialObserver();
    }

    @Provides
    @Singleton
    AlertProgress provideAlertProgress(Context mContext) {
        return new AlertProgress(mContext);
    }

    @Singleton
    @Provides
    UseCaseHandler provideUseCaseHandler(){
        return UseCaseHandler.Companion.getGetInstance();
    }

    @Provides
    CartHandlerRepository provideCartRepository(CartHandlerRepositoryImpl userInfoRepo) {
        return userInfoRepo;
    }

    @Provides
    public CartHandler getCartUseCase(CartHandlerImpl userInfoHandler) {
        return userInfoHandler;
    }


    @Provides
    AddressHandlerRepository provideRepository(AddressHandlerRepositoryImpl userInfoRepo) {
        return userInfoRepo;
    }

    @Provides
    public AddressHandler getUseCase(AddressHandlerImpl userInfoHandler) {
        return userInfoHandler;
    }
}