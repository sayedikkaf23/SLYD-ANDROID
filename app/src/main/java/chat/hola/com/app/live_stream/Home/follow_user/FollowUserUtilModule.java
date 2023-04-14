package chat.hola.com.app.live_stream.Home.follow_user;

import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Module;
import dagger.Provides;

@Module
public class FollowUserUtilModule {

  @ActivityScoped
  @Provides
  Loader loader(FollowUserActivity followUserActivity){
    return new Loader(followUserActivity);
  }
}
