package chat.hola.com.app.collections.create_collection;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface CreateCollectionModule {

    @ActivityScoped
    @Binds
    CreateCollectionContract.Presenter presenter(CreateCollectionPresenter presenter);

    @ActivityScoped
    @Binds
    CreateCollectionContract.View view(CreateCollectionActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(CreateCollectionActivity activity);
}
