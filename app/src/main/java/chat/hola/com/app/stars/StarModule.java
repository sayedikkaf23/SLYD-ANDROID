package chat.hola.com.app.stars;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface StarModule {

    @ActivityScoped
    @Binds
    StarContact.View starView(StarActivity activity);

    @ActivityScoped
    @Binds
    StarContact.Presenter starPresenter(StarPresenter presenter);

    @ActivityScoped
    @Binds
    Activity activity(StarActivity activity);
}
