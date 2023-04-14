package chat.hola.com.app.profileScreen.discover.contact;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.BasePresenter;
import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contact;
import chat.hola.com.app.profileScreen.discover.contact.pojo.ContactRequest;

/**
 * <h>ContactContract</h>
 *
 * @author 3Embed.
 * @since 02/03/18.
 */

public interface ContactContract {

    interface View extends BaseView {
        void initialization();

        void applyFont();

        void initPostRecycler();

        void isFollowing(int pos, boolean isFollow,int status, int isPrivate, boolean isFollowAll);

        void loading(boolean flag);

        void showContacts(ArrayList<Contact> contacts);

        void requestContactsPermission();

        void postToBus();

        void followedAll(boolean flag);

    }

    interface Presenter extends BasePresenter<ContactContract.View> {

        void init();

        void addContacts();

        void fetchContact();

        void contactSync(ContactRequest request);

        void follow(int pos, String id);

        void unfollow(int pos, String id);

        void requestContactsPermission();

        void mqttResponse(JSONObject object);

        void followAll(List<String> strings);
    }
}
