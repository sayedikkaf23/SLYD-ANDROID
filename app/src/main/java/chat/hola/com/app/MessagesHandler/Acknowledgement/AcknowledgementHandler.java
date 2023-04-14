package chat.hola.com.app.MessagesHandler.Acknowledgement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Utilities;

public class AcknowledgementHandler {

    private AppController instance = AppController.getInstance();

    public void handleAcknowledgement(JSONObject obj) {

        try{
            String sender = obj.getString("from");
            String document_id_DoubleTick = obj.getString("doc_id");
            String status = obj.getString("status");

            JSONArray arr_temp = obj.getJSONArray("msgIds");
            String id = arr_temp.getString(0);

            /*
             * For callback in to activity to update UI
             */
            try {
                obj.put("msgId", id);
                obj.put("eventName", obj.getString("topic"));
                AppController.getBus().post(obj);
            } catch (JSONException e) {

            }


            String secretId = "";
            if (obj.has("secretId")) {
                secretId = obj.getString("secretId");


            }
            if (status.equals("2")) {

                /*
                 * message delivered
                 */
                if (!(instance.getDbController().updateMessageStatus(document_id_DoubleTick, id, 1, obj.getString("deliveryTime")))) {
                    instance.getDbController().updateMessageStatus(
                            instance.getDbController().getDocumentIdOfReceiverChatlistScreen(
                                    AppController.getInstance().getChatDocId(), sender, secretId), id, 1, obj.getString("deliveryTime"));

                }
            } else {

                /*
                 * message read
                 */
                if (!secretId.isEmpty()) {

                    if (obj.getLong("dTime") != -1) {
                        String docId = instance.getDatabaseDocumentHelper().findDocumentIdOfReceiver(sender, secretId);
                        if (!docId.isEmpty()) {
                            setTimer(docId, id, obj.getLong("dTime") * 1000);
                        }
                    }
                }


                if (!(instance.getDbController().drawBlueTickUptoThisMessage(document_id_DoubleTick, id, obj.getString("readTime")))) {


                    instance.getDbController().drawBlueTickUptoThisMessage(
                            instance.getDbController().getDocumentIdOfReceiverChatlistScreen(
                                    AppController.getInstance().getChatDocId(), sender, secretId), id, obj.getString("readTime"));


                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public void setTimer(String documentId, String messageId, long expectedDTime) {

        if (expectedDTime > 0) {
            long temp = Utilities.getGmtEpoch();
            instance.getDbController().setTimerStarted(documentId, messageId, temp + expectedDTime);
        }

    }

}
