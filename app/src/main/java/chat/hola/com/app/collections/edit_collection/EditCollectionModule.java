package chat.hola.com.app.collections.edit_collection;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface EditCollectionModule {

    @ActivityScoped
    @Binds
    EditCollectionContract.Presenter presenter(EditCollectionPresenter presenter);

    @ActivityScoped
    @Binds
    EditCollectionContract.View view(EditCollectionActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(EditCollectionActivity activity);
}
