package chat.hola.com.app.SecretChat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;

import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.ezcall.android.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


import chat.hola.com.app.Activities.MediaHistory_FullScreenImage;
import chat.hola.com.app.Activities.MediaHistory_FullScreenVideo;
import chat.hola.com.app.AppController;
import chat.hola.com.app.BlurTransformation.BlurTransformation;
import chat.hola.com.app.DocumentPicker.FilePickerConst;
import chat.hola.com.app.DownloadFile.FileDownloadService;
import chat.hola.com.app.DownloadFile.ServiceGenerator;
import chat.hola.com.app.Giphy.GifPlayer;
import chat.hola.com.app.Utilities.ApiOnServer;
import chat.hola.com.app.Utilities.AudioWife;
import chat.hola.com.app.Utilities.LinkUtils;
import chat.hola.com.app.Utilities.RingProgressBar;
import chat.hola.com.app.ViewHolders.ViewHolderAudioReceived;
import chat.hola.com.app.ViewHolders.ViewHolderAudioSent;
import chat.hola.com.app.ViewHolders.ViewHolderContactReceived;
import chat.hola.com.app.ViewHolders.ViewHolderContactSent;
import chat.hola.com.app.ViewHolders.ViewHolderDocumentReceived;
import chat.hola.com.app.ViewHolders.ViewHolderDocumentSent;
import chat.hola.com.app.ViewHolders.ViewHolderDoodleReceived;
import chat.hola.com.app.ViewHolders.ViewHolderDoodleSent;
import chat.hola.com.app.ViewHolders.ViewHolderGifReceived;
import chat.hola.com.app.ViewHolders.ViewHolderGifSent;
import chat.hola.com.app.ViewHolders.ViewHolderImageReceived;
import chat.hola.com.app.ViewHolders.ViewHolderImageSent;
import chat.hola.com.app.ViewHolders.ViewHolderLoading;
import chat.hola.com.app.ViewHolders.ViewHolderLocationReceived;
import chat.hola.com.app.ViewHolders.ViewHolderLocationSent;
import chat.hola.com.app.ViewHolders.ViewHolderMessageReceived;
import chat.hola.com.app.ViewHolders.ViewHolderMessageSent;
import chat.hola.com.app.ViewHolders.ViewHolderPostReceived;
import chat.hola.com.app.ViewHolders.ViewHolderPostSent;
import chat.hola.com.app.ViewHolders.ViewHolderServerMessage;
import chat.hola.com.app.ViewHolders.ViewHolderStickerReceived;
import chat.hola.com.app.ViewHolders.ViewHolderStickerSent;
import chat.hola.com.app.ViewHolders.ViewHolderVideoReceived;
import chat.hola.com.app.ViewHolders.ViewHolderVideoSent;
import chat.hola.com.app.socialDetail.SocialDetailActivity;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by moda on 05/08/17.
 */

public class SecretChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<SecretChatMessageItem> mListData = new ArrayList<>();
    private static final int MESSAGERECEIVED = 0;
    private static final int MESSAGESENT = 1;

    private static final int IMAGERECEIVED = 2;
    private static final int IMAGESENT = 3;

    private static final int VIDEORECEIVED = 4;
    private static final int VIDEOSENT = 5;

    private static final int LOCATIONRECEIVED = 6;
    private static final int LOCATIONSENT = 7;

    private static final int CONTACTRECEIVED = 8;
    private static final int CONTACTSENT = 9;

    private static final int AUDIORECEIVED = 10;
    private static final int AUDIOSENT = 11;

    /**
     * For non standard sup like sharing
     */
    private static final int STICKERSRECEIVED = 12;
    private static final int STICKERSSENT = 13;
    private static final int SERVERMESSAGE = 14;
    private static final int DOODLERECEIVED = 15;
    private static final int DOODLESENT = 16;

    private static final int GIFRECEIVED = 17;
    private static final int GIFSENT = 18;

    private final int LOADING = 19;

    private final int DOCUMENTSENT = 20;
    private final int DOCUMENTRECEIVED = 21;



    /**
     * For adding the sharing of the post functionality,specific to dubly app
     */
    private final int POSTSENT = 22;
    private final int POSTRECEIVED = 23;



    private static final int SECTOMILLSEC = 1000;


    private Context mContext;


    private long fileSizeDownloaded;
    private RelativeLayout root;

    private int density;

    private HashMap<String, Object> map = new HashMap<>();

    private Bitmap thumbnail;

    //    private Typeface tf;
    private String documentId, secretId;

    private int transparentColor, lightBlueColor;

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {


        if (holder.getItemViewType() == LOCATIONRECEIVED) {
            if (((ViewHolderLocationReceived) holder).mMap != null) {
                ((ViewHolderLocationReceived) holder).mMap.clear();
                ((ViewHolderLocationReceived) holder).mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            }
        } else if (holder.getItemViewType() == LOCATIONSENT) {
            if (((ViewHolderLocationSent) holder).mMap != null) {
                ((ViewHolderLocationSent) holder).mMap.clear();
                ((ViewHolderLocationSent) holder).mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
            }
        }
    }


    public SecretChatMessageAdapter(Context mContext, ArrayList<SecretChatMessageItem> mListData, RelativeLayout root, String documentId, String secretId) {
        this.mListData = mListData;
        this.mContext = mContext;
        this.root = root;
        this.documentId = documentId;
        this.secretId = secretId;


        density = (int) mContext.getResources().getDisplayMetrics().density;

        //  tf = AppController.getInstance().getRegularFont();


        transparentColor = ContextCompat.getColor(mContext, R.color.transparent);

        lightBlueColor = ContextCompat.getColor(mContext, R.color.message_select);

    }


    @Override
    public int getItemCount() {
        return this.mListData.size();
    }


    @Override
    public int getItemViewType(int position) {


        if (mListData.get(position).isDTimeTag()) {
            return SERVERMESSAGE;
        }


        String type = mListData.get(position).getMessageType();

        /*
         * For showing of the loading more item
         */

        if (type.equals("99")) {
            return LOADING;
        }

        if (mListData.get(position).isSelf()) {

            if (type.equals("0")) {
                return MESSAGESENT;
            } else if (type.equals("1")) {
                return IMAGESENT;
            } else if (type.equals("2")) {
                return VIDEOSENT;
            } else if (type.equals("3")) {
                return LOCATIONSENT;
            } else if (type.equals("4")) {
                return CONTACTSENT;
            } else if (type.equals("5")) {
                return AUDIOSENT;
            } else if (type.equals("6")) {
                return STICKERSSENT;
            } else if (type.equals("7")) {
                return DOODLESENT;
            } else if (type.equals("8")) {
                return GIFSENT;
            }

            else if (type.equals("13")) {
                return POSTSENT;
            }

            else {

                return DOCUMENTSENT;


            }
        } else {

            if (type.equals("0")) {
                return MESSAGERECEIVED;
            } else if (type.equals("1")) {


                return IMAGERECEIVED;

            } else if (type.equals("2")) {
                return VIDEORECEIVED;
            } else if (type.equals("3")) {
                return LOCATIONRECEIVED;
            } else if (type.equals("4")) {
                return CONTACTRECEIVED;
            } else if (type.equals("5")) {

                return AUDIORECEIVED;

            } else if (type.equals("6")) {
                return STICKERSRECEIVED;
            } else if (type.equals("7")) {
                return DOODLERECEIVED;
            } else if (type.equals("8")) {
                return GIFRECEIVED;
            }
            else if (type.equals("13")) {
                return POSTRECEIVED;
            }

            else {
                return DOCUMENTRECEIVED;

            }
        }


    }




    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(  ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View v1;
        switch (viewType) {
            case MESSAGERECEIVED:
                v1 = inflater.inflate(R.layout.message_text_received, viewGroup, false);
                viewHolder = new ViewHolderMessageReceived(v1);
                break;

            case IMAGERECEIVED:
                v1 = inflater.inflate(R.layout.message_image_received, viewGroup, false);
                viewHolder = new ViewHolderImageReceived(v1);
                break;

            case VIDEORECEIVED:
                v1 = inflater.inflate(R.layout.message_video_received, viewGroup, false);
                viewHolder = new ViewHolderVideoReceived(v1);
                break;

            case LOCATIONRECEIVED:
                v1 = inflater.inflate(R.layout.message_location_received, viewGroup, false);
                viewHolder = new ViewHolderLocationReceived(v1);
                break;

            case CONTACTRECEIVED:
                v1 = inflater.inflate(R.layout.message_contact_received, viewGroup, false);
                viewHolder = new ViewHolderContactReceived(v1);
                break;

            case AUDIORECEIVED:
                v1 = inflater.inflate(R.layout.message_audio_received, viewGroup, false);
                viewHolder = new ViewHolderAudioReceived(v1);
                break;


            case STICKERSRECEIVED:
                v1 = inflater.inflate(R.layout.message_sticker_received, viewGroup, false);
                viewHolder = new ViewHolderStickerReceived(v1);
                break;


            case DOODLERECEIVED:
                v1 = inflater.inflate(R.layout.message_doodle_received, viewGroup, false);
                viewHolder = new ViewHolderDoodleReceived(v1);
                break;

            case GIFRECEIVED:
                v1 = inflater.inflate(R.layout.message_gif_received, viewGroup, false);
                viewHolder = new ViewHolderGifReceived(v1);
                break;


            case DOCUMENTRECEIVED:
                v1 = inflater.inflate(R.layout.message_document_received, viewGroup, false);
                viewHolder = new ViewHolderDocumentReceived(v1);
                break;


            case POSTRECEIVED:
                v1 = inflater.inflate(R.layout.message_post_received, viewGroup, false);
                viewHolder = new ViewHolderPostReceived(v1);
                break;



            case MESSAGESENT:
                v1 = inflater.inflate(R.layout.message_text_sent, viewGroup, false);
                viewHolder = new ViewHolderMessageSent(v1);
                break;

            case IMAGESENT:
                v1 = inflater.inflate(R.layout.message_image_sent, viewGroup, false);
                viewHolder = new ViewHolderImageSent(v1);
                break;

            case VIDEOSENT:
                v1 = inflater.inflate(R.layout.message_video_sent, viewGroup, false);
                viewHolder = new ViewHolderVideoSent(v1);
                break;

            case LOCATIONSENT:
                v1 = inflater.inflate(R.layout.message_location_sent, viewGroup, false);
                viewHolder = new ViewHolderLocationSent(v1);
                break;


            case CONTACTSENT:
                v1 = inflater.inflate(R.layout.message_contact_sent, viewGroup, false);
                viewHolder = new ViewHolderContactSent(v1);
                break;
            case AUDIOSENT:
                v1 = inflater.inflate(R.layout.message_audio_sent, viewGroup, false);
                viewHolder = new ViewHolderAudioSent(v1);
                break;

            case STICKERSSENT:

                v1 = inflater.inflate(R.layout.message_sticker_sent, viewGroup, false);
                viewHolder = new ViewHolderStickerSent(v1);
                break;

            case DOODLESENT:
                v1 = inflater.inflate(R.layout.message_doodle_sent, viewGroup, false);
                viewHolder = new ViewHolderDoodleSent(v1);
                break;

            case GIFSENT:
                v1 = inflater.inflate(R.layout.message_gif_sent, viewGroup, false);
                viewHolder = new ViewHolderGifSent(v1);
                break;
            case DOCUMENTSENT:
                v1 = inflater.inflate(R.layout.message_document_sent, viewGroup, false);
                viewHolder = new ViewHolderDocumentSent(v1);
                break;


            case POSTSENT:
                v1 = inflater.inflate(R.layout.message_post_sent, viewGroup, false);
                viewHolder = new ViewHolderPostSent(v1);
                break;

            case LOADING:
                v1 = inflater.inflate(R.layout.loading_item, viewGroup, false);
                viewHolder = new ViewHolderLoading(v1);
                break;

            default:
                v1 = inflater.inflate(R.layout.servermessage, viewGroup, false);
                viewHolder = new ViewHolderServerMessage(v1);
                break;


        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        switch (viewHolder.getItemViewType()) {


            case MESSAGERECEIVED:
                ViewHolderMessageReceived vh2 = (ViewHolderMessageReceived) viewHolder;

                configureViewHolderMessageReceived(vh2, position);

                break;

            case IMAGERECEIVED:
                ViewHolderImageReceived vh3 = (ViewHolderImageReceived) viewHolder;
                configureViewHolderImageReceived(vh3, position);
                break;

            case VIDEORECEIVED:

                ViewHolderVideoReceived vh4 = (ViewHolderVideoReceived) viewHolder;

                configureViewHolderVideoReceived(vh4, position);
                break;

            case LOCATIONRECEIVED:

                ViewHolderLocationReceived vh5 = (ViewHolderLocationReceived) viewHolder;

                configureViewHolderLocationReceived(vh5, position);
                break;

            case CONTACTRECEIVED:

                ViewHolderContactReceived vh6 = (ViewHolderContactReceived) viewHolder;
                configureViewHolderContactReceived(vh6, position);
                break;

            case AUDIORECEIVED:

                ViewHolderAudioReceived vh7 = (ViewHolderAudioReceived) viewHolder;

                configureViewHolderAudioReceived(vh7, position);
                break;


            case STICKERSRECEIVED:
                ViewHolderStickerReceived vh8 = (ViewHolderStickerReceived) viewHolder;
                configureViewHolderStickerReceived(vh8, position);
                break;

            case DOODLERECEIVED:
                ViewHolderDoodleReceived vh9 = (ViewHolderDoodleReceived) viewHolder;
                configureViewHolderDoodleReceived(vh9, position);
                break;

            case GIFRECEIVED:

                ViewHolderGifReceived vh10 = (ViewHolderGifReceived) viewHolder;
                configureViewHolderGifReceived(vh10, position);
                break;

            case MESSAGESENT:


                ViewHolderMessageSent vh11 = (ViewHolderMessageSent) viewHolder;

                configureViewHolderMessageSent(vh11, position);

                break;

            case IMAGESENT:


                ViewHolderImageSent vh12 = (ViewHolderImageSent) viewHolder;
                configureViewHolderImageSent(vh12, position);
                break;

            case VIDEOSENT:

                ViewHolderVideoSent vh13 = (ViewHolderVideoSent) viewHolder;
                configureViewHolderVideoSent(vh13, position);
                break;

            case LOCATIONSENT:
                ViewHolderLocationSent vh14 = (ViewHolderLocationSent) viewHolder;
                configureViewHolderLocationSent(vh14, position);
                break;


            case CONTACTSENT:
                ViewHolderContactSent vh15 = (ViewHolderContactSent) viewHolder;
                configureViewHolderContactSent(vh15, position);
                break;


            case AUDIOSENT:
                ViewHolderAudioSent vh16 = (ViewHolderAudioSent) viewHolder;
                configureViewHolderAudioSent(vh16, position);
                break;

            case STICKERSSENT:


                ViewHolderStickerSent vh17 = (ViewHolderStickerSent) viewHolder;
                configureViewHolderStickersSent(vh17, position);
                break;
            case DOODLESENT:


                ViewHolderDoodleSent vh18 = (ViewHolderDoodleSent) viewHolder;
                configureViewHolderDoodleSent(vh18, position);
                break;

            case GIFSENT:

                ViewHolderGifSent vh19 = (ViewHolderGifSent) viewHolder;
                configureViewHolderGifSent(vh19, position);
                break;

            case LOADING:

                ViewHolderLoading vh20 = (ViewHolderLoading) viewHolder;
                configureViewHolderLoading(vh20, position);
                break;


            case DOCUMENTRECEIVED:

                ViewHolderDocumentReceived vh21 = (ViewHolderDocumentReceived) viewHolder;
                configureViewHolderDocumentReceived(vh21, position);
                break;

            case DOCUMENTSENT:

                ViewHolderDocumentSent vh22 = (ViewHolderDocumentSent) viewHolder;
                configureViewHolderDocumentSent(vh22, position);
                break;



            /*
             * For the post message
             */

            case POSTRECEIVED:

                ViewHolderPostReceived vh23 = (ViewHolderPostReceived) viewHolder;
                configureViewHolderPostReceived(vh23, position);
                break;
            case POSTSENT:

                ViewHolderPostSent vh24 = (ViewHolderPostSent) viewHolder;
                configureViewHolderPostSent(vh24, position);
                break;





            default:
                ViewHolderServerMessage vh1 = (ViewHolderServerMessage) viewHolder;
                configureViewHolderServerMessage(vh1, position);


        }
    }


    private void configureViewHolderMessageReceived(ViewHolderMessageReceived vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {


            // vh2.senderName.setText(message.getSenderName());


//            vh2.time.setTypeface(tf, Typeface.ITALIC);
//
//            vh2.date.setTypeface(tf, Typeface.ITALIC);
//            vh2.message.setTypeface(tf, Typeface.NORMAL);

            vh2.forward.setVisibility(View.GONE);
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");


            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");
            try {
                vh2.message.setText(message.getTextMessage());
                LinkUtils.autoLink(vh2.message, null);

                vh2.message.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label", message.getTextMessage());
                        clipboard.setPrimaryClip(clip);


                        Toast toast = Toast.makeText(mContext, "Message Copied", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        final Vibrator vibe = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                        vibe.vibrate(200);

                        return true;
                    }
                });
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }


            if (!message.isTimerStarted()) {
                try {
                    message.setTimerStarted(true);

                    if (message.getdTime() > 0) {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

//                                db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                                ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mListData.remove(position);
//                                        if (position == 0) {
//                                            notifyDataSetChanged();
//                                        } else {
//                                            notifyItemRemoved(position);
//                                        }
//                                    }
//                                });


                                try {
                                    ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, (message.getdTime() * SECTOMILLSEC));

                        ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                    }
                    //     ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);

                    //AppController.getInstance().addActiveTimer(documentId, message.getMessageId());


//                        else {
//
//                            //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                            db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mListData.remove(position);
//                                    if (position == 0) {
//                                        notifyDataSetChanged();
//                                    } else {
//                                        notifyItemRemoved(position);
//                                    }
//                                }
//                            });
//
//                        }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }

            if(((SecretChatMessageScreen)mContext).receiverImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);
                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).receiverImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }


        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderImageReceived(final ViewHolderImageReceived vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {

            if(((SecretChatMessageScreen)mContext).receiverImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);
                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).receiverImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }


            vh2.forward.setVisibility(View.GONE);
            vh2.fnf.setVisibility(View.GONE);


//            vh2.time.setTypeface(tf, Typeface.ITALIC);
//
//            vh2.date.setTypeface(tf, Typeface.ITALIC);
            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");


            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });


            try {


                if (message.getDownloadStatus() == 1) {

                    /*
                     *
                     * image already downloaded
                     *
                     * */
                    vh2.progressBar2.setVisibility(View.GONE);

                    vh2.progressBar.setVisibility(View.GONE);
                    vh2.download.setVisibility(View.GONE);
                    vh2.cancel.setVisibility(View.GONE);

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {


                        try {


                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(message.getImagePath(), options);


                            int height = options.outHeight;
                            int width = options.outWidth;


                            int reqHeight;


//                            reqHeight = ((150 * height) / width);


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
                                        .with(mContext)
                                        .load(message.getImagePath())
                                        .override(150 * density, reqHeight * density)

                                        .crossFade()
                                        .centerCrop()


                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .placeholder(R.drawable.home_grid_view_image_icon)
                                        .listener(new RequestListener<String, GlideDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                                                return false;
                                            }
                                        })
                                        .into(vh2.imageView);
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }


                            vh2.imageView.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {


                                    Intent i = new Intent(mContext, MediaHistory_FullScreenImage.class);

                                    i.putExtra("imagePath", message.getImagePath());

                                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    ActivityOptionsCompat options = ActivityOptionsCompat.
                                            makeSceneTransitionAnimation((Activity) mContext, vh2.imageView, "image");
                                    mContext.startActivity(i, options.toBundle());


                                }


                            });


                        } catch (Exception e) {
                            //   vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                            Glide.clear(vh2.imageView);
                            vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                            vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                            vh2.fnf.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Glide.clear(vh2.imageView);
                        //  vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                        vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                        vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                        vh2.fnf.setVisibility(View.VISIBLE);


                        vh2.fnf.setText(mContext.getString(R.string.string_211));


                        vh2.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                                intent.setData(uri);
                                mContext.startActivity(intent);
                            }
                        });

                    }


                    if (!message.isTimerStarted()) {
                        try {
                            message.setTimerStarted(true);

                            if (message.getdTime() > 0) {


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {


                                        try {
                                            ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, (message.getdTime() * SECTOMILLSEC));

                                ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                            }

                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }


                } else {


                    if (message.isDownloading()) {


                        vh2.cancel.setVisibility(View.VISIBLE);


                        vh2.download.setVisibility(View.GONE);


                        vh2.progressBar2.setVisibility(View.VISIBLE);

                        vh2.progressBar.setVisibility(View.GONE);


                    } else {
                        vh2.download.setVisibility(View.VISIBLE);

                        vh2.progressBar2.setVisibility(View.GONE);
                        vh2.progressBar.setVisibility(View.GONE);
                        vh2.cancel.setVisibility(View.GONE);
                    }

                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;


                    BitmapFactory.decodeFile(message.getThumbnailPath(), options);


                    int height = options.outHeight;
                    int width = options.outWidth;


                    int reqHeight;


//                    reqHeight = ((150 * height) / width);


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
                                .with(mContext)
                                .load(message.getThumbnailPath())


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext))


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
                                        vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                                        return false;
                                    }
                                })


                                .into(vh2.imageView);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.imageView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            if (!message.isDownloading()) {
                                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {


                                    AlertDialog.Builder builder =
                                            new AlertDialog.Builder(mContext, 0);
                                    builder.setTitle(R.string.string_393);
                                    builder.setMessage(mContext.getString(R.string.string_506) + " " + message.getSize() + " " + mContext.getString(R.string.string_534));
                                    builder.setPositiveButton(R.string.string_580, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {


                                            String receiverUid = message.getReceiverUid();

                                            String messageId = message.getMessageId();


                                            message.setDownloading(true);


                                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //notifyItemChanged(viewHolder.getAdapterPosition());

                                                    notifyDataSetChanged();
                                                }
                                            });

                                            download(message.getImagePath(), message.getThumbnailPath(),
                                                    mContext.getExternalFilesDir(null) + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".jpg",
                                                    AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId), message, vh2);


                                            // dialog.dismiss();

                                            Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                                            if (context instanceof Activity) {


                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                        dialog.dismiss();
                                                    }
                                                } else {


                                                    if (!((Activity) context).isFinishing()) {
                                                        dialog.dismiss();
                                                    }
                                                }
                                            } else {


                                                try {
                                                    dialog.dismiss();
                                                } catch (final IllegalArgumentException e) {
                                                    e.printStackTrace();

                                                } catch (final Exception e) {
                                                    e.printStackTrace();

                                                }
                                            }


                                        }
                                    });
                                    builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {


                                            dialog.cancel();

                                        }
                                    });
                                    builder.show();
                                } else {


                                    /*
                                     *
                                     * have to request permission
                                     *
                                     * */


                                    requestStorageAccessPermission("image");

                                }
                            } else {


                                Snackbar snackbar = Snackbar.make(root, R.string.string_38, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                            }

                        }
                    });
                }


            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }


