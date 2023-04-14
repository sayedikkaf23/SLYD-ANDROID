package chat.hola.com.app.dublycategory;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.dubly.Dub;
import chat.hola.com.app.dublycategory.modules.CategoriesCombo;
import chat.hola.com.app.dublycategory.modules.DubCategory;
import chat.hola.com.app.dublycategory.modules.DubCategoryAdapter;
import chat.hola.com.app.dublycategory.modules.DubFavListAdapter;
import chat.hola.com.app.dublycategory.modules.DubListAdapter;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>SearchUserUtilModule</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */
//@ActivityScoped
@Module
public class DubCategoryUtilModule {

    @ActivityScoped
    @Provides
    @Named("default")
    List<Dub> getDubs() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    List<CategoriesCombo> getCategories() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    DubCategoryAdapter dubsAdapter(List<CategoriesCombo> dubs, Activity mContext, TypefaceManager typefaceManager) {
        return new DubCategoryAdapter(dubs, mContext, typefaceManager);
    }

    @ActivityScoped
    @Provides
    DubListAdapter dubsListAdapter(@Named("default") List<Dub> dubs, Activity mContext, TypefaceManager typefaceManager) {
        return new DubListAdapter(dubs, mContext, typefaceManager);
    }

    @ActivityScoped
    @Provides
    @Named("favourite")
    List<Dub> getFavDubs() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    DubFavListAdapter dubsFavListAdapter(@Named("favourite") List<Dub> dubs, Activity mContext, TypefaceManager typefaceManager) {
        return new DubFavListAdapter(dubs, mContext, typefaceManager);
    }
}
