package chat.hola.com.app.home.trending.model;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.manager.session.SessionManager;

/**
 * <h1>TrendingModel</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 6/18/2018.
 */

public class TrendingModel {

    @Inject
    ArrayList<Header> headers;
    @Inject
    ArrayList<TrendingResponse> data;
    @Inject
    HeaderAdapter headerAdapter;
    @Inject
    TrendingContentAdapter contentAdapter;
    @Inject
    SessionManager sessionManager;
    private String categoryId;

    @Inject
    TrendingModel() {
    }

    public ArrayList<Header> setHeader(ArrayList<Header> headers) {
        this.headers = headers;
        this.headerAdapter.notifyDataSetChanged();
        categoryId = headers.get(1).getId();
        return this.headers;
    }

    public ArrayList<TrendingResponse> setData(ArrayList<TrendingResponse> data) {
        if (!data.isEmpty()) {
            this.data.addAll(data);
            this.contentAdapter.refresh(this.data);
        }
        return this.data;
    }

    public String getCategoryId(int position) {
        return headers.get(position).getId();
    }

    public TrendingResponse getItem(int position) {
        return data.get(position);
    }

    public StringBuilder generateHashtagString(List<String> hashTags) {
        StringBuilder result = new StringBuilder();
        for (String s : hashTags)
            result.append(s).append(" ");
        return result;
    }


    public void setSelected(String selectedCategoryId) {
        for (Header header : headers)
            if (header.getId().equals(selectedCategoryId)) {
                header.setSelected(true);
                this.categoryId = selectedCategoryId;
            }
    }

    public List<TrendingResponse> getList() {
        return data;
    }

    public void clear() {
        data.clear();
        contentAdapter.notifyDataSetChanged();
    }

}
