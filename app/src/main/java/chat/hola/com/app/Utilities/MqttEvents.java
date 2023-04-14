package chat.hola.com.app.Utilities;

/**
 * Created by moda on 29/06/17.
 */


public enum MqttEvents {


    OnlineStatus("OnlineStatus"),
    Acknowledgement("Acknowledgement"),
    Typing("Typ/"),
    Message("Message"),
    Calls("Calls"),
    CallsAvailability("CallsAvailability"),


    /*
     * Not a topic on server,just are used locally
     */
    Connect("Connect"),

    MessageResponse("MessageResponse"),

    Disconnect("Disconnect"),


    /**
     * For contact Sync
     */
    ContactSync("ContactSync"),


    /*
     *For the user updates of various kinds
     */



    UserUpdates("UserUpdate"),


    FetchChats("GetChats"),


    FetchMessages("GetMessages"),


    /*
     *For logging on the server
     */

    GroupChatAcks("GroupChatAcknowledgement"),


    GroupChatMessages("GroupChat"),

    /*
     * For peer to peer messaging
     */
    GroupChats("GroupChats");


    public String value;


    MqttEvents(String value) {
        this.value = value;
    }


}