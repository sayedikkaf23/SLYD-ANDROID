package chat.hola.com.app.home;

import static com.appscrip.myapplication.utility.Constants.CALL_ID;
import static com.appscrip.myapplication.utility.Constants.CALL_STATUS;
import static com.appscrip.myapplication.utility.Constants.CALL_TYPE;
import static com.appscrip.myapplication.utility.Constants.COMING_FROM;
import static com.appscrip.myapplication.utility.Constants.ROOM_ID;
import static com.appscrip.myapplication.utility.Constants.USER_ID;
import static com.appscrip.myapplication.utility.Constants.USER_IMAGE;
import static com.appscrip.myapplication.utility.Constants.USER_NAME;
import static chat.hola.com.app.Utilities.Constants.CURRENCY_SYMBOL;
import static chat.hola.com.app.Utilities.Constants.PROFILE_SCREEN;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.BuildConfig;
import com.ezcall.android.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.gson.Gson;
import com.jio.consumer.domain.interactor.user.handler.UserHandler;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.onesignal.OSPermissionObserver;
import com.onesignal.OSPermissionStateChanges;
import com.onesignal.OneSignal;
import com.squareup.otto.Bus;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Activities.ContactsSecretChat;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.DublyCamera.CameraInFragments.CameraInFragmentsActivity;
import chat.hola.com.app.DublyCamera.deepar.DeeparFiltersTabCameraActivity;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Share.socialmedia.DownloadMedia;
import chat.hola.com.app.Share.socialmedia.DownloadMediaToShareResult;
import chat.hola.com.app.Share.socialmedia.SupportedSocialMediaAppsConfig;
import chat.hola.com.app.Utilities.BottomNavigationViewHelper;
import chat.hola.com.app.Utilities.ConfirmDialog;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.activities_user.UserActivitiesActivity;
import chat.hola.com.app.calling.model.Data;
import chat.hola.com.app.calling.myapplication.utility.CallStatus;
import chat.hola.com.app.calling.video.call.CallingActivity;
import chat.hola.com.app.cameraActivities.SandriosCamera;
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;
import chat.hola.com.app.coin.base.CoinActivity;
import chat.hola.com.app.collections.saved.SavedActivity;
import chat.hola.com.app.ecom.addresslist.SavedAddressListActivity;
import chat.hola.com.app.ecom.help.HelpActivity;
import chat.hola.com.app.ecom.wishlist.WishListActivity;
import chat.hola.com.app.home.callhistory.CallsFragment;
import chat.hola.com.app.home.connect.ConnectFragment;
import chat.hola.com.app.home.connect.ContactActivity;
import chat.hola.com.app.home.connectall.ConnectAllFragment;
import chat.hola.com.app.home.contact.ContactFragment;
import chat.hola.com.app.home.profile.ProfileFragment;
import chat.hola.com.app.home.profile.ProfileFragmentNew;
import chat.hola.com.app.home.social.SocialFragment;
import chat.hola.com.app.home.stories.StoriesFrag;
import chat.hola.com.app.home.trending.TrendingFragment;
import chat.hola.com.app.live_stream.Home.live_users.LiveUsersActivity;
import chat.hola.com.app.live_stream.utility.AlertProgress;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.ConnectionObserver;
import chat.hola.com.app.models.IPServicePojo;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.WalletResponse;
import chat.hola.com.app.orders.historyscreen.HistoryActivity;
import chat.hola.com.app.post.PostActivity;
import chat.hola.com.app.post.model.UploadObserver;
import chat.hola.com.app.postshare.WatermarkConfig;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.search.SearchActivity;
import chat.hola.com.app.settings.SettingsActivity;
import chat.hola.com.app.ui.cards.CardActivity;
import chat.hola.com.app.ui.dashboard.WalletDashboardActivity;
import chat.hola.com.app.ui.kyc.KycActivity;
import chat.hola.com.app.ui.validate.ValidateActivity;
import dagger.android.support.DaggerAppCompatActivity;


/**
 * <h1>LandingActivity</h1>
 *
 * <p> It is main activity which attaches and redirect fragments
 * {@link SocialFragment} {@link ConnectFragment}
 * {@link chat.hola.com.app.DublyCamera.deepar.DeeparFiltersTabCameraActivity} {@link TrendingFragment}
 * {@link ContactFragment} {@link UserActivitiesActivity}
 * Fetching current location and IP address
 * Loading profile data</p>
 *
 * @author Shaktisinh Jadeja
 * @version 1.0
 * @since 21/2/2018
 */

public class LandingActivity extends DaggerAppCompatActivity
        implements LandingContract.View, SandriosCamera.CameraCallback,
        BottomNavigationView.OnNavigationItemSelectedListener,
        NavigationView.OnNavigationItemSelectedListener, OSPermissionObserver,
        DownloadMediaToShareResult {

    private static final String TAG = LandingActivity.class.getSimpleName();
    private final int CAMERA_REQUEST = 222;
    private final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CODE = 111;
    //    private final int READ_STORAGE_REQ_CODE = 26;

    public static boolean isConnect = false;
    private Unbinder unbinder;
    private FragmentTransaction ft;
    private boolean doubleBackToExitPressedOnce = false;
    private boolean isStar = false;
    private static Bus bus = AppController.getBus();
    private boolean isCoinWallet = false;
    //    @Inject
    //    SocialObserver socialShareObserver;
    @Inject
    ConnectionObserver connectionObserver;
    @Inject
    BlockDialog dialog;
    @Inject
    LandingPresenter presenter;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    UploadObserver uploadObserver;
    //    @Inject
//    HomeFragment homeFragment;
//    @Inject
//    PopularFragment popularFragment;
//    @Inject
//    XclusiveFragment xclusiveFragment;
    @Inject
    TrendingFragment trendingFragment;
    @Inject
    ConnectFragment connectFragment;
    @Inject
    ConnectAllFragment connectAllFragment;
    @Inject
    ContactFragment contactFragment;
    @Inject
    ProfileFragment profileFragment;
    @Inject
    ProfileFragmentNew profileFragmentNew;
    @Inject
    SessionManager sessionManager;
    @Inject
    ImageSourcePicker imageSourcePicker;
    @Inject
    NetworkConnector networkConnector;
    @Inject
    UserHandler mUserHandler;

    @BindView(R.id.bottomNavigationView)
    public BottomNavigationView bottomNavigationView;
    @BindView(R.id.actionBarRl)
    RelativeLayout actionBarRl;
    @BindView(R.id.profilePicIv)
    public ImageView ivProfilePic;
    @BindView(R.id.tvSearch)
    public TextView tvSearch;
    @BindView(R.id.iV_plus)
    public ImageView iV_plus;
    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.linearPostTabs)
    public LinearLayout linearPostTabs;
    private TextView chatBadge;
    private boolean isMoveDone = false;
    Loader loader;
    @BindView(R.id.tVFollowing)
    TextView tVFollowing;
    @BindView(R.id.tVForYou)
    TextView tVForYou;
    @BindView(R.id.tVExclusive)
    TextView tVExclusive;

    //    @BindView(R.id.adView)
