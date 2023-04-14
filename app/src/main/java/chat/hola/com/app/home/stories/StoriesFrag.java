package chat.hola.com.app.home.stories;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Dialog.BlockDialog;
import chat.hola.com.app.DublyCamera.deepar.DeeparFiltersTabCameraActivity;
import chat.hola.com.app.ImageCropper.CropImage;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TimeAgo;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.cameraActivities.SandriosCamera;
import chat.hola.com.app.cameraActivities.configuration.CameraConfiguration;
import chat.hola.com.app.cameraActivities.manager.CameraOutputModel;
import chat.hola.com.app.home.LandingActivity;
import chat.hola.com.app.home.stories.model.StoriesAdapter;
import chat.hola.com.app.home.stories.model.StoryPost;
import chat.hola.com.app.manager.session.SessionManager;
import chat.hola.com.app.models.InternetErrorView;
import chat.hola.com.app.mystories.MyStoriesActivity;
import chat.hola.com.app.poststatus.PostStatusActivity;
import chat.hola.com.app.preview.PreviewActivity;
import dagger.android.support.DaggerFragment;

/**
 * <h1>StoriesFrag</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/26/2018
 */

public class StoriesFrag extends DaggerFragment implements StoriesContract.View {

    static final int READ_STORAGE_REQ_CODE = 101;
    private static final int RESULT_LOAD_IMAGE = 202;
    private static final int REQUEST_CODE = 303;
    public static final String TAG = "StoriesFrag";
    private Unbinder unbinder;

    @Inject
    StoriesPresenter mPresenter;
    @Inject
    TypefaceManager typefaceManager;
    @Inject
    SessionManager sessionManager;
    //    @Inject
    //    ImageSourcePicker imageSourcePicker;
    @Inject
    StoriesAdapter mAdapter;
    @Inject
    BlockDialog dialog;
    @Inject
    PreviewActivity previewActivity;

    @BindView(R.id.iV_storyOption)
    ImageView iV_storyOption;
    @BindView(R.id.myStoryTv)
    TextView myStoryTv;
    @BindView(R.id.tapToAddStoryTv)
    TextView tapToAddStoryTv;
    @BindView(R.id.recentStoriesRv)
    RecyclerView recentStoriesRv;
    @BindView(R.id.addMyStoryIv)
    ImageView addMyStoryIv;
    @BindView(R.id.llNetworkError)
    InternetErrorView llNetworkError;
    @BindView(R.id.ivAdd)
    AppCompatImageView ivAdd;
    @BindView(R.id.llEmpty)
    LinearLayout llEmpty;
    @BindView(R.id.ll_statuslayout)
    LinearLayout ll_statuslayout;
    @BindView(R.id.tvEmptyTitle)
    TextView tvEmptyTitle;
    @BindView(R.id.tvEmptyMsg)
    TextView tvEmptyMsg;
    @BindView(R.id.fabCameraImage)
    ImageView fabCameraImage;

    @Override
    public void userBlocked() {
        dialog.show();
    }

    @Inject
    public StoriesFrag() {
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            try {
                mPresenter.attachView(this);
                // imageSourcePicker.setOnSelectImageSource(callback);
                mAdapter.setListener(mPresenter);
                recentStoriesRv.setHasFixedSize(true);
                recentStoriesRv.setLayoutManager(
                        new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                recentStoriesRv.setAdapter(mAdapter);
                mPresenter.myStories();
                mPresenter.stories();
                mPresenter.storyObserver();
            } catch (Exception ignored) {
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_stories, container, false);
        unbinder = ButterKnife.bind(this, view);
        setTypefaces();
        llNetworkError.setErrorListner(this);
        mPresenter.setView(this);
        changeVisibilityOfViews();


        mPresenter.attachView(this);
        // imageSourcePicker.setOnSelectImageSource(callback);
        mAdapter.setListener(mPresenter);
        recentStoriesRv.setHasFixedSize(true);
        recentStoriesRv.setLayoutManager(
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recentStoriesRv.setAdapter(mAdapter);
//        mPresenter.myStories();
//        mPresenter.stories();
        mPresenter.storyObserver();


        return view;
    }

    private void updateMyStoryThumbnail() {

        if (sessionManager.getUserProfilePic().isEmpty() || sessionManager.getUserProfilePic() == null) {
            Utilities.setTextRoundDrawable(getActivity(), sessionManager.getFirstName()
                    , sessionManager.getLastName(), addMyStoryIv);
        } else {
            Glide.with(getContext())
                    .load(sessionManager.getUserProfilePic())
                    .asBitmap()
                    .centerCrop()
                    .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    .into(new BitmapImageViewTarget(addMyStoryIv) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            addMyStoryIv.setImageDrawable(circularBitmapDrawable);
                        }
                    });

        }


    }

    private void setTypefaces() {
        myStoryTv.setTypeface(typefaceManager.getSemiboldFont());
        tapToAddStoryTv.setTypeface(typefaceManager.getRegularFont());
        tvEmptyTitle.setTypeface(typefaceManager.getSemiboldFont());
        tvEmptyMsg.setTypeface(typefaceManager.getRegularFont());
    }

    @OnClick(R.id.fabCameraImage)
    public void addStories() {
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
        //imageSourcePicker.show();
    }

    @OnClick(R.id.iV_mystory_list)
    public void myStoryList() {
        List<StoryPost> storyPosts = mPresenter.getStoryPosts();
        Intent intent = new Intent(getActivity(), MyStoriesActivity.class);
        intent.putExtra("myStories", (Serializable) storyPosts);
        startActivity(intent);
    }

