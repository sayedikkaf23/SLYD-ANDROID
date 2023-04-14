package chat.hola.com.app.ForwardMessage;

/**
 * Created by moda on 30/08/17.
 */

public class Forward_ContactItem {


    private boolean isSelected,isChatEnable;
    private Boolean isInvite;
    private String  type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getInvite() {
        return isInvite;
    }

    public void setInvite(Boolean invite) {
        isInvite = invite;
    }

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

//For star secret chat

    private boolean isStar;

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }

    public void setChatEnable(boolean isChatEnable) {
        this.isChatEnable = isChatEnable;
    }

    public boolean isChatEnable() {
        return isChatEnable;
    }
}
