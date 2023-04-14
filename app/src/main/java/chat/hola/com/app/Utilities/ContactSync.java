package chat.hola.com.app.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.ezcall.android.R;
import com.google.gson.JsonObject;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.ContactPermissionDialog;
import chat.hola.com.app.Networking.HowdooServiceTrending;
import chat.hola.com.app.Networking.UnsafeOkHttpClient;
import chat.hola.com.app.Networking.connection.ContactHolder;
import chat.hola.com.app.Networking.observer.ContactObserver;
import chat.hola.com.app.home.connect.ContactActivity;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contact;
import chat.hola.com.app.profileScreen.discover.contact.pojo.ContactRequest;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contacts;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Follow;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.arthenica.mobileffmpeg.Config.getPackageName;


public class ContactSync {

    public static int page = 0;
    public static final int PAGE_SIZE = 100;
    private final Activity mActivity;
    private String from;
    public boolean isLastPage = false;

    private ArrayList<Contact> contactsList = new ArrayList<>();
    private Context context;
    private ArrayList<String> alreadyAddedContacts = new ArrayList<>();
    private ArrayList<Map<String, Object>> localContactsList = new ArrayList<>();
    private ContactRequest contactRequest;
    private final String[] PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.TYPE,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID
    };

    public ContactSync(Context context) {
        this.context = context;
        contactRequest = new ContactRequest();
        this.mActivity = (Activity) context;
        permissionAllow();

    }

    private void permissionAllow() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ContactPermissionDialog permissionDialog = new ContactPermissionDialog(context);
            permissionDialog.show();

            Button btnAccept = permissionDialog.findViewById(R.id.btnContinue);
            btnAccept.setOnClickListener(v -> {
                permissionDialog.dismiss();
                permission();
            });
        }else{
            // permission already granted
            permission();
        }
    }

    public void permission() {
        Dexter.withActivity(mActivity)
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted, open the camera
//                        Toast.makeText(mActivity.getApplication(), "ContactSync: P SyncContact", Toast.LENGTH_SHORT).show();
                        new syncContacts().execute();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            gotoSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void gotoSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("We need permission to access your contact list, please grant");
        builder.setPositiveButton("Setting", (dialogInterface, i) -> {

            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            context.startActivity(intent);
        });
        builder.show();
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private class syncContacts extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
//            mActivity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(mActivity.getApplication(), "ContactSync: B Background", Toast.LENGTH_SHORT).show();
//
//                }
//            });
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
                        String phoneNo = "" + cursor.getString(phoneNumberIndex).replaceAll("[\\D\\s\\-()]", "");
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
                        contactsInfo.put(Constants.DiscoverContact.TYPE, "1");
                        contactsInfo.put(Constants.DiscoverContact.PHONE_NUMBER, phoneNo);
                        contactsInfo.put(Constants.DiscoverContact.USER_NAME, cursor.getString(displayNameIndex));
//                        contactsInfo.put(Constants.DiscoverContact.CONTACT_ID, userId);

                        Contact contact = new Contact();
                        contact.setType("1");
                        contact.setName(cursor.getString(displayNameIndex));
                        contact.setNumber(phoneNo);
                        Log.v("CountryCode:",phoneNo);
                        contacts.add(contact);
                        localContactsList.add(contactsInfo);
                    }
                } finally {
                    cursor.close();
                }
            }
            contactRequest.setContacts(contacts);
            return true;

        }

        @Override
        protected void onPostExecute(Object o) {
            contactSyncWithAPI(contactRequest, PAGE_SIZE * page, PAGE_SIZE);
        }
    }

    public void contactSyncWithAPI(ContactRequest request, int skip, int limit) {
//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                Toast.makeText(mActivity.getApplication(), "ContactSync: ContactSync Api", Toast.LENGTH_SHORT).show();
//            }
//        });
        AppController.getInstance()
                .subscribeToTopic(
                        MqttEvents.ContactSync.value + "/" + AppController.getInstance().getUserId(), 1);

        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(
                AppController.getInstance().getApplicationContext());
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiOnServer.TYPE + ApiOnServer.TRENDING_HOST_API)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit.create(HowdooServiceTrending.class)
                .contactSync(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                        skip,
                        limit,
                        request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<Contacts>>() {
                    @Override
                    public void onNext(Response<Contacts> response) {
//                        mActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(mActivity.getApplication(), "ContactSync: Response Code:  "+ response.code(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
                        switch (response.code()) {
                            case 200:
                                if (response.body() != null) {
                                    Log.v("HelloTestTime","HelloTestTime");
                                    isLastPage = response.body().isLastPage();

                                    saveContactsFromApiResponse(response.body().getContacts());

                                    if (!isLastPage) {
                                        //page++;
                                        //contactSync(contactHolder.getContactRequest(),page*PAGE_SIZE,PAGE_SIZE);
                                    } else {
                                        page = 0;
                                        AppController.getInstance().setContactSynced(true);
                                        AppController.getInstance().setFriendsFetched(true);
                                    }
                                }

                                AppController.getInstance().registerContactsObserver();
                                break;
                            case 401:
                                break;
                            case 406:
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
                                                        contactSyncWithAPI(request, PAGE_SIZE * page, PAGE_SIZE);
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
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        mActivity.runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(mActivity.getApplication(), "ContactSync: Response Code: E  "+ e.toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
                        //view.empty(true);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * <p>It saves contact details to database which we get from server API</p>
     *
     * @param contactsList : contains list of contacts
     */
    void saveContactsFromApiResponse(ArrayList<Contact> contactsList) {
        try {

            ArrayList<Map<String, Object>> friends = new ArrayList<>();

            for (Contact f : contactsList) {

                Map<String, Object> friend = new HashMap<>();

                friend.put("userId", f.getId());
                friend.put("userName", f.getUserName());
                //friend.put("countryCode", f.getCountryCode());
                friend.put("number", f.getNumber());
                friend.put("name",f.getName());
                friend.put("type",f.getType());
                friend.put("isInvite",f.isInvite());
                friend.put("profilePic", f.getProfilePic());
                friend.put("firstName", f.getFirstName());
                friend.put("lastName", f.getLastName());
                //friend.put("socialStatus", f.getStatus());
                friend.put("isStar", f.isStar());
//                friend.put("starRequest", f.getStarData());
//                friend.put("private", f.getPrivate());
                friend.put("friendStatusCode", f.getFriendStatusCode());
                friend.put("followStatus", f.getFollowStatus());
//                friend.put("timestamp", f.getTimestamp());
//                friend.put("message", f.getMessage());
                friend.put("isChatEnable", true);

                //            Log.d("log1","friends data-->"+f.isStar());

                friends.add(friend);

                friend = null;
            }

            AppController.getInstance()
                    .getDbController()
                    .insertFriendsInfo(friends, AppController.getInstance().getFriendsDocId());
            AppController.getInstance()
                    .getDbController()
                    .updateAllContacts(
                            localContactsList,
                            AppController.getInstance().getAllContactsDocId()
                    );
            Log.v("HelloTestTime1","HelloTestTime1");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("eventName", MqttEvents.ContactSync.value + "/" + AppController.getInstance().getUserId());
            AppController.getBus().post(jsonObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

