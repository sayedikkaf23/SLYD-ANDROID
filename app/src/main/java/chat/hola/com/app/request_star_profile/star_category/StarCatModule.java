package chat.hola.com.app.request_star_profile.star_category;

/**
 * <h1>{@link chat.hola.com.app.request_star_profile.star_category.StarCatModule}</h1>
 * <p>Dagger module interface for star category</p>
 * @author: Hardik Karkar
 * @since : 23rd May 2019
 * {@link chat.hola.com.app.request_star_profile.star_category.StarCatContract}
 * {@link chat.hola.com.app.request_star_profile.star_category.StarCatPresenter}
 * {@link chat.hola.com.app.request_star_profile.star_category.StarCategoryActivity}
 */

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface StarCatModule {

    @ActivityScoped
    @Binds
    StarCatContract.Presenter presenter(StarCatPresenter starCatPresenter);

    @ActivityScoped
    @Binds
    StarCatContract.View view(StarCategoryActivity starCategoryActivity);
}
