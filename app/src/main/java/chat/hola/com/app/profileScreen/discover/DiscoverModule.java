package chat.hola.com.app.profileScreen.discover;

import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.profileScreen.discover.contact.ContactFrag;
import chat.hola.com.app.profileScreen.discover.contact.ContactModule;
import chat.hola.com.app.profileScreen.discover.facebook.FacebookFrag;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * <h>DiscoverModule</h>
 * @author 3Embed.
 * @since 02/03/18.
 */


//@ActivityScoped
@Module
public interface DiscoverModule {

    @FragmentScoped
    @ContributesAndroidInjector()
    FacebookFrag facebookFrag();

    @FragmentScoped
    @ContributesAndroidInjector(modules = {ContactModule.class})
    ContactFrag contactFrag();

    @ActivityScoped
    @Binds
    DiscoverContract.Presenter discoverPresenter(DiscoverPresenter presenter);

    @ActivityScoped
    @Binds
    DiscoverContract.View discoverView(DiscoverActivity discoverActivity);
}