//    AdView adView;
    @BindView(R.id.vHighlightFollowing)
    View vHighlightFollowing;
    @BindView(R.id.vHighlightForYou)
    View vHighlightForYou;
    @BindView(R.id.vHighlightExclusive)
    View vHighlightExclusive;
    @BindView(R.id.iVcamera)
    public ImageView iVcamera;

    @BindView(R.id.tvCoins)
    public TextView tvCoins;
    @BindView(R.id.liveStream)
    public ImageView ivLiveStream;


    public InterstitialAd mInterstitialAd;
    private FusedLocationProviderClient fusedLocationClient;
    private chat.hola.com.app.profileScreen.model.Data profileData;

    private int selectedSubTabPosition = 1;
    //Social media share
    private SupportedSocialMediaAppsConfig supportedSocialMediaAppsConfig;
    private CallbackManager callbackManager;
    private ShareDialog shareOnFacebookDialog;
    private androidx.appcompat.app.AlertDialog alert;
    private AlertProgress alertProgress;
//    private androidx.appcompat.app.AlertDialog alertDialog;
    private AlertDialog alertDialog;

    //fragments for hola's
    CallsFragment callsFragment;
    @Inject
    StoriesFrag storiesFrag;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_page);
        callsFragment = new CallsFragment();
        /**
         * Bug Title- can’t see the name of collection if I want to type
         * Bug Id-DUBAND2730
         * Fix Description- add SOFT_INPUT_ADJUST_PAN to WindowManager.LayoutParams
         * Developer Name-Ashutosh
         * Fix Date-25/6/21
         **/
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        unbinder = ButterKnife.bind(this);
        bus.register(this);

        OneSignal.addPermissionObserver(this);
//        if (!AppController.getInstance().isFriendsFetched()) presenter.friends();

        try{
            tVFollowing.setTypeface(typefaceManager.getRobotoCondensedBold());
            tVForYou.setTypeface(typefaceManager.getRobotoCondensedBold());
            tVExclusive.setTypeface(typefaceManager.getRobotoCondensedBold());
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            tvCoins.setVisibility(View.GONE);
        } catch (Exception e){
            e.printStackTrace();
        }

        //        nvDrawer.setNavigationItemSelectedListener(this);
        //        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        //        View headerView = nvDrawer.getHeaderView(0);
        //        tvProfileUserName = headerView.findViewById(R.id.tvProfileUserName);
        //        tvProfileMobile = headerView.findViewById(R.id.tvProfileMobile);
        //        tvProfileLogin = headerView.findViewById(R.id.tvProfileLogin);
        //        ivProfileImage = headerView.findViewById(R.id.ivProfileImage);
        //        clProfile = headerView.findViewById(R.id.clProfile);
        if (AppController.getInstance().isGuest()) {
            presenter.getGuestToken();
        }
        //        headerView.setOnClickListener(view -> {
        //            drawerLayout.closeDrawers();
        //            if (AppController.getInstance().isGuest()) {
        //                AppController.getInstance().openSignInDialog(this);
        //            } else {
        //                setProfileData();
        //                startActivity(new Intent(LandingActivity.this, ProfileActivity.class));
        //            }
        //        });

        loader = new Loader(this);
        //set app badge count
        setBadge(this, 0);

        //get current location details
        new IPAddress().execute();

        if (AppController.getInstance().getSignedIn()) CheckLocationPermissions();

//         Initialize the Mobile Ads SDK.
//        MobileAds.initialize(this, initializationStatus -> {
//            Log.d(TAG, "admob init:"+initializationStatus);
//        });

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.d(TAG, "admob init:" + initializationStatus);

            }
        });

//        MobileAds.initialize(this, getString(R.string.admob_appid));


//        AdView adView = new AdView(this);
//
//        adView.setAdSize(AdSize.BANNER);
//
//        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");


//
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_id));
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                Log.d(TAG, "onAdLoaded: ");
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                // Code to be executed when an ad request fails.
//                Log.d(TAG, "onAdFailedToLoad: " + errorCode);
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//                Log.d(TAG, "onAdOpened: ");
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//                Log.d(TAG, "onAdClicked: ");
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//                Log.d(TAG, "onAdLeftApplication: ");
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the interstitial ad is closed.
//                Log.d(TAG, "onAdClosed: ");
//                // Load the next interstitial.
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//        });

        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

//        bottomNavigationView.setItemIconTintList(null);

        View badge = LayoutInflater.from(this).inflate(R.layout.notification_badge, itemView, true);

        chatBadge = (TextView) badge.findViewById(R.id.tV_badge);

        /*When open app we create connect fragment this is required
         * for showing bottom badge while user on other fragment*/
        removeShift(bottomNavigationView, 0);

//        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close) {
//
//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                super.onDrawerSlide(drawerView, slideOffset);
//                Log.d("onDrawerSlide", ":" + slideOffset);
//            }
//        };
//        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //if (!AppController.getInstance().isFriendsFetched()) presenter.friends();

        if (!AppController.getInstance().isGuest()) {
            presenter.getPendingCalls();
            presenter.getUserProfile();
            mUserHandler.setAuthToken(AppController.getInstance().getApiToken());
            mUserHandler.setUserId(AppController.getInstance().getUserId());
        }

        if (!sessionManager.isAskAutoStart() && !AppController.getInstance().isGuest()) {
            askDeviceSpecificPermission();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createCallNotificationChannel();
        }

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && !AppController.getInstance()
                .isGuest()) {
            if (am.isBackgroundRestricted() && !sessionManager.isBackgroundRestrictAsk()) {
                openBackgroundRestrictedDialog();
            }
        }

        callbackManager = CallbackManager.Factory.create();
        shareOnFacebookDialog = new ShareDialog(this);
        shareOnFacebookDialog.registerCallback(callbackManager, shareCallback);
        supportedSocialMediaAppsConfig = new SupportedSocialMediaAppsConfig();
        alertProgress = new AlertProgress(this);
        //To delete files downloaded for share
        try {

            new ClearDownloadedFilesAsyncTask().execute();
        } catch (Exception ignore) {
        }
    }

    /*sets the profile data in drawer*/
