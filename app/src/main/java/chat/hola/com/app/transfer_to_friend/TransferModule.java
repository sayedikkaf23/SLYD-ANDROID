package chat.hola.com.app.transfer_to_friend;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface TransferModule {

    @ActivityScoped
    @Binds
    TransferContract.View view(TransferToFriendActivity activity);

    @ActivityScoped
    @Binds
    TransferContract.Presenter presenter(TransferPresenter presenter);
}

