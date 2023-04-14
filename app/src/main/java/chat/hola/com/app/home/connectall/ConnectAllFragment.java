package chat.hola.com.app.home.connectall;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.Activities.ContactsSecretChat;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.deepar.DeeparFiltersTabCameraActivity;
import chat.hola.com.app.GroupChat.Activities.CreateGroup;
import chat.hola.com.app.Networking.HowdooService;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.SessionApiCall;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.callhistory.CallsFragment;
import chat.hola.com.app.home.connect.ChatsFragment;
import chat.hola.com.app.home.connect.ConnectPagerAdapter;
import chat.hola.com.app.home.connect.ContactActivity;
import chat.hola.com.app.home.connect.GroupChatsFragment;
import chat.hola.com.app.home.stories.StoriesFrag;
import chat.hola.com.app.home.stories.model.ChatStoriesAdapter;
import chat.hola.com.app.home.stories.model.ClickListner;
import chat.hola.com.app.home.stories.model.StoryData;
import chat.hola.com.app.home.stories.model.StoryPost;
import chat.hola.com.app.home.stories.model.StoryResponse;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.SessionObserver;
import chat.hola.com.app.mystories.MyStoriesActivity;
import chat.hola.com.app.preview.PreviewActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by ${3embed} on ${27-10-2017}.
 * Banglore
 */

public class ConnectAllFragment extends Fragment
        implements View.OnClickListener, ClickListner, View.OnLongClickListener {

    TabLayout connectTl;
    SearchView searchView;
    LinearLayout llStories;
    ImageView addMyStoryIv;
    AppCompatImageView ivAdd;
    RecyclerView rvStories;
    LinearLayout llMyStory;
    ImageView iV_myStory;
    ImageView iV_plus,iV_info;
    ImageView activity;
//    ChatStoriesAdapter chatStoriesAdapter;
    ArrayList<StoryPost> storyPosts = new ArrayList<>();
    ArrayList<StoryData> storyData = new ArrayList<>();
    SessionApiCall sessionApiCall = new SessionApiCall();
    private SearchListner searchListner;
    @Inject
    HowdooService service;
    private TextView tvMyStory,text_friends_messages;
    private ViewPager connectVp;
    private FloatingActionButton faBtn;
    private Typeface fontBold;
    public SearchView search;
    private RelativeLayout rel_new_add,rel_add_new_contact,rel_create_new_group,
            rel_create_new_section,rel_create_new_chat,rel_create_new_secretChat;
    SessionManager sessionManager;
    private static Bus bus = AppController.getBus();

    public ChatsFragment chatsFragment;

    /*This is used for storiesFetched list*/
    public static int storyPage = 0;
    public static final int storyPageSize = Constants.PAGE_SIZE;
    private LinearLayoutManager storyLayoutManager;
    private boolean isLoading,isLastPage;
    /*--------storiesFetched list end----------*/

    @Inject
    public ConnectAllFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            bus.register(this);
            chatsFragment = new ChatsFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if (bus != null) bus.unregister(this);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_connect_all, container, false);

        sessionManager = new SessionManager(getActivity());

        TypefaceManager typefaceManager = new TypefaceManager(getActivity());

        connectTl = view.findViewById(R.id.connectTl);
        connectVp = view.findViewById(R.id.connectVp);
        rel_new_add = view.findViewById(R.id.rel_new_add);
        rel_add_new_contact = view.findViewById(R.id.rel_add_new_contact);
        rel_create_new_chat = view.findViewById(R.id.rel_create_new_chat);
        rel_create_new_group = view.findViewById(R.id.rel_create_new_group);
        rel_create_new_secretChat = view.findViewById(R.id.rel_create_new_secretChat);
        rel_create_new_section = view.findViewById(R.id.rel_create_new_section);
        ImageView imgBack = (ImageView) view.findViewById(R.id.img_back);
        RelativeLayout rel_new_message = (RelativeLayout) view.findViewById(R.id.rel_new_message);
        faBtn = view.findViewById(R.id.faBtn);
        changeVisibilityOfViews();
        connectTl.setVisibility(View.VISIBLE);
        rel_new_add.setVisibility(View.GONE);
        AppController appController = AppController.getInstance();
        fontBold = appController.getSemiboldFont();

        search = (SearchView) view.findViewById(R.id.search);
        iV_plus = (ImageView) view.findViewById(R.id.iV_info);
        iV_info = (ImageView) view.findViewById(R.id.iV_info);
        iV_plus.setOnClickListener(this);
        iV_info.setOnClickListener(this);
        /*
         *BugId:DUBAND051
         *BugTitle:
         * Developer name:Ankit K Tiwary
         * Fixed date:6April2021
         *
         * */

