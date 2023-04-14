package chat.hola.com.app.collections.saved;

import android.app.Activity;

import chat.hola.com.app.comment.CommentActivity;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface SavedModule {

    @ActivityScoped
    @Binds
    SavedContract.Presenter presenter(SavedPresenter presenter);

    @ActivityScoped
    @Binds
    SavedContract.View view(SavedActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(SavedActivity activity);

}
