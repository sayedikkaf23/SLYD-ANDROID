package chat.hola.com.app.request_star_profile.star_category;

/**
 * <h1>{@link chat.hola.com.app.request_star_profile.star_category.StarCatContract}</h1>
 * <p> Iterface which is contains PricePresenter interface and View interface  </p>
 * @Author: Hardik Karkar
 * @Since: 23rd May 2019
 * {@link chat.hola.com.app.request_star_profile.star_category.StarCatPresenter},
 * {@link chat.hola.com.app.request_star_profile.star_category.StarCategoryActivity}
 *
 */

import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;

public interface StarCatContract {

    interface View extends BaseView{

        /**
        * <p>These methods callback are in view</p>
        * */

        /**
         * <p>When getting response from api this method used to call</p>
         * @Params List<Data> : Category list from response
         * */
        void getCategorySuccuss(List<Data> data); // This method returns the category list in view.
    }

    interface Presenter extends BasePresenter<View>{

        /**
         * <p>These methods callback are in presenter class</p>
         * */

        /**
         * <p>This method is used to call get star category api</p>
         * */
        void getStarCategory();
    }
}
