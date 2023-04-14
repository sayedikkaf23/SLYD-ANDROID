package chat.hola.com.app.GroupChat.ModelClasses;

import android.net.Uri;

/**
 * Created by moda on 03/10/17.
 */

public class GroupChatMessageItem {

    private String message, messageDateOverlay, messageType, id, fromName, videoPath, imagepath, audioPath, ts, date, deliveryStatus, contactInfo;
    private boolean isSelf;


    private boolean isDownloading;


    private long messageDateGMT;

    private Uri imageUrl;

    private int downloadStatus;

    private String thumbnailPath;

    private String size;

    private String placeInfo;


    private String receiverUid;

    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public void setDownloading(boolean downloading) {
        isDownloading = downloading;
    }


    public String getMessageId() {
        return id;
    }

    public void setMessageId(String id) {
        this.id = id;
    }


    public String getMessageDateOverlay() {
        return messageDateOverlay;
    }

    public void setMessageDateOverlay(String MessageDateOverlay) {
        this.messageDateOverlay = MessageDateOverlay;
    }


    public long getMessageDateGMTEpoch() {
        return messageDateGMT;
    }

    public void setMessageDateGMTEpoch(long MessageDateGMT) {
        this.messageDateGMT = MessageDateGMT;
    }


    public String getTS() {
        return ts;
    }

    public void setTS(String ts) {
        this.ts = ts;
    }


    public String getSenderName() {
        return fromName;
    }

    public void setSenderName(String fromName) {
        this.fromName = fromName;
    }


    public boolean isSelf() {
        return isSelf;
    }

    public void setIsSelf(boolean isSelf) {
        this.isSelf = isSelf;
    }


    /**
     * 0-text,1-image,2-video,3-location,4-contact,5-audio
     */
    public void setMessageType(String MessageType) {
        this.messageType = MessageType;
    }

    public String getMessageType() {
        return messageType;
    }


    public String getTextMessage() {
        return message;
    }

    public void setTextMessage(String message) {
        this.message = message;
    }


    public void setImagePath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getImagePath() {
        return imagepath;
    }


    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoPath() {
        return videoPath;
    }


    public void setPlaceInfo(String placeInfo) {
        this.placeInfo = placeInfo;
    }

    public String getPlaceInfo() {
        return placeInfo;
    }


    public void setContactInfo(String ContactInfo) {
        this.contactInfo = ContactInfo;
    }

    public String getContactInfo() {
        return contactInfo;
    }


    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getAudioPath() {
        return audioPath;
    }


    public void setDate(String date) {
        this.date = date;
    }


    public String getDate() {
        return date;
    }


    /**
     * status-0 not sent
     * status-1 sent
     * status-2 delivered
     * status-3 read
     */


    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String DeliveryStatus) {
        this.deliveryStatus = DeliveryStatus;
    }


    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }


    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }


    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }


    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Uri getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Uri imageUrl) {
        this.imageUrl = imageUrl;
    }


    /*
     * For non-sup specific item sharing
     */


    private String gifUrl;

    public String getStickerUrl() {
        return stickerUrl;
    }

    public void setStickerUrl(String stickerUrl) {
        this.stickerUrl = stickerUrl;
    }

    private String stickerUrl;

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    /*
     * For document sharing
     */

    private String extension;
    private String mimeType;
    private String fileName;
    private String documentUrl;

    private String fileType;

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }


    public void setDocumentUrl(String documentUrl) {
        this.documentUrl = documentUrl;
    }


    public String getDocumentUrl() {
        return documentUrl;
    }


    public String getExtension() {
        return extension;
    }


    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    /*
     * For the reply message feature
     */

    private String previousMessageType;
    private String previousMessageId;
    private String previousMessagePayload;
    private String previousSenderName;
    private String previousSenderId;

    private String previousFileType;


    private String replyType;

    public String getReplyType() {
        return replyType;
    }

    public void setReplyType(String replyType) {
        this.replyType = replyType;
    }

    public String getPreviousMessageType() {
        return previousMessageType;
    }

    public void setPreviousMessageType(String previousMessageType) {
        this.previousMessageType = previousMessageType;
    }

    public String getPreviousMessageId() {
        return previousMessageId;
    }

    public void setPreviousMessageId(String previousMessageId) {
        this.previousMessageId = previousMessageId;
    }

    public String getPreviousMessagePayload() {
        return previousMessagePayload;
    }

    public void setPreviousMessagePayload(String previousMessagePayload) {
        this.previousMessagePayload = previousMessagePayload;
    }

    public String getPreviousSenderName() {
        return previousSenderName;
    }

    public void setPreviousSenderName(String previousSenderName) {
        this.previousSenderName = previousSenderName;
    }

    public String getPreviousSenderId() {
        return previousSenderId;
    }

    public void setPreviousSenderId(String previousSenderId) {
        this.previousSenderId = previousSenderId;
    }

    public String getPreviousFileType() {
        return previousFileType;
    }

    public void setPreviousFileType(String previousFileType) {
        this.previousFileType = previousFileType;
    }


    /*
     * For the group chat exclusively
     */
    /**
     * Used top identify the sender of the previous message incase of the reply message
     */

    private String senderId, senderIdentifier;

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderIdentifier() {
        return senderIdentifier;
    }

    public void setSenderIdentifier(String senderIdentifier) {
        this.senderIdentifier = senderIdentifier;
    }


    private String previousReceiverIdentifier;

    public String getPreviousReceiverIdentifier() {
        return previousReceiverIdentifier;
    }

    public void setPreviousReceiverIdentifier(String previousReceiverIdentifier) {
        this.previousReceiverIdentifier = previousReceiverIdentifier;
    }



    /*
     * For the post message
     */

    private String postId;
    private String postTitle;
    private int postType;


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public int getPostType() {
        return postType;
    }

    public void setPostType(int postType) {
        this.postType = postType;
    }

}
