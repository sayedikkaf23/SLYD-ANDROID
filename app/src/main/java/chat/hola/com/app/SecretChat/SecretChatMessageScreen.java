package chat.hola.com.app.SecretChat;

import static com.appscrip.myapplication.utility.Constants.AUDIO;
import static com.appscrip.myapplication.utility.Constants.CALL_ID;
import static com.appscrip.myapplication.utility.Constants.CALL_STATUS;
import static com.appscrip.myapplication.utility.Constants.CALL_TYPE;
import static com.appscrip.myapplication.utility.Constants.ROOM_ID;
import static com.appscrip.myapplication.utility.Constants.USER_ID;
import static com.appscrip.myapplication.utility.Constants.USER_IMAGE;
import static com.appscrip.myapplication.utility.Constants.USER_NAME;
import static com.appscrip.myapplication.utility.Constants.VIDEO;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.imangazaliev.circlemenu.CircleMenu;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import chat.hola.com.app.Activities.ChatCameraActivity;
import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.Activities.FriendsListActivity;
import chat.hola.com.app.Activities.LocationFetchActivity;
import chat.hola.com.app.Activities.MediaHistory;
import chat.hola.com.app.Activities.MessageInfo;
import chat.hola.com.app.Adapters.AttachPagerAdapter;
import chat.hola.com.app.Adapters.SuggestionAdapter;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.DocumentPicker.FilePickerBuilder;
import chat.hola.com.app.DocumentPicker.FilePickerConst;
import chat.hola.com.app.Doodle.DoodleAction;
import chat.hola.com.app.Doodle.DoodlePop;
import chat.hola.com.app.DownloadFile.FileUploadService;
import chat.hola.com.app.DownloadFile.FileUtils;
import chat.hola.com.app.DownloadFile.ServiceGenerator;
import chat.hola.com.app.Giphy.SelectGIF;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Profile.OpponentProfileNew;
import chat.hola.com.app.RecordAudio.ViewProxy;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.CustomLinearLayoutManager;
import chat.hola.com.app.Utilities.FilenameUtils;
import chat.hola.com.app.Utilities.FloatingView;
import chat.hola.com.app.Utilities.GPSTracker;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.TextDrawable;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.Utilities.WrapContentViewPager;
import chat.hola.com.app.Wallpapers.Activities.DrawActivity;
import chat.hola.com.app.Wallpapers.Activities.SolidColorActivity;
import chat.hola.com.app.Wallpapers.Library.LibraryActivity;
import chat.hola.com.app.calling.myapplication.utility.CallStatus;
import chat.hola.com.app.calling.video.call.CallingActivity;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.send_star_packet.SendStarPacketActivity;
import chat.hola.com.app.transfer_to_friend.TransferToFriendActivity;
import github.ankushsachdeva.emojicon.EmojiconEditText;
import github.ankushsachdeva.emojicon.EmojiconGridView;
import github.ankushsachdeva.emojicon.EmojiconsPopup;
import github.ankushsachdeva.emojicon.emoji.Emojicon;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moda on 09/08/17.
 */

public class SecretChatMessageScreen extends AppCompatActivity implements AttachPagerAdapter.SetOnAttachMenuItemClickListner, SuggestionAdapter.ClickListener {
    private SessionApiCall sessionApiCall = new SessionApiCall();
    private static final int MESSAGE_PAGE_SIZE = 10;
    private static final int RESULT_LOAD_WALLPAPER = 1;
    private static final int RESULT_LOAD_VIDEO = 2;
    //    private static final int RESULT_CAPTURE_VIDEO = 3;
    private static final int REQUEST_CODE_CONTACTS = 4;
    private static final int RESULT_SHARE_LOCATION = 5;
    private static final int REQUEST_SELECT_AUDIO = 7;
    private static final int RESULT_LOAD_GIF = 8;
    private static final int RESULT_LOAD_STICKER = 9;

    private static final int RESULT_CAPTURE_WALLPAPER = 11;
    private static final int IMAGE_QUALITY = 50;//change it to higher level if want,but then slower image sending
    private static final int IMAGE_CAPTURED_QUALITY = 50;//change it to higher level if want,but then slower image sending
    private static final double MAX_VIDEO_SIZE = 26 * 1024 * 1024;
    private static Uri imageUrl;
    private static final int RESULT_CAPTURE_MEDIA = 0;
    private static final int RESULT_EDIT_IMAGE = 12;
    private int userBlocked = 0;
    /**
     * For secret chat
     */
//    final String[] dTimeForDB = getResources().getStringArray(R.array.dTimeForDB); //{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "30", "60", "3600", "86400", "604800"};
//    final String[] dTimeToShow = getResources().getStringArray(R.array.dTimeToShow);//{"off", "1s", "2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "10s", "15s", "30s", "1m", "1h", "1d", "1w"};
//    final String[] dTimeOptions = getResources().getStringArray(R.array.dTimeOptions);//{"off", "1 second", "2 seconds", "3 seconds", "4 seconds", "5 seconds", "6 seconds", "7 seconds", "8 seconds", "9 seconds", "10 seconds", "15 seconds", "30 seconds", "1 minute", "1 hour", "1 day", "1 week"};


    final String[] dTimeForDB = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "15", "30", "60", "3600", "86400", "604800"};
    final String[] dTimeToShow = {"off", "1s", "2s", "3s", "4s", "5s", "6s", "7s", "8s", "9s", "10s", "15s", "30s", "1m", "1h", "1d", "1w"};
    final String[] dTimeOptions = {"off", "1 second", "2 seconds", "3 seconds", "4 seconds", "5 seconds", "6 seconds", "7 seconds", "8 seconds", "9 seconds", "10 seconds", "15 seconds", "30 seconds", "1 minute", "1 hour", "1 day", "1 week"};
    private ImageView sendButton, selEmoji, selKeybord, doodle;
    private ImageView sendSticker;
    private EmojiconEditText sendMessage;
    private Drawable drawable2, drawable1;
    private boolean limit = false;
    private String userId, userName, receiverUid, documentId, picturePath, tsForServerEpoch, tsForServer, videoPath, audioPath, gifUrl, stickerUrl;
    private SecretChatMessageAdapter mAdapter;
    private RecyclerView recyclerView_chat;
    private String receiverName;
    private String top;
    private Uri imageUri;
    private String placeString;
    private String contactInfo;
    private ArrayList<SecretChatMessageItem> mChatData;
    private int button01pos, MessageType, size, status;
    private boolean firstTenLoaded = false;
    private CouchDbController db;
    private CustomLinearLayoutManager llm;
    private FrameLayout profilePic;
    private TextView tv, receiverNameHeader, header_receiverName;
    private ImageView attachment, backButton;//, dummyGifIv;
    private RelativeLayout root;
    private TextView dateView;
    private String contactInfoForSaving;
    private DoodlePop toddle;
    private boolean opponentOnline = false;
    //    private String typingTopic;
    private boolean allowLastSeen;
    public String receiverImage;
    private long dTime = 0;
    private String secretId = "";

    private TextView dTimeTv;
    private RelativeLayout invite_rl;

    private boolean inviteVisible = true;
    private String receiverIdentifier;
    private ImageView pic;
    private boolean hasPendingAcknowledgement = false;
    private RelativeLayout header_rl, header_center_rl;
    private Bus bus = AppController.getBus();
    private boolean hasChatId = false, showingLoadingItem = false, canHaveMoreMessages = true;

    private String chatId = "";

    private CircleMenu circleMenu;
    private RelativeLayout rl_recordStop;
    private AppCompatImageView ivDeleteRecord;

    private int pendingApiCalls = 0;


    private boolean fromNotification;


    /**
     * For the audio recording
     */
    private TextView recordTimeText;

    private View recordPanel;
    private View slideText;
    private float startedDraggingX = -1;
    private float distCanMove = dp(80);
    private long startTime = 0L;
    long timeInMilliseconds = 0L;
    long timeSwapBuff = 0L;
    long updatedTime = 0L;
    private Timer timer;

    private LinearLayout sendMessagePanel;
    private MediaRecorder mediaRecorder;

    private boolean recordingAudio = false;

    private AlertDialog locationDialog;


    /**
     * For the message header on  the top
     */
    private TextView messageSelectedCount;

    private ImageView copy;
    private View messageHeader;

    private SecretChatMessageItem currentlySelectedMessage;


    private float density;
    private ImageView info;


    /**
     * For the wallpaper functionality
     */
    private ImageView chatBackground;


    private String wallpaperPath;
    private View wallpaperView;
    private BottomSheetDialog mBottomSheetDialog;

    /**
     * For the block functionality
     */
    private LinearLayout sendMessagePanelLL;
    private TextView blockedTv;
    private boolean lastSeenResponseCame = false;
    //private ImageView initiateCall;


    private String colorCode;
    private RelativeLayout rlAttach;
    private ImageView initiateVideoCall;
    private ImageView initiateAudioCall;

    public SessionManager sessionManager;
    public String userImage;

    private boolean isStar;

    private boolean isStarInFriend = false;

    /*
     * To mark the status of given message as delivered,for which acknowledgement of delivery has been receiveds
     */
    private ImageView iV_star;

    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private RecyclerView rvSuggestion;
    private SuggestionAdapter suggestionAdapter;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.secret_chat_message_screen);

        rvSuggestion = findViewById(R.id.rvSuggestion);
        suggestionAdapter = new SuggestionAdapter(this);
        rvSuggestion.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvSuggestion.setAdapter(suggestionAdapter);

        sessionManager = new SessionManager(this);
        userImage = sessionManager.getUserProfilePic();


        density = getResources().getDisplayMetrics().density;
        button01pos = 0;

        MessageType = 0;
        status = 0;

        sendMessagePanelLL = (LinearLayout) findViewById(R.id.bottomlayout);

        blockedTv = (TextView) findViewById(R.id.blocked);

        top = "Connecting...";

        //   int density = (int) (getResources().getDisplayMetrics().density);
        iV_star = (ImageView) findViewById(R.id.iV_star);
        recyclerView_chat = (RecyclerView) findViewById(R.id.list_view_messages);
        mChatData = new ArrayList<>();
        root = (RelativeLayout) findViewById(R.id.typing);
//        mAdapter = new SecretChatMessageAdapter(SecretChatMessageScreen.this, mChatData, root);
        //  llm = new LinearLayoutManager( SecretChatMessageScreen.this, LinearLayoutManager.VERTICAL, false);

        llm = new CustomLinearLayoutManager(SecretChatMessageScreen.this, LinearLayoutManager.VERTICAL, false);


        recyclerView_chat.setLayoutManager(llm);
        recyclerView_chat.setItemAnimator(new DefaultItemAnimator());

        recyclerView_chat.setHasFixedSize(true);

        chatBackground = (ImageView) findViewById(R.id.chatBackground);
        selKeybord = (ImageView) findViewById(R.id.chat_keyboard_icon);
        circleMenu = findViewById(R.id.fab);

        //dummyGifIv = (ImageView) findViewById(R.id.dummyGifIv);
        doodle = (ImageView) findViewById(R.id.capture_image);

        dTimeTv = (TextView) findViewById(R.id.dTimeTv);
        ItemTouchHelper.Callback callback = new ChatMessageTouchHelper(mAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView_chat);

        invite_rl = (RelativeLayout) findViewById(R.id.RL_secretInvite);
        sendButton = (ImageView) findViewById(R.id.enter_chat1);
        View include = findViewById(R.id.chatHeader);
        header_rl = (RelativeLayout) include.findViewById(R.id.header_rl);
        header_center_rl = (RelativeLayout) include.findViewById(R.id.rl2);

        header_receiverName = (TextView) include.findViewById(R.id.headerReceiverName);
        profilePic = (FrameLayout) include.findViewById(R.id.profileImageChatScreen);
        tv = (TextView) include.findViewById(R.id.onlineStatus);

        dateView = (TextView) findViewById(R.id.dateView);


        dateView.setText(R.string.string_207);




        /*
         * For message header to be shown at the top,when message has been long pressed
         */
        messageHeader = findViewById(R.id.messageHelper);


        //  messageHelper_rl = (RelativeLayout) messageHeader.findViewById(R.id.messageHeader_rl);

        messageHeader.setVisibility(View.GONE);


        info = (ImageView) messageHeader.findViewById(R.id.info);
        ImageView delete = (ImageView) messageHeader.findViewById(R.id.delete);

        copy = (ImageView) messageHeader.findViewById(R.id.copy);
        ImageView back = (ImageView) messageHeader.findViewById(R.id.backButton);

        rlAttach = (RelativeLayout) findViewById(R.id.rlAttach);

        // messageHelper_rl.setVisibility(View.GONE);


        /*
         * To add the click listener for the message header items
         */


        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Integer.parseInt(currentlySelectedMessage.getDeliveryStatus()) > 0) {
                        Intent i = new Intent(SecretChatMessageScreen.this, MessageInfo.class);
                        i.putExtra("documentId", documentId);
                        i.putExtra("messageType", currentlySelectedMessage.getMessageType());
                        i.putExtra("messageId", currentlySelectedMessage.getMessageId());

                        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(i);
                    } else {


                        if (root != null) {


                            Snackbar snackbar = Snackbar.make(root, getString(R.string.NotSent), Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view2 = snackbar.getView();
                            TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                        }
                    }

                } catch (Exception e) {

                    Intent i = new Intent(SecretChatMessageScreen.this, MessageInfo.class);
                    i.putExtra("documentId", documentId);
                    i.putExtra("messageType", currentlySelectedMessage.getMessageType());
                    i.putExtra("messageId", currentlySelectedMessage.getMessageId());

                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);


                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int val = findMessagePosition(currentlySelectedMessage.getMessageId());


                if (val != -1) {

                    deleteMessage(val);


                }
            }
        });


        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", currentlySelectedMessage.getTextMessage());
                clipboard.setPrimaryClip(clip);

                Toast toast = Toast.makeText(SecretChatMessageScreen.this, "Message Copied", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibe.vibrate(200);


            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideMessageHeader();
            }
        });


        receiverNameHeader = (TextView) include.findViewById(R.id.receiverName);
        backButton = (ImageView) include.findViewById(R.id.backButton);

        attachment = (ImageView) findViewById(R.id.chatAttachment);


        ImageView more = (ImageView) include.findViewById(R.id.more);
        pic = (ImageView) include.findViewById(R.id.imv);
        //initiateCall = (ImageView) include.findViewById(R.id.initiateCall);
        initiateVideoCall = (ImageView) include.findViewById(R.id.initiateVideoCall);
        initiateAudioCall = (ImageView) include.findViewById(R.id.initiateAudioCall);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final View inflatedView;
        final TextView media_tv, delete_tv, lastseen_tv, wallpaper_tv;


        inflatedView = layoutInflater.inflate(R.layout.chat_custom_menu, null, false);


        media_tv = (TextView) inflatedView.findViewById(R.id.tv1);
        delete_tv = (TextView) inflatedView.findViewById(R.id.tv2);
        lastseen_tv = (TextView) inflatedView.findViewById(R.id.tv3);
        wallpaper_tv = (TextView) inflatedView.findViewById(R.id.tv4);
        Typeface tf = AppController.getInstance().getRegularFont();

        media_tv.setTypeface(tf, Typeface.NORMAL);
        delete_tv.setTypeface(tf, Typeface.NORMAL);
        lastseen_tv.setTypeface(tf, Typeface.NORMAL);
        wallpaper_tv.setTypeface(tf, Typeface.NORMAL);
        RelativeLayout history_rl = (RelativeLayout) inflatedView.findViewById(R.id.rl_media);

        RelativeLayout delete_rl = (RelativeLayout) inflatedView.findViewById(R.id.rl_delete);
        RelativeLayout last_rl = (RelativeLayout) inflatedView.findViewById(R.id.rl_last);


        RelativeLayout wallpaper_rl = (RelativeLayout) inflatedView.findViewById(R.id.rl_wallpaper);
        final ImageView lastSeenImage = (ImageView) inflatedView.findViewById(R.id.button_done);

        sendMessage = (EmojiconEditText) findViewById(R.id.chat_edit_text1);


        selEmoji = (ImageView) findViewById(R.id.emojiButton);


        sendSticker = (ImageView) findViewById(R.id.stickersbutton);


        /*
         * For audio record and sharing
         */

        final ImageView recordAudio = (ImageView) findViewById(R.id.recordAudio);
        slideText = findViewById(R.id.slideText);
        rl_recordStop = findViewById(R.id.rl_recordStop);
        ivDeleteRecord = findViewById(R.id.iv_Deleterecord);
        recordTimeText = (TextView) findViewById(R.id.recording_time_text);
        recordPanel = findViewById(R.id.record_panel);
        sendMessagePanel = (LinearLayout) findViewById(R.id.bottomLayoutInner);

        final View rootView = findViewById(R.id.mainRelativeLayout);


        if (rootView != null) {

            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FloatingView.dismissWindow();
                }
            });


        }
        include.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatingView.dismissWindow();
            }
        });
        final EmojiconsPopup popup = new EmojiconsPopup(rootView, this);
        drawable1 = ContextCompat.getDrawable(SecretChatMessageScreen.this, R.drawable.ic_chat_send);
        drawable2 = ContextCompat.getDrawable(SecretChatMessageScreen.this, R.drawable.ic_chat_send_active);
        sendButton.setImageDrawable(drawable1);


        userId = AppController.getInstance().getUserId();


        userName = AppController.getInstance().getUserName();
        db = AppController.getInstance().

                getDbController();
        setupWallpaperDialog();


        setUpActivity(getIntent());


        Typeface face = AppController.getInstance().getRegularFont();

        tv.setTypeface(face, Typeface.NORMAL);
        receiverNameHeader.setTypeface(face, Typeface.NORMAL);


        /* Registering click Listener*/


        popup.setSizeForSoftKeyboard();

        recordAudio.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                            .getLayoutParams();
                    params.leftMargin = dp(30);
                    slideText.setLayoutParams(params);
                    ViewProxy.setAlpha(slideText, 1);
                    startedDraggingX = -1;

                    startRecord();


                    recordAudio.getParent()
                            .requestDisallowInterceptTouchEvent(true);


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP
                        || motionEvent.getAction() == MotionEvent.ACTION_CANCEL) {
                    startedDraggingX = -1;


                    if (recordingAudio) {
                        stopRecord(true);
                    }

                } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {


                    try {


                        float x = motionEvent.getX();
                        if (x < -distCanMove) {
                            if (recordingAudio) {
                                stopRecord(false);
                            }

                        }


                        x = x + ViewProxy.getX(recordAudio);
                        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) slideText
                                .getLayoutParams();
                        if (startedDraggingX != -1) {
                            float dist = (x - startedDraggingX);
                            params.leftMargin = dp(30) + (int) dist;
                            slideText.setLayoutParams(params);
                            float alpha = 1.0f + dist / distCanMove;
                            if (alpha > 1) {
                                alpha = 1;
                            } else if (alpha < 0) {
                                alpha = 0;
                            }
                            ViewProxy.setAlpha(slideText, alpha);


                        }
                        if (x <= ViewProxy.getX(slideText) + slideText.getWidth()
                                + dp(30)) {


                            if (startedDraggingX == -1) {


                                startedDraggingX = x;
                                distCanMove = (recordPanel.getMeasuredWidth()
                                        - slideText.getMeasuredWidth() - dp(48)) / 2.0f;
                                if (distCanMove <= 0) {
                                    distCanMove = dp(80);
                                } else if (distCanMove > dp(80)) {
                                    distCanMove = dp(80);
                                }
                            }
                        }
                        if (params.leftMargin > dp(30)) {


                            params.leftMargin = dp(30);
                            slideText.setLayoutParams(params);
                            ViewProxy.setAlpha(slideText, 1);
                            startedDraggingX = -1;
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                view.onTouchEvent(motionEvent);
                return true;
            }
        });

        circleMenu.setOnItemClickListener(new Function1<Integer, Unit>() {
            @Override
            public Unit invoke(Integer integer) {
                Log.d("Selected_item","Selected" + integer);
                if(integer == 0){
                    if(userBlocked == 0){
                        onAttachCameraClick();
                    }
                } else if(integer == 1){
                    if(userBlocked == 0){
                        onAttachAlbumClick();
                    }
                }else if(integer == 2) {
                    if (userBlocked == 0) {
                        onAttachLocationClick();
                    }
                }else if(integer == 3) {
                    if (userBlocked == 0) {
//                    checkWalletAvailability();
                        checkIfCanStartRecord();
                    }
                }else if(integer == 4){

                }else if(integer == 5){
//                    checkIfCanStartRecord();
                }
                return null;
            }
        });

        rl_recordStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord(true);
            }
        });

        ivDeleteRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord(false);
            }
        });


        popup.setOnDismissListener(new PopupWindow.OnDismissListener() {

                                       @Override
                                       public void onDismiss() {


                                           selKeybord.setVisibility(View.GONE);
                                           selEmoji.setVisibility(View.VISIBLE);

                                       }
                                   }

        );


        popup.setOnSoftKeyboardOpenCloseListener(new EmojiconsPopup.OnSoftKeyboardOpenCloseListener() {

                                                     @Override
                                                     public void onKeyboardOpen(int keyBoardHeight) {

                                                     }

                                                     @Override
                                                     public void onKeyboardClose() {


                                                         if (popup.isShowing())
                                                             popup.dismiss();

                                                     }
                                                 }

        );


        popup.setOnEmojiconClickedListener(new EmojiconGridView.OnEmojiconClickedListener() {

                                               @Override
                                               public void onEmojiconClicked(Emojicon emojicon) {
                                                   if (sendMessage == null || emojicon == null) {
                                                       return;
                                                   }

                                                   int start = sendMessage.getSelectionStart();
                                                   int end = sendMessage.getSelectionEnd();
                                                   if (start < 0) {
                                                       sendMessage.append(emojicon.getEmoji());
                                                   } else {
                                                       sendMessage.getText().replace(Math.min(start, end),
                                                               Math.max(start, end), emojicon.getEmoji(), 0,
                                                               emojicon.getEmoji().length());
                                                   }
                                               }
                                           }

        );


        popup.setOnEmojiconBackspaceClickedListener(new EmojiconsPopup.OnEmojiconBackspaceClickedListener() {

                                                        @Override
                                                        public void onEmojiconBackspaceClicked(View v) {
                                                            KeyEvent event = new KeyEvent(
                                                                    0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
                                                            sendMessage.dispatchKeyEvent(event);
                                                        }
                                                    }

        );


        selEmoji.setOnClickListener(new View.OnClickListener() {

                                        @Override
                                        public void onClick(View v) {

                                            if (toddle.isKeyBoardOpen()) {
                                                toddle.dismiss();
                                            }

                                            if (rlAttach.getVisibility() == View.VISIBLE)
                                                rlAttach.setVisibility(View.GONE);

                                            selKeybord.setVisibility(View.VISIBLE);
                                            selEmoji.setVisibility(View.GONE);


                                            if (!popup.isShowing()) {


                                                if (popup.isKeyBoardOpen()) {
                                                    popup.showAtBottom();

                                                } else {
                                                    sendMessage.setFocusableInTouchMode(true);
                                                    sendMessage.requestFocus();
                                                    popup.showAtBottomPending();
                                                    final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                    inputMethodManager.showSoftInput(sendMessage, InputMethodManager.SHOW_IMPLICIT);

                                                }
                                            } else {
                                                popup.dismiss();
                                            }


                                        }
                                    }

        );


        selKeybord.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {


                                              selKeybord.setVisibility(View.GONE);
                                              selEmoji.setVisibility(View.VISIBLE);


                                              if (!popup.isShowing()) {


                                                  sendMessage.setFocusableInTouchMode(true);
                                                  sendMessage.requestFocus();
                                                  popup.showAtBottomPending();
                                                  final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                  inputMethodManager.showSoftInput(sendMessage, InputMethodManager.SHOW_IMPLICIT);

                                              } else {
                                                  popup.dismiss();

                                              }

                                          }
                                      }

        );


        toddle = new DoodlePop(rootView, SecretChatMessageScreen.this, new DoodleAction() {
            @Override
            public void doodleBitmap(Bitmap bitmap) {

                Uri uri = null;
                String id = null;

                if (bitmap != null) {
                    try {


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, baos);


                        byte[] br = baos.toByteArray();
                        createDoodleUri(br);
                        try {
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        baos = null;

                        //   b = compress(b);


                        id = new Utilities().gmtToEpoch(Utilities.tsInGmt());

                        File f = convertByteArrayToFile(br, id, ".jpg");
                        br = null;

                        uri = Uri.fromFile(f);
                        f = null;


                    } catch (
                            OutOfMemoryError e) {


                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, getString(R.string.doodle_failed), Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }


                    }


                    if (uri != null) {


                        /*
                         *
                         *
                         * make thumbnail
                         *
                         * */


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        bitmap.compress(Bitmap.CompressFormat.JPEG, 1, baos);


                        byte[] br = baos.toByteArray();

                        try {
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        baos = null;


                        addMessageToSendInUi(setMessageToSend(true, 7, id, Base64.encodeToString(br, Base64.DEFAULT).

                                trim()), true, 7, uri, false, false, true);


                        uri = null;
                        br = null;
                        bitmap = null;
                    }


                } else {
                    if (root != null) {
                        Snackbar snackbar = Snackbar.make(root, getString(R.string.doodle_failed), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                }


            }

            @Override
            public void keyboardOpen() {


            }

            @Override
            public void KeyboardClose() {

                toddle.dismiss();

            }


        });

        toddle.setSizeForSoftKeyboard();


        sendMessage.addTextChangedListener(new

                                                   TextWatcher() {

                                                       public void afterTextChanged(Editable s) {


                                                       }

                                                       public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                                       }

                                                       public void onTextChanged(CharSequence s, int start, int before, int count) {


                                                           if (s.length() > 0 && !(s.toString().equals(" "))) {
                                                               sendSticker.setVisibility(View.GONE);
                                                               doodle.setVisibility(View.GONE);

                                                               recordAudio.setVisibility(View.GONE);

                                                               sendButton.setVisibility(View.VISIBLE);
                                                               circleMenu.setVisibility(View.GONE);
                                                               sendButton.setImageDrawable(drawable2);

                                                               button01pos = 1;

                                                               if (opponentOnline) {

                                                                   JSONObject obj = new JSONObject();
                                                                   try {
                                                                       obj.put("from", userId);
                                                                   } catch (JSONException e) {
                                                                       e.printStackTrace();
                                                                   }

                                                                   //                                                                   AppController.getInstance().publish(MqttEvents.Typing.value+typingTopic, obj, 0, fal
                                                                   AppController.getInstance()
                                                                           .publish(MqttEvents.Typing.value + userId,
                                                                                   obj, 0, false);
                                                                   obj = null;
                                                               }
                                                           } else {
                                                               recordAudio.setVisibility(View.GONE);
                                                               sendButton.setVisibility(View.GONE);

                                                               InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                                               if (imm.isAcceptingText()) {
//                                                                   writeToLog("Software Keyboard was shown");
                                                                   circleMenu.setVisibility(View.GONE);
                                                               } else {
//                                                                   writeToLog("Software Keyboard was not shown");
                                                                   if (s.length() > 0 && !(s.toString().equals(" "))) {
                                                                       circleMenu.setVisibility(View.VISIBLE);                                                                   }
                                                               }
                                                               Log.v("Circlemenu2","circlemenuKB");
                                                           }
                                                       }
                                                   });


        sendButton.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("unchecked")
            @Override
            public void onClick(View v) {
                if (sendMessage.getText().toString().trim().length() > 0) {
                    if (MessageType == 0) {
                        addMessageToSendInUi(setMessageToSend(false, 0, null, null), false, 0, null, false, false, true);
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (root != null) {
                                Snackbar snackbar = Snackbar.make(root, R.string.string_46, Snackbar.LENGTH_SHORT);
                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }
                        }
                    });
                }
            }
        });


        openDialog();

        attachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                toddle.dismiss();
                if (rlAttach.getVisibility() == View.VISIBLE)
                    rlAttach.setVisibility(View.GONE);
                else
                    rlAttach.setVisibility(View.VISIBLE);
                //openDialog();
                hideKeyboard();

            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                onBackPressed();


            }
        });


        doodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(SecretChatMessageScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

                    showDoodlePopup();


                } else {
                    requestReadImagePermission(2);

                }


            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (blockedTv.getVisibility() == View.GONE) {
                    openDTimeDialogBox();
                }
            }
        });


        header_center_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(SecretChatMessageScreen.this, OpponentProfileNew.class);
                i.putExtra("contactId", receiverUid);
                i.putExtra("documentId", documentId);
                i.putExtra("secretId", secretId);
                i.putExtra("isStar", isStar);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                i.putExtra("blocked", blockedTv.getVisibility() == View.VISIBLE);
                startActivity(i);
            }
        });


        header_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent i = new Intent(SecretChatMessageScreen.this, OpponentProfileNew.class);
                i.putExtra("contactId", receiverUid);
                i.putExtra("documentId", documentId);
                i.putExtra("secretId", secretId);
                i.putExtra("isStar", isStar);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                i.putExtra("blocked", blockedTv.getVisibility() == View.VISIBLE);
                startActivity(i);
            }
        });


        recyclerView_chat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (llm.findFirstVisibleItemPosition() >= 0)


                            dateView.setText(findOverlayDate(mChatData.get(llm.findFirstVisibleItemPosition()).getMessageDateOverlay()));


                    }
                });


                if (llm.findFirstVisibleItemPosition() == 0) {


                    if (firstTenLoaded) {


                        if (size != 0) {
                            loadTenMore();
                        } else {

                            if (hasChatId && canHaveMoreMessages) {


                                if (pendingApiCalls == 0 && dy != 0) {


                                    retrieveChatMessage(MESSAGE_PAGE_SIZE);
                                }
                            }
                        }


                    } else {

                        if (!limit) {


                            limit = true;
                        }


                    }

                }


            }
        });

        history_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatingView.dismissWindow();

                Intent j = new Intent(SecretChatMessageScreen.this, MediaHistory.class);
                j.putExtra("docId", documentId);

                j.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(j, ActivityOptionsCompat.makeSceneTransitionAnimation(SecretChatMessageScreen.this).toBundle());


            }
        });


        delete_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatingView.dismissWindow();

                AlertDialog.Builder builder =
                        new AlertDialog.Builder(SecretChatMessageScreen.this, 0);
                builder.setTitle(R.string.string_369);
                builder.setMessage(getString(R.string.string_558) + " " + receiverName + "?");
                builder.setPositiveButton(R.string.string_584, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {


                        AppController.getInstance().getDbController().deleteChat(documentId);


                        mChatData.clear();


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

//                        mAdapter.notifyItemInserted(mProductData.size() - 1);


                                mAdapter.notifyDataSetChanged();


                            }
                        });


                        Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                        if (context instanceof Activity) {


                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                    dialog.dismiss();
                                }
                            } else {


                                if (!((Activity) context).isFinishing()) {
                                    dialog.dismiss();
                                }
                            }
                        } else {


                            try {
                                dialog.dismiss();
                            } catch (final IllegalArgumentException e) {
                                e.printStackTrace();

                            } catch (final Exception e) {
                                e.printStackTrace();

                            }
                        }


                    }
                });


                builder.setNegativeButton(R.string.string_593, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });


                builder.show();
            }
        });

        last_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatingView.dismissWindow();
                allowLastSeen = !allowLastSeen;


                AppController.getInstance().updateLastSeenSettings(allowLastSeen);


                AppController.getInstance().updatePresence(1, false);


            }
        });

        wallpaper_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatingView.dismissWindow();

                showWallpaperSheet();

            }
        });


        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popup.dismiss();
                toddle.dismiss();


                if (allowLastSeen) {

                    lastseen_tv.setText(getString(R.string.string_692));
                    lastSeenImage.setImageResource(R.drawable.cancel_normal);
                } else {
                    lastseen_tv.setText(getString(R.string.string_691));
                    lastSeenImage.setImageResource(R.drawable.ic_done_all_black_24px);
                }

                FloatingView.onShowPopup(SecretChatMessageScreen.this, inflatedView);

            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (rlAttach.getVisibility() == View.VISIBLE) rlAttach.setVisibility(View.GONE);
                toddle.dismiss();
                popup.dismiss();

                showKeyBoard();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if (imm.isAcceptingText()) {
//                    writeToLog("Software Keyboard was shown");
                    circleMenu.setVisibility(View.GONE);
                } else {
//                    writeToLog("Software Keyboard was not shown");
                    circleMenu.setVisibility(View.VISIBLE);
                }
            }
        });


        sendMessage.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (rlAttach.getVisibility() == View.VISIBLE)
                    rlAttach.setVisibility(View.GONE);
                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // recyclerView_chat.scrollToPosition(mChatData.size() - 1);


                            //   llm.scrollToPositionWithOffset(mChatData.size() - 1, 0);
                            if (mChatData != null) {
                                llm.scrollToPositionWithOffset(mChatData.size() - 1, 0);
                            }
                        }
                    }, 500);

                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        recyclerView_chat.addOnItemTouchListener(new RecyclerItemClickListener(SecretChatMessageScreen.this, recyclerView_chat, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                if (position >= 0) {
                    final SecretChatMessageItem item = mChatData.get(position);

                    /*
                     * To allow unselecting of the item,by clicking on the already selected message
                     */
                    try {
                        if (item.isSelected()) {
                            hideMessageHeader();
                            item.setSelected(false);

                            mChatData.set(position, item);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyItemChanged(position);
                                }
                            });


                        }

                        /*
                         *
                         * If message header is already visible
                         */
                        else if (messageHeader.getVisibility() == View.VISIBLE) {
                            hideMessageHeader();
                            /*
                             * Setting the previously selected message as unselected
                             */


                            final int pos = findMessagePosition(currentlySelectedMessage.getMessageId());

                            currentlySelectedMessage.setSelected(false);

                            mChatData.set(pos, currentlySelectedMessage);


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyItemChanged(pos);
                                }
                            });


                        }

                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                    if (item.getMessageType().equals("3")) {


//                        RecyclerView.ViewHolder vh;
//
//                        if (item.isSelf()) {
//                                      vh = (ViewHolderLocationSent) recyclerView_chat.getChildViewHolder(view);
//                        } else {
//                            vh = (ViewHolderLocationReceived) recyclerView_chat.getChildViewHolder(view).;
//                        }


                        final String args[] = item.getPlaceInfo().split("@@");


                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(SecretChatMessageScreen.this, 0);

                        LayoutInflater inflater = LayoutInflater.from(SecretChatMessageScreen.this);
                        final View dialogView = inflater.inflate(R.layout.location_popup, null);


                        builder.setView(dialogView);


                        TextView name = (TextView) dialogView.findViewById(R.id.Name);

                        TextView address = (TextView) dialogView.findViewById(R.id.Address);

                        TextView latlng = (TextView) dialogView.findViewById(R.id.LatLng);


                        name.setText(getString(R.string.string_346) + " " + args[1]);
                        address.setText(getString(R.string.string_347) + " " + args[2]);
                        latlng.setText(getString(R.string.string_348) + " " + args[0]);


                        builder.setTitle(R.string.string_395);


                        builder.setPositiveButton(R.string.string_581, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {


                                try {


                                    String LatLng = args[0];

                                    String[] parts = LatLng.split(",");

                                    String lat = parts[0].substring(1);
                                    String lng = parts[1].substring(0, parts[1].length() - 1);


                                    String uri = "geo:" + lat + ","
                                            + lng + "?q=" + lat
                                            + "," + lng;
                                    startActivity(new Intent(Intent.ACTION_VIEW,
                                                    Uri.parse(uri)),
                                            ActivityOptionsCompat.makeSceneTransitionAnimation(SecretChatMessageScreen.this).toBundle());

                                    uri = null;
                                    lat = null;
                                    lng = null;
                                    parts = null;
                                    LatLng = null;


                                } catch (ActivityNotFoundException e) {
                                    if (root != null) {

                                        Snackbar snackbar = Snackbar.make(root, R.string.string_34, Snackbar.LENGTH_SHORT);


                                        snackbar.show();
                                        View view2 = snackbar.getView();
                                        TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                    }
                                }


                                //  dialog.dismiss();


                                Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                                if (context instanceof Activity) {


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                            dialog.dismiss();
                                        }
                                    } else {


                                        if (!((Activity) context).isFinishing()) {
                                            dialog.dismiss();
                                        }
                                    }
                                } else {


                                    try {
                                        dialog.dismiss();
                                    } catch (final IllegalArgumentException e) {
                                        e.printStackTrace();

                                    } catch (final Exception e) {
                                        e.printStackTrace();

                                    }
                                }


                            }
                        });
                        builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {


                                dialog.cancel();

                            }
                        });
                        locationDialog = builder.show();


                    }

