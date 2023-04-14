package chat.hola.com.app.home.social.model;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.home.model.Data;

/**
 * <h1>SocialModel</h1>
 *
 * @author 3Embed
 * @since 4/5/2018.
 */

public class SocialModel {

    @Inject
    public List<Data> arrayList;
    @Inject
    SocialAdapter mAdapter;

    @Inject
    SocialModel() {
    }

    public String getUserId(int position) {
        try {
            return arrayList.get(position).getUserId();
        } catch (ArrayIndexOutOfBoundsException e) {
            return arrayList.get(arrayList.size() - 1).getUserId();
        }
    }

    public void clearData() {
        this.arrayList.clear();
    }

    public void setData(List<Data> data, boolean isFirst) {
        if (isFirst)
            clearData();
        this.arrayList.addAll(data);
        mAdapter.setData(arrayList);
    }

    public Data getData(int position) {
        return arrayList.get(position);
    }

    public String getChannelId(int position) {
        return arrayList.get(position).getChannelId();
    }

    public List<Data> getAllData() {
        return new ArrayList<>(arrayList);
    }

}
