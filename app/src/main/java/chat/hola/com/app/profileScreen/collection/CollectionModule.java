package chat.hola.com.app.profileScreen.collection;
import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

//@FragmentScoped
@Module
public interface CollectionModule {

    @FragmentScoped
    @Binds
    CollectionContract.Presenter presenter(CollectionPresenter presenter);

   /* @FragmentScoped
    @Binds
    DubFavouriteFragmentContract.View view(TagFragment fragment);*/
}
