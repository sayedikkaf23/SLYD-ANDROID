package chat.hola.com.app.socialDetail;

import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * <h1>SocialDetailModule</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 23/3/18.
 */
//@ActivityScoped
@Module
public interface SocialDetailModule {

    @ActivityScoped
    @Binds
    SocialDetailContract.Presenter presenter(SocialDetailPresenter presenter);

    @ActivityScoped
    @Binds
    SocialDetailContract.View view(SocialDetailActivity detail);

    @FragmentScoped
    @ContributesAndroidInjector(modules = PostModule.class)
    PostFragment postFragment();
}
