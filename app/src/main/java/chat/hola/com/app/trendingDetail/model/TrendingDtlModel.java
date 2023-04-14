package chat.hola.com.app.trendingDetail.model;

import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.profileScreen.channel.Model.ChannelData;

/**
 * <h1>TrendingDtlModel</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 5/4/2018.
 */

public class TrendingDtlModel {

    @Inject
    List<Data> data;
    @Inject
    TrendingAdapter adapter;

    @Inject
    TrendingDtlModel() {
    }

    public void setData(ChannelData data, boolean clear) {
        if(clear)this.data.clear();
        this.data.addAll(data.getData());
        adapter.notifyDataSetChanged();
    }

    public List<Data> getData() {
        return data;
    }
}
