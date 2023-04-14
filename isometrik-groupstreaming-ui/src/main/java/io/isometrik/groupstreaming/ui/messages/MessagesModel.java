package io.isometrik.groupstreaming.ui.messages;

import io.isometrik.groupstreaming.ui.IsometrikUiSdk;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.utils.DateUtil;
import io.isometrik.groupstreaming.ui.utils.MessageTypeEnum;
import io.isometrik.groupstreaming.ui.utils.UserSession;
import io.isometrik.gs.events.message.MessageAddEvent;
import io.isometrik.gs.response.message.FetchMessagesResult;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The type Messages model.
 */
public class MessagesModel {

  private String messageId;

  private long timestamp;

  private String senderId;

  private String senderIdentifier;

  private String senderImage;

  private String senderName;

  private String message;

  private int messageType;

  private boolean delivered;

  private boolean canRemoveMessage;

  private boolean receivedMessage;

  private boolean canJoin;
  private String messageTime;

  private int coinValue;
  private String giftName;
  private boolean isInitiator;
  /**
   * Type 0- normal message, Type 3- presence message, Type 2- Removed message,  Type 1- like
   * message,
   */
  private int messageItemType;

  /**
   * Instantiates a new Messages model.
   *
   * @param message the message
   * @param givenUserIsMember the given user is member
   * @param userId the user id
   */
  public MessagesModel(FetchMessagesResult.StreamMessage message, boolean givenUserIsMember,
      String userId) {

    this.message = message.getMessage();
    messageId = message.getMessageId();
    messageType = message.getMessageType();
    senderId = message.getSenderId();
    senderIdentifier = message.getSenderIdentifier();
    senderImage = message.getSenderImage();
    if (IsometrikUiSdk.getInstance().getUserSession().getUserId().equals(senderId)) {

      senderName = IsometrikUiSdk.getInstance()
          .getContext()
          .getString(R.string.ism_you, message.getSenderName());
    } else {

      senderName = message.getSenderName();
    }
    timestamp = message.getSentAt();
    canRemoveMessage = givenUserIsMember;

    if (messageType == MessageTypeEnum.HeartMessage.getValue()) {
      messageItemType = MessageTypeEnum.HeartMessage.getValue();
    } else if (messageType == MessageTypeEnum.GiftMessage.getValue()) {
      messageItemType = MessageTypeEnum.GiftMessage.getValue();
      try {
        JSONObject jsonObject = new JSONObject(this.message);
        this.message = jsonObject.getString("message");
        coinValue = jsonObject.getInt("coinsValue");
        giftName = jsonObject.getString("giftName");
      } catch (JSONException e) {
        e.printStackTrace();
      }
    } else {
      messageItemType = MessageTypeEnum.NormalMessage.getValue();
    }
    messageTime = DateUtil.getDate(timestamp);
    if (userId.equals(senderId)) {
      delivered = true;
      receivedMessage = false;
    } else {
      receivedMessage = true;
    }
  }

  /**
   * Instantiates a new Messages model.
   *
   * @param message the message
   * @param messageItemType the message item type
   * @param timestamp the timestamp
   */
  public MessagesModel(String message, int messageItemType, long timestamp) {

    this.message = message;
    this.messageItemType = messageItemType;
    messageTime = DateUtil.getDate(timestamp);
  }

  /**
   * Instantiates a new Messages model.
   *
   * @param messageAddEvent the message add event
   * @param givenUserIsMember the given user is member
   */
  public MessagesModel(MessageAddEvent messageAddEvent, boolean givenUserIsMember) {

    this.message = messageAddEvent.getMessage();
    messageId = messageAddEvent.getMessageId();
    messageType = messageAddEvent.getMessageType();

    senderId = messageAddEvent.getSenderId();
    senderIdentifier = messageAddEvent.getSenderIdentifier();
    senderImage = messageAddEvent.getSenderImage();
    senderName = messageAddEvent.getSenderName();
    timestamp = messageAddEvent.getTimestamp();
    canRemoveMessage = givenUserIsMember;
    messageItemType = MessageTypeEnum.NormalMessage.getValue();
    messageTime = DateUtil.getDate(timestamp);
    receivedMessage = true;
  }