//                    else if (item.getMessageType().equals("4") && !(item.isSelf())) {
//
//
//                        checkWriteContactPermission(item.getContactInfo());
//
//                    }
                }
            }

            @Override
            public void onItemLongClick(View view, final int position) {

                /*
                 *
                 * If message header is already visible
                 */

                try {

                    if (messageHeader.getVisibility() == View.VISIBLE) {
                        hideMessageHeader();
                        /*
                         * Setting the previously selected message as unselected
                         */


                        final int pos = findMessagePosition(currentlySelectedMessage.getMessageId());

                        currentlySelectedMessage.setSelected(false);

                        mChatData.set(pos, currentlySelectedMessage);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyItemChanged(pos);
                            }
                        });


                    }


                    currentlySelectedMessage = mChatData.get(position);


                    /*
                     * To avoid clicking on the loading item
                     */


                    if (!currentlySelectedMessage.getMessageType().equals("99") && !currentlySelectedMessage.isDTimeTag()) {


                        /*
                         * Setting the currently long pressed item as selected
                         */


                        showMessageHeader(currentlySelectedMessage.getMessageType(), currentlySelectedMessage);

                        currentlySelectedMessage.setSelected(true);

                        mChatData.set(position, currentlySelectedMessage);


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyItemChanged(position);
                            }
                        });


                    }


                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
            }
        }));


        sendSticker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SecretChatMessageScreen.this, SelectGIF.class);
