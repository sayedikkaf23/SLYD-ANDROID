package chat.hola.com.app.Activities;


/*
 * Created by moda on 15/04/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TouchImageView;


/*
 *
 * Activity containing the full screen imageview with functionality to pinch and zoom
 *
 * */
public class MediaHistory_FullScreenImage extends AppCompatActivity {

    private TouchImageView imgDisplay;
    private Bus bus = AppController.getBus();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_fullscreen_image);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        imgDisplay = (TouchImageView) findViewById(R.id.imgDisplay);
        setupActivity(getIntent());
        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                onBackPressed();

            }
        });
        bus.register(this);
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

    @SuppressWarnings("TryWithIdenticalCatches")
    private void setupActivity(Intent intent) {


        Bundle extras = intent.getExtras();
        if (extras != null) {

            String path = extras.getString("imagePath");

            try {


                Glide
                        .with(MediaHistory_FullScreenImage.this)
                        .load(path)


                        .crossFade()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                imgDisplay.setBackgroundColor(ContextCompat.getColor(MediaHistory_FullScreenImage.this, R.color.color_black));
                                return false;
                            }
                        })
                        .into(imgDisplay);


            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }


    }

    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(MediaHistory_FullScreenImage.this, ChatMessageScreen.class);

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
                JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bus.unregister(this);
    }
}
