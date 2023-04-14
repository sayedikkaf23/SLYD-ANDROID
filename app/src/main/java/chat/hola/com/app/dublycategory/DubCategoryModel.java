package chat.hola.com.app.dublycategory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.dublycategory.modules.CategoriesCombo;
import chat.hola.com.app.dublycategory.modules.DubCategory;
import chat.hola.com.app.dublycategory.modules.DubCategoryAdapter;
import chat.hola.com.app.manager.session.SessionManager;

/**
 * <h1>DubCategoryModel</h1>
 *
 * @author 3Embed
 * @since 4/10/2018.
 */

public class DubCategoryModel {

    @Inject
    List<CategoriesCombo> categories;
    @Inject
    DubCategoryAdapter categoryAdapter;
    @Inject
    SessionManager sessionManager;

    @Inject
    DubCategoryModel() {
    }

    public void setCategoryData(List<DubCategory> dubCategories, boolean isLoadAll) {
        if (dubCategories != null) {
            createCombo((ArrayList<DubCategory>) dubCategories);
//            int max = isLoadAll || dubCategories.size() < 8 ? dubCategories.size() : 8;
//            this.categories.clear();
//            for (int i = 0; i < max; i++)
//                this.categories.add(dubCategories.get(i));
//            categoryAdapter.notifyDataSetChanged();
        }
    }

    static <T> ArrayList<ArrayList<T>> chopped(ArrayList<T> list, final int L) {
        ArrayList<ArrayList<T>> parts = new ArrayList<ArrayList<T>>();
        final int N = list.size();
        for (int i = 0; i < N; i += L) {
            parts.add(new ArrayList<T>(
                    list.subList(i, Math.min(N, i + L)))
            );
        }
        return parts;
    }

    private void createCombo(ArrayList<DubCategory> gifts) {
        categories.clear();
        ArrayList<ArrayList<DubCategory>> partition = chopped(gifts, 8);

        for (ArrayList<DubCategory> list : partition) {
            CategoriesCombo combo = new CategoriesCombo();
            combo.setDubCategories(list);
            categories.add(combo);
        }
        categoryAdapter.notifyDataSetChanged();

    }


//    public String getCategoryId(int position) {
//        return categories.get(position).getId();
//    }
//
//    public String getCategoryName(int position) {
//        return categories.get(position).getName();
//    }
}
