package chat.hola.com.app.settings;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Activities.DataUsage;
import chat.hola.com.app.Activities.MainActivity;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.PostDb;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.authentication.newpassword.NewPasswordActivity;
import chat.hola.com.app.blockUser.BlockUserActivity;
import chat.hola.com.app.collections.saved.SavedActivity;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.business.StartBusinessProfileActivity;
import chat.hola.com.app.profileScreen.business.configuration.BusinessConfigurationActivity;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.referral_code.ReferralCodeActivity;
import chat.hola.com.app.request_star_profile.request_star.RequestStarProfileActivity;
import chat.hola.com.app.star_configuration.StarConfigurationActivity;
import chat.hola.com.app.subscription.SubscriptionActivity;
import chat.hola.com.app.webScreen.WebActivity;

import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;

import dagger.android.support.DaggerAppCompatActivity;

import java.io.Serializable;

import javax.inject.Inject;

/**
 * <h1>SettingsActivity</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 24/2/18.
 */
public class SettingsActivity extends DaggerAppCompatActivity implements SettingsContract.View {

    private Unbinder unbinder;
    private String version = "";

    @Inject
    SessionManager sessionManager;
    @Inject
    SettingsPresenter presenter;
    @Inject
    TypefaceManager typefaceManager;

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ivBack)
    ImageView ivBack;
    @BindView(R.id.tvTitleInvite)
    TextView tvTitleInvite;
    @BindView(R.id.tvChangePassword)
    TextView tvChangePassword;
    @BindView(R.id.tvInvitesFriend)
    TextView tvInviteFriend;
    @BindView(R.id.tV_saved)
    TextView tvSaved;
    @BindView(R.id.tvTitleFollowPeople)
    TextView tvTitleFollowPeople;
    @BindView(R.id.tvFindFbFriend)
    TextView tvFindFbFriend;
    @BindView(R.id.tvFindContacts)
    TextView tvFindContacts;
    @BindView(R.id.tvTitleSettings)
    TextView tvTitleSettings;
    @BindView(R.id.tvDataUses)
    TextView tvDataUses;
    @BindView(R.id.tvBlockedUsers)
    TextView tvBlockedUsers;
    @BindView(R.id.tV_requestStar)
    TextView tV_requestStar;
    @BindView(R.id.tV_starConfig)
    TextView tV_starConfig;
    @BindView(R.id.tvTitleSupport)
    TextView tvTitleSupport;
    @BindView(R.id.tvReportAProb)
    TextView tvReportAProb;
    @BindView(R.id.tvDeleteAccount)
    TextView tvDeleteAccount;
    @BindView(R.id.tvTitleAbout)
    TextView tvTitleAbout;
    @BindView(R.id.tvAbout)
    TextView tvAbout;
    @BindView(R.id.tvPrivacy)
    TextView tvPrivacy;
    @BindView(R.id.tvTermsOfService)
    TextView tvTermsOfService;
    @BindView(R.id.tvVersion)
    TextView tvVersion;
    @BindView(R.id.tvLogout)
    TextView tvLogout;
    @BindView(R.id.root)
    RelativeLayout rlRoot;
    @Inject
    BlockDialog dialog;
    @Inject
    PostDb postDb;
    @BindView(R.id.tV_switchProfile)
    TextView tV_switchProfile;
    @BindView(R.id.rlSwitchProfile)
    RelativeLayout rlSwitchProfile;
    @BindView(R.id.rlStarConfig)
    RelativeLayout rlStarConfig;
    @BindView(R.id.tvSwitchProfile)
    TextView tvSwitchProfile;
    @BindView(R.id.rlBusinessProfile)
    RelativeLayout rlBusinessProfile;
    @BindView(R.id.rlSwitchBusinessProfile)
    RelativeLayout rlSwitchBusinessProfile;

    @BindView(R.id.tvBusinessSection)
    TextView tvBusinessSection;
    @BindView(R.id.tvVerifiedProfile)
    TextView tvVerifiedProfile;
    @BindView(R.id.rlVerifiedProfile)
    RelativeLayout rlVerifiedProfile;
    private Loader loader;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    private Data profileData;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_new);
        unbinder = ButterKnife.bind(this);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        loader = new Loader(this);
        profileData = (Data) getIntent().getSerializableExtra("profileData");
        presenter.init();

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
            String v = "v" + version;
            tvVersion.setText(v);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        rlBusinessProfile.setVisibility(sessionManager.appliedBusinessProfile() ? View.GONE : View.VISIBLE);
        rlVerifiedProfile.setVisibility(sessionManager.isBusinessProfileAvailable() ? View.GONE : View.VISIBLE);
        tvVerifiedProfile.setVisibility(sessionManager.isBusinessProfileAvailable() ? View.GONE : View.VISIBLE);

        rlStarConfig.setVisibility(sessionManager.isStar() ? View.VISIBLE : View.GONE);
        rlSwitchBusinessProfile.setVisibility(sessionManager.isBusinessProfileApproved() ? View.VISIBLE : View.GONE);
        tvSwitchProfile.setText("Switch to " + (sessionManager.isBusinessProfileAvailable() ? "personal profile" : "business profile"));

        rlSwitchBusinessProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBusiness = !sessionManager.isBusinessProfileAvailable();
                presenter.switchBusiness(isBusiness, sessionManager.getBusinessCategoryId());
            }
        });
    }

    @Override
    public void showMessage(String msg, int msgId) {

    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getApplicationContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @OnClick(R.id.rlSwitchProfile)
    public void onSwitchProfile() {
        if (!profileData.isStar()) {
            Intent intent = new Intent(SettingsActivity.this, RequestStarProfileActivity.class);
            intent.putExtra("profileData", profileData);
            startActivityForResult(intent, 555);
        }
    }

    @OnClick(R.id.tvLogout)
    public void callLogout() {
        presenter.logout();
    }

    @OnClick(R.id.ivAppIcon)
    public void forceCrash() {
//        if(BuildConfig.DEBUG)
//            throw new RuntimeException("This is a crash");
    }

    @OnClick(R.id.tvChangePassword)
    public void changePassword() {
        startActivity(new Intent(this, NewPasswordActivity.class).putExtra("call", "setting"));
    }

    @Override
    public void applyFont() {
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
    }

    @OnClick(R.id.tV_saved)
    public void openSaved() {
        startActivity(new Intent(this, SavedActivity.class));
    }

    @OnClick(R.id.tvBusinessProfile)
    public void business() {
        startActivity(new Intent(this, StartBusinessProfileActivity.class));
    }

    /*
     * Bug Title: Profile Page-> SProfile Page-> Settings-> Not showing subscribers user list
     * Bug Id: DUBAND143
     * Fix Desc: add SubscriptionActivity
     * Fix Dev: Hardik
     * Fix Date: 7/5/21
     * */

    /**
     * <p>redirects to {@link chat.hola.com.app.subscription.SubscriptionActivity}'s activity</p>
     */
    @OnClick(R.id.tvSubscriptions)
    public void subscriptions() {
        Intent intent = new Intent(SettingsActivity.this, SubscriptionActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tV_subscription)
    public void startSubscription() {
        openSubscriptionDialog();
    }

    /*
     * Bug Title: Profile Page-> Settings-> Not showing subscribtion setting option
     * Bug Id: DUBAND138
     * Fix Desc: add subscribtion setting
     * Fix Dev: Hardik
     * Fix Date: 7/5/21
     * */

    private void openSubscriptionDialog() {
        SetSubscriptionDialog setSubscriptionDialog = new SetSubscriptionDialog(profileData, (amount, youAmount, appAmount) -> {
            presenter.postSubscription(amount, youAmount, appAmount);
        });
        setSubscriptionDialog.show(this.getSupportFragmentManager(), "set subscription");
    }

    @Override
    public void logout() {
        postDb.delete();
        sessionManager.logOut(this);
        finish();
    }

    @Override
    public void delete() {
        postDb.delete();
        sessionManager.delete(this);
        finish();
    }

    @Override
    public void gotoProfile(boolean isBusiness) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finishAffinity();
    }

    @Override
    public void showLoader() {
        loader.show();
    }

    @Override
    public void hideLoader() {
        if (loader != null && loader.isShowing()) loader.dismiss();
    }

    @OnClick(R.id.tvReportAProb)
    public void report() {

        String stringBuilder = "\n\n\n\n\n\n--------------------------------------------------\n\n"
                + "Device Id: "
                + AppController.getInstance().getDeviceId()
                + "\n"
                + "Device Name: "
                + Build.DEVICE
                + "\n"
                + "Device OS: "
                + Build.VERSION.RELEASE
                + "\n"
                + "Model Number: "
                + Build.MODEL
                + "\n"
                + "Device Type: "
                + "Android"
                + "\n"
                + "App Version: "
                + version;

        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.setType("message/rfc822");
        i.setData(Uri.parse("mailto:appscrip@gmail.com"));
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"appscrip@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Report a problem");
        i.putExtra(Intent.EXTRA_TEXT, stringBuilder);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.tvDeleteAccount)
    public void deleteAccount() {
        presenter.deleteAccount(SettingsActivity.this);
    }

    @OnClick(R.id.tV_requestStar)
    public void requestStarProfile() {
        Intent intent = new Intent(SettingsActivity.this, RequestStarProfileActivity.class);
        intent.putExtra("profileData", profileData);
        startActivityForResult(intent, 555);
    }

    @OnClick(R.id.tV_starConfig)
    public void starConfiguration() {
        Intent intent = new Intent(SettingsActivity.this, StarConfigurationActivity.class);
        intent.putExtra("profileData", profileData);
        startActivity(intent);
    }

    /**
     * <p>invite friend</p>
     */
    @OnClick(R.id.tvInvitesFriend)
    public void inviteFriend() {

//        Intent intent = new Intent(SettingsActivity.this, ReferralCodeActivity.class);
//        intent.putExtra("profileData", profileData);
//        startActivity(intent);

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.inviteMsg) + "\n " + getString(R.string.howdooPlayStore));
                intent.setType("text/plain");
                Intent chooser = Intent.createChooser(intent, getString(R.string.selectApp));
                startActivity(chooser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode, requestCode, data, RESULT_OK);
        if (resultCode == 555) {
            onBackPressed();
        }
    }

    /**
     * <p>redirects to DiscoverActivity's facebook tab to follow facebook friend</p>
     */
    @OnClick(R.id.tvFindFbFriend)
    public void followFbFriend() {
        Intent intent = new Intent(SettingsActivity.this, DiscoverActivity.class);
        intent.putExtra("caller", "SettingsActivity");
        intent.putExtra("is_contact", false);
        startActivity(intent);
    }

    /**
     * <p>redirects to DiscoverActivity's contact tab to follow facebook friend</p>
     */
    @OnClick(R.id.tvFindContacts)
    public void followContact() {
        Intent intent = new Intent(SettingsActivity.this, DiscoverActivity.class);
        intent.putExtra("caller", "SettingsActivity");
        intent.putExtra("is_contact", true);
        startActivity(intent);
    }

    /**
     * <p>redirects to DataUsage's activity</p>
     */
    @OnClick(R.id.tvDataUses)
    public void dataUses() {
        Intent intent = new Intent(SettingsActivity.this, DataUsage.class);
        startActivity(intent);
    }

    /**
     * <p>redirects to BlockedUser's activity</p>
     */
    @OnClick(R.id.tvBlockedUsers)
    public void blockedUsers() {
        Intent intent = new Intent(SettingsActivity.this, BlockUserActivity.class);
        startActivity(intent);
    }

    /**
     * <p>redirects to WebActivity's activity and opens privacy policy link</p>
     */
    @OnClick(R.id.tvPrivacy)
    public void privacyPolicy() {
        Intent intent = new Intent(SettingsActivity.this, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", getResources().getString(R.string.privacyPolicyUrl));
        bundle.putString("title", getResources().getString(R.string.privacyPolicy));
        intent.putExtra("url_data", bundle);
        intent.putExtra("clear", true);
        startActivity(intent);
    }

    /**
     * <p>redirects to WebActivity's activity and opens terms of service link</p>
     */
    @OnClick(R.id.tvTermsOfService)
    public void terms() {
        Intent intent = new Intent(SettingsActivity.this, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", getResources().getString(R.string.termsUrl));
        bundle.putString("title", getResources().getString(R.string.termsOfServiceTitle));
        intent.putExtra("url_data", bundle);
        intent.putExtra("clear", false);
        startActivity(intent);
    }

    /**
     * <p>redirects to WebActivity's activity and opens about us link</p>
     */
    @OnClick(R.id.tvAbout)
    public void about() {
        Intent intent = new Intent(SettingsActivity.this, WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", getResources().getString(R.string.aboutUsUrl));
        bundle.putString("title", getResources().getString(R.string.about));
        intent.putExtra("url_data", bundle);
        startActivity(intent);
    }

    @OnClick(R.id.ivBack)
    public void ivBack() {
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) unbinder.unbind();
    }

    @Override
    public void reload() {

    }

    @Override
    public void onSuccessSubscriptionAdded() {
        onBackPressed();
    }

}
