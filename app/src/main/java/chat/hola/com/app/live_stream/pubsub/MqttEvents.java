package chat.hola.com.app.live_stream.pubsub;

/**
 * <h>MqttEvents</h>
 * Created by moda on 27/09/17.
 */


public enum MqttEvents {

    WillTopic("last-will"),
    // stream-now/ -- for all live streams
    AllStreams("stream-now"),

    //for subscribed stream chat
    ParticularStreamChatMessages("stream-chat"),

    // for subscribed stream presence events
    ParticularStreamPresenceEvents("stream-subscribe"),


    //for subscribed stream like events
    ParticularStreamLikeEvent("stream-like"),

    // for subscribed stream gift events
    ParticularStreamGiftEvent("stream-gift"),


    Call("call"),

    Connect("connect"),


    Disconnect("disconnect");
    public String value;

    MqttEvents(String value) {
        this.value = value;
    }
//   /* Provider("provider"),
//    JobStatus("jobStatus"),
//    LiveTrack("liveTrack"),
//    Message("message"),*/
//    PresenceTopic("PresenceTopic"),
//    WillTopic("lastWill"),
//    StreamNow("stream-now"),
//    StreamChat("stream-chat"),
//    StreamSubscrib("stream-subscribe"),
//    Call("call");
//    public String value;
//
//    MqttEvents(String value) {
//        this.value = value;
//    }


}