package chat.hola.com.app.profileScreen.discover.contact.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by DELL on 3/14/2018.
 */

public class ContactRequest implements Serializable {
    @SerializedName("contacts")
    @Expose
    private ArrayList<Contact> contacts = null;

    public ArrayList<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }
}
