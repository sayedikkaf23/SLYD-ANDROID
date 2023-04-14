package chat.hola.com.app.subscription.active;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface ActiveModule {
    @FragmentScoped
    @Binds
    ActiveContract.Presenter presenter(ActivePresenter presenter);

    @FragmentScoped
    @Binds
    ActiveContract.View view(ActiveFragment fragment);
}
