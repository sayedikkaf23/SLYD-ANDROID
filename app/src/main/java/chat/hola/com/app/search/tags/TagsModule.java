package chat.hola.com.app.search.tags;
import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by ankit on 24/2/18.
 */

//@FragmentScoped
@Module
public interface TagsModule {

    @FragmentScoped
    @Binds
    TagsContract.Presenter presenter(TagsPresenter presenter);

   /* @FragmentScoped
    @Binds
    ChannelContract.View view(ChannelFragment frag);
*/
}
