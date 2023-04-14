package chat.hola.com.app.profileScreen.business.category;


import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.comment.model.Comment;
import chat.hola.com.app.comment.model.CommentAdapter;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.profileScreen.model.BusinessCategory;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>BusinessCategoryUtilModule</h1>
 * It contains utilities related to business category
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 16 August 2019
 */
@Module
public class BusinessCategoryUtilModule {
    @ActivityScoped
    @Provides
    List<BusinessCategory.Data> businessCategories() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    BusinessCategoryAdapter businessCategoryAdapter(List<BusinessCategory.Data> categories, Activity mContext, TypefaceManager typefaceManager) {
        return new BusinessCategoryAdapter(categories, mContext, typefaceManager);
    }
}
