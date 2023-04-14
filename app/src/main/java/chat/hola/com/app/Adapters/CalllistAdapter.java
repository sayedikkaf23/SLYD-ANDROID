package chat.hola.com.app.Adapters;

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
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Database.CouchDbController;
import chat.hola.com.app.ModelClasses.CallItem;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TextDrawable;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.ViewHolders.ViewHolderCall;
import chat.hola.com.app.calling.myapplication.utility.CallStatus;
import chat.hola.com.app.calling.video.call.CallingActivity;
import chat.hola.com.app.home.callhistory.CallsFragment;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by moda on 24/08/17.
 */

public class CalllistAdapter extends RecyclerView.Adapter<ViewHolderCall> implements Filterable {

    public CalllistAdapter.CallClickListener CallClickListener;
    private Context mcontext;
    private int position = 0;
    private String receiverUid;
    private boolean blocked;
    private CallItem callItem;

    private ArrayList<CallItem> mOriginalListData = new ArrayList<>();
    private ArrayList<CallItem> mFilteredListData;

    private CouchDbController db = AppController.getInstance().getDbController();

    private CallsFragment fragment;

    //   private Typeface tf;
    private String str = "";

    private int density;

    private Drawable drawable1, drawable2;

    public CalllistAdapter(Context activity, ArrayList<CallItem> mListData, CallsFragment fragment, CalllistAdapter.CallClickListener listener) {

        this.CallClickListener = listener;
        this.mcontext = activity;
        this.mOriginalListData = mListData;
        this.mFilteredListData = mListData;
        this.fragment = fragment;
        // tf = AppController.getInstance().getMediumFont();
        density = (int) mcontext.getResources().getDisplayMetrics().density;
        drawable1 = ContextCompat.getDrawable(mcontext, R.drawable.call_incoming_call_icon);
        drawable2 = ContextCompat.getDrawable(mcontext, R.drawable.call_outgoing_call_icon);
        str = mcontext.getString(R.string.VideoCall);
    }

    @Override
    public ViewHolderCall onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.call_item_new, parent, false);

        return new ViewHolderCall(itemView);
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    @Override
    public void onBindViewHolder(ViewHolderCall vh, int position) {

        callItem = mFilteredListData.get(position);

        if (callItem != null) {
            this.position = vh.getAdapterPosition();

            //            vh.callTypeTv.setTypeface(tf, Typeface.NORMAL);
            //            vh.name.setTypeface(tf, Typeface.NORMAL);
            //
            //            vh.callTime.setTypeface(tf, Typeface.NORMAL);

            vh.name.setText(callItem.getReceiverName());
            vh.callTypeTv.setText(R.string.dash_space + callItem.getCallType());//todo change string with getString()

            if (callItem.isCallNotAllowed() || callItem.isStar()) {

                vh.callTypeIv.setVisibility(View.GONE);
            } else {
                vh.callTypeIv.setVisibility(View.VISIBLE);

                if (callItem.getIsMissedCall().equals("1")) {
                    vh.callTypeIv.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ic_misscall));
                } else if (callItem.isCallInitiated()) {
                    vh.callTypeIv.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ic_call_out));
                } else {
                    vh.callTypeIv.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ic_call_in));

                }

