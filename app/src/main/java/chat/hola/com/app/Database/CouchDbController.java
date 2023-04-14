package chat.hola.com.app.Database;

/*
 * Created by moda on 25/3/16.
 */

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.android.AndroidContext;
import com.ezcall.android.R;

import org.json.JSONArray;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.MessageSorter;


/*
 * Database helper class for couchdb database
 */
public class CouchDbController {


    public static final String TAG = "CouchBaseEvents";
    public Database database;
    Manager manager;
    private String DB_NAME = "mqttdb";
    private AndroidContext a_context;


    public CouchDbController(AndroidContext a_context) {
        this.a_context = a_context;
        try {
            manager = getManagerInstance();
            database = getDatabaseInstance();
        } catch (Exception e) {
            Log.e(TAG, "Error getting database", e);
        }
    }


    /*
     *
     * To get existing db or create new db if db doesn't exists
     *
     * */
    private Database getDatabaseInstance() {
        if ((this.database == null) & (this.manager != null)) {
            try {
                this.database = manager.getExistingDatabase(DB_NAME);
                if (database == null) {
                    this.database = manager.getDatabase(DB_NAME);
                }

                database.open();
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error getting database", e);
            }
        }
        return database;
    }


    /**
     * to get instance of db manager
     */
    private Manager getManagerInstance() throws IOException {
        if (manager == null) {
            manager = new Manager(a_context, Manager.DEFAULT_OPTIONS);

        }
        return manager;
    }


    /*
     * To create the index document which contains docId for impressions and notificatioImpressions docs along with status of currently signed-in user and array of who all users have signed in on that device
     *
     * */

    @SuppressWarnings("unchecked")
    public String createIndexDocument() {


//


        Map<String, Object> map = new HashMap<>();

        database.clearDocumentCache();

        Document document = database.createDocument();
        ArrayList<String> arr_userName = new ArrayList<>();
        ArrayList<String> arr_userDocId = new ArrayList<>();

        map.put("userNameArray", arr_userName);
        map.put("userDocIdArray", arr_userDocId);


        map.put("isSignedIn", false);

        map.put("profileSaved", false);
        map.put("signInType", -1);


        map.put("signedUserId", null);


        try {

            document.putProperties(map);

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }


        database.clearDocumentCache();


        return document.getId();
    }


    /**
     * To add the user details to index doc whenever new user signs in
     */
    @SuppressWarnings("unchecked")
    public void addToIndexDocument(String docId, String userName, String userdocId) {


        database.clearDocumentCache();


        Document document = database.getDocument(docId);


        Map<String, Object> map_old = document.getProperties();


        if (map_old != null) {
            ArrayList<String> arrUserName = (ArrayList<String>) map_old.get("userNameArray");


            ArrayList<String> arrUserDocId = (ArrayList<String>) map_old.get("userDocIdArray");


            arrUserName.add(userName);

            arrUserDocId.add(userdocId);
            Map<String, Object> map_temp = new HashMap<>();
            map_temp.putAll(map_old);


            map_temp.put("userNameArray", arrUserName);

            map_temp.put("userDocIdArray", arrUserDocId);
            try {
                document.putProperties(map_temp);
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error putting", e);
            }


        }

        database.clearDocumentCache();


    }


    /*
     * To get the user Info docId from the index docId with help of its userId(althought it is written username here but actually it is userId)
     *
     * */

    @SuppressWarnings("unchecked")
    public String getUserInformationDocumentId(String indexDocId, String userName) {
        new CreateSearchIndexes().execute();

        String userDocId = "";

        database.clearDocumentCache();


        Document document = database.getDocument(indexDocId);

        Map<String, Object> map = document.getProperties();


        if (map != null) {


            ArrayList<String> arr = (ArrayList<String>) map.get("userNameArray");

            for (int i = arr.size() - 1; i >= 0; i--) {


                if (arr.get(i).equals(userName)) {


                    userDocId = ((ArrayList<String>) map.get("userDocIdArray")).get(i);


                    break;
                }

            }


        }


        database.clearDocumentCache();


        return userDocId;


    }

    /*
     *
     * To check if index doc already contains info of current user
     * */
    @SuppressWarnings("unchecked")
    public boolean checkUserDocExists(String docId, String userName) {

        boolean exists = false;

        database.clearDocumentCache();


        Document document = database.getDocument(docId);

        Map<String, Object> map = document.getProperties();


        if (map != null) {


            ArrayList<String> arr = (ArrayList<String>) map.get("userNameArray");

            for (int i = arr.size() - 1; i >= 0; i--) {


                if (arr.get(i).equals(userName)) {


                    exists = true;


                    break;
                }

            }


        }


        database.clearDocumentCache();


        return exists;
    }


    /**
     * to be created when user signsup or logsin for the first time
     */
    public String createUserInformationDocument(Map<String, Object> map1) {

        Map<String, Object> map = new HashMap<>();

        database.clearDocumentCache();


        Document document = database.createDocument();

        map.put("excludedFilterIds", map1.get("excludedFilterIds"));
        map.put("socialStatus", map1.get("socialStatus"));

        map.put("userIdentifier", map1.get("userIdentifier"));
        map.put("userId", map1.get("userId"));
        map.put("userName", map1.get("userName"));
        map.put("userImageUrl", map1.get("userImageUrl"));

        map.put("apiToken", map1.get("apiToken"));


        map.put("userLoginType", map1.get("userLoginType"));


        map.put("chatDocument", createChatDocument());


        map.put("unsentMessagesDocument", createUnsentMessagesDocument());


        map.put("ImqttTokenDocument", createMqttTokenToMessageIdMappingDocument());

//        map.put("unsentAcksDocument", createAcksDoc());

        map.put("callsDocument", createCallsDocument());
        map.put("contactsDocument", createContactsDocument());
        map.put("friendsDocument", createFriendsDocument());

        map.put("allContactsDocument", createAllContactsDocument());

        /*
         * For storing of the manually added status locally
         */
        map.put("statusDocument", createStatusDocument());


        map.put("notificationDocument", createNotificationDocument());


        /*
         * For storing the groupchat members info
         */
        map.put("groupChatsDocument", createGroupChatsDocument());


        /*
         *For the mute notifications functionality
         */
        map.put("mutedChatsDocument", createMuteChatsDocument());


        /*
         *For the blocked users functionality
         */
        map.put("blockedUsersDocument", createBlockedUsersDocument());

        map.put("viewPostDocument", createViewPostDocument());

        //map.put("activeTimersDocument", createActiveTimersDocument());
        try {


            document.putProperties(map);

        } catch (CouchbaseLiteException e) {
            com.couchbase.lite.util.Log.e(TAG, "Error putting", e);
        }


        return document.getId();
    }

    public String createFriendsDocument() {
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<>();

        map.put("friendsArray", new ArrayList<Map<String, String>>());
        try {
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }


    /**
     * To get list of all docIds for a particular user
     */
    public ArrayList<String> getUserDocIds(String docId) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        ArrayList<String> arr = new ArrayList<>();

        if (map != null) {


            arr.add((String) map.get("chatDocument"));


            arr.add((String) map.get("unsentMessagesDocument"));

            /*
             *Actually we don't need to save the entire IMQttToken for each message,but just messageId
             * is enough but still not changed yet due to shortage of time,will change later.
             */


            arr.add((String) map.get("ImqttTokenDocument"));

//            arr.add((String) map.get("unsentAcksDocument"));
            arr.add((String) map.get("callsDocument"));
            arr.add((String) map.get("contactsDocument"));
            arr.add((String) map.get("allContactsDocument"));


            arr.add((String) map.get("statusDocument"));

            arr.add((String) map.get("notificationDocument"));


            arr.add((String) map.get("groupChatsDocument"));


            arr.add((String) map.get("mutedChatsDocument"));

            arr.add((String) map.get("blockedUsersDocument"));

            arr.add((String) map.get("friendsDocument"));

            arr.add((String) map.get("viewPostDocument"));


            //    arr.add((String) map.get("activeTimersDocument"));

        }


        database.clearDocumentCache();
        return arr;
    }


    /**
     * To get the user info from his docId
     */
    public Map<String, Object> getUserInfo(String docId) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        Map<String, Object> map = document.getProperties();


        Map<String, Object> userInfo = new HashMap<>();


        if (map != null) {
            userInfo.put("excludedFilterIds", map.get("excludedFilterIds"));
            userInfo.put("socialStatus", map.get("socialStatus"));
            userInfo.put("userId", map.get("userId"));
            userInfo.put("userName", map.get("userName"));
            userInfo.put("userImageUrl", map.get("userImageUrl"));
            userInfo.put("userIdentifier", map.get("userIdentifier"));
            userInfo.put("apiToken", map.get("apiToken"));
        }


        database.clearDocumentCache();
        return userInfo;
    }


    /*
     * **************************************************
     * chatDatabaseStructure
     ****************************************************/

    /**
     * To create the doc to contain the messages until they have been delivered to the server and on getting response from the server they are deleted.Unsent messages from this doc are sent sutomatically once socket connects again when internet is back
     */
    private String createUnsentMessagesDocument() {
        database.clearDocumentCache();
        Document document = database.createDocument();
        Map<String, Object> map = new HashMap<>();

        ArrayList<Map<String, Object>> arr = new ArrayList<>();

        map.put("unsentMessageArray", arr);
        try {
            document.putProperties(map);

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }


        database.clearDocumentCache();


        return document.getId();


    }


    /**
     * To delete a given chat
     */

    @SuppressWarnings("unchecked")
    public void deleteParticularChatDetail(String docId, String chatDocId, boolean isGroupChat, String groupMembersDocId) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();

            if (map != null) {


                ArrayList<String> receiverDocIdArray = (ArrayList<String>) map.get("receiverDocIdArray");
                for (int i = 0; i < receiverDocIdArray.size(); i++) {
                    if (receiverDocIdArray.get(i).equals(chatDocId)) {


                        ArrayList<String> receiverUidArray = (ArrayList<String>) map.get("receiverUidArray");


                        ArrayList<String> secretIdArray = (ArrayList<String>) map.get("secretIdArray");

                        receiverDocIdArray.remove(i);
                        receiverUidArray.remove(i);
                        secretIdArray.remove(i);

                        Map<String, Object> map_temp = new HashMap<>();

                        map_temp.putAll(map);

                        map_temp.put("receiverUidArray", receiverUidArray);
                        map_temp.put("receiverDocIdArray", receiverDocIdArray);
                        map_temp.put("secretIdArray", secretIdArray);
                        try {
                            document.putProperties(map_temp);

                        } catch (CouchbaseLiteException e) {
                            com.couchbase.lite.util.Log.e(TAG, "Error putting", e);
                        }

                        try {


                            /*
                             * To delete the
                             */


                            database.getDocument(chatDocId).delete();
                            // database.deleteLocalDocument(chatDocId);


                        } catch (CouchbaseLiteException e) {
                            com.couchbase.lite.util.Log.e(TAG, "Error putting", e);
                        }
                        break;
                    }

                }


            }
            try {
                if (isGroupChat && groupMembersDocId != null) {


                    (database.getDocument(groupMembersDocId)).delete();


                    removeGroupChat(AppController.getInstance().getGroupChatsDocId(), groupMembersDocId);
                }

            } catch (CouchbaseLiteException e) {
                com.couchbase.lite.util.Log.e(TAG, "Error putting", e);
            }
        }


    }










    /*
     *
     * To create document containing all the chats info
     * */

    private String createChatDocument() {


        database.clearDocumentCache();

        Document document = database.createDocument();
        Map<String, Object> map = new HashMap<>();

//        ArrayList<String> arr1 = new ArrayList<>();
//        ArrayList<String> arr2 = new ArrayList<>();
//        ArrayList<String> arr3 = new ArrayList<>();

        map.put("receiverUidArray", new ArrayList<String>());
        map.put("receiverDocIdArray", new ArrayList<String>());

//        map.put("unreadChatsReceiverUidSet", new ArrayList<String>());

        /*
         * For the secret chat
         */

        map.put("secretIdArray", new ArrayList<String>());

        try {
            document.putProperties(map);

        } catch (CouchbaseLiteException e) {
            com.couchbase.lite.util.Log.e(TAG, "Error putting", e);
        }


        database.clearDocumentCache();
        return document.getId();
    }


    /**
     * To create document for a particular chat between any two of the users
     */
    @SuppressWarnings("unchecked")


    public String createDocumentForChat(String timeInGmt, String receiverUid, String receiverName, String
            receiverImage, String secretId, boolean invited, String receiverIdentifier, String chatId, boolean groupChat, boolean isStar) {


        database.clearDocumentCache();
        Document document = database.createDocument();
        String documentId = document.getId();


        Map<String, Object> map = new HashMap<>();


        map.put("messsageArray", new ArrayList<Map<String, Object>>());

        map.put("hasNewMessage", false);


        map.put("groupChat", groupChat);


        map.put("newMessage", "");
        map.put("newMessageTime", timeInGmt);
        map.put("newMessageDate", timeInGmt);
        map.put("newMessageCount", "0");

        /*
         *
         * last message date is required to show elements in chatlist in sorted order
         *
         * */
        map.put("lastMessageDate", timeInGmt);


        map.put("receiver_uid_array", new ArrayList<String>());
        map.put("receiver_docid_array", new ArrayList<String>());


        map.put("receiverIdentifier", receiverIdentifier);


        map.put("receiverName", receiverName);

        map.put("receiverImage", receiverImage);


        map.put("selfDocId", documentId);

        map.put("selfUid", receiverUid);


        map.put("firstDate", "Mon 07/Mar/2016");
        map.put("lastDate", "Mon 07/Mar/2016");


        /*
         * For secret chat
         *
         *
         */


        map.put("wasInvited", invited);
        map.put("secretId", secretId);
        map.put("dTime", -1L);
        map.put("secretInviteVisibility", true);
        if (!secretId.isEmpty()) {

            map.put("messageIdsArray", new ArrayList<String>());
        }

        /*
         * For fetching of the chat history
         */

        map.put("chatId", chatId);


        if (chatId.isEmpty()) {

            map.put("canHaveMoreMessages", false);
        } else {


            map.put("canHaveMoreMessages", true);
        }


        /*
         *For the wallpaper
         */


        map.put("wallpaperType", 1);

        /*
         * For Star
         */

        map.put("isStar", isStar);


        try {
            document.putProperties(map);

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }


        database.clearDocumentCache();


        return documentId;

    }


    /*
     * To fetch if the chat has messages stored locally only or the chat has been fetched from the server
     */
    public Map<String, Object> getChatInfo(String docId) {

        Map<String, Object> mapTemp = new HashMap<>();
        database.clearDocumentCache();

        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();


            mapTemp.put("chatId", map.get("chatId"));

            mapTemp.put("canHaveMoreMessages", map.get("canHaveMoreMessages"));
            mapTemp.put("partiallyRetrieved", map.containsKey("partiallyRetrieved"));

        }
        return mapTemp;
    }

    public void saveCanHaveMoreMessage(String docId) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();

            if (map != null) {

                Map<String, Object> map_temp = new HashMap<>();

                map_temp.putAll(map);

                map_temp.put("canHaveMoreMessages", false);
                if (map_temp.containsKey("partiallyRetrieved")) {
                    map_temp.remove("partiallyRetrieved");
                }

                try {
                    document.putProperties(map_temp);

                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error putting", e);
                }

            }


        }


        database.clearDocumentCache();

    }

    /*
     *
     * Add the details of newly created chat to list of all chats
     * */
    @SuppressWarnings("unchecked")
    public void addChatDocumentDetails(String receiverUid, String receiverDocId, String chatDocId, String secretId) {


        database.clearDocumentCache();

        Document document = database.getDocument(chatDocId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();

            if (map != null) {
                ArrayList<String> receiverUidArray = (ArrayList<String>) map.get("receiverUidArray");


                ArrayList<String> receiverDocIdArray = (ArrayList<String>) map.get("receiverDocIdArray");

                ArrayList<String> secretIdArray = (ArrayList<String>) map.get("secretIdArray");
                receiverUidArray.add(receiverUid);

                receiverDocIdArray.add(receiverDocId);
                secretIdArray.add(secretId);
                Map<String, Object> map_temp = new HashMap<>();

                map_temp.putAll(map);

                map_temp.put("receiverUidArray", receiverUidArray);
                map_temp.put("receiverDocIdArray", receiverDocIdArray);
                map_temp.put("secretIdArray", secretIdArray);
                try {
                    document.putProperties(map_temp);

                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error putting", e);
                }


            }


        }


        database.clearDocumentCache();


    }

    /*
     * To get details of the chats
     * */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getAllChatDetails(String chatDocId) {


        Map<String, Object> map = null;
        database.clearDocumentCache();


        Document document = database.getDocument(chatDocId);


        if (document != null) {

            map = document.getProperties();
        }


        database.clearDocumentCache();


        return map;


    }


    /*
     *
     *
     * Get details of the particular chat*/
    @SuppressWarnings("unchecked")
