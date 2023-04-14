package chat.hola.com.app.DublyCamera.CameraInFragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.signature.StringSignature;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.ezcall.android.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery.CustomGalleryBucketAdapter;
import chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery.CustomGalleryBucketItemListener;
import chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery.CustomGalleryBucketPojo;
import chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery.CustomGalleryBucketSelector;
import chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery.CustomGalleryLoadData;
import chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery.CustomGalleryMediaAdapter;
import chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery.CustomGalleryMediaItemPojo;
import chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery.GalleryImageHandler.CropImageView;
import chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery.ItemDecorationGridview;
import chat.hola.com.app.DublyCamera.ResultHolder;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;

import static android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION;

public class GalleryFragment extends Fragment implements View.OnClickListener, LoaderManager.LoaderCallbacks<CustomGalleryBucketPojo> {


    private static final int GALLERY_PERMISSIONS_REQ_CODE = 0;
    private View view;
    private Context context;

    @BindView(R.id.parent_layout)
    CoordinatorLayout parent;


    //For the custom gallery

    private ArrayList<CustomGalleryMediaItemPojo> mediaList;
    private ArrayList<String> mediaBucketList;
    private CustomGalleryMediaAdapter mediaListAdapter;
    private CustomGalleryBucketAdapter bucketListAdapter;

    private CropImageView cropImageView;
    private AppCompatImageView snapButton;
    private AppBarLayout appBarLayout;
    private RelativeLayout player;
    private VideoView videoView;
    private CustomGalleryMediaItemPojo temporaryMediaItem = null;
    private CustomGalleryBucketSelector bucketPicker = null;
    private int density;
    private boolean fragmentVisible;
    private String folderPath;
    private String appName;
    private int galleryVideoWidth, galleryVideoHeight;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        if (view == null) {

            view = inflater.inflate(R.layout.fragment_custom_gallery, container, false);

        } else {

            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
        }
        context = getActivity();


        ButterKnife.bind(this, view);

