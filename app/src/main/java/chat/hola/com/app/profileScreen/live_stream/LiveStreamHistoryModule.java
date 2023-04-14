package chat.hola.com.app.profileScreen.live_stream;


import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface LiveStreamHistoryModule {
    @FragmentScoped
    @Binds
    LiveStreamHistoryContract.Presenter presenter(LiveStreamHistoryPresenter presenter);
}