//                if (callItem.getCallType().equals(str)) {
//                    if(callItem.getIsMissedCall().equals("1")){
//                        vh.callTypeIv.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ic_misscall));
//                    }else{
//                        vh.callTypeIv.setImageDrawable(ContextCompat.getDrawable(mcontext, R.drawable.ic_video));
//                    }
//                } else {
//                    vh.callTypeIv.setImageDrawable(
//                            ContextCompat.getDrawable(mcontext, R.drawable.ic_old_handphone));
//                }
            }
            vh.name.setTextColor(Color.parseColor("#252525"));//todo change colorString

            if (callItem.getReceiverImage() != null && !callItem.getReceiverImage().isEmpty()) {

                try {
                    Glide.with(mcontext).load(callItem.getReceiverImage()).asBitmap()

                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame)
                            //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                            .signature(new StringSignature(
                                    AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .into(new BitmapImageViewTarget(vh.receiverImage) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mcontext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh.receiverImage.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {

                try {
                    vh.receiverImage.setImageDrawable(TextDrawable.builder()

                            .beginConfig()
                            .textColor(Color.WHITE)
                            .useFont(Typeface.DEFAULT)
                            .fontSize(24 * density) /* size in px */
                            .bold()
                            .toUpperCase()
                            .endConfig()

                            .buildRound((callItem.getReceiverName().trim()).charAt(0) + "", Color.parseColor(
                                    AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
                } catch (IndexOutOfBoundsException e) {
                    vh.receiverImage.setImageDrawable(TextDrawable.builder()

                            .beginConfig()
                            .textColor(Color.WHITE)
                            .useFont(Typeface.DEFAULT)
                            .fontSize(24 * density) /* size in px */
                            .bold()
                            .toUpperCase()
                            .endConfig()

                            .buildRound("C", Color.parseColor(
                                    AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
                } catch (NullPointerException e) {
                    vh.receiverImage.setImageDrawable(TextDrawable.builder()

                            .beginConfig()
                            .textColor(Color.WHITE)
                            .useFont(Typeface.DEFAULT)
                            .fontSize(24 * density) /* size in px */
                            .bold()
                            .toUpperCase()
                            .endConfig()

                            .buildRound("C", Color.parseColor(
                                    AppController.getInstance().getColorCode(vh.getAdapterPosition() % 19))));
                }
            }

            if (callItem.getIsMissedCall().equals("1") && callItem.getCallType().equals(str)) {
                if(callItem.isCallInitiated()){
                    vh.callTypeText.setText(R.string.outgoing_);
                    vh.callTypeText.setTextColor(vh.callTypeText.getContext().getResources().getColor(R.color.direct_message));
                } else {
                    vh.callTypeText.setText(R.string.you_miss_video_call);
                    vh.callTypeText.setTextColor(vh.callTypeText.getContext().getResources().getColor(R.color.red));
                }
            } else if (callItem.getIsMissedCall().equals("1") && !callItem.getCallType().equals(str)) {
                if(callItem.isCallInitiated()){
                    vh.callTypeText.setText(R.string.outgoing_);
                    vh.callTypeText.setTextColor(vh.callTypeText.getContext().getResources().getColor(R.color.direct_message));
                } else {
                    vh.callTypeText.setText(R.string.you_miss_voice_call);
                    vh.callTypeText.setTextColor(vh.callTypeText.getContext().getResources().getColor(R.color.red));
                }
            } else if (callItem.isCallInitiated()) {
                vh.callTypeText.setText(R.string.outgoing_);
                vh.callTypeText.setTextColor(vh.callTypeText.getContext().getResources().getColor(R.color.direct_message));
            } else {
                vh.callTypeText.setText(R.string.incoming_);
                vh.callTypeText.setTextColor(vh.callTypeText.getContext().getResources().getColor(R.color.direct_message));
            }

//                if (callItem.isCallInitiated()) {
//                vh.callTypeText.setText(R.string.outgoing_);
//
//                vh.callDialledIv.setImageDrawable(drawable1);
//            } else {
//                vh.callTypeText.setText(R.string.incoming_);
//
//                vh.callDialledIv.setImageDrawable(drawable2);
//            }


            String[] myDate =
                    Utilities.tsFromGmtToLocalTimeZone(callItem.getCallInitiateTime()).split(" ");

            String last = convert24to12hourformat(myDate[1]);

            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            try {
                Date date = format.parse(myDate[0]);

                boolean isSameWeek = isDateInCurrentWeek(date);

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(date);

                String[] days = mcontext.getResources().getStringArray(R.array.days);
                String day = "";
                if (calendar.get(Calendar.DAY_OF_WEEK) <= 6) {

                    day = days[calendar.get(Calendar.DAY_OF_WEEK) - 1];
                } else {
                    day = days[0];
                }

                if (isSameWeek) {
//          vh.callTime.setText(day + R.string.comma + mcontext.getString(R.string.space) + last);//todo not able to change getString
                    SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                    Date d = new Date();
                    String dayOfTheWeek = sdf.format(d);

                    if (dayOfTheWeek.equalsIgnoreCase(day)) {
                        day = "today";
                    }

                    Calendar cal = Calendar.getInstance();
                    cal.add(Calendar.DATE, -1);
                    String yesterday = sdf.format(cal.getTime());
                    if (yesterday.equalsIgnoreCase(day)) {
                        day = "yesterday";
                    }

                    vh.callTime.setText(day.toLowerCase());
                } else {
//          vh.callTime.setText(myDate[0] + mcontext.getString(R.string.space) + last);//todo not able to change getString
                    vh.callTime.setText(myDate[0]);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            vh.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    CallClickListener.onItemClick(callItem,vh.callTime.getText().toString(), position);
                }
            });

            vh.callTypeIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.System.canWrite(mcontext) || !Settings.canDrawOverlays(mcontext)) {
                            //                            if (!Settings.System.canWrite(mcontext)) {
                            //                                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                            //                                intent.setData(Uri.parse("package:" + mcontext.getPackageName()));
                            //                                mcontext.startActivity(intent);
                            //                            }

                            //If the draw over permission is not available open the settings screen
                            //to grant the permission.

                            if (!Settings.canDrawOverlays(mcontext)) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                        Uri.parse("package:" + mcontext.getPackageName()));
                                mcontext.startActivity(intent);
                            }
                        } else {

                            showCallTypeChooserPopup(callItem);
                        }
                    } else {
                        showCallTypeChooserPopup(callItem);
                    }
                }
            });

            vh.img_audio_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Log.e("Click==>","==>> Audio");
                    receiverUid = callItem.getReceiverUid();
                    String audio = "Audio";
                    checkIfUserIsBlocked(audio,position,vh);
                }
            });

            vh.img_video_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Log.e("Click==>","==>> Video");
                    receiverUid = callItem.getReceiverUid();
                    String video = "Video";
                    checkIfUserIsBlocked(video,position,vh);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mFilteredListData.size();
    }

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

    private static boolean isDateInCurrentWeek(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        int week = currentCalendar.get(Calendar.WEEK_OF_YEAR);
        int year = currentCalendar.get(Calendar.YEAR);
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(date);
        int targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR);
        int targetYear = targetCalendar.get(Calendar.YEAR);
        return week == targetWeek && year == targetYear;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mFilteredListData = (ArrayList<CallItem>) results.values;
                CalllistAdapter.this.notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                ArrayList<CallItem> filteredResults = null;
                if (constraint.length() == 0) {
                    filteredResults = mOriginalListData;
                } else {
                    filteredResults = getFilteredResults(constraint.toString().toLowerCase());
                }

                mFilteredListData = filteredResults;

                fragment.showNoSearchResults(constraint,
                        mFilteredListData.size() == mOriginalListData.size());

                FilterResults results = new FilterResults();
                results.values = filteredResults;

                return results;
            }
        };
    }

    private ArrayList<CallItem> getFilteredResults(String constraint) {
        ArrayList<CallItem> results = new ArrayList<>();

        for (CallItem item : mOriginalListData) {
            if (item.getReceiverName().toLowerCase().contains(constraint)) {
                results.add(item);
            }
        }
        return results;
    }

    public List<CallItem> getList() {

        return mFilteredListData;
    }

    public void requestAudioCall(CallItem contactsCallItem) {

        if (contactsCallItem == null) {

            contactsCallItem = mFilteredListData.get(position);
        }

        Map<String, Object> callItem = new HashMap<>();

        String callId = AppController.getInstance().randomString();
        callItem.put("receiverName", contactsCallItem.getReceiverName());
        callItem.put("receiverImage", contactsCallItem.getReceiverImage());
        callItem.put("receiverUid", contactsCallItem.getReceiverUid());
        callItem.put("receiverIdentifier", contactsCallItem.getReceiverIdentifier());
        callItem.put("callTime", Utilities.tsInGmt());
        callItem.put("callInitiated", true);
        callItem.put("callId", callId);
        callItem.put("callType", mcontext.getResources().getString(R.string.AudioCall));

        callItem.put("isStar", contactsCallItem.isStar());

        db.addNewCall(AppController.getInstance().getCallsDocId(), callItem);
//    Common.callerName = contactsCallItem.getReceiverName();
//
//    CallingApis.initiateCall(mcontext, contactsCallItem.getReceiverUid(),
//        contactsCallItem.getReceiverName(), contactsCallItem.getReceiverImage(), "0",
//        contactsCallItem.getReceiverIdentifier(), callId, contactsCallItem.isStar());
    }

    public void requestVideoCall(CallItem contactsCallItem) {

        if (contactsCallItem == null) {

            contactsCallItem = mFilteredListData.get(position);
        }

        Map<String, Object> callItem = new HashMap<>();

        String callId = AppController.getInstance().randomString();

        callItem.put("receiverName", contactsCallItem.getReceiverName());
        callItem.put("receiverImage", contactsCallItem.getReceiverImage());
        callItem.put("receiverUid", contactsCallItem.getReceiverUid());
        callItem.put("receiverIdentifier", contactsCallItem.getReceiverIdentifier());
        callItem.put("callTime", Utilities.tsInGmt());
        callItem.put("callInitiated", true);
        callItem.put("callId", callId);
        callItem.put("callType", mcontext.getResources().getString(R.string.VideoCall));

        callItem.put("isStar", contactsCallItem.isStar());

        db.addNewCall(AppController.getInstance().getCallsDocId(), callItem);
//    Common.callerName = contactsCallItem.getReceiverName();
//    CallingApis.initiateCall(mcontext, contactsCallItem.getReceiverUid(),
//        contactsCallItem.getReceiverName(), contactsCallItem.getReceiverImage(), "1",
//        contactsCallItem.getReceiverIdentifier(), callId, contactsCallItem.isStar());
    }

    private void showCallTypeChooserPopup(final CallItem callItem) {

        if (!AppController.getInstance().isActiveOnACall()) {
            final AlertDialog.Builder builder;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder =
                        new AlertDialog.Builder(mcontext, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(mcontext);
            }

            builder.setTitle(mcontext.getResources().getString(R.string.Start_Call));

            builder.setMessage(mcontext.getResources().getString(R.string.Start_Audio_Video_call));
            builder.setPositiveButton(mcontext.getResources().getString(R.string.AudioCall),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.RECORD_AUDIO)
                                    != PackageManager.PERMISSION_GRANTED) {

                                fragment.requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 71);
                            } else {

                                requestAudioCall(callItem);
                            }
                        }
                    });
            builder.setNegativeButton(mcontext.getResources().getString(R.string.VideoCall),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            ArrayList<String> arr1 = new ArrayList<>();

                            if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {

                                arr1.add(Manifest.permission.CAMERA);
                            }

                            if (ActivityCompat.checkSelfPermission(mcontext, Manifest.permission.RECORD_AUDIO)
                                    != PackageManager.PERMISSION_GRANTED) {

                                arr1.add(Manifest.permission.RECORD_AUDIO);
                            }
                            if (arr1.size() > 0) {

                                fragment.requestPermissions(arr1.toArray(new String[arr1.size()]), 72);
                            } else {
                                requestVideoCall(callItem);
                            }
                        }
                    });

            if (AppController.getInstance().canPublish()) {
                ((Activity) mcontext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        builder.show();
                    }
                });
            } else {
                ((Activity) mcontext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mcontext,
                                mcontext.getResources().getString(R.string.No_Internet_Connection_Available),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        } else {

            ((Activity) mcontext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mcontext, mcontext.getResources().getString(R.string.call_initiate),
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void sortBy(String type) {

        if (type.equalsIgnoreCase("Missed")) {

//            ArrayList<CallItem> tempList = new ArrayList<>();
//            ArrayList<CallItem> tempList2 = mOriginalListData;
//            Log.e("size==>", "==>" + mOriginalListData.size());
//
//
//            for (int i = 0; i < tempList2.size(); i++) {
//                Log.e("data inn==>", "==>" + tempList2.get(i).isMissedCall());
//
//                if (tempList2.get(i).isMissedCall()) {
//                    tempList.add(tempList2.get(i));
//                }
//            }
//
//            Log.e("FilterData==>", "==>" + tempList.size());
//            mFilteredListData = tempList;

//            Collections.sort(mOriginalListData, new Comparator<CallItem>() {
//                @Override
//                public int compare(CallItem callItem, CallItem t1) {
//                    int check = -1;
//                    if (callItem.isMissedCall()) check = 1;
//                    else check = -1;
//                    return check;
//                }
//            });
            CalllistAdapter.this.notifyDataSetChanged();

        } else {
//            Collections.sort(mOriginalListData, new Comparator<CallItem>() {
//                @Override
//                public int compare(CallItem callItem, CallItem t1) {
//                    return 0;
//                }
//            });

            mFilteredListData = mOriginalListData;

            CalllistAdapter.this.notifyDataSetChanged();


        }

    }

    public void updateData(ArrayList<CallItem> list) {
        mFilteredListData = list;
        CalllistAdapter.this.notifyDataSetChanged();
    }

    public void addData(ArrayList<CallItem> list) {
        mOriginalListData.addAll(list);
        CalllistAdapter.this.notifyDataSetChanged();
    }

    public interface CallClickListener {
        void onItemClick(CallItem item, String time, int position);
    }

    /*
     * Will check if the user is blocked,first in database and then from the api.
     */

    private void checkIfUserIsBlocked(String callType, int position,ViewHolderCall vh) {
        callItem = mFilteredListData.get(position);

        JSONObject obj = new JSONObject();
        try {
            obj.put("opponentId", receiverUid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                ApiOnServer.BLOCK_USER + "?opponentId=" + receiverUid, null,
                new com.android.volley.Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            switch (response.getInt("code")) {
                                case 200: {

                                    response = response.getJSONObject("data");
                                    if (response.getBoolean("blocked")) {
                                        Toast.makeText(mcontext.getApplicationContext(),
                                                "You cant make a call or message until unBlock",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        if(callType.equals("Audio")){
                                            if (!AppController.getInstance().isActiveOnACall() ||
                                                    AppController.getInstance().canPublish()) {
                                                AppController.getInstance().setActiveOnACall(true,
                                                        true);
                                                Intent intent = new Intent(mcontext, CallingActivity.class);
                                                intent.putExtra(USER_NAME, callItem.getReceiverName());
                                                intent.putExtra(USER_IMAGE, callItem.getReceiverName());
                                                intent.putExtra(USER_ID, callItem.getReceiverUid());
                                                intent.putExtra(CALL_STATUS, CallStatus.CALLING);
                                                intent.putExtra(CALL_TYPE, AUDIO);
                                                intent.putExtra(CALL_ID, "");
                                                intent.putExtra(ROOM_ID, "0");
                                                vh.img_audio_call.getContext().startActivity(intent);
                                            } else {
                                                Toast.makeText(mcontext,
                                                        mcontext.getString(R.string.calling_service_not_available),
                                                        Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            if (!AppController.getInstance().isActiveOnACall() || AppController.getInstance().canPublish()) {
                                                AppController.getInstance().setActiveOnACall(true, true);
                                                Intent intent = new Intent(mcontext, CallingActivity.class);
                                                intent.putExtra(USER_NAME, callItem.getReceiverName());
                                                intent.putExtra(USER_IMAGE, callItem.getReceiverName());
                                                intent.putExtra(USER_ID, callItem.getReceiverUid());
                                                intent.putExtra(CALL_STATUS, CallStatus.CALLING);
                                                intent.putExtra(CALL_TYPE, VIDEO);
                                                intent.putExtra(CALL_ID, "");
                                                intent.putExtra(ROOM_ID, "0");
                                                vh.img_video_call.getContext().startActivity(intent);
                                            } else {
                                                Toast.makeText(mcontext, mcontext.getString(R.string.calling_service_not_available), Toast.LENGTH_LONG).show();
                                            }
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
//                                                    new ChatMessageScreen.checkIfUserIsBlocked().execute(params);
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
//                            sessionApiCall.getNewSession(sessionObserver);
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

        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(20 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        /* Add the request to the RequestQueue.*/
        AppController.getInstance().addToRequestQueue(jsonObjReq, "checkBlockedApi");

    }
}
