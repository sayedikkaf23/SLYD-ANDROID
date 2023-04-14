package chat.hola.com.app.home.profile;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableString;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.BuildConfig;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.squareup.otto.Bus;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.Activities.SelectLoginActivity;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.PostDb;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Utilities.BioCustomTextView;
import chat.hola.com.app.Utilities.CommonClass;
import chat.hola.com.app.Utilities.ConnectivityReceiver;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.activities_user.UserActivitiesActivity;
import chat.hola.com.app.authentication.login.LoginActivity;
import chat.hola.com.app.blockUser.BlockUserActivity;
import chat.hola.com.app.cameraActivities.SandriosCamera;
import chat.hola.com.app.cameraActivities.configuration.CameraConfiguration;
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;
import chat.hola.com.app.coin.base.CoinActivity;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.WalletResponse;
import chat.hola.com.app.my_qr_code.MyQRCodeActivity;
import chat.hola.com.app.post.PostActivity;
import chat.hola.com.app.profileScreen.ProfilePageAdapter;
import chat.hola.com.app.profileScreen.addChannel.AddChannelActivity;
import chat.hola.com.app.profileScreen.channel.ChannelFragment;
import chat.hola.com.app.profileScreen.collection.CollectionFragment;
import chat.hola.com.app.profileScreen.discover.DiscoverActivity;
import chat.hola.com.app.profileScreen.editProfile.EditProfileActivity;
import chat.hola.com.app.profileScreen.followers.FollowersActivity;
import chat.hola.com.app.profileScreen.liked.LikedPostFragment;
import chat.hola.com.app.profileScreen.live_stream.LiveStreamHistoryFragment;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.profileScreen.model.Profile;
import chat.hola.com.app.profileScreen.profile_story.ProfileStoryFrag;
import chat.hola.com.app.profileScreen.purchase.PurchaseFragment;
import chat.hola.com.app.profileScreen.star_video.PlayVideoActivity;
import chat.hola.com.app.profileScreen.story.StoryFragment;
import chat.hola.com.app.profileScreen.tag.TagFragment;
import chat.hola.com.app.settings.SettingsActivity;
import chat.hola.com.app.ui.dashboard.WalletDashboardActivity;
import chat.hola.com.app.ui.kyc.KycActivity;
import chat.hola.com.app.ui.validate.ValidateActivity;
import dagger.android.support.DaggerFragment;
import chat.hola.com.app.webScreen.WebActivity;

import static chat.hola.com.app.Utilities.Utilities.findMatch;
import static chat.hola.com.app.Utilities.Utilities.runOnUiThread;

