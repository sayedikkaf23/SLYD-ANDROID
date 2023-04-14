package chat.hola.com.app.profileScreen.discover.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.profileScreen.discover.facebook.apiModel.Data;
import chat.hola.com.app.profileScreen.discover.follow.Follow;
import chat.hola.com.app.profileScreen.discover.follow.FollowAdapter;

/**
 * <h>FacebookFrag.class</h>
 * <p>
 * This fragment show activities of other on your post and use the
 *
 * @author 3Embed
 * @since 02/03/18.
 */

public class FacebookFrag extends Fragment implements FacebookContract.View,
        FollowAdapter.OnFollowUnfollowClickCallback, SwipeRefreshLayout.OnRefreshListener {

//    private CallbackManager callbackManager;

    @Inject
    SessionManager sessionManager;
    @Inject
    FacebookPresenter presenter;
    @Inject
    FollowAdapter followAdapter;
    @Inject
    TypefaceManager typefaceManager;

    @BindView(R.id.tvTitleFb)
    TextView tvTitleFb;
    @BindView(R.id.tvMsgFb)
    TextView tvMsgFb;
//    @BindView(R.id.login_button)
//    LoginButton login_button;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvMsg)
    TextView tvMsg;
    @BindView(R.id.btnFollowAll)
    Button btnFollowAll;
    @BindView(R.id.llFacebook)
    LinearLayout llFacebook;
    @BindView(R.id.rlContact)
    RelativeLayout rlContact;
    @BindView(R.id.recyclerContact)
    RecyclerView mRecyclerContact;
    @BindView(R.id.srSwipe)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.llHeader)
    LinearLayout llHeader;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;
    @BindView(R.id.tvEmpty)
    TextView tvEmpty;

    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private static final String USER_FRIENDS = "user_friends";

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {

    }

    private Unbinder unbinder;
    private String prefixTitle = "";
    private List<String> contactIds = new ArrayList<>();
    @Inject
    BlockDialog dialog;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Inject
    public FacebookFrag() {
    }

    public static FacebookFrag newInstance() {
        return new FacebookFrag();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefixTitle = getResources().getString(R.string.FollowFragTitle);
        //init the facebook callback manager
//        callbackManager = CallbackManager.Factory.create();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_facebook_tab, container, false);
        presenter.attachView(this);
        unbinder = ButterKnife.bind(this, rootView);
        presenter.init();
        swipeRefreshLayout.setOnRefreshListener(this);

//        login_button.setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE, USER_FRIENDS));
//        LoginManager.getInstance().registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // App
//                        Log.i("Facebook", "onSuccess: " + loginResult);
//                        Set<String> permissions = loginResult.getRecentlyGrantedPermissions();
//                        if (permissions.contains("USER_FRIENDS")) {
//                            getFacebookFriends(loginResult.getAccessToken());
//                        }
//                        for (String permission : permissions)
//                            Log.i("FACEBOOK-PERMISSION", permission);
//
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                        Log.i("Facebook", "onCancel: ");
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                        Log.i("Facebook", "onError: " + exception.toString());
//                    }
//                });
        return rootView;
    }

//    private void getFacebookFriends(AccessToken accessToken) {
//        GraphRequest request = GraphRequest.newGraphPathRequest(
//                accessToken,
//                "/" + accessToken.getUserId() + "/friends",
//                new GraphRequest.Callback() {
//                    @Override
//                    public void onCompleted(GraphResponse response) {
//                        // Insert your code here
//                        Log.i("FACEBOOK-FRIENDS", response.toString());
//                    }
//                });
//
//        request.executeAsync();
//    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.checkForFbLogin();
    }

    @OnClick(R.id.btnFollowAll)
    public void followAll() {
        presenter.followAll(contactIds);
        btnFollowAll.setEnabled(false);
    }

    @Override
    public void showMessage(String msg, int msgId) {
        if (msg != null && !msg.isEmpty()) {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        } else if (msgId != 0) {
            Toast.makeText(getContext(), getResources().getString(msgId), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void applyFont() {
        tvTitle.setTypeface(typefaceManager.getSemiboldFont());
        tvMsg.setTypeface(typefaceManager.getMediumFont());
//        btnFollowFbFend.setTypeface(typefaceManager.getSemiboldFont());

        tvTitleFb.setTypeface(typefaceManager.getSemiboldFont());
        tvMsgFb.setTypeface(typefaceManager.getMediumFont());
        btnFollowAll.setTypeface(typefaceManager.getSemiboldFont());
        tvEmpty.setTypeface(typefaceManager.getMediumFont());
    }

    @Override
    public void initPostRecycler() {
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        mRecyclerContact.setLayoutManager(llm);
        mRecyclerContact.setHasFixedSize(true);
        mRecyclerContact.setAdapter(followAdapter);
        followAdapter.setOnFollowUnfollowClickCallback(this);
        presenter.loadFollows();
    }

    @Override
    public void showFollows(Follow follow) {
        // tvTitle.setText(follow.getFollowersCount()+" "+tvTitle.getText());
        // followAdapter.setData(follow.getContacts());
    }

    public void setFbVisibility(boolean show) {
        if (show)
            llFacebook.setVisibility(View.VISIBLE);
        else
            llFacebook.setVisibility(View.GONE);
    }

    public void setContactVisibility(boolean show) {
        if (show)
            rlContact.setVisibility(View.VISIBLE);
        else
            rlContact.setVisibility(View.GONE);
    }

    @Override
    public void showFbContacts(ArrayList<Data> dataList) {
        if (dataList.size() > 0) {
            for (Data data : dataList)
                contactIds.add(data.getId());
            llHeader.setVisibility(View.VISIBLE);
            tvTitle.setText(dataList.size() + getString(R.string.space) + prefixTitle);
            followAdapter.setData(dataList);
            showEmptyUi(false);
        } else {
            showEmptyUi(true);
        }

        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }

    public void showEmptyUi(boolean show) {
        if (show) {
            llEmpty.setVisibility(View.VISIBLE);
            setFbVisibility(false);
        } else {
            llEmpty.setVisibility(View.GONE);
            //setFbVisibility(true);
        }
    }

    @Override
    public void followedAll(boolean flag) {
        if (flag) {
            llHeader.setVisibility(View.GONE);
            presenter.fetchFriendList();
        } else
            btnFollowAll.setEnabled(true);
    }

    @Override
    public void showFbContactUi(boolean show) {
        if (show) {
            presenter.setFbVisibility(true);
            presenter.setContactVisibility(false);
        } else {
            presenter.setFbVisibility(false);
            presenter.setContactVisibility(true);
        }
    }

    public void isDataLoading(boolean isLoading) {
        if (swipeRefreshLayout != null) {
            if (isLoading)
                swipeRefreshLayout.setRefreshing(true);
            else
                swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();

        presenter.detachView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFollow(String followerId) {
        presenter.follow(followerId);
    }

    @Override
    public void onUnfollow(String followerId) {
        presenter.unFollow(followerId);
    }

    @Override
    public void onRefresh() {
        presenter.fbLogin();
    }

    @Override
    public void reload() {

    }
}