package chat.hola.com.app.collections.add_to_collection;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface AddToCollectionModule {

    @ActivityScoped
    @Binds
    AddToCollectionContract.Presenter presenter(AddToCollectionPresenter presenter);

    @ActivityScoped
    @Binds
    AddToCollectionContract.View view(AddToCollectionActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(AddToCollectionActivity activity);
}
