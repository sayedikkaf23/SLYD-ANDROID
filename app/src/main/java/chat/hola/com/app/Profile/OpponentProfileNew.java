package chat.hola.com.app.Profile;

import static chat.hola.com.app.Utilities.Utilities.findMatch;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
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
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.Activities.MediaHistory;
import chat.hola.com.app.Activities.MediaHistory_FullScreenVideo;
import chat.hola.com.app.Adapters.OpponentMediaHistory_Adapter;
import chat.hola.com.app.AppController;
import chat.hola.com.app.ModelClasses.ChatlistItem;
import chat.hola.com.app.ModelClasses.Media_History_Item;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.HowdooServiceTrending;
import chat.hola.com.app.Networking.UnsafeOkHttpClient;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.CommonClass;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.CustomLinearLayoutManager;
import chat.hola.com.app.Utilities.FloatingView;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.TextDrawable;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.model.Posts;
import chat.hola.com.app.home.model.SuggestedUserResponse;
import chat.hola.com.app.home.trending.model.HeaderResponse;
import chat.hola.com.app.home.trending.model.Trending;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.models.ViewPostRequest;
import chat.hola.com.app.post.ReportReason;
import chat.hola.com.app.profileScreen.ProfileActivity;
import chat.hola.com.app.profileScreen.channel.Model.ChannelData;
import chat.hola.com.app.profileScreen.discover.contact.pojo.ContactRequest;
import chat.hola.com.app.profileScreen.discover.contact.pojo.Contacts;
import chat.hola.com.app.profileScreen.model.Data;
import chat.hola.com.app.profileScreen.model.Profile;
import chat.hola.com.app.search.model.SearchResponse;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class OpponentProfileNew extends AppCompatActivity implements DialogInterface.OnClickListener {

  private Bus bus = AppController.getBus();
  private RelativeLayout root;
  private CollapsingToolbarLayout collapsingToolbarLayout;
  private TextView userStatus, userIdentifier, mediaCount;
  private ImageView userImage, call,ivProfileBg;
  private String receiverImage, receiverUid, receiverName, receiverIdentifier, docId, number;
  private RelativeLayout MediaHistory_rl;
  private OpponentMediaHistory_Adapter mAdapter;
  private SessionApiCall sessionApiCall = new SessionApiCall();
  private HowdooService howdooService;
  private RecyclerView recyclerView;

  AlertDialog.Builder reportDialog;
  ArrayAdapter<String> arrayAdapter;

  /*
   *For the mute functionality
   *
   */
  private ArrayList<Media_History_Item> mMediaData = new ArrayList<>();
  private SwitchCompat muteSwitch;
  private String secretId;
  /**
   * For the block functionality
   */

  private TextView blockTv, textAbout;
  private TextView tvLastSeen, tvDeleteChat;
  private LinearLayout linearMenu,llReportuser;
  private boolean blocked;
  private TextView block;
  private TextView profileName;
  private TextView tvNumber;
  private TextView tvUsername;
  private TextView text_block;
  private TextView tv_reportText;
  private String userName;
  private TextView email;
  private String firstName,lastName,countryCode;
  private boolean allowLastSeen;

  private boolean isStar;

  @SuppressWarnings("unchecked,TryWithIdenticalCatches")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_opponet_profile_new2);

    ivProfileBg = (ImageView) findViewById(R.id.ivProfileBg);
    call = (ImageView) findViewById(R.id.iv_call);
//    root = (RelativeLayout) findViewById(R.id.root);
//    block = (TextView) findViewById(R.id.text_block);
//    blockTv = (TextView) findViewById(R.id.text_block);
    textAbout = (TextView) findViewById(R.id.text_about);
    muteSwitch = (SwitchCompat) findViewById(R.id.iv5);

    MediaHistory_rl = (RelativeLayout) findViewById(R.id.media_rl);
    RelativeLayout mediaHeader_rl = (RelativeLayout) findViewById(R.id.mediaHeader_rl);