//    public Map<String, Object> getParticularChatInfo(String chatDocId) {
//
//
//        database.clearDocumentCache();
//
//        Document document = database.getDocument(chatDocId);
//
//        Map<String, Object> map = document.getProperties();
//
//
//        database.clearDocumentCache();
//
//
//        return map;
//
//
//    }


    public Map<String, Object> getParticularChatInfo(String chatDocId, boolean fetchGroup) {

        database.clearDocumentCache();
        Document document = database.getDocument(chatDocId);
        Map<String, Object> map = document.getProperties();

        boolean isGroupChat  = map.containsKey("groupChat") && (boolean) map.get("groupChat");

        if (fetchGroup) {
            if (!isGroupChat) {
                map = null;
            }
        } else {
            if (isGroupChat) {
                map = null;
            }
        }
        database.clearDocumentCache();
        return map;
    }


    /**
     * Get list of messages in the list
     */

    @SuppressWarnings("unchecked")


    public ArrayList<Map<String, Object>> retrieveAllMessages(String docId) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);
        ArrayList<Map<String, Object>> arr = new ArrayList<>();
        try {

            Map<String, Object> map = document.getProperties();

            if (map != null) {

                arr = (ArrayList<Map<String, Object>>) map.get("messsageArray");


            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        database.clearDocumentCache();


        return arr;
    }


    /*
     *
     * To update the status of a particular chat message sent
     *
     * */

    /**
     * status-0 not sent
     * status-1 sent
     * status-2 delivered
     * status-3 read
     */


    @SuppressWarnings("unchecked")
    public boolean updateMessageStatus(String docId, String messageId, int status, String deliveryTime) {

        boolean flag = false;

        database.clearDocumentCache();

        Document document = database.getDocument(docId);

        try {
            Map<String, Object> map = document.getProperties();

            if (map == null) {
                return false;
            } else {

                Map<String, Object> mapMessages;

                Map<String, Object> mapTemp = new HashMap<>();


                mapTemp.putAll(map);

                ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("messsageArray");
                String Id;
                for (int i = arr.size() - 1; i >= 0; i--) {

                    if (flag) {
                        break;


                    }

                    mapMessages = (arr.get(i));
                    Id = (String) mapMessages.get("id");


                    if (Id == null) {
                        return false;
                    }


                    if (Id.equals(messageId)) {

                        switch (status) {

                            case 0:


                                /*
                                 *
                                 * For the removed message or the edited message issue,
                                 * for the case when the message has been removed or edited before it was
                                 * acknowledged as being sent.
                                 *
                                 */
                                if (mapMessages.get("deliveryStatus").equals("0")) {
                                    mapMessages.put("deliveryStatus", "1");
                                    flag = true;
                                    arr.set(i, mapMessages);
                                    mapTemp.put("messsageArray", arr);

                                }
                                break;
                            case 1:
                                mapMessages.put("deliveryStatus", "2");

                                mapMessages.put("deliveryTime", deliveryTime);


                                flag = true;
                                arr.set(i, mapMessages);
                                mapTemp.put("messsageArray", arr);


                        }

                        try {
                            document.putProperties(mapTemp);

                        } catch (CouchbaseLiteException e) {
                            Log.e(TAG, "Error putting", e);
                        }

                    }

                }


                database.clearDocumentCache();


                return true;


            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        database.clearDocumentCache();


        return false;
    }


    /**
     * to update the time of message sent incase it has been sent later when internet came rather than original time as typed or selected by the user
     */
    @SuppressWarnings("unchecked")


    public void updateMessageTs(String docId, String messageId, String ts) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);
        if (document != null) {
            Map<String, Object> map = document.getProperties();
            Map<String, Object> mapMessages;

            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.putAll(map);


            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("messsageArray");

            for (int i = arr.size() - 1; i >= 0; i--) {

                mapMessages = (arr.get(i));


                String Id = (String) mapMessages.get("id");

                if (Id.equals(messageId)) {
                    mapMessages.put("Ts", ts);

                    arr.set(i, mapMessages);
                    mapTemp.put("messsageArray", arr);
                    try {
                        document.putProperties(mapTemp);


                    } catch (CouchbaseLiteException e) {
                        Log.e(TAG, "Error putting", e);
                    }


                    break;
                }

            }

        }


        database.clearDocumentCache();


    }


    /*To delete chat locally
     * */
    public void deleteChat(String docId) {


        database.clearDocumentCache();

        Document doc = database.getDocument(docId);

        Map<String, Object> mapTemp = new HashMap<>();


        Map<String, Object> mapOld = doc.getProperties();


        if (mapOld != null) {


            mapTemp.putAll(mapOld);


            mapTemp.put("messsageArray", new ArrayList<Map<String, Object>>());


            mapTemp.put("firstDate", "Mon 07/Mar/2016");
            mapTemp.put("lastDate", "Mon 07/Mar/2016");

        }
        try {
            doc.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }


        database.clearDocumentCache();


    }

    /**
     * To update in db locally that no new unread messages for the chat once that chat has been seen
     */
    public void updateChatListOnViewingMessage(String docId) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);

        Map<String, Object> map = document.getProperties();


        if (map == null) {
            return;
        }

        Map<String, Object> mapTemp = new HashMap<>();


        mapTemp.putAll(map);

        mapTemp.put("newMessageCount", "0");
        mapTemp.put("hasNewMessage", false);


        try {

            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }


        database.clearDocumentCache();


    }


    /**
     * in each of the document we are storing the last message date and last message time so that if flag hasNewMessage is set
     * then we can show that and also it will help us to sort chatlist based on the most recent
     */

    public void updateChatListForNewMessage(String docId, String lastmessage,
                                            boolean hasNewMessage, String lastMessageDate, String lastmessagetime) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        Map<String, Object> map = document.getProperties();


        if (map == null) {
            return;
        }
        String newMessageCount = (String) map.get("newMessageCount");

        int count = Integer.parseInt(newMessageCount);

        count += 1;


        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);
        mapTemp.put("hasNewMessage", hasNewMessage);
        if (hasNewMessage) {
            mapTemp.put("newMessage", lastmessage);
            mapTemp.put("newMessageTime", lastmessagetime);

            mapTemp.put("newMessageDate", lastMessageDate);


            mapTemp.put("newMessageCount", String.valueOf(count));
        }

        /*
         * Have put just for safety although not required
         */
        mapTemp.put("isSelf", false);
        mapTemp.put("deliveryStatus", 0);

        mapTemp.put("lastMessageDate", lastMessageDate);
        try {
            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }


        database.clearDocumentCache();


    }


    public void updateChatListForNewMessageFromHistory(String docId, String lastmessage,
                                                       boolean hasNewMessage, String lastMessageDate, String lastmessagetime, int count,
                                                       boolean isSelf, int deliveryStatus)//, boolean countToUpdate, int unreadCount)
    {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        Map<String, Object> map = document.getProperties();


        if (map == null) {
            return;
        }


        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);
        mapTemp.put("hasNewMessage", hasNewMessage);

        mapTemp.put("newMessage", lastmessage);
        mapTemp.put("newMessageTime", lastmessagetime);

        mapTemp.put("newMessageDate", lastMessageDate);
        mapTemp.put("isSelf", isSelf);


        mapTemp.put("deliveryStatus", deliveryStatus);

        mapTemp.put("newMessageCount", String.valueOf(count));


        mapTemp.put("lastMessageDate", lastMessageDate);


        /*
         * Required for the last message details for chats fetched from server,but fro whom messages has not been fetched yet
         */
        mapTemp.put("lastMessage", lastmessage);


        try {
            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }


        database.clearDocumentCache();


    }

    public void updateChatListForNewMessageFromHistory(String docId, String lastmessage,
                                                       boolean hasNewMessage, String lastMessageDate, String lastmessagetime, int count,
                                                       boolean isSelf, int deliveryStatus,  JSONArray memberInfo)//, boolean countToUpdate, int unreadCount)
    {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        Map<String, Object> map = document.getProperties();


        if (map == null) {
            return;
        }


        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);
        if(memberInfo != null){
            mapTemp.put("groupMemberInfo", memberInfo.toString());
        }

