package chat.hola.com.app.category;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.category.model.CategoryAdapter;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.post.model.CategoryData;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>CategoryUtilModule</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 28/3/18
 */

////@ActivityScoped
@Module
public class CategoryUtilModule
{

    @ActivityScoped
    @Provides
    List<CategoryData> categoryData() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    CategoryAdapter categoryAdapter(CategoryActivity activity, List<CategoryData> categoryData, TypefaceManager typefaceManager) {
        return new CategoryAdapter(activity, categoryData, typefaceManager);
    }

}
