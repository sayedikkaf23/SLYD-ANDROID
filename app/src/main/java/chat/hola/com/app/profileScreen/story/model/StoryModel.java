package chat.hola.com.app.profileScreen.story.model;

import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.home.model.Data;

public class StoryModel {

    @Inject
    List<Data> data;
    @Inject
    TrendingDtlAdapter adapter;


    @Inject
    public StoryModel() {

    }

    public void setData(List<Data> data, boolean isFirst) {
        if (isFirst)
            this.data.clear();
        this.data.addAll(data);
        adapter.notifyDataSetChanged();
    }
}
