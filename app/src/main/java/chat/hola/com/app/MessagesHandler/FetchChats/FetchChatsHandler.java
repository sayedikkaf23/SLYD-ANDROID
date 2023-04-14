package chat.hola.com.app.MessagesHandler.FetchChats;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.ezcall.android.R;
import com.squareup.otto.Bus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.Utilities.Utilities;

import static chat.hola.com.app.AppController.findDocumentIdOfReceiver;

public class FetchChatsHandler {

    private AppController instance = AppController.getInstance();

    public void handleFetchChatResult(JSONObject obj) {
        /*
         * To fetch the list of the chats
         */
        try {

            String userId = instance.getUserId();
            CouchDbController db = instance.getDbController();
            String[] dTimeForDB = instance.getdTimeForDB();
            Bus bus = AppController.getBus();
            String[] dTimeOptions = instance.getdTimeOptions();


            String topic = obj.getString("topic");

            JSONArray chats = obj.getJSONArray("chats");



            /*
             *New flag names as groupChat has been added to identify if the chat is the group chat
             */

            String tsInGmt, receiverName;
            JSONObject jsonObject;
            Map<String, Object> contactInfo;
            boolean wasInvited;

            int totalUnreadMessageCount;

            boolean hasNewMessage, isSelf;
            String profilePic;
            boolean isStar;

            for (int j = 0; j < chats.length(); j++) {

                jsonObject = chats.getJSONObject(j);

                if (jsonObject.getBoolean("groupChat")) {
                    /*
                     * For the group chat,will have seperate way of handling chatlist
                     */

                    if (jsonObject.getString("senderId").equals(userId)) {
                        isSelf = true;
                    } else {
                        isSelf = false;
                    }

                    totalUnreadMessageCount = jsonObject.getInt("totalUnread");

                    hasNewMessage = totalUnreadMessageCount > 0;
                    tsInGmt = Utilities.epochtoGmt(String.valueOf(jsonObject.getLong("timestamp")));


                    /*
                     *
                     *This is equal to the groupName
                     */
                    receiverName = jsonObject.getString("userName");

                    /*

                     * For Group Members
                     *
                     * */
                    JSONArray groupMembers = new JSONArray();

                    if (jsonObject.has("groupMembersInfo")) {

                         groupMembers = jsonObject.getJSONArray("groupMembersInfo");

                    }

                    Log.e("GroupMemberInfoMQTT","====>>"+groupMembers);


                    /*
                     * For the group image
                     */
                    if (jsonObject.has("profilePic") && jsonObject.getString("profilePic") != null) {
                        profilePic = jsonObject.getString("profilePic");
                    } else {
                        profilePic = "";
                    }

                    String documentId = AppController.getInstance().
                            findDocumentIdOfReceiver(jsonObject.getString("chatId"), "");

                    if (documentId.isEmpty()) {

                        documentId = findDocumentIdOfReceiver(jsonObject.getString("chatId"), tsInGmt,
                                receiverName, profilePic, "", false, jsonObject.getString("chatId"),
                                jsonObject.getString("chatId"), true, false);
                    } else {
                        /*
                         * If doc already exists then have to update the corresponding chat id
                         */

                        db.updateChatId(documentId, jsonObject.getString("chatId"));
                    }

                    if (jsonObject.has("payload")) {
                        /*
                         * Last message has been a normal group chat message
                         */

                        switch (Integer.parseInt(jsonObject.getString("messageType"))) {

                            case 0:

                                String text = "";
                                try {

                                    text = new String(
                                            Base64.decode(jsonObject.getString("payload"), Base64.DEFAULT),
                                            "UTF-8");
                                } catch (UnsupportedEncodingException e) {

                                }

                                if (text.trim().isEmpty()) {

                                    if (jsonObject.getLong("dTime") != -1) {
                                        String message_dTime = String.valueOf(jsonObject.getLong("dTime"));

                                        for (int i = 0; i < dTimeForDB.length; i++) {
                                            if (message_dTime.equals(dTimeForDB[i])) {

                                                if (i == 0) {

                                                    text = instance.getString(R.string.Timer_set_off);
                                                } else {

                                                    text = instance.getString(R.string.Timer_set_to) + " " + dTimeOptions[i];
                                                }
                                                break;
                                            }
                                        }

                                        db.updateChatListForNewMessageFromHistory(documentId, text,
                                                hasNewMessage, tsInGmt, tsInGmt, totalUnreadMessageCount, isSelf,
                                                jsonObject.getInt("status"),groupMembers);
                                    } else {

                                        if (jsonObject.getString("senderId").equals(userId)) {

                                            text = instance.getResources().getString(R.string.YouInvited)
                                                    + " "
                                                    + receiverName
                                                    + " "
                                                    + instance.getResources().getString(R.string.JoinSecretChat);
                                        } else {

                                            text = instance.getResources().getString(R.string.youAreInvited)
                                                    + " "
                                                    + receiverName
                                                    + " "
                                                    + instance.getResources().getString(R.string.JoinSecretChat);
                                        }

                                        db.updateChatListForNewMessageFromHistory(documentId, text,
                                                hasNewMessage, tsInGmt, tsInGmt, totalUnreadMessageCount, false,
                                                jsonObject.getInt("status"),groupMembers);
                                    }
                                } else {
                                    db.updateChatListForNewMessageFromHistory(documentId, text, hasNewMessage,
                                            tsInGmt, tsInGmt, totalUnreadMessageCount, isSelf,
                                            jsonObject.getInt("status"),groupMembers);
                                }
                                break;
                            case 1:
                                /*
                                 * Image message
                                 */

                                if (totalUnreadMessageCount > 0) {
                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.NewImage), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                } else {
                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.Image), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                }
                                break;
                            case 2:

                                /*
                                 * Video message
                                 */
                                if (totalUnreadMessageCount > 0) {

                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.NewVideo), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                } else {
                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.Video), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                }

                                break;
                            case 3:
                                /*
                                 * Location message
                                 */
                                if (totalUnreadMessageCount > 0) {

                                    db.updateChatListForNewMessageFromHistory(

                                            documentId, instance.getString(R.string.NewLocation), hasNewMessage, tsInGmt,
                                            tsInGmt, totalUnreadMessageCount, isSelf,
                                            jsonObject.getInt("status"),groupMembers);
                                } else {

                                    db.updateChatListForNewMessageFromHistory(

                                            documentId, instance.getString(R.string.Location), hasNewMessage, tsInGmt,
                                            tsInGmt, totalUnreadMessageCount, isSelf,
                                            jsonObject.getInt("status"),groupMembers);
                                }
                                break;
                            case 4:
                                /*
                                 * Follow message
                                 */

                                if (totalUnreadMessageCount > 0) {
                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.NewContact), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                } else {

                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.Contact), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                }
                                break;
                            case 5:

