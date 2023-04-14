package chat.hola.com.app.profileScreen.tag;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 23/2/18.
 */

//@FragmentScoped
@Module
public interface TagModule {

    @FragmentScoped
    @Binds
    TagContract.Presenter presenter(TagPresenter presenter);

   /* @FragmentScoped
    @Binds
    DubFavouriteFragmentContract.View view(TagFragment fragment);*/
}
