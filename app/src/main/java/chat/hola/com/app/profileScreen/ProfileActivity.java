package chat.hola.com.app.profileScreen;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Utilities.BioCustomTextView;
import chat.hola.com.app.Utilities.CommonClass;
import chat.hola.com.app.Utilities.ConnectivityReceiver;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.cameraActivities.SandriosCamera;
import chat.hola.com.app.cameraActivities.configuration.CameraConfiguration;
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.WalletResponse;
import chat.hola.com.app.my_qr_code.MyQRCodeActivity;
import chat.hola.com.app.post.PostActivity;
import chat.hola.com.app.profileScreen.addChannel.AddChannelActivity;
import chat.hola.com.app.profileScreen.bottomProfileMenu.ProfileMenuFrag;
import chat.hola.com.app.profileScreen.channel.ChannelFragment;
import chat.hola.com.app.profileScreen.collection.CollectionFragment;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.profileScreen.editProfile.EditProfileActivity;
import chat.hola.com.app.profileScreen.followers.FollowersActivity;
import chat.hola.com.app.profileScreen.liked.LikedPostFragment;
import chat.hola.com.app.profileScreen.live_stream.LiveStreamHistoryFragment;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.profileScreen.model.Profile;
import chat.hola.com.app.profileScreen.paid.PaidFragment;
import chat.hola.com.app.profileScreen.profile_story.ProfileStoryFrag;
import chat.hola.com.app.profileScreen.purchase.PurchaseFragment;
import chat.hola.com.app.profileScreen.star_video.PlayVideoActivity;
import chat.hola.com.app.profileScreen.story.StoryFragment;
import chat.hola.com.app.profileScreen.tag.TagFragment;
import chat.hola.com.app.settings.SettingsActivity;
import chat.hola.com.app.ui.dashboard.WalletDashboardActivity;
import chat.hola.com.app.ui.kyc.KycActivity;
import chat.hola.com.app.ui.validate.ValidateActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.otto.Bus;

import chat.hola.com.app.wallet.wallet_detail.WalletActivity;
import dagger.android.support.DaggerAppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import org.json.JSONObject;

import static chat.hola.com.app.Utilities.Utilities.findMatch;

public class ProfileActivity extends DaggerAppCompatActivity
        implements DialogInterface.OnClickListener, ProfileContract.View,
        ConnectivityReceiver.ConnectivityReceiverListener, SwipeRefreshLayout.OnRefreshListener,
        SandriosCamera.CameraCallback {

    private static final String TAG = ProfileActivity.class.getSimpleName();
    private final int CAMERA_REQUEST = 222;
    private final int READ_STORAGE_REQ_CODE = 26;
    private final int RESULT_LOAD_IMAGE = 1;
    private final int RESULT_LOAD_DATA = 143;
    public static boolean isPrivate = false;
    public static int followStatus = 0;
    @Inject
    ProfilePresenter presenter;
    @Inject
    ProfileMenuFrag profileMenuFrag;
    @Inject
    ChannelFragment channelFragment;
    @Inject
    ProfileStoryFrag profileStoryFrag;
    @Inject
    TagFragment tagFragment;
    @Inject
    LiveStreamHistoryFragment streamHistoryFragment;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    BlockDialog dialog1;
    @Inject
    StoryFragment postFragment;
    @Inject
    LikedPostFragment likedPostFragment;
    @Inject
    CollectionFragment collectionFragment;
    @Inject
    PurchaseFragment purchaseFragment;
    @Inject
    PaidFragment paidFragment;

    @BindView(R.id.ivEditProfile)
    ImageView ibEditProfile;
    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.ivProfileBg)
    ImageView ivProfileBg;
    @BindView(R.id.tvProfileName)
    TextView tvProfileName;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.tvProfileMob)
    TextView tvProfileMob;
    @BindView(R.id.tvProfileStatus)
    BioCustomTextView tvProfileStatus;
    @BindView(R.id.tvPostCount)
    TextView tvPostCount;
    @BindView(R.id.tvFollowingCount)
    TextView tvFollowingCount;
    @BindView(R.id.tvFollowersCount)
    TextView tvFollowersCount;
    @BindView(R.id.linearSubCount)
    LinearLayout linearSubCount;
    @BindView(R.id.tvSubscriberCount)
    TextView tvSubscriberCount;
    @BindView(R.id.tvPostTitle)
    TextView tvPostTitle;
    @BindView(R.id.tvFollowingTitle)
    TextView tvFollowingTitle;
    @BindView(R.id.tvFollowersTitle)
    TextView tvFollowersTitle;
    @BindView(R.id.tvSubscriberTitle)
    TextView tvSubscriberTitle;
    @BindView(R.id.tabLayoutProfile)
    TabLayout tabLayoutProfile;
    @BindView(R.id.viewPagerProfile)
    ViewPager viewPagerProfile;
    @BindView(R.id.faButton)
    FloatingActionButton faButton;
    @BindView(R.id.layoutAppBar)
    AppBarLayout appBarLayout;
    @BindView(R.id.btnFollow)
    public ToggleButton btnFollow;
    //    @BindView(R.id.collapseToolbarLayout)
//    CollapsingToolbarLayout collapseToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.profileRoot)
    CoordinatorLayout root;
    @BindView(R.id.llwallet)
    LinearLayout llwallet;
    @BindView(R.id.iV_wallet)
    ImageView iV_wallet;
    @BindView(R.id.iV_scan)
    ImageView iV_scan;
    @BindView(R.id.tv_earn)
    TextView tv_earn;
    @BindView(R.id.rl_balance)
    RelativeLayout rl_balance;
    @BindView(R.id.tV_balance)
    TextView tV_balance;
    @BindView(R.id.iV_star)
    ImageView iV_star;
    @BindView(R.id.rL_star_connect)
    RelativeLayout rL_star_connect;
    @BindView(R.id.iV_email)
    ImageView iV_email;
    @BindView(R.id.iV_chat)
    ImageView iV_chat;
    @BindView(R.id.iV_phone)
    ImageView iV_phone;
    @BindView(R.id.cV_starVideo)
    CardView cV_starVideo;
    @BindView(R.id.iV_starVideo)
    ImageView iV_starVideo;
    @BindView(R.id.tV_starDesc)
    TextView tV_starDesc;

    @BindView(R.id.tvPrivateTitle)
    TextView tvPrivateTitle;
    @BindView(R.id.tvPrivateMessage)
    TextView tvPrivateMessage;
    @BindView(R.id.llPrivate)
    LinearLayout llPrivate;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tVKnownAs)
    TextView tVKnownAs;
    @BindView(R.id.tvCoinBalance)
    TextView tvCoinBalance;

    @BindView(R.id.ibBusinessCall)
    Button ibBusinessCall;
    @BindView(R.id.ibBusinessChat)
    Button ibBusinessChat;
    @BindView(R.id.ibBusinessEmail)
    Button ibBusinessEmail;
    @BindView(R.id.ibBusinessLocation)
    Button ibBusinessLocation;
    @BindView(R.id.llBusinessProfileContact)
    LinearLayout llBusinessProfileContact;
    @BindView(R.id.tvBusinessType)
    TextView tvBusinessType;
    @BindView(R.id.tvwebsite)
    TextView tvwebsite;
    @BindView(R.id.iVshareProfile)
    ImageView iVshareProfile;
    @BindView(R.id.btnMessage)
    Button btnMessage;
    @BindView(R.id.btnSubscribe)
    ToggleButton btnSubscribe;


    private TextView tvTitle;

    AlertDialog.Builder reportDialog;
    AlertDialog.Builder blockDialog;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> blockReasons;

    private Unbinder unbinder;
    private ProgressDialog dialog;
    public String userId;
    public String userName = "";
    private Data profileData;
    private Data.BusinessProfile businessProfile;
    private Menu menu;
    private ActionBar actionBar;
    private Drawable backDrawableBlack;
    private Drawable backDrawableWhite;
    private Drawable shareProfileBlack;
    private Drawable shareProfileWhite;
    private String myid;
    boolean isBlocked = false;
    private MenuItem block, unfriend;
    private Drawable menudots;
    private Drawable menudotsWhite;
    private Loader loader;
    private Bus bus = AppController.getBus();
    private boolean isFriend = false;
    private boolean isBusiness = false;
    private boolean isVpSetUp = false;
    private boolean isSelf = false;
    private final HashMap<String, Boolean> mPostTitleToggledPositions = new HashMap();
    private MediaPlayer mediaPlayer;
    public int selectedTab = 0;

    @Override
    public void userBlocked() {
        dialog1.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_new);
        ButterKnife.bind(this);
        mediaPlayer = MediaPlayer.create(this, R.raw.coin_spend);
        //bus.register(this);
        loader = new Loader(this);
        tvTitle = toolbar.findViewById(R.id.tvTitle);
        iVshareProfile = findViewById(R.id.iVshareProfile);
        backDrawableBlack = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        backDrawableWhite = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        shareProfileBlack = getResources().getDrawable(R.drawable.ic_share_black);
        shareProfileWhite = getResources().getDrawable(R.drawable.ic_share);
        menudots = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_option_menu);
        menudotsWhite = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_menu_icon);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            this.actionBar = actionBar;
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(backDrawableWhite);
            if (toolbar != null) {
                toolbar.setOverflowIcon(menudotsWhite);
            }
        }

        //collapseToolbarLayout.setTitle("Profile");
