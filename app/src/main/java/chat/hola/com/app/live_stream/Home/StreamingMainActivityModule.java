package chat.hola.com.app.live_stream.Home;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by moda on 11/20/2018.
 */
@Module
public interface StreamingMainActivityModule
{
    @Binds
    StreamingMainActivityPresenterContract.presenterMain provideMainPresenter(StreamingMainActivityPresenterImpl mainPresenter);

    @Binds
    StreamingMainActivityPresenterContract.ViewMain provideMainView(StreamingMainActivity mainPresenter);

    @ContributesAndroidInjector
    BroadcastersFragment provideBroadCasterFragment();

    @ContributesAndroidInjector
    ProfileFragment provideProfileFragment();

    @Binds
    BroadcastersPresenterContract.BroadcastPresenter provideBroadCasterPresenter(BroadcastersPresenterImpl presenter);


    @Binds
    ProfilePresenterContract. ProfilePresenter provideProfilePresenter(ProfilePresenterImpl presenter);
}
