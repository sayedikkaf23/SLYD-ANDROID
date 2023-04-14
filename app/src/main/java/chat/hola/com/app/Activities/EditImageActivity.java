package chat.hola.com.app.Activities;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

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
import androidx.core.content.ContextCompat;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TouchImageView;
import chat.hola.com.app.stickerView.StickerImageView;
import chat.hola.com.app.stickerView.StickerTextView;

public class EditImageActivity extends AppCompatActivity implements View.OnClickListener {

    private Activity mActivity;
    private TouchImageView imgDisplay;
    private FrameLayout flCanvasView;
    private Bus bus = AppController.getBus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        mActivity = EditImageActivity.this;

        initializeViews();

        bus.register(this);
    }

    private void initializeViews() {


        imgDisplay = (TouchImageView) findViewById(R.id.imgDisplay);
        flCanvasView = (FrameLayout) findViewById(R.id.fl_canvas_view);

        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(this);


        ImageView addStickers = (ImageView) findViewById(R.id.iv_add_sticker);
        addStickers.setOnClickListener(this);

        ImageView drawDoodle = (ImageView) findViewById(R.id.iv_draw_doodle);
        drawDoodle.setOnClickListener(this);

        TextView addText = (TextView) findViewById(R.id.tv_add_text);
        addText.setOnClickListener(this);


        setupActivity(getIntent());

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

                Glide.with(mActivity)
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
                                imgDisplay.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.color_black));
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
            Intent intent = new Intent(mActivity, ChatMessageScreen.class);

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
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_add_sticker:
                // add a stickerImage to canvas
                StickerImageView iv_sticker = new StickerImageView(mActivity);
                iv_sticker.setImageDrawable(getResources().getDrawable(R.drawable.c10));
                flCanvasView.addView(iv_sticker);
                break;

            case R.id.iv_draw_doodle:

                break;

            case R.id.tv_add_text:
// add a stickerText to canvas
                StickerTextView tv_sticker = new StickerTextView(mActivity);
                tv_sticker.setText(getString(R.string.text_howdoo));
                flCanvasView.addView(tv_sticker);
                break;

            case R.id.close:
                onBackPressed();
                break;


        }

    }
}