//                intent.putExtra("getStickers", true);
//                startActivityForResult(intent, RESULT_LOAD_STICKER);


                if (!AppController.getInstance().isActiveOnACall()) {
                    checkCameraPermissionImage(0);
                } else {

                    if (root != null) {
                        Snackbar snackbar = Snackbar.make(root, getString(R.string.call_camera), Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view2 = snackbar.getView();
                        TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                }


            }
        });


       /* initiateCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.System.canWrite(SecretChatMessageScreen.this) || !Settings.canDrawOverlays(SecretChatMessageScreen.this)) {
                        if (!Settings.System.canWrite(SecretChatMessageScreen.this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        }


                        //If the draw over permission is not available open the settings screen
                        //to grant the permission.

                        if (!Settings.canDrawOverlays(SecretChatMessageScreen.this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        }


                    } else {


                        showCallTypeChooserPopup(view);
                    }
                } else {
                    showCallTypeChooserPopup(view);

                }
            }
        });*/

        initiateAudioCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppController.getInstance().isActiveOnACall() || AppController.getInstance().canPublish()) {
                    AppController.getInstance().setActiveOnACall(true, true);
                    Intent intent = new Intent(SecretChatMessageScreen.this, CallingActivity.class);
                    intent.putExtra(USER_NAME, receiverName);
                    intent.putExtra(USER_IMAGE, receiverImage);
                    intent.putExtra(USER_ID, receiverUid);
                    intent.putExtra(CALL_STATUS, CallStatus.CALLING);
                    intent.putExtra(CALL_TYPE, AUDIO);
                    intent.putExtra(CALL_ID, "");
                    intent.putExtra(ROOM_ID, "0");
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),getString(R.string.calling_service_not_available),Toast.LENGTH_LONG).show();
                }

                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(SecretChatMessageScreen.this)) {
                        if (!Settings.System.canWrite(SecretChatMessageScreen.this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        }


                        //If the draw over permission is not available open the settings screen
                        //to grant the permission.

                        if (!Settings.canDrawOverlays(SecretChatMessageScreen.this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        }
                    } else {


                        startAudioCall(v);
                    }
                } else {
                    startAudioCall(v);

                }*/
            }
        });

        initiateVideoCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppController.getInstance().isActiveOnACall() || AppController.getInstance().canPublish()) {
                    AppController.getInstance().setActiveOnACall(true, true);
                    Intent intent = new Intent(SecretChatMessageScreen.this, CallingActivity.class);
                    intent.putExtra(USER_NAME, receiverName);
                    intent.putExtra(USER_IMAGE, receiverImage);
                    intent.putExtra(USER_ID, receiverUid);
                    intent.putExtra(CALL_STATUS, CallStatus.CALLING);
                    intent.putExtra(CALL_TYPE, VIDEO);
                    intent.putExtra(CALL_ID, "");
                    intent.putExtra(ROOM_ID, "0");
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),getString(R.string.calling_service_not_available),Toast.LENGTH_LONG).show();
                }
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(SecretChatMessageScreen.this)) {
                        if (!Settings.System.canWrite(SecretChatMessageScreen.this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                            intent.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        }


                        //If the draw over permission is not available open the settings screen
                        //to grant the permission.

                        if (!Settings.canDrawOverlays(SecretChatMessageScreen.this)) {
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivity(intent);
                        }
                    } else {


                        startVideoCall(v);
                    }
                } else {
                    startVideoCall(v);

                }*/
            }
        });


        /*
         * For handling open from the notification
         */
        try {
            db.updateChatListOnViewingMessage(documentId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateTypefaces();
        bus.register(this);
        sendMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.v("sendMessage",String.valueOf(hasFocus));
                if (hasFocus) {
                    circleMenu.setVisibility(View.GONE);
                    //got focus
                } else {
                    //lost focus
                }
            }
        });
    }

    @Override
    protected void onResume() {


        super.onResume();


        if (sendMessage.getText().length() == 1)

            sendMessage.setText("");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            if (!Settings.System.canWrite(SecretChatMessageScreen.this)) {
//                Intent intent =
//                        new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//                intent.setData(Uri.parse("package:" + getPackageName()));
//                startActivity(intent);
//            }
//        }
        if (hasPendingAcknowledgement) {

            for (int i = mChatData.size() - 1; i >= 0; i--) {

                if (!mChatData.get(i).isSelf()) {

                    SecretChatMessageItem item = mChatData.get(i);

                    createObjectToSend(item.getMessageId(), item.getReceiverUid(), item.getdTime());
                    break;
                }
            }

            hasPendingAcknowledgement = false;
            CouchDbController db = AppController.getInstance().getDbController();
            db.updateChatListOnViewingMessage(documentId);
        }
    }

    /*
     * @param isImageOrVideoOrAudio to check if message is image/video or audio in which case it is to be uploaded to the server
     * @param messageType           messageType
     * @param id                    messageId
     * @param thumbnail             thumbnail of message in case of image or video message
     * @return JSONObject containing details of the message to be emitted on socket
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    private JSONObject setMessageToSend(boolean isImageOrVideoOrAudio, int messageType, String
            id, String thumbnail) {
        JSONObject obj = new JSONObject();


        if (id == null) {
            tsForServer = Utilities.tsInGmt();
            tsForServerEpoch = new Utilities().gmtToEpoch(tsForServer);
        } else {
            tsForServerEpoch = id;

            tsForServer = Utilities.epochtoGmt(id);
        }


        if (!isImageOrVideoOrAudio) {
            /*
             *
             * normal text message so payload field is set as well
             *
             * */

            if (messageType == 0) {

                /*
                 * Text message
                 */


                try {

                    byte[] byteArray = sendMessage.getText().toString().trim().getBytes("UTF-8");

                    String messageInbase64 = Base64.encodeToString(byteArray, Base64.DEFAULT).trim();


                    /*
                     * Have been commented out intentionally
                     */
//                    if (messageInbase64.isEmpty()) {
//                        messageInbase64 = " ";
//                    }

                    obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());
                    obj.put("from", AppController.getInstance().getUserId());
                    obj.put("to", receiverUid);
                    obj.put("payload", messageInbase64);
                    obj.put("toDocId", documentId);
                    obj.put("timestamp", tsForServerEpoch);

                    obj.put("id", tsForServerEpoch);
                    obj.put("type", "0");

                    obj.put("name", AppController.getInstance().getUserName());


                    /*
                     * For secret chat exclusively
                     */

                    obj.put("secretId", secretId);
                    obj.put("dTime", dTime);

                    messageInbase64 = null;
                    byteArray = null;


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }

            }


            /*
             *
             * location message so payload field is set as well
             *
             * */


            else if (messageType == 3) {


                /*
                 * Location
                 */


                try {


                    String MessageInbase64 = Base64.encodeToString(placeString.getBytes("UTF-8"), Base64.DEFAULT);

                    obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());
                    obj.put("from", AppController.getInstance().getUserId());
                    obj.put("to", receiverUid);
                    obj.put("payload", MessageInbase64.trim());
                    obj.put("toDocId", documentId);

                    obj.put("timestamp", tsForServerEpoch);
                    obj.put("id", tsForServerEpoch);
                    obj.put("type", "3");

                    obj.put("name", AppController.getInstance().getUserName());


                    /*
                     * For secret chat exclusively
                     */

                    obj.put("secretId", secretId);
                    obj.put("dTime", dTime);
                    MessageInbase64 = null;

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }

            }


            /*
             *
             * contact message so payload field is set as well
             *
             * */


            else if (messageType == 4) {

                /*
                 * Contacts
                 */
                try {


                    String MessageInbase64 = Base64.encodeToString(contactInfo.getBytes("UTF-8"), Base64.DEFAULT);
                    obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());

                    obj.put("from", AppController.getInstance().getUserId());
                    obj.put("to", receiverUid);
                    obj.put("payload", MessageInbase64.trim());
                    obj.put("toDocId", documentId);

                    obj.put("timestamp", tsForServerEpoch);
                    obj.put("id", tsForServerEpoch);
                    obj.put("type", "4");
                    obj.put("name", AppController.getInstance().getUserName());

                    /*
                     * For secret chat exclusively
                     */

                    obj.put("secretId", secretId);
                    obj.put("dTime", dTime);

                    MessageInbase64 = null;
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }


            } else if (messageType == 8) {
                /*
                 * Gifs
                 */

                try {


                    String messageInbase64 = Base64.encodeToString(gifUrl.trim().getBytes("UTF-8"), Base64.DEFAULT).trim();


                    if (messageInbase64.isEmpty()) {
                        messageInbase64 = " ";
                    }


                    /*
                     * Has removed the thumbnail key for now,which was added for the ios
                     */
                    obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());
                    obj.put("from", AppController.getInstance().getUserId());
                    obj.put("to", receiverUid);
                    obj.put("payload", messageInbase64);
                    obj.put("toDocId", documentId);
                    obj.put("timestamp", tsForServerEpoch);
                    obj.put("id", tsForServerEpoch);
                    obj.put("type", "8");
                    // obj.put("thumbnail", thumbnail);
                    obj.put("name", AppController.getInstance().getUserName());

                    /*
                     * For secret chat exclusively
                     */

                    obj.put("secretId", secretId);
                    obj.put("dTime", dTime);
                    messageInbase64 = null;


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }

            } else if (messageType == 6) {


                /*
                 * Stickers
                 */

                try {


                    String messageInbase64 = Base64.encodeToString(stickerUrl.trim().getBytes("UTF-8"), Base64.DEFAULT).trim();


                    if (messageInbase64.isEmpty()) {
                        messageInbase64 = " ";
                    }

                    obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());
                    obj.put("from", AppController.getInstance().getUserId());
                    obj.put("to", receiverUid);
                    obj.put("payload", messageInbase64);
                    obj.put("toDocId", documentId);
                    obj.put("timestamp", tsForServerEpoch);
                    obj.put("id", tsForServerEpoch);
                    obj.put("type", "6");

                    obj.put("name", AppController.getInstance().getUserName());
                    /*
                     * For secret chat exclusively
                     */

                    obj.put("secretId", secretId);
                    obj.put("dTime", dTime);

                    messageInbase64 = null;


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {

                    e.printStackTrace();
                }


            }


        } else {

            /*
             *
             *
             * image message so payload field is not set
             *
             * */

            if (messageType == 1) {
                try {

                    obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());
                    obj.put("from", AppController.getInstance().getUserId());
                    obj.put("to", receiverUid);

                    obj.put("toDocId", documentId);
                    obj.put("timestamp", tsForServerEpoch);

                    obj.put("id", tsForServerEpoch);
                    obj.put("type", "1");
                    obj.put("name", AppController.getInstance().getUserName());
                    obj.put("thumbnail", thumbnail);

                    /*
                     * For secret chat exclusively
                     */

                    obj.put("secretId", secretId);
                    obj.put("dTime", dTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            /*
             *
             *
             * video message so payload field is not set
             *
             * */


            else if (messageType == 2) {
                try {
                    obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());
                    obj.put("from", AppController.getInstance().getUserId());
                    obj.put("to", receiverUid);

                    obj.put("toDocId", documentId);
                    obj.put("timestamp", tsForServerEpoch);

                    obj.put("id", tsForServerEpoch);
                    obj.put("type", "2");
                    obj.put("name", AppController.getInstance().getUserName());
                    obj.put("thumbnail", thumbnail);

                    /*
                     * For secret chat exclusively
                     */

                    obj.put("secretId", secretId);
                    obj.put("dTime", dTime);
                } catch (JSONException e) {
                    e.printStackTrace();


                }

            }


            /*
             *
             *
             * audio message so payload field is not set
             *
             * */


            else if (messageType == 5) {
                try {
                    obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());
                    obj.put("from", AppController.getInstance().getUserId());
                    obj.put("to", receiverUid);

                    obj.put("toDocId", documentId);
                    obj.put("timestamp", tsForServerEpoch);

                    obj.put("id", tsForServerEpoch);
                    obj.put("type", "5");
                    obj.put("name", AppController.getInstance().getUserName());

                    /*
                     * For secret chat exclusively
                     */

                    obj.put("secretId", secretId);
                    obj.put("dTime", dTime);
                } catch (JSONException e) {
                    e.printStackTrace();


                }

            } else if (messageType == 7) {

                /*
                 * Doodle
                 */
                try {
                    obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());

                    obj.put("from", AppController.getInstance().getUserId());
                    obj.put("to", receiverUid);

                    obj.put("toDocId", documentId);
                    obj.put("timestamp", tsForServerEpoch);


                    obj.put("id", tsForServerEpoch);
                    obj.put("type", "7");

                    obj.put("thumbnail", thumbnail);
                    obj.put("name", AppController.getInstance().getUserName());


                    /*
                     * For secret chat exclusively
                     */

                    obj.put("secretId", secretId);
                    obj.put("dTime", dTime);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }


        return obj;

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {


        outState.putParcelable("file_uri", imageUri);

        outState.putString("file_path", picturePath);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable("file_uri");

            picturePath = savedInstanceState.getString("file_path");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }


    /*
     * To update the last seen status in action bar when notified by server of some change in status of receiver(with whom user is chatting)
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RESULT_SHARE_LOCATION && resultCode == RESULT_OK && null != data) {


            //Place place = PlacePicker.getPlace(SecretChatMessageScreen.this, data);
            try {

                String latlng = data.getStringExtra("latLong");
                String address = data.getStringExtra("address");
                String name = "";

                if (name.length() != 0 && name.charAt(0) == '(') {
                    String[] parts = name.split(",");
                    name = parts[0].trim() + "," + parts[1].trim();

                }


                if (latlng != null) {


                    placeString = latlng.substring(9) + "@@";
                    //  sendMessage.setText(getString(R.string.string_349) + " " + latlng);
                }


                if (latlng != null && name.equals(latlng.substring(9))) {


                    placeString = placeString + "Not Applicable" + "@@";

                } else {


                    placeString = placeString + name + "@@";
                    //   sendMessage.setText(getString(R.string.string_350) + " " + name);
                }


                if (address.isEmpty()) {


                    placeString = placeString + "Not Applicable";


                } else {

                    placeString = placeString + address;


                }


                addMessageToSendInUi(setMessageToSend(false, 3, null, null), false, 3, null, false, false, true);


                latlng = null;

                name = null;
                address = null;
            } catch (NullPointerException e) {


                Snackbar snackbar = Snackbar.make(root, R.string.string_600,
                        Snackbar.LENGTH_SHORT);
                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

            } catch (StringIndexOutOfBoundsException e) {


                Snackbar snackbar = Snackbar.make(root, R.string.string_600,
                        Snackbar.LENGTH_SHORT);
                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

            }
            //place = null;


        }

//        else if (requestCode == RESULT_LOAD_IMAGE) {
//
//            if (resultCode == RESULT_OK && null != data) {
//                picturePath = getPath(this, data.getData());
//
//                imageUrl = data.getData();
//
//                Uri uri = null;
//                Bitmap bm = null;
//                String id = null;
//
//                try {
//
//                    final BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inJustDecodeBounds = true;
//                    BitmapFactory.decodeFile(picturePath, options);
//
//
//                    int height = options.outHeight;
//                    int width = options.outWidth;
//
//                    float density = getResources().getDisplayMetrics().density;
//                    int reqHeight;
//
//
//                    if (width != 0) {
//                        reqHeight = (int) ((150 * density) * (height / width));
//
//                        bm = decodeSampledBitmapFromResource(picturePath, (int) (150 * density), reqHeight);
//
//                        if (bm != null) {
//
//
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//                            bm.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, baos);
//
//
//                            // bm = null;
//                            byte[] b = baos.toByteArray();
//
//                            try {
//                                baos.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            baos = null;
//
//                            //   b = compress(b);
//
//
//                            id = new Utilities().gmtToEpoch(Utilities.tsInGmt());
//
//                            File f = convertByteArrayToFile(b, id, ".jpg");
//                            b = null;
//
//                            uri = Uri.fromFile(f);
//                            f = null;
//
//
//                        } else {
//                            if (root != null) {
//
//                                Snackbar snackbar = Snackbar.make(root, R.string.string_48, Snackbar.LENGTH_SHORT);
//
//
//                                snackbar.show();
//                                View view = snackbar.getView();
//                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                            }
//                        }
//
//
//                    } else {
//                        if (root != null) {
//
//                            Snackbar snackbar = Snackbar.make(root, R.string.string_48, Snackbar.LENGTH_SHORT);
//
//
//                            snackbar.show();
//                            View view = snackbar.getView();
//                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                        }
//                    }
//
//
//                } catch (OutOfMemoryError e) {
//                    e.printStackTrace();
//
//                    if (root != null) {
//
//                        Snackbar snackbar = Snackbar.make(root, R.string.string_49, Snackbar.LENGTH_SHORT);
//
//
//                        snackbar.show();
//                        View view = snackbar.getView();
//                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                    }
//
//
//                }
//
//
//                if (uri != null) {
//
//
//                /*
//                 *
//                 *
//                 * make thumbnail
//                 *
//                 * */
//
//
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//                    bm.compress(Bitmap.CompressFormat.JPEG, 1, baos);
//
//
//                    bm = null;
//                    byte[] b = baos.toByteArray();
//
//                    try {
//                        baos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    baos = null;
//
//
//                    addMessageToSendInUi(setMessageToSend(true, 1, id,
//                            Base64.encodeToString(b, Base64.DEFAULT).trim()), true, 1, uri, false, false, true);
//                    uri = null;
//                    b = null;
//                    bm = null;
//                }
//            } else {
//
//
//                if (resultCode == Activity.RESULT_CANCELED) {
//
//
//                    Snackbar snackbar = Snackbar.make(root, R.string.string_16, Snackbar.LENGTH_SHORT);
//
//
//                    snackbar.show();
//                    View view = snackbar.getView();
//                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//
//                } else {
//
//
//                    Snackbar snackbar = Snackbar.make(root, R.string.string_113, Snackbar.LENGTH_SHORT);
//
//
//                    snackbar.show();
//                    View view = snackbar.getView();
//                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//
//                }
//
//            }
//
//        }
//

        else if (requestCode == RESULT_EDIT_IMAGE) {

            if (resultCode == RESULT_OK) {
                Uri uri = null;
                String id = null;
                Bitmap bm = null;

                picturePath = data.getStringExtra("imagePath");
                try {

                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(picturePath, options);


                    int height = options.outHeight;
                    int width = options.outWidth;

                    float density = getResources().getDisplayMetrics().density;
                    int reqHeight;


                    if (width != 0) {


                        reqHeight = (int) ((150 * density) * (height / width));


                        bm = decodeSampledBitmapFromResource(picturePath, (int) (150 * density), reqHeight);


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();


                        if (bm != null) {

                            bm.compress(Bitmap.CompressFormat.JPEG, IMAGE_CAPTURED_QUALITY, baos);
                            //bm = null;
                            byte[] b = baos.toByteArray();
                            try {
                                baos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            baos = null;
//                b = compress(b);


                            id = new Utilities().gmtToEpoch(Utilities.tsInGmt());

                            File f = convertByteArrayToFile(b, id, ".jpg");
                            b = null;

                            uri = Uri.fromFile(f);
                            f = null;


                        } else {


                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, R.string.string_50, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }


                        }


                    } else {


                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, R.string.string_50, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    }


                } catch (OutOfMemoryError e) {
                    e.printStackTrace();


                    if (root != null) {

                        Snackbar snackbar = Snackbar.make(root, R.string.string_49, Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }


                }


                if (uri != null) {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    bm.compress(Bitmap.CompressFormat.JPEG, 1, baos);


                    bm = null;
                    byte[] b = baos.toByteArray();

                    try {
                        baos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    baos = null;


                    addMessageToSendInUi(setMessageToSend(true, 1, id, Base64.encodeToString(b,
                            Base64.DEFAULT).trim()), true, 1, uri, false, false, true);
                    uri = null;
                    b = null;
                    bm = null;

                }
            } else {


                if (resultCode == Activity.RESULT_CANCELED) {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_18, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                } else {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_17, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                }

            }

        } else if (requestCode == RESULT_CAPTURE_MEDIA) {

            if (resultCode == RESULT_OK) {
                sendCapturedImage(data.getStringExtra("imagePath"), data.getBooleanExtra("isImage", true));


            } else {
                if (resultCode == Activity.RESULT_CANCELED) {


                    Snackbar snackbar = Snackbar.make(root, R.string.media_capture_canceled, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                } else {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_17, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                }
            }

        } else if (requestCode == RESULT_LOAD_VIDEO && resultCode == RESULT_OK && null != data) {

            Uri uri = null;
            String id = null;
            try {


                videoPath = getPath(SecretChatMessageScreen.this, data.getData());


                File video = new File(videoPath);

                if (video.length() <= (MAX_VIDEO_SIZE)) {

                    try {


                        byte[] b = convertFileToByteArray(video);
                        video = null;
                        //        b = compress(b);


                        id = new Utilities().gmtToEpoch(Utilities.tsInGmt());

                        File f = convertByteArrayToFile(b, id, ".mp4");
                        b = null;

                        uri = Uri.fromFile(f);
                        f = null;

                        b = null;


                    } catch (OutOfMemoryError e) {

                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, R.string.string_51, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }


                    }

                    if (uri != null) {


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Bitmap bm = ThumbnailUtils.createVideoThumbnail(videoPath,
                                MediaStore.Images.Thumbnails.MINI_KIND);

                        if (bm != null) {

                            bm.compress(Bitmap.CompressFormat.JPEG, 1, baos);
                            bm = null;
                            byte[] b = baos.toByteArray();
                            try {
                                baos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            baos = null;

                            addMessageToSendInUi(setMessageToSend(true, 2, id,
                                    Base64.encodeToString(b, Base64.DEFAULT).trim()), true, 2, uri, false, false, true);


                            uri = null;
                            b = null;

                        } else {


                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, getString(R.string.string_674)

                                        , Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }


                        }
                    } else {


                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, getString(R.string.string_674)
                                    , Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }


                    }

                } else {


                    if (root != null) {

                        Snackbar snackbar = Snackbar.make(root, getString(R.string.string_52) + " " + MAX_VIDEO_SIZE / (1024 * 1024) + " " + getString(R.string.string_56), Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }


                }

            } catch (NullPointerException e) {


                if (root != null) {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_764, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }

            }


        }


//        else if (requestCode == RESULT_CAPTURE_VIDEO && resultCode == RESULT_OK && null != data) {
//
//
//            String id = null;
//            Uri uri = null;
//            try {
//                videoPath = getPath(this, data.getData());
//
//                File video = new File(videoPath);
//                if (video.length() <= (MAX_VIDEO_SIZE)) {
//
//                    try {
//                        byte[] b = convertFileToByteArray(video);
//                        video = null;
//
//                        //          b = compress(b);
//
//
//                        id = new Utilities().gmtToEpoch(Utilities.tsInGmt());
//
//                        File f = convertByteArrayToFile(b, id, ".mp4");
//                        b = null;
//
//                        uri = Uri.fromFile(f);
//                        f = null;
//
//                        b = null;
//                    } catch (OutOfMemoryError e) {
//                        e.printStackTrace();
//                        if (root != null) {
//
//                            Snackbar snackbar = Snackbar.make(root, R.string.string_51, Snackbar.LENGTH_SHORT);
//
//
//                            snackbar.show();
//                            View view = snackbar.getView();
//                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                        }
//
//
//                    }
//
//
//                    if (uri != null) {
//
//                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                        Bitmap bm = ThumbnailUtils.createVideoThumbnail(videoPath,
//                                MediaStore.Images.Thumbnails.MINI_KIND);
//
//
//                        if (bm != null) {
//
//                            bm.compress(Bitmap.CompressFormat.JPEG, 1, baos);
//                            bm = null;
//                            byte[] b = baos.toByteArray();
//                            try {
//                                baos.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            baos = null;
//
//
//                            addMessageToSendInUi(setMessageToSend(true, 2, id, Base64.encodeToString(b, Base64.DEFAULT).trim()), true, 2, uri);
//
//                            uri = null;
//                            b = null;
//
//                        }
//                    }
//
//                } else {
//                    if (root != null) {
//
//                        Snackbar snackbar = Snackbar.make(root, getString(R.string.string_52) + " " + MAX_VIDEO_SIZE / (1024 * 1024) + " " + getString(R.string.string_56), Snackbar.LENGTH_SHORT);
//
//
//                        snackbar.show();
//                        View view = snackbar.getView();
//                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                    }
//
//                }
//
//            } catch (NullPointerException e) {
//
//
//                if (root != null) {
//
//                    Snackbar snackbar = Snackbar.make(root, R.string.string_765, Snackbar.LENGTH_SHORT);
//
//
//                    snackbar.show();
//                    View view = snackbar.getView();
//                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                }
//
//            }
//        }


        else if (requestCode == REQUEST_SELECT_AUDIO && resultCode == RESULT_OK && null != data) {

            String id = null;
            Uri uri = null;
            try {


                audioPath = getPath(this, data.getData());

                File audio = new File(audioPath);


                if (audio.length() <= (MAX_VIDEO_SIZE)) {
                    try {


                        byte[] b = convertFileToByteArray(audio);
                        audio = null;
                        // b = compress(b);


                        id = new Utilities().gmtToEpoch(Utilities.tsInGmt());

                        File f = convertByteArrayToFile(b, id, ".3gp");
                        b = null;

                        uri = Uri.fromFile(f);
                        f = null;

                        b = null;


                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, R.string.string_53, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }


                    }

                    if (uri != null) {


                        addMessageToSendInUi(setMessageToSend(true, 5, id, null), true, 5, uri, false, false, true);

                        uri = null;

                    }
                } else {


                    if (root != null) {

                        Snackbar snackbar = Snackbar.make(root, getString(R.string.string_54) + " " + MAX_VIDEO_SIZE / (1024 * 1024) + " " + getString(R.string.string_56), Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }

                }


            } catch (NullPointerException e) {


                if (root != null) {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_766, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }

            }


        } else if (resultCode == REQUEST_CODE_CONTACTS && null != data) {


            try {

                String name = data.getStringExtra("name");
                String identifier = data.getStringExtra("identifier");
                String userId = data.getStringExtra("userId");
                String userImage = data.getStringExtra("userImage");
                contactInfo = name + "@@" + identifier + "@@" + userId + "@@" + userImage;
                addMessageToSendInUi(setMessageToSend(false, 4, null, null), false, 4, null, false, false, true);
//                Uri contactData = data.getData();
//                Cursor c = getContentResolver().query(contactData, null, null, null, null);
//                if (c.moveToFirst()) {
//                    String name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//
//
//                    String contactID = "";
//
//                    String contactNumber = null;
//
//
//                    Cursor cursorID = getContentResolver().query(contactData,
//                            new String[]{ContactsContract.Contacts._ID},
//                            null, null, null);
//
//                    if (cursorID.moveToFirst()) {
//
//                        contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
//                    }
//
//                    cursorID.close();
//
//
//                    Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
//                            new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},
//
//                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
//                                    ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
//                                    ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,
//
//                            new String[]{contactID},
//                            null);
//
//                    if (cursorPhone.moveToFirst()) {
//
//
//                        try {
//                            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//
//                            contactNumber.replaceAll("\\s+", "");
//                        } catch (NullPointerException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//
//                    cursorPhone.close();
//                    c.close();
//
//
//                    if (contactNumber == null) {
//                        contactInfo = name + "@@" + getString(R.string.string_246);
//                    } else {
//
//                        contactInfo = name + "@@" + contactNumber;
//                    }


            } catch (NullPointerException e) {


                if (root != null) {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_767, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }

            }

        } else if (requestCode == RESULT_LOAD_GIF && resultCode == RESULT_OK && null != data) {


            gifUrl = data.getStringExtra("gifUrl");
//            final String[] thumbnail = new String[1];
//
//
//            final ProgressDialog pDialog = new ProgressDialog(this);
//            pDialog.setMessage(getString(R.string.ShareGif));
//            pDialog.setCancelable(false);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    pDialog.show();
//                }
//            });
//
//
//            if (gifUrl != null && !gifUrl.isEmpty()) {
//
//                Glide
//                        .with(this)
//                        .load(gifUrl)
//                        .asGif()
//                        .fitCenter()
//
//                        .listener(new RequestListener<String, GifDrawable>() {
//                            @Override
//                            public boolean onException(Exception e, String model, Target<GifDrawable> target, boolean isFirstResource) {
//                                return false;
//                            }
//
//                            @Override
//                            public boolean onResourceReady(GifDrawable resource, String model, Target<GifDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//
//                                Bitmap bm = resource.getFirstFrame();
//
//
//                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                                bm.compress(Bitmap.CompressFormat.JPEG, 10, baos);
//                                bm = null;
//                                byte[] b = baos.toByteArray();
//                                try {
//                                    baos.close();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                                baos = null;
//                                thumbnail[0] = Base64.encodeToString(b, Base64.DEFAULT).trim();
//
//
////                                addMessageToSendInUi(setMessageToSend(false, 8, null, thumbnail[0]), false, 8, null, "", false);
//
//
//                                addMessageToSendInUi(setMessageToSend(false, 8, null, thumbnail[0]), false, 8, null);
//
//
//                                recyclerView_chat.scrollToPosition(mChatData.size());
//
//                                b = null;
//                                bm = null;
//
//
//                                if (pDialog.isShowing())
//                                    pDialog.dismiss();
//
//                                return false;
//                            }
//
//                        })
//                        .into(dummyGifIv);

            // }
            addMessageToSendInUi(setMessageToSend(false, 8, null, null), false, 8, null, false, false, true);
        } else if (requestCode == RESULT_LOAD_STICKER && resultCode == RESULT_OK && null != data) {


            /*
             * In the thumbnail field only i will send the sticker url
             */

            stickerUrl = data.getStringExtra("gifUrl");

            if (stickerUrl != null && !stickerUrl.isEmpty()) {


                addMessageToSendInUi(setMessageToSend(false, 6, null, stickerUrl), false, 6, null, false, false, true);

                recyclerView_chat.scrollToPosition(mChatData.size());
            }

        } else if (requestCode == FilePickerConst.REQUEST_CODE_PHOTO) {


//            ArrayList<Image> imagesList = data.getParcelableArrayListExtra(Constants.KEY_BUNDLE_LIST);

            if (resultCode == RESULT_OK && null != data) {


                ArrayList<String> imagesList = new ArrayList<>();
                imagesList.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));


                if (imagesList.size() == 0) {


                    Snackbar snackbar = Snackbar.make(root, R.string.NoImage, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                } else {
                    Uri uri;
                    String id;
                    Bitmap bm;

                    for (int i = 0; i < imagesList.size(); i++) {


                        uri = null;
                        id = null;
                        bm = null;
//                picturePath = imagesList.get(i).imagePath;


                        picturePath = imagesList.get(i);
                        try {


                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(picturePath, options);


                            int height = options.outHeight;
                            int width = options.outWidth;

                            float density = getResources().getDisplayMetrics().density;
                            int reqHeight;


                            if (width != 0) {


                                reqHeight = (int) ((150 * density) * (height / width));


                                bm = decodeSampledBitmapFromResource(picturePath, (int) (150 * density), reqHeight);


                                ByteArrayOutputStream baos = new ByteArrayOutputStream();


                                if (bm != null) {

                                    bm.compress(Bitmap.CompressFormat.JPEG, IMAGE_CAPTURED_QUALITY, baos);
                                    //bm = null;
                                    byte[] b = baos.toByteArray();
                                    try {
                                        baos.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    baos = null;
//                b = compress(b);


                                    id = new Utilities().gmtToEpoch(Utilities.tsInGmt());

                                    File f = convertByteArrayToFile(b, id, ".jpg");
                                    b = null;

                                    uri = Uri.fromFile(f);
                                    f = null;


                                } else {


                                    if (root != null) {

                                        Snackbar snackbar = Snackbar.make(root, R.string.string_48, Snackbar.LENGTH_SHORT);


                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                    }


                                }


                            } else {


                                if (root != null) {

                                    Snackbar snackbar = Snackbar.make(root, R.string.string_48, Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                }
                            }


                        } catch (OutOfMemoryError e) {
                            e.printStackTrace();


                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, R.string.string_49, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }


                        }


                        if (uri != null) {

                            ByteArrayOutputStream baos = new ByteArrayOutputStream();

                            bm.compress(Bitmap.CompressFormat.JPEG, 1, baos);


                            bm = null;
                            byte[] b = baos.toByteArray();

                            try {
                                baos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            baos = null;


                            //   addMessageToSendInUi(setMessageToSend(true, 1, id, Base64.encodeToString(b, Base64.DEFAULT).trim()), true, 1, uri, true);


                            addMessageToSendInUi(setMessageToSend(true, 1, id,
                                    Base64.encodeToString(b, Base64.DEFAULT).trim()), true, 1, uri, false, false, true);

                            uri = null;
                            b = null;
                            bm = null;

                        }


                    }
                }
            } else {


                if (resultCode == Activity.RESULT_CANCELED) {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_16, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                } else {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_113, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                }

            }
        } else if (requestCode == FilePickerConst.REQUEST_CODE_DOC) {


            if (resultCode == RESULT_OK && null != data) {


                ArrayList<String> documentsList = new ArrayList<>();

                ArrayList<String> documentsMimeList = new ArrayList<>();


                documentsList.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                documentsMimeList.addAll(data.getStringArrayListExtra(FilePickerConst.SELECTED_DOCS_MIME_TYPES));


                if (documentsList.size() == 0) {


                    Snackbar snackbar = Snackbar.make(root, R.string.NoDocument, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                } else {

                    File f;
                    String name, extension;
                    for (int i = 0; i < documentsList.size(); i++) {


                        f = new File(documentsList.get(i));

                        name = f.getName();
                        extension = FilenameUtils.getExtension(name);
                        addDocumentToSendInUi(setDocumentObjectToSend(new Utilities().gmtToEpoch(Utilities.tsInGmt()), documentsMimeList.get(i), name, extension), Uri.fromFile(f), documentsMimeList.get(i), name, documentsList.get(i), extension);
                        f = null;

                    }
                }
            } else {


                if (resultCode == Activity.RESULT_CANCELED) {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_7, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                } else {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_115, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                }

            }

        } else if (requestCode == RESULT_LOAD_WALLPAPER) {

            if (resultCode == Activity.RESULT_OK) {
                try {

                    wallpaperPath = getPath(SecretChatMessageScreen.this, data.getData());

                    if (wallpaperPath != null) {

                        final BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(wallpaperPath, options);

                        if (options.outWidth > 0 && options.outHeight > 0) {
                            /*
                             * Have to start the intent for the image cropping
                             */
                            CropImage.activity(data.getData())
                                    .start(this);


                        } else {

                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, R.string.string_31, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }


                        }

                    } else {


                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, R.string.string_31, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    }
                } catch (OutOfMemoryError e) {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_15, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                }
            } else {
                if (resultCode == Activity.RESULT_CANCELED) {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_16, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                } else {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_113, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                }
            }
        } else if (requestCode == RESULT_CAPTURE_WALLPAPER) {


            if (resultCode == Activity.RESULT_OK) {
                try {
                    // picturePath = getPath(Deal_Add.this, imageUri);


                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(wallpaperPath, options);


                    if (options.outWidth > 0 && options.outHeight > 0) {


                        CropImage.activity(imageUri)
                                .start(this);


                    } else {


                        wallpaperPath = null;
                        Snackbar snackbar = Snackbar.make(root, R.string.string_17, Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                    }
                } catch (OutOfMemoryError e) {

                    wallpaperPath = null;
                    Snackbar snackbar = Snackbar.make(root, R.string.string_15, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                }
            } else {


                if (resultCode == Activity.RESULT_CANCELED) {

                    wallpaperPath = null;
                    Snackbar snackbar = Snackbar.make(root, R.string.string_18, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                } else {

                    wallpaperPath = null;
                    Snackbar snackbar = Snackbar.make(root, R.string.string_17, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                }
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {


            try {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {


                    wallpaperPath = getPath(SecretChatMessageScreen.this, result.getUri());

                    if (wallpaperPath != null) {


                        Bitmap bitmap = BitmapFactory.decodeFile(wallpaperPath);


                        if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {


                            //  chatBackground.setImageBitmap(bitmap);

                            db.addWallpaper(documentId, 3, wallpaperPath);
                            updateWallpaper(3, wallpaperPath);

                        } else {


                            wallpaperPath = null;
                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, R.string.string_19, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }


                        }


                    } else {


                        wallpaperPath = null;
                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, R.string.string_19, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }


                    }


                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                    wallpaperPath = null;
                    if (root != null) {

                        Snackbar snackbar = Snackbar.make(root, R.string.string_19, Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                }

            } catch (OutOfMemoryError e) {

                picturePath = null;
                Snackbar snackbar = Snackbar.make(root, R.string.string_15, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

            }

        }


    }

    @SuppressWarnings("TryWithIdenticalCatches")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {


        if (requestCode == 21) {


            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_55,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                } else {


                    Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                }
            } else {


                Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

            }


        } else if (requestCode == 22) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {


                    if (new GPSTracker(SecretChatMessageScreen.this).canGetLocation()) {

                        launchLocationPickerIntent();

                    } else {


                        Snackbar snackbar = Snackbar.make(root, R.string.string_58,
                                Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }


                } else {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_59,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_59,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        } else if (requestCode == 23) {

        } else if (requestCode == 24) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {


                    if (ActivityCompat.checkSelfPermission(SecretChatMessageScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        if (intent.resolveActivity(getPackageManager()) != null) {

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(0));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                            } else {


                                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                                for (ResolveInfo resolveInfo : resInfoList) {
                                    String packageName = resolveInfo.activityInfo.packageName;
                                    grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                }


                            }

//                            startActivityForResult(intent, RESULT_CAPTURE_MEDIA);

                            openCamera();

                        } else {
                            Snackbar snackbar = Snackbar.make(root, R.string.string_61,
                                    Snackbar.LENGTH_SHORT);
                            snackbar.show();


                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    } else {


                        requestReadImagePermission(0);
                    }
                } else {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_62,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_62,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }


//        else if (requestCode == 25) {
//
//            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//
//
//                        == PackageManager.PERMISSION_GRANTED) {
//
//                    if (ActivityCompat.checkSelfPermission( SecretChatMessageScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                            == PackageManager.PERMISSION_GRANTED)
//
//
//                    {
//                        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);//mms quality video not hd
//                        intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120);//max 120s video
//                        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 26214400L);//max 25 mb size recording
//
//
//                        startActivityForResult(intent, RESULT_CAPTURE_VIDEO);
//
//                    } else {
//
//
//                        requestReadVideoPermission(0);
//                    }
//
//                } else {
//
//                    Snackbar snackbar = Snackbar.make(root, R.string.string_62,
//                            Snackbar.LENGTH_SHORT);
//
//
//                    snackbar.show();
//                    View view = snackbar.getView();
//                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                }
//            } else {
//
//                Snackbar snackbar = Snackbar.make(root, R.string.string_62,
//                        Snackbar.LENGTH_SHORT);
//
//
//                snackbar.show();
//                View view = snackbar.getView();
//                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//            }
//        }
//


        else if (requestCode == 26) {

            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {


                    showGalleryPopup();
                } else {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        } else if (requestCode == 27) {

            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(intent, RESULT_LOAD_VIDEO);

                } else {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        } else if (requestCode == 28) {

            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    Intent intent_upload = new Intent();
                    intent_upload.setType("audio/*");
                    intent_upload.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent_upload, REQUEST_SELECT_AUDIO);
                } else {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        } else if (requestCode == 37) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(SecretChatMessageScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(0));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                        } else {


                            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            for (ResolveInfo resolveInfo : resInfoList) {
                                String packageName = resolveInfo.activityInfo.packageName;
                                grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }


                        }

//                        startActivityForResult(intent, RESULT_CAPTURE_MEDIA);
                        openCamera();

                    } else {
                        Snackbar snackbar = Snackbar.make(root, R.string.string_61,
                                Snackbar.LENGTH_SHORT);
                        snackbar.show();


                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                } else {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }

            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        }


//
//        else if (requestCode == 38) {
//            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//
//
//                if (ActivityCompat.checkSelfPermission( SecretChatMessageScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        == PackageManager.PERMISSION_GRANTED) {
//                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);//mms quality video not hd
//                    intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120);//max 120s video
//                    intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 26214400L);//max 25 mb size recording
//
//
//                    startActivityForResult(intent, RESULT_CAPTURE_VIDEO);
//                } else {
//
//                    Snackbar snackbar = Snackbar.make(root, R.string.string_57,
//                            Snackbar.LENGTH_SHORT);
//
//
//                    snackbar.show();
//                    View view = snackbar.getView();
//                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                }
//            } else {
//
//                Snackbar snackbar = Snackbar.make(root, R.string.string_57,
//                        Snackbar.LENGTH_SHORT);
//
//
//                snackbar.show();
//                View view = snackbar.getView();
//                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//            }
//
//        }

        else if (requestCode == 71) {


            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                /*
                 * Not required essentially
                 */
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED) {


                    requestAudioCall();
                }
            }

        } else if (requestCode == 72) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {


                    requestVideoCall();
                }
            } else if (grantResults.length == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {


                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {


                    requestVideoCall();

                }


            }

        } else if (requestCode == 47) {

            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {


                    showDoodlePopup();


                } else {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        } else if (requestCode == 81) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    startAudioRecord(false);

                } else {

                    requestReadImagePermission(3);
                }

            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_68,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        } else if (requestCode == 82) {


            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {


                    startAudioRecord(false);

                } else {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        } else if (requestCode == 85) {


            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {


                    showDocumentPopup();

                } else {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        } else if (requestCode == 43) {


            /*
             *For selecting the wallpaper from the gallery
             */


            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.SelectWallpaper)), RESULT_LOAD_WALLPAPER);
                    } else {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, getString(R.string.SelectWallpaper)), RESULT_LOAD_WALLPAPER);
                    }


                } else {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        } else if (requestCode == 44) {


            /*
             *For capturing of the wallpaper functionality
             */


            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED) {


                    if (ActivityCompat.checkSelfPermission(SecretChatMessageScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        if (intent.resolveActivity(getPackageManager()) != null) {

                            intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(1));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                            } else {


                                List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                                for (ResolveInfo resolveInfo : resInfoList) {
                                    String packageName = resolveInfo.activityInfo.packageName;
                                    grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                }


                            }

                            startActivityForResult(intent, RESULT_CAPTURE_WALLPAPER);


                        } else {
                            Snackbar snackbar = Snackbar.make(root, R.string.string_61,
                                    Snackbar.LENGTH_SHORT);
                            snackbar.show();


                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    } else {


                        requestReadImagePermission(6);
                    }
                } else {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_62,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_62,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        } else if (requestCode == 45) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(SecretChatMessageScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (intent.resolveActivity(getPackageManager()) != null) {

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(1));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                        } else {


                            List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                            for (ResolveInfo resolveInfo : resInfoList) {
                                String packageName = resolveInfo.activityInfo.packageName;
                                grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }


                        }

                        startActivityForResult(intent, RESULT_CAPTURE_WALLPAPER);
                    } else {
                        Snackbar snackbar = Snackbar.make(root, R.string.string_61,
                                Snackbar.LENGTH_SHORT);
                        snackbar.show();


                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                } else {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }

            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        } else if (requestCode == 49) {


            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {


                    Intent i = new Intent(SecretChatMessageScreen.this, DrawActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i.putExtra("documentId", documentId);

                    startActivity(i);


                } else {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                            Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {

                Snackbar snackbar = Snackbar.make(root, R.string.string_57,
                        Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }

    }


    /*
     * Helper class to facilitate swipe to delete messages functionality
     */

    @SuppressWarnings("all")
    private Uri setImageUri(int type) {
        String name = Utilities.tsInGmt();
        name = new Utilities().gmtToEpoch(name);


        File folder = new File(getExternalFilesDir(null) + ApiOnServer.IMAGE_CAPTURE_URI);

        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }


        File file = new File(getExternalFilesDir(null) + ApiOnServer.IMAGE_CAPTURE_URI, name + ".jpg");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Uri imgUri = FileProvider.getUriForFile(SecretChatMessageScreen.this, getApplicationContext().getPackageName() + ".provider", file);
        this.imageUri = imgUri;

        if (type == 0) {
            this.picturePath = file.getAbsolutePath();
        } else {

            this.wallpaperPath = file.getAbsolutePath();
        }

        name = null;
        folder = null;
        file = null;


        return imgUri;
    }

    @Override
    public void onAttachFileClick() {
        checkReadImage(1);
    }

    @Override
    public void onAttachAlbumClick() {
        checkReadImage(0);
    }

    @Override
    public void onAttachCameraClick() {
        if (!AppController.getInstance().isActiveOnACall()) {
            checkCameraPermissionImage(0);
        } else {

            if (root != null) {
                Snackbar snackbar = Snackbar.make(root, getString(R.string.call_camera), Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view2 = snackbar.getView();
                TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }
    }

    @Override
    public void onAttachGIFClick() {
        Intent intent = new Intent(SecretChatMessageScreen.this, SelectGIF.class);
        startActivityForResult(intent, RESULT_LOAD_GIF);
    }

    @Override
    public void onAttachAudioClick() {
        checkReadAudio();
    }

    @Override
    public void onAttachContactClick() {
        Intent intentContact = new Intent(SecretChatMessageScreen.this, FriendsListActivity.class);
        startActivityForResult(intentContact, REQUEST_CODE_CONTACTS);
        //checkReadContactPermission();
    }

    @Override
    public void onAttachVideoClick() {
        checkReadVideo();
    }

    @Override
    public void onAttachLocationClick() {
        checkLocationAccessPermission();
    }

    @Override
    public void onAttachTransferClick() {
        Intent intent = new Intent(SecretChatMessageScreen.this, TransferToFriendActivity.class);
        intent.putExtra("userId", receiverUid);
        startActivity(intent);
    }

    @Override
    public void onAttachStarPacketClick() {
        Intent intent = new Intent(SecretChatMessageScreen.this, SendStarPacketActivity.class);
        intent.putExtra("receiverImage", receiverImage);
        intent.putExtra("receiverName", receiverName);
        intent.putExtra("receiverUid", receiverUid);
        intent.putExtra("receiverIdentifier", receiverIdentifier);
        startActivity(intent);
    }

    @Override
    public void onCoinTransferClick() {

    }

    public void openDialog() {

        WrapContentViewPager viewPager = (WrapContentViewPager) findViewById(R.id.vP_attach);
        TabLayout indicator = (TabLayout) findViewById(R.id.indicator);
        indicator.setupWithViewPager(viewPager, true);

        viewPager.setAdapter(new AttachPagerAdapter(this, sessionManager.isWalletAvailable(), false));
    }

    /*
     * To load first 10 messages from  the local couchDb database
     */
    public void loadFromDbFirstTen() {

        ArrayList<Map<String, Object>> arrMessage = db.retrieveAllMessages(documentId);


        int s = arrMessage.size();
        //  boolean flag = false;
        size = (s > 10) ? (s - 10) : (0);
        boolean lastMessage = false;
        Map<String, Object> mapMessage;


        String ts, messageType, id, message, deliveryStatus, senderName, thumbnailPath;

        boolean isSelf, timerStarted;
        int downloadStatus;
        long destTime;
        String extension, mimeType, fileName;

        boolean isDTimeTag = false;
        long expectedDTime;
        String postId = "", postTitle = "";
        int postType = -1;
        for (int i = s - 1; i >= size; i--) {
            extension = "";
            mimeType = "";
            fileName = "";
            try {
                mapMessage = (arrMessage.get(i));


                ts = (String) mapMessage.get("Ts");
                isSelf = (boolean) mapMessage.get("isSelf");
                messageType = (String) mapMessage.get("messageType");
                id = (String) mapMessage.get("id");
                message = (String) mapMessage.get("message");

                deliveryStatus = (String) mapMessage.get("deliveryStatus");

                senderName = (String) mapMessage.get("from");


                try {

                    destTime = (long) mapMessage.get("dTime");

                } catch (ClassCastException e) {

                    destTime = (int) mapMessage.get("dTime");

                } catch (NullPointerException e) {

                    // destTime = -1;
                    continue;
                }


                timerStarted = (boolean) mapMessage.get("timerStarted");
                if (!lastMessage && !isSelf) {
                    lastMessage = true;


                    createObjectToSend(id, receiverUid, destTime);


                }


                downloadStatus = -1;
                thumbnailPath = null;
                int size = -1;

                if (messageType.equals("0")) {

                    /*
                     * Text message
                     */


                    MessageType = 0;

                    isDTimeTag = (boolean) mapMessage.get("isDTag");
                } else if (messageType.equals("1")) {
                    MessageType = 1;

                    /*
                     * receiverImage
                     */


                    downloadStatus = (int) mapMessage.get("downloadStatus");


                    if (downloadStatus == 0) {


                        thumbnailPath = (String) mapMessage.get("thumbnailPath");


                        size = (int) mapMessage.get("dataSize");
                    }


                } else if (messageType.equals("2")) {
                    /*
                     * Video
                     */

                    MessageType = 2;


                    downloadStatus = (int) mapMessage.get("downloadStatus");


                    if (downloadStatus == 0) {


                        thumbnailPath = (String) mapMessage.get("thumbnailPath");


                        size = (int) mapMessage.get("dataSize");


                    }


                } else if (messageType.equals("3")) {
                    /*
                     * Location
                     */


                    MessageType = 3;


                } else if (messageType.equals("4")) {


                    /*
                     * Follow
                     */

                    MessageType = 4;

                } else if (messageType.equals("5")) {


                    /*
                     * Audio
                     */
                    MessageType = 5;


                    downloadStatus = (int) mapMessage.get("downloadStatus");


                    if (downloadStatus == 0) {


                        size = (int) mapMessage.get("dataSize");

                    }


                } else if (messageType.equals("6")) {


                    /*
                     * Sticker
                     */
                    MessageType = 6;

                } else if (messageType.equals("7")) {


                    /*
                     * Doodle
                     */

                    MessageType = 7;

                    downloadStatus = (int) mapMessage.get("downloadStatus");
                    if (downloadStatus == 0) {
                        thumbnailPath = (String) mapMessage.get("thumbnailPath");
                        size = (int) mapMessage.get("dataSize");
                    }


                } else if (messageType.equals("8")) {


                    /*
                     * Giphy
                     */

                    MessageType = 8;

                } else if (messageType.equals("9")) {


                    /*
                     * Document
                     */

                    MessageType = 9;
                    downloadStatus = (int) mapMessage.get("downloadStatus");
                    if (downloadStatus == 0) {

                        size = (int) mapMessage.get("dataSize");
                    }
                    extension = (String) mapMessage.get("extension");
                    mimeType = (String) mapMessage.get("mimeType");
                    fileName = (String) mapMessage.get("fileName");
                } else if (messageType.equals("13")) {


                    /*
                     * Post
                     */

                    MessageType = 13;

                    postId = (String) mapMessage.get("postId");
                    postTitle = (String) mapMessage.get("postTitle");
                    postType = (int) mapMessage.get("postType");
                }
                if (timerStarted) {

                    try {

                        expectedDTime = (long) mapMessage.get("expectedDTime");
                    } catch (ClassCastException e) {


                        expectedDTime = (int) mapMessage.get("expectedDTime");
                    } catch (NullPointerException e) {


                        continue;
                    }
                    //   destTime = expectedDTime - Long.parseLong(Utilities.tsInGmt());
                    destTime = expectedDTime - Utilities.getGmtEpoch();


                    if (destTime < 0) {
                        /*
                         * Already expired
                         */

                        new removeMessage().execute(documentId, id);


                    } else {


                        if (message != null) {


                            /*
                             * Have made this false intentionally
                             */


                            loadFromDb(MessageType, isSelf, message, senderName, ts, deliveryStatus, id,


                                    status, downloadStatus, thumbnailPath, size, (destTime / 1000), false, isDTimeTag, extension, mimeType, fileName, postId, postTitle, postType);

                        }
                    }
                } else {

                    if (message != null) {

                        loadFromDb(MessageType, isSelf, message, senderName, ts, deliveryStatus, id,


                                status, downloadStatus, thumbnailPath, size, destTime, false, isDTimeTag, extension, mimeType, fileName, postId, postTitle, postType);

                    }
                }

                senderName = null;
                ts = null;
                messageType = null;
                id = null;
                message = null;
                deliveryStatus = null;

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }


        MessageType = 0;

        arrMessage = null;


    }


    /*
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();


        AppController.getInstance().setActiveReceiverId("");
        AppController.getInstance().setActiveSecretId("");
        AppController.getInstance().unsubscribeToTopic(MqttEvents.OnlineStatus.value + "/" + receiverUid);
//        AppController.getInstance().unsubscribeToTopic(MqttEvents.Typing.value + typingTopic);


        AppController.getInstance().unsubscribeToTopic(MqttEvents.Typing.value + receiverUid);


        sendButton = null;
        sendSticker = null;
        selEmoji = null;
        selKeybord = null;
        sendMessage = null;

        llm = null;

        profilePic = null;

        tv = null;
        receiverNameHeader = null;


//        delete = null;
        attachment = null;
        // history = null;

        backButton = null;


        drawable2 = null;
        drawable1 = null;


        userId = null;

        userName = null;
        receiverUid = null;
        documentId = null;

        tsForServerEpoch = null;
        tsForServer = null;
        videoPath = null;
        audioPath = null;

        mAdapter = null;
        recyclerView_chat = null;
        receiverName = null;

        imageUrl = null;


        placeString = null;


        contactInfo = null;
        mChatData = null;

        db = null;


        Glide.get(this).clearMemory();
        Glide.get(this).getBitmapPool().clearMemory();


//        AppController.getInstance().freeMemory();

        bus.unregister(this);
    }

    /*
     * To acknowledge all messages above last message has been read
     */
    private void createObjectToSend(String id, String recieverUid, long dTime) {
        JSONObject obj = new JSONObject();

        try {


            obj.put("from", userId);


            obj.put("msgIds", new JSONArray(Arrays.asList(new String[]{id})));
            obj.put("doc_id", db.getDocumentIdOfReceiver(documentId, recieverUid));
            obj.put("to", recieverUid);
            obj.put("status", "3");

            obj.put("secretId", secretId);

            obj.put("dTime", dTime);
            obj.put("readTime", Utilities.getGmtEpoch());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        AppController.getInstance().publish(MqttEvents.Acknowledgement.value + "/" + receiverUid, obj, 2, false);


        obj = null;
        id = null;
        recieverUid = null;

    }

    /*
     * to convert string from the 24 hour format to 12 hour format
     */

    private void updateLastSeenInActionBar(JSONObject obj) {
        if (!lastSeenResponseCame)
            lastSeenResponseCame = true;
        try {


            if (obj.getBoolean("lastSeenEnabled")) {


                switch (obj.getInt("status")) {

                    case 1:
                        opponentOnline = true;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                top = getString(R.string.string_337);


                                if (tv != null)
                                    tv.setText(top);
                            }
                        });
                        break;
                    case 0:
                        opponentOnline = false;


                        String lastSeen = obj.getString("timestamp");


                        lastSeen = Utilities.changeStatusDateFromGMTToLocal(lastSeen);

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);

                        Date date2 = new Date(System.currentTimeMillis() - AppController.getInstance().getTimeDelta());
                        String current_date = sdf.format(date2);

                        current_date = current_date.substring(0, 8);


                        if (lastSeen != null) {

                            final String onlineStatus;
                            if (current_date.equals(lastSeen.substring(0, 8))) {

                                lastSeen = convert24to12hourformat(lastSeen.substring(8, 10) + ":" + lastSeen.substring(10, 12));


                                onlineStatus = "Last Seen:Today " + lastSeen;

                                lastSeen = null;

                            } else {

                                String last = convert24to12hourformat(lastSeen.substring(8, 10) + ":" + lastSeen.substring(10, 12));


                                String date = lastSeen.substring(6, 8) + "-" + lastSeen.substring(4, 6) + "-" + lastSeen.substring(0, 4);

                                onlineStatus = "Last Seen:" + date + " " + last;


                                last = null;
                                date = null;

                            }


                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    top = onlineStatus;
                                    if (tv != null) {

                                        tv.setText(top);
                                    }
                                }
                            });

                        }


                        lastSeen = null;
                        sdf = null;
                        date2 = null;
                        current_date = null;

                        break;
                    case 2:

                        opponentOnline = false;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                top = getString(R.string.string_755);
                                if (tv != null)
                                    tv.setText(top);
                            }
                        });

                }

                try {


                    header_rl.setVisibility(View.VISIBLE);


                    header_center_rl.setVisibility(View.GONE);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {

                /*
                 * Have to update the visibility
                 */
                switch (obj.getInt("status")) {

                    case 1:
                        opponentOnline = true;
                        break;
                    case 0:

                        opponentOnline = false;

                        break;
                    case 2:


                        opponentOnline = false;
                }

                try {


                    header_rl.setVisibility(View.GONE);


                    header_center_rl.setVisibility(View.VISIBLE);

                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    /*
     * status--
     * 1-reached server
     * 2-delivered
     * 3-read
     */
    private void drawDoubleTick(String docId, String id, String status) {

        if (documentId.equals(docId)) {


            boolean flag = false;

            for (int i = mChatData.size() - 1; i >= 0; i--) {
                if (mChatData.get(i).isSelf() && (mChatData.get(i).getMessageId().equals(id))) {
                    flag = true;


                    if (status.equals("1")) {

                        if (mChatData.get(i).getDeliveryStatus().equals("0")) {

                            mChatData.get(i).setDeliveryStatus("1");


                            final int k = i;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyItemChanged(k);
                                }
                            });

                        }

                    } else if (status.equals("2")) {


                        mChatData.get(i).setDeliveryStatus("2");


                        final int k = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                mAdapter.notifyItemChanged(k);
                            }
                        });


                    } else if (status.equals("3")) {


                        if (!(mChatData.get(i).getDeliveryStatus().equals("3"))) {

                            for (int j = i; j >= 0; j--) {
                                if (mChatData.get(j).isSelf() && !mChatData.get(j).getDeliveryStatus().equals("0")) {

                                    if (mChatData.get(j).getDeliveryStatus().equals("3")) {


                                        break;
                                    } else {

                                        mChatData.get(j).setDeliveryStatus("3");

                                        final int k = j;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {


                                                mAdapter.notifyItemChanged(k);
                                            }
                                        });
                                    }
                                }
                            }


                        }
                    }
                }


                if (flag) {
                    break;
                }
            }
        }


        id = null;
        docId = null;
        status = null;
    }

    /*
     * To draw single tick when message has been received and acknowledged by the server
     */
    private void drawSingleTick(String id) {

        for (int i = mChatData.size() - 1; i >= 0; i--) {
            if (mChatData.get(i).isSelf() && (mChatData.get(i).getMessageId()).equals(id)) {


                if (!(mChatData.get(i).getDeliveryStatus().equals("1"))) {
                    mChatData.get(i).setDeliveryStatus("1");


                    final int k = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            mAdapter.notifyItemChanged(k);


                        }
                    });

                    break;
                }


            }
        }


        id = null;
    }


    /*
     * file uri will not be null only in case of image,audio or video
     */

    /*
     * To load messgae received on the socket at appropriate position in list of the messages
     */


    @SuppressWarnings("TryWithIdenticalCatches")

    private void loadMessageInChatUI(String sender, String id, String messageType, String message, String tsFromServer, int dataSize, long dTime, String mimeType, String fileName, String extension, String postId, String postTitle, int postType) {


        byte[] data = Base64.decode(message, Base64.DEFAULT);


        String ts = Utilities.formatDate(Utilities.tsFromGmt(Utilities.epochtoGmt(tsFromServer)));


        SecretChatMessageItem messageItem = new SecretChatMessageItem();

        messageItem.setSenderName(receiverName);

        messageItem.setIsSelf(false);

        if ((messageType.equals("1")) || (messageType.equals("2")) || (messageType.equals("5")) || (messageType.equals("7")) || (messageType.equals("9"))) {
            String size;


            if (dataSize < 1024) {


                size = dataSize + " bytes";

            } else if (dataSize >= 1024 && dataSize <= 1048576) {


                size = (dataSize / 1024) + " KB";

            } else {


                size = (dataSize / 1048576) + " MB";
            }


            messageItem.setSize(size);
        }


        messageItem.setReceiverUid(sender);

        messageItem.setTS(ts.substring(0, 9));
        messageItem.setTimerStarted(false);


        if (dTime != -1) {


            messageItem.setdTime(dTime);
        }
        messageItem.setMessageDateOverlay(ts.substring(9, 24));

        messageItem.setMessageDateGMTEpoch(Long.parseLong(tsFromServer));
        messageItem.setMessageId(id);
        messageItem.setDTimeTag(false);

        if (messageType.equals("0")) {


            messageItem.setMessageType("0");

            try {
                messageItem.setTextMessage(new String(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            if (messageItem.getTextMessage().trim().isEmpty()) {
                messageItem.setDTimeTag(true);


                this.dTime = dTime;
                String destTime = String.valueOf(dTime);
                //db.updateDestructTime(documentId, dTime);

                for (int i = 0; i < dTimeForDB.length; i++) {
                    if (destTime.equals(dTimeForDB[i])) {

                        final int finalI = i;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dTimeTv.setText(dTimeToShow[finalI]);
                            }
                        });

                        if (i == 0) {
                            messageItem.setTextMessage(getString(R.string.Timer_set_off));
                        } else {
                            messageItem.setTextMessage(getString(R.string.Timer_set_to) + " " + dTimeOptions[i]);
                        }
                        break;
                    }
                }
            }


            data = null;


        } else if (messageType.equals("1")) {

            messageItem.setDownloading(false);
            messageItem.setMessageType("1");

            messageItem.setDownloadStatus(0);


            messageItem.setThumbnailPath(getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + tsFromServer + ".jpg");


            try {

                messageItem.setImagePath(new String(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            data = null;

        } else if (messageType.equals("2")) {


            messageItem.setMessageType("2");
            messageItem.setDownloadStatus(0);
            messageItem.setDownloading(false);


            messageItem.setThumbnailPath(getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + tsFromServer + ".jpg");


            try {

                messageItem.setVideoPath(new String(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            data = null;
        } else if (messageType.equals("3")) {


            String placeString = "";

            try {

                placeString = new String(data, "UTF-8");


                data = null;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            messageItem.setMessageType("3");


            messageItem.setPlaceInfo(placeString);


            placeString = null;

        } else if (messageType.equals("4")) {


            String contactString = "";

            try {

                contactString = new String(data, "UTF-8");
                data = null;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            messageItem.setMessageType("4");


            messageItem.setContactInfo(contactString);


            contactString = null;


        } else if (messageType.equals("5")) {


            messageItem.setMessageType("5");
            messageItem.setDownloadStatus(0);

            messageItem.setDownloading(false);
            try {

                messageItem.setAudioPath(new String(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            data = null;

        } else if (messageType.equals("6")) {
            messageItem.setMessageType("6");

            try {
                messageItem.setStickerUrl(new String(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            data = null;

        } else if (messageType.equals("7")) {


            messageItem.setMessageType("7");

            messageItem.setDownloadStatus(0);

            messageItem.setThumbnailPath(getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + tsFromServer + ".jpg");

            try {

                messageItem.setImagePath(new String(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            data = null;

        } else if (messageType.equals("8")) {


            messageItem.setMessageType("8");

            try {
                messageItem.setGifUrl(new String(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            data = null;


        } else if (messageType.equals("9")) {


            messageItem.setDownloadStatus(0);

            messageItem.setDownloading(false);
            messageItem.setMimeType(mimeType);

            messageItem.setFileName(fileName);
            messageItem.setExtension(extension);
            messageItem.setMessageType("9");


            messageItem.setFileType(findFileTypeFromExtension(extension));


            try {
                messageItem.setDocumentUrl(new String(data, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            data = null;


        } else if (messageType.equals("13")) {


            String url = "";

            try {

                url = new String(data, "UTF-8");


                data = null;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }


            messageItem.setMessageType("13");


            messageItem.setImagePath(url);

            messageItem.setPostId(postId);
            messageItem.setPostTitle(postTitle);
            messageItem.setPostType(postType);

            url = null;

        }

        final int position = getPositionOfMessage(Long.parseLong(tsFromServer));


        mChatData.add(position, messageItem);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {

                    mAdapter.notifyItemInserted(position);


//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // recyclerView_chat.scrollToPosition(mChatData.size() - 1);
//
//
//                        llm.scrollToPositionWithOffset(mChatData.size() - 1, 0);
//                    }
//                }, 10);


                    llm.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, 0);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }


            }
        });


        messageItem = null;
    }

    /*
     * To load previous 10 messages exchanged (if any) before the message on top
     */
    @SuppressWarnings("TryWithIdenticalCatches")

    private void loadTenMore() {


        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage(getString(R.string.string_484));
        pDialog.setCancelable(false);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                pDialog.show();
                ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


                bar.getIndeterminateDrawable().setColorFilter(
                        ContextCompat.getColor(SecretChatMessageScreen.this, R.color.progress_drawable_color),
                        android.graphics.PorterDuff.Mode.SRC_IN);
            }
        });


        status = 1;
        ArrayList<Map<String, Object>> arrMessage = db.retrieveAllMessages(documentId);
        Map<String, Object> mapMessage;


        int s1 = (size > 10) ? (size - 10) : (0);


        String ts, messageType, id, message, deliveryStatus, senderName;

        boolean isSelf, timerStarted;

        long destTime;

        String extension, mimeType, fileName;
        boolean isDTimeTag = false;

        long expectedDTime;

        String postId = "", postTitle = "";
        int postType = -1;
        for (int i = size - 1; i >= s1; i--) {

            try {


                extension = "";
                mimeType = "";
                fileName = "";
                mapMessage = (arrMessage.get(i));


                ts = (String) mapMessage.get("Ts");
                isSelf = (boolean) mapMessage.get("isSelf");
                messageType = (String) mapMessage.get("messageType");
                id = (String) mapMessage.get("id");
                message = (String) mapMessage.get("message");

                deliveryStatus = (String) mapMessage.get("deliveryStatus");

                senderName = (String) mapMessage.get("from");


                try {

                    destTime = (long) mapMessage.get("dTime");

                } catch (ClassCastException e) {

                    destTime = (int) mapMessage.get("dTime");

                } catch (NullPointerException e) {


                    continue;
                }
                timerStarted = (boolean) mapMessage.get("timerStarted");

                int downloadStatus = -1;
                String thumbnailPath = null;
                int size = -1;


                if (messageType.equals("0")) {
                    MessageType = 0;
                    /*
                     *Text message
                     */


                    isDTimeTag = (boolean) mapMessage.get("isDTag");

                } else if (messageType.equals("1")) {


                    /*
                     * receiverImage
                     */
                    MessageType = 1;


                    downloadStatus = (int) mapMessage.get("downloadStatus");


                    if (downloadStatus == 0) {


                        thumbnailPath = (String) mapMessage.get("thumbnailPath");
                        size = (int) mapMessage.get("dataSize");
                    }


                } else if (messageType.equals("2")) {

                    /*
                     * Video
                     */


                    MessageType = 2;


                    downloadStatus = (int) mapMessage.get("downloadStatus");


                    if (downloadStatus == 0) {


                        thumbnailPath = (String) mapMessage.get("thumbnailPath");
                        size = (int) mapMessage.get("dataSize");
                    }


                } else if (messageType.equals("3")) {


                    /*
                     * Location
                     */
                    MessageType = 3;


                } else if (messageType.equals("4")) {

                    /*
                     * Follow
                     */

                    MessageType = 4;

                } else if (messageType.equals("5")) {


                    /*
                     * Audio
                     */
                    MessageType = 5;


                    downloadStatus = (int) mapMessage.get("downloadStatus");


                    if (downloadStatus == 0) {


                        size = (int) mapMessage.get("dataSize");
                    }

                } else if (messageType.equals("6")) {

                    /*
                     *Sticker
                     */


                    MessageType = 6;
                } else if (messageType.equals("7")) {

                    /*
                     * Doodle
                     */


                    MessageType = 7;

                    downloadStatus = (int) mapMessage.get("downloadStatus");
                    if (downloadStatus == 0) {
                        thumbnailPath = (String) mapMessage.get("thumbnailPath");
                        size = (int) mapMessage.get("dataSize");
                    }

                } else if (messageType.equals("8")) {

                    /*
                     * Giphy
                     */


                    MessageType = 8;
                } else if (messageType.equals("9")) {


                    /*
                     * Document
                     */

                    MessageType = 9;
                    downloadStatus = (int) mapMessage.get("downloadStatus");
                    if (downloadStatus == 0) {

                        size = (int) mapMessage.get("dataSize");
                    }
                    extension = (String) mapMessage.get("extension");
                    mimeType = (String) mapMessage.get("mimeType");
                    fileName = (String) mapMessage.get("fileName");
                } else if (messageType.equals("13")) {


                    /*
                     * Post
                     */

                    MessageType = 13;

                    postId = (String) mapMessage.get("postId");
                    postTitle = (String) mapMessage.get("postTitle");
                    postType = (int) mapMessage.get("postType");
                }


                if (timerStarted) {

                    try {

                        expectedDTime = (long) mapMessage.get("expectedDTime");
                    } catch (ClassCastException e) {


                        expectedDTime = (int) mapMessage.get("expectedDTime");
                    } catch (NullPointerException e) {


                        continue;
                    }

                    // destTime = expectedDTime - Long.parseLong(Utilities.tsInGmt());
                    destTime = expectedDTime - Utilities.getGmtEpoch();
                    if (destTime < 0) {
                        /*
                         * Already expired
                         */

                        new removeMessage().execute(documentId, id);


                    } else {
                        if (message != null) {



                            /*
                             * Have made this false intentionally
                             */
                            loadFromDb(MessageType, isSelf, message, senderName, ts, deliveryStatus, id,


                                    status, downloadStatus, thumbnailPath, size, (destTime / 1000), false, isDTimeTag, extension, mimeType, fileName, postId, postTitle, postType);


                        }
                    }
                } else {


                    if (message != null) {


                        loadFromDb(MessageType, isSelf, message, senderName, ts, deliveryStatus, id, status,

                                downloadStatus, thumbnailPath, size, destTime, false, isDTimeTag, extension, mimeType, fileName, postId, postTitle, postType);


                    }

                }
                senderName = null;
                message = null;
                ts = null;
                deliveryStatus = null;
                id = null;

            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }


        size = (size > 10) ? (size - 10) : (0);

        MessageType = 0;


        if (sendMessage.getText().length() == 1) {
            sendMessage.setText("");
        }

        if (pDialog.isShowing()) {
            // pDialog.dismiss();
            Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();


            if (context instanceof Activity) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                        pDialog.dismiss();
                    }
                } else {


                    if (!((Activity) context).isFinishing()) {
                        pDialog.dismiss();
                    }
                }
            } else {


                try {
                    pDialog.dismiss();
                } catch (final IllegalArgumentException e) {
                    e.printStackTrace();

                } catch (final Exception e) {
                    e.printStackTrace();

                }
            }


        }

        arrMessage = null;


    }

    /*
     * To add the messages loaded from the db to the list of messages in the UI
     */
    @SuppressWarnings("unchecked")

    public void loadFromDb(int messageType, boolean isSelf, String message,
                           String senderName, String timestamp, String deliveryStatus, String id,
                           int status, int downloadStatus, String thumbnailPath, int datasize, long destTime, boolean timerStarted, boolean isDTag, String extension, String mimeType, String fileName, String postId, String postTitle, int postType) {


        String date = Utilities.formatDate(Utilities.tsFromGmt(timestamp));


        SecretChatMessageItem message_item = new SecretChatMessageItem();

        if (isSelf) {


            message_item.setSenderName(senderName);
        } else {


            message_item.setSenderName(receiverName);
        }
        message_item.setReceiverUid(receiverUid);
        message_item.setDownloading(false);

        message_item.setDownloadStatus(downloadStatus);

        if (downloadStatus == 0) {

            if (messageType == 1 || messageType == 2 || messageType == 7) {
                message_item.setThumbnailPath(thumbnailPath);
            }
            String size;


            if (datasize < 1024) {


                size = datasize + " bytes";

            } else if (datasize >= 1024 && datasize <= 1048576) {


                size = (datasize / 1024) + " KB";

            } else {


                size = (datasize / 1048576) + " MB";
            }


            message_item.setSize(size);


        }

        message_item.setIsSelf(isSelf);

        message_item.setdTime(destTime);

        message_item.setTimerStarted(timerStarted);

        message_item.setTS(date.substring(0, 9));

        message_item.setMessageId(id);
        message_item.setMessageDateOverlay(date.substring(9, 24));
        message_item.setMessageDateGMTEpoch(Long.parseLong(new Utilities().gmtToEpoch(timestamp)));


        message_item.setDeliveryStatus(deliveryStatus);


        message_item.setMessageType(Integer.toString(messageType));


        if (messageType == 0) {


            if (!isDTag ||
                    !message.equals(getResources().getString(R.string.youAreInvited) + " " +
                            receiverName + " " + getResources().getString(R.string.JoinSecretChat))) {
                //    isInviteMessage=true;

                message_item.setTextMessage(message);

                message_item.setDTimeTag(isDTag);

            } else {
                return;
            }


        } else if (messageType == 1) {

            message_item.setImagePath(message);


        } else if (messageType == 2) {


            try {


                message_item.setVideoPath(message);


            } catch (NullPointerException e) {

                e.printStackTrace();
            }


        } else if (messageType == 3) {


            message_item.setPlaceInfo(message);


        } else if (messageType == 4) {


            message_item.setContactInfo(message);

        } else if (messageType == 5) {

            try {


                message_item.setAudioPath(message);


            } catch (NullPointerException e) {

                e.printStackTrace();
            }


        } else if (messageType == 6) {

            message_item.setStickerUrl(message);

        } else if (messageType == 7) {

            message_item.setImagePath(message);


        } else if (messageType == 8) {


            try {

                message_item.setGifUrl(message);

            } catch (NullPointerException e) {

                e.printStackTrace();
            }

        } else if (messageType == 9) {


            try {
                message_item.setMimeType(mimeType);

                message_item.setExtension(extension);
                message_item.setFileType(findFileTypeFromExtension(extension));
                message_item.setDocumentUrl(message);
                message_item.setFileName(fileName);
            } catch (NullPointerException e) {

                e.printStackTrace();
            }

        } else if (messageType == 13) {


            message_item.setPostType(postType);
            message_item.setPostTitle(postTitle);
            message_item.setImagePath(message);
            message_item.setPostId(postId);


        }
        mChatData.add(0, message_item);


        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                mAdapter.notifyItemInserted(0);
            }
        });

        if (status == 0) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        llm.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, 0);

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                }
            });

        }

        message_item = null;
        message = null;
        senderName = null;
        timestamp = null;
        deliveryStatus = null;
        id = null;

    }


    /*
     * To find the position at which to insert the newly received message
     */

//    @Override
//    public void onBackPressed() {
//
//        if (toddle != null && toddle.isShowing()) {
//            toddle.dismiss();
//        }
//
//
//        if (AppController.getInstance().isActiveOnACall() && AppController.getInstance().isFirstTimeAfterCallMinimized()) {
//
//
//            AppController.getInstance().setFirstTimeAfterCallMinimized(false);
//
//            Intent i = new Intent(SecretChatMessageScreen.this, LandingActivity.class);
//
//
//            i.putExtra("userId", AppController.getInstance().getUserId());
//            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//            startActivity(i);
//
//
//        }
//
//        super.onBackPressed();
//        this.supportFinishAfterTransition();
//    }

    @Override
    public void onBackPressed() {

        if (rlAttach.getVisibility() == View.VISIBLE) {
            rlAttach.setVisibility(View.GONE);
            return;
        }

        if (AppController.getInstance().isActiveOnACall()) {
            super.onBackPressed();
        } else {

            if (fromNotification) {

                if (AppController.getInstance().getActiveActivitiesCount() == 1) {
                    Intent i = new Intent(SecretChatMessageScreen.this, LandingActivity.class);


                    i.putExtra("userId", AppController.getInstance().getUserId());
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(i);


                }
            }

            if (toddle != null && toddle.isShowing()) {
                toddle.dismiss();
            }


            super.onBackPressed();
            this.supportFinishAfterTransition();

        }


    }
///////////////////////////////////////////////////////////////////////////////////////////////////////////

    public String convert24to12hourformat(String d) {

        String datein12hour = null;

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.US);
            final Date dateObj = sdf.parse(d);


            datein12hour = new SimpleDateFormat("h:mm a", Locale.US).format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }


        return datein12hour;

    }

    /*
     * To find the date to be shown for the date overlay on top,which shows the date of message currently ontop
     */
    private String findOverlayDate(String date) {


        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MMM/yyyy", Locale.US);


            String m1 = "", m2 = "";


            String month1, month2;

            String d1, d2;


            d1 = sdf.format(new Date(System.currentTimeMillis() - AppController.getInstance().getTimeDelta()));

            d2 = date;


            month1 = d1.substring(7, 10);


            month2 = d2.substring(7, 10);

            if (month1.equals("Jan")) {
                m1 = "01";
            } else if (month1.equals("Feb")) {
                m1 = "02";
            } else if (month2.equals("Mar")) {
                m2 = "03";
            } else if (month1.equals("Apr")) {
                m1 = "04";
            } else if (month1.equals("May")) {
                m1 = "05";
            } else if (month1.equals("Jun")) {
                m1 = "06";
            } else if (month1.equals("Jul")) {
                m1 = "07";
            } else if (month1.equals("Aug")) {
                m1 = "08";
            } else if (month1.equals("Sep")) {
                m1 = "09";
            } else if (month1.equals("Oct")) {
                m1 = "10";
            } else if (month1.equals("Nov")) {
                m1 = "11";
            } else if (month1.equals("Dec")) {
                m1 = "12";
            }


            if (month2.equals("Jan")) {
                m2 = "01";
            } else if (month2.equals("Feb")) {
                m2 = "02";
            } else if (month1.equals("Mar")) {
                m1 = "03";
            } else if (month2.equals("Apr")) {
                m2 = "04";
            } else if (month2.equals("May")) {
                m2 = "05";
            } else if (month2.equals("Jun")) {
                m2 = "06";
            } else if (month2.equals("Jul")) {
                m2 = "07";
            } else if (month2.equals("Aug")) {
                m2 = "08";
            } else if (month2.equals("Sep")) {
                m2 = "09";
            } else if (month2.equals("Oct")) {
                m2 = "10";
            } else if (month2.equals("Nov")) {
                m2 = "11";
            } else if (month2.equals("Dec")) {
                m2 = "12";
            }
            month1 = null;
            month2 = null;


            if (sdf.format(new Date(System.currentTimeMillis() - AppController.getInstance().getTimeDelta())).equals(date)) {


                m2 = null;
                m1 = null;
                d2 = null;
                d1 = null;
                sdf = null;
                return "Today";
            } else if ((Integer.parseInt(d1.substring(11) + m1 + d1.substring(4, 6)) - Integer.parseInt(d2.substring(11) + m2 + d2.substring(4, 6))) == 1) {

                m2 = null;
                m1 = null;
                d2 = null;
                d1 = null;
                sdf = null;
                return "Yesterday";

            } else {

                m2 = null;
                m1 = null;
                d2 = null;
                d1 = null;
                sdf = null;
                return date;
            }
        } catch (Exception e) {


            return date;

        }

    }

    /*
     * To add message to send in the list of messages
     */
    private void addMessageToSendInUi(JSONObject obj, boolean isImageOrVideoOrAudio, int messageType, Uri uri,
                                      boolean isDTimeTag, boolean creatingSecretChat, boolean toDelete) {
        if (!creatingSecretChat) {

            String tempDate = Utilities.formatDate(Utilities.tsFromGmt(tsForServer));


            SecretChatMessageItem message = new SecretChatMessageItem();

            message.setSenderName(userName);
            message.setIsSelf(true);
            message.setTS(tempDate.substring(0, 9));
            message.setDTimeTag(isDTimeTag);
            message.setdTime(dTime);
            message.setTimerStarted(false);
            message.setMessageDateOverlay(tempDate.substring(9, 24));

            message.setMessageDateGMTEpoch(Long.parseLong(tsForServerEpoch));


            message.setDeliveryStatus("0");
            message.setMessageId(tsForServerEpoch);
            message.setDownloadStatus(1);

            /*
             *
             *
             * db will contain upload status field only for the image ,video or audio item.
             *
             *
             * */

            String mess_str;
            if (messageType == 0) {


                if (isDTimeTag) {



                    /*
                     * Destruction time changed tag
                     */


                    mess_str = getString(R.string.Timer_set_to) + " " + dTime;

                    message.setTextMessage(mess_str);

                    String time = String.valueOf(dTime);
                    for (int i = 0; i < dTimeForDB.length; i++) {
                        if (time.equals(dTimeForDB[i])) {
                            dTimeTv.setText(dTimeToShow[i]);

                            if (i == 0) {


                                mess_str = getString(R.string.Timer_set_off);

                            } else {


                                mess_str = getString(R.string.Timer_set_to) + " " + dTimeOptions[i];


                            }
                            break;
                        }
                    }

                } else {


                    /*
                     * Text message
                     */

                    mess_str = sendMessage.getText().toString().trim();

                }


                message.setTextMessage(mess_str);


                message.setMessageType("0");


                Map<String, Object> map = new HashMap<>();


                map.put("isDTag", isDTimeTag);
                map.put("message", mess_str);
                map.put("messageType", "0");
                map.put("isSelf", true);
                map.put("from", userName);


                map.put("Ts", tsForServer);
                map.put("deliveryStatus", "0");
                map.put("id", tsForServerEpoch);
                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", false);


                AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);

                map = null;


            } else if (messageType == 1) {


                /*
                 * receiverImage
                 */

                message.setImagePath(picturePath);

                message.setMessageType("1");

                message.setImageUrl(imageUrl);


                Map<String, Object> map = new HashMap<>();
                map.put("message", picturePath);
                map.put("messageType", "1");
                map.put("isSelf", true);
                map.put("downloadStatus", 1);
                map.put("from", userName);
                map.put("Ts", tsForServer);
                map.put("deliveryStatus", "0");
                map.put("id", tsForServerEpoch);

                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", false);

                AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);
                map = null;


            } else if (messageType == 2) {

                /*
                 * Video
                 */


                message.setVideoPath(videoPath);
                message.setMessageType("2");


                Map<String, Object> map = new HashMap<>();
                map.put("message", videoPath);
                map.put("messageType", "2");
                map.put("isSelf", true);
                map.put("downloadStatus", 1);
                map.put("from", userName);
                map.put("Ts", tsForServer);
                map.put("deliveryStatus", "0");
                map.put("id", tsForServerEpoch);
                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", false);


                AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);
                map = null;


            } else if (messageType == 3) {


                /*
                 * Location
                 */
                message.setPlaceInfo(placeString);
                message.setMessageType("3");


                Map<String, Object> map = new HashMap<>();


                map.put("message", placeString);

                map.put("messageType", "3");
                map.put("isSelf", true);
                map.put("from", userName);
                map.put("Ts", tsForServer);
                map.put("deliveryStatus", "0");
                map.put("id", tsForServerEpoch);
                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", false);


                AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);


                map = null;


            } else if (messageType == 4) {


                /*
                 * Follow
                 */


                message.setContactInfo(contactInfo);
                message.setMessageType("4");


                Map<String, Object> map = new HashMap<>();
                map.put("message", contactInfo);

                map.put("messageType", "4");
                map.put("isSelf", true);
                map.put("from", userName);
                map.put("Ts", tsForServer);
                map.put("deliveryStatus", "0");
                map.put("id", tsForServerEpoch);

                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", false);

                AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);


                map = null;


            } else if (messageType == 5) {

                /*
                 * Audio
                 */
                message.setAudioPath(audioPath);
                message.setMessageType("5");


                Map<String, Object> map = new HashMap<>();
                map.put("message", audioPath);
                map.put("messageType", "5");
                map.put("isSelf", true);
                map.put("from", userName);
                map.put("Ts", tsForServer);
                map.put("downloadStatus", 1);
                map.put("deliveryStatus", "0");
                map.put("id", tsForServerEpoch);

                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", false);

                AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);


                map = null;


            } else if (messageType == 6) {


                /*
                 *Stickers
                 */


                message.setStickerUrl(stickerUrl);
                message.setMessageType("6");

                Map<String, Object> map = new HashMap<>();
                map.put("message", stickerUrl);
                map.put("messageType", "6");
                map.put("isSelf", true);
                map.put("from", userName);
                map.put("Ts", tsForServer);
                map.put("deliveryStatus", "0");
                map.put("id", tsForServerEpoch);
                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", false);

                AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);

                map = null;


            } else if (messageType == 7) {


                /*
                 * Doodle
                 */
                message.setImagePath(picturePath);

                message.setMessageType("7");

                message.setImageUrl(imageUrl);


                Map<String, Object> map = new HashMap<>();
                map.put("message", picturePath);
                map.put("messageType", "7");
                map.put("downloadStatus", 1);
                map.put("isSelf", true);
                map.put("from", userName);
                map.put("Ts", tsForServer);
                map.put("deliveryStatus", "0");
                map.put("id", tsForServerEpoch);
                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", false);

                AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);
                map = null;


            } else if (messageType == 8) {

                /*
                 * Gif
                 */


                message.setGifUrl(gifUrl.trim());
                message.setMessageType("8");


                Map<String, Object> map = new HashMap<>();
                map.put("message", gifUrl.trim());
                map.put("messageType", "8");
                map.put("isSelf", true);
                map.put("from", userName);
                map.put("Ts", tsForServer);
                map.put("deliveryStatus", "0");
                map.put("id", tsForServerEpoch);

                /*
                 * For secret chat exclusively
                 */

                map.put("dTime", dTime);
                map.put("timerStarted", false);

                AppController.getInstance().getDbController().addNewChatMessageAndSort(documentId, map, tsForServer, secretId);

                map = null;


            }
            mChatData.add(message);


            message = null;


            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    //llm.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, 0);
                    try {
                        mAdapter.notifyItemInserted(mChatData.size() - 1);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {


                                llm.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, 0);

                            }
                        }, 500);

                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }
            });


            if (inviteVisible) {
                inviteVisible = false;
                invite_rl.setVisibility(View.GONE);
                db.updateSecretInviteImageVisibility(documentId, false);

            }
        }

        if (button01pos == 1) {

            sendButton.setImageDrawable(drawable1);

            button01pos = 0;
        }


        /*
         *
         *
         *
         * Need to store all the messages in db so that incase internet
         * not present then has to resend all messages whenever internet comes back
         *
         *
         * */


        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.put("from", userId);
        mapTemp.put("to", receiverUid);

        mapTemp.put("toDocId", documentId);


        mapTemp.put("id", tsForServerEpoch);

        mapTemp.put("timestamp", tsForServerEpoch);


        /*
         * Exclusively for secret chat
         */
        mapTemp.put("dTime", dTime);
        mapTemp.put("secretId", secretId);

        String type = Integer.toString(messageType);


        mapTemp.put("type", type);


        mapTemp.put("name", AppController.getInstance().getUserName());


        if ((type).equals("0")) {


            mapTemp.put("message", sendMessage.getText().toString().trim());


        } else if (type.equals("1")) {

            mapTemp.put("message", picturePath);


            picturePath = null;
        } else if ((type).equals("2")) {

            mapTemp.put("message", videoPath);


            videoPath = null;

        } else if ((type).equals("3")) {


            mapTemp.put("message", placeString);


            placeString = null;


        } else if ((type).equals("4")) {

            mapTemp.put("message", contactInfo);


            contactInfo = null;

        } else if ((type).equals("5")) {

            mapTemp.put("message", audioPath);
            if (!toDelete)
                mapTemp.put("toDelete", false);
            audioPath = null;


        } else if ((type).equals("6")) {

            mapTemp.put("message", stickerUrl);


            stickerUrl = null;


        } else if ((type).equals("7")) {

            mapTemp.put("message", picturePath);


            picturePath = null;


        } else if ((type).equals("8")) {

            mapTemp.put("message", gifUrl);


            gifUrl = null;


        }
        AppController.getInstance().getDbController().addUnsentMessage(AppController.getInstance().getunsentMessageDocId(), mapTemp);


        /*s
         *
         *
         * emit directly if not image or video or audio
         *
         *
         * */


        if (!isImageOrVideoOrAudio) {


            try {


                obj.put("name", AppController.getInstance().getUserName());


            } catch (JSONException e) {
                e.printStackTrace();
            }


            HashMap<String, Object> map = new HashMap<>();
            map.put("messageId", tsForServerEpoch);
            map.put("docId", documentId);


            AppController.getInstance().publishChatMessage(MqttEvents.Message.value + "/" + receiverUid, obj, 1, false, map);


        } else {

            /*
             *
             * if is an image or a video than have to upload and for that a dummy file in memory is
             * created which contains the compressed version of file to be send
             * */


            uploadFile(uri, userId + tsForServerEpoch, messageType, obj, toDelete, null);


        }


        obj = null;
        mapTemp = null;


        sendMessage.setText("");
        MessageType = 0;


    }




    /*
     *
     * To check for access gallery permission to select image
     * */

    /*
     * To calculate the required dimensions of image withoutb actually loading the bitmap in to the memory
     */
    private int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {


        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;


            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
    /*
     *
     * To check for access gallery permission to select video
     * */

    private Bitmap decodeSampledBitmapFromResource(String pathName,
                                                   int reqWidth, int reqHeight) {


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);


        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);


        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(pathName, options);

    }

