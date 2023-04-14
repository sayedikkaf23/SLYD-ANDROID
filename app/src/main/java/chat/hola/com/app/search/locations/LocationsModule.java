package chat.hola.com.app.search.locations;
import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 24/2/18.
 */

//@FragmentScoped
@Module
public interface LocationsModule {

    @FragmentScoped
    @Binds
    LocationsContract.Presenter presenter(LocationsPresenter presenter);

}
