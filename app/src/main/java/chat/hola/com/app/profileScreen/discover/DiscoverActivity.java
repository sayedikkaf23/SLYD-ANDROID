package chat.hola.com.app.profileScreen.discover;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ezcall.android.R;
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

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Dialog.ContactPermissionDialog;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.activity.followingTab.FollowingFrag;
import chat.hola.com.app.home.activity.youTab.YouFrag;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.discover.contact.ContactAdapter;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contact;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h>DiscoverActivity.class</h>
 * <p>
 * This fragment has two tabs {@link FollowingFrag} and {@link YouFrag}
 * which is handle by {@link DiscoverPageAdapter}.
 *
 * @author 3Embed
 * @since 02/03/18.
 */

public class DiscoverActivity extends DaggerAppCompatActivity implements DiscoverContract.View {

    @Inject
    DiscoverPresenter presenter;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    ContactAdapter contactAdapter;
    @Inject
    BlockDialog dialog;

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

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getApplicationContext());
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

        caller = getIntent().getStringExtra("caller");
        callerHandle(caller == null ? "" : caller);
        presenter.init(this);

        contactsList = new ArrayList<>();

        DividerItemDecoration itemDecorator = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(getResources().getDrawable(R.drawable.divider));

        llm = new LinearLayoutManager(this);
        mRecyclerContact.setLayoutManager(llm);
        mRecyclerContact.setHasFixedSize(true);
        mRecyclerContact.setAdapter(contactAdapter);
        mRecyclerContact.addItemDecoration(itemDecorator);

        contactAdapter.setListener(presenter);
        contactAdapter.setFilterListener(presenter);


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
                    empty(contactAdapter.contactsFiltered.size()==0);
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
        Contact data = contactsList.get(position);
//        Intent intent = new Intent(this, ProfileActivity.class);
//        intent.putExtra("userId", data.getId());
//        startActivity(intent);
    }

    @Override
    public void add(int position) {
        Contact data = contactsList.get(position);
//        if (data.get_private() == 1) {
//            Intent intent = new Intent(this, AcceptRequestActivity.class);
//            intent.putExtra("userId", data.getId());
//            intent.putExtra("userName", data.getUserName());
//            intent.putExtra("firstName", data.getUserName());
//            intent.putExtra("lastName", "");
//            intent.putExtra("profilePic", data.getProfilePic());
//            intent.putExtra("mobileNumber", data.getNumber());
//            intent.putExtra("call", "send");
//            startActivity(intent);
//        } else {
        presenter.addAsFriend(data);
//        }
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
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        btnSkip.setTypeface(typefaceManager.getSemiboldFont());
        tvErrorMessage.setTypeface(typefaceManager.getSemiboldFont());
        btnFindPeople.setTypeface(typefaceManager.getSemiboldFont());
    }

    @Override
    public void changeSkipText(String text) {
        btnSkip.setText(text);
    }

    @Override
    public void showContacts(ArrayList<Contact> contacts, boolean clear) {
        if (contacts == null || contacts.isEmpty()) {
            if(clear)
                empty(true);
        } else {
            try {
                empty(false);

                if(clear)
                    contactsList.clear();

//                Set<String> temp = new HashSet<>();
//                for (int i = 0; i < contacts.size(); i++) {
//                    String obj1 = Character.toString(contacts.get(i).getFirstName().charAt(0)).toUpperCase();
//                    String obj2;
//                    try {
//                        obj2 = Character.toString(contacts.get(i + 1).getFirstName().charAt(0)).toUpperCase();
//                    } catch (IndexOutOfBoundsException e) {
//                        obj2 = "#";
//                    }
//
//                    if (!obj1.equalsIgnoreCase(obj2)) {
//                        temp.add(obj1);
//                    }
//
//                }
//
//                for (String title : temp) {
//                    Contact contact = new Contact();
//                    contact.setTitle(title);
//                    contact.setFirstName(title);
//                    contact.setIsTitle(true);
//                    contactsList.add(contact);
//                }

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
        presenter.init(this);
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
                        presenter.fetchContact();
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

        if (ContextCompat.checkSelfPermission(DiscoverActivity.this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ContactPermissionDialog permissionDialog = new ContactPermissionDialog(this);
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
}
