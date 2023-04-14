package chat.hola.com.app.mystories;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by embed on 13/12/18.
 */

@Module
public interface MyStoriesDaggerModule
{
    @ActivityScoped
    @Binds
    MyStoriesPresenterImpl.MyStoriesPresenterView provideMyStoriesView(MyStoriesActivity myStoriesActivity);

    @ActivityScoped
    @Binds
    MyStoriesPresenterImpl.MyStoriesPresent provideMyStoriesPresent(MyStoriesPresenter myStoriesPresenter);


}
