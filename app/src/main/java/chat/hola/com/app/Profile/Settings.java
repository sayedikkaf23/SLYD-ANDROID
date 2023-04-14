package chat.hola.com.app.Profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONException;
import org.json.JSONObject;

import chat.hola.com.app.Activities.ChatMessageScreen;
import chat.hola.com.app.Activities.ContactsList;
import chat.hola.com.app.Activities.DataUsage;
import chat.hola.com.app.Activities.Terms;
import chat.hola.com.app.AppController;

/**
 * Created by moda on 19/08/17.
 */

public class Settings extends AppCompatActivity {

    private Bus bus = AppController.getBus();
    private ImageView profilePic;
    private TextView userName, userStatus;

    private RelativeLayout root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        RelativeLayout profile_rl, terms_rl, contacts_rl, data_rl, invite_rl, blocked_rl;

        RelativeLayout profileHeader_rl;

        root = (RelativeLayout) findViewById(R.id.root);

        profile_rl = (RelativeLayout) findViewById(R.id.profile_rl);
        terms_rl = (RelativeLayout) findViewById(R.id.terms_rl);
        contacts_rl = (RelativeLayout) findViewById(R.id.contacts_rl);

        data_rl = (RelativeLayout) findViewById(R.id.data_rl);

        invite_rl = (RelativeLayout) findViewById(R.id.invite_rl);
        blocked_rl = (RelativeLayout) findViewById(R.id.block_rl);

        profileHeader_rl = (RelativeLayout) findViewById(R.id.rl);
        Typeface tf = AppController.getInstance().getRegularFont();

        TextView title = (TextView) findViewById(R.id.title);

        title.setTypeface(tf, Typeface.BOLD);

        profilePic = (ImageView) findViewById(R.id.userImage);

        userName = (TextView) findViewById(R.id.userName);

        userStatus = (TextView) findViewById(R.id.userStatus);

        setupActivity();
        ImageView close = (ImageView) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         onBackPressed();

                                     }
                                 }
        );


        profileHeader_rl.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    Intent i = new Intent(Settings.this, OwnProfileDetails.class);


                                                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                    startActivity(i);
                                                }
                                            }
        );
        profile_rl.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              Intent i = new Intent(Settings.this, OwnProfileDetails.class);


                                              i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                              startActivity(i);
                                          }
                                      }
        );

        terms_rl.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent(Settings.this, Terms.class);

                                            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                            startActivity(i);
                                        }
                                    }
        );

        contacts_rl.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View view) {
                                               Intent i = new Intent(Settings.this, ContactsList.class);

                                               i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                               startActivity(i);
                                           }
                                       }
        );


        data_rl.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View view) {
                                           Intent i = new Intent(Settings.this, DataUsage.class);

                                           i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                           startActivity(i);
                                       }
                                   }
        );


        blocked_rl.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              Intent i = new Intent(Settings.this, BlockedUser.class);


                                              i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                              startActivity(i);
                                          }
                                      }
        );


//        invite_rl.setOnClickListener(new View.OnClickListener() {
//                                         @Override
//                                         public void onClick(View view) {
//                                             Intent intent = new AppInviteInvitation.IntentBuilder("Invitation")
//
//                                                     .setMessage(getString(R.string.InviteMessage))
//                                                     //   .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
//                                                     .setCustomImage(Uri.parse(getString(R.string.AppIconUrl)))
//                                                     //  .setCallToActionText(getString(R.string.invitation_cta))
//                                                     .build();
//                                             startActivityForResult(intent, 0);
//                                         }
//                                     }
//        );


        Typeface robotoRegular = AppController.getInstance().getMediumFont();
        userName.setTypeface(robotoRegular, Typeface.NORMAL);
        userStatus.setTypeface(robotoRegular, Typeface.NORMAL);

        TextView profile = (TextView) findViewById(R.id.tv1);

        TextView terms = (TextView) findViewById(R.id.tv2);
        TextView contacts = (TextView) findViewById(R.id.tv3);

        TextView data = (TextView) findViewById(R.id.tv4);

        TextView invite = (TextView) findViewById(R.id.tv5);


        TextView blocked = (TextView) findViewById(R.id.tv6);
        Typeface robotoMedium = AppController.getInstance().getSemiboldFont();
        profile.setTypeface(robotoMedium, Typeface.NORMAL);
        terms.setTypeface(robotoMedium, Typeface.NORMAL);
        contacts.setTypeface(robotoMedium, Typeface.NORMAL);
        data.setTypeface(robotoMedium, Typeface.NORMAL);
        invite.setTypeface(robotoMedium, Typeface.NORMAL);


        blocked.setTypeface(robotoMedium, Typeface.NORMAL);
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

    private void minimizeCallScreen(JSONObject obj) {
        try {
            Intent intent = new Intent(Settings.this, ChatMessageScreen.class);

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

    @SuppressWarnings("TryWithIdenticalCatches")
    @Subscribe
    public void getMessage(JSONObject object) {
        try {


            if (object.getString("eventName").equals("callMinimized")) {

                minimizeCallScreen(object);
            } else if (object.getString("eventName").equals("nameUpdated")) {
                try {
                    userName.setText(object.getString("name"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (object.getString("eventName").equals("profilePicUpdated")) {
                String pictureUrl = object.getString("profilePicUrl");

                if (pictureUrl != null && !pictureUrl.isEmpty()) {
                    try {
                        Glide.with(Settings.this).load(pictureUrl).asBitmap()
                            .signature(new StringSignature(
                                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                                .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                                into(new BitmapImageViewTarget(profilePic) {
                                    @Override
                                    protected void setResource(Bitmap resource) {
                                        RoundedBitmapDrawable circularBitmapDrawable =
                                                RoundedBitmapDrawableFactory.create(getResources(), resource);
                                        circularBitmapDrawable.setCircular(true);
                                        profilePic.setImageDrawable(circularBitmapDrawable);
                                    }
                                });
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }

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

    @SuppressWarnings("TryWithIdenticalCatches")
    private void setupActivity() {

        userName.setText(AppController.getInstance().getUserName());
        userStatus.setText(AppController.getInstance().getUserStatus());

        String pictureUrl = AppController.getInstance().getUserImageUrl();

        if (pictureUrl != null && !pictureUrl.isEmpty()) {
            try {
                Glide.with(Settings.this).load(pictureUrl).asBitmap()
                    .signature(new StringSignature(
                        AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                        into(new BitmapImageViewTarget(profilePic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                profilePic.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setupActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 0) {
            if (resultCode != RESULT_OK) {
                // Get the invitation IDs of all sent messages


                Snackbar snackbar = Snackbar.make(root, R.string.InviteFailed, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }
    }


}