//        Log.e("GroupMemberInfoCouch","====>> "+memberInfo.toString()+"====> "+docId);

        mapTemp.put("hasNewMessage", hasNewMessage);

        mapTemp.put("newMessage", lastmessage);
        mapTemp.put("newMessageTime", lastmessagetime);

        mapTemp.put("newMessageDate", lastMessageDate);
        mapTemp.put("isSelf", isSelf);


        mapTemp.put("deliveryStatus", deliveryStatus);

        mapTemp.put("newMessageCount", String.valueOf(count));


        mapTemp.put("lastMessageDate", lastMessageDate);


        /*
         * Required for the last message details for chats fetched from server,but fro whom messages has not been fetched yet
         */
        mapTemp.put("lastMessage", lastmessage);


        try {
            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
            Log.e("GroupMemberInfoCouch","Exception====>> "+e.getMessage());

        }


        database.clearDocumentCache();


    }


    /**
     * Get number of new messages
     */
    public String getNewMessageCount(String docId) {

        database.clearDocumentCache();

        Document document = database.getDocument(docId);
        Map<String, Object> map = document.getProperties();

        database.clearDocumentCache();


        return (String) map.get("newMessageCount");
    }


    /**
     * Get last message details to show in list of all the chats
     */
    @SuppressWarnings("unchecked")

    public Map<String, Object> getLastMessageDetails(String docId) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);
        Map<String, Object> map = document.getProperties();

        Map<String, Object> result = new HashMap<>();
        if (map != null) {
            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("messsageArray");


            if (arr.size() == 0) {


                String time = (String) map.get("lastMessageDate");


                result.put("lastMessageDate", time);
                result.put("lastMessageTime", time);


                if (((String) map.get("chatId")).isEmpty()) {
                    if (!(((String) map.get("secretId")).isEmpty())) {

                        if (map.containsKey("messageDeleted")) {
                            result.put("lastMessage", "No Messages To Show!!");


                        } else {
                            result.put("lastMessage", map.get("lastMessage"));


                        }

                    } else {


                        result.put("lastMessage", "No Messages To Show!!");
                    }
                    result.put("showTick", false);
                } else {

                    /*
                     *
                     * Chats fetched from server
                     */
                    if (map.containsKey("messageDeleted")) {
                        result.put("lastMessage", "No Messages To Show!!");
                        result.put("showTick", false);

                    } else {
                        result.put("lastMessage", map.get("lastMessage"));


                        try {
                            if ((boolean) map.get("isSelf")) {
                                result.put("showTick", true);

                                result.put("tickStatus", map.get("deliveryStatus"));

                            } else {
                                result.put("showTick", false);
                            }
                        } catch (Exception w) {


                            /*
                             * For handling  crash for the messageType 10,not receiving the reply type
                             */

                            result.put("showTick", false);

                        }
                    }


                }

                database.clearDocumentCache();
                return result;
            }


            Map<String, Object> lastMessage = arr.get(arr.size() - 1);

            String ts = (String) lastMessage.get("Ts");

            String type = (String) lastMessage.get("messageType");


            result.put("lastMessageDate", ts);
            result.put("lastMessageTime", ts);


            try {
                if ((boolean) lastMessage.get("isSelf")) {
                    result.put("showTick", true);
                    try {
                        result.put("tickStatus", Integer.parseInt((String) lastMessage.get("deliveryStatus")));
                    } catch (ClassCastException e) {


                        result.put("tickStatus", lastMessage.get("deliveryStatus"));
                    }
                } else {
                    result.put("showTick", false);
                }
            } catch (Exception w) {

                result.put("showTick", false);

            }
            try {


                switch (Integer.parseInt(type)) {

                    case 0:
                        result.put("lastMessage", lastMessage.get("message"));
                        break;
                    case 1:

                        result.put("lastMessage", "Image");
                        break;
                    case 2:
                        result.put("lastMessage", "Video");
                        break;

                    case 3:
                        result.put("lastMessage", "Location");
                        break;
                    case 4:
                        result.put("lastMessage", "Follow");
                        break;
                    case 5:
                        result.put("lastMessage", "Audio");
                        break;
                    case 6:
                        result.put("lastMessage", "Sticker");
                        break;
                    case 7:
                        result.put("lastMessage", "Doodle");
                        break;
                    case 8:
                        result.put("lastMessage", "Gif");
                        break;


                    case 9:
                        result.put("lastMessage", "Document");
                        break;

                    case 15:
                        result.put("lastMessage", "Transfer");
                        break;

                    case 16:
                        result.put("lastMessage", "Missed call");
                        break;

                    case 17:
                        result.put("lastMessage", "Call");
                        break;


                    case 10: {


                        switch (Integer.parseInt((String) lastMessage.get("replyType"))) {


                            case 0:
                                result.put("lastMessage", lastMessage.get("message"));
                                break;
                            case 1:

                                result.put("lastMessage", "Image");
                                break;
                            case 2:
                                result.put("lastMessage", "Video");
                                break;

                            case 3:
                                result.put("lastMessage", "Location");
                                break;
                            case 4:
                                result.put("lastMessage", "Follow");
                                break;
                            case 5:
                                result.put("lastMessage", "Audio");
                                break;
                            case 6:
                                result.put("lastMessage", "Sticker");
                                break;
                            case 7:
                                result.put("lastMessage", "Doodle");
                                break;
                            case 8:
                                result.put("lastMessage", "Gif");
                                break;


                            case 9:
                                result.put("lastMessage", "Document");
                                break;


                            case 13:
                                result.put("lastMessage", "Post");
                                break;
                        }

                        break;
                    }
                    case 11: {

                        /*
                         * For the message removed
                         */

                        result.put("lastMessage", lastMessage.get("message"));
                        break;


                    }
                    case 98: {

                        result.put("showTick", false);


                        String initiatorName, memberName = "", initiatorId, memberId, message = "";

                        initiatorId = (String) lastMessage.get("initiatorId");

                        int groupMessageType = (int) lastMessage.get("type");


                        Context context = AppController.getInstance();
                        if (groupMessageType == 1 || groupMessageType == 2 || groupMessageType == 3) {

                            memberId = (String) lastMessage.get("memberId");

                            if (memberId.equals(AppController.getInstance().getUserId())) {

                                memberName = context.getString(R.string.YouSmall);
                            } else {
                                memberName = getFriendName(AppController.getInstance().getFriendsDocId(), memberId);
                                if (memberName == null) {

                                    memberName = (String) lastMessage.get("memberIdentifier");
                                }
                            }
                        }
                        if (initiatorId.equals(AppController.getInstance().getUserId())) {

                            initiatorName = context.getString(R.string.You);
                        } else {
                            initiatorName = getFriendName(AppController.getInstance().getFriendsDocId(), initiatorId);
                            if (initiatorName == null) {

                                initiatorName = (String) lastMessage.get("initiatorIdentifier");
                            }
                        }


                        switch (groupMessageType) {


                            case 0:
                                /*
                                 * Group created
                                 */
                                message = context.getString(R.string.CreatedGroup, initiatorName) + " " + lastMessage.get("groupName");
                                break;

                            case 1:

                                /*
                                 * Member added
                                 */
                                message = initiatorName + " " + context.getString(R.string.AddedMember, memberName) + " " + context.getString(R.string.ToGroup);
                                break;
                            case 2:


                                /*
                                 *Member removed
                                 */


                                message = initiatorName + " " + context.getString(R.string.Removed) + " " + memberName;

                                break;


                            case 3:


                                /*
                                 * Member made admin
                                 */

                                message = initiatorName + " " + context.getString(R.string.Made) + " " + memberName + " " + context.getString(R.string.MakeAdmin);


                                break;


                            case 4:


                                /*
                                 * Group subject updated
                                 */

                                message = initiatorName + " " + context.getString(R.string.UpdatedGroupSubject, (String) lastMessage.get("groupSubject"));

                                break;


                            case 5:
                                /*
                                 *
                                 * Member updated group icon
                                 */

                                message = initiatorName + " " + context.getString(R.string.UpdatedGroupIcon);
                                break;


                            case 6:

                                /*
                                 * Member left the conversation
                                 */

                                message = context.getString(R.string.LeftGroup, initiatorName);
                                break;

                        }

                        result.put("lastMessage", message);


                        break;
                    }
                    case 13:
                        result.put("lastMessage", "Post");
                        break;
                }


            } catch (NullPointerException e) {
                e.printStackTrace();
            }


        }


        database.clearDocumentCache();


        return result;
    }


    /**
     * To update status of all message above a message as read if that message has been reported as read by the reciver
     */
    @SuppressWarnings("unchecked")
    public boolean drawBlueTickUptoThisMessage(String document_id, String id, String readTime) {


        database.clearDocumentCache();

        Document document = database.getDocument(document_id);

        try {
            Map<String, Object> map = document.getProperties();


            if (map == null) {


                database.clearDocumentCache();


                return false;
            } else {

                Map<String, Object> mapMessages;

                Map<String, Object> mapTemp = new HashMap<>();


                mapTemp.putAll(map);


                ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("messsageArray");

                boolean flag = false;

                for (int i = arr.size() - 1; i >= 0; i--) {
                    mapMessages = (arr.get(i));
                    String Id = (String) mapMessages.get("id");


                    if (Id.equals(id)) {
                        flag = true;

                        for (int j = i; j >= 0; j--) {


                            mapMessages = arr.get(j);


                            if (mapMessages != null) {


                                if (((boolean) mapMessages.get("isSelf")) && !(mapMessages.get("deliveryStatus").equals("0"))) {

                                    if (mapMessages.get("deliveryStatus").equals("3"))
                                        break;
                                    else {
                                        mapMessages.put("deliveryStatus", "3");


                                        mapMessages.put("readTime", readTime);


                                        arr.set(j, mapMessages);
                                    }
                                }

                            }
                        }
                        mapTemp.put("messsageArray", arr);


                    }


                    if (flag) {
                        try {
                            document.putProperties(mapTemp);

                        } catch (CouchbaseLiteException e) {
                            Log.e(TAG, "Error putting", e);
                        }
                        break;
                    }

                }


                database.clearDocumentCache();


                return true;

            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }


        database.clearDocumentCache();


        return false;


    }


    /**
     * To get the docId of the receiver on the chatlist screen
     */
    @SuppressWarnings("unchecked")
    public String getDocumentIdOfReceiverChatlistScreen(String documentId, String receiverUid, String secretId) {
        String docId = "";


        database.clearDocumentCache();
        Document document = database.getDocument(documentId);
        if (document != null) {

            Map<String, Object> map = document.getProperties();


            ArrayList<String> arrReceiverUid = (ArrayList<String>) map.get("receiverUidArray");
            ArrayList<String> arrReceiverDocid = (ArrayList<String>) map.get("receiverDocIdArray");
            ArrayList<String> secretIdArray = (ArrayList<String>) map.get("secretIdArray");

            for (int i = 0; i < arrReceiverUid.size(); i++) {


                if (arrReceiverUid.get(i).equals(receiverUid) && secretIdArray.get(i).equals(secretId)) {
                    return arrReceiverDocid.get(i);


                }


            }


        }


        database.clearDocumentCache();


        return docId;
    }


    /**
     * To get the docId of the receiver,this is to be used when acknowledging received message
     * and as sender might have changed his device so we store his latest docId corresponding
     * \to last device he sent message from and use that docId at time of acknowledging
     */
    @SuppressWarnings("unchecked")
    public String getDocumentIdOfReceiver(String document_id, String receiver_uid) {
        String doc_id = null;


        database.clearDocumentCache();
        Document document = database.getDocument(document_id);

        Map<String, Object> map = document.getProperties();


        ArrayList<String> arr_receiver_uid = (ArrayList<String>) map.get("receiver_uid_array");
        ArrayList<String> arr_receiver_docid = (ArrayList<String>) map.get("receiver_docid_array");


        for (int i = 0; i < arr_receiver_uid.size(); i++) {
            if (arr_receiver_uid.get(i).equals(receiver_uid)) {

                doc_id = arr_receiver_docid.get(i);
            }


        }


        database.clearDocumentCache();


        return doc_id;
    }

    /*
     *
     *
     * To set the latest docId for the sender*/
    @SuppressWarnings("unchecked")
    public void setDocumentIdOfReceiver(String document_id, String receiver_doc_id, String
            receiver_uid) {


        database.clearDocumentCache();

        Document document = database.getDocument(document_id);

        Map<String, Object> map = document.getProperties();


        ArrayList<String> arr_receiver_uid = (ArrayList<String>) map.get("receiver_uid_array");
        ArrayList<String> arr_receiver_docid = (ArrayList<String>) map.get("receiver_docid_array");

        boolean flag = false;

        for (int i = 0; i < arr_receiver_uid.size(); i++) {
            if (arr_receiver_uid.get(i).equals(receiver_uid)) {
                arr_receiver_docid.remove(i);
                arr_receiver_docid.add(i, receiver_doc_id);
                flag = true;
                break;

            }
        }

        if (!flag) {

            arr_receiver_uid.add(receiver_uid);
            arr_receiver_docid.add(receiver_doc_id);
        }
        Map<String, Object> map_temp = new HashMap<>();
        map_temp.putAll(map);


        map_temp.put("receiver_uid_array", arr_receiver_uid);

        map_temp.put("receiver_docid_array", arr_receiver_docid);


        try {
            document.putProperties(map_temp);

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }


        database.clearDocumentCache();


    }


    /**
     * To add the message to be sent to the doc,from where it can be resent automatically again when internet comes and delete from doc when server acknowledges of receipt of the message
     */

    @SuppressWarnings("unchecked")


    public void addUnsentMessage(String docId, Map<String, Object> map) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> mapOld = document.getProperties();


            Map<String, Object> map_temp = new HashMap<>();
            map_temp.putAll(mapOld);


            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) mapOld.get("unsentMessageArray");

            arr.add(map);
            map_temp.put("unsentMessageArray", arr);

            try {
                document.putProperties(map_temp);

            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error putting", e);
            }


        }


        database.clearDocumentCache();


    }


    /**
     * To remove message from the list of the unsent messages,when server acknowledges that it has received a message
     */
    @SuppressWarnings("unchecked")


    public void removeUnsentMessage(String docId, String messageId) {

        boolean removed = false;
        database.clearDocumentCache();
        Document document = database.getDocument(docId);


        Map<String, Object> mapOld = document.getProperties();


        Map<String, Object> mapTemp = new HashMap<>();


        mapTemp.putAll(mapOld);


        if (mapOld != null) {
            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) mapOld.get("unsentMessageArray");


            if (arr.size() > 0) {
                Map<String, Object> map;
                for (int i = 0; i < arr.size(); i++) {


                    map = arr.get(i);


                    String id = (String) map.get("id");


                    if (id.equals(messageId)) {


                        arr.remove(i);
                        removed = true;


                        break;
                    }


                }

                if (removed) {


                    mapTemp.put("unsentMessageArray", arr);


                    try {
                        document.putProperties(mapTemp);

                    } catch (CouchbaseLiteException e) {
                        Log.e(TAG, "Error putting", e);
                    }
                }


            }


        }


        database.clearDocumentCache();


    }


    /**
     * To get list of all unsent messages to be emitted to server when socket connects
     */

    @SuppressWarnings("unchecked")

    public ArrayList<Map<String, Object>> getUnsentMessages(String docId) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);


        Map<String, Object> mapOld = document.getProperties();


        database.clearDocumentCache();


        return (ArrayList<Map<String, Object>>) mapOld.get("unsentMessageArray");


    }


    /**
     * To delete the message locally when user swipes and delete that message
     * from the list of the messages
     */

    @SuppressWarnings("unchecked")


    public void deleteParticularChatMessage(String docId, String messageId) {
        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        boolean deleted = false;

        Map<String, Object> mapOld = document.getProperties();


        Map<String, Object> mapTemp = new HashMap<>();


        mapTemp.putAll(mapOld);

        Map<String, Object> mapMessages;
        if (mapOld != null) {


            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) mapOld.get("messsageArray");

            for (int i = arr.size() - 1; i >= 0; i--) {


                mapMessages = (arr.get(i));


                String Id = (String) mapMessages.get("id");


                if (Id.equals(messageId)) {


                    arr.remove(i);
                    deleted = true;
                    break;
                }
            }


            if (deleted) {


                mapTemp.put("messsageArray", arr);


                try {
                    document.putProperties(mapTemp);

                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error putting", e);
                }

            }


        }


        database.clearDocumentCache();


    }


    /**
     * To check if message already saved locally to prevent duplication of the messages locally
     */

    @SuppressWarnings("unchecked")
    public boolean checkAlreadyExists(String documentId, String id) {


        database.clearDocumentCache();
        Document document = database.getDocument(documentId);


        Map<String, Object> map_old = document.getProperties();


        if (map_old != null) {
            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map_old.get("messsageArray");


            Map<String, Object> map;
            for (int i = 0; i < arr.size(); i++) {

                map = arr.get(i);

                if (map.get("id") != null) {


                    if ((!(boolean) map.get("isSelf")) && map.get("id").equals(id)) {


                        database.clearDocumentCache();
                        return true;
                    }
                }
            }
        }


        database.clearDocumentCache();


        return false;

    }

    /**
     * To check if message already saved locally to prevent duplication of the messages locally in secretchat
     */

    @SuppressWarnings("unchecked")
    public boolean checkAlreadyExistsSecretChat(String documentId, String id) {


        database.clearDocumentCache();
        Document document = database.getDocument(documentId);


        Map<String, Object> map_old = document.getProperties();


        if (map_old != null) {
            ArrayList<String> arr = (ArrayList<String>) map_old.get("messageIdsArray");

            if (arr != null) {
                for (int i = arr.size() - 1; i >= 0; i--) {
                    if (arr.get(i).equals(id)) {


                        database.clearDocumentCache();
                        return true;
                    }
                }
            }

        }


        database.clearDocumentCache();


        return false;

    }
/////////////////////////////////////////////////////////////////////////////////////////////


    /*
     * sup specific
     */


