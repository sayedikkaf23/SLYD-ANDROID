package chat.hola.com.app.profileScreen.addChannel;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 22/2/18.
 */

//@ActivityScoped
@Module
public interface AddChannelModule {

    @ActivityScoped
    @Binds
    AddChannelContract.Presenter presenter(AddChannelPresenter presenter);

    @ActivityScoped
    @Binds
    AddChannelContract.View view(AddChannelActivity addChannelActivity);

}
