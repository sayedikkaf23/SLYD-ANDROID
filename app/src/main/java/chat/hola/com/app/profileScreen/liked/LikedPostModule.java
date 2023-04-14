package chat.hola.com.app.profileScreen.liked;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 23/2/18.
 */

//@FragmentScoped
@Module
public interface LikedPostModule {

    @FragmentScoped
    @Binds
    LikedPostContract.Presenter presenter(LikedPostyPresenter presenter);

   /* @FragmentScoped
    @Binds
    DubFavouriteFragmentContract.View view(TagFragment fragment);*/
}
