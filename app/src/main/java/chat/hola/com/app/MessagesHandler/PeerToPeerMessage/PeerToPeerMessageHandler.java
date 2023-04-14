package chat.hola.com.app.MessagesHandler.PeerToPeerMessage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Base64;
import androidx.core.app.ActivityCompat;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.MessagesHandler.Utilities.DownloadMessage;
import chat.hola.com.app.SecretChat.SecretChatMessageScreen;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.Utilities;

public class PeerToPeerMessageHandler {

    private AppController instance = AppController.getInstance();
    /*
     *For message auto download
     */
    private String removedMessageString = "The message has been removed";

    public void handlePeerToPeerMessage(JSONObject obj) {
        /*
         * For an actual message(Like text,image,video etc.) received
         */

        try {

            String topic =obj.getString("topic");

            String receiverUid = obj.getString("from");

            String receiverIdentifier = obj.getString("receiverIdentifier");


            String messageType = obj.getString("type");

            // Missedcall message come to call initiater then nothing to do with this
            if ((messageType.equals("16") ||messageType.equals("17"))
                    && receiverUid.equals(instance.getUserId())) {
                return;
            }


            String actualMessage = obj.getString("payload").trim();
            String timestamp = String.valueOf(obj.getString("timestamp"));


            String id = obj.getString("id");

            String mimeType = "", fileName = "", extension = "";
            String docIdForDoubleTickAck = obj.getString("toDocId");
            int dataSize = -1;

            String postId = "", postTitle = "";
            int postType = -1;
            boolean isStar = false;

            if (messageType.equals("1") || messageType.equals("2") || messageType.equals("5") || messageType.equals("7")
                    || messageType.equals("9")) {
                dataSize = obj.getInt("dataSize");


                if (messageType.equals("9")) {


                    mimeType = obj.getString("mimeType");
                    fileName = obj.getString("fileName");
                    extension = obj.getString("extension");


                }


            } else if (messageType.equals("10")) {

                String replyType = obj.getString("replyType");

                if (replyType.equals("1") || replyType.equals("2") || replyType.equals("5") || replyType.equals("7")
                        || replyType.equals("9")) {
                    dataSize = obj.getInt("dataSize");


                    if (replyType.equals("9")) {


                        mimeType = obj.getString("mimeType");
                        fileName = obj.getString("fileName");
                        extension = obj.getString("extension");


                    }


                } else if (replyType.equals("13")) {

                    postId = obj.getString("postId");
                    postTitle = obj.getString("postTitle");
                    postType = obj.getInt("postType");


                }
            } else if (messageType.equals("13")) {

                postId = obj.getString("postId");
                postTitle = obj.getString("postTitle");
                postType = obj.getInt("postType");


            }


            String secretId = "";
            long dTime = -1;
            if (obj.has("secretId")) {
                secretId = obj.getString("secretId");
                dTime = obj.getLong("dTime");
            }


            if (obj.has("isStar")) {
                isStar = obj.getBoolean("isStar");
            }

            String receiverName;
            String userImage;
            String toId = obj.getString("to");


            if (instance.getsignInType() == 0) {

                receiverName = obj.getString("name");
                userImage = obj.getString("userImage");
            } else {
                Map<String, Object> contactInfo;
                if ((messageType.equals("11") || messageType.equals("15")) && receiverUid.equals(instance.getUserId())) {
                    contactInfo = instance.getDbController().getFriendInfoFromUid(AppController.getInstance().getFriendsDocId(), toId);
                } else {
                    contactInfo = instance.getDbController().getFriendInfoFromUid(AppController.getInstance().getFriendsDocId(), receiverUid);
                }

                if (contactInfo != null) {
                    receiverName = contactInfo.get("firstName") + " " + contactInfo.get("lastName");
                    userImage = (String) contactInfo.get("profilePic");


                    /*
                     * For showing correct name in secret chat invitation message
                     */


                } else {


                    /*
                     * If userId doesn't exists in contact
                     */
                    //receiverName = receiverIdentifier;
                    //receiverName = obj.getString("userName");
                    receiverName = obj.getString("name");
                    userImage = obj.getString("userImage");

                }
                obj.put("contactName", receiverName);
            }


            // message type transfer for identify the own sent message
            boolean isSelf = false;
            String documentId;
            if (messageType.equals("15")/*for transfer*/ || messageType.equals("11")/*for post delete*/) {
                if (receiverUid.equals(instance.getUserId())) {
                    isSelf = true;
                    documentId = AppController.getInstance().findDocumentIdOfReceiver(toId, secretId);
                    receiverUid = toId;
                } else {
                    isSelf = false;
                    documentId = AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId);
                }
            } else {
                documentId = AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId);
            }
            if (documentId.isEmpty()) {


                /*
                 * Here, chatId is assumed to be empty
                 */
                documentId = instance.getDatabaseDocumentHelper().findDocumentIdOfReceiver(receiverUid, Utilities.tsInGmt(),
                        receiverName, userImage, secretId, true, receiverIdentifier, "", false, isStar);


            }
            ///////////////////////////////////////////////////////////