//            if (!message.isTimerStarted()) {
//                try {
//                    message.setTimerStarted(true);
//
//                    if (message.getdTime() > 0) {
//
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//
//                                try {
//                                    ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//                                } catch (NullPointerException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }, (message.getdTime() * SECTOMILLSEC));
//
//                        ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
//                    }
//
//                } catch (IllegalStateException e) {
//                    e.printStackTrace();
//                }
//            }
            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderVideoReceived(final ViewHolderVideoReceived vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {

            if(((SecretChatMessageScreen)mContext).receiverImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);
                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).receiverImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            vh2.forward.setVisibility(View.GONE);
            vh2.fnf.setVisibility(View.GONE);

//            vh2.time.setTypeface(tf, Typeface.ITALIC);
//
//            vh2.date.setTypeface(tf, Typeface.ITALIC);
            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");

            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");


            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });

            try {


                if (message.getDownloadStatus() == 1) {

                    /*
                     *
                     * video already downloaded
                     *
                     * */
                    vh2.download.setVisibility(View.GONE);
                    vh2.progressBar.setVisibility(View.GONE);
                    vh2.progressBar2.setVisibility(View.GONE);
                    vh2.cancel.setVisibility(View.GONE);

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {


                        final File f = new File(message.getVideoPath());


                        if (f.exists()) {


                            thumbnail = ThumbnailUtils.createVideoThumbnail(message.getVideoPath(),
                                    MediaStore.Images.Thumbnails.MINI_KIND);


                            vh2.thumbnail.setImageBitmap(thumbnail);
                            vh2.thumbnail.setOnClickListener(new View.OnClickListener() {
                                                                 public void onClick(View v) {


                                                                     try {
//                                        Intent intent = new Intent();
//                                        intent.setAction(Intent.ACTION_VIEW);
//
//                                        intent.setDataAndType(Uri.fromFile(f), "video/*");
//
//                                        mContext.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext).toBundle());


                                                                         Uri intentUri;
                                                                         if (Build.VERSION.SDK_INT >= 24) {
                                                                             intentUri = Uri.parse(message.getVideoPath());
                                                                         } else {
                                                                             intentUri = Uri.fromFile(f);
                                                                         }


                                                                         Intent intent = new Intent();
                                                                         intent.setAction(Intent.ACTION_VIEW);


                                                                         intent.setDataAndType(intentUri, "video/*");

                                                                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                                                                             intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                                                                         } else {


                                                                             List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                                                                             for (ResolveInfo resolveInfo : resInfoList) {
                                                                                 String packageName = resolveInfo.activityInfo.packageName;
                                                                                 mContext.grantUriPermission(packageName, intentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                                             }


                                                                         }


                                                                         mContext.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext).toBundle());


                                                                     } catch (ActivityNotFoundException e) {
                                                                         Intent i = new Intent(mContext, MediaHistory_FullScreenVideo.class);
                                                                         i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                                         i.putExtra("videoPath", message.getVideoPath());
                                                                         mContext.startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext).toBundle());


                                                                     }
                                                                 }
                                                             }

                            );

                        } else {

                            Glide.clear(vh2.thumbnail);
                            vh2.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                            vh2.thumbnail.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));

                            //    vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                            vh2.fnf.setVisibility(View.VISIBLE);


                        }


                    } else {
                        Glide.clear(vh2.thumbnail);
                        vh2.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                        vh2.thumbnail.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                        vh2.fnf.setVisibility(View.VISIBLE);
                        //     vh2.fnf.setTypeface(tf, Typeface.NORMAL);

                        vh2.fnf.setText(R.string.string_211);


                        vh2.thumbnail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                                intent.setData(uri);
                                mContext.startActivity(intent);
                            }
                        });

                    }

                    if (!message.isTimerStarted()) {
                        try {
                            message.setTimerStarted(true);

                            if (message.getdTime() > 0) {


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {


                                        try {
                                            ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, (message.getdTime() * SECTOMILLSEC));

                                ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                            }


                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }
                } else {


                    if (message.isDownloading()) {


                        vh2.download.setVisibility(View.GONE);


                        vh2.progressBar2.setVisibility(View.VISIBLE);

                        vh2.progressBar.setVisibility(View.GONE);
                        vh2.cancel.setVisibility(View.VISIBLE);


                    } else {
                        vh2.download.setVisibility(View.VISIBLE);
                        vh2.progressBar2.setVisibility(View.GONE);
                        vh2.cancel.setVisibility(View.GONE);
                        vh2.progressBar.setVisibility(View.GONE);
                    }
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getThumbnailPath())
                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext))
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        vh2.thumbnail.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                                        return false;
                                    }
                                })
                                .into(vh2.thumbnail);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.thumbnail.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {


                            /* ppopup to ask if wanna download
                             *
                             *
                             * */


                            if (!message.isDownloading()) {
                                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {


                                    AlertDialog.Builder builder =
                                            new AlertDialog.Builder(mContext, 0);
                                    builder.setTitle(R.string.string_393);
                                    builder.setMessage(mContext.getString(R.string.string_506) + " " + message.getSize() + " " + mContext.getString(R.string.string_535));
                                    builder.setPositiveButton(R.string.string_578, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {


                                            String messageId = message.getMessageId();

                                            String receiverUid = message.getReceiverUid();
                                            message.setDownloading(true);


                                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //notifyItemChanged(viewHolder.getAdapterPosition());

                                                    notifyDataSetChanged();
                                                }
                                            });


                                            download(message.getVideoPath(), message.getThumbnailPath(), mContext.getExternalFilesDir(null) + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".mp4", AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId), message, vh2);

                                            //    dialog.dismiss();


                                            Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                                            if (context instanceof Activity) {


                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                        dialog.dismiss();
                                                    }
                                                } else {


                                                    if (!((Activity) context).isFinishing()) {
                                                        dialog.dismiss();
                                                    }
                                                }
                                            } else {


                                                try {
                                                    dialog.dismiss();
                                                } catch (final IllegalArgumentException e) {
                                                    e.printStackTrace();

                                                } catch (final Exception e) {
                                                    e.printStackTrace();

                                                }
                                            }


                                        }
                                    });
                                    builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {


                                            dialog.cancel();

                                        }
                                    });
                                    builder.show();


                                } else {


                                    /*
                                     *
                                     * have to request permission
                                     *
                                     * */


                                    requestStorageAccessPermission("video");


                                }
                            } else {

                                Snackbar snackbar = Snackbar.make(root, R.string.string_38, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                            }

                        }
                    });


                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }

//            if (!message.isTimerStarted()) {
//                try {
//                    message.setTimerStarted(true);
//
//                    if (message.getdTime() > 0) {
//
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//
//                                try {
//                                    ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                                } catch (NullPointerException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }, (message.getdTime() * SECTOMILLSEC));
//
//                        ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
//                    }
//
//
//                } catch (IllegalStateException e) {
//                    e.printStackTrace();
//                }
//            }
            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }


    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")
    private void configureViewHolderLocationReceived(ViewHolderLocationReceived vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {

            if(((SecretChatMessageScreen)mContext).receiverImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);
                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).receiverImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            vh2.forward.setVisibility(View.GONE);
            //   vh2.senderName.setText(message.getSenderName());


//            vh2.time.setTypeface(tf, Typeface.ITALIC);
//
//            vh2.date.setTypeface(tf, Typeface.ITALIC);


            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");


            if (vh2.mMap != null)

                vh2.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            String args[] = message.getPlaceInfo().split("@@");

            String LatLng = args[0];

            String[] parts = LatLng.split(",");

            String lat = parts[0].substring(1);
            String lng = parts[1].substring(0, parts[1].length() - 1);
            args = null;
            parts = null;

            vh2.positionSelected = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));


