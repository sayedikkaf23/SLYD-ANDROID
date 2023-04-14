package chat.hola.com.app.Wallpapers.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
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

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;

/**
 * Created by moda on 10/10/17.
 */

public class SolidColorActivity extends AppCompatActivity {


    private Bus bus = AppController.getBus();


    private String docId;

    private RelativeLayout root;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper_solids);

        root = (RelativeLayout) findViewById(R.id.root);

        setupActivity(getIntent());

        View solid1, solid2, solid3, solid4, solid5, solid6, solid7, solid8, solid9, solid10, solid11, solid12, solid13, solid14, solid15, solid16, solid17, solid18, solid19, solid20, solid21;

        solid1 = findViewById(R.id.solid1);
        solid2 = findViewById(R.id.solid2);
        solid3 = findViewById(R.id.solid3);
        solid4 = findViewById(R.id.solid4);
        solid5 = findViewById(R.id.solid5);
        solid6 = findViewById(R.id.solid6);
        solid7 = findViewById(R.id.solid7);
        solid8 = findViewById(R.id.solid8);
        solid9 = findViewById(R.id.solid9);
        solid10 = findViewById(R.id.solid10);
        solid11 = findViewById(R.id.solid11);
        solid12 = findViewById(R.id.solid12);


        solid13 = findViewById(R.id.solid13);
        solid14 = findViewById(R.id.solid14);
        solid15 = findViewById(R.id.solid15);
        solid16 = findViewById(R.id.solid16);
        solid17 = findViewById(R.id.solid17);
        solid18 = findViewById(R.id.solid18);
        solid19 = findViewById(R.id.solid19);
        solid20 = findViewById(R.id.solid20);
        solid21 = findViewById(R.id.solid21);


        solid1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateWallpaper(1);
            }
        });

        solid2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(2);
            }
        });

        solid3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(3);
            }
        });

        solid4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(4);
            }
        });

        solid5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(5);
            }
        });

        solid6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(6);
            }
        });

        solid7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(7);
            }
        });

        solid8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(8);
            }
        });

        solid9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(9);
            }
        });

        solid10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(10);
            }
        });

        solid11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(11);
            }
        });

        solid12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(12);
            }
        });

        solid13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(13);
            }
        });

        solid14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(14);
            }
        });

        solid15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(15);
            }
        });

        solid16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(16);
            }
        });

        solid17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(17);
            }
        });

        solid18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(18);
            }
        });

        solid19.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(19);
            }
        });

        solid20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(20);
            }
        });

        solid21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateWallpaper(21);
            }
        });


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
            Intent intent = new Intent(SolidColorActivity.this, ChatMessageScreen.class);

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


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setupActivity(intent);
    }

    private void setupActivity(Intent intent) {

        Bundle extras = intent.getExtras();


        docId = extras.getString("documentId");


    }


    /**
     * To update the wallpaper as the solid color wallpaper
     */
    private void updateWallpaper(int position) {

        String[] colorCodes = new String[]{"#E57373", "#FF4081", "#BA68C8", "#9575CD", "#7986CB", "#64B5F6",
                "#B3E5FC", "#26C6DA", "#4DB6AC", "#81C784", "#7CB342", "#76FF03", "#FFEB3B", "#FFA000", "#e5c999", "#FF9E80", "#00897B", "#795548",
                "#BDBDBD", "#607D8B", "#000000"};


        AppController.getInstance().getDbController().addWallpaper(docId, 0, colorCodes[position - 1]);


        try {


            JSONObject obj = new JSONObject();
            obj.put("eventName", "WallpaperUpdated");

            obj.put("type", 0);
            obj.put("wallpaperDetails", colorCodes[position - 1]);


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
