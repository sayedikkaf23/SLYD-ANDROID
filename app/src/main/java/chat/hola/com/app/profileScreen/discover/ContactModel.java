package chat.hola.com.app.profileScreen.discover;

import static chat.hola.com.app.Utilities.CountryCodeUtil.readEncodedJsonString;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import com.ezcall.android.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Networking.connection.ContactHolder;
import chat.hola.com.app.Networking.observer.ContactObserver;
import chat.hola.com.app.NumberVerification.Country;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contact;
import chat.hola.com.app.profileScreen.discover.contact.pojo.ContactRequest;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Follow;

/**
 * <h>ContactFriendModel</h>
 *
 * @author 3Embed
 * @since 3/24/2018.
 */

public class ContactModel {

    private ArrayList<Contact> contactsList = new ArrayList<>();
    private Context context;
    private ArrayList<String> alreadyAddedContacts = new ArrayList<>();
    private ArrayList<Map<String, Object>> localContactsList = new ArrayList<>();
    private ContactRequest contactRequest;
    public static final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
    };

    @Inject
    ContactHolder contactHolder;
    @Inject
    ContactObserver contactObserver;

    public List<Country> allCountriesList;



    @Inject
    public ContactModel(Context context) {
        this.context = context;
        contactRequest = new ContactRequest();
    }

    void getAllCountries( Context context) {
        if (allCountriesList == null) {
            try {
                allCountriesList = new ArrayList<>();

                String allCountriesCode = readEncodedJsonString(context);


                JSONArray countrArray = new JSONArray(allCountriesCode);

                for (int i = 0; i < countrArray.length(); i++) {
                    JSONObject jsonObject = countrArray.getJSONObject(i);
                    String countryName = jsonObject.getString("name");
                    String countryDialCode = jsonObject.getString("dial_code");
                    String countryCode = jsonObject.getString("code");
                    int maxDigits = jsonObject.getInt("max_digits");

                    Country country = new Country();
                    country.setCode(countryCode);
                    country.setName(countryName);
                    country.setDialCode(countryDialCode);
                    country.setMaxDigits(maxDigits);
                    allCountriesList.add(country);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * <p>It returns all contacts from database</p>
     */
    ArrayList<Contact> addContact() {
        contactsList.clear();

        ArrayList<Map<String, Object>> contacts = AppController.getInstance().getDbController().loadContacts(AppController.getInstance().getContactsDocId());
        Contact contact;
        Map<String, Object> contactIthPosition;

        if (contactsList != null) {
            for (int i = 0; i < contacts.size(); i++) {
                contact = new Contact();
                contactIthPosition = contacts.get(i);
                contact.setId((String) contactIthPosition.get(Constants.DiscoverContact.CONTACT_UID));
                contact.setProfilePic((String) contactIthPosition.get(Constants.DiscoverContact.CONTACT_PICTURE));
                contact.setUserName((String) contactIthPosition.get(Constants.DiscoverContact.CONTACT_NAME));
                contact.set_private((Integer) contactIthPosition.get(Constants.DiscoverContact.PRIVATE));

                Follow follow = new Follow();
                follow.setStatus((Integer) contactIthPosition.get(Constants.DiscoverContact.FOLLOWING));
                contact.setFollow(follow);
                contactsList.add(contact);
            }
        }
        return contactsList;

    }

    /**
     * <p>It saves contact details to database which we get from server API</p>
     *
     * @param jsonObject : contains list of contacts
     */
    boolean saveContactsFromApiResponse(JSONObject jsonObject) {

        AppController.getInstance().unsubscribeToTopic(MqttEvents.ContactSync.value + "/" + AppController.getInstance().getUserId());

        try {
            JSONArray array = jsonObject.getJSONArray(Constants.DiscoverContact.CONTACTS);
            ArrayList<Map<String, Object>> contacts = new ArrayList<>();
            Map<String, Object> contact;
            String contactUid, contactName, contactStatus, contactProfilePic, contactIdentifier, localContactNumber;
            int _private, followStatus, friendStatusCode;
            JSONObject obj;

            for (int i = 0; i < array.length(); i++) {

                obj = array.getJSONObject(i);
                JSONObject follow = obj.getJSONObject("follow");
                try {
                    followStatus = Integer.parseInt(follow.get("status").toString());
                } catch (Exception e) {
                    followStatus = 0;
                }
                _private = Integer.parseInt(obj.get("private").toString());
                friendStatusCode = Integer.parseInt(obj.get("friendStatusCode").toString());

                contactUid = obj.getString(Constants.DiscoverContact.ID);
                //    localContactNumber = obj.getString(Constants.DiscoverContact.LOCAL_NUMBER);
                contactName = obj.getString(Constants.DiscoverContact.USER_NAME);

                contactProfilePic = "";
                contactIdentifier = obj.getString(Constants.DiscoverContact.NUMBER);
                contactStatus = context.getResources().getString(R.string.default_status);
                if (obj.has(Constants.DiscoverContact.PROFILE_PICTURE))
                    contactProfilePic = obj.getString(Constants.DiscoverContact.PROFILE_PICTURE);
                if (obj.has(Constants.DiscoverContact.SOCIAL_STATUS))
                    contactStatus = obj.getString(Constants.DiscoverContact.SOCIAL_STATUS);

                contact = new HashMap<>();
                contact.put(Constants.DiscoverContact.CONTACT_LOCAL_NUMBER, contactIdentifier);
                contact.put(Constants.DiscoverContact.CONTACT_LOCAL_ID, contactUid);
                contact.put(Constants.DiscoverContact.CONTACT_UID, contactUid);
                contact.put(Constants.DiscoverContact.CONTACT_TYPE, "1");
                contact.put(Constants.DiscoverContact.CONTACT_NAME, contactName);
                contact.put(Constants.DiscoverContact.CONTACT_STATUS, contactStatus);
                contact.put(Constants.DiscoverContact.PROFILE_PICTURE_URL, contactProfilePic);
                contact.put(Constants.DiscoverContact.CONTACT_IDENTIFIRE, contactIdentifier);
                contact.put(Constants.DiscoverContact.FOLLOWING, followStatus);
                contact.put(Constants.DiscoverContact.PRIVATE, _private);
                contact.put(Constants.DiscoverContact.FRIEND_STATUS_CODE, friendStatusCode);
                contacts.add(contact);
            }

            //AppController.getInstance().getDbController().insertContactsInfo(contacts, AppController.getInstance().getContactsDocId());
            AppController.getInstance().getDbController().updateAllContacts(localContactsList, AppController.getInstance().getAllContactsDocId());
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }


    /**
     * <p>Fetch all device contacts</p>
     */
    void contactSync() {
        new syncContacts().execute();
    }

    public void updateFolow(String userId, Integer status) {
        AppController.getInstance().getDbController().updateFollowStatus(AppController.getInstance().getContactsDocId(), userId, status);
    }

    public String getId(int position) {
        return contactsList.get(position).getId();
    }

    public Contact getData(int position) {
        return contactsList.get(position);
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private class syncContacts extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            ArrayList<Contact> contacts = new ArrayList<>();
            alreadyAddedContacts.clear();
            localContactsList.clear();

            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);

            if (cursor != null) {
                try {
                    final int userId = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                    final int phoneTypeIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
                    final int displayNameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    final int phoneNumberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    HashMap<String, Object> contactsInfo;
                    while (cursor.moveToNext()) {
                        contactsInfo = new HashMap<>();
                        int type = -1;
//                        String phoneNo = "" + cursor.getString(phoneNumberIndex).replaceAll("[\\D\\s\\-()]", "");
                        String phoneNo = "" + cursor.getString(phoneNumberIndex).replaceAll("[\\s\\-()]", "");

                        for (Country country  : allCountriesList) {
                            if(phoneNo.contains(country.getDialCode())){
                                phoneNo = phoneNo.replace(country.getDialCode(),"");
                            }
                        }

                        switch (cursor.getInt(phoneTypeIndex)) {
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                type = 1;
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                type = 0;
                                break;
                            case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER:
                                type = 8;
                                break;
                        }
                        contactsInfo.put(Constants.DiscoverContact.TYPE, type);
                        contactsInfo.put(Constants.DiscoverContact.PHONE_NUMBER, phoneNo);
                        contactsInfo.put(Constants.DiscoverContact.USER_NAME, cursor.getString(displayNameIndex));
                        contactsInfo.put(Constants.DiscoverContact.CONTACT_ID, userId);

                        Contact contact = new Contact();
                        contact.setType("1");
                        contact.setNumber(phoneNo);
                        contacts.add(contact);
                        localContactsList.add(contactsInfo);
                    }
                } finally {
                    cursor.close();
                }
            }

            contactRequest.setContacts(contacts);
            contactHolder.setContactRequest(contactRequest);
            return true;

        }

        @Override
        protected void onPostExecute(Object o) {
            contactObserver.publishData(contactHolder);
        }
    }
}