/////////////////////////////////////////////////////////////////////////////////////////////


    public boolean changeTransferMessageInDb(String documentId, String messageId, Map<String, Object> newMap) {


        database.clearDocumentCache();

        Document document = database.getDocument(documentId);


        Map<String, Object> mapOld = document.getProperties();


        if (mapOld != null) {
            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) mapOld.get("messsageArray");


            for (int i = arr.size() - 1; i >= 0; i--) {
                if (arr.get(i).get("id").equals(messageId)) {
                    arr.set(i, newMap);

                    Map<String, Object> mapTemp = new HashMap<>();
                    mapTemp.putAll(mapOld);


                    mapTemp.put("messsageArray", arr);
                    try {
                        document.putProperties(mapTemp);


                    } catch (CouchbaseLiteException e) {
                        Log.e(TAG, "Error putting", e);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return true;
                }
            }


        }


        database.clearDocumentCache();

        return false;

    }


    /*
     * To update a message as removed
     */
    @SuppressWarnings("unchecked")

    public void markMessageAsRemoved(String documentId, String messageId, String payload, String removedAt) {


        database.clearDocumentCache();

        Document document = database.getDocument(documentId);


        Map<String, Object> mapOld = document.getProperties();


        if (mapOld != null) {
            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) mapOld.get("messsageArray");


            for (int i = arr.size() - 1; i >= 0; i--) {
                if (arr.get(i).get("id").equals(messageId)) {

                    Map<String, Object> map = arr.get(i);
                    map.put("messageType", "11");
                    map.put("message", payload);
                    map.put("removedAt", removedAt);

                    arr.set(i, map);
                    break;
                }


            }


            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.putAll(mapOld);


            mapTemp.put("messsageArray", arr);
            try {
                document.putProperties(mapTemp);


            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error putting", e);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        database.clearDocumentCache();


    }


    /*
     * To update a message as edited
     */
    @SuppressWarnings("unchecked")

    public void markMessageAsEdited(String documentId, String messageId, String payload) {


        database.clearDocumentCache();

        Document document = database.getDocument(documentId);


        Map<String, Object> mapOld = document.getProperties();

        try {


            payload = new String(Base64.decode(payload, Base64.DEFAULT), "UTF-8");


        } catch (UnsupportedEncodingException e) {

        }
        if (mapOld != null) {
            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) mapOld.get("messsageArray");


            for (int i = arr.size() - 1; i >= 0; i--) {
                if (arr.get(i).get("id").equals(messageId)) {

                    Map<String, Object> map = arr.get(i);
                    map.put("wasEdited", true);
                    map.put("message", payload);

                    arr.set(i, map);
                    break;
                }


            }


            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.putAll(mapOld);


            mapTemp.put("messsageArray", arr);
            try {
                document.putProperties(mapTemp);


            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error putting", e);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        database.clearDocumentCache();


    }


    /**
     * To sort the messages array after adding the new message
     */

    @SuppressWarnings("unchecked")

    public void addNewChatMessageAndSort(String documentId, Map<String, Object> map, String dateInGmt, String secretId) {


        database.clearDocumentCache();

        Document document = database.getDocument(documentId);


        Map<String, Object> mapOld = document.getProperties();


        if (mapOld != null) {
            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) mapOld.get("messsageArray");


            arr.add(map);


            Collections.sort(arr, new MessageSorter());


            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.putAll(mapOld);

            if (dateInGmt != null) {
                mapTemp.put("lastMessageDate", dateInGmt);
            }


            if (!secretId.isEmpty()) {
                try {
                    ArrayList<String> messageIds = (ArrayList<String>) mapOld.get("messageIdsArray");
                    if (messageIds != null) {

                        messageIds.add((String) map.get("id"));


                        mapTemp.put("messageIdsArray", messageIds);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            mapTemp.put("messsageArray", arr);
            try {
                document.putProperties(mapTemp);


            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error putting", e);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        database.clearDocumentCache();


    }


    /**
     * Update the status as downloaded and change path once download of file is complete
     */
    @SuppressWarnings("unchecked")

    public void updateDownloadStatusAndPath(String docId, String path, String messageId) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        boolean updated = false;

        Map<String, Object> mapOld = document.getProperties();


        Map<String, Object> mapTemp = new HashMap<>();


        mapTemp.putAll(mapOld);

        Map<String, Object> mapMessages;
        if (mapOld != null) {


            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) mapOld.get("messsageArray");

            for (int i = arr.size() - 1; i >= 0; i--) {


                mapMessages = (arr.get(i));


                String Id = (String) mapMessages.get("id");


                if (Id.equals(messageId)) {


                    arr.remove(i);
                    updated = true;


                    mapMessages.put("downloadStatus", 1);

                    mapMessages.put("message", path);

                    arr.add(i, mapMessages);
                    break;
                }
            }


            if (updated) {


                mapTemp.put("messsageArray", arr);


                try {
                    document.putProperties(mapTemp);

                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error putting", e);
                }

            }


        }


        database.clearDocumentCache();


    }

///////////////////////////////////////////////////


    /*
     * sign in specific
     */


    /**
     * Update index document when sign in status of user changes(user signs in)
     */
    public void updateIndexDocumentOnSignIn(String docId, String userId, int signInType, boolean profileSaved) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);

        Map<String, Object> map = document.getProperties();


        Map<String, Object> mapTemp = new HashMap<>();

        mapTemp.putAll(map);


        mapTemp.put("isSignedIn", true);
        mapTemp.put("signedUserId", userId);
        mapTemp.put("signInType", signInType);

        mapTemp.put("profileSaved", profileSaved);
        try {
            document.putProperties(mapTemp);
        } catch (CouchbaseLiteException e) {
            com.couchbase.lite.util.Log.e(TAG, "Error putting", e);
        }

    }

    /**
     * Update index document when sign in status of user changes(user signs out)
     */
    public void updateIndexDocumentOnSignOut(String docId) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);

        Map<String, Object> map = document.getProperties();


        Map<String, Object> mapTemp = new HashMap<>();

        mapTemp.putAll(map);


        mapTemp.put("isSignedIn", false);
        mapTemp.put("signedUserId", null);
        mapTemp.put("signInType", -1);
        mapTemp.put("profileSaved", false);

        try {
            document.putProperties(mapTemp);
        } catch (CouchbaseLiteException e) {
            com.couchbase.lite.util.Log.e(TAG, "Error putting", e);
        }

    }

    /**
     * To check if user is signed in
     */
    public Map<String, Object> isSignedIn(String docId) {

        database.clearDocumentCache();


        Map<String, Object> signInDetails = new HashMap<>();

        Document document = database.getDocument(docId);

        Map<String, Object> map = document.getProperties();

        signInDetails.put("profileSaved", map.get("profileSaved"));
        signInDetails.put("isSignedIn", map.get("isSignedIn"));
        signInDetails.put("signInType", map.get("signInType"));
        signInDetails.put("signedUserId", map.get("signedUserId"));
        return signInDetails;

    }

//
//    /**
//     * To get user id of currently signed in user
//     */
//
//    public String getSignedInUserId(String docId) {
//
//        database.clearDocumentCache();
//
//        Document document = database.getDocument(docId);
//
//        Map<String, Object> map = document.getProperties();
//
//        return (String) map.get("signedUserId");
//
//    }


//    /**
//     * For adding of the badges for the chat module
//     */
//
//    @SuppressWarnings("unchecked")
//    public int getUnreadChatsReceiverUidsCount(String docId) {
//
//        database.clearDocumentCache();
//
//        Document document = database.getDocument(docId);
//
//        Map<String, Object> map = document.getProperties();
//
////        document = null;
//
//
//        return ((ArrayList<String>) map.get("unreadChatsReceiverUidSet")).size();
//    }
//
//    /**
//     * To get list of receiverUids for whom we have unread chats
//     */
//    @SuppressWarnings("unchecked")
//    public ArrayList<String> getUnreadChatsReceiverUids(String docId) {
//
//        database.clearDocumentCache();
//
//        Document document = database.getDocument(docId);
//
//        Map<String, Object> map = document.getProperties();
//
////        document = null;
//        return (ArrayList<String>) map.get("unreadChatsReceiverUidSet");
//
//
//    }
//
//
//    /**
//     * To add the receiverUid to list of receiverUids for whom we have unread chats
//     */
//
//
//    @SuppressWarnings("unchecked")
//    public void addUnreadChatsReceiverUids(String docId, String receiverUid) {
//
//        database.clearDocumentCache();
//
//        Document document = database.getDocument(docId);
//
//        Map<String, Object> map = document.getProperties();
//        Map<String, Object> mapTemp = new HashMap<>();
//        mapTemp.putAll(map);
//
//
//        ArrayList<String> set = (ArrayList<String>) map.get("unreadChatsReceiverUidSet");
//
//
//        set.add(receiverUid);
//
//
//        mapTemp.put("unreadChatsReceiverUidSet", set);
//
//
//        try {
//            document.putProperties(mapTemp);
//        } catch (CouchbaseLiteException e) {
//            com.couchbase.lite.util.Log.e(TAG, "Error putting", e);
//        }
//
//
////        map = null;
////        document = null;
////        set = null;
////        mapTemp = null;
//    }
//
//    /**
//     * To clear the list of the unread chats from list of receiverUids for whom we have unread chats
//     */
//    @SuppressWarnings("unchecked")
//    public void clearUnreadChatsReceiverUids(String docId) {
//
//        database.clearDocumentCache();
//
//        Document document = database.getDocument(docId);
//
//        Map<String, Object> map = document.getProperties();
//        Map<String, Object> mapTemp = new HashMap<>();
//        mapTemp.putAll(map);
//
//
//        ArrayList<String> set = new ArrayList<>();
//
//        mapTemp.put("unreadChatsReceiverUidSet", set);
//
//
//        try {
//            document.putProperties(mapTemp);
//        } catch (CouchbaseLiteException e) {
//            com.couchbase.lite.util.Log.e(TAG, "Error putting", e);
//        }
//
//
////        map = null;
////        document = null;
////        set = null;
////        mapTemp = null;
//    }
//
//
//    /**
//     * To remove the receiverUid from the list of receiverUids for whom we have unread chats
//     */
//
//    @SuppressWarnings("unchecked")
//    public void removeUnreadChatsReceiverUids(String docId, String receiverUid) {
//
//        database.clearDocumentCache();
//
//        Document document = database.getDocument(docId);
//
//        Map<String, Object> map = document.getProperties();
//        Map<String, Object> mapTemp = new HashMap<>();
//        mapTemp.putAll(map);
//
//
//        ArrayList<String> set = (ArrayList<String>) map.get("unreadChatsReceiverUidSet");
//
//        if (set.contains(receiverUid))
//            set.remove(receiverUid);
//
//
//        mapTemp.put("unreadChatsReceiverUidSet", set);
//
//
//        try {
//            document.putProperties(mapTemp);
//        } catch (CouchbaseLiteException e) {
//            com.couchbase.lite.util.Log.e(TAG, "Error putting", e);
//        }
//
//
////        map = null;
////        document = null;
////        set = null;
////        mapTemp = null;
//    }
//



    /*
     * To update the user details on login/signup
     */

    /**
     * @param docId   user doc id
     * @param mapProp HashMap<String, Object> containing user details to update in couchdb locally
     */
    public void updateUserDetails(String docId, Map<String, Object> mapProp) {

        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();

            if (map != null) {


                Map<String, Object> map_temp = new HashMap<>();

                map_temp.putAll(map);


                map_temp.put("userId", mapProp.get("userId"));
                map_temp.put("userName", mapProp.get("userName"));

                map_temp.put("userIdentifier", mapProp.get("userIdentifier"));

                map_temp.put("userLoginType", mapProp.get("userLoginType"));

                map_temp.put("apiToken", mapProp.get("apiToken"));

                try {
                    document.putProperties(map_temp);


                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error putting", e);
                }


            }


        }
    }


    public void updateUserName(String docId, String name) {


        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();

            if (map != null) {


                Map<String, Object> map_temp = new HashMap<>();

                map_temp.putAll(map);


                map_temp.put("userName", name);


                try {
                    document.putProperties(map_temp);


                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error putting", e);
                }


            }


        }
    }


    /**
     * To update the image of the user in ocuch db
     */

    public void updateUserImageUrl(String docId, String imageUrl) {


        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();

            if (map != null) {


                Map<String, Object> map_temp = new HashMap<>();

                map_temp.putAll(map);


                map_temp.put("userImageUrl", imageUrl);


                try {
                    document.putProperties(map_temp);


                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error putting", e);
                }


            }


        }
    }

    /**
     * To update the user status into the db
     */
    public void updateUserStatus(String docId, String status) {


        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();

            if (map != null) {


                Map<String, Object> map_temp = new HashMap<>();

                map_temp.putAll(map);


                map_temp.put("socialStatus", status);


                try {
                    document.putProperties(map_temp);


                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error putting", e);
                }


            }


        }
    }


    /**
     * To get the user document id from the index document on login/signup in case the user doc already exists
     */

    @SuppressWarnings("unchecked")
    public String getUserDocId(String userId, String indexDocId) {

        String docId = "";
        database.clearDocumentCache();


        Document document = database.getDocument(indexDocId);

        Map<String, Object> map = document.getProperties();


        if (map != null) {


            ArrayList<String> arr = (ArrayList<String>) map.get("userNameArray");
            ArrayList<String> arr2 = (ArrayList<String>) map.get("userDocIdArray");


            for (int i = arr.size() - 1; i >= 0; i--) {


                if (arr.get(i).equals(userId)) {


                    docId = arr2.get(i);


                    break;
                }

            }


        }

        database.clearDocumentCache();
        return docId;


    }


    private String createMqttTokenToMessageIdMappingDocument() {

        database.clearDocumentCache();
        Document document = database.createDocument();
        Map<String, Object> map = new HashMap<>();


        map.put("idMappingArray", new ArrayList<Map<String, Object>>());
        try {
            document.putProperties(map);

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }


        database.clearDocumentCache();


        return document.getId();


    }


    @SuppressWarnings("unchecked")
    public void addMqttTokenMapping(String docId, ArrayList<HashMap<String, Object>> arrToken) {
        database.clearDocumentCache();

        Document document = database.getDocument(docId);

        Map<String, Object> map = document.getProperties();


        Map<String, Object> mapTemp = new HashMap<>();

        mapTemp.putAll(map);


        mapTemp.put("idMappingArray", arrToken);
        try {
            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }


        database.clearDocumentCache();


    }


    @SuppressWarnings("unchecked")

    public ArrayList<HashMap<String, Object>> fetchMqttTokenMapping(String docId) {
        database.clearDocumentCache();

        Document document = database.getDocument(docId);

        Map<String, Object> map = document.getProperties();


        return ((ArrayList<HashMap<String, Object>>) map.get("idMappingArray"));

    }


