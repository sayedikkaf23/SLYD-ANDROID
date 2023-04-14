package chat.hola.com.app.profileScreen.business.category;


import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.profileScreen.model.BusinessCategory;

/**
 * <h1>BusinessCategoryModel</h1>
 * It sets and gets data, do logic changes on data, apply to the adapter
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 16 August 2019
 */
public class BusinessCategoryModel {

    @Inject
    List<BusinessCategory.Data> categories;
    @Inject
    BusinessCategoryAdapter adapter;

    @Inject
    BusinessCategoryModel() {
    }

    public void setCategories(List<BusinessCategory.Data> categories) {
        this.categories.clear();
        this.categories.addAll(categories);

        int pos = 0;
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId().equalsIgnoreCase(BusinessCategoryActivity.category)) {
                pos = i;
                break;
            }
        }
        adapter.setLastCheckedPosition(pos);
        adapter.notifyDataSetChanged();
    }
}
