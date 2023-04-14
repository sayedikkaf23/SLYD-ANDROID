package chat.hola.com.app.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.ezcall.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import chat.hola.com.app.AppController;
import chat.hola.com.app.BlurTransformation.BlurTransformation;
import chat.hola.com.app.DocumentPicker.FilePickerConst;
import chat.hola.com.app.Utilities.AdjustableImageView;
import chat.hola.com.app.Utilities.MqttEvents;
import chat.hola.com.app.Utilities.Utilities;

import static chat.hola.com.app.Utilities.Utilities.convert24to12hourformat;

/**
 * Created by moda on 04/09/17.
 */

public class MessageInfo extends AppCompatActivity implements OnMapReadyCallback {

    private Bus bus = AppController.getBus();


    private TextView deliveredAt, readAt;
    private RelativeLayout message_rl, image_rl, video_rl, location_rl, contact_rl, audio_rl, sticker_rl, doodle_rl, gif_rl, document_rl, post_rl;


    private Typeface tf = AppController.getInstance().getRegularFont();
    private GoogleMap mMap;

    private LatLng positionSelected;


    private ImageView singleTick, doubleTick, blueTick, clock, chatBackground;

    private String messageId, documentId;

    private RelativeLayout root;


    private TextView messageDate, messageTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_info);
        //Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler(this));
        final ImageView backButton = (ImageView) findViewById(R.id.close);

        chatBackground = (ImageView) findViewById(R.id.chatBackground);
        root = (RelativeLayout) findViewById(R.id.root);


        deliveredAt = (TextView) findViewById(R.id.deliveredAt);
        readAt = (TextView) findViewById(R.id.readAt);


        message_rl = (RelativeLayout) findViewById(R.id.message_rl);
        image_rl = (RelativeLayout) findViewById(R.id.image_rl);
        video_rl = (RelativeLayout) findViewById(R.id.video_rl);
        location_rl = (RelativeLayout) findViewById(R.id.location_rl);
        contact_rl = (RelativeLayout) findViewById(R.id.contact_rl);
        audio_rl = (RelativeLayout) findViewById(R.id.audio_rl);
        sticker_rl = (RelativeLayout) findViewById(R.id.sticker_rl);
        doodle_rl = (RelativeLayout) findViewById(R.id.doodle_rl);
        gif_rl = (RelativeLayout) findViewById(R.id.gif_rl);
        document_rl = (RelativeLayout) findViewById(R.id.document_rl);
        post_rl = (RelativeLayout) findViewById(R.id.post_rl);


        setupActivity(getIntent());


//        TextView title = (TextView) findViewById(R.id.title);


        tf = AppController.getInstance().getRegularFont();


