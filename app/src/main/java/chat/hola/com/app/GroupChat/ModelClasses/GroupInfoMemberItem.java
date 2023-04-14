package chat.hola.com.app.GroupChat.ModelClasses;

/**
 * Created by moda on 22/09/17.
 */

public class GroupInfoMemberItem {

    private boolean isAdmin;

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    private String contactUid, contactName, contactImage, contactIdentifier, contactStatus;

    public String getContactUid() {
        return contactUid;
    }

    public void setContactUid(String contactUid) {
        this.contactUid = contactUid;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactImage() {
        return contactImage;
    }

    public void setContactImage(String contactImage) {
        this.contactImage = contactImage;
    }

    public String getContactIdentifier() {
        return contactIdentifier;
    }

    public void setContactIdentifier(String contactIdentifier) {
        this.contactIdentifier = contactIdentifier;
    }

    public String getContactStatus() {
        return contactStatus;
    }


    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }


    private boolean isStar;

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }
}
