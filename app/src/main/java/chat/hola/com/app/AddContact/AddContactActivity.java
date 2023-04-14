package chat.hola.com.app.AddContact;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.otto.Bus;
import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.contact.Friend;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.qr_code_scanner.QRCodeActivity;
import chat.hola.com.app.search_user.SearchUserActivity;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h1>AddContactActivity</h1>
 *
 * @author 3Embed
 * @since 4/5/2018.
 */

public class AddContactActivity extends DaggerAppCompatActivity implements AddContactContract.View {
    private Unbinder unbinder;
    private Bus bus = AppController.getBus();

    @Inject
    SessionManager sessionManager;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    AddContactPresenter mPresenter;

    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvMyIdTitle)
    TextView tvMyIdTitle;
    @BindView(R.id.tvStarChatId)
    TextView tvStarChatId;
    @BindView(R.id.atvSearch)
    TextView atvSearch;

    @BindView(R.id.tvQrCodeTitle)
    TextView tvQrCodeTitle;
    @BindView(R.id.tvQrCodeDescription)
    TextView tvQrCodeDescription;
    @BindView(R.id.tvContactTitle)
    TextView tvContactTitle;
    @BindView(R.id.tvContactDescription)
    TextView tvContactDescription;
    @BindView(R.id.tvOfficialAccountTitle)
    TextView tvOfficialAccountTitle;
    @BindView(R.id.tvOfficialAccountDescription)
    TextView tvOfficialAccountDescription;

    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.toolbar)
    RelativeLayout toolbar;

    private List<Friend> friends;
    private boolean isSearch = false;

    @Override
    public void userBlocked() {
    }

    @Inject
    public AddContactActivity() {
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        bus.register(this);
        unbinder = ButterKnife.bind(this);
        mPresenter.attachView(this);

        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        tvMyIdTitle.setTypeface(typefaceManager.getRegularFont());
        tvStarChatId.setTypeface(typefaceManager.getRegularFont());
        tvContactTitle.setTypeface(typefaceManager.getSemiboldFont());
        tvQrCodeTitle.setTypeface(typefaceManager.getSemiboldFont());
        tvOfficialAccountTitle.setTypeface(typefaceManager.getSemiboldFont());
        tvContactDescription.setTypeface(typefaceManager.getRegularFont());
        tvQrCodeDescription.setTypeface(typefaceManager.getRegularFont());
        tvOfficialAccountDescription.setTypeface(typefaceManager.getRegularFont());

        tvStarChatId.setText(AppController.getInstance().getUserName());

        friends = new ArrayList<>();
//        atvSearch.setAdapter(adapter);

        atvSearch.setOnClickListener(v -> {
            startActivity(new Intent(AddContactActivity.this, SearchUserActivity.class));
//                updateUI(true);
        });
//
//        atvSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() > 0)
//                    mPresenter.search(s.toString());
//                updateUI(s.length() > 0);
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
    }

    private void updateUI(boolean isSearch) {
//        this.isSearch = isSearch;
//        llContent.setVisibility(isSearch ? View.GONE : View.VISIBLE);
//        toolbar.setVisibility(isSearch ? View.GONE : View.VISIBLE);
    }

    @OnClick(R.id.rlContact)
    public void contact() {
        startActivity(new Intent(this, DiscoverActivity.class));
    }

    @OnClick(R.id.rlQRCode)
    public void invite() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(AddContactActivity.this, QRCodeActivity.class);
                            startActivity(intent);
                        } else if (!report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(AddContactActivity.this, "Need the Camera Permission !", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            Toast.makeText(AddContactActivity.this, "Please give the Camera Permission from settings", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public void onBackPressed() {
        if (isSearch)
            updateUI(false);
        else
            super.onBackPressed();
    }

    @OnClick(R.id.ivScan)
    public void scan() {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    @OnClick(R.id.ivBack)
    public void back() {
        super.onBackPressed();
    }

    @Override
    public void showMessage(String msg, int msgId) {
        Toast.makeText(this, msg != null && !msg.isEmpty() ? msg : getResources().getString(msgId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(this);
    }


    @Override
    public void isInternetAvailable(boolean flag) {
    }

    @Override
    public void onDestroy() {
        mPresenter.detachView();
        if (unbinder != null)
            unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void reload() {
    }

    @Override
    public void finishActivity() {
        finish();
    }

    @Override
    public void showData(List<Friend> data) {
        if (data.isEmpty()) {
            noData();
        } else {
            friends.clear();
            friends.addAll(data);
//            adapter = new SearchUserAdapter(this, R.layout.simple_item, friends, typefaceManager, this);
//            atvSearch.setAdapter(adapter);
        }
    }

    @Override
    public void noData() {
    }

}