    @OnClick(R.id.iV_postStatus)
    public void postStatus() {
        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            startActivity(new Intent(getActivity(), PostStatusActivity.class));
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //        imageSourcePicker.setOnSelectImageSource(callback);
        //        mAdapter.setListener(mPresenter);
        //        recentStoriesRv.setHasFixedSize(true);
        //        recentStoriesRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //        recentStoriesRv.setAdapter(mAdapter);
        //        mPresenter.myStories();
        //        mPresenter.stories();
        //        mPresenter.storyObserver();
    }

    @OnClick(R.id.rlMyStory)
    public void seeStory() {
        List<StoryPost> storyPosts = mPresenter.getStoryPosts();
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

  /*@OnClick(R.id.iV_storyOption)
  public void storyOption() {
    List<StoryPost> storyPosts = mPresenter.getStoryPosts();
    Intent intent = new Intent(getActivity(), MyStoriesActivity.class);
    intent.putExtra("myStories", (Serializable) storyPosts);
    startActivity(intent);
  }*/

    //    ImageSourcePicker.OnSelectImageSource callback = new ImageSourcePicker.OnSelectImageSource() {
    //        @Override
    //        public void onCamera() {
    //            launchCamera();
    //        }
    //
    //        @Override
    //        public void onGallary() {
    //            checkReadImage();
    //        }
    //
    //        @Override
    //        public void onCancel() {
    //        }
    //    };

    private void launchCamera() {
        SandriosCamera.with((Activity) getContext())
                .setShowPicker(true)
                .setVideoFileSize(Constants.Camera.FILE_SIZE)
                .setMediaAction(CameraConfiguration.MEDIA_ACTION_BOTH)
                .enableImageCropping(true)
                .launchCamera(mPresenter);
    }

    private void checkReadImage() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mPresenter.launchImagePicker();
        } else {
            requestReadImagePermission();
        }
    }

    private void requestReadImagePermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
        }, READ_STORAGE_REQ_CODE);
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
    public void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
        if (unbinder != null) unbinder.unbind();
    }

    @Override
    public void sessionExpired() {
        sessionManager.sessionExpired(getContext());
    }

    @Override
    public void isInternetAvailable(boolean flag) {
        llNetworkError.setVisibility(flag ? View.GONE : View.VISIBLE);
    }

    @Override
    public void myStories(String url, String timeStamp) {
        try {
            ivAdd.setVisibility(View.GONE);
            Glide.with(getContext())
                    .load(url.replace("mp4", "jpg"))
                    .asBitmap()
                    .centerCrop()
                    .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    //.diskCacheStrategy(DiskCacheStrategy.NONE)
                    //.skipMemoryCache(true)
                    .into(new BitmapImageViewTarget(addMyStoryIv) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            addMyStoryIv.setImageDrawable(circularBitmapDrawable);
                        }
                    });


            tapToAddStoryTv.setText(timeStamp == null ? getContext().getString(R.string.justnow)
                    : TimeAgo.getTimeAgo(Long.parseLong(timeStamp)));
        } catch (Exception ignore) {
        }
    }

    @Override
    public void launchImagePicker(Intent intent) {
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onComplete(Bundle bundle) {
        //  getContext().startService(new Intent(getContext(), StoryService.class).putExtra("data", bundle));
    }

    @Override
    public void isDataAvailable(boolean empty) {
        llEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RESULT_LOAD_IMAGE) {
            mPresenter.parseMedia(resultCode, data);
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            mPresenter.parseCropedImage(resultCode, data);
        }
    }

    @Override
    public void onResume() {
        updateMyStoryThumbnail();
        mPresenter.myStories();
        mPresenter.stories();
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivity(new Intent(getContext(), DeeparFiltersTabCameraActivity.class));
            }
        }
    }

    @Override
    public void launchCropImage(Uri data) {
        CropImage.activity(data).start(getContext(), this);
    }

    @Override
    public void launchActivity(CameraOutputModel model) {
        mPresenter.onComplete(model);
    }

    private String[] resources, mediaType, storyIds, captions, viewCounts;
    //private ArrayList<ArrayList<Viewer>> viewerList;

    long[] durationArray;

    @Override
    public void preview(int position) {

        startPreviewActivity(mPresenter.getStoryPosts(position), false, position);
        //startActivity(new Intent(getContext(), PreviewActivity.class).putExtra("data", (Serializable) mPresenter.getStoryPosts(position)));
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
            a.putExtra(PreviewActivity.ALL_STORY_POST, (Serializable) mPresenter.getAllStoryData());
        }

        startActivity(a);
    }

    @Override
    public void reload() {
        mPresenter.myStories();
        mPresenter.stories();
    }

    public void updateViewed(int position) {
        mPresenter.updateViewed(position);
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

        onResume();
    }

    /*public void setdurationInArray(){
        for(int i=0;i<resources.length;i++){
            if(isVideo(resources[i])){
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(resources[i], new HashMap<String, String>());
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInMillisec = Long.parseLong(time);
                retriever.release();

                durationArray[i] = timeInMillisec;
            }else {
                durationArray[i] = 3000L;
            }
        }
    }*/

    @OnLongClick(R.id.ll_statuslayout)
    public void storyOption() {
        List<StoryPost> storyPosts = mPresenter.getStoryPosts();
        Intent intent = new Intent(getActivity(), MyStoriesActivity.class);
        intent.putExtra("myStories", (Serializable) storyPosts);
        startActivity(intent);
    }
}