//    private void checkCameraPermissionVideo() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.checkSelfPermission( SecretChatMessageScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED)
//
//
//            {
//
//                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
//                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);//mms quality video not hd
//                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120);//max 120s video
//                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 26214400L);//max 25 mb size recording
//
//
//                startActivityForResult(intent, RESULT_CAPTURE_VIDEO);
//            } else {
//
//
//                requestReadVideoPermission(0);
//            }
//        } else {
//
//            requestCameraPermissionVideo();
//        }
//
//    }

    /*
     * Check camera permission to capture video
     */

    private int getPositionOfMessage(long ts) {


        for (int i = mChatData.size() - 1; i >= 0; i--) {


            if (mChatData.get(i).getMessageDateGMTEpoch() < ts) {


                return (i + 1);


            }
        }

        return 0;

    }

    /*
     * Uploading images and video and audio to  the server
     */
    @SuppressWarnings("TryWithIdenticalCatches,all")

    private void uploadFile(final Uri fileUri, final String name, final int messageType, final JSONObject obj, final boolean toDeleteFile, final String extension) {


        FileUploadService service =
                ServiceGenerator.createService(FileUploadService.class);


        final File file = FileUtils.getFile(this, fileUri);

        String url = null;
        if (messageType == 1) {

            url = name + ".jpg";


        } else if (messageType == 2) {

            url = name + ".mp4";


        } else if (messageType == 5) {

            url = name + ".3gp";


        } else if (messageType == 7) {

            url = name + ".jpg";


        } else if (messageType == 9) {

            url = name + "." + extension;


        }

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);


        MultipartBody.Part body =
                MultipartBody.Part.createFormData("photo", url, requestFile);


        String descriptionString = getString(R.string.string_803);
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);


        Call<ResponseBody> call = service.upload(description, body);


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {

                /*
                 *
                 *
                 * has to get url from the server in response
                 *
                 *
                 * */


                try {


                    if (response.code() == 200) {


                        String url = null;
                        if (messageType == 1) {

                            url = name + ".jpg";


                        } else if (messageType == 2) {

                            url = name + ".mp4";


                        } else if (messageType == 5) {

                            url = name + ".3gp";


                        } else if (messageType == 7) {

                            url = name + ".jpg";


                        } else if (messageType == 9) {

                            url = name + "." + extension;


                        }
                        obj.put("payload", Base64.encodeToString((ApiOnServer.CHAT_UPLOAD_PATH + url).getBytes("UTF-8"), Base64.DEFAULT));
                        obj.put("dataSize", file.length());
                        obj.put("timestamp", new Utilities().gmtToEpoch(Utilities.tsInGmt()));

                        if (toDeleteFile) {
                            File fdelete = new File(fileUri.getPath());
                            if (fdelete.exists()) fdelete.delete();

                        }

                    } else {


                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, R.string.string_63, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                /*
                 *
                 *
                 * emitting to the server the values after the file has been uploaded
                 *
                 * */


                try {
                    obj.put("name", AppController.getInstance().getUserName());

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("messageId", obj.getString("id"));
                    map.put("docId", documentId);


                    AppController.getInstance().publishChatMessage(MqttEvents.Message.value + "/" + receiverUid, obj, 1, false, map);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


                t.printStackTrace();

            }
        });
    }

    /*
     * To save the byte array received in to file
     */
    @SuppressWarnings("all")
    public File convertByteArrayToFile(byte[] data, String name, String extension) {


        File file = null;

        try {


            File folder = new File(getExternalFilesDir(null) + ApiOnServer.CHAT_UPLOAD_THUMBNAILS_FOLDER);

            if (!folder.exists() && !folder.isDirectory()) {
                folder.mkdirs();
            }


            file = new File(getExternalFilesDir(null) + ApiOnServer.CHAT_UPLOAD_THUMBNAILS_FOLDER, name + extension);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return file;

    }

    /*
     *
     * To check for camera permission
     * */
    private void checkCameraPermissionImage(int type) {


        /*
         * Type 0--image capture
         *
         *
         * Type 1--camera capture
         *
         */


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.checkSelfPermission(SecretChatMessageScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if (intent.resolveActivity(getPackageManager()) != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri(type));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                    } else {


                        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        for (ResolveInfo resolveInfo : resInfoList) {
                            String packageName = resolveInfo.activityInfo.packageName;
                            grantUriPermission(packageName, imageUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        }


                    }

                    if (type == 0) {
//                        startActivityForResult(intent, RESULT_CAPTURE_MEDIA);
                        openCamera();

                    } else if (type == 1) {
                        startActivityForResult(intent, RESULT_CAPTURE_WALLPAPER);
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(root, R.string.string_61,
                            Snackbar.LENGTH_SHORT);
                    snackbar.show();


                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
            } else {


                /*
                 *permission required to save the image captured
                 */
                if (type == 0) {
                    requestReadImagePermission(0);
                } else if (type == 1) {
                    requestReadImagePermission(6);
                }

            }


        } else {

            requestCameraPermissionImage(type);
        }

    }


    /*
     *
     * To request access location permission
     * */

//    private void checkReadImage() {
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED) {
//
//
//            showGalleryPopup();
//        } else {
//
//            requestReadImagePermission(1);
//        }
//
//    }

    private void checkReadImage(int k) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            if (k == 0) {
                showGalleryPopup();
            } else if (k == 1) {


                showDocumentPopup();

            } else if (k == 2) {


                /*
                 *
                 *For adding of the wallpapers
                 */

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.SelectWallpaper)), RESULT_LOAD_WALLPAPER);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.SelectWallpaper)), RESULT_LOAD_WALLPAPER);
                }

            }


        } else {
            if (k == 0) {
                requestReadImagePermission(1);
            } else if (k == 1) {

                requestReadImagePermission(4);
            } else if (k == 2) {

                requestReadImagePermission(5);
            }
        }

    }


