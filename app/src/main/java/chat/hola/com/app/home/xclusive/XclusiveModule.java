package chat.hola.com.app.home.xclusive;

import chat.hola.com.app.dagger.FragmentScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>XclusiveModule</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 23/3/18.
 */

@Module
public interface XclusiveModule {
    @FragmentScoped
    @Binds
    XclusiveContract.Presenter presenter(XclusivePresenter presenter);

//    @FragmentScoped
//    @Binds
//    XclusiveContract.View view(XclusiveFragment detail);
}