//
//    public String createAcksDoc(){
//
//
//
//
//        database.clearDocumentCache();
//        Document document = database.createDocument();
//        Map<String, Object> map = new HashMap<>();
//
//
////        map.put("unsentAcksArray", new ArrayList<JSONObject>());
//
//
//        map.put("unsentAcksArray", new ArrayList<String>());
//
//        try {
//            document.putProperties(map);
//
//        } catch (CouchbaseLiteException e) {
//            Log.e(TAG, "Error putting", e);
//        }
////
////        map = null;
////        arr = null;
//
//
//        database.clearDocumentCache();
//
//
//        return document.getId();
//
//
//    }
//
//
//@SuppressWarnings("unchecked")
//    public void addUnsentAck(String docId,JSONObject ackObject){
//
//
///*
// * Since was having the problem with the serialization of the string
// */
//
//        database.clearDocumentCache();
//
//        Document document = database.getDocument(docId);
//        if(document!=null) {
//            Map<String, Object> map = document.getProperties();
////ArrayList<JSONObject> arr=(ArrayList<JSONObject>)map.get("unsentAcksArray");
//
//
//            ArrayList<String> arr=(ArrayList<String>)map.get("unsentAcksArray");
//
//
//
//arr.add(ackObject.toString());
//            Map<String, Object> mapTemp = new HashMap<>();
//            mapTemp.putAll(map);
//
//
//
//
//
//            mapTemp.put("unsentAcksArray",arr);
//            try {
//                document.putProperties(mapTemp);
//
//            } catch (CouchbaseLiteException e) {
//                Log.e(TAG, "Error putting", e);
//            }
//
//        }
//
//    }
//
//
//
//
//
//    public void removeAllUnsentAcks(String docId){
//
//
//
//
//
//        database.clearDocumentCache();
//
//        Document document = database.getDocument(docId);
//
//
//if(document!=null) {
//
//    Map<String, Object> map = document.getProperties();
//    Map<String, Object> mapTemp = new HashMap<>();
//    mapTemp.putAll(map);
//
//
//
//
//    mapTemp.put("unsentAcksArray",new ArrayList<String>());
////    mapTemp.put("unsentAcksArray",new ArrayList<JSONObject>());
//    try {
//        document.putProperties(mapTemp);
//
//    } catch (CouchbaseLiteException e) {
//        Log.e(TAG, "Error putting", e);
//    }
//
//}
//
//
//    }
//
//
//    @SuppressWarnings("unchecked")
//  //  public ArrayList<JSONObject> getUnsentAcks(String docId){
//    public ArrayList<String> getUnsentAcks(String docId){
//
//
////ArrayList<JSONObject>  arr=new ArrayList<>();
//
//        ArrayList<String>  arr=new ArrayList<>();
//
//        database.clearDocumentCache();
//
//        Document document = database.getDocument(docId);
//        if(document!=null) {
//            Map<String, Object> map = document.getProperties();
////           arr= (ArrayList<JSONObject>)map.get("unsentAcksArray");
//
//
//            arr= (ArrayList<String>)map.get("unsentAcksArray");
//    }
//
//    return  arr;
//
//
//
//
//
//
//
//
//    }


    /*
     * For the social status
     */


    /*
     * To create document containing all the status info
     * */
    private String createStatusDocument() {
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<>();

        map.put("statusArray", new ArrayList<String>());
        try {
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }


    /*
     * To create document containing all the notifications info
     * */
    private String createNotificationDocument() {
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<>();

        map.put("notificationArray", new ArrayList<String>());


        try {
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }


    /*
     *
     * To add a new status
     */
    @SuppressWarnings("unchecked")
    public void addNewStatus(String documentId, String status) {
        Document document = database.getDocument(documentId);
        if (document != null) {
            Map<String, Object> map_old = document.getProperties();
            if (map_old != null) {
                ArrayList<String> arr = (ArrayList<String>) map_old.get("statusArray");


                arr.add(status);


                Map<String, Object> mapTemp = new HashMap<>();
                mapTemp.putAll(map_old);

                mapTemp.put("statusArray", arr);

                try {
                    document.putProperties(mapTemp);
                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error putting", e);
                }
            }
        }
    }

    /*
     * To delete all status
     */


    @SuppressWarnings("unchecked")
    public void deleteAllStatus(String documentId) {
        Document document = database.getDocument(documentId);
        if (document != null) {
            Map<String, Object> map_old = document.getProperties();
            if (map_old != null) {


                Map<String, Object> mapTemp = new HashMap<>();
                mapTemp.putAll(map_old);

                mapTemp.put("statusArray", new ArrayList<String>());

                try {
                    document.putProperties(mapTemp);
                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error putting", e);
                }
            }
        }
    }


    /*
     * To delete particular status from the lsit of all status for the given user
     */


    @SuppressWarnings("unchecked")
    public void deleteParticularStatus(String documentId, String status) {
        Document document = database.getDocument(documentId);
        if (document != null) {
            Map<String, Object> map_old = document.getProperties();
            if (map_old != null) {


                ArrayList<String> arr = (ArrayList<String>) map_old.get("statusArray");


                for (int i = 0; i < arr.size(); i++) {
                    if (arr.get(i).equals(status)) {
                        arr.remove(i);

                        Map<String, Object> mapTemp = new HashMap<>();
                        mapTemp.putAll(map_old);

                        mapTemp.put("statusArray", arr);

                        try {
                            document.putProperties(mapTemp);
                        } catch (CouchbaseLiteException e) {
                            Log.e(TAG, "Error putting", e);
                        }


                        return;
                    }

                }


            }
        }
    }


    /*
     * To fetch list of all status
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String> fetchAllStatus(String documentId) {


        ArrayList<String> arr = new ArrayList<>();


        Document document = database.getDocument(documentId);
        if (document != null) {
            Map<String, Object> map_old = document.getProperties();
            if (map_old != null) {


                arr = (ArrayList<String>) map_old.get("statusArray");
            }
        }


        return arr;
    }




    /*
     * For the calls
     */


    /*
     * To create document containing all the calls info
     * */
    private String createCallsDocument() {
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<>();

        map.put("callsArray", new ArrayList<Map<String, String>>());
        try {
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }

    /*
     *To add a new call to the list of the calls
     *
     */
    @SuppressWarnings("unchecked")
    public void addNewCall(String documentId, Map<String, Object> callItem) {
        Document document = database.getDocument(documentId);
        if (document != null) {
            Map<String, Object> map_old = document.getProperties();
            if (map_old != null) {
                ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map_old.get("callsArray");


                arr.add(callItem);


                Map<String, Object> mapTemp = new HashMap<>();
                mapTemp.putAll(map_old);

                mapTemp.put("callsArray", arr);

                try {
                    document.putProperties(mapTemp);
                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error putting", e);
                }
            }
        }
    }


    /*
     *To add a new call to the list of the calls
     *
     */
    @SuppressWarnings("unchecked")
    public void addCallLogs(String documentId, ArrayList<Map<String, Object>> callsArray) {
        Document document = database.getDocument(documentId);
        if (document != null) {
            Map<String, Object> map_old = document.getProperties();
            if (map_old != null) {
                ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map_old.get("callsArray");


                for (int i = callsArray.size() - 1; i >= 0; i--) {

                    arr.add( callsArray.get(i));
                }


                Map<String, Object> mapTemp = new HashMap<>();
                mapTemp.putAll(map_old);

                mapTemp.put("callsArray", arr);

                try {
                    document.putProperties(mapTemp);
                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error putting", e);
                }
            }
        }
    }
    public void appendCallLogs(String documentId, ArrayList<Map<String, Object>> callsArray) {
        Document document = database.getDocument(documentId);
        if (document != null) {
            Map<String, Object> map_old = document.getProperties();
            if (map_old != null) {
                ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map_old.get("callsArray");


                for (int i = callsArray.size() - 1; i >= 0; i--) {

                    arr.add(0, callsArray.get(i));
                }


                Map<String, Object> mapTemp = new HashMap<>();
                mapTemp.putAll(map_old);

                mapTemp.put("callsArray", arr);

                try {
                    document.putProperties(mapTemp);
                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error putting", e);
                }
            }
        }
    }



    /*
     *
     * To delete the list of the calls stored locally
     */

    public void deleteAllCalls(String docId) {
        Document doc = database.getDocument(docId);
        Map<String, Object> mapTemp = new HashMap<>();
        Map<String, Object> mapOld = doc.getProperties();
        if (mapOld != null) {

            mapTemp.putAll(mapOld);
            mapTemp.put("callsArray", new ArrayList<Map<String, Object>>());
        }
        try {
            doc.putProperties(mapTemp);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
    }


    /**
     * To fetch the list  of the calls saved locally
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Map<String, Object>> getAllTheCalls(String docId) {
        ArrayList<Map<String, Object>> callsArray = new ArrayList<>();


        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> map = document.getProperties();
            callsArray = (ArrayList<Map<String, Object>>) map.get("callsArray");
        }

        return callsArray;
    }




    /*
     *To delete the list of the particular call
     */

    @SuppressWarnings("unchecked")

    public void deleteParticularCallDetail(String docId, String messageId) {
        Document document = database.getDocument(docId);
        boolean deleted = false;
        if (document != null) {
            Map<String, Object> mapOld = document.getProperties();
            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.putAll(mapOld);
            Map<String, Object> mapMessages;
            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) mapOld.get("callsArray");
            for (int i = arr.size() - 1; i >= 0; i--) {
                mapMessages = (arr.get(i));
                String Id = (String) mapMessages.get("callId");
                if (Id != null && messageId != null) {
                    if (Id.equals(messageId)) {
                        arr.remove(i);
                        deleted = true;
                        break;
                    }
                }
            }
            if (deleted) {
                mapTemp.put("callsArray", arr);
                try {
                    document.putProperties(mapTemp);
                } catch (CouchbaseLiteException e) {
                    Log.e(TAG, "Error putting", e);
                }
            }
        }
    }


    /*
     * For the contacts
     */


    /*
     * To create the contacts document
     */
    private String createContactsDocument() {
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<>();

        map.put("contactsArray", new ArrayList<Map<String, String>>());
        try {
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }


    /*
     * To create the contacts document
     */
    private String createAllContactsDocument() {
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<>();

        map.put("allContactsArray", new ArrayList<Map<String, String>>());
        try {
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }


//    /**
//     * To fetch the contact info corresponding to a given uid/number
//     */
//    @SuppressWarnings("unchecked")
//    public Map<String, Object> getContactInfo(String docId, String from) {
//
//
//        Document document = database.getDocument(docId);
//        Map<String, Object> map = document.getProperties();
//        ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("contactsArray");
//        Map<String, Object> contact;
//
//        for (int i = 0; i < arr.size(); i++) {
//
//            contact = arr.get(i);
//
//
//            if ((contact.get("contactUid")).equals(from)) {
////                return contact.get("contactUserName");
//
//
//                return contact;
//            }
//        }
//
//        return null;
//    }
    /**
     * To fetch the contact info corresponding to a given uid/number
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getFriendInfo(String docId, String from) {


        Document document = database.getDocument(docId);
        Map<String, Object> map = document.getProperties();
        ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("friendsArray");
        Map<String, Object> contact;

        for (int i = 0; i < arr.size(); i++) {

            contact = arr.get(i);

            if(contact.get("usreId") != null){
                if ((contact.get("userId")).equals(from)) {
//                return contact.get("contactUserName");

                    return contact;
                }
            }
        }

        return null;
    }
//    /**
//     * To fetch the contact name corresponding to a given uid/number
//     */
//    @SuppressWarnings("unchecked")
//    public String getContactName(String docId, String uid) {
//
//
//        Document document = database.getDocument(docId);
//        Map<String, Object> map = document.getProperties();
//        ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("contactsArray");
//
//        for (int i = 0; i < arr.size(); i++) {
//
//
//            if ((arr.get(i).get("contactUid")).equals(uid)) {
//
//                return (String) arr.get(i).get("contactName");
//
//
//            }
//        }
//
//        return null;
//    }


    /**
     * To fetch the friend name corresponding to a given uid/number
     */
    @SuppressWarnings("unchecked")
    public String getFriendName(String docId, String uid) {


        Document document = database.getDocument(docId);
        Map<String, Object> map = document.getProperties();
        ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("friendsArray");

        for (int i = 0; i < arr.size(); i++) {

            try {

                if ((arr.get(i).get("userId")).equals(uid)) {

                    return arr.get(i).get("firstName") + " " + arr.get(i).get("lastName");


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }



    /*
     * To update the contact details
     */


    @SuppressWarnings("unchecked")

    public void updateContactDetails(String docId, String contactUid, String contactProfilePic, String contactStatus) {

        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> mapOld = document.getProperties();


            Map<String, Object> mapTemp = new HashMap<>();


            mapTemp.putAll(mapOld);


            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) mapOld.get("friendsArray");


            Map<String, Object> map;


            for (int i = 0, len = arr.size(); i < len; i++) {

                map = arr.get(i);
                if (map.get("userId").equals(contactUid)) {
                    if (contactProfilePic != null) {
                        map.put("profilePic", contactProfilePic);
                    }

                    if (contactStatus != null) {
                        map.put("socialStatus", contactStatus);
                    }

                    arr.set(i, map);

                    mapTemp.put("friendsArray", arr);
                    try {

                        document.putProperties(mapTemp);

                    } catch (CouchbaseLiteException e) {
                        Log.e(TAG, "Error putting", e);
                    }

                    break;


                }
            }


        }


    }

//    /**
//     * To fetch the contact name corresponding to a given uid/number
//     */
//    @SuppressWarnings("unchecked")
//    public Map<String, Object> getContactInfoFromUid(String docId, String uid) {
//
//
//        Document document = database.getDocument(docId);
//        Map<String, Object> map = document.getProperties();
//        ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("contactsArray");
//        Map<String, Object> contact;
//        Map<String, Object> contactDetails = null;
//        for (int i = 0; i < arr.size(); i++) {
//
//            contact = arr.get(i);
//
//
//            if ((contact.get("contactUid")).equals(uid)) {
//
//                contactDetails = contact;
//                break;
//
//            }
//        }
//
//        return contactDetails;
//    }

    public boolean getStarFromFriendsDoc(String friendsDocId, String receiverUid) {
        Document document = database.getDocument(friendsDocId);
        Map<String, Object> map = document.getProperties();
        ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("friendsArray");
        Map<String, Object> friend;
        for (int i = 0; i < arr.size(); i++) {
            friend = arr.get(i);
            try{
                if ((friend.get("userId")).equals(receiverUid)) {
                    return (boolean) friend.get("isStar");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return false;

    }


    /**
     * To fetch the contact name corresponding to a given uid/number
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Map<String, Object>> loadContacts(String docId) {

        ArrayList<Map<String, Object>> arr = new ArrayList<>();
        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> map = document.getProperties();

            arr = (ArrayList<Map<String, Object>>) map.get("contactsArray");
        }
        return arr;


    }

    /**
     * To fetch the contact name corresponding to a given uid/number
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Map<String, Object>> loadFriends(String docId) {

        ArrayList<Map<String, Object>> arr = new ArrayList<>();
        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> map = document.getProperties();

            arr = (ArrayList<Map<String, Object>>) map.get("friendsArray");
        }
        return arr;


    }

    /**
     * To fetch the friend name corresponding to a given uid/number
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getFriendInfoFromUid(String docId, String uid) {


        Document document = database.getDocument(docId);
        Map<String, Object> map = document.getProperties();
        ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("friendsArray");
        Map<String, Object> contact;
        Map<String, Object> contactDetails = null;
        for (int i = 0; i < arr.size(); i++) {

            contact = arr.get(i);

            try{
                if ((contact.get("userId")).equals(uid)) {

                    contactDetails = contact;
                    break;

                }
            } catch (Exception e){
                contactDetails = contact;
                e.printStackTrace();
            }

        }

        return contactDetails;
    }

    /**
     * To fetch the contact name corresponding to a given uid/number
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Map<String, Object>> fetchAllContacts(String docId) {

        ArrayList<Map<String, Object>> arr = new ArrayList<>();
        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> map = document.getProperties();

            arr = (ArrayList<Map<String, Object>>) map.get("allContactsArray");
        }
        return arr;


    }


    /**
     * To delete the contact corresponding to a given localNumber
     */
    @SuppressWarnings("unchecked")
    public void deleteContactFromAllContactsList(String docId, String localNumber) {


        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> map = document.getProperties();

            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("allContactsArray");


            for (int i = 0; i < arr.size(); i++) {

                if (arr.get(i).get("phoneNumber").equals(localNumber)) {

                    arr.remove(i);

                    Map<String, Object> mapTemp = new HashMap<>();

                    mapTemp.putAll(map);

                    mapTemp.put("allContactsArray", arr);
                    try {
                        document.putProperties(mapTemp);
                    } catch (CouchbaseLiteException e) {
                        Log.e(TAG, "Error putting", e);
                    }

                    return;
                }
            }

        }


    }


    /**
     * To delete the contact corresponding to a given localNumber
     */
    @SuppressWarnings("unchecked")
    public void deleteActiveContact(String docId, String userId) {


        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> map = document.getProperties();

            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("contactsArray");


            for (int i = 0; i < arr.size(); i++) {


                if (arr.get(i).get("contactUid").equals(userId)) {

                    arr.remove(i);

                    Map<String, Object> mapTemp = new HashMap<>();

                    mapTemp.putAll(map);

                    mapTemp.put("contactsArray", arr);
                    try {
                        document.putProperties(mapTemp);
                    } catch (CouchbaseLiteException e) {
                        Log.e(TAG, "Error putting", e);
                    }

                    return;
                }
            }

        }


    }

    /**
     * To delete the contact corresponding to a given Follow uid
     */
    @SuppressWarnings("unchecked")
    public void deleteActiveContactByUid(String docId, String contactUid) {


        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> map = document.getProperties();

            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("contactsArray");


            for (int i = 0; i < arr.size(); i++) {

                if (arr.get(i).get("contactUid").equals(contactUid)) {

                    arr.remove(i);

                    Map<String, Object> mapTemp = new HashMap<>();

                    mapTemp.putAll(map);

                    mapTemp.put("contactsArray", arr);
                    try {
                        document.putProperties(mapTemp);
                    } catch (CouchbaseLiteException e) {
                        Log.e(TAG, "Error putting", e);
                    }

                    return;
                }
            }

        }


    }


    /**
     * To delete the contact corresponding to a given Follow uid
     */
    @SuppressWarnings("unchecked")
    public void addUpdatedContact(String docId, Map<String, Object> contact, String contactUid) {


        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> map = document.getProperties();

            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("contactsArray");


            for (int i = 0; i < arr.size(); i++) {

                if (arr.get(i).get("contactUid").equals(contactUid)) {


                    arr.set(i, contact);

                    Map<String, Object> mapTemp = new HashMap<>();

                    mapTemp.putAll(map);

                    mapTemp.put("contactsArray", arr);
                    try {
                        document.putProperties(mapTemp);
                    } catch (CouchbaseLiteException e) {
                        Log.e(TAG, "Error putting", e);
                    }

                    return;
                }
            }


            arr.add(contact);
            Map<String, Object> mapTemp = new HashMap<>();

            mapTemp.putAll(map);

            mapTemp.put("contactsArray", arr);
            try {
                document.putProperties(mapTemp);
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error putting", e);
            }


        }


    }


    /**
     * To delete the contact corresponding to a given Follow uid
     */
    @SuppressWarnings("unchecked")
    public void addActiveContact(String docId, Map<String, Object> contact, String userId) {


        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> map = document.getProperties();


            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("contactsArray");
            for (int i = 0; i < arr.size(); i++) {

                if (arr.get(i).get("contactUid").equals(userId)) {


                    arr.set(i, contact);

                    Map<String, Object> mapTemp = new HashMap<>();

                    mapTemp.putAll(map);

                    mapTemp.put("contactsArray", arr);
                    try {
                        document.putProperties(mapTemp);
                    } catch (CouchbaseLiteException e) {
                        Log.e(TAG, "Error putting", e);
                    }

                    return;
                }


            }

            arr.add(contact);

            Map<String, Object> mapTemp = new HashMap<>();

            mapTemp.putAll(map);

            mapTemp.put("contactsArray", arr);
            try {
                document.putProperties(mapTemp);
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error putting", e);
            }


        }
    }


    /**
     * To update the list of all contacts
     */
    @SuppressWarnings("unchecked")
    public void updateAllContacts(ArrayList<Map<String, Object>> arrContacts, String docId) {
        Document document = database.getDocument(docId);
        if (document != null) {
            Map<String, Object> map = document.getProperties();

            Map<String, Object> map_temp = new HashMap<>();
            map_temp.putAll(map);
            map_temp.put("allContactsArray", arrContacts);
            try {
                document.putProperties(map_temp);
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error putting", e);
            }
        }
    }


    /**
     * To check if the contact whose number has been updated was active
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> wasActiveContact(String docId, String contactId) {


        Document document = database.getDocument(docId);

        Map<String, Object> result = new HashMap<>();
        if (document != null) {
            Map<String, Object> map = document.getProperties();

            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("contactsArray");


            for (int i = 0; i < arr.size(); i++) {

                if (arr.get(i).get("contactLocalId").equals(contactId)) {


                    result.put("wasActive", true);
                    result.put("contactUid", arr.get(i).get("contactUid"));
                    result.put("contactIdentifier", arr.get(i).get("contactIdentifier"));

                    return result;
                }
            }
        }
        result.put("wasActive", false);


        return result;


    }

    /**
     * To add newly added contact to the list of all contacts
     */
    @SuppressWarnings("unchecked")
    public void addToAllContacts(String docId, Map<String, Object> contactAdded) {


        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> map = document.getProperties();

            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("allContactsArray");

            arr.add(contactAdded);


            Map<String, Object> mapTemp = new HashMap<>();

            mapTemp.putAll(map);

            mapTemp.put("allContactsArray", arr);
            try {
                document.putProperties(mapTemp);
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error putting", e);
            }


        }


    }


    @SuppressWarnings("unchecked")
    public void updateFollowStatus(String contactsDocId, String ContactUid, int status) {

        Document document = database.getDocument(contactsDocId);
        if (document != null) {
            Map<String, Object> map = document.getProperties();

            Map<String, Object> map_temp = new HashMap<>();
            map_temp.putAll(map);

            ArrayList<Map<String, Object>> arrContacts = (ArrayList<Map<String, Object>>) map_temp.get("contactsArray");
            Map<String, Object> mapContact;
            for (int i = 0; i < arrContacts.size(); i++) {
                mapContact = arrContacts.get(i);

                if (mapContact.get(Constants.DiscoverContact.CONTACT_UID).equals(ContactUid)) {
                    mapContact.put(Constants.DiscoverContact.FOLLOWING, status);
                    break;
                }
            }

            map_temp.putAll(map);
            map_temp.put("contactsArray", arrContacts);
            try {
                document.putProperties(map_temp);
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error putting", e);
            }
        }


    }


    /**
     * To add the list of the contacts to the db
     */

    public void insertContactsInfo(ArrayList<Map<String, Object>> arrContacts, String docId) {
        Document document = database.getDocument(docId);
        if (document != null) {
            Map<String, Object> map = document.getProperties();

            Map<String, Object> map_temp = new HashMap<>();
            map_temp.putAll(map);
            map_temp.put("contactsArray", arrContacts);
            try {
                document.putProperties(map_temp);
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error putting", e);
            }
        }
    }

    /**
     * To add the list of the friends to the db
     */

    public void insertFriendsInfo(ArrayList<Map<String, Object>> arrFriends, String docId) {
        Document document = database.getDocument(docId);
        if (document != null) {
            Map<String, Object> map = document.getProperties();

            Map<String, Object> map_temp = new HashMap<>();
            map_temp.putAll(map);
            map_temp.put("friendsArray", arrFriends);
            try {
                document.putProperties(map_temp);
            } catch (CouchbaseLiteException e) {
                Log.e(TAG, "Error putting", e);
            }
        }
    }


    /**
     * To update the contact name in the list of the active contacts
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> updateActiveContactName(String docId, String newName, String contactId) {
        Document document = database.getDocument(docId);

        Map<String, Object> result = new HashMap<>();
        if (document != null) {
            Map<String, Object> map = document.getProperties();

            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("contactsArray");


            for (int i = 0; i < arr.size(); i++) {

                if (arr.get(i).get("contactLocalId").equals(contactId)) {

                    Map<String, Object> contact = arr.get(i);

                    contact.put("contactName", newName);
                    arr.set(i, contact);
                    Map<String, Object> mapTemp = new HashMap<>();

                    mapTemp.putAll(map);

                    mapTemp.put("contactsArray", arr);
                    try {
                        document.putProperties(mapTemp);
                    } catch (CouchbaseLiteException e) {
                        Log.e(TAG, "Error putting", e);
                    }
                    result.put("updated", true);
                    result.put("contactUid", arr.get(i).get("contactUid"));

                    return result;
                }
            }
        }
        result.put("updated", false);


        return result;
    }


    /**
     * To update the contact name in the list of the all contacts
     */
    @SuppressWarnings("unchecked")
    public void updateAllContactName(String docId, String newName, String contactId) {
        Document document = database.getDocument(docId);
        if (document != null) {
            Map<String, Object> map = document.getProperties();

            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("allContactsArray");


            for (int i = 0; i < arr.size(); i++) {

                if (arr.get(i).get("contactId").equals(contactId)) {

                    Map<String, Object> contact = arr.get(i);

                    contact.put("userName", newName);
                    arr.set(i, contact);
                    Map<String, Object> mapTemp = new HashMap<>();

                    mapTemp.putAll(map);

                    mapTemp.put("allContactsArray", arr);
                    try {
                        document.putProperties(mapTemp);
                    } catch (CouchbaseLiteException e) {
                        Log.e(TAG, "Error putting", e);
                    }

                    return;
                }
            }
        }

    }

    /**
     * To update the contact following status
     */
    @SuppressWarnings("unchecked")
    public void updateContactFollowingStatus(String docId, int following, String id) {
        Document document = database.getDocument(docId);
        if (document != null) {
            Map<String, Object> map = document.getProperties();
            ArrayList<Map<String, Object>> arr = AppController.getInstance().getDbController().fetchAllContacts(docId);

            for (int i = 0; i < arr.size(); i++) {

                if (arr.get(i).get("contactUid").toString().equals(id)) {

                    Map<String, Object> contact = arr.get(i);
                    contact.put("following", following);
                    arr.set(i, contact);
                    Map<String, Object> mapTemp = new HashMap<>();

                    mapTemp.putAll(map);
                    mapTemp.put("allContactsArray", arr);
                    try {
                        document.putProperties(mapTemp);
                    } catch (CouchbaseLiteException e) {
                        Log.e(TAG, "Error putting", e);
                    }

                    return;
                }
            }
        }

    }


    /**
     * To update the contact name in the list of the all contacts
     */
    @SuppressWarnings("unchecked")
    public void updateAllContactNunber(String docId, String newNumber, String contactId) {
        Document document = database.getDocument(docId);
        if (document != null) {
            Map<String, Object> map = document.getProperties();

            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("allContactsArray");


            for (int i = 0; i < arr.size(); i++) {

                if (arr.get(i).get("contactId").equals(contactId)) {

                    Map<String, Object> contact = arr.get(i);

                    contact.put("phoneNumber", newNumber);
                    arr.set(i, contact);
                    Map<String, Object> mapTemp = new HashMap<>();

                    mapTemp.putAll(map);

                    mapTemp.put("allContactsArray", arr);
                    try {
                        document.putProperties(mapTemp);
                    } catch (CouchbaseLiteException e) {
                        Log.e(TAG, "Error putting", e);
                    }

                    return;
                }
            }
        }

    }

    public void updateChatId(String docId, String chatId) {

        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();


            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.putAll(map);

            mapTemp.put("chatId", chatId);


            if (!mapTemp.containsKey("canHaveMoreMessages")) {

                mapTemp.put("canHaveMoreMessages", true);
                mapTemp.put("partiallyRetrieved", true);
            }

            try {

                document.putProperties(mapTemp);

            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }


            database.clearDocumentCache();
        }


    }




    /*
     * For the secret chat
     */


    /*
     * To update the destruction time for the secret chat
     */


    public void updateDestructTime(String docId, long dTime) {

        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();

            if (map == null) {
                return;
            }

            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.putAll(map);

            mapTemp.put("dTime", dTime);

            try {

                document.putProperties(mapTemp);

            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }


            database.clearDocumentCache();
        }


    }


    /*
     * To update the image for the secret invite image
     */
    public void updateSecretInviteImageVisibility(String docId, boolean imgVisibility) {

        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();

            if (map == null) {
                return;
            }

            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.putAll(map);

            mapTemp.put("secretInviteVisibility", imgVisibility);

            try {

                document.putProperties(mapTemp);

            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }


            database.clearDocumentCache();
        }


    }

    /**
     * Set timer started
     */


    @SuppressWarnings("unchecked")
    public void setTimerStarted(String docId, String messageId, long expectedDTime) {

        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();


            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("messsageArray");

            Map<String, Object> message;
            for (int i = arr.size() - 1; i >= 0; i--) {


                if (arr.get(i).get("id").equals(messageId)) {

                    message = arr.get(i);

                    message.put("timerStarted", true);
                    message.put("expectedDTime", expectedDTime);
                    arr.set(i, message);
                    break;
                }

            }
            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.putAll(map);
            mapTemp.put("messsageArray", arr);

            try {

                document.putProperties(mapTemp);

            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }


            database.clearDocumentCache();
        }


    }


