package chat.hola.com.app.profileScreen.discover.contact.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DELL on 3/21/2018.
 */

public class Contacts implements Serializable {

    @SerializedName("contacts")
    @Expose
    private ArrayList<Contact> contacts = null;
    @SerializedName("eventName")
    @Expose
    private String eventName;
    @SerializedName("isFollowedAll")
    @Expose
    private boolean isFollowedAll;
    @SerializedName("isLastPage")
    @Expose
    private boolean isLastPage;

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public boolean getIsFollowedAll() {
        return isFollowedAll;
    }

    public void setIsFollowedAll(boolean isFollowedAll) {
        this.isFollowedAll = isFollowedAll;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }
}