//        collapseToolbarLayout.setCollapsedTitleTypeface(typefaceManager.getSemiboldFont());
//        collapseToolbarLayout.setExpandedTitleTypeface(typefaceManager.getSemiboldFont());
//        collapseToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);

        myid = AppController.getInstance().getUserId();
        userId = getIntent().getStringExtra("userId");

        tvCoinBalance.setText(sessionManager.getCoinBalance());

        //preferences = getSharedPreferences("userProfile", MODE_PRIVATE);
        presenter.init();
        reportDialog = new AlertDialog.Builder(this);
        reportDialog.setTitle(R.string.report);
        presenter.getReportReasons();

        blockDialog = new AlertDialog.Builder(this);
        blockDialog.setTitle(R.string.Block);
        presenter.getBlockReasons();

        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.please_wait));
        dialog.setCancelable(true);

        loadData();

        btnFollow.setChecked(false); //default
        btnFollow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (btnFollow.isChecked()) {
                    presenter.follow(userId);
                } else {
                    presenter.unfollow(userId);
                }
            }
        });

        /*
        * Bug Title: on un-subscribing it should show an alert
        * Bug Id: #2738
        * Fix Desc: alert dialog
        * Fix Dev: Hardik
        * Fix Date: 23/6/21
        * */

        btnSubscribe.setOnClickListener(view -> {
            btnSubscribe.setChecked(!btnSubscribe.isChecked());
            if(btnSubscribe.isChecked()){
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                String msg;
                if(profileData.isSubscriptionCancelled()){
                    msg = "You have already cancelled the subscription. Your current billing cycle ends on "
                            + Utilities.getDateDDMMYYYY(profileData.getSubscribeEndDate())
                            + ", so your subscription to "
                            + profileData.getStarRequest().getStarUserKnownBy()
                            + " will remain active till then and it will stop there after";
                    builder.setMessage(msg)
                            .setNegativeButton(R.string.ok, (dialog, id) -> {
                                // User cancelled the dialog
                                dialog.dismiss();
                            });
                }else {
                    msg = "Your current billing cycle ends on "
                            + Utilities.getDateDDMMYYYY(profileData.getSubscribeEndDate())
                            + ", so your subscription to "
                            + profileData.getStarRequest().getStarUserKnownBy()
                            + " will remain active till then and it will stop there after";
                    builder.setMessage(msg)
                            .setPositiveButton(R.string.continueText, (dialog, id) -> {
                                // FIRE ZE MISSILES!
                                openSubscribeConfirmation(!btnSubscribe.isChecked());
                            })
                            .setNegativeButton(R.string.cancel, (dialog, id) -> {
                                // User cancelled the dialog
                                dialog.dismiss();
                            });
                }

                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
            }else {
                openSubscribeConfirmation(!btnSubscribe.isChecked());
            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                changeColor(verticalOffset > -600);
            }
        });
        viewPagerProfile.setOffscreenPageLimit(5);
        viewPagerProfile.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedTab = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //load profile data
        isBusiness = getIntent().getBooleanExtra("isBusiness", false);
        ibEditProfile.setEnabled(false);
    }

    /**
     * Set Business profile details
     *
     * @param isChecked : true= business profile, false= non business profile
     */
    private void switchToBusiness(boolean isChecked) {

        Data.BusinessProfile businessProfile = profileData.getBusinessProfiles().get(0);

        boolean isMine = userId.equals(AppController.getInstance().getUserId());

        String fname = profileData.getFirstName();
        String lname = profileData.getLastName();
        String name = fname + " " + lname;

        btnMessage.setVisibility(View.GONE);

        tvBusinessType.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        tvBusinessType.setText(profileData.getBusinessProfiles().get(0).getBusinessCategory());
        tvwebsite.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        tvwebsite.setText(businessProfile.getWebsite());
        tvwebsite.setAutoLinkMask(Linkify.WEB_URLS);
        Linkify.addLinks(tvwebsite, Linkify.WEB_URLS);
        llBusinessProfileContact.setVisibility(!isMine && isChecked ? View.VISIBLE : View.GONE);
        //tvProfileMob.setVisibility(isMine && isChecked ? View.VISIBLE : View.GONE);
        //tvProfileMob.setText(isChecked ? businessProfile.getAddress() : profileData.getNumber());
        tvUserName.setText(isChecked ? businessProfile.getBusinessName() : name);
        tvProfileName.setText("@" + businessProfile.getBusinessUserName());

        ibBusinessCall.setVisibility(
                isChecked && businessProfile.getPhone().getVisible() == 1 ? View.VISIBLE : View.GONE);
//        ibBusinessEmail.setVisibility(
//                isChecked && businessProfile.getEmail().getVisible() == 1 ? View.VISIBLE : View.GONE);

        String profileStatus = profileData.getStatus().replace("dub.ly",getString(R.string.app_name)).replace("Dub.ly",getString(R.string.app_name));
        if (profileStatus == null || profileStatus.isEmpty()) {
            profileStatus = "";
        }
        String status = isChecked ? businessProfile.getBusinessBio() : profileStatus;
//        if (!status.toLowerCase().contains(getString(R.string.app_name).toLowerCase()))
//            tvProfileStatus.setAutoLinkMask(Linkify.WEB_URLS);
//        tvProfileStatus.setText(status);
        SpannableString spanString = new SpannableString(status);
        Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(spanString);
        findMatch(this, spanString, matcher);
        Matcher userMatcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
        findMatch(this, spanString, userMatcher);
        tvProfileStatus.setText(spanString, mPostTitleToggledPositions, userId, typefaceManager);

        if (businessProfile.getBusinessProfilePic() != null && !businessProfile.getBusinessProfilePic().isEmpty()) {
            Glide.with(getBaseContext())
                    .load(businessProfile.getBusinessProfilePic())
                    .asBitmap()
                    .centerCrop()
                    .placeholder(R.drawable.profile_one)
                    .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    .into(new BitmapImageViewTarget(ivProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivProfile.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } else {
            /*
             * Bug Title: business profile also should show initials
             * Bug Id: DUBAND156
             * Fix Desc: set text drawable
             * Fix Dev: Hardik
             * Fix Date: 13/5/21
             * */
            Utilities.setTextRoundDrawable(getBaseContext(), businessProfile.getBusinessName(),"", ivProfile);
        }
        Glide.with(getBaseContext())
                .load(businessProfile.getBusinessProfileCoverImage())
                .asBitmap()
                .centerCrop()
                .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                .placeholder(R.drawable.default_cover_photo)
                .into(ivProfileBg);

        String pic = isChecked ? businessProfile.getBusinessProfilePic() : profileData.getProfilePic();
        if (isMine) sessionManager.setUserProfilePic(pic, false);
    }

    @OnClick(R.id.cV_starVideo)
    public void openVideo() {
        Intent intent = new Intent(ProfileActivity.this, PlayVideoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ivCloseAd)
    public void closeAd() {
        cV_starVideo.setVisibility(View.GONE);
    }

    @OnClick(R.id.iV_wallet)
    public void walletClick() {
                Intent intent = new Intent(ProfileActivity.this, WalletActivity.class);
                startActivity(intent);
    }

    @OnClick(R.id.ibBusinessLocation)
    public void location() {

        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="
                    + businessProfile.getBusinessLat()
                    + ","
                    + businessProfile.getBusinessLng()
                    + " ( )"));
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, getString(R.string.activity_not_found), Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.iV_scan)
    public void openMyQRCode() {
        if (profileData != null) {
            Intent intent = new Intent(ProfileActivity.this, MyQRCodeActivity.class);
            intent.putExtra("profileData", profileData);
            startActivity(intent);
        }
    }

    @OnClick(R.id.btnMessage)
    public void sendMessage() {
        if (userId != null && !userId.equals(myid)) {
            openChatForStar(profileData, 1);
        }
    }

    @OnClick({R.id.iV_email, R.id.ibBusinessEmail})
    public void showEmail() {

        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
            emailIntent.setData(Uri.parse(
                    "mailto:" + (profileData.isActiveBussinessProfile() ? businessProfile.getEmail().getId()
                            : profileData.getVerified().getEmailId())));

            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            //TODO: Handle case where no email app is available
        } catch (NullPointerException e) {

        }

        //openContactDialog(1);
    }

    @OnClick({R.id.iV_phone, R.id.ibBusinessCall})
    public void showPhoneNumber() {

        if (profileData.getVerified() != null) {

            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse(
                    "tel:" + (profileData.isActiveBussinessProfile() ? businessProfile.getPhone()
                            .getCountryCode() + "" + businessProfile.getPhone().getNumber()
                            : profileData.getVerified().getNumber())));
            startActivity(intent);
        }

        //openContactDialog(2);
    }

    @OnClick({R.id.iV_chat, R.id.ibBusinessChat})
    public void openChat() {
        if (userId != null && !userId.equals(myid)) {
            openChatForStar(profileData, 1);
        }
    }

    public void openChatForStar(Data profileData, int position) {

        String fullName =
                CommonClass.createFullName(profileData.getFirstName(), profileData.getLastName());

        Intent intent = new Intent(ProfileActivity.this, ChatMessageScreen.class);
        intent.putExtra("receiverUid", profileData.getId());
        intent.putExtra("receiverName", fullName);
        intent.putExtra("isStar", profileData.isStar());

        String docId = AppController.getInstance().findDocumentIdOfReceiver(profileData.getId(), "");

        if (docId.isEmpty()) {
            docId =
                    AppController.findDocumentIdOfReceiver(profileData.getId(), Utilities.tsInGmt(), fullName,
                            profileData.getProfilePic(), "", false, profileData.getNumber(), "", false, true);
        }

        intent.putExtra("documentId", docId);
        intent.putExtra("receiverIdentifier", profileData.getNumber());
        intent.putExtra("receiverImage", profileData.getProfilePic());
        intent.putExtra("colorCode", AppController.getInstance().getColorCode(position % 19));
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        startActivity(intent);
    }

    @OnClick(R.id.rl_balance)
    public void openTransaction() {
        startActivity(new Intent(this, WalletDashboardActivity.class));
    }

    private void loadData() {
        //load data
        if (userId != null && !userId.equals(myid)) {
            ibEditProfile.setVisibility(View.GONE);
            presenter.loadMemberData(userId);
        } else {
            ibEditProfile.setVisibility(View.VISIBLE);
            presenter.loadProfileData();
            presenter.getWalletBalance();
        }
    }

    //@Subscribe
    //public void getMessage(JSONObject object) {
    //    try {
    //        if (object.getString("eventName").equals("profileUpdated")) {
    //            loadData();
    //        }
    //    } catch (JSONException e) {
    //        e.printStackTrace();
    //    }
    //}

    private void changeColor(boolean white) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (white) {
                    actionBar.setHomeAsUpIndicator(backDrawableWhite);
                    iVshareProfile.setImageDrawable(shareProfileWhite);
                    if (toolbar != null) toolbar.setOverflowIcon(menudotsWhite);
                    tvTitle.setVisibility(View.GONE);
                } else {
                    actionBar.setHomeAsUpIndicator(backDrawableBlack);
                    iVshareProfile.setImageDrawable(shareProfileBlack);
                    if (toolbar != null) toolbar.setOverflowIcon(menudots);
                    tvTitle.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuItem setting = menu.findItem(R.id.actionSettings);
        MenuItem report = menu.findItem(R.id.actionReport);

        if (AppController.getInstance().getUserId() != null) {
            setting.setVisible(AppController.getInstance().getUserId().equals(userId));
            report.setVisible(!AppController.getInstance().getUserId().equals(userId));

            block = menu.findItem(R.id.actionBlock);
            block.setTitle(getString(isBlocked ? R.string.ubBlock : R.string.Block));
            //        block.setVisible(false);
            block.setVisible(!AppController.getInstance().getUserId().equals(userId));

            unfriend = menu.findItem(R.id.actionUnFriend);
            //        unfriend.setVisible(false);
            unfriend.setVisible(!AppController.getInstance().getUserId().equals(userId) && isFriend);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public static void setOverflowButtonColor(final Toolbar toolbar, final int color) {
        Drawable drawable = toolbar.getOverflowIcon();
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), color);
            toolbar.setOverflowIcon(drawable);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionUnFriend:
                presenter.unfriend(userId);
            case android.R.id.home:
                back();
                return true;
            //            case R.id.actionDiscoverPpl:
            //                discover();
            //                return true;
            case R.id.actionSettings:
                settings();
                return true;
            case R.id.actionReport:
                reportDialog.show();
                return true;
            case R.id.actionBlock:
                if (isBlocked) {
                    AlertDialog.Builder confirm = new AlertDialog.Builder(this);
                    confirm.setMessage("Are you sure you want to " + " unblock " + userName + "?");
                    confirm.setPositiveButton(R.string.confirm,
                            (dialog, w) -> presenter.block(userId, "unblock", "unblock"));
                    confirm.setNegativeButton(R.string.cancel, (dialog, w) -> dialog.dismiss());
                    confirm.create().show();
                } else {
                    AlertDialog.Builder confirm = new AlertDialog.Builder(this);
                    confirm.setMessage("Are you sure you want to " + " block " + userName + "?");
                    confirm.setPositiveButton(R.string.confirm,
                            (dialog, w) -> presenter.block(userId, "block", "block"));
                    confirm.setNegativeButton(R.string.cancel, (dialog, w) -> dialog.dismiss());
                    confirm.create().show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void unblockDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unblock_user_msg);
        builder.setPositiveButton(R.string.unblock, (dialog, which) -> {
            presenter.block(userId, "unblock", "");
        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            dialog.dismiss();
        });
        builder.create();
        builder.show();
    }

    public void back() {
        super.onBackPressed();
    }

    public void discover() {
        startActivity(new Intent(this, DiscoverActivity.class).putExtra("caller", "ProfileActivity"));
    }

    @OnClick(R.id.tvPostCount)
    public void post() {
        //        viewPagerProfile.setCurrentItem(0);
        appBarLayout.setExpanded(false, true);
    }

    @OnClick({R.id.tvFollowersCount, R.id.tvFollowersTitle})
    public void followers() {
        try {
            if (Integer.parseInt(tvFollowersCount.getText().toString()) != 0) {
                Intent intent = new Intent(ProfileActivity.this, FollowersActivity.class);
                intent.putExtra("title", getResources().getString(R.string.followers));
                intent.putExtra("following", profileData.getFollowers());
                intent.putExtra("userId", profileData.getId());
                startActivity(intent);
            }
        } catch (Exception ignored) {
        }
    }

    @OnClick({R.id.tvFollowingCount, R.id.tvFollowingTitle})
    public void following() {
        try {
            String followingCount = tvFollowingCount.getText().toString();
            if (!followingCount.isEmpty()) {
                if (Integer.parseInt(followingCount) != 0) {
                    Intent intent = new Intent(ProfileActivity.this, FollowersActivity.class);
                    intent.putExtra("title", getResources().getString(R.string.following));
                    intent.putExtra("following", profileData.getFollowing());
                    intent.putExtra("userId", profileData.getId());
                    startActivity(intent);
                }
            }
        } catch (Exception ignored) {
        }
    }

    @OnClick({R.id.tvSubscriberCount, R.id.tvSubscriberTitle})
    public void subscribes() {
        try {
            if(isSelf) {
                String followingCount = tvFollowingCount.getText().toString();
                if (!followingCount.isEmpty()) {
                    if (Integer.parseInt(followingCount) != 0) {
                        Intent intent = new Intent(this, FollowersActivity.class);
                        intent.putExtra("title", "Subscribers");
                        intent.putExtra("following", profileData.getSubscribeUserCount());
                        intent.putExtra("userId", profileData.getId());
                        startActivity(intent);
                    }
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    public void settings() {
        Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
        intent.putExtra("profileData", profileData);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if (requestCode == 0) {
        //    if (resultCode == RESULT_OK) {
        //
        //        boolean imageUpdated = data.getBooleanExtra("imageUpdated", false);
        //
        //        if (imageUpdated) {
        //            AppController.getInstance().getSessionManager().setUserProfilePicUpdateTime();
        //  try {
        //    new ClearGlideCacheAsyncTask().execute();
        //  } catch (Exception e) {
        //    e.printStackTrace();
        //  }
        //}
        //presenter.loadProfileData();
        //}
        //} else
        if (requestCode == RESULT_LOAD_DATA) {
            loadData();
        } else if (requestCode == RESULT_LOAD_IMAGE) {
            presenter.parseMedia(requestCode, resultCode, data);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            presenter.parseCropedImage(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewPagerProfile.setCurrentItem(selectedTab);
    }

    @Override
    public void applyFont() {
        tvProfileName.setTypeface(typefaceManager.getSemiboldFont());
        tvUserName.setTypeface(typefaceManager.getMediumFont());
        tvProfileMob.setTypeface(typefaceManager.getRegularFont());
        //tvProfileStatus.setTypeface(typefaceManager.getRegularFont());
        tvPostCount.setTypeface(typefaceManager.getMediumFont());
        tvFollowingCount.setTypeface(typefaceManager.getMediumFont());
        tvFollowersCount.setTypeface(typefaceManager.getMediumFont());
        tvSubscriberCount.setTypeface(typefaceManager.getMediumFont());
        tvPostTitle.setTypeface(typefaceManager.getMediumFont());
        tvFollowingTitle.setTypeface(typefaceManager.getMediumFont());
        tvFollowersTitle.setTypeface(typefaceManager.getMediumFont());
        tvSubscriberTitle.setTypeface(typefaceManager.getMediumFont());
        btnFollow.setTypeface(typefaceManager.getMediumFont());
        tvBusinessType.setTypeface(typefaceManager.getMediumFont());
    }

    @Override
    public void setupViewPager() {
        tabLayoutProfile.setupWithViewPager(viewPagerProfile);
        ProfilePageAdapter fragmentPageAdapter = new ProfilePageAdapter(getSupportFragmentManager());

        if (isSelf) {
            try {
                fragmentPageAdapter.addFragment(postFragment, "", "grid", userId, userName);
                fragmentPageAdapter.addFragment(likedPostFragment, "", "nogrid", userId, userName);
                fragmentPageAdapter.addFragment(tagFragment, "", "", userId, userName);
                //fragmentPageAdapter.addFragment(profileStoryFrag, "", "", userId, userName);
                fragmentPageAdapter.addFragment(purchaseFragment, "", "", userId, userName);
                fragmentPageAdapter.addFragment(collectionFragment, "", "", userId, userName);
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
            viewPagerProfile.setAdapter(fragmentPageAdapter);

            Drawable drawableOne = getResources().getDrawable(R.drawable.ic_home_profile_selected);
            Drawable drawableTwo = getResources().getDrawable(R.drawable.ic_like_profile_selected);
            Drawable drawableThree = getResources().getDrawable(R.drawable.ic_tag_profile_selected);
            //Drawable drawableFour = getResources().getDrawable(R.drawable.ic_live_profile_selected);
            Drawable drawableFive = getResources().getDrawable(R.drawable.ic_unlock_selected);
            Drawable drawableSix = getResources().getDrawable(R.drawable.ic_saved_profile_selected);

            final Drawable[] tabDrawableOn = {
                    drawableOne, drawableTwo, drawableThree, drawableFive, drawableSix
            };

            Drawable drawableOneOff = getResources().getDrawable(R.drawable.ic_home_profile);
            Drawable drawableTwoOff = getResources().getDrawable(R.drawable.ic_like_profile);
            Drawable drawableThreeOff = getResources().getDrawable(R.drawable.ic_tagged_profile);
            //Drawable drawableFourOff = getResources().getDrawable(R.drawable.ic_live_profile);
            Drawable drawableFiveOff = getResources().getDrawable(R.drawable.ic_unlock);
            Drawable drawableSixOff = getResources().getDrawable(R.drawable.ic_saved_profile);

            final Drawable[] tabDrawableOff = {
                    drawableOneOff, drawableTwoOff, drawableThreeOff, drawableFiveOff, drawableSixOff
            };

            tabLayoutProfile.getTabAt(0).setIcon(tabDrawableOn[0]);
            tabLayoutProfile.getTabAt(1).setIcon(tabDrawableOff[1]);
            tabLayoutProfile.getTabAt(2).setIcon(tabDrawableOff[2]);
            tabLayoutProfile.getTabAt(3).setIcon(tabDrawableOff[3]);
            tabLayoutProfile.getTabAt(4).setIcon(tabDrawableOff[4]);
            //tabLayoutProfile.getTabAt(5).setIcon(tabDrawableOff[5]);

            tabLayoutProfile.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    tab.setIcon(tabDrawableOn[tab.getPosition()]);
                    selectedTab = tab.getPosition();
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    tab.setIcon(tabDrawableOff[tab.getPosition()]);
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });


        } else {

            /* if business profile then not need to show paid post fragment*/
            if (isBusiness) {
                /*
                 * Bug Title: Call: Profile Page: for business profile there won’t be paid posts tab as business profile cannot create a paid post
                 * Bug Id: DUBAND134
                 * Fix desc: add this if block
                 * Fix dev: hardik
                 * Fix date: 11/5/21
                 * */
                try {
                    fragmentPageAdapter.addFragment(postFragment, "", "grid", userId, userName);
                    fragmentPageAdapter.addFragment(likedPostFragment, "", "nogrid", userId, userName);
                    fragmentPageAdapter.addFragment(tagFragment, "", "", userId, userName);
                    //fragmentPageAdapter.addFragment(profileStoryFrag, "", "", userId, userName);
                    //fragmentPageAdapter.addFragment(channelFragment, "", "", userId, userName);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                viewPagerProfile.setAdapter(fragmentPageAdapter);
                Drawable drawableOne = getResources().getDrawable(R.drawable.ic_home_profile_selected);
                Drawable drawableTwo = getResources().getDrawable(R.drawable.ic_like_profile_selected);
                Drawable drawableThree = getResources().getDrawable(R.drawable.ic_tag_profile_selected);
                //Drawable drawableFive = getResources().getDrawable(R.drawable.ic_live_profile_selected);
                //Drawable drawableSix = getResources().getDrawable(R.drawable.ic_channel_black);

                final Drawable[] tabDrawableOn =
                        {drawableOne, drawableTwo, drawableThree};

                Drawable drawableOneOff = getResources().getDrawable(R.drawable.ic_home_profile);
                Drawable drawableTwoOff = getResources().getDrawable(R.drawable.ic_like_profile);
                Drawable drawableThreeOff = getResources().getDrawable(R.drawable.ic_tagged_profile);
                //Drawable drawableFiveOff = getResources().getDrawable(R.drawable.ic_live_profile);

                final Drawable[] tabDrawableOff = {
                        drawableOneOff, drawableTwoOff, drawableThreeOff,
                };

                tabLayoutProfile.getTabAt(0).setIcon(tabDrawableOn[0]);
                tabLayoutProfile.getTabAt(1).setIcon(tabDrawableOff[1]);
                tabLayoutProfile.getTabAt(2).setIcon(tabDrawableOff[2]);
                //tabLayoutProfile.getTabAt(3).setIcon(tabDrawableOff[3]);

                tabLayoutProfile.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        tab.setIcon(tabDrawableOn[tab.getPosition()]);
                        selectedTab = tab.getPosition();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        tab.setIcon(tabDrawableOff[tab.getPosition()]);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            } else {
                try {
                    fragmentPageAdapter.addFragment(postFragment, "", "grid", userId, userName);
                    fragmentPageAdapter.addFragment(likedPostFragment, "", "nogrid", userId, userName);
                    fragmentPageAdapter.addFragment(tagFragment, "", "", userId, userName);
                    fragmentPageAdapter.addFragment(paidFragment, "", "", userId, userName);
                    //fragmentPageAdapter.addFragment(profileStoryFrag, "", "", userId, userName);
                    //fragmentPageAdapter.addFragment(channelFragment, "", "", userId, userName);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
                viewPagerProfile.setAdapter(fragmentPageAdapter);
                Drawable drawableOne = getResources().getDrawable(R.drawable.ic_home_profile_selected);
                Drawable drawableTwo = getResources().getDrawable(R.drawable.ic_like_profile_selected);
                Drawable drawableThree = getResources().getDrawable(R.drawable.ic_tag_profile_selected);
                Drawable drawableFour = getResources().getDrawable(R.drawable.ic_lock_icon_selected);
                //Drawable drawableFive = getResources().getDrawable(R.drawable.ic_live_profile_selected);
                //Drawable drawableSix = getResources().getDrawable(R.drawable.ic_channel_black);

                final Drawable[] tabDrawableOn = {
                        drawableOne, drawableTwo, drawableThree, drawableFour
                };

                Drawable drawableOneOff = getResources().getDrawable(R.drawable.ic_home_profile);
                Drawable drawableTwoOff = getResources().getDrawable(R.drawable.ic_like_profile);
                Drawable drawableThreeOff = getResources().getDrawable(R.drawable.ic_tagged_profile);
                Drawable drawableFourOff = getResources().getDrawable(R.drawable.ic_lock_icon);
                //Drawable drawableFiveOff = getResources().getDrawable(R.drawable.ic_live_profile);
                //Drawable drawableSixOff = getResources().getDrawable(R.drawable.ic_channel_gray);

                final Drawable[] tabDrawableOff = {
                        drawableOneOff, drawableTwoOff, drawableThreeOff, drawableFourOff
                };

                tabLayoutProfile.getTabAt(0).setIcon(tabDrawableOn[0]);
                tabLayoutProfile.getTabAt(1).setIcon(tabDrawableOff[1]);
                tabLayoutProfile.getTabAt(2).setIcon(tabDrawableOff[2]);
                tabLayoutProfile.getTabAt(3).setIcon(tabDrawableOff[3]);
                //tabLayoutProfile.getTabAt(4).setIcon(tabDrawableOff[4]);

                tabLayoutProfile.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        tab.setIcon(tabDrawableOn[tab.getPosition()]);
                        selectedTab = tab.getPosition();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        tab.setIcon(tabDrawableOff[tab.getPosition()]);
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {

                    }
                });
            }
        }

        isVpSetUp = true;
    }

    @Override
    public void isLoading(boolean flag) {
    }

    @OnClick(R.id.faButton)
    public void fab() {
        startActivity(new Intent(this, AddChannelActivity.class));
    }

    @OnClick(R.id.ivEditProfile)
    public void editProfile() {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("profile_data", profileData);
        intent.putExtra("bundle", bundle);
        intent.putExtra("call", "ProfileActivity");
        intent.putExtra("userId", userId);
        startActivityForResult(intent, RESULT_LOAD_DATA);
    }

    @Override
    protected void onDestroy() {
        selectedTab = 0;
        if (unbinder != null) unbinder.unbind();
        //bus.unregister(this);
        super.onDestroy();
    }

    /*
     * Bug Title: From the following page if I open the profile page channel option is showing if I direct open profile page it is note showing
     * Bug Id: #2831
     * Fix Dev: Hardik
     * Fix Desc: remove invoke
     * Fix Date: 26/6/21
     * */

    public void setFabButtonVisible(boolean show) {
        //  btnFollow.setVisibility(show ? View.GONE : View.VISIBLE);
        faButton.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setActionBarExpand(boolean expand) {
        appBarLayout.setExpanded(expand, true);
    }

    @Override
    public void showMessage(String msg, int msgId) {
        Toast.makeText(this, msg != null && !msg.isEmpty() ? msg : getResources().getString(msgId), Toast.LENGTH_SHORT).show();
        if(msgId==204) {
            //user not found
            finish();
        }
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getApplicationContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {
        //   llNetworkError.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showProfileData(Profile profile) {
        try {
            userId = profile.getData().get(0).getId();
            Log.d("usedId3=", userId);
            showProfile(profile);
        } catch (Exception ignored) {
        }
    }

    private void showProfile(Profile profile) {
        try {
            if (!profile.getData().isEmpty()) {
                isSelf = profile.getData().get(0).getId().equals(myid);
                invalidateOptionsMenu();
                ibEditProfile.setEnabled(true);
                followStatus = profile.getData().get(0).getFollowStatus();
                isBlocked = profile.getData().get(0).getBlock() == 1; // 1=blocked
                this.block.setTitle(
                        getResources().getString(isBlocked ? R.string.Block : R.string.ubBlock));
                isPrivate = profile.getData().get(0).getPrivate().equals("1");
                isFriend = profile.getData().get(0).getFriendStatusCode() == 2;

                block.setVisible(!AppController.getInstance().getUserId().equals(userId));
                unfriend.setVisible(!AppController.getInstance().getUserId().equals(userId) && isFriend);

                this.profileData = profile.getData().get(0);
                isBusiness = profileData.getUserType() == 9;
                String fname = profileData.getFirstName();
                String lname = profileData.getLastName();

                if (profile.getData().get(0).getId().equals(myid)) {
                    sessionManager.setUserProfilePic(profile.getData().get(0).getProfilePic(), false);
                    sessionManager.setBusinessProfileAvailable(this.profileData.isActiveBussinessProfile());
                    sessionManager.setBusinessProfileApproved(profileData.isBusinessProfileApproved());
                }

                if (this.profileData.getBusinessProfiles() != null
                        && this.profileData.getBusinessProfiles().size() > 0) {
                    businessProfile = this.profileData.getBusinessProfiles().get(0);
                    sessionManager.setBusinessCategoryId(businessProfile.getBussinessId());
                    sessionManager.businessProfile(businessProfile != null);
                }

                if (profile.getData().get(0).getId().equals(myid)) {
                    sessionManager.setUserProfilePic(profile.getData().get(0).getProfilePic(), false);
                    sessionManager.setFirstName(fname);
                    sessionManager.setLastsName(lname);
                }

                userName = profileData.getUserName();
                if (!isVpSetUp) setupViewPager();
                //                1)type: { status: 0, message: "unfollowed" }
                //                2)type: { status: 1, message: ' started following' }
                //                3)type: { status: 2, message: 'requested' }
                //                4)type: { status: 3, message: 'reject' }
                boolean isChecked;
                if (btnFollow != null) {

                    switch (profileData.getFollowStatus()) {
                        case 0:
                            //public - unfollow
                            isPrivate = profileData.getPrivate().equals("1");
                            isChecked = false;

                            if (isPrivate && !userId.equals(myid)) {
                                llPrivate.setVisibility(View.VISIBLE);
                                viewPagerProfile.setVisibility(View.GONE);
                            } else {
                                llPrivate.setVisibility(View.GONE);
                                viewPagerProfile.setVisibility(View.VISIBLE);
                            }
                            break;
                        case 1:
                            //public - follow
                            isPrivate = false;
                            isChecked = true;
                            llPrivate.setVisibility(View.GONE);
                            viewPagerProfile.setVisibility(View.VISIBLE);
                            break;
                        case 2:
                            //private - requested
                            isPrivate = true;
                            isChecked = true;
                            llPrivate.setVisibility(View.VISIBLE);
                            viewPagerProfile.setVisibility(View.GONE);
                            break;
                        case 3:
                            //private - request
                            isPrivate = true;
                            isChecked = false;
                            llPrivate.setVisibility(View.VISIBLE);
                            viewPagerProfile.setVisibility(View.GONE);
                            break;
                        default:
                            isChecked = false;
                            break;
                    }
                    btnFollow.setTextOn(
                            getResources().getString(isPrivate ? R.string.requested : R.string.following));
                    btnFollow.setTextOff(getResources().getString(R.string.follow));
                    btnFollow.setChecked(isChecked);
                }

                Data data = profile.getData().get(0);

                if (data.getProfilePic() != null && !data.getProfilePic().isEmpty()) {
                    Glide.with(getBaseContext())
                            .load(data.getProfilePic())
                            .asBitmap()
                            .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop()
                            .placeholder(R.drawable.profile_one)
                            .into(new BitmapImageViewTarget(ivProfile) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    super.setResource(resource);
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    ivProfile.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } else {
                    Utilities.setTextRoundDrawable(getBaseContext(), data.getFirstName(),data.getLastName(), ivProfile);
                }

                Glide.with(getBaseContext())
                        .load(
                                data.getProfileCoverImage() != null && !data.getProfileCoverImage().isEmpty() ? data
                                        .getProfileCoverImage() : data.getProfilePic())
                        .asBitmap()
                        .signature(new StringSignature(
                                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        .centerCrop()
                        .placeholder(R.drawable.default_cover_photo)
                        .into(ivProfileBg);

                String profileStatus = data.getStatus().replace("dub.ly",getString(R.string.app_name)).replace("Dub.ly",getString(R.string.app_name));
                if (profileStatus == null || profileStatus.isEmpty()) {
                    profileStatus = "";
                }
//                if (!profileStatus.toLowerCase().contains(getString(R.string.app_name).toLowerCase()))
//                    tvProfileStatus.setAutoLinkMask(Linkify.WEB_URLS);
//                tvProfileStatus.setText(profileStatus);
                //                tvProfileStatus.setMovementMethod(LinkMovementMethod.getInstance());

                tvProfileStatus.setOnExpandStateChangeListener(mPostTitleToggledPositions::put);

                SpannableString spanString = new SpannableString(profileStatus);
                Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(spanString);
                findMatch(this, spanString, matcher);
                Matcher userMatcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
                findMatch(this, spanString, userMatcher);
                tvProfileStatus.setText(spanString, mPostTitleToggledPositions, data.getId(), typefaceManager);

                String name = (fname != null ? fname : "") + " " + (lname != null ? lname : "");
                //collapseToolbarLayout.setTitle(name);
                tvTitle.setText("@" + profileData.getUserName());
                tvProfileName.setText("@" + profileData.getUserName());
                //tvLocation.setVisibility(isSelf ? View.VISIBLE : View.GONE);
                tvLocation.setText(sessionManager.getCity() + ", " + sessionManager.getCountry());
//                sessionManager.setIsStar(data.isStar());
//                sessionManager.setUserName(profileData.getUserName());
                tvUserName.setText(name);
                tvProfileMob.setText(data.getNumber());
                tvProfileMob.setVisibility(View.GONE);
                tvPostCount.setText(String.valueOf(data.getPostsCount()));
                tvFollowingCount.setText(String.valueOf(data.getFollowing()));
                tvFollowersCount.setText(String.valueOf(data.getFollowers()));
                 /*
                 * Bug Title: for stars users that have subscribers, it should show the count of the number of subscribers on the stars profile
                        but don’t allow click to load a list of subscribers for now
                 * Bug Id: DUBAND139
                 * Fix Desc: add count textview
                 * Fix Dev: Hardik
                 * Fix Date: 7/5/21
                 * */
                tvSubscriberCount.setText(String.valueOf(data.getSubscribeUserCount()));

                btnMessage.setVisibility(!isSelf && !data.isStar() ? View.VISIBLE : View.GONE);

                if (data.isStar()) {
                    iV_star.setVisibility(View.VISIBLE);
                    //                    cV_starVideo.setVisibility(View.VISIBLE);
                    //                    iV_starVideo.setImageDrawable(getResources().getDrawable(R.drawable.trailer1));
                    //                    Glide.with(this)
                    //                            .load("https://res.cloudinary.com/dafszph29/image/upload/v1552033376/default/trailer1.png")
                    //                            .asBitmap()
                    //                            .into(iV_starVideo);

//                    if (!profileData.isActiveBussinessProfile() && userId != null && !userId.equals(myid)) {
//                        rL_star_connect.setVisibility(View.VISIBLE);
//                    } else {
//                        rL_star_connect.setVisibility(View.GONE);
//                    }

                    /*
                     * Bug Title: subscribe button not working and ui not proper
                     * Bug Id: DUBAND147
                     * Fix Desc: set text and end drawble
                     * Fix dev: hardik
                     * Fix date: 11/5/21
                     * */
                    if (!isSelf && profileData.getSubscriptionAmount() >= 0) {
                        btnSubscribe.setVisibility(View.VISIBLE);
                        String subTxt = getString(R.string.subscribe_new)
                                +
//                                " " + profileData.getUserName()

                                " " + getString(R.string.for_text)

                                + " " + profileData.getSubscriptionAmount();
                        //+ " " + getString(R.string.slash_month);
                        btnSubscribe.setTextOff(subTxt);
                        btnSubscribe.setTextOn(getString(R.string.subscribed));
                        btnSubscribe.setChecked(profileData.isSubscribe());
                    }
                    if(profileData.getWallet()!=null && profileData.getWallet().getTotalAmount()>0) {
                        tv_earn.setVisibility(View.VISIBLE);
                        tv_earn.setText(String.format("%s", Utilities.formatMoney(profileData.getWallet().getTotalAmount())));
                    }

                    if (data.getStarRequest() != null) {

                        if (data.isActiveBussinessProfile()) {
                            /*
                             * Bug Title:Business profile-> like the star and business profile merged
                             * Bug Id: #2830
                             * Fix Desc: hiding known as and star badge if business profile is active
                             * Fix Dev: Shaktisinh
                             * Fix Date: 28/6/21
                             * */
                            tVKnownAs.setVisibility(View.GONE);
                            iV_star.setVisibility(View.GONE);
                        } else {
                            tVKnownAs.setVisibility(View.VISIBLE);
                            iV_star.setVisibility(View.VISIBLE);
                        }

                        /*
                         * Bug Title: known as - spacing on top is too little , increase it and align this B) make known as in capital or bold maybe , need to check what looks good
                         * Bug Id: #2694
                         * Fix Desc: set separate textview for known as, spacing 5dp in xml
                         * Fix Dev: Hardik
                         * Fix Date: 22/6/21
                         * */
                        tvUserName.setText(data.getStarRequest().getStarUserKnownBy());
                        if (data.getStarRequest().isEmailVisible()) {
                            iV_email.setVisibility(View.VISIBLE);
                        } else {
                            iV_email.setVisibility(View.GONE);
                        }

                        if (data.getStarRequest().isChatVisible()) {
                            iV_chat.setVisibility(View.VISIBLE);
                            btnMessage.setVisibility(!isSelf ? View.VISIBLE : View.GONE);
                        } else {
                            iV_chat.setVisibility(View.GONE);
                        }

                        if (data.getStarRequest().isNumberVisible()) {
                            iV_phone.setVisibility(View.VISIBLE);
                        } else {
                            iV_phone.setVisibility(View.GONE);
                        }

                        if (profileData.isActiveBussinessProfile()
                                || !data.getStarRequest().isEmailVisible()
                                && !data.getStarRequest().isChatVisible()
                                && !data.getStarRequest().isNumberVisible()) {
                            rL_star_connect.setVisibility(View.GONE);
                        } /*else {
                            rL_star_connect.setVisibility(View.VISIBLE);
                        }*/

                        if (data.getStarRequest().getDescription() != null && !data.getStarRequest()
                                .getDescription()
                                .isEmpty()) {
                            tV_starDesc.setText(data.getStarRequest().getDescription());
                            tV_starDesc.setVisibility(View.VISIBLE);
                        }
                    }
                    linearSubCount.setVisibility(View.VISIBLE);
                } else {
                    linearSubCount.setVisibility(View.GONE);
                    iV_star.setVisibility(View.GONE);
                    cV_starVideo.setVisibility(View.GONE);
                }

                if (userId != null && !userId.equals(myid)) {
                    btnFollow.setVisibility(View.VISIBLE);
                    llwallet.setVisibility(View.GONE);
                } else {
                    btnFollow.setVisibility(View.GONE);
                    llwallet.setVisibility(View.VISIBLE);
                }
            }

            if (profile.getData().get(0).getId().equals(myid)) {
                isBusiness = sessionManager.isBusinessProfileAvailable();
            } else {
                isBusiness = profileData.getUserType() == 9;
            }

            if (isBusiness) switchToBusiness(isBusiness);
        } catch (Exception ignored) {

        }
    }

    @Override
    public void isFollowing(boolean flag) {
        if (btnFollow != null) {
            if (userId != null && !userId.equals(myid)) {
                presenter.loadMemberData(userId);
            } else {
                presenter.loadProfileData();
            }

            btnFollow.setEnabled(true);
            btnFollow.setChecked(flag);
        }
    }

    @Override
    public void launchCustomCamera() {
        SandriosCamera.with(this)
                .setShowPicker(true)
                .setVideoFileSize(Constants.Camera.FILE_SIZE)
                .setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
                .enableImageCropping(true)
                .launchCamera(this);
    }

    @Override
    public void checkReadImage() {
        if (ActivityCompat.checkSelfPermission(ProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ProfileActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            presenter.launchImagePicker();
        } else {
            requestReadImagePermission();
        }
    }

    private void requestReadImagePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(ProfileActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            Snackbar snackbar = Snackbar.make(root, R.string.string_222, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.string_580), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            }, READ_STORAGE_REQ_CODE);
                        }
                    });
            snackbar.show();
            View view = snackbar.getView();
            ((TextView) view.findViewById(R.id.snackbar_text)).setGravity(Gravity.CENTER_HORIZONTAL);
        } else {
            ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, READ_STORAGE_REQ_CODE);
        }
    }

    @Override
    public void launchImagePicker(Intent intent) {
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Toast.makeText(this, isConnected ? "Internet connected" : "No internet", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    @Override
    public void launchPostActivity(CameraOutputModel model) {
        onComplete(model);
    }

    @Override
    public void addToReportList(ArrayList<String> data) {
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        reportDialog.setAdapter(arrayAdapter, this);
    }

    @Override
    public void addToBlockList(ArrayList<String> data) {
        blockReasons = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        blockDialog.setAdapter(blockReasons, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.block(userId, "block", blockReasons.getItem(i));
            }
        });
    }

    @Override
    public void block(boolean block) {
        try {
            JSONObject obj = new JSONObject();
            obj.put("eventName", "reloadFriends");
            bus.post(obj);
        } catch (Exception ignored) {

        }
        isBlocked = block;
        String message = getResources().getString(block ? R.string.ubBlock : R.string.Block);
        this.block.setTitle(message);
        Toast.makeText(this, getResources().getString(!block ? R.string.ubBlock : R.string.Block),
                Toast.LENGTH_SHORT).show();

        if (AppController.getInstance().getSignedIn() && AppController.getInstance().profileSaved()) {
            Intent i2 = new Intent(ProfileActivity.this, LandingActivity.class);
            i2.putExtra("userId", AppController.getInstance().getUserId());
            i2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i2);
            finish();
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }

    @Override
    public void unfriend() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("eventName", "reloadFriends");
            bus.post(obj);
        } catch (Exception ignored) {

        }
        loadData();
    }

    @Override
    public void showBalance(WalletResponse.Data.Wallet data) {
        if (data != null && data.getBalance() != null) {

            String balance = sessionManager.getCurrencySymbol() + " " + Utilities.formatMoney(Double.valueOf(sessionManager.getWalletBalance()));
            if (tV_balance != null)
                tV_balance.setText(balance);
            rl_balance.setVisibility(View.VISIBLE);
            sessionManager.setWalletBalance(String.valueOf(data.getBalance()));
//            sessionManager.setCurrencySymbol(data.getCurrencySymbol());
//            sessionManager.setCurrency(data.getCurrency());
        }
    }

    @Override
    public void showCoinBalance(WalletResponse.Data.Wallet wallet) {
        tvCoinBalance.setText(sessionManager.getCoinBalance());
    }

    @Override
    public void moveNext(Integer verificationStatus) {
        switch (verificationStatus) {
            case 0:
                //not approved
                startActivity(new Intent(this, ValidateActivity.class)
                        .putExtra("image", R.drawable.ic_validate)
                        .putExtra("title", getString(R.string.kyc_not_verified))
                        .putExtra("message", getString(R.string.kyc_not_verified_message))
                );
                break;
            case 1:
                // approved
                startActivity(new Intent(this, WalletDashboardActivity.class));
                break;
            default:
                //not applied
                startActivity(new Intent(this, KycActivity.class));
                break;
        }
    }

    @Override
    public void onSuccessSubscribe(boolean isChecked) {
        mediaPlayer.start();
        btnSubscribe.setChecked(isChecked);
        loadData();
    }

    @Override
    public void insufficientBalance() {

        Utilities.openInsufficientBalanceDialog(this);
    }

    @Override
    public void noProfile(String message) {
        Dialog dialog = new Dialog(this);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_profile);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()
                .setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

        TextView tvMessage = dialog.findViewById(R.id.tV_msg);
        tvMessage.setText(message);
        Button ok = dialog.findViewById(R.id.btnOk);
        ok.setOnClickListener(v -> {
            dialog.dismiss();
            onBackPressed();
        });
        dialog.show();
    }

    @Override
    public void showLoader() {
        loader.show();
    }

    @Override
    public void hideLoader() {
        if (loader != null && loader.isShowing())
            loader.dismiss();
    }

    @Override
    public void showSnackMsg(int msgId) {
        String msg = getResources().getString(msgId);
        Snackbar snackbar = Snackbar.make(root, "" + msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setGravity(Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void launchCropImage(Uri data) {
        CropImage.activity(data).start(this);
    }

    @Override
    public void onComplete(CameraOutputModel model) {
        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        intent.putExtra(Constants.Post.PATH, model.getPath());
        intent.putExtra(Constants.Post.TYPE,
                model.getType() == 0 ? Constants.Post.IMAGE : Constants.Post.VIDEO);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    public void reload() {
        presenter.init();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        AlertDialog.Builder confirm = new AlertDialog.Builder(this);
        confirm.setMessage("Are you sure you want to report " + userName + "?");
        confirm.setPositiveButton(R.string.confirm,
                (dialog, w) -> presenter.reportUser(userId, arrayAdapter.getItem(i),
                        arrayAdapter.getItem(i)));
        confirm.setNegativeButton(R.string.cancel, (dialog, w) -> dialog.dismiss());
        confirm.create().show();
    }

    private void openContactDialog(int type) {
        Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.verified_star_email_phone_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow()
                .setLayout(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);

        ImageView imageView = dialog.findViewById(R.id.imageView);
        TextView textView = dialog.findViewById(R.id.tV_msg);
        RelativeLayout rL_ok = dialog.findViewById(R.id.rL_ok);
        if (type == 1) {
            imageView.setImageDrawable(getDrawable(R.drawable.ic_email_verified));
            //textView.setText(getString(R.string.verified_email_msg));
            textView.setText(profileData.getVerified().getEmailId());
        } else {
            imageView.setImageDrawable(getDrawable(R.drawable.ic_phone_number_verified));
            textView.setText(profileData.getVerified().getNumber());
            //textView.setText(getString(R.string.verified_phone_num_msg));
        }
        rL_ok.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    /*
     * Bug title: Profile-> Share-> When click back to the share option it is giving this behaviour please see video.
     * Bug Id: DUBAND150
     * Fix Desc: dismiss and cancelable
     * Fix Dev: Hardik
     * Fix Date: 12/5/21
     * */
    private void shareProfile(String userId) {
        dialog.show();
        new Thread(() -> {
            String url = Constants.DYNAMIC_LINK + "/" + Constants.DYNAMIC_LINK_PROFILE + "/" + userId;
            Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance()
                    .createDynamicLink()
                    .setLongLink(Uri.parse(Constants.DYNAMIC_LINK + "?link=" + url + "&" + BuildConfig.APPLICATION_ID))
                    .buildShortDynamicLink()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                            intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                            intent.setType("text/plain");
                            Intent chooser = Intent.createChooser(intent, getString(R.string.selectApp));
                            startActivity(chooser);
                        }
                        runOnUiThread(() -> dialog.dismiss());
                    });
        }).start();
    }

    @OnClick(R.id.iVshareProfile)
    public void shareProfileOption() {
        shareProfile(userId);
    }

    /*
     * Bug Title: change text to “are you sure you want to subscribe to “known as “ for “x” coins ?
     * Bug Id: #2734
     * Fix Desc: set message
     * Fix Dev: Hardik
     * Fix Date: 23/6/21
     * */
    private void openSubscribeConfirmation(boolean isChecked) {
        View view = LayoutInflater.from(this).inflate(R.layout.subscription_confirmation_dialog, null, false);
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.setContentView(view);

        TextView tVUserName = dialog.findViewById(R.id.tVUserName);
        TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        ImageView profilePic = dialog.findViewById(R.id.profilePic);

        Glide.with(getBaseContext())
                .load(profileData.getProfilePic())
                .asBitmap()
                .centerCrop()
                .placeholder(R.drawable.profile_one)
                .signature(new StringSignature(
                        AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                //.diskCacheStrategy(DiskCacheStrategy.NONE)
                //.skipMemoryCache(true)
                .into(profilePic);

        tVUserName.setText(profileData.getUserName());


        if (isChecked) {
            tvMessage.setText(getString(R.string.subscribe_confirm)
                    + " " + profileData.getStarRequest().getStarUserKnownBy()
                    + " " + getString(R.string.for_text)
                    + " " + profileData.getSubscriptionAmount()
                    + " " + getString(R.string.coins)
                    + "?");
        } else {
            tvMessage.setText(getString(R.string.unsubscribe_dialog_msg)
                    + " " + profileData.getUserName()
                    + "?");
        }

        Button btnConfirm = dialog.findViewById(R.id.btn_confirm);
        Button btn_cancel = dialog.findViewById(R.id.btn_cancel);

        btnConfirm.setOnClickListener(v -> {
            presenter.subscribeStarUser(isChecked, profileData.getId());
            dialog.dismiss();
        });
        btn_cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

}
