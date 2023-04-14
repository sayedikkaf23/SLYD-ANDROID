package chat.hola.com.app.profileScreen.business.post;

import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.profileScreen.business.post.actionbutton.ActionButtonFragment;
import chat.hola.com.app.profileScreen.business.post.actionbutton.ActionButtonModule;
import chat.hola.com.app.profileScreen.business.post.link.LinkFragment;
import chat.hola.com.app.profileScreen.business.post.price.PriceFragment;
import chat.hola.com.app.profileScreen.business.post.price.PriceModule;
import chat.hola.com.app.profileScreen.business.post.type.PostTypeFragment;
import chat.hola.com.app.profileScreen.business.post.type.PostTypeModule;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * <h1>BusinessPostModule</h1>
 *
 * @author 3Embed
 * @since 21/2/18.
 */

@Module
public interface BusinessPostModule {

    @FragmentScoped
    @ContributesAndroidInjector(modules = ActionButtonModule.class)
    ActionButtonFragment actionButtonFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = PriceModule.class)
    PriceFragment priceFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = PostTypeModule.class)
    PostTypeFragment postTypeFragment();

    @FragmentScoped
    @ContributesAndroidInjector()
    LinkFragment linkFragment();
}
