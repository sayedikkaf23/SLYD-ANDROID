package chat.hola.com.app.request_star_profile.request_star;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface RequestStarModule {

    @ActivityScoped
    @Binds
    RequestStarContract.View view(RequestStarProfileActivity requestStarProfileActivity);

    @ActivityScoped
    @Binds
    RequestStarContract.Presenter presenter(RequestStarPresenter requestStarPresenter);
}