public class ProfileFragment extends DaggerFragment
        implements ProfileContract.View, ConnectivityReceiver.ConnectivityReceiverListener,
        SwipeRefreshLayout.OnRefreshListener, SandriosCamera.CameraCallback {

    private static final String TAG = ProfileFragment.class.getSimpleName();
    private final int CAMERA_REQUEST = 222;
    private final int READ_STORAGE_REQ_CODE = 26;
    private final int RESULT_LOAD_IMAGE = 1;
    public static boolean isPrivate = false;
    public static int followStatus = 0;

    @Inject
    ProfilePresenter presenter;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    SessionManager sessionManager;
    @Inject
    BlockDialog dialog1;
    @Inject
    PostDb postDb;

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
    @BindView(R.id.tvSubscriberCount)
    TextView tvSubscriberCount;
    @BindView(R.id.linearSubCount)
    LinearLayout linearSubCount;
    @BindView(R.id.tvPostTitle)
    TextView tvPostTitle;
    @BindView(R.id.tvFollowingTitle)
    TextView tvFollowingTitle;
    @BindView(R.id.tvFollowersTitle)
    TextView tvFollowersTitle;
    @BindView(R.id.tvSubscriberTitle)
    TextView tvSubscriberTitle;
    @BindView(R.id.tvtellafriend)
    TextView tvtellafriend;
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
    @BindView(R.id.llCoinBalance)
    LinearLayout llCoinBalance;
    @BindView(R.id.ivCoin)
    ImageView ivCoin;
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
    @BindView(R.id.tvAbout)
    TextView tvAbout;
    @BindView(R.id.tvblockedusers)
    TextView tvblockedusers;
    @BindView(R.id.tvPrivacy)
    TextView tvPrivacy;
    @BindView(R.id.tvLogout)
    TextView tvLogout;
    @BindView(R.id.tvTermsOfService)
    TextView tvTermsOfService;
    @BindView(R.id.tvReportAProb)
    TextView tvReportAProb;
    @BindView(R.id.ll_profilelayout)
    LinearLayout ll_profilelayout;

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
    @BindView(R.id.iV_settings)
    ImageView iV_settings;
    @BindView(R.id.llActivity)
    LinearLayout llActivity;
    @BindView(R.id.ivActivity)
    ImageView ivActivity;
    @BindView(R.id.tvActivity)
    TextView tvActivity;
    @BindView(R.id.tvCoinBalance)
    TextView tvCoinBalance;
    @BindView(R.id.tvTaptoaddstatus)
    TextView tvTaptoaddstatus;
    @BindView(R.id.tvVersion)
    TextView tvVersion;


    AlertDialog.Builder reportDialog;
    AlertDialog.Builder blockDialog;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> blockReasons;

    private Unbinder unbinder;
    private String version = "";
    private ProgressDialog dialog;
    public String userId;
    public String userName = "";
    private Data profileData;
    private boolean isCoinWallet = false;
    private Data.BusinessProfile businessProfile;
    //private Menu menu;
    //private ActionBar actionBar;
    //    private Drawable backDrawableBlack;
    //    private Drawable backDrawableWhite;
    private String myid;
    boolean isBlocked = false;
    //private MenuItem block, unfriend;
    //    private Drawable menudots;
    //    private Drawable menudotsWhite;

    Bus bus = AppController.getBus();
    private boolean isFriend = false;
    private boolean isBusiness = false;

    private Activity mActivity;
    private boolean isVpSetUp = false;

    private TextView tVTitle;
    private ImageView iVDiscover, iVMenu, iVshareProfile;
    private LinearLayout linearTitle;
    private final HashMap<String, Boolean> mPostTitleToggledPositions = new HashMap();
    public int selectedTab = 0;
    private Loader loader;

    @Inject
    public ProfileFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //bus.register(this);
        mActivity = getActivity();
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getResources().getString(R.string.please_wait));
        dialog.setCancelable(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);
        presenter.attachView(this);
        loader = new Loader(mActivity);
        /*BugId:DUBAND101
         * Bug Title:Profile page-> Coins count is not updated.
         * Bug Desc:set coin balance
         * Developer name :Ankit K Tiwary
         * Fixed Date:27-April-2021*/
        tvCoinBalance.setText(Utilities.formatMoney(Double.parseDouble(sessionManager.getCoinBalance())));
        //changeVisibilityOfViews();

        //        backDrawableBlack = getResources().getDrawable(R.drawable.ic_arrow_back_black_24dp);
        //        backDrawableWhite = getResources().getDrawable(R.drawable.ic_arrow_back_white_24dp);
        //        menudots = ContextCompat.getDrawable(mActivity.getApplicationContext(), R.drawable.ic_more_black);
        //        menudotsWhite = ContextCompat.getDrawable(mActivity.getApplicationContext(), R.drawable.ic_more_vert);

        myid = AppController.getInstance().getUserId();

        linearTitle = toolbar.findViewById(R.id.linearTitle);
        tVTitle = toolbar.findViewById(R.id.tvTitle);
        iVDiscover = toolbar.findViewById(R.id.iVDiscover);
        iVMenu = toolbar.findViewById(R.id.iVMenu);
        iVshareProfile = toolbar.findViewById(R.id.iVshareProfile);
        iVDiscover.setVisibility(View.VISIBLE);

        //collapseToolbarLayout.setTitle("Profile");
//        collapseToolbarLayout.setCollapsedTitleTypeface(typefaceManager.getSemiboldFont());
//        collapseToolbarLayout.setExpandedTitleTypeface(typefaceManager.getSemiboldFont());
//        collapseToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);
//        collapseToolbarLayout.setCollapsedTitleTextColor(Color.TRANSPARENT);

        userId = AppController.getInstance().getUserId();
        //preferences = getSharedPreferences("userProfile", MODE_PRIVATE);
        presenter.init();
