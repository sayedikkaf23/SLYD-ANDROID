package chat.hola.com.app.GroupChat.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ezcall.android.R;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.Activities.MediaHistory;
import chat.hola.com.app.Activities.MediaHistory_FullScreenVideo;
import chat.hola.com.app.Adapters.OpponentMediaHistory_Adapter;
import chat.hola.com.app.AppController;
import chat.hola.com.app.GroupChat.Adapters.GroupInfoMembersAdapter;
import chat.hola.com.app.GroupChat.ModelClasses.GroupInfoMemberItem;
import chat.hola.com.app.GroupChat.Utilities.SortGroupMembers;
import chat.hola.com.app.ModelClasses.Media_History_Item;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.CustomLinearLayoutManager;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by moda on 22/09/17.
 */

public class GroupInfo extends AppCompatActivity {
    private SessionApiCall sessionApiCall = new SessionApiCall();
    private Bus bus = AppController.getBus();
    private RelativeLayout root;
    private RelativeLayout MediaHistory_rl, addParticipants_rl;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView mediaCount, participantsCount;

    private OpponentMediaHistory_Adapter mAdapter;
    private GroupInfoMembersAdapter mAdapter2;

    private ArrayList<Media_History_Item> mMediaData = new ArrayList<>();

    private ArrayList<GroupInfoMemberItem> mGroupMembersData = new ArrayList<>();


    private String docId, groupMembersDocId, groupId;
    private ImageView groupImage, editName;


    private boolean isAdmin;


    private TextView groupName, groupCreatedInfo;
    private String groupImageUrl;


    private float percentage;

    private boolean titleHidden = false;


    private CardView mediaCardView, exitCardView, deleteCardView, muteCardView;

    private RecyclerView recyclerViewMembers;


    private SwitchCompat muteSwitch;


