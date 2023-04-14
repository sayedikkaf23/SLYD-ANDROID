package chat.hola.com.app.music;

import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.home.model.Data;

/**
 * <h1>MusicModel</h1>
 *
 * @author 3Embed
 * @since 4/10/2018.
 */

class MusicModel {

    @Inject
    List<Data> dataList;
    @Inject
    MusicAdapter adapter;

    @Inject
    MusicModel() {
    }

    public void setData(List<Data> data, boolean clear) {
        if (data != null) {
            if(clear)this.dataList.clear();
            this.dataList.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    public List<Data> getData() {
        return dataList;
    }
}
