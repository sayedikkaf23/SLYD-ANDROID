package chat.hola.com.app.star_configuration;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface StarConfigModule {

    @ActivityScoped
    @Binds
    StarConfigContract.Presenter presenter(StarConfigPresenter starConfigPresenter);

    @ActivityScoped
    @Binds
    StarConfigContract.View view(StarConfigurationActivity activity);

}