//    /*
//       * To create the active timers document
//       */
//
//    private String createActiveTimersDocument() {
//        Document document = database.createDocument();
//        String documentId = document.getId();
//        Map<String, Object> map = new HashMap<>();
//
//        map.put("activeTimersArray", new ArrayList<Map<String, String>>());
//        try {
//            document.putProperties(map);
//        } catch (CouchbaseLiteException e) {
//            Log.e(TAG, "Error putting", e);
//        }
//        return documentId;
//    }
//
//
//    /*
//     *
//     * Have to add the list of the active timers at the time of the app shutdown or the
//     */
//
//
//    public void saveActiveTimers(String docId, ArrayList<Map<String, Object>> arrayList) {
//        database.clearDocumentCache();
//        Document document = database.getDocument(docId);
//
//        if (document != null) {
//
//            Map<String, Object> map = document.getProperties();
//
//
//            Map<String, Object> mapTemp = new HashMap<>();
//            mapTemp.putAll(map);
//
//            mapTemp.put("activeTimersArray", arrayList);
//
//            try {
//
//                document.putProperties(mapTemp);
//
//            } catch (CouchbaseLiteException e) {
//                e.printStackTrace();
//            }
//            map = null;
//            mapTemp = null;
//            document = null;
//
//            database.clearDocumentCache();
//        }
//
//    }
//
//     /*
//     *
//     * Have to fetch the list of the active timers at the time of the phone restart or app start
//     */
//
//
//    @SuppressWarnings("unchecked")
//    public ArrayList<Map<String, Object>> fetchActiveTimers(String docId) {
//        database.clearDocumentCache();
//
//
//        ArrayList<Map<String, Object>> arr = new ArrayList<>();
//
//        Document document = database.getDocument(docId);
//
//        if (document != null) {
//
//            Map<String, Object> map = document.getProperties();
//            arr = (ArrayList<Map<String, Object>>) map.get("activeTimersArray");
//
//        }
//
//
//        return arr;
//    }


    /**
     * @return receiver identifier for the given chat for which the messages are fetched
     */


    @SuppressWarnings("unchecked")
    public String fetchReceiverIdentifierFromChatId(String docId, String chatId) {
        String receiverIdentifier = "";
        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();


            if (map.get("chatId").equals(chatId)) {

                return (String) map.get("receiverIdentifier");
            }
        }


        return receiverIdentifier;
    }





    /*
     * For clubbing of the notifications
     */


    /*
     * For updating the content of a notification
     */
    @SuppressWarnings("unchecked")
    public void addOrUpdateNotificationContent(String docId, String notificationId, Map<String, Object> notification) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();


            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("notificationArray");


            for (int i = arr.size() - 1; i >= 0; i--) {


                if (arr.get(i).get("notificationId").equals(notificationId)) {


                    arr.set(i, notification);

                    Map<String, Object> mapTemp = new HashMap<>();
                    mapTemp.putAll(map);
                    mapTemp.put("notificationArray", arr);

                    try {

                        document.putProperties(mapTemp);

                    } catch (CouchbaseLiteException e) {
                        e.printStackTrace();
                    }
                    return;
                }

            }


            arr.add(notification);
            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.putAll(map);
            mapTemp.put("notificationArray", arr);

            try {

                document.putProperties(mapTemp);

            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }


            database.clearDocumentCache();
        }

    }

    @SuppressWarnings("unchecked")
    public int removeNotification(String docId, String notificationId) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);
        int systemNotificationId = -1;
        if (document != null) {

            Map<String, Object> map = document.getProperties();


            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("notificationArray");


            for (int i = arr.size() - 1; i >= 0; i--) {


                if (arr.get(i).get("notificationId").equals(notificationId)) {

                    systemNotificationId = (int) (arr.get(i).get("systemNotificationId"));
                    arr.remove(i);

                    Map<String, Object> mapTemp = new HashMap<>();
                    mapTemp.putAll(map);
                    mapTemp.put("notificationArray", arr);

                    try {

                        document.putProperties(mapTemp);

                    } catch (CouchbaseLiteException e) {
                        e.printStackTrace();
                    }
                    break;
                }

            }


            database.clearDocumentCache();
        }
        return systemNotificationId;
    }


    /**
     * For fetching the info for all of the notifications
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Map<String, Object>> fetchAllNotifications(String docId) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);
        ArrayList<Map<String, Object>> arr = new ArrayList<>();
        if (document != null) {

            Map<String, Object> map = document.getProperties();

            arr = (ArrayList<Map<String, Object>>) map.get("notificationArray");


            database.clearDocumentCache();
        }
        return arr;
    }


//    /**
//     * For fetching the info for a particular notification
//     */
//    @SuppressWarnings("unchecked")
//    public Map<String, Object> getParticularNotification(String docId, String notificationId) {
//
//
//        database.clearDocumentCache();
//        Document document = database.getDocument(docId);
//
//        if (document != null) {
//
//            Map<String, Object> map = document.getProperties();
//
//            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("notificationArray");
//
//
//            for (int i = 0; i < arr.size(); i++) {
//
//
//                if (arr.get(i).get("notificationId").equals(notificationId)) {
//
//
//                    return arr.get(i);
//                }
//            }
//        }
//
//        database.clearDocumentCache();
//        return null;
//    }


    /**
     * @param docId document id of the secret chat in which to add the message deleted key
     */
    public void addMessageDeletedKey(String docId) {
        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();


            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.putAll(map);
            mapTemp.put("messageDeleted", true);

            try {

                document.putProperties(mapTemp);

            } catch (CouchbaseLiteException e) {
                e.printStackTrace();

            }

        }


    }


    @SuppressWarnings("unchecked")

    public Map<String, Object> getMessageDetails(String docId, String messageId) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        if (document != null) {

            Map<String, Object> map = document.getProperties();

            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("messsageArray");


            for (int i = 0; i < arr.size(); i++) {

                if (arr.get(i).get("id").equals(messageId)) {


                    return arr.get(i);
                }


            }


            database.clearDocumentCache();
        }


        return null;


    }


