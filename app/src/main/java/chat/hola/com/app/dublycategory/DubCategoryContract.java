package chat.hola.com.app.dublycategory;

import java.util.List;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.dublycategory.modules.CategoryClickListner;
import chat.hola.com.app.dublycategory.modules.DubCategory;

/**
 * <h1>DubCategoryContract</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 7/16/2018.
 */

public interface DubCategoryContract {

    interface View extends BaseView {
        void getList(String categoryId, String name);

        void categories(List<DubCategory> dubs);
    }

    interface Presenter {

        CategoryClickListner getCategoryPresenter();

        void getCategories(boolean b);
    }
}
