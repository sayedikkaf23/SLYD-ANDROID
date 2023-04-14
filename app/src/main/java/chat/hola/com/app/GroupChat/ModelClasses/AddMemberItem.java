package chat.hola.com.app.GroupChat.ModelClasses;

/**
 * Created by moda on 26/09/17.
 */

public class AddMemberItem {


    private boolean isSelected,isChatEnable;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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


    /*
     * For adding the groupMember functionality
     */


    public boolean isAllowedToAdd() {
        return allowedToAdd;
    }

    public void setAllowedToAdd(boolean allowedToAdd) {
        this.allowedToAdd = allowedToAdd;
    }

    private boolean allowedToAdd;


    private boolean isStar;

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }

    public boolean isChatEnable() {
        return isChatEnable;
    }

    public void setChatEnable(boolean chatEnable) {
        isChatEnable = chatEnable;
    }
}
