package chat.hola.com.app.ModelClasses;

/**
 * Created by moda on 28/07/17.
 */

public class ContactsItem {//implements GroupListWrapper.Selector {

    private String contactUid, contactName, contactImage, contactIdentifier, contactStatus, type;
    private Integer followStatus, _private;
    private boolean star;
    private Boolean isSelected = false,isInvite;
    private boolean isChatEnable;

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

    public void set_private(Integer _private) {
        this._private = _private;
    }

    public void setFollowStatus(Integer followStatus) {
        this.followStatus = followStatus;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public boolean getStar() {
        return star;
    }

    public Boolean isSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public void setChatEnable(boolean isChatEnable) {
        this.isChatEnable = isChatEnable;
    }

    public boolean isChatEnable() {
        return isChatEnable;
    }

//    private int itemType;
//
//    public int getItemType() {
//        return itemType;
//    }
//
//
//    public void setItemType(int itemType) {
//        this.itemType = itemType;
//    }
//    @Override
//    public String select() {
//        return contactName;
//    }
}
