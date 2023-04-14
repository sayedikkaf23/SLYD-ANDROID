package chat.hola.com.app.post;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezcall.android.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.PostDb;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Dialog.ChannelPicker;
import chat.hola.com.app.Utilities.App_permission_23;
import chat.hola.com.app.Utilities.ConnectivityReceiver;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SocialShare;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.category.CategoryActivity;
import chat.hola.com.app.hastag.AutoCompleteTextView;
import chat.hola.com.app.hastag.Hash_tag_people_pojo;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.location.Address_list_holder;
import chat.hola.com.app.location.Address_list_item_pojo;
import chat.hola.com.app.location.Location_service;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.Business;
import chat.hola.com.app.models.SocialObserver;
import chat.hola.com.app.post.location.LocationActivity;
import chat.hola.com.app.post.model.AddressAdapter;
import chat.hola.com.app.post.model.CategoryData;
import chat.hola.com.app.post.model.ChannelData;
import chat.hola.com.app.post.model.Post;
import chat.hola.com.app.post.model.PostData;
import chat.hola.com.app.profileScreen.business.post.BusinessPostActivity;
import dagger.android.support.DaggerAppCompatActivity;

/**
 * <h2>PostActivity</h2>
 * <p>It uploads media to cloudinary server and creates a post</p>
 *
 * @author 3Embed
 * @since 2/26/2018
 */

