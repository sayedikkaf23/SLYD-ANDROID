package chat.hola.com.app.profileScreen.business.post.type;


import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.models.PostTypeResponse;

/**
 * <h1>PostTypeModel</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 09 September 2019
 */
public class PostTypeModel {

    @Inject
    List<PostTypeResponse.PostType> dataList;
    @Inject
    PostTypeAdapter adapter;

    @Inject
    public PostTypeModel() {
    }

    public void setData(List<PostTypeResponse.PostType> dataList) {
        this.dataList.clear();
        this.dataList = dataList;
        int position = 0;
        for (int i = 0; i < dataList.size(); i++) {
            if (PostTypeFragment.postType.equalsIgnoreCase(dataList.get(i).getText())) {
                position = i;
                break;
            }
        }
        adapter.setData(dataList, position);
    }
}