                                /*
                                 * Audio message
                                 */

                                if (totalUnreadMessageCount > 0) {
                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.NewAudio), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                } else {
                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.Audio), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                }
                                break;
                            case 6:


                                /*
                                 * Sticker
                                 */
                                if (totalUnreadMessageCount > 0) {

                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.NewSticker), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                } else {

                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.Stickers), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                }
                                break;
                            case 7:
                                /*
                                 * Doodle
                                 */
                                if (totalUnreadMessageCount > 0) {

                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.NewDoodle), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                } else {

                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.Doodle), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                }

                                break;
                            case 8:
                                /*
                                 * Gif
                                 */
                                if (totalUnreadMessageCount > 0) {
                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.NewGiphy), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                } else {

                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.Giphy), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                }
                                break;

                            case 9:
                                /*
                                 * Document
                                 */
                                if (totalUnreadMessageCount > 0) {
                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.NewDocument), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                } else {

                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.Document), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                }
                                break;

                            case 10: {

                                switch (Integer.parseInt(jsonObject.getString("replyType"))) {
                                    case 0:

                                        String text2 = "";
                                        try {

                                            text2 = new String(
                                                    Base64.decode(jsonObject.getString("payload"), Base64.DEFAULT),
                                                    "UTF-8");
                                        } catch (UnsupportedEncodingException e) {

                                        }

                                        db.updateChatListForNewMessageFromHistory(documentId, text2,
                                                hasNewMessage, tsInGmt, tsInGmt, totalUnreadMessageCount, isSelf,
                                                jsonObject.getInt("status"),groupMembers);

                                        break;
                                    case 1:
                                        /*
                                         * receiverImage message
                                         */

                                        if (totalUnreadMessageCount > 0) {

                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.NewImage), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        } else {
                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.Image), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        }
                                        break;
                                    case 2:

                                        /*
                                         * Video message
                                         */
                                        if (totalUnreadMessageCount > 0) {

                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.NewVideo), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        } else {
                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.Video), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        }

                                        break;
                                    case 3:
                                        /*
                                         * Location message
                                         */
                                        if (totalUnreadMessageCount > 0) {

                                            db.updateChatListForNewMessageFromHistory(

                                                    documentId, instance.getString(R.string.NewLocation), hasNewMessage,
                                                    tsInGmt, tsInGmt, totalUnreadMessageCount, isSelf,
                                                    jsonObject.getInt("status"),groupMembers);
                                        } else {

                                            db.updateChatListForNewMessageFromHistory(

                                                    documentId, instance.getString(R.string.Location), hasNewMessage, tsInGmt,
                                                    tsInGmt, totalUnreadMessageCount, isSelf,
                                                    jsonObject.getInt("status"),groupMembers);
                                        }
                                        break;
                                    case 4:
                                        /*
                                         * Follow message
                                         */

                                        if (totalUnreadMessageCount > 0) {
                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.NewContact), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        } else {

                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.Contact), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        }
                                        break;
                                    case 5:

                                        /*
                                         * Audio message
                                         */

                                        if (totalUnreadMessageCount > 0) {
                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.NewAudio), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        } else {
                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.Audio), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        }
                                        break;
                                    case 6:


                                        /*
                                         * Sticker
                                         */
                                        if (totalUnreadMessageCount > 0) {

                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.NewSticker), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        } else {

                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.Stickers), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        }
                                        break;
                                    case 7:
                                        /*
                                         * Doodle
                                         */
                                        if (totalUnreadMessageCount > 0) {

                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.NewDoodle), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        } else {

                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.Doodle), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        }

                                        break;
                                    case 8:
                                        /*
                                         * Gif
                                         */
                                        if (totalUnreadMessageCount > 0) {
                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.NewGiphy), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        } else {

                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.Giphy), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        }
                                        break;

                                    case 9:
                                        /*
                                         * Document
                                         */
                                        if (totalUnreadMessageCount > 0) {
                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.NewDocument), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        } else {

                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.Document), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        }
                                        break;

                                    case 13:
                                        /*
                                         * Post
                                         */
                                        if (totalUnreadMessageCount > 0) {
                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.NewPost), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        } else {

                                            db.updateChatListForNewMessageFromHistory(documentId,
                                                    instance.getString(R.string.Post), hasNewMessage, tsInGmt, tsInGmt,
                                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                        }
                                        break;
                                }

                                break;
                            }

                            case 11: {

                                String text2 = "";
                                try {

                                    text2 = new String(
                                            Base64.decode(jsonObject.getString("payload"), Base64.DEFAULT),
                                            "UTF-8");
                                } catch (UnsupportedEncodingException e) {

                                }

                                //  db.updateChatListForNewMessageFromHistory(documentId, text2, hasNewMessage, tsInGmt, tsInGmt, totalUnreadMessageCount, isSelf, jsonObject.getInt("status"));

                                db.updateChatListForNewMessageFromHistory(documentId, text2, hasNewMessage,
                                        Utilities.epochtoGmt(jsonObject.getString("removedAt")),
                                        Utilities.epochtoGmt(jsonObject.getString("removedAt")),
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);

                                break;
                            }
                            case 13:
                                /*
                                 * Post
                                 */
                                if (totalUnreadMessageCount > 0) {
                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.NewPost), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                } else {

                                    db.updateChatListForNewMessageFromHistory(documentId,
                                            instance.getString(R.string.Post), hasNewMessage, tsInGmt, tsInGmt,
                                            totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                }
                                break;
                        }
                    } else {


                        /*
                         * Last message has been the group tag message in the corresponding group chat
                         */

                        Log.e("GroupMemberInfoMQTYPE","====>>"+Integer.parseInt(jsonObject.getString("messageType")));

                        switch (Integer.parseInt(jsonObject.getString("messageType"))) {
                            case 0: {
                                /*
                                 * Group created
                                 *
                                 */

                                String initiatorName;

                                if (jsonObject.getString("initiatorId")
                                        .equals(AppController.getInstance().getUserId())) {

                                    initiatorName = instance.getString(R.string.You);
                                } else {
                                    initiatorName =
                                            db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                    jsonObject.getString("initiatorId"));
                                    if (initiatorName == null) {

                                        initiatorName = jsonObject.getString("initiatorIdentifier");
                                    }
                                }

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.CreatedGroup, initiatorName)
                                                + " "
                                                + jsonObject.getString("groupSubject"), hasNewMessage, tsInGmt,
                                        tsInGmt, totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);

                                break;
                            }

                            case 1: {

                                /*
                                 * Member added
                                 */

                                Log.e("GroupMemberInfoMQADD","====>>"+groupMembers);


                                String initiatorName, memberName;

                                if (jsonObject.getString("initiatorId")
                                        .equals(AppController.getInstance().getUserId())) {

                                    initiatorName = instance.getString(R.string.You);
                                } else {
                                    initiatorName =
                                            db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                    jsonObject.getString("initiatorId"));
                                    if (initiatorName == null) {

                                        initiatorName = jsonObject.getString("initiatorIdentifier");
                                    }
                                }

                                if (jsonObject.getString("memberId")
                                        .equals(AppController.getInstance().getUserId())) {

                                    memberName = instance.getString(R.string.YouSmall);
                                } else {
                                    memberName =
                                            db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                    jsonObject.getString("memberId"));
                                    if (memberName == null) {

                                        memberName = jsonObject.getString("memberIdentifier");
                                    }
                                }

                                db.updateChatListForNewMessageFromHistory(documentId, initiatorName
                                                + " "
                                                + instance.getString(R.string.AddedMember, memberName)
                                                + " "
                                                + instance.getString(R.string.ToGroup), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                break;
                            }
                            case 2: {
                                /*
                                 * Member removed
                                 *
                                 */

                                String initiatorName, memberName;

                                if (jsonObject.getString("initiatorId")
                                        .equals(AppController.getInstance().getUserId())) {

                                    initiatorName = instance.getString(R.string.You);
                                } else {
                                    initiatorName =
                                            db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                    jsonObject.getString("initiatorId"));
                                    if (initiatorName == null) {

                                        initiatorName = jsonObject.getString("initiatorIdentifier");
                                    }
                                }

                                if (jsonObject.getString("memberId")
                                        .equals(AppController.getInstance().getUserId())) {

                                    memberName = instance.getString(R.string.YouSmall);
                                } else {
                                    memberName =
                                            db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                    jsonObject.getString("memberId"));
                                    if (memberName == null) {

                                        memberName = jsonObject.getString("memberIdentifier");
                                    }
                                }

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        initiatorName + " " + instance.getString(R.string.Removed) + " " + memberName,
                                        hasNewMessage, tsInGmt, tsInGmt, totalUnreadMessageCount, isSelf,
                                        jsonObject.getInt("status"),groupMembers);
                                break;
                            }
                            case 3: {
                                /*
                                 * Made admin
                                 *
                                 */

                                String initiatorName, memberName;

                                if (jsonObject.getString("initiatorId")
                                        .equals(AppController.getInstance().getUserId())) {

                                    initiatorName = instance.getString(R.string.You);
                                } else {
                                    initiatorName =
                                            db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                    jsonObject.getString("initiatorId"));
                                    if (initiatorName == null) {

                                        initiatorName = jsonObject.getString("initiatorIdentifier");
                                    }
                                }

                                if (jsonObject.getString("memberId")
                                        .equals(AppController.getInstance().getUserId())) {

                                    memberName = instance.getString(R.string.YouSmall);
                                } else {
                                    memberName =
                                            db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                    jsonObject.getString("memberId"));
                                    if (memberName == null) {

                                        memberName = jsonObject.getString("memberIdentifier");
                                    }
                                }

                                db.updateChatListForNewMessageFromHistory(documentId, initiatorName
                                                + " "
                                                + instance.getString(R.string.Made)
                                                + " "
                                                + memberName
                                                + " "
                                                + instance.getString(R.string.MakeAdmin), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                break;
                            }
                            case 4: {
                                /*
                                 * Group name updated
                                 *
                                 */

                                String initiatorName;

                                if (jsonObject.getString("initiatorId")
                                        .equals(AppController.getInstance().getUserId())) {

                                    initiatorName = instance.getString(R.string.You);
                                } else {
                                    initiatorName =
                                            db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                    jsonObject.getString("initiatorId"));
                                    if (initiatorName == null) {

                                        initiatorName = jsonObject.getString("initiatorIdentifier");
                                    }
                                }

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        initiatorName + " " + instance.getString(R.string.UpdatedGroupSubject,
                                                jsonObject.getString("groupSubject")), hasNewMessage, tsInGmt,

                                        tsInGmt, totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                break;
                            }
                            case 5: {
                                /*
                                 * Group icon updated
                                 *
                                 */

                                String initiatorName;

                                if (jsonObject.getString("initiatorId")
                                        .equals(AppController.getInstance().getUserId())) {

                                    initiatorName = instance.getString(R.string.You);
                                } else {
                                    initiatorName =
                                            db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                    jsonObject.getString("initiatorId"));
                                    if (initiatorName == null) {

                                        initiatorName = jsonObject.getString("initiatorIdentifier");
                                    }
                                }

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        initiatorName + " " + instance.getString(R.string.UpdatedGroupIcon),
                                        hasNewMessage, tsInGmt, tsInGmt, totalUnreadMessageCount, isSelf,
                                        jsonObject.getInt("status"),groupMembers);
                                break;
                            }

                            case 6: {

                                /*
                                 *Member left the group
                                 */
                                String memberName;

                                if (jsonObject.getString("initiatorId")
                                        .equals(AppController.getInstance().getUserId())) {

                                    memberName = instance.getString(R.string.You);
                                } else {
                                    memberName =
                                            db.getFriendName(AppController.getInstance().getFriendsDocId(),
                                                    jsonObject.getString("initiatorId"));
                                    if (memberName == null) {

                                        memberName = jsonObject.getString("initiatorIdentifier");
                                    }
                                }

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.LeftGroup, memberName), hasNewMessage, tsInGmt,
                                        tsInGmt, totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                break;
                            }
                        }
                    }
                } else {
                    /*
                     * For the normal or the secret chat
                     */
                    if (jsonObject.getString("senderId").equals(userId)) {
                        isSelf = true;
                    } else {
                        isSelf = false;
                    }

                    totalUnreadMessageCount = jsonObject.getInt("totalUnread");

                    hasNewMessage = totalUnreadMessageCount > 0;
                    tsInGmt = Utilities.epochtoGmt(String.valueOf(jsonObject.getLong("timestamp")));

                    contactInfo

                            = db.getFriendInfoFromUid(AppController.getInstance().getFriendsDocId(),
                            jsonObject.getString("recipientId"));

                    /*

                     * For Group Members
                     *
                     * */
                    JSONArray groupMembers = new JSONArray();

                    if (jsonObject.has("groupMembersInfo")) {

                        groupMembers = jsonObject.getJSONArray("groupMembersInfo");

                    }

                    Log.e("GroupMemberInfoMQTT","====>>"+groupMembers);

                    if (contactInfo != null) {
                        receiverName = contactInfo.get("firstName") + " " + contactInfo.get("lastName");
                    } else {


                        /*
                         * If userId doesn't exists in contact
                         */
                        //                                        receiverName = jsonObject.getString("number");
                        receiverName = jsonObject.getString("userName");
                    }

                    if (jsonObject.has("profilePic")) {
                        profilePic = jsonObject.getString("profilePic");
                    } else {

                        profilePic = "";
                    }

                    if (jsonObject.has("isStar")) {
                        isStar = jsonObject.getBoolean("isStar");
                    } else {
                        isStar = false;
                    }

                    String documentId = AppController.getInstance().
                            findDocumentIdOfReceiver(jsonObject.getString("recipientId"),
                                    jsonObject.getString("secretId"));

                    if (documentId.isEmpty()) {

                        wasInvited = !jsonObject.getString("senderId").equals(userId);
                        //                                        documentId = findDocumentIdOfReceiver(jsonObject.getString("recipientId"),
                        //                                                tsInGmt, receiverName, profilePic, jsonObject.getString("secretId"),
                        //                                                wasInvited, jsonObject.getString("number"), jsonObject.getString("chatId"), false);
                        documentId =
                                findDocumentIdOfReceiver(jsonObject.getString("recipientId"), tsInGmt,
                                        receiverName, profilePic, jsonObject.getString("secretId"), wasInvited,
                                        jsonObject.getString("userName"), jsonObject.getString("chatId"), false,
                                        isStar);
                    } else {
                        /*
                         * If doc already exists then have to update the corresponding chat id
                         */

                        db.updateChatId(documentId, jsonObject.getString("chatId"));
                    }
                    if (!jsonObject.getString("secretId").isEmpty()) {

                        db.updateDestructTime(documentId, jsonObject.getLong("dTime"));
                    }

                    switch (Integer.parseInt(jsonObject.getString("messageType"))) {

                        case 0:

                            String text = "";
                            try {

                                text = new String(
                                        Base64.decode(jsonObject.getString("payload"), Base64.DEFAULT),
                                        "UTF-8");
                            } catch (UnsupportedEncodingException e) {

                            }

                            if (text.trim().isEmpty()) {

                                if (jsonObject.getLong("dTime") != -1) {
                                    String message_dTime = String.valueOf(jsonObject.getLong("dTime"));

                                    for (int i = 0; i < dTimeForDB.length; i++) {
                                        if (message_dTime.equals(dTimeForDB[i])) {

                                            if (i == 0) {

                                                text = instance.getString(R.string.Timer_set_off);
                                            } else {

                                                text = instance.getString(R.string.Timer_set_to) + " " + dTimeOptions[i];
                                            }
                                            break;
                                        }
                                    }

                                    db.updateChatListForNewMessageFromHistory(documentId, text, hasNewMessage,
                                            tsInGmt, tsInGmt, totalUnreadMessageCount, isSelf,
                                            jsonObject.getInt("status"),groupMembers);
                                } else {

                                    if (jsonObject.getString("senderId").equals(userId)) {

                                        text = instance.getResources().getString(R.string.YouInvited)
                                                + " "
                                                + receiverName
                                                + " "
                                                + instance.getResources().getString(R.string.JoinSecretChat);
                                    } else {

                                        text = instance.getResources().getString(R.string.youAreInvited)
                                                + " "
                                                + receiverName
                                                + " "
                                                + instance.getResources().getString(R.string.JoinSecretChat);
                                    }

                                    db.updateChatListForNewMessageFromHistory(documentId, text, hasNewMessage,
                                            tsInGmt, tsInGmt, totalUnreadMessageCount, false,
                                            jsonObject.getInt("status"),groupMembers);
                                }
                            } else {
                                db.updateChatListForNewMessageFromHistory(documentId, text, hasNewMessage,
                                        tsInGmt, tsInGmt, totalUnreadMessageCount, isSelf,
                                        jsonObject.getInt("status"),groupMembers);
                            }
                            break;
                        case 1:
                            /*
                             * Image message
                             */

                            if (totalUnreadMessageCount > 0) {

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.NewImage), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            } else {
                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.Image), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            }
                            break;
                        case 2:

                            /*
                             * Video message
                             */
                            if (totalUnreadMessageCount > 0) {

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.NewVideo), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            } else {
                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.Video), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            }

                            break;
                        case 3:
                            /*
                             * Location message
                             */
                            if (totalUnreadMessageCount > 0) {

                                db.updateChatListForNewMessageFromHistory(

                                        documentId, instance.getString(R.string.NewLocation), hasNewMessage, tsInGmt,
                                        tsInGmt, totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            } else {

                                db.updateChatListForNewMessageFromHistory(

                                        documentId, instance.getString(R.string.Location), hasNewMessage, tsInGmt,
                                        tsInGmt, totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            }
                            break;
                        case 4:
                            /*
                             * Follow message
                             */

                            if (totalUnreadMessageCount > 0) {
                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.NewContact), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            } else {

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.Contact), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            }
                            break;
                        case 5:

                            /*
                             * Audio message
                             */

                            if (totalUnreadMessageCount > 0) {
                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.NewAudio), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            } else {
                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.Audio), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            }
                            break;
                        case 6:


                            /*
                             * Sticker
                             */
                            if (totalUnreadMessageCount > 0) {

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.NewSticker), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            } else {

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.Stickers), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            }
                            break;
                        case 7:
                            /*
                             * Doodle
                             */
                            if (totalUnreadMessageCount > 0) {

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.NewDoodle), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            } else {

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.Doodle), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            }

                            break;
                        case 8:
                            /*
                             * Gif
                             */
                            if (totalUnreadMessageCount > 0) {
                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.NewGiphy), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            } else {

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.Giphy), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            }
                            break;

                        case 9:
                            /*
                             * Document
                             */
                            if (totalUnreadMessageCount > 0) {
                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.NewDocument), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            } else {

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.Document), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            }
                            break;

                        case 10: {

                            switch (Integer.parseInt(jsonObject.getString("replyType"))) {
                                case 0:

                                    String text2 = "";
                                    try {

                                        text2 = new String(
                                                Base64.decode(jsonObject.getString("payload"), Base64.DEFAULT),
                                                "UTF-8");
                                    } catch (UnsupportedEncodingException e) {

                                    }

                                    db.updateChatListForNewMessageFromHistory(documentId, text2,
                                            hasNewMessage, tsInGmt, tsInGmt, totalUnreadMessageCount, isSelf,
                                            jsonObject.getInt("status"),groupMembers);

                                    break;
                                case 1:
                                    /*
                                     * receiverImage message
                                     */

                                    if (totalUnreadMessageCount > 0) {

                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.NewImage), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    } else {
                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.Image), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    }
                                    break;
                                case 2:

                                    /*
                                     * Video message
                                     */
                                    if (totalUnreadMessageCount > 0) {

                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.NewVideo), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    } else {
                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.Video), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    }

                                    break;
                                case 3:
                                    /*
                                     * Location message
                                     */
                                    if (totalUnreadMessageCount > 0) {

                                        db.updateChatListForNewMessageFromHistory(

                                                documentId, instance.getString(R.string.NewLocation), hasNewMessage, tsInGmt,
                                                tsInGmt, totalUnreadMessageCount, isSelf,
                                                jsonObject.getInt("status"),groupMembers);
                                    } else {

                                        db.updateChatListForNewMessageFromHistory(

                                                documentId, instance.getString(R.string.Location), hasNewMessage, tsInGmt,
                                                tsInGmt, totalUnreadMessageCount, isSelf,
                                                jsonObject.getInt("status"),groupMembers);
                                    }
                                    break;
                                case 4:
                                    /*
                                     * Follow message
                                     */

                                    if (totalUnreadMessageCount > 0) {
                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.NewContact), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    } else {

                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.Contact), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    }
                                    break;
                                case 5:

                                    /*
                                     * Audio message
                                     */

                                    if (totalUnreadMessageCount > 0) {
                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.NewAudio), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    } else {
                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.Audio), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    }
                                    break;
                                case 6:


                                    /*
                                     * Sticker
                                     */
                                    if (totalUnreadMessageCount > 0) {

                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.NewSticker), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    } else {

                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.Stickers), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    }
                                    break;
                                case 7:
                                    /*
                                     * Doodle
                                     */
                                    if (totalUnreadMessageCount > 0) {

                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.NewDoodle), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    } else {

                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.Doodle), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    }

                                    break;
                                case 8:
                                    /*
                                     * Gif
                                     */
                                    if (totalUnreadMessageCount > 0) {
                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.NewGiphy), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    } else {

                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.Giphy), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    }
                                    break;

                                case 9:
                                    /*
                                     * Document
                                     */
                                    if (totalUnreadMessageCount > 0) {
                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.NewDocument), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    } else {

                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.Document), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    }
                                    break;

                                case 13:
                                    /*
                                     * Post
                                     */
                                    if (totalUnreadMessageCount > 0) {
                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.NewPost), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    } else {

                                        db.updateChatListForNewMessageFromHistory(documentId,
                                                instance.getString(R.string.Post), hasNewMessage, tsInGmt, tsInGmt,
                                                totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                                    }
                                    break;
                            }

                            break;
                        }

                        case 11: {
                            /*
                             * Removed message
                             */

                            String text2 = "";
                            try {

                                text2 = new String(
                                        Base64.decode(jsonObject.getString("payload"), Base64.DEFAULT),
                                        "UTF-8");
                            } catch (UnsupportedEncodingException e) {

                            }

                            db.updateChatListForNewMessageFromHistory(documentId, text2, hasNewMessage,
                                    Utilities.epochtoGmt(jsonObject.getString("removedAt")),
                                    Utilities.epochtoGmt(jsonObject.getString("removedAt")),
                                    totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);

                            break;
                        }

                        case 13:
                            /*
                             * Post
                             */
                            if (totalUnreadMessageCount > 0) {
                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.NewPost), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            } else {

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.Post), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            }
                            break;

                        case 15:
                            /*
                             * Transfer
                             */
                            if (totalUnreadMessageCount > 0) {
                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.new_transfer), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            } else {

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.transfer), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            }
                            break;

                        case 16:
                            /*
                             * Missed Call
                             */
                            if (totalUnreadMessageCount > 0) {
                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.missed_call), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            } else {

                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.missed_call), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            }
                            break;
                        case 17:
                            /*
                             * Call Message
                             */
                            if (totalUnreadMessageCount > 0) {
                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.call), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            } else {
                                db.updateChatListForNewMessageFromHistory(documentId,
                                        instance.getString(R.string.call), hasNewMessage, tsInGmt, tsInGmt,
                                        totalUnreadMessageCount, isSelf, jsonObject.getInt("status"),groupMembers);
                            }
                            break;
                    }
                }
            }
            obj.put("eventName", topic);
            bus.post(obj);
        } catch (JSONException e) {

        }
    }
}