//        iV_plus.setVisibility(View.VISIBLE);
        activity = (ImageView) view.findViewById(R.id.activity);
        activity.setVisibility(View.GONE);
        activity.setOnClickListener(this);

        llStories = (LinearLayout) view.findViewById(R.id.llStories);
        addMyStoryIv = (ImageView) view.findViewById(R.id.addMyStoryIv);
        addMyStoryIv.setOnClickListener(this);
        //addMyStoryIv.setOnLongClickListener(this);
        ivAdd = (AppCompatImageView) view.findViewById(R.id.ivAdd);

        llMyStory = (LinearLayout) view.findViewById(R.id.llMyStory);
        llMyStory.setOnClickListener(this);
        iV_myStory = (ImageView) view.findViewById(R.id.iV_myStory);

        rvStories = (RecyclerView) view.findViewById(R.id.rvStories);
        tvMyStory = (TextView) view.findViewById(R.id.tvMyStory);
        text_friends_messages = (TextView) view.findViewById(R.id.text_friends_messages);
        tvMyStory.setTypeface(typefaceManager.getMediumFont());

//        chatStoriesAdapter = new ChatStoriesAdapter(storyData, typefaceManager, this);
//        rvStories.setHasFixedSize(true);
//        storyLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        rvStories.setLayoutManager(storyLayoutManager);
//        rvStories.setAdapter(chatStoriesAdapter);

        rvStories.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = storyLayoutManager.getChildCount();
                int totalItemCount = storyLayoutManager.getItemCount();
                int firstVisibleItemPosition = storyLayoutManager.findFirstVisibleItemPosition();
                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= storyPageSize){
                        storyPage++;
//                        getStories(storyPage*storyPageSize,storyPageSize);
                    }
                }
            }
        });

        try {
            if(sessionManager.getUserProfilePic() != null && !sessionManager.getUserProfilePic().isEmpty()){
                Glide.with(getContext())
                        .load(sessionManager.getUserProfilePic())
                        .asBitmap()
                        .centerCrop()
                        .signature(new StringSignature(
                                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        .placeholder(R.drawable.profile_one)
                        .into(new BitmapImageViewTarget(addMyStoryIv) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                if (getContext() != null) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getContext().getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    addMyStoryIv.setImageDrawable(circularBitmapDrawable);
                                }
                            }
                        });
            } else {
                Utilities.setTextRoundDrawable(getActivity(), sessionManager.getFirstName(), sessionManager.getLastName(), addMyStoryIv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!AppController.getInstance().isFriendsFetched()) {

            try {
                JSONObject object = new JSONObject();

                object.put("eventName", "FetchFriends");

                AppController.getBus().post(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        searchView = (SearchView) view.findViewById(R.id.search);

        searchView.setIconified(true);
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                searchView.setIconifiedByDefault(true);
                searchView.setIconified(true);
                searchView.setQuery("", false);
                searchView.clearFocus();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                chatsFragment.onQueryTextChanges(newText);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                chatsFragment.onClosed();
                return false;
            }
        });
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_new_add.setVisibility(View.GONE);
            }
        });
        rel_new_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rel_new_add.setVisibility(View.GONE);
            }
        });
        rel_add_new_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addContactIntent = new Intent(Intent.ACTION_INSERT);
                addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivity(addContactIntent);
            }
        });
        rel_create_new_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateGroup.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });
        rel_create_new_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name", "ChatFragment");
                startActivity(new Intent(getActivity(), ContactActivity.class));
            }
        });
        rel_create_new_secretChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getActivity(), ContactsSecretChat.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent1);
            }
        });
        rel_create_new_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        return view;
    }

    /*Here we change the common views visibility on selection of fragment*/
    public void changeVisibilityOfViews() {
        LandingActivity mActivity = (LandingActivity) getActivity();

        mActivity.hideActionBar();
        mActivity.ivProfilePic.setVisibility(View.GONE);
        mActivity.iV_plus.setVisibility(View.VISIBLE);
        mActivity.tvSearch.setVisibility(View.GONE);
        mActivity.setTitle(getString(R.string.string_639), new TypefaceManager(mActivity).getMediumFont());
        mActivity.removeFullScreenFrame();
        mActivity.linearPostTabs.setVisibility(View.GONE);
        mActivity.tvCoins.setVisibility(View.GONE);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        //Adding the tabs using addTab() method
        ConnectPagerAdapter adapter = new ConnectPagerAdapter(getChildFragmentManager());
        // Add Fragments to adapter one by one
        String chats = getResources().getString(R.string.chats);
        String groups = getResources().getString(R.string.groups);
        String calls = getResources().getString(R.string.calls);

        ChatsFragment chatsFragment = new ChatsFragment();
        GroupChatsFragment groupFragment = new GroupChatsFragment();
        CallsFragment callsFragment = new CallsFragment();
//        callsFragment.hideHeader();
        try {
            adapter.addFragment(chatsFragment, chats);
            adapter.addFragment(groupFragment, groups);
            adapter.addFragment(callsFragment, calls);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }


        // chatsFragment.hideTablayout();
        connectVp.setAdapter(adapter);
        connectTl.setupWithViewPager(connectVp);
        faBtn.setOnClickListener(this);

        connectVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
//                Log.v("abcde",String.valueOf(position));
                rel_new_add.setVisibility(View.GONE);
            }
        });

        TextView tabOne =
                (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tab_textview, null);
        tabOne.setTypeface(fontBold);
        tabOne.setText(chats);
        connectTl.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tab_textview, null);
        tabTwo.setTypeface(fontBold);
        tabTwo.setText(groups);
        connectTl.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tab_textview, null);
        tabThree.setTypeface(fontBold);
        tabThree.setText(calls);
        connectTl.getTabAt(2).setCustomView(tabThree);
    }

    @Override
    public void onResume() {
        super.onResume();
        //loadStories();
    }

    public void loadStories(){
        storyPage=0;
//        getStories(storyPage * storyPageSize, storyPageSize);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.addMyStoryIv:
                if (!storyPosts.isEmpty()) {
                    Intent intent = new Intent(getActivity(), MyStoriesActivity.class);
                    intent.putExtra("myStories", (Serializable) storyPosts);
                    startActivity(intent);
                } else {
                    Dexter.withActivity(getActivity())
                            .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .withListener(new MultiplePermissionsListener() {
                                @Override
                                public void onPermissionsChecked(MultiplePermissionsReport report) {
                                    // check if all permissions are granted
                                    if (report.areAllPermissionsGranted()) {
                                        startActivity(
                                                new Intent(getContext(), DeeparFiltersTabCameraActivity.class).putExtra("call", "story"));
                                    }

                                    // check for permanent denial of any permission
                                    if (report.isAnyPermissionPermanentlyDenied()) {
                                        // permission is denied permenantly, navigate user to app settings
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
                //seeStory();
                break;
            case R.id.llMyStory:
                seeStory();
                break;
            //            case R.id.activity:
            //                ((LandingActivity)getActivity()).activity();
            //                break;
            case R.id.iV_info:

                rel_new_add.setVisibility(View.VISIBLE);
                /*PopupMenu popup = new PopupMenu(getActivity(), v);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.plus_icon_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.newChat:
                                Bundle bundle = new Bundle();
                                bundle.putString("name", "ChatFragment");
                                startActivity(new Intent(getActivity(), ContactActivity.class));
                                break;
//                            case R.id.groupChat:
//                                Intent intent = new Intent(getActivity(), CreateGroup.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                                startActivity(intent);
//                                break;
                            case R.id.secretChat:
                                Intent intent1 = new Intent(getActivity(), ContactsSecretChat.class);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                startActivity(intent1);
                                break;
                        }

                        return true;
                    }
                });

                popup.show();*/
                break;
        }
    }


    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.addMyStoryIv:
                //List<StoryPost> storyPosts = mPresenter.getStoryPosts();
                Intent intent = new Intent(getActivity(), MyStoriesActivity.class);
                intent.putExtra("myStories", (Serializable) storyPosts);
                startActivity(intent);
                //seeStory();
                break;
        }
        return false;
    }

    public void selectTab(int i) {
        connectVp.setCurrentItem(i);
    }

    /*
    * Bug Title: Chat-> Stories-> My stories is not constant sometime displays sometime not
    * Bug Id: #2809
    * Fix Dev: Hardik
    * Fix Desc: refreshing adapter in this method only
    * Fix Date: 26/6/21
    * */

    public void getMyStories() {
        service.getMyStories(AppController.getInstance().getApiToken(), Constants.LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Response<StoryResponse>>() {
                    @Override
                    public void onNext(Response<StoryResponse> response) {
                        try {
                            switch (response.code()) {
                                case 200:
                                    if (response.body() != null) {
                                        if (response.body().getData().size() > 0) {
                                            List<StoryPost> dataList = response.body().getData().get(0).getPosts();
                                            if (myStories(dataList)) {
                                                updateMyStoryView(dataList.get(0).getUrlPath(), dataList.get(0).getTimestamp());
                                            }
                                        } else {
                                            storyPosts.clear();
                                            llMyStory.setVisibility(View.GONE);
                                        }
                                    }
//                                    chatStoriesAdapter.notifyDataSetChanged();
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
                                                            getMyStories();
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
                                    sessionApiCall.getNewSession(service, sessionObserver);
                                    break;
                                default:
//                                    chatStoriesAdapter.notifyDataSetChanged();
                                    break;
                            }
                        } catch (Exception ignored) {
                            storyPosts.clear();
                            llMyStory.setVisibility(View.GONE);
                            //ivAdd.setVisibility(View.VISIBLE);
//                            chatStoriesAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        chatStoriesAdapter.notifyDataSetChanged();
                        Log.e(getClass().getName(), "onError: " + e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void updateMyStoryView(String url, String timestamp) {
        try {
            //llMyStory.setVisibility(View.VISIBLE);
            iV_myStory.setImageURI(Uri.parse(url.replace("mp4", "jpg")));

            //ivAdd.setVisibility(View.GONE);
            //addMyStoryIv.setImageURI(url.replace("mp4", "jpg"));
            //            tapToAddStoryTv.setText(timestamp == null ? getString(R.string.justnow) : TimeAgo.getTimeAgo(Long.parseLong(timestamp)));
        } catch (Exception ignore) {
        }
    }

    public boolean myStories(List<StoryPost> posts) {
        storyPosts.clear();
        storyPosts.addAll(posts);
        addMystoryInList();
        //chatStoriesAdapter.notifyDataSetChanged();
        return storyPosts.size() > 0;
    }

    public void getStories(int skip, int limit) {
        isLoading = true;
        if (service != null) {
            service.getStories(AppController.getInstance().getApiToken(), Constants.LANGUAGE,
                    skip,limit)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Response<StoryResponse>>() {
                        @Override
                        public void onNext(Response<StoryResponse> response) {
                            isLoading = false;
                            try {
                                switch (response.code()) {
                                    case 200:
                                        if(skip==0)
                                            getMyStories();
                                        if (response.body().getData().size() > 0) {
                                            isLastPage = response.body().getData().size() < storyPageSize;
                                            storiesFetched(response.body().getData(),skip==0);
                                        } else if(skip==0){
                                            storyData.clear();
                                        }
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
                                                                getStories(skip, limit);
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
                                        sessionApiCall.getNewSession(service, sessionObserver);
                                        break;
                                }
                            } catch (Exception ignored) {

                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            e.printStackTrace();
                            isLoading = false;
                        }

                        @Override
                        public void onComplete() {
                            isLoading = false;
                        }
                    });
        }
    }

    public void storiesFetched(List<StoryData> data, boolean isClear) {
        if(isClear) storyData.clear();
        storyData.addAll(data);
        //chatStoriesAdapter.notifyDataSetChanged();
    }

    private void addMystoryInList() {
        if (!storyPosts.isEmpty()) {
            StoryData story = new StoryData();
            story.setPosts(storyPosts);
            story.setMine(true);

            if (!storyData.isEmpty() && storyData.get(0).isMine()) {
                storyData.set(0, story);
            } else {
                storyData.add(0, story);
            }
        }
    }


    public void seeStory() {
        if (storyPosts.isEmpty()) {
            //imageSourcePicker.show();
            Dexter.withActivity(getActivity())
                    .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(new MultiplePermissionsListener() {
                        @Override
                        public void onPermissionsChecked(MultiplePermissionsReport report) {
                            // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                startActivity(
                                        new Intent(getContext(), DeeparFiltersTabCameraActivity.class).putExtra("call", "story"));
                            }

                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied()) {
                                // permission is denied permenantly, navigate user to app settings
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
        } else {
            startPreviewActivity(storyPosts, true, 0);
            //startActivity(new Intent(getContext(), PreviewActivity.class).putExtra("data", (Serializable) storyPosts).putExtra("isMine", true));
        }
    }

    public void startPreviewActivity(List<StoryPost> data, boolean isMystory, int postion) {

        Intent a = new Intent(getActivity(), PreviewActivity.class);
        a.putExtra(PreviewActivity.IS_IMMERSIVE_KEY, false);
        a.putExtra(PreviewActivity.IS_CACHING_ENABLED_KEY, false);
        a.putExtra(PreviewActivity.IS_TEXT_PROGRESS_ENABLED_KEY, false);
        a.putExtra(PreviewActivity.STATUS_IS_MY_STORY, isMystory);
        a.putExtra("position", postion);

        if (isMystory) {
            a.putExtra(PreviewActivity.MY_STORY_POSTS, (Serializable) data);
        } else {
            a.putExtra(PreviewActivity.ALL_STORY_POST, (Serializable) storyData);
        }

        startActivity(a);
    }

    @Override
    public void onItemClick(int position) {
        try {

            if (position >= 0) {
                if (storyData.get(position).isMine()) {
                    startPreviewActivity(storyPosts, true, 0);
                } else {

                    startPreviewActivity(storyData.get(position).getPosts(), false, position);
                }
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void getMessage(JSONObject jsonObject) {
        try {
            if (jsonObject.getString("eventName").equals("myStoryUpdate")) {
                getMyStories();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public interface SearchListner {

        void onQueryTextChange(String newText);

        void onClose();
    }

    public void setSearchListner(SearchListner searchListner) {
        this.searchListner = searchListner;
    }
}
