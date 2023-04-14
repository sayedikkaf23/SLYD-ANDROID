package chat.hola.com.app.MessagesHandler.FetchMessages;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Base64;

import androidx.core.app.ActivityCompat;

import com.ezcall.android.R;
import com.squareup.otto.Bus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import chat.hola.com.app.AppController;

import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.MessagesHandler.GroupChatMessages.GroupChatDatabaseHelper;
import chat.hola.com.app.MessagesHandler.PeerToPeerMessage.PeerToPeerMessageDatabaseHandler;
import chat.hola.com.app.MessagesHandler.Utilities.DownloadMessage;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.Utilities;

import static chat.hola.com.app.AppController.findDocumentIdOfReceiver;

public class FetchMessageHandler {

    private AppController instance = AppController.getInstance();


    public void handleFetchMessageResult(JSONObject obj) {


        /*
         * To receive the result of the fetch messages api
         */

        String topic = null;
        GroupChatDatabaseHelper gcDatabaseHelper = instance.getGroupChatDatabaseHelper();
        PeerToPeerMessageDatabaseHandler messageDatabaseHandler = instance.getPeerToPeerMessageDatabaseHandler();
        String chatDocId = instance.getChatDocId();
        int signInType = instance.getsignInType();
        Bus bus = AppController.getBus();
        CouchDbController db = instance.getDbController();
        String userId = instance.getUserId();


        /*
         * First add the message received in the local db and then post on the bus
         */
        try {

            topic = obj.getString("topic");

            JSONArray messages = obj.getJSONArray("messages");

            if (messages.length() > 0) {


                /*
                 *Group chat
                 */
                if (obj.getString("opponentUid").isEmpty()) {

                    JSONObject messageObject;

                    String receiverUid = obj.getString("chatId");

                    boolean isSelf;
                    String actualMessage, timestamp, id, mimeType, fileName, extension;
                    int dataSize;

                    String postId = "", postTitle = "";
                    int postType = -1;

                    for (int i = 0; i < messages.length(); i++) {

                        messageObject = messages.getJSONObject(i);

                        isSelf = messageObject.getString("senderId").equals(userId);


                        /*
                         * For an actual message(Like text,image,video etc.) received
                         */

                        String messageType = messageObject.getString("messageType");

                        if (messageObject.has("payload")) {

                            actualMessage = messageObject.getString("payload").trim();
                            timestamp = String.valueOf(messageObject.getLong("timestamp"));

                            id = messageObject.getString("messageId");
                            mimeType = "";
                            fileName = "";
                            extension = "";

                            dataSize = -1;

                            if (messageType.equals("1") || messageType.equals("2") || messageType.equals(
                                    "5") || messageType.equals("7") || messageType.equals("9")) {
                                dataSize = messageObject.getInt("dataSize");

                                if (messageType.equals("9")) {

                                    mimeType = messageObject.getString("mimeType");
                                    fileName = messageObject.getString("fileName");
                                    extension = messageObject.getString("extension");
                                }
                            } else if (messageType.equals("10")) {

                                String replyType = messageObject.getString("replyType");

                                if (replyType.equals("1")
                                        || replyType.equals("2")
                                        || replyType.equals("5")
                                        || replyType.equals("7")
                                        || replyType.equals("9")) {
                                    dataSize = messageObject.getInt("dataSize");

                                    if (replyType.equals("9")) {

                                        mimeType = messageObject.getString("mimeType");
                                        fileName = messageObject.getString("fileName");
                                        extension = messageObject.getString("extension");
                                    } else if (replyType.equals("13")) {

                                        postId = messageObject.getString("postId");
                                        postTitle = messageObject.getString("postTitle");
                                        postType = messageObject.getInt("postType");
                                    }
                                }
                            } else if (messageType.equals("13")) {

                                postId = messageObject.getString("postId");
                                postTitle = messageObject.getString("postTitle");
                                postType = messageObject.getInt("postType");
                            }

                            String documentId =
                                    AppController.getInstance().findDocumentIdOfReceiver(receiverUid, "");
                            if (documentId.isEmpty()) {

                                String receiverName = messageObject.getString("name");
                                String userImage = messageObject.getString("userImage");

                                /*
                                 * Here, chatId is assumed to be empty
                                 */
                                documentId =
                                        findDocumentIdOfReceiver(receiverUid, Utilities.tsInGmt(), receiverName,
                                                userImage, "", true, receiverUid, obj.getString("chatId"), true,
                                                false);
                            }

                            if (!db.checkAlreadyExists(documentId, id)) {

                                if (messageType.equals("11")) {
                                    gcDatabaseHelper
                                            .putGroupMessageInDb(messageObject.getString("senderId"), messageType,
                                                    actualMessage, timestamp, id, documentId, null, dataSize,
                                                    messageObject.getString("receiverIdentifier"), isSelf, 1,
                                                    Utilities.epochtoGmt(messageObject.getString("removedAt")),
                                                    false);
                                } else {
                                    if (messageType.equals("1")
                                            || messageType.equals("2")
                                            || messageType.equals("7")) {

                                        gcDatabaseHelper
                                                .putGroupMessageInDb(messageObject.getString("senderId"),
                                                        messageType, actualMessage, timestamp, id, documentId,
                                                        messageObject.getString("thumbnail").trim(), dataSize,
                                                        messageObject.getString("receiverIdentifier"), isSelf, 1, null,
                                                        false);
                                    } else if (messageType.equals("9")) {

                                        gcDatabaseHelper
                                                .putGroupMessageInDb(messageObject.getString("senderId"),
                                                        actualMessage, timestamp, id, documentId, dataSize, isSelf, 1,
                                                        mimeType, fileName, extension,
                                                        messageObject.getString("receiverIdentifier"));
                                    } else if (messageType.equals("13")) {

                                        gcDatabaseHelper
                                                .putGroupPostMessageInDb(messageObject.getString("senderId"),
                                                        actualMessage, timestamp, id, documentId, isSelf, 1, mimeType,
                                                        fileName, extension,
                                                        messageObject.getString("receiverIdentifier"), postId,
                                                        postTitle, postType);
                                    } else if (messageType.equals("10")) {

                                        /*
                                         * For the reply message received
                                         */

                                        String replyType = messageObject.getString("replyType");

                                        String previousMessageType = messageObject.getString("previousType");

                                        String previousFileType = "", thumbnail = "";

                                        if (previousMessageType.equals("9")) {

                                            previousFileType = messageObject.getString("previousFileType");
                                        }
                                        if (replyType.equals("1") || replyType.equals("2") || replyType.equals(
                                                "7")) {

                                            thumbnail = messageObject.getString("thumbnail").trim();
                                        }

                                        gcDatabaseHelper
                                                .putGroupReplyMessageInDb(messageObject.getString("senderId"),
                                                        actualMessage, timestamp, id, documentId, dataSize, isSelf, 1,
                                                        mimeType, fileName, extension, thumbnail,
                                                        messageObject.getString("receiverIdentifier"), replyType,
                                                        messageObject.getString("previousReceiverIdentifier"),
                                                        messageObject.getString("previousFrom"),
                                                        messageObject.getString("previousPayload"),
                                                        messageObject.getString("previousType"),
                                                        messageObject.getString("previousId"), previousFileType,
                                                        messageObject.has("wasEdited"), postId, postTitle, postType);
                                    } else {

                                        gcDatabaseHelper
                                                .putGroupMessageInDb(messageObject.getString("senderId"),
                                                        messageType, actualMessage, timestamp, id, documentId, null,
                                                        dataSize, messageObject.getString("receiverIdentifier"), isSelf,
                                                        1, null, messageObject.has("wasEdited"));
                                    }
                                    int type = Integer.parseInt(messageType);

                                    int replyTypeInt = -1;

                                    if (obj.has("replyType")) {
                                        replyTypeInt = Integer.parseInt(obj.getString("replyType"));
                                    }
                                    if (type == 1 || type == 2 || type == 5 || type == 7 || type == 9 || (type
                                            == 10 && (replyTypeInt == 1
                                            || replyTypeInt == 2
                                            || replyTypeInt == 5
                                            || replyTypeInt == 7
                                            || replyTypeInt == 9))) {
                                        if (ActivityCompat.checkSelfPermission(instance,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                                == PackageManager.PERMISSION_GRANTED) {
                                            //                                                if (autoDownloadAllowed) {

                                            if (instance.checkWifiConnected()) {

                                                Object[] params = new Object[8];
                                                try {

                                                    params[0] =
                                                            new String(Base64.decode(actualMessage, Base64.DEFAULT),
                                                                    "UTF-8");
                                                } catch (UnsupportedEncodingException e) {

                                                }

                                                params[1] = messageType;
                                                params[5] = id;
                                                params[6] = documentId;
                                                params[7] = receiverUid;
                                                switch (type) {
                                                    case 1: {

                                                        if (instance.getSharedPreferences().getBoolean("wifiPhoto", false)) {
                                                            params[3] = instance.getFilesDir()
                                                                    + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                    + "/"
                                                                    + timestamp
                                                                    + ".jpg";
                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".jpg";

                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 2: {
                                                        if (instance.getSharedPreferences().getBoolean("wifiVideo", false)) {
                                                            params[3] = instance.getFilesDir()
                                                                    + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                    + "/"
                                                                    + timestamp
                                                                    + ".jpg";
                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".mp4";
                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 5: {

                                                        if (instance.getSharedPreferences().getBoolean("wifiAudio", false)) {

                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".3gp";
                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 7: {

                                                        if (instance.getSharedPreferences().getBoolean("wifiPhoto", false)) {

                                                            params[3] = instance.getFilesDir()
                                                                    + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                    + "/"
                                                                    + timestamp
                                                                    + ".jpg";
                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".jpg";

                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }

                                                    case 9: {
                                                        if (instance.getSharedPreferences().getBoolean("wifiDocument", false)) {

                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + fileName;

                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 10: {

                                                        params[2] = obj.getString("replyType");

                                                        switch (Integer.parseInt((String) params[2])) {

                                                            case 1: {
                                                                if (instance.getSharedPreferences().getBoolean("wifiPhoto", false)) {
                                                                    params[3] = instance.getFilesDir()
                                                                            + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                            + "/"
                                                                            + timestamp
                                                                            + ".jpg";
                                                                    params[4] = instance.getExternalFilesDir(null)
                                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                            + receiverUid
                                                                            + id
                                                                            + ".jpg";

                                                                    new DownloadMessage().execute(params);
                                                                }
                                                                break;
                                                            }

                                                            case 2: {
                                                                if (instance.getSharedPreferences().getBoolean("wifiVideo", false)) {

                                                                    params[3] = instance.getFilesDir()
                                                                            + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                            + "/"
                                                                            + timestamp
                                                                            + ".jpg";
                                                                    params[4] = instance.getExternalFilesDir(null)
                                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                            + receiverUid
                                                                            + id
                                                                            + ".mp4";
                                                                    new DownloadMessage().execute(params);
                                                                }
                                                                break;
                                                            }
                                                            case 5: {
                                                                if (instance.getSharedPreferences().getBoolean("wifiAudio", false)) {

                                                                    params[4] = instance.getExternalFilesDir(null)
                                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                            + receiverUid
                                                                            + id
                                                                            + ".3gp";
                                                                    new DownloadMessage().execute(params);
                                                                }

                                                                break;
                                                            }

                                                            case 7: {
                                                                if (instance.getSharedPreferences().getBoolean("wifiPhoto", false)) {

                                                                    params[3] = instance.getFilesDir()
                                                                            + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                            + "/"
                                                                            + timestamp
                                                                            + ".jpg";
                                                                    params[4] = instance.getExternalFilesDir(null)
                                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                            + receiverUid
                                                                            + id
                                                                            + ".jpg";
                                                                    new DownloadMessage().execute(params);
                                                                }
                                                                break;
                                                            }
                                                            case 9:

                                                                if (instance.getSharedPreferences().getBoolean("wifiDocument", false)) {
                                                                    params[4] = instance.getExternalFilesDir(null)
                                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                            + fileName;

                                                                    new DownloadMessage().execute(params);
                                                                }
                                                                break;
                                                        }
                                                        break;
                                                    }
                                                }
                                            } else if (instance.checkMobileDataOn()) {

                                                Object[] params = new Object[8];
                                                try {

                                                    params[0] =
                                                            new String(Base64.decode(actualMessage, Base64.DEFAULT),
                                                                    "UTF-8");
                                                } catch (UnsupportedEncodingException e) {

                                                }

                                                params[1] = messageType;
                                                params[5] = id;
                                                params[6] = documentId;
                                                params[7] = receiverUid;
                                                switch (type) {
                                                    case 1: {

                                                        if (instance.getSharedPreferences().getBoolean("mobilePhoto", false)) {
                                                            params[3] = instance.getFilesDir()
                                                                    + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                    + "/"
                                                                    + timestamp
                                                                    + ".jpg";
                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".jpg";

                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 2: {
                                                        if (instance.getSharedPreferences().getBoolean("mobileVideo", false)) {
                                                            params[3] = instance.getFilesDir()
                                                                    + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                    + "/"
                                                                    + timestamp
                                                                    + ".jpg";
                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".mp4";
                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 5: {

                                                        if (instance.getSharedPreferences().getBoolean("mobileAudio", false)) {

                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".3gp";
                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 7: {

                                                        if (instance.getSharedPreferences().getBoolean("mobilePhoto", false)) {

                                                            params[3] = instance.getFilesDir()
                                                                    + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                    + "/"
                                                                    + timestamp
                                                                    + ".jpg";
                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".jpg";

                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }

                                                    case 9: {
                                                        if (instance.getSharedPreferences().getBoolean("mobileDocument", false)) {

                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + fileName;

                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 10: {

                                                        params[2] = obj.getString("replyType");

                                                        switch (Integer.parseInt((String) params[2])) {

                                                            case 1: {
                                                                if (instance.getSharedPreferences().getBoolean("mobilePhoto", false)) {
                                                                    params[3] = instance.getFilesDir()
                                                                            + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                            + "/"
                                                                            + timestamp
                                                                            + ".jpg";
                                                                    params[4] = instance.getExternalFilesDir(null)
                                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                            + receiverUid
                                                                            + id
                                                                            + ".jpg";

                                                                    new DownloadMessage().execute(params);
                                                                }
                                                                break;
                                                            }

                                                            case 2: {
                                                                if (instance.getSharedPreferences().getBoolean("mobileVideo", false)) {

                                                                    params[3] = instance.getFilesDir()
                                                                            + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                            + "/"
                                                                            + timestamp
                                                                            + ".jpg";
                                                                    params[4] = instance.getExternalFilesDir(null)
                                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                            + receiverUid
                                                                            + id
                                                                            + ".mp4";
                                                                    new DownloadMessage().execute(params);
                                                                }
                                                                break;
                                                            }
                                                            case 5: {
                                                                if (instance.getSharedPreferences().getBoolean("mobileAudio", false)) {

                                                                    params[4] = instance.getExternalFilesDir(null)
                                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                            + receiverUid
                                                                            + id
                                                                            + ".3gp";
                                                                    new DownloadMessage().execute(params);
                                                                }

                                                                break;
                                                            }

                                                            case 7: {
                                                                if (instance.getSharedPreferences().getBoolean("mobilePhoto", false)) {

                                                                    params[3] = instance.getFilesDir()
                                                                            + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                            + "/"
                                                                            + timestamp
                                                                            + ".jpg";
                                                                    params[4] = instance.getExternalFilesDir(null)
                                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                            + receiverUid
                                                                            + id
                                                                            + ".jpg";
                                                                    new DownloadMessage().execute(params);
                                                                }
                                                                break;
                                                            }
                                                            case 9:

                                                                if (instance.getSharedPreferences().getBoolean("mobileDocument", false)) {
                                                                    params[4] = instance.getExternalFilesDir(null)
                                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                            + fileName;

                                                                    new DownloadMessage().execute(params);
                                                                }
                                                                break;
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {

                                if (messageType.equals("11")) {

                                    gcDatabaseHelper
                                            .putGroupMessageInDb(messageObject.getString("senderId"), messageType,
                                                    actualMessage, timestamp, id, documentId, null, dataSize,
                                                    messageObject.getString("receiverIdentifier"), isSelf, 1,
                                                    Utilities.epochtoGmt(messageObject.getString("removedAt")),
                                                    false);
                                }
                            }
                        } else {

                            String tsInGmt =
                                    Utilities.epochtoGmt(String.valueOf(messageObject.getLong("timestamp")));

                            switch (messageObject.getInt("messageType")) {
                                case 0: {
                                    /*
                                     * Group created
                                     *
                                     */

                                    String groupId = obj.getString("chatId");

                                    String docId =
                                            AppController.getInstance().findDocumentIdOfReceiver(groupId, "");

                                    Map<String, Object> map = new HashMap<>();

                                    map.put("messageType", "98");

                                    map.put("isSelf", false);
                                    map.put("from", groupId);
                                    map.put("Ts", tsInGmt);
                                    map.put("id", messageObject.getString("messageId"));

                                    map.put("type", 0);

                                    map.put("groupName", messageObject.getString("groupSubject"));

                                    map.put("initiatorId", messageObject.getString("initiatorId"));
                                    map.put("initiatorIdentifier",
                                            messageObject.getString("initiatorIdentifier"));

                                    map.put("deliveryStatus", "0");

                                    String text, initiatorName;
                                    String initiatorId = messageObject.getString("initiatorId");
                                    if (initiatorId.equals(AppController.getInstance().getUserId())) {

                                        initiatorName = instance.getString(R.string.You);
                                    } else {
                                        initiatorName =
                                                db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                        initiatorId);
                                        if (initiatorName == null) {

                                            initiatorName = messageObject.getString("initiatorIdentifier");
                                        }
                                    }

                                    text = instance.getString(R.string.CreatedGroup, initiatorName)
                                            + " "
                                            + messageObject.getString("groupSubject");

                                    gcDatabaseHelper.addGroupChatMessageInDB(docId, map, tsInGmt, text, false);

                                    break;
                                }

                                case 1: {

                                    /*
                                     * Member added
                                     */

                                    String groupId = obj.getString("chatId");

                                    /*
                                     *
                                     * To check if group already exists
                                     */
                                    String docId =
                                            AppController.getInstance().findDocumentIdOfReceiver(groupId, "");

                                    String memberId = messageObject.getString("memberId");

                                    if (!docId.isEmpty()) {

                                        Map<String, Object> map = new HashMap<>();

                                        map.put("messageType", "98");

                                        map.put("isSelf", false);
                                        map.put("from", groupId);
                                        map.put("Ts", tsInGmt);
                                        map.put("id", messageObject.getString("messageId"));

                                        map.put("type", 1);

                                        map.put("memberId", memberId);

                                        map.put("memberIdentifier",
                                                messageObject.getString("memberIdentifier"));

                                        map.put("initiatorId", messageObject.getString("initiatorId"));
                                        map.put("initiatorIdentifier",
                                                messageObject.getString("initiatorIdentifier"));

                                        map.put("deliveryStatus", "0");

                                        String text, initiatorName, memberName;
                                        String initiatorId = messageObject.getString("initiatorId");
                                        if (initiatorId.equals(AppController.getInstance().getUserId())) {

                                            initiatorName = instance.getString(R.string.You);
                                        } else {
                                            initiatorName =
                                                    db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                            initiatorId);
                                            if (initiatorName == null) {

                                                initiatorName = messageObject.getString("initiatorIdentifier");
                                            }
                                        }

                                        if (memberId.equals(AppController.getInstance().getUserId())) {

                                            memberName = instance.getString(R.string.YouSmall);
                                        } else {
                                            memberName =
                                                    db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                            memberId);
                                            if (memberName == null) {

                                                memberName = messageObject.getString("memberIdentifier");
                                            }
                                        }

                                        text = initiatorName
                                                + " "
                                                + instance.getString(R.string.AddedMember, memberName)
                                                + " "
                                                + instance.getString(R.string.ToGroup);

                                        gcDatabaseHelper.addGroupChatMessageInDB(docId, map, tsInGmt, text, false);
                                    }

                                    break;
                                }
                                case 2: {
                                    /*
                                     * Member removed
                                     *
                                     */

                                    String groupId = obj.getString("chatId");

                                    String docId =
                                            AppController.getInstance().findDocumentIdOfReceiver(groupId, "");

                                    String memberId = messageObject.getString("memberId");
                                    if (!docId.isEmpty()) {

                                        Map<String, Object> map = new HashMap<>();

                                        map.put("messageType", "98");

                                        map.put("isSelf", false);
                                        map.put("from", groupId);
                                        map.put("Ts", tsInGmt);
                                        map.put("id", messageObject.getString("messageId"));

                                        map.put("type", 2);

                                        map.put("memberId", memberId);

                                        map.put("memberIdentifier",
                                                messageObject.getString("memberIdentifier"));

                                        map.put("initiatorId", messageObject.getString("initiatorId"));
                                        map.put("initiatorIdentifier",
                                                messageObject.getString("initiatorIdentifier"));

                                        map.put("deliveryStatus", "0");

                                        String text, initiatorName, memberName;
                                        String initiatorId = messageObject.getString("initiatorId");
                                        if (initiatorId.equals(AppController.getInstance().getUserId())) {

                                            initiatorName = instance.getString(R.string.You);
                                        } else {
                                            initiatorName =
                                                    db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                            initiatorId);
                                            if (initiatorName == null) {

                                                initiatorName = messageObject.getString("initiatorIdentifier");
                                            }
                                        }

                                        if (memberId.equals(AppController.getInstance().getUserId())) {

                                            memberName = instance.getString(R.string.YouSmall);
                                        } else {
                                            memberName =
                                                    db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                            memberId);
                                            if (memberName == null) {

                                                memberName = messageObject.getString("memberIdentifier");
                                            }
                                        }

                                        text = initiatorName
                                                + " "
                                                + instance.getString(R.string.Removed)
                                                + " "
                                                + memberName;

                                        gcDatabaseHelper.addGroupChatMessageInDB(docId, map, tsInGmt, text, false);
                                    }

                                    break;
                                }
                                case 3: {
                                    /*
                                     * Made admin
                                     *
                                     */

                                    String groupId = obj.getString("chatId");

                                    String docId =
                                            AppController.getInstance().findDocumentIdOfReceiver(groupId, "");

                                    String memberId = messageObject.getString("memberId");
                                    if (!docId.isEmpty()) {

                                        Map<String, Object> map = new HashMap<>();

                                        map.put("messageType", "98");

                                        map.put("isSelf", false);
                                        map.put("from", groupId);
                                        map.put("Ts", tsInGmt);
                                        map.put("id", messageObject.getString("messageId"));

                                        map.put("type", 3);

                                        map.put("memberId", memberId);

                                        map.put("memberIdentifier",
                                                messageObject.getString("memberIdentifier"));

                                        map.put("initiatorId", messageObject.getString("initiatorId"));
                                        map.put("initiatorIdentifier",
                                                messageObject.getString("initiatorIdentifier"));

                                        map.put("deliveryStatus", "0");

                                        String text, initiatorName, memberName;
                                        String initiatorId = messageObject.getString("initiatorId");
                                        if (initiatorId.equals(AppController.getInstance().getUserId())) {

                                            initiatorName = instance.getString(R.string.You);
                                        } else {
                                            initiatorName =
                                                    db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                            initiatorId);
                                            if (initiatorName == null) {

                                                initiatorName = messageObject.getString("initiatorIdentifier");
                                            }
                                        }

                                        if (memberId.equals(AppController.getInstance().getUserId())) {

                                            memberName = instance.getString(R.string.YouSmall);
                                        } else {
                                            memberName =
                                                    db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                            memberId);
                                            if (memberName == null) {

                                                memberName = messageObject.getString("memberIdentifier");
                                            }
                                        }

                                        text = initiatorName
                                                + " "
                                                + instance.getString(R.string.Made)
                                                + " "
                                                + memberName
                                                + " "
                                                + instance.getString(R.string.MakeAdmin);

                                        gcDatabaseHelper.addGroupChatMessageInDB(docId, map, tsInGmt, text, false);
                                    }
                                    break;
                                }
                                case 4: {
                                    /*
                                     * Group name updated
                                     *
                                     */
                                    String groupId = obj.getString("chatId");

                                    String docId =
                                            AppController.getInstance().findDocumentIdOfReceiver(groupId, "");

                                    if (!docId.isEmpty()) {

                                        Map<String, Object> map = new HashMap<>();

                                        map.put("messageType", "98");

                                        map.put("isSelf", false);
                                        map.put("from", groupId);
                                        map.put("Ts", tsInGmt);
                                        map.put("id", messageObject.getString("messageId"));

                                        map.put("type", 4);

                                        map.put("groupSubject", messageObject.getString("groupSubject"));
                                        map.put("initiatorId", messageObject.getString("initiatorId"));
                                        map.put("initiatorIdentifier",
                                                messageObject.getString("initiatorIdentifier"));

                                        map.put("deliveryStatus", "0");

                                        String text, initiatorName;
                                        String initiatorId = messageObject.getString("initiatorId");
                                        if (initiatorId.equals(AppController.getInstance().getUserId())) {

                                            initiatorName = instance.getString(R.string.You);
                                        } else {
                                            initiatorName =
                                                    db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                            initiatorId);
                                            if (initiatorName == null) {

                                                initiatorName = messageObject.getString("initiatorIdentifier");
                                            }
                                        }

                                        text = initiatorName + " " + instance.getString(R.string.UpdatedGroupSubject,
                                                messageObject.getString("groupSubject"));

                                        gcDatabaseHelper.addGroupChatMessageInDB(docId, map, tsInGmt, text, false);
                                    }

                                    break;
                                }
                                case 5: {
                                    /*
                                     * Group icon updated
                                     *
                                     */

                                    String groupId = obj.getString("chatId");

                                    String docId =
                                            AppController.getInstance().findDocumentIdOfReceiver(groupId, "");

                                    if (!docId.isEmpty()) {

                                        Map<String, Object> map = new HashMap<>();

                                        map.put("messageType", "98");

                                        map.put("isSelf", false);
                                        map.put("from", groupId);
                                        map.put("Ts", tsInGmt);
                                        map.put("id", messageObject.getString("messageId"));

                                        map.put("type", 5);

                                        map.put("initiatorId", messageObject.getString("initiatorId"));
                                        map.put("initiatorIdentifier",
                                                messageObject.getString("initiatorIdentifier"));

                                        map.put("deliveryStatus", "0");

                                        String text, initiatorName;
                                        String initiatorId = messageObject.getString("initiatorId");
                                        if (initiatorId.equals(AppController.getInstance().getUserId())) {

                                            initiatorName = instance.getString(R.string.You);
                                        } else {
                                            initiatorName =
                                                    db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                            initiatorId);
                                            if (initiatorName == null) {

                                                initiatorName = messageObject.getString("initiatorIdentifier");
                                            }
                                        }

                                        text = initiatorName + " " + instance.getString(R.string.UpdatedGroupIcon);

                                        gcDatabaseHelper.addGroupChatMessageInDB(docId, map, tsInGmt, text, false);
                                    }

                                    break;
                                }

                                case 6: {

                                    /*
                                     * Member left the conversation
                                     */

                                    String groupId = obj.getString("chatId");

                                    String memberId = messageObject.getString("initiatorId");

                                    String docId =
                                            AppController.getInstance().findDocumentIdOfReceiver(groupId, "");

                                    if (!docId.isEmpty()) {

                                        Map<String, Object> map = new HashMap<>();

                                        map.put("messageType", "98");

                                        map.put("isSelf", false);
                                        map.put("from", groupId);
                                        map.put("Ts", tsInGmt);
                                        map.put("id", messageObject.getString("messageId"));

                                        map.put("type", 6);

                                        map.put("initiatorId", memberId);
                                        map.put("initiatorIdentifier",
                                                messageObject.getString("initiatorIdentifier"));

                                        map.put("deliveryStatus", "0");

                                        String text, memberName;

                                        if (memberId.equals(AppController.getInstance().getUserId())) {

                                            memberName = instance.getString(R.string.You);
                                        } else {
                                            memberName =
                                                    db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                            memberId);
                                            if (memberName == null) {

                                                memberName = messageObject.getString("initiatorIdentifier");
                                            }
                                        }

                                        text = instance.getString(R.string.LeftGroup, memberName);

                                        gcDatabaseHelper.addGroupChatMessageInDB(docId, map, tsInGmt, text, false);
                                    }

                                    break;
                                }
                            }
                        }
                    }
                } else {


                    /*
                     * Messages in the normal or the secret chat
                     */

                    JSONObject messageObject;

                    String receiverUid = obj.getString("opponentUid");

                    String secretId = "";

                    boolean isStar;

                    long dTime, expectedDTime = -1;
                    if (obj.has("secretId") && !obj.getString("secretId").isEmpty()) {
                        secretId = obj.getString("secretId");
                    }

                    String receiverIdentifier = "";
                    Map<String, Object> chatDetails = AppController.getInstance().getDbController().
                            getAllChatDetails(chatDocId);

                    if (chatDetails != null) {

                        ArrayList<String> receiverUidArray =
                                (ArrayList<String>) chatDetails.get("receiverUidArray");

                        ArrayList<String> receiverDocIdArray =
                                (ArrayList<String>) chatDetails.get("receiverDocIdArray");

                        ArrayList<String> secretIdArray =
                                (ArrayList<String>) chatDetails.get("secretIdArray");

                        for (int i = 0; i < receiverUidArray.size(); i++) {

                            if (receiverUidArray.get(i).equals(receiverUid) && secretIdArray.get(i)
                                    .equals(secretId)) {

                                receiverIdentifier =
                                        db.fetchReceiverIdentifierFromChatId(receiverDocIdArray.get(i),
                                                obj.getString("chatId"));
                                break;
                            }
                        }
                    }

                    if (receiverIdentifier.isEmpty()) {
                        return;
                    }

                    boolean isSelf;
                    String messageType, actualMessage, timestamp, id, mimeType, fileName, extension,
                            docIdForDoubleTickAck;
                    int dataSize;

                    String postId = "", postTitle = "";
                    int postType = -1;

                    for (int i = 0; i < messages.length(); i++) {

                        messageObject = messages.getJSONObject(i);

                        if (!secretId.isEmpty()) {
                            dTime = messageObject.getLong("dTime");
                        } else {
                            dTime = -1;
                        }

                        isSelf = messageObject.getString("senderId").equals(userId);


                        /*
                         * For an actual message(Like text,image,video etc.) received
                         */

                        messageType = messageObject.getString("messageType");
                        actualMessage = messageObject.getString("payload").trim();
                        timestamp = String.valueOf(messageObject.getLong("timestamp"));

                        id = messageObject.getString("messageId");
                        mimeType = "";
                        fileName = "";
                        extension = "";
                        docIdForDoubleTickAck = messageObject.getString("toDocId");
                        dataSize = -1;

                        if (messageType.equals("1") || messageType.equals("2") || messageType.equals(
                                "5") || messageType.equals("7") || messageType.equals("9")) {
                            dataSize = messageObject.getInt("dataSize");

                            if (messageType.equals("9")) {

                                mimeType = messageObject.getString("mimeType");
                                fileName = messageObject.getString("fileName");
                                extension = messageObject.getString("extension");
                            }
                        } else if (messageType.equals("10")) {

                            String replyType = messageObject.getString("replyType");

                            if (replyType.equals("1")
                                    || replyType.equals("2")
                                    || replyType.equals("5")
                                    || replyType.equals("7")
                                    || replyType.equals("9")) {
                                dataSize = messageObject.getInt("dataSize");

                                if (replyType.equals("9")) {

                                    mimeType = messageObject.getString("mimeType");
                                    fileName = messageObject.getString("fileName");
                                    extension = messageObject.getString("extension");
                                } else if (replyType.equals("13")) {

                                    postId = messageObject.getString("postId");
                                    postTitle = messageObject.getString("postTitle");
                                    postType = messageObject.getInt("postType");
                                }
                            }
                        } else if (messageType.equals("13")) {

                            postId = messageObject.getString("postId");
                            postTitle = messageObject.getString("postTitle");
                            postType = messageObject.getInt("postType");
                        }

                        String receiverName;
                        String userImage;

                        if (signInType == 0) {

                            receiverName = messageObject.getString("name");
                            userImage = messageObject.getString("userImage");
                        } else {

                            Map<String, Object> contactInfo

                                    = db.getFriendInfoFromUid(AppController.getInstance().getFriendsDocId(),
                                    receiverUid);

                            if (contactInfo != null) {

                                receiverName =
                                        contactInfo.get("firstName") + " " + contactInfo.get("lastName");

                                userImage = (String) contactInfo.get("profilePic");
                            } else {


                                /*
                                 * If userId doesn't exists in contact
                                 */
                                receiverName = receiverIdentifier;
                                userImage = messageObject.getString("userImage");
                            }
                        }

                        if (messageObject.has("isStar")) {
                            isStar = messageObject.getBoolean("isStar");
                        } else {
                            isStar = false;
                        }

                        String documentId =
                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId);
                        if (documentId.isEmpty()) {


                            /*
                             * Here, chatId is assumed to be empty
                             */
                            documentId =
                                    findDocumentIdOfReceiver(receiverUid, Utilities.tsInGmt(), receiverName,
                                            userImage, secretId, true, receiverIdentifier,
                                            obj.getString("chatId"), false, isStar);
                        }

                        db.setDocumentIdOfReceiver(documentId, docIdForDoubleTickAck, receiverUid);

                        boolean alreadyExists;

                        if (secretId.isEmpty()) {
                            alreadyExists = db.checkAlreadyExists(documentId, id);
                        } else {
                            alreadyExists = db.checkAlreadyExistsSecretChat(documentId, id);
                        }

                        if (!alreadyExists || messageType.equals("15")) {

                            if (messageObject.has("expectedDTime")) {

                                expectedDTime = messageObject.getLong("expectedDTime");
                                setTimer(documentId, id, expectedDTime);
                            }

                            if (messageType.equals("11")) {

                                messageDatabaseHandler
                                        .putMessageInDb(receiverUid, messageType, actualMessage, timestamp, id,
                                                documentId,

                                                null, dataSize, dTime, receiverName, isSelf,
                                                messageObject.getInt("status"), messageObject.has("expectedDTime"),
                                                expectedDTime,
                                                Utilities.epochtoGmt(messageObject.getString("removedAt")), false,
                                                secretId);
                            } else if (messageType.equals("13")) {

                                messageDatabaseHandler
                                        .putPostMessageInDb(receiverUid, actualMessage, timestamp, id,
                                                documentId, dTime, isSelf, messageObject.getInt("status"),
                                                messageObject.has("expectedDTime"), expectedDTime, postId,
                                                postTitle, postType, secretId);
                            } else if (messageType.equals("1")
                                    || messageType.equals("2")
                                    || messageType.equals("7")) {

                                messageDatabaseHandler
                                        .putMessageInDb(receiverUid, messageType, actualMessage, timestamp, id,
                                                documentId, messageObject.getString("thumbnail").trim(),

                                                dataSize, dTime, receiverName, isSelf,
                                                messageObject.getInt("status"), messageObject.has("expectedDTime"),
                                                expectedDTime, null, false, secretId);
                            } else if (messageType.equals("9")) {

                                messageDatabaseHandler
                                        .putMessageInDb(receiverUid, actualMessage, timestamp, id, documentId,
                                                dataSize, dTime, isSelf, messageObject.getInt("status"),
                                                messageObject.has("expectedDTime"), expectedDTime, mimeType,
                                                fileName, extension, secretId);
                            } else if (messageType.equals("10")) {

                                /*
                                 * For the reply message received
                                 */

                                String replyType = messageObject.getString("replyType");

                                String previousMessageType = messageObject.getString("previousType");

                                String previousFileType = "", thumbnail = "";

                                if (previousMessageType.equals("9")) {

                                    previousFileType = messageObject.getString("previousFileType");
                                }
                                if (replyType.equals("1") || replyType.equals("2") || replyType.equals(
                                        "7")) {

                                    thumbnail = messageObject.getString("thumbnail").trim();
                                }

                                messageDatabaseHandler
                                        .putReplyMessageInDb(receiverUid, actualMessage, timestamp, id,
                                                documentId, dataSize, dTime, isSelf, messageObject.getInt("status"),
                                                messageObject.has("expectedDTime"), expectedDTime, mimeType,
                                                fileName, extension, thumbnail, receiverName, replyType,
                                                messageObject.getString("previousReceiverIdentifier"),
                                                messageObject.getString("previousFrom"),
                                                messageObject.getString("previousPayload"),
                                                messageObject.getString("previousType"),
                                                messageObject.getString("previousId"), previousFileType,
                                                messageObject.has("wasEdited"), postId, postTitle, postType,
                                                secretId);
                            } else if (messageType.equals("15")) {
                                String transactionId = obj.getString("fromTxnId");
                                String amount = messageObject.getString("amount");
                                String transferStatus = messageObject.getString("transferStatus");
                                String transferStatusText = messageObject.getString("transferStatusText");

                                messageDatabaseHandler
                                        .putTransferMessageInDb(receiverUid, messageType, actualMessage,
                                                timestamp, id, documentId, documentId, amount, transferStatus,
                                                transferStatusText, dataSize, dTime, receiverName, isSelf, -1,
                                                false, -1, secretId,transactionId);
                            } else if (messageType.equals("16")) {

                                String typeOfCall, callId;
                                int creation;

                                typeOfCall = obj.getString("typeOfCall");
                                callId = obj.getString("callId");
                                creation = obj.getInt("creation");

                                if (!receiverUid.equals(userId)) {
                                    messageDatabaseHandler
                                            .putMissedCallMessageInDb(receiverUid, messageType, actualMessage,
                                                    timestamp, id, documentId, documentId, typeOfCall, callId,
                                                    creation, dataSize, dTime, receiverName, isSelf, -1, false, -1,
                                                    secretId);
                                }
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

                                messageDatabaseHandler
                                        .putCallMessageInDb(receiverUid, messageType, actualMessage, timestamp,
                                                id, documentId, documentId, typeOfCall, callId, creation, dataSize,
                                                dTime, receiverName, isSelf, -1, false, -1, secretId, duration,
                                                isInComingCall);
                            } else {

                                messageDatabaseHandler
                                        .putMessageInDb(receiverUid, messageType, actualMessage, timestamp, id,
                                                documentId, null, dataSize, dTime, receiverName, isSelf,
                                                messageObject.getInt("status"), messageObject.has("expectedDTime"),
                                                expectedDTime, null, messageObject.has("wasEdited"), secretId);
                            }

                            if ((messageObject.getInt("status") == 1) && isSelf && !messageType.equals(
                                    "11") && (!messageType.equals("0") || !actualMessage.isEmpty())) {
                                JSONObject obj2 = new JSONObject();
                                obj2.put("from", AppController.getInstance().getUserId());
                                obj2.put("msgIds", new JSONArray(Arrays.asList(new String[]{id})));
                                obj2.put("doc_id", docIdForDoubleTickAck);
                                obj2.put("to", receiverUid);

                                obj2.put("status", "2");
                                obj2.put("deliveryTime", Utilities.getGmtEpoch());

                                if (!secretId.isEmpty()) {
                                    obj2.put("secretId", secretId);
                                    obj2.put("dTime", dTime);
                                }

                                AppController.getInstance()
                                        .publish(MqttEvents.Acknowledgement.value + "/" + receiverUid, obj2, 2,
                                                false);
                            }
                            int type = Integer.parseInt(messageType);
                            int replyTypeInt = -1;

                            if (obj.has("replyType")) {
                                replyTypeInt = Integer.parseInt(obj.getString("replyType"));
                            }
                            if (type == 1 || type == 2 || type == 5 || type == 7 || type == 9 || (type
                                    == 10 && (replyTypeInt == 1
                                    || replyTypeInt == 2
                                    || replyTypeInt == 5
                                    || replyTypeInt == 7
                                    || replyTypeInt == 9))) {
                                if (ActivityCompat.checkSelfPermission(instance,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {

                                    if (instance.checkWifiConnected()) {

                                        Object[] params = new Object[8];
                                        try {

                                            params[0] =
                                                    new String(Base64.decode(actualMessage, Base64.DEFAULT), "UTF-8");
                                        } catch (UnsupportedEncodingException e) {

                                        }

                                        params[1] = messageType;
                                        params[5] = id;
                                        params[6] = documentId;
                                        params[7] = receiverUid;
                                        switch (type) {
                                            case 1: {
                                                if (instance.getSharedPreferences().getBoolean("wifiPhoto", false)) {
                                                    params[3] = instance.getFilesDir()
                                                            + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                            + "/"
                                                            + timestamp
                                                            + ".jpg";
                                                    params[4] = instance.getExternalFilesDir(null)
                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                            + receiverUid
                                                            + id
                                                            + ".jpg";

                                                    new DownloadMessage().execute(params);
                                                }
                                                break;
                                            }

                                            case 2: {
                                                if (instance.getSharedPreferences().getBoolean("wifiVideo", false)) {
                                                    params[3] = instance.getFilesDir()
                                                            + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                            + "/"
                                                            + timestamp
                                                            + ".jpg";
                                                    params[4] = instance.getExternalFilesDir(null)
                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                            + receiverUid
                                                            + id
                                                            + ".mp4";

                                                    new DownloadMessage().execute(params);
                                                }
                                                break;
                                            }
                                            case 5: {
                                                if (instance.getSharedPreferences().getBoolean("wifiAudio", false)) {
                                                    params[4] = instance.getExternalFilesDir(null)
                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                            + receiverUid
                                                            + id
                                                            + ".3gp";

                                                    new DownloadMessage().execute(params);
                                                }
                                                break;
                                            }

                                            case 7: {
                                                if (instance.getSharedPreferences().getBoolean("wifiPhoto", false)) {
                                                    params[3] = instance.getFilesDir()
                                                            + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                            + "/"
                                                            + timestamp
                                                            + ".jpg";
                                                    params[4] = instance.getExternalFilesDir(null)
                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                            + receiverUid
                                                            + id
                                                            + ".jpg";

                                                    new DownloadMessage().execute(params);
                                                }
                                                break;
                                            }
                                            case 9: {
                                                if (instance.getSharedPreferences().getBoolean("wifiDocument", false)) {
                                                    params[4] = instance.getExternalFilesDir(null)
                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                            + fileName;

                                                    new DownloadMessage().execute(params);
                                                }
                                                break;
                                            }
                                            case 10: {

                                                params[2] = obj.getString("replyType");

                                                switch (Integer.parseInt((String) params[2])) {

                                                    case 1: {
                                                        if (instance.getSharedPreferences().getBoolean("wifiPhoto", false)) {

                                                            params[3] = instance.getFilesDir()
                                                                    + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                    + "/"
                                                                    + timestamp
                                                                    + ".jpg";
                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".jpg";
                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 2: {
                                                        if (instance.getSharedPreferences().getBoolean("wifiVideo", false)) {

                                                            params[3] = instance.getFilesDir()
                                                                    + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                    + "/"
                                                                    + timestamp
                                                                    + ".jpg";
                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".mp4";

                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 5: {
                                                        if (instance.getSharedPreferences().getBoolean("wifiAudio", false)) {

                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".3gp";
                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 7: {
                                                        if (instance.getSharedPreferences().getBoolean("wifiPhoto", false)) {

                                                            params[3] = instance.getFilesDir()
                                                                    + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                    + "/"
                                                                    + timestamp
                                                                    + ".jpg";
                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".jpg";
                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 9: {
                                                        if (instance.getSharedPreferences().getBoolean("wifiDocument", false)) {
                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + fileName;

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

                                            params[0] =
                                                    new String(Base64.decode(actualMessage, Base64.DEFAULT), "UTF-8");
                                        } catch (UnsupportedEncodingException e) {

                                        }

                                        params[1] = messageType;
                                        params[5] = id;
                                        params[6] = documentId;
                                        params[7] = receiverUid;
                                        switch (type) {
                                            case 1: {
                                                if (instance.getSharedPreferences().getBoolean("mobilePhoto", false)) {
                                                    params[3] = instance.getFilesDir()
                                                            + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                            + "/"
                                                            + timestamp
                                                            + ".jpg";
                                                    params[4] = instance.getExternalFilesDir(null)
                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                            + receiverUid
                                                            + id
                                                            + ".jpg";

                                                    new DownloadMessage().execute(params);
                                                }
                                                break;
                                            }

                                            case 2: {
                                                if (instance.getSharedPreferences().getBoolean("mobileVideo", false)) {
                                                    params[3] = instance.getFilesDir()
                                                            + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                            + "/"
                                                            + timestamp
                                                            + ".jpg";
                                                    params[4] = instance.getExternalFilesDir(null)
                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                            + receiverUid
                                                            + id
                                                            + ".mp4";

                                                    new DownloadMessage().execute(params);
                                                }
                                                break;
                                            }
                                            case 5: {
                                                if (instance.getSharedPreferences().getBoolean("mobileAudio", false)) {
                                                    params[4] = instance.getExternalFilesDir(null)
                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                            + receiverUid
                                                            + id
                                                            + ".3gp";

                                                    new DownloadMessage().execute(params);
                                                }
                                                break;
                                            }

                                            case 7: {
                                                if (instance.getSharedPreferences().getBoolean("mobilePhoto", false)) {
                                                    params[3] = instance.getFilesDir()
                                                            + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                            + "/"
                                                            + timestamp
                                                            + ".jpg";
                                                    params[4] = instance.getExternalFilesDir(null)
                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                            + receiverUid
                                                            + id
                                                            + ".jpg";

                                                    new DownloadMessage().execute(params);
                                                }
                                                break;
                                            }
                                            case 9: {
                                                if (instance.getSharedPreferences().getBoolean("mobileDocument", false)) {
                                                    params[4] = instance.getExternalFilesDir(null)
                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                            + fileName;

                                                    new DownloadMessage().execute(params);
                                                }
                                                break;
                                            }
                                            case 10: {

                                                params[2] = obj.getString("replyType");

                                                switch (Integer.parseInt((String) params[2])) {

                                                    case 1: {
                                                        if (instance.getSharedPreferences().getBoolean("mobilePhoto", false)) {

                                                            params[3] = instance.getFilesDir()
                                                                    + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                    + "/"
                                                                    + timestamp
                                                                    + ".jpg";
                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".jpg";
                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 2: {
                                                        if (instance.getSharedPreferences().getBoolean("mobileVideo", false)) {

                                                            params[3] = instance.getFilesDir()
                                                                    + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                    + "/"
                                                                    + timestamp
                                                                    + ".jpg";
                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".mp4";

                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 5: {
                                                        if (instance.getSharedPreferences().getBoolean("mobileAudio", false)) {

                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".3gp";
                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 7: {
                                                        if (instance.getSharedPreferences().getBoolean("mobilePhoto", false)) {

                                                            params[3] = instance.getFilesDir()
                                                                    + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER
                                                                    + "/"
                                                                    + timestamp
                                                                    + ".jpg";
                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + receiverUid
                                                                    + id
                                                                    + ".jpg";
                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                    case 9: {
                                                        if (instance.getSharedPreferences().getBoolean("mobileDocument", false)) {
                                                            params[4] = instance.getExternalFilesDir(null)
                                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER
                                                                    + fileName;

                                                            new DownloadMessage().execute(params);
                                                        }
                                                        break;
                                                    }
                                                }
                                                break;
                                            }
                                        }
                                    }

                                    //                                            }
                                }
                            }
                        } else {

                            if (messageType.equals("11")) {

                                messageDatabaseHandler
                                        .putMessageInDb(receiverUid, messageType, actualMessage, timestamp, id,
                                                documentId,

                                                null, dataSize, dTime, receiverName, isSelf,
                                                messageObject.getInt("status"), messageObject.has("expectedDTime"),
                                                expectedDTime,
                                                Utilities.epochtoGmt(messageObject.getString("removedAt")), false,
                                                secretId);
                            }
                        }

                        if (!secretId.isEmpty()) {

                            if (!actualMessage.trim().isEmpty() || dTime != -1) {

                                db.updateSecretInviteImageVisibility(documentId, false);
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /*
         * For callback in to activity to update UI
         */

        try {
            obj.put("eventName", topic);
            bus.post(obj);
        } catch (Exception e) {

        }
    }

    public void setTimer(String documentId, String messageId, long expectedDTime) {

        if (expectedDTime > 0) {
            long temp = Utilities.getGmtEpoch();
            instance.getDbController().setTimerStarted(documentId, messageId, temp + expectedDTime);
        }
    }
}
