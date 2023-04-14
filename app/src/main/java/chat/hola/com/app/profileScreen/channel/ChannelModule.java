package chat.hola.com.app.profileScreen.channel;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h>ChannelModule</h>
 * @author 3Embed.
 * @since 23/2/18.
 */

//@FragmentScoped
@Module
public interface ChannelModule {

    @FragmentScoped
    @Binds
    ChannelContract.Presenter channelPresenter(ChannelPresenter presenter);

   /* @FragmentScoped
    @Binds
    ChannelContract.View channelView(ChannelFragment fragment);
*/
}
