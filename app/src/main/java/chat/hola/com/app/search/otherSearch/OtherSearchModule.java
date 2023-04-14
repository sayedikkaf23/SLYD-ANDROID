package chat.hola.com.app.search.otherSearch;
import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 24/2/18.
 */

//@FragmentScoped
@Module
public interface OtherSearchModule {

    @FragmentScoped
    @Binds
    OtherSearchContract.Presenter presenter(OtherSearchPresenter presenter);

   /* @FragmentScoped
    @Binds
    OtherSearchContract.View view(OtherSearchFrag frag);*/
}
