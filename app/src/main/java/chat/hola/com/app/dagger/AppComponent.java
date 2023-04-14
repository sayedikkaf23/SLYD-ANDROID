package chat.hola.com.app.dagger;

import android.app.Application;

import chat.hola.com.app.dagger.module.PdpModule;
import javax.inject.Singleton;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.connection.NetworkCheckerService;
import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * Created by ankit on 20/2/18.
 */

@Singleton
@Component(modules = {AppModule.class, AppUtilModule.class, NetworkModule.class
        , ActivityBindingModule.class, ServiceBindingModule.class, AndroidSupportInjectionModule.class, PdpModule.class})
public interface AppComponent extends AndroidInjector<DaggerApplication> {

    void inject(AppController appController);

    void inject(NetworkCheckerService service);

    @Override
    void inject(DaggerApplication instance);

    @Component.Builder
    interface Builder {

        @BindsInstance
        AppComponent.Builder application(Application application);

        @BindsInstance
        AppComponent.Builder netModule(NetworkModule module);

        @BindsInstance
        AppComponent.Builder appUtilModule(AppUtilModule module);

        AppComponent build();
    }
}