//       /*
//    *
//    * To request access camera permission to capture image
//    * */
//
//    private void requestCameraPermissionVideo() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.CAMERA)) {
//
//            Snackbar snackbar = Snackbar.make(root, R.string.string_65,
//                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    ActivityCompat.requestPermissions( SecretChatMessageScreen.this, new String[]{Manifest.permission.CAMERA},
//                            25);
//                }
//            });
//
//
//            snackbar.show();
//
//
//            View view = snackbar.getView();
//            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
//            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//
//
//        } else {
//
//
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
//                    25);
//        }
//    }



    /*
     *
     * To request access location permission to capture video
     * */

    private void checkReadVideo() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("video/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);

            startActivityForResult(intent, RESULT_LOAD_VIDEO);

        } else {

            requestReadVideoPermission(1);
        }

    }



    /*
     *
     * To request access contacts permission
     * */

    /*
     *
     * To check for access gallery permission to select audio
     * */
    private void checkReadAudio() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            Intent intent_upload = new Intent();
            intent_upload.setType("audio/*");
            intent_upload.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent_upload, REQUEST_SELECT_AUDIO);


        } else {

            requestReadAudioPermission();
        }

    }

    /*Launch the location picker activity*/
    private void launchLocationPickerIntent() {
        Intent intent = new Intent(SecretChatMessageScreen.this, LocationFetchActivity.class);
        startActivityForResult(intent, RESULT_SHARE_LOCATION);
    }


    /*
     *
     * To request update contacts permission
     * */

    /*
     *
     * To check for the access location permission
     * */
    @SuppressWarnings("TryWithIdenticalCatches")
    private void checkLocationAccessPermission() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {


            if (new GPSTracker(SecretChatMessageScreen.this).canGetLocation()) {

                launchLocationPickerIntent();

            } else {


                Snackbar snackbar = Snackbar.make(root, R.string.string_58,
                        Snackbar.LENGTH_SHORT);
                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        } else {

            requestLocationPermission();
        }

    }

    /*
     *
     * To check for the update contacts permission
     * */
    public void checkWriteContactPermission(String contactInfo) {
        try {
            String parts[] = contactInfo.split("@@");

            String userId = parts[2];

            Intent intent = new Intent(SecretChatMessageScreen.this, ProfileActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     *
     * To request access gallery permission to select audio
     * */

    private void requestLocationPermission() {


        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            Snackbar snackbar = Snackbar.make(root, R.string.string_64,
                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            22);
                }
            });


            snackbar.show();


            View view = snackbar.getView();
            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    22);
        }
    }



    /*
     *
     * To save the contact details
     * */

    private void requestCameraPermissionImage(int type) {

        if (type == 0) {

            /*
             *Normal image capture
             */


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_65,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.CAMERA},
                                24);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        24);
            }

        } else if (type == 1) {

            /*
             * Wallpaper image capture
             */

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_65,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.CAMERA},
                                44);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                        44);
            }
        }
    }

    private void requestReadImagePermission(int k) {
        if (k == 1) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_67,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                26);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        26);
            }
        } else if (k == 0) {




            /*
             * For capturing the image permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(SecretChatMessageScreen.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(SecretChatMessageScreen.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_981,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                37);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        37);
            }


        } else if (k == 2) {




            /*
             * For capturing the image permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(SecretChatMessageScreen.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(SecretChatMessageScreen.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_881,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                47);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        47);
            }


        } else if (k == 3) {



            /*
             * For capturing the image permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(SecretChatMessageScreen.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(SecretChatMessageScreen.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_882,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                82);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        82);
            }
        } else if (k == 4) {



            /*
             * For capturing the image permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(SecretChatMessageScreen.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(SecretChatMessageScreen.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_883,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                85);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        85);
            }


        } else if (k == 5) {



            /*
             * For selecting the wallpaper permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(SecretChatMessageScreen.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(SecretChatMessageScreen.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.WallpaperAccess,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                43);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        43);
            }


        } else if (k == 6) {




            /*
             * For capturing the image permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(SecretChatMessageScreen.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(SecretChatMessageScreen.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.CameraAccess,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                45);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        45);
            }


        } else if (k == 7) {



            /*
             * For saving the doodle drawn permission
             */
            if (ActivityCompat.shouldShowRequestPermissionRationale(SecretChatMessageScreen.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(SecretChatMessageScreen.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.WallpaperDrawn,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                49);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        49);
            }


        }
    }

    /*
     *
     * To request access gallery permission to select video
     * */

    private void requestReadVideoPermission(int k) {

        if (k == 1) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_67,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                27);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        27);

            }
        } else {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_982,
                        Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                38);

                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            } else {


                ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        38);

            }

        }
    }

    private void requestReadAudioPermission() {


        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            Snackbar snackbar = Snackbar.make(root, R.string.string_67,
                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            28);
                }
            });


            snackbar.show();


            View view = snackbar.getView();
            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);


        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    28);
        }

    }

    @SuppressWarnings("unchecked")

    private void saveContact(String contactInfo) {


        String contactName = "", contactNumber = "";


        try {


            String parts[] = contactInfo.split("@@");


            contactName = parts[0];


            String arr[] = parts[1].split("/");


            contactNumber = arr[0];
            arr = null;
            parts = null;


            if (contactName == null || contactName.isEmpty()) {
                contactName = getString(R.string.string_247);
            } else if (contactNumber == null || contactNumber.isEmpty()) {
                contactNumber = getString(R.string.string_246);
            }

        } catch (StringIndexOutOfBoundsException e) {
            contactNumber = getString(R.string.string_246);
        }

        Intent intentInsertEdit = new Intent(Intent.ACTION_INSERT_OR_EDIT);

        intentInsertEdit.setType(ContactsContract.Contacts.CONTENT_ITEM_TYPE);


        intentInsertEdit.putExtra(ContactsContract.Intents.Insert.PHONE, contactNumber);


        intentInsertEdit.putExtra(ContactsContract.Intents.Insert.NAME, contactName);


        intentInsertEdit.putExtra(ContactsContract.Intents.Insert.PHONE_TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE);


        intentInsertEdit.putExtra("finishActivityOnSaveCompleted", true);

        startActivity(intentInsertEdit, ActivityOptionsCompat.makeSceneTransitionAnimation(SecretChatMessageScreen.this).toBundle());


        contactInfo = null;
        contactName = null;
        contactNumber = null;

    }

    @SuppressWarnings("all")
    private void createDoodleUri(byte[] data) {
        String name = Utilities.tsInGmt();
        name = new Utilities().gmtToEpoch(name);


        File folder = new File(getExternalFilesDir(null) + ApiOnServer.CHAT_DOODLES_FOLDER);

        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }


        File file = new File(getExternalFilesDir(null) + ApiOnServer.CHAT_DOODLES_FOLDER, name + ".jpg");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        this.picturePath = file.getAbsolutePath();
        try {
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(data);
            fos.flush();
            fos.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        name = null;
        folder = null;


    }


    /*
     * Result of request permission
     */

    private void requestAudioCall() {

        Map<String, Object> callItem = new HashMap<>();


        String callId = AppController.getInstance().randomString();


        callItem.put("receiverName", receiverName);
        callItem.put("receiverImage", receiverImage);
        callItem.put("receiverUid", receiverUid);
        callItem.put("callTime", Utilities.tsInGmt());
        callItem.put("callInitiated", true);
        callItem.put("callId", callId);
        callItem.put("callType", getResources().getString(R.string.AudioCall));
        callItem.put("receiverIdentifier", receiverIdentifier);
        callItem.put("isStar", isStar);
        db.addNewCall(AppController.getInstance().getCallsDocId(), callItem);

//        Common.callerName = receiverName;
//
//
//        CallingApis.initiateCall(SecretChatMessageScreen.this, receiverUid, receiverName, receiverImage,
//                "0", receiverIdentifier, callId, isStar);

    }

    private void requestVideoCall() {


        Map<String, Object> callItem = new HashMap<>();
        String callId = AppController.getInstance().randomString();

        callItem.put("receiverName", receiverName);
        callItem.put("receiverImage", receiverImage);
        callItem.put("receiverUid", receiverUid);
        callItem.put("callTime", Utilities.tsInGmt());
        callItem.put("callInitiated", true);
        callItem.put("callId", callId);
        callItem.put("callType", getResources().getString(R.string.VideoCall));
        callItem.put("receiverIdentifier", receiverIdentifier);
        callItem.put("isStar", isStar);
        db.addNewCall(AppController.getInstance().getCallsDocId(), callItem);
//        Common.callerName = receiverName;
//
//
//        CallingApis.initiateCall(SecretChatMessageScreen.this, receiverUid, receiverName, receiverImage,
//                "1", receiverIdentifier, callId, isStar);
    }

    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e("SecurityException", "Google Play Services not available.");
        }
    }

    private void openDTimeDialogBox() {

        final Dialog dTimerDialoglog = new Dialog(this);
        dTimerDialoglog.setTitle(getResources().getString(R.string.SelectTimer));
        dTimerDialoglog.setContentView(R.layout.secretchat_dtimer_dialog);

        TextView selectDTimeTv = (TextView) dTimerDialoglog.findViewById(R.id.select_dTimetv);
        NumberPicker NP_dTime = (NumberPicker) dTimerDialoglog.findViewById(R.id.dTimeNp);

        final int[] position = new int[1];
        final int[] prePos = new int[1];

        NP_dTime.setMinValue(0);
        NP_dTime.setMaxValue(dTimeOptions.length - 1);
        NP_dTime.setDisplayedValues(dTimeOptions);
        NP_dTime.setWrapSelectorWheel(true);

        String destTime = String.valueOf(dTime);
        for (int i = 0; i < dTimeForDB.length; i++) {
            if (destTime.equals(dTimeForDB[i])) {

                final int finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        NP_dTime.setValue(finalI);
                        prePos[0] = finalI;
                        position[0] = finalI;
                    }
                });
                break;
            }
        }


        NP_dTime.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {


                position[0] = newVal;
            }
        });

        selectDTimeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prePos[0] != position[0]) {

                    dTimeTv.setText(dTimeToShow[position[0]]);
                    dTime = Long.valueOf(dTimeForDB[position[0]]);
                    db.updateDestructTime(documentId, dTime);

                    addMessageToSendInUi(setMessageToSend(false, 0, null, null), false, 0, null, true, false, true);

                }
                dTimerDialoglog.dismiss();
            }
        });

        dTimerDialoglog.show();
    }


    /*
     * To show the popup for the secret chat
     */

    public void deleteParticularMessage(String docId, final int position, String messageId) {

        if (mChatData != null) {


            if (messageHeader.getVisibility() == View.VISIBLE) {


                if (currentlySelectedMessage.getMessageId().equals(messageId)) {
                    hideMessageHeader();

                }
            }


            try {
                int mPosition = position;

                for (int i = 0; i < mChatData.size(); i++) {
                    if (mChatData.get(i).getMessageId().equals(messageId)) {
                        mPosition = i;
                        break;
                    }
                }

                mChatData.remove(mPosition);

                final int finalMPosition = mPosition;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (finalMPosition == 0) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            mAdapter.notifyItemRemoved(finalMPosition);
                        }
                    }
                });

                mAdapter.notifyItemChanged(mPosition);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        new removeMessage().execute(docId, messageId);


    }

    private void updateDTimeInToolbar() {
        /*
         *Have to show the current dTimer value on top
         */
        String destTime = String.valueOf(dTime);

        for (int i = 0; i < dTimeForDB.length; i++) {

            if (destTime.equals(dTimeForDB[i])) {


                final int finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dTimeTv.setText(dTimeToShow[finalI]);
                    }
                });


                break;
            }
        }


    }

    public void setTimer(String messageId, long expectedDTime) {


        long temp = Utilities.getGmtEpoch();


        new startTimer().execute(Long.parseLong(messageId), temp + expectedDTime);

    }

    private void showDoodlePopup() {


        if (!toddle.isShowing()) {
            if (toddle.isKeyBoardOpen()) {
                toddle.showAtBottom();

            } else {
                sendMessage.setFocusableInTouchMode(true);
                sendMessage.requestFocus();
                final InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(sendMessage, InputMethodManager.SHOW_IMPLICIT);
                toddle.showAtBottomPending();

            }
        } else {
            toddle.dismiss();
        }
    }

    @Override
    public void onChipClick(@NonNull String chipText) {
        sendMessage.setText(chipText);
    }

    private class ChatMessageTouchHelper extends ItemTouchHelper.Callback {

        private final SecretChatMessageAdapter mAdapter2;

        ChatMessageTouchHelper(SecretChatMessageAdapter adapter) {
            mAdapter2 = adapter;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return false;
        }


        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

            if (mChatData.get(viewHolder.getAdapterPosition()).getMessageType().equals("15")) {
                return 0;
            }

            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, swipeFlags);
        }


        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {

            return false;
        }

        @SuppressWarnings("TryWithIdenticalCatches")
        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if (!mChatData.get(viewHolder.getAdapterPosition()).getMessageType().equals("15")) {
                deleteMessage(viewHolder.getAdapterPosition());
            }

        }


    }

    private class UpdateSecretTimerText extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] params) {


            updateDTimeInToolbar();
            return null;
        }
    }

    private class startTimer extends AsyncTask<Long, Void, Void> {

        @Override
        protected Void doInBackground(Long... params) {

            try {
                AppController.getInstance().getDbController().setTimerStarted(documentId, String.valueOf(params[0]), params[1]);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    private class removeMessage extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                AppController.getInstance().getDbController().deleteParticularChatMessage(params[0], params[1]);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setUpActivity(intent);
    }

    @SuppressWarnings("TryWithIdenticalCatches,unchecked")
    private void setUpActivity(Intent intent) {


        final Bundle bundle = intent.getExtras();


        if (bundle != null) {


            receiverImage = bundle.getString("receiverImage");


            receiverIdentifier = bundle.getString("receiverIdentifier");
            receiverUid = bundle.getString("receiverUid");
            receiverName = bundle.getString("receiverName");
            documentId = bundle.getString("documentId");
            AppController.getInstance().setActiveReceiverId(receiverUid);

            fromNotification = bundle.containsKey("fromNotification");


            colorCode = bundle.getString("colorCode");
            isStar = bundle.getBoolean("isStar", false);

//            isStarInFriend = AppController.getInstance().getDbController().getStarFromFriendsDoc(AppController.getInstance().getFriendsDocId(), receiverUid);
            if (isStar) {
                iV_star.setVisibility(View.VISIBLE);
            } else {
                iV_star.setVisibility(View.GONE);
            }
            setCallsVisibility();
            new checkIfUserIsBlocked().execute();
            //new getWallpaperDetails().execute();
        }


        TextView inviteTv = (TextView) findViewById(R.id.TV_invite);


        Map<String, Object> chat_item = db.getParticularChatInfo(documentId,false);


        try {

            dTime = (long) (chat_item.get("dTime"));

        } catch (ClassCastException e) {

            dTime = (int) (chat_item.get("dTime"));

        }


        new UpdateSecretTimerText().execute();


        secretId = (String) chat_item.get("secretId");
        AppController.getInstance().setActiveSecretId(secretId);
        mAdapter = new SecretChatMessageAdapter(SecretChatMessageScreen.this, mChatData, root, documentId, secretId);
        recyclerView_chat.setAdapter(mAdapter);
        /*
         * Intentionally checked for this condition more than once
         */

        if (bundle != null && bundle.containsKey("secretChatInitiated")) {


            addMessageToSendInUi(setMessageToSend(false, 0, null, null), false, 0, null, true, true, true);


        }


        if ((boolean) chat_item.get("secretInviteVisibility")) {
            invite_rl.setVisibility(View.VISIBLE);


            if ((boolean) chat_item.get("wasInvited")) {
                /*
                 * I was invited to join the secret chat by the receiver
                 */
                inviteTv.setText(getResources().getString(R.string.youAreInvited) + " " + receiverName + " " + getResources().getString(R.string.JoinSecretChat));


            } else {
                /*
                 * I invited receiver to join the secret chat
                 */


                inviteTv.setText(getResources().getString(R.string.YouInvited) + " " + receiverName + " " + getResources().getString(R.string.JoinSecretChat));

            }


        } else {

            invite_rl.setVisibility(View.GONE);
            inviteVisible = false;
        }

        /*
         * For the last seen time/online status on the top
         */

        header_rl.setVisibility(View.GONE);

        header_center_rl.setVisibility(View.VISIBLE);
        header_receiverName.setText(receiverName);
        receiverNameHeader.setText(receiverName);

        allowLastSeen = AppController.getInstance().

                getSharedPreferences().

                getBoolean("enableLastSeen", true);


        /*
         * For the last seen time/online status on the top
         */
        top = "";

        opponentOnline = false;
//        AppController.getInstance().
//
//                subscribeToTopic(MqttEvents.OnlineStatus.value + "/" + receiverUid, 0);
//
//
//        AppController.getInstance().
//
//                subscribeToTopic(MqttEvents.Typing.value + receiverUid, 0);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mChatData.clear();
                mAdapter.notifyDataSetChanged();
            }
        });


        if (!bundle.containsKey("secretChatInitiated")) {


            loadFromDbFirstTen();


            Map<String, Object> chatInfo = db.getChatInfo(documentId);

            chatId = (String) chatInfo.get("chatId");
            canHaveMoreMessages = (boolean) chatInfo.get("canHaveMoreMessages");


            if (chatId.isEmpty()) {

                hasChatId = false;


            } else {

                hasChatId = true;

                if (canHaveMoreMessages) {
                    if (mChatData.size() == 0 || (boolean) chatInfo.get("partiallyRetrieved")) {
                        retrieveChatMessage(MESSAGE_PAGE_SIZE);
                    }
                }


            }


        }
        /*
         * For handling open from the notification
         */
        try {
            db.updateChatListOnViewingMessage(documentId);
        } catch (
                Exception e) {
            e.printStackTrace();
        }


        limit = false;

        firstTenLoaded = true;


    }

    @SuppressWarnings("TryWithIdenticalCatches")
    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            float density = getResources().getDisplayMetrics().density;

            if (object.getString("eventName").equals(MqttEvents.UserUpdates.value + "/" + AppController.getInstance().getUserId())) {


                switch (object.getInt("type")) {


                    case 2: {


                        if (object.getString("userId").equals(receiverUid)) {

                            receiverImage = object.getString("profilePic");
                            /*
                             * Profile pic update
                             */

                            if (receiverImage != null && !receiverImage.isEmpty()) {
                                try {

                                    Glide.with(SecretChatMessageScreen.this).load(receiverImage).asBitmap()
                                            .signature(new StringSignature(
                                                    AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                                            into(new BitmapImageViewTarget(pic) {
                                                @Override
                                                protected void setResource(Bitmap resource) {
                                                    RoundedBitmapDrawable circularBitmapDrawable =
                                                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                                                    circularBitmapDrawable.setCircular(true);
                                                    pic.setImageDrawable(circularBitmapDrawable);
                                                }
                                            });
                                } catch (IllegalArgumentException e) {
                                    e.printStackTrace();
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                            } else {


                                pic.setImageDrawable(TextDrawable.builder()


                                        .beginConfig()
                                        .textColor(Color.WHITE)
                                        .useFont(Typeface.DEFAULT)
                                        .fontSize((int) ((20) * density)) /* size in px */
                                        .bold()
                                        .toUpperCase()
                                        .endConfig()


                                        .buildRound((receiverName.trim()).charAt(0) + "", R.color.color_profile));

                            }
                        }
                        break;
                    }
                    case 6: {

                        /*
                         * Block or unblock user
                         */


                        if (object.getString("initiatorId").equals(receiverUid)) {
                            updateBlockedVisibility(object.getBoolean("blocked"));

                        }
                        break;
                    }

                }
            } else if (object.getString("eventName").equals(MqttEvents.Message.value + "/" + userId)) {


                /*
                 * Listening for the messages on mine own channel
                 */

                try {
                    String docIdForDoubleTickAck = object.getString("toDocId");


                    String sender = object.getString("from");


                    String secretId = "";
                    long dTime = -1;


                    if (object.has("secretId")) {
                        secretId = object.getString("secretId");
                        dTime = object.getLong("dTime");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                invite_rl.setVisibility(View.GONE);
                                inviteVisible = false;


                            }
                        });
                        //    }
                        String docId = AppController.getInstance().findDocumentIdOfReceiver(sender, secretId);

                        String messageType = object.getString("type");
                        if (docId.equals(documentId)) {

                            String id = object.getString("id");

                            String message = object.getString("payload");

                            if (AppController.getInstance().isForeground() && (!AppController.getInstance().isActiveOnACall() || AppController.getInstance().isCallMinimized())) {

                                if (!messageType.equals("0") || !message.trim().isEmpty()) {

                                    /*
                                     * Need to publish to the topic of the receiver uid
                                     */
                                    JSONObject obj = new JSONObject();
                                    obj.put("from", userId);
                                    obj.put("msgIds", new JSONArray(Arrays.asList(new String[]{id})));
                                    obj.put("doc_id", docIdForDoubleTickAck);
                                    obj.put("to", sender);

                                    obj.put("secretId", secretId);
                                    obj.put("readTime", Utilities.getGmtEpoch());

                                    obj.put("dTime", dTime);


                                    obj.put("status", "3");
                                    AppController.getInstance().publish(MqttEvents.Acknowledgement.value + "/" + receiverUid, obj, 2, false);
                                    obj = null;
                                }


                                CouchDbController db = AppController.getInstance().getDbController();
                                db.updateChatListOnViewingMessage(documentId);

                            } else {

                                hasPendingAcknowledgement = true;
                            }


                            String tsFromServer = object.getString("timestamp");


                            int dataSize = -1;
                            String mimeType = "", fileName = "", extension = "";

                            if (messageType.equals("1") || messageType.equals("2") || messageType.equals("5") || messageType.equals("7") || messageType.equals("9")) {
                                dataSize = object.getInt("dataSize");


                                if (messageType.equals("9")) {


                                    mimeType = object.getString("mimeType");
                                    fileName = object.getString("fileName");
                                    extension = object.getString("extension");


                                }
                            }
                            if (messageType.equals("13")) {


                                loadMessageInChatUI(sender, id, messageType, message, tsFromServer, dataSize, dTime, mimeType, fileName, extension, object.getString("postId"), object.getString("postTitle"), object.getInt("postType"));

                            } else {

                                loadMessageInChatUI(sender, id, messageType, message, tsFromServer, dataSize, dTime, mimeType, fileName, extension, null, null, -1);


                            }

                            //                            AppController.getInstance().decrementUnreadChatCount();
//                            AppController.getInstance().getDbController().removeUnreadChatsReceiverUids(AppController.getInstance().getChatDocId(), receiverUid);


                            messageType = null;
                            message = null;
                            tsFromServer = null;
                            id = null;
                        }


                        docId = null;
                        docIdForDoubleTickAck = null;

                        sender = null;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (object.getString("eventName").equals(MqttEvents.Connect.value)) {

                if (blockedTv.getVisibility() == View.GONE)
                    AppController.getInstance().subscribeToTopic(MqttEvents.OnlineStatus.value + "/" + receiverUid, 0);


            } else if (object.getString("eventName").equals(MqttEvents.Disconnect.value)) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        top = getString(R.string.string_336);
                        if (tv != null)
                            tv.setText(top);
                    }
                });

                /*
                 *Incase mine internet goes off,then also i don't emit on typing event
                 */

                opponentOnline = false;


            } else if (object.getString("eventName").substring(0, 3).equals("Onl")) {


                try {


                    if (object.getString("userId").equals(receiverUid)) {

                        updateLastSeenInActionBar(object);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();


                }
            } else if (object.getString("eventName").equals(MqttEvents.MessageResponse.value)) {


                try {


                    String docId = object.getString("docId");


                    if (docId.equals(documentId)) {

                        drawSingleTick(object.getString("messageId"));


                    }

                    docId = null;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (object.getString("eventName").equals(MqttEvents.Typing.value + receiverUid)) {
                try {
                    if (object.getString("from").equals(receiverUid)) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (tv != null) {
                                    tv.setText(R.string.Typing);

                                    new Handler().postDelayed(new Runnable() {
                                        public void run() {

                                            if (tv != null) {

                                                tv.setText(top);
                                            }

                                        }
                                    }, 1000);

                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (object.getString("eventName").equals(MqttEvents.Acknowledgement.value + "/" + userId)) {


                try {
                    if (object.has("secretId") && object.getString("secretId").equals(secretId)) {


                        /*
                         * Although this check is redundant, but have put for safety
                         */
                        if (object.getString("from").equals(receiverUid)) {

                            drawDoubleTick(object.getString("doc_id"), object.getString("msgId"), object.getString("status"));
                        }
                    }

                    String documentIdDoubleTick = object.getString("doc_id");
                    if (documentId.equals(documentIdDoubleTick)) {
//
//                        JSONObject json_obj1 = object.getJSONObject("msgIds");
//                        JSONArray arr_temp = json_obj1.getJSONArray("values");


                        JSONArray arr_temp = object.getJSONArray("msgIds");
                        String id = arr_temp.getString(0);

                        String status = object.getString("status");


                        boolean flag = false;

                        for (int i = mChatData.size() - 1; i >= 0; i--) {


                            if (mChatData.get(i).isSelf() && (mChatData.get(i).getMessageId()).equals(id)) {
                                flag = true;


                                if (status.equals("2")) {


                                    mChatData.get(i).setDeliveryStatus("2");


                                    final int k = i;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {


                                            mAdapter.notifyItemChanged(k);


                                        }
                                    });


                                } else if (status.equals("3")) {


                                    if (!mChatData.get(i).getDeliveryStatus().equals("3")) {


                                        for (int j = i; j >= 0; j--) {
                                            if (mChatData.get(j).isSelf() && !mChatData.get(j).getDeliveryStatus().equals("0")) {

                                                if (mChatData.get(j).getDeliveryStatus().equals("3")) {


                                                    break;
                                                } else {

                                                    mChatData.get(j).setDeliveryStatus("3");

                                                    final int k = j;
                                                    runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {


                                                            mAdapter.notifyItemChanged(k);
                                                        }
                                                    });


                                                }
                                            }
                                        }


                                    }
                                }

                            }

                            documentIdDoubleTick = null;

                            arr_temp = null;
                            //      json_obj1 = null;
                            if (flag) {
                                break;
                            }

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (object.getString("eventName").equals(MqttEvents.FetchMessages.value + "/" + userId)) {

                if (showingLoadingItem) {


                    mChatData.remove(0);


                    showingLoadingItem = false;
                }
                if (object.getString("chatId").equals(chatId)) {
                    addMessagesFetchedFromServer(object.getJSONArray("messages"));
                }

                final int length = object.getJSONArray("messages").length();


                if ((length > 0 && length % MESSAGE_PAGE_SIZE != 0) || (length == 0)) {
                    canHaveMoreMessages = false;
                    db.saveCanHaveMoreMessage(documentId);
                }
                pendingApiCalls--;

            } else if (object.getString("eventName").equals("callMinimized")) {

                minimizeCallScreen(object);
            } else if (object.getString("eventName").equals("MessageDownloaded")) {


                /*
                 *Callback from the autodownload of the message
                 */


                if (object.getString("docId").equals(documentId)) {
                    String replyType = "";


                    if (object.has("replyType")) {


                        replyType = object.getString("replyType");
                    }
                    updateMessageStatusAsDownloaded(object.getString("messageId"),
                            object.getString("senderId"),
                            object.getString("messageType"),
                            object.getString("filePath"), replyType);
                }


            } else if (object.getString("eventName").equals("WallpaperUpdated")) {


                updateWallpaper(object.getInt("type"), object.getString("wallpaperDetails"));


            } else if (object.getString("eventName").equals("UserBlocked")) {


                if (object.getString("opponentId").equals(receiverUid)) {
                    updateBlockedVisibility(true);

                }

            } else if (object.getString("eventName").equals("UserUnblocked")) {


                if (object.getString("opponentId").equals(receiverUid)) {
                    updateBlockedVisibility(false);

                }

            } else if (object.getString("eventName").equals("ContactNameUpdated")) {
                receiverName = object.getString("contactName");

                header_receiverName.setText(receiverName);

                receiverNameHeader.setText(receiverName);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    /**
     * @param pageSize to identify the number foR messages to be retrieved at a time
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private void retrieveChatMessage(int pageSize) {

        /*
         * As of now we just fetch the message of the 0th page b4 the timestamp of the first message
         */
        pendingApiCalls++;


        long timestamp = 0;


        try {


            timestamp = (mChatData.get(0).getMessageDateGMTEpoch());
        } catch (Exception e) {
            e.printStackTrace();


            timestamp = Utilities.getGmtEpoch();
        }


        final ProgressDialog pDialog = new ProgressDialog(SecretChatMessageScreen.this, 0);
        if (mChatData.size() == 0) {


            /*
             *
             *For first time coming in
             */

            showingLoadingItem = false;


            pDialog.setCancelable(false);


            pDialog.setMessage(getString(R.string.Retrieve_Message));


            pDialog.show();

            ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


            bar.getIndeterminateDrawable().setColorFilter(
                    ContextCompat.getColor(SecretChatMessageScreen.this, R.color.color_black),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        } else {


            SecretChatMessageItem loadingItem = new SecretChatMessageItem();
            loadingItem.setMessageType("99");


            mChatData.add(0, loadingItem);


            mAdapter.notifyDataSetChanged();


            showingLoadingItem = true;
        }

        JSONObject obj = new JSONObject();
        try {
            obj.put("chatId", chatId);
            obj.put("timestamp", timestamp);
            obj.put("pageSize", pageSize);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                ApiOnServer.FETCH_MESSAGES + "?chatId=" + chatId + "&timestamp=" + timestamp + "&pageSize=" + pageSize, null,
                new com.android.volley.Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {


                        try {


                            if (response.getInt("code") != 200) {


                                if (root != null) {

                                    Snackbar snackbar = Snackbar.make(root, response.getString("message"), Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                }


                            }

                            if (pDialog.isShowing()) {


                                Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();


                                if (context instanceof Activity) {


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                            pDialog.dismiss();
                                        }
                                    } else {


                                        if (!((Activity) context).isFinishing()) {
                                            pDialog.dismiss();
                                        }
                                    }
                                } else {


                                    try {
                                        pDialog.dismiss();
                                    } catch (final IllegalArgumentException e) {
                                        e.printStackTrace();

                                    } catch (final Exception e) {
                                        e.printStackTrace();

                                    }
                                }


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
                    SessionObserver sessionObserver = new SessionObserver();
                    sessionObserver.getObservable().subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DisposableObserver<Boolean>() {
                                @Override
                                public void onNext(Boolean flag) {
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            retrieveChatMessage(pageSize);
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
                    sessionApiCall.getNewSession(sessionObserver);
                } else if (pDialog.isShowing()) {


                    Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();


                    if (context instanceof Activity) {


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                pDialog.dismiss();
                            }
                        } else {


                            if (!((Activity) context).isFinishing()) {
                                pDialog.dismiss();
                            }
                        }
                    } else {


                        try {
                            pDialog.dismiss();
                        } catch (final IllegalArgumentException e) {
                            e.printStackTrace();

                        } catch (final Exception e) {
                            e.printStackTrace();

                        }
                    }

                    if (root != null) {


                        Snackbar snackbar = Snackbar.make(root, R.string.No_Internet_Connection_Available, Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }

                } else {


                    mChatData.remove(0);


                    mAdapter.notifyDataSetChanged();

                    if (root != null) {
                        Snackbar snackbar = Snackbar.make(root, R.string.Failed_Message_Retrieve, Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }


                }

            }
        }


        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("authorization", AppController.getInstance().getApiToken());
                headers.put("lang", Constants.LANGUAGE);


                return headers;
            }
        };


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "fetchMessagesApiRequest");


    }


    /**
     * @param messages JSONArray containing the lsit of the messages fetched from the server
     */


    @SuppressWarnings("unchecked")
    private void addMessagesFetchedFromServer(final JSONArray messages) {
        invite_rl.setVisibility(View.GONE);
        recyclerView_chat.setLayoutFrozen(true);
        JSONObject obj;

        SecretChatMessageItem message;


        String messageType, ts, tsFromServer;

        byte[] data;
        long dataSize;

        for (int i = 0; i < messages.length(); i++) {


            try {
                obj = messages.getJSONObject(i);
                message = new SecretChatMessageItem();


                data = Base64.decode(obj.getString("payload"), Base64.DEFAULT);

                tsFromServer = String.valueOf(obj.getLong("timestamp"));
                ts = Utilities.formatDate(Utilities.tsFromGmt(Utilities.epochtoGmt(tsFromServer)));


                message.setReceiverUid(receiverUid);


                if (obj.getString("receiverId").equals(receiverUid)) {

                    message.setIsSelf(true);
                    /*
                     * To identify if the message has been delivered/read by the opponent
                     */

                    message.setDeliveryStatus(obj.getString("status"));


                } else {


                    message.setIsSelf(false);
                }

                messageType = obj.getString("messageType");

                if ((messageType.equals("1")) || (messageType.equals("2")) || (messageType.equals("5")) || (messageType.equals("7")) || (messageType.equals("9"))) {
                    String size;
                    dataSize = obj.getLong("dataSize");

                    if (dataSize < 1024) {


                        size = dataSize + " bytes";

                    } else if (dataSize >= 1024 && dataSize <= 1048576) {


                        size = (dataSize / 1024) + " KB";

                    } else {


                        size = (dataSize / 1048576) + " MB";
                    }


                    message.setSize(size);
                }


                message.setTS(ts.substring(0, 9));


                message.setMessageDateOverlay(ts.substring(9, 24));

                message.setMessageDateGMTEpoch(Long.parseLong(tsFromServer));
                message.setMessageId(obj.getString("messageId"));


                /*
                 * By default assuming all the values being not downloaded
                 */


                new UpdateSecretTimerText().execute();
                switch (Integer.parseInt(messageType)) {


                    case 0:

                        message.setMessageType("0");

                        try {
                            message.setTextMessage(new String(data, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }


                        if (message.getTextMessage().trim().isEmpty()) {
                            message.setDTimeTag(true);

                            long dTime = obj.getLong("dTime");
                            if (dTime == -1) {
                                continue;
                            }

                            String destTime = String.valueOf(dTime);


                            for (int j = 0; j < dTimeForDB.length; j++) {
                                if (destTime.equals(dTimeForDB[j])) {


                                    if (j == 0) {

                                        message.setTextMessage(getString(R.string.Timer_set_off));
                                    } else {

                                        message.setTextMessage(getString(R.string.Timer_set_to) + " " + dTimeOptions[j]);
                                    }
                                    break;
                                }
                            }
                        }


                        data = null;

                        break;
                    case 1:

                        message.setMessageType("1");


                        message.setDownloading(false);
                        message.setDownloadStatus(0);


                        message.setThumbnailPath(getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + tsFromServer + ".jpg");


                        try {

                            message.setImagePath(new String(data, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }


                        data = null;


                        break;


                    case 2:

                        message.setMessageType("2");


                        message.setDownloadStatus(0);
                        message.setDownloading(false);


                        message.setThumbnailPath(getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + tsFromServer + ".jpg");


                        try {

                            message.setVideoPath(new String(data, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }


                        data = null;

                        break;


                    case 3:

                        message.setMessageType("3");


                        String placeString = "";

                        try {

                            placeString = new String(data, "UTF-8");


                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }


                        message.setPlaceInfo(placeString);

                        data = null;
                        placeString = null;


                        break;

                    case 4:

                        message.setMessageType("4");


                        String contactString = "";

                        try {

                            contactString = new String(data, "UTF-8");

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }


                        message.setContactInfo(contactString);


                        contactString = null;
                        data = null;

                        break;


                    case 5:

                        message.setMessageType("5");


                        message.setDownloadStatus(0);

                        message.setDownloading(false);
                        try {

                            message.setAudioPath(new String(data, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        data = null;

                        break;


                    case 6:

                        message.setMessageType("6");


                        try {
                            message.setStickerUrl(new String(data, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        data = null;


                        break;


                    case 7:

                        message.setMessageType("7");


                        message.setDownloadStatus(0);

                        message.setThumbnailPath(getFilesDir() + ApiOnServer.CHAT_RECEIVED_THUMBNAILS_FOLDER + "/" + tsFromServer + ".jpg");

                        try {

                            message.setImagePath(new String(data, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        data = null;


                        break;


                    case 8:

                        message.setMessageType("8");


                        try {
                            message.setGifUrl(new String(data, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                        data = null;

                        break;
                    case 9:

                        message.setMessageType("9");
                        message.setFileName(obj.getString("fileName"));
                        message.setMimeType(obj.getString("mimeType"));
                        message.setExtension(obj.getString("extension"));
                        message.setDownloadStatus(0);
                        message.setFileType(findFileTypeFromExtension(obj.getString("extension")));
                        message.setDownloading(false);
                        try {

                            message.setDocumentUrl(new String(data, "UTF-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        data = null;

                        break;
                }
                mChatData.add(0, message);


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                try {
                    mAdapter.notifyDataSetChanged();

                } catch (Exception e) {


                    e.printStackTrace();
                }
                try {

                    if (mChatData.size() <= MESSAGE_PAGE_SIZE) {

                        /*
                         * To scroll the item to last position incase of adding the last message
                         */

                        llm.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, 0);


                    } else {
                        /*
                         *
                         * To handle the problem of the unsmooth scroll
                         */

                        llm.scrollToPositionWithOffset(messages.length(), 0);
                    }
                } catch (Exception e) {


                    e.printStackTrace();


                }

            }
        });
        recyclerView_chat.setLayoutFrozen(false);
    }

    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(SecretChatMessageScreen.this, ChatMessageScreen.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("receiverUid", obj.getString("receiverUid"));
            intent.putExtra("receiverName", obj.getString("receiverName"));
            intent.putExtra("documentId", obj.getString("documentId"));
            intent.putExtra("isStar", obj.getBoolean("isStar"));
            intent.putExtra("receiverImage", obj.getString("receiverImage"));
            intent.putExtra("colorCode", obj.getString("colorCode"));

            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showCallTypeChooserPopup(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        updateAndroidSecurityProvider(SecretChatMessageScreen.this);
        //CallingApis.CheckForLiveCall( SecretChatMessageScreen.this);
        /* / Open the dialing audio call screen here /*/


        if (!AppController.getInstance().isActiveOnACall()) {

            final AlertDialog.Builder builder = new AlertDialog.Builder(SecretChatMessageScreen.this, 0);


            builder.setTitle(getResources().getString(R.string.StartCall));

            builder.setMessage(getResources().getString(R.string.CallOption));


            builder.setPositiveButton(getResources().getString(R.string.AudioCall), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    if (ActivityCompat.checkSelfPermission(SecretChatMessageScreen.this, Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED) {


                        ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.RECORD_AUDIO},
                                71);

                    } else {


                        requestAudioCall();

                    }

                }
            });
            builder.setNegativeButton(getResources().getString(R.string.VideoCall), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {


                    ArrayList<String> arr1 = new ArrayList<>();
                    if (ActivityCompat.checkSelfPermission(SecretChatMessageScreen.this, Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

                        arr1.add(Manifest.permission.CAMERA);
                    }


                    if (ActivityCompat.checkSelfPermission(SecretChatMessageScreen.this, Manifest.permission.RECORD_AUDIO)
                            != PackageManager.PERMISSION_GRANTED) {


                        arr1.add(Manifest.permission.RECORD_AUDIO);

                    }


                    if (arr1.size() > 0) {

                        ActivityCompat.requestPermissions(SecretChatMessageScreen.this, arr1.toArray(new String[arr1.size()]),
                                72);
                    } else {
                        requestVideoCall();
                    }

                }
            });


            if (AppController.getInstance().canPublish()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        AlertDialog alertDialog = builder.create();


                        alertDialog.show();


                        Button b_pos;
                        b_pos = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        if (b_pos != null) {
                            b_pos.setTextColor(ContextCompat.getColor(SecretChatMessageScreen.this, R.color.color_black));
                        }
                        Button n_pos;
                        n_pos = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        if (n_pos != null) {
                            n_pos.setTextColor(ContextCompat.getColor(SecretChatMessageScreen.this, R.color.color_black));
                        }


                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (root != null) {
                            Snackbar snackbar = Snackbar.make(root, R.string.No_Internet_Connection_Available,
                                    Snackbar.LENGTH_SHORT);
                            snackbar.show();


                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }

                    }
                });
            }
        } else {
            if (root != null) {
                Snackbar snackbar = Snackbar.make(root, getString(R.string.call_initiate), Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view2 = snackbar.getView();
                TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        }
    }


    /**
     * For audio recording and sharing
     */

    private void startRecord() {
        // TODO Auto-generated method stub

        vibrate();


        if (hasMicrophone()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {


                startAudioRecord(true);


            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {

                requestRecordAudioPermission();
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadImagePermission(3);
            }


        } else {


            recordingAudio = false;
            if (root != null) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_79, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }

    }

    private void stopRecord(boolean sendAudio) {


        // TODO Auto-generated method stub


        recordingAudio = false;
        if (timer != null) {
            timer.cancel();
        }


        vibrate();


        sendMessagePanel.setVisibility(View.VISIBLE);
        recordPanel.setVisibility(View.GONE);


        if (mediaRecorder != null) {


            try {

                mediaRecorder.stop();
                if (sendAudio) {
                    if (!recordTimeText.getText().toString().equals("00:00")) {
                        shareRecordedAudio();
                    }
                }
            } catch (Exception e) {

                if (root != null) {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_768, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }

            }
            mediaRecorder.reset();
            mediaRecorder.release();

        }
        recordTimeText.setText("00:00");

    }

    private void vibrate() {
        // TODO Auto-generated method stub
        try {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(200);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int dp(float value) {
//        return (int) Math.ceil(1 * value);

        return (int) Math.ceil(value);
    }


    private class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updatedTime = timeSwapBuff + timeInMilliseconds;
            final String hms = String.format(Locale.US,
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(updatedTime)
                            - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
                            .toHours(updatedTime)),
                    TimeUnit.MILLISECONDS.toSeconds(updatedTime)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                            .toMinutes(updatedTime)));
//            long lastsec = TimeUnit.MILLISECONDS.toSeconds(updatedTime)
//                    - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
//                    .toMinutes(updatedTime));

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (recordTimeText != null)
                            recordTimeText.setText(hms);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                }
            });
        }
    }


    @SuppressWarnings("all")
    private String createFileForRecording() {
        String name = Utilities.tsInGmt();
        name = new Utilities().gmtToEpoch(name);


        File folder = new File(getExternalFilesDir(null) + ApiOnServer.IMAGE_CAPTURE_URI);

        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }


//        File file = new File(Environment.getExternalStorageDirectory().getPath() + ApiOnServer.IMAGE_CAPTURE_URI, name + ".3gp");
        File file = new File(getExternalFilesDir(null) + ApiOnServer.IMAGE_CAPTURE_URI, name + ".3gp");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        Uri imgUri = FileProvider.getUriForFile(SecretChatMessageScreen.this, getApplicationContext().getPackageName() + ".provider", file);
        this.imageUri = imgUri;

        this.audioPath = file.getAbsolutePath();


        name = null;
        folder = null;
        file = null;

        return audioPath;
    }


    private void shareRecordedAudio() {


        String id = null;
        Uri uri = null;
        try {


            File audio = new File(audioPath);


            if (audio.length() <= (MAX_VIDEO_SIZE)) {
                try {

                    id = new Utilities().gmtToEpoch(Utilities.tsInGmt());


                    uri = Uri.fromFile(audio);


                    audio = null;
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                    if (root != null) {

                        Snackbar snackbar = Snackbar.make(root, R.string.string_53, Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }


                }

                if (uri != null) {


                    addMessageToSendInUi(setMessageToSend(true, 5, id, null), true, 5, uri, false, false, false);

                    uri = null;

                }
            } else {


                if (root != null) {

                    Snackbar snackbar = Snackbar.make(root, getString(R.string.string_54) + " " + MAX_VIDEO_SIZE / (1024 * 1024) + " " + getString(R.string.string_56), Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }

            }


        } catch (NullPointerException e) {


            if (root != null) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_768, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        }


    }


    protected boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE);
    }


    private void startAudioRecord(boolean actualRecord) {

        if (actualRecord) {


            recordPanel.setVisibility(View.VISIBLE);

            sendMessagePanel.setVisibility(View.GONE);

            try {

                //  if (mediaRecorder == null) {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

//                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);

                //     }


                mediaRecorder.setOutputFile(createFileForRecording());
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mediaRecorder.prepare();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


                mediaRecorder.start();

                recordingAudio = true;
                startTime = SystemClock.uptimeMillis();
                timer = new Timer();
                MyTimerTask myTimerTask = new MyTimerTask();
                timer.schedule(myTimerTask, 1000, 1000);


            } catch (Exception e) {

                if (root != null) {

                    Snackbar snackbar = Snackbar.make(root, getString(R.string.RecordFailed), Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                }


            }
        } else {


            if (root != null) {

                Snackbar snackbar = Snackbar.make(root, getString(R.string.record_audio), Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);


            }
        }
    }


    private void requestRecordAudioPermission() {


        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.RECORD_AUDIO)) {
            Snackbar snackbar = Snackbar.make(root, R.string.string_75,
                    Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.string_580), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.RECORD_AUDIO},
                            81);
                }
            });


            snackbar.show();


            View view = snackbar.getView();
            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO},
                    81);
        }


    }

    private void showGalleryPopup() {

        FilePickerBuilder.getInstance().setMaxCount(5)


                .enableCameraSupport(false)
                .enableVideoPicker(false)
                .pickPhoto(SecretChatMessageScreen.this);


    }


    private void showDocumentPopup() {

        FilePickerBuilder.getInstance().setMaxCount(1)


                .pickFile(SecretChatMessageScreen.this);

    }


    @SuppressWarnings("TryWithIdenticalCatches")

    private JSONObject setDocumentObjectToSend(String id, String mimeType, String fileName, String extension) {
        JSONObject obj = new JSONObject();


        if (id == null) {
            tsForServer = Utilities.tsInGmt();
            tsForServerEpoch = new Utilities().gmtToEpoch(tsForServer);
        } else {
            tsForServerEpoch = id;

            tsForServer = Utilities.epochtoGmt(id);
        }

        try {

            obj.put("receiverIdentifier", AppController.getInstance().getUserIdentifier());
            obj.put("from", AppController.getInstance().getUserId());
            obj.put("to", receiverUid);

            obj.put("toDocId", documentId);
            obj.put("timestamp", tsForServerEpoch);


            obj.put("secretId", secretId);
            obj.put("dTime", dTime);
            obj.put("id", tsForServerEpoch);
            obj.put("type", "9");
            obj.put("mimeType", mimeType);
            obj.put("extension", extension);
            obj.put("fileName", fileName);
            obj.put("name", AppController.getInstance().getUserName());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }


    /*
     * To add document to send in the list of messages
     */

    @SuppressWarnings("TryWithIdenticalCatches")
    private void addDocumentToSendInUi(JSONObject obj, Uri uri, String mimeType, String fileName, String filePath, String extension) {


        String tempDate = Utilities.formatDate(Utilities.tsFromGmt(tsForServer));


        SecretChatMessageItem message = new SecretChatMessageItem();

        message.setSenderName(userName);
        message.setIsSelf(true);
        message.setTS(tempDate.substring(0, 9));
        message.setDTimeTag(false);
        message.setdTime(dTime);
        message.setTimerStarted(false);

        message.setMessageDateOverlay(tempDate.substring(9, 24));

        message.setMessageDateGMTEpoch(Long.parseLong(tsForServerEpoch));


        message.setDeliveryStatus("0");
        message.setMessageId(tsForServerEpoch);
        message.setDownloadStatus(1);
        message.setMimeType(mimeType);

        message.setFileName(fileName);
        message.setExtension(extension);
        message.setFileType(findFileTypeFromExtension(extension));

        /*
         * Document
         */
        message.setDocumentUrl(filePath);

        message.setMessageType("9");


        Map<String, Object> map = new HashMap<>();
        map.put("message", filePath);
        map.put("messageType", "9");
        map.put("isSelf", true);
        map.put("from", userName);
        map.put("Ts", tsForServer);
        map.put("mimeType", mimeType);
        map.put("fileName", fileName);

        map.put("extension", extension);


        map.put("downloadStatus", 1);
        map.put("deliveryStatus", "0");
        map.put("id", tsForServerEpoch);


        map.put("isDTag", false);
        map.put("dTime", dTime);
        map.put("timerStarted", false);


        AppController.getInstance().getDbController().
                addNewChatMessageAndSort(documentId, map, tsForServer, secretId);
        map = null;


        mChatData.add(message);


        message = null;


        runOnUiThread(new Runnable() {
            @Override
            public void run() {


                //llm.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, 0);
                try {
                    mAdapter.notifyItemInserted(mChatData.size() - 1);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {


                            llm.scrollToPositionWithOffset(mAdapter.getItemCount() - 1, 0);

                        }
                    }, 500);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();


                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });


        if (button01pos == 1) {

            sendButton.setImageDrawable(drawable1);

            button01pos = 0;
        }


        /*
         *
         *
         *
         * Need to store all the messages in db so that incase internet
         * not present then has to resend all messages whenever internet comes back
         *
         *
         * */


        Map<String, Object> mapTemp = new HashMap<>();
        mapTemp.put("from", userId);
        mapTemp.put("to", receiverUid);

        mapTemp.put("toDocId", documentId);
        mapTemp.put("mimeType", mimeType);
        mapTemp.put("fileName", fileName);
        mapTemp.put("extension", extension);


        mapTemp.put("id", tsForServerEpoch);

        mapTemp.put("timestamp", tsForServerEpoch);


        mapTemp.put("type", "9");


        mapTemp.put("name", AppController.getInstance().getUserName());


        mapTemp.put("message", filePath);

        /*
         * Exclusively for secret chat
         */
        mapTemp.put("dTime", dTime);
        mapTemp.put("secretId", secretId);
        AppController.getInstance().getDbController().addUnsentMessage(AppController.getInstance().getunsentMessageDocId(), mapTemp);




        /*
         *
         * if is an document than have to upload and for that a dummy file in memory is
         * created which contains the compressed version of file to be send
         * */


        uploadFile(uri, userId + tsForServerEpoch, 9, obj, false, extension);


        obj = null;
        mapTemp = null;


        sendMessage.setText("");
        MessageType = 0;


    }


    private String findFileTypeFromExtension(String extension) {


        if (extension.equals("pdf")) {
            return FilePickerConst.PDF;
        } else if (extension.equals("doc") || extension.equals("docx") || extension.equals("dot") || extension.equals("dotx")) {
            return FilePickerConst.DOC;
        } else if (extension.equals("ppt") || extension.equals("pptx")) {
            return FilePickerConst.PPT;
        } else if (extension.equals("xls") || extension.equals("xlsx")) {
            return FilePickerConst.XLS;
        } else if (extension.equals("txt")) {
            return FilePickerConst.TXT;
        } else return "UNKNOWN";
    }

    @Override
    protected void onPause() {
        super.onPause();


        if (locationDialog != null && locationDialog.isShowing()) {
            locationDialog.dismiss();
            locationDialog = null;
        }
    }


    private int findMessagePosition(String messageId) {


        for (int i = 0; i < mChatData.size(); i++) {
            if (mChatData.get(i).getMessageId().equals(messageId)) {

                return i;
            }

        }

        return -1;

    }

    private void hideMessageHeader() {
        messageHeader.setVisibility(View.GONE);

        try {


            if (currentlySelectedMessage != null) {
                final int position = findMessagePosition(currentlySelectedMessage.getMessageId());
                if (position != -1) {
                    currentlySelectedMessage.setSelected(false);

                    mChatData.set(position, currentlySelectedMessage);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyItemChanged(position);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void showMessageHeader(String messageType, SecretChatMessageItem messageItem) {
        if (messageItem.isSelf()) {


            info.setVisibility(View.VISIBLE);
        } else {

            info.setVisibility(View.GONE);
        }


        messageHeader.setVisibility(View.VISIBLE);
        if (messageType.equals("0")) {
            /*
             *Text message,then show option to copy as well
             */

            //copy.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) copy.getLayoutParams();


            params.width = (int) (24 * density);
            copy.setLayoutParams(params);

        } else {

            // copy.setVisibility(View.GONE);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) copy.getLayoutParams();
            params.width = 0;
            copy.setLayoutParams(params);
        }


    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void deleteMessage(final int position) {

        try {


            AlertDialog.Builder builder =
                    new AlertDialog.Builder(SecretChatMessageScreen.this, 0);
            builder.setTitle(R.string.string_369);
            builder.setMessage(getString(R.string.string_485));
            builder.setPositiveButton(R.string.string_578, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {


                    String type = getString(R.string.string_801);


                    if (mChatData.get(position).getMessageType().equals("1")) {


                        type = getString(R.string.string_280);

                    } else if (mChatData.get(position).getMessageType().equals("2")) {


                        type = getString(R.string.string_281);

                    } else if (mChatData.get(position).getMessageType().equals("3")) {


                        type = getString(R.string.string_647);

                    } else if (mChatData.get(position).getMessageType().equals("4")) {


                        type = getString(R.string.string_648);

                    } else if (mChatData.get(position).getMessageType().equals("5")) {


                        type = getString(R.string.string_646);

                    } else if (mChatData.get(position).getMessageType().equals("6")) {
                        type = getString(R.string.Sticker);

                    } else if (mChatData.get(position).getMessageType().equals("7")) {
                        type = getString(R.string.Doodle);

                    } else if (mChatData.get(position).getMessageType().equals("8")) {
                        type = getString(R.string.Gif);

                    } else if (mChatData.get(position).getMessageType().equals("9")) {
                        type = getString(R.string.Document);

                    } else if (mChatData.get(position).getMessageType().equals("13")) {
                        type = getString(R.string.Post);

                    }
                    db.deleteParticularChatMessage(documentId, mChatData.get(position).getMessageId());


                    mChatData.remove(position);


                    final String str = type;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            if (position == 0) {
                                mAdapter.notifyDataSetChanged();
                            } else {


                                mAdapter.notifyItemRemoved(position);
                            }


                            Snackbar snackbar = Snackbar.make(root, str + " " + getString(R.string.string_11), Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                        }
                    });

                    // dialog.dismiss();


                    Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                    if (context instanceof Activity) {


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                dialog.dismiss();
                            }
                        } else {


                            if (!((Activity) context).isFinishing()) {
                                dialog.dismiss();
                            }
                        }
                    } else {


                        try {
                            dialog.dismiss();
                        } catch (final IllegalArgumentException e) {
                            e.printStackTrace();

                        } catch (final Exception e) {
                            e.printStackTrace();

                        }
                    }

                    hideMessageHeader();
                }
            });
            builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            mAdapter.notifyItemChanged(position);


                        }
                    });
                    dialog.cancel();

                }
            });
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


//                                mAdapter.notifyItemChanged(position);
                            mAdapter.notifyDataSetChanged();


                        }
                    });
                    dialog.cancel();

                }
            });
            builder.show();


            db.addMessageDeletedKey(documentId);


        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }


    }


    private void updateMessageStatusAsDownloaded(String messageId,
                                                 String senderId,
                                                 String messageType,
                                                 String filePath, String replyType) {


        for (int i = mChatData.size() - 1; i >= 0; i--) {


            if (mChatData.get(i).getMessageId().equals(messageId)) {
                /*
                 * To update the download status as being downloaded
                 */


                if (senderId.equals(receiverUid)) {


                    SecretChatMessageItem message = mChatData.get(i);
                    message.setDownloadStatus(1);


                    switch (Integer.parseInt(messageType)) {


                        case 1:
                            message.setImagePath(filePath);
                            break;

                        case 2:
                            message.setVideoPath(filePath);
                            break;

                        case 5:
                            message.setAudioPath(filePath);
                            break;

                        case 7:
                            message.setImagePath(filePath);
                            break;


                        case 9:
                            message.setDocumentUrl(filePath);
                            break;


                        case 10: {


                            switch (Integer.parseInt(replyType)) {


                                case 1:
                                    message.setImagePath(filePath);
                                    break;

                                case 2:
                                    message.setVideoPath(filePath);
                                    break;

                                case 5:
                                    message.setAudioPath(filePath);
                                    break;

                                case 7:
                                    message.setImagePath(filePath);
                                    break;


                                case 9:
                                    message.setDocumentUrl(filePath);
                                    break;


                            }


                        }


                    }


                    mChatData.set(i, message);

                    final int k = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyItemChanged(k);
                        }
                    });

                }

            }
        }


    }


    private void setupWallpaperDialog() {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        wallpaperView = layoutInflater.inflate(R.layout.wallpaper_bottom_sheet_2, null, false);


        RelativeLayout wlGallery = (RelativeLayout) wallpaperView.findViewById(R.id.rl1);
        RelativeLayout wlCamera = (RelativeLayout) wallpaperView.findViewById(R.id.rl2);
        RelativeLayout wlSolid = (RelativeLayout) wallpaperView.findViewById(R.id.rl3);
        RelativeLayout wlNo = (RelativeLayout) wallpaperView.findViewById(R.id.rl7);
        RelativeLayout wlDraw = (RelativeLayout) wallpaperView.findViewById(R.id.rl5);
        RelativeLayout wlLibrary = (RelativeLayout) wallpaperView.findViewById(R.id.rl4);
        RelativeLayout wlDefault = (RelativeLayout) wallpaperView.findViewById(R.id.rl6);


        wlGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {
                    try {
                        mBottomSheetDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                /*
                 *To select the wallpaper from the gallery
                 */
                checkReadImage(2);


            }
        });


        wlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {
                    try {
                        mBottomSheetDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                /*
                 *To capture the wallpaper from the camera
                 */


                checkCameraPermissionImage(1);


            }
        });


        wlSolid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {
                    try {
                        mBottomSheetDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Intent i = new Intent(SecretChatMessageScreen.this, SolidColorActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                i.putExtra("documentId", documentId);

                startActivity(i);

            }
        });


        wlNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {
                    try {
                        mBottomSheetDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                updateWallpaper(2, "");


                db.removeWallpaper(documentId);
            }
        });


        wlDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {
                    try {
                        mBottomSheetDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (ActivityCompat.checkSelfPermission(SecretChatMessageScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(SecretChatMessageScreen.this, DrawActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    i.putExtra("documentId", documentId);

                    startActivity(i);


                } else {
                    requestReadImagePermission(7);

                }


            }
        });


        wlLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                 *To select the wallpapers from the list of predefined wallpapers
                 */
                if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {
                    try {
                        mBottomSheetDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Intent i = new Intent(SecretChatMessageScreen.this, LibraryActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                i.putExtra("documentId", documentId);

                startActivity(i);

            }
        });


        wlDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBottomSheetDialog != null && mBottomSheetDialog.isShowing()) {
                    try {
                        mBottomSheetDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                updateWallpaper(1, "");
                db.addWallpaper(documentId, 1, "");
            }
        });


    }


    private void updateWallpaper(int type, final String wallpaperDetails) {
        switch (type) {
            case 0: {
                /*
                 * Solid color
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatBackground.setImageDrawable(null);
                        chatBackground.setBackgroundColor(Color.parseColor(wallpaperDetails));
                    }
                });

                break;
            }

            case 1: {
                /*
                 *Default
                 */


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Glide.with(SecretChatMessageScreen.this)
                                        .load(R.drawable.chat_background)
                                        .crossFade()

                                        .centerCrop()


                                        .into(chatBackground);
                            }
                        });
                    }
                });


                break;
            }

            case 2: {
                /*
                 *No wallpaper
                 */

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatBackground.setImageDrawable(null);
                        chatBackground.setBackgroundColor(ContextCompat.getColor(SecretChatMessageScreen.this, R.color.color_white));

                    }
                });


                break;
            }

            case 3: {
                /*
                 *Image from gallery or camera
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Glide.with(SecretChatMessageScreen.this)
                                .load(wallpaperDetails)
                                .crossFade()

                                .centerCrop()


                                .into(chatBackground);
                    }
                });


                break;
            }


            case 4: {
                /*
                 *Image from api call
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Glide.with(SecretChatMessageScreen.this)
                                .load(wallpaperDetails)
                                .crossFade()

                                .centerCrop()


                                .into(chatBackground);
                    }
                });


                break;
            }


            case 5: {
                /*
                 * Doodle drawn
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Glide.with(SecretChatMessageScreen.this)
                                .load(wallpaperDetails)
                                .crossFade()

                                .centerCrop()


                                .into(chatBackground);
                    }
                });


                break;
            }

        }
    }


    @SuppressWarnings("TryWithIdenticalCatches")
    private class getWallpaperDetails extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] params) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Map<String, Object> map = db.getWallpaperDetails(documentId);


                        String wallpaperData = null;


                        if (map.get("wallpaperData") != null) {


                            wallpaperData = (String) map.get("wallpaperData");
                        }

                        updateWallpaper((int) map.get("wallpaperType"), wallpaperData);

                    } catch (Exception e) {
                        updateWallpaper(1, null);

                    }
                }
            });

            return null;
        }
    }

    private void updateTypefaces() {
        TextView tvGallery, tvCamera, tvSolid, tvNo, tvDraw, tvLibrary, tvDefault;
        tvGallery = (TextView) wallpaperView.findViewById(R.id.tv1);
        tvCamera = (TextView) wallpaperView.findViewById(R.id.tv2);
        tvSolid = (TextView) wallpaperView.findViewById(R.id.tv3);
        tvNo = (TextView) wallpaperView.findViewById(R.id.tv7);
        tvDraw = (TextView) wallpaperView.findViewById(R.id.tv5);
        tvLibrary = (TextView) wallpaperView.findViewById(R.id.tv4);
        tvDefault = (TextView) wallpaperView.findViewById(R.id.tv6);


        Typeface face = AppController.getInstance().getRegularFont();
        tvGallery.setTypeface(face, Typeface.NORMAL);
        tvCamera.setTypeface(face, Typeface.NORMAL);
        tvSolid.setTypeface(face, Typeface.NORMAL);
        tvNo.setTypeface(face, Typeface.NORMAL);
        tvDraw.setTypeface(face, Typeface.NORMAL);
        tvLibrary.setTypeface(face, Typeface.NORMAL);

        tvDefault.setTypeface(face, Typeface.NORMAL);
    }

    private void showWallpaperSheet() {


        if (wallpaperView.getParent() != null)
            ((ViewGroup) wallpaperView.getParent()).removeView(wallpaperView);

        mBottomSheetDialog = new BottomSheetDialog(SecretChatMessageScreen.this);

        mBottomSheetDialog.setContentView(wallpaperView);
        mBottomSheetDialog.show();

    }


    /*
     * Will check if the user is blocked,first in database and then from the api.
     */
    @SuppressWarnings("TryWithIdenticalCatches")
    private class checkIfUserIsBlocked extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] params) {

            if (db == null) db = AppController.getInstance().getDbController();
            final boolean blocked = db.checkIfBlocked(AppController.getInstance().getBlockedDocId(), receiverUid);

            JSONObject obj = new JSONObject();
            try {
                obj.put("opponentId", receiverUid);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            updateBlockedVisibility(blocked);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                    ApiOnServer.BLOCK_USER + "?opponentId=" + receiverUid, null, new com.android.volley.Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {


                    try {


                        switch (response.getInt("code")) {


                            case 200: {

                                response = response.getJSONObject("data");
                                if (response.getBoolean("blocked") != blocked) {
                                    boolean self = true;


                                    updateBlockedVisibility(response.getBoolean("blocked"));


                                    if (response.getBoolean("blocked")) {

                                        userBlocked = 1;
                                        if (response.has("self")) {

                                            self = response.getBoolean("self");


                                        }

                                        db.addBlockedUser(AppController.getInstance().getFriendsDocId(), receiverUid, receiverIdentifier, self);

                                    } else {
                                        userBlocked = 0;

                                        db.removeUnblockedUser(AppController.getInstance().getFriendsDocId(), receiverUid);
                                    }
                                }
                            }


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();


                    }


                }
            }, new com.android.volley.Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
                        SessionObserver sessionObserver = new SessionObserver();
                        sessionObserver.getObservable().subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DisposableObserver<Boolean>() {
                                    @Override
                                    public void onNext(Boolean flag) {
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                new checkIfUserIsBlocked().execute(params);
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
                        sessionApiCall.getNewSession(sessionObserver);
                    }
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    headers.put("authorization", AppController.getInstance().getApiToken());
                    headers.put("lang", Constants.LANGUAGE);


                    return headers;
                }
            };

            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    20 * 1000, 0,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            /* Add the request to the RequestQueue.*/
            AppController.getInstance().addToRequestQueue(jsonObjReq, "checkBlockedApi");


            return null;
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void updateBlockedVisibility(final boolean blocked) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    if (blocked) {
                        try {
                            //initiateCall.setVisibility(View.GONE);
                            initiateAudioCall.setVisibility(View.GONE);
                            initiateVideoCall.setVisibility(View.GONE);


                        } catch (NullPointerException e) {
                        }
                        AppController.getInstance().

                                unsubscribeToTopic(MqttEvents.OnlineStatus.value + "/" + receiverUid);


                        AppController.getInstance().

                                unsubscribeToTopic(MqttEvents.Typing.value + receiverUid);


                        try {

                            pic.setImageDrawable(TextDrawable.builder()
                                    .beginConfig()
                                    .textColor(Color.WHITE)
                                    .useFont(Typeface.DEFAULT)
                                    .fontSize((int) ((20) * density)) /* size in px */
                                    .bold()
                                    .toUpperCase()
                                    .endConfig()
                                    .buildRound((receiverName.trim()).charAt(0) + "", Color.parseColor(colorCode)));


                        } catch (Exception e) {


                            pic.setImageDrawable(TextDrawable.builder()


                                    .beginConfig()
                                    .textColor(Color.WHITE)
                                    .useFont(Typeface.DEFAULT)
                                    .fontSize((int) ((20) * density)) /* size in px */
                                    .bold()
                                    .toUpperCase()
                                    .endConfig()


                                    .buildRound((receiverName.trim()).charAt(0) + "", R.color.color_profile));
                        }
                        sendMessagePanelLL.setVisibility(View.GONE);
                        blockedTv.setText(getString(R.string.YouCantSendMessage, receiverName));
                        blockedTv.setVisibility(View.VISIBLE);
                        circleMenu.setVisibility(View.GONE);
                        userBlocked = 1;
                        attachment.setVisibility(View.GONE);
                        hideMessageHeader();


                        try {


                            header_rl.setVisibility(View.GONE);


                            header_center_rl.setVisibility(View.VISIBLE);

                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            //initiateCall.setVisibility(View.VISIBLE);
                            initiateVideoCall.setVisibility(View.VISIBLE);
                            initiateAudioCall.setVisibility(View.VISIBLE);
                        } catch (NullPointerException e) {
                        }
                        AppController.getInstance().

                                subscribeToTopic(MqttEvents.OnlineStatus.value + "/" + receiverUid, 0);


                        AppController.getInstance().

                                subscribeToTopic(MqttEvents.Typing.value + receiverUid, 0);


                        blockedTv.setVisibility(View.GONE);
                        sendMessagePanelLL.setVisibility(View.VISIBLE);
                        attachment.setVisibility(View.GONE);


                        if (lastSeenResponseCame) {
                            try {


                                header_rl.setVisibility(View.VISIBLE);


                                header_center_rl.setVisibility(View.GONE);
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }

                        if (receiverImage != null && !receiverImage.isEmpty()) {

                            try {
                                Glide.with(SecretChatMessageScreen.this).load(receiverImage).asBitmap()
                                        .signature(new StringSignature(
                                                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                                        .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                                        into(new BitmapImageViewTarget(pic) {
                                            @Override
                                            protected void setResource(Bitmap resource) {
                                                RoundedBitmapDrawable circularBitmapDrawable =
                                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                                circularBitmapDrawable.setCircular(true);
                                                pic.setImageDrawable(circularBitmapDrawable);
                                            }
                                        });
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }


                        } else {


                            float density = getResources().getDisplayMetrics().density;
                            try {

                                pic.setImageDrawable(TextDrawable.builder()


                                        .beginConfig()
                                        .textColor(Color.WHITE)
                                        .useFont(Typeface.DEFAULT)
                                        .fontSize((int) ((20) * density)) /* size in px */
                                        .bold()
                                        .toUpperCase()
                                        .endConfig()
                                        .buildRound((receiverName.trim()).charAt(0) + "", Color.parseColor(colorCode)));
                            } catch (Exception e) {

                                pic.setImageDrawable(TextDrawable.builder()


                                        .beginConfig()
                                        .textColor(Color.WHITE)
                                        .useFont(Typeface.DEFAULT)
                                        .fontSize((int) ((20) * density)) /* size in px */
                                        .bold()
                                        .toUpperCase()
                                        .endConfig()


                                        .buildRound((receiverName.trim()).charAt(0) + "",
                                                ContextCompat.getColor(SecretChatMessageScreen.this, R.color.color_profile)));
                            }

                        }
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideKeyboard() {
        try {
            ((InputMethodManager) this.getSystemService(
                    Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    (this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * To hide keyboard when clicked outside on keyboard,except that on the
     */


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            View view = getCurrentFocus();
            if (view != null && view instanceof EmojiconEditText && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) &&
                    !view.getClass().getName().startsWith("android.webkit.")) {

                if (inViewInBounds(sendMessagePanel, (int) ev.getRawX(), (int) ev.getRawY())) {

                    /*
                     * Clicked on send message panel
                     * */

                } else if (!inViewInBounds(sendButton, (int) ev.getRawX(), (int) ev.getRawY())) {
                    /*
                     *Not clicked on the send button
                     */

                    int scrcoords[] = new int[2];
                    view.getLocationOnScreen(scrcoords);
                    float x = ev.getRawX() + view.getLeft() - scrcoords[0];
                    float y = ev.getRawY() + view.getTop() - scrcoords[1];
                    if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                        ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);


                }

                if (!inViewInBounds(rlAttach, (int) ev.getRawX(), (int) ev.getRawY())) {
                    /*not clicked on attachment layout*/
                    if (rlAttach.getVisibility() == View.VISIBLE)
                        rlAttach.setVisibility(View.GONE);
                }


            }
        } catch (Exception e) {
        }
        return super.dispatchTouchEvent(ev);
    }

    Rect outRect = new Rect();
    int[] location = new int[2];

    private boolean inViewInBounds(View view, int x, int y) {

        try {

            view.getDrawingRect(outRect);
            view.getLocationOnScreen(location);
            outRect.offset(location[0], location[1]);
            return outRect.contains(x, y);
        } catch (Exception e) {

            return false;
        }
    }


    private void openCamera() {

//        SandriosCamera
//                .with(SecretChatMessageScreen.this)
//                .setShowPicker(false)
//                .setVideoFileSize(20)
//                .setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
//                .enableImageCropping(true)
//                .launchCamera(new SandriosCamera.CameraCallback() {
//                    @Override
//                    public void onComplete(CameraOutputModel model) {
//
////                        Toast.makeText(getApplicationContext(), "Media captured.", Toast.LENGTH_SHORT).show();
//
//                        sendCapturedImage(model.getPath(), model.getType() == 0);
//                    }
//                });
        Intent intent = new Intent(SecretChatMessageScreen.this, ChatCameraActivity.class);

        startActivityForResult(intent, RESULT_CAPTURE_MEDIA);
    }

    private void sendCapturedImage(String path, boolean isImage) {

        if (isImage) {
            Uri uri = null;
            String id = null;
            Bitmap bm = null;

            picturePath = path;
            try {

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(picturePath, options);


                int height = options.outHeight;
                int width = options.outWidth;

                float density = getResources().getDisplayMetrics().density;
                int reqHeight;


                if (width != 0) {


                    reqHeight = (int) ((150 * density) * (height / width));


                    bm = decodeSampledBitmapFromResource(picturePath, (int) (150 * density), reqHeight);


                    ByteArrayOutputStream baos = new ByteArrayOutputStream();


                    if (bm != null) {

                        bm.compress(Bitmap.CompressFormat.JPEG, IMAGE_CAPTURED_QUALITY, baos);
                        //bm = null;
                        byte[] b = baos.toByteArray();
                        try {
                            baos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        baos = null;
//                b = compress(b);


                        id = new Utilities().gmtToEpoch(Utilities.tsInGmt());

                        File f = convertByteArrayToFile(b, id, ".jpg");
                        b = null;

                        uri = Uri.fromFile(f);
                        f = null;


                    } else {


                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, R.string.string_50, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }


                    }


                } else {


                    if (root != null) {

                        Snackbar snackbar = Snackbar.make(root, R.string.string_50, Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                }


            } catch (OutOfMemoryError e) {
                e.printStackTrace();


                if (root != null) {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_49, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }


            }


            if (uri != null) {

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bm.compress(Bitmap.CompressFormat.JPEG, 1, baos);


                bm = null;
                byte[] b = baos.toByteArray();

                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                baos = null;


                addMessageToSendInUi(setMessageToSend(true, 1, id, Base64.encodeToString(b,
                        Base64.DEFAULT).trim()), true, 1, uri, false, false, true);
                uri = null;
                b = null;
                bm = null;

            }

        } else {
            Uri uri = null;
            String id = null;
            try {

                videoPath = path;


                File video = new File(videoPath);

                if (video.length() <= (MAX_VIDEO_SIZE)) {

                    try {


                        byte[] b = convertFileToByteArray(video);
                        video = null;
                        //        b = compress(b);


                        id = new Utilities().gmtToEpoch(Utilities.tsInGmt());

                        File f = convertByteArrayToFile(b, id, ".mp4");
                        b = null;

                        uri = Uri.fromFile(f);
                        f = null;

                        b = null;


                    } catch (OutOfMemoryError e) {

                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, R.string.string_51, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }


                    }

                    if (uri != null) {


                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        Bitmap bm = ThumbnailUtils.createVideoThumbnail(videoPath,
                                MediaStore.Images.Thumbnails.MINI_KIND);

                        if (bm != null) {

                            bm.compress(Bitmap.CompressFormat.JPEG, 1, baos);
                            bm = null;
                            byte[] b = baos.toByteArray();
                            try {
                                baos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            baos = null;

                            addMessageToSendInUi(setMessageToSend(true, 2, id,
                                    Base64.encodeToString(b, Base64.DEFAULT).trim()), true, 2, uri, false, false, true);


                            uri = null;
                            b = null;

                        } else {


                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, getString(R.string.string_674)

                                        , Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }


                        }
                    } else {


                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, getString(R.string.string_674)
                                    , Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }


                    }

                } else {


                    if (root != null) {

                        Snackbar snackbar = Snackbar.make(root, getString(R.string.string_52) + " " + MAX_VIDEO_SIZE / (1024 * 1024) + " " + getString(R.string.string_56), Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }


                }

            } catch (NullPointerException e) {


                if (root != null) {

                    Snackbar snackbar = Snackbar.make(root, R.string.string_764, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }

            }


        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private static byte[] convertFileToByteArray(File f) {


        byte[] byteArray = null;
        byte[] b;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {

            InputStream inputStream = new FileInputStream(f);
            b = new byte[2663];

            int bytesRead;

            while ((bytesRead = inputStream.read(b)) != -1) {
                bos.write(b, 0, bytesRead);
            }


            inputStream = null;

            byteArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        } finally {
            b = null;

            try {
                bos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }


            bos = null;
        }


        return byteArray;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;


        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            final String documentId = DocumentsContract.getDocumentId(uri);
            if (documentId.startsWith("raw:")) {
                return documentId.replaceFirst("raw:", "");
            }
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return context.getExternalFilesDir(null) + "/" + split[1];
                }


            } else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {


            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    /*
     * *************************************************************************/

    /*
     * To show the dialog for selecting the type of message to send
     */

    /*
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /*
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /*
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /*
     * To get details of the type of the attachment selected
     */

    /*
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    /*
     *
     *To update the last seen time details in the action bar
     *
     */

    /*
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public void setCallsVisibility() {
        if (isStar) {
            if (isStarInFriend) {
                initiateAudioCall.setVisibility(View.VISIBLE);
                initiateVideoCall.setVisibility(View.VISIBLE);
            } else {
                initiateAudioCall.setVisibility(View.GONE);
                initiateVideoCall.setVisibility(View.GONE);
            }
        } else {
            initiateAudioCall.setVisibility(View.VISIBLE);
            initiateVideoCall.setVisibility(View.VISIBLE);
        }
    }


    private void startAudioCall(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        updateAndroidSecurityProvider(SecretChatMessageScreen.this);


        if (!AppController.getInstance().isActiveOnACall()) {


            if (AppController.getInstance().canPublish()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (ActivityCompat.checkSelfPermission(SecretChatMessageScreen.this, Manifest.permission.RECORD_AUDIO)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(SecretChatMessageScreen.this, new String[]{Manifest.permission.RECORD_AUDIO},
                                    71);

                        } else {

                            requestAudioCall();

                        }
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (root != null) {
                            Snackbar snackbar = Snackbar.make(root, R.string.No_Internet_Connection_Available,
                                    Snackbar.LENGTH_SHORT);
                            snackbar.show();


                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }

                    }
                });
            }

        } else {
            if (root != null) {
                Snackbar snackbar = Snackbar.make(root, getString(R.string.call_initiate), Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view2 = snackbar.getView();
                TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        }

    }

    private void startVideoCall(View view) {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


        updateAndroidSecurityProvider(SecretChatMessageScreen.this);


        if (!AppController.getInstance().isActiveOnACall()) {


            if (AppController.getInstance().canPublish()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        ArrayList<String> arr1 = new ArrayList<>();
                        if (ActivityCompat.checkSelfPermission(SecretChatMessageScreen.this, Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {

                            arr1.add(Manifest.permission.CAMERA);
                        }


                        if (ActivityCompat.checkSelfPermission(SecretChatMessageScreen.this, Manifest.permission.RECORD_AUDIO)
                                != PackageManager.PERMISSION_GRANTED) {


                            arr1.add(Manifest.permission.RECORD_AUDIO);

                        }


                        if (arr1.size() > 0) {

                            ActivityCompat.requestPermissions(SecretChatMessageScreen.this, arr1.toArray(new String[arr1.size()]),
                                    72);
                        } else {
                            requestVideoCall();
                        }

                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (root != null) {
                            Snackbar snackbar = Snackbar.make(root, R.string.No_Internet_Connection_Available,
                                    Snackbar.LENGTH_SHORT);
                            snackbar.show();


                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }

                    }
                });
            }

        } else {
            if (root != null) {
                Snackbar snackbar = Snackbar.make(root, getString(R.string.call_initiate), Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view2 = snackbar.getView();
                TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Copy");
        menu.add(0, v.getId(), 0, "Remove");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
         if (item.getTitle() == "Copy"){

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("label", currentlySelectedMessage.getTextMessage());
            clipboard.setPrimaryClip(clip);

            Toast toast = Toast.makeText(SecretChatMessageScreen.this, "Message Copied", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(200);

        } else if (item.getTitle() == "Remove"){

            int val = findMessagePosition(currentlySelectedMessage.getMessageId());

            if (val != -1) {

                deleteMessage(val);
            }
        }
        else {
            return  false;
        }
        return true;
    }

    private void showKeyBoard(){
        final InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(sendMessage, InputMethodManager.SHOW_FORCED);
        circleMenu.setVisibility(View.GONE);
    }

    private void hideKeyBoard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        circleMenu.setVisibility(View.VISIBLE);
        Log.v("Circlemenu","circlemenuKB");
        sendButton.setVisibility(View.GONE);
    }

    private void hideKB(){

        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        if (imm.isAcceptingText()) {
            Log.d("Keyboard","keyboardopen");
            circleMenu.setVisibility(View.GONE);
        } else {
            Log.d("Keyboard","keyboardclosed");
        }
    }

    /*
     * For audio recording and sharing
     */

    private void checkIfCanStartRecord() {
        // TODO Auto-generated method stub

        vibrate();

        if (hasMicrophone()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                startAudioRecord(true);
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                    != PackageManager.PERMISSION_GRANTED) {

                requestRecordAudioPermission();
            } else if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestReadImagePermission(3);
            }
        } else {

            recordingAudio = false;
            if (root != null) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_79, Snackbar.LENGTH_SHORT);

                snackbar.show();
                View view = snackbar.getView();
                TextView txtv =
                        (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }
    }
}