//            vh2.mapView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//
//                    android.support.v7.app.ProgressDialog.Builder builder =
//                            new android.support.v7.app.ProgressDialog.Builder(mContext, 0);
//
//                    LayoutInflater inflater = LayoutInflater.from(mContext);
//                    final View dialogView = inflater.inflate(R.layout.location_popup, null);
//
//
//                    builder.setView(dialogView);
//
//
//                    TextView name = (TextView) dialogView.findViewById(R.id.Name);
//
//                    TextView address = (TextView) dialogView.findViewById(R.id.Address);
//
//                    TextView latlng = (TextView) dialogView.findViewById(R.id.LatLng);
//
//
//                    name.setText(mContext.getString(R.string.string_346) + " " + args[1]);
//                    address.setText(mContext.getString(R.string.string_347) + " " + args[2]);
//                    latlng.setText(mContext.getString(R.string.string_348) + " " + args[0]);
//
//
//                    builder.setTitle(R.string.string_395);
//
//
//                    builder.setPositiveButton(R.string.string_581, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//
//
//                            try {
//
//
//                                String LatLng = args[0];
//
//                                String[] parts = LatLng.split(",");
//
//                                String lat = parts[0].substring(1);
//                                String lng = parts[1].substring(0, parts[1].length() - 1);
//
//
//                                String uri = "geo:" + lat + ","
//                                        + lng + "?q=" + lat
//                                        + "," + lng;
//                                mContext.startActivity(new Intent(Intent.ACTION_VIEW,
//                                                Uri.parse(uri)),
//                                        ActivityOptionsCompat.makeSceneTransitionAnimation((SecretChatMessageScreen) mContext).toBundle());
//
//                                uri = null;
//                                lat = null;
//                                lng = null;
//                                parts = null;
//                                LatLng = null;
//
//
//                            } catch (ActivityNotFoundException e) {
//                                if (root != null) {
//
//                                    Snackbar snackbar = Snackbar.make(root, R.string.string_34, Snackbar.LENGTH_SHORT);
//
//
//                                    snackbar.show();
//                                    View view2 = snackbar.getView();
//                                    TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
//                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                                }
//                            }
//
//
//                            //  dialog.dismiss();
//
//
//                            Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();
//
//
//                            if (context instanceof Activity) {
//
//
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
//                                        dialog.dismiss();
//                                    }
//                                } else {
//
//
//                                    if (!((Activity) context).isFinishing()) {
//                                        dialog.dismiss();
//                                    }
//                                }
//                            } else {
//
//
//                                try {
//                                    dialog.dismiss();
//                                } catch (final IllegalArgumentException e) {
//                                    e.printStackTrace();
//
//                                } catch (final Exception e) {
//                                    e.printStackTrace();
//
//                                }
//                            }
//
//
//                        }
//                    });
//                    builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//
//
//                            dialog.cancel();
//
//                        }
//                    });
//                    builder.show();
//
//
//                }
//            });


            if (!message.isTimerStarted()) {
                try {
                    message.setTimerStarted(true);

                    if (message.getdTime() > 0) {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

//                                db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                                ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mListData.remove(position);
//                                        if (position == 0) {
//                                            notifyDataSetChanged();
//                                        } else {
//                                            notifyItemRemoved(position);
//                                        }
//                                    }
//                                });

                                try {
                                    ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, (message.getdTime() * SECTOMILLSEC));

                        ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                    }

                    //   ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);


                    // AppController.getInstance().addActiveTimer(documentId, message.getMessageId());

//                        else {
//
//                            //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                            db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mListData.remove(position);
//                                    if (position == 0) {
//                                        notifyDataSetChanged();
//                                    } else {
//                                        notifyItemRemoved(position);
//                                    }
//                                }
//                            });
//
//                        }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }
            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderContactReceived(ViewHolderContactReceived vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {

            if(((SecretChatMessageScreen)mContext).receiverImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);
                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).receiverImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            vh2.forward.setVisibility(View.GONE);

//            vh2.senderName.setText(message.getSenderName());

//            vh2.time.setTypeface(tf, Typeface.ITALIC);
//
//            vh2.date.setTypeface(tf, Typeface.ITALIC);
            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");
//            vh2.contactName.setTypeface(tf, Typeface.NORMAL);
//
//            vh2.contactNumber.setTypeface(tf, Typeface.NORMAL);
            String contactInfo = message.getContactInfo();


            String contactName = null, contactNumber = null;


            try {

                String parts[] = contactInfo.split("@@");

                contactName = parts[0];


                String arr[] = parts[1].split("/");


                contactNumber = arr[0];
                arr = null;
                parts = null;

            } catch (StringIndexOutOfBoundsException e) {
                vh2.contactNumber.setText(R.string.string_246);
            } catch (Exception e) {
                vh2.contactNumber.setText(R.string.string_246);
            }


            try {


                vh2.contactName.setText(contactName);

                vh2.contactNumber.setText(contactNumber);


            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            if (contactName == null || contactName.isEmpty()) {
                vh2.contactName.setText(R.string.string_247);
            } else if (contactNumber == null || contactNumber.isEmpty()) {
                vh2.contactNumber.setText(R.string.string_246);
            }
            if (!message.isTimerStarted()) {
                try {
                    message.setTimerStarted(true);

                    if (message.getdTime() > 0) {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

//                                db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                                ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mListData.remove(position);
//                                        if (position == 0) {
//                                            notifyDataSetChanged();
//                                        } else {
//                                            notifyItemRemoved(position);
//                                        }
//                                    }
//                                });
                                try {
                                    ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, (message.getdTime() * SECTOMILLSEC));

                        ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                    }


                    //   ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);

                    // AppController.getInstance().addActiveTimer(documentId, message.getMessageId());

//                        else {
//
//                            //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                            db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mListData.remove(position);
//                                    if (position == 0) {
//                                        notifyDataSetChanged();
//                                    } else {
//                                        notifyItemRemoved(position);
//                                    }
//                                }
//                            });
//
//                        }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }


            vh2.contact_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((SecretChatMessageScreen) mContext).checkWriteContactPermission(message.getContactInfo());
                }
            });

            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")


    private void configureViewHolderAudioReceived(final ViewHolderAudioReceived vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);


        if (message != null) {

            if(((SecretChatMessageScreen)mContext).receiverImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);
                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).receiverImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }


            vh2.forward.setVisibility(View.GONE);
            vh2.playButton.setVisibility(View.VISIBLE);
            vh2.fnf.setVisibility(View.GONE);

//            vh2.time.setTypeface(tf, Typeface.ITALIC);
//
//            vh2.date.setTypeface(tf, Typeface.ITALIC);

            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");
            //    vh2.tv.setTypeface(tf, Typeface.NORMAL);


            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");


            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });


            if (message.getDownloadStatus() == 0) {


                if (message.isDownloading()) {


                    vh2.download.setVisibility(View.GONE);


                    vh2.progressBar2.setVisibility(View.VISIBLE);

                    vh2.progressBar.setVisibility(View.GONE);
                    vh2.cancel.setVisibility(View.VISIBLE);

                } else {
                    vh2.download.setVisibility(View.VISIBLE);


                    vh2.progressBar.setVisibility(View.GONE);
                    vh2.cancel.setVisibility(View.GONE);

                    vh2.progressBar2.setVisibility(View.GONE);

                }


                vh2.playButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {

                            if (!message.isDownloading()) {
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(mContext, 0);
                                builder.setTitle(R.string.string_393);
                                builder.setMessage(mContext.getString(R.string.string_506) + " " + message.getSize() + " " + mContext.getString(R.string.string_538));
                                builder.setPositiveButton(R.string.string_578, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        String messageId = message.getMessageId();
                                        message.setDownloading(true);


                                        ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getAudioPath(), null, mContext.getExternalFilesDir(null) + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".3gp", AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId), message, vh2);

                                        //  dialog.dismiss();


                                        Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                                        if (context instanceof Activity) {


                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                    dialog.dismiss();
                                                }
                                            } else {


                                                if (!((Activity) context).isFinishing()) {
                                                    dialog.dismiss();
                                                }
                                            }
                                        } else {


                                            try {
                                                dialog.dismiss();
                                            } catch (final IllegalArgumentException e) {
                                                e.printStackTrace();

                                            } catch (final Exception e) {
                                                e.printStackTrace();

                                            }
                                        }


                                    }
                                });
                                builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        dialog.cancel();

                                    }
                                });
                                builder.show();

                            } else {


                                Snackbar snackbar = Snackbar.make(root, R.string.string_38, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                            }
                        } else

                        {


                            /*
                             * have to request permission
                             *
                             * */


                            requestStorageAccessPermission("audio");
                        }
                    }
                });


            } else {
                vh2.download.setVisibility(View.GONE);


                vh2.progressBar.setVisibility(View.GONE);


                vh2.cancel.setVisibility(View.GONE);

                vh2.progressBar2.setVisibility(View.GONE);


                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {


                    final File file = new File(message.getAudioPath());


                    if (file.exists()) {


                        vh2.playButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {


                                try {
//                                    Intent intent = new Intent();
//                                    intent.setAction(Intent.ACTION_VIEW);
//
//                                    intent.setDataAndType(Uri.fromFile(file), "audio/*");
//                                    intent.setPackage("com.google.android.music");
//                                    mContext.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext).toBundle());


                                    Uri intentUri;
                                    if (Build.VERSION.SDK_INT >= 24) {
                                        intentUri = Uri.parse(message.getAudioPath());
                                    } else {
                                        intentUri = Uri.fromFile(file);
                                    }


                                    Intent intent = new Intent();
                                    intent.setAction(Intent.ACTION_VIEW);

                                    intent.setDataAndType(intentUri, "audio/*");


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                                    } else {


                                        List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                                        for (ResolveInfo resolveInfo : resInfoList) {
                                            String packageName = resolveInfo.activityInfo.packageName;
                                            mContext.grantUriPermission(packageName, intentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        }


                                    }


                                    intent.setPackage("com.google.android.music");
                                    mContext.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext).toBundle());
                                } catch (ActivityNotFoundException e) {


//                                    ProgressDialog.Builder builder =
//                                            new ProgressDialog.Builder(mContext, 0);
//                                    builder.setTitle(R.string.string_394);
//                                    builder.setMessage(mContext.getString(R.string.string_483));
//                                    builder.setPositiveButton(R.string.string_582, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int id) {
//
//
//                                            mediaPlayer = new MediaPlayer();
//
//                                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//
//                                            try {
//                                                mediaPlayer.setDataSource(mContext, Uri.fromFile(file));
//                                                mediaPlayer.prepare();
//                                            } catch (IOException er) {
//                                                er.printStackTrace();
//                                            }
//
//                                            mediaPlayer.start();
//
//                                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//
//                                                @Override
//
//                                                public void onCompletion(MediaPlayer mp) {
//
//                                                    // TODO Auto-generated method stub
//
//                                                    mediaPlayer.release();
//
//                                                    mediaPlayer = null;
//
//                                                }
//
//                                            });
//
//
//                                            // dialog.dismiss();
//
//
//                                            Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();
//
//
//                                            if (context instanceof Activity) {
//
//
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
//                                                        dialog.dismiss();
//                                                    }
//                                                } else {
//
//
//                                                    if (!((Activity) context).isFinishing()) {
//                                                        dialog.dismiss();
//                                                    }
//                                                }
//                                            } else {
//
//
//                                                try {
//                                                    dialog.dismiss();
//                                                } catch (final IllegalArgumentException e) {
//                                                    e.printStackTrace();
//
//                                                } catch (final Exception e) {
//                                                    e.printStackTrace();
//
//                                                }
//                                            }
//
//
//                                        }
//                                    });
//                                    builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int id) {
//
//
//                                            dialog.cancel();
//
//                                        }
//                                    });
//                                    builder.show();
//
                                    try {
                                        final AlertDialog.Builder builder =
                                                new AlertDialog.Builder(mContext, 0);

                                        LayoutInflater inflater = LayoutInflater.from(mContext);
                                        final View dialogView = inflater.inflate(R.layout.dialog_audio_player, null);


                                        builder.setView(dialogView);

                                        Uri intentUri;
                                        if (Build.VERSION.SDK_INT >= 24) {
                                            intentUri = Uri.parse(message.getAudioPath());
                                        } else {
                                            intentUri = Uri.fromFile(file);
                                        }


                                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                AudioWife.getInstance().release();
                                            }
                                        });
                                        final AlertDialog dlg = builder.create();
                                        // dlg.setView(dialogView, 0, 0, 0, 0);
                                        AudioWife.getInstance()

                                                .init(mContext, intentUri)


                                                .setPlayView(dialogView.findViewById(R.id.play))
                                                .setPauseView(dialogView.findViewById(R.id.pause))
                                                .setSeekBar((SeekBar) dialogView.findViewById(R.id.media_seekbar))
                                                .setRuntimeView((TextView) dialogView.findViewById(R.id.run_time))
                                                .setTotalTimeView((TextView) dialogView.findViewById(R.id.playback_time))
                                                .addOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                                                    @Override
                                                    public void onCompletion(MediaPlayer mp) {
                                                        dlg.dismiss();
                                                    }
                                                }).play();


                                        dlg.show();
                                        try {
                                            dlg.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, (120 * density));
                                        } catch (NullPointerException ef) {
                                            ef.printStackTrace();
                                        }

                                    } catch (Exception ef) {
                                        ef.printStackTrace();
                                    }
                                }

                            }
                        });
                    } else {

                        //   vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                        vh2.playButton.setVisibility(View.GONE);
                        vh2.fnf.setVisibility(View.VISIBLE);

                    }
                } else {

                    // vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                    vh2.playButton.setVisibility(View.GONE);
                    vh2.fnf.setVisibility(View.VISIBLE);
                    vh2.fnf.setText(R.string.string_211);


                    vh2.fnf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                            intent.setData(uri);
                            mContext.startActivity(intent);
                        }
                    });


                }
                if (!message.isTimerStarted()) {
                    try {
                        message.setTimerStarted(true);

                        if (message.getdTime() > 0) {


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, (message.getdTime() * SECTOMILLSEC));
                            ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);

                        }


                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }


