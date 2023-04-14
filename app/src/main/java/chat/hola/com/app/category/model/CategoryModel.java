package chat.hola.com.app.category.model;

import android.content.Intent;

import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.post.model.CategoryData;

/**
 * <h1>CategoryModel</h1>
 *
 * @author 3Embed
 * @since 5/2/2018.
 */

public class CategoryModel {
    @Inject
    List<CategoryData> categoryData;

    @Inject
    CategoryAdapter categoryAdapter;

    private CategoryData selectedData;

    @Inject
    CategoryModel() {
    }

    public void setChannel(List<CategoryData> channel, String categoryId) {
        this.categoryData.clear();
        this.categoryData.addAll(channel);
        for (int i = 0; i < categoryData.size(); i++) {
            categoryData.get(i).setSelected(categoryData.get(i).getId().equals(categoryId));
            if (categoryData.get(i).isSelected())
                selectItem(i);
        }
        categoryAdapter.notifyDataSetChanged();
    }

    public void selectItem(int position) {
        for (int i = 0; i < categoryData.size(); i++) {
            categoryData.get(i).setSelected(i == position);
            if (i == position)
                selectedData = categoryData.get(i);
        }
        categoryAdapter.notifyDataSetChanged();
    }

    public Intent selectedCategory() {
        if (selectedData != null) {
            Intent intent = new Intent();
            intent.putExtra("category", selectedData.getCategoryName());
            intent.putExtra("category_id", selectedData.getId());
            return intent;
        } else {
            return null;

        }
    }

    public void selectItem(String categoryId) {
        for (CategoryData data : categoryData)
            if (data.getId().equals(categoryId))
                selectedData = data;
    }
}
