package io.isometrik.gs.models.events;

import io.isometrik.gs.Isometrik;
import io.isometrik.gs.events.message.MessageAddEvent;
import io.isometrik.gs.events.message.MessageRemoveEvent;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The model for handling the message events received and broadcasting them to all
 * MessageEventCallback{@link io.isometrik.gs.callbacks.MessageEventCallback} listeners listening
 * for MessageAddEvent{@link io.isometrik.gs.events.message.MessageAddEvent} or
 * MessageRemoveEvent{@link io.isometrik.gs.events.message.MessageRemoveEvent}.
 *
 * @see io.isometrik.gs.callbacks.MessageEventCallback
 * @see io.isometrik.gs.events.message.MessageAddEvent
 * @see io.isometrik.gs.events.message.MessageRemoveEvent
 */
public class MessageEvents {
  /**
   * Handle message event.
   *
   * @param jsonObject the json object containing the details of the message add or remove event received
   * @param isometrikInstance the isometrik instance
   * @throws JSONException the json exception
   */
  public void handleMessageEvent(JSONObject jsonObject, @NotNull Isometrik isometrikInstance)
      throws JSONException {

    String action = jsonObject.getString("action");

    switch (action) {

      case "messageSent":

        isometrikInstance.getMessageListenerManager()
            .announce(
                isometrikInstance.getGson().fromJson(jsonObject.toString(), MessageAddEvent.class));
        break;
      case "messageRemoved":

        isometrikInstance.getMessageListenerManager()
            .announce(isometrikInstance.getGson()
                .fromJson(jsonObject.toString(), MessageRemoveEvent.class));
        break;
    }
  }
}
