package chat.hola.com.app.collections.collection;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface CollectionModule {

    @ActivityScoped
    @Binds
    CollectionContract.Presenter presenter(CollectionPresenter presenter);

    @ActivityScoped
    @Binds
    CollectionContract.View view(CollectionActivity activity);


    @ActivityScoped
    @Binds
    Activity activity(CollectionActivity activity);

}