  /**
   * Instantiates a new Messages model.
   *
   * @param message the message
   * @param messageId the message id
   * @param messageType the message type
   * @param timestamp the timestamp
   * @param givenUserIsMember the given user is member
   * @param userSession the user session
   */
  public MessagesModel(String message, String messageId, int messageType, long timestamp,
      boolean givenUserIsMember, UserSession userSession) {

    this.message = message;
    this.messageId = messageId;
    this.messageType = messageType;
    this.timestamp = timestamp;

    senderId = userSession.getUserId();
    senderIdentifier = userSession.getUserIdentifier();
    senderImage = userSession.getUserProfilePic();
    senderName = IsometrikUiSdk.getInstance()
        .getContext()
        .getString(R.string.ism_you, userSession.getUserName());

    canRemoveMessage = givenUserIsMember;
    messageItemType = MessageTypeEnum.NormalMessage.getValue();
    messageTime = DateUtil.getDate(timestamp);
    delivered = false;
    receivedMessage = false;
  }

  public MessagesModel(String message, int messageItemType, String senderId,
      String senderIdentifier, String senderImage, String senderName, long timestamp,
      boolean isInitiator, boolean canJoin) {

    this.message = message;
    this.messageItemType = messageItemType;
    this.messageTime = DateUtil.getDate(timestamp);

    this.senderId = senderId;
    this.senderIdentifier = senderIdentifier;
    this.senderImage = senderImage;
    this.senderName = senderName;
    this.timestamp = timestamp;
    this.messageTime = DateUtil.getDate(timestamp);
    this.isInitiator = isInitiator;
    this.canJoin = canJoin;
  }

  /**
   * Gets message id.
   *
   * @return the message id
   */
  public String getMessageId() {
    return messageId;
  }

  /**
   * Gets timestamp.
   *
   * @return the timestamp
   */
  long getTimestamp() {
    return timestamp;
  }

  /**
   * Gets sender id.
   *
   * @return the sender id
   */
  public String getSenderId() {
    return senderId;
  }

  /**
   * Gets sender identifier.
   *
   * @return the sender identifier
   */
  public String getSenderIdentifier() {
    return senderIdentifier;
  }

  /**
   * Gets sender image.
   *
   * @return the sender image
   */
  String getSenderImage() {
    return senderImage;
  }

  /**
   * Gets sender name.
   *
   * @return the sender name
   */
  String getSenderName() {
    return senderName;
  }

  /**
   * Gets message.
   *
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * Gets message type.
   *
   * @return the message type
   */
  public int getMessageType() {
    return messageType;
  }

  /**
   * Is delivered boolean.
   *
   * @return the boolean
   */
  boolean isDelivered() {
    return delivered;
  }

  /**
   * Is can remove message boolean.
   *
   * @return the boolean
   */
  boolean isCanRemoveMessage() {
    return canRemoveMessage;
  }

  /**
   * Gets message time.
   *
   * @return the message time
   */
  String getMessageTime() {
    return messageTime;
  }

  /**
   * Gets message item type.
   *
   * @return the message item type
   */
  public int getMessageItemType() {
    return messageItemType;
  }

  /**
   * Is received message boolean.
   *
   * @return the boolean
   */
  boolean isReceivedMessage() {
    return receivedMessage;
  }

  /**
   * Sets message.
   *
   * @param message the message
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Sets message item type.
   *
   * @param messageItemType the message item type
   */
  public void setMessageItemType(int messageItemType) {
    this.messageItemType = messageItemType;
  }

  /**
   * Sets message id.
   *
   * @param messageId the message id
   */
  public void setMessageId(String messageId) {
    this.messageId = messageId;
  }

  /**
   * Sets timestamp.
   *
   * @param timestamp the timestamp
   */
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Sets delivered.
   *
   * @param delivered the delivered
   */
  public void setDelivered(boolean delivered) {
    this.delivered = delivered;
  }

  /**
   * Gets coin value.
   *
   * @return the coin value
   */
  int getCoinValue() {
    return coinValue;
  }

  /**
   * Gets gift name.
   *
   * @return the gift name
   */
  String getGiftName() {
    return giftName;
  }

  /**
   * Gets whether given user is the initiator of the stream.
   *
   * @return the boolean isInitiator
   */
  public boolean isInitiator() {
    return isInitiator;
  }

  /**
   * Gets whether given user can join the stream as co-publisher.
   *
   * @return the boolean canJoin
   */
  public boolean isCanJoin() {
    return canJoin;
  }
  /**
   * Updates status of a user as initiator
   *
   * @param initiator whether given user is initiator or not
   */
  public void setInitiator(boolean initiator) {
    isInitiator = initiator;
  }

  /**
   * Updates status of a user as can join as publisher or not
   *
   * @param canJoin whether given user can join a broadcast a publisher
   */
  public void setCanJoin(boolean canJoin) {
    this.canJoin = canJoin;
  }
}
