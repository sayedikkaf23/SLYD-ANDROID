package chat.hola.com.app.profileScreen.paid;
import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 23/2/18.
 */

//@FragmentScoped
@Module
public interface PaidModule {

    @FragmentScoped
    @Binds
    PaidContract.Presenter presenter(PaidPresenter presenter);

   /* @FragmentScoped
    @Binds
    DubFavouriteFragmentContract.View view(TagFragment fragment);*/
}