//    @SuppressWarnings("unchecked")
//    public String checkContactExists(String docId, String contactUid) {
//
//
//        database.clearDocumentCache();
//        Document document = database.getDocument(docId);
//
//        if (document != null) {
//            Map<String, Object> map = document.getProperties();
//
//
//            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("contactsArray");
//
//
//            for (int i = 0, len = arr.size(); i < len; i++) {
//
//
//                if (arr.get(i).get("contactUid").equals(contactUid)) {
//
//                    return (String) arr.get(i).get("contactIdentifier");
//
//
//                }
//            }
//
//
//        }
//        return null;
//
//    }

    @SuppressWarnings("unchecked")
    public String checkFriendExists(String docId, String contactUid) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> map = document.getProperties();


            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("friendsArray");


            for (int i = 0, len = arr.size(); i < len; i++) {


                if (arr.get(i).get("userId").equals(contactUid)) {

                    return (String) arr.get(i).get("userName");


                }
            }


        }
        return null;

    }


    @SuppressWarnings("unchecked")

    public boolean checkIfLastMessage(String docId, String messageId) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("messsageArray");


        try {


            if (arr.get(arr.size() - 1).get("id").equals(messageId)) {
                return true;
            }
        } catch (IndexOutOfBoundsException e) {


            return false;
        }


        database.clearDocumentCache();


        return false;


    }



    /*
     * For the group chats
     */


    /*
     * To create document containing all the group chats info
     * */
    private String createGroupChatsDocument() {
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<>();

        map.put("groupIdsArray", new ArrayList<String>());

        map.put("groupDocIdsArray", new ArrayList<String>());

        map.put("isActive", true);
        try {
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }


    @SuppressWarnings("unchecked")
    public String checkGroupChatExists(String docId, String groupId) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        ArrayList<String> arr = (ArrayList<String>) map.get("groupIdsArray");


        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i).equals(groupId)) {
                database.clearDocumentCache();

                return (((ArrayList<String>) map.get("groupDocIdsArray")).get(i));
            }

        }


        database.clearDocumentCache();

        return null;
    }

    @SuppressWarnings("unchecked")
    public void addGroupChat(String docId, String groupId, String groupDocId) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        ArrayList<String> groupIdsArray = (ArrayList<String>) map.get("groupIdsArray");
        ArrayList<String> groupDocIdsArray = (ArrayList<String>) map.get("groupDocIdsArray");

        boolean groupExists = false;
        for (int i = 0; i < groupIdsArray.size(); i++) {
            if (groupIdsArray.get(i).equals(groupId)) {
                groupDocIdsArray.set(i, groupDocId);

                groupExists = true;
                break;

            }


        }
        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);

        if (!groupExists) {

            groupIdsArray.add(groupId);
            groupDocIdsArray.add(groupDocId);
        }


        mapTemp.put("groupIdsArray", groupIdsArray);
        mapTemp.put("groupDocIdsArray", groupDocIdsArray);

        try {

            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        database.clearDocumentCache();

    }

    @SuppressWarnings("unchecked")
    public String fetchGroupChatDocumentId(String docId, String groupId) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        ArrayList<String> groupIdsArray = (ArrayList<String>) map.get("groupIdsArray");
        ArrayList<String> groupDocIdsArray = (ArrayList<String>) map.get("groupDocIdsArray");


        for (int i = 0; i < groupIdsArray.size(); i++) {
            if (groupIdsArray.get(i).equals(groupId)) {

                return groupDocIdsArray.get(i);
            }

        }


        database.clearDocumentCache();
        return null;
    }


    public String createGroupMembersDocument() {

        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<>();




        /*
         * Have removed json array since,couchbase not able to serialize json array
         */


        map.put("groupMembersArray", new ArrayList<Map<String, Object>>());



        /*
         * For displaying of the group created info
         */

        map.put("createdByMemberId", "");
        map.put("createdByMemberIdentifier", "");

        map.put("createdAt", 0);


        map.put("isActive", true);


        try {
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }


    /**
     * To add the group members info in the individual groupchat document
     */

    @SuppressWarnings("unchecked")


    public void addGroupMembersDetails(String docId, ArrayList<Map<String, Object>> members, String creatorId,
                                       String creatorIdentifier, long createdAt, boolean isActive) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);


        mapTemp.put("groupMembersArray", members);
        mapTemp.put("createdByMemberId", creatorId);
        mapTemp.put("createdByMemberIdentifier", creatorIdentifier);

        mapTemp.put("createdAt", createdAt);


        mapTemp.put("isActive", isActive);
        try {

            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        database.clearDocumentCache();


    }


    /*
     * To add a member to the group chat
     */

    @SuppressWarnings("unchecked")
    public void addGroupMember(String docId, Map<String, Object> member, String memberId) {


        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        ArrayList<Map<String, Object>> members = (ArrayList<Map<String, Object>>) map.get("groupMembersArray");
        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);
        boolean memberExists = false;
        /*
         * To avoid creation of the multiple docs for single group chat,incase message for group creation comes more than once
         */
        for (int i = 0; i < members.size(); i++) {


            if (members.get(i).get("memberId").equals(memberId)) {

                memberExists = true;

                members.set(i, member);
                break;
            }


        }

        if (!memberExists) {

            members.add(member);
        }


        mapTemp.put("groupMembersArray", members);


        if (memberId.equals(AppController.getInstance().getUserId())) {


            mapTemp.put("isActive", true);


        }


        try {

            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        database.clearDocumentCache();

    }


    /*
     *To remove a member from the group chat
     */


    @SuppressWarnings("unchecked")
    public void removeGroupMember(String docId, String memberId) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        ArrayList<Map<String, Object>> members = (ArrayList<Map<String, Object>>) map.get("groupMembersArray");


        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);


        for (int i = 0; i < members.size(); i++) {


            if (members.get(i).get("memberId").equals(memberId)) {

                members.remove(i);
                break;
            }


        }


        mapTemp.put("groupMembersArray", members);

        if (memberId.equals(AppController.getInstance().getUserId())) {


            mapTemp.put("isActive", false);


        }
        try {

            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        database.clearDocumentCache();


    }

    /*
     * To make member as the groupAdmin
     */


    @SuppressWarnings("unchecked")
    public void makeGroupAdmin(String docId, String memberId) {
        database.clearDocumentCache();

        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        ArrayList<Map<String, Object>> members = (ArrayList<Map<String, Object>>) map.get("groupMembersArray");


        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);


        for (int i = 0; i < members.size(); i++) {


            if (members.get(i).get("memberId").equals(memberId)) {


                Map<String, Object> member = members.get(i);


                member.put("memberIsAdmin", true);
                members.set(i, member);
                break;
            }


        }


        mapTemp.put("groupMembersArray", members);


        try {

            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        database.clearDocumentCache();


    }


//    /*
//     * To leave a group
//     *
//     */
//
//
//    @SuppressWarnings("unchecked")
//    public void toLeaveTheGroup(String docId, String userId) {
//
//
//        database.clearDocumentCache();
//
//        Document document = database.getDocument(docId);
//
//
//        Map<String, Object> map = document.getProperties();
//
//
//        ArrayList<Map<String, Object>> members = (ArrayList<Map<String, Object>>) map.get("groupMembersArray");
//
//
//        Map<String, Object> mapTemp = new HashMap<>();
//        mapTemp.putAll(map);
//        mapTemp.put("isActive", false);
//
//
//        for (int i = 0; i < members.size(); i++) {
//
//
//            if (members.get(i).get("memberId").equals(userId)) {
//
//                members.remove(i);
//                break;
//            }
//
//
//        }
//
//
//        mapTemp.put("groupMembersArray", members);
//
//
//        try {
//
//            document.putProperties(mapTemp);
//
//        } catch (CouchbaseLiteException e) {
//            e.printStackTrace();
//        }
//        database.clearDocumentCache();
//
//    }


    /*
     *
     * To check if current user is active on the group chat
     */

    public boolean checkIfActive(String docId) {

        database.clearDocumentCache();

        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();

        return (boolean) map.get("isActive");
    }



    /*
     *To remove a member from the group chat
     */


    @SuppressWarnings("unchecked")
    public ArrayList<Map<String, Object>> fetchGroupMember(String docId) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();

        database.clearDocumentCache();
        return (ArrayList<Map<String, Object>>) map.get("groupMembersArray");


    }


    @SuppressWarnings("unchecked")
    public Map<String, Object> fetchGroupInfo(String docId) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);


        database.clearDocumentCache();
        return document.getProperties();


    }


    /*
     * update group icon
     */
    public void updateGroupIcon(String docId, String imageUrl) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);
        mapTemp.put("receiverImage", imageUrl);

        try {
            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }


        database.clearDocumentCache();


    }


    /*
     * update group subject
     */
    public void updateGroupSubject(String docId, String subject) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);
        mapTemp.put("receiverName", subject);

        try {
            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }


        database.clearDocumentCache();


    }


    /*
     * To fetch group subject
     */
    public String getGroupSubject(String docId) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();

        database.clearDocumentCache();
        return (String) map.get("receiverName");


    }

    /*
     * To fetch group image url
     */
    public String getGroupImageUrl(String docId) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();

        database.clearDocumentCache();
        return (String) map.get("receiverImage");


    }


    /*
     * To fetch group image url
     */
    public Map<String, Object> getGroupSubjectAndImageUrl(String docId) {

        Map<String, Object> mapTemp = null;
        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        try {
            Map<String, Object> map = document.getProperties();

            mapTemp = new HashMap<>();
            mapTemp.put("receiverImage", map.get("receiverImage"));
            mapTemp.put("receiverName", map.get("receiverName"));


        } catch (Exception e) {

            /*
             * To handle the chat being deleted before message has been sent from the list of unsent messages
             */
            e.printStackTrace();
        }
        database.clearDocumentCache();
        return mapTemp;


    }


    /**
     * To update the group name and group image.
     */
    public void updateGroupNameAndImage(String docId, String receiverName, String receiverImage) {
        database.clearDocumentCache();
        Document document = database.getDocument(docId);


        Map<String, Object> mapOld = document.getProperties();


        Map<String, Object> mapTemp = new HashMap<>();


        mapTemp.putAll(mapOld);


        mapTemp.put("receiverName", receiverName);
        mapTemp.put("receiverImage", receiverImage);


        try {
            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            com.couchbase.lite.util.Log.e(TAG, "Error putting", e);
        }

        database.clearDocumentCache();


    }

    @SuppressWarnings("unchecked")

    private void removeGroupChat(String docId, String groupDocId) {


        database.clearDocumentCache();

        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        ArrayList<String> groupIdsArray = (ArrayList<String>) map.get("groupIdsArray");
        ArrayList<String> groupDocIdsArray = (ArrayList<String>) map.get("groupDocIdsArray");


        for (int i = 0; i < groupIdsArray.size(); i++) {
            if (groupDocIdsArray.get(i).equals(groupDocId)) {
                groupDocIdsArray.remove(i);

                groupIdsArray.remove(i);
                break;

            }


        }
        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);


        mapTemp.put("groupIdsArray", groupIdsArray);
        mapTemp.put("groupDocIdsArray", groupDocIdsArray);

        try {

            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        database.clearDocumentCache();
    }


    /*
     * For the mute chats functionality
     *
     */


    private String createMuteChatsDocument() {


        database.clearDocumentCache();

        Document document = database.createDocument();
        Map<String, Object> map = new HashMap<>();

        map.put("receiverUidArray", new ArrayList<String>());
        /*
         * For the secret chat
         */

        map.put("secretIdArray", new ArrayList<String>());

        try {
            document.putProperties(map);

        } catch (CouchbaseLiteException e) {
            com.couchbase.lite.util.Log.e(TAG, "Error putting", e);
        }

        database.clearDocumentCache();
        return document.getId();
    }

    @SuppressWarnings("unchecked")
    public void addMuteChat(String docId, String receiverUid, String secretId) {

        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        ArrayList<String> opponent = (ArrayList<String>) map.get("receiverUidArray");

        ArrayList<String> secretIds = (ArrayList<String>) map.get("secretIdArray");


        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);
        boolean opponentExists = false;

        for (int i = 0; i < opponent.size(); i++) {


            if (opponent.get(i).equals(receiverUid)) {


                if (secretIds.get(i).equals(secretId)) {
                    opponentExists = true;


                    break;
                }
            }


        }

        if (!opponentExists) {

            opponent.add(receiverUid);

            secretIds.add(secretId);
        }


        mapTemp.put("receiverUidArray", opponent);
        mapTemp.put("secretIdArray", secretIds);


        try {

            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        database.clearDocumentCache();

    }


    @SuppressWarnings("unchecked")
    public boolean checkIfReceiverChatMuted(String docId, String receiverUid, String secretId) {

        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> map = document.getProperties();


            ArrayList<String> members = (ArrayList<String>) map.get("receiverUidArray");

            ArrayList<String> secretIds = (ArrayList<String>) map.get("secretIdArray");


            for (int i = 0; i < members.size(); i++) {


                if (members.get(i).equals(receiverUid)) {


                    if (secretIds.get(i).equals(secretId)) {
                        return true;
                    }
                }


            }

        }
        database.clearDocumentCache();

        return false;
    }


    @SuppressWarnings("unchecked")
    public void removeMuteChat(String docId, String receiverUid, String secretId) {


        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        ArrayList<String> opponent = (ArrayList<String>) map.get("receiverUidArray");

        ArrayList<String> secretIds = (ArrayList<String>) map.get("secretIdArray");


        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);

        boolean exists = false;
        for (int i = 0; i < opponent.size(); i++) {


            if (opponent.get(i).equals(receiverUid)) {


                if (secretIds.get(i).equals(secretId)) {
                    opponent.remove(i);


                    secretIds.remove(i);
                    exists = true;
                    break;
                }
            }


        }

        if (exists) {
            mapTemp.put("receiverUidArray", opponent);
            mapTemp.put("secretIdArray", secretIds);


            try {

                document.putProperties(mapTemp);

            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        }
        database.clearDocumentCache();

    }

    /*
     * For the wallpapers
     */

    public void addWallpaper(String docId, int wallpaperType, String wallpaperData) {


        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);


        mapTemp.put("wallpaperType", wallpaperType);
        mapTemp.put("wallpaperData", wallpaperData);


        try {

            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        database.clearDocumentCache();


    }

    /**
     * No wallpaper
     */
    public void removeWallpaper(String docId) {


        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);

        if (mapTemp.containsKey("wallpaperData")) {

            mapTemp.remove("wallpaperData");
        }
        mapTemp.put("wallpaperType", 2);
        try {

            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        database.clearDocumentCache();


    }


    public Map<String, Object> getWallpaperDetails(String docId) {


        Document document = database.getDocument(docId);


        Map<String, Object> map = document.getProperties();


        Map<String, Object> mapTemp = new HashMap<>();


        mapTemp.put("wallpaperType", map.get("wallpaperType"));
        mapTemp.put("wallpaperData", map.get("wallpaperData"));


        database.clearDocumentCache();


        return mapTemp;
    }

    /*
     * For the block user functionality
     */


    @SuppressWarnings("unchecked")
    public void addBlockedUser(String docId, String userId, String userIdentifier, boolean self) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);


        boolean exists = false;

        if (document != null) {
            Map<String, Object> map = document.getProperties();

            Map<String, Object> contactInfo;
            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("friendsArray");


            for (int i = 0, len = arr.size(); i < len; i++) {


                if (arr.get(i).get("userId").equals(userId)) {


                    contactInfo = arr.get(i);
                    contactInfo.put("blocked", true);

                    contactInfo.put("self", self);

                    arr.set(i, contactInfo);
                    exists = true;
                    break;
                }
            }

            if (exists) {
                Map<String, Object> mapTemp = new HashMap<>();
                mapTemp.putAll(map);


                mapTemp.put("friendsArray", arr);
                try {

                    document.putProperties(mapTemp);

                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }


            }


            contactInfo = new HashMap<>();


            contactInfo.put("userId", userId);
            contactInfo.put("userIdentifier", userIdentifier);
            contactInfo.put("self", self);
            addUserToBlockList(userId, contactInfo);

        }


    }


    @SuppressWarnings("unchecked")
    public void removeUnblockedUser(String docId, String userId) {

        database.clearDocumentCache();
        Document document = database.getDocument(docId);


        boolean exists = false;

        if (document != null) {
            Map<String, Object> map = document.getProperties();

            Map<String, Object> contactInfo;
            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("friendsArray");


            for (int i = 0, len = arr.size(); i < len; i++) {


                if (arr.get(i).get("userId").equals(userId)) {


                    contactInfo = arr.get(i);

                    if (contactInfo.containsKey("blocked")) {
                        contactInfo.remove("blocked");

                        arr.set(i, contactInfo);
                        exists = true;
                    }
                    break;
                }
            }

            if (exists) {
                Map<String, Object> mapTemp = new HashMap<>();
                mapTemp.putAll(map);


                mapTemp.put("friendsArray", arr);
                try {

                    document.putProperties(mapTemp);

                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }

            }
        }

        removeUserFromBlockList(userId);
    }


    //    @SuppressWarnings("unchecked")
