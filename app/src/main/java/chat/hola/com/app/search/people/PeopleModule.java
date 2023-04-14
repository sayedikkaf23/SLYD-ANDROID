package chat.hola.com.app.search.people;
import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 24/2/18.
 */

//@FragmentScoped
@Module
public interface PeopleModule {

    @FragmentScoped
    @Binds
    PeopleContract.Presenter presenter(PeoplePresenter presenter);

   /* @FragmentScoped
    @Binds
    ChannelContract.View view(ChannelFragment frag);
*/
}
