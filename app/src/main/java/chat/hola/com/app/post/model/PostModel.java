package chat.hola.com.app.post.model;

import com.google.gson.Gson;

import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.location.Address_list_holder;
import chat.hola.com.app.location.Address_list_item_pojo;
import chat.hola.com.app.manager.session.SessionManager;

/**
 * <h1>PostModel</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 5/9/2018.
 */

public class PostModel {
    @Inject
    Address_list_holder listHolder;
    @Inject
    List<Address_list_item_pojo> addresslist;
    @Inject
    SessionManager sessionManager;
    @Inject
    AddressAdapter addressAdapter;

    @Inject
    PostModel() {
    }

    public void setAddresses() {
        listHolder = new Gson().fromJson(sessionManager.getAdresses(), Address_list_holder.class);
        addresslist.addAll(listHolder.getList_of_address());
        addressAdapter.notifyDataSetChanged();
    }

    public Address_list_item_pojo getAddess(int position) {
        return addresslist.get(position);
    }
}
