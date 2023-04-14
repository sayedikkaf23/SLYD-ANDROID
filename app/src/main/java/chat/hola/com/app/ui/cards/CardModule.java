package chat.hola.com.app.ui.cards;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface CardModule {
    @ActivityScoped
    @Binds
    CardContract.Presenter presenter(CardPresenter presenter);
}