//     userName = (TextView) findViewById(R.id.userName);
    email = (TextView) findViewById(R.id.tvEmail);

    profileName = (TextView) findViewById(R.id.tvProfileName);
    text_block = findViewById(R.id.text_block);
    tv_reportText = findViewById(R.id.tv_reportText);
    tvUsername = (TextView) findViewById(R.id.tvUsername);
    tvNumber = (TextView) findViewById(R.id.tvNumber);
    mediaCount = (TextView) findViewById(R.id.mediaCount);
    userStatus = (TextView) findViewById(R.id.userStatus);
    userIdentifier = (TextView) findViewById(R.id.userIdentifier);
    userImage = (ImageView) findViewById(R.id.ivProfile);
    llReportuser = (LinearLayout) findViewById(R.id.llReportuser);

    collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_container);
    collapsingToolbarLayout.setTitleEnabled(true);
    recyclerView = (RecyclerView) findViewById(R.id.media_rv);
    recyclerView.setHasFixedSize(true);
    mAdapter = new OpponentMediaHistory_Adapter(OpponentProfileNew.this, mMediaData);
    reportDialog = new AlertDialog.Builder(this);
    reportDialog.setTitle(R.string.report);
    getReportReasons();

    String screen = getIntent().getStringExtra("call");

    NestedScrollView nsvOption = findViewById(R.id.nsvOption);
    NestedScrollView view = findViewById(R.id.view);
    linearMenu = findViewById(R.id.linearMenu);
    tvLastSeen = findViewById(R.id.tvLastSeen);
    tvDeleteChat = findViewById(R.id.tvDeleteChat);
    allowLastSeen =
        AppController.getInstance().getSharedPreferences().getBoolean("enableLastSeen", true);
    if (allowLastSeen) {
      tvLastSeen.setText(getString(R.string.string_692));
    } else {
      tvLastSeen.setText(getString(R.string.string_691));
    }

    recyclerView.setLayoutManager(
        new CustomLinearLayoutManager(OpponentProfileNew.this, LinearLayoutManager.HORIZONTAL, false));
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    recyclerView.setAdapter(mAdapter);

    recyclerView.addOnItemTouchListener(
        new RecyclerItemClickListener(OpponentProfileNew.this, recyclerView,
            new RecyclerItemClickListener.OnItemClickListener() {
              @Override
              public void onItemClick(View view, int position) {

                final Media_History_Item item = mMediaData.get(position);

                if (item.getMessageType().equals("2")) {

                  if (item.getDownloadStatus() == 1) {

                    if (ActivityCompat.checkSelfPermission(OpponentProfileNew.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

                      File file = new File(item.getVideoPath());

                      if (file.exists()) {
                        try {

                          Uri intentUri;
                          if (Build.VERSION.SDK_INT >= 24) {
                            intentUri = Uri.parse(item.getVideoPath());
                          } else {
                            intentUri = Uri.fromFile(file);
                          }

                          Intent intent = new Intent();
                          intent.setAction(Intent.ACTION_VIEW);

                          intent.setDataAndType(intentUri, "video/*");

                          startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(
                              OpponentProfileNew.this).toBundle());
                        } catch (ActivityNotFoundException e) {
                          Intent i =
                              new Intent(OpponentProfileNew.this, MediaHistory_FullScreenVideo.class);
                          i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                          i.putExtra("videoPath", item.getVideoPath());
                          startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(
                              OpponentProfileNew.this).toBundle());
                        }
                      } else {
                        if (root != null) {
                          Snackbar snackbar =
                              Snackbar.make(root, R.string.string_1005, Snackbar.LENGTH_SHORT);

                          snackbar.show();
                          View view2 = snackbar.getView();
                          TextView txtv2 = (TextView) view2.findViewById(
                              com.google.android.material.R.id.snackbar_text);
                          txtv2.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                      }
                    } else {
                      if (root != null) {
                        Snackbar snackbar =
                            Snackbar.make(root, R.string.string_1006, Snackbar.LENGTH_SHORT);

                        snackbar.show();
                        View view2 = snackbar.getView();
                        TextView txtv2 = (TextView) view2.findViewById(
                            com.google.android.material.R.id.snackbar_text);
                        txtv2.setGravity(Gravity.CENTER_HORIZONTAL);
                      }
                    }
                  } else {

                    if (root != null) {
                      Snackbar snackbar =
                          Snackbar.make(root, R.string.string_1004, Snackbar.LENGTH_SHORT);

                      snackbar.show();
                      View view2 = snackbar.getView();
                      TextView txtv2 = (TextView) view2.findViewById(
                          com.google.android.material.R.id.snackbar_text);
                      txtv2.setGravity(Gravity.CENTER_HORIZONTAL);
                    }
                  }
                }
              }

              @Override
              public void onItemLongClick(View view, int position) {

              }
            }));
    muteSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (receiverUid != null && secretId != null) {
          if (isChecked) {
            /*
             *To add a muted chat
             */

            AppController.getInstance()
                .getDbController()
                .addMuteChat(AppController.getInstance().getMutedDocId(), receiverUid, secretId);
          } else {

            /*
             *To remove a muted chat
             */

            AppController.getInstance()
                .getDbController()
                .removeMuteChat(AppController.getInstance().getMutedDocId(), receiverUid, secretId);
          }
        } else {

          if (root != null) {

            Snackbar snackbar =
                Snackbar.make(root, getString(R.string.MuteFailed), Snackbar.LENGTH_SHORT);

            snackbar.show();
            View view = snackbar.getView();
            TextView txtv =
                (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
          }
        }
      }
    });

    setupActivity(getIntent());

    if (blocked) {

      call.setVisibility(View.GONE);
    } else {

      //call.setVisibility(View.VISIBLE);
    }

    ImageView chat = (ImageView) findViewById(R.id.iv_chat);

    call.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          if (!android.provider.Settings.System.canWrite(OpponentProfileNew.this)
              || !android.provider.Settings.canDrawOverlays(OpponentProfileNew.this)) {
            //                        if (!android.provider.Settings.System.canWrite(OpponentProfile.this)) {
            //                            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
            //                            intent.setData(Uri.parse("package:" + getPackageName()));
            //                            startActivity(intent);
            //                        }

            //If the draw over permission is not available open the settings screen
            //to grant the permission.

            if (!android.provider.Settings.canDrawOverlays(OpponentProfileNew.this)) {
              Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
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
    });

    chat.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent intent = new Intent(OpponentProfileNew.this, ChatMessageScreen.class);
        intent.putExtra("receiverUid", receiverUid);
        intent.putExtra("receiverName", receiverName);
        intent.putExtra("documentId", docId);
        intent.putExtra("isStar", isStar);
        intent.putExtra("receiverIdentifier", receiverIdentifier);
        intent.putExtra("receiverImage", receiverImage);
        intent.putExtra("colorCode", AppController.getInstance().getColorCode(5));
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        supportFinishAfterTransition();
      }
    });

    text_block.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        /*
         * For option of the blocking of the user
         */

        requestBlockFeatureOnServer();
      }
    });

    mediaHeader_rl.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        Intent j = new Intent(OpponentProfileNew.this, MediaHistory.class);
        j.putExtra("docId", docId);

        j.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(j,
            ActivityOptionsCompat.makeSceneTransitionAnimation(OpponentProfileNew.this).toBundle());
      }
    });

    collapsingToolbarLayout.setExpandedTitleColor(
        ContextCompat.getColor(OpponentProfileNew.this, R.color.color_text_black));
    collapsingToolbarLayout.setCollapsedTitleTextColor(
        ContextCompat.getColor(OpponentProfileNew.this, R.color.color_text_black));

    TextView tv4 = (TextView) findViewById(R.id.tv4);

    tv4.setTypeface(AppController.getInstance().getRegularFont(), Typeface.NORMAL);