//    void setProfileData() {
//        tvProfileUserName.setVisibility(View.VISIBLE);
//        tvProfileMobile.setVisibility(View.VISIBLE);
//        tvProfileLogin.setVisibility(View.GONE);
//        Glide.with(this)
//                .load(sessionManager.getUserProfilePic())
//                .asBitmap()
//                .centerCrop()
//                .placeholder(R.drawable.default_profile)
//                .signature(new StringSignature(
//                        AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
//                .into(new BitmapImageViewTarget(ivProfileImage));
//        tvProfileUserName.setText(String.format("%s %s", sessionManager.getFirstName(),
//                sessionManager.getLastName()));
//        tvProfileMobile.setText(sessionManager.getMobileNumber());
//    }

    private void CheckLocationPermissions() {

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted())
                            fetchLastLocation();

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        System.out.println(TAG + " " + permissions);
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        System.out.println(TAG + " " + error);
                    }
                })
                .onSameThread().check();
    }

    private void fetchLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location -> {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        // Logic to handle location object
                        sessionManager.setCurrentLocation(location);
                        presenter.patchLatLong(AppController.getInstance().getApiToken(), location.getLatitude(), location.getLongitude());
                    }
                });
    }

//    @OnClick({R.id.iVSettings, R.id.tVSettings})
//    public void onSettingsClick() {
//    }
//
//    @OnClick(R.id.rLSaved)
//    public void onSavedClick() {
//        if (AppController.getInstance().isGuest()) {
//            homeFragment.resetMediaPlayer();
//            popularFragment.resetMediaPlayer();
//            xclusiveFragment.resetMediaPlayer();
//            AppController.getInstance().openSignInDialog(this);
//        }else {
//            startActivity(new Intent(this, SavedActivity.class));
//        }
//
//    }
//
//    @OnClick(R.id.rLDiscover)
//    public void openDiscoverPeople() {
//        if (AppController.getInstance().isGuest()) {
//            homeFragment.resetMediaPlayer();
//            popularFragment.resetMediaPlayer();
//            xclusiveFragment.resetMediaPlayer();
//            AppController.getInstance().openSignInDialog(this);
//        }else {
//            Intent intent = new Intent(LandingActivity.this, DiscoverActivity.class);
//            intent.putExtra("caller", "LandingActivity");
//            intent.putExtra("is_contact", false);
//            startActivity(intent);
//        }
//
//    }

    /**
     * <p>It sets the title of the screen from attached Fragment<p/>
     *
     * @param txt      : title text
     * @param typeface : font name
     */
    public void setTitle(String txt, Typeface typeface) {
        title.setText(txt);
        title.setTypeface(typeface);
    }

    /**
     * opens alert popup dialog when user is blocked from admin
     */
    @Override
    public void userBlocked() {
        dialog.show();
    }

    @OnClick(R.id.iVcamera)
    public void cameraClick() {
        if (AppController.getInstance().isGuest()) {
            AppController.getInstance().openSignInDialog(this);
            return;
        }
        isStar = false;
        openCamera();
    }

    @OnClick({R.id.tVFollowing})
    public void followingClick() {

        if (AppController.getInstance().isGuest()) {
            AppController.getInstance().openSignInDialog(this);
            return;
        }

        if (selectedSubTabPosition != 0) {
            selectedSubTabPosition = 0;
            //changeTab(true);
        }
    }

    @OnClick({R.id.tVForYou})
    public void forYouClick() {
        if (selectedSubTabPosition != 1) {
            selectedSubTabPosition = 1;
            //changeTab(true);
        }
    }

    @OnClick({R.id.tVExclusive})
    public void xclusivePostsClicked() {
        if (selectedSubTabPosition != 2) {
            selectedSubTabPosition = 2;
            //changeTab(true);
        }
    }


    @Override
    public void removeShift(BottomNavigationView bottomNavigationView, int position) {
        Menu menu = bottomNavigationView.getMenu();
        selectFragment(menu.getItem(position));
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return selectFragment(item);
    }

    @Override
    public boolean selectFragment(MenuItem item) {
//        drawerLayout.closeDrawers();
        item.setChecked(true);
        FragmentManager fragmentManager = getSupportFragmentManager();
        ft = fragmentManager.beginTransaction();
        isStar = false;
        if (item.getItemId() == R.id.profile) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isStar", isStar);
            //contactFragment.setArguments(bundle);
            //profileFragment.setArguments(bundle);
        }
        return fragment(item.getItemId(), item);
    }

    @OnClick(R.id.liveStream)
    public void liveStream() {
        if (AppController.getInstance().isGuest()) {
            AppController.getInstance().openSignInDialog(this);
        } else {
            isStar = false;
            startActivity(new Intent(this, LiveUsersActivity.class).putExtra("isLiveStream", true).putExtra("fromLandingActivity", true));
            this.overridePendingTransition(R.anim.left_enter, R.anim.left_exit);
            //finish();
        }
    }

    /*
     * Bug Title: Coins->Not updating the coins after buying
     * Star profile page: after a post purchase or subscription refresh the wallet balance on top right
     * Bug Id: #2647, #2707
     * Fix Desc: global method in activity to refresh coin and wallet data,
     *           calling from HomeFragment, PopularFragment,,XclusiveFragment
     * Fix Dev: Hardik
     * Fix Date: 22/6/21
     * */

    public void getWalletBalance() {
        presenter.getWalletBalance();
    }

    @OnClick(R.id.activity)
    public void activity() {
        isStar = false;
        startActivity(new Intent(this, UserActivitiesActivity.class));
    }


    /**
     * <p>It controls bottom navigation menu<p/>
     *
     * @param itemId : ID of selected menu option
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean fragment(int itemId, MenuItem item) {
        isConnect = false;
        switch (itemId) {

            case R.id.status:
                if (profileFragment.isAdded()) {
                    ft.hide(profileFragment);
                }
                if (profileFragmentNew.isAdded()) {
                    ft.hide(profileFragmentNew);
                }
                if (storiesFrag.isAdded()) {
                    ft.hide(storiesFrag);
                }
                if (callsFragment.isAdded()) {
                    ft.hide(callsFragment);
                }
                if (!connectAllFragment.isAdded()) {
                    pushFragment(connectAllFragment);
                } else {
                    ft.show(connectAllFragment);
                    ft.commitNowAllowingStateLoss();
                    connectAllFragment.changeVisibilityOfViews();
                }

                bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.color_white));
//                bottomNavigationView.setItemIconTintList(null);
                sessionManager.setHomeTab(0);
                return true;
//            case R.id.messages:
//
//                if (profileFragment.isAdded()) {
//                    ft.hide(profileFragment);
//                }
//                if (connectAllFragment.isAdded()) {
//                    ft.hide(connectFragment);
//                }
//                if (profileFragmentNew.isAdded()) {
//                    ft.hide(profileFragmentNew);
//                }
//                if (callsFragment.isAdded()) {
//                    ft.hide(callsFragment);
//                }
//                if (!storiesFrag.isAdded()) {
//                    pushFragment(storiesFrag);
//                } else {
//                    ft.show(storiesFrag);
//                    ft.commitNowAllowingStateLoss();
//                    storiesFrag.changeVisibilityOfViews();
//                }
//
//                bottomNavigationView.setBackgroundColor(
//                        getResources().getColor(R.color.color_white));
////                bottomNavigationView.setItemIconTintList(null);
//                sessionManager.setHomeTab(1);
//                return true;

            case R.id.chat:

                if (profileFragment.isAdded()) {
                    ft.hide(profileFragment);
                }

                if (connectAllFragment.isAdded()) {
                    ft.hide(connectAllFragment);
                }
                if (profileFragmentNew.isAdded()) {
                    ft.hide(profileFragmentNew);
                }
                if (callsFragment.isAdded()) {
                    ft.hide(callsFragment);
                }
                if (!storiesFrag.isAdded()) {
                    pushFragment(storiesFrag);
                } else {
                    ft.show(storiesFrag);
                    ft.commitNowAllowingStateLoss();
                    storiesFrag.changeVisibilityOfViews();
                }

                bottomNavigationView.setBackgroundColor(
                        getResources().getColor(R.color.color_white));
//                bottomNavigationView.setItemIconTintList(null);
                sessionManager.setHomeTab(1);
                return true;

            case R.id.call:
                if (profileFragment.isAdded()) {
                    ft.hide(profileFragment);
                }
                if (connectAllFragment.isAdded()) {
                    ft.hide(connectAllFragment);
                }
                if (profileFragmentNew.isAdded()) {
                    ft.hide(profileFragmentNew);
                }
                if (storiesFrag.isAdded()) {
                    ft.hide(storiesFrag);
                }
                if (!callsFragment.isAdded()) {
                    pushFragment(callsFragment);
                } else {
                    ft.show(callsFragment);
                    ft.commitNowAllowingStateLoss();
                    callsFragment.changeVisibilityOfViews();
                }

                bottomNavigationView.setBackgroundColor(
                        getResources().getColor(R.color.color_white));
//                bottomNavigationView.setItemIconTintList(null);
                sessionManager.setHomeTab(2);
                return true;

            case R.id.profile:
                if (profileFragment.isAdded()) {
                    ft.hide(profileFragment);
                }
                if (connectAllFragment.isAdded()) {
                    ft.hide(connectAllFragment);
                }
                if (storiesFrag.isAdded()) {
                    ft.hide(storiesFrag);
                }
                if (callsFragment.isAdded()) {
                    ft.hide(callsFragment);
                }
                if (!profileFragmentNew.isAdded()) {
                    pushFragment(profileFragmentNew);
                } else {
                    ft.show(profileFragmentNew);
//                    ft.commitNowAllowingStateLoss();
                    ft.commitNowAllowingStateLoss();
                    profileFragmentNew.changeVisibilityOfViews();
                }

                bottomNavigationView.setBackgroundColor(
                        getResources().getColor(R.color.color_white));
//                bottomNavigationView.setItemIconTintList(null);
                sessionManager.setHomeTab(3);
                return true;

            case R.id.settings:

                if (profileFragmentNew.isAdded()) {
                    ft.hide(profileFragmentNew);
                }

                if (connectAllFragment.isAdded()) {
                    ft.hide(connectAllFragment);
                }
                if (storiesFrag.isAdded()) {
                    ft.hide(storiesFrag);
                }
                if (callsFragment.isAdded()) {
                    ft.hide(callsFragment);
                }
                if (!profileFragment.isAdded()) {
                    pushFragment(profileFragment);
                } else {
                    ft.show(profileFragment);
//                    ft.commitNowAllowingStateLoss();
                    ft.commitNowAllowingStateLoss();
                    profileFragment.changeVisibilityOfViews();
                }

                bottomNavigationView.setBackgroundColor(
                        getResources().getColor(R.color.color_white));
//                bottomNavigationView.setItemIconTintList(null);
                sessionManager.setHomeTab(4);

                return true;

            case R.id.actionActivity:
                item.setChecked(false);
                isStar = false;
                startActivity(new Intent(this, UserActivitiesActivity.class));
                return true;
            case R.id.actionSavedPost:
                item.setChecked(false);
                startActivity(new Intent(this, SavedActivity.class));
                return true;

            case R.id.actionHistory:
                item.setChecked(false);
                if (AppController.getInstance().isGuest()) {
                    AppController.getInstance().openSignInDialog(this);
                    return false;
                }
                startActivity(new Intent(this, HistoryActivity.class));
                return true;
            case R.id.actionWallet:
                item.setChecked(false);
                if (AppController.getInstance().isGuest()) {
                    AppController.getInstance().openSignInDialog(this);
                    return false;
                }
                startActivity(new Intent(this, WalletDashboardActivity.class));
                return true;
            case R.id.actionCard:
                item.setChecked(false);
                if (AppController.getInstance().isGuest()) {
                    AppController.getInstance().openSignInDialog(this);
                    return false;
                }
                startActivity(new Intent(this, CardActivity.class)
                        .putExtra(CURRENCY_SYMBOL, sessionManager.getCurrencySymbol())
                        .putExtra(USER_ID, sessionManager.getUserId()));
                return true;
            case R.id.actionFaq:
            case R.id.actionHelp:
                item.setChecked(false);
                startActivity(new Intent(this, HelpActivity.class));
                return true;
            case R.id.actionSettings:
                item.setChecked(false);
                if (profileData != null) {
                    Intent intent = new Intent(this, SettingsActivity.class);
                    intent.putExtra("profileData", profileData);
                    startActivity(intent);
                }
                return true;
            case R.id.actionWishlist:
                item.setChecked(false);
                startActivity(new Intent(this, WishListActivity.class));
                return true;
            case R.id.actionAddress:
                item.setChecked(false);
                if (AppController.getInstance().isGuest()) {
                    AppController.getInstance().openSignInDialog(this);
                    return false;
                }
                Intent intents = new Intent(this, SavedAddressListActivity.class);
                intents.putExtra(COMING_FROM, PROFILE_SCREEN);
                startActivity(intents);
                return true;
            /*
             *BugId:DUBAND053
             *BugTitle:coins tab added on home menu drawer
             * Developer name:Ankit K Tiwary
             * Fixed date:6April2021*/
            case R.id.actionCoins:
                item.setChecked(false);
                isCoinWallet = true;
                item.setIcon(R.drawable.ic_coin_small);
                item.setIconTintList(null);
                startActivity(new Intent(this, CoinActivity.class));
            default:
                sessionManager.setHomeTab(0);
                return false;
        }
    }

    private void callForViewCountUpdate() {

    }

    public void fullScreenFrame() {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        frameLayout.setLayoutParams(params);
    }

    public void removeFullScreenFrame() {

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.BELOW, R.id.actionBarRl);
        params.addRule(RelativeLayout.ABOVE, R.id.bottomNavigationViewLl);
        frameLayout.setLayoutParams(params);
    }

    @Override
    public void pushFragment(Fragment fragment) {
        try {
            if (!fragment.isAdded()) {
                ft.add(R.id.frameLayout, fragment);
                ft.commitNowAllowingStateLoss();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void hideActionBar() {
        actionBarRl.setVisibility(View.GONE);
//        LinearLayout.LayoutParams newLayoutParams = (LinearLayout.LayoutParams) clProfile.getLayoutParams();
//        newLayoutParams.topMargin = 100;
//        newLayoutParams.leftMargin = 0;
//        newLayoutParams.rightMargin = 0;
//        clProfile.setLayoutParams(newLayoutParams);
    }

    @Override
    public void visibleActionBar() {
        actionBarRl.setVisibility(View.VISIBLE);
//        LinearLayout.LayoutParams newLayoutParams = (LinearLayout.LayoutParams) clProfile.getLayoutParams();
//        newLayoutParams.topMargin = 0;
//        newLayoutParams.leftMargin = 0;
//        newLayoutParams.rightMargin = 0;
//        clProfile.setLayoutParams(newLayoutParams);
    }

    @Override
    protected void onResume() {

//        if (!AppController.getInstance().isGuest()) {
//            setProfileData();
//        } else {
//            tvProfileUserName.setVisibility(View.GONE);
//            tvProfileMobile.setVisibility(View.GONE);
//            tvProfileLogin.setVisibility(View.VISIBLE);
//        }

//        if (!AppController.getInstance().isFriendsFetched()) presenter.friends();

        if (!AppController.getInstance().isGuest()) {
            getWalletBalance();
            presenter.getUserProfile();
        }
        profilepic(null, sessionManager.getUserProfilePic(),
                sessionManager.isStar(),
                sessionManager.getUserName(),
                sessionManager.isBusinessProfileAvailable(),
                sessionManager.isBusinessProfile());

        //String call = getIntent().getStringExtra("caller");
        //if (!isMoveDone && call != null && call.equals("PostActivity")) {
        //    isMoveDone = true;
        //    sessionManager.setHomeTab(0);
        //    selectedSubTabPosition = 0;
        //}

        if (AppController.getInstance().isNewPostCreated()) {
            AppController.getInstance().setNewPostCreated(false);
            sessionManager.setHomeTab(0);
            selectedSubTabPosition = 0;

            int menuIndex = getIntent().getIntExtra("menuIndex", sessionManager.getHomeTab());
            removeShift(bottomNavigationView, menuIndex);
            BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);
        }
        AppController.getInstance().setConnectivityListener(presenter);
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        try {
            bus.unregister(this);
            super.onDestroy();
            if (unbinder != null) unbinder.unbind();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCamera() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            if (AppController.getInstance().isFullScreenCameraTobeOpened()) {
                                //startActivity(
                                //        new Intent(LandingActivity.this, CameraInFragmentsActivity.class).putExtra("call",
                                //                "post"));
                                //startActivity(
                                //        new Intent(LandingActivity.this, DeeparFiltersTabCameraActivity.class).putExtra("call",
                                //                "post"));
                                startActivity(
                                        new Intent(LandingActivity.this, DeeparFiltersTabCameraActivity.class).putExtra("call",
                                                "post"));
                                //startActivity(
                                //        new Intent(LandingActivity.this, CameraActivity.class).putExtra("call",
                                //                "post"));
                            } else {
                                startActivity(
                                        new Intent(LandingActivity.this, CameraInFragmentsActivity.class).putExtra(
                                                "call", "post"));
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showMessage(null, R.string.permission_requires);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public void launchImagePicker(Intent intent) {
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void showSnackMsg(int msgId) {
        String msg = getResources().getString(msgId);
        Snackbar snackbar = Snackbar.make(root, "" + msg, Snackbar.LENGTH_SHORT);
        snackbar.show();
        View view = snackbar.getView();
        ((TextView) view.findViewById(com.google.android.material.R.id.snackbar_text)).setGravity(
                Gravity.CENTER_HORIZONTAL);
    }

    @Override
    public void launchCropImage(Uri data) {
        CropImage.activity(data).start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            presenter.instagramShare(data.getStringExtra(Constants.TYPE),
                    data.getStringExtra(Constants.PATH));
        } else if (requestCode == RESULT_LOAD_IMAGE) {
            presenter.parseMedia(requestCode, resultCode, data);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            presenter.parseCropedImage(requestCode, resultCode, data);
        } else if (requestCode == 123) {
            connectAllFragment.selectTab(1);
            removeShift(bottomNavigationView, 4);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 26) {
            if (grantResults.length == 2
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(LandingActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    presenter.launchImagePicker();
                } else {
                    //Access storage permission denied
                    showSnackMsg(R.string.string_1006);
                }
            } else {
                //Access storage permission denied
                showSnackMsg(R.string.string_1006);
            }
        } else if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(this, DeeparFiltersTabCameraActivity.class));
            }
        }
    }

    @OnClick(R.id.tvSearch)
    public void search() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.profilePicIv)
    public void profilePic() {
        startActivity(new Intent(LandingActivity.this, ProfileActivity.class));
    }

//    @OnClick(R.id.ivMenu)
//    public void menuClicked() {
//        openDrawer();
//    }

    @OnClick(R.id.iV_plus)
    public void chatOptions() {
        PopupMenu popup = new PopupMenu(LandingActivity.this, iV_plus);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.plus_icon_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.newChat:
                        Bundle bundle = new Bundle();
                        bundle.putString("name", "ChatFragment");
                        startActivity(new Intent(LandingActivity.this, ContactActivity.class));
                        break;
//                    case R.id.groupChat:
//                        Intent intent = new Intent(LandingActivity.this, CreateGroup.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                        startActivity(intent);
//                        break;
                    case R.id.secretChat:
                        Intent intent1 = new Intent(LandingActivity.this, ContactsSecretChat.class);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent1);
                        break;
                }

                return true;
            }
        });

        popup.show();
    }

    /*opens drawer*/
