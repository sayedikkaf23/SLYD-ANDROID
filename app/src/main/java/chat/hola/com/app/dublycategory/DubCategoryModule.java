package chat.hola.com.app.dublycategory;

import android.app.Activity;

import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.dublycategory.fagments.DubFragment;
import chat.hola.com.app.dublycategory.fagments.DubFragmentModule;
import chat.hola.com.app.dublycategory.favourite.DubFavFragment;
import chat.hola.com.app.dublycategory.favourite.DubFavFragmentModule;
import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * <h1>DubCategoryModule</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

//@ActivityScoped
@Module
public interface DubCategoryModule {
    @ActivityScoped
    @Binds
    DubCategoryContract.Presenter presenter(DubCategoryPresenter presenter);

    @ActivityScoped
    @Binds
    DubCategoryContract.View view(DubCategoryActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(DubCategoryActivity activity);

    @FragmentScoped
    @ContributesAndroidInjector(modules = {DubFragmentModule.class})
    DubFragment dubFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = {DubFavFragmentModule.class})
    DubFavFragment dubFavouriteFragment();
}
