package chat.hola.com.app.DublyCamera.CameraInFragments.Filters;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.RelativeLayout;

import com.ezcall.android.R;

import java.io.File;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.Adapters.Manage_Adapter;
import chat.hola.com.app.DublyCamera.CameraInFragments.Filters.ModelClasses.Manage_item;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;

public class ManageFilters extends AppCompatActivity {


    private Manage_Adapter mAdapter;

    private int[] filterString = {R.string.text_filter_in1977, R.string.text_filter_amaro,
            R.string.text_filter_brannan, R.string.text_filter_early_bird, R.string.text_filter_hefe, R.string.text_filter_hudson,
            R.string.text_filter_inkwell, R.string.text_filter_lomofi, R.string.text_filter_lord_kelvin,
            R.string.text_filter_early_bird, R.string.text_filter_rise, R.string.text_filter_sierra,
            R.string.text_filter_sutro, R.string.text_filter_toaster, R.string.text_filter_valencia,
            R.string.text_filter_walden, R.string.text_filter_xproii};


    private ArrayList<Manage_item> mFilterData = new ArrayList<>();
    private String filterFolderPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_manage_filters);
        final File filterImageFolder;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            filterImageFolder = new File(getExternalFilesDir(null)  + "/" + getResources().getString(R.string.app_name) + "/Media/filters");

        } else {

            filterImageFolder = new File(getFilesDir() + "/" + getResources().getString(R.string.app_name) + "/Media/filters");
        }
        if (!filterImageFolder.exists() && !filterImageFolder.isDirectory())
            filterImageFolder.mkdirs();


        filterFolderPath = filterImageFolder.getAbsolutePath();

        RecyclerView thumb_list_view = (RecyclerView) findViewById(R.id.thumb_list_view);
        thumb_list_view.setHasFixedSize(true);
        thumb_list_view.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new Manage_Adapter(this, mFilterData);
        thumb_list_view.setAdapter(mAdapter);
        loadFilterData();
        RelativeLayout tick_image = (RelativeLayout) findViewById(R.id.tick_image);
        tick_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Integer> excludedIds = new ArrayList<>();
                for (int i = 0; i < mFilterData.size(); i++) {
                    if (!mFilterData.get(i).isSelected()) {
                        excludedIds.add(mFilterData.get(i).getFilterId());
                    }
                }
                AppController.getInstance().setFiltersUpdated(true);
                AppController.getInstance().setExcludedFilterIds(excludedIds);
                onBackPressed();
            }
        });
        thumb_list_view.addOnItemTouchListener(new RecyclerItemClickListener(this, thumb_list_view, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {


                Manage_item item = mFilterData.get(position);


                item.setSelected(!item.isSelected());


                mFilterData.set(position, item);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();

                    }
                });

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        }));


    }


    private void loadFilterData() {
        ArrayList<Integer> excludedIds = AppController.getInstance().getExcludedFilterIds();
        Manage_item item;
        for (int i = 0; i < 17; i++) {
            item = new Manage_item();
            item.setSelected(!excludedIds.contains(i + 1));
            item.setFilterId(i + 1);
            item.setFilterName(getString(filterString[i]));
            item.setFilterImageUrl(filterFolderPath + "/filter_" + (i) + ".jpg");

//            item.setFilterImageUrl("");

            mFilterData.add(item);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }


}
