package chat.hola.com.app.profileScreen.discover;


import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.profileScreen.discover.contact.ContactAdapter;
import chat.hola.com.app.profileScreen.discover.follow.FollowAdapter;
import dagger.Module;
import dagger.Provides;

/**
 * <h>DiscoverUtilModule</h>
 * @author 3Embed.
 * @since 02/03/18.
 */

//@ActivityScoped
@Module
public class DiscoverUtilModule {

    @ActivityScoped
    @Provides
    FollowAdapter followAdapter(DiscoverActivity context, TypefaceManager typefaceManager){
        return new FollowAdapter(context , typefaceManager);
    }

    @ActivityScoped
    @Provides
    ContactAdapter contactAdapter(DiscoverActivity context, TypefaceManager typefaceManager){
        return new ContactAdapter(context , typefaceManager);
    }

//    @ActivityScoped
//    @Provides
//    FacebookQueryManager facebookQueryManager() {
//        return new FacebookFactory();
//    }
}
