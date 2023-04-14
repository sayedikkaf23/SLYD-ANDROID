package chat.hola.com.app.home.activity.youTab.model;

import java.util.ArrayList;

import javax.inject.Inject;

import chat.hola.com.app.home.activity.youTab.YouAdapter;

/**
 * @author 3Embed.
 * @since 5/4/18.
 */

public class YouModel {

    @Inject
    YouAdapter adapter;

    @Inject
    ArrayList<Data> arrayList;

    @Inject
    public YouModel() {
    }

    public void setData(ArrayList<Data> arrayList){
        this.arrayList.addAll(arrayList);
        adapter.setData(arrayList);
        adapter.notifyDataSetChanged();
    }

}
