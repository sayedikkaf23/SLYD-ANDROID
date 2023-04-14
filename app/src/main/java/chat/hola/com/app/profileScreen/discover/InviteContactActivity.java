package chat.hola.com.app.profileScreen.discover;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ezcall.android.R;
import com.google.firebase.BuildConfig;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.ContactPermissionDialog;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.profileScreen.discover.contact.InviteContactAdapter;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contact;

public class InviteContactActivity extends AppCompatActivity implements DiscoverContract.View, InviteContactAdapter.FilterListener,InviteContactAdapter.ClickListner {

    InviteContactAdapter contactAdapter;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.btnSkip)
    Button btnSkip;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.recyclerContact)
    RecyclerView mRecyclerContact;
    @BindView(R.id.srSwipe)
    SwipeRefreshLayout srSwipe;
    @BindView(R.id.llErrorLayout)
    LinearLayout llErrorLayout;
    @BindView(R.id.tvErrorMessage)
    TextView tvErrorMessage;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.btnFindPeople)
    Button btnFindPeople;
    @BindView(R.id.ivErrorImage)
    ImageView ivErrorImage;

    private Bus bus = AppController.getBus();
    private Unbinder unbinder;
    private String caller = "";
    private ArrayList<Contact> contactsList;
    private boolean doubleBackToExitPressedOnce = false;
    private LinearLayoutManager llm;

    private ArrayList<String> alreadyAddedContacts = new ArrayList<>();
    private ArrayList<Map<String, Object>> localContactsList = new ArrayList<>();

    @Override
    public void userBlocked() {
//        dialog.show();
    }

    @Override
    public void sessionExpired() {
//        sessionManager.sessionExpired(getApplicationContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {
        //    llNetworkError.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    @Override
    public void triggerSearch(String query, @Nullable Bundle appSearchData) {
        super.triggerSearch(query, appSearchData);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        unbinder = ButterKnife.bind(this);
        bus.register(this);

        tvTitle.setText(getString(R.string.Invite));
        contactAdapter = new InviteContactAdapter(this);
        caller = getIntent().getStringExtra("caller");
        callerHandle(caller == null ? "" : caller);
//        presenter.init();

        contactsList = new ArrayList<>();

        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(getResources().getDrawable(R.drawable.divider));

        llm = new LinearLayoutManager(this);
        mRecyclerContact.setLayoutManager(llm);
        mRecyclerContact.setHasFixedSize(true);
        mRecyclerContact.setAdapter(contactAdapter);
        mRecyclerContact.addItemDecoration(itemDecorator);

        contactAdapter.setListener(this);
        contactAdapter.setFilterListener(this);


        permissionAllow();

        srSwipe.setOnRefreshListener(() -> permissionAllow());

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (etSearch.getText().toString().isEmpty()) {
                    srSwipe.setRefreshing(true);
                    permissionAllow();
                    tvErrorMessage.setText(getString(R.string.none_contacts));
                    ivErrorImage.setImageResource(R.drawable.ic_default_post);
                } else {
                    contactAdapter.getFilter().filter(etSearch.getText().toString().toLowerCase().trim());
                    empty(contactAdapter.contactsFiltered.size() == 0);
                }
            }
        });
    }


    @Subscribe
    public void getMessage(JSONObject object) {
        //presenter.mqttResponse(object);
    }

    @Override
    public void postToBus() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("eventName", "contactRefreshed");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bus.post(obj);
    }

    @Override
    public void onUserSelected(int position) {

    }

    @Override
    public void onUserSelected(Contact contact) {
        if(contact.getNumber() != null && !contact.getNumber().isEmpty()){
            sendSMS(contact.getNumber());
        }
    }

    public void sendSMS(String phoneNumber){
        String shareMessage= "\nCheck out "+getString(R.string.app_name)+" for your smartphone. Download it today from\n\n";
        shareMessage = shareMessage+"https://play.google.com/store/apps/details?id="+ BuildConfig.APPLICATION_ID +"\n\n";
        String uri= "smsto:"+phoneNumber;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        intent.putExtra("sms_body", shareMessage);
        startActivity(intent);
    }

    @Override
    public void onFollow(String id, boolean checked, int position) {

    }

    @Override
    public void add(int position) {
        Contact data = contactsList.get(position);
    }

    @Override
    public void view(int position) {

    }

    @Override
    public void empty(boolean b) {
        try {
            llErrorLayout.setVisibility(b ? View.VISIBLE : View.GONE);
            mRecyclerContact.setVisibility(b ? View.GONE : View.VISIBLE);
            if (srSwipe != null && srSwipe.isRefreshing())
                srSwipe.setRefreshing(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void isFollowing(int pos, int status) {
        contactsList.get(pos).setFollowStatus(status);
        contactAdapter.notifyItemChanged(pos);
    }

    @Override
    public void onFilter(int count) {
        if (count > 0) {
            llErrorLayout.setVisibility(View.GONE);
            mRecyclerContact.setVisibility(View.VISIBLE);
        } else {
            mRecyclerContact.setVisibility(View.GONE);
            llErrorLayout.setVisibility(View.VISIBLE);
            ivErrorImage.setImageResource(R.drawable.ic_error);
            tvErrorMessage.setText(getString(R.string.NoDataFound));
        }
    }

    @OnClick(R.id.btnFindPeople)
    public void find() {
        reload();
    }

    private void callerHandle(String caller) {
        switch (caller) {
            case "SaveProfile":
                btnSkip.setVisibility(View.VISIBLE);
                ivBack.setVisibility(View.GONE);
                break;
            default:
                ivBack.setVisibility(View.VISIBLE);
                btnSkip.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void applyFont() {
    }

    @Override
    public void changeSkipText(String text) {
        btnSkip.setText(text);
    }

    @Override
    public void showContacts(ArrayList<Contact> contacts, boolean clear) {
        if (contacts == null || contacts.isEmpty()) {
            if (clear)
                empty(true);
        } else {
            try {
                empty(false);

                if (clear)
                    contactsList.clear();
                contactsList.addAll(contacts);
                Collections.sort(contactsList, (obj1, obj2) -> obj1.getFirstName().compareToIgnoreCase(obj2.getFirstName()));

                for (int i = 0; i < contactsList.size(); i++)
                    contactsList.get(i).setFullName(contactsList.get(i).getFirstName(), contactsList.get(i).getLastName());

                contactAdapter.setData(contactsList);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if (srSwipe != null && srSwipe.isRefreshing())
            srSwipe.setRefreshing(false);
    }

    @OnClick({R.id.ivBack, R.id.btnSkip})
    public void back() {
        if (caller != null && caller.equals("SaveProfile")) {
            Intent i = new Intent(this, LandingActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i);
            supportFinishAfterTransition();
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (!caller.equals("SaveProfile")) {
                super.onBackPressed();
            } else {
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();

                    supportFinishAfterTransition();
                    //actionBarRl.setVisibility(View.VISIBLE);
                    return;
                }

                this.doubleBackToExitPressedOnce = true;
                showMessage(null, R.string.back_press_message);

                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        } catch (Exception e) {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //if (AppController.getInstance().getContactSynced() && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
        //    presenter.fetchContact();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void showMessage(String msg, int msgId) {
        if (msg != null && !msg.isEmpty()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else if (msgId != 0) {
            Toast.makeText(this, getResources().getString(msgId), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void reload() {
//        presenter.init();
    }

    public void permission() {
        srSwipe.setRefreshing(false);
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_CONTACTS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        // permission is granted, open the camera
                        srSwipe.setRefreshing(true);
                        fetchContactForInvite();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We need permission to access your contact list, please grant");
        builder.setPositiveButton("Setting", (dialogInterface, i) -> {

            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        });
        builder.show();

        srSwipe.setRefreshing(false);
    }


    private void permissionAllow() {

        if (ContextCompat.checkSelfPermission(InviteContactActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ContactPermissionDialog permissionDialog = new ContactPermissionDialog(this);
            permissionDialog.show();

            Button btnAccept = permissionDialog.findViewById(R.id.btnContinue);
            btnAccept.setOnClickListener(v -> {
                permissionDialog.dismiss();
                permission();
            });
        } else {
            // permission already granted
            permission();
        }

    }


    private void fetchContactForInvite() {
        new syncContactsForInvite().execute();
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private class syncContactsForInvite extends AsyncTask {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {

            ArrayList<Contact> contacts = new ArrayList<>();
            alreadyAddedContacts.clear();
            localContactsList.clear();

            ContentResolver cr = getContentResolver();
            Cursor cursor = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, ContactModel.PROJECTION, null, null, null);

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
                        contactsInfo.put(Constants.DiscoverContact.TYPE, type);
                        contactsInfo.put(Constants.DiscoverContact.PHONE_NUMBER, phoneNo);
                        contactsInfo.put(Constants.DiscoverContact.USER_NAME, cursor.getString(displayNameIndex));
                        contactsInfo.put(Constants.DiscoverContact.CONTACT_ID, userId);

                        Contact contact = new Contact();
                        contact.setType("1");
                        contact.setNumber(phoneNo);
                        contact.setFirstName(cursor.getString(displayNameIndex));
                        contacts.add(contact);
                        localContactsList.add(contactsInfo);
                    }
                } finally {
                    cursor.close();
                }
            }

            showContacts(contacts, true);
            return true;

        }

        @Override
        protected void onPostExecute(Object o) {
        }
    }
}
