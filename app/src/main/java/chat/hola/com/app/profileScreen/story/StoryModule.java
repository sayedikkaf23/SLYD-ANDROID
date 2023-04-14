package chat.hola.com.app.profileScreen.story;
import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 23/2/18.
 */

//@FragmentScoped
@Module
public interface StoryModule {

    @FragmentScoped
    @Binds
    StoryContract.Presenter presenter(StoryPresenter presenter);

   /* @FragmentScoped
    @Binds
    DubFavouriteFragmentContract.View view(TagFragment fragment);*/
}
