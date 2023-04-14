package chat.hola.com.app.GroupChat.ModelClasses;

/**
 * Created by moda on 05/10/17.
 */

public class MemberMessageAckItem {


    private String contactName;
    private String contactImage;
    private String deliveryTime;
    private boolean isReadMember;

    public boolean isReadMember() {
        return isReadMember;
    }

    public void setReadMember(boolean readMember) {
        isReadMember = readMember;
    }

    public String getReadTime() {
        return readTime;
    }

    public void setReadTime(String readTime) {
        this.readTime = readTime;
    }

    private String readTime;


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


    public String getDeliveryTime() {
        return deliveryTime;
    }


    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }


}
