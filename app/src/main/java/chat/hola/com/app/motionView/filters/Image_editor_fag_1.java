/*
package chat.hola.com.howdoo.motionView.filters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.howdoo.chat.R;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import chat.hola.com.howdoo.AppController;
import chat.hola.com.howdoo.Utilities.RecyclerItemClickListener;
import chat.hola.com.howdoo.motionView.filters.adapters.Filter_Adapter;
import chat.hola.com.howdoo.motionView.filters.helperClasses.CancelHandler;
import chat.hola.com.howdoo.motionView.filters.helperClasses.FilterTools;
import chat.hola.com.howdoo.motionView.filters.helperClasses.GenerateThumbnails;
import chat.hola.com.howdoo.motionView.filters.modelClasses.Filter_item;
import jp.co.cyberagent.android.gpuimage.GPUImageFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageView;

public class Image_editor_fag_1 extends Fragment implements View.OnClickListener
{
    private static final String ARG_PARAM1 = "image_path";
    private static final String ARG_PARAM2 = "param2";
    private static final String EXTERNAL_STORAGE = "external_storage";
    private String image_path;
    private String mParam2;
    private GPUImageView mGPUImageView;
    private LinearLayout filters, llTools;
    private TextView tvFilters, tvTools;
    private RelativeLayout rlSeekBar;
    private LinearLayout llCancelDone, llCancel, llDone, llFiltersTools;
    private Activity parent_Activity;
    private boolean is_rotational = false;
    private ArrayList<Filter_item> mFilterData = new ArrayList<>();
    private Filter_Adapter mAdapter;
    private GPUImageView dummyIv;
    private Dialog pDialog;
    private int count = 0;
    private int density_100;
    private File temp;
    private int min2 = 0, max2 = 100;
    private int min1 = -100, max1 = 100;

    private boolean briSel = false, conSel = false, shadSel = false, vigSel = false, sharSel = false, satSel = false;
    private Animation anim1, anim2;
    private boolean filterApplied = false;
    private boolean firstTime = true;
    private LinearLayout ll_filters, ll_tools;

    private boolean cancelShowing = false;
    private RelativeLayout title, topView;
    private TextView toolName;
    private FilterTools.FilterAdjuster mFilterAdjuster;
    private ArrayList<Integer> excludedIds = new ArrayList<>();


    private int filterId = -1;

    public Image_editor_fag_1() {
    }


    public static Image_editor_fag_1 newInstance(String param1, String param2) {
        Image_editor_fag_1 fragment = new Image_editor_fag_1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private int[] filterString = {R.string.text_filter_in1977, R.string.text_filter_amaro,
            R.string.text_filter_brannan, R.string.text_filter_early_bird, R.string.text_filter_hefe, R.string.text_filter_hudson,
            R.string.text_filter_inkwell, R.string.text_filter_lomofi, R.string.text_filter_lord_kelvin,
            R.string.text_filter_early_bird, R.string.text_filter_rise, R.string.text_filter_sierra,
            R.string.text_filter_sutro, R.string.text_filter_toaster, R.string.text_filter_valencia,
            R.string.text_filter_walden, R.string.text_filter_xproii};



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent_Activity = getActivity();
        if (getArguments() != null) {
            image_path = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_editor_fag_1, container, false);
        init_xml_content(view);
        initialization_data();

        if (firstTime) {
            firstTime = false;
            
            generatePreviewImages(new File(getResources().getString(R.string.app_name) +"/Filters").getPath());

        }
        return view;
    }

    private void init_xml_content(View view) {
        view.findViewById(R.id.image_editor_parent).setOnClickListener(this);

        dummyIv = (GPUImageView) view.findViewById(R.id.dummy);
        llFiltersTools = (LinearLayout) view.findViewById(R.id.ll_filters_tools_filter_image);
        llCancelDone = (LinearLayout) view.findViewById(R.id.ll_cancel_done_filter_image);

        filters = (LinearLayout) view.findViewById(R.id.ll_filters_filter_image);
        llTools = (LinearLayout) view.findViewById(R.id.ll_tools_filter_image);


        ll_filters = (LinearLayout) view.findViewById(R.id.ll_filters);
        ll_tools = (LinearLayout) view.findViewById(R.id.ll_tools);

        title = (RelativeLayout) view.findViewById(R.id.title);

        topView = (RelativeLayout) view.findViewById(R.id.topview_filter_image);
        ll_tools.setVisibility(View.GONE);


        tvFilters = (TextView) view.findViewById(R.id.tv_filters_filter_image);
        tvTools = (TextView) view.findViewById(R.id.tv_tools_filter_image);

        llCancel = (LinearLayout) view.findViewById(R.id.ll_cancel_filter_image);
        llDone = (LinearLayout) view.findViewById(R.id.ll_done_filter_image);
        toolName = (TextView) view.findViewById(R.id.toolName);
        llDone.setOnClickListener(this);
        llCancel.setOnClickListener(this);
        TextView tv_cancel = (TextView) view.findViewById(R.id.tv_cancel_filter_image);

        TextView tv_done = (TextView) view.findViewById(R.id.tv_done_filter_image);

        rlSeekBar = (RelativeLayout) view.findViewById(R.id.rl_seekbar_filter_image);

        mGPUImageView = (GPUImageView) view.findViewById(R.id.gpuimage_filter_image);
        RelativeLayout back_button_img = (RelativeLayout) view.findViewById(R.id.back_button_img);
        back_button_img.setOnClickListener(this);

        RelativeLayout next_button = (RelativeLayout) view.findViewById(R.id.next_button);
        next_button.setOnClickListener(this);
        RecyclerView filtered_item_list = (RecyclerView) view.findViewById(R.id.rv_all_filters_filter_image);
        filtered_item_list.setLayoutManager(new LinearLayoutManager(parent_Activity, LinearLayoutManager.HORIZONTAL, false));
        mAdapter = new Filter_Adapter(getActivity(), mFilterData);
        filtered_item_list.setAdapter(mAdapter);


        RecyclerView edit_item_list = (RecyclerView) view.findViewById(R.id.rv_all_tools_tool_image);
        edit_item_list.setLayoutManager(new LinearLayoutManager(parent_Activity, LinearLayoutManager.HORIZONTAL, false));

        count = 0;
        density_100 = (int) ((getResources().getDisplayMetrics().density) * (90));


        anim1 = new ScaleAnimation(
                1f, 0.85f,
                1f, 0.85f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim1.setFillAfter(true);
        anim1.setDuration(100);

        anim2 = new ScaleAnimation(
                0.85f, 1f,
                0.85f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        anim2.setFillAfter(true);
        anim2.setDuration(100);


        filtered_item_list.
                addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), filtered_item_list, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View view, final int position) {


                        view.startAnimation(anim1);


                        anim1.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                                view.startAnimation(anim2);


                            }
                        });


                        anim2.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {


                                int itemClickedId = mFilterData.get(position).getFilterId();



                                if (itemClickedId > 0 && itemClickedId < 18) {

                                    filterApplied = true;

                                    filterId = itemClickedId;
                                    switchFilterTo(GenerateThumbnails
                                            .createFilterForType(getActivity(),
                                                    GenerateThumbnails.FilterType.values()[itemClickedId]), true);


                                    Filter_item item;


                                    for (int i = 0; i < mFilterData.size(); i++) {


                                        item = mFilterData.get(i);
                                        item.setSelected(false);
                                        mFilterData.set(i, item);
                                    }


                                    item = mFilterData.get(position);
                                    item.setSelected(true);
                                    mFilterData.set(position, item);


                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.notifyDataSetChanged();

                                        }
                                    });


                                } else if (itemClickedId == 0) {


                                    filterId = -1;

                                    filterApplied = false;
                                    if (temp != null) {


                                        mGPUImageView.setFilter(new GPUImageFilter());

                                        mGPUImageView.requestRender();
                                    }
                                    Filter_item item;


                                    for (int i = 0; i < mFilterData.size(); i++) {


                                        item = mFilterData.get(i);
                                        item.setSelected(false);
                                        mFilterData.set(i, item);
                                    }


                                    item = mFilterData.get(0);
                                    item.setSelected(true);
                                    mFilterData.set(0, item);


                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.notifyDataSetChanged();

                                        }
                                    });

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mAdapter.notifyDataSetChanged();

                                        }
                                    });
                                }


                            }
                        });

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));


        edit_item_list.
                addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), edit_item_list, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(final View view, final int position) {


                        view.startAnimation(anim1);


                        anim1.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {

                                view.startAnimation(anim2);


                            }
                        });
                        anim2.setAnimationListener(new Animation.AnimationListener() {

                            @Override
                            public void onAnimationStart(Animation animation) {
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {
                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {


                                if (position >= 0) {

                                    switch (position) {


                                        case 0:


                                            briSel = true;
                                            conSel = false;

                                            satSel = false;
                                            shadSel = false;

                                            vigSel = false;

                                            sharSel = false;
                                            break;
                                        case 1:


                                            briSel = false;
                                            conSel = true;

                                            satSel = false;
                                            shadSel = false;

                                            vigSel = false;

                                            sharSel = false;
                                            break;
                                        case 2:
                                            briSel = false;
                                            conSel = false;

                                            satSel = true;
                                            shadSel = false;

                                            vigSel = false;

                                            sharSel = false;

                                            break;


                                        case 3:
                                            briSel = false;
                                            conSel = false;

                                            satSel = false;
                                            shadSel = true;

                                            vigSel = false;

                                            sharSel = false;

                                            break;

                                        case 4:
                                            briSel = false;
                                            conSel = false;

                                            satSel = false;
                                            shadSel = false;

                                            vigSel = true;

                                            sharSel = false;

                                            break;


                                        case 5:
                                            briSel = false;
                                            conSel = false;

                                            satSel = false;
                                            shadSel = false;

                                            vigSel = false;

                                            sharSel = true;


                                    }
                                    customEditFilter(FilterTools
                                            .createFilterForType(getActivity(),
                                                    FilterTools.FilterType.values()[position]));


                                    rlSeekBar.setVisibility(View.VISIBLE);


                                    ll_tools.setVisibility(View.GONE);


                                    if (ll_filters.getVisibility() == View.VISIBLE) {


                                        ll_filters.setVisibility(View.GONE);
                                    }


                                    llCancelDone.setVisibility(View.VISIBLE);


                                    llFiltersTools.setVisibility(View.GONE);

                                    cancelShowing = true;
                                    topView.setVisibility(View.GONE);


                                    title.setVisibility(View.VISIBLE);


                                }
                            }
                        });
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
    }


    private void initialization_data() {
        if (image_path != null) {
            temp = new File(image_path);

            mGPUImageView.setImage(temp);
            mGPUImageView.requestRender();
            dummyIv.setImage(temp);
        }

        filters.setOnClickListener(this);
        llTools.setOnClickListener(this);
    }


    private void discard_Image() {
        File temp = new File(image_path);
        if (temp.exists()) {
            boolean deleted = temp.delete();
        }
    }


    private void save_Image() {
        if (filterApplied) {
            ProgressDialog pDialog = new ProgressDialog(getActivity());


            pDialog.setCancelable(false);
            pDialog.show();

            ProgressBar bar = (ProgressBar) pDialog.findViewById(android.R.id.progress);


 bar.getIndeterminateDrawable().setColorFilter(
                    ContextCompat.getColor(getActivity(), R.color.calls_color),
                    android.graphics.PorterDuff.Mode.SRC_IN);



            try {


                Bitmap bitmap = mGPUImageView.capture();


                try {
                    FileOutputStream out = new FileOutputStream(image_path);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }


            } catch (InterruptedException e) {
                e.printStackTrace();
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            } catch (Exception e) {


                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }
        }
    }




    private void generatePreviewImages(String folderPath) {



        generateThumbnails(folderPath);


    }


    private void switchFilterTo(final GPUImageFilter mFilter, boolean flag) {


        if (flag) {
            mGPUImageView.setFilter(mFilter);
            mGPUImageView.requestRender();


        } else {

            dummyIv.setFilter(mFilter);
            dummyIv.requestRender();

        }

    }


    private void customEditFilter(final GPUImageFilter mFilter) {


        GPUImageFilterGroup group = new GPUImageFilterGroup();


        group.addFilter(mGPUImageView.getFilter());
        group.addFilter(mFilter);

        mGPUImageView.setFilter(group);


        mFilterAdjuster = new FilterTools.FilterAdjuster(mFilter);
        mGPUImageView.requestRender();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    private void generateThumbnails(final String folderPath) {



        if (count > 16) {
            dummyIv.setVisibility(View.GONE);


            Filter_item item = new Filter_item();

            item.setFilterId(0);
            item.setSelected(true);

            item.setItemType("0");


            item.setImageUrl(image_path);
            item.setFilterName(getString(R.string.text_filter_normal));

            mFilterData.add(0, item);

            item = new Filter_item();

            item.setFilterId(18);
            item.setSelected(false);

            item.setItemType("1");


            item.setFilterName("Manage");
            mFilterData.add(item);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();

                    if (pDialog != null && pDialog.isShowing()) {

                        pDialog.dismiss();
                    }
                }
            });


            return;
        }
        switchFilterTo(GenerateThumbnails
                .createFilterForType(getActivity(),
                        GenerateThumbnails.FilterType.values()[count + 1]), false);
        dummyIv.saveToPictures(folderPath, "filter_" + count + ".jpg", density_100, density_100, new GPUImageView.OnPictureSavedListener() {
            @Override
            public void onPictureSaved(Uri uri) {

                if (!excludedIds.contains(count + 1)) {
                    Filter_item item = new Filter_item();

                    item.setFilterId(count + 1);
                    item.setSelected(false);

                    item.setItemType("0");


                    item.setImageUrl(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + folderPath + "/filter_" + (count) + ".jpg");

                    item.setFilterName(getString(filterString[count]));

                    mFilterData.add(item);


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();


                        }
                    });
                }


                count++;

                generateThumbnails(folderPath);

            }
        });

    }


    private void hideCancelView() {


        try {
            if (cancelShowing) {


                llCancelDone.setVisibility(View.GONE);


                llFiltersTools.setVisibility(View.VISIBLE);


                ll_tools.setVisibility(View.VISIBLE);


                rlSeekBar.setVisibility(View.GONE);
            }

            cancelShowing = false;

            title.setVisibility(View.GONE);


            topView.setVisibility(View.VISIBLE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }



        briSel = false;
        conSel = false;

        satSel = false;
        shadSel = false;


        vigSel = false;

        sharSel = false;


    }

    @Override
    public void onResume() {
        super.onResume();

    }




    private void addUpdatedFiltersToList() {

        mFilterData.clear();

        for (int i = 0; i < 17; i++) {
            if (!excludedIds.contains(i + 1)) {
                Filter_item item = new Filter_item();

                item.setFilterId(i + 1);
                item.setSelected(false);
                item.setItemType("0");
                item.setImageUrl(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + getResources().getString(R.string.app_name) + "/Filters" + "/filter_" + (i) + ".jpg");
                item.setFilterName(getString(filterString[i]));
                mFilterData.add(item);
            }

        }

        Filter_item item = new Filter_item();
        item.setFilterId(0);
        item.setSelected(true);
        item.setItemType("0");
        item.setImageUrl(image_path);
        item.setFilterName(getString(R.string.text_filter_normal));
        mFilterData.add(0, item);
        item = new Filter_item();
        item.setFilterId(18);
        item.setSelected(false);
        item.setItemType("1");
        item.setFilterName("Manage");
        mFilterData.add(item);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}
*/
