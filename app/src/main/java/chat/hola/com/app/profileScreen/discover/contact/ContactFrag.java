package chat.hola.com.app.profileScreen.discover.contact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.profileScreen.discover.DiscoverPresenter;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contact;
import dagger.android.support.DaggerFragment;

/**
 * <h>FollowFrag.class</h>
 * <p>  This fragment shows list of posts , links and following info in
 * a recyclerView which is populated by {@link ContactAdapter}
 * </p>
 *
 * @author 3Embed
 * @since 02/03/18.
 */

public class ContactFrag extends DaggerFragment implements ContactContract.View, SwipeRefreshLayout.OnRefreshListener, ContactAdapter.ClickListner {

    private static final String TAG = "ContactFrag";
    @Inject
    ContactPresenter presenter;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    ContactAdapter contactAdapter;
    @Inject
    DiscoverPresenter discoverPresenter;
    @Inject
    SessionManager sessionManager;
    @Inject
    BlockDialog dialog;

    @BindView(R.id.recyclerContact)
    RecyclerView mRecyclerContact;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvMsg)
    TextView tvMsg;
    @BindView(R.id.btnFollowAll)
    Button btnFollowAll;
    @BindView(R.id.srSwipe)
    SwipeRefreshLayout srSwipe;
    @BindView(R.id.llHeader)
    LinearLayout llHeader;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;
    @BindView(R.id.llPermission)
    LinearLayout llPermission;

    private Bus bus = AppController.getBus();
    private Unbinder unbinder;
    private List<String> contactIds = new ArrayList<>();
    private String prefixTitle = "";
    private static final int REQUEST_PERMISSION = 101;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {
    }

    @Inject
    public ContactFrag() {
    }

    private DiscoverActivity mActivity;
    private ArrayList<Contact> contactsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus.register(this);
        prefixTitle = getResources().getString(R.string.FollowFragTitle);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_tab, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.attachView(this);
        return rootView;
    }

    @OnClick(R.id.btnFollowAll)
    public void followAll() {
        srSwipe.setRefreshing(true);
        presenter.followAll(contactIds);
        btnFollowAll.setEnabled(false);
    }

    @Override
    public void initialization() {
        mActivity = (DiscoverActivity) getActivity();
        contactsList = new ArrayList<>();
        srSwipe.setOnRefreshListener(this);
    }

    @Override
    public void requestContactsPermission() {

    }

    @Override
    public void applyFont() {
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        tvMsg.setTypeface(typefaceManager.getMediumFont());
        btnFollowAll.setTypeface(typefaceManager.getSemiboldFont());
        tvEmpty.setTypeface(typefaceManager.getMediumFont());
    }

    @Override
    public void initPostRecycler() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mRecyclerContact.setLayoutManager(llm);
        mRecyclerContact.setHasFixedSize(true);
        mRecyclerContact.setAdapter(contactAdapter);
        contactAdapter.setListener(this);
    }

    @Override
    public void isFollowing(int pos, boolean flag, int status, int isPrivate, boolean isFollowAll) {
//        contactsList.get(pos).setFollowing(status);
//        contactAdapter.notifyDataSetChanged();
        sessionManager.setFollowAll(isFollowAll);
        if (isFollowAll) {
            llHeader.setVisibility(View.GONE);
            btnFollowAll.setEnabled(false);

            JSONObject obj = new JSONObject();
            try {
                obj.put("eventName", "postRefresh");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            bus.post(obj);
        } else if (!flag) {
            llHeader.setVisibility(View.VISIBLE);
            btnFollowAll.setEnabled(true);
        } else if (flag) {
            JSONObject obj = new JSONObject();
            try {
                obj.put("eventName", "postRefresh");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            bus.post(obj);
        }


        JSONObject obj = new JSONObject();
        try {
            obj.put("eventName", "profileUpdated");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        bus.post(obj);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showContacts(ArrayList<Contact> contacts) {
        llPermission.setVisibility(View.GONE);
        if (contacts == null || contacts.isEmpty()) {
            llEmpty.setVisibility(View.VISIBLE);
            mRecyclerContact.setVisibility(View.GONE);
            llHeader.setVisibility(View.GONE);
        } else {
            llEmpty.setVisibility(View.GONE);
            mRecyclerContact.setVisibility(View.VISIBLE);
            for (Contact contact : contacts)
                contactIds.add(contact.getId());
            tvTitle.setText(contacts.size() + " " + prefixTitle);
            contactsList.clear();
            contactsList.addAll(contacts);
            contactAdapter.setData(contacts);
            contactAdapter.notifyDataSetChanged();
            llHeader.setVisibility(sessionManager.isFollowAll() ? View.GONE : View.VISIBLE);
        }
        srSwipe.setRefreshing(false);
    }

    @Override
    public void loading(boolean flag) {
        srSwipe.setRefreshing(flag);
    }

    @Override
    public void showMessage(String msg, int msgId) {
        if (msg != null && !msg.isEmpty()) {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        } else if (msgId != 0) {
            Toast.makeText(getContext(), getResources().getString(msgId), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
        if (unbinder != null)
            unbinder.unbind();
        presenter.detachView();
    }


    @Subscribe
    public void getMessage(JSONObject object) {
        presenter.mqttResponse(object);
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
    public void followedAll(boolean flag) {
        llEmpty.setVisibility(View.GONE);
        sessionManager.setFollowAll(flag);
        if (flag) {
            presenter.fetchContact();
            llHeader.setVisibility(View.GONE);
            JSONObject obj = new JSONObject();
            try {
                obj.put("eventName", "postRefresh");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            bus.post(obj);
        } else
            btnFollowAll.setEnabled(true);
        srSwipe.setRefreshing(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void gotoSettings() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("We need permission to access your contact list, please grant");
        builder.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (getActivity() == null) {
                    return;
                }
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                intent.setData(uri);
                ContactFrag.this.startActivity(intent);
            }
        });
        builder.show();

        srSwipe.setRefreshing(false);
        llEmpty.setVisibility(View.GONE);
    }

    private void showPermissionMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("We need permission to access your contact list, please grant");
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.requestContactsPermission();

            }
        });
        builder.show();

        srSwipe.setRefreshing(false);
//        llPermission.setVisibility(View.VISIBLE);
        llEmpty.setVisibility(View.GONE);
    }


//    @Override
//    public void follow(int position, boolean isFollowing) {
//        try {
//            if (isFollowing)
//                presenter.unfollow(position, contactsList.get(position).getId());
//            else
//                presenter.follow(position, contactsList.get(position).getId());
//
//            discoverPresenter.changeText(getString(R.string.next));
//
//        } catch (Exception e) {
//
//        }
//    }

    @Override
    public void onRefresh() {
        //permission();
    }

    @Override
    public void reload() {

    }

    @Override
    public void add(int position) {

    }

    @Override
    public void view(int position) {

    }

    @Override
    public void onUserSelected(int position) {

//        Intent intent = new Intent(getContext(), ProfileActivity.class);
//        intent.putExtra(Constants.SocialFragment.USERID, presenter.getUserId(position));
//        startActivity(intent);
    }

    @Override
    public void onFollow(String id, boolean checked, int position) {

    }
}