//        title.setText(getString(R.string.MessageInfo));
//        title.setTypeface(tf, Typeface.BOLD);

        TextView delivered = (TextView) findViewById(R.id.delivered);
        TextView read = (TextView) findViewById(R.id.read);

        delivered.setTypeface(tf, Typeface.NORMAL);
        deliveredAt.setTypeface(tf, Typeface.NORMAL);

        read.setTypeface(tf, Typeface.NORMAL);
        readAt.setTypeface(tf, Typeface.NORMAL);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        bus.register(this);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_container);
        collapsingToolbarLayout.setTitleEnabled(false);


        collapsingToolbarLayout.setExpandedTitleColor(ContextCompat.getColor(MessageInfo.this, R.color.transparent));
        collapsingToolbarLayout.setCollapsedTitleTextColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));
        collapsingToolbarLayout.setTitle(getString(R.string.MessageInfo));


    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setupActivity(intent);
    }


    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(MessageInfo.this, ChatMessageScreen.class);

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


    @Subscribe
    public void getMessage(JSONObject object) {
        try {
            if (object.getString("eventName").equals("callMinimized")) {

                minimizeCallScreen(object);
            } else if (object.getString("eventName").equals(MqttEvents.Acknowledgement.value + "/" + AppController.getInstance().getUserId())) {

                /*
                 * If for the current message i have received the delivery acknowledgement
                 */

                if (object.getString("msgId").equals(messageId)) {

                    /*
                     * For type 2 or type 3
                     */


                    int status = Integer.parseInt(object.getString("status"));
                    updateMessageDeliveryStatus(status);


                    if (status == 2) {


                        updateDeliveryTime(object.getString("deliveryTime"));
                    } else if (status == 3) {
                        updateReadTime(object.getString("readTime"));
                    }


                }


            } else if (object.getString("eventName").equals(MqttEvents.MessageResponse.value)) {


                if (object.getString("messageId").equals(messageId) && object.getString("docId").equals(documentId)) {

                    updateMessageDeliveryStatus(1);


                }


            }


        } catch (JSONException e)

        {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void setupActivity(Intent intent) {
        message_rl.setVisibility(View.GONE);
        image_rl.setVisibility(View.GONE);
        video_rl.setVisibility(View.GONE);
        location_rl.setVisibility(View.GONE);
        contact_rl.setVisibility(View.GONE);
        audio_rl.setVisibility(View.GONE);
        sticker_rl.setVisibility(View.GONE);
        doodle_rl.setVisibility(View.GONE);
        gif_rl.setVisibility(View.GONE);
        document_rl.setVisibility(View.GONE);
        if (post_rl != null)
            post_rl.setVisibility(View.GONE);


        Bundle extras = intent.getExtras();

        if (extras != null) {
            documentId = extras.getString("documentId");
            //new getWallpaperDetails().execute();
            messageId = extras.getString("messageId");


            Map<String, Object> map = AppController.getInstance()
                    .getDbController().getMessageDetails(documentId, messageId);

            if (map != null) {
                int deliveryStatus = Integer.parseInt((String) map.get("deliveryStatus"));


                String messageContent = (String) map.get("message");

                switch (Integer.parseInt(extras.getString("messageType"))) {


                    case 0: {
                        /*
                         * Text
                         */

                        message_rl.setVisibility(View.VISIBLE);


                        TextView message = (TextView) findViewById(R.id.txtMsg);

                        message.setText(messageContent);


                        messageTime = (TextView) findViewById(R.id.ts1);

                        messageDate = (TextView) findViewById(R.id.date1);


                        singleTick = (ImageView) findViewById(R.id.single_tick_green1);

                        doubleTick = (ImageView) findViewById(R.id.double_tick_green1);

                        blueTick = (ImageView) findViewById(R.id.double_tick_blue1);

                        clock = (ImageView) findViewById(R.id.clock1);


                        messageTime.setTypeface(tf, Typeface.ITALIC);

                        messageDate.setTypeface(tf, Typeface.ITALIC);

                        break;

                    }
                    case 1: {
                        /*
                         * Image
                         */
                        image_rl.setVisibility(View.VISIBLE);


                        final AdjustableImageView imageView = (AdjustableImageView) findViewById(R.id.imgshow);

                        ImageView download2 = (ImageView) findViewById(R.id.download);

                        int density = (int) getResources().getDisplayMetrics().density;

                        TextView fnf = (TextView) findViewById(R.id.fnf);
                        int downloadStatus = (int) map.get("downloadStatus");
                        if (downloadStatus == 1) {


                            /*
                             * Already downloaded
                             */

                            download2.setVisibility(View.GONE);

                            if (messageContent != null) {


                                try {


                                    if (ActivityCompat.checkSelfPermission(MessageInfo.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                            == PackageManager.PERMISSION_GRANTED) {


                                        final BitmapFactory.Options options = new BitmapFactory.Options();
                                        options.inJustDecodeBounds = true;
                                        BitmapFactory.decodeFile(messageContent, options);


                                        int height = options.outHeight;
                                        int width = options.outWidth;


                                        int reqHeight;


                                        if (width == 0) {
                                            reqHeight = 150;
                                        } else {


                                            reqHeight = ((150 * height) / width);


                                            if (reqHeight > 150) {
                                                reqHeight = 150;
                                            }
                                        }

                                        try {
                                            Glide
                                                    .with(MessageInfo.this)
                                                    .load(messageContent)
                                                    .override((150 * density), (reqHeight * density))
                                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                                .crossFade()
                                                    .centerCrop()
                                                    .placeholder(R.drawable.home_grid_view_image_icon)


                                                    .listener(new RequestListener<String, GlideDrawable>() {
                                                        @Override
                                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                            return false;
                                                        }

                                                        @Override
                                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                            imageView.setBackgroundColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));


                                                            return false;
                                                        }
                                                    })
                                                    .into(imageView);

                                        } catch (IllegalArgumentException e) {
                                            e.printStackTrace();
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }


                                    } else {

                                        fnf.setVisibility(View.VISIBLE);

                                        fnf.setText(R.string.string_211);
                                        Glide.clear(imageView);
                                        imageView.setImageDrawable(ContextCompat.getDrawable(MessageInfo.this, R.drawable.chat_white_circle));
                                        imageView.setBackgroundColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));


                                    }


                                } catch (OutOfMemoryError e) {
                                    e.printStackTrace();
                                } catch (Exception e) {


                                    fnf.setVisibility(View.VISIBLE);
                                    Glide.clear(imageView);
                                    imageView.setImageDrawable(ContextCompat.getDrawable(MessageInfo.this, R.drawable.chat_white_circle));
                                    imageView.setBackgroundColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));


                                }
                            } else {


                                Glide.clear(imageView);
                                imageView.setImageDrawable(ContextCompat.getDrawable(MessageInfo.this, R.drawable.chat_white_circle));
                                imageView.setBackgroundColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));
                            }


                        } else {



                            /*
                             *
                             *To allow an option to download
                             *
                             */


                            download2.setVisibility(View.GONE);


                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;


                            BitmapFactory.decodeFile(messageContent, options);


                            int height = options.outHeight;
                            int width = options.outWidth;


                            int reqHeight;


                            if (width == 0) {
                                reqHeight = 150;
                            } else {


                                reqHeight = ((150 * height) / width);


                                if (reqHeight > 150) {
                                    reqHeight = 150;
                                }
                            }

                            try {
                                Glide
                                        .with(MessageInfo.this)
                                        .load((String) map.get("thumbnailPath"))


                                        .bitmapTransform(new CenterCrop(MessageInfo.this), new BlurTransformation(MessageInfo.this))


                                        .override((150 * density), (density * reqHeight))
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                        .placeholder(R.drawable.home_grid_view_image_icon)
                                        .listener(new RequestListener<String, GlideDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                imageView.setBackgroundColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));
                                                return false;
                                            }
                                        })


                                        .into(imageView);

                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }


                        }


                        messageTime = (TextView) findViewById(R.id.ts2);

                        messageDate = (TextView) findViewById(R.id.date2);


                        singleTick = (ImageView) findViewById(R.id.single_tick_green2);

                        doubleTick = (ImageView) findViewById(R.id.double_tick_green2);

                        blueTick = (ImageView) findViewById(R.id.double_tick_blue2);

                        clock = (ImageView) findViewById(R.id.clock2);


                        messageTime.setTypeface(tf, Typeface.ITALIC);

                        messageDate.setTypeface(tf, Typeface.ITALIC);
                        break;
                    }
                    case 2: {

                        /*
                         * Video
                         */
                        video_rl.setVisibility(View.VISIBLE);

                        final AdjustableImageView imageView = (AdjustableImageView) findViewById(R.id.vidshow);

                        ImageView download3 = (ImageView) findViewById(R.id.download2);


                        TextView fnf = (TextView) findViewById(R.id.fnf2);

                        int downloadStatus = (int) map.get("downloadStatus");
                        if (downloadStatus == 1) {
                            /*
                             *
                             * image already downloaded
                             *
                             * */
                            download3.setVisibility(View.GONE);

                            try {


                                if (ActivityCompat.checkSelfPermission(MessageInfo.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {


                                    final File file = new File(messageContent);


                                    if (file.exists()) {


                                        imageView.setImageBitmap(ThumbnailUtils.createVideoThumbnail(messageContent,
                                                MediaStore.Images.Thumbnails.MINI_KIND));


                                    } else {


                                        Glide.clear(imageView);
                                        fnf.setVisibility(View.VISIBLE);
                                        // vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                                        imageView.setImageDrawable(ContextCompat.getDrawable(MessageInfo.this, R.drawable.chat_white_circle));
                                        imageView.setBackgroundColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));

                                    }
                                } else {

                                    fnf.setVisibility(View.VISIBLE);
                                    Glide.clear(imageView);
                                    //   vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                                    fnf.setText(getString(R.string.string_211));
                                    imageView.setImageDrawable(ContextCompat.getDrawable(MessageInfo.this, R.drawable.chat_white_circle));
                                    imageView.setBackgroundColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));


                                }
                            } catch (OutOfMemoryError e) {
                                e.printStackTrace();
                            } catch (Exception e) {


                                e.printStackTrace();

                            }

                        } else {

                            download3.setVisibility(View.GONE);

                            try {
                                Glide
                                        .with(MessageInfo.this)
                                        .load((String) map.get("thumbnailPath"))
                                        .bitmapTransform(new CenterCrop(MessageInfo.this), new BlurTransformation(MessageInfo.this))
                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                        .placeholder(R.drawable.home_grid_view_image_icon)


                                        .listener(new RequestListener<String, GlideDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                imageView.setBackgroundColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));
                                                return false;
                                            }
                                        })
                                        .into(imageView);

                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }


                        }


                        messageTime = (TextView) findViewById(R.id.ts3);

                        messageDate = (TextView) findViewById(R.id.date3);


                        singleTick = (ImageView) findViewById(R.id.single_tick_green3);

                        doubleTick = (ImageView) findViewById(R.id.double_tick_green3);

                        blueTick = (ImageView) findViewById(R.id.double_tick_blue3);

                        clock = (ImageView) findViewById(R.id.clock3);


                        messageTime.setTypeface(tf, Typeface.ITALIC);

                        messageDate.setTypeface(tf, Typeface.ITALIC);


                        break;
                    }

                    case 3: {

                        /*
                         * Location
                         */
                        location_rl.setVisibility(View.VISIBLE);


                        MapView mapView = (MapView) findViewById(R.id.map);


                        String args[] = messageContent.split("@@");
                        String LatLng = args[0];

                        String[] parts = LatLng.split(",");

                        String lat = parts[0].substring(1);
                        String lng = parts[1].substring(0, parts[1].length() - 1);

                        parts = null;
                        args = null;
                        positionSelected = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                        mapView.getMapAsync(this);

                        messageTime = (TextView) findViewById(R.id.ts4);

                        messageDate = (TextView) findViewById(R.id.date4);


                        singleTick = (ImageView) findViewById(R.id.single_tick_green4);

                        doubleTick = (ImageView) findViewById(R.id.double_tick_green4);

                        blueTick = (ImageView) findViewById(R.id.double_tick_blue4);

                        clock = (ImageView) findViewById(R.id.clock4);


                        messageTime.setTypeface(tf, Typeface.ITALIC);

                        messageDate.setTypeface(tf, Typeface.ITALIC);


                        break;
                    }

                    case 4: {

                        /*
                         * contact
                         */

                        contact_rl.setVisibility(View.VISIBLE);


                        TextView contactName = (TextView) findViewById(R.id.contactName);

                        TextView contactNumber = (TextView) findViewById(R.id.contactNumber);


                        String contactNameS, contactNumberS;

                        try {


                            String parts[] = messageContent.split("@@");


                            contactNameS = parts[0];


                            String arr[] = parts[1].split("/");


                            contactNumberS = arr[0];
                            arr = null;
                            parts = null;

                            contactName.setText(contactNameS);

                            contactNumber.setText(contactNumberS);
                            if (contactNameS == null || contactNameS.isEmpty()) {
                                contactName.setText(getString(R.string.string_247));
                            } else if (contactNumberS == null || contactNumberS.isEmpty()) {
                                contactNumber.setText(getString(R.string.string_246));
                            }
                        } catch (StringIndexOutOfBoundsException e) {
                            contactNumber.setText(getString(R.string.string_246));
                        } catch (Exception e) {
                            contactNumber.setText(getString(R.string.string_246));
                        }


                        messageTime = (TextView) findViewById(R.id.ts);

                        messageDate = (TextView) findViewById(R.id.date);


//                    ImageView singleTick5 = (ImageView) findViewById(R.id.single_tick_green);
//
//                    ImageView doubleTickGreen5 = (ImageView) findViewById(R.id.double_tick_green);
//
//                    ImageView doubleTickBlue5 = (ImageView) findViewById(R.id.double_tick_blue);
//
//                    ImageView clock5 = (ImageView) findViewById(R.id.clock);


                        singleTick = (ImageView) findViewById(R.id.single_tick_green);

                        doubleTick = (ImageView) findViewById(R.id.double_tick_green);

                        blueTick = (ImageView) findViewById(R.id.double_tick_blue);

                        clock = (ImageView) findViewById(R.id.clock);


                        messageTime.setTypeface(tf, Typeface.ITALIC);

                        messageDate.setTypeface(tf, Typeface.ITALIC);


                        break;

                    }
                    case 5: {
                        /*
                         * Audio
                         */

                        audio_rl.setVisibility(View.VISIBLE);
                        messageTime = (TextView) findViewById(R.id.ts6);

                        messageDate = (TextView) findViewById(R.id.date6);


                        singleTick = (ImageView) findViewById(R.id.single_tick_green6);

                        doubleTick = (ImageView) findViewById(R.id.double_tick_green6);

                        blueTick = (ImageView) findViewById(R.id.double_tick_blue6);

                        clock = (ImageView) findViewById(R.id.clock6);


                        ImageView download6 = (ImageView) findViewById(R.id.download6);

                        int downloadStatus = (int) map.get("downloadStatus");
                        if (downloadStatus == 0) {

                            download6.setVisibility(View.VISIBLE);
                        } else {
                            download6.setVisibility(View.GONE);
                        }


                        messageTime.setTypeface(tf, Typeface.ITALIC);

                        messageDate.setTypeface(tf, Typeface.ITALIC);


                        break;
                    }

                    case 6: {
                        /*
                         * Sticker
                         */

                        sticker_rl.setVisibility(View.VISIBLE);

                        ImageView stickerImage = (ImageView) findViewById(R.id.imgshow2);


                        try {


                            Glide.with(MessageInfo.this)
                                    .load(messageContent)
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(stickerImage);

                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }


                        messageTime = (TextView) findViewById(R.id.ts7);

                        messageDate = (TextView) findViewById(R.id.date7);


                        singleTick = (ImageView) findViewById(R.id.single_tick_green7);

                        doubleTick = (ImageView) findViewById(R.id.double_tick_green7);

                        blueTick = (ImageView) findViewById(R.id.double_tick_blue7);

                        clock = (ImageView) findViewById(R.id.clock7);


                        messageTime.setTypeface(tf, Typeface.ITALIC);

                        messageDate.setTypeface(tf, Typeface.ITALIC);


                        break;
                    }
                    case 7: {
                        /*
                         * Doodle
                         */
                        doodle_rl.setVisibility(View.VISIBLE);


                        final ImageView doodleImage = (ImageView) findViewById(R.id.imgshow10);

                        ImageView download8 = (ImageView) findViewById(R.id.download10);

                        TextView fnf = (TextView) findViewById(R.id.fnf5);


                        int downloadStatus = (int) map.get("downloadStatus");

                        if (downloadStatus == 1) {


                            if (messageContent != null) {


                                try {

                                    download8.setVisibility(View.GONE);


                                    if (ActivityCompat.checkSelfPermission(MessageInfo.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                            == PackageManager.PERMISSION_GRANTED) {


                                        try {
                                            Glide
                                                    .with(MessageInfo.this)
                                                    .load(messageContent)

                                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                                .crossFade()
                                                    .centerCrop()
                                                    .placeholder(R.drawable.home_grid_view_image_icon)


                                                    .listener(new RequestListener<String, GlideDrawable>() {
                                                        @Override
                                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                            return false;
                                                        }

                                                        @Override
                                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                            doodleImage.setBackgroundColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));


                                                            return false;
                                                        }
                                                    })
                                                    .into(doodleImage);

                                        } catch (IllegalArgumentException e) {
                                            e.printStackTrace();
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }


                                    } else {

                                        fnf.setVisibility(View.VISIBLE);

                                        // vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                                        fnf.setText(getString(R.string.string_211));
                                        Glide.clear(doodleImage);
                                        doodleImage.setImageDrawable(ContextCompat.getDrawable(MessageInfo.this, R.drawable.chat_white_circle));
                                        doodleImage.setBackgroundColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));


                                    }


                                } catch (OutOfMemoryError e) {
                                    e.printStackTrace();
                                } catch (Exception e) {


                                    fnf.setVisibility(View.VISIBLE);
                                    Glide.clear(doodleImage);
                                    doodleImage.setImageDrawable(ContextCompat.getDrawable(MessageInfo.this, R.drawable.chat_white_circle));
                                    doodleImage.setBackgroundColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));
                                    //  vh2.fnf.setTypeface(tf, Typeface.NORMAL);


                                }
                            } else {
                                Glide.clear(doodleImage);
                                doodleImage.setImageDrawable(ContextCompat.getDrawable(MessageInfo.this, R.drawable.chat_white_circle));
                                doodleImage.setBackgroundColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));
                            }
                        } else {


                            download8.setVisibility(View.GONE);


                            try {
                                Glide
                                        .with(MessageInfo.this)
                                        .load((String) map.get("thumbnailPath"))


                                        .bitmapTransform(new CenterCrop(MessageInfo.this), new BlurTransformation(MessageInfo.this))


                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                        .placeholder(R.drawable.home_grid_view_image_icon)
                                        .listener(new RequestListener<String, GlideDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                doodleImage.setBackgroundColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));
                                                return false;
                                            }
                                        })


                                        .into(doodleImage);

                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }


                        }


                        messageTime = (TextView) findViewById(R.id.ts10);

                        messageDate = (TextView) findViewById(R.id.date10);


                        singleTick = (ImageView) findViewById(R.id.single_tick_green10);

                        doubleTick = (ImageView) findViewById(R.id.double_tick_green10);

                        blueTick = (ImageView) findViewById(R.id.double_tick_blue10);

                        clock = (ImageView) findViewById(R.id.clock10);


                        messageTime.setTypeface(tf, Typeface.ITALIC);

                        messageDate.setTypeface(tf, Typeface.ITALIC);


                        break;
                    }
                    case 8: {
                        /*
                         * Gif
                         */
                        gif_rl.setVisibility(View.VISIBLE);


                        ImageView stillGifImage = (ImageView) findViewById(R.id.stillGifImage);


                        try {


                            Glide.with(MessageInfo.this)
                                    .load(messageContent)
                                    .asBitmap()
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .placeholder(R.drawable.home_grid_view_image_icon)
                                    .into(stillGifImage);


                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();

                        }


                        messageTime = (TextView) findViewById(R.id.ts9);

                        messageDate = (TextView) findViewById(R.id.date9);


                        singleTick = (ImageView) findViewById(R.id.single_tick_green9);

                        doubleTick = (ImageView) findViewById(R.id.double_tick_green9);

                        blueTick = (ImageView) findViewById(R.id.double_tick_blue9);

                        clock = (ImageView) findViewById(R.id.clock9);


                        messageTime.setTypeface(tf, Typeface.ITALIC);

                        messageDate.setTypeface(tf, Typeface.ITALIC);

                        break;

                    }
                    case 9: {
                        /*
                         * Document
                         */
                        document_rl.setVisibility(View.VISIBLE);

                        ImageView fileImage = (ImageView) findViewById(R.id.fileImage);


                        TextView fileName = (TextView) findViewById(R.id.fileName);

                        TextView fileType = (TextView) findViewById(R.id.fileType);
                        ImageView download10 = (ImageView) findViewById(R.id.download4);
                        RelativeLayout documentLayout = (RelativeLayout) findViewById(R.id.rl);


                        TextView fnf = (TextView) findViewById(R.id.fnf4);

                        int downloadStatus = (int) map.get("downloadStatus");
                        if (downloadStatus == 1) {


                            /*
                             * Already downloaded
                             */

                            download10.setVisibility(View.GONE);


                            if (ActivityCompat.checkSelfPermission(MessageInfo.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {


                                final File file = new File(messageContent);


                                if (file.exists()) {
                                    fileName.setText((String) map.get("fileName"));


                                    String fileTypeS = findFileTypeFromExtension((String) map.get("extension"));


                                    fileType.setText(fileTypeS);


                                    fileType.setVisibility(View.VISIBLE);


                                    if (fileTypeS.equals(FilePickerConst.PDF)) {

                                        fileImage.setImageResource(R.drawable.ic_pdf);

                                    } else if (fileTypeS.equals(FilePickerConst.DOC)) {

                                        fileImage.setImageResource(R.drawable.ic_word);
                                    } else if (fileTypeS.equals(FilePickerConst.PPT)) {
                                        fileImage.setImageResource(R.drawable.ic_ppt);
                                    } else if (fileTypeS.equals(FilePickerConst.XLS)) {
                                        fileImage.setImageResource(R.drawable.ic_excel);
                                    } else if (fileTypeS.equals(FilePickerConst.TXT)) {
                                        fileImage.setImageResource(R.drawable.ic_txt);
                                    }
                                    documentLayout.setVisibility(View.VISIBLE);


                                } else {


                                    fnf.setVisibility(View.VISIBLE);
                                    documentLayout.setVisibility(View.GONE);


                                    fileType.setVisibility(View.GONE);
                                }
                            } else {

                                documentLayout.setVisibility(View.GONE);


                                fileType.setVisibility(View.GONE);
                                fnf.setVisibility(View.VISIBLE);
                                fnf.setText(getString(R.string.string_211));


                            }

                        } else {

                            /*
                             *
                             *To allow an option to download
                             *
                             */


                            String fileTypeS = findFileTypeFromExtension((String) map.get("extension"));

                            fileName.setText((String) map.get("fileName"));

                            fileType.setText(fileTypeS);
                            if (fileTypeS.equals(FilePickerConst.PDF)) {

                                fileImage.setImageResource(R.drawable.ic_pdf);

                            } else if (fileTypeS.equals(FilePickerConst.DOC)) {

                                fileImage.setImageResource(R.drawable.ic_word);
                            } else if (fileTypeS.equals(FilePickerConst.PPT)) {
                                fileImage.setImageResource(R.drawable.ic_ppt);
                            } else if (fileTypeS.equals(FilePickerConst.XLS)) {
                                fileImage.setImageResource(R.drawable.ic_excel);
                            } else if (fileTypeS.equals(FilePickerConst.TXT)) {
                                fileImage.setImageResource(R.drawable.ic_txt);
                            }
                            download10.setVisibility(View.GONE);

                        }


                        messageTime = (TextView) findViewById(R.id.ts8);

                        messageDate = (TextView) findViewById(R.id.date8);


                        singleTick = (ImageView) findViewById(R.id.single_tick_green8);

                        doubleTick = (ImageView) findViewById(R.id.double_tick_green8);

                        blueTick = (ImageView) findViewById(R.id.double_tick_blue8);

                        clock = (ImageView) findViewById(R.id.clock8);


                        messageTime.setTypeface(tf, Typeface.ITALIC);

                        messageDate.setTypeface(tf, Typeface.ITALIC);


                        break;
                    }

                    case 13: {
                        /*
                         * Post
                         */
if(post_rl!=null)
                        post_rl.setVisibility(View.VISIBLE);

                        ImageView postImage = (ImageView) findViewById(R.id.postImage);
                        ImageView playButton = (ImageView) findViewById(R.id.playButton);
                        TextView title = (TextView) findViewById(R.id.title);
                        title.setText((String) map.get("postTitle"));
                        if ((int) map.get("postType") == 0) {
                            playButton.setVisibility(View.GONE);
                        } else {
                            playButton.setVisibility(View.VISIBLE);
                        }

                        try {


                            Glide.with(MessageInfo.this)
                                    .load(messageContent)
                                    .centerCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                    .into(postImage);

                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (NullPointerException e) {
                            e.printStackTrace();
                        }


                        messageTime = (TextView) findViewById(R.id.ts11);

                        messageDate = (TextView) findViewById(R.id.date11);


                        singleTick = (ImageView) findViewById(R.id.single_tick_green11);

                        doubleTick = (ImageView) findViewById(R.id.double_tick_green11);

                        blueTick = (ImageView) findViewById(R.id.double_tick_blue11);

                        clock = (ImageView) findViewById(R.id.clock11);


                        messageTime.setTypeface(tf, Typeface.ITALIC);

                        messageDate.setTypeface(tf, Typeface.ITALIC);


                        break;
                    }


                }

                if (deliveryStatus == 3) {

                    clock.setVisibility(View.GONE);
                    singleTick.setVisibility(View.GONE);

                    doubleTick.setVisibility(View.GONE);
                    blueTick.setVisibility(View.VISIBLE);

                } else if (deliveryStatus == 2) {
                    clock.setVisibility(View.GONE);
                    singleTick.setVisibility(View.GONE);

                    doubleTick.setVisibility(View.VISIBLE);
                    blueTick.setVisibility(View.GONE);
                } else if (deliveryStatus == 1) {

                    clock.setVisibility(View.GONE);
                    singleTick.setVisibility(View.VISIBLE);

                    doubleTick.setVisibility(View.GONE);
                    blueTick.setVisibility(View.GONE);
                } else {


                    clock.setVisibility(View.VISIBLE);
                    singleTick.setVisibility(View.GONE);

                    doubleTick.setVisibility(View.GONE);
                    blueTick.setVisibility(View.GONE);
                }


                setMessageTime((String) map.get("Ts"));


                updateDeliveryTime((String) map.get("deliveryTime"));

                updateReadTime((String) map.get("readTime"));

            } else {


                if (root != null) {
                    Snackbar snackbar = Snackbar.make(root, getString(R.string.NotFound), Snackbar.LENGTH_SHORT);


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


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(true);

        mMap.addMarker(new MarkerOptions().position(positionSelected).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(positionSelected, 16.0f));


    }


    private void updateMessageDeliveryStatus(int deliveryStatus) {

        try {

            if (deliveryStatus == 3) {

                clock.setVisibility(View.GONE);
                singleTick.setVisibility(View.GONE);

                doubleTick.setVisibility(View.GONE);
                blueTick.setVisibility(View.VISIBLE);

            } else if (deliveryStatus == 2) {
                clock.setVisibility(View.GONE);
                singleTick.setVisibility(View.GONE);

                doubleTick.setVisibility(View.VISIBLE);
                blueTick.setVisibility(View.GONE);
            } else if (deliveryStatus == 1) {

                clock.setVisibility(View.GONE);
                singleTick.setVisibility(View.VISIBLE);

                doubleTick.setVisibility(View.GONE);
                blueTick.setVisibility(View.GONE);
            } else {


                clock.setVisibility(View.VISIBLE);
                singleTick.setVisibility(View.GONE);

                doubleTick.setVisibility(View.GONE);
                blueTick.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }


    private void updateDeliveryTime(String deliveryTime) {


        if (deliveryTime != null) {

            try {
                String tempDeliveryTime = Utilities.changeStatusDateFromGMTToLocal(Utilities.epochtoGmt(deliveryTime));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);

                Date date2 = new Date(System.currentTimeMillis() - AppController.getInstance().getTimeDelta());
                String current_date = sdf.format(date2);

                current_date = current_date.substring(0, 8);


                if (tempDeliveryTime != null) {


                    if (current_date.equals(tempDeliveryTime.substring(0, 8))) {

                        tempDeliveryTime = convert24to12hourformat(tempDeliveryTime.substring(8, 10) + ":" + tempDeliveryTime.substring(10, 12));


                        deliveryTime = "Today " + tempDeliveryTime;


                    } else {

                        String last = convert24to12hourformat(tempDeliveryTime.substring(8, 10) + ":" + tempDeliveryTime.substring(10, 12));


                        String date = tempDeliveryTime.substring(6, 8) + "-" + tempDeliveryTime.substring(4, 6) + "-" + tempDeliveryTime.substring(0, 4);

                        deliveryTime = date + " " + last;


                        last = null;
                        date = null;

                    }


                }


                deliveredAt.setText(deliveryTime);
                tempDeliveryTime = null;
                sdf = null;
                date2 = null;
                current_date = null;
            } catch (Exception e) {

                deliveredAt.setText(deliveryTime);
            }
        } else {
            deliveredAt.setText(getString(R.string.NotDelivered));

        }

    }

    private void updateReadTime(String readTime) {
        if (readTime != null) {
            try {


                String tempReadTime = Utilities.changeStatusDateFromGMTToLocal(Utilities.epochtoGmt(readTime));

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS z", Locale.US);

                Date date2 = new Date(System.currentTimeMillis() - AppController.getInstance().getTimeDelta());
                String current_date = sdf.format(date2);

                current_date = current_date.substring(0, 8);


                if (tempReadTime != null) {


                    if (current_date.equals(tempReadTime.substring(0, 8))) {

                        tempReadTime = convert24to12hourformat(tempReadTime.substring(8, 10) + ":" + tempReadTime.substring(10, 12));


                        readTime = "Today " + tempReadTime;


                    } else {

                        String last = convert24to12hourformat(tempReadTime.substring(8, 10) + ":" + tempReadTime.substring(10, 12));


                        String date = tempReadTime.substring(6, 8) + "-" + tempReadTime.substring(4, 6) + "-" + tempReadTime.substring(0, 4);

                        readTime = date + " " + last;


                        last = null;
                        date = null;

                    }


                }


                readAt.setText(readTime);
                tempReadTime = null;
                sdf = null;
                date2 = null;
                current_date = null;


            } catch (Exception e) {

                readAt.setText(readTime);
            }
        } else {
            readAt.setText(getString(R.string.NotRead));

        }

    }

    private String findFileTypeFromExtension(String extension) {


        if (extension.equals("pdf")) {
            return FilePickerConst.PDF;
        } else if (extension.equals("doc") || extension.equals("docx") || extension.equals("dot") || extension.equals("dotx")) {
            return FilePickerConst.DOC;
        } else if (extension.equals("ppt") || extension.equals("pptx")) {
            return FilePickerConst.PPT;
        } else if (extension.equals("xls") || extension.equals("xlsx")) {
            return FilePickerConst.XLS;
        } else if (extension.equals("txt")) {
            return FilePickerConst.TXT;
        } else return "UNKNOWN";
    }


    private void setMessageTime(String ts) {

        ts = Utilities.formatDate(Utilities.tsFromGmt(ts));
        messageDate.setText(Utilities.findOverlayDate(ts.substring(9, 24)) + getString(R.string.space));


        messageTime.setText(convert24to12hourformat(ts.substring(0, 9)) + getString(R.string.space));

    }


    @SuppressWarnings("TryWithIdenticalCatches")
    private class getWallpaperDetails extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] params) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Map<String, Object> map = AppController.getInstance().getDbController().getWallpaperDetails(documentId);


                        String wallpaperData = null;


                        if (map.get("wallpaperData") != null) {


                            wallpaperData = (String) map.get("wallpaperData");
                        }

                        updateWallpaper((int) map.get("wallpaperType"), wallpaperData);
                    } catch (Exception e) {
                        updateWallpaper(1, null);

                    }

                }
            });

            return null;
        }
    }

    private void updateWallpaper(int type, final String wallpaperDetails) {
        switch (type) {
            case 0: {
                /*
                 * Solid color
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatBackground.setImageDrawable(null);
                        chatBackground.setBackgroundColor(Color.parseColor(wallpaperDetails));
                    }
                });

                break;
            }

            case 1: {
                /*
                 *Default
                 */


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {


                                Glide.with(MessageInfo.this)
                                        .load(R.drawable.chat_background)
                                        .crossFade()

                                        .centerCrop()


                                        .into(chatBackground);
                            }
                        });
                    }
                });


                break;
            }

            case 2: {
                /*
                 *No wallpaper
                 */

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chatBackground.setImageDrawable(null);
                        chatBackground.setBackgroundColor(ContextCompat.getColor(MessageInfo.this, R.color.color_white));

                    }
                });


                break;
            }

            case 3: {
                /*
                 *Image from gallery or camera
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Glide.with(MessageInfo.this)
                                .load(wallpaperDetails)
                                .crossFade()

                                .centerCrop()


                                .into(chatBackground);
                    }
                });


                break;
            }


            case 4: {
                /*
                 *Image from api call
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Glide.with(MessageInfo.this)
                                .load(wallpaperDetails)
                                .crossFade()

                                .centerCrop()


                                .into(chatBackground);
                    }
                });


                break;
            }


            case 5: {
                /*
                 * Doodle drawn
                 */
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        Glide.with(MessageInfo.this)
                                .load(wallpaperDetails)
                                .crossFade()

                                .centerCrop()


                                .into(chatBackground);
                    }
                });


                break;
            }

        }
    }


}
