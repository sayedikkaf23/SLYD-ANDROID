package chat.hola.com.app.dublycategory.favourite;
import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.dublycategory.fagments.DubFragmentContract;
import chat.hola.com.app.dublycategory.fagments.DubFragmentPresenter;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 23/2/18.
 */

//@FragmentScoped
@Module
public interface DubFavFragmentModule {

    @FragmentScoped
    @Binds
    DubFragmentContract.Presenter presenter(DubFragmentPresenter presenter);

   /* @FragmentScoped
    @Binds
    DubFavouriteFragmentContract.View view(TagFragment fragment);*/
}