public class PostActivity extends DaggerAppCompatActivity
        implements App_permission_23.Permission_Callback, Location_service.GetLocationListener,
        PostContract.View, ChannelPicker.ChannelSelectCallback, AddressAdapter.ClickListner,
        ConnectivityReceiver.ConnectivityReceiverListener {
    public static final int GET_PRICE = 198;
    private static final String TAG = "PostActivity";
    Geocoder mGeoCoder;
    private String POST_TYPE_REGULAR = "Regular";
    @Inject
    AlertDialog.Builder reportDialog;
    @Inject
    PostPresenter presenter;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    ChannelPicker channelPicker;
    @Inject
    SocialShare socialShare;
    @Inject
    SocialObserver socialObserver;

    @BindView(R.id.root)
    RelativeLayout root;
    @BindView(R.id.ivPreview)
    ImageView ivPreview;
    @BindView(R.id.video_icon)
    ImageView video_icon;
    @BindView(R.id.vidViewPreview)
    SimpleExoPlayerView vidViewPreview;
    @BindView(R.id.sdPreview)
    ImageView sdPreview;
    @BindView(R.id.etPostTitle)
    AutoCompleteTextView etPostTitle;
    @BindView(R.id.actionBarRl)
    androidx.appcompat.widget.Toolbar toolbar;
    @BindView(R.id.tvAddLocation)
    TextView tvAddLocation;
    @BindView(R.id.tvCategory)
    TextView tvCategory;
    @BindView(R.id.ll_category)
    LinearLayout ll_category;
    @BindView(R.id.tvCategoryTitle)
    TextView tvCategoryTitle;
    @BindView(R.id.tvAddToMyChannel)
    TextView tvAddToMyChannel;
    @BindView(R.id.recyclerChannel)
    RecyclerView recyclerChannel;
    @BindView(R.id.tvShare)
    TextView tvShare;
    @BindView(R.id.tvAddress)
    TextView tvAddress;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.btnPublish)
    Button btnPublish;
    @BindView(R.id.switchAddToMyChannel)
    SwitchCompat switchAddToMyChannel;
    @BindView(R.id.rvRecentAddress)
    RecyclerView rvRecentAddress;
    @BindView(R.id.ibClose)
    ImageButton ibClose;
    @BindView(R.id.switchFacebook)
    SwitchCompat switchFacebook;
    @BindView(R.id.switchInstagram)
    SwitchCompat switchInsta;
    @BindView(R.id.switchTwitter)
    SwitchCompat switchTwitter;
    @BindView(R.id.llChannel)
    RelativeLayout llChannel;

    @BindView(R.id.tvAddCategory)
    TextView tvAddCategory;
    @BindView(R.id.llShare)
    LinearLayout llShare;
    @BindView(R.id.viewFilter)
    View viewFilter;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.ibBack)
    ImageButton ibBack;

    @BindView(R.id.llAddButton)
    LinearLayout llAddButton;
    @BindView(R.id.llBusinessDetail)
    LinearLayout llBusinessDetail;
    @BindView(R.id.tvBusinessPost)
    TextView tvBusinessPost;
    @BindView(R.id.tvActionButton)
    TextView tvActionButton;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvUrl)
    TextView tvUrl;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvPosType)
    TextView tvPosType;
    @BindView(R.id.tvTypeTitle)
    TextView tvTypeTitle;

    @BindView(R.id.tvButtonText)
    TextView tvButtonText;
    @BindView(R.id.tvPriceAndMoney)
    TextView tvPriceAndMoney;
    @BindView(R.id.tvLink)
    TextView tvLink;

    @BindView(R.id.tvFacebook)
    TextView tvFacebook;
    @BindView(R.id.tvTwitter)
    TextView tvTwitter;
    @BindView(R.id.tvInstagram)
    TextView tvInstagram;

    @BindView(R.id.cbAllowComments)
    AppCompatCheckBox cbAllowComments;
    @BindView(R.id.cbAllowDownload)
    AppCompatCheckBox cbAllowDownload;
    @BindView(R.id.cbAllowDuet)
    AppCompatCheckBox cbAllowDuet;

    @BindView(R.id.tvAllowDuet)
    TextView tvAllowDuet;

    private Unbinder unbinder;
    private String path;
    private String type;
    private AddressAdapter addressAdapter;
    private Address_list_holder addresslist;
    private boolean first;
    private String categoryId = "";
    private String channelId = "";
    private String musicId = "";
    PostData postData = new PostData();
    Post post = new Post();
    PostDb db = new PostDb(this);
    @Inject
    SessionManager sessionManager;
    private InputMethodManager imm;
    @Inject
    BlockDialog dialog;

    ProgressDialog progressDialog;
    private boolean isGallery;
    LocationManager locationManager;
    private String city;
    private String place;
    private String businessPostTypeId = "", businessPostType = "Regular", businessPrice = "",
            businessUrl = "", businessCurrency = "", businessButtonText = "", businessButtonColor = "";
    private String placeId = "";
    private SimpleExoPlayer player;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    Data data = new Data();
    boolean isEdit = false;
    Map<String, Object> map = new HashMap<>();
    ArrayList<String> files = new ArrayList<>();
    String filterColor;

    @BindView(R.id.llPaidPost)
    LinearLayout llPaidPost;
    @BindView(R.id.switchPaidPost)
    SwitchCompat switchPaidPost;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.rgAmountTag)
    RadioGroup rgAmountTag;
    @BindView(R.id.ll_amount)
    LinearLayout llAmount;

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getApplicationContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        unbinder = ButterKnife.bind(this);
        mGeoCoder = new Geocoder(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        requestStoragePermission();
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        btnPublish.setTypeface(typefaceManager.getSemiboldFont());
        isGallery = getIntent().getBooleanExtra("isGallery", false);
        //init the facebook callback manager
        progressDialog = new ProgressDialog(this);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        etPostTitle.requestFocus();
        etPostTitle.setOnItemClickListener((adapterView, view, i, l) -> {
            etPostTitle.setText(etPostTitle.getText().toString().replace("##", "#"));
            etPostTitle.setSelection(etPostTitle.getText().length());
        });
        hideKeyBoard();

        files = getIntent().getStringArrayListExtra("videoArray");
        post.setFiles(files);
        post.setAudioFile(getIntent().getStringExtra("audio"));
        filterColor = getIntent().getStringExtra("filterColor");
        post.setFilterColor(filterColor);

        data = (Data) getIntent().getSerializableExtra("data");
        isEdit = getIntent().getStringExtra("call") != null;
        type = getIntent().getStringExtra(Constants.Post.TYPE);
        if (type != null) {
            cbAllowDuet.setVisibility(type.equals(Constants.Post.IMAGE) ? View.GONE : View.VISIBLE);
            tvAllowDuet.setVisibility(type.equals(Constants.Post.IMAGE) ? View.GONE : View.VISIBLE);
        }

        tvPosType.setText(POST_TYPE_REGULAR);

        llPaidPost.setVisibility(sessionManager.isStar() && !sessionManager.isBusinessProfileAvailable() ? View.VISIBLE : View.GONE);

        switchPaidPost.setOnCheckedChangeListener((buttonView, isChecked) -> {
            llAmount.setVisibility(isChecked ? View.VISIBLE : View.GONE);
        });

        presenter.getSuggestedCoinAmount();

        if (!isEdit) {

            cbAllowComments.setChecked(true);
            cbAllowDownload.setChecked(true);
            cbAllowDuet.setChecked(true);

            path = getIntent().getStringExtra(Constants.Post.PATH)
                    .replace(Constants.Post.PATH_FILE, Constants.EMPTY);
            musicId = getIntent().getStringExtra("musicId");
            first = true;

            presenter.init(path, type);
            post.setPathForCloudinary(path);
            post.setTypeForCloudinary(type);
            post.setMusicId(musicId);
            tvTitle.setText("New Post");
        } else {
            tvTitle.setText("Edit Post");
            switchPaidPost.setChecked(data.getPaid());

            cbAllowComments.setChecked(data.isAllowComments());
            cbAllowDownload.setChecked(data.isAllowDownload());
            cbAllowDuet.setChecked(data.isAllowDuet());

            ivPreview.setVisibility(View.GONE);
            vidViewPreview.setVisibility(View.GONE);
            viewFilter.setVisibility(View.GONE);
            sdPreview.setVisibility(View.VISIBLE);

            tvLocation.setVisibility(View.VISIBLE);
            tvAddress.setVisibility(View.VISIBLE);
            video_icon.setVisibility(type.equals(Constants.Post.IMAGE) ? View.GONE : View.VISIBLE);

            Glide.with(this).load(Utilities.getModifiedImageLink(data.getImageUrl1())).into(sdPreview);

            etPostTitle.setText(data.getTitle());
            tvLocation.setText(data.getPlace());
            tvAddress.setText(data.getCity());
            tvCategory.setText(data.getCategoryName());
            categoryId = data.getCategoryId();
            channelId = data.getChannelId();
            placeId = data.getPlaceId();
            etPostTitle.setSelection(etPostTitle.getText().length());
            switchAddToMyChannel.setChecked(channelId != null && !channelId.isEmpty());
            clickSwitchAddToMyChannel(channelId != null && !channelId.isEmpty());

            //business
            Business businessProfile = data.getBusiness();
            if (businessProfile != null) {
                businessPostTypeId = businessProfile.getBusinessPostType();
                businessPostType = businessProfile.getBusinessPostTypeLabel();
                tvPosType.setText(businessPostType);
                businessUrl = businessProfile.getBusinessUrl();
                tvLink.setText(businessUrl);
                businessButtonText = businessProfile.getBusinessButtonText();
                businessButtonColor = businessProfile.getBusinessButtonColor();
                tvButtonText.setText(businessButtonText);
                businessPrice = businessProfile.getBusinessPrice();
                if (businessPrice.equals("null")) businessPrice = "";
                businessCurrency = businessProfile.getBusinessCurrency();
                tvPriceAndMoney.setText(businessCurrency + " " + businessPrice);

                if (!businessProfile.getBusinessPostTypeLabel().equalsIgnoreCase(POST_TYPE_REGULAR)) {
                    llAddButton.setVisibility(View.VISIBLE);
                }
            }
        }

        llBusinessDetail.setVisibility(
                sessionManager.isBusinessProfileAvailable() ? View.VISIBLE : View.GONE);

        presenter.getChannels();
        updateUi();

        switchAddToMyChannel.setOnCheckedChangeListener((compoundButton, b) -> {
            if (switchAddToMyChannel.isChecked()) {
                post.setStory(true);
            } else {
                post.setStory(false);
            }
        });

        switchAddToMyChannel.setOnCheckedChangeListener(
                (compoundButton, b) -> clickSwitchAddToMyChannel(b));
        recyclerChannel.setOnClickListener(v -> hideKeyBoard());
        presenter.getCategories();

        PackageManager pm = getPackageManager();
        switchFacebook.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b && !isPackageInstalled("com.facebook.katana", pm)) {
                showMessage("Facebook is not installed", -1);
                switchFacebook.setChecked(false);
            }
        });

        switchInsta.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b && !isPackageInstalled("com.instagram.android", pm)) {
                showMessage("Instagram is not installed", -1);
                switchInsta.setChecked(false);
            }
        });

        switchTwitter.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b && !isPackageInstalled("com.twitter.android", pm)) {
                showMessage("Twitter is not installed", -1);
                switchTwitter.setChecked(false);
            }
        });

        applyFont();
    }

    private boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    @OnClick(R.id.tvPosType)
    public void postType() {
        startActivityForResult(
                new Intent(this, BusinessPostActivity.class).putExtra("postType", businessPostType)
                        .putExtra("VIEW_ID", 4444), 4444);
    }

    @OnClick(R.id.tvButtonText)
    public void actionButton() {
        startActivityForResult(
                new Intent(this, BusinessPostActivity.class).putExtra("businessButtonText",
                        businessButtonText).putExtra("VIEW_ID", 1111), 1111);
    }

    @OnClick(R.id.tvLink)
    public void url() {
        startActivityForResult(
                new Intent(this, BusinessPostActivity.class).putExtra("businessUrl", businessUrl)
                        .putExtra("VIEW_ID", 3333), 3333);
    }

    @OnClick(R.id.tvPriceAndMoney)
    public void price() {
        startActivityForResult(
                new Intent(this, BusinessPostActivity.class).putExtra("businessPrice", businessPrice)
                        .putExtra("businessCurrency", businessCurrency)
                        .putExtra("VIEW_ID", 2222), 2222);
    }

    @OnClick(R.id.ibBack)
    public void back() {
        super.onBackPressed();
    }

    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                        }

                        if (!report.getGrantedPermissionResponses().isEmpty()) {
                            getCurrentPlaceItems();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                                   PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                    }
                })
                .onSameThread()
                .check();
    }

    private void getCurrentPlaceItems() {

        // Initialize Places.
        com.google.android.libraries.places.api.Places.initialize(this,
                getString(R.string.google_api_key_places));

        // Create a new Places client instance.
        PlacesClient placesClient = com.google.android.libraries.places.api.Places.createClient(this);

        // Use fields to define the data types to return.
        List<Place.Field> placeFields =
                Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

        // Use the builder to create a FindCurrentPlaceRequest.
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.builder(placeFields).build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        placesClient.findCurrentPlace(request)
                .addOnSuccessListener((new OnSuccessListener<FindCurrentPlaceResponse>() {
                    @Override
                    public void onSuccess(FindCurrentPlaceResponse response) {

                        Address_list_holder holder = new Address_list_holder();
                        ArrayList<Address_list_item_pojo> list = new ArrayList<>();
                        for (com.google.android.libraries.places.api.model.PlaceLikelihood placeLikelihood : response
                                .getPlaceLikelihoods()) {
                            Address_list_item_pojo item = new Address_list_item_pojo();
                            item.setId(placeLikelihood.getPlace().getId());
                            item.setLatitude(String.valueOf(placeLikelihood.getPlace().getLatLng().latitude));
                            item.setLogitude(String.valueOf(placeLikelihood.getPlace().getLatLng().longitude));
                            item.setAddress_title(placeLikelihood.getPlace().getName());
                            item.setSub_Address(placeLikelihood.getPlace().getAddress());
                            list.add(item);
                        }
                        holder.setList_of_address(list);
                        sessionManager.setAdresses(new Gson().toJson(holder));
                        setAddressList();
                    }
                }))
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        exception.printStackTrace();
                    }
                });
    }

    private void showKeyboard() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                etPostTitle.requestFocus();
                if (imm != null) {
                    imm.showSoftInput(etPostTitle, InputMethodManager.SHOW_FORCED);
                }
            }
        }, 200);
    }

    private void hideKeyBoard() {
        if (imm != null) imm.hideSoftInputFromWindow(etPostTitle.getWindowToken(), 0);
    }

    @OnClick(R.id.ibClose)
    public void close() {
        ibClose.setVisibility(View.GONE);
        tvLocation.setVisibility(View.GONE);
        tvAddress.setVisibility(View.GONE);
        rvRecentAddress.setVisibility(View.VISIBLE);
    }

    //@OnCheckedChanged(R.id.switchAddToMyChannel)
    public void clickSwitchAddToMyChannel(boolean isChecked) {
        recyclerChannel.setVisibility(switchAddToMyChannel.isChecked() ? View.VISIBLE : View.GONE);
        ll_category.setEnabled(!isChecked);
        //tvCategory.setEnabled(this.channelId == null || this.channelId.isEmpty());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setAddressList() {
        try {
            SessionManager sessionManager = new SessionManager(this);
            addresslist = new Gson().fromJson(sessionManager.getAdresses(), Address_list_holder.class);

            if (addresslist != null && addresslist.getList_of_address().size() > 0) {
                rvRecentAddress.setVisibility(View.VISIBLE);
                addressAdapter = new AddressAdapter(addresslist.getList_of_address());
                addressAdapter.setListener(this);
                RecyclerView.LayoutManager mLayoutManager =
                        new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
                rvRecentAddress.setLayoutManager(mLayoutManager);
                rvRecentAddress.setItemAnimator(new DefaultItemAnimator());
                rvRecentAddress.setAdapter(addressAdapter);
            } else {
                rvRecentAddress.setVisibility(View.GONE);
                // tvAddLocation.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUi() {
        llChannel.setVisibility(View.GONE);
        etPostTitle.setListener(new AutoCompleteTextView.AutoTxtCallback() {
            @Override
            public void onHashTag(String tag) {
                Log.d("erd1", "" + tag);
                presenter.searchHashTag(tag);
            }

            @Override
            public void onUserSearch(String tag) {
                presenter.searchUserTag(tag);
            }

            @Override
            public void onClear() {

            }
        });
    }

    @Override
    public void setUser(Hash_tag_people_pojo tag) {
        etPostTitle.updateUserSearch(tag);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.disposeObservable();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
    }

    @OnClick(R.id.tvAddLocation)
    public void location() {
        hideKeyBoard();
        //        Intent intent = new Intent(this, Location_Search_Activity.class);
        Intent intent = new Intent(this, LocationActivity.class);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.btnPublish)
    public void publish() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        etPostTitle.onEditorAction(EditorInfo.IME_ACTION_DONE);
        if (!isEdit) {
            if (type.equals(Constants.Post.VIDEO)) {
                post.setDub(true);
            }
        }
        /*
         * BugId:DUBAND075
         * Bug Title:Caption and category should not be mandatory
         * Bug Desc:the posting of video and duet video must not have any mandatory fields to be filled in order to post (fields like the category and caption should not be mandatory anymore)
         * Developer name:Ankit K Tiwary
         * Fixed Date:15-April-2021*/
//
        if (validatePost()) {
            postIt();
            /*
             * BugId:#2387
             * Bug Title:on posting a video, it i being posted twice
             * Bug Desc: disable
             * Developer name:Hardik
             * Fixed Date:24-5-2021*/
            btnPublish.setEnabled(false);
        }

    }

    private boolean validatePost() {
        try {
            if (switchPaidPost.isChecked()) {
                if (etAmount.getText().toString().trim().isEmpty()
                        || Integer.parseInt(etAmount.getText().toString()) <= 0) {
                    showMessage("Please Enter Valid Amount", 0);
                    return false;
                }
            }
        }catch (NumberFormatException ignored){ }
        /*if (etPostTitle.getText().toString().isEmpty()) {
            showMessage(getString(R.string.enter_caption), 0);
            return false;
        }
        if (categoryId.isEmpty()) {
            showMessage(getString(R.string.please_select_category), 0);
            return false;
        }*/
        return true;
    }

    //converts path to bitmap image
    private Bitmap getBitmapImage(String path) {
        return BitmapFactory.decodeFile(path);
    }

    @Override
    public void applyFont() {
        etPostTitle.setTypeface(typefaceManager.getRegularFont());

        tvAddLocation.setTypeface(typefaceManager.getMediumFont());
        tvAddCategory.setTypeface(typefaceManager.getRegularFont());
        tvCategory.setTypeface(typefaceManager.getRegularFont());
        tvAddToMyChannel.setTypeface(typefaceManager.getRegularFont());
        tvShare.setTypeface(typefaceManager.getRegularFont());

        tvBusinessPost.setTypeface(typefaceManager.getRegularFont());
        tvActionButton.setTypeface(typefaceManager.getRegularFont());
        tvPrice.setTypeface(typefaceManager.getRegularFont());
        tvUrl.setTypeface(typefaceManager.getRegularFont());

        tvFacebook.setTypeface(typefaceManager.getRegularFont());
        tvTwitter.setTypeface(typefaceManager.getRegularFont());
        tvInstagram.setTypeface(typefaceManager.getRegularFont());

        tvButtonText.setTypeface(typefaceManager.getRegularFont());
        tvPriceAndMoney.setTypeface(typefaceManager.getRegularFont());
        tvLink.setTypeface(typefaceManager.getRegularFont());
        tvPosType.setTypeface(typefaceManager.getRegularFont());
        tvType.setTypeface(typefaceManager.getRegularFont());
        tvCategory.setTypeface(typefaceManager.getRegularFont());
        tvCategoryTitle.setTypeface(typefaceManager.getRegularFont());
        tvTypeTitle.setTypeface(typefaceManager.getMediumFont());
        presenter.getCategories();
    }

    @Override
    public void onBackPressed() {
        hideKeyBoard();
        super.onBackPressed();
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 4444 && resultCode == RESULT_OK) {
            businessPostTypeId = data.getStringExtra("id");
            businessPostType = data.getStringExtra("postType");
            tvPosType.setText(businessPostType);
            llAddButton.setVisibility(
                    businessPostType.equalsIgnoreCase(POST_TYPE_REGULAR) ? View.GONE : View.VISIBLE);
        } else if (requestCode == 3333 && resultCode == RESULT_OK) {
            businessUrl = data.getStringExtra("url");
            tvLink.setText(businessUrl);
        } else if (requestCode == 1111 && resultCode == RESULT_OK) {
            businessButtonText = data.getStringExtra("button");
            businessButtonColor = data.getStringExtra("color");
            tvButtonText.setText(businessButtonText);
        } else if (requestCode == 2222 && resultCode == RESULT_OK) {
            businessPrice = data.getStringExtra("price");
            businessCurrency = data.getStringExtra("currency");
            tvPriceAndMoney.setText(businessCurrency + " " + businessPrice);
        } else if (requestCode == 222 && resultCode == RESULT_OK) {
            tvCategory.setText(data.getStringExtra("category"));
            post.setCategoryId(data.getStringExtra("category_id"));
            categoryId = post.getCategoryId();
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                if (data != null) {
                    String lat = data.getStringExtra("latitude");
                    String lng = data.getStringExtra("longitude");
                    String location = data.getStringExtra("locationName");
                    placeId = data.getStringExtra("placeId");
                    post.setPlaceId(placeId);

                    if (lat == null) {
                        lat = "0.0";
                    }

                    post.setLatitude(lat);
                    map.put("latitude", Double.parseDouble(lat));

                    if (lng == null) {
                        lng = "0.0";
                    }
                    post.setLongitude(lng);
                    map.put("longitude", Double.parseDouble(lng));

                    if (location == null) {
                        location = "";
                    }
                    post.setLocation(location);
                    map.put("place", location);

                    setAddress(location, data.getStringExtra("locationDetails"));

                    first = false;
                    setAddressList();
                    getCityAndCountry(Double.parseDouble(lat), Double.parseDouble(lng));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    void getCityAndCountry(double lat, double lng) {
        try {
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = gcd.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                post.setCountrySname(addresses.get(0).getCountryCode());
                post.setCity(addresses.get(0).getLocality());
                map.put("countrySname", addresses.get(0).getCountryCode());
                map.put("city", addresses.get(0).getLocality());
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }
    }

    private void setAddress(String locationName, String locationDetails) {
        rvRecentAddress.setVisibility(View.GONE);
        //  tvAddLocation.setVisibility(View.GONE);
        ibClose.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(locationName)) {
            tvLocation.setVisibility(View.VISIBLE);
            tvLocation.setText(locationName);
            map.put("place", locationName);
            post.setLocation(locationName);
        }

        if (!TextUtils.isEmpty(locationDetails)) {
            tvAddress.setVisibility(View.VISIBLE);
            tvAddress.setText(locationDetails);
        }
    }

    @Override
    public void displayMedia() {
        try {
            btnPublish.setEnabled(true);
            if (type.equals(Constants.Post.IMAGE)) {
                ivPreview.setVisibility(View.VISIBLE);
                vidViewPreview.setVisibility(View.GONE);
                viewFilter.setVisibility(View.GONE);
                video_icon.setVisibility(View.GONE);
                File imgFile = new File(path);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ivPreview.setImageBitmap(myBitmap);
            } else {
                initializePlayer();
                ivPreview.setVisibility(View.GONE);
                vidViewPreview.setVisibility(View.VISIBLE);
                if (filterColor != null && !filterColor.isEmpty()) {
                    viewFilter.setVisibility(View.VISIBLE);
                    viewFilter.setBackgroundColor(Color.parseColor(filterColor));
                }
                video_icon.setVisibility(View.VISIBLE);
                try {
                    buildMediaSource(Uri.parse(files.get(0)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //        vidViewPreview.setVideoPath(path);
                //        vidViewPreview.seekTo(1000);
                //        MediaController mediaController = new MediaController(PostActivity.this);
                //        mediaController.setVisibility(View.GONE);
                //        vidViewPreview.setMediaController(mediaController);
                //        vidViewPreview.setVideoPath(files.get(0));

                //        vidViewPreview.setOnPreparedListener(mp -> {
                //          mp.setVolume(0, 0);
                //          mp.setLooping(true);
                //          vidViewPreview.start();
                //        });
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onBackPress() {

    }

    @Override
    public void setTag(Hash_tag_people_pojo response) {
        etPostTitle.updateHashTagDetails(response);
    }

    @Override
    public void attacheCategory(List<CategoryData> data) {
    }

    @Override
    public void attacheChannels(List<ChannelData> data) {
        if (data.size() > 0) {
            llChannel.setVisibility(View.GONE);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            recyclerChannel.setLayoutManager(llm);
            recyclerChannel.setHasFixedSize(true);
            recyclerChannel.setNestedScrollingEnabled(false);
            recyclerChannel.addItemDecoration(
                    new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
            channelPicker.refreshData(this, data, channelId);
            recyclerChannel.setAdapter(channelPicker);
            channelPicker.setChannelSelector(this, channelId);
        } else {
            llChannel.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMessage(String msg, int msgId) {
        if (msg != null && !msg.isEmpty()) {
            Snackbar snack = Snackbar.make(root, msg, Snackbar.LENGTH_LONG);
            View view = snack.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            snack.show();
            //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        } else if (msgId != 0) {
            Snackbar snack = Snackbar.make(root, getResources().getString(msgId), Snackbar.LENGTH_LONG);
            View view = snack.getView();
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);
            snack.show();
            //Toast.makeText(this, getResources().getString(msgId), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.onResumeFragments();
    }

    @OnClick(R.id.ll_category)
    public void addCategory() {
        hideKeyBoard();
        startActivityForResult(
                new Intent(PostActivity.this, CategoryActivity.class).putExtra("categoryId", categoryId),
                222);
    }

    @Override
    public void onItemClick(int position) {
        setAddress(addresslist.getList_of_address().get(position).getAddress_title(),
                addresslist.getList_of_address().get(position).getSub_Address());
        post.setLatitude(addresslist.getList_of_address().get(position).getLatitude());
        post.setLongitude(addresslist.getList_of_address().get(position).getLogitude());
        post.setLocation(addresslist.getList_of_address().get(position).getAddress_title());
        placeId = addresslist.getList_of_address().get(position).getId();
        post.setPlaceId(placeId);
        if (addresslist.getList_of_address().get(position).getLatitude() != null
                && addresslist.getList_of_address().get(position).getLogitude() != null) {
            getCityAndCountry(
                    Double.parseDouble(addresslist.getList_of_address().get(position).getLatitude()),
                    Double.parseDouble(addresslist.getList_of_address().get(position).getLogitude()));
        }
    }

    @Override
    public void onclick(String channelId, String categoryId, String categoryName) {
        this.channelId = channelId;
        this.categoryId = categoryId;
        tvCategory.setText(categoryName);
        post.setChannelId(channelId);
        post.setCategoryId(categoryId);
        ll_category.setEnabled(this.channelId == null || this.channelId.isEmpty());
    }

    @Override
    public void onUnSelectChannel() {
        channelId = "";
        //tvCategory.setEnabled(true);
    }

    @Override
    public void updateLocation(Location location) {
        sessionManager.setCurrentLocation(location);
    }

    @Override
    public void location_Error(String error) {

    }

    @Override
    public void onPermissionGranted(boolean isAllGranted, String tag) {

    }

    @Override
    public void onPermissionDenied(ArrayList<String> deniedPermission, String tag) {

    }

    @Override
    public void onPermissionRotation(ArrayList<String> rotationPermission, String tag) {

    }

    @Override
    public void onPermissionPermanent_Denied(String tag) {

    }

    private boolean appInstalledOrNot(String uri, String name) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            switch (uri) {
                case "com.twitter.android":
                    switchTwitter.setChecked(false);
                    break;
                case "com.instagram.android":
                    switchInsta.setChecked(false);
                    break;
            }
            Toast.makeText(this, "Please install " + name + " app", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void facebookLogin() {
    }

    @Override
    public void showCategory(List<CategoryData> data) {
        //        this.categoryId = data.get(0).getId();
        //        tvCategory.setText(data.get(0).getId());
    }

    @Override
    public void updated() {
        finish();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Toast.makeText(this, isConnected ? "Internet connected" : "No Internet", Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void reload() {

    }

    private void postIt() {

        presenter.init(path, type);
        post.setPathForCloudinary(path);
        post.setTypeForCloudinary(type);
        post.setMusicId(musicId);
        String text = etPostTitle.getText().toString();
        String regexPattern = "(#\\w+)";

        Pattern p = Pattern.compile(regexPattern);
        Matcher m = p.matcher(text);
        StringBuilder hashtag = new StringBuilder();
        while (m.find()) {
            hashtag.append(",").append(m.group(1));
        }

        try {
            hashtag.replace(0, 1, "");
        } catch (Exception ignored) {
        }

        post.setHashTags(hashtag.toString());
        post.setTitle(etPostTitle.getText().toString());
        post.setChannelId(channelId);
        long tsLong = System.currentTimeMillis() / 1000;
        String ts = Long.toString(tsLong);
        post.setId(ts);
        post.setGallery(isGallery);
        postData.setId(ts);
        postData.setUserId(AppController.getInstance().getUserId());

        postData.setStatus(0); //Notstarted

        post.setPaid(switchPaidPost.isChecked());
        if (!etAmount.getText().toString().trim().isEmpty())
            post.setPostAmount(Double.valueOf(etAmount.getText().toString().trim()));

        if (!isEdit) {
            presenter.init(path, type);
            post.setPathForCloudinary(path);
            post.setTypeForCloudinary(type);
            post.setMusicId(musicId);

            post.setAllowComments(cbAllowComments.isChecked());
            post.setAllowDownload(cbAllowDownload.isChecked());
            post.setAllowDuet(cbAllowDuet.isChecked());

            post.setCityForPost(sessionManager.getCity());
            post.setCountryForPost(sessionManager.getCountry());
            post.setLatForPost(sessionManager.getLatitude());
            post.setLongForPost(sessionManager.getLongitude());

            if (!businessPostType.equalsIgnoreCase(POST_TYPE_REGULAR)) {
                if (validateBusinessPost()) {
                    if (businessButtonText != null) {
                        post.setBusinessButtonText(businessButtonText);
                        post.setBusinessButtonColor(businessButtonColor);
                    }
                    if (businessCurrency != null) post.setBusinessCurrency(businessCurrency);
                    if (businessPostTypeId != null) post.setBusinessPostType(businessPostTypeId);
                    if (businessUrl != null) post.setBusinessUrl(businessUrl);
                    if (businessPrice != null && !businessPrice.isEmpty()) {
                        post.setBusinessPrice(Double.parseDouble(businessPrice));
                    }

                    postData.setData(new Gson().toJson(post));
                    postData.setFbShare(switchFacebook.isChecked());
                    postData.setTwitterShare(switchTwitter.isChecked());
                    postData.setInstaShare(switchInsta.isChecked());
                    postData.setMerged(false);
                    db.addData(postData);

                    try {
                        AppController.getInstance().addNewPost(postData, socialObserver);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //Intent i1 = new Intent(getApplicationContext(), LandingActivity.class);
                    //i1.setAction(Intent.ACTION_MAIN);
                    //i1.addCategory(Intent.CATEGORY_HOME);
                    //i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //i1.putExtra("caller", "PostActivity");
                    //startActivity(i1);
                    supportFinishAfterTransition();
                }
            } else {
                postData.setData(new Gson().toJson(post));
                postData.setFbShare(switchFacebook.isChecked());
                postData.setTwitterShare(switchTwitter.isChecked());
                postData.setInstaShare(switchInsta.isChecked());
                postData.setMerged(false);
                db.addData(postData);

                try {
                    AppController.getInstance().addNewPost(postData, socialObserver);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Intent i1 = new Intent(getApplicationContext(), LandingActivity.class);
                //i1.setAction(Intent.ACTION_MAIN);
                //i1.addCategory(Intent.CATEGORY_HOME);
                //i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //i1.putExtra("caller", "PostActivity");
                //startActivity(i1);
                supportFinishAfterTransition();
            }
        } else {

            if (businessButtonText != null) {
                map.put("businessButtonText", businessButtonText);
                map.put("businessButtonColor", businessButtonColor);
            }
            if (businessCurrency != null) map.put("businessCurrency", businessCurrency);
            if (businessPostTypeId != null) map.put("businessPostTypeId", businessPostTypeId);
            if (businessUrl != null) map.put("businessUrl", businessUrl);
            if (businessPrice != null && !businessPrice.isEmpty()) {
                map.put("businessPrice", businessPrice);
            }

            map.put("postId", data.getId());
            map.put("title", etPostTitle.getText().toString());
            map.put("categoryId", categoryId);
            map.put("categoryName", tvCategory.getText().toString());
            map.put("channelId", channelId);
            map.put("hashTags", hashtag.toString());
            map.put("musicId", musicId);
            map.put("placeId", placeId);
            map.put("place", tvLocation.getText().toString());
            map.put("allowComment", cbAllowComments.isChecked());
            map.put("allowDownload", cbAllowDownload.isChecked());
            map.put("allowDuet", cbAllowDuet.isChecked());

            //userTag
            String regexPattern1 = "(@\\w+)";
            Pattern p1 = Pattern.compile(regexPattern1);
            Matcher m1 = p1.matcher(etPostTitle.getText().toString());
            int i = 0;
            ArrayList<String> strings = new ArrayList<>();
            String userTag[] = new String[i + 1];
            while (m1.find()) {
                strings.add(m1.group(1).replace("@", ""));
                // userTag[i++] = m1.group(1).replace("@", "");
            }

            map.put("userTags", strings);

            presenter.updatePost(map);
        }

        //        }
    }

    private boolean validateBusinessPost() {
        if (businessButtonText == null || businessButtonText.isEmpty()) {
            showMessage("Add business button", -1);
            return false;
        }

        if (businessUrl == null || businessUrl.isEmpty()) {
            showMessage("Add business url", -1);
            return false;
        }

        return true;
    }

    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        try {
            OutputStream out = new FileOutputStream(dst);
            try {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            } finally {
                out.close();
            }
        } finally {
            in.close();
        }
    }

    private void getData(double lat, double lng) {
        try {
            List<Address> addresses = mGeoCoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                String full_addr = addresses.get(0).toString();
                String addrLine1 = addresses.get(0).getAddressLine(0);
                String addrLine2 = addresses.get(0).getAddressLine(1);
                //                new_lat = addresses.get(0).getLatitude();
                //                new_long = addresses.get(0).getLongitude();I
                String city = addresses.get(0).getLocality();
                //                state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String pinCode = addresses.get(0).getPostalCode();

                //                etStreet.setText(addrLine1);
                //                etCity.setText(city);
                //                etZipCode.setText(pinCode);

                String fullAddress = getFullAddress(lat, lng);

                post.setCountrySname(country);
                post.setCity(city);
                map.put("countrySname", country);
                map.put("city", city);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Get address from the provided latitude and longitude
    public String getFullAddress(double lat, double lng) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses.size() > 0) {
                Address obj = addresses.get(0);
                return obj.getAddressLine(0);
            } else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return "Dragged location";
        }
    }

    private void initializePlayer() {
        if (player == null) {
            player = ExoPlayerFactory.newSimpleInstance(this);
            vidViewPreview.setPlayer(player);
            vidViewPreview.setUseController(false);
        }
    }

    private void buildMediaSource(Uri mUri) {
        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory =
                new DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)),
                        bandwidthMeter);
        // This is the MediaSource representing the media to be played.
        MediaSource videoSource =
                new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mUri);
        // Loops the video indefinitely.
        LoopingMediaSource loopingSource = new LoopingMediaSource(videoSource);
        // Prepare the player with the source.
        player.prepare(loopingSource);
        player.setPlayWhenReady(true);
        player.seekTo(200);
        player.setVolume(0.0f);
        //        player.addListener(this);
    }

    @Override
    public void onSuccessCoinAmount(List<String> amounts) {
        setSuggestedAmount("", amounts);
    }

    @SuppressLint("SetTextI18n")
    public void setSuggestedAmount(String currency_symbol, List<String> amounts) {

        for (int i = 0; i < amounts.size(); i++) {
            try {
                String amount = amounts.get(i);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                RadioButton radioButton = new RadioButton(this);
                radioButton.setText(currency_symbol + " " + amount);
                radioButton.setBackground(getDrawable(R.drawable.amount_selector));
                radioButton.setPadding(30, 30, 30, 30);
                radioButton.setButtonDrawable(null);
                radioButton.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin_small, 0, 0, 0);
                radioButton.setTextSize(15);
                params.setMargins(10, 10, 10, 10);
                radioButton.setLayoutParams(params);

                /*
                 * Bug Title: on edit does not show the value I set For a paid post
                 * Bug Id: #2769
                 * Fix Desc; set values in views
                 * Fix Dev: Hardik
                 * Fix Date: 24/6/21
                 * */
                if (isEdit) {
                    if (String.valueOf(data.getPostAmount()).equals(amount)) {
                        etAmount.setText(amount);
                        radioButton.setChecked(true);
                    }
                }

                radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        etAmount.setText(amount);
                    }
                });
                rgAmountTag.addView(radioButton);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
}