//    public boolean checkIfBlocked(String docId, String userId) {
//
//        database.clearDocumentCache();
//        Document document = database.getDocument(docId);
//
//
//        if (document != null) {
//            Map<String, Object> map = document.getProperties();
//
//            Map<String, Object> contactInfo;
//            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("contactsArray");
//
//
//            for (int i = 0, len = arr.size(); i < len; i++) {
//
//
//                if (arr.get(i).get("contactUid").equals(userId)) {
//
//
//                    contactInfo = arr.get(i);
//
//                    if (contactInfo.containsKey("blocked")) {
//
//                        return true;
//                    }
//                    break;
//                }
//            }
//        }
//        return false;
//    }
    @SuppressWarnings("unchecked")
    public boolean checkIfBlocked(String docId, String userId) {

        database.clearDocumentCache();
        Document document = database.getDocument(docId);


        if (document != null) {
            Map<String, Object> map = document.getProperties();


            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("blockedArray");


            for (int i = 0, len = arr.size(); i < len; i++) {


                if (arr.get(i).get("userId").equals(userId)) {


                    return true;


                }
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public boolean checkIfSelfBlocked(String docId, String userId) {

        database.clearDocumentCache();
        Document document = database.getDocument(docId);


        if (document != null) {
            Map<String, Object> map = document.getProperties();

            Map<String, Object> contactInfo;
            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("friendsArray");


            for (int i = 0, len = arr.size(); i < len; i++) {


                if (arr.get(i).get("userId").equals(userId)) {


                    contactInfo = arr.get(i);

                    if (contactInfo.containsKey("blocked") && (boolean) contactInfo.get("self")) {

                        return true;


                    }
                    break;
                }
            }
        }
        return false;
    }

    /*
     * For the mute chats functionality
     *
     */


    private String createBlockedUsersDocument() {


        database.clearDocumentCache();

        Document document = database.createDocument();
        Map<String, Object> map = new HashMap<>();


        map.put("blockedArray", new ArrayList<Map<String, Object>>());


        try {
            document.putProperties(map);

        } catch (CouchbaseLiteException e) {
            com.couchbase.lite.util.Log.e(TAG, "Error putting", e);
        }

        database.clearDocumentCache();
        return document.getId();
    }


    @SuppressWarnings("unchecked")
    private void addUserToBlockList(String userId, Map<String, Object> userInfo) {


        database.clearDocumentCache();
        Document document = database.getDocument(AppController.getInstance().getBlockedDocId());

        boolean exists = false;


        if (document != null) {
            Map<String, Object> map = document.getProperties();


            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("blockedArray");


            for (int i = 0, len = arr.size(); i < len; i++) {


                if (arr.get(i).get("userId").equals(userId)) {

                    arr.set(i, userInfo);

                    exists = true;
                    break;
                }
            }

            if (!exists) {


                arr.add(userInfo);
            }

            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.putAll(map);


            mapTemp.put("blockedArray", arr);
            try {

                document.putProperties(mapTemp);

            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }


        }

    }

    @SuppressWarnings("unchecked")
    private void removeUserFromBlockList(String userId) {


        database.clearDocumentCache();
        Document document = database.getDocument(AppController.getInstance().getBlockedDocId());

        boolean exists = false;


        if (document != null) {
            Map<String, Object> map = document.getProperties();


            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("blockedArray");


            for (int i = 0, len = arr.size(); i < len; i++) {


                if (arr.get(i).get("userId").equals(userId)) {

                    arr.remove(i);

                    exists = true;
                    break;
                }
            }

            if (exists) {


                Map<String, Object> mapTemp = new HashMap<>();
                mapTemp.putAll(map);


                mapTemp.put("blockedArray", arr);
                try {

                    document.putProperties(mapTemp);

                } catch (CouchbaseLiteException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    /**
     * To fetch the contact name corresponding to a given uid/number
     */
    @SuppressWarnings("unchecked")
    public ArrayList<Map<String, Object>> loadBlockedUsers(String docId) {

        ArrayList<Map<String, Object>> arr = new ArrayList<>();
        Document document = database.getDocument(docId);

        if (document != null) {
            Map<String, Object> map = document.getProperties();

            arr = (ArrayList<Map<String, Object>>) map.get("blockedArray");
        }
        return arr;


    }


    /*
     * To setup the searchView, which is to be eventually used fior searching for the text messages.
     */


    @SuppressWarnings("unchecked")
    private void setupSearchView() {

        View searchView = database.getView("searchView");

        searchView.setMap(new Mapper() {
            @Override
            public void map(Map<String, Object> document, Emitter emitter) {

                if (document.containsKey("messsageArray")) {

                    ArrayList<Map<String, Object>> messagesArray =
                            (ArrayList<Map<String, Object>>) document.get("messsageArray");


                    for (int i = 0; i < messagesArray.size(); i++) {

                        /*
                         * Indexing only the text messages
                         */


                        try {

                            if (messagesArray.get(i).get("messageType").equals("0")) {

                                String actualMessage = (String) messagesArray.get(i).get("message");


                                if (!actualMessage.isEmpty()) {
                                    /*
                                     *To avoid secret chat tag messages
                                     */


                                    /*
                                     * As of now will map the message id and the corresponding payload
                                     */


                                    Map<String, Object> viewInfo = new HashMap<>();

                                    viewInfo.put("id", messagesArray.get(i).get("id"));
                                    viewInfo.put("position", i);
                                    viewInfo.put("message", actualMessage);
                                    viewInfo.put("receiverUid", document.get("selfUid"));

                                    viewInfo.put("groupChat", document.get("groupChat"));


                                    if ((boolean) document.get("groupChat")) {


                                        viewInfo.put("receiverName", document.get("receiverName"));

                                        viewInfo.put("receiverImage", document.get("receiverImage"));


                                    } else {
                                        viewInfo.put("receiverIdentifier", document.get("receiverIdentifier"));

                                        viewInfo.put("secretId", document.get("secretId"));

                                    }

                                    viewInfo.put("timestamp", messagesArray.get(i).get("Ts"));


                                    emitter.emit(messagesArray.get(i).get("id"), viewInfo);


//                                    emitter.emit(messagesArray.get(i).get("id"), actualMessage);

                                }


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

            }
        }, "0");


    }


    /**
     * @param searchString the text to be searched for among the text messages
     * @return ArrayList<Map   <   String   ,       Object>> containing the list of the messages
     */

    @SuppressWarnings("unchecked,TryWithIdenticalCatches")
    public ArrayList<Map<String, Object>> searchTextMessages(String searchString) {
        searchString = searchString.toLowerCase(Locale.US);

        Query query = database.getView("searchView").createQuery();
//        query.setLimit(20);

        ArrayList<Map<String, Object>> arrayList = new ArrayList<>();

        Map<String, Object> contactInfo;


        String contactsDocId = AppController.getInstance().getFriendsDocId();

        try {
            QueryEnumerator result = query.run();


            for (Iterator<QueryRow> it = result; it.hasNext(); ) {


                QueryRow row = it.next();


                Map<String, Object> messageInfo = (Map<String, Object>) row.getValue();

                if (((String) messageInfo.get("message")).toLowerCase(Locale.US).contains(searchString)) {
                    messageInfo.put("docId", row.getSourceDocumentId());
                    if (!(boolean) messageInfo.get("groupChat")) {

                        /*
                         * Have to search in contacts
                         */

                        String receiverName = (String) messageInfo.get("receiverIdentifier");


                        String receiverImage = "";

                        String receiverUid = (String) messageInfo.get("receiverUid");

                        contactInfo = getFriendInfoFromUid(contactsDocId, receiverUid);

                        if (contactInfo != null) {

                            messageInfo.put("receiverName", contactInfo.get("firstName") + " " + contactInfo.get("lastName"));

                            String image = (String) contactInfo.get("profilePic");


                            if (image != null && !image.isEmpty()) {

                                receiverImage = image;


                            }

                        } else {
                            messageInfo.put("receiverName", receiverName);


                        }

                        messageInfo.put("receiverImage", receiverImage);
                        messageInfo.put("secretId", messageInfo.get("secretId"));


                    }

                    messageInfo.put("timestamp", messageInfo.get("timestamp"));
                    arrayList.add(messageInfo);
                }
            }


        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }


    /**
     * To create the index for searching using the view on the text messages across all the chats.
     * <p>
     * Have been put on the background thread intentionally to avoid delay in opening of the app,due to time involved
     * in creation of the search indexes.
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private class CreateSearchIndexes extends android.os.AsyncTask {


        @Override
        protected Object doInBackground(Object[] params) {

            setupSearchView();
            return null;
        }
    }


    /**
     * in each of the document we are storing the last message date and last message time so that if flag hasNewMessage is set
     * then we can show that and also it will help us to sort chatlist based on the most recent
     */

    public void updateChatListForRemovedOrEditedMessage(String docId, String lastmessage,
                                                        boolean hasNewMessage, String lastMessageDate) {


        database.clearDocumentCache();
        Document document = database.getDocument(docId);

        Map<String, Object> map = document.getProperties();


        if (map == null) {
            return;
        }


        try {


            lastmessage = new String(Base64.decode(lastmessage, Base64.DEFAULT), "UTF-8");


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.putAll(map);
        mapTemp.put("hasNewMessage", hasNewMessage);
        if (hasNewMessage) {


            mapTemp.put("newMessage", lastmessage);
            mapTemp.put("newMessageTime", lastMessageDate);

            mapTemp.put("newMessageDate", lastMessageDate);


        }

        /*
         * Have put just for safety although not required
         */
        mapTemp.put("isSelf", false);
        mapTemp.put("deliveryStatus", 0);

        mapTemp.put("lastMessageDate", lastMessageDate);
        try {
            document.putProperties(mapTemp);

        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }


        database.clearDocumentCache();


    }

    @SuppressWarnings("unchecked")
    public void updateExcludedFilters(String docId, ArrayList<Integer> excludedIds) {
        Document document = database.getDocument(docId);
        Map<String, Object> mapOld = document.getProperties();
        Map<String, Object> map_temp = new HashMap<>();
        map_temp.putAll(mapOld);
        map_temp.put("excludedFilterIds", excludedIds);
        try {
            document.putProperties(map_temp);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
    }

    //add view
    private String createViewPostDocument() {
        Document document = database.createDocument();
        String documentId = document.getId();
        Map<String, Object> map = new HashMap<>();

        map.put("viewPost", new ArrayList<Map<String, Object>>());
        try {
            document.putProperties(map);
        } catch (CouchbaseLiteException e) {
            Log.e(TAG, "Error putting", e);
        }
        return documentId;
    }

    @SuppressWarnings("unchecked")
    public void addViewPost(Map<String, Object> post) {

        database.clearDocumentCache();
        Document document = database.getDocument(AppController.getInstance().getViewPostDocId());

        if (document != null) {
            Map<String, Object> map = document.getProperties();
            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("viewPost");

            Map<String, Object> mapTemp = new HashMap<>();
            mapTemp.putAll(map);
            arr.add(post);

            mapTemp.put("viewPost", arr);
            try {

                document.putProperties(mapTemp);

            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        }

    }

    @SuppressWarnings("unchecked")
    public boolean isPostAlreadyViewed(String postId,String userId) {

        database.clearDocumentCache();
        Document document = database.getDocument(AppController.getInstance().getViewPostDocId());

        if (document != null) {
            Map<String, Object> map = document.getProperties();
            ArrayList<Map<String, Object>> arr = (ArrayList<Map<String, Object>>) map.get("viewPost");
            try {
                for (int i = 0, len = arr.size(); i < len; i++) {
                    if (arr.get(i).get("postId").equals(postId)) {
                        //Log.d(TAG, "view post: "+postId +" "+true);
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //Log.d(TAG, "view post: "+postId +" "+false);
        return false;
    }
}