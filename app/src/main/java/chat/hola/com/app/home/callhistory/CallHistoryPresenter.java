package chat.hola.com.app.home.callhistory;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.ezcall.android.R;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import chat.hola.com.app.AppController;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.ModelClasses.CallItem;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Networking.UnsafeOkHttpClient;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.ImageFilePath;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.calling.model.PendingCallsResponse;
import chat.hola.com.app.cameraActivities.SandriosCamera;
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;
import chat.hola.com.app.home.callhistory.model.CallHistory;
import chat.hola.com.app.home.stories.StoriesContract;
import chat.hola.com.app.home.stories.StoriesFrag;
import chat.hola.com.app.home.stories.model.CallHistoryResponse;
import chat.hola.com.app.home.stories.model.ClickListner;
import chat.hola.com.app.home.stories.model.StoryData;
import chat.hola.com.app.home.stories.model.StoryModel;
import chat.hola.com.app.home.stories.model.StoryObserver;
import chat.hola.com.app.home.stories.model.StoryPost;
import chat.hola.com.app.home.stories.model.StoryResponse;
import chat.hola.com.app.models.NetworkConnector;
import chat.hola.com.app.models.SessionObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * <h1>CallHistoryPresenter</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 9/07/2021
 */

public class CallHistoryPresenter
        implements CallHistoryContract.Presenter, ClickListner {


    @Inject
    HowdooService service;

    @Inject
    NetworkConnector networkConnector;
    private SessionApiCall sessionApiCall = new SessionApiCall();
    @Nullable
    private CallHistoryContract.View view;
    private String picturePath;
    private Context context;
    public static int page = 0;
        static final int PAGE_SIZE = Constants.PAGE_SIZE;
    private boolean isLoading = false;
    private boolean isLastPage = false;


    @Inject
    public CallHistoryPresenter(Context context) {
        this.context = context;
    }


    @Override
    public void onItemClick(int position) {

    }


    @Override
    public void attachView(CallHistoryContract.View view) {
        this.view = view;

    }

    @Override
    public void detachView() {
        this.view = null;
    }


//  @Override
//  public void storyObserver() {
//    storyObserver.subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new DisposableObserver<StoryPost>() {
//          @Override
//          public void onNext(StoryPost data) {
//
//            if (data.isSuccess()) {
//              myStories();
//              //                            model.addStory(data);
//              //                            assert view != null;
//              //                            view.myStories(data.getUrlPath(), data.getTimestamp());
//            }
//          }
//
//          @Override
//          public void onError(Throwable e) {
//
//          }
//
//          @Override
//          public void onComplete() {
//          }
//        });
//  }
//
//  @Override
//  public StoryData getStoryData(int position) {
//    return model.getStoryData(position);
//  }
//
//  @Override
//  public List<StoryPost> getStoryPosts() {
//    return model.getStoryPosts();
//  }
//
//  @Override
//  public List<StoryPost> getStoryPosts(int position) {
//    return model.getStoryPosts(position);
//  }
//
//  @Override
//  public List<StoryData> getAllStoryData() {
//    return model.getAllStoryData();
//  }


    @Override
    public void init() {
        if (view != null) view.setupRecyclerView();
    }

    @Override
    public void loadData(int skip, int limit) {
        isLoading = true;
        if (view != null) {
            view.isLoading(true);
        }
        if (skip == 0) {
            isLastPage = false;
            page = 0;
        }


        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient(
                AppController.getInstance().getApplicationContext());
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiOnServer.CALLING_BASE)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        retrofit.create(HowdooService.class)
                .getUserCallHistory(AppController.getInstance().getApiToken(), Constants.LANGUAGE,AppController.getInstance().getUserId(), skip, limit)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<CallHistoryResponse>>() {

                    //      service.getUserCallHistory(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
//              .subscribeOn(Schedulers.io())
//              .observeOn(AndroidSchedulers.mainThread())
//              .subscribe(new DisposableObserver<Response<CallHistoryResponse>>() {
                    @Override
                    public void onNext(Response<CallHistoryResponse> response) {
//                        Log.e("ResponseCallHistory", "==>Data " + response.body().getData().size());

                        isLoading = false;
                        try {
                            if (response.code() == 200) {

                                if (response.body() != null) {
                                    isLastPage = response.body().getData().size() < PAGE_SIZE;
                                    if (response.body().getData().size() > 0) {
                                        if (view != null) {
                                            addCallsFetchedFromServer(response.body().getData());

//                                            view.showData(response.body().getData(), skip == 0);
                                            view.isLoading(false);
                                        }
                                    }
                                }
                            } else if (response.code() == 204) {
                                isLastPage = true;
                                if (view != null) view.noData(skip == 0);
                                view.isLoading(false);

                            } else{
                                view.isLoading(false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            view.isLoading(false);

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (view != null) {
                            view.isInternetAvailable(networkConnector.isConnected());
                            view.isLoading(false);

                        }
                        Log.e(getClass().getName(), "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {
                        if (view != null) {

                            view.isLoading(false);}

                    }
                });
    }


    @Override
    public void callHistoryObserver() {

    }

    private void addCallsFetchedFromServer(List<CallHistory> callLogs) {


        CallHistory callLog;
        String image;
        CallItem callItem;
        Map<String, Object> contactInfo;

        Map<String, Object> callsMap;
        ArrayList<Map<String, Object>> callArray = new ArrayList<>();
        ArrayList<CallItem> callList = new ArrayList<>();


        String contactDocId = AppController.getInstance().getFriendsDocId();
        try {
            for (int i = callLogs.size() - 1; i >= 0; i--) {


                callLog = callLogs.get(i);

                callItem = new CallItem();
                callsMap = new HashMap<>();

                callItem.setCallId(callLog.getCallId());
                callItem.setCallDuration(callLog.getCallDuration());
                callItem.setCallInitiated(callLog.getCallInitiated());
                callItem.setCallInitiateTime(Utilities.epochtoGmt(callLog.getCalltime()));

                if (callLog.getCallType().equals("0")) {
                    callItem.setCallType("Audio Call");
                    callsMap.put("callType", "Audio Call");
                } else if (callLog.getCallType().equals("1")) {
                    callItem.setCallType("Video Call");
                    callsMap.put("callType", "Video Call");
                }

                callItem.setReceiverUid(callLog.getOpponentUid());
                String isMissedCall = "0";
                if(callLog.getMissCalled()){
                    isMissedCall = "1";
                }
                callItem.setIsMissedCall(isMissedCall);
                callsMap.put("isMissedCall", isMissedCall);
                callsMap.put("callDuration", callLog.getCallDuration());

                callItem.setReceiverIdentifier(callLog.getOpponentNumber());
//
//                /*
//                 * If the uid exists in contacts
//                 */
//                contactInfo = AppController.getInstance().getDbController().getFriendInfoFromUid(contactDocId, callLog.getOpponentUid());
//
//                if (contactInfo != null) {
//                    callItem.setReceiverName(contactInfo.get("firstName") + " " + contactInfo.get("lastName"));
//                    callsMap.put("receiverName", contactInfo.get("firstName") + " " + contactInfo.get("lastName"));
//
//                    image = (String) contactInfo.get("profilePic");
//
//
//                    if (image != null && !image.isEmpty()) {
//                        callItem.setReceiverImage(image);
//
//                        callsMap.put("receiverImage", image);
//                    } else {
//
//                        callItem.setReceiverImage("");
//                        callsMap.put("receiverImage", "");
//                    }
//
//                    callItem.setReceiverInContacts(true);
//                } else {

                    callItem.setReceiverInContacts(false);
                    /*
                     * If userId doesn't exists in contact
                     */
                    callItem.setReceiverName(callLog.getOpponentNumber());


                    callsMap.put("receiverName", callLog.getOpponentNumber());


//                    callItem.setReceiverName(callLog.getString("userName"));
//
//
//                    callsMap.put("receiverName", callLog.getString("userName"));


                    image = "";
                    if (!callLog.getOpponentProfilePic().isEmpty()) {
                        image = (String) callLog.getOpponentProfilePic();
                    }
                    if (image != null && !image.isEmpty()) {
                        callItem.setReceiverImage(image);
                        callsMap.put("receiverImage", image);
                    } else {

                        callItem.setReceiverImage("");
                        callsMap.put("receiverImage", "");
                    }

//                }
//                callList.add(callItem);

                callsMap.put("receiverUid", callLog.getOpponentUid());
                callsMap.put("callTime", Utilities.epochtoGmt(callLog.getCalltime()));
                callsMap.put("callInitiated", callLog.getCallInitiated());
                callsMap.put("callId", callLog.getCallId());

                callsMap.put("receiverIdentifier", callLog.getOpponentNumber());
                callArray.add(callsMap);
                callList.add(callItem);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*
         * Crash on Vivo phone
         */
//        if (getActivity() != null) {
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//
//                    mAdapter.notifyDataSetChanged();
//                    rv.scrollToPosition(0);
//                }
//            });
//
//        } else {
//
//
//            mAdapter.notifyDataSetChanged();
//            rv.scrollToPosition(0);
//
//        }
//        if (callList.size() == 0) {
//
//
//            llEmpty.setVisibility(View.VISIBLE);
////            try {
////                llEmpty.setTypeface(AppController.getInstance().getRegularFont(), Typeface.NORMAL);
////
////            } catch (NullPointerException e) {
////                e.printStackTrace();
////            }
////
////            tv.setText(R.string.No_Calls)
//
//
//        } else {
//
//
//            // TextView tv = (TextView) view.findViewById(R.id.userMessagechat);
//
//
//            llEmpty.setVisibility(View.GONE);
//
//
//        }

        if (page == 0) {
            AppController.getInstance().getDbController().deleteAllCalls(AppController.getInstance().getCallsDocId());
        }

        if (callArray.size() > 0) {
            AppController.getInstance().getDbController().appendCallLogs(AppController.getInstance().getCallsDocId(), callArray);

        }


        if (view != null) {
            view.isDataAvailable(false);

            ArrayList<CallItem> tempList = new ArrayList<>();

            for (int i = callList.size() - 1; i >= 0; i--) {

                tempList.add( callList.get(i));
            }

            view.showCallHistory(tempList,page == 0);
        }
    }

    public void callApiOnScroll(int firstVisibleItemPosition, int visibleItemCount,
                                int totalItemCount) {
        if (!isLoading && !isLastPage) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE) {
                page++;
                loadData(page * PAGE_SIZE, PAGE_SIZE);

            }
        }
    }

}
