package chat.hola.com.app.MessagesHandler.Utilities;

import java.util.ArrayList;
import java.util.Map;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;

public class DatabaseDocumentHelper {
    /**
     * To find the document id of the receiver on receipt of new message,if exists or create a new document for chat with that receiver and return its document id
     */
    @SuppressWarnings("unchecked")
    public String findDocumentIdOfReceiver(String receiverUid, String timestamp, String receiverName,
                                                  String receiverImage, String secretId, boolean invited,
                                                  String receiverIdentifier, String chatId, boolean groupChat, boolean isStar) {

        CouchDbController db = AppController.getInstance().getDbController();
        Map<String, Object> chatDetails = db.getAllChatDetails(AppController.getInstance().getChatDocId());

        if (chatDetails != null) {

            ArrayList<String> receiverUidArray = (ArrayList<String>) chatDetails.get("receiverUidArray");


            ArrayList<String> receiverDocIdArray = (ArrayList<String>) chatDetails.get("receiverDocIdArray");


            ArrayList<String> secretIdArray = (ArrayList<String>) chatDetails.get("secretIdArray");

            for (int i = 0; i < receiverUidArray.size(); i++) {


                if (receiverUidArray.get(i).equals(receiverUid) && secretIdArray.get(i).equals(secretId)) {

                    return receiverDocIdArray.get(i);

                }

            }
        }


        /*  here we also need to enter receiver name*/


        String docId = db.createDocumentForChat(timestamp, receiverUid, receiverName, receiverImage, secretId, invited,
                receiverIdentifier, chatId, groupChat, isStar);


        db.addChatDocumentDetails(receiverUid, docId, AppController.getInstance().getChatDocId(), secretId);

        return docId;
    }

    /**
     * To search if there exists any document containing chat for a particular receiver and if founde returns its document
     * id else return empty string
     */
    @SuppressWarnings("unchecked")
    public String findDocumentIdOfReceiver(String ReceiverUid, String secretId) {

        CouchDbController db = AppController.getInstance().getDbController();

        String docId = "";

        Map<String, Object> chatDetails = db.getAllChatDetails(AppController.getInstance().getChatDocId());

        if (chatDetails != null) {

            ArrayList<String> receiverUidArray = (ArrayList<String>) chatDetails.get("receiverUidArray");
            ArrayList<String> receiverDocIdArray = (ArrayList<String>) chatDetails.get("receiverDocIdArray");
            ArrayList<String> secretIdArray = (ArrayList<String>) chatDetails.get("secretIdArray");


            for (int i = 0; i < receiverUidArray.size(); i++) {
                if (receiverUidArray.get(i) != null && receiverUidArray.get(i).equals(ReceiverUid)) {
                    if (secretId.isEmpty()) {
                        if (secretIdArray.get(i).isEmpty()) {
                            return receiverDocIdArray.get(i);
                        }
                    } else {
                        if (secretIdArray.get(i).equals(secretId)) {
                            return receiverDocIdArray.get(i);
                        }
                    }
                }
            }
        }

        return docId;
    }
}