        final File imageFolder;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            imageFolder = new File(context.getExternalFilesDir(null)  + "/" + getResources().getString(R.string.app_name) + "/Media/");

        } else {

            imageFolder = new File(context.getFilesDir() + "/" + getResources().getString(R.string.app_name) + "/Media/");
        }
        if (!imageFolder.exists() && !imageFolder.isDirectory())
            imageFolder.mkdirs();


        folderPath = imageFolder.getAbsolutePath();

        appName = "Demo";
        /*
         * Creating the item list.*/
        mediaList = new ArrayList<>();
        mediaBucketList = new ArrayList<>();
        mediaListAdapter = new CustomGalleryMediaAdapter((CameraInFragmentsActivity) context, mediaList);
        bucketListAdapter = new CustomGalleryBucketAdapter(mediaBucketList);
        density = (int) context.getResources().getDisplayMetrics().density;
        initializeLayout(view);
        validateGalleryPermission();
        return view;
    }


    private void validateGalleryPermission() {


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

            try {
                setUpGallery();

            } catch (Exception e) {
                e.printStackTrace();
            }


        } else {
            checkGalleryPermissions();
        }


    }

    private void checkGalleryPermissions() {


        if (ActivityCompat.shouldShowRequestPermissionRationale((CameraInFragmentsActivity) context, Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale((CameraInFragmentsActivity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Permissions");
            builder.setMessage(R.string.gallery_permission);
            builder.setPositiveButton("OK", (dialogInterface, i) -> requestGalleryPermission());
            builder.setNegativeButton("DENY", (dialogInterface, i) -> {
                galleryPermissionsDenied();
            });
            builder.show();


        } else {

            requestGalleryPermission();
        }


    }

    private void galleryPermissionsDenied() {

        if (parent != null) {
            Snackbar snackbar = Snackbar.make(parent, R.string.gallery_permission_denied,
                    Snackbar.LENGTH_SHORT);
            snackbar.show();


            View view = snackbar.getView();
            TextView txtv = (TextView) view.findViewById(R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ((CameraInFragmentsActivity) context).supportFinishAfterTransition();
            }
        }, 500);
    }

    private void requestGalleryPermission() {


        ArrayList<String> permissionsRequired = new ArrayList<>();

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            permissionsRequired.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            permissionsRequired.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }


        requestPermissions(permissionsRequired.toArray(new String[permissionsRequired.size()]), GALLERY_PERMISSIONS_REQ_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == GALLERY_PERMISSIONS_REQ_CODE) {
            int size = grantResults.length;


            boolean allPermissionsGranted = true;
            for (int i = 0; i < size; i++) {

                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {

                    allPermissionsGranted = false;
                    break;
                }

            }
            if (allPermissionsGranted) {

                setUpGallery();
            } else {
                galleryPermissionsDenied();
            }
        }


    }


    /**
     * <h2>initializeLayout</h2>
     * <p>
     * Initialization of all the required xml content and
     * settign up the typeface and the required initialization.
     * </P>
     *
     * @param view contains the parent view of all the chield view of fragment.
     */
    private void initializeLayout(View view) {
        appBarLayout = view.findViewById(R.id.mAppBarContainer);


        ViewGroup.LayoutParams params = appBarLayout.getLayoutParams();
        params.height = Resources.getSystem().getDisplayMetrics().widthPixels;
        appBarLayout.setLayoutParams(params);
        snapButton = view.findViewById(R.id.snap_button);
        snapButton.setOnClickListener(this);
        cropImageView = view.findViewById(R.id.mPreview);


        RecyclerView recyclerView = view.findViewById(R.id.mGalleryRecyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new ItemDecorationGridview(2 * density, 4));
        recyclerView.setAdapter(mediaListAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(context, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                temporaryMediaItem = mediaList.get(position);
                appBarLayout.setExpanded(true, true);

                if (temporaryMediaItem.isVideo()) {
                    handleSelectedMedia(temporaryMediaItem.getPath(), true);
                } else {
                    /*
                     * Loading the image on item click.*/
                    handleSelectedMedia(temporaryMediaItem.getPath(), false);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));

        player = view.findViewById(R.id.player);

        videoView = view.findViewById(R.id.video);


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Toast.makeText(context, context.getString(R.string.not_able_to_play_text), Toast.LENGTH_SHORT).show();
                return true;
            }
        });


    }

    /**
     * Instantiate and return a new Loader for the given ID.
     *
     * @param id   The ID whose loader is to be created.
     * @param args Any arguments supplied by the caller.
     * @return Return a new Loader instance that is ready to start loading.
     */

    @NonNull
    @Override
    public Loader<CustomGalleryBucketPojo> onCreateLoader(int id, Bundle args) {
        return new CustomGalleryLoadData(context);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<CustomGalleryBucketPojo> loader, CustomGalleryBucketPojo data) {

        if (mediaList.size() > 0) {
            mediaList.clear();
        }
        mediaList.addAll(data.getList_data());
        mediaListAdapter.notifyDataSetChanged();

        if (mediaBucketList.size() > 0) {
            mediaBucketList.clear();
        }
        mediaBucketList.addAll(data.getBucket_name_list());

        if (mediaList.size() > 0) {
            addFirstMediaItem();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<CustomGalleryBucketPojo> loader) {
    }

    void showBucketPicker(View view) {
        if (bucketPicker == null) {
            bucketPicker = CustomGalleryBucketSelector.getInstance();
        }
        bucketPicker.showBucketPicker(view, (CameraInFragmentsActivity) context, bucketListAdapter, new CustomGalleryBucketItemListener() {
            @Override
            public void onCLick(int position) {
                String filter = mediaBucketList.get(position);





                ((CameraInFragmentsActivity)context).updateTitle(filter);

                mediaListAdapter.getFilter().filter(filter);
            }
        });
    }

    /**
     * <h2>addItem_List</h2>
     * <p>
     * Adding the first item list to the .
     * </P>
     */
    private void addFirstMediaItem() {
        temporaryMediaItem = mediaList.get(0);
        if (temporaryMediaItem.isVideo()) {
            handleSelectedMedia(temporaryMediaItem.getPath(), true);
        } else {
            handleSelectedMedia(temporaryMediaItem.getPath(), false);
        }
    }


    private void loadNewImage(String filePath) {
        player.setVisibility(View.GONE);
        cropImageView.setVisibility(View.VISIBLE);
        try {
            Glide.with(context)
                    .load(filePath)

                    .asBitmap()
                .signature(new StringSignature(
                    AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    .priority(Priority.IMMEDIATE)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            cropImageView.setImageBitmap(resource);
                        }
                    });
        } catch (OutOfMemoryError error) {
            Toast.makeText(context, R.string.not_able_to_load_text, Toast.LENGTH_SHORT).show();
            error.printStackTrace();
        }
    }

    /**
     * <h2>handleSelectedMedia</h2>
     * <p>
     * Handle videoView view and the Image view.
     * </P>
     *
     * @param isVideo    tell weather it is videoView
     * @param url_string contains the file url.
     */
    private void handleSelectedMedia(String url_string, boolean isVideo) {
        if (isVideo) {
            cropImageView.setVisibility(View.GONE);
            if (url_string != null) {
                player.setVisibility(View.VISIBLE);
                snapButton.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoPath(url_string);
                videoView.start();
            }

        } else {
            cropImageView.setVisibility(View.VISIBLE);
            player.setVisibility(View.GONE);
            videoView.setVisibility(View.GONE);
            snapButton.setVisibility(View.VISIBLE);
            try {

                if (videoView != null) {
                    if (videoView.isPlaying()) {
                        // videoView.pause();
                        videoView.stopPlayback();
                        videoView.seekTo(0);
                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            loadNewImage(url_string);
        }
    }

    @Override
    public void onStop() {


        stopCurrentlyPlayingVideo();
        super.onStop();

    }


    /**
     * <h2>cropImage</h2>
     * <p>
     * Croping the image after done with some cool addition.
     * </P>
     */
    private String cropImage() {
        String file_path = null;
        Bitmap bitmap = cropImageView.getCroppedImage();
        if (bitmap != null) {
            try {


                File file = new File(folderPath, System.currentTimeMillis() + appName + ".jpg");
                writeBitmapToFile(bitmap, file, 90);
                file_path = file.getPath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file_path;
    }

    /**
     * <h2>snapImage</h2>
     * <p>
     * Doing the cropping the image.
     * </P>
     */
    private void snapImage() {
        if (cropImageView.isZoomed()) {
            cropImageView.resetZoom();
        } else {
            cropImageView.setZoom(1.3f);
        }
    }

    /**
     * Write the given bitmap into the given file. JPEG is used as the compression format with
     * quality set
     * to 100.
     *
     * @param bm   The bitmap.
     * @param file The file to write the bitmap into.
     */
    private static void writeBitmapToFile(Bitmap bm, File file, int quality) throws IOException {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, quality, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpGallery() {
        getLoaderManager().initLoader(0, null, this).forceLoad();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.snap_button: {
                snapImage();
                break;
            }
        }
    }

    void shareSelectedMedia() {
        if (temporaryMediaItem != null) {

            String requestType = ((CameraInFragmentsActivity) context).getIntent().getStringExtra("call");

            if (requestType == null) {
                requestType = "post";
            }
            if (temporaryMediaItem.isVideo()) {

                String path = temporaryMediaItem.getPath();

                ResultHolder.setCall(requestType);

                ResultHolder.setType("video");

                long duration = getVideoDuration(new File(path));


                if (duration > 0 && duration < 300000) {
                    if (((int) (duration / 1000)) > 0) {
                        duration = (int) (duration / 1000);
                    } else {

                        duration = 1;
                    }
                    ArrayList<Integer> recordedVideoDurations = new ArrayList<>();
                    recordedVideoDurations.add((int) duration);


                    ArrayList<String> recordedVideosList = new ArrayList<>();
                    recordedVideosList.add(path);


                    Intent intent = new Intent(context, PreviewFragmentGalleryVideoActivity.class);


                    intent.putExtra("videoArray", recordedVideosList);

                    intent.putExtra("durationArray", recordedVideoDurations);
                    intent.putExtra("maximumDuration", (int) duration);

                    intent.putExtra("videoWidth", galleryVideoWidth);
                    intent.putExtra("videoHeight", galleryVideoHeight);

                    startActivity(intent);
                    if (requestType.equals("story"))
                        ((CameraInFragmentsActivity) context).supportFinishAfterTransition();

                } else {

                    Toast.makeText(context, context.getString(R.string.not_able_to_share_text), Toast.LENGTH_SHORT).show();
                }
            } else {

                String path = cropImage();


                ResultHolder.dispose();

                ResultHolder.setCall(requestType);
                ResultHolder.setType("image");


                ResultHolder.setPath(path);


                Intent intent;
                if (AppController.getInstance().isPreviewImagesForFiltersToBeGenerated()) {
                    intent = new Intent(context, PreviewFragmentFilterImageActivity.class);
                } else {
                    intent = new Intent(context, PreviewFragmentImageActivity.class);
                }

                if (requestType.equals("SaveProfile")) {

                    Bundle bundle = ((CameraInFragmentsActivity) context).getIntent().getExtras();

                    String userName = bundle != null ? bundle.getString("userName") : "";
                    String firstName = bundle != null ? bundle.getString("firstName") : "";
                    String lastName = bundle != null ? bundle.getString("lastName") : "";
                    boolean isPrivate = bundle.getBoolean("isPrivate");
                    if (!TextUtils.isEmpty(userName))
                        intent.putExtra("userName", userName);
                    if (!TextUtils.isEmpty(firstName))
                        intent.putExtra("firstName", firstName);
                    if (!TextUtils.isEmpty(lastName))
                        intent.putExtra("lastName", lastName);
                    intent.putExtra("isPrivate", isPrivate);
                }


                startActivity(intent);
                if (requestType.equals("story"))
                    ((CameraInFragmentsActivity) context).supportFinishAfterTransition();
            }


        }
    }

    private long getVideoDuration(File file) {
        long timeInMilliSec = 0;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        //use one of overloaded setDataSource() functions to set your data source

        try {
            retriever.setDataSource(context, Uri.fromFile(file));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            timeInMilliSec = Long.parseLong(time);


            String metaRotation = retriever.extractMetadata(METADATA_KEY_VIDEO_ROTATION);
            int rotation = metaRotation == null ? 0 : Integer.parseInt(metaRotation);


            if (rotation == 90 || rotation == 270) {
                galleryVideoWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                galleryVideoHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            } else {

                galleryVideoHeight = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                galleryVideoWidth = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));


            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        retriever.release();
        return timeInMilliSec;
    }


    private void stopCurrentlyPlayingVideo() {
        if (temporaryMediaItem != null && temporaryMediaItem.isVideo()) {
            if (videoView != null) {

                if (videoView.isPlaying()) {
                    try {
                        videoView.stopPlayback();
                        videoView.seekTo(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        fragmentVisible = isVisibleToUser;
        if (isVisibleToUser) {

            if (temporaryMediaItem != null && temporaryMediaItem.isVideo()) {
                if (videoView != null) {

                    if (!videoView.isPlaying()) {
                        try {
                            videoView.setVideoPath(temporaryMediaItem.getPath());
                            videoView.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        } else {


            stopCurrentlyPlayingVideo();
        }


    }

    @Override
    public void onResume() {
        super.onResume();

        if (fragmentVisible) {
            if (temporaryMediaItem != null && temporaryMediaItem.isVideo()) {
                if (videoView != null) {

                    if (!videoView.isPlaying()) {
                        try {

                            videoView.setVideoPath(temporaryMediaItem.getPath());
                            videoView.start();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }

//        if (refreshRequired) {
//
//
//            refreshRequired = false;
//
//            validateGalleryPermission();
//        }
    }

//    private boolean refreshRequired;
//
//    public void refreshRequired(boolean refreshRequired) {
//
//        this.refreshRequired = refreshRequired;
//    }
}
