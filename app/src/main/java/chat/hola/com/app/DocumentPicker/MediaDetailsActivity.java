package chat.hola.com.app.DocumentPicker;

/**
 * Created by moda on 22/08/17.
 */

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import chat.hola.com.app.DocumentPicker.Adapters.FileAdapterListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import chat.hola.com.app.AppController;
import chat.hola.com.app.DocumentPicker.Adapters.PhotoGridAdapter;
import chat.hola.com.app.DocumentPicker.Cursors.LoaderCallbacks.FileResultCallback;
import chat.hola.com.app.DocumentPicker.Models.Media;
import chat.hola.com.app.DocumentPicker.Models.PhotoDirectory;
import chat.hola.com.app.DocumentPicker.Utils.AndroidLifecycleUtils;
import chat.hola.com.app.DocumentPicker.Utils.MediaStoreHelper;
import com.ezcall.android.R;


public class MediaDetailsActivity extends BaseFilePickerActivity implements FileAdapterListener {

    private static final int SCROLL_THRESHOLD = 30;
    private RecyclerView recyclerView;
    private TextView emptyView;
    private RequestManager mGlideRequestManager;
    private PhotoGridAdapter photoGridAdapter;
    private int fileType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState, R.layout.activity_media_details);

    }

    @Override
    protected void initView() {
        mGlideRequestManager = Glide.with(this);
        Intent intent = getIntent();
        if (intent != null) {

            fileType = intent.getIntExtra(FilePickerConst.EXTRA_FILE_TYPE, FilePickerConst.MEDIA_TYPE_IMAGE);
            PhotoDirectory photoDirectory = intent.getParcelableExtra(PhotoDirectory.class.getSimpleName());
            if (photoDirectory != null) {

                setUpView(photoDirectory);
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setTitle(photoDirectory.getName());
                    getSupportActionBar().setBackgroundDrawable(getDrawable(R.color.base_color));
                }
            }
        }
    }

    private void setUpView(PhotoDirectory photoDirectory) {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        emptyView = (TextView) findViewById(R.id.empty_view);


        emptyView.setTypeface(AppController.getInstance().getRegularFont(), Typeface
                .NORMAL);


        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Log.d(">>> Picker >>>", "dy = " + dy);
                if (Math.abs(dy) > SCROLL_THRESHOLD) {
                    mGlideRequestManager.pauseRequests();
                } else {
                    resumeRequestsIfNotDestroyed();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    resumeRequestsIfNotDestroyed();
                }
            }
        });

        getDataFromMedia(photoDirectory.getBucketId());
    }

    private void getDataFromMedia(String bucketId) {
        Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putBoolean(FilePickerConst.EXTRA_SHOW_GIF, false);
        mediaStoreArgs.putString(FilePickerConst.EXTRA_BUCKET_ID, bucketId);

        mediaStoreArgs.putInt(FilePickerConst.EXTRA_FILE_TYPE, fileType);

        if (fileType == FilePickerConst.MEDIA_TYPE_IMAGE) {
            MediaStoreHelper.getPhotoDirs(this, mediaStoreArgs,
                    new FileResultCallback<PhotoDirectory>() {
                        @Override
                        public void onResultCallback(List<PhotoDirectory> dirs) {
                            updateList(dirs);
                        }
                    });
        } else if (fileType == FilePickerConst.MEDIA_TYPE_VIDEO) {
            MediaStoreHelper.getVideoDirs(this, mediaStoreArgs,
                    new FileResultCallback<PhotoDirectory>() {
                        @Override
                        public void onResultCallback(List<PhotoDirectory> dirs) {
                            updateList(dirs);
                        }
                    });
        }
    }

    private void updateList(List<PhotoDirectory> dirs) {
        ArrayList<Media> medias = new ArrayList<>();
        for (int i = 0; i < dirs.size(); i++) {
            medias.addAll(dirs.get(i).getMedias());
        }

        Collections.sort(medias, new Comparator<Media>() {
            @Override
            public int compare(Media a, Media b) {
                return b.getId() - a.getId();
            }
        });

        if (medias.size() > 0) {
            emptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        }

        if (photoGridAdapter != null) {
            photoGridAdapter.setData(medias);
            photoGridAdapter.notifyDataSetChanged();
        } else {
            photoGridAdapter = new PhotoGridAdapter(this, mGlideRequestManager, (ArrayList<Media>) medias, PickerManager.getInstance().getSelectedPhotos(), false, this);
            recyclerView.setAdapter(photoGridAdapter);
        }

    }

    private void resumeRequestsIfNotDestroyed() {
        if (!AndroidLifecycleUtils.canLoadImage(this)) {
            return;
        }

        mGlideRequestManager.resumeRequests();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picker_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_done) {
            setResult(RESULT_OK, null);
            finish();

            return true;
        } else if (i == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected() {
        if (PickerManager.getInstance().getMaxCount() == 1) {
            setResult(RESULT_OK, null);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED, null);
        finish();
    }
}