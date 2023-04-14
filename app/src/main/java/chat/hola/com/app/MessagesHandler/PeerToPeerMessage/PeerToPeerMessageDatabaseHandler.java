package chat.hola.com.app.MessagesHandler.PeerToPeerMessage;

import android.content.Context;
import android.util.Base64;

import com.ezcall.android.R;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.FileHelper;
import chat.hola.com.app.Utilities.Utilities;

public class PeerToPeerMessageDatabaseHandler {


    private AppController instance = AppController.getInstance();
    private Context context = AppController.getInstance();
    private FileHelper fileHelper = instance.getFileHelper();

    public void putCallMessageInDb(String receiverUid, String messageType, String actualMessage,
                                    String timestamp, String id, String documentId, String toId,
                                    String typeOfCall, String callId, int creation,
                                    int dataSize, long dTime, String receiverName, boolean isSelf, int status, boolean timerStarted,
                                    int expectedDTime, String secretId, long duration, boolean isInComingCall) {

        String tsInGmt = Utilities.epochtoGmt(timestamp);

        /*
         * Text message
         */
        if (messageType.equals("17")) {

            boolean isDTag = false;

            Map<String, Object> map = new HashMap<>();
            map.put("message", actualMessage);
            map.put("messageType", messageType);
            map.put("isDTag", isDTag);
            map.put("isSelf", isSelf);
            map.put("from", receiverUid);
            map.put("Ts", tsInGmt);
            map.put("id", id);
            map.put("typeOfCall", typeOfCall);
            map.put("creation", creation);
            map.put("callId", callId);
            map.put("duration", duration);
            map.put("isInComingCall", isInComingCall);


            if (isSelf) {
                map.put("deliveryStatus", String.valueOf(status));
            }

            /*
             * For secret chat exclusively
             */

            map.put("dTime", dTime);
            map.put("timerStarted", timerStarted);
            if (timerStarted) {
                map.put("expectedDTime", expectedDTime);

            }


            if (status == -1) {
                instance.getDbController().addNewChatMessageAndSort(documentId, map, tsInGmt, secretId);
                instance.getDbController().updateChatListForNewMessage(documentId, actualMessage, true, tsInGmt, tsInGmt);
            } else {
                instance.getDbController().addNewChatMessageAndSort(documentId, map, null, secretId);
            }
        }
    }

    public void putMissedCallMessageInDb(String receiverUid, String messageType, String actualMessage,
                                          String timestamp, String id, String documentId, String toId,
                                          String typeOfCall, String callId, int creation,
                                          int dataSize, long dTime, String receiverName, boolean isSelf, int status, boolean timerStarted, int expectedDTime, String secretId) {

        String tsInGmt = Utilities.epochtoGmt(timestamp);

        /*
         * Text message
         */
        if (messageType.equals("16")) {

            boolean isDTag = false;

            Map<String, Object> map = new HashMap<>();
            map.put("message", actualMessage);
            map.put("messageType", messageType);
            map.put("isDTag", isDTag);
            map.put("isSelf", isSelf);
            map.put("from", receiverUid);
            map.put("Ts", tsInGmt);
            map.put("id", id);
            map.put("typeOfCall", typeOfCall);
            map.put("creation", creation);
            map.put("callId", callId);


            if (isSelf) {
                map.put("deliveryStatus", String.valueOf(status));
            }

            /*
             * For secret chat exclusively
             */

            map.put("dTime", dTime);
            map.put("timerStarted", timerStarted);
            if (timerStarted) {
                map.put("expectedDTime", expectedDTime);

            }


            if (status == -1) {
                instance.getDbController().addNewChatMessageAndSort(documentId, map, tsInGmt, secretId);
                instance.getDbController().updateChatListForNewMessage(documentId, actualMessage, true, tsInGmt, tsInGmt);
            } else {
                instance.getDbController().addNewChatMessageAndSort(documentId, map, null, secretId);
            }


        }
    }

    public void putTransferMessageInDb(String receiverUid, String messageType, String actualMessage,
                                        String timestamp, String id, String documentId, String toId, String amount, String transferStatus, String transferStatusText,
                                        int dataSize, long dTime, String receiverName, boolean isSelf, int status, boolean timerStarted, int expectedDTime, String secretId,String transactionId) {

        String tsInGmt = Utilities.epochtoGmt(timestamp);

        /*
         * Text message
         */
        if (messageType.equals("15")) {

            boolean isDTag = false;

            Map<String, Object> map = new HashMap<>();
            map.put("message", actualMessage);
            map.put("messageType", messageType);
            map.put("isDTag", isDTag);
            map.put("isSelf", isSelf);
            map.put("from", receiverUid);
            map.put("Ts", tsInGmt);
            map.put("id", id);
            map.put("amount", amount);
            map.put("transferStatus", transferStatus);
            map.put("transferStatusText", transferStatusText);
            map.put("transactionId", transactionId);

            if (isSelf) {
                map.put("deliveryStatus", String.valueOf(status));
            }

            /*
             * For secret chat exclusively
             */

            map.put("dTime", dTime);
            map.put("timerStarted", timerStarted);
            if (timerStarted) {
                map.put("expectedDTime", expectedDTime);

            }

            // if Message already exist then change this message on this index
            boolean isExist = instance.getDbController().changeTransferMessageInDb(documentId, id, map);

            if (!isExist) {
                if (status == -1) {
                    instance.getDbController().addNewChatMessageAndSort(documentId, map, tsInGmt, secretId);
                    instance.getDbController().updateChatListForNewMessage(documentId, actualMessage, true, tsInGmt, tsInGmt);
                } else {
                    instance.getDbController().addNewChatMessageAndSort(documentId, map, null, secretId);
                }
            }

        }
    }

    public void putMessageInDb(String receiverUid, String messageType, String actualMessage,
                                String timestamp, String id, String receiverdocId, String thumbnailMessage,


                                int dataSize, long dTime, String senderName, boolean isSelf, int status, boolean timerStarted, long expectedDTime, String removedAt, boolean wasEdited, String secretId) {


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


            boolean isDTag = false;

            if (text.trim().isEmpty()) {


                isDTag = true;

                if (dTime != -1) {

                    String message_dTime = String.valueOf(dTime);

                    for (int i = 0; i < instance.getdTimeForDB().length; i++) {
                        if (message_dTime.equals(instance.getdTimeForDB()[i])) {

                            if (i == 0) {


                                text = context.getString(R.string.Timer_set_off);
                            } else {


                                text = context.getString(R.string.Timer_set_to) + " " + instance.getdTimeOptions()[i];

                            }
                            break;
                        }
                    }
                } else {

                    if (isSelf) {


                        text = context.getResources().getString(R.string.YouInvited) + " " + senderName + " " +
                                context.getResources().getString(R.string.JoinSecretChat);

                    } else {
                        text = context.getResources().getString(R.string.youAreInvited) + " " + senderName + " " +
                                context.getResources().getString(R.string.JoinSecretChat);
                    }
                }

            }


            Map<String, Object> map = new HashMap<>();
            map.put("message", text);
            map.put("messageType", "0");

            map.put("isDTag", isDTag);
            if (wasEdited)
                map.put("wasEdited", true);


            map.put("isSelf", isSelf);
            map.put("from", receiverUid);
            map.put("Ts", tsInGmt);
            map.put("id", id);

            if (isSelf) {


                map.put("deliveryStatus", String.valueOf(status));


            }

            /*
             * For secret chat exclusively
             */

            map.put("dTime", dTime);
            map.put("timerStarted", timerStarted);
            if (timerStarted) {
                map.put("expectedDTime", expectedDTime);

            }

            if (status == -1) {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);


                instance.getDbController().updateChatListForNewMessage(receiverdocId, text, true, tsInGmt, tsInGmt);


            } else {


                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);

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
            map.put("Ts", tsInGmt);
            map.put("id", id);

            map.put("downloadStatus", 0);

            map.put("dataSize", dataSize);


            map.put("thumbnailPath", thumbnailPath);
            if (isSelf) {


                map.put("deliveryStatus", String.valueOf(status));


            }



            /*
             * For secret chat exclusively
             */

            map.put("dTime", dTime);
            map.put("timerStarted", timerStarted);

