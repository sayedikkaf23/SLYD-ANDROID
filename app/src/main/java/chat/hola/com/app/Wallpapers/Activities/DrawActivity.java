package chat.hola.com.app.Wallpapers.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Doodle.DrawView;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.Utilities;

/**
 * Created by moda on 10/10/17.
 */

public class DrawActivity extends AppCompatActivity {

    private Bus bus = AppController.getBus();

    private DrawView drawView;

    private RelativeLayout mainLinearLayout, colourId, root;
    private ImageView redButton, blackButton, greenButton, blueButton;
    private static final int IMAGE_QUALITY = 50;


    private String docId;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallpaper_doodle);
        docId = getIntent().getExtras().getString("documentId");
        root = (RelativeLayout) findViewById(R.id.root);
        mainLinearLayout = (RelativeLayout) findViewById(R.id.doodleLayout);
        colourId = (RelativeLayout) findViewById(R.id.coloursLayout);
        drawView = new DrawView(this);


        if (drawView.getParent() != null)
            ((ViewGroup) drawView.getParent()).removeView(drawView);

        if (colourId.getParent() != null)
            ((ViewGroup) colourId.getParent()).removeView(colourId);

        mainLinearLayout.addView(drawView);
        mainLinearLayout.addView(colourId);

        redButton = (ImageView) findViewById(R.id.redColour);
        blackButton = (ImageView) findViewById(R.id.blackColour);
        greenButton = (ImageView) findViewById(R.id.greenColour);
        blueButton = (ImageView) findViewById(R.id.blueColour);

        blackButton.setSelected(true);
        redButton.setSelected(false);
        greenButton.setSelected(false);
        blueButton.setSelected(false);
        redButton.setClickable(true);
        blackButton.setClickable(true);
        greenButton.setClickable(true);
        blueButton.setClickable(true);
        blackButton.setSelected(true);

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                blackButton.setSelected(false);
                redButton.setSelected(true);
                greenButton.setSelected(false);
                blueButton.setSelected(false);
                drawView.mPaint.setColor(ContextCompat.getColor(DrawActivity.this, R.color.doodle_color_red));
            }
        });


        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blackButton.setSelected(true);
                redButton.setSelected(false);
                greenButton.setSelected(false);
                blueButton.setSelected(false);
                drawView.mPaint.setColor(ContextCompat.getColor(DrawActivity.this, R.color.doodle_color_black));
            }
        });


        blueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blackButton.setSelected(false);
                redButton.setSelected(false);
                greenButton.setSelected(false);
                blueButton.setSelected(true);
                drawView.mPaint.setColor(ContextCompat.getColor(DrawActivity.this, R.color.doodle_color_blue));
            }
        });

        greenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blackButton.setSelected(false);
                redButton.setSelected(false);
                greenButton.setSelected(true);
                blueButton.setSelected(false);
                drawView.mPaint.setColor(ContextCompat.getColor(DrawActivity.this, R.color.doodle_color_green));
            }
        });

        final ImageView deletebutton = (ImageView) findViewById(R.id.deleteDoddle);

        deletebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawView.getParent() != null)
                    ((ViewGroup) drawView.getParent()).removeView(drawView);

                if (colourId.getParent() != null)
                    ((ViewGroup) colourId.getParent()).removeView(colourId);
                mainLinearLayout.removeAllViews();
                mainLinearLayout.invalidate();
                mainLinearLayout.addView(drawView);
                mainLinearLayout.addView(colourId);
                redButton.setClickable(true);
                blackButton.setClickable(true);
                greenButton.setClickable(true);
                blueButton.setClickable(true);
                blackButton.setSelected(true);
                redButton.setSelected(false);
                greenButton.setSelected(false);
                blueButton.setSelected(false);
                drawView.mPaint.setColor(Color.parseColor("#000000"));

            }
        });


        final ImageView colurSelected = (ImageView) findViewById(R.id.done);

        colurSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Bitmap imageOnView = ConvertToBitmap();


                doodleBitmap(imageOnView);
                mainLinearLayout.removeAllViews();
                mainLinearLayout.invalidate();


                drawView = new DrawView(DrawActivity.this);
                mainLinearLayout.addView(drawView);
                mainLinearLayout.addView(colourId);
                redButton.setClickable(true);
                blackButton.setClickable(true);
                greenButton.setClickable(true);
                blueButton.setClickable(true);
                blackButton.setSelected(true);
                redButton.setSelected(false);
                greenButton.setSelected(false);
                blueButton.setSelected(false);
                drawView.mPaint.setColor(Color.parseColor("#000000"));

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
            Intent intent = new Intent(DrawActivity.this, ChatMessageScreen.class);

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

    protected Bitmap ConvertToBitmap() {
        return drawView.getmBitmap();
    }


    public void doodleBitmap(Bitmap bitmap) {


        if (bitmap != null) {
            try {


                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, baos);


                byte[] br = baos.toByteArray();
                String path = createDoodleUri(br);
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                AppController.getInstance().getDbController().addWallpaper(docId, 5, path);


                try {


                    JSONObject obj = new JSONObject();
                    obj.put("eventName", "WallpaperUpdated");

                    obj.put("type", 5);
                    obj.put("wallpaperDetails", path);


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

                baos = null;


                br = null;


            } catch (OutOfMemoryError e)

            {


                if (root != null) {

                    Snackbar snackbar = Snackbar.make(root, getString(R.string.WallpaperFailed), Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }


            }


        } else

        {
            if (root != null) {
                Snackbar snackbar = Snackbar.make(root, getString(R.string.WallpaperFailed), Snackbar.LENGTH_SHORT);
                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }


    }

    @SuppressWarnings("all")
    private String createDoodleUri(byte[] data) {
        String name = Utilities.tsInGmt();
        name = new Utilities().gmtToEpoch(name);


        File folder = new File(getExternalFilesDir(null) + ApiOnServer.CHAT_DOODLES_FOLDER);

        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }


        File file = new File(getExternalFilesDir(null)  + ApiOnServer.CHAT_DOODLES_FOLDER, name + ".jpg");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        try {
            FileOutputStream fos = new FileOutputStream(file);

            fos.write(data);
            fos.flush();
            fos.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
        name = null;
        folder = null;

        return file.getAbsolutePath();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);


        docId = intent.getExtras().getString("documentId");
    }

}
