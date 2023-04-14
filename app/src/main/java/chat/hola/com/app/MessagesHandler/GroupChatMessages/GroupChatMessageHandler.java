package chat.hola.com.app.MessagesHandler.GroupChatMessages;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import chat.hola.com.app.GroupChat.Activities.GroupChatMessageScreen;
import chat.hola.com.app.MessagesHandler.Utilities.DownloadMessage;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import static chat.hola.com.app.AppController.findDocumentIdOfReceiver;

@SuppressWarnings("warning")
public class GroupChatMessageHandler {
    private AppController instance = AppController.getInstance();
    private String removedMessageString = "The message has been removed";
    private SessionApiCall sessionApiCall = new SessionApiCall();

    /**
     * @param obj group message that has been received
     */

    @SuppressWarnings("unchecked")
    public void processGroupMessageReceived(JSONObject obj) {
        /*
         * Actual message being exchanged in the group
         */

        CouchDbController db = instance.getDbController();
        GroupChatDatabaseHelper gcDatabaseHelper = instance.getGroupChatDatabaseHelper();
        Bus bus = AppController.getBus();
        String groupChatsDocId = instance.getGroupChatsDocId();

        String topic = MqttEvents.GroupChats.value + "/" + instance.getUserId();

        try {
            if (obj.has("payload")) {

                /*
                 * For an actual message(Like text,image,video etc.) received
                 */


                /*
                 * Sender details
                 */
                String receiverUid = obj.getString("from");
                String receiverIdentifier = obj.getString("receiverIdentifier");

                String groupId = obj.getString("to");

                String messageType = obj.getString("type");
                String actualMessage = obj.getString("payload").trim();
                String timestamp = obj.getString("timestamp");

                String id = obj.getString("id");

                String mimeType = "", fileName = "", extension = "";
                String postTitle = "", postId = "";
                int postType = -1;

                int dataSize = -1;

                if (messageType.equals("1")
                        || messageType.equals("2")
                        || messageType.equals("5")
                        || messageType.equals("7")
                        || messageType.equals("9")) {
                    dataSize = obj.getInt("dataSize");

                    if (messageType.equals("9")) {

                        mimeType = obj.getString("mimeType");
                        fileName = obj.getString("fileName");
                        extension = obj.getString("extension");
                    }
                } else if (messageType.equals("10")) {

                    String replyType = obj.getString("replyType");

                    if (replyType.equals("1")
                            || replyType.equals("2")
                            || replyType.equals("5")
                            || replyType.equals("7")
                            || replyType.equals("9")) {
                        dataSize = obj.getInt("dataSize");

                        if (replyType.equals("9")) {

                            mimeType = obj.getString("mimeType");
                            fileName = obj.getString("fileName");
                            extension = obj.getString("extension");
                        } else if (replyType.equals("13")) {

                            postType = obj.getInt("postType");
                            postId = obj.getString("postId");
                            postTitle = obj.getString("postTitle");
                        }
                    }
                } else if (messageType.equals("13")) {

                    postType = obj.getInt("postType");
                    postId = obj.getString("postId");
                    postTitle = obj.getString("postTitle");
                }

                String receiverName = obj.getString("name");
                String userImage = obj.getString("userImage");

                String documentId = AppController.getInstance().findDocumentIdOfReceiver(groupId, "");
                if (documentId.isEmpty()) {
                    /*
                     * Here, chatId is essentially put as empty,although it is same as groupId
                     */
                    documentId =
                            findDocumentIdOfReceiver(groupId, Utilities.tsInGmt(), receiverName, userImage, "",
                                    false, receiverIdentifier, "", true, false);
                }



                /*
                 * For callback in to activity to update UI
                 */

                if (!db.checkAlreadyExists(documentId, id)) {

                    String replyType = "";

                    if (messageType.equals("1") || messageType.equals("2") || messageType.equals("7")) {

                        gcDatabaseHelper
                                .putGroupMessageInDb(receiverUid, messageType, actualMessage, timestamp, id,
                                        documentId, obj.getString("thumbnail").trim(), dataSize, receiverIdentifier,
                                        false, -1, null, false);
                    } else if (messageType.equals("9")) {
                        /*
                         * For document received
                         */

                        gcDatabaseHelper
                                .putGroupMessageInDb(receiverUid, actualMessage, timestamp, id, documentId,
                                        dataSize, false, -1, mimeType, fileName, extension, receiverIdentifier);
                    } else if (messageType.equals("13")) {
                        /*
                         * For post received
                         */

                        gcDatabaseHelper
                                .putGroupPostMessageInDb(receiverUid, actualMessage, timestamp, id, documentId,
                                        false, -1, mimeType, fileName, extension, receiverIdentifier, postId, postTitle,
                                        postType);
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
                        gcDatabaseHelper
                                .putGroupReplyMessageInDb(receiverUid, actualMessage, timestamp, id, documentId,
                                        dataSize, false, -1, mimeType, fileName, extension, thumbnail,
                                        receiverIdentifier, replyType, obj.getString("previousReceiverIdentifier"),
                                        obj.getString("previousFrom"), obj.getString("previousPayload"),
                                        obj.getString("previousType"), obj.getString("previousId"), previousFileType,
                                        obj.has("wasEdited"), postId, postTitle, postType);
                    } else if (messageType.equals("11")) {

                        /*
                         * For message removal
                         */
                        db.updateChatListForRemovedOrEditedMessage(documentId, actualMessage, true,
                                Utilities.epochtoGmt(obj.getString("removedAt")));
                        db.markMessageAsRemoved(documentId, id, removedMessageString,
                                Utilities.epochtoGmt(obj.getString("removedAt")));
                        try {

                            obj.put("eventName", topic);

                            obj.put("groupId", groupId);
                            bus.post(obj);
                        } catch (Exception e) {

                        }
                        return;
                    } else if (messageType.equals("12")) {

                        /*
                         * For message edit
                         */

                        db.updateChatListForRemovedOrEditedMessage(documentId, actualMessage, true,
                                Utilities.epochtoGmt(obj.getString("editedAt")));
                        db.markMessageAsEdited(documentId, id, actualMessage);
                        try {

                            obj.put("eventName", topic);

                            obj.put("groupId", groupId);
                            bus.post(obj);
                        } catch (Exception e) {

                        }
                        return;
                    } else {

                        gcDatabaseHelper
                                .putGroupMessageInDb(receiverUid, messageType, actualMessage, timestamp, id,
                                        documentId, null, dataSize, receiverIdentifier, false, -1, null,
                                        obj.has("wasEdited"));
                    }

                    JSONObject obj2 = new JSONObject();
                    obj2.put("from", AppController.getInstance().getUserId());
                    obj2.put("msgIds", new JSONArray(Arrays.asList(new String[]{id})));
                    obj2.put("to", receiverUid);
                    obj2.put("status", "2");

                    AppController.getInstance()
                            .publish(MqttEvents.GroupChatAcks.value + "/" + receiverUid, obj2, 2, false);


                    /*
                     * For callback in to activity to update UI
                     */

                    try {

                        obj.put("eventName", topic);

                        obj.put("groupId", groupId);
                        bus.post(obj);
                    } catch (Exception e) {

                    }

                    if (instance.getContactSynced()) {
                        if (!db.checkIfReceiverChatMuted(instance.getMutedDocId(), groupId, "")) {
                            Intent intent = new Intent(instance, GroupChatMessageScreen.class);

                            intent.putExtra("receiverUid", groupId);
                            intent.putExtra("receiverName", receiverName);

                            intent.putExtra("receiverIdentifier", receiverIdentifier);

                            intent.putExtra("documentId", documentId);

                            intent.putExtra("colorCode", AppController.getInstance().getColorCode(5));

                            intent.putExtra("receiverImage", userImage);

                            intent.putExtra("fromNotification", true);


                            /*
                             *To generate the push notification locally
                             */

                            instance.getNotificationGenerator().generatePushNotificationLocal(documentId, messageType, receiverName, actualMessage,
                                    intent, -1, "", groupId, "", replyType, "", "", "");
                        }
                    }
                    int type = Integer.parseInt(messageType);
                    int replyTypeInt = -1;
                    if (!replyType.isEmpty()) {
                        replyTypeInt = Integer.parseInt(replyType);
                    }
                    if (type == 1 || type == 2 || type == 5 || type == 7 || type == 9 || (type == 10 && (
                            replyTypeInt == 1
                                    || replyTypeInt == 2
                                    || replyTypeInt == 5
                                    || replyTypeInt == 7
                                    || replyTypeInt == 9))) {
                        if (ActivityCompat.checkSelfPermission(instance,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

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
                                params[7] = groupId;
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

                                    params[0] = new String(Base64.decode(actualMessage, Base64.DEFAULT), "UTF-8");
                                } catch (UnsupportedEncodingException e) {

                                }

                                params[1] = messageType;
                                params[5] = id;
                                params[6] = documentId;
                                params[7] = groupId;
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
                        }
                    }
                } else {
                    if (messageType.equals("11")) {

                        /*
                         * For message removal
                         */

                        db.updateChatListForRemovedOrEditedMessage(documentId, actualMessage, true,
                                Utilities.epochtoGmt(obj.getString("removedAt")));
                        db.markMessageAsRemoved(documentId, id, removedMessageString,
                                Utilities.epochtoGmt(obj.getString("removedAt")));
                        try {

                            obj.put("eventName", topic);

                            obj.put("groupId", groupId);
                            bus.post(obj);
                        } catch (Exception e) {

                        }
                        return;
                    } else if (messageType.equals("12")) {

                        /*
                         * For message edit
                         */
                        db.updateChatListForRemovedOrEditedMessage(documentId, actualMessage, true,
                                Utilities.epochtoGmt(obj.getString("editedAt")));
                        db.markMessageAsEdited(documentId, id, actualMessage);
                        try {

                            obj.put("eventName", topic);

                            obj.put("groupId", groupId);
                            bus.post(obj);
                        } catch (Exception e) {

                        }
                        return;
                    }
                }

                /*
                 * For sending the acknowledgement to the server of the message in the group being delivered(Only sent for the normal messages)
                 */

            } else {

                switch (obj.getInt("type")) {
                    case 0: {
                        /*
                         * Group created
                         *
                         */

                        String groupId = obj.getString("groupId");

                        /*
                         *
                         * To check if group already exists
                         */

                        String groupMembersDocId = db.fetchGroupChatDocumentId(groupChatsDocId, groupId);
                        if (groupMembersDocId == null) {
                            /*
                             * Group doesn't exists
                             */

                            groupMembersDocId = db.createGroupMembersDocument();
                        }

                        JSONArray members = obj.getJSONArray("members");

                        ArrayList<Map<String, Object>> groupMembers = new ArrayList<Map<String, Object>>();

                        JSONObject memberObject;

                        Map<String, Object> memberMap;

                        for (int i = 0; i < members.length(); i++) {
                            memberMap = new HashMap<>();
                            try {
                                memberObject = members.getJSONObject(i);

                                memberMap.put("memberId", memberObject.getString("memberId"));
                                memberMap.put("memberIdentifier", memberObject.getString("memberIdentifier"));
                                memberMap.put("memberImage", memberObject.getString("memberImage"));
                                memberMap.put("memberStatus", memberObject.getString("memberStatus"));
                                memberMap.put("memberIsAdmin", memberObject.getBoolean("memberIsAdmin"));

                                memberMap.put("memberIsStar", memberObject.getBoolean("memberIsStar"));
                            } catch (JSONException e) {

                            }
                            groupMembers.add(memberMap);
                        }

                        db.addGroupMembersDetails(groupMembersDocId, groupMembers,
                                obj.getString("createdByMemberId"), obj.getString("createdByMemberIdentifier"),
                                obj.getLong("createdAt"), true);
                        db.addGroupChat(groupChatsDocId, groupId, groupMembersDocId);

                        String docId = AppController.getInstance().findDocumentIdOfReceiver(groupId, "");
                        String groupPicUrl = "";

                        if (obj.has("groupImageUrl")) {

                            groupPicUrl = obj.getString("groupImageUrl");
                        }

                        if (docId.isEmpty()) {

                            docId = findDocumentIdOfReceiver(groupId, obj.getString("timestamp"),
                                    obj.getString("groupSubject"), groupPicUrl, "", false, groupId, "", true, false);
                        } else {

                            db.updateGroupNameAndImage(docId, obj.getString("groupSubject"), groupPicUrl);
                        }

                        Map<String, Object> map = new HashMap<>();

                        map.put("messageType", "98");

                        map.put("isSelf", false);
                        map.put("from", groupId);
                        map.put("Ts", obj.getString("timestamp"));
                        map.put("id", obj.getString("id"));

                        map.put("type", 0);

                        map.put("groupName", obj.getString("groupSubject"));

                        map.put("initiatorId", obj.getString("initiatorId"));
                        map.put("initiatorIdentifier", obj.getString("initiatorIdentifier"));

                        map.put("deliveryStatus", "0");

                        String text, initiatorName;
                        String initiatorId = obj.getString("initiatorId");
                        if (initiatorId.equals(AppController.getInstance().getUserId())) {

                            initiatorName = instance.getString(R.string.You);
                        } else {
                            initiatorName =
                                    db.getFriendName(AppController.getInstance().getFriendsDocId(), initiatorId);
                            if (initiatorName == null) {

                                initiatorName = obj.getString("initiatorIdentifier");
                            }
                        }

                        text = instance.getString(R.string.CreatedGroup, initiatorName) + " " + obj.getString(
                                "groupSubject");

                        gcDatabaseHelper.addGroupChatMessageInDB(docId, map, obj.getString("timestamp"), text, true);
                        obj.put("docId", docId);

                        obj.put("message", text);

                        obj.put("eventName", topic);
                        bus.post(obj);
                        break;
                    }

                    case 1: {

                        /*
                         * Member added
                         */

                        String groupId = obj.getString("groupId");

                        /*
                         *
                         * To check if group already exists
                         */
                        String docId = AppController.getInstance().findDocumentIdOfReceiver(groupId, "");
                        String groupMembersDocId = db.fetchGroupChatDocumentId(groupChatsDocId, groupId);

                        boolean updateGroupDetailsRequired = true;

                        if (groupMembersDocId == null) {
                            /*
                             * Group doesn't exists
                             */

                            if (!obj.has("members")) {

                                new requestGroupMembersOnBackgroundThread().execute((JSONObject) obj);

                                return;
                            }

                            groupMembersDocId = db.createGroupMembersDocument();

                            db.addGroupChat(groupChatsDocId, groupId, groupMembersDocId);

                            String groupPicUrl = "";

                            if (obj.has("groupImageUrl")) {

                                groupPicUrl = obj.getString("groupImageUrl");
                            }

                            if (docId.isEmpty()) {

                                docId = findDocumentIdOfReceiver(groupId, obj.getString("timestamp"),
                                        obj.getString("groupSubject"), groupPicUrl, "", false, groupId, "", true,
                                        false);
                            } else {

                                db.updateGroupNameAndImage(docId, obj.getString("groupSubject"), groupPicUrl);
                            }

                            updateGroupDetailsRequired = false;
                        }

                        String memberId = obj.getString("memberId");

                        if (memberId.equals(instance.getUserId())) {

                            JSONArray members = obj.getJSONArray("members");

                            ArrayList<Map<String, Object>> groupMembers = new ArrayList<Map<String, Object>>();

                            JSONObject memberObject;

                            Map<String, Object> memberMap;


                            /*
                             * Current member is added for the first time
                             */

                            for (int i = 0; i < members.length(); i++) {
                                memberMap = new HashMap<>();
                                try {
                                    memberObject = members.getJSONObject(i);

                                    memberMap.put("memberId", memberObject.getString("memberId"));
                                    memberMap.put("memberIdentifier", memberObject.getString("memberIdentifier"));
                                    memberMap.put("memberImage", memberObject.getString("memberImage"));
                                    memberMap.put("memberStatus", memberObject.getString("memberStatus"));
                                    memberMap.put("memberIsAdmin", memberObject.getBoolean("memberIsAdmin"));

                                    memberMap.put("memberIsStar", memberObject.getBoolean("memberIsStar"));
                                } catch (JSONException e) {

                                }
                                groupMembers.add(memberMap);
                            }

                            db.addGroupMembersDetails(groupMembersDocId, groupMembers,
                                    obj.getString("createdByMemberId"), obj.getString("createdByMemberIdentifier"),
                                    obj.getLong("createdAt"), true);

                            if (updateGroupDetailsRequired) {

                                String groupPicUrl = "";

                                if (obj.has("groupImageUrl")) {

                                    groupPicUrl = obj.getString("groupImageUrl");
                                }
                                db.updateGroupNameAndImage(docId, obj.getString("groupSubject"), groupPicUrl);
                            }
                        }

                        Map<String, Object> map = new HashMap<>();
                        map.put("memberId", obj.getString("memberId"));
                        map.put("memberIdentifier", obj.getString("memberIdentifier"));
                        map.put("memberImage", obj.getString("memberImage"));
                        map.put("memberStatus", obj.getString("memberStatus"));
                        map.put("memberIsAdmin", obj.getBoolean("memberIsAdmin"));
                        map.put("memberIsStar", obj.getBoolean("memberIsStar"));
                        db.addGroupMember(groupMembersDocId, map, memberId);
                        if (!docId.isEmpty()) {

                            map = new HashMap<>();

                            map.put("messageType", "98");

                            map.put("isSelf", false);
                            map.put("from", groupId);
                            map.put("Ts", obj.getString("timestamp"));
                            map.put("id", obj.getString("id"));

                            map.put("type", 1);

                            map.put("memberId", memberId);

                            map.put("memberIdentifier", obj.getString("memberIdentifier"));

                            map.put("initiatorId", obj.getString("initiatorId"));
                            map.put("initiatorIdentifier", obj.getString("initiatorIdentifier"));

                            map.put("deliveryStatus", "0");

                            String text, initiatorName, memberName;
                            String initiatorId = obj.getString("initiatorId");
                            if (initiatorId.equals(AppController.getInstance().getUserId())) {

                                initiatorName = instance.getString(R.string.You);
                            } else {
                                initiatorName =
                                        db.getFriendName(AppController.getInstance().getFriendsDocId(), initiatorId);
                                if (initiatorName == null) {

                                    initiatorName = obj.getString("initiatorIdentifier");
                                }
                            }

                            if (memberId.equals(AppController.getInstance().getUserId())) {

                                memberName = instance.getString(R.string.YouSmall);
                            } else {
                                memberName =
                                        db.getFriendName(AppController.getInstance().getFriendsDocId(), memberId);
                                if (memberName == null) {

                                    memberName = obj.getString("memberIdentifier");
                                }
                            }

                            text = initiatorName
                                    + " "
                                    + instance.getString(R.string.AddedMember, memberName)
                                    + " "
                                    + instance.getString(R.string.ToGroup);

                            gcDatabaseHelper.addGroupChatMessageInDB(docId, map, obj.getString("timestamp"), text, true);

                            obj.put("docId", docId);

                            obj.put("memberName", memberName);

                            obj.put("message", text);

                            obj.put("eventName", topic);
                            bus.post(obj);
                        }

                        break;
                    }

                    case 2: {
                        /*
                         * Member removed
                         *
                         */

                        String groupId = obj.getString("groupId");

                        /*
                         *
                         * To check if group already exists
                         */

                        String groupMembersDocId = db.fetchGroupChatDocumentId(groupChatsDocId, groupId);
                        if (groupMembersDocId == null) {
                            /*
                             * Group doesn't exists
                             */

                            new requestGroupMembersOnBackgroundThread().execute((JSONObject) obj);

                            return;
                        }
                        db.removeGroupMember(groupMembersDocId, obj.getString("memberId"));

                        String docId = AppController.getInstance().findDocumentIdOfReceiver(groupId, "");

                        String memberId = obj.getString("memberId");
                        if (!docId.isEmpty()) {

                            Map<String, Object> map = new HashMap<>();

                            map.put("messageType", "98");

                            map.put("isSelf", false);
                            map.put("from", groupId);
                            map.put("Ts", obj.getString("timestamp"));
                            map.put("id", obj.getString("id"));

                            map.put("type", 2);

                            map.put("memberId", memberId);

                            map.put("memberIdentifier", obj.getString("memberIdentifier"));

                            map.put("initiatorId", obj.getString("initiatorId"));
                            map.put("initiatorIdentifier", obj.getString("initiatorIdentifier"));

                            map.put("deliveryStatus", "0");

                            String text, initiatorName, memberName;
                            String initiatorId = obj.getString("initiatorId");
                            if (initiatorId.equals(AppController.getInstance().getUserId())) {

                                initiatorName = instance.getString(R.string.You);
                            } else {
                                initiatorName =
                                        db.getFriendName(AppController.getInstance().getFriendsDocId(), initiatorId);
                                if (initiatorName == null) {

                                    initiatorName = obj.getString("initiatorIdentifier");
                                }
                            }

                            if (memberId.equals(AppController.getInstance().getUserId())) {

                                memberName = instance.getString(R.string.YouSmall);
                            } else {
                                memberName =
                                        db.getFriendName(AppController.getInstance().getFriendsDocId(), memberId);
                                if (memberName == null) {

                                    memberName = obj.getString("memberIdentifier");
                                }
                            }

                            text = initiatorName + " " + instance.getString(R.string.Removed) + " " + memberName;

                            gcDatabaseHelper.addGroupChatMessageInDB(docId, map, obj.getString("timestamp"), text, true);

                            obj.put("docId", docId);

                            obj.put("message", text);

                            obj.put("eventName", topic);
                            bus.post(obj);
                        }

                        break;
                    }
                    case 3: {
                        /*
                         * Made admin
                         *
                         */

                        String groupId = obj.getString("groupId");

                        /*
                         *
                         * To check if group already exists
                         */

                        String groupMembersDocId = db.fetchGroupChatDocumentId(groupChatsDocId, groupId);
                        if (groupMembersDocId == null) {
                            /*
                             * Group doesn't exists
                             */

                            new requestGroupMembersOnBackgroundThread().execute((JSONObject) obj);

                            return;
                        }
                        db.makeGroupAdmin(groupMembersDocId, obj.getString("memberId"));

                        String docId = AppController.getInstance().findDocumentIdOfReceiver(groupId, "");

                        String memberId = obj.getString("memberId");
                        if (!docId.isEmpty()) {

                            Map<String, Object> map = new HashMap<>();

                            map.put("messageType", "98");

                            map.put("isSelf", false);
                            map.put("from", groupId);
                            map.put("Ts", obj.getString("timestamp"));
                            map.put("id", obj.getString("id"));

                            map.put("type", 3);

                            map.put("memberId", memberId);

                            map.put("memberIdentifier", obj.getString("memberIdentifier"));

                            map.put("initiatorId", obj.getString("initiatorId"));
                            map.put("initiatorIdentifier", obj.getString("initiatorIdentifier"));

                            map.put("deliveryStatus", "0");

                            String text, initiatorName, memberName;
                            String initiatorId = obj.getString("initiatorId");
                            if (initiatorId.equals(AppController.getInstance().getUserId())) {

                                initiatorName = instance.getString(R.string.You);
                            } else {
                                initiatorName =
                                        db.getFriendName(AppController.getInstance().getFriendsDocId(), initiatorId);
                                if (initiatorName == null) {

                                    initiatorName = obj.getString("initiatorIdentifier");
                                }
                            }

                            if (memberId.equals(AppController.getInstance().getUserId())) {

                                memberName = instance.getString(R.string.YouSmall);
                            } else {
                                memberName =
                                        db.getFriendName(AppController.getInstance().getFriendsDocId(), memberId);
                                if (memberName == null) {

                                    memberName = obj.getString("memberIdentifier");
                                }
                            }

                            text = initiatorName
                                    + " "
                                    + instance.getString(R.string.Made)
                                    + " "
                                    + memberName
                                    + " "
                                    + instance.getString(R.string.MakeAdmin);

                            gcDatabaseHelper.addGroupChatMessageInDB(docId, map, obj.getString("timestamp"), text, true);

                            obj.put("docId", docId);

                            obj.put("message", text);

                            obj.put("eventName", topic);
                            bus.post(obj);
                        }
                        break;
                    }
                    case 4: {
                        /*
                         * Group name updated
                         *
                         */
                        String groupId = obj.getString("groupId");

                        /*
                         *
                         * To check if group already exists
                         */

                        String groupMembersDocId = db.fetchGroupChatDocumentId(groupChatsDocId, groupId);
                        if (groupMembersDocId == null) {
                            /*
                             * Group doesn't exists
                             */

                            new requestGroupMembersOnBackgroundThread().execute((JSONObject) obj);

                            return;
                        }

                        String docId = AppController.getInstance().findDocumentIdOfReceiver(groupId, "");

                        if (!docId.isEmpty()) {

                            db.updateGroupSubject(docId, obj.getString("groupSubject"));

                            Map<String, Object> map = new HashMap<>();

                            map.put("messageType", "98");

                            map.put("isSelf", false);
                            map.put("from", groupId);
                            map.put("Ts", obj.getString("timestamp"));
                            map.put("id", obj.getString("id"));

                            map.put("type", 4);

                            map.put("groupSubject", obj.getString("groupSubject"));
                            map.put("initiatorId", obj.getString("initiatorId"));
                            map.put("initiatorIdentifier", obj.getString("initiatorIdentifier"));

                            map.put("deliveryStatus", "0");

                            String text, initiatorName;
                            String initiatorId = obj.getString("initiatorId");
                            if (initiatorId.equals(AppController.getInstance().getUserId())) {

                                initiatorName = instance.getString(R.string.You);
                            } else {
                                initiatorName =
                                        db.getFriendName(AppController.getInstance().getFriendsDocId(), initiatorId);
                                if (initiatorName == null) {

                                    initiatorName = obj.getString("initiatorIdentifier");
                                }
                            }

                            text = initiatorName + " " + instance.getString(R.string.UpdatedGroupSubject,
                                    obj.getString("groupSubject"));

                            gcDatabaseHelper.addGroupChatMessageInDB(docId, map, obj.getString("timestamp"), text, true);

                            obj.put("docId", docId);
                            //                                String url = db.getGroupImageUrl(docId);
                            //
                            //                                if (url != null) {
                            //                                    obj.put("groupImageUrl", url);
                            //
                            //                                } else {
                            //
                            //
                            //                                    obj.put("groupImageUrl", "");
                            //
                            //                                }

                            obj.put("message", text);

                            obj.put("eventName", topic);
                            bus.post(obj);
                        }

                        break;
                    }
                    case 5: {
                        /*
                         * Group icon updated
                         *
                         */

                        String groupId = obj.getString("groupId");

                        /*
                         *
                         * To check if group already exists
                         */

                        String groupMembersDocId = db.fetchGroupChatDocumentId(groupChatsDocId, groupId);
                        if (groupMembersDocId == null) {
                            /*
                             * Group doesn't exists
                             */

                            new requestGroupMembersOnBackgroundThread().execute((JSONObject) obj);

                            return;
                        }

                        String docId = AppController.getInstance().findDocumentIdOfReceiver(groupId, "");

                        if (!docId.isEmpty()) {

                            db.updateGroupIcon(docId, obj.getString("groupImageUrl"));

                            Map<String, Object> map = new HashMap<>();

                            map.put("messageType", "98");

                            map.put("isSelf", false);
                            map.put("from", groupId);
                            map.put("Ts", obj.getString("timestamp"));
                            map.put("id", obj.getString("id"));

                            map.put("type", 5);

                            map.put("groupImageUrl", obj.getString("groupImageUrl"));
                            map.put("initiatorId", obj.getString("initiatorId"));
                            map.put("initiatorIdentifier", obj.getString("initiatorIdentifier"));

                            map.put("deliveryStatus", "0");

                            String text, initiatorName;
                            String initiatorId = obj.getString("initiatorId");
                            if (initiatorId.equals(AppController.getInstance().getUserId())) {

                                initiatorName = instance.getString(R.string.You);
                            } else {
                                initiatorName =
                                        db.getFriendName(AppController.getInstance().getFriendsDocId(), initiatorId);
                                if (initiatorName == null) {

                                    initiatorName = obj.getString("initiatorIdentifier");
                                }
                            }

                            text = initiatorName + " " + instance.getString(R.string.UpdatedGroupIcon);

                            gcDatabaseHelper.addGroupChatMessageInDB(docId, map, obj.getString("timestamp"), text, true);

                            obj.put("docId", docId);

                            obj.put("message", text);

                            obj.put("message", text);

                            //                                obj.put("groupSubject", db.getGroupSubject(docId));
                            obj.put("eventName", topic);
                            bus.post(obj);
                        }

                        break;
                    }

                    case 6: {

                        /*
                         * Member left the conversation
                         */

                        String groupId = obj.getString("groupId");

                        /*
                         *
                         * To check if group already exists
                         */

                        String groupMembersDocId = db.fetchGroupChatDocumentId(groupChatsDocId, groupId);
                        if (groupMembersDocId == null) {
                            /*
                             * Group doesn't exists
                             */

                            new requestGroupMembersOnBackgroundThread().execute((JSONObject) obj);

                            return;
                        }

                        String memberId = obj.getString("initiatorId");
                        db.removeGroupMember(groupMembersDocId, memberId);

                        String docId = AppController.getInstance().findDocumentIdOfReceiver(groupId, "");

                        if (!docId.isEmpty()) {

                            Map<String, Object> map = new HashMap<>();

                            map.put("messageType", "98");

                            map.put("isSelf", false);
                            map.put("from", groupId);
                            map.put("Ts", obj.getString("timestamp"));
                            map.put("id", obj.getString("id"));

                            map.put("type", 6);

                            map.put("initiatorId", memberId);
                            map.put("initiatorIdentifier", obj.getString("initiatorIdentifier"));

                            map.put("deliveryStatus", "0");

                            String text, memberName;

                            if (memberId.equals(AppController.getInstance().getUserId())) {

                                memberName = instance.getString(R.string.You);
                            } else {
                                memberName =
                                        db.getFriendName(AppController.getInstance().getFriendsDocId(), memberId);
                                if (memberName == null) {

                                    memberName = obj.getString("initiatorIdentifier");
                                }
                            }

                            text = instance.getString(R.string.LeftGroup, memberName);

                            gcDatabaseHelper.addGroupChatMessageInDB(docId, map, obj.getString("timestamp"), text, true);

                            obj.put("docId", docId);

                            obj.put("message", text);

                            obj.put("eventName", topic);
                            bus.post(obj);
                        }

                        break;
                    }
                }
            }
        } catch (JSONException e) {

        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private class requestGroupMembersOnBackgroundThread extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {

            requestGroupMembers((JSONObject) params[0]);
            return null;
        }
    }

    private void requestGroupMembers(final JSONObject messageReceived) {


        /*
         * Have to fetch the member details for the group in which message has been received,but it emmbers doesn't exist locally as of now
         */

        try {
            final String groupId = messageReceived.getString("groupId");

            JSONObject object = new JSONObject();
            try {
                object.put("chatId", groupId);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonObjReq =
                    new JsonObjectRequest(Request.Method.GET, ApiOnServer.GROUP_MEMBER + "?chatId=" + groupId,
                            object, new com.android.volley.Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(final JSONObject response) {

                            try {

                                if (response.getInt("code") == 200) {
                                    updateGroupMembersFromApi(response.getJSONObject("data"), groupId,
                                            messageReceived);
                                }
                            } catch (JSONException e) {

                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
                                SessionObserver sessionObserver = new SessionObserver();
                                sessionObserver.getObservable()
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new DisposableObserver<Boolean>() {
                                            @Override
                                            public void onNext(Boolean flag) {
                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        requestGroupMembers(messageReceived);
                                                    }
                                                }, 1000);
                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                            }

                                            @Override
                                            public void onComplete() {
                                            }
                                        });
                                sessionApiCall.getNewSession(sessionObserver);
                            }
                        }
                    }

                    ) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("authorization", AppController.getInstance().getApiToken());
                            headers.put("lang", Constants.LANGUAGE);

                            return headers;
                        }
                    };

            jsonObjReq.setRetryPolicy(
                    new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            /* Add the request to the RequestQueue.*/
            AppController.getInstance().addToRequestQueue(jsonObjReq, "fetchMembersApiRequest");
        } catch (JSONException e) {

        }
    }

    /**
     * @param groupData JSONObject containing the group  data which included member details and
     *                  created by whom and created at details
     */
    @SuppressWarnings("unchecked")
    private void updateGroupMembersFromApi(JSONObject groupData, String groupId,
                                           final JSONObject messageReceived) {
        try {

            try {

                JSONArray members = groupData.getJSONArray("members");
                JSONObject member;

                Map<String, Object> memberMap;

                boolean isActive = false;
                String userId = AppController.getInstance().getUserId();
                ArrayList<Map<String, Object>> membersArray = new ArrayList<>();

                /*
                 *To avoid creation of multiple documents for the group members corresponding to the single group chat
                 */

                CouchDbController db = instance.getDbController();
                String groupMembersDocId = db.createGroupMembersDocument();
                db.addGroupChat(AppController.getInstance().getGroupChatsDocId(), groupId,
                        groupMembersDocId);

                for (int i = 0; i < members.length(); i++) {

                    member = members.getJSONObject(i);
                    memberMap = new HashMap<>();

                    memberMap.put("memberId", member.getString("userId"));

                    if (userId.equals(member.getString("userId"))) {

                        isActive = true;
                    }

                    memberMap.put("memberIdentifier", member.getString("userIdentifier"));

                    if (member.has("profilePic")) {
                        memberMap.put("memberImage", member.getString("profilePic"));
                    } else {

                        memberMap.put("memberImage", "");
                    }

                    if (member.has("isStar")) {

                        memberMap.put("memberIsStar", member.getBoolean("isStar"));
                    } else {

                        memberMap.put("memberIsStar", false);
                    }
                    if (member.has("socialStatus")) {
                        memberMap.put("memberStatus", member.getString("socialStatus"));
                    } else {

                        memberMap.put("memberStatus", instance.getString(R.string.default_status));
                    }

                    memberMap.put("memberIsAdmin", member.getBoolean("isAdmin"));
                    membersArray.add(memberMap);
                }

                AppController.getInstance()
                        .getDbController()
                        .addGroupMembersDetails(groupMembersDocId, membersArray,
                                groupData.getString("createdByMemberId"),
                                groupData.getString("createdByMemberIdentifier"), groupData.getLong("createdAt"),
                                isActive);
            } catch (JSONException e) {

            }


            /*
             * Have been intentionally put on the ui thread
             */
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {

                    processGroupMessageReceived(messageReceived);
                }
            });
        } catch (Exception e) {

        }
    }
}
