package chat.hola.com.app.MessagesHandler.GroupChatMessages;

import android.content.Context;
import android.util.Base64;

import com.ezcall.android.R;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.Utilities.FileHelper;
import chat.hola.com.app.Utilities.Utilities;

public class GroupChatDatabaseHelper {
    private AppController instance = AppController.getInstance();
    private CouchDbController db = AppController.getInstance().getDbController();
    private FileHelper fileHelper = instance.getFileHelper();

    /**
     * To add the group chat message into the db
     */
    public void addGroupChatMessageInDB(String docId, Map<String, Object> groupMessageDetails,
                                         String tsInGmt, String message, boolean newMessage) {

        db.addNewChatMessageAndSort(docId, groupMessageDetails, tsInGmt, "");

        if (newMessage) {
            db.updateChatListForNewMessage(docId, message, true, tsInGmt, tsInGmt);
        }
    }

    /*
     *To save the group chat messages
     */

    public void putGroupMessageInDb(String receiverUid, String messageType, String actualMessage,
                                     String timestamp, String id, String receiverdocId, String thumbnailMessage,

                                     int dataSize, String receiverIdentifier, boolean isSelf, int status, String removedAt,
                                     boolean wasEdited) {

        byte[] data = Base64.decode(actualMessage, Base64.DEFAULT);

        byte[] thumbnailData = null;

        if ((messageType.equals("1")) || (messageType.equals("2")) || (messageType.equals("7"))) {

            thumbnailData = Base64.decode(thumbnailMessage, Base64.DEFAULT);
        }

        String name = timestamp;
        /*
         *
         *
         * initially in db we only store path of the thumbnail and nce downloaded we will replace that field withthe actual path of the downloaded file
         *
         *
         * */

        String tsInGmt = Utilities.epochtoGmt(timestamp);

        /*
         * Text message
         */
        if (messageType.equals("0")) {

            String text = "";
            try {

                text = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {

            }

            Map<String, Object> map = new HashMap<>();
            map.put("message", text);
            map.put("messageType", "0");

            if (wasEdited) {
                map.put("wasEdited", true);
            }

            map.put("isSelf", isSelf);
            map.put("from", receiverUid);

            /*
             * Group chat specific
             */
            map.put("receiverIdentifier", receiverIdentifier);

            map.put("Ts", tsInGmt);
            map.put("id", id);

            if (isSelf) {

                map.put("deliveryStatus", String.valueOf(status));
            }

            if (status == -1) {
                db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                db.updateChatListForNewMessage(receiverdocId, text, true, tsInGmt, tsInGmt);
            } else {

                db.addNewChatMessageAndSort(receiverdocId, map, null, "");
            }
        } else if (messageType.equals("1")) {
            /*
             * Image message
             */

            String thumbnailPath = fileHelper.convertByteArrayToFile(thumbnailData, name, "jpg");

            Map<String, Object> map = new HashMap<>();

            try {

                map.put("message", new String(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {

            }

            map.put("messageType", "1");
            map.put("isSelf", isSelf);
            map.put("from", receiverUid);

            /*
             * Group chat specific
             */
            map.put("receiverIdentifier", receiverIdentifier);

            map.put("Ts", tsInGmt);
            map.put("id", id);

            map.put("downloadStatus", 0);

            map.put("dataSize", dataSize);

            map.put("thumbnailPath", thumbnailPath);
            if (isSelf) {

                map.put("deliveryStatus", String.valueOf(status));
            }

            if (status == -1) {
                db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewImage), true, tsInGmt,
                        tsInGmt);
            } else {

                db.addNewChatMessageAndSort(receiverdocId, map, null, "");
            }
        } else if (messageType.equals("2")) {

            /*
             * Video message
             */
            String thumbnailPath = fileHelper.convertByteArrayToFile(thumbnailData, name, "jpg");

            Map<String, Object> map = new HashMap<>();


            /*
             *
             *
             * message key will contail the url on server until downloaded and once downloaded
             * it will contain the local path of the video or image
             *
             * */
            try {

                map.put("message", new String(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {

            }

            map.put("messageType", "2");
            map.put("isSelf", isSelf);
            map.put("from", receiverUid);
            /*
             * Group chat specific
             */
            map.put("receiverIdentifier", receiverIdentifier);

            map.put("Ts", tsInGmt);
            map.put("id", id);

            map.put("downloadStatus", 0);
            map.put("dataSize", dataSize);

            map.put("thumbnailPath", thumbnailPath);

            if (isSelf) {

                map.put("deliveryStatus", String.valueOf(status));
            }

            if (status == -1) {
                db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewVideo), true, tsInGmt,
                        tsInGmt);
            } else {
                db.addNewChatMessageAndSort(receiverdocId, map, null, "");
            }
        } else if (messageType.equals("3")) {
            /*
             * Location message
             */

            String placeString = "";
            try {

                placeString = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {

            }

            Map<String, Object> map = new HashMap<>();
            map.put("message", placeString);
            map.put("messageType", "3");
            map.put("isSelf", isSelf);
            map.put("from", receiverUid);
            /*
             * Group chat specific
             */
            map.put("receiverIdentifier", receiverIdentifier);

            map.put("Ts", tsInGmt);
            map.put("id", id);

            if (isSelf) {

                map.put("deliveryStatus", String.valueOf(status));
            }

            if (status == -1) {
                db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewLocation), true,
                        tsInGmt, tsInGmt);
            } else {
                db.addNewChatMessageAndSort(receiverdocId, map, null, "");
            }
        } else if (messageType.equals("4")) {
            /*
             * Follow message
             */

            String contactString = "";
            try {

                contactString = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {

            }

            Map<String, Object> map = new HashMap<>();
            map.put("message", contactString);
            map.put("messageType", "4");
            map.put("isSelf", isSelf);
            map.put("from", receiverUid);


            /*
             * Group chat specific
             */
            map.put("receiverIdentifier", receiverIdentifier);

            map.put("Ts", tsInGmt);
            map.put("id", id);

            if (isSelf) {

                map.put("deliveryStatus", String.valueOf(status));
            }

            if (status == -1) {
                db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewContact), true, tsInGmt,
                        tsInGmt);
            } else {
                db.addNewChatMessageAndSort(receiverdocId, map, null, "");
            }
        } else if (messageType.equals("5")) {

            /*
             * Audio message
             */
            Map<String, Object> map = new HashMap<>();

            try {

                map.put("message", new String(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {

            }

            map.put("messageType", "5");
            map.put("isSelf", isSelf);
            map.put("from", receiverUid);

            /*
             * Group chat specific
             */
            map.put("receiverIdentifier", receiverIdentifier);

            map.put("Ts", tsInGmt);
            map.put("id", id);
            map.put("downloadStatus", 0);
            map.put("dataSize", dataSize);

            if (isSelf) {

                map.put("deliveryStatus", String.valueOf(status));
            }

            if (status == -1) {
                db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewAudio), true, tsInGmt,
                        tsInGmt);
            } else {
                db.addNewChatMessageAndSort(receiverdocId, map, null, "");
            }
        } else if (messageType.equals("6")) {


            /*
             * Sticker
             */

            String text = "";
            try {

                text = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {

            }
            Map<String, Object> map = new HashMap<>();
            map.put("message", text);
            map.put("messageType", "6");
            map.put("isSelf", isSelf);
            map.put("from", receiverUid);

            /*
             * Group chat specific
             */
            map.put("receiverIdentifier", receiverIdentifier);

            map.put("Ts", tsInGmt);
            map.put("id", id);

            if (isSelf) {

                map.put("deliveryStatus", String.valueOf(status));
            }

            if (status == -1) {
                db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");

                db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewSticker), true, tsInGmt,
                        tsInGmt);
            } else {
                db.addNewChatMessageAndSort(receiverdocId, map, null, "");
            }
        } else if (messageType.equals("7")) {
            /*
             * Doodle
             */
            String thumbnailPath = fileHelper.convertByteArrayToFile(thumbnailData, name, "jpg");

            Map<String, Object> map = new HashMap<>();

            try {

                map.put("message", new String(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {

            }

            map.put("messageType", "7");
            map.put("isSelf", isSelf);
            map.put("from", receiverUid);

            /*
             * Group chat specific
             */
            map.put("receiverIdentifier", receiverIdentifier);

            map.put("Ts", tsInGmt);
            map.put("id", id);

            map.put("downloadStatus", 0);

            map.put("dataSize", dataSize);
            map.put("thumbnailPath", thumbnailPath);

            if (isSelf) {

                map.put("deliveryStatus", String.valueOf(status));
            }

            thumbnailPath = null;

            if (status == -1) {
                db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewDoodle), true, tsInGmt,
                        tsInGmt);
            } else {
                db.addNewChatMessageAndSort(receiverdocId, map, null, "");
            }
        } else if (messageType.equals("8")) {

            /*
             * Gif
             */

            String url = "";
            try {

                url = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {

            }
            Map<String, Object> map = new HashMap<>();
            map.put("message", url);
            map.put("messageType", "8");
            map.put("isSelf", isSelf);
            map.put("from", receiverUid);

            /*
             * Group chat specific
             */
            map.put("receiverIdentifier", receiverIdentifier);

            map.put("Ts", tsInGmt);
            map.put("id", id);

            if (isSelf) {

                map.put("deliveryStatus", String.valueOf(status));
            }

            if (status == -1) {
                db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewGiphy), true, tsInGmt,
                        tsInGmt);
            } else {
                db.addNewChatMessageAndSort(receiverdocId, map, null, "");
            }
        }

        /*
         * Removed message
         */
        else if (messageType.equals("11")) {

            String text = "";
            try {

                text = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {

            }

            Map<String, Object> map = new HashMap<>();
            map.put("message", text);
            map.put("messageType", "11");

            map.put("isSelf", isSelf);
            map.put("from", receiverUid);

            map.put("removedAt", removedAt);




            /*
             * Group chat specific
             */
            map.put("receiverIdentifier", receiverIdentifier);

            map.put("Ts", tsInGmt);
            map.put("id", id);

            if (isSelf) {

                map.put("deliveryStatus", String.valueOf(status));
            }

            if (status == -1) {
                db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                db.updateChatListForNewMessage(receiverdocId, text, true, tsInGmt, tsInGmt);
            } else {

                db.addNewChatMessageAndSort(receiverdocId, map, null, "");
            }
        }
    }

    public void putGroupMessageInDb(String receiverUid, String actualMessage, String timestamp,
                                     String id, String receiverdocId,

                                     int dataSize, boolean isSelf, int status, String mimeType, String fileName, String extension,
                                     String receiverIdentifier) {


        /*
         * For saving the document received
         */

        byte[] data = Base64.decode(actualMessage, Base64.DEFAULT);

        String tsInGmt = Utilities.epochtoGmt(timestamp);


        /*
         * document message
         */

        Map<String, Object> map = new HashMap<>();

        try {

            map.put("message", new String(data, "UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }

        map.put("messageType", "9");
        map.put("isSelf", isSelf);
        map.put("from", receiverUid);


        /*
         * Group chat specific
         */
        map.put("receiverIdentifier", receiverIdentifier);

        map.put("Ts", tsInGmt);
        map.put("id", id);

        map.put("fileName", fileName);
        map.put("mimeType", mimeType);
        map.put("extension", extension);

        map.put("downloadStatus", 0);

        map.put("dataSize", dataSize);

        if (isSelf) {

            map.put("deliveryStatus", String.valueOf(status));
        }

        if (status == -1) {
            db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
            db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewDocument), true, tsInGmt,
                    tsInGmt);
        } else {

            db.addNewChatMessageAndSort(receiverdocId, map, null, "");
        }
    }

    public void putGroupPostMessageInDb(String receiverUid, String actualMessage, String timestamp,
                                         String id, String receiverdocId,

                                         boolean isSelf, int status, String mimeType, String fileName, String extension,
                                         String receiverIdentifier, String postId, String postTitle, int postType) {


        /*
         * For saving the document received
         */

        byte[] data = Base64.decode(actualMessage, Base64.DEFAULT);

        String tsInGmt = Utilities.epochtoGmt(timestamp);


        /*
         * document message
         */

        Map<String, Object> map = new HashMap<>();

        try {

            map.put("message", new String(data, "UTF-8"));
        } catch (UnsupportedEncodingException e) {

        }

        map.put("messageType", "13");
        map.put("isSelf", isSelf);
        map.put("from", receiverUid);


        /*
         * Group chat specific
         */
        map.put("receiverIdentifier", receiverIdentifier);

        map.put("Ts", tsInGmt);
        map.put("id", id);

        map.put("postId", postId);
        map.put("postTitle", postTitle);
        map.put("postType", postType);

        if (isSelf) {

            map.put("deliveryStatus", String.valueOf(status));
        }

        if (status == -1) {
            db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
            db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewPost), true, tsInGmt,
                    tsInGmt);
        } else {

            db.addNewChatMessageAndSort(receiverdocId, map, null, "");
        }
    }

    public void putGroupReplyMessageInDb(String receiverUid, String actualMessage, String timestamp,
                                          String id, String receiverdocId,

                                          int dataSize, boolean isSelf, int status, String mimeType, String fileName, String extension,
                                          String thumbnail, String receiverIdentifier, String replyType,
                                          String previousReceiverIdentifier, String previousFrom,

                                          String previousPayload, String previousType, String

                                                  previousId, String previousFileType, boolean wasEdited, String postId, String postTitle,
                                          int postType) {

        String name = timestamp;
        Map<String, Object> map = new HashMap<>();
        map.put("messageType", "10");
        map.put("previousReceiverIdentifier", previousReceiverIdentifier);

        map.put("previousFrom", previousFrom);

        map.put("previousType", previousType);

        map.put("previousId", previousId);

        if (previousType.equals("9")) {

            map.put("previousFileType", previousFileType);

            map.put("previousPayload", previousPayload);
        } else if (previousType.equals("1") || previousType.equals("2") || previousType.equals("7")) {

            map.put("previousPayload",
                    fileHelper.convertByteArrayToFile(Base64.decode(previousPayload, Base64.DEFAULT), previousId,
                            "jpg"));
        } else {

            map.put("previousPayload", previousPayload);
        }



        /*
         * For saving the reply message received
         */

        byte[] data = Base64.decode(actualMessage, Base64.DEFAULT);

        byte[] thumbnailData = null;

        if ((replyType.equals("1")) || (replyType.equals("2")) || (replyType.equals("7"))) {

            thumbnailData = Base64.decode(thumbnail, Base64.DEFAULT);
        }



        /*
         *
         *
         * Initially in db we only store path of the thumbnail and nce downloaded we will replace that field withthe actual path of the downloaded file
         *
         *
         * */

        String tsInGmt = Utilities.epochtoGmt(timestamp);
        switch (Integer.parseInt(replyType)) {

            case 0: {
                /*
                 * Text message
                 */
                String text = "";
                try {

                    text = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {

                }

                map.put("message", text);
                map.put("replyType", "0");
                if (wasEdited) map.put("wasEdited", true);

                map.put("isSelf", isSelf);
                map.put("from", receiverUid);

                /*
                 * Group chat specific
                 */
                map.put("receiverIdentifier", receiverIdentifier);

                map.put("Ts", tsInGmt);
                map.put("id", id);

                if (isSelf) {

                    map.put("deliveryStatus", String.valueOf(status));
                }

                if (status == -1) {
                    db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                    db.updateChatListForNewMessage(receiverdocId, text, true, tsInGmt, tsInGmt);
                } else {

                    db.addNewChatMessageAndSort(receiverdocId, map, null, "");
                }
                break;
            }

            case 1: {
                /*
                 * Image
                 */

                String thumbnailPath = fileHelper.convertByteArrayToFile(thumbnailData, name, "jpg");

                try {

                    map.put("message", new String(data, "UTF-8"));
                } catch (UnsupportedEncodingException e) {

                }

                map.put("replyType", "1");
                map.put("isSelf", isSelf);
                map.put("from", receiverUid);


                /*
                 * Group chat specific
                 */
                map.put("receiverIdentifier", receiverIdentifier);

                map.put("Ts", tsInGmt);
                map.put("id", id);

                map.put("downloadStatus", 0);

                map.put("dataSize", dataSize);

                map.put("thumbnailPath", thumbnailPath);
                if (isSelf) {

                    map.put("deliveryStatus", String.valueOf(status));
                }

                if (status == -1) {
                    db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                    db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewImage), true, tsInGmt,
                            tsInGmt);
                } else {

                    db.addNewChatMessageAndSort(receiverdocId, map, null, "");
                }
                break;
            }
            case 2: {
                /*
                 * Video
                 */

                String thumbnailPath = fileHelper.convertByteArrayToFile(thumbnailData, name, "jpg");





                /*
                 *
                 *
                 * message key will contail the url on server until downloaded and once downloaded
                 * it will contain the local path of the video or image
                 *
                 * */
                try {

                    map.put("message", new String(data, "UTF-8"));
                } catch (UnsupportedEncodingException e) {

                }

                map.put("replyType", "2");
                map.put("isSelf", isSelf);
                map.put("from", receiverUid);


                /*
                 * Group chat specific
                 */
                map.put("receiverIdentifier", receiverIdentifier);

                map.put("Ts", tsInGmt);
                map.put("id", id);

                map.put("downloadStatus", 0);
                map.put("dataSize", dataSize);

                map.put("thumbnailPath", thumbnailPath);

                if (isSelf) {

                    map.put("deliveryStatus", String.valueOf(status));
                }

                if (status == -1) {
                    db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                    db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewVideo), true, tsInGmt,
                            tsInGmt);
                } else {
                    db.addNewChatMessageAndSort(receiverdocId, map, null, "");
                }
                break;
            }
            case 3: {
                /*
                 * Location
                 */

                String placeString = "";
                try {

                    placeString = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {

                }

                map.put("message", placeString);
                map.put("replyType", "3");
                map.put("isSelf", isSelf);
                map.put("from", receiverUid);


                /*
                 * Group chat specific
                 */
                map.put("receiverIdentifier", receiverIdentifier);

                map.put("Ts", tsInGmt);
                map.put("id", id);

                if (isSelf) {

                    map.put("deliveryStatus", String.valueOf(status));
                }

                if (status == -1) {
                    db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                    db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewLocation), true,
                            tsInGmt, tsInGmt);
                } else {
                    db.addNewChatMessageAndSort(receiverdocId, map, null, "");
                }
                break;
            }
            case 4: {
                /*
                 * Follow
                 */
                String contactString = "";
                try {

                    contactString = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {

                }

                map.put("message", contactString);
                map.put("replyType", "4");
                map.put("isSelf", isSelf);
                map.put("from", receiverUid);



                /*
                 * Group chat specific
                 */
                map.put("receiverIdentifier", receiverIdentifier);

                map.put("Ts", tsInGmt);
                map.put("id", id);

                if (isSelf) {

                    map.put("deliveryStatus", String.valueOf(status));
                }

                if (status == -1) {
                    db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                    db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewContact), true,
                            tsInGmt, tsInGmt);
                } else {
                    db.addNewChatMessageAndSort(receiverdocId, map, null, "");
                }

                break;
            }
            case 5: {
                /*
                 * Audio
                 */

                try {

                    map.put("message", new String(data, "UTF-8"));
                } catch (UnsupportedEncodingException e) {

                }

                map.put("replyType", "5");
                map.put("isSelf", isSelf);
                map.put("from", receiverUid);

                /*
                 * Group chat specific
                 */
                map.put("receiverIdentifier", receiverIdentifier);

                map.put("Ts", tsInGmt);
                map.put("id", id);
                map.put("downloadStatus", 0);
                map.put("dataSize", dataSize);

                if (isSelf) {

                    map.put("deliveryStatus", String.valueOf(status));
                }

                if (status == -1) {
                    db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                    db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewAudio), true, tsInGmt,
                            tsInGmt);
                } else {
                    db.addNewChatMessageAndSort(receiverdocId, map, null, "");
                }
                break;
            }
            case 6: {
                /*
                 * Sticker
                 */

                String text = "";
                try {

                    text = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {

                }

                map.put("message", text);
                map.put("replyType", "6");
                map.put("isSelf", isSelf);
                map.put("from", receiverUid);

                /*
                 * Group chat specific
                 */
                map.put("receiverIdentifier", receiverIdentifier);

                map.put("Ts", tsInGmt);
                map.put("id", id);

                if (isSelf) {

                    map.put("deliveryStatus", String.valueOf(status));
                }

                if (status == -1) {
                    db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");

                    db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewSticker), true,
                            tsInGmt, tsInGmt);
                } else {
                    db.addNewChatMessageAndSort(receiverdocId, map, null, "");
                }
                break;
            }

            case 7: {
                /*
                 * Doodle
                 */

                String thumbnailPath = fileHelper.convertByteArrayToFile(thumbnailData, name, "jpg");

                try {

                    map.put("message", new String(data, "UTF-8"));
                } catch (UnsupportedEncodingException e) {

                }

                map.put("replyType", "7");
                map.put("isSelf", isSelf);
                map.put("from", receiverUid);


                /*
                 * Group chat specific
                 */
                map.put("receiverIdentifier", receiverIdentifier);

                map.put("Ts", tsInGmt);
                map.put("id", id);

                map.put("downloadStatus", 0);

                map.put("dataSize", dataSize);
                map.put("thumbnailPath", thumbnailPath);

                if (isSelf) {

                    map.put("deliveryStatus", String.valueOf(status));
                }

                thumbnailPath = null;

                if (status == -1) {
                    db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                    db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewDoodle), true,
                            tsInGmt, tsInGmt);
                } else {
                    db.addNewChatMessageAndSort(receiverdocId, map, null, "");
                }

                break;
            }

            case 8: {
                /*
                 * Gif
                 */

                String url = "";
                try {

                    url = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {

                }

                map.put("message", url);
                map.put("replyType", "8");
                map.put("isSelf", isSelf);
                map.put("from", receiverUid);

                /*
                 * Group chat specific
                 */
                map.put("receiverIdentifier", receiverIdentifier);

                map.put("Ts", tsInGmt);
                map.put("id", id);

                if (isSelf) {

                    map.put("deliveryStatus", String.valueOf(status));
                }

                if (status == -1) {
                    db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                    db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewGiphy), true, tsInGmt,
                            tsInGmt);
                } else {
                    db.addNewChatMessageAndSort(receiverdocId, map, null, "");
                }
                break;
            }
            case 9: {



                /*
                 * document message
                 */

                try {

                    map.put("message", new String(data, "UTF-8"));
                } catch (UnsupportedEncodingException e) {

                }

                map.put("replyType", "9");
                map.put("isSelf", isSelf);
                map.put("from", receiverUid);

                /*
                 * Group chat specific
                 */
                map.put("receiverIdentifier", receiverIdentifier);

                map.put("Ts", tsInGmt);
                map.put("id", id);

                map.put("fileName", fileName);
                map.put("mimeType", mimeType);
                map.put("extension", extension);

                map.put("downloadStatus", 0);

                map.put("dataSize", dataSize);

                if (isSelf) {

                    map.put("deliveryStatus", String.valueOf(status));
                }

                if (status == -1) {
                    db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                    db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewDocument), true,
                            tsInGmt, tsInGmt);
                } else {

                    db.addNewChatMessageAndSort(receiverdocId, map, null, "");
                }

                break;
            }

            case 13: {
                /*
                 * Post
                 */

                String url = "";
                try {

                    url = new String(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {

                }

                map.put("message", url);
                map.put("replyType", "13");
                map.put("isSelf", isSelf);
                map.put("from", receiverUid);
                map.put("postId", postId);
                map.put("postTitle", postTitle);
                map.put("postType", postType);


                /*
                 * Group chat specific
                 */
                map.put("receiverIdentifier", receiverIdentifier);

                map.put("Ts", tsInGmt);
                map.put("id", id);

                if (isSelf) {

                    map.put("deliveryStatus", String.valueOf(status));
                }

                if (status == -1) {
                    db.addNewChatMessageAndSort(receiverdocId, map, tsInGmt, "");
                    db.updateChatListForNewMessage(receiverdocId, instance.getString(R.string.NewPost), true, tsInGmt,
                            tsInGmt);
                } else {
                    db.addNewChatMessageAndSort(receiverdocId, map, null, "");
                }
                break;
            }
        }
    }

}
