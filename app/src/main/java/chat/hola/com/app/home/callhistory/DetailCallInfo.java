package chat.hola.com.app.home.callhistory;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TextDrawable;
import chat.hola.com.app.home.LandingActivity;

public class DetailCallInfo extends AppCompatActivity {

    String receiverName, receiverCallType, receiverImage, calledDate, callTime, callDurationString;
    TextView tvTitle, usernamecall, call_type_text, timeingcall, callDuration;
    ImageView ivBack;
    AppCompatImageView userImage, Statuscall, callStatus, callInfo;
    Boolean callNotAllowed, isCallInitiated;
    private String str = "";
    private Drawable drawable1, drawable2;
    private int density, position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_call_info);

        density = (int) getResources().getDisplayMetrics().density;


        receiverName = getIntent().getStringExtra("receiverName");
        receiverCallType = getIntent().getStringExtra("callType");
        receiverImage = getIntent().getStringExtra("receiverImage");
        calledDate = getIntent().getStringExtra("calledDate");
        callNotAllowed = Boolean.valueOf(getIntent().getStringExtra("isCallNotAllowed"));
        isCallInitiated = getIntent().getExtras().getBoolean("isCallInitiated");
        callTime = getIntent().getStringExtra("callTime");
        callDurationString = getIntent().getStringExtra("callDuration");
        position = getIntent().getIntExtra("position", 0);


        drawable1 = ContextCompat.getDrawable(DetailCallInfo.this, R.drawable.call_incoming_call_icon);
        drawable2 = ContextCompat.getDrawable(DetailCallInfo.this, R.drawable.call_outgoing_call_icon);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivBack = (ImageView) findViewById(R.id.ivBack);
        userImage = (AppCompatImageView) findViewById(R.id.userImage);
        usernamecall = (TextView) findViewById(R.id.usernamecall);
        Statuscall = (AppCompatImageView) findViewById(R.id.Statuscall);
        call_type_text = (TextView) findViewById(R.id.call_type_text);
        callStatus = (AppCompatImageView) findViewById(R.id.callStatus);
        callInfo = (AppCompatImageView) findViewById(R.id.callInfo);
        timeingcall = (TextView) findViewById(R.id.timeingcall);
        callDuration = (TextView) findViewById(R.id.callDuration);


        if (receiverImage != null && !receiverImage.isEmpty()) {

            try {
                Glide.with(this).load(receiverImage).asBitmap()

                        .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame)
                        //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                        .signature(new StringSignature(
                                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        .into(new BitmapImageViewTarget(userImage) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                userImage.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        } else {

            try {
                userImage.setImageDrawable(TextDrawable.builder()

                        .beginConfig()
                        .textColor(Color.WHITE)
                        .useFont(Typeface.DEFAULT)
                        .fontSize(24 * density) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()

                        .buildRound((receiverName.trim()).charAt(0) + "", Color.parseColor(
                                AppController.getInstance().getColorCode(position % 19))));
            } catch (IndexOutOfBoundsException e) {
                userImage.setImageDrawable(TextDrawable.builder()

                        .beginConfig()
                        .textColor(Color.WHITE)
                        .useFont(Typeface.DEFAULT)
                        .fontSize(24 * density) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()

                        .buildRound("C", Color.parseColor(
                                AppController.getInstance().getColorCode(position % 19))));
            } catch (NullPointerException e) {
                userImage.setImageDrawable(TextDrawable.builder()

                        .beginConfig()
                        .textColor(Color.WHITE)
                        .useFont(Typeface.DEFAULT)
                        .fontSize(24 * density) /* size in px */
                        .bold()
                        .toUpperCase()
                        .endConfig()

                        .buildRound("C", Color.parseColor(
                                AppController.getInstance().getColorCode(position % 19))));
            }
        }

        if (isCallInitiated) {
            Statuscall.setImageDrawable(drawable1);
            call_type_text.setText(receiverCallType);
            call_type_text.setTextColor(getResources().getColor(R.color.green));

        } else {
            Statuscall.setImageDrawable(drawable2);
            call_type_text.setText(receiverCallType);
            call_type_text.setTextColor(getResources().getColor(R.color.red));
        }

        usernamecall.setText(receiverName);
        timeingcall.setText(callTime);

        if (callDurationString == null || callDurationString.isEmpty()) {
            setCallDuration("0");

        } else {
            setCallDuration(callDurationString);

        }
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void setCallDuration(String ms) {

//        long s = seconds % 60;
//        long m = (seconds / 60) % 60;
//        long h = (seconds / (60 * 60)) % 24;

        int millis = Integer.parseInt(ms);
        int seconds = (millis / 1000) % 60;
        int minutes = (seconds / 60) % 60;
//        long minutes = ((millis - seconds) / 1000) / 60;
        int hour = (seconds / (60 * 60)) % 24;


        String time = "0";
        if (hour > 0) {
            time = hour + " hour" + minutes + " min " + seconds + " sec";

        } else if (minutes > 0) {
            time = minutes + " min " + seconds + " sec";
        } else {
            time = seconds + " sec";

        }

        callDuration.setText(time);
    }
}
