package chat.hola.com.app.Wallpapers.Library;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.RecyclerItemClickListener;

/**
 * Created by moda on 10/10/17.
 */

public class LibraryActivity extends AppCompatActivity {


    private Bus bus = AppController.getBus();


    //    private SelectWallpaper_Adapter mAdapter;
    private ArrayList<Wallpaper_Library_item> mWallpaperData = new ArrayList<>();

    private RelativeLayout root;


    private String docId;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper_library);
        docId = getIntent().getExtras().getString("documentId");


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvGif);
        recyclerView.setHasFixedSize(true);


        SelectWallpaper_Adapter mAdapter = new SelectWallpaper_Adapter(this, mWallpaperData);


        root = (RelativeLayout) findViewById(R.id.root);


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        GridLayoutManager.SpanSizeLookup onSpanSizeLookup = new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        };

        mGridLayoutManager.setSpanSizeLookup(onSpanSizeLookup);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setAdapter(mAdapter);
        addWallpaperData();
        mAdapter.notifyDataSetChanged();


        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(LibraryActivity.this, recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int position) {
                        if (position >= 0) {
                            AppController.getInstance().getDbController().addWallpaper(docId, 4, mWallpaperData.get(position).getWallpaperUrl());


                            try {


                                JSONObject obj = new JSONObject();
                                obj.put("eventName", "WallpaperUpdated");

                                obj.put("type", 4);
                                obj.put("wallpaperDetails", mWallpaperData.get(position).getWallpaperUrl());


                                bus.post(obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if (root != null) {

                                Snackbar snackbar = Snackbar.make(root, getString(R.string.WallpaperUpdated), Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view2 = snackbar.getView();
                                TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    onBackPressed();
                                }
                            }, 500);
                        }
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {


                    }
                }));

        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        Typeface tf = AppController.getInstance().getRegularFont();

        TextView title = (TextView) findViewById(R.id.title);

        title.setTypeface(tf, Typeface.BOLD);
        bus.register(this);
    }

    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(LibraryActivity.this, ChatMessageScreen.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.putExtra("receiverUid", obj.getString("receiverUid"));
            intent.putExtra("receiverName", obj.getString("receiverName"));
            intent.putExtra("documentId", obj.getString("documentId"));
            intent.putExtra("isStar", obj.getBoolean("isStar"));
            intent.putExtra("receiverImage", obj.getString("receiverImage"));
            intent.putExtra("colorCode", obj.getString("colorCode"));

            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("callMinimized")) {

                minimizeCallScreen(object);
            }

        } catch (
                JSONException e)

        {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        bus.unregister(this);
    }

    @Override
    public void onBackPressed() {


        if (AppController.getInstance().isActiveOnACall()) {
            if (AppController.getInstance().isCallMinimized()) {
                super.onBackPressed();
                supportFinishAfterTransition();
            }
        } else {
            super.onBackPressed();
            supportFinishAfterTransition();
        }

    }


    private void addWallpaperData() {

        String[] urls = new String[]{"http://res.cloudinary.com/moda/image/upload/s--a_6BfnAl--/v1507712457/wall1_rjyeyl.png",
                "http://res.cloudinary.com/moda/image/upload/s--FbVtjg4E--/v1507712457/wall2_cnk3yu.png",
                "http://res.cloudinary.com/moda/image/upload/s--hdOVYJhE--/v1507712458/wall3_fjambk.png",
                "http://res.cloudinary.com/moda/image/upload/s--ZSaXNuQh--/v1507712457/wall4_yv8ywd.png",
                "http://res.cloudinary.com/moda/image/upload/s--Bx1v-Z3d--/v1507712458/wall5_yv8qr9.png",
                "http://res.cloudinary.com/moda/image/upload/s--jUkEtoJV--/v1507712458/wall6_r728an.png",
                "http://res.cloudinary.com/moda/image/upload/s--FaIXBh7L--/v1507712458/wall7_rif7z6.png",
                "http://res.cloudinary.com/moda/image/upload/s--N7dGJieO--/v1507712459/wall8_shzr6h.png",
                "http://res.cloudinary.com/moda/image/upload/s--s-yRZK3---/v1507712459/wall9_p7aqkn.png",
                "http://res.cloudinary.com/moda/image/upload/s--FAV-bkeN--/v1507712459/wall10_gq2isr.png",
                "http://res.cloudinary.com/moda/image/upload/s--a4o7Sg5A--/v1507712459/wall11_eqhtmr.png",
                "http://res.cloudinary.com/moda/image/upload/s--y_Ak81Ot--/v1507712460/wall12_ddlicy.png",
                "http://res.cloudinary.com/moda/image/upload/s--6nziKvVP--/v1507712459/wall13_bny4xv.png",
                "http://res.cloudinary.com/moda/image/upload/s--xN-LS8MZ--/v1507712459/wall14_ibffmd.png",
                "http://res.cloudinary.com/moda/image/upload/s--KRJbWCbk--/v1507712459/wall16_o3qyk9.png",
                "http://res.cloudinary.com/moda/image/upload/s--ZtjeQzOY--/v1507712460/wall18_pzy3bv.png",
                "http://res.cloudinary.com/moda/image/upload/s--U9T0-kQM--/v1507712459/wall19_egmres.png"
        };

        Wallpaper_Library_item wallpaperLibraryItem;
        for (int i = 0; i < urls.length; i++) {

            wallpaperLibraryItem = new Wallpaper_Library_item();

            wallpaperLibraryItem.setWallpaperUrl(urls[i]);

            mWallpaperData.add(wallpaperLibraryItem);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        docId = intent.getExtras().getString("documentId");
    }
}
