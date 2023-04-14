package chat.hola.com.app.Activities;

/*
 * Created by moda on 15/04/16.
 */


import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import chat.hola.com.app.AppController;


/**
 * Activity containing the full screen videoview to play video incase android video player is not found
 */
public class MediaHistory_FullScreenVideo extends AppCompatActivity {

    private VideoView video;
    private Bus bus = AppController.getBus();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        supportRequestWindowFeature(AppCompatDelegate.FEATURE_SUPPORT_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_fullscreen_video);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));

        video = (VideoView) findViewById(R.id.video);
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


    private void setupActivity(Intent intent) {

        Bundle extras = intent.getExtras();
        if (extras != null) {

            String path = extras.getString("videoPath");
            try {
                if (extras.containsKey("flag")) {


                    video.setVideoURI(Uri.parse(path));


                } else {

                    video.setVideoPath(path);
                }

            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }


            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(video);

            video.setMediaController(mediaController);


//            video.seekTo(2);
            video.start();


            video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    onBackPressed();
                }
            });
        }
    }

    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(MediaHistory_FullScreenVideo.this, ChatMessageScreen.class);

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
}
