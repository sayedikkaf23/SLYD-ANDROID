package chat.hola.com.app.profileScreen.discover;

import android.content.Context;

import java.util.ArrayList;

import chat.hola.com.app.Utilities.BaseView;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contact;

/**
 * <h>DiscoverContract</h>
 *
 * @author 3Embed.
 * @since 2/3/18.
 */

public interface DiscoverContract {

    interface View extends BaseView {
        void applyFont();

        void changeSkipText(String text);

        void showContacts(ArrayList<Contact> addContact, boolean clear);

        void postToBus();

        void onUserSelected(int position);

        void add(int position);

        void empty(boolean b);


        void isFollowing(int pos, int status);

        void onFilter(int count);
    }

    interface Presenter {
        void init(Context context);

        void changeText(String text);

        void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount, int totalItemCount);
    }
}
