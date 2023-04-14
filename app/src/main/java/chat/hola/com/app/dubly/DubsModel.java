package chat.hola.com.app.dubly;

import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.manager.session.SessionManager;

/**
 * <h1>DubCategoryModel</h1>
 *
 * @author 3Embed
 * @since 4/10/2018.
 */

public class DubsModel {

    @Inject
    List<Dub> dubs;
    @Inject
    DubsAdapter adapter;
    @Inject
    SessionManager sessionManager;

    @Inject
    DubsModel() {
    }

    public void setData(List<Dub> data) {
        if (data != null) {
            this.dubs.addAll(data);
            adapter.notifyDataSetChanged();
        }
    }

    public void clearList() {
        this.dubs.clear();
    }

    public void setPlaying(int position, boolean isPlaying) {
        dubs.get(position).setPlaying(isPlaying);
        if (isPlaying) {
            for (int i = 0; i < dubs.size(); i++)
                if (i != position)
                    dubs.get(i).setPlaying(false);
        }
        adapter.notifyDataSetChanged();
    }

    public String getPath(int position) {
        return dubs.get(position).getPath();
    }

    public String getAudio(int position) {
        return dubs.get(position).getPath();
    }

    public String getMusicId(int position) {
        return dubs.get(position).getId();
    }

    public void setFavourite(int position, boolean flag) {
        dubs.get(position).setMyFavourite(flag ? 1 : 0);
    }

    public String getName(int position) {
        return dubs.get(position).getName();
    }

    public String getDuration(int position) {
        return dubs.get(position).getDuration();
    }
}