    @SuppressWarnings("unchecked,TryWithIdenticalCatches")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);


        setContentView(R.layout.gc_details);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        root = (RelativeLayout) findViewById(R.id.root);

        MediaHistory_rl = (RelativeLayout) findViewById(R.id.media_rl);
        RelativeLayout mediaHeader_rl = (RelativeLayout) findViewById(R.id.mediaHeader_rl);
        addParticipants_rl = (RelativeLayout) findViewById(R.id.addMember);

        muteCardView = (CardView) findViewById(R.id.cv5);

        muteSwitch = (SwitchCompat) findViewById(R.id.iv5);


        mediaCardView = (CardView) findViewById(R.id.cv1);


        exitCardView = (CardView) findViewById(R.id.cv3);

        deleteCardView = (CardView) findViewById(R.id.cv4);


        groupName = (TextView) findViewById(R.id.groupName);
        groupCreatedInfo = (TextView) findViewById(R.id.createdBy);

        editName = (ImageView) findViewById(R.id.edit);


        mediaCount = (TextView) findViewById(R.id.mediaCount);

        participantsCount = (TextView) findViewById(R.id.numberOfParticipants);


        groupImage = (ImageView) findViewById(R.id.userImage);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_container);
        collapsingToolbarLayout.setTitleEnabled(true);
        ImageView close = (ImageView) findViewById(R.id.close);
        /*
         * Recyclerview for the media history items
         */


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.media_rv);
        recyclerView.setHasFixedSize(true);
        mAdapter = new OpponentMediaHistory_Adapter(GroupInfo.this, mMediaData);


        recyclerView.setLayoutManager(new CustomLinearLayoutManager(GroupInfo.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        recyclerView.setAdapter(mAdapter);


        /*
         * Recyclerview for the group members
         */


        recyclerViewMembers = (RecyclerView) findViewById(R.id.groupMembers);
        recyclerViewMembers.setHasFixedSize(true);
        mAdapter2 = new GroupInfoMembersAdapter(GroupInfo.this, mGroupMembersData);


        recyclerViewMembers.setLayoutManager(new CustomLinearLayoutManager(GroupInfo.this, LinearLayoutManager.VERTICAL, false));
        recyclerViewMembers.setItemAnimator(new DefaultItemAnimator());


        recyclerViewMembers.setAdapter(mAdapter2);


        setupActivity(getIntent());


        recyclerViewMembers.addOnItemTouchListener(new RecyclerItemClickListener(GroupInfo.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position >= 0) {


                    final GroupInfoMemberItem groupInfoMemberItem = mGroupMembersData.get(position);


                    if (isAdmin && !groupInfoMemberItem.getContactUid().equals(AppController.getInstance().getUserId())) {


                        final Dialog dialog = new Dialog(GroupInfo.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.gc_admin_options);
                        dialog.show();


                        TextView makeAdmin = (TextView) dialog.findViewById(R.id.makeAdmin);
                        TextView removeMember = (TextView) dialog.findViewById(R.id.removeMember);


                        RelativeLayout makeAdmin_rl = (RelativeLayout) dialog.findViewById(R.id.rl1);
                        RelativeLayout removeMember_rl = (RelativeLayout) dialog.findViewById(R.id.rl2);


                        if (groupInfoMemberItem.isAdmin()) {
                            makeAdmin_rl.setVisibility(View.GONE);

                        }


                        removeMember.setText(getString(R.string.Remove) + getString(R.string.space)
                                + groupInfoMemberItem.getContactName());
                        makeAdmin.setText(getString(R.string.Make) + getString(R.string.space)
                                + groupInfoMemberItem.getContactName() +
                                getString(R.string.space) + getString(R.string.MakeAdmin));

                        removeMember_rl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {


                                dialog.dismiss();

                                removeMemberFromGroup(groupInfoMemberItem.getContactUid(), groupInfoMemberItem.getContactIdentifier(),
                                        groupInfoMemberItem.getContactName(),

                                        getString(R.string.Removing) +
                                                getString(R.string.space) + groupInfoMemberItem.getContactName());

                            }
                        });


                        makeAdmin_rl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                                makeMemberGroupAdmin(groupInfoMemberItem.getContactUid(),
                                        groupInfoMemberItem.getContactIdentifier(), groupInfoMemberItem.getContactName(),
                                        getString(R.string.Making) +getString(R.string.space)
                                                + groupInfoMemberItem.getContactName() +
                                                getString(R.string.space) + getString(R.string.MakeAdmin));

                            }
                        });
                    }


                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(GroupInfo.this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                final Media_History_Item item = mMediaData.get(position);

                if (item.getMessageType().equals("2")) {


                    if (item.getDownloadStatus() == 1) {

                        if (ActivityCompat.checkSelfPermission(GroupInfo.this, Manifest.permission.READ_EXTERNAL_STORAGE)
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

                                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(GroupInfo.this).toBundle());

                                } catch (ActivityNotFoundException e) {
                                    Intent i = new Intent(GroupInfo.this, MediaHistory_FullScreenVideo.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    i.putExtra("videoPath", item.getVideoPath());
                                    startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(GroupInfo.this).toBundle());


                                }
                            } else {
                                if (root != null) {
                                    Snackbar snackbar = Snackbar.make(root, R.string.string_1005, Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view2 = snackbar.getView();
                                    TextView txtv2 = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv2.setGravity(Gravity.CENTER_HORIZONTAL);

                                }
                            }
                        } else {
                            if (root != null) {
                                Snackbar snackbar = Snackbar.make(root, R.string.string_1006, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view2 = snackbar.getView();
                                TextView txtv2 = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv2.setGravity(Gravity.CENTER_HORIZONTAL);

                            }

                        }

                    } else {


                        if (root != null) {
                            Snackbar snackbar = Snackbar.make(root, R.string.string_1004, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view2 = snackbar.getView();
                            TextView txtv2 = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
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

                if (groupId != null) {
                    if (isChecked) {
                        /*
                         *To add a muted chat
                         */

                        AppController.getInstance().getDbController().addMuteChat(AppController.getInstance().getMutedDocId(), groupId, "");


                    } else {

                        /*
                         *To remove a muted chat
                         */

                        AppController.getInstance().getDbController().removeMuteChat(AppController.getInstance().getMutedDocId(), groupId, "");


                    }

                } else {


                    if (root != null) {


                        Snackbar snackbar = Snackbar.make(root, getString(R.string.SaveFailed), Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }


                }

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                onBackPressed();
            }
        });
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(GroupInfo.this, EditGroupSubject.class);
                intent.putExtra("groupId", groupId);

                intent.putExtra("groupSubject", groupName.getText().toString().trim());

                intent.putExtra("groupMembersDocId", groupMembersDocId);

                intent.putExtra("groupMessagesDocId", docId);


                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);

            }
        });


        groupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (groupImageUrl != null && !groupImageUrl.isEmpty()) {
                Intent intent = new Intent(GroupInfo.this, GroupIconFullScreen.class);
                intent.putExtra("groupId", groupId);

                intent.putExtra("groupImageUrl", groupImageUrl);

                intent.putExtra("groupMembersDocId", groupMembersDocId);

                intent.putExtra("groupMessagesDocId", docId);


                intent.putExtra("canEditImage", (exitCardView.getVisibility() == View.VISIBLE));


                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(GroupInfo.this, groupImage, "image");
                startActivity(intent, options.toBundle());
//                }
            }
        });


        mediaHeader_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupInfo.this, MediaHistory.class);
                intent.putExtra("docId", docId);

                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(GroupInfo.this).toBundle());

            }
        });


        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(GroupInfo.this, R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(GroupInfo.this, R.color.color_text_black));


        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.my_appbar_container);


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                percentage = (float) Math.abs(offset) / (float) maxScroll;


                if (percentage < 0.05) {


                    if (titleHidden) {


                        groupName.setVisibility(View.VISIBLE);
                        groupCreatedInfo.setVisibility(View.VISIBLE);

                        titleHidden = false;
                    }


                } else {


                    if (!titleHidden) {


                        groupName.setVisibility(View.GONE);
                        groupCreatedInfo.setVisibility(View.GONE);
                        titleHidden = true;

                    }


                }


            }
        });
        addParticipants_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(GroupInfo.this, AddMember.class);

                intent.putExtra("groupId", groupId);
                intent.putExtra("groupName", groupName.getText().toString().trim());
                intent.putExtra("groupMembersDocId", groupMembersDocId);

                intent.putExtra("groupMessagesDocId", docId);
                intent.putExtra("groupImageUrl", groupImageUrl);

                startActivity(intent);


            }
        });


        exitCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                exitGroup();

            }
        });


        deleteCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteGroup();

            }
        });


        Typeface tf = AppController.getInstance().getRegularFont();

        groupName.setTypeface(tf, Typeface.BOLD);

        groupCreatedInfo.setTypeface(tf, Typeface.NORMAL);


        participantsCount.setTypeface(tf, Typeface.NORMAL);


        TextView tv = (TextView) findViewById(R.id.tv);
        tv.setTypeface(tf, Typeface.NORMAL);


        TextView tv2 = (TextView) findViewById(R.id.tv2);
        tv2.setTypeface(tf, Typeface.NORMAL);
        TextView tv3 = (TextView) findViewById(R.id.tv3);
        tv3.setTypeface(tf, Typeface.NORMAL);


        TextView tv4 = (TextView) findViewById(R.id.tv4);
        tv4.setTypeface(tf, Typeface.NORMAL);

        applyFontForToolbarTitle(this);
        bus.register(this);
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
            Intent intent = new Intent(GroupInfo.this, ChatMessageScreen.class);

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

    private void addMediaContent() {

        ArrayList<Map<String, Object>> arrMessage = AppController.getInstance().getDbController().retrieveAllMessages(docId);

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

            /*
             * To avoid padding issuie on pre lollipop
             */
            mediaCardView.setVisibility(View.VISIBLE);
        } else {

            MediaHistory_rl.setVisibility(View.GONE);

            /*
             * To avoid padding issuie on pre lollipop
             */
            mediaCardView.setVisibility(View.GONE);
        }
    }


    private void setupActivity(Intent intent) {

        groupImageUrl = "";
        mMediaData.clear();
        mGroupMembersData.clear();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
                mAdapter2.notifyDataSetChanged();
            }
        });

        /*
         * document contains the document id for the group chat messages
         */


        Bundle extras = intent.getExtras();
        docId = extras.getString("documentId");

        groupMembersDocId = extras.getString("groupMembersDocId");

        groupId = extras.getString("groupId");
        groupImageUrl = extras.getString("groupImage");
        Glide.with(GroupInfo.this)
                .load(groupImageUrl)
                .crossFade()

                .centerCrop()


                .placeholder(R.drawable.avatar_group_large).

                into(groupImage);

        collapsingToolbarLayout.setTitle(extras.getString("groupName"));

        updateGroupSubject(extras.getString("groupName"));

        if (groupMembersDocId == null) {

            fetchGroupMemberDetailsViaApi(extras.getString("groupName"));

        } else {

            //fetchGroupMemberDetailsViaApi(extras.getString("groupName"));


            addGroupMembers();
            addMediaContent();
        }


    }


    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("callMinimized")) {

                minimizeCallScreen(object);
            } else if (object.getString("eventName").equals(MqttEvents.UserUpdates.value + "/" + AppController.getInstance().getUserId())) {


                switch (object.getInt("type")) {


                    case 1:


                        /*
                         * Status update by any of the contact
                         */



                        /*
                         * To update in the contacts list
                         */

                        final int pos = findContactPositionInList(object.getString("userId"));
                        if (pos != -1) {


                            GroupInfoMemberItem item = mGroupMembersData.get(pos);


                            item.setContactStatus(object.getString("socialStatus"));

                            mGroupMembersData.set(pos, item);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter2.notifyItemChanged(pos);
                                }
                            });
                        }


                        break;

                    case 2:
                        /*
                         * Profile pic update
                         */


                        /*
                         * To update in the group members list
                         */


                        final int pos1 = findContactPositionInList(object.getString("userId"));
                        if (pos1 != -1) {

                            /*
                             * Have to clear the glide cache
                             */

                            GroupInfoMemberItem item = mGroupMembersData.get(pos1);


                            item.setContactImage(object.getString("profilePic"));
                            mGroupMembersData.set(pos1, item);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter2.notifyItemChanged(pos1);
                                }
                            });
                        }


                        break;
                    case 3:
                        /*
                         * Any of mine previous phone contact join
                         */

                        GroupInfoMemberItem item2 = new GroupInfoMemberItem();
                        item2.setContactImage("");
                        item2.setContactUid(object.getString("userId"));


                        item2.setContactStatus(getString(R.string.default_status));

                        item2.setContactIdentifier(object.getString("number"));


                        item2.setContactName(object.getString("name"));
                        final int position = findContactPositionInList(object.getString("userId"));
                        if (position == -1) {


                            mGroupMembersData.add(item2);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter2.notifyItemInserted(mGroupMembersData.size() - 1);
                                }
                            });


                        } else {
                            mGroupMembersData.set(position, item2);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter2.notifyItemChanged(position);
                                }
                            });

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


                                final int pos3 = findContactPositionInList(object.getString("contactUid"));


                                if (pos3 != -1) {


                                    GroupInfoMemberItem item = mGroupMembersData.get(pos3);


                                    item.setContactName(object.getString("contactName"));
                                    mGroupMembersData.set(pos3, item);

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter2.notifyItemChanged(pos3);
                                        }
                                    });


                                }

                                break;
                            case 1:
                                /*
                                 * Number of active contact changed and new number not in contact
                                 */

                                final int pos4 = findContactPositionInList(object.getString("contactUid"));


                                if (pos4 != -1) {


                                    mGroupMembersData.remove(pos4);


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter2.notifyItemChanged(pos4);
                                        }
                                    });
                                }

                                break;

                            case 2:

                                /*
                                 * New contact added
                                 */


                                try {
                                    GroupInfoMemberItem item = new GroupInfoMemberItem();

                                    item.setContactImage(object.getString("contactPicUrl"));
                                    item.setContactName(object.getString("contactName"));
                                    item.setContactStatus(object.getString("contactStatus"));
                                    item.setContactIdentifier(object.getString("contactIdentifier"));
                                    item.setContactUid(object.getString("contactUid"));
                                    mGroupMembersData.add(item);


                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter2.notifyItemInserted(mGroupMembersData.size() - 1);
                                        }
                                    });

                                } catch (JSONException e) {
                                    e.printStackTrace();
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

                            final int pos2 = findContactPositionInList(object.getString("userId"));

                            if (pos2 != -1) {


                                mGroupMembersData.remove(pos2);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        mAdapter2.notifyItemChanged(pos2);
                                        //  mAdapter.notifyDataSetChanged();
                                    }
                                });
                            }
                        }
                        break;


                }


            } else if (object.getString("eventName").equals("ContactNameUpdated")) {

                final int pos = findContactPositionInList(object.getString("contactUid"));


                GroupInfoMemberItem item = mGroupMembersData.get(pos);


                item.setContactName(object.getString("contactName"));
                mGroupMembersData.set(pos, item);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter2.notifyDataSetChanged();
                    }
                });

            } else if (object.getString("eventName").equals(MqttEvents.GroupChats.value + "/" + AppController.getInstance().getUserId())) {

                /*
                 * Don't have to take any action for the locally received ones
                 */


                if (object.getString("groupId").equals(groupId)) {
                    if (!object.has("self")) {


                        switch (object.getInt("type")) {


                            case 1:
                                /*
                                 * Member added to group
                                 */

                                try {


                                    String memberId = object.getString("memberId");


                                    if (memberId.equals(AppController.getInstance().getUserId())) {

                                        /*
                                         * Update the entire list of the members
                                         */

                                        mGroupMembersData.clear();
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                mAdapter2.notifyDataSetChanged();
                                            }
                                        });


                                        addGroupMembers();
                                        //    updateExitGroupVisibility(true);

                                    } else {
                                        boolean exists = false;

                                        for (int i = 0; i < mGroupMembersData.size(); i++) {


                                            if (mGroupMembersData.get(i).getContactUid().equals(memberId)) {

                                                exists = true;

                                                break;
                                            }
                                        }


                                        if (!exists) {
                                            GroupInfoMemberItem item = new GroupInfoMemberItem();

                                            item.setContactImage(object.getString("memberImage"));
                                            item.setContactName(object.getString("memberName"));
                                            item.setContactStatus(object.getString("memberStatus"));
                                            item.setContactIdentifier(object.getString("memberIdentifier"));
                                            item.setContactUid(object.getString("memberId"));


                                            /*
                                             * To avoid adding duplicated in the ui
                                             */


                                            mGroupMembersData.add(item);


                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {


                                                    // mAdapter2.notifyItemInserted(mGroupMembersData.size() - 1);


                                                    Collections.sort(mGroupMembersData, new SortGroupMembers());

                                                    mAdapter2.notifyDataSetChanged();
                                                }
                                            });
                                        }


                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                updateParticipantsCount();


                                break;
                            case 2:
                                /*
                                 *
                                 *Member removed
                                 */
                                removeMemberFromGroupUI(object.getString("memberId"));


                                break;

                            case 3:

                                /*
                                 *
                                 *Member made admin
                                 */
                                makeMemberAdminInGroupUI(object.getString("memberId"));

                                break;


                            case 4:

                                /*
                                 *
                                 *Group subject updated
                                 */
                                updateGroupSubject(object.getString("groupSubject"));

                                break;


                            case 5:
                                /*
                                 *
                                 *Group icon updated
                                 */

                                updateGroupIcon(object.getString("groupImageUrl"));
                                break;


                            case 6:

                                /*
                                 *
                                 *Member left the conversation
                                 */
                                removeMemberFromGroupUI(object.getString("initiatorId"));


                                break;
                        }

                    } else {

                        switch (object.getInt("type")) {

                            case 1:
                                /*
                                 * Member added to group
                                 */

                                try {


                                    String memberId = object.getString("memberId");
                                    boolean exists = false;

                                    for (int i = 0; i < mGroupMembersData.size(); i++) {


                                        if (mGroupMembersData.get(i).getContactUid().equals(memberId)) {

                                            exists = true;

                                            break;
                                        }
                                    }


                                    if (!exists) {
                                        GroupInfoMemberItem item = new GroupInfoMemberItem();

                                        item.setContactImage(object.getString("memberImage"));
                                        item.setContactName(object.getString("memberName"));
                                        item.setContactStatus(object.getString("memberStatus"));
                                        item.setContactIdentifier(object.getString("memberIdentifier"));
                                        item.setContactUid(object.getString("memberId"));


                                        /*
                                         * To avoid adding duplicated in the ui
                                         */


                                        mGroupMembersData.add(item);


                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {


                                                // mAdapter2.notifyItemInserted(mGroupMembersData.size() - 1);


                                                Collections.sort(mGroupMembersData, new SortGroupMembers());

                                                mAdapter2.notifyDataSetChanged();
                                            }
                                        });
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                updateParticipantsCount();


                                break;

                            case 4:

                                /*
                                 *
                                 *Group subject updated
                                 */
                                updateGroupSubject(object.getString("groupSubject"));

                                break;
                            case 5:
                                /*
                                 *
                                 *Group icon updated
                                 */

                                updateGroupIcon(object.getString("groupImageUrl"));
                                break;

                        }
                    }


                }


            }


        } catch (
                JSONException e) {
            e.printStackTrace();
        }

    }


    private int findContactPositionInList(String contactUid) {
        int pos = -1;

        for (int i = 0; i < mGroupMembersData.size(); i++) {


            if (mGroupMembersData.get(i).getContactUid().equals(contactUid)) {

                return i;
            }
        }

        return pos;
    }


    @SuppressWarnings("unchecked")
    private void addGroupMembers() {
        isAdmin = false;


        boolean isCurrentUserNotMember = true;


        Map<String, Object> groupInfo = AppController.getInstance().getDbController().fetchGroupInfo(groupMembersDocId);

//        ArrayList<Map<String, Object>> groupMembers = AppController.getInstance().getDbController().fetchGroupMember(groupMembersDocId);

        ArrayList<Map<String, Object>> groupMembers = (ArrayList<Map<String, Object>>) groupInfo.get("groupMembersArray");

        Map<String, Object> groupMember, contactInfo;

        String memberId, image;
        String userId = AppController.getInstance().getUserId();


        String contactDocId = AppController.getInstance().getFriendsDocId();


        GroupInfoMemberItem groupInfoMemberItem;

        for (int i = 0; i < groupMembers.size(); i++) {
            groupMember = groupMembers.get(i);
            groupInfoMemberItem = new GroupInfoMemberItem();


            memberId = (String) groupMember.get("memberId");


            if (memberId.equals(userId)) {


                groupInfoMemberItem.setContactName(getString(R.string.You));


                image = AppController.getInstance().getUserImageUrl();


                if (image != null && !image.isEmpty()) {
                    groupInfoMemberItem.setContactImage(image);
                } else {

                    groupInfoMemberItem.setContactImage("");
                }


                groupInfoMemberItem.setContactStatus(new SessionManager(this).getUserName());
                groupInfoMemberItem.setContactIdentifier(AppController.getInstance().getUserIdentifier());
                groupInfoMemberItem.setAdmin((boolean) groupMember.get("memberIsAdmin"));


                groupInfoMemberItem.setContactUid(memberId);

                groupInfoMemberItem.setStar(new SessionManager(GroupInfo.this).isStar());
                if ((boolean) groupMember.get("memberIsAdmin")) {


                    addParticipants_rl.setVisibility(View.VISIBLE);


                    isAdmin = true;
                } else {

                    addParticipants_rl.setVisibility(View.GONE);


                }


                isCurrentUserNotMember = false;
                updateExitGroupVisibility(true);
            } else {

                /*
                 * Have to fetch each member's contact details
                 */



                /*
                 * If the uid exists in contacts
                 */


                contactInfo = AppController.getInstance().getDbController().getFriendInfoFromUid(contactDocId, memberId);

                if (contactInfo != null) {
                    groupInfoMemberItem.setContactName(contactInfo.get("firstName") + " " + contactInfo.get("lastName"));


                    image = (String) contactInfo.get("profilePic");


                    if (image != null && !image.isEmpty()) {
                        groupInfoMemberItem.setContactImage(image);
                    } else {

                        groupInfoMemberItem.setContactImage("");
                    }
                    groupInfoMemberItem.setContactStatus((String) contactInfo.get("userName"));
                    groupInfoMemberItem.setContactIdentifier((String) contactInfo.get("userName"));
                } else {


                    /*
                     * If userId doesn't exists in contact
                     */
                    groupInfoMemberItem.setContactName((String) groupMember.get("memberIdentifier"));


                    image = (String) groupMember.get("memberImage");
                    if (image != null && !image.isEmpty()) {
                        groupInfoMemberItem.setContactImage(image);
                    } else {

                        groupInfoMemberItem.setContactImage("");
                    }
                    groupInfoMemberItem.setContactStatus((String) groupMember.get("memberStatus"));
                    groupInfoMemberItem.setContactIdentifier((String) groupMember.get("memberIdentifier"));
                }


                groupInfoMemberItem.setAdmin((boolean) groupMember.get("memberIsAdmin"));

                groupInfoMemberItem.setStar(groupMember.containsKey("memberIsStar") && (boolean) groupMember.get("memberIsStar"));

                groupInfoMemberItem.setContactUid(memberId);
            }


            mGroupMembersData.add(groupInfoMemberItem);


//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    mAdapter2.notifyItemInserted(mGroupMembersData.size() - 1);
//                }
//            });

        }


        Collections.sort(mGroupMembersData, new SortGroupMembers());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter2.notifyDataSetChanged();
            }
        });

        updateParticipantsCount();


        String creatorId, creatorName;

        creatorId = (String) groupInfo.get("createdByMemberId");


        if (creatorId.equals(AppController.getInstance().getUserId())) {

            creatorName = getString(R.string.You);
        } else {
            creatorName = AppController.getInstance().getDbController().getFriendName(AppController.getInstance().getFriendsDocId(), creatorId);
            if (creatorName == null) {

                creatorName = (String) groupInfo.get("createdByMemberIdentifier");
            }
        }


        setGroupCreatedInfo(creatorName, (long) groupInfo.get("createdAt"));

        if (isCurrentUserNotMember) {

            addParticipants_rl.setVisibility(View.GONE);

            deleteCardView.setVisibility(View.VISIBLE);


            muteCardView.setVisibility(View.GONE);


            editName.setVisibility(View.GONE);
        } else {

            muteCardView.setVisibility(View.VISIBLE);


            muteSwitch.setChecked(AppController.getInstance().getDbController().checkIfReceiverChatMuted(AppController.getInstance().getMutedDocId(), groupId, ""));


        }

    }


    /**
     * To make a member as group admin
     */


    @SuppressWarnings("TryWithIdenticalCatches")

    private void makeMemberGroupAdmin(final String memberId, final String memberIdentifier, final String memberName, String text) {


        final ProgressDialog pDialog = new ProgressDialog(GroupInfo.this, 0);


        pDialog.setCancelable(false);


        pDialog.setMessage(text);
        pDialog.show();

        ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


        bar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(GroupInfo.this, R.color.color_black),
                PorterDuff.Mode.SRC_IN);


        JSONObject obj = new JSONObject();


        try {


            obj.put("type", "admin");


            obj.put("chatId", groupId);


            obj.put("memberId", memberId);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.PUT,
                ApiOnServer.GROUP_MEMBER, obj, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {

                    /*
                     * Progress dialog has been put in 3 places intentionally
                     */

                    switch (response.getInt("code")) {


                        case 200: {
                            /*
                             * Member made admin successfully
                             */

                            informAllMembersOfNewAdmin(memberId, memberIdentifier, memberName);
                            break;

                        }


                        default: {

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
                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, response.getString("message"), Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }
                        }

                    }

//  hideProgressDialog();
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
                } catch (JSONException e) {
                    e.printStackTrace();


                }


            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                if (error.networkResponse!=null && error.networkResponse.statusCode == 406) {
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
                                            makeMemberGroupAdmin(memberId, memberIdentifier, memberName, text);
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
                if (pDialog.isShowing()) {

                    //  pDialog.dismiss();
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

                    Snackbar snackbar = Snackbar.make(root, getString(R.string.No_Internet_Connection_Available), Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("authorization", AppController.getInstance().getApiToken());
                headers.put("lang", Constants.LANGUAGE);


                // headers.put("X-HTTP-Method-Override", "PATCH");


                return headers;
            }
        };

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "makeGroupAdminApi");


    }


    /**
     * To remove a member from the group
     */


    @SuppressWarnings("TryWithIdenticalCatches")
    private void removeMemberFromGroup(final String memberId, final String memberIdentifier, final String memberName, String text) {


        final ProgressDialog pDialog = new ProgressDialog(GroupInfo.this, 0);


        pDialog.setCancelable(false);


        pDialog.setMessage(text);
        pDialog.show();

        ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


        bar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(GroupInfo.this, R.color.color_black),
                PorterDuff.Mode.SRC_IN);

        JSONObject object = new JSONObject();
        try {
            object.put("chatId", groupId);
            object.put("memberId", memberId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                ApiOnServer.GROUP_BY_MEMBER+"?chatId="+groupId+"&memberId="+memberId, object, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {

                    /*
                     * Progress dialog has been put in 3 places intentionally
                     */

                    switch (response.getInt("code")) {


                        case 200: {
                            /*
                             * Member removed successfully
                             */

                            informAllMembersOfMemberRemoved(memberId, memberIdentifier, memberName);
                            break;

                        }


                        default: {

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
                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, response.getString("message"), Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }
                        }

                    }

//  hideProgressDialog();
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
                } catch (JSONException e) {
                    e.printStackTrace();


                }


            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse!=null && error.networkResponse.statusCode == 406) {
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
                                            removeMemberFromGroup(memberId, memberIdentifier, memberName, text);
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

                if (pDialog.isShowing()) {

                    //  pDialog.dismiss();
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

                    Snackbar snackbar = Snackbar.make(root, getString(R.string.No_Internet_Connection_Available), Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, "removeGroupMemberApi");


    }


    /*
     * To publish to all members of new admin been made and update in local database as well
     */

    private void informAllMembersOfNewAdmin(String memberIdMadeAdmin, String memberIdentifier, String memberName) {

        /*
         * Intentionally fetching list of members from db again
         */
        String userId = AppController.getInstance().getUserId();
        String tsInGmt = Utilities.tsInGmt();

        String messageId = String.valueOf(Utilities.getGmtEpoch());
        JSONObject obj = new JSONObject();
        try {
            obj.put("initiatorId", userId);

            obj.put("initiatorIdentifier", AppController.getInstance().getUserIdentifier());
            obj.put("type", 3);
            obj.put("groupId", groupId);

            obj.put("memberId", memberIdMadeAdmin);
            obj.put("memberIdentifier", memberIdentifier);


            obj.put("id", messageId);
            obj.put("timestamp", tsInGmt);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<>();

        map.put("messageType", "98");


        map.put("isSelf", true);
        map.put("from", groupId);
        map.put("Ts", tsInGmt);
        map.put("id", messageId);

        map.put("type", 3);
        map.put("deliveryStatus", "0");


        map.put("memberId", memberIdMadeAdmin);
        map.put("memberIdentifier", memberIdentifier);

        map.put("initiatorId", userId);
        map.put("initiatorIdentifier", AppController.getInstance().getUserIdentifier());

        AppController.getInstance().getDbController().addNewChatMessageAndSort(docId, map, tsInGmt, "");

        ArrayList<Map<String, Object>> groupMembers = AppController.getInstance().getDbController().fetchGroupMember(groupMembersDocId);

        if (AppController.getInstance().canPublish()) {
            AppController.getInstance().getDbController().makeGroupAdmin(groupMembersDocId, memberIdMadeAdmin);
            for (int i = 0; i < groupMembers.size(); i++) {


                String memberId = (String) groupMembers.get(i).get("memberId");


                if (memberId.equals(userId)) {


                    try {
                        JSONObject obj2 = new JSONObject(obj.toString());
                        obj2.put("self", true);

                        obj2.put("timestamp", tsInGmt);

                        obj2.put("id", messageId);
                        obj2.put("message", getString(R.string.You) + " " + getString(R.string.Made) + " " + memberName + " " + getString(R.string.MakeAdmin));

                        obj2.put("eventName", MqttEvents.GroupChats.value + "/" + userId);


                        obj2.put("type", 3);


                        bus.post(obj2);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    AppController.getInstance().publish(MqttEvents.GroupChats.value + "/" + memberId,
                            obj, 1, false);
                }


            }



            /*
             * To update the list of members in ui
             */

            makeMemberAdminInGroupUI(memberIdMadeAdmin);

        } else {
            if (root != null) {


                Snackbar snackbar = Snackbar.make(root, R.string.No_Internet_Connection_Available, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        }
//        AppController.getInstance().getDbController().makeGroupAdmin(groupMembersDocId, memberId);

    }





    /*
     * To publish to all members of new admin been made and update in local database as well
     */

    private void informAllMembersOfMemberRemoved(String memberIdRemoved, String memberIdentifier, String memberName) {


        /*
         * Intentionally fetching list of members from db again
         */

        String userId = AppController.getInstance().getUserId();
        String tsInGmt = Utilities.tsInGmt();
        String messageId = String.valueOf(Utilities.getGmtEpoch());
        JSONObject obj = new JSONObject();
        try {
            obj.put("initiatorId", userId);

            obj.put("initiatorIdentifier", AppController.getInstance().getUserIdentifier());
            obj.put("type", 2);
            obj.put("groupId", groupId);

            obj.put("memberId", memberIdRemoved);
            obj.put("memberIdentifier", memberIdentifier);

            obj.put("timestamp", tsInGmt);
            obj.put("id", messageId);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        Map<String, Object> map = new HashMap<>();

        map.put("messageType", "98");


        map.put("isSelf", true);
        map.put("from", groupId);
        map.put("Ts", tsInGmt);
        map.put("id", messageId);

        map.put("type", 2);
        map.put("deliveryStatus", "0");


        map.put("memberId", memberIdRemoved);
        map.put("memberIdentifier", memberIdentifier);

        map.put("initiatorId", userId);
        map.put("initiatorIdentifier", AppController.getInstance().getUserIdentifier());

        AppController.getInstance().getDbController().addNewChatMessageAndSort(docId, map, tsInGmt, "");

        ArrayList<Map<String, Object>> groupMembers = AppController.getInstance().getDbController().fetchGroupMember(groupMembersDocId);
        if (AppController.getInstance().canPublish()) {

            AppController.getInstance().getDbController().removeGroupMember(groupMembersDocId, memberIdRemoved);
            for (int i = 0; i < groupMembers.size(); i++) {


                String memberId = (String) groupMembers.get(i).get("memberId");

                if (memberId.equals(userId)) {


                    try {


                        JSONObject obj2 = new JSONObject(obj.toString());

                        obj2.put("self", true);

                        obj2.put("timestamp", tsInGmt);

                        obj2.put("id", messageId);

                        obj2.put("message", getString(R.string.You) + " " + getString(R.string.Removed) + " " + memberName
                                + " " + getString(R.string.FromGroup));

                        obj2.put("eventName", MqttEvents.GroupChats.value + "/" + userId);
                        obj2.put("type", 2);
                        bus.post(obj2);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    AppController.getInstance().publish(MqttEvents.GroupChats.value + "/" + memberId,
                            obj, 1, false);
                }


            }



            /*
             * To update the list of members in ui
             */

            removeMemberFromGroupUI(memberIdRemoved);


        } else {
            if (root != null) {


                Snackbar snackbar = Snackbar.make(root, R.string.No_Internet_Connection_Available, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        }


    }

    private void removeMemberFromGroupUI(String memberId) {


        for (int i = 0; i < mGroupMembersData.size(); i++) {


            if (mGroupMembersData.get(i).getContactUid().equals(memberId)) {
                mGroupMembersData.remove(i);
                if (i == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            mAdapter2.notifyDataSetChanged();
                        }
                    });
                } else {

                    final int k = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            mAdapter2.notifyItemRemoved(k);

                            recyclerViewMembers.requestLayout();
                        }
                    });
                }

                break;

            }
        }

        if (memberId.equals(AppController.getInstance().getUserId())) {
            /*
             * If user is no longer the member
             */
            isAdmin = false;


            addParticipants_rl.setVisibility(View.GONE);


            updateExitGroupVisibility(false);
        }
        updateParticipantsCount();

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//
//
//                recyclerViewMembers.requestLayout();
//            }
//        });
    }


    private void makeMemberAdminInGroupUI(String memberId) {
        for (int i = 0; i < mGroupMembersData.size(); i++) {


            if (mGroupMembersData.get(i).getContactUid().equals(memberId)) {


                GroupInfoMemberItem memberItem = mGroupMembersData.get(i);

                memberItem.setAdmin(true);
                mGroupMembersData.set(i, memberItem);

                final int k = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        mAdapter2.notifyItemChanged(k);
                    }
                });


                break;

            }
        }


        if (memberId.equals(AppController.getInstance().getUserId())) {
            /*
             * If user himself the new admin
             */
            isAdmin = true;


            addParticipants_rl.setVisibility(View.VISIBLE);


        }

    }


    private void updateGroupIcon(String groupIcon) {
        groupImageUrl = groupIcon;
        Glide.with(GroupInfo.this)
                .load(groupIcon)
                .crossFade()

                .centerCrop()


                .placeholder(R.drawable.avatar_group_large).

                into(groupImage);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        bus.unregister(this);
    }


    private void updateGroupSubject(String groupSubject) {


        collapsingToolbarLayout.setTitle(groupSubject);

        groupName.setText(groupSubject);
    }

    /**
     * To change font of the title in the toolbar
     */
    public void applyFontForToolbarTitle(Activity context) {
        Toolbar toolbar = (Toolbar) context.findViewById(R.id.toolbar);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView tv = (TextView) view;
                Typeface titleFont = AppController.getInstance().getRegularFont();
                if (tv.getText().equals(context.getTitle())) {
                    tv.setTypeface(titleFont, Typeface.BOLD);
                    break;
                }
            }
        }
    }


    private void setGroupCreatedInfo(String memberName, long timestamp) {
        String createdInfo = getString(R.string.CreatedBy, memberName) + ", ";


        String lastSeen = Utilities.epochtoGmt(String.valueOf(timestamp));


        lastSeen = Utilities.changeStatusDateFromGMTToLocal(lastSeen);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);

        Date date2 = new Date(System.currentTimeMillis() - AppController.getInstance().getTimeDelta());
        String current_date = sdf.format(date2);

        current_date = current_date.substring(0, 8);


        if (lastSeen != null) {


            if (current_date.equals(lastSeen.substring(0, 8))) {

                lastSeen = Utilities.convert24to12hourformat(lastSeen.substring(8, 10) + ":" + lastSeen.substring(10, 12));


                createdInfo = createdInfo + "Today at " + lastSeen;

                lastSeen = null;

            } else {

                String last = Utilities.convert24to12hourformat(lastSeen.substring(8, 10) + ":" + lastSeen.substring(10, 12));


                String date = lastSeen.substring(6, 8) + "-" + lastSeen.substring(4, 6) + "-" + lastSeen.substring(0, 4);

                createdInfo = createdInfo + date + " at " + last;


                last = null;
                date = null;

            }


            if (groupCreatedInfo != null) {

                groupCreatedInfo.setText(createdInfo);
            }


        }


    }


    private void updateParticipantsCount() {
        participantsCount.setText(mGroupMembersData.size() + getString(R.string.space) + getString(R.string.Participants));
    }


    private void updateExitGroupVisibility(boolean visible) {


        if (visible) {


            exitCardView.setVisibility(View.VISIBLE);
            deleteCardView.setVisibility(View.GONE);

            muteCardView.setVisibility(View.VISIBLE);
            editName.setVisibility(View.VISIBLE);

        } else {

            exitCardView.setVisibility(View.GONE);


            deleteCardView.setVisibility(View.VISIBLE);

            muteCardView.setVisibility(View.GONE);
            editName.setVisibility(View.GONE);

        }


    }


    @SuppressWarnings("TryWithIdenticalCatches")

    private void exitGroup() {

        final ProgressDialog pDialog = new ProgressDialog(GroupInfo.this, 0);


        pDialog.setCancelable(false);


        pDialog.setMessage(getString(R.string.Leaving, groupName.getText().toString().trim()));
        pDialog.show();

        ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


        bar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(GroupInfo.this, R.color.color_black),
                PorterDuff.Mode.SRC_IN);

        JSONObject object = new JSONObject();
        try {
            object.put("chatId", groupId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                ApiOnServer.GROUP_MEMBER+"?chatId="+groupId, object, new com.android.volley.Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {

                    /*
                     * Progress dialog has been put in 3 places intentionally
                     */

                    switch (response.getInt("code")) {


                        case 200: {
                            /*
                             * Member left group successfully
                             */

                            informAllMembersOfGroupExit();
                            break;

                        }


                        case 403: {
                            /*
                             * Can't leave the group
                             */

                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, getString(R.string.Leave), Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }

                            break;
                        }


                        default: {

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
                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, response.getString("message"), Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }
                        }

                    }

//  hideProgressDialog();
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
                } catch (JSONException e) {
                    e.printStackTrace();


                }


            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                error.printStackTrace();
                if (error.networkResponse!=null && error.networkResponse.statusCode == 406) {
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
                                            exitGroup();
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
                if (pDialog.isShowing()) {

                    //  pDialog.dismiss();
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

                    Snackbar snackbar = Snackbar.make(root, getString(R.string.No_Internet_Connection_Available), Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, "exitGroupApi");


    }


    private void informAllMembersOfGroupExit() {


        String userId = AppController.getInstance().getUserId();


        String userIdentifier = AppController.getInstance().getUserIdentifier();


        String tsInGmt = Utilities.tsInGmt();

        String messageId = String.valueOf(Utilities.getGmtEpoch());


        JSONObject obj = new JSONObject();

        try {
            obj.put("initiatorId", userId);

            obj.put("initiatorIdentifier", userIdentifier);
            obj.put("type", 6);
            obj.put("groupId", groupId);
            obj.put("id", messageId);
            obj.put("timestamp", tsInGmt);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Map<String, Object> map = new HashMap<>();

        map.put("messageType", "98");


        map.put("isSelf", true);
        map.put("from", groupId);
        map.put("Ts", tsInGmt);
        map.put("id", messageId);

        map.put("type", 6);
        map.put("deliveryStatus", "0");


        map.put("initiatorId", userId);
        map.put("initiatorIdentifier", AppController.getInstance().getUserIdentifier());

        AppController.getInstance().getDbController().addNewChatMessageAndSort(docId, map, tsInGmt, "");

        ArrayList<Map<String, Object>> groupMembers = AppController.getInstance().getDbController().fetchGroupMember(groupMembersDocId);

        if (AppController.getInstance().canPublish()) {
            AppController.getInstance().getDbController().removeGroupMember(groupMembersDocId, userId);

            for (int i = 0; i < groupMembers.size(); i++) {


                String memberId = (String) groupMembers.get(i).get("memberId");


                if (memberId.equals(userId)) {

                    try {
                        JSONObject obj2 = new JSONObject(obj.toString());
                        obj2.put("self", true);
                        obj2.put("timestamp", tsInGmt);


                        obj2.put("id", messageId);
                        obj2.put("message", getString(R.string.LeftGroup, getString(R.string.You)));

                        obj2.put("eventName", MqttEvents.GroupChats.value + "/" + userId);
                        bus.post(obj2);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    AppController.getInstance().publish(MqttEvents.GroupChats.value + "/" + memberId,
                            obj, 1, false);
                }


            }

            removeMemberFromGroupUI(userId);


        } else {


            if (root != null) {


                Snackbar snackbar = Snackbar.make(root, R.string.No_Internet_Connection_Available, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }

    }


    @SuppressWarnings("TryWithIdenticalCatches")
    private void deleteGroup() {

        /*
         * If has come on the delete group screen from the chat messages screen then will have to close that screen
         */
        final ProgressDialog pDialog = new ProgressDialog(GroupInfo.this, 0);


        pDialog.setCancelable(false);


        pDialog.setMessage(getString(R.string.Deleting, groupName.getText().toString().trim()));
        pDialog.show();

        ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


        bar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(GroupInfo.this, R.color.color_black),
                PorterDuff.Mode.SRC_IN);

        JSONObject object = new JSONObject();
        try {
            object.put("chatId", groupId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.DELETE,
                ApiOnServer.DELETE_GROUP + "?chatId=" + groupId, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
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
                try {
                    if (response.getInt("code") == 200) {


                        AppController.getInstance().getDbController().deleteParticularChatDetail(AppController.getInstance().getChatDocId(),
                                docId, true, groupMembersDocId);


                        JSONObject obj = new JSONObject();


                        try {
                            obj.put("eventName", "conversationDeleted");

                            obj.put("groupId", groupId);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bus.post(obj);
                        supportFinishAfterTransition();
                    } else {


                        if (root != null) {

                            Snackbar snackbar = Snackbar.make(root, response.getString("message"), Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.networkResponse!=null && error.networkResponse.statusCode == 406) {
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
                                            deleteGroup();
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
                if (root != null) {


                    Snackbar snackbar = Snackbar.make(root, R.string.No_Internet_Connection_Available, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
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


        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "deleteGroupApiRequest");
    }


    @SuppressWarnings("TryWithIdenticalCatches")
    private void fetchGroupMemberDetailsViaApi(String name) {


        final ProgressDialog pDialog = new ProgressDialog(GroupInfo.this, 0);


        pDialog.setCancelable(false);


        pDialog.setMessage(getString(R.string.FetchMembers, name));


        pDialog.show();

        ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


        bar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(GroupInfo.this, R.color.color_black),
                android.graphics.PorterDuff.Mode.SRC_IN);
        JSONObject object = new JSONObject();
        try {
            object.put("chatId", groupId);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                ApiOnServer.GROUP_MEMBER+"?chatId="+ groupId, object,
                new com.android.volley.Response.Listener<JSONObject>() {


                    @Override
                    public void onResponse(JSONObject response) {


                        try {


                            if (response.getInt("code") == 200) {

                                //     Log.d("log1", "group members------>" + response.toString());

                                updateGroupMembersFromApi(response.getJSONObject("data"));

                            } else {


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
                if (error.networkResponse!=null && error.networkResponse.statusCode == 406) {
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
                                            fetchGroupMemberDetailsViaApi(name);
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


                    Snackbar snackbar = Snackbar.make(root, R.string.FetchDetails, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBackPressed();
                    }
                }, 500);
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
        AppController.getInstance().addToRequestQueue(jsonObjReq, "fetchMembersApiRequest");
    }


    /**
     * @param groupData JSONObject containing the group  data which included member details and created by whom and created at details
     */
    @SuppressWarnings("unchecked")
    private void updateGroupMembersFromApi(JSONObject groupData) {
        try {
            JSONArray members = groupData.getJSONArray("members");
            JSONObject member;


            Map<String, Object> memberMap;


            boolean isActive = false;
            String userId = AppController.getInstance().getUserId();
            ArrayList<Map<String, Object>> membersArray = new ArrayList<>();


            if (groupMembersDocId == null) {
                groupMembersDocId = AppController.getInstance().getDbController().createGroupMembersDocument();
            }

            for (int i = 0; i < members.length(); i++) {


                member = members.getJSONObject(i);
                memberMap = new HashMap<>();

                memberMap.put("memberId", member.getString("userId"));

                if (userId.equals(member.getString("userId"))) {

                    isActive = true;
                }

                //memberMap.put("memberIdentifier", member.getString("userIdentifier"));
                memberMap.put("memberIdentifier", member.getString("userName"));

                if (member.has("profilePic")) {
                    memberMap.put("memberImage", member.getString("profilePic"));
                } else {

                    memberMap.put("memberImage", "");
                }


                if (member.has("socialStatus")) {
                    memberMap.put("memberStatus", member.getString("socialStatus"));
                } else {

                    memberMap.put("memberStatus", getString(R.string.default_status));
                }

                memberMap.put("memberIsAdmin", member.getBoolean("isAdmin"));

                if (member.has("isStar")) {
                    memberMap.put("memberIsStar", member.getBoolean("isStar"));
                } else {

                    memberMap.put("memberIsStar", false);
                }
                membersArray.add(memberMap);


            }
            AppController.getInstance().getDbController().addGroupMembersDetails(groupMembersDocId, membersArray,
                    groupData.getString("createdByMemberId"),
                    groupData.getString("createdByMemberIdentifier"), groupData.getLong("createdAt"), isActive);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        addGroupMembers();
        addMediaContent();
    }


}