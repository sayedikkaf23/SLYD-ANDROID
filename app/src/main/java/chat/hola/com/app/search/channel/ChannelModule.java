package chat.hola.com.app.search.channel;
import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 24/2/18.
 */

//@FragmentScoped
@Module
public interface ChannelModule {

    @FragmentScoped
    @Binds
    ChannelContract.Presenter presenter(ChannelPresenter presenter);

   /* @FragmentScoped
    @Binds
    ChannelContract.View view(ChannelFragment frag);
*/
}