//    text_block.setTypeface(AppController.getInstance().getRegularFont(), Typeface.NORMAL);

    tvLastSeen.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        FloatingView.dismissWindow();
        allowLastSeen = !allowLastSeen;
        if (allowLastSeen) {
          tvLastSeen.setText(getString(R.string.string_692));
        } else {
          tvLastSeen.setText(getString(R.string.string_691));
        }
        AppController.getInstance().updateLastSeenSettings(allowLastSeen);
        AppController.getInstance().updatePresence(1, false);
      }
    });

    tvDeleteChat.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        openDeleteChatDialog();
      }
    });

    TextView tvViewProfile = findViewById(R.id.tvViewProfile);
    tvViewProfile.setOnClickListener(v -> {
      Intent intent = new Intent(OpponentProfileNew.this, ProfileActivity.class);
      intent.putExtra("userId", receiverUid);
      startActivity(intent);
    });

    Switch swHideMyPost = findViewById(R.id.swHideMyPost);
    swHideMyPost.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        hideMyPost(isChecked);
      }
    });

    ImageView close = (ImageView) findViewById(R.id.close);
    close.setOnClickListener(v -> onBackPressed());

    llReportuser.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        reportDialog.show();
      }
    });
    bus.register(this);
  }

  public void addToReportList(ArrayList<String> data) {
    arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
    reportDialog.setAdapter(arrayAdapter, this);
  }

  private void getReportReasons() {
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(
            AppController.getInstance().getApplicationContext());
    Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiOnServer.HOST_API_2)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    retrofit.create(HowdooService.class)
            .userReportReasons(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new DisposableObserver<retrofit2.Response<ReportReason>>() {
              @Override
              public void onNext(retrofit2.Response<ReportReason> response) {
                switch (response.code()) {
                  case 200:
                    addToReportList(response.body().getData());
                    break;
                  case 406:
                    SessionObserver sessionObserver = new SessionObserver();
                    sessionObserver.getObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DisposableObserver<Boolean>() {
                              @Override
                              public void onNext(Boolean flag) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                  @Override
                                  public void run() {
                                    getReportReasons();
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
                    break;
                }
              }

              @Override
              public void onError(Throwable e) {

              }

              @Override
              public void onComplete() {
              }
            });
  }

  private void openDeleteChatDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(OpponentProfileNew.this, 0);
    builder.setTitle(R.string.string_369);
    builder.setMessage(getString(R.string.string_558) + " " + receiverName + "?");
    builder.setPositiveButton(R.string.string_584, new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int id) {

        AppController.getInstance().getDbController().deleteChat(docId);

        /*Delete the chat from server*/
        ChatlistItem item = new ChatlistItem();
        item.setReceiverUid(receiverUid);
        item.setSecretId(secretId);
        deleteChatFromServer(item);

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
        Intent intent = new Intent(getApplicationContext(), LandingActivity.class);
        intent.putExtra("userId", AppController.getInstance().getUserId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
      }
    });

    builder.setNegativeButton(R.string.string_593, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {

        dialog.cancel();
      }
    });

    builder.show();
  }

  private void deleteChatFromServer(final ChatlistItem chat) {

    String secretId = chat.getSecretId();

    if (secretId == null || secretId.isEmpty()) {
      secretId = null;
    }

    JSONObject object = new JSONObject();
    try {
      object.put("recipientId", chat.getReceiverUid());
      object.put("secretId", secretId);
    } catch (JSONException e) {
      e.printStackTrace();
    }

    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
        ApiOnServer.FETCH_CHATS + "?recipientId=" + chat.getReceiverUid() + "&secretId=" + secretId,
        null, new Response.Listener<JSONObject>() {

      @Override
      public void onResponse(JSONObject response) {

        try {
          if (response.getInt("code") == 200) {
            AppController.getInstance()
                .getDbController()
                .deleteParticularChatDetail(AppController.getInstance().getChatDocId(),
                    AppController.getInstance()
                        .findDocumentIdOfReceiver(chat.getReceiverUid(), chat.getSecretId()), false,
                    null);

            //                        AppController.getInstance().getDbController().deleteParticularChatDetail(chat.getDocumentId());
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
          SessionObserver sessionObserver = new SessionObserver();
          sessionObserver.getObservable()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new DisposableObserver<Boolean>() {
                @Override
                public void onNext(Boolean flag) {
                  Handler handler = new Handler();
                  handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      deleteChatFromServer(chat);
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
        } else if (root != null) {

          Snackbar snackbar = Snackbar.make(root, R.string.delete_failed, Snackbar.LENGTH_SHORT);

          snackbar.show();
          View view = snackbar.getView();
          TextView txtv =
              (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
          txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
      }
    }) {
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("authorization", AppController.getInstance().getApiToken());
        headers.put("lang", Constants.LANGUAGE);
        return headers;
      }
    };

    jsonObjReq.setRetryPolicy(new

        DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    /* Add the request to the RequestQueue.*/
    AppController.getInstance().

        addToRequestQueue(jsonObjReq, "deleteChatApiRequest");
  }

  private void hideMyPost(boolean isChecked) {
    //TODO hide my post
  }

  @Override
  protected void onNewIntent(Intent intent) {
    super.onNewIntent(intent);
    setupActivity(intent);
  }

  @Override
  public void onBackPressed() {
    if (AppController.getInstance().isActiveOnACall()) {
      if (AppController.getInstance().isCallMinimized()) {
        super.onBackPressed();
        supportFinishAfterTransition();
      }
    } else {
      super.onBackPressed();
      supportFinishAfterTransition();
    }
  }

  private void minimizeCallScreen(JSONObject obj) {
    try {
      Intent intent = new Intent(OpponentProfileNew.this, ChatMessageScreen.class);

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

  @Override
  protected void onDestroy() {
    super.onDestroy();

    bus.unregister(this);
  }

  @Subscribe
  @SuppressWarnings("TryWithIdenticalCatches")
  public void getMessage(JSONObject object) {
    try {
      if (object.getString("eventName").equals("callMinimized")) {

        minimizeCallScreen(object);
      } else if (object.getString("eventName").substring(0, 3).equals("Onl")) {

        try {

          if (object.getString("userId").equals(receiverUid)) {

            //                        updateLastSeenInActionBar(object);
          }
        } catch (JSONException e) {
          e.printStackTrace();
        }
      } else if (object.getString("eventName")
          .equals(MqttEvents.UserUpdates.value + "/" + AppController.getInstance().getUserId())) {

        switch (object.getInt("type")) {

          case 1:


            /*
             * Status update by any of the contact
             */

            if (!blocked) {

              if (object.getString("userId").equals(receiverUid)) {

                userStatus.setText(object.getString("socialStatus"));
              }
            }
            break;

          case 2:
            /*
             * Profile pic update
             */

            if (object.getString("userId").equals(receiverUid)) {

              receiverImage = object.getString("profilePic");
              if (!blocked) {

                try {

                  Glide.with(OpponentProfileNew.this).load(receiverImage).crossFade()

                      .centerCrop()

                      .placeholder(R.drawable.chat_attachment_profile_default_image_frame).

                      into(userImage);
                } catch (IllegalArgumentException e) {
                  e.printStackTrace();
                } catch (NullPointerException e) {
                  e.printStackTrace();
                }
              }
            }

            break;

          case 4:
            /*
             * New contact added request sent,for the response of the PUT contact api
             */

            switch (object.getInt("subtype")) {

              case 0:

                /*
                 * Follow name or number changed but number still valid
                 */
                if (object.getString("contactUid").equals(receiverUid)) {

                  //userName.setText(object.getString("contactName"));

                  collapsingToolbarLayout.setTitle(object.getString("contactName"));
                }

                break;
              case 1:
                /*
                 * Number of active contact changed and new number not in contact
                 */

                if (object.getString("contactUid").equals(receiverUid)) {

                  if (root != null) {
                    Snackbar snackbar =
                        Snackbar.make(root, R.string.ContactDeleted, Snackbar.LENGTH_SHORT);
                    snackbar.show();

                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(
                        com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                  }
                  new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      onBackPressed();
                    }
                  }, 500);
                }

                break;
            }

            break;

          case 5:
            /*
             * Follow deleted request sent,for the response of the DELETE contact api
             */


            /*
             * Number was in active contact
             */
            if (object.has("status") && object.getInt("status") == 0) {

              if (object.getString("userId").equals(receiverUid)) {

                if (root != null) {
                  Snackbar snackbar =
                      Snackbar.make(root, R.string.ContactDeleted, Snackbar.LENGTH_SHORT);
                  snackbar.show();

                  View view = snackbar.getView();
                  TextView txtv =
                      (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                  txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
                new Handler().postDelayed(new Runnable() {
                  @Override
                  public void run() {
                    supportFinishAfterTransition();
                  }
                }, 500);
              }
            }
            break;

          case 6: {

            /*
             * Block or unblock user
             */

            if (object.getString("initiatorId").equals(receiverUid)) {

              blocked = object.getBoolean("blocked");
              if (blocked) {
                text_block.setVisibility(View.GONE);
                if (call != null) call.setVisibility(View.GONE);

                try {

                  try {
                    userImage.setImageDrawable(TextDrawable.builder()

                        .beginConfig()
                        .textColor(Color.WHITE)
                        .useFont(Typeface.DEFAULT)
                        .fontSize(90
                            * ((int) getResources().getDisplayMetrics().density)) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()

                        .buildRound((receiverName).charAt(0) + "",
                            Color.parseColor(AppController.getInstance().getColorCode(5))));
                  } catch (Exception e) {

                    Glide.with(OpponentProfileNew.this)

                        .load(R.drawable.chat_attachment_profile_default_image_frame).crossFade()

                        .centerCrop()

                        .into(userImage);
                  }
                } catch (IllegalArgumentException e) {
                  e.printStackTrace();
                } catch (NullPointerException e) {
                  e.printStackTrace();
                }
              } else {

                text_block.setVisibility(View.VISIBLE);

                  if (call != null)
                  // call.setVisibility(View.VISIBLE);
                  {
                      try {

                          if (receiverImage != null && !receiverImage.isEmpty()) {
                              Glide.with(OpponentProfileNew.this)

                                  .load(receiverImage).crossFade()

                                  .centerCrop()

                                  .placeholder(
                                      R.drawable.chat_attachment_profile_default_image_frame).

                                  into(userImage);
                          } else {

                              try {
                                  userImage.setImageDrawable(TextDrawable.builder()

                                      .beginConfig()
                                      .textColor(Color.WHITE)
                                      .useFont(Typeface.DEFAULT)
                                      .fontSize(90
                                          * ((int) getResources().getDisplayMetrics().density)) /* size in px */
                                      .bold()
                                      .toUpperCase()
                                      .endConfig()

                                      .buildRound((receiverName).charAt(0) + "", Color.parseColor(
                                          AppController.getInstance().getColorCode(5))));
                              } catch (Exception e) {

                                  Glide.with(OpponentProfileNew.this)

                                      .load(R.drawable.chat_attachment_profile_default_image_frame)
                                      .crossFade()

                                      .centerCrop()

                                      .into(userImage);
                              }
                          }
                      } catch (IllegalArgumentException e) {
                          e.printStackTrace();
                      } catch (NullPointerException e) {
                          e.printStackTrace();
                      }
                  }
              }
            }
            break;
          }
        }
      } else if (object.getString("eventName").equals("ContactNameUpdated")) {
        if (object.getString("userId").equals(receiverUid)) {
          collapsingToolbarLayout.setTitle(object.getString("contactName"));
          //  userName.setText(object.getString("contactName"));
        }
      }
    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("TryWithIdenticalCatches")
  private void setupActivity(Intent intent) {
    receiverImage = "";

    receiverUid = intent.getExtras().getString("contactId");
    blocked = intent.getExtras().getBoolean("blocked");
    isStar = intent.getExtras().getBoolean("isStar");
    docId = intent.getExtras().getString("documentId");
/*    try{
      loadMemberData(receiverUid);
    }catch (Exception e){
      e.printStackTrace();
    }*/
    Map<String, Object> contactInfo = AppController.getInstance().getDbController().
        getFriendInfo(AppController.getInstance().getFriendsDocId(), receiverUid);
    {
    /*if (contactInfo != null) {

      //receiverName = (String) contactInfo.get("contactName");

      //String name = (String) contactInfo.get("firstName")+" "+(String) contactInfo.get("lastName");
      receiverName = CommonClass.createFullName((String) contactInfo.get("firstName"),
          (String) contactInfo.get("lastName"));
//      profileName.setText(receiverName);
      String textblock = "Block" + " " + receiverName;
      String tvreportText = "Report" + " " + receiverName;
      text_block.setText(textblock);
      tv_reportText.setText(tvreportText);
      receiverIdentifier = (String) contactInfo.get("userName");
//      tvUsername.setText("@" + receiverIdentifier);
      number = (String) contactInfo.get("number");
//      tvNumber.setText(number);

      //  userName.setText(receiverName);

//      collapsingToolbarLayout.setTitle(receiverName);

      //receiverIdentifier = (String) contactInfo.get("contactIdentifier");
//      userIdentifier.setText(receiverIdentifier);

      //userStatus.setText((String) contactInfo.get("contactStatus"));
//      userStatus.setText((String) contactInfo.get("socialStatus"));

      //receiverImage = (String) contactInfo.get("contactPicUrl");
      receiverImage = (String) contactInfo.get("profilePic");

      *//*
       *To load the new image everytime
       *
       *//*

      try {
        if (!blocked && (receiverImage != null && !receiverImage.isEmpty())) {

          Glide.with(OpponentProfileNew.this)

              .load(receiverImage).crossFade()

              .centerCrop()
                  .signature(new StringSignature(
                          AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
              .placeholder(R.drawable.chat_attachment_profile_default_image_frame).

              into(userImage);
        } else {

          try {
            userImage.setImageDrawable(TextDrawable.builder()

                .beginConfig()
                .textColor(Color.WHITE)
                .useFont(Typeface.DEFAULT)
                .fontSize(90 * ((int) getResources().getDisplayMetrics().density)) *//* size in px *//*
                .bold()
                .toUpperCase()
                .endConfig()

                .buildRound((receiverName).charAt(0) + "",
                    Color.parseColor(AppController.getInstance().getColorCode(5))));
          } catch (Exception e) {

            Glide.with(OpponentProfileNew.this)

                .load(R.drawable.chat_attachment_profile_default_image_frame).crossFade()

                .centerCrop()

                .into(userImage);
          }
        }

        Glide.with(getBaseContext())
                .load("")
                .asBitmap()
                .centerCrop()
                .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                .placeholder(R.drawable.default_cover_photo)
                .into(ivProfileBg);
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (NullPointerException e) {
        e.printStackTrace();
      }
    } else {*/
      fetchOpponentDetails(receiverUid);
    }


    secretId = "";
    secretId = intent.getExtras().getString("secretId");


    /*
     * For the mute functionality
     */

    muteSwitch.setChecked(AppController.getInstance()
        .getDbController()
        .checkIfReceiverChatMuted(AppController.getInstance().getMutedDocId(), receiverUid,
            secretId));
    /*
     * For the blocked functionality
     */

    if (blocked) {
      checkIfSelfBlocked();
      text_block.setText("Unblock" + " " + userName);
    } else {
      text_block.setText("Block" + " " + userName);
    }

    mMediaData.clear();

    runOnUiThread(new Runnable() {
      @Override
      public void run() {
        mAdapter.notifyDataSetChanged();
      }
    });
    addMediaContent();
  }

  private void showCallTypeChooserPopup(View view) {
    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    updateAndroidSecurityProvider(OpponentProfileNew.this);

    if (!AppController.getInstance().isActiveOnACall()) {


      /* / Open the dialing audio call screen here /*/
      final AlertDialog.Builder builder =
          new AlertDialog.Builder(OpponentProfileNew.this, 0);

      builder.setTitle(getResources().getString(R.string.StartCall));

      builder.setMessage(getResources().getString(R.string.CallOption));

      builder.setPositiveButton(getResources().getString(R.string.AudioCall),
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

              if (ActivityCompat.checkSelfPermission(OpponentProfileNew.this,
                  Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(OpponentProfileNew.this,
                    new String[] { Manifest.permission.RECORD_AUDIO }, 71);
              } else {

                requestAudioCall();
              }
            }
          });
      builder.setNegativeButton(getResources().getString(R.string.VideoCall),
          new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

              ArrayList<String> arr1 = new ArrayList<>();
              if (ActivityCompat.checkSelfPermission(OpponentProfileNew.this,
                  Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                arr1.add(Manifest.permission.CAMERA);
              }

              if (ActivityCompat.checkSelfPermission(OpponentProfileNew.this,
                  Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                arr1.add(Manifest.permission.RECORD_AUDIO);
              }

              if (arr1.size() > 0) {

                ActivityCompat.requestPermissions(OpponentProfileNew.this,
                    arr1.toArray(new String[arr1.size()]), 72);
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
              b_pos.setTextColor(ContextCompat.getColor(OpponentProfileNew.this, R.color.color_black));
            }
            Button n_pos;
            n_pos = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            if (n_pos != null) {
              n_pos.setTextColor(ContextCompat.getColor(OpponentProfileNew.this, R.color.color_black));
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
              TextView txtv =
                  (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
              txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
          }
        });
      }
    } else {
      if (root != null) {
        Snackbar snackbar =
            Snackbar.make(root, getString(R.string.call_initiate), Snackbar.LENGTH_SHORT);

        snackbar.show();
        View view2 = snackbar.getView();
        TextView txtv =
            (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
      }
    }
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
    AppController.getInstance()
        .getDbController()
        .addNewCall(AppController.getInstance().getCallsDocId(), callItem);

//    Common.callerName = receiverName;
//
//    CallingApis.initiateCall(OpponentProfile.this, receiverUid, receiverName, receiverImage, "0",
//        receiverIdentifier, callId, isStar);
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
    AppController.getInstance()
        .getDbController()
        .addNewCall(AppController.getInstance().getCallsDocId(), callItem);
//    Common.callerName = receiverName;
//
//    CallingApis.initiateCall(OpponentProfile.this, receiverUid, receiverName, receiverImage, "1",
//        receiverIdentifier, callId, isStar);
  }

  @SuppressWarnings("TryWithIdenticalCatches")
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
      @NonNull int[] grantResults) {
    if (requestCode == 71) {

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
            == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {

          requestVideoCall();
        }
      } else if (grantResults.length == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED
          && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED
            || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {

          requestVideoCall();
        }
      }
    }
  }

  private void addMediaContent() {

    ArrayList<Map<String, Object>> arrMessage =
        AppController.getInstance().getDbController().retrieveAllMessages(docId);

    Map<String, Object> mapMessage;

    Media_History_Item item;

    int count = 0;
    for (int i = arrMessage.size() - 1; i >= 0; i--) {

      mapMessage = (arrMessage.get(i));

      if (mapMessage.get("messageType").equals("1")) {

        count++;
        item = new Media_History_Item();

        item.setImagePath((String) mapMessage.get("message"));

        item.setTS((String) mapMessage.get("Ts"));
        item.setMessageId((String) mapMessage.get("id"));

        item.setIsSelf(false);

        int downloadStatus = ((int) mapMessage.get("downloadStatus"));

        item.setDownloadStatus(downloadStatus);
        if (downloadStatus == 0) {

          item.setThumbnailPath((String) mapMessage.get("thumbnailPath"));
        }

        item.setMessageType("1");

        mMediaData.add(item);

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            mAdapter.notifyItemInserted(mMediaData.size() - 1);
          }
        });
      } else if (mapMessage.get("messageType").equals("2")) {

        count++;
        item = new Media_History_Item();

        item.setVideoPath((String) mapMessage.get("message"));

        item.setTS((String) mapMessage.get("Ts"));
        item.setMessageId((String) mapMessage.get("id"));
        item.setMessageType("2");

        item.setIsSelf(false);

        int downloadStatus = ((int) mapMessage.get("downloadStatus"));

        item.setDownloadStatus(downloadStatus);

        if (downloadStatus == 0) {

          item.setThumbnailPath((String) mapMessage.get("thumbnailPath"));
        }

        mMediaData.add(item);

        runOnUiThread(new Runnable() {
          @Override
          public void run() {
            mAdapter.notifyItemInserted(mMediaData.size() - 1);
          }
        });
      }
    }

    if (count > 0) {

      MediaHistory_rl.setVisibility(View.VISIBLE);

      mediaCount.setText(String.valueOf(count));
    } else {
      recyclerView.setVisibility(View.GONE);
//      MediaHistory_rl.setVisibility(View.GONE);
    }
  }

  /*
   * to convert string from the 24 hour format to 12 hour format
   */

  //    private void updateLastSeenInActionBar(JSONObject obj) {
  //
  //
  //        try {
  //
  //
  //            if (obj.getBoolean("lastSeenEnabled")) {
  //
  //
  //                switch (obj.getInt("status")) {
  //
  //                    case 1:
  //                        opponentOnline = true;
  //                        runOnUiThread(new Runnable() {
  //                            @Override
  //                            public void run() {
  //
  //
  //                                top = getString(R.string.string_337);
  //
  //
  //                                if (tv != null)
  //                                    tv.setText(top);
  //                            }
  //                        });
  //                        break;
  //                    case 0:
  //                        opponentOnline = false;
  //
  //
  //                        String lastSeen = obj.getString("timestamp");
  //
  //
  //                        lastSeen = Utilities.changeStatusDateFromGMTToLocal(lastSeen);
  //
  //                        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS z");
  //
  //                        Date date2 = new Date(System.currentTimeMillis() - AppController.getInstance().getTimeDelta());
  //                        String current_date = sdf.format(date2);
  //
  //                        current_date = current_date.substring(0, 8);
  //
  //
  //                        if (lastSeen != null) {
  //
  //                            final String onlineStatus;
  //                            if (current_date.equals(lastSeen.substring(0, 8))) {
  //
  //                                lastSeen = convert24to12hourformat(lastSeen.substring(8, 10) + ":" + lastSeen.substring(10, 12));
  //
  //
  //                                onlineStatus = "Last Seen:Today " + lastSeen;
  //
  //                                lastSeen = null;
  //
  //                            } else {
  //
  //                                String last = convert24to12hourformat(lastSeen.substring(8, 10) + ":" + lastSeen.substring(10, 12));
  //
  //
  //                                String date = lastSeen.substring(6, 8) + "-" + lastSeen.substring(4, 6) + "-" + lastSeen.substring(0, 4);
  //
  //                                onlineStatus = "Last Seen:" + date + " " + last;
  //
  //
  //                                last = null;
  //                                date = null;
  //
  //                            }
  //
  //
  //                            runOnUiThread(new Runnable() {
  //                                @Override
  //                                public void run() {
  //
  //                                    top = onlineStatus;
  //                                    if (tv != null) {
  //
  //                                        tv.setText(top);
  //                                    }
  //                                }
  //                            });
  //
  //                        }
  //
  //
  //                        lastSeen = null;
  //                        sdf = null;
  //                        date2 = null;
  //                        current_date = null;
  //
  //                        break;
  //                    case 2:
  //
  //                        opponentOnline = false;
  //                        runOnUiThread(new Runnable() {
  //                            @Override
  //                            public void run() {
  //
  //
  //                                top = getString(R.string.string_755);
  //                                if (tv != null)
  //                                    tv.setText(top);
  //                            }
  //                        });
  //
  //                }
  //
  //                try {
  //
  //
  //                    header_rl.setVisibility(View.VISIBLE);
  //
  //
  //
  //                } catch (NullPointerException e) {
  //                    e.printStackTrace();
  //                }
  //            } else {
  //
  //            /*
  //             * Have to update the visibility
  //             */
  //                switch (obj.getInt("status")) {
  //
  //                    case 1:
  //                        opponentOnline = true;
  //                        break;
  //                    case 0:
  //
  //                        opponentOnline = false;
  //
  //                        break;
  //                    case 2:
  //
  //
  //                        opponentOnline = false;
  //                }
  //
  //                try {
  //
  //
  //                    header_rl.setVisibility(View.GONE);
  //
  //
  //
  //
  //                } catch (NullPointerException e) {
  //                    e.printStackTrace();
  //                }
  //            }
  //        } catch (JSONException e) {
  //            e.printStackTrace();
  //        }
  //
  //
  //    }

  /*
   * To fetch the contact details incase the contact identifier is not in the contacts
   */

  @SuppressWarnings("TryWithIdenticalCatches")
  private void fetchOpponentDetails(String opponentId) {

    final ProgressDialog pDialog = new ProgressDialog(OpponentProfileNew.this, 0);

    pDialog.setCancelable(false);

    pDialog.setMessage(getString(R.string.Fetch_Details));
    pDialog.show();

    ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);

    bar.getIndeterminateDrawable()
        .setColorFilter(ContextCompat.getColor(OpponentProfileNew.this, R.color.color_black),
            PorterDuff.Mode.SRC_IN);

    //        JSONObject obj = new JSONObject();
    //        try {
    //            obj.put("participantId", opponentId);
    //        } catch (JSONException e) {
    //            e.printStackTrace();
    //        }

    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
        ApiOnServer.OPPONENT_PROFILE + "?participantId=" + opponentId, null,
        new Response.Listener<JSONObject>() {

          @Override
          public void onResponse(JSONObject response) {

            try {

              if (response.getInt("code") == 200) {
                response = response.getJSONObject("response");

                if (response.has("profilePic")) {

                  receiverImage = response.getString("profilePic");
                }
                if (response.has("socialStatus")) {

                  textAbout.setText(response.getString("socialStatus"));
                } else {

                  textAbout.setText(getString(R.string.default_status));
                }
                if(response.has("userName")){
                  tvUsername.setText("@"+ response.getString("userName"));
                  userName = response.getString("userName");
                  text_block.setText("Block" + " " + response.getString("userName"));
                }
                if(response.has("firstName")){
                  firstName = response.getString("firstName");
                }
                if(response.has("lastName")){
                  lastName = response.getString("lastName");
                }
                profileName.setText(firstName + lastName);
                if(response.has("countryCode")){
                  countryCode = response.getString("countryCode");
                }
                if(response.has("userIdentifier")){
                  tvNumber.setText("+91" + " " + response.getString("userIdentifier"));
                }
//                if(response.has("mobileNumber")){
//                  tvNumber.setText(countryCode + " " + response.getString("mobileNumber"));
//                }
                if(response.has("emailAddress")){
                  email.setText(response.getString("emailAddress"));
                }

                /*
                 * This is same as the registered name
                 */

                if(receiverName == null || receiverName.isEmpty()){

                  receiverName = response.getString("userName");
                }

                //receiverName = CommonClass.createFullName(response.getString("firstName"),response.getString("lastName"));

//                collapsingToolbarLayout.setTitle(receiverName);
//                profileName.setText(receiverName);

                receiverIdentifier = response.getString("userIdentifier");
//                userIdentifier.setText(receiverIdentifier);

                try {

                  /*
                   *To load the new image every time
                   *
                   */

                  if (!blocked && (receiverImage != null && !receiverImage.isEmpty())) {
                    Glide.with(OpponentProfileNew.this)

                        .load(receiverImage).crossFade()

                        .centerCrop()

                        .placeholder(R.drawable.chat_attachment_profile_default_image_frame).

                        into(userImage);
                  } else {

                    try {
                      userImage.setImageDrawable(TextDrawable.builder()

                          .beginConfig()
                          .textColor(Color.WHITE)
                          .useFont(Typeface.DEFAULT)
                          .fontSize(90 * ((int) getResources().getDisplayMetrics().density)) /* size in px */
                          .bold()
                          .toUpperCase()
                          .endConfig()

                          .buildRound((receiverName).charAt(0) + "",
                              Color.parseColor(AppController.getInstance().getColorCode(5))));
                    } catch (Exception e) {

                      Glide.with(OpponentProfileNew.this)

                          .load(R.drawable.chat_attachment_profile_default_image_frame).crossFade()

                          .centerCrop()

                          .into(userImage);
                    }
                  }
                } catch (IllegalArgumentException e) {
                  e.printStackTrace();
                } catch (NullPointerException e) {
                  e.printStackTrace();
                }
              } else {

                if (pDialog.isShowing()) {

                  Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();

                  if (context instanceof Activity) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                      if (!((Activity) context).isFinishing()
                          && !((Activity) context).isDestroyed()) {
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

                if (root != null) {

                  Snackbar snackbar =
                      Snackbar.make(root, response.getString("message"), Snackbar.LENGTH_SHORT);

                  snackbar.show();
                  View view = snackbar.getView();
                  TextView txtv =
                      (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                  txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }
              }
            } catch (JSONException e) {
              e.printStackTrace();
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
          }
        }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
          SessionObserver sessionObserver = new SessionObserver();
          sessionObserver.getObservable()
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new DisposableObserver<Boolean>() {
                @Override
                public void onNext(Boolean flag) {
                  Handler handler = new Handler();
                  handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                      fetchOpponentDetails(opponentId);
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
        } else {
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

          if (root != null) {

            Snackbar snackbar =
                Snackbar.make(root, getString(R.string.No_Internet_Connection_Available),
                    Snackbar.LENGTH_SHORT);

            snackbar.show();
            View view = snackbar.getView();
            TextView txtv =
                (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
          }

          new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
              supportFinishAfterTransition();
            }
          }, 500);
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

    jsonObjReq.setRetryPolicy(
        new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    /* Add the request to the RequestQueue.*/
    AppController.getInstance().addToRequestQueue(jsonObjReq, "opponentProfileApiRequest");
  }

  /**
   * To hit the block user api on the server
   */
  @SuppressWarnings("TryWithIdenticalCatches")
  private void requestBlockFeatureOnServer() {

    String str;

    final ProgressDialog pDialog = new ProgressDialog(OpponentProfileNew.this, 0);
    pDialog.setCancelable(true);

    pDialog.setCancelable(false);
    if (blocked) {
      str = "unblock";

      pDialog.setMessage(getString(R.string.BlockUser, "Unblocking"));
    } else {
      str = "block";

      pDialog.setMessage(getString(R.string.BlockUser, "Blocking"));
    }

    pDialog.show();

    ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);

    bar.getIndeterminateDrawable()
        .setColorFilter(ContextCompat.getColor(OpponentProfileNew.this, R.color.color_black),
            PorterDuff.Mode.SRC_IN);

    JSONObject obj = new JSONObject();
    try {
      obj.put("opponentId", receiverUid);
      obj.put("type", str);
    } catch (JSONException e) {
      e.printStackTrace();
    }
    JsonObjectRequest jsonObjReq =
        new JsonObjectRequest(Request.Method.POST, ApiOnServer.BLOCK_USER, obj,
            new Response.Listener<JSONObject>() {

              @Override
              public void onResponse(JSONObject response) {

                try {

                  if (response.getInt("code") == 200) {

                    if (blocked) {

                      AppController.getInstance()
                          .getDbController()
                          .removeUnblockedUser(AppController.getInstance().getFriendsDocId(),
                              receiverUid);


                      /*
                       *  Successfully unblocked previously blocked user
                       */

                      JSONObject obj = new JSONObject();

                      obj.put("eventName", "UserUnblocked");

                      obj.put("opponentId", receiverUid);
                      // obj.put("initiatorId", AppController.getInstance().getUserId());
                      bus.post(obj);

                      obj = new JSONObject();
                      obj.put("type", 6);

                      obj.put("blocked", false);

                      obj.put("initiatorId", AppController.getInstance().getUserId());

                      AppController.getInstance()
                          .publish(MqttEvents.UserUpdates.value + "/" + receiverUid, obj, 1, false);

                      text_block.setText("Block" + " " + receiverName);
                        if (call != null)
                        // call.setVisibility(View.VISIBLE);
                        {
                            try {

                                if (receiverImage != null && !receiverImage.isEmpty()) {
                                    Glide.with(OpponentProfileNew.this)

                                        .load(receiverImage).crossFade()

                                        .centerCrop()

                                        .placeholder(
                                            R.drawable.chat_attachment_profile_default_image_frame).

                                        into(userImage);
                                } else {

                                    try {
                                        userImage.setImageDrawable(TextDrawable.builder()

                                            .beginConfig()
                                            .textColor(Color.WHITE)
                                            .useFont(Typeface.DEFAULT)
                                            .fontSize(90
                                                * ((int) getResources().getDisplayMetrics().density)) /* size in px */
                                            .bold()
                                            .toUpperCase()
                                            .endConfig()

                                            .buildRound((receiverName).charAt(0) + "",
                                                Color.parseColor(
                                                    AppController.getInstance().getColorCode(5))));
                                    } catch (Exception e) {

                                        Glide.with(OpponentProfileNew.this)

                                            .load(
                                                R.drawable.chat_attachment_profile_default_image_frame)
                                            .crossFade()

                                            .centerCrop()

                                            .into(userImage);
                                    }
                                }
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                      /*
                       *Successfully blocked previously unblocked user
                       */
                      AppController.getInstance()
                          .getDbController()
                          .addBlockedUser(AppController.getInstance().getFriendsDocId(),
                              receiverUid, receiverIdentifier, true);

                      JSONObject obj = new JSONObject();
                      // obj.put("initiatorId", AppController.getInstance().getUserId());
                      obj.put("eventName", "UserBlocked");
                      obj.put("opponentId", receiverUid);
                      bus.post(obj);

                      obj = new JSONObject();
                      obj.put("type", 6);
                      obj.put("blocked", true);
                      obj.put("initiatorIdentifier",
                          AppController.getInstance().getUserIdentifier());
                      obj.put("initiatorId", AppController.getInstance().getUserId());

                      AppController.getInstance()
                          .publish(MqttEvents.UserUpdates.value + "/" + receiverUid, obj, 1, false);

                      text_block.setText("Unblock" + " " + receiverName);
                      if (call != null) call.setVisibility(View.GONE);
                      try {

                        try {
                          userImage.setImageDrawable(TextDrawable.builder()

                              .beginConfig()
                              .textColor(Color.WHITE)
                              .useFont(Typeface.DEFAULT)
                              .fontSize(90
                                  * ((int) getResources().getDisplayMetrics().density)) /* size in px */
                              .bold()
                              .toUpperCase()
                              .endConfig()

                              .buildRound((receiverName).charAt(0) + "",
                                  Color.parseColor(AppController.getInstance().getColorCode(5))));
                        } catch (Exception e) {

                          Glide.with(OpponentProfileNew.this)

                              .load(R.drawable.chat_attachment_profile_default_image_frame)
                              .crossFade()

                              .centerCrop()

                              .into(userImage);
                        }
                      } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                      } catch (NullPointerException e) {
                        e.printStackTrace();
                      }
                    }

                    blocked = !blocked;
                  }

                  if (pDialog.isShowing()) {

                    Context context = ((ContextWrapper) (pDialog).getContext()).getBaseContext();

                    if (context instanceof Activity) {

                      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        if (!((Activity) context).isFinishing()
                            && !((Activity) context).isDestroyed()) {
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
            }, new Response.ErrorListener() {

          @Override
          public void onErrorResponse(VolleyError error) {
            if (error.networkResponse != null && error.networkResponse.statusCode == 406) {
              SessionObserver sessionObserver = new SessionObserver();
              sessionObserver.getObservable()
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new DisposableObserver<Boolean>() {
                    @Override
                    public void onNext(Boolean flag) {
                      Handler handler = new Handler();
                      handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                          requestBlockFeatureOnServer();
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
            }
            if (root != null) {

              Snackbar snackbar = Snackbar.make(root, R.string.No_Internet_Connection_Available,
                  Snackbar.LENGTH_SHORT);

              snackbar.show();
              View view = snackbar.getView();
              TextView txtv =
                  (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
              txtv.setGravity(Gravity.CENTER_HORIZONTAL);
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

    jsonObjReq.setRetryPolicy(
        new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    /* Add the request to the RequestQueue.*/
    AppController.getInstance().addToRequestQueue(jsonObjReq, "opponentBlockApiRequest");
  }

  private void checkIfSelfBlocked() {

    if (AppController.getInstance()
        .getDbController()
        .checkIfSelfBlocked(AppController.getInstance().getFriendsDocId(), receiverUid)) {

      text_block.setVisibility(View.VISIBLE);
    } else {
      text_block.setVisibility(View.GONE);
    }
  }
  public void loadMemberData(String userId) {
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(
            AppController.getInstance().getApplicationContext());
    Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiOnServer.MAIN_LINK)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    retrofit.create(HowdooService.class)
            .getMember(AppController.getInstance().getApiToken(), Constants.LANGUAGE, userId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Observer<retrofit2.Response<Profile>>() {
              @Override
              public void onSubscribe(Disposable d) {

              }

              @Override
              public void onNext(retrofit2.Response<Profile> response) {

                switch (response.code()) {
                  case 200:
                    if (response.body() != null) {
                      showProfileData(response.body());
                    }
                    break;
                }
              }

              @Override
              public void onError(@NonNull Throwable e) {

              }

              @Override
              public void onComplete() {

              }
            });
  }

  public void showProfileData(Profile profile) {
    try {
      Log.d("usedId3=", profile.getData().get(0).getId());
      profileName.setText(profile.getData().get(0).getFirstName() + profile.getData().get(0).getLastName());
      tvUsername.setText(profile.getData().get(0).getUserName());
      email.setText(profile.getData().get(0).getEmail());
      tvNumber.setText(profile.getData().get(0).getNumber());
    } catch (Exception ignored) {
    }
  }

  @Override
  public void onClick(DialogInterface dialog, int i) {
    AlertDialog.Builder confirm = new AlertDialog.Builder(this);
    confirm.setMessage("Are you sure you want to report " + userName + "?");
    confirm.setPositiveButton(R.string.confirm,
            (mdialog, w) -> reportUser(receiverUid, arrayAdapter.getItem(i),
                    arrayAdapter.getItem(i)));
    confirm.setNegativeButton(R.string.cancel, (mdialog, w) -> dialog.dismiss());
    confirm.create().show();
  }

  public void reportUser(String userId, String reason, String message) {
    Map<String, String> map = new HashMap<>();
    map.put("targetUserId", userId);
    map.put("reason", reason);
    map.put("message", message);
    OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(
            AppController.getInstance().getApplicationContext());
    Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiOnServer.HOST_API_2)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    retrofit.create(HowdooService.class)
            .reportUser(AppController.getInstance().getApiToken(), Constants.LANGUAGE, map)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new DisposableObserver<retrofit2.Response<ResponseBody>>() {

              @Override
              public void onNext(retrofit2.Response<ResponseBody> response) {
                switch (response.code()) {
                  case 200:
                   showMessage(null, R.string.reported_profile);
                    break;
                  case 406:
                    SessionObserver sessionObserver = new SessionObserver();
                    sessionObserver.getObservable()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DisposableObserver<Boolean>() {
                              @Override
                              public void onNext(Boolean flag) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                  @Override
                                  public void run() {
                                    reportUser(userId, reason, message);
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
                    break;
                }
              }

              @Override
              public void onError(Throwable e) {

              }

              @Override
              public void onComplete() {
              }
            });
  }

  private void showMessage(String msg, int msgId) {
    Toast.makeText(this, msg != null && !msg.isEmpty() ? msg : getResources().getString(msgId), Toast.LENGTH_SHORT).show();
    if (msgId == 204) {
      //user not found
      finish();
    }
  }

}