//            if (!message.isTimerStarted()) {
//                try {
//                    message.setTimerStarted(true);
//
//                    if (message.getdTime() > 0) {
//
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                try {
//                                    ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                                } catch (NullPointerException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }, (message.getdTime() * SECTOMILLSEC));
//                        ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
//
//                    }
//
//
//                } catch (IllegalStateException e) {
//                    e.printStackTrace();
//                }
//            }

            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }


    }


    private void configureViewHolderMessageSent(ViewHolderMessageSent vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {


            //    vh2.senderName.setText(message.getSenderName());

//            vh2.time.setTypeface(tf, Typeface.ITALIC);

            vh2.forward.setVisibility(View.GONE);
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");
            //    vh2.date.setTypeface(tf, Typeface.ITALIC);
            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");
            //   vh2.message.setTypeface(tf, Typeface.NORMAL);
            try {
                vh2.message.setText(message.getTextMessage());
                LinkUtils.autoLink(vh2.message, null);

                vh2.message.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {

                        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label", message.getTextMessage());
                        clipboard.setPrimaryClip(clip);


                        Toast toast = Toast.makeText(mContext, "Message Copied", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                        final Vibrator vibe = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
                        vibe.vibrate(200);

                        return true;
                    }
                });
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }


            String status = message.getDeliveryStatus();

            if (status.equals("3")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.VISIBLE);


                if (!message.isTimerStarted()) {


                    try {

                        message.setTimerStarted(true);

                        if (message.getdTime() > 0) {


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

//                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

//                                    db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mListData.remove(position);
//                                            if (position == 0) {
//                                                notifyDataSetChanged();
//                                            } else {
//                                                notifyItemRemoved(position);
//                                            }
//                                        }
//                                    });

                                    try {
                                        ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, (message.getdTime() * SECTOMILLSEC));

                            ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        }
                        //  AppController.getInstance().addActiveTimer(documentId, message.getMessageId());


                        //        ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);


//                        else {
//
//                            //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                            db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mListData.remove(position);
//                                    if (position == 0) {
//                                        notifyDataSetChanged();
//                                    } else {
//                                        notifyItemRemoved(position);
//                                    }
//                                }
//                            });
//                        }


                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }


            } else if (status.equals("2")) {
                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.VISIBLE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else if (status.equals("1")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.VISIBLE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else {


                vh2.clock.setVisibility(View.VISIBLE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);


            }
            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }

            if(((SecretChatMessageScreen)mContext).userImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);

                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).userImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderImageSent(final ViewHolderImageSent vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {

            if(((SecretChatMessageScreen)mContext).userImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);
                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).userImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            vh2.forward.setVisibility(View.GONE);
            vh2.fnf.setVisibility(View.GONE);

            //vh2.time.setTypeface(tf, Typeface.ITALIC);
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");
            //vh2.date.setTypeface(tf, Typeface.ITALIC);
            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");

            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });
            if (message.getDownloadStatus() == 1) {



                /*
                 *
                 * image already downloaded
                 *
                 * */
                vh2.progressBar2.setVisibility(View.GONE);

                vh2.progressBar.setVisibility(View.GONE);
                vh2.download.setVisibility(View.GONE);
                vh2.cancel.setVisibility(View.GONE);

                if (message.getImagePath() != null) {


                    try {


                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {


                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inJustDecodeBounds = true;
                            BitmapFactory.decodeFile(message.getImagePath(), options);


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
                                        .with(mContext)
                                        .load(message.getImagePath())
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
                                                vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                                                return false;
                                            }
                                        })
                                        .into(vh2.imageView);

                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }

                            vh2.imageView.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {


                                    Intent i = new Intent(mContext, MediaHistory_FullScreenImage.class);

                                    i.putExtra("imagePath", message.getImagePath());

                                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    ActivityOptionsCompat options = ActivityOptionsCompat.
                                            makeSceneTransitionAnimation((Activity) mContext, vh2.imageView, "image");
                                    mContext.startActivity(i, options.toBundle());


                                }
                            });
                        } else {

                            vh2.fnf.setVisibility(View.VISIBLE);

                            //  vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                            vh2.fnf.setText(R.string.string_211);
                            Glide.clear(vh2.imageView);
                            vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                            vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));

                            vh2.imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                                    intent.setData(uri);
                                    mContext.startActivity(intent);
                                }
                            });


                        }


                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    } catch (Exception e) {


                        vh2.fnf.setVisibility(View.VISIBLE);
                        Glide.clear(vh2.imageView);
                        vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                        vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                        // vh2.fnf.setTypeface(tf, Typeface.NORMAL);


                    }
                } else {


                    vh2.imageView.setImageURI(message.getImageUrl());
                }
            } else {


                if (message.isDownloading()) {


                    vh2.cancel.setVisibility(View.VISIBLE);


                    vh2.download.setVisibility(View.GONE);


                    vh2.progressBar2.setVisibility(View.VISIBLE);

                    vh2.progressBar.setVisibility(View.GONE);


                } else {
                    vh2.download.setVisibility(View.VISIBLE);

                    vh2.progressBar2.setVisibility(View.GONE);
                    vh2.progressBar.setVisibility(View.GONE);
                    vh2.cancel.setVisibility(View.GONE);
                }

                final BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;


                BitmapFactory.decodeFile(message.getThumbnailPath(), options);


                int height = options.outHeight;
                int width = options.outWidth;


                int reqHeight;


//                    reqHeight = ((150 * height) / width);


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
                            .with(mContext)
                            .load(message.getThumbnailPath())


                            .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext))


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
                                    vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                                    return false;
                                }
                            })


                            .into(vh2.imageView);

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                vh2.imageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        if (!message.isDownloading()) {
                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {


                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(mContext, 0);
                                builder.setTitle(R.string.string_393);
                                builder.setMessage(mContext.getString(R.string.string_506) + " " + message.getSize() + " " + mContext.getString(R.string.string_534));
                                builder.setPositiveButton(R.string.string_580, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        String messageId = message.getMessageId();


                                        message.setDownloading(true);


                                        ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getImagePath(), message.getThumbnailPath(),
                                                mContext.getExternalFilesDir(null) + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".jpg",
                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId), message, vh2);


                                        // dialog.dismiss();

                                        Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                                        if (context instanceof Activity) {


                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                    dialog.dismiss();
                                                }
                                            } else {


                                                if (!((Activity) context).isFinishing()) {
                                                    dialog.dismiss();
                                                }
                                            }
                                        } else {


                                            try {
                                                dialog.dismiss();
                                            } catch (final IllegalArgumentException e) {
                                                e.printStackTrace();

                                            } catch (final Exception e) {
                                                e.printStackTrace();

                                            }
                                        }


                                    }
                                });
                                builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        dialog.cancel();

                                    }
                                });
                                builder.show();
                            } else {


                                /*
                                 *
                                 * have to request permission
                                 *
                                 * */


                                requestStorageAccessPermission("image");

                            }
                        } else {


                            Snackbar snackbar = Snackbar.make(root, R.string.string_38, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                        }

                    }
                });
            }

            String status = message.getDeliveryStatus();

            if (status.equals("3")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.VISIBLE);


                if (!message.isTimerStarted()) {
                    try {
                        message.setTimerStarted(true);

                        if (message.getdTime() > 0) {


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

//                                    db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mListData.remove(position);
//                                            if (position == 0) {
//                                                notifyDataSetChanged();
//                                            } else {
//                                                notifyItemRemoved(position);
//                                            }
//                                        }
//                                    });

                                    try {
                                        ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, (message.getdTime() * SECTOMILLSEC));

                            ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        }

                        //  ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);

                        //    AppController.getInstance().addActiveTimer(documentId, message.getMessageId());
//                        else {
//
//                            //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                            db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mListData.remove(position);
//                                    if (position == 0) {
//                                        notifyDataSetChanged();
//                                    } else {
//                                        notifyItemRemoved(position);
//                                    }
//                                }
//                            });
//
//                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }

            } else if (status.equals("2")) {
                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.VISIBLE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else if (status.equals("1")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.VISIBLE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else {


                vh2.clock.setVisibility(View.VISIBLE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            }
            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }

        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")


    private void configureViewHolderVideoSent(final ViewHolderVideoSent vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {

            if(((SecretChatMessageScreen)mContext).userImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);

                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).userImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            vh2.forward.setVisibility(View.GONE);

            vh2.fnf.setVisibility(View.GONE);

            //   vh2.time.setTypeface(tf, Typeface.ITALIC);
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");
            // vh2.date.setTypeface(tf, Typeface.ITALIC);
            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");


            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });


            if (message.getDownloadStatus() == 1) {

                /*
                 *
                 * video already downloaded
                 *
                 * */
                vh2.download.setVisibility(View.GONE);
                vh2.progressBar.setVisibility(View.GONE);
                vh2.progressBar2.setVisibility(View.GONE);
                vh2.cancel.setVisibility(View.GONE);


                try {


                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {


                        final File file = new File(message.getVideoPath());


                        if (file.exists()) {

                            thumbnail = ThumbnailUtils.createVideoThumbnail(message.getVideoPath(),
                                    MediaStore.Images.Thumbnails.MINI_KIND);


                            vh2.thumbnail.setImageBitmap(thumbnail);


                            vh2.thumbnail.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {


                                    try {
//                                    Intent intent = new Intent();
//                                    intent.setAction(Intent.ACTION_VIEW);
//
//                                    intent.setDataAndType(Uri.fromFile(file), "video/*");
//
//                                    mContext.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext).toBundle());


                                        Uri intentUri;
                                        if (Build.VERSION.SDK_INT >= 24) {
                                            intentUri = Uri.parse(message.getVideoPath());
                                        } else {
                                            intentUri = Uri.fromFile(file);
                                        }

                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);

                                        intent.setDataAndType(intentUri, "video/*");


                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                                        } else {


                                            List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                                            for (ResolveInfo resolveInfo : resInfoList) {
                                                String packageName = resolveInfo.activityInfo.packageName;
                                                mContext.grantUriPermission(packageName, intentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            }


                                        }


                                        mContext.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext).toBundle());


                                    } catch (ActivityNotFoundException e) {
                                        Intent i = new Intent(mContext, MediaHistory_FullScreenVideo.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        i.putExtra("videoPath", message.getVideoPath());
                                        mContext.startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext).toBundle());


                                    }
                                }
                            });
                        } else {
                            Glide.clear(vh2.thumbnail);
                            vh2.fnf.setVisibility(View.VISIBLE);
                            //   vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                            vh2.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                            vh2.thumbnail.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));

                        }
                    } else {

                        vh2.fnf.setVisibility(View.VISIBLE);
                        Glide.clear(vh2.thumbnail);
                        //vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                        vh2.fnf.setText(R.string.string_211);
                        vh2.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                        vh2.thumbnail.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));


                        vh2.thumbnail.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                                intent.setData(uri);
                                mContext.startActivity(intent);
                            }
                        });

                    }
                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                } catch (Exception e) {


                    e.printStackTrace();

                }
            } else {


                if (message.isDownloading()) {


                    vh2.download.setVisibility(View.GONE);


                    vh2.progressBar2.setVisibility(View.VISIBLE);

                    vh2.progressBar.setVisibility(View.GONE);
                    vh2.cancel.setVisibility(View.VISIBLE);


                } else {
                    vh2.download.setVisibility(View.VISIBLE);
                    vh2.progressBar2.setVisibility(View.GONE);
                    vh2.cancel.setVisibility(View.GONE);
                    vh2.progressBar.setVisibility(View.GONE);
                }
                try {
                    Glide
                            .with(mContext)
                            .load(message.getThumbnailPath())
                            .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext))
                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                            .placeholder(R.drawable.home_grid_view_image_icon)


                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    vh2.thumbnail.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                                    return false;
                                }
                            })
                            .into(vh2.thumbnail);

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                vh2.thumbnail.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {


                        /* ppopup to ask if wanna download
                         *
                         *
                         * */


                        if (!message.isDownloading()) {
                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {


                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(mContext, 0);
                                builder.setTitle(R.string.string_393);
                                builder.setMessage(mContext.getString(R.string.string_506) + " " + message.getSize() + " " + mContext.getString(R.string.string_535));
                                builder.setPositiveButton(R.string.string_578, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String messageId = message.getMessageId();

                                        String receiverUid = message.getReceiverUid();
                                        message.setDownloading(true);


                                        ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });


                                        download(message.getVideoPath(), message.getThumbnailPath(), mContext.getExternalFilesDir(null) + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".mp4", AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId), message, vh2);

                                        //    dialog.dismiss();


                                        Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                                        if (context instanceof Activity) {


                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                    dialog.dismiss();
                                                }
                                            } else {


                                                if (!((Activity) context).isFinishing()) {
                                                    dialog.dismiss();
                                                }
                                            }
                                        } else {


                                            try {
                                                dialog.dismiss();
                                            } catch (final IllegalArgumentException e) {
                                                e.printStackTrace();

                                            } catch (final Exception e) {
                                                e.printStackTrace();

                                            }
                                        }


                                    }
                                });
                                builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        dialog.cancel();

                                    }
                                });
                                builder.show();


                            } else {


                                /*
                                 *
                                 * have to request permission
                                 *
                                 * */


                                requestStorageAccessPermission("video");


                            }
                        } else {

                            Snackbar snackbar = Snackbar.make(root, R.string.string_38, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                        }

                    }
                });
            }

            String status = message.getDeliveryStatus();

            if (status.equals("3")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.VISIBLE);


                if (!message.isTimerStarted()) {
                    try {
                        message.setTimerStarted(true);

                        if (message.getdTime() > 0) {


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

//                                    db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mListData.remove(position);
//                                            if (position == 0) {
//                                                notifyDataSetChanged();
//                                            } else {
//                                                notifyItemRemoved(position);
//                                            }
//                                        }
//                                    });

                                    try {
                                        ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, (message.getdTime() * SECTOMILLSEC));

                            ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        }

                        //     ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);

                        //    AppController.getInstance().addActiveTimer(documentId, message.getMessageId());
//                        else {
//
//                            //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                            db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mListData.remove(position);
//                                    if (position == 0) {
//                                        notifyDataSetChanged();
//                                    } else {
//                                        notifyItemRemoved(position);
//                                    }
//                                }
//                            });
//
//                        }

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }

            } else if (status.equals("2")) {
                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.VISIBLE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else if (status.equals("1")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.VISIBLE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else {


                vh2.clock.setVisibility(View.VISIBLE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            }

            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }

    }

    @SuppressWarnings("TryWithIdenticalCatches,unchecked")
    private void configureViewHolderLocationSent(ViewHolderLocationSent vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {

            if(((SecretChatMessageScreen)mContext).userImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);

                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).userImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            vh2.forward.setVisibility(View.GONE);
            //      vh2.senderName.setText(message.getSenderName());

//            vh2.time.setTypeface(tf, Typeface.ITALIC);
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");
//            vh2.date.setTypeface(tf, Typeface.ITALIC);
            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");


            String args[] = message.getPlaceInfo().split("@@");

            String LatLng = args[0];

            String[] parts = LatLng.split(",");

            String lat = parts[0].substring(1);
            String lng = parts[1].substring(0, parts[1].length() - 1);

            parts = null;
            args = null;
            vh2.positionSelected = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));


            if (vh2.mMap != null) {
                vh2.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

//                vh2.mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(LatLng arg0)
//
//                    {
//
//                        android.support.v7.app.ProgressDialog.Builder builder =
//                                new android.support.v7.app.ProgressDialog.Builder(mContext, 0);
//
//                        LayoutInflater inflater = LayoutInflater.from(mContext);
//                        final View dialogView = inflater.inflate(R.layout.location_popup, null);
//
//
//                        builder.setView(dialogView);
//
//
//                        TextView name = (TextView) dialogView.findViewById(R.id.Name);
//
//                        TextView address = (TextView) dialogView.findViewById(R.id.Address);
//
//                        TextView latlng = (TextView) dialogView.findViewById(R.id.LatLng);
//
//
//                        name.setText(mContext.getString(R.string.string_346) + " " + args[1]);
//                        address.setText(mContext.getString(R.string.string_347) + " " + args[2]);
//                        latlng.setText(mContext.getString(R.string.string_348) + " " + args[0]);
//
//
//                        builder.setTitle(R.string.string_395);
//
//
//                        builder.setPositiveButton(R.string.string_581, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//
//
//                                try {
//
//
//                                    String LatLng = args[0];
//
//                                    String[] parts = LatLng.split(",");
//
//                                    String lat = parts[0].substring(1);
//                                    String lng = parts[1].substring(0, parts[1].length() - 1);
//
//
//                                    String uri = "geo:" + lat + ","
//                                            + lng + "?q=" + lat
//                                            + "," + lng;
//                                    mContext.startActivity(new Intent(Intent.ACTION_VIEW,
//                                                    Uri.parse(uri)),
//                                            ActivityOptionsCompat.makeSceneTransitionAnimation((SecretChatMessageScreen) mContext).toBundle());
//
//                                    uri = null;
//                                    lat = null;
//                                    lng = null;
//                                    parts = null;
//                                    LatLng = null;
//
//
//                                } catch (ActivityNotFoundException e) {
//                                    if (root != null) {
//
//                                        Snackbar snackbar = Snackbar.make(root, R.string.string_34, Snackbar.LENGTH_SHORT);
//
//
//                                        snackbar.show();
//                                        View view2 = snackbar.getView();
//                                        TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
//                                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
//                                    }
//                                }
//
//
//                                //  dialog.dismiss();
//
//
//                                Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();
//
//
//                                if (context instanceof Activity) {
//
//
//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                                        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
//                                            dialog.dismiss();
//                                        }
//                                    } else {
//
//
//                                        if (!((Activity) context).isFinishing()) {
//                                            dialog.dismiss();
//                                        }
//                                    }
//                                } else {
//
//
//                                    try {
//                                        dialog.dismiss();
//                                    } catch (final IllegalArgumentException e) {
//                                        e.printStackTrace();
//
//                                    } catch (final Exception e) {
//                                        e.printStackTrace();
//
//                                    }
//                                }
//
//
//                            }
//                        });
//                        builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int id) {
//
//
//                                dialog.cancel();
//
//                            }
//                        });
//                        builder.show();
//
//
//                    }
//                });
            }
            String status = message.getDeliveryStatus();

            if (status.equals("3")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.VISIBLE);


                if (!message.isTimerStarted()) {
                    try {
                        message.setTimerStarted(true);

                        if (message.getdTime() > 0) {


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

//                                    db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mListData.remove(position);
//                                            if (position == 0) {
//                                                notifyDataSetChanged();
//                                            } else {
//                                                notifyItemRemoved(position);
//                                            }
//                                        }
//                                    });

                                    try {
                                        ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, (message.getdTime() * SECTOMILLSEC));

                            ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        }


                        //     ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        //  AppController.getInstance().addActiveTimer(documentId, message.getMessageId());
//                        else {
//
//                            //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                            db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mListData.remove(position);
//                                    if (position == 0) {
//                                        notifyDataSetChanged();
//                                    } else {
//                                        notifyItemRemoved(position);
//                                    }
//                                }
//                            });
//
//                        }


                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }


            } else if (status.equals("2")) {
                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.VISIBLE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else if (status.equals("1")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.VISIBLE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else {


                vh2.clock.setVisibility(View.VISIBLE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            }

            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")

    private void configureViewHolderContactSent(ViewHolderContactSent vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {

            if(((SecretChatMessageScreen)mContext).userImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);

                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).userImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }


            vh2.forward.setVisibility(View.GONE);
//            vh2.senderName.setText(message.getSenderName());

//            vh2.time.setTypeface(tf, Typeface.ITALIC);
//
//            vh2.date.setTypeface(tf, Typeface.ITALIC);
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");

//            vh2.contactName.setTypeface(tf, Typeface.NORMAL);
//
//            vh2.contactNumber.setTypeface(tf, Typeface.NORMAL);
            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");


            String contactInfo = message.getContactInfo();


            String contactName, contactNumber;

            try {


                String parts[] = contactInfo.split("@@");


                contactName = parts[0];


                String arr[] = parts[1].split("/");


                contactNumber = arr[0];
                arr = null;
                parts = null;

                vh2.contactName.setText(contactName);

                vh2.contactNumber.setText(contactNumber);
                if (contactName == null || contactName.isEmpty()) {
                    vh2.contactName.setText(R.string.string_247);
                } else if (contactNumber == null || contactNumber.isEmpty()) {
                    vh2.contactNumber.setText(R.string.string_246);
                }
            } catch (StringIndexOutOfBoundsException e) {
                vh2.contactNumber.setText(R.string.string_246);
            } catch (Exception e) {
                vh2.contactNumber.setText(R.string.string_246);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }


            String status = message.getDeliveryStatus();

            if (status.equals("3")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.VISIBLE);


                if (!message.isTimerStarted()) {
                    try {
                        message.setTimerStarted(true);

                        if (message.getdTime() > 0) {


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

//                                    db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mListData.remove(position);
//                                            if (position == 0) {
//                                                notifyDataSetChanged();
//                                            } else {
//                                                notifyItemRemoved(position);
//                                            }
//                                        }
//                                    });
                                    try {
                                        ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, (message.getdTime() * SECTOMILLSEC));

                            ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        }


                        //     ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        //     AppController.getInstance().addActiveTimer(documentId, message.getMessageId());
//                        else {
//
//                            //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                            db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mListData.remove(position);
//                                    if (position == 0) {
//                                        notifyDataSetChanged();
//                                    } else {
//                                        notifyItemRemoved(position);
//                                    }
//                                }
//                            });
//                        }


                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            } else if (status.equals("2")) {
                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.VISIBLE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else if (status.equals("1")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.VISIBLE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else {


                vh2.clock.setVisibility(View.VISIBLE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);

            }


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }

            vh2.contact_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SecretChatMessageScreen) mContext).checkWriteContactPermission(message.getContactInfo());
                }
            });

        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderAudioSent(final ViewHolderAudioSent vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {

            if(((SecretChatMessageScreen)mContext).userImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);

                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).userImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            vh2.forward.setVisibility(View.GONE);

            vh2.fnf.setVisibility(View.GONE);


            vh2.playButton.setVisibility(View.VISIBLE);

//            vh2.time.setTypeface(tf, Typeface.ITALIC);
//
//
//            vh2.tv.setTypeface(tf, Typeface.NORMAL);
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");

//            vh2.date.setTypeface(tf, Typeface.ITALIC);
            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");

            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });


            if (message.getDownloadStatus() == 1) {


                vh2.download.setVisibility(View.GONE);


                vh2.progressBar.setVisibility(View.GONE);


                vh2.cancel.setVisibility(View.GONE);

                vh2.progressBar2.setVisibility(View.GONE);
                try {


                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {


                        final File file = new File(message.getAudioPath());


                        if (file.exists()) {
                            vh2.playButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {


                                    try {


                                        Uri intentUri;
                                        if (Build.VERSION.SDK_INT >= 24) {
                                            intentUri = Uri.parse(message.getAudioPath());
                                        } else {
                                            intentUri = Uri.fromFile(file);
                                        }

                                        Intent intent = new Intent();
                                        intent.setAction(Intent.ACTION_VIEW);


                                        intent.setDataAndType(intentUri, "audio/*");
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


                                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                                        } else {


                                            List<ResolveInfo> resInfoList = mContext.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                                            for (ResolveInfo resolveInfo : resInfoList) {
                                                String packageName = resolveInfo.activityInfo.packageName;
                                                mContext.grantUriPermission(packageName, intentUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                            }


                                        }


                                        intent.setPackage("com.google.android.music");
                                        mContext.startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext).toBundle());
                                    } catch (ActivityNotFoundException e) {
                                        try {
                                            final AlertDialog.Builder builder =
                                                    new AlertDialog.Builder(mContext, 0);

                                            LayoutInflater inflater = LayoutInflater.from(mContext);
                                            final View dialogView = inflater.inflate(R.layout.dialog_audio_player, null);


                                            builder.setView(dialogView);

                                            Uri intentUri;
                                            if (Build.VERSION.SDK_INT >= 24) {
                                                intentUri = Uri.parse(message.getAudioPath());
                                            } else {
                                                intentUri = Uri.fromFile(file);
                                            }


                                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                                @Override
                                                public void onDismiss(DialogInterface dialogInterface) {
                                                    AudioWife.getInstance().release();
                                                }
                                            });
                                            final AlertDialog dlg = builder.create();
                                            // dlg.setView(dialogView, 0, 0, 0, 0);
                                            AudioWife.getInstance()

                                                    .init(mContext, intentUri)


                                                    .setPlayView(dialogView.findViewById(R.id.play))
                                                    .setPauseView(dialogView.findViewById(R.id.pause))
                                                    .setSeekBar((SeekBar) dialogView.findViewById(R.id.media_seekbar))
                                                    .setRuntimeView((TextView) dialogView.findViewById(R.id.run_time))
                                                    .setTotalTimeView((TextView) dialogView.findViewById(R.id.playback_time))
                                                    .addOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                                                        @Override
                                                        public void onCompletion(MediaPlayer mp) {
                                                            dlg.dismiss();
                                                        }
                                                    }).play();


                                            dlg.show();
                                            try {
                                                dlg.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, (120 * density));
                                            } catch (NullPointerException ef) {
                                                ef.printStackTrace();
                                            }


