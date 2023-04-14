package chat.hola.com.app.dublycategory.fagments;
import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 23/2/18.
 */

//@FragmentScoped
@Module
public interface DubFragmentModule {

    @FragmentScoped
    @Binds
    DubFragmentContract.Presenter presenter(DubFragmentPresenter presenter);

   /* @FragmentScoped
    @Binds
    DubFavouriteFragmentContract.View view(TagFragment fragment);*/
}
