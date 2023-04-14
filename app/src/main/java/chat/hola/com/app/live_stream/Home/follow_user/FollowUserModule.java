package chat.hola.com.app.live_stream.Home.follow_user;

import dagger.Binds;
import dagger.Module;

@Module
public interface FollowUserModule {
  @Binds
  FollowUserContract.Presenter providePresenter(FollowUserPresenter followUserPresenter);

  @Binds
  FollowUserContract.View provideLiveView(FollowUserActivity followUserActivity);
}