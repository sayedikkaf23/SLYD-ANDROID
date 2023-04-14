package chat.hola.com.app.subscription.cancelled;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface CancelledModule {
    @FragmentScoped
    @Binds
    CancelledContract.Presenter presenter(CancelledPresenter presenter);

    @FragmentScoped
    @Binds
    CancelledContract.View view(CancelledFragment fragment);
}