            if (timerStarted) {
                map.put("expectedDTime", expectedDTime);

            }
            if (status == -1) {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewImage), true, tsInGmt, tsInGmt);

            } else {


                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);

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
            map.put("Ts", tsInGmt);
            map.put("id", id);

            map.put("downloadStatus", 0);
            map.put("dataSize", dataSize);

            map.put("thumbnailPath", thumbnailPath);

            if (isSelf) {


                map.put("deliveryStatus", String.valueOf(status));


            }
            /*
             * For secret chat exclusively
             */

            map.put("dTime", dTime);
            map.put("timerStarted", timerStarted);
            if (timerStarted) {
                map.put("expectedDTime", expectedDTime);

            }

            if (status == -1) {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewVideo), true, tsInGmt, tsInGmt);

            } else {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


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
            map.put("Ts", tsInGmt);
            map.put("id", id);

            if (isSelf) {


                map.put("deliveryStatus", String.valueOf(status));


            }
            /*
             * For secret chat exclusively
             */

            map.put("dTime", dTime);
            map.put("timerStarted", timerStarted);
            if (timerStarted) {
                map.put("expectedDTime", expectedDTime);

            }

            if (status == -1) {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewLocation), true, tsInGmt, tsInGmt);
            } else {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


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
            map.put("Ts", tsInGmt);
            map.put("id", id);

            if (isSelf) {


                map.put("deliveryStatus", String.valueOf(status));


            }
            /*
             * For secret chat exclusively
             */

            map.put("dTime", dTime);
            map.put("timerStarted", timerStarted);

            if (timerStarted) {
                map.put("expectedDTime", expectedDTime);

            }
            if (status == -1) {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewContact), true, tsInGmt, tsInGmt);

            } else {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


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
            map.put("Ts", tsInGmt);
            map.put("id", id);
            map.put("downloadStatus", 0);
            map.put("dataSize", dataSize);


            if (isSelf) {


                map.put("deliveryStatus", String.valueOf(status));


            }
            /*
             * For secret chat exclusively
             */

            map.put("dTime", dTime);
            map.put("timerStarted", timerStarted);
            if (timerStarted) {
                map.put("expectedDTime", expectedDTime);

            }

            if (status == -1) {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewAudio), true, tsInGmt, tsInGmt);
            } else {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


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
            map.put("Ts", tsInGmt);
            map.put("id", id);


            if (isSelf) {


                map.put("deliveryStatus", String.valueOf(status));


            }

            /*
             * For secret chat exclusively
             */

            map.put("dTime", dTime);
            map.put("timerStarted", timerStarted);
            if (timerStarted) {
                map.put("expectedDTime", expectedDTime);

            }
            if (status == -1) {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);

                instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewSticker), true, tsInGmt, tsInGmt);

            } else {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


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
            map.put("Ts", tsInGmt);
            map.put("id", id);

            map.put("downloadStatus", 0);

            map.put("dataSize", dataSize);
            map.put("thumbnailPath", thumbnailPath);

            if (isSelf) {


                map.put("deliveryStatus", String.valueOf(status));


            }
            /*
             * For secret chat exclusively
             */

            map.put("dTime", dTime);
            map.put("timerStarted", timerStarted);
            if (timerStarted) {
                map.put("expectedDTime", expectedDTime);

            }
            thumbnailPath = null;

            if (status == -1) {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewDoodle), true, tsInGmt, tsInGmt);
            } else {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


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
            map.put("Ts", tsInGmt);
            map.put("id", id);

            if (isSelf) {


                map.put("deliveryStatus", String.valueOf(status));


            }
            /*
             * For secret chat exclusively
             */

            map.put("dTime", dTime);
            map.put("timerStarted", timerStarted);
            if (timerStarted) {
                map.put("expectedDTime", expectedDTime);

            }

            if (status == -1) {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewGiphy), true, tsInGmt, tsInGmt);
            } else {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


            }

        } else if (messageType.equals("11")) {

            String text = "";
            try {

                text = new String(data, "UTF-8");
            } catch (UnsupportedEncodingException e) {

            }

            Map<String, Object> map = new HashMap<>();
            map.put("message", text);
            map.put("messageType", "11");
            map.put("removedAt", removedAt);
            map.put("isDTag", false);

            map.put("isSelf", isSelf);
            map.put("from", receiverUid);
            map.put("Ts", tsInGmt);
            map.put("id", id);

            if (isSelf) {


                map.put("deliveryStatus", String.valueOf(status));


            }

            /*
             * For secret chat exclusively
             */

            map.put("dTime", dTime);
            map.put("timerStarted", timerStarted);
            if (timerStarted) {
                map.put("expectedDTime", expectedDTime);

            }

            if (status == -1) {
                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);


                instance.getDbController().updateChatListForNewMessage(receiverdocId, text, true, tsInGmt, tsInGmt);


            } else {


                instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);

            }

        }

    }

    public void putMessageInDb(String receiverUid, String actualMessage,
                                String timestamp, String id, String receiverdocId,


                                int dataSize, long dTime, boolean isSelf, int status, boolean timerStarted, long expectedDTime, String mimeType, String fileName, String extension, String secretId) {


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



        /*
         * For secret chat exclusively
         */

        map.put("dTime", dTime);
        map.put("timerStarted", timerStarted);

        if (timerStarted) {
            map.put("expectedDTime", expectedDTime);

        }
        if (status == -1) {
            instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
            instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewDocument), true, tsInGmt, tsInGmt);

        } else {


            instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);

        }


    }

    public void putPostMessageInDb(String receiverUid, String actualMessage,
                                    String timestamp, String id, String receiverdocId,


                                    long dTime, boolean isSelf, int status, boolean timerStarted, long expectedDTime, String postId, String postTitle, int postType, String secretId) {


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
        map.put("Ts", tsInGmt);
        map.put("id", id);


        map.put("postId", postId);
        map.put("postType", postType);
        map.put("postTitle", postTitle);


        if (isSelf) {


            map.put("deliveryStatus", String.valueOf(status));


        }



        /*
         * For secret chat exclusively
         */

        map.put("dTime", dTime);
        map.put("timerStarted", timerStarted);

        if (timerStarted) {
            map.put("expectedDTime", expectedDTime);

        }
        if (status == -1) {
            instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
            instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewPost), true, tsInGmt, tsInGmt);

        } else {


            instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);

        }


    }

    public void putReplyMessageInDb(String receiverUid,
                                    String actualMessage,
                                    String timestamp,
                                    String id,
                                    String receiverdocId,
                                    int dataSize,
                                    long dTime,
                                    boolean isSelf,
                                    int status,
                                    boolean timerStarted,
                                    long expectedDTime,
                                    String mimeType,
                                    String fileName,
                                    String extension,
                                    String thumbnail,
                                    String senderName,
                                    String replyType,
                                    String previousReceiverIdentifier,
                                    String previousFrom,
                                    String previousPayload,
                                    String previousType,
                                    String previousId,
                                    String previousFileType,
                                    boolean wasEdited,
                                    String postId,
                                    String postTitle,
                                    int postType,
                                    String secretId) {


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


            map.put("previousPayload", fileHelper.convertByteArrayToFile(Base64.decode(previousPayload, Base64.DEFAULT), previousId, "jpg"));
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


                boolean isDTag = false;

                if (text.trim().isEmpty()) {


                    isDTag = true;

                    if (dTime != -1) {

                        String message_dTime = String.valueOf(dTime);

                        for (int i = 0; i < instance.getdTimeForDB().length; i++) {
                            if (message_dTime.equals(instance.getdTimeForDB()[i])) {

                                if (i == 0) {


                                    text = context.getString(R.string.Timer_set_off);
                                } else {


                                    text = context.getString(R.string.Timer_set_to) + " " + instance.getdTimeOptions()[i];

                                }
                                break;
                            }
                        }
                    } else {

                        if (isSelf) {


                            text = context.getResources().getString(R.string.YouInvited) + " " + senderName + " " +
                                    context.getResources().getString(R.string.JoinSecretChat);

                        } else {
                            text = context.getResources().getString(R.string.youAreInvited) + " " + senderName + " " +
                                    context.getResources().getString(R.string.JoinSecretChat);
                        }
                    }

                }

//                Map<String, Object> map = new HashMap<>();

                map.put("message", text);
                map.put("replyType", "0");

                map.put("isDTag", isDTag);
                if (wasEdited)
                    map.put("wasEdited", true);
                map.put("isSelf", isSelf);
                map.put("from", receiverUid);
                map.put("Ts", tsInGmt);
                map.put("id", id);

                if (isSelf) {


                    map.put("deliveryStatus", String.valueOf(status));


                }

                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", timerStarted);
                if (timerStarted) {
                    map.put("expectedDTime", expectedDTime);

                }

                if (status == -1) {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                    instance.getDbController().updateChatListForNewMessage(receiverdocId, text, true, tsInGmt, tsInGmt);


                } else {


                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);

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
                map.put("Ts", tsInGmt);
                map.put("id", id);

                map.put("downloadStatus", 0);

                map.put("dataSize", dataSize);


                map.put("thumbnailPath", thumbnailPath);
                if (isSelf) {


                    map.put("deliveryStatus", String.valueOf(status));


                }



                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", timerStarted);

                if (timerStarted) {
                    map.put("expectedDTime", expectedDTime);

                }
                if (status == -1) {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                    instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewImage), true, tsInGmt, tsInGmt);

                } else {


                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);

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
                map.put("Ts", tsInGmt);
                map.put("id", id);

                map.put("downloadStatus", 0);
                map.put("dataSize", dataSize);

                map.put("thumbnailPath", thumbnailPath);

                if (isSelf) {


                    map.put("deliveryStatus", String.valueOf(status));


                }
                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", timerStarted);
                if (timerStarted) {
                    map.put("expectedDTime", expectedDTime);

                }

                if (status == -1) {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                    instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewVideo), true, tsInGmt, tsInGmt);

                } else {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


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
                map.put("Ts", tsInGmt);
                map.put("id", id);

                if (isSelf) {


                    map.put("deliveryStatus", String.valueOf(status));


                }
                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", timerStarted);
                if (timerStarted) {
                    map.put("expectedDTime", expectedDTime);

                }

                if (status == -1) {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                    instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewLocation), true, tsInGmt, tsInGmt);
                } else {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


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
                map.put("Ts", tsInGmt);
                map.put("id", id);

                if (isSelf) {


                    map.put("deliveryStatus", String.valueOf(status));


                }
                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", timerStarted);

                if (timerStarted) {
                    map.put("expectedDTime", expectedDTime);

                }
                if (status == -1) {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                    instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewContact), true, tsInGmt, tsInGmt);

                } else {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


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
                map.put("Ts", tsInGmt);
                map.put("id", id);
                map.put("downloadStatus", 0);
                map.put("dataSize", dataSize);


                if (isSelf) {


                    map.put("deliveryStatus", String.valueOf(status));


                }
                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", timerStarted);
                if (timerStarted) {
                    map.put("expectedDTime", expectedDTime);

                }

                if (status == -1) {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                    instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewAudio), true, tsInGmt, tsInGmt);
                } else {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


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
                map.put("Ts", tsInGmt);
                map.put("id", id);


                if (isSelf) {


                    map.put("deliveryStatus", String.valueOf(status));


                }

                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", timerStarted);
                if (timerStarted) {
                    map.put("expectedDTime", expectedDTime);

                }
                if (status == -1) {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);

                    instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewSticker), true, tsInGmt, tsInGmt);

                } else {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


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
                map.put("Ts", tsInGmt);
                map.put("id", id);

                map.put("downloadStatus", 0);

                map.put("dataSize", dataSize);
                map.put("thumbnailPath", thumbnailPath);

                if (isSelf) {


                    map.put("deliveryStatus", String.valueOf(status));


                }
                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", timerStarted);
                if (timerStarted) {
                    map.put("expectedDTime", expectedDTime);

                }
                thumbnailPath = null;

                if (status == -1) {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                    instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewDoodle), true, tsInGmt, tsInGmt);
                } else {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


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
                map.put("Ts", tsInGmt);
                map.put("id", id);

                if (isSelf) {


                    map.put("deliveryStatus", String.valueOf(status));


                }
                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", timerStarted);
                if (timerStarted) {
                    map.put("expectedDTime", expectedDTime);

                }

                if (status == -1) {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                    instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewGiphy), true, tsInGmt, tsInGmt);
                } else {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


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



                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", timerStarted);

                if (timerStarted) {
                    map.put("expectedDTime", expectedDTime);

                }
                if (status == -1) {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                    instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewDocument), true, tsInGmt, tsInGmt);

                } else {


                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);

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
                map.put("Ts", tsInGmt);
                map.put("id", id);
                map.put("postId", postId);
                map.put("postTitle", postTitle);
                map.put("postType", postType);

                if (isSelf) {


                    map.put("deliveryStatus", String.valueOf(status));


                }
                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", timerStarted);
                if (timerStarted) {
                    map.put("expectedDTime", expectedDTime);

                }

                if (status == -1) {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, tsInGmt, secretId);
                    instance.getDbController().updateChatListForNewMessage(receiverdocId, context.getString(R.string.NewPost), true, tsInGmt, tsInGmt);
                } else {
                    instance.getDbController().addNewChatMessageAndSort(receiverdocId, map, null, secretId);


                }
                break;

            }
        }


    }

}