//                                        ProgressDialog.Builder builder =
//                                                new ProgressDialog.Builder(mContext, 0);
//                                        builder.setTitle(R.string.string_394);
//                                        builder.setMessage(mContext.getString(R.string.string_483));
//                                        builder.setPositiveButton(R.string.string_582, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int id) {
//
//
//                                                mediaPlayer = new MediaPlayer();
//
//                                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//
//
//                                                try {
//                                                    mediaPlayer.setDataSource(mContext, Uri.fromFile(file));
//                                                    mediaPlayer.prepare();
//                                                } catch (IOException er) {
//                                                    er.printStackTrace();
//                                                }
//
//                                                mediaPlayer.start();
//
//                                                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//
//
//                                                    @Override
//
//                                                    public void onCompletion(MediaPlayer mp) {
//
//                                                        // TODO Auto-generated method stub
//
//                                                        mediaPlayer.release();
//
//                                                        mediaPlayer = null;
//
//                                                    }
//
//                                                });
//
//
//                                                //dialog.dismiss();
//                                                Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();
//
//
//                                                if (context instanceof Activity) {
//
//
//                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                                                        if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
//                                                            dialog.dismiss();
//                                                        }
//                                                    } else {
//
//
//                                                        if (!((Activity) context).isFinishing()) {
//                                                            dialog.dismiss();
//                                                        }
//                                                    }
//                                                } else {
//
//
//                                                    try {
//                                                        dialog.dismiss();
//                                                    } catch (final IllegalArgumentException e) {
//                                                        e.printStackTrace();
//
//                                                    } catch (final Exception e) {
//                                                        e.printStackTrace();
//
//                                                    }
//                                                }
//
//
//                                            }
//                                        });
//                                        builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int id) {
//
//
//                                                dialog.cancel();
//
//                                            }
//                                        });
//                                        builder.show();

                                        } catch (Exception ef) {
                                            ef.printStackTrace();
                                        }
                                    }


                                }
                            });
                        } else {

                            vh2.playButton.setVisibility(View.GONE);
                            vh2.fnf.setVisibility(View.VISIBLE);
                            //  vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                        }
                    } else {

                        vh2.playButton.setVisibility(View.GONE);
                        vh2.fnf.setVisibility(View.VISIBLE);
                        //vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                        vh2.fnf.setText(R.string.string_211);

                        vh2.fnf.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                                intent.setData(uri);
                                mContext.startActivity(intent);
                            }
                        });

                    }

                } catch (Exception e) {
                    e.printStackTrace();


                }
            } else {
                if (message.isDownloading()) {


                    vh2.download.setVisibility(View.GONE);


                    vh2.progressBar2.setVisibility(View.VISIBLE);

                    vh2.progressBar.setVisibility(View.GONE);
                    vh2.cancel.setVisibility(View.VISIBLE);

                } else {
                    vh2.download.setVisibility(View.VISIBLE);


                    vh2.progressBar.setVisibility(View.GONE);
                    vh2.cancel.setVisibility(View.GONE);

                    vh2.progressBar2.setVisibility(View.GONE);

                }


                vh2.playButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {


                            if (!message.isDownloading()) {
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(mContext, 0);
                                builder.setTitle(R.string.string_393);
                                builder.setMessage(mContext.getString(R.string.string_506) + " " + message.getSize() + " " + mContext.getString(R.string.string_538));
                                builder.setPositiveButton(R.string.string_578, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        String messageId = message.getMessageId();
                                        message.setDownloading(true);


                                        ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getAudioPath(), null, mContext.getExternalFilesDir(null) + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".3gp", AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId), message, vh2);

                                        //  dialog.dismiss();


                                        Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                                        if (context instanceof Activity) {


                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                    dialog.dismiss();
                                                }
                                            } else {


                                                if (!((Activity) context).isFinishing()) {
                                                    dialog.dismiss();
                                                }
                                            }
                                        } else {


                                            try {
                                                dialog.dismiss();
                                            } catch (final IllegalArgumentException e) {
                                                e.printStackTrace();

                                            } catch (final Exception e) {
                                                e.printStackTrace();

                                            }
                                        }


                                    }
                                });
                                builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        dialog.cancel();

                                    }
                                });
                                builder.show();

                            } else {


                                Snackbar snackbar = Snackbar.make(root, R.string.string_38, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                            }
                        } else

                        {


                            /*
                             * have to request permission
                             *
                             * */


                            requestStorageAccessPermission("audio");
                        }
                    }
                });


            }


            String status = message.getDeliveryStatus();

            if (status.equals("3")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.VISIBLE);


                if (!message.isTimerStarted()) {
                    try {
                        message.setTimerStarted(true);

                        if (message.getdTime() > 0) {


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

//                                    db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mListData.remove(position);
//                                            if (position == 0) {
//                                                notifyDataSetChanged();
//                                            } else {
//                                                notifyItemRemoved(position);
//                                            }
//                                        }
//                                    });
                                    try {
                                        ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, (message.getdTime() * SECTOMILLSEC));

                            ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        }

                        //   ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);

                        //        AppController.getInstance().addActiveTimer(documentId, message.getMessageId());

//                        else {
//
//                            //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                            db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mListData.remove(position);
//                                    if (position == 0) {
//                                        notifyDataSetChanged();
//                                    } else {
//                                        notifyItemRemoved(position);
//                                    }
//                                }
//                            });
//
//                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }

            } else if (status.equals("2")) {
                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.VISIBLE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else if (status.equals("1")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.VISIBLE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else {


                vh2.clock.setVisibility(View.VISIBLE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            }
            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }


    }


    private String convert24to12hourformat(String d) {

        String datein12hour = null;

        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("H:mm", Locale.US);
            final Date dateObj = sdf.parse(d);

            datein12hour = new SimpleDateFormat("h:mm a", Locale.US).format(dateObj);
        } catch (final ParseException e) {
            e.printStackTrace();
        }


        return datein12hour;

    }


    private void download(final String url, final String thumbnailPath, final String filePath,
                          final String receiverDocid, final SecretChatMessageItem message,
                          final RecyclerView.ViewHolder viewHolder) {


        final FileDownloadService downloadService =
                ServiceGenerator.createService(FileDownloadService.class);


        Call<ResponseBody> call = downloadService.downloadFileWithDynamicUrlAsync(url);


        map.put(message.getMessageId(), call);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {


                if (response.isSuccessful()) {
                    new AsyncTask<Void, Long, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {


                            boolean writtenToDisk = writeResponseBodyToDisk(response.body(), filePath, viewHolder, message.getMessageType(), message.getMessageId());


                            message.setDownloading(false);

                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    notifyDataSetChanged();
                                }
                            });


                            if (writtenToDisk) {


                                //  deleteFileFromServer(url);


                                if (thumbnailPath != null) {
                                    /*
                                     *
                                     * incase of image or video delete the thumbnail
                                     *
                                     * */


                                    File fDelete = new File(thumbnailPath);
                                    if (fDelete.exists()) fDelete.delete();


                                }


                                message.setDownloadStatus(1);

                                String type = message.getMessageType();

                                if (type.equals("1")) {
                                    message.setImagePath(filePath);
                                } else if (type.equals("2")) {
                                    message.setVideoPath(filePath);
                                } else if (type.equals("5")) {

                                    message.setAudioPath(filePath);
                                } else if (type.equals("7")) {
                                    /*
                                     * For doodle
                                     */
                                    message.setImagePath(filePath);
                                } else if (type.equals("9")) {
                                    /*
                                     * For document
                                     */
                                    message.setDocumentUrl(filePath);
                                }

                                ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyDataSetChanged();
                                    }
                                });

                                try {
                                    AppController.getInstance().getDbController().updateDownloadStatusAndPath(receiverDocid, filePath, message.getMessageId());
                                } catch (Exception e) {
                                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {

                                            Snackbar snackbar = Snackbar.make(root, R.string.string_39, Snackbar.LENGTH_SHORT);


                                            snackbar.show();
                                            View view = snackbar.getView();
                                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                        }
                                    });
                                }

                            } else {
                                /*
                                 *
                                 * failed to download the file from the server
                                 *
                                 *
                                 * */


                                ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {


                                        Snackbar snackbar = Snackbar.make(root, R.string.string_39, Snackbar.LENGTH_SHORT);


                                        snackbar.show();
                                        View view = snackbar.getView();
                                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                                    }
                                });


                            }


                            return null;


                        }
                    }.execute();


                } else {


                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            message.setDownloading(false);
                            notifyDataSetChanged();


                            Snackbar snackbar = Snackbar.make(root, R.string.string_40, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                        }
                    });
                }


            }

            @Override
            public void onFailure(final Call<ResponseBody> call, Throwable t) {


                t.printStackTrace();


                ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        message.setDownloading(false);


                        notifyDataSetChanged();


                        if (call.isCanceled()) {


                            Snackbar snackbar = Snackbar.make(root, R.string.string_41, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                        } else {


                            Snackbar snackbar = Snackbar.make(root, R.string.string_4, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                        }


                    }
                });


            }

        });


    }


    @SuppressWarnings("all")
    private boolean writeResponseBodyToDisk(ResponseBody body, String filePath,
                                            final RecyclerView.ViewHolder viewHolder, String messageType, final String messageId) {


        fileSizeDownloaded = 0;


        if (messageType.equals("1")) {


            try {
                if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                if (mListData.get(viewHolder.getAdapterPosition()).getMessageId().equals(messageId)) {
                                    if (((ViewHolderImageSent) viewHolder).progressBar.getVisibility() == View.GONE) {
                                        ((ViewHolderImageSent) viewHolder).progressBar.setVisibility(View.VISIBLE);
                                    }
                                }


                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                            ((ViewHolderImageSent) viewHolder).progressBar2.setVisibility(View.GONE);

                        }
                    });


                    ((ViewHolderImageSent) viewHolder).progressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {

                        @Override
                        public void progressToComplete() {


                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    Snackbar snackbar = Snackbar.make(root, R.string.string_42, Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                                }
                            });

                        }
                    });


                } else {

                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                if (mListData.get(viewHolder.getAdapterPosition()).getMessageId().equals(messageId)) {
                                    if (((ViewHolderImageReceived) viewHolder).progressBar.getVisibility() == View.GONE) {
                                        ((ViewHolderImageReceived) viewHolder).progressBar.setVisibility(View.VISIBLE);
                                    }
                                }


                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                            ((ViewHolderImageReceived) viewHolder).progressBar2.setVisibility(View.GONE);

                        }
                    });


                    ((ViewHolderImageReceived) viewHolder).progressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {

                        @Override
                        public void progressToComplete() {


                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    Snackbar snackbar = Snackbar.make(root, R.string.string_42, Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                                }
                            });

                        }
                    });
                }


            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else if (messageType.equals("2")) {


            try {
                if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                if (mListData.get(viewHolder.getAdapterPosition()).getMessageId().equals(messageId)) {

                                    if (((ViewHolderVideoSent) viewHolder).progressBar.getVisibility() == View.GONE) {
                                        ((ViewHolderVideoSent) viewHolder).progressBar.setVisibility(View.VISIBLE);
                                    }
                                }


                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }

                            ((ViewHolderVideoSent) viewHolder).progressBar2.setVisibility(View.GONE);


                        }
                    });


                    ((ViewHolderVideoSent) viewHolder).progressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {

                        @Override
                        public void progressToComplete() {
                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    Snackbar snackbar = Snackbar.make(root, R.string.string_43, Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                                }
                            });

                        }
                    });


                } else {
                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                if (mListData.get(viewHolder.getAdapterPosition()).getMessageId().equals(messageId)) {

                                    if (((ViewHolderVideoReceived) viewHolder).progressBar.getVisibility() == View.GONE) {
                                        ((ViewHolderVideoReceived) viewHolder).progressBar.setVisibility(View.VISIBLE);
                                    }
                                }


                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }

                            ((ViewHolderVideoReceived) viewHolder).progressBar2.setVisibility(View.GONE);


                        }
                    });


                    ((ViewHolderVideoReceived) viewHolder).progressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {

                        @Override
                        public void progressToComplete() {
                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    Snackbar snackbar = Snackbar.make(root, R.string.string_43, Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                                }
                            });

                        }
                    });
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else if (messageType.equals("5")) {
            try {
                if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {

                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {


                                if (mListData.get(viewHolder.getAdapterPosition()).getMessageId().equals(messageId)) {
                                    if (((ViewHolderAudioSent) viewHolder).progressBar.getVisibility() == View.GONE) {
                                        ((ViewHolderAudioSent) viewHolder).progressBar.setVisibility(View.VISIBLE);
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }


                            ((ViewHolderAudioSent) viewHolder).progressBar2.setVisibility(View.GONE);


                        }
                    });


                    ((ViewHolderAudioSent) viewHolder).progressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {

                        @Override
                        public void progressToComplete() {


                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    Snackbar snackbar = Snackbar.make(root, R.string.string_44, Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                                }
                            });


                        }
                    });


                } else {
                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {


                                if (mListData.get(viewHolder.getAdapterPosition()).getMessageId().equals(messageId)) {
                                    if (((ViewHolderAudioReceived) viewHolder).progressBar.getVisibility() == View.GONE) {
                                        ((ViewHolderAudioReceived) viewHolder).progressBar.setVisibility(View.VISIBLE);
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }


                            ((ViewHolderAudioReceived) viewHolder).progressBar2.setVisibility(View.GONE);


                        }
                    });


                    ((ViewHolderAudioReceived) viewHolder).progressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {

                        @Override
                        public void progressToComplete() {


                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    Snackbar snackbar = Snackbar.make(root, R.string.string_44, Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                                }
                            });


                        }
                    });
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else if (messageType.equals("7")) {

            try {

                if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                if (mListData.get(viewHolder.getAdapterPosition()).getMessageId().equals(messageId)) {

                                    if (((ViewHolderDoodleSent) viewHolder).progressBar.getVisibility() == View.GONE) {
                                        ((ViewHolderDoodleSent) viewHolder).progressBar.setVisibility(View.VISIBLE);
                                    }
                                }


                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }

                            ((ViewHolderDoodleSent) viewHolder).progressBar2.setVisibility(View.GONE);


                        }
                    });


                    ((ViewHolderDoodleSent) viewHolder).progressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {

                        @Override
                        public void progressToComplete() {
                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    Snackbar snackbar = Snackbar.make(root, R.string.string_47, Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                                }
                            });

                        }
                    });


                } else {
                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                if (mListData.get(viewHolder.getAdapterPosition()).getMessageId().equals(messageId)) {

                                    if (((ViewHolderDoodleReceived) viewHolder).progressBar.getVisibility() == View.GONE) {
                                        ((ViewHolderDoodleReceived) viewHolder).progressBar.setVisibility(View.VISIBLE);
                                    }
                                }


                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }

                            ((ViewHolderDoodleReceived) viewHolder).progressBar2.setVisibility(View.GONE);


                        }
                    });


                    ((ViewHolderDoodleReceived) viewHolder).progressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {

                        @Override
                        public void progressToComplete() {
                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    Snackbar snackbar = Snackbar.make(root, R.string.string_47, Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                                }
                            });

                        }
                    });
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        } else if (messageType.equals("9")) {

            try {
                if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                if (mListData.get(viewHolder.getAdapterPosition()).getMessageId().equals(messageId)) {
                                    if (((ViewHolderDocumentSent) viewHolder).progressBar.getVisibility() == View.GONE) {
                                        ((ViewHolderDocumentSent) viewHolder).progressBar.setVisibility(View.VISIBLE);
                                    }
                                }


                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                            ((ViewHolderDocumentSent) viewHolder).progressBar2.setVisibility(View.GONE);

                        }
                    });


                    ((ViewHolderDocumentSent) viewHolder).progressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {

                        @Override
                        public void progressToComplete() {


                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    Snackbar snackbar = Snackbar.make(root, R.string.string_498, Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                                }
                            });

                        }
                    });
                } else {
                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            try {
                                if (mListData.get(viewHolder.getAdapterPosition()).getMessageId().equals(messageId)) {
                                    if (((ViewHolderDocumentReceived) viewHolder).progressBar.getVisibility() == View.GONE) {
                                        ((ViewHolderDocumentReceived) viewHolder).progressBar.setVisibility(View.VISIBLE);
                                    }
                                }


                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            }
                            ((ViewHolderDocumentReceived) viewHolder).progressBar2.setVisibility(View.GONE);

                        }
                    });


                    ((ViewHolderDocumentReceived) viewHolder).progressBar.setOnProgressListener(new RingProgressBar.OnProgressListener() {

                        @Override
                        public void progressToComplete() {


                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    Snackbar snackbar = Snackbar.make(root, R.string.string_498, Snackbar.LENGTH_SHORT);


                                    snackbar.show();
                                    View view = snackbar.getView();
                                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);


                                }
                            });

                        }
                    });


                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }

        }
        try {
            // todo change the file location/name according to your needs


            File folder = new File(mContext.getExternalFilesDir(null)  + "/howdoo");


            //  File folder = new File(getFilesDir(),"modaClient/receivedThumbnails");

            if (!folder.exists() && !folder.isDirectory()) {
                folder.mkdirs();
            }

            File file = new File(filePath);


            if (!file.exists()) {
                file.createNewFile();
            }


            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                final long fileSize = body.contentLength();


                inputStream = body.byteStream();
                outputStream = new FileOutputStream(file);


                while (true) {


                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;


                    try {
                        if (messageType.equals("1")) {


                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                                            ((ViewHolderImageSent) viewHolder).progressBar.setProgress((int) ((fileSizeDownloaded * 100) / fileSize));


                                        } else {
                                            ((ViewHolderImageReceived) viewHolder).progressBar.setProgress((int) ((fileSizeDownloaded * 100) / fileSize));
                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } else if (messageType.equals("2")) {


                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                                            ((ViewHolderVideoSent) viewHolder).progressBar.setProgress((int) ((fileSizeDownloaded * 100) / fileSize));


                                        } else {
                                            ((ViewHolderVideoReceived) viewHolder).progressBar.setProgress((int) ((fileSizeDownloaded * 100) / fileSize));

                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        } else if (messageType.equals("5")) {


                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    try {
                                        if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {

                                            ((ViewHolderAudioSent) viewHolder).progressBar.setProgress((int) ((fileSizeDownloaded * 100) / fileSize));


                                        } else {

                                            ((ViewHolderAudioReceived) viewHolder).progressBar.setProgress((int) ((fileSizeDownloaded * 100) / fileSize));

                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } else if (messageType.equals("7")) {


                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {

                                            ((ViewHolderDoodleSent) viewHolder).progressBar.setProgress((int) ((fileSizeDownloaded * 100) / fileSize));


                                        } else {
                                            ((ViewHolderDoodleReceived) viewHolder).progressBar.setProgress((int) ((fileSizeDownloaded * 100) / fileSize));
                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });

                        } else if (messageType.equals("9")) {


                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {

                                            ((ViewHolderDocumentSent) viewHolder).progressBar.setProgress((int) ((fileSizeDownloaded * 100) / fileSize));


                                        } else {
                                            ((ViewHolderDocumentReceived) viewHolder).progressBar.setProgress((int) ((fileSizeDownloaded * 100) / fileSize));
                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        }

                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }

                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }

                try {
                    if (messageType.equals("1")) {


                        ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                                        ((ViewHolderImageSent) viewHolder).progressBar.setVisibility(View.GONE);
                                    } else {
                                        ((ViewHolderImageReceived) viewHolder).progressBar.setVisibility(View.GONE);
                                    }


                                } catch (ArrayIndexOutOfBoundsException e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                    } else if (messageType.equals("2")) {


                        ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                                        ((ViewHolderVideoSent) viewHolder).progressBar.setVisibility(View.GONE);
                                    } else {
                                        ((ViewHolderVideoReceived) viewHolder).progressBar.setVisibility(View.GONE);
                                    }


                                } catch (ArrayIndexOutOfBoundsException e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                    } else if (messageType.equals("5")) {


                        ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                                        ((ViewHolderAudioSent) viewHolder).progressBar.setVisibility(View.GONE);

                                    } else {
                                        ((ViewHolderAudioReceived) viewHolder).progressBar.setVisibility(View.GONE);
                                    }

                                } catch (ArrayIndexOutOfBoundsException e) {
                                    e.printStackTrace();
                                }
                            }

                        });
                    } else if (messageType.equals("7")) {


                        ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {
                                        ((ViewHolderDoodleSent) viewHolder).progressBar.setVisibility(View.GONE);
                                    } else {
                                        ((ViewHolderDoodleReceived) viewHolder).progressBar.setVisibility(View.GONE);
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    e.printStackTrace();
                                }
                            }

                        });


                    } else if (messageType.equals("9")) {


                        ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {
                                        ((ViewHolderDocumentSent) viewHolder).progressBar.setVisibility(View.GONE);
                                    } else {
                                        ((ViewHolderDocumentReceived) viewHolder).progressBar.setVisibility(View.GONE);
                                    }
                                } catch (ArrayIndexOutOfBoundsException e) {
                                    e.printStackTrace();
                                }
                            }

                        });


                    }
                } catch (ClassCastException e) {
                    e.printStackTrace();
                }

            }
        } catch (IOException e)

        {
            return false;
        }

    }