            /*
             * For message removal
             */


            instance.getDbController().setDocumentIdOfReceiver(documentId, docIdForDoubleTickAck, receiverUid);
            instance.getDbController().updateDestructTime(documentId, dTime);


            /*
             * For callback in to activity to update UI
             */

            String amount = "";
            String transferStatus = "";
            String transferStatusText = "";
            String transactionId;

            boolean alreadyExists;


            if (secretId.isEmpty()) {
                alreadyExists = instance.getDbController().checkAlreadyExists(documentId, id);
            } else {
                alreadyExists = instance.getDbController().checkAlreadyExistsSecretChat(documentId, id);
            }


            if (!alreadyExists || messageType.equals("15")) {
                String replyType = "";

                if (messageType.equals("1") || messageType.equals("2") || messageType.equals("7")) {


                    instance.getPeerToPeerMessageDatabaseHandler().putMessageInDb(receiverUid, messageType,
                            actualMessage, timestamp, id, documentId, obj.getString("thumbnail").trim(),
                            dataSize, dTime, receiverName, false, -1, false, -1, null, obj.has("wasEdited"), secretId);
                } else if (messageType.equals("9")) {
                    /*
                     * For document received
                     */

                    instance.getPeerToPeerMessageDatabaseHandler().putMessageInDb(receiverUid,
                            actualMessage, timestamp, id, documentId, dataSize,
                            dTime, false, -1, false, -1, mimeType, fileName, extension, secretId);


                } else if (messageType.equals("10")) {

                    /*
                     * For the reply message received
                     */

                    replyType = obj.getString("replyType");

                    String previousMessageType = obj.getString("previousType");

                    String previousFileType = "", thumbnail = "";


                    if (previousMessageType.equals("9")) {

                        previousFileType = obj.getString("previousFileType");

                    }
                    if (replyType.equals("1") || replyType.equals("2") || replyType.equals("7")) {


                        thumbnail = obj.getString("thumbnail").trim();

                    }


                    instance.getPeerToPeerMessageDatabaseHandler().putReplyMessageInDb(receiverUid,
                            actualMessage, timestamp, id, documentId,
                            dataSize, dTime, false, -1, false, -1, mimeType, fileName, extension, thumbnail, receiverName,
                            replyType, obj.getString("previousReceiverIdentifier"), obj.getString("previousFrom"),
                            obj.getString("previousPayload"), obj.getString("previousType"), obj.getString("previousId"), previousFileType, obj.has("wasEdited"), postId, postTitle, postType, secretId);


                } else if (messageType.equals("11")) {


                    instance.getDbController().updateChatListForRemovedOrEditedMessage(documentId, actualMessage, true, Utilities.epochtoGmt(obj.getString("removedAt")));
                    instance.getDbController().markMessageAsRemoved(documentId, id, removedMessageString, Utilities.epochtoGmt(obj.getString("removedAt")));
                    try {

                        obj.put("eventName", topic);
                        AppController.getBus().post(obj);


                    } catch (Exception e) {

                    }
                    return;
                } else if (messageType.equals("12")) {


                    instance.getDbController().updateChatListForRemovedOrEditedMessage(documentId, actualMessage, true, Utilities.epochtoGmt(obj.getString("editedAt")));
                    instance.getDbController().markMessageAsEdited(documentId, id, actualMessage);
                    try {

                        obj.put("eventName", topic);
                        AppController.getBus().post(obj);


                    } catch (Exception e) {

                    }
                    return;
                } else if (messageType.equals("13")) {

                    instance.getPeerToPeerMessageDatabaseHandler().putPostMessageInDb(receiverUid,
                            actualMessage, timestamp, id, documentId,
                            dTime, false, -1, false, -1, postId, postTitle, postType, secretId);


                } else if (messageType.equals("15")) {
                    transactionId = obj.getString("fromTxnId");
                    amount = obj.getString("amount");
                    transferStatus = obj.getString("transferStatus");
                    transferStatusText = obj.getString("transferStatusText");

                  instance.getPeerToPeerMessageDatabaseHandler().putTransferMessageInDb(receiverUid, messageType, actualMessage, timestamp, id, documentId, toId, amount, transferStatus, transferStatusText, dataSize, dTime, receiverName, isSelf, -1, false, -1, secretId,transactionId);


                } else if (messageType.equals("16")) {

                    String typeOfCall, callId;
                    int creation;

                    typeOfCall = obj.getString("typeOfCall");
                    callId = obj.getString("callId");
                    creation = obj.getInt("creation");

                    instance.getPeerToPeerMessageDatabaseHandler().putMissedCallMessageInDb(receiverUid, messageType, actualMessage, timestamp, id, documentId, toId, typeOfCall, callId, creation, dataSize, dTime, receiverName, isSelf, -1, false, -1, secretId);

                } else if (messageType.equals("17")) {

                    String typeOfCall, callId;
                    int creation;
                    long duration;
                    boolean isInComingCall;

                    typeOfCall = obj.getString("typeOfCall");
                    callId = obj.getString("callId");
                    creation = obj.getInt("creation");
                    duration = obj.getLong("duration");
                    isInComingCall = obj.getBoolean("isIncommingCall");

                    instance.getPeerToPeerMessageDatabaseHandler().putCallMessageInDb(receiverUid, messageType, actualMessage, timestamp, id, documentId, toId, typeOfCall, callId, creation, dataSize, dTime, receiverName, isSelf, -1, false, -1, secretId, duration, isInComingCall);

                } else {

                    instance.getPeerToPeerMessageDatabaseHandler().putMessageInDb(receiverUid,
                            messageType, actualMessage, timestamp, id, documentId, null, dataSize, dTime, receiverName, false, -1, false, -1, null, obj.has("wasEdited"), secretId);


                }


                //     if (!messageType.equals("11") && !messageType.equals("12") &&(!messageType.equals("0") || !actualMessage.isEmpty())) {

                if (!messageType.equals("0") || !actualMessage.isEmpty()) {

                    JSONObject obj2 = new JSONObject();
                    obj2.put("from", instance.getUserId());
                    obj2.put("msgIds", new JSONArray(Arrays.asList(new String[]{id})));
                    obj2.put("doc_id", docIdForDoubleTickAck);
                    obj2.put("to", receiverUid);

                    obj2.put("status", "2");


                    if (!secretId.isEmpty()) {
                        obj2.put("secretId", secretId);
                        obj2.put("dTime", dTime);

                    }
                    obj2.put("deliveryTime", Utilities.getGmtEpoch());

                    AppController.getInstance().publish(MqttEvents.Acknowledgement.value + "/" + receiverUid, obj2, 2, false);


                }

                /*
                 * For callback in to activity to update UI
                 */


                try {

                    obj.put("eventName", topic);
                    AppController.getBus().post(obj);


                } catch (Exception e) {

                }

//                        if (contactSynced)
                {
                    if (!instance.getDbController().checkIfReceiverChatMuted(instance.getMutedDocId(), receiverUid, secretId)) {
                        Intent intent;


//                        if (signInType != 0) {


//                            if (secretId.isEmpty()) {
//                                intent = new Intent(mInstance, com.example.moda.mqttchat.ContactSync.Activities.ChatMessagesScreen.class);
//                            } else {
//
//                                intent = new Intent(mInstance, com.example.moda.mqttchat.ContactSync.SecretChat.SecretChatMessageScreen.class);
//                                intent.putExtra("secretId", secretId);
//                            }


                        /*
                         * To allow retrieval
                         */


                        if (secretId.isEmpty()) {
                            intent = new Intent(instance, ChatMessageScreen.class);
                        } else {

                            intent = new Intent(instance, SecretChatMessageScreen.class);
                            intent.putExtra("secretId", secretId);
                        }


                        //  }

                        intent.putExtra("receiverUid", receiverUid);
                        intent.putExtra("receiverName", receiverName);

                        intent.putExtra("receiverIdentifier", receiverIdentifier);

                        intent.putExtra("documentId", documentId);


                        intent.putExtra("colorCode", AppController.getInstance().getColorCode(5));

                        intent.putExtra("receiverImage", userImage);


                        intent.putExtra("fromNotification", true);




                        /*
                         *To generate the push notification locally
                         */


                        if (!messageType.equals("16") && !messageType.equals("17")) {
                            instance.getNotificationGenerator().generatePushNotificationLocal(documentId, messageType, receiverName,
                                    actualMessage, intent, dTime, secretId, receiverUid, toId, replyType, amount, transferStatus, transferStatusText);
                        }

                    }
                }

                int type = Integer.parseInt(messageType);
                int replyTypeInt = -1;
                if (!replyType.isEmpty()) {
                    replyTypeInt = Integer.parseInt(replyType);
                }

                if (type == 1 || type == 2 || type == 5 || type == 7 || type == 9 ||
                        (type == 10 && (replyTypeInt == 1 || replyTypeInt == 2 || replyTypeInt == 5
                                || replyTypeInt == 7 || replyTypeInt == 9))) {
                    if (ActivityCompat.checkSelfPermission(instance, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
//                            if (autoDownloadAllowed) {

                        if (instance.checkWifiConnected()) {

                            Object[] params = new Object[8];
                            try {


                                params[0] = new String(Base64.decode(actualMessage, Base64.DEFAULT), "UTF-8");

                            } catch (UnsupportedEncodingException e) {

                            }

                            params[1] = messageType;
                            params[5] = id;
                            params[6] = documentId;
                            params[7] = receiverUid;
                            switch (type) {
                                case 1: {
                                    if (instance.getSharedPreferences().getBoolean("wifiPhoto", false)) {

                                        params[3] = instance.getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + timestamp + ".jpg";
                                        params[4] = instance.getExternalFilesDir(null) +
                                                ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".jpg";

                                        new DownloadMessage().execute(params);

                                    }
                                    break;

                                }
                                case 2: {
                                    if (instance.getSharedPreferences().getBoolean("wifiVideo", false)) {
                                        params[3] = instance.getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + timestamp + ".jpg";
                                        params[4] = instance.getExternalFilesDir(null) +
                                                ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".mp4";

                                        new DownloadMessage().execute(params);

                                    }
                                    break;
                                }
                                case 5: {
                                    if (instance.getSharedPreferences().getBoolean("wifiAudio", false)) {
                                        params[4] = instance.getExternalFilesDir(null) +
                                                ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".3gp";


                                        new DownloadMessage().execute(params);
                                    }
                                    break;

                                }
                                case 7: {
                                    if (instance.getSharedPreferences().getBoolean("wifiPhoto", false)) {
                                        params[3] = instance.getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + timestamp + ".jpg";
                                        params[4] = instance.getExternalFilesDir(null) +
                                                ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".jpg";

                                        new DownloadMessage().execute(params);

                                    }
                                    break;
                                }
                                case 9: {
                                    if (instance.getSharedPreferences().getBoolean("wifiDocument", false)) {
                                        params[4] = instance.getExternalFilesDir(null)
                                                + ApiOnServer.CHAT_DOWNLOADS_FOLDER + fileName;


                                        new DownloadMessage().execute(params);
                                    }
                                    break;
                                }
                                case 10: {


                                    params[2] = obj.getString("replyType");

                                    switch (Integer.parseInt((String) params[2])) {

                                        case 1: {
                                            if (instance.getSharedPreferences().getBoolean("wifiPhoto", false)) {

                                                params[3] = instance.getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + timestamp + ".jpg";
                                                params[4] = instance.getExternalFilesDir(null) +
                                                        ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".jpg";

                                                new DownloadMessage().execute(params);
                                            }
                                            break;

                                        }
                                        case 2: {
                                            if (instance.getSharedPreferences().getBoolean("wifiVideo", false)) {
                                                params[3] = instance.getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + timestamp + ".jpg";
                                                params[4] = instance.getExternalFilesDir(null) +
                                                        ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".mp4";

                                                new DownloadMessage().execute(params);
                                            }
                                            break;
                                        }
                                        case 5: {
                                            if (instance.getSharedPreferences().getBoolean("wifiAudio", false)) {
                                                params[4] = instance.getExternalFilesDir(null) +
                                                        ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".3gp";

                                                new DownloadMessage().execute(params);

                                            }
                                            break;
                                        }

                                        case 7: {
                                            if (instance.getSharedPreferences().getBoolean("wifiPhoto", false)) {

                                                params[3] = instance.getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + timestamp + ".jpg";
                                                params[4] = instance.getExternalFilesDir(null) +
                                                        ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".jpg";


                                                new DownloadMessage().execute(params);
                                            }
                                            break;
                                        }
                                        case 9: {
                                            if (instance.getSharedPreferences().getBoolean("wifiDocument", false)) {
                                                params[4] = instance.getExternalFilesDir(null)
                                                        + ApiOnServer.CHAT_DOWNLOADS_FOLDER + fileName;

                                                new DownloadMessage().execute(params);
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                }

                            }


                        } else if (instance.checkMobileDataOn()) {


                            Object[] params = new Object[8];
                            try {


                                params[0] = new String(Base64.decode(actualMessage, Base64.DEFAULT), "UTF-8");

                            } catch (UnsupportedEncodingException e) {

                            }

                            params[1] = messageType;
                            params[5] = id;
                            params[6] = documentId;
                            params[7] = receiverUid;
                            switch (type) {
                                case 1: {
                                    if (instance.getSharedPreferences().getBoolean("mobilePhoto", false)) {

                                        params[3] = instance.getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + timestamp + ".jpg";
                                        params[4] = instance.getExternalFilesDir(null) +
                                                ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".jpg";

                                        new DownloadMessage().execute(params);

                                    }
                                    break;

                                }
                                case 2: {
                                    if (instance.getSharedPreferences().getBoolean("mobileVideo", false)) {
                                        params[3] = instance.getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + timestamp + ".jpg";
                                        params[4] = instance.getExternalFilesDir(null) +
                                                ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".mp4";

                                        new DownloadMessage().execute(params);

                                    }
                                    break;
                                }
                                case 5: {
                                    if (instance.getSharedPreferences().getBoolean("mobileAudio", false)) {
                                        params[4] = instance.getExternalFilesDir(null) +
                                                ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".3gp";


                                        new DownloadMessage().execute(params);
                                    }
                                    break;

                                }
                                case 7: {
                                    if (instance.getSharedPreferences().getBoolean("mobilePhoto", false)) {
                                        params[3] = instance.getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + timestamp + ".jpg";
                                        params[4] = instance.getExternalFilesDir(null) +
                                                ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".jpg";

                                        new DownloadMessage().execute(params);

                                    }
                                    break;
                                }
                                case 9: {
                                    if (instance.getSharedPreferences().getBoolean("mobileDocument", false)) {
                                        params[4] = instance.getExternalFilesDir(null)
                                                + ApiOnServer.CHAT_DOWNLOADS_FOLDER + fileName;


                                        new DownloadMessage().execute(params);
                                    }
                                    break;
                                }
                                case 10: {


                                    params[2] = obj.getString("replyType");

                                    switch (Integer.parseInt((String) params[2])) {

                                        case 1: {
                                            if (instance.getSharedPreferences().getBoolean("mobilePhoto", false)) {

                                                params[3] = instance.getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + timestamp + ".jpg";
                                                params[4] = instance.getExternalFilesDir(null) +
                                                        ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".jpg";

                                                new DownloadMessage().execute(params);
                                            }
                                            break;

                                        }
                                        case 2: {
                                            if (instance.getSharedPreferences().getBoolean("mobileVideo", false)) {
                                                params[3] = instance.getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + timestamp + ".jpg";
                                                params[4] = instance.getExternalFilesDir(null) +
                                                        ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".mp4";

                                                new DownloadMessage().execute(params);
                                            }
                                            break;
                                        }
                                        case 5: {
                                            if (instance.getSharedPreferences().getBoolean("mobileAudio", false)) {
                                                params[4] = instance.getExternalFilesDir(null) +
                                                        ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".3gp";

                                                new DownloadMessage().execute(params);

                                            }
                                            break;
                                        }

                                        case 7: {
                                            if (instance.getSharedPreferences().getBoolean("mobilePhoto", false)) {

                                                params[3] = instance.getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + timestamp + ".jpg";
                                                params[4] = instance.getExternalFilesDir(null) +
                                                        ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + id + ".jpg";


                                                new DownloadMessage().execute(params);
                                            }
                                            break;
                                        }
                                        case 9: {
                                            if (instance.getSharedPreferences().getBoolean("mobileDocument", false)) {
                                                params[4] = instance.getExternalFilesDir(null)
                                                        + ApiOnServer.CHAT_DOWNLOADS_FOLDER + fileName;

                                                new DownloadMessage().execute(params);
                                            }
                                            break;
                                        }
                                    }
                                    break;
                                }

                            }


                        }


                    }
                }
            } else {


                if (messageType.equals("11")) {
                    instance.getDbController().updateChatListForRemovedOrEditedMessage(documentId, actualMessage, true, Utilities.epochtoGmt(obj.getString("removedAt")));
                    instance.getDbController().markMessageAsRemoved(documentId, id, removedMessageString, Utilities.epochtoGmt(obj.getString("removedAt")));
                    try {

                        obj.put("eventName", topic);
                        AppController.getBus().post(obj);


                    } catch (Exception e) {

                    }
                    return;
                } else if (messageType.equals("12")) {


                    instance.getDbController().updateChatListForRemovedOrEditedMessage(documentId, actualMessage, true, Utilities.epochtoGmt(obj.getString("editedAt")));
                    instance.getDbController().markMessageAsEdited(documentId, id, actualMessage);
                    try {

                        obj.put("eventName", topic);
                        AppController.getBus().post(obj);


                    } catch (Exception e) {

                    }
                    return;
                }

            }
            if (!secretId.isEmpty()) {
                if (!actualMessage.trim().isEmpty() || dTime != -1) {
                    instance.getDbController().updateSecretInviteImageVisibility(documentId, false);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