//        reportDialog = new AlertDialog.Builder(mActivity);
//        reportDialog.setTitle(R.string.report);
//        presenter.getReportReasons();
//
//        blockDialog = new AlertDialog.Builder(mActivity);
//        blockDialog.setTitle(R.string.Block);
//        presenter.getBlockReasons();
//
//        dialog = new ProgressDialog(mActivity);
//        dialog.setMessage(getResources().getString(R.string.please_wait));
//        dialog.setCancelable(false);
        faButton.setVisibility(View.GONE);

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

        /**
         * <p>redirects to BlockedUser's activity</p>
         */
        tvblockedusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BlockUserActivity.class);
                startActivity(intent);
            }
        });

        /**
         * <p>redirects to Report a Problem</p>
         */
        tvReportAProb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                report();
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
        //isBusiness = getIntent().getBooleanExtra("isBusiness", false);
        changeVisibilityOfViews();
        loadData();
        ibEditProfile.setEnabled(false);

        iVMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings();
            }
        });

        iVDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                discover();
            }
        });

        try {
            PackageInfo pInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0);
            version = pInfo.versionName;
            String v = "v" + version;
            tvVersion.setText(v);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return view;
    }

    /*
     * Bug Title: set a value but value not showing up in currency,coin balance on profile is not updated
     * Bug Id: #2754, #2736
     * Fix Desc: reload the profile data when comes back to activity (for edit profile and settings)
     * Fix Dev: Hardik
     * Fix Date: 24/6/21
     * */

    @Override
    public void onResume() {
        super.onResume();
        viewPagerProfile.setCurrentItem(selectedTab);
        loadData();
    }


    @Override
    public void onDestroy() {
        presenter.detachView();
        selectedTab = 0;
        if (unbinder != null) unbinder.unbind();
        //bus.unregister(this);
        super.onDestroy();
    }

    public void changeVisibilityOfViews() {
        ((LandingActivity) mActivity).hideActionBar();
        ((LandingActivity) mActivity).removeFullScreenFrame();
        ((LandingActivity) mActivity).linearPostTabs.setVisibility(View.GONE);
//        ((LandingActivity) mActivity).drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ((LandingActivity) mActivity).tvCoins.setVisibility(View.GONE);
        ((LandingActivity) mActivity).tvSearch.setVisibility(View.GONE);
        ((LandingActivity) mActivity).ivLiveStream.setVisibility(View.GONE);
        loadData();
    }

    /**
     * Set Business profile details
     *
     * @param isChecked : true= business profile, false= non business profile
     */
    private void switchToBusiness(boolean isChecked) {

        Data.BusinessProfile businessProfile = profileData.getBusinessProfiles().get(0);

        sessionManager.setBusinessUniqueId(businessProfile.getBusinessUniqueId());
        boolean isMine = userId.equals(AppController.getInstance().getUserId());

        String fname = profileData.getFirstName();
        String lname = profileData.getLastName();
        String name;
        if (fname.contains(lname)) {
            name = fname;
        } else {
            name = fname + " " + lname;
        }

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

        String profileStatus = profileData.getStatus().replace("dub.ly", getString(R.string.app_name)).replace("Dub.ly", getString(R.string.app_name)).replace("BeSocial",getString(R.string.app_name));
        if (profileStatus == null || profileStatus.isEmpty()) {
            profileStatus = "";
        }

        String status = isChecked ? businessProfile.getBusinessBio() : profileStatus;
//        if (!status.toLowerCase().contains(getString(R.string.app_name).toLowerCase()))
//            tvProfileStatus.setAutoLinkMask(Linkify.WEB_URLS);
//        Linkify.addLinks(tvProfileStatus, Linkify.WEB_URLS);
//        tvProfileStatus.setText(status);
        SpannableString spanString = new SpannableString(status);
        Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(spanString);
        findMatch(mActivity, spanString, matcher);
        Matcher userMatcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
        findMatch(mActivity, spanString, userMatcher);
        tvProfileStatus.setText(spanString, mPostTitleToggledPositions, userId, typefaceManager);

        if (businessProfile.getBusinessProfilePic() != null && !businessProfile.getBusinessProfilePic().isEmpty()) {
            Glide.with(mActivity.getBaseContext())
                    .load(businessProfile.getBusinessProfilePic())
                    .asBitmap()
                    .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    .centerCrop()
                    //.placeholder(R.drawable.profile_one)
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
            Utilities.setTextRoundDrawable(mActivity, businessProfile.getBusinessName(), "", ivProfile);
        }

        Glide.clear(ivProfileBg);
        Glide.with(mActivity.getBaseContext())
                .load(businessProfile.getBusinessProfileCoverImage())
                .asBitmap()
                .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                .centerCrop()
                .placeholder(R.drawable.default_cover_photo)
                .into(ivProfileBg);
    }

    @OnClick(R.id.cV_starVideo)
    public void openVideo() {
        Intent intent = new Intent(mActivity, PlayVideoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.ivCloseAd)
    public void closeAd() {
        cV_starVideo.setVisibility(View.GONE);
    }

    @OnClick(R.id.rl_balance)
    public void openTransaction() {
        startActivity(new Intent(getContext(), WalletDashboardActivity.class));
    }

    @OnClick(R.id.ibBusinessLocation)
    public void location() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="
                + businessProfile.getBusinessLat()
                + ","
                + businessProfile.getBusinessLng()
                + " ( )"));
        startActivity(intent);
    }

    @OnClick(R.id.iV_scan)
    public void openMyQRCode() {
        if (profileData != null) {
            Intent intent = new Intent(mActivity, MyQRCodeActivity.class);
            intent.putExtra("profileData", profileData);
            startActivity(intent);
        }
    }

    @OnClick({R.id.iV_email, R.id.ibBusinessEmail})
    public void showEmail() {

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(
                "mailto:" + (profileData.isActiveBussinessProfile() ? businessProfile.getEmail().getId()
                        : profileData.getVerified().getEmailId())));
        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            //TODO: Handle case where no email app is available
        }

        //openContactDialog(1);
    }

    @OnClick({R.id.iV_phone, R.id.ibBusinessCall})
    public void showPhoneNumber() {

        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse(
                "tel:" + (profileData.isActiveBussinessProfile() ? businessProfile.getPhone()
                        .getCountryCode() + "" + businessProfile.getPhone().getNumber()
                        : profileData.getVerified().getNumber())));
        startActivity(intent);

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

        Intent intent = new Intent(mActivity, ChatMessageScreen.class);
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

    public void loadData() {
        //load data
        if (userId != null && !userId.equals(myid)) {
            ibEditProfile.setVisibility(View.GONE);
            setFabButtonVisible(false);
            presenter.loadMemberData(userId);
        } else {
            ibEditProfile.setVisibility(View.VISIBLE);
            setFabButtonVisible(true);
            presenter.loadProfileData();
            presenter.getWalletBalance();
        }
    }

    @OnClick(R.id.llActivity)
    public void activity() {
        startActivity(new Intent(getContext(), UserActivitiesActivity.class));
    }

    private void changeColor(boolean white) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (white) {
                    Log.d(TAG, "changeColor white");
                    //                    MenuItem discover = menu.findItem(R.id.actionDiscoverPpl);
                    //                    discover.setIcon(R.drawable.ic_discover);
                    //                    MenuItem settings = menu.findItem(R.id.actionSettings);
                    //                    settings.setIcon(R.drawable.ic_settings_white_24dp);
                    //actionBar.setHomeAsUpIndicator(backDrawableWhite);
                    //if (toolbar != null)
                    iVMenu.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white),
                            android.graphics.PorterDuff.Mode.SRC_IN);
                    iVDiscover.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white),
                            android.graphics.PorterDuff.Mode.SRC_IN);
                    iVshareProfile.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white),
                            android.graphics.PorterDuff.Mode.SRC_IN);
                    tVTitle.setVisibility(View.GONE);
                    //toolbar.setOverflowIcon(menudotsWhite);
                } else {
                    Log.d(TAG, "changeColor black");
                    //                    MenuItem discover = menu.findItem(R.id.actionDiscoverPpl);
                    //                    discover.setIcon(R.drawable.ic_discover_black);
                    //                    MenuItem settings = menu.findItem(R.id.actionSettings);
                    //                    settings.setIcon(R.drawable.ic_settings_black_24dp);
                    //actionBar.setHomeAsUpIndicator(backDrawableBlack);
                    //if (toolbar != null)
                    iVMenu.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_black),
                            android.graphics.PorterDuff.Mode.SRC_IN);
                    iVDiscover.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_black),
                            android.graphics.PorterDuff.Mode.SRC_IN);
                    iVshareProfile.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_black),
                            android.graphics.PorterDuff.Mode.SRC_IN);
                    tVTitle.setVisibility(View.VISIBLE);
                    //toolbar.setOverflowIcon(menudots);
                }
            }
        });
    }

    public static void setOverflowButtonColor(final Toolbar toolbar, final int color) {
        Drawable drawable = toolbar.getOverflowIcon();
        if (drawable != null) {
            drawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(drawable.mutate(), color);
            toolbar.setOverflowIcon(drawable);
        }
    }

    //    @Override
    //    public boolean onCreateOptionsMenu(Menu menu) {
    //        this.menu = menu;
    //        MenuInflater menuInflater = getMenuInflater();
    //        menuInflater.inflate(R.menu.new_profile_menu, menu);
    //        return true;
    //    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.actionUnFriend:
                presenter.unfriend(userId);
            case android.R.id.home:
                //back();
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
                    AlertDialog.Builder confirm = new AlertDialog.Builder(mActivity);
                    confirm.setMessage("Are you sure you want to " + " unblock " + userName + "?");
                    confirm.setPositiveButton(R.string.confirm,
                            (dialog, w) -> presenter.block(userId, "unblock", "unblock"));
                    confirm.setNegativeButton(R.string.cancel, (dialog, w) -> dialog.dismiss());
                    confirm.create().show();
                } else {
                    AlertDialog.Builder confirm = new AlertDialog.Builder(mActivity);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
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

    public void discover() {
        startActivity(
                new Intent(mActivity, DiscoverActivity.class).putExtra("caller", "ProfileActivity"));
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
                Intent intent = new Intent(mActivity, FollowersActivity.class);
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
                    Intent intent = new Intent(mActivity, FollowersActivity.class);
                    intent.putExtra("title", getResources().getString(R.string.following));
                    intent.putExtra("following", profileData.getSubscribeUserCount());
                    intent.putExtra("userId", profileData.getId());
                    startActivity(intent);
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    @OnClick({R.id.tvSubscriberCount, R.id.tvSubscriberTitle})
    public void subscribers() {
        try {
            String followingCount = tvFollowingCount.getText().toString();
            if (!followingCount.isEmpty()) {
                if (Integer.parseInt(followingCount) != 0) {
                    Intent intent = new Intent(mActivity, FollowersActivity.class);
                    intent.putExtra("title", "Subscribers");
                    intent.putExtra("following", profileData.getFollowing());
                    intent.putExtra("userId", profileData.getId());
                    startActivity(intent);
                }
            }
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    @OnClick(R.id.iV_settings)
    public void settings() {
        Intent intent = new Intent(mActivity, SettingsActivity.class);
        intent.putExtra("profileData", profileData);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if (requestCode == 0) {

        //if (resultCode == RESULT_OK) {

        //boolean imageUpdated = data.getBooleanExtra("imageUpdated", false);
        //
        //if (imageUpdated) {
        //try {
        //  new ClearGlideCacheAsyncTask().execute();
        //} catch (Exception e) {
        //  e.printStackTrace();
        //}
        //AppController.getInstance().getSessionManager().setUserProfilePicUpdateTime();
        //}
        //presenter.loadProfileData();
        //}
        //} else

        if (requestCode == RESULT_LOAD_IMAGE) {
            presenter.parseMedia(requestCode, resultCode, data);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            presenter.parseCropedImage(requestCode, resultCode, data);
        } else if (requestCode == Constants.EDIT_PROFILE_REQ) {
            //loadData();
            // refreshing in resume
        }
        super.onActivityResult(requestCode, resultCode, data);
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
    public void isLoading(boolean flag) {
    }

    @OnClick(R.id.faButton)
    public void fab() {
        startActivity(new Intent(mActivity, AddChannelActivity.class));
    }

   /* @OnClick(R.id.ivEditProfile)
    public void editProfile() {
        Intent intent = new Intent(mActivity, EditProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("profile_data", profileData);
        intent.putExtra("bundle", bundle);
        intent.putExtra("call", "ProfileActivity");
        startActivityForResult(intent, Constants.EDIT_PROFILE_REQ);
    }*/

    public void setFabButtonVisible(boolean show) {
        //  btnFollow.setVisibility(show ? View.GONE : View.VISIBLE);
        faButton.setVisibility(show ? View.GONE : View.GONE);
    }

    public void setActionBarExpand(boolean expand) {
        appBarLayout.setExpanded(expand, true);
    }

    @Override
    public void showMessage(String msg, int msgId) {
        //        Toast.makeText(this, msg != null && !msg.isEmpty() ? msg : getResources().getString(msgId), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(mActivity);
    }

    @Override
    public void isInternetAvailable(boolean flag) {
        //   llNetworkError.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    @Override
    public void userBlocked() {
        dialog1.show();
    }

    @Override
    public void showProfileData(Profile profile) {
        try {
            userId = profile.getData().get(0).getId();
            showProfile(profile);
        } catch (Exception ignored) {
        }
    }

    private void showProfile(Profile profile) {
        try {
            if (!profile.getData().isEmpty()) {
                ibEditProfile.setEnabled(true);
                followStatus = profile.getData().get(0).getFollowStatus();
                isBlocked = profile.getData().get(0).getBlock() == 1; // 1=blocked
                isPrivate = profile.getData().get(0).getPrivate().equals("1");
                isFriend = profile.getData().get(0).getFriendStatusCode() == 2;

                this.profileData = profile.getData().get(0);

                sessionManager.setAppliedBusinessProfile(!this.profileData.getBusinessProfiles().isEmpty() && this.profileData.getBusinessProfiles().get(0).getStatusCode() != 3);
                isBusiness = profileData.getUserType() == 9;

                String fname = profileData.getFirstName();
                String lname = profileData.getLastName();

                tvTaptoaddstatus.setText(profileData.getStatus().replace("dub.ly", getContext().getString(R.string.app_name))
                        .replace("Dub.ly", getContext().getString(R.string.app_name)).replace("BeSocial",getString(R.string.app_name)));
                sessionManager.setBusinessProfileAvailable(this.profileData.isActiveBussinessProfile());
                sessionManager.setBusinessProfileApproved(profileData.isBusinessProfileApproved());

                if (this.profileData.getBusinessProfiles() != null && this.profileData.getBusinessProfiles().size() > 0) {
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
                            llPrivate.setVisibility(View.GONE);
                            viewPagerProfile.setVisibility(View.VISIBLE);
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
                    btnFollow.setTextOn(getResources().getString(isPrivate ? R.string.requested : R.string.following));
                    btnFollow.setTextOff(getResources().getString(R.string.follow));
                    btnFollow.setChecked(isChecked);
                }

                Data data = profile.getData().get(0);

                if (data.getProfilePic() != null && !data.getProfilePic().isEmpty()) {
                    Glide.with(mActivity).load(data.getProfilePic()).asBitmap()
                            .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().into(new BitmapImageViewTarget(ivProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            super.setResource(resource);
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivProfile.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                } else {
                    Utilities.setTextRoundDrawable(mActivity, data.getFirstName(), data.getLastName(), ivProfile);
                }

                Glide.with(mActivity)
                        .load(data.getProfileCoverImage() != null && !data.getProfileCoverImage().isEmpty() ? data.getProfileCoverImage() : data.getProfilePic()).asBitmap()
                        .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime())).centerCrop()
                        .placeholder(R.drawable.default_cover_photo).into(ivProfileBg);

                String profileStatus = data.getStatus().replace("dub.ly", getString(R.string.app_name)).replace("Dub.ly", getString(R.string.app_name)).replace("BeSocial",getString(R.string.app_name));
                if (profileStatus == null || profileStatus.isEmpty()) {
                    profileStatus = "";
                }

                tvProfileStatus.setOnExpandStateChangeListener(mPostTitleToggledPositions::put);

                SpannableString spanString = new SpannableString(profileStatus);
                Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(spanString);
                findMatch(mActivity, spanString, matcher);
                Matcher userMatcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
                findMatch(mActivity, spanString, userMatcher);
                tvProfileStatus.setText(spanString, mPostTitleToggledPositions, data.getId(), typefaceManager);

                /*tvProfileStatus.setText(profileStatus);
                tvProfileStatus.setAutoLinkMask(Linkify.WEB_URLS);
                Linkify.addLinks(tvProfileStatus, Linkify.WEB_URLS);*/

                //                tvProfileStatus.setMovementMethod(LinkMovementMethod.getInstance());

                String name = (fname != null ? fname : "") + " " + (lname != null ? lname : "");
               if(fname != null && lname != null){
                   if (fname.contains(lname)) {
                       name = fname;
                   } else {
                       name = fname + " " + lname;
                   }
               }

                //collapseToolbarLayout.setTitle(name);
                tVTitle.setText("@" + profileData.getUserName());
                tvProfileName.setText("@" + profileData.getUserName());
                tvLocation.setText(sessionManager.getCity() + ", " + sessionManager.getCountry());
                sessionManager.setIsStar(data.isStar());
                sessionManager.setUserName(profileData.getUserName());
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

                if (data.isStar()) {

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
                    //                    cV_starVideo.setVisibility(View.VISIBLE);
                    //                    iV_starVideo.setImageDrawable(getResources().getDrawable(R.drawable.trailer1));
                    //                    Glide.with(this)
                    //                            .load("https://res.cloudinary.com/dafszph29/image/upload/v1552033376/default/trailer1.png")
                    //                            .asBitmap()
                    //                            .into(iV_starVideo);

                    if (!profileData.isActiveBussinessProfile() && userId != null && !userId.equals(myid)) {
                        rL_star_connect.setVisibility(View.VISIBLE);
                    } else {
                        rL_star_connect.setVisibility(View.GONE);
                    }

                    if (profileData.getWallet() != null && profileData.getWallet().getTotalAmount() > 0) {
                        tv_earn.setVisibility(View.VISIBLE);
                        tv_earn.setText(String.format("%s", Utilities.formatMoney(profileData.getWallet().getTotalAmount())));
                    }

                    if (data.getStarRequest() != null) {
                        if (data.getStarRequest().isEmailVisible()) {
                            iV_email.setVisibility(View.VISIBLE);
                        } else {
                            iV_email.setVisibility(View.GONE);
                        }

                        if (data.getStarRequest().isChatVisible()) {
                            iV_chat.setVisibility(View.VISIBLE);
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

//                if (userId != null && !userId.equals(myid)) {
//                    btnFollow.setVisibility(View.VISIBLE);
//                    llwallet.setVisibility(View.GONE);
//                } else {
                btnFollow.setVisibility(View.GONE);
                llwallet.setVisibility(View.VISIBLE);
                //}
            }

            isBusiness = sessionManager.isBusinessProfileAvailable();
            if (isBusiness) switchToBusiness(isBusiness);
        } catch (Exception ignored) {

            ignored.printStackTrace();
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
        SandriosCamera.with(mActivity)
                .setShowPicker(true)
                .setVideoFileSize(Constants.Camera.FILE_SIZE)
                .setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
                .enableImageCropping(true)
                .launchCamera(this);
    }

    @Override
    public void checkReadImage() {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            presenter.launchImagePicker();
        } else {
            requestReadImagePermission();
        }
    }

    private void requestReadImagePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                || ActivityCompat.shouldShowRequestPermissionRationale(mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            Snackbar snackbar = Snackbar.make(root, R.string.string_222, Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.string_580), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(mActivity, new String[]{
                                    Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                            }, READ_STORAGE_REQ_CODE);
                        }
                    });
            snackbar.show();
            View view = snackbar.getView();
            ((TextView) view.findViewById(R.id.snackbar_text)).setGravity(Gravity.CENTER_HORIZONTAL);
        } else {
            ActivityCompat.requestPermissions(mActivity, new String[]{
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
        Toast.makeText(mActivity, isConnected ? "Internet connected" : "No internet",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void launchPostActivity(CameraOutputModel model) {
        onComplete(model);
    }

    @Override
    public void addToReportList(ArrayList<String> data) {
        arrayAdapter = new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, data);
        //reportDialog.setAdapter(arrayAdapter, mActivity);
    }

    @Override
    public void addToBlockList(ArrayList<String> data) {
        blockReasons = new ArrayAdapter<String>(mActivity, android.R.layout.simple_list_item_1, data);
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
        //this.block.setTitle(message);
        Toast.makeText(mActivity, getResources().getString(!block ? R.string.ubBlock : R.string.Block),
                Toast.LENGTH_SHORT).show();

        if (AppController.getInstance().getSignedIn() && AppController.getInstance().profileSaved()) {
            Intent i2 = new Intent(mActivity, LandingActivity.class);
            i2.putExtra("userId", AppController.getInstance().getUserId());
            i2.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(i2);
            //finish();
            //overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
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
//            rl_balance.setVisibility(View.VISIBLE);
            sessionManager.setWalletBalance(String.valueOf(data.getBalance()));

//            sessionManager.setCurrencySymbol(data.getCurrencySymbol());
//            sessionManager.setCurrency(data.getCurrency());
        }
    }

    @Override
    public void showCoinBalance(WalletResponse.Data.Wallet wallet) {
        tvCoinBalance.setText(Utilities.formatMoney(Double.parseDouble(sessionManager.getCoinBalance())));
    }

    @Override
    public void logout() {
        postDb.delete();
        sessionManager.logOut(mActivity);
        startActivity(new Intent(getContext(), SelectLoginActivity.class));
        getActivity().finish();
    }

    @Override
    public void noProfile(String message) {
        Dialog dialog = new Dialog(mActivity);
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
            //onBackPressed();
        });
        dialog.show();
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
        CropImage.activity(data).start(mActivity);
    }

    @Override
    public void onComplete(CameraOutputModel model) {
        Intent intent = new Intent(mActivity, PostActivity.class);
        intent.putExtra(Constants.Post.PATH, model.getPath());
        intent.putExtra(Constants.Post.TYPE,
                model.getType() == 0 ? Constants.Post.IMAGE : Constants.Post.VIDEO);
        startActivityForResult(intent, CAMERA_REQUEST);
    }

    @Override
    public void reload() {
        presenter.init();
    }

    //    @Override
    //    public void onClick(DialogInterface dialogInterface, int i) {
    //        AlertDialog.Builder confirm = new AlertDialog.Builder(mActivity);
    //        confirm.setMessage("Are you sure you want to report " + userName + "?");
    //        confirm.setPositiveButton(R.string.confirm, (dialog, w) -> presenter.reportUser(userId, arrayAdapter.getItem(i), arrayAdapter.getItem(i)));
    //        confirm.setNegativeButton(R.string.cancel, (dialog, w) -> dialog.dismiss());
    //        confirm.create().show();
    //    }

    private void openContactDialog(int type) {
        Dialog dialog = new Dialog(mActivity);
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
            imageView.setImageDrawable(mActivity.getDrawable(R.drawable.ic_email_verified));
            //textView.setText(getString(R.string.verified_email_msg));
            textView.setText(profileData.getVerified().getEmailId());
        } else {
            imageView.setImageDrawable(mActivity.getDrawable(R.drawable.ic_phone_number_verified));
            textView.setText(profileData.getVerified().getNumber());
            //textView.setText(getString(R.string.verified_phone_num_msg));
        }
        rL_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void moveNext(Integer verificationStatus) {
        switch (verificationStatus) {
            case 0:
                //not approved
                startActivity(new Intent(getContext(), ValidateActivity.class)
                        .putExtra("image", R.drawable.ic_validate)
                        .putExtra("title", getString(R.string.kyc_not_verified))
                        .putExtra("message", getString(R.string.kyc_not_verified_message))
                );
                break;
            case 1:
                // approved
                startActivity(new Intent(getContext(), WalletDashboardActivity.class));
                break;
            default:
                //not applied
                startActivity(new Intent(getContext(), KycActivity.class));
                break;
        }
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
                    .addOnCompleteListener(getActivity(), task -> {
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


    private void inviteFriend(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.inviteMsg) + "\n " + getString(R.string.howdooPlayStore));
        intent.setType("text/plain");
        Intent chooser = Intent.createChooser(intent, getString(R.string.selectApp));
        startActivity(chooser);
    }

//    @OnClick(R.id.iVshareProfile)
//    public void shareProfileOption() {
//        shareProfile(userId);
//    }

    @OnClick(R.id.tvtellafriend)
    public void shareProfileOption() {
//        shareProfile(userId);
        inviteFriend();
    }

    @OnClick(R.id.llCoinBalance)
    public void coinWallet() {
        isCoinWallet = true;
//        presenter.kycVerification();
        startActivity(new Intent(getContext(), CoinActivity.class));
    }

    @Override
    public void showLoader() {
        loader.show();
    }

    @Override
    public void hideLoader() {
        if (loader != null && loader.isShowing()) loader.dismiss();
    }

    @OnClick(R.id.tvLogout)
    public void callLogout() {
        presenter.logout(mActivity);
    }

    @OnClick(R.id.ll_profilelayout)
    public void editProfile() {
        Intent intent = new Intent(mActivity, EditProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("profile_data", profileData);
        intent.putExtra("bundle", bundle);
        intent.putExtra("call", "ProfileActivity");
        startActivityForResult(intent, Constants.EDIT_PROFILE_REQ);
    }

    /* Report a Problem */
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
            Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * <p>redirects to WebActivity's activity and opens About link</p>
     */
    @OnClick(R.id.tvAbout)
    public void about() {
        Intent intent = new Intent(getContext(), WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", getResources().getString(R.string.aboutUsUrl));
        bundle.putString("title", getResources().getString(R.string.about));
        intent.putExtra("url_data", bundle);
        startActivity(intent);
    }

    /**
     * <p>redirects to WebActivity's activity and opens privacy policy link</p>
     */
    @OnClick(R.id.tvPrivacy)
    public void privacyPolicy() {
        Intent intent = new Intent(getContext(), WebActivity.class);
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
        Intent intent = new Intent(getContext(), WebActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", getResources().getString(R.string.termsUrl));
        bundle.putString("title", getResources().getString(R.string.termsOfServiceTitle));
        intent.putExtra("url_data", bundle);
        intent.putExtra("clear", false);
        startActivity(intent);
    }
}
