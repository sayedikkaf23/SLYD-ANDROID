package chat.hola.com.app.category;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>CategoryModule</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 28/3/18
 */

//@ActivityScoped
@Module
public interface CategoryModule {

    @ActivityScoped
    @Binds
    CategoryContract.Presenter categoryPresenter(CategoryPresenter presenter);

    @ActivityScoped
    @Binds
    CategoryContract.View categoryView(CategoryActivity activity);

}