//    public void openDrawer() {
//        if (!drawerLayout.isDrawerOpen(GravityCompat.START))
//            drawerLayout.openDrawer(GravityCompat.START);
//        else drawerLayout.closeDrawer(GravityCompat.END);
//    }

    /**
     * <p>It handles back press. If first item is selected and click back twice it will exit the app
     * otherwise redirects to first item<p/>
     */
    @Override
    public void onBackPressed() {
        if (connectAllFragment.isVisible()) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();

                supportFinishAfterTransition();
                //actionBarRl.setVisibility(View.VISIBLE);
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            showMessage(null, R.string.back_press_message);

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else {
            removeShift(bottomNavigationView, 0);
        }
    }

    @Override
    public void showMessage(String msg, int msgId) {
        Toast.makeText(this, msg != null && !msg.isEmpty() ? msg : getResources().getString(msgId),
                Toast.LENGTH_SHORT).show();
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
    public void launchPostActivity(CameraOutputModel model) {
        onComplete(model);
    }

    @Override
    public void intenetStatusChanged(boolean isConnected) {
        //showSnack(isConnected);
    }

    @Override
    public void profilepic(chat.hola.com.app.profileScreen.model.Data data, String profilePic, boolean isStar, String userName,
                           boolean isBusinessProfileActive, boolean businessProfile) {


        if (!AppController.getInstance().isGuest()) {
            if (data != null) {
                profileData = data;
                sessionManager.setBusinessProfileAvailable(this.profileData.isActiveBussinessProfile());
                profilePic(data.getProfilePic());
            }
//            setProfileData();
        }

        try {
            Glide.with(this).load(profilePic).asBitmap().centerCrop()
                    .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    //    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    //.diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)
                    .placeholder(R.drawable.ic_profile_tab)
                    .into(new BitmapImageViewTarget(ivProfilePic) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivProfilePic.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getApplicationContext());
    }

    private class IPAddress extends AsyncTask<Void, Void, IPServicePojo> {
        @Override
        protected IPServicePojo doInBackground(Void... voids) {
            String stringUrl = "https://ipinfo.io/json";
            try {
                URL url = new URL(stringUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                StringBuffer response = new StringBuffer();
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                //Log.d("Update Profile", "ipAddress2: " + response.toString());
                Gson gson = new Gson();
                return gson.fromJson(response.toString(), IPServicePojo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(IPServicePojo s) {
            super.onPostExecute(s);
            if (s != null) {
                //Log.d("Update Profile", "IPADDRESS onPostExecute: " + s);
                //Log.d("Update Profile", "IPADDRESS onPostExecute: " + s.getIp());
                sessionManager.setIpAdress(s.getIp());
                sessionManager.setCity(s.getCity());
                sessionManager.setCountry(s.getCountry());
                if (AppController.getInstance().isGuest()) {
                    presenter.getGuestToken();
                }
            }
        }
    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @Override
    public void reload() {

    }

    /**
     * to update app badge count
     *
     * @param context : activity context
     * @param count   : badge count (pass 0 if you want to hide)
     */
    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    /**
     * to get launcher class name
     *
     * @param context : activity context
     */
    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

    public void updateChatBadge(boolean visible, int count) {

        try {
            if (visible) {
                chatBadge.setText(String.valueOf(count));
                chatBadge.setVisibility(View.VISIBLE);
                //unreadCount.setText(String.valueOf(count));
                //chatBadge_rl.setVisibility(View.VISIBLE);
            } else {
                chatBadge.setText(getString(R.string.double_inverted_comma));
                chatBadge.setVisibility(View.GONE);
                //chatBadge_rl.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showBalance(WalletResponse.Data.Wallet data) {
        sessionManager.setWalletAvailable(data != null);
        if (data != null && data.getBalance() != null) {
            sessionManager.setWalletId(data.getWalletid());
            sessionManager.setWalletBalance(data.getBalance());
            sessionManager.setCurrencySymbol(data.getCurrencySymbol());
            sessionManager.setCurrency(data.getCurrency());
        }
        String balance = sessionManager.getCurrencySymbol() + " " + Utilities.formatMoney(Double.valueOf(sessionManager.getWalletBalance()));
        tvBalance.setText(balance);
    }

    @Override
    public void showCoinBalance(WalletResponse.Data.Wallet coinWallet) {
        if (coinWallet != null) {
            sessionManager.setCoinWalletId(coinWallet.getWalletid());
            sessionManager.setCoinBalance(coinWallet.getBalance());
            String coin = Utilities.formatMoney(Double.valueOf(sessionManager.getCoinBalance()));
            tvCoins.setText(coin);
        }
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
    public void gotoWalletDashboard(Integer verificationStatus) {
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
                if (isCoinWallet)
                    startActivity(new Intent(this, CoinActivity.class));
                else
                    startActivity(new Intent(this, WalletDashboardActivity.class));
                break;
            default:
                //not applied
                startActivity(new Intent(this, KycActivity.class));
                break;
        }
    }

    /*
     * Bug Title: we cannot cut the call once we minimize the application and maximize the application on call,
     * caller press home button calling screen gone not able to retrieve again
     * Fix Description: launch flags are set in activity
     * Developer Name: Hardik
     * Fix Date: 5/4/2021
     * */
    @Override
    public void showPendingCalls(Data data) {
        Intent notifyIntent = new Intent(this, CallingActivity.class);
        notifyIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        notifyIntent.putExtra(USER_NAME, data.getUserName());
        notifyIntent.putExtra(USER_IMAGE, data.getUserImage());
        notifyIntent.putExtra(USER_ID, data.getUserId());
        notifyIntent.putExtra(CALL_STATUS, CallStatus.NEW_CALL);
        notifyIntent.putExtra(CALL_TYPE, data.getType());
        notifyIntent.putExtra(CALL_ID, data.getId());
        notifyIntent.putExtra(ROOM_ID, data.getRoom());
        startActivity(notifyIntent);
    }

    @OnClick(R.id.rlWallet)
    public void wallet() {
        isCoinWallet = false;
        presenter.kycVerification();
    }

    @OnClick(R.id.tvCoins)
    public void coinWallet() {
        isCoinWallet = true;
//        presenter.kycVerification();
        startActivity(new Intent(this, CoinActivity.class));
    }

    @Override
    public void guestTokenFetched() {
        /**
         * Have commented this line as currently there is no concept of guest xclusiveposts
         */
        //if (xclusiveFragment != null) xclusiveFragment.getGuestDataFirstTime();
    }

    private void openBackgroundRestrictedDialog() {
        ConfirmDialog confirmDialog = new ConfirmDialog(this, R.mipmap.ic_launcher,
                getString(R.string.app_background_restrictiion_msg));
        confirmDialog.show();

        confirmDialog.setCancelable(false);
        Button btnYes = confirmDialog.findViewById(R.id.btnYes);
        Button btnNo = confirmDialog.findViewById(R.id.btnNo);
        btnYes.setText(getString(R.string.open_app_info));
        btnNo.setText(getString(R.string.cancel));
        btnYes.setOnClickListener(v -> {
            sessionManager.setBackgroundRestrictAsk(true);
            try {
                //Open the specific App Info page:
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                //e.printStackTrace();
                //Open the generic Apps page:
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                startActivity(intent);
            }
            confirmDialog.dismiss();
        });
    }

    private void askDeviceSpecificPermission() {
        String manufacturer = android.os.Build.MANUFACTURER;
        switch (manufacturer.toLowerCase()) {
            case "xiaomi":
            case "oppo":
            case "vivo":
                openAppInfoDialog();
                break;
        }
    }

    private void openAppInfoDialog() {
        ConfirmDialog confirmDialog = new ConfirmDialog(this, R.mipmap.ic_launcher, getString(R.string.device_specific_settings_msg));
        confirmDialog.show();

        confirmDialog.setCancelable(false);
        Button btnYes = confirmDialog.findViewById(R.id.btnYes);
        Button btnNo = confirmDialog.findViewById(R.id.btnNo);
        btnYes.setText(getString(R.string.open_app_info));
        btnNo.setText(getString(R.string.cancel));
        btnYes.setOnClickListener(v -> {
            sessionManager.setAskAutoStart(true);
            try {
                //Open the specific App Info page:
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                //e.printStackTrace();
                //Open the generic Apps page:
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                startActivity(intent);
            }
            confirmDialog.dismiss();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createCallNotificationChannel() {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .build();

        String CHANNEL_ID = BuildConfig.APPLICATION_ID.concat("call_notification");
        String CHANNEL_NAME = getString(R.string.call_notificaiton);
        assert notificationManager != null;

        NotificationChannel mChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
        if (mChannel == null) {
            mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            mChannel.setSound(sound, attributes);
            mChannel.enableLights(true);
            mChannel.enableVibration(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                mChannel.setAllowBubbles(true);
            }
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationManager.createNotificationChannel(mChannel);
        }
    }

    @Override
    public void onOSPermissionChanged(OSPermissionStateChanges stateChanges) {
        if (stateChanges.getFrom().areNotificationsEnabled() && !stateChanges.getTo().areNotificationsEnabled()) {
            new AlertDialog.Builder(this).setMessage("Notifications Disabled!").show();
        }
        Log.i("Debug", "onOSPermissionChanged: " + stateChanges);
    }

    private void profilePic(String url) {
        try {
            Glide.with(this).load(url).asBitmap().centerCrop()
                    .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    .placeholder(R.drawable.ic_profile_tab)
                    .error(R.drawable.ic_profile_tab)
                    .into(new BitmapImageViewTarget(ivProfilePic) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ivProfilePic.setImageDrawable(circularBitmapDrawable);
                            //bottomNavigationView.getMenu().findItem(R.id.friends).setIcon(circularBitmapDrawable);
                        }
                    });
        } catch (Exception ignore) {
        }
    }

    public void prepareDynamicLink(String postId) {

        showProgressDialog(getResources().getString(R.string.please_wait));
        new Thread(() -> {
            String url =
                    AppController.getInstance().getString(R.string.dynamic_link_base_url) + postId;
            FirebaseDynamicLinks.getInstance()
                    .createDynamicLink()
                    .setLongLink(Uri.parse(
                            AppController.getInstance().getString(R.string.dynamic_long_link_url)
                                    + url
                                    + AppController.getInstance().getString(R.string.dynamic_query_param)))
                    .buildShortDynamicLink()
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Uri shortLink = task.getResult().getShortLink();
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.putExtra(Intent.EXTRA_TEXT, shortLink.toString());
                            intent.putExtra(Intent.EXTRA_SUBJECT,
                                    AppController.getInstance().getString(R.string.app_name));
                            intent.setType("text/plain");
                            Intent chooser = Intent.createChooser(intent,
                                    AppController.getInstance().getString(R.string.share_post));
                            startActivity(chooser);
                        }
                        hideProgressDialog();
                    })
                    .addOnFailureListener(Throwable::printStackTrace);
        }).start();
    }

    private final FacebookCallback<Sharer.Result> shareCallback =
            new FacebookCallback<Sharer.Result>() {
                @Override
                public void onCancel() {
                    Toast.makeText(LandingActivity.this,
                            AppController.getInstance().getString(R.string.facebook_share_canceled),
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(LandingActivity.this, String.format("Error: %s", error.toString()),
                            Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(LandingActivity.this,
                            AppController.getInstance().getString(R.string.facebook_share_successfull),
                            Toast.LENGTH_SHORT).show();
                }
            };

    private void shareOnFacebook(boolean isImage, String mediaPath) {

        File media = new File(mediaPath);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            uri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".provider", media);
        } else {
            uri = Uri.fromFile(media);
        }
        if (isImage) {
            SharePhoto sharePhoto = new SharePhoto.Builder().setImageUrl(uri).build();

            SharePhotoContent photoContent =
                    new SharePhotoContent.Builder().addPhoto(sharePhoto).build();

            if (ShareDialog.canShow(SharePhotoContent.class)) {
                shareOnFacebookDialog.show(photoContent);
                //shareDialog.show(photoContent,ShareDialog.Mode.WEB);
            } else {

                runOnUiThread(
                        () -> Toast.makeText(this, getString(R.string.facebook_share_not_available),
                                Toast.LENGTH_SHORT).show());
            }
        } else {
            // Create the URI from the media

            ShareVideo shareVideo = new ShareVideo.Builder().setLocalUrl(uri).build();
            ShareVideoContent videoContent = new ShareVideoContent.Builder().setVideo(shareVideo)
                    .setContentTitle(getString(R.string.share_post_title))
                    .setContentDescription(getString(R.string.share_post_description))
                    //.setPreviewPhoto()
                    .build();
            if (ShareDialog.canShow(ShareVideoContent.class)) {
                shareOnFacebookDialog.show(videoContent);
            } else {

                runOnUiThread(
                        () -> Toast.makeText(this, getString(R.string.facebook_share_not_available),
                                Toast.LENGTH_SHORT).show());
            }
        }
    }

    private void shareOnOtherSocialMediaApps(boolean isImage, String mediaPath) {

        String type;
        if (isImage) {
            type = "image/*";
        } else {
            type = "video/*";
        }

        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType(type);
        List<ResolveInfo> queryIntentActivities =
                getPackageManager().queryIntentActivities(shareIntent, 0);
        if (!queryIntentActivities.isEmpty()) {

            for (ResolveInfo resolveInfo : queryIntentActivities) {
                String packageName = resolveInfo.activityInfo.packageName;

                if (supportedSocialMediaAppsConfig.isSupportedAppInstalled(packageName)) {
                    Intent intent = new Intent();
                    intent.setComponent(
                            new ComponentName(packageName, resolveInfo.activityInfo.name));
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType(type);

                    // Create the URI from the media
                    File media = new File(mediaPath);
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        uri = FileProvider.getUriForFile(this,
                                getApplicationContext().getPackageName() + ".provider", media);
                    } else {
                        uri = Uri.fromFile(media);
                    }
                    intent.putExtra(Intent.EXTRA_STREAM, uri);
                    intent.putExtra(Intent.EXTRA_SUBJECT,
                            getString(R.string.share_post_description));
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if (!targetShareIntents.isEmpty()) {

                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0),
                        getString(R.string.choose_app));
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
                        targetShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
            } else {
                Toast.makeText(this, getString(R.string.no_apps_installed), Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public void downloadMediaToShare(String mediaUrl, boolean isImage, String postId,
                                     boolean shareOnFacebook) {
        androidx.appcompat.app.AlertDialog.Builder alertDialog =
                new androidx.appcompat.app.AlertDialog.Builder(this);
        @SuppressLint("InflateParams") final View vDownload = getLayoutInflater().inflate(R.layout.share_confirmation, null);

        AppCompatTextView tvShare = vDownload.findViewById(R.id.tvShare);
        tvShare.setEnabled(true);
        tvShare.setText(getString(R.string.share));
        tvShare.setOnClickListener(v -> {
            alert.setCancelable(false);
            tvShare.setEnabled(false);
            tvShare.setText(getString(R.string.share_preparing));
            String fileName;
            if (isImage) {
                fileName = postId + ".jpg";
            } else {
                fileName = postId + ".mp4";
            }

            DownloadMedia.startMediaDownload(vDownload.findViewById(R.id.pbDownload), mediaUrl.replace("upload/", "upload/" + WatermarkConfig.WATERMARK_CONFIG),
                    this, fileName, isImage, shareOnFacebook);
        });

        alertDialog.setView(vDownload);

        alert = alertDialog.create();
        alert.setCancelable(true);
        if (!isFinishing()) alert.show();
    }

    @Override
    public void downloadResult(String result, String filePath, boolean isImage,
                               boolean shareOnFacebook) {
        if (alert != null && alert.isShowing()) {
            try {
                alert.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (result == null) {
            if (shareOnFacebook) {
                shareOnFacebook(isImage, filePath);
            } else {
                shareOnOtherSocialMediaApps(isImage, filePath);
            }
        } else {

            runOnUiThread(
                    () -> Toast.makeText(LandingActivity.this, result, Toast.LENGTH_SHORT).show());
        }
    }

    private void showProgressDialog(String message) {

        alertDialog = alertProgress.getProgressDialog(this, message);
        if (!isFinishing()) alertDialog.show();
    }

    private void hideProgressDialog() {

        if (alertDialog != null && alertDialog.isShowing()) alertDialog.dismiss();
    }

    public boolean isFacebookInstalled() {

        return supportedSocialMediaAppsConfig.isFacebookInstalled(getPackageManager());
    }

    public static class ClearDownloadedFilesAsyncTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                File dir = new File(AppController.getInstance().getFilesDir() + "/share/");
                if (dir.isDirectory()) {
                    String[] children = dir.list();
                    for (String child : children) {
                        new File(dir, child).delete();
                    }
                }
            } catch (Exception ignore) {
            }
            return "";
        }
    }
}
