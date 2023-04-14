package chat.hola.com.app.profileScreen.purchase;
import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 23/2/18.
 */

//@FragmentScoped
@Module
public interface PurchaseModule {

    @FragmentScoped
    @Binds
    PurchaseContract.Presenter presenter(PurchasePresenter presenter);

   /* @FragmentScoped
    @Binds
    DubFavouriteFragmentContract.View view(TagFragment fragment);*/
}
