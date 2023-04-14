package chat.hola.com.app.ModelClasses;

/**
 * Created by moda on 26/07/17.
 */

public class ChatlistItem {


    private String receiverUid;
    private String documentId;
    private String newMessage;
    private String newMessageCount;
    private String newMessageTime;
    private String membersInfo;
    private boolean isStar;

    private boolean receiverInContacts;

    private int tickStatus;

    public int getTickStatus() {
        return tickStatus;
    }

    public void setTickStatus(int tickStatus) {
        this.tickStatus = tickStatus;
    }

    public boolean isShowTick() {
        return showTick;
    }

    public void setShowTick(boolean showTick) {
        this.showTick = showTick;
    }

    private boolean showTick;


    public boolean isReceiverInContacts() {
        return receiverInContacts;
    }

    public void setReceiverInContacts(boolean receiverInContacts) {
        this.receiverInContacts = receiverInContacts;
    }

    /**
     * Can be receiver phoneNumber,email or the userName
     */


    private String receiverIdentifier;

    public String getReceiverIdentifier() {
        return receiverIdentifier;
    }

    public void setReceiverIdentifier(String receiverIdentifier) {
        this.receiverIdentifier = receiverIdentifier;
    }

    private String receiverName;

    public String getReceiverImage() {
        return receiverImage;
    }

    public void setReceiverImage(String receiverImage) {
        this.receiverImage = receiverImage;
    }

    private String receiverImage;

    private boolean isNewMessage;


    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String ReceiverName) {
        this.receiverName = ReceiverName;
    }


    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String DocumentId) {
        this.documentId = DocumentId;
    }


    public String getNewMessageCount() {
        return newMessageCount;
    }

    public void setNewMessageCount(String newMessageCount) {
        this.newMessageCount = newMessageCount;
    }


    public String getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(String newMessage) {
        this.newMessage = newMessage;
    }


    public String getNewMessageTime() {
        return newMessageTime;
    }

    public void setNewMessageTime(String newMessageTime) {
        this.newMessageTime = newMessageTime;
    }


    public boolean hasNewMessage() {
        return isNewMessage;
    }

    public void sethasNewMessage(boolean isNewMessage) {
        this.isNewMessage = isNewMessage;
    }


    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String ReceiverUid) {
        this.receiverUid = ReceiverUid;
    }


    public boolean isSecretChat() {
        return isSecretChat;
    }

    public void setSecretChat(boolean secretChat) {
        isSecretChat = secretChat;
    }

    private boolean isSecretChat;

    private String secretId;

    public String getSecretId() {
        return secretId;
    }

    public void setSecretId(String secretId) {
        this.secretId = secretId;
    }


    /*
     * For allowing the message forwarding
     */


    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    private boolean isGroupChat;

    public boolean isGroupChat() {
        return isGroupChat;
    }

    public void setGroupChat(boolean groupChat) {
        isGroupChat = groupChat;
    }


    /*
     * For the option of the message forwarding int he group chat
     */


    private String groupMembersDocId;

    public String getGroupMembersDocId() {
        return groupMembersDocId;
    }

    public void setGroupMembersDocId(String groupMembersDocId) {
        this.groupMembersDocId = groupMembersDocId;
    }


    /*
     * For the option of the direct scroll to the specified message in the chat
     */

    private boolean fromSearchMessage;

    public boolean isFromSearchMessage() {
        return fromSearchMessage;
    }

    public void setFromSearchMessage(boolean fromSearchMessage) {
        this.fromSearchMessage = fromSearchMessage;
    }


    private int messagePosition;

    public int getMessagePosition() {
        return messagePosition;
    }

    public void setMessagePosition(int messagePosition) {
        this.messagePosition = messagePosition;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }

    public String getMembersInfo() {
        return membersInfo;
    }

    public void setMembersInfo(String membersInfo) {
        this.membersInfo = membersInfo;
    }
}