//
//    private void deleteFileFromServer(String url) {
//
//
//        String[] arr = url.split("/");
//
//        JSONObject obj = new JSONObject();
//
//
//        try {
//
//            obj.put("ImageName", arr[arr.length - 1]);
//
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                ApiOnServer.DELETE_DOWNLOAD, obj, new com.android.volley.Response.Listener<JSONObject>() {
//
//
//            @Override
//            public void onResponse(JSONObject response) {
//
//
//            }
//        }, new com.android.volley.Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//
//
//
//            }
//        });
//
//
///*
// *
// *
// * setting timeout to 20 sec
// *
// * */
//
//
//        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
//                20 * 1000, 0,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
///* Add the request to the RequestQueue.*/
//        AppController.getInstance().addToRequestQueue(jsonObjReq, "deleteFileApiRequest");
//
//    }


    private String findOverlayDate(String date) {
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("EEE dd/MMM/yyyy", Locale.US);


            String m1 = "", m2 = "";


            String month1, month2;

            String d1, d2;


            d1 = sdf.format(new Date(System.currentTimeMillis() - AppController.getInstance().getTimeDelta()));

            d2 = date;


            month1 = d1.substring(7, 10);


            month2 = d2.substring(7, 10);

            if (month1.equals("Jan")) {
                m1 = "01";
            } else if (month1.equals("Feb")) {
                m1 = "02";
            } else if (month2.equals("Mar")) {
                m2 = "03";
            } else if (month1.equals("Apr")) {
                m1 = "04";
            } else if (month1.equals("May")) {
                m1 = "05";
            } else if (month1.equals("Jun")) {
                m1 = "06";
            } else if (month1.equals("Jul")) {
                m1 = "07";
            } else if (month1.equals("Aug")) {
                m1 = "08";
            } else if (month1.equals("Sep")) {
                m1 = "09";
            } else if (month1.equals("Oct")) {
                m1 = "10";
            } else if (month1.equals("Nov")) {
                m1 = "11";
            } else if (month1.equals("Dec")) {
                m1 = "12";
            }


            if (month2.equals("Jan")) {
                m2 = "01";
            } else if (month2.equals("Feb")) {
                m2 = "02";
            } else if (month1.equals("Mar")) {
                m1 = "03";
            } else if (month2.equals("Apr")) {
                m2 = "04";
            } else if (month2.equals("May")) {
                m2 = "05";
            } else if (month2.equals("Jun")) {
                m2 = "06";
            } else if (month2.equals("Jul")) {
                m2 = "07";
            } else if (month2.equals("Aug")) {
                m2 = "08";
            } else if (month2.equals("Sep")) {
                m2 = "09";
            } else if (month2.equals("Oct")) {
                m2 = "10";
            } else if (month2.equals("Nov")) {
                m2 = "11";
            } else if (month2.equals("Dec")) {
                m2 = "12";
            }
            month1 = null;
            month2 = null;


            if (sdf.format(new Date(System.currentTimeMillis() - AppController.getInstance().getTimeDelta())).equals(date)) {


                m2 = null;
                m1 = null;
                d2 = null;
                d1 = null;
                sdf = null;
                return "Today";
            } else if ((Integer.parseInt(d1.substring(11) + m1 + d1.substring(4, 6)) - Integer.parseInt(d2.substring(11) + m2 + d2.substring(4, 6))) == 1) {

                m2 = null;
                m1 = null;
                d2 = null;
                d1 = null;
                sdf = null;
                return "Yesterday";

            } else {

                m2 = null;
                m1 = null;
                d2 = null;
                d1 = null;
                sdf = null;
                return date;
            }

        } catch (Exception e) {
            e.printStackTrace();

            return date;
        }
    }


    private void requestStorageAccessPermission(String type) {


        if (ActivityCompat.shouldShowRequestPermissionRationale((SecretChatMessageScreen) mContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar snackbar = Snackbar.make(root, mContext.getString(R.string.string_45, type),
                    Snackbar.LENGTH_INDEFINITE).setAction(mContext.getString(R.string.string_580), new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ActivityCompat.requestPermissions((SecretChatMessageScreen) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            21);
                }
            });


            snackbar.show();


            View view = snackbar.getView();
            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

        } else

        {

            ActivityCompat.requestPermissions((SecretChatMessageScreen) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    21);
        }


    }


    /*
     * View holders for non-sup specific items
     */

    /*********************************************/
    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderGifReceived(final ViewHolderGifReceived vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {

            if(((SecretChatMessageScreen)mContext).receiverImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);
                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).receiverImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            vh2.forward.setVisibility(View.GONE);

//            vh2.time.setTypeface(tf, Typeface.ITALIC);
//
//            vh2.date.setTypeface(tf, Typeface.ITALIC);
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");


            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");
            try {
                Glide.with(mContext)
                        .load(message.getGifUrl())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.home_grid_view_image_icon)
                        .into(vh2.gifStillImage);


                Glide.with(mContext)
                        .load(message.getGifUrl())
                        .asGif()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .crossFade()
                        .into(vh2.gifImage);

            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            try {
                vh2.gifStillImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vh2.gifImage.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                vh2.gifImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mContext, GifPlayer.class);
                        intent.putExtra("gifUrl", message.getGifUrl());

                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity) mContext, vh2.gifImage, "image");
                        mContext.startActivity(intent, options.toBundle());

                        vh2.gifImage.setVisibility(View.GONE);


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!message.isTimerStarted()) {
                try {
                    message.setTimerStarted(true);

                    if (message.getdTime() > 0) {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

//                                db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                                ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mListData.remove(position);
//                                        if (position == 0) {
//                                            notifyDataSetChanged();
//                                        } else {
//                                            notifyItemRemoved(position);
//                                        }
//                                    }
//                                });

                                try {
                                    ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, (message.getdTime() * SECTOMILLSEC));

                        ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                    }
                    //    ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                    //     AppController.getInstance().addActiveTimer(documentId, message.getMessageId());
//                        else {
//
//                            //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                            db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mListData.remove(position);
//                                    if (position == 0) {
//                                        notifyDataSetChanged();
//                                    } else {
//                                        notifyItemRemoved(position);
//                                    }
//                                }
//                            });
//
//                        }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }

            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderGifSent(final ViewHolderGifSent vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {

            if(((SecretChatMessageScreen)mContext).userImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);

                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).userImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }


            vh2.forward.setVisibility(View.GONE);

//            vh2.time.setTypeface(tf, Typeface.ITALIC);
//
//            vh2.date.setTypeface(tf, Typeface.ITALIC);
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");


            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");

            try {
                Glide.with(mContext)
                        .load(message.getGifUrl())
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.home_grid_view_image_icon)
                        .into(vh2.stillGifImage);

                Glide.with(mContext)
                        .load(message.getGifUrl())
                        .asGif()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .crossFade()
                        .into(vh2.gifImage);

            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            String status = message.getDeliveryStatus();

            if (status.equals("3")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.VISIBLE);


                if (!message.isTimerStarted()) {
                    try {
                        message.setTimerStarted(true);

                        if (message.getdTime() > 0) {


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

//                                    db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mListData.remove(position);
//                                            if (position == 0) {
//                                                notifyDataSetChanged();
//                                            } else {
//                                                notifyItemRemoved(position);
//                                            }
//                                        }
//                                    });
                                    try {
                                        ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, (message.getdTime() * SECTOMILLSEC));

                            ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        }
                        //   ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);

                        //     AppController.getInstance().addActiveTimer(documentId, message.getMessageId());
//                        else {
//
//                            //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                            db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mListData.remove(position);
//                                    if (position == 0) {
//                                        notifyDataSetChanged();
//                                    } else {
//                                        notifyItemRemoved(position);
//                                    }
//                                }
//                            });
//
//                        }
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            } else if (status.equals("2")) {
                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.VISIBLE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else if (status.equals("1")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.VISIBLE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else {


                vh2.clock.setVisibility(View.VISIBLE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            }


            try {
                vh2.stillGifImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vh2.gifImage.setVisibility(View.VISIBLE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                vh2.gifImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent intent = new Intent(mContext, GifPlayer.class);
                        intent.putExtra("gifUrl", message.getGifUrl());
                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity) mContext, vh2.gifImage, "image");
                        mContext.startActivity(intent, options.toBundle());
                        vh2.gifImage.setVisibility(View.GONE);

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }

        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderStickersSent(final ViewHolderStickerSent vh15, final int position) {
        final SecretChatMessageItem message = mListData.get(position);

        if (message != null) {


            vh15.forward.setVisibility(View.GONE);
//            vh15.time.setTypeface(tf, Typeface.ITALIC);
//
//            vh15.date.setTypeface(tf, Typeface.ITALIC);
            vh15.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");


            vh15.time.setText(convert24to12hourformat(message.getTS()) + " ");
            try {

                Glide.with(mContext)
                        .load(message.getStickerUrl())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(vh15.imageView);


            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            String status = message.getDeliveryStatus();

            if (status.equals("3")) {

                vh15.clock.setVisibility(View.GONE);
                vh15.singleTick.setVisibility(View.GONE);

                vh15.doubleTickGreen.setVisibility(View.GONE);
                vh15.doubleTickBlue.setVisibility(View.VISIBLE);


                if (!message.isTimerStarted()) {
                    try {
                        message.setTimerStarted(true);

                        if (message.getdTime() > 0) {


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

//                                    db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mListData.remove(position);
//                                            if (position == 0) {
//                                                notifyDataSetChanged();
//                                            } else {
//                                                notifyItemRemoved(position);
//                                            }
//                                        }
//                                    });

                                    try {
                                        ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, (message.getdTime() * SECTOMILLSEC));

                            ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        }

                        //    ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        //      AppController.getInstance().addActiveTimer(documentId, message.getMessageId());
//                        else {
//
//                            //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                            db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mListData.remove(position);
//                                    if (position == 0) {
//                                        notifyDataSetChanged();
//                                    } else {
//                                        notifyItemRemoved(position);
//                                    }
//                                }
//                            });
//
//                        }

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }


            } else if (status.equals("2")) {
                vh15.clock.setVisibility(View.GONE);
                vh15.singleTick.setVisibility(View.GONE);

                vh15.doubleTickGreen.setVisibility(View.VISIBLE);
                vh15.doubleTickBlue.setVisibility(View.GONE);
            } else if (status.equals("1")) {

                vh15.clock.setVisibility(View.GONE);
                vh15.singleTick.setVisibility(View.VISIBLE);

                vh15.doubleTickGreen.setVisibility(View.GONE);
                vh15.doubleTickBlue.setVisibility(View.GONE);
            } else {


                vh15.clock.setVisibility(View.VISIBLE);
                vh15.singleTick.setVisibility(View.GONE);

                vh15.doubleTickGreen.setVisibility(View.GONE);
                vh15.doubleTickBlue.setVisibility(View.GONE);
            }

            if (message.isSelected()) {

                vh15.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh15.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderStickerReceived(final ViewHolderStickerReceived vh16, final int position) {
        final SecretChatMessageItem message = mListData.get(position);


        if (message != null) {


            vh16.forward.setVisibility(View.GONE);
//            vh16.time.setTypeface(tf, Typeface.ITALIC);
//
//            vh16.date.setTypeface(tf, Typeface.ITALIC);
            vh16.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");


            vh16.time.setText(convert24to12hourformat(message.getTS()) + " ");

            try {

                Glide.with(mContext)
                        .load(message.getStickerUrl())

                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(vh16.imageView);


            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (NullPointerException n) {
                n.printStackTrace();

                vh16.relative_layout_message.setVisibility(View.GONE);

            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            if (!message.isTimerStarted()) {
                try {
                    message.setTimerStarted(true);

                    if (message.getdTime() > 0) {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());

//                                db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                                ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mListData.remove(position);
//                                        if (position == 0) {
//                                            notifyDataSetChanged();
//                                        } else {
//                                            notifyItemRemoved(position);
//                                        }
//                                    }
//                                });
                                try {
                                    ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, (message.getdTime() * SECTOMILLSEC));

                        ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                    }
                    //      ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                    //    AppController.getInstance().addActiveTimer(documentId, message.getMessageId());
//                        else {
//
//                            //                                ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//
//                            db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mListData.remove(position);
//                                    if (position == 0) {
//                                        notifyDataSetChanged();
//                                    } else {
//                                        notifyItemRemoved(position);
//                                    }
//                                }
//                            });
//
//                        }
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }


            if (message.isSelected()) {

                vh16.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh16.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }

    private void configureViewHolderServerMessage(ViewHolderServerMessage vh14, int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {
            try {
//                if (message.getTextMessage().contains("created") || vh14.getAdapterPosition() == 0) {
//                    vh14.gap.setVisibility(View.VISIBLE);
//                } else {
//                    vh14.gap.setVisibility(View.GONE);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            vh14.serverupdate.setText(message.getTextMessage());
        }
    }


    /*
     * Since image size for the doodle is hardcoded as 150dp X 150dp
     */
    @SuppressWarnings("TryWithIdenticalCatches,unchecked")


    private void configureViewHolderDoodleReceived(final ViewHolderDoodleReceived vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {

            vh2.forward.setVisibility(View.GONE);
            vh2.fnf.setVisibility(View.GONE);

//
//            vh2.time.setTypeface(tf, Typeface.ITALIC);
//
//            vh2.date.setTypeface(tf, Typeface.ITALIC);
            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");


            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });


            try {


                if (message.getDownloadStatus() == 1) {

                    /*
                     *
                     * doodle already downloaded
                     *
                     * */
                    vh2.progressBar2.setVisibility(View.GONE);

                    vh2.progressBar.setVisibility(View.GONE);
                    vh2.download.setVisibility(View.GONE);
                    vh2.cancel.setVisibility(View.GONE);

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {


                        try {


                            try {
                                Glide
                                        .with(mContext)
                                        .load(message.getImagePath())

                                        .crossFade()


                                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                        .placeholder(R.drawable.home_grid_view_image_icon)
                                        .listener(new RequestListener<String, GlideDrawable>() {
                                            @Override
                                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                return false;
                                            }

                                            @Override
                                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                                                return false;
                                            }
                                        })
                                        .into(vh2.imageView);
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }


                            vh2.imageView.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {


                                    Intent i = new Intent(mContext, MediaHistory_FullScreenImage.class);

                                    i.putExtra("imagePath", message.getImagePath());

                                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    ActivityOptionsCompat options = ActivityOptionsCompat.
                                            makeSceneTransitionAnimation((Activity) mContext, vh2.imageView, "image");
                                    mContext.startActivity(i, options.toBundle());


                                }


                            });


                        } catch (Exception e) {
                            // vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                            Glide.clear(vh2.imageView);
                            vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                            vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                            vh2.fnf.setVisibility(View.VISIBLE);
                        }

                    } else {
                        Glide.clear(vh2.imageView);
                        // vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                        vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                        vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                        vh2.fnf.setVisibility(View.VISIBLE);


                        vh2.fnf.setText(R.string.string_211);


                        vh2.imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent();
                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                                intent.setData(uri);
                                mContext.startActivity(intent);
                            }
                        });

                    }
                    if (!message.isTimerStarted()) {
                        try {
                            message.setTimerStarted(true);

                            if (message.getdTime() > 0) {


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {


                                        try {
                                            ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                        } catch (NullPointerException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, (message.getdTime() * SECTOMILLSEC));

                                ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                            }


                        } catch (IllegalStateException e) {
                            e.printStackTrace();
                        }
                    }

                } else {


                    if (message.isDownloading()) {


                        vh2.cancel.setVisibility(View.VISIBLE);


                        vh2.download.setVisibility(View.GONE);


                        vh2.progressBar2.setVisibility(View.VISIBLE);

                        vh2.progressBar.setVisibility(View.GONE);


                    } else {
                        vh2.download.setVisibility(View.VISIBLE);

                        vh2.progressBar2.setVisibility(View.GONE);
                        vh2.progressBar.setVisibility(View.GONE);
                        vh2.cancel.setVisibility(View.GONE);
                    }


                    try {
                        Glide
                                .with(mContext)
                                .load(message.getThumbnailPath())


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                                        return false;
                                    }
                                })


                                .into(vh2.imageView);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.imageView.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            if (!message.isDownloading()) {
                                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                        == PackageManager.PERMISSION_GRANTED) {


                                    AlertDialog.Builder builder =
                                            new AlertDialog.Builder(mContext, 0);
                                    builder.setTitle(R.string.string_393);
                                    builder.setMessage(mContext.getString(R.string.string_506) + " " + message.getSize() + " " + mContext.getString(R.string.string_534));
                                    builder.setPositiveButton(R.string.string_580, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {


                                            String receiverUid = message.getReceiverUid();

                                            String messageId = message.getMessageId();


                                            message.setDownloading(true);


                                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //notifyItemChanged(viewHolder.getAdapterPosition());

                                                    notifyDataSetChanged();
                                                }
                                            });

                                            download(message.getImagePath(), message.getThumbnailPath(), mContext.getExternalFilesDir(null)
                                                    + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".jpg", AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId), message, vh2);


                                            // dialog.dismiss();

                                            Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                                            if (context instanceof Activity) {


                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                    if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                        dialog.dismiss();
                                                    }
                                                } else {


                                                    if (!((Activity) context).isFinishing()) {
                                                        dialog.dismiss();
                                                    }
                                                }
                                            } else {


                                                try {
                                                    dialog.dismiss();
                                                } catch (final IllegalArgumentException e) {
                                                    e.printStackTrace();

                                                } catch (final Exception e) {
                                                    e.printStackTrace();

                                                }
                                            }


                                        }
                                    });
                                    builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {


                                            dialog.cancel();

                                        }
                                    });
                                    builder.show();
                                } else {


                                    /*
                                     *
                                     * have to request permission
                                     *
                                     * */


                                    requestStorageAccessPermission("image");

                                }
                            } else {


                                Snackbar snackbar = Snackbar.make(root, R.string.string_38, Snackbar.LENGTH_SHORT);


                                snackbar.show();
                                View view = snackbar.getView();
                                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                            }

                        }
                    });
                }


            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }


//            if (!message.isTimerStarted()) {
//                try {
//                    message.setTimerStarted(true);
//
//                    if (message.getdTime() > 0) {
//
//
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//
//                                try {
//                                    ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//                                } catch (NullPointerException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        }, (message.getdTime() * SECTOMILLSEC));
//
//                        ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
//                    }
//
//
//                } catch (IllegalStateException e) {
//                    e.printStackTrace();
//                }
//            }


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderDoodleSent(final ViewHolderDoodleSent vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);
        if (message != null) {

            vh2.forward.setVisibility(View.GONE);
            vh2.fnf.setVisibility(View.GONE);
//
//            vh2.time.setTypeface(tf, Typeface.ITALIC);
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");
            //   vh2.date.setTypeface(tf, Typeface.ITALIC);
            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");
            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });


            if (message.getDownloadStatus() == 1) {


                /*
                 *
                 * doodle already downloaded
                 *
                 * */
                vh2.progressBar2.setVisibility(View.GONE);

                vh2.progressBar.setVisibility(View.GONE);
                vh2.download.setVisibility(View.GONE);
                vh2.cancel.setVisibility(View.GONE);

                if (message.getImagePath() != null) {


                    try {


                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                                == PackageManager.PERMISSION_GRANTED) {


                            try {
                                Glide
                                        .with(mContext)
                                        .load(message.getImagePath())

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
                                                vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                                                return false;
                                            }
                                        })
                                        .into(vh2.imageView);

                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }

                            vh2.imageView.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {


                                    Intent i = new Intent(mContext, MediaHistory_FullScreenImage.class);

                                    i.putExtra("imagePath", message.getImagePath());

                                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    ActivityOptionsCompat options = ActivityOptionsCompat.
                                            makeSceneTransitionAnimation((Activity) mContext, vh2.imageView, "image");
                                    mContext.startActivity(i, options.toBundle());


                                }
                            });
                        } else {

                            vh2.fnf.setVisibility(View.VISIBLE);

//                        vh2.fnf.setTypeface(tf, Typeface.NORMAL);
                            vh2.fnf.setText(R.string.string_211);
                            Glide.clear(vh2.imageView);
                            vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                            vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));


                            vh2.imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                                    intent.setData(uri);
                                    mContext.startActivity(intent);
                                }
                            });

                        }


                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                    } catch (Exception e) {


                        vh2.fnf.setVisibility(View.VISIBLE);
                        Glide.clear(vh2.imageView);
                        vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                        vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                        //  vh2.fnf.setTypeface(tf, Typeface.NORMAL);


                    }
                } else {


                    vh2.imageView.setImageURI(message.getImageUrl());
                }
            } else {


                if (message.isDownloading()) {


                    vh2.cancel.setVisibility(View.VISIBLE);


                    vh2.download.setVisibility(View.GONE);


                    vh2.progressBar2.setVisibility(View.VISIBLE);

                    vh2.progressBar.setVisibility(View.GONE);


                } else {
                    vh2.download.setVisibility(View.VISIBLE);

                    vh2.progressBar2.setVisibility(View.GONE);
                    vh2.progressBar.setVisibility(View.GONE);
                    vh2.cancel.setVisibility(View.GONE);
                }


                try {
                    Glide
                            .with(mContext)
                            .load(message.getThumbnailPath())


                            .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext))


                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                            .placeholder(R.drawable.home_grid_view_image_icon)
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                                    return false;
                                }
                            })


                            .into(vh2.imageView);

                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                vh2.imageView.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        if (!message.isDownloading()) {
                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {


                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(mContext, 0);
                                builder.setTitle(R.string.string_393);
                                builder.setMessage(mContext.getString(R.string.string_506) + " " + message.getSize() + " " + mContext.getString(R.string.string_534));
                                builder.setPositiveButton(R.string.string_580, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        String messageId = message.getMessageId();


                                        message.setDownloading(true);


                                        ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getImagePath(), message.getThumbnailPath(), mContext.getExternalFilesDir(null)
                                                + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".jpg", AppController.getInstance().findDocumentIdOfReceiver(receiverUid, secretId), message, vh2);


                                        // dialog.dismiss();

                                        Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                                        if (context instanceof Activity) {


                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                    dialog.dismiss();
                                                }
                                            } else {


                                                if (!((Activity) context).isFinishing()) {
                                                    dialog.dismiss();
                                                }
                                            }
                                        } else {


                                            try {
                                                dialog.dismiss();
                                            } catch (final IllegalArgumentException e) {
                                                e.printStackTrace();

                                            } catch (final Exception e) {
                                                e.printStackTrace();

                                            }
                                        }


                                    }
                                });
                                builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        dialog.cancel();

                                    }
                                });
                                builder.show();
                            } else {


                                /*
                                 *
                                 * have to request permission
                                 *
                                 * */


                                requestStorageAccessPermission("image");

                            }
                        } else {


                            Snackbar snackbar = Snackbar.make(root, R.string.string_38, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                        }

                    }
                });


            }

            String status = message.getDeliveryStatus();

            if (status.equals("3")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.VISIBLE);


                if (!message.isTimerStarted()) {
                    try {
                        message.setTimerStarted(true);

                        if (message.getdTime() > 0) {


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    //     ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//                                    db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//
//                                    ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            mListData.remove(position);
//                                            if (position == 0) {
//                                                notifyDataSetChanged();
//                                            } else {
//                                                notifyItemRemoved(position);
//                                            }
//                                        }
//                                    });

                                    try {
                                        ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, (message.getdTime() * SECTOMILLSEC));

                            ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        }
                        //     ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        //     AppController.getInstance().addActiveTimer(documentId, message.getMessageId());
//                        else {
//
//                            //   ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
//                            db.deleteParticularChatMessage(documentId, message.getMessageId());
//
//
//                            ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    mListData.remove(position);
//                                    if (position == 0) {
//                                        notifyDataSetChanged();
//                                    } else {
//                                        notifyItemRemoved(position);
//                                    }
//                                }
//                            });
//
//                        }

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }


            } else if (status.equals("2")) {
                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.VISIBLE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else if (status.equals("1")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.VISIBLE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else {


                vh2.clock.setVisibility(View.VISIBLE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            }

            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }

    /**
     * although position of item is not used as of now,but we might use it in the future
     */
    private void configureViewHolderLoading(final ViewHolderLoading vh, int position) {


        vh.slack.start();


    }


    private void configureViewHolderDocumentSent(final ViewHolderDocumentSent vh2, final int position) {


        final SecretChatMessageItem message = mListData.get(position);


        if (message != null) {

            if(((SecretChatMessageScreen)mContext).userImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);

                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).userImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            vh2.forward.setVisibility(View.GONE);

            vh2.fnf.setVisibility(View.GONE);

            //  vh2.time.setTypeface(tf, Typeface.ITALIC);
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");
            //vh2.date.setTypeface(tf, Typeface.ITALIC);
            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");


            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });


            if (message.getDownloadStatus() == 1) {


                /*
                 * Already downloaded
                 */
                vh2.progressBar2.setVisibility(View.GONE);

                vh2.progressBar.setVisibility(View.GONE);
                vh2.download.setVisibility(View.GONE);
                vh2.cancel.setVisibility(View.GONE);


                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {


                    final File file = new File(message.getDocumentUrl());


                    if (file.exists()) {
                        vh2.fileName.setText(message.getFileName());

                        vh2.fileType.setText(message.getFileType());


                        vh2.fileType.setVisibility(View.VISIBLE);


                        if (message.getFileType().equals(FilePickerConst.FILE_TYPE.PDF)) {

                            vh2.fileImage.setImageResource(R.drawable.ic_pdf);

                        } else if (message.getFileType().equals(FilePickerConst.DOC)) {

                            vh2.fileImage.setImageResource(R.drawable.ic_word);
                        } else if (message.getFileType().equals(FilePickerConst.PPT)) {
                            vh2.fileImage.setImageResource(R.drawable.ic_ppt);
                        } else if (message.getFileType().equals(FilePickerConst.XLS)) {
                            vh2.fileImage.setImageResource(R.drawable.ic_excel);
                        } else if (message.getFileType().equals(FilePickerConst.TXT)) {
                            vh2.fileImage.setImageResource(R.drawable.ic_txt);
                        }
                        vh2.documentLayout.setVisibility(View.VISIBLE);


//                        vh2.documentLayout.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                Intent intent = new Intent(Intent.ACTION_VIEW);
//                                Uri data = Uri.fromFile(file);
//
//                                intent.setDataAndType(data, message.getMimeType());
//
//                                mContext.startActivity(intent);
//                            }
//                        });


                        vh2.documentLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);

                                Uri data;

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    data = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);

                                } else {
                                    data = Uri.fromFile(file);
                                }

                                intent.setDataAndType(data, message.getMimeType());
                                Intent i = Intent.createChooser(intent, "Open Document");
                                try {
                                    mContext.startActivity(i);
                                } catch (ActivityNotFoundException e) {
                                    // Instruct the user to install a PDF reader here, or something

                                    if (root != null) {

                                        Snackbar snackbar = Snackbar.make(root, mContext.getString(R.string.NoAppForDocument, message.getFileType()), Snackbar.LENGTH_SHORT);


                                        snackbar.show();
                                        View view2 = snackbar.getView();
                                        TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                    }
                                }
                            }
                        });

                    } else {


                        vh2.fnf.setVisibility(View.VISIBLE);
                        vh2.documentLayout.setVisibility(View.GONE);


                        vh2.fileType.setVisibility(View.GONE);
                    }
                } else {

                    vh2.documentLayout.setVisibility(View.GONE);


                    vh2.fileType.setVisibility(View.GONE);
                    vh2.fnf.setVisibility(View.VISIBLE);
                    vh2.fnf.setText(R.string.string_211);

                    vh2.fnf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                            intent.setData(uri);
                            mContext.startActivity(intent);
                        }
                    });


                }

            } else {

                /*
                 *
                 *To allow an option to download
                 *
                 */

                vh2.fileName.setText(message.getFileName());

                vh2.fileType.setText(message.getFileType());
                if (message.getFileType().equals(FilePickerConst.FILE_TYPE.PDF)) {

                    vh2.fileImage.setImageResource(R.drawable.ic_pdf);

                } else if (message.getFileType().equals(FilePickerConst.DOC)) {

                    vh2.fileImage.setImageResource(R.drawable.ic_word);
                } else if (message.getFileType().equals(FilePickerConst.PPT)) {
                    vh2.fileImage.setImageResource(R.drawable.ic_ppt);
                } else if (message.getFileType().equals(FilePickerConst.XLS)) {
                    vh2.fileImage.setImageResource(R.drawable.ic_excel);
                } else if (message.getFileType().equals(FilePickerConst.TXT)) {
                    vh2.fileImage.setImageResource(R.drawable.ic_txt);
                }

                if (message.isDownloading()) {


                    vh2.cancel.setVisibility(View.VISIBLE);


                    vh2.download.setVisibility(View.GONE);


                    vh2.progressBar2.setVisibility(View.VISIBLE);

                    vh2.progressBar.setVisibility(View.GONE);


                } else {
                    vh2.download.setVisibility(View.VISIBLE);

                    vh2.progressBar2.setVisibility(View.GONE);
                    vh2.progressBar.setVisibility(View.GONE);
                    vh2.cancel.setVisibility(View.GONE);
                }
                vh2.documentLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        if (!message.isDownloading()) {
                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {


                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(mContext, 0);
                                builder.setTitle(R.string.string_393);
                                builder.setMessage(mContext.getString(R.string.string_506) + " " + message.getSize() + " " + mContext.getString(R.string.string_537));
                                builder.setPositiveButton(R.string.string_580, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        // String messageId = message.getMessageId();


                                        message.setDownloading(true);


                                        ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getDocumentUrl(), null, mContext.getExternalFilesDir(null)
                                                + ApiOnServer.CHAT_DOWNLOADS_FOLDER + message.getFileName(), AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);


                                        // dialog.dismiss();

                                        Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                                        if (context instanceof Activity) {


                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                    dialog.dismiss();
                                                }
                                            } else {


                                                if (!((Activity) context).isFinishing()) {
                                                    dialog.dismiss();
                                                }
                                            }
                                        } else {


                                            try {
                                                dialog.dismiss();
                                            } catch (final IllegalArgumentException e) {
                                                e.printStackTrace();

                                            } catch (final Exception e) {
                                                e.printStackTrace();

                                            }
                                        }


                                    }
                                });
                                builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        dialog.cancel();

                                    }
                                });
                                builder.show();
                            } else {


                                /*
                                 *
                                 * have to request permission
                                 *
                                 * */


                                requestStorageAccessPermission("document");

                            }
                        } else {


                            Snackbar snackbar = Snackbar.make(root, R.string.string_38, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                        }

                    }
                });
            }


            String status = message.getDeliveryStatus();

            if (status.equals("3")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.VISIBLE);
                if (!message.isTimerStarted()) {
                    try {
                        message.setTimerStarted(true);

                        if (message.getdTime() > 0) {


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {


                                    try {
                                        ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, (message.getdTime() * SECTOMILLSEC));

                            ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        }

                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }

            } else if (status.equals("2")) {
                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.VISIBLE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else if (status.equals("1")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.VISIBLE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            } else {


                vh2.clock.setVisibility(View.VISIBLE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.GONE);
            }

            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }


    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderDocumentReceived(final ViewHolderDocumentReceived vh2, final int position) {
        final SecretChatMessageItem message = mListData.get(position);


        if (message != null) {

            if(((SecretChatMessageScreen)mContext).receiverImage!=null) {

                vh2.pic.setVisibility(View.VISIBLE);
                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).receiverImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh2.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh2.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            vh2.forward.setVisibility(View.GONE);
            vh2.fnf.setVisibility(View.GONE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");


            vh2.time.setText(convert24to12hourformat(message.getTS()) + " ");


            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });


            if (message.getDownloadStatus() == 0) {

                vh2.fileName.setText(message.getFileName());

                vh2.fileType.setText(message.getFileType());
                if (message.getFileType().equals(FilePickerConst.FILE_TYPE.PDF)) {

                    vh2.fileImage.setImageResource(R.drawable.ic_pdf);

                } else if (message.getFileType().equals(FilePickerConst.DOC)) {

                    vh2.fileImage.setImageResource(R.drawable.ic_word);
                } else if (message.getFileType().equals(FilePickerConst.PPT)) {
                    vh2.fileImage.setImageResource(R.drawable.ic_ppt);
                } else if (message.getFileType().equals(FilePickerConst.XLS)) {
                    vh2.fileImage.setImageResource(R.drawable.ic_excel);
                } else if (message.getFileType().equals(FilePickerConst.TXT)) {
                    vh2.fileImage.setImageResource(R.drawable.ic_txt);
                }
                if (message.isDownloading()) {


                    vh2.download.setVisibility(View.GONE);


                    vh2.progressBar2.setVisibility(View.VISIBLE);

                    vh2.progressBar.setVisibility(View.GONE);
                    vh2.cancel.setVisibility(View.VISIBLE);

                } else {
                    vh2.download.setVisibility(View.VISIBLE);


                    vh2.progressBar.setVisibility(View.GONE);
                    vh2.cancel.setVisibility(View.GONE);

                    vh2.progressBar2.setVisibility(View.GONE);

                }


//                try {
//                    Glide
//                            .with(mContext)
//                            //.load(message.getThumbnailPath())
//
//
//                            .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext))
//
//
//                            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//
//
//                            .placeholder(R.drawable.home_grid_view_image_icon)
//
//
//                            .into(vh2.fileImage);
//
//                } catch (IllegalArgumentException e) {
//                    e.printStackTrace();
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }


                vh2.documentLayout.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        if (!message.isDownloading()) {
                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {


                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(mContext, 0);
                                builder.setTitle(R.string.string_393);
                                builder.setMessage(mContext.getString(R.string.string_506) + " " + message.getSize() + " " + mContext.getString(R.string.string_537));
                                builder.setPositiveButton(R.string.string_580, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        // String messageId = message.getMessageId();


                                        message.setDownloading(true);


                                        ((SecretChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getDocumentUrl(), null, mContext.getExternalFilesDir(null)
                                                + ApiOnServer.CHAT_DOWNLOADS_FOLDER + message.getFileName(), AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);


                                        // dialog.dismiss();

                                        Context context = ((ContextWrapper) ((Dialog) dialog).getContext()).getBaseContext();


                                        if (context instanceof Activity) {


                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                                if (!((Activity) context).isFinishing() && !((Activity) context).isDestroyed()) {
                                                    dialog.dismiss();
                                                }
                                            } else {


                                                if (!((Activity) context).isFinishing()) {
                                                    dialog.dismiss();
                                                }
                                            }
                                        } else {


                                            try {
                                                dialog.dismiss();
                                            } catch (final IllegalArgumentException e) {
                                                e.printStackTrace();

                                            } catch (final Exception e) {
                                                e.printStackTrace();

                                            }
                                        }


                                    }
                                });
                                builder.setNegativeButton(R.string.string_591, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        dialog.cancel();

                                    }
                                });
                                builder.show();
                            } else {


                                /*
                                 *
                                 * have to request permission
                                 *
                                 * */


                                requestStorageAccessPermission("document");

                            }
                        } else {


                            Snackbar snackbar = Snackbar.make(root, R.string.string_38, Snackbar.LENGTH_SHORT);


                            snackbar.show();
                            View view = snackbar.getView();
                            TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                            txtv.setGravity(Gravity.CENTER_HORIZONTAL);

                        }

                    }
                });


            } else {
                vh2.download.setVisibility(View.GONE);


                vh2.progressBar.setVisibility(View.GONE);


                vh2.cancel.setVisibility(View.GONE);

                vh2.progressBar2.setVisibility(View.GONE);


                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {


                    final File file = new File(message.getDocumentUrl());


                    if (file.exists()) {
                        vh2.fileName.setText(message.getFileName());

                        vh2.fileType.setText(message.getFileType());

                        vh2.documentLayout.setVisibility(View.VISIBLE);


                        vh2.fileType.setVisibility(View.VISIBLE);


                        if (message.getFileType().equals(FilePickerConst.FILE_TYPE.PDF)) {

                            vh2.fileImage.setImageResource(R.drawable.ic_pdf);

                        } else if (message.getFileType().equals(FilePickerConst.DOC)) {

                            vh2.fileImage.setImageResource(R.drawable.ic_word);
                        } else if (message.getFileType().equals(FilePickerConst.PPT)) {
                            vh2.fileImage.setImageResource(R.drawable.ic_ppt);
                        } else if (message.getFileType().equals(FilePickerConst.XLS)) {
                            vh2.fileImage.setImageResource(R.drawable.ic_excel);
                        } else if (message.getFileType().equals(FilePickerConst.TXT)) {
                            vh2.fileImage.setImageResource(R.drawable.ic_txt);
                        }
                        vh2.documentLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);

                                Uri data;

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    data = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);

                                } else {
                                    data = Uri.fromFile(file);
                                }

                                intent.setDataAndType(data, message.getMimeType());
                                Intent i = Intent.createChooser(intent, "Open Document");
                                try {
                                    mContext.startActivity(i);
                                } catch (ActivityNotFoundException e) {
                                    // Instruct the user to install a PDF reader here, or something

                                    if (root != null) {

                                        Snackbar snackbar = Snackbar.make(root, mContext.getString(R.string.NoAppForDocument, message.getFileType()), Snackbar.LENGTH_SHORT);


                                        snackbar.show();
                                        View view2 = snackbar.getView();
                                        TextView txtv = (TextView) view2.findViewById(com.google.android.material.R.id.snackbar_text);
                                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                                    }

                                }
                            }
                        });
                    } else {


                        vh2.fnf.setVisibility(View.VISIBLE);
                        vh2.documentLayout.setVisibility(View.GONE);


                        vh2.fileType.setVisibility(View.GONE);
                    }
                } else {

                    vh2.documentLayout.setVisibility(View.GONE);


                    vh2.fileType.setVisibility(View.GONE);
                    vh2.fnf.setVisibility(View.VISIBLE);
                    vh2.fnf.setText(R.string.string_211);

                    vh2.fnf.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
                            intent.setData(uri);
                            mContext.startActivity(intent);
                        }
                    });


                }
                if (!message.isTimerStarted()) {
                    try {
                        message.setTimerStarted(true);

                        if (message.getdTime() > 0) {


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {


                                    try {
                                        ((SecretChatMessageScreen) mContext).deleteParticularMessage(documentId, position, message.getMessageId());
                                    } catch (NullPointerException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, (message.getdTime() * SECTOMILLSEC));

                            ((SecretChatMessageScreen) mContext).setTimer(message.getMessageId(), message.getdTime() * SECTOMILLSEC);
                        }


                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }


    }


    /**
     * For the post
     */

    @SuppressWarnings("TryWithIdenticalCatches")

    private void configureViewHolderPostSent(final ViewHolderPostSent vh, final int position) {
        final SecretChatMessageItem message = mListData.get(position);

        if (message != null) {

            if(((SecretChatMessageScreen)mContext).userImage!=null) {

                vh.pic.setVisibility(View.VISIBLE);

                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).userImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }


            vh.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");
            vh.time.setText(convert24to12hourformat(message.getTS()) + " ");
            vh.title.setText(message.getPostTitle());


            try {

                Glide.with(mContext)
                        .load(message.getImagePath())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(vh.thumbnail);


            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }


            if(message.getPostType() == 0){


                vh.playButton.setVisibility(View.GONE);

            }


            else{


                vh.playButton.setVisibility(View.VISIBLE);
            }

            String status = message.getDeliveryStatus();

            if (status.equals("3")) {

                vh.clock.setVisibility(View.GONE);
                vh.singleTick.setVisibility(View.GONE);

                vh.doubleTickGreen.setVisibility(View.GONE);
                vh.doubleTickBlue.setVisibility(View.VISIBLE);

            } else if (status.equals("2")) {
                vh.clock.setVisibility(View.GONE);
                vh.singleTick.setVisibility(View.GONE);

                vh.doubleTickGreen.setVisibility(View.VISIBLE);
                vh.doubleTickBlue.setVisibility(View.GONE);
            } else if (status.equals("1")) {

                vh.clock.setVisibility(View.GONE);
                vh.singleTick.setVisibility(View.VISIBLE);

                vh.doubleTickGreen.setVisibility(View.GONE);
                vh.doubleTickBlue.setVisibility(View.GONE);
            } else {


                vh.clock.setVisibility(View.VISIBLE);
                vh.singleTick.setVisibility(View.GONE);

                vh.doubleTickGreen.setVisibility(View.GONE);
                vh.doubleTickBlue.setVisibility(View.GONE);
            }



            if (message.isSelected()) {

                vh.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh.messageRoot.setBackgroundColor(transparentColor);

            }

            vh.forward.setVisibility(View.GONE);

            vh.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent i = new Intent(mContext, SocialDetailActivity.class);

                    i.putExtra("imageUrl", message.getImagePath());
                    i.putExtra("postId", message.getPostId());
                    i.putExtra("postType", message.getPostType());
                    i.putExtra("postTitle", message.getPostTitle());
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) mContext, vh.thumbnail, "image");
                    mContext.startActivity(i, options.toBundle());

                }
            });

        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderPostReceived(final ViewHolderPostReceived vh, final int position) {
        final SecretChatMessageItem message = mListData.get(position);


        if (message != null) {

            if(((SecretChatMessageScreen)mContext).receiverImage!=null) {

                vh.pic.setVisibility(View.VISIBLE);
                try {
                    Glide.with(mContext).load(((SecretChatMessageScreen) mContext).receiverImage).asBitmap()
                        .signature(new StringSignature(
                            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                            .centerCrop().placeholder(R.drawable.chat_attachment_profile_default_image_frame).
                            into(new BitmapImageViewTarget(vh.pic) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    vh.pic.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            vh.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");


            vh.time.setText(convert24to12hourformat(message.getTS()) + " ");

            vh.title.setText(message.getPostTitle());

            try {

                Glide.with(mContext)
                        .load(message.getImagePath())

                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(vh.thumbnail);


            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            } catch (NullPointerException n) {
                n.printStackTrace();



            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            if(message.getPostType() == 0){


                vh.playButton.setVisibility(View.GONE);

            }
            else{


                vh.playButton.setVisibility(View.VISIBLE);
            }




            if (message.isSelected()) {

                vh.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh.messageRoot.setBackgroundColor(transparentColor);

            }

            vh.forward.setVisibility(View.GONE);


            vh.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent i = new Intent(mContext, SocialDetailActivity.class);

                    i.putExtra("imageUrl", message.getImagePath());
                    i.putExtra("postId", message.getPostId());
                    i.putExtra("postType", message.getPostType());
                    i.putExtra("postTitle", message.getPostTitle());
                    i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) mContext, vh.thumbnail, "image");
                    mContext.startActivity(i, options.toBundle());

                }
            });
        }
    }







}
