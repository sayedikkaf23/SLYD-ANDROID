package chat.hola.com.app.profileScreen.discover.follow;

import java.util.ArrayList;

import chat.hola.com.app.profileScreen.discover.contact.pojo.Contact;

/**
 * @author 3Embed.
 * @since 02/03/18.
 */

public class Follow {

    int followersCount = 200;
    ArrayList<Contact> contacts;

    public Follow(int followersCount, ArrayList<Contact> contacts) {
        this.followersCount = followersCount;
        this.contacts = contacts;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public ArrayList<Contact> getContacts() {
        return contacts;
    }
}
