package chat.hola.com.app.MessagesHandler.UserUpdate;

import android.content.Context;

import com.ezcall.android.R;
import com.squareup.otto.Bus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.Utilities.Utilities;

public class UserUpdateHandler {

    private AppController instance = AppController.getInstance();
    private CouchDbController db = instance.getDbController();

    public void handleUserUpdate(JSONObject obj) {


        try {

            CouchDbController db = instance.getDbController();
            Bus bus = AppController.getBus();
            String friendsDocId = instance.getFriendsDocId();
            String contactsDocId = instance.getContactsDocId();
            String allContactsDocId = instance.getAllContactsDocId();
            ArrayList<Map<String, Object>> dirtyContacts = instance.dirtyContacts;
            ArrayList<Map<String, Object>> newContacts = instance.newContacts;
            ArrayList<Map<String, Object>> inactiveContacts = instance.inactiveContacts;

            /*
             * Instead of taking callbacks on 2 sepearte interface implementations,its better to use the otto bus
             */

            String topic = obj.getString("topic");

            if (obj.getInt("type") != 4 && obj.getInt("type") != 3) {
                obj.put("eventName", topic);
                bus.post(obj);
            }

            switch (obj.getInt("type")) {

                case 6:
                    if (obj.getBoolean("blocked")) {
                        db.addBlockedUser(friendsDocId, obj.getString("initiatorId"),
                                obj.getString("initiatorIdentifier"), false);
                    } else {

                        db.removeUnblockedUser(friendsDocId, obj.getString("initiatorId"));
                    }

                    break;

                case 1:
                    db.updateContactDetails(AppController.getInstance().getFriendsDocId(),
                            obj.getString("userId"), null, obj.getString("socialStatus"));

                    break;
                case 2:

                    AppController.getInstance().getSessionManager().setUserProfilePicUpdateTime();

                    db.updateContactDetails(AppController.getInstance().getFriendsDocId(),
                            obj.getString("userId"), obj.getString("profilePic"), null);

                    break;

                case 3:
                    obj = obj.getJSONArray("contacts").getJSONObject(0);

                    String name =
                           findNewlyJoinedContactName(obj.getString("number"), obj.getString("userId"));

                    if (!name.isEmpty()) {
                        JSONObject tempObj = new JSONObject();

                        tempObj.put("name", name);
                        tempObj.put("number", obj.getString("number"));
                        tempObj.put("userId", obj.getString("userId"));
                        tempObj.put("type", 3);
                        tempObj.put("eventName", topic);

                        bus.post(tempObj);
                    }

                    break;
                case 4:
                    instance.pendingContactUpdateRequests--;

                    JSONArray array = obj.getJSONArray("contacts");

                    JSONObject contact;
                    boolean updatedContactExists;
                    String contactProfilePic, contactStatus;
                    Map<String, Object> dirtyContact;
                    Map<String, Object> newContact;
                    int k = 0;

                    boolean alreadyFound;
                    for (int j = 0; j < array.length(); j++) {
                        try {
                            contact = array.getJSONObject(j);
                            alreadyFound = false;

                            if (dirtyContacts.size() > 0) {

                                updatedContactExists = false;

                                for (int i = 0; i < dirtyContacts.size(); i++) {

                                    dirtyContact = dirtyContacts.get(i);

                                    if ((dirtyContact.get("contactNumber")
                                            .equals(contact.getString("localNumber"))) && (dirtyContact.get(
                                            "contactUid").equals(contact.getString("_id")))) {

                                        updatedContactExists = true;

                                        k = i;
                                        break;
                                    }
                                }

                                if (updatedContactExists) {

                                    alreadyFound = true;
                                    dirtyContact = dirtyContacts.get(k);
                                    dirtyContacts.remove(k);
                                    /*
                                     * For the updated contact name and contact identifier to be sent as callback
                                     */

                                    JSONObject obj2 = new JSONObject();
                                    obj2.put("eventName", topic);
                                    obj2.put("subtype", 0);
                                    obj2.put("type", 4);
                                    obj2.put("contactUid", dirtyContact.get("contactUid"));
                                    obj2.put("contactName", dirtyContact.get("contactName"));

                                    bus.post(obj2);


                                    /*
                                     *  Update the corresponding contact details
                                     */

                                    Map<String, Object> contactDetails = new HashMap<>();
                                    contactDetails.put("contactLocalId", dirtyContact.get("contactId"));

                                    contactDetails.put("contactLocalNumber", contact.getString("localNumber"));

                                    contactDetails.put("contactUid", contact.getString("_id"));
                                    contactDetails.put("contactName", dirtyContact.get("contactName"));

                                    contactDetails.put("contactIdentifier", contact.getString("number"));
                                    contactProfilePic = "";

                                    if (contact.has("profilePic")) {
                                        contactProfilePic = contact.getString("profilePic");
                                    }

                                    if (contact.has("socialStatus")) {

                                        contactStatus = contact.getString("socialStatus");
                                    } else {
                                        contactStatus = instance.getString(R.string.default_status);
                                    }
                                    contactDetails.put("contactStatus", contactStatus);
                                    contactDetails.put("contactPicUrl", contactProfilePic);

                                    db.addUpdatedContact(contactsDocId, contactDetails,
                                            contact.getString("_id"));
                                }
                            }

                            if (!alreadyFound && newContacts.size() > 0) {

                                /*
                                 * New contacts were added
                                 */

                                for (int i = 0; i < newContacts.size(); i++) {

                                    newContact = newContacts.get(i);

                                    if ((newContact.get("phoneNumber")
                                            .equals(contact.getString("localNumber")))) {
                                        alreadyFound = true;
                                        Map<String, Object> contactDetails = new HashMap<>();
                                        contactDetails.put("contactLocalId", newContact.get("contactId"));

                                        contactDetails.put("contactLocalNumber",
                                                contact.getString("localNumber"));

                                        contactDetails.put("contactUid", contact.getString("_id"));
                                        contactDetails.put("contactName", newContact.get("userName"));

                                        contactDetails.put("contactIdentifier", contact.getString("number"));
                                        contactProfilePic = "";

                                        if (contact.has("profilePic")) {
                                            contactProfilePic = contact.getString("profilePic");
                                        }

                                        if (contact.has("socialStatus")) {

                                            contactStatus = contact.getString("socialStatus");
                                        } else {
                                            contactStatus = instance.getString(R.string.default_status);
                                        }
                                        contactDetails.put("contactStatus", contactStatus);
                                        contactDetails.put("contactPicUrl", contactProfilePic);

                                        db.addUpdatedContact(contactsDocId, contactDetails,
                                                contact.getString("_id"));

                                        JSONObject obj2 = new JSONObject();
                                        obj2.put("subtype", 2);
                                        obj2.put("contactUid", contact.getString("_id"));
                                        obj2.put("contactStatus", contactStatus);
                                        obj2.put("contactPicUrl", contactProfilePic);
                                        obj2.put("contactName", newContact.get("userName"));
                                        obj2.put("contactIdentifier", contact.getString("number"));
                                        obj2.put("contactLocalId", newContact.get("contactId"));
                                        obj2.put("contactLocalNumber", contact.getString("localNumber"));
                                        obj2.put("eventName", topic);
                                        obj2.put("type", 4);
                                        bus.post(obj2);

                                        newContacts.remove(i);
                                    }
                                }


                                /*
                                 * Not clearing it here intentionally
                                 */

                            }

                            if (!alreadyFound && inactiveContacts.size() > 0) {
                                Map<String, Object> inactiveContact;
                                for (int i = 0; i < inactiveContacts.size(); i++) {

                                    inactiveContact = inactiveContacts.get(i);

                                    if ((inactiveContact.get("phoneNumber")
                                            .equals(contact.getString("localNumber")))) {

                                        Map<String, Object> contactDetails = new HashMap<>();
                                        contactDetails.put("contactLocalId", inactiveContact.get("contactId"));

                                        contactDetails.put("contactLocalNumber",
                                                contact.getString("localNumber"));

                                        contactDetails.put("contactUid", contact.getString("_id"));
                                        contactDetails.put("contactName", inactiveContact.get("userName"));

                                        contactDetails.put("contactIdentifier", contact.getString("number"));
                                        contactProfilePic = "";

                                        if (contact.has("profilePic")) {
                                            contactProfilePic = contact.getString("profilePic");
                                        }

                                        if (contact.has("socialStatus")) {

                                            contactStatus = contact.getString("socialStatus");
                                        } else {
                                            contactStatus = instance.getString(R.string.default_status);
                                        }
                                        contactDetails.put("contactStatus", contactStatus);
                                        contactDetails.put("contactPicUrl", contactProfilePic);

                                        db.addUpdatedContact(contactsDocId, contactDetails,
                                                contact.getString("_id"));

                                        JSONObject obj2 = new JSONObject();
                                        obj2.put("subtype", 2);
                                        obj2.put("contactUid", contact.getString("_id"));
                                        obj2.put("contactStatus", contactStatus);
                                        obj2.put("contactPicUrl", contactProfilePic);
                                        obj2.put("contactName", inactiveContact.get("userName"));
                                        obj2.put("contactIdentifier", contact.getString("number"));
                                        obj2.put("contactLocalId", inactiveContact.get("contactId"));
                                        obj2.put("contactLocalNumber", contact.getString("localNumber"));
                                        obj2.put("eventName", topic);
                                        obj2.put("type", 4);
                                        bus.post(obj2);

                                        inactiveContacts.remove(i);
                                    }
                                }
                            }
                        } catch (JSONException e) {

                        }
                    }

                    if (instance.pendingContactUpdateRequests == 0) {

                        if (dirtyContacts.size() > 0) {
                            /*
                             * Active contact number was changed but is no longer in list of active contact numbers
                             */

                            for (int i = 0; i < dirtyContacts.size(); i++) {

                                db.deleteActiveContactByUid(contactsDocId,
                                        (String) dirtyContacts.get(i).get("contactUid"));

                                JSONObject obj2 = new JSONObject();

                                obj2.put("subtype", 1);
                                obj2.put("contactUid", dirtyContacts.get(i).get("contactUid"));
                                obj2.put("eventName", topic);
                                obj2.put("type", 4);
                                bus.post(obj2);

                                dirtyContacts.remove(i);
                            }
                        }

                        inactiveContacts.clear();
                        newContacts.clear();
                    }

                    AppController.getInstance()
                            .getSharedPreferences()
                            .edit()
                            .putString("lastUpdated", String.valueOf(Utilities.getGmtEpoch()))
                            .apply();
                    break;
                case 5:

                    JSONArray array2 = obj.getJSONArray("contacts");

                    JSONObject contact2;

                    for (int j = 0; j < array2.length(); j++) {
                        try {
                            contact2 = array2.getJSONObject(j);


                            /*
                             * status 0- number registered on app
                             *
                             * status 1- number not registered on app
                             */

                            if (contact2.has("status")) {
                                if (contact2.getInt("status") == 0) {


                                    /*
                                     *Its actually a soft delete if user has chats and calls with him
                                     *
                                     */
                                    db.deleteActiveContact(contactsDocId, contact2.getString("userId"));
                                }

                                db.deleteContactFromAllContactsList(allContactsDocId,
                                        contact2.getString("localNumber"));
                            }
                        } catch (JSONException e) {

                        }
                    }

                    AppController.getInstance()
                            .getSharedPreferences()
                            .edit()
                            .putString("lastDeleted", String.valueOf(Utilities.getGmtEpoch()))
                            .apply();

                    break;
            }


        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String findNewlyJoinedContactName(String number, String userId) {


        /*
         * Have to find the local contact name for the given number
         */

        number = number.substring(1);
        ArrayList<Map<String, Object>> arr = db.fetchAllContacts(instance.getAllContactsDocId());
        Map<String, Object> contact;

        String localNumber = "", contactId = "", contactName = "";
        int contactType = 1;
        int diff = 65536;

        for (int i = 0; i < arr.size(); i++) {

            contact = arr.get(i);

            if (number.contains(((String) contact.get("phoneNumber")).substring(1))) {

                if (number.length() - ((String) contact.get("phoneNumber")).length() < diff) {

                    diff = number.length() - ((String) contact.get("phoneNumber")).length();

                    localNumber = ((String) contact.get("phoneNumber"));
                    contactId = ((String) contact.get("contactId"));
                    contactName = ((String) contact.get("userName"));

                    contactType = ((int) contact.get("type"));
                }
            }
        }

        if (!contactName.isEmpty()) {
            /*
             * To save a contact
             */
            Map<String, Object> contactMap = new HashMap<>();

            contactMap.put("contactLocalNumber", localNumber);
            contactMap.put("contactLocalId", contactId);
            contactMap.put("contactUid", userId);

            contactMap.put("contactType", contactType);

            contactMap.put("contactName", contactName);
            contactMap.put("contactStatus", instance.getString(R.string.default_status));
            contactMap.put("contactPicUrl", "");
            contactMap.put("contactIdentifier", number);

            db.addActiveContact(instance.getContactsDocId(), contactMap, userId);
        }
        return contactName;
    }
}
