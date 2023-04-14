package chat.hola.com.app.Fragments;
/*
 * Created by moda on 14/04/16.
 */

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import chat.hola.com.app.Activities.MediaHistory;
import chat.hola.com.app.Activities.MediaHistory_FullScreenVideo;
import chat.hola.com.app.Adapters.MediaHistory_Adapter;
import chat.hola.com.app.AppController;
import chat.hola.com.app.ModelClasses.Media_History_Item;
import com.ezcall.android.R;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;
import chat.hola.com.app.Utilities.Utilities;


/*
* Fragment containing the list of the media(image or video) received
*
* */
public class MediaHistory_Received extends Fragment {


    private View view;


    private RecyclerView recyclerView;

    private ArrayList<Media_History_Item> mMessageData = new ArrayList<>();

    private MediaHistory_Adapter mAdapter;

    private CoordinatorLayout root;
    private boolean list_visible = false;


    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager llm;


    public MediaHistory_Received() {
        /* Required empty public constructor*/


        mMessageData = new ArrayList<>();


    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        /* Inflate the layout for this fragment*/

        if (view == null) {

            view = inflater.inflate(R.layout.switch_product_layout, container, false);
        } else {

            if (view.getParent() != null)
                ((ViewGroup) view.getParent()).removeView(view);
        }


        root = (CoordinatorLayout) view.findViewById(R.id.root);
        mGridLayoutManager = new GridLayoutManager(view.getContext(), 2);


        llm = new LinearLayoutManager(view.getContext());


        final TextView stickyHeader = (TextView) view.findViewById(R.id.textView2);
        stickyHeader.setText(getString(R.string.string_283));

        RelativeLayout toolbar = (RelativeLayout) view.findViewById(R.id.toolbar);


        toolbar.setVisibility(View.GONE);


//        RelativeLayout refine = (RelativeLayout) view.findViewById(R.id.refine);
//
//
//        refine.setVisibility(View.GONE);
        RelativeLayout overlay = (RelativeLayout) view.findViewById(R.id.overlay);


        overlay.setVisibility(View.GONE);

        Typeface tf = AppController.getInstance().getRegularFont();
        TextView count = (TextView) view.findViewById(R.id.textView);
        stickyHeader.setTypeface(tf, Typeface.NORMAL);
        count.setTypeface(tf, Typeface.BOLD);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


                if (dy != 0) {


                    try {
                        if (list_visible) {


                            View view = recyclerView.getChildAt(0);


                            Media_History_Item p = mMessageData.get(recyclerView.getChildAdapterPosition(view));
                            stickyHeader.setText(Utilities.formatDate(Utilities.tsFromGmt(p.getTS())).substring(9, 24));


                        } else {


                            View view = recyclerView.getChildAt(0);

                            int pos = recyclerView.getChildAdapterPosition(view);
                            Media_History_Item p = mMessageData.get(pos);
                            if (pos == mMessageData.size() - 1) {
                                stickyHeader.setText(Utilities.formatDate(Utilities.tsFromGmt(p.getTS())).substring(9, 24));

                            } else {

                                try {
                                    stickyHeader.setText(Utilities.formatDate(Utilities.tsFromGmt(p.getTS())).substring(9, 24) + " " + getString(R.string.string_204) + " " + Utilities.formatDate(Utilities.tsFromGmt(mMessageData.get(pos + 1).getTS())).substring(9, 24));

                                } catch (IllegalStateException e) {
                                    stickyHeader.setText(Utilities.formatDate(Utilities.tsFromGmt(p.getTS())).substring(9, 24) + " AND " + Utilities.formatDate(Utilities.tsFromGmt(mMessageData.get(pos + 1).getTS())).substring(9, 24));


                                }

                            }


                        }

                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
                }

            }
        });


        mAdapter = new MediaHistory_Adapter(view.getContext(), mMessageData);


        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), LinearLayoutManager.VERTICAL));


        recyclerView.setAdapter(mAdapter);


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(view.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {


                final Media_History_Item item = mMessageData.get(position);

                if (item.getMessageType().equals("2")) {


                    if (item.getDownloadStatus() == 1) {

                        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
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

                                    startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle());

                                } catch (ActivityNotFoundException e) {
                                    Intent i = new Intent(getActivity(), MediaHistory_FullScreenVideo.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    i.putExtra("videoPath", item.getVideoPath());
                                    startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle());


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


        final ImageView grid = (ImageView) view.findViewById(R.id.imageView1);
        grid.setImageResource(R.drawable.home_grid_view_grid_icon_selector);


        final ImageView list = (ImageView) view.findViewById(R.id.imageView2);
        list.setImageResource(R.drawable.home_grid_view_menu_icon_unselector);

        grid.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                list_visible = false;


                stickyHeader.setText(getString(R.string.string_283));
                list.setImageResource(R.drawable.home_grid_view_menu_icon_unselector);


                grid.setImageResource(R.drawable.home_grid_view_grid_icon_selector);


                recyclerView.setLayoutManager(mGridLayoutManager);


            }
        });

        list.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {
                stickyHeader.setText(getString(R.string.string_283));
                list_visible = true;
                list.setImageResource(R.drawable.home_grid_view_menu_icon_selector);


                grid.setImageResource(R.drawable.home_grid_view_grid_icon_unselector);


                recyclerView.setLayoutManager(llm);

            }
        });


        addImageMedia();


        if (mMessageData.size() == 0) {


            TextView notProducts = (TextView) view.findViewById(R.id.notLoggedIn);


            notProducts.setVisibility(View.VISIBLE);
            notProducts.setTypeface(tf, Typeface.NORMAL);

            notProducts.setText(getString(R.string.string_282));

        }


        count.setText((mMessageData.size() + getString(R.string.double_inverted_comma)).trim());
        return view;
    }


    public void addImageMedia() {

        String docId = ((MediaHistory) this.getActivity()).getDocId();
        ArrayList<Map<String, Object>> arrMessage = AppController.getInstance().getDbController().retrieveAllMessages(docId);

        Map<String, Object> mapMessage;


        Media_History_Item item;


        for (int i = arrMessage.size() - 1; i >= 0; i--) {


            mapMessage = (arrMessage.get(i));


            if (!((boolean) mapMessage.get("isSelf"))) {


                if (mapMessage.get("messageType").equals("1"))

                {
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

                    mMessageData.add(item);
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyItemInserted(mMessageData.size() - 1);
                            }
                        });
                    }

                } else if (mapMessage.get("messageType").equals("2")) {


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

                    mMessageData.add(item);

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyItemInserted(mMessageData.size() - 1);
                            }
                        });

                    }

                }


            }


        }
    }


}
