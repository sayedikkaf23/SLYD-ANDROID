package chat.hola.com.app.GroupChat.Adapters;

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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.provider.Settings;

import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
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
import chat.hola.com.app.ForwardMessage.ActivityForwardMessage;
import chat.hola.com.app.Giphy.GifPlayer;
import chat.hola.com.app.GroupChat.Activities.GroupChatMessageScreen;
import chat.hola.com.app.GroupChat.ModelClasses.GroupChatMessageItem;
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
import chat.hola.com.app.ViewHolders.ViewHolderRemoveReceived;
import chat.hola.com.app.ViewHolders.ViewHolderRemoveSent;
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
 * Created by moda on 08/08/17.
 */

public class GroupChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int MESSAGERECEIVED = 0;
    private final int MESSAGESENT = 1;
    private final int IMAGERECEIVED = 2;
    private final int IMAGESENT = 3;
    private final int VIDEORECEIVED = 4;
    private final int VIDEOSENT = 5;
    private final int LOCATIONRECEIVED = 6;
    private final int LOCATIONSENT = 7;
    private final int CONTACTRECEIVED = 8;
    private final int CONTACTSENT = 9;
    private final int AUDIORECEIVED = 10;
    private final int AUDIOSENT = 11;
    /**
     * For non standard sup like sharing
     */
    private final int STICKERSRECEIVED = 12;
    private final int STICKERSSENT = 13;
    private final int SERVERMESSAGE = 14;
    private final int DOODLERECEIVED = 15;
    private final int DOODLESENT = 16;
    private final int GIFRECEIVED = 17;
    private final int GIFSENT = 18;


    private final int LOADING = 19;


    private final int DOCUMENTSENT = 20;
    private final int DOCUMENTRECEIVED = 21;


    private final int GROUPCHATTAG = 98;


    private ArrayList<GroupChatMessageItem> mListData = new ArrayList<>();
    private Context mContext;

    //private MediaPlayer mediaPlayer;


    private long fileSizeDownloaded;
    private RelativeLayout root;

    private int density;

    private HashMap<String, Object> map = new HashMap<>();

    private Bitmap thumbnail;

    // private Typeface tf;


    /*
     * For reply to a message option
     */

    private final int REPLY_MESSAGERECEIVED = 22;
    private final int REPLY_MESSAGESENT = 23;
    private final int REPLY_IMAGERECEIVED = 24;
    private final int REPLY_IMAGESENT = 25;
    private final int REPLY_VIDEORECEIVED = 26;
    private final int REPLY_VIDEOSENT = 27;
    private final int REPLY_LOCATIONRECEIVED = 28;
    private final int REPLY_LOCATIONSENT = 29;
    private final int REPLY_CONTACTRECEIVED = 30;
    private final int REPLY_CONTACTSENT = 31;
    private final int REPLY_AUDIORECEIVED = 32;
    private final int REPLY_AUDIOSENT = 33;
    /**
     * For non standard sup like sharing
     */
    private final int REPLY_STICKERSRECEIVED = 34;
    private final int REPLY_STICKERSSENT = 35;

    private final int REPLY_DOODLERECEIVED = 36;
    private final int REPLY_DOODLESENT = 37;
    private final int REPLY_GIFRECEIVED = 38;
    private final int REPLY_GIFSENT = 39;


    private final int REPLY_DOCUMENTSENT = 40;
    private final int REPLY_DOCUMENTRECEIVED = 41;


    private final int REMOVESENT = 42;
    private final int REMOVERECEIVED = 43;


    private final int EDITSENT = 44;
    private final int EDITRECEIVED = 45;


    private final int REPLY_EDITSENT = 46;
    private final int REPLY_EDITRECEIVED = 47;



    /**
     * For adding the sharing of the post functionality,specific to dubly app
     */
    private final int POSTSENT = 48;
    private final int POSTRECEIVED = 49;
    private final int REPLY_POSTSENT = 50;
    private final int REPLY_POSTRECEIVED = 51;

    private int transparentColor, lightBlueColor;


    public GroupChatMessageAdapter(Context mContext, ArrayList<GroupChatMessageItem> mListData, RelativeLayout root) {
        this.mListData = mListData;
        this.mContext = mContext;
        this.root = root;

        density = (int) mContext.getResources().getDisplayMetrics().density;


        transparentColor = ContextCompat.getColor(mContext, R.color.transparent);

        lightBlueColor = ContextCompat.getColor(mContext, R.color.message_select);

        //   tf = AppController.getInstance().getRegularFont();
    }

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

    @Override
    public int getItemCount() {
        return this.mListData.size();
    }


    @Override
    public int getItemViewType(int position) {

        String type = mListData.get(position).getMessageType();


        /*
         * For showing of the loading more item
         */

        if (type.equals("99")) {
            return LOADING;
        }
        if (type.equals("98")) {
            return GROUPCHATTAG;
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
            } else if (type.equals("9")) {
                return DOCUMENTSENT;
            } else if (type.equals("11")) {
                return REMOVESENT;
            } else if (type.equals("12")) {
                return EDITSENT;
            }

            else if (type.equals("13")) {
                return POSTSENT;
            }

            else {
                String replyType = mListData.get(position).getReplyType();
                if (replyType.equals("0")) {
                    return REPLY_MESSAGESENT;
                } else if (replyType.equals("1")) {
                    return REPLY_IMAGESENT;
                } else if (replyType.equals("2")) {
                    return REPLY_VIDEOSENT;
                } else if (replyType.equals("3")) {
                    return REPLY_LOCATIONSENT;
                } else if (replyType.equals("4")) {
                    return REPLY_CONTACTSENT;
                } else if (replyType.equals("5")) {
                    return REPLY_AUDIOSENT;
                } else if (replyType.equals("6")) {
                    return REPLY_STICKERSSENT;
                } else if (replyType.equals("7")) {
                    return REPLY_DOODLESENT;
                } else if (replyType.equals("8")) {
                    return REPLY_GIFSENT;
                } else if (replyType.equals("12")) {
                    return REPLY_EDITSENT;
                }
                else if (replyType.equals("13")) {
                    return REPLY_POSTSENT;
                }

                else {
                    return REPLY_DOCUMENTSENT;
                }


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
            } else if (type.equals("9")) {
                return DOCUMENTRECEIVED;
            } else if (type.equals("11")) {
                return REMOVERECEIVED;
            } else if (type.equals("12")) {
                return EDITRECEIVED;
            }

            else if (type.equals("13")) {
                return POSTRECEIVED;
            }

            else {
                String replyType = mListData.get(position).getReplyType();
                if (replyType.equals("0")) {
                    return REPLY_MESSAGERECEIVED;
                } else if (replyType.equals("1")) {
                    return REPLY_IMAGERECEIVED;
                } else if (replyType.equals("2")) {
                    return REPLY_VIDEORECEIVED;
                } else if (replyType.equals("3")) {
                    return REPLY_LOCATIONRECEIVED;
                } else if (replyType.equals("4")) {
                    return REPLY_CONTACTRECEIVED;
                } else if (replyType.equals("5")) {
                    return REPLY_AUDIORECEIVED;
                } else if (replyType.equals("6")) {
                    return REPLY_STICKERSRECEIVED;
                } else if (replyType.equals("7")) {
                    return REPLY_DOODLERECEIVED;
                } else if (replyType.equals("8")) {
                    return REPLY_GIFRECEIVED;
                } else if (replyType.equals("12")) {
                    return REPLY_EDITRECEIVED;
                }

                else if (replyType.equals("13")) {
                    return REPLY_POSTRECEIVED;
                }

                else {
                    return REPLY_DOCUMENTRECEIVED;
                }

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






            case REMOVESENT:
                v1 = inflater.inflate(R.layout.message_remove_sent, viewGroup, false);
                viewHolder = new ViewHolderRemoveSent(v1);
                break;
            case REMOVERECEIVED:
                v1 = inflater.inflate(R.layout.message_remove_received, viewGroup, false);
                viewHolder = new ViewHolderRemoveReceived(v1);
                break;

            case EDITSENT:
                v1 = inflater.inflate(R.layout.message_edit_sent, viewGroup, false);
                viewHolder = new ViewHolderMessageSent(v1);
                break;
            case EDITRECEIVED:
                v1 = inflater.inflate(R.layout.message_edit_received, viewGroup, false);
                viewHolder = new ViewHolderMessageReceived(v1);
                break;





            /*
             * For the message reply feature
             *
             */


            case REPLY_EDITRECEIVED:
                v1 = inflater.inflate(R.layout.reply_edit_received, viewGroup, false);
                viewHolder = new ViewHolderMessageReceived(v1);
                break;
            case REPLY_MESSAGERECEIVED:
                v1 = inflater.inflate(R.layout.reply_message_received, viewGroup, false);
                viewHolder = new ViewHolderMessageReceived(v1);
                break;


            case REPLY_IMAGERECEIVED:
                v1 = inflater.inflate(R.layout.reply_image_received, viewGroup, false);
                viewHolder = new ViewHolderImageReceived(v1);
                break;

            case REPLY_VIDEORECEIVED:
                v1 = inflater.inflate(R.layout.reply_video_received, viewGroup, false);
                viewHolder = new ViewHolderVideoReceived(v1);
                break;

            case REPLY_LOCATIONRECEIVED:
                v1 = inflater.inflate(R.layout.reply_location_received, viewGroup, false);
                viewHolder = new ViewHolderLocationReceived(v1);
                break;

            case REPLY_CONTACTRECEIVED:
                v1 = inflater.inflate(R.layout.reply_contact_received, viewGroup, false);
                viewHolder = new ViewHolderContactReceived(v1);
                break;

            case REPLY_AUDIORECEIVED:
                v1 = inflater.inflate(R.layout.reply_audio_received, viewGroup, false);
                viewHolder = new ViewHolderAudioReceived(v1);
                break;


            case REPLY_STICKERSRECEIVED:
                v1 = inflater.inflate(R.layout.reply_sticker_received, viewGroup, false);
                viewHolder = new ViewHolderStickerReceived(v1);
                break;


            case REPLY_DOODLERECEIVED:
                v1 = inflater.inflate(R.layout.reply_doodle_received, viewGroup, false);
                viewHolder = new ViewHolderDoodleReceived(v1);
                break;

            case REPLY_GIFRECEIVED:
                v1 = inflater.inflate(R.layout.reply_gif_received, viewGroup, false);
                viewHolder = new ViewHolderGifReceived(v1);
                break;


            case REPLY_DOCUMENTRECEIVED:
                v1 = inflater.inflate(R.layout.reply_document_received, viewGroup, false);
                viewHolder = new ViewHolderDocumentReceived(v1);
                break;

            case REPLY_POSTRECEIVED:
                v1 = inflater.inflate(R.layout.reply_post_received, viewGroup, false);
                viewHolder = new ViewHolderPostReceived(v1);
                break;


            case REPLY_POSTSENT:
                v1 = inflater.inflate(R.layout.reply_post_sent, viewGroup, false);
                viewHolder = new ViewHolderPostSent(v1);
                break;


            case REPLY_EDITSENT:
                v1 = inflater.inflate(R.layout.reply_edit_sent, viewGroup, false);
                viewHolder = new ViewHolderMessageSent(v1);
                break;

            case REPLY_MESSAGESENT:
                v1 = inflater.inflate(R.layout.reply_message_sent, viewGroup, false);
                viewHolder = new ViewHolderMessageSent(v1);
                break;

            case REPLY_IMAGESENT:
                v1 = inflater.inflate(R.layout.reply_image_sent, viewGroup, false);
                viewHolder = new ViewHolderImageSent(v1);
                break;

            case REPLY_VIDEOSENT:
                v1 = inflater.inflate(R.layout.reply_video_sent, viewGroup, false);
                viewHolder = new ViewHolderVideoSent(v1);
                break;

            case REPLY_LOCATIONSENT:
                v1 = inflater.inflate(R.layout.reply_location_sent, viewGroup, false);
                viewHolder = new ViewHolderLocationSent(v1);
                break;


            case REPLY_CONTACTSENT:
                v1 = inflater.inflate(R.layout.reply_contact_sent, viewGroup, false);
                viewHolder = new ViewHolderContactSent(v1);
                break;
            case REPLY_AUDIOSENT:
                v1 = inflater.inflate(R.layout.reply_audio_sent, viewGroup, false);
                viewHolder = new ViewHolderAudioSent(v1);
                break;

            case REPLY_STICKERSSENT:

                v1 = inflater.inflate(R.layout.reply_sticker_sent, viewGroup, false);
                viewHolder = new ViewHolderStickerSent(v1);
                break;

            case REPLY_DOODLESENT:
                v1 = inflater.inflate(R.layout.reply_doodle_sent, viewGroup, false);
                viewHolder = new ViewHolderDoodleSent(v1);
                break;

            case REPLY_GIFSENT:
                v1 = inflater.inflate(R.layout.reply_gif_sent, viewGroup, false);
                viewHolder = new ViewHolderGifSent(v1);
                break;

            case REPLY_DOCUMENTSENT:
                v1 = inflater.inflate(R.layout.reply_document_sent, viewGroup, false);
                viewHolder = new ViewHolderDocumentSent(v1);
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
             * For the message replying feature
             */


            case REPLY_MESSAGERECEIVED:
                ViewHolderMessageReceived vh23 = (ViewHolderMessageReceived) viewHolder;

                configureViewHolderReplyMessageReceived(vh23, position);

                break;

            case REPLY_IMAGERECEIVED:
                ViewHolderImageReceived vh24 = (ViewHolderImageReceived) viewHolder;
                configureViewHolderReplyImageReceived(vh24, position);
                break;

            case REPLY_VIDEORECEIVED:

                ViewHolderVideoReceived vh25 = (ViewHolderVideoReceived) viewHolder;

                configureViewHolderReplyVideoReceived(vh25, position);
                break;

            case REPLY_LOCATIONRECEIVED:

                ViewHolderLocationReceived vh26 = (ViewHolderLocationReceived) viewHolder;

                configureViewHolderReplyLocationReceived(vh26, position);
                break;

            case REPLY_CONTACTRECEIVED:

                ViewHolderContactReceived vh27 = (ViewHolderContactReceived) viewHolder;
                configureViewHolderReplyContactReceived(vh27, position);
                break;

            case REPLY_AUDIORECEIVED:

                ViewHolderAudioReceived vh28 = (ViewHolderAudioReceived) viewHolder;

                configureViewHolderReplyAudioReceived(vh28, position);
                break;


            case REPLY_STICKERSRECEIVED:
                ViewHolderStickerReceived vh29 = (ViewHolderStickerReceived) viewHolder;
                configureViewHolderReplyStickerReceived(vh29, position);
                break;

            case REPLY_DOODLERECEIVED:
                ViewHolderDoodleReceived vh30 = (ViewHolderDoodleReceived) viewHolder;
                configureViewHolderReplyDoodleReceived(vh30, position);
                break;

            case REPLY_GIFRECEIVED:

                ViewHolderGifReceived vh31 = (ViewHolderGifReceived) viewHolder;
                configureViewHolderReplyGifReceived(vh31, position);
                break;


            case REPLY_MESSAGESENT:


                ViewHolderMessageSent vh32 = (ViewHolderMessageSent) viewHolder;

                configureViewHolderReplyMessageSent(vh32, position);

                break;

            case REPLY_IMAGESENT:


                ViewHolderImageSent vh33 = (ViewHolderImageSent) viewHolder;
                configureViewHolderReplyImageSent(vh33, position);
                break;

            case REPLY_VIDEOSENT:

                ViewHolderVideoSent vh34 = (ViewHolderVideoSent) viewHolder;
                configureViewHolderReplyVideoSent(vh34, position);
                break;

            case REPLY_LOCATIONSENT:
                ViewHolderLocationSent vh35 = (ViewHolderLocationSent) viewHolder;
                configureViewHolderReplyLocationSent(vh35, position);
                break;


            case REPLY_CONTACTSENT:
                ViewHolderContactSent vh36 = (ViewHolderContactSent) viewHolder;
                configureViewHolderReplyContactSent(vh36, position);
                break;


            case REPLY_AUDIOSENT:
                ViewHolderAudioSent vh37 = (ViewHolderAudioSent) viewHolder;
                configureViewHolderReplyAudioSent(vh37, position);
                break;

            case REPLY_STICKERSSENT:


                ViewHolderStickerSent vh38 = (ViewHolderStickerSent) viewHolder;
                configureViewHolderReplyStickersSent(vh38, position);
                break;
            case REPLY_DOODLESENT:


                ViewHolderDoodleSent vh39 = (ViewHolderDoodleSent) viewHolder;
                configureViewHolderReplyDoodleSent(vh39, position);
                break;

            case REPLY_GIFSENT:

                ViewHolderGifSent vh40 = (ViewHolderGifSent) viewHolder;
                configureViewHolderReplyGifSent(vh40, position);
                break;


            case REPLY_DOCUMENTRECEIVED:

                ViewHolderDocumentReceived vh41 = (ViewHolderDocumentReceived) viewHolder;
                configureViewHolderReplyDocumentReceived(vh41, position);
                break;

            case REPLY_DOCUMENTSENT:

                ViewHolderDocumentSent vh42 = (ViewHolderDocumentSent) viewHolder;
                configureViewHolderReplyDocumentSent(vh42, position);
                break;

            /*
             * For the remove message feature
             */


            case REMOVERECEIVED:

                ViewHolderRemoveReceived vh43 = (ViewHolderRemoveReceived) viewHolder;
                configureViewHolderRemoveReceived(vh43, position);
                break;

            case REMOVESENT:

                ViewHolderRemoveSent vh44 = (ViewHolderRemoveSent) viewHolder;
                configureViewHolderRemoveSent(vh44, position);
                break;


            case EDITRECEIVED:

                ViewHolderMessageReceived vh45 = (ViewHolderMessageReceived) viewHolder;
                configureViewHolderMessageReceived(vh45, position);
                break;

            case EDITSENT:

                ViewHolderMessageSent vh46 = (ViewHolderMessageSent) viewHolder;
                configureViewHolderMessageSent(vh46, position);
                break;

            case REPLY_EDITRECEIVED:

                ViewHolderMessageReceived vh47 = (ViewHolderMessageReceived) viewHolder;
                configureViewHolderReplyMessageReceived(vh47, position);
                break;
            case REPLY_EDITSENT:

                ViewHolderMessageSent vh48 = (ViewHolderMessageSent) viewHolder;
                configureViewHolderReplyMessageSent(vh48, position);
                break;



            /*
             * For the post message
             */

            case POSTRECEIVED:

                ViewHolderPostReceived vh49 = (ViewHolderPostReceived) viewHolder;
                configureViewHolderPostReceived(vh49, position);
                break;
            case POSTSENT:

                ViewHolderPostSent vh50 = (ViewHolderPostSent) viewHolder;
                configureViewHolderPostSent(vh50, position);
                break;


            case REPLY_POSTRECEIVED:

                ViewHolderPostReceived vh51 = (ViewHolderPostReceived) viewHolder;
                configureViewHolderReplyPostReceived(vh51, position);
                break;
            case REPLY_POSTSENT:

                ViewHolderPostSent vh52 = (ViewHolderPostSent) viewHolder;
                configureViewHolderReplyPostSent(vh52, position);
                break;




            default:
                ViewHolderServerMessage vh1 = (ViewHolderServerMessage) viewHolder;
                configureViewHolderServerMessage(vh1, position);


        }
    }


    private void configureViewHolderMessageReceived(ViewHolderMessageReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {

            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) +
                    mContext.getString(R.string.space));


            vh2.time.setText(convert24to12hourformat(message.getTS()) +
                    mContext.getString(R.string.space));
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


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }


            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(0, -1, message.getTextMessage(), null);
                }
            });

        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderImageReceived(final ViewHolderImageReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {
            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());
            vh2.fnf.setVisibility(View.GONE);


            vh2.time.setText(convert24to12hourformat(message.getTS()) +
                    mContext.getString(R.string.space));//todo getString() cannot be applied
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) +
                    mContext.getString(R.string.space));//todo getString() cannot be applied


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

                                                vh2.forward.setVisibility(View.VISIBLE);
                                                vh2.forward.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {


                                                        forwardMessage(1, -1, message.getImagePath(), null);
                                                    }
                                                });
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


                            vh2.forward.setVisibility(View.GONE);


                            Glide.clear(vh2.imageView);
                            vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                            vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                            vh2.fnf.setVisibility(View.VISIBLE);
                        }

                    } else {

                        vh2.forward.setVisibility(View.GONE);


                        Glide.clear(vh2.imageView);

                        vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                        vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                        vh2.fnf.setVisibility(View.VISIBLE);


                        vh2.fnf.setText(mContext.getString(R.string.string_211));//todo getString() cannot be applied


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

                } else {
                    vh2.forward.setVisibility(View.GONE);

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


                                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //notifyItemChanged(viewHolder.getAdapterPosition());

                                                    notifyDataSetChanged();
                                                }
                                            });

                                            download(message.getImagePath(), message.getThumbnailPath(),
                                                    mContext.getExternalFilesDir(null)  +
                                                            ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".jpg",
                                                    AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);


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


                                    requestStorageAccessPermission("image", 0);

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


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }


        }


    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderVideoReceived(final ViewHolderVideoReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {
            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());
            vh2.fnf.setVisibility(View.GONE);


            vh2.time.setText(convert24to12hourformat(message.getTS()) +
                    mContext.getString(R.string.space));//todo getString() cannot be applied

            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) +
                    mContext.getString(R.string.space));//todo getString cannot be applied


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

                                                                         i.putExtra("videoPath", message.getVideoPath());
                                                                         mContext.startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext).toBundle());


                                                                     }
                                                                 }
                                                             }

                            );
                            vh2.forward.setVisibility(View.VISIBLE);

                            vh2.forward.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    forwardMessage(2, -1, message.getVideoPath(), null);
                                }
                            });

                        } else {
                            vh2.forward.setVisibility(View.GONE);
                            Glide.clear(vh2.thumbnail);
                            vh2.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                            vh2.thumbnail.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));


                            vh2.fnf.setVisibility(View.VISIBLE);


                        }


                    } else {

                        vh2.forward.setVisibility(View.GONE);

                        Glide.clear(vh2.thumbnail);
                        vh2.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                        vh2.thumbnail.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                        vh2.fnf.setVisibility(View.VISIBLE);


                        vh2.fnf.setText(mContext.getString(R.string.string_211));//todo getString() cannot be applied


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


                } else {

                    vh2.forward.setVisibility(View.GONE);
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

                                    //todo geString cannot be applied
                                    AlertDialog.Builder builder =
                                            new AlertDialog.Builder(mContext, 0);
                                    builder.setTitle(R.string.string_393);
                                    builder.setMessage(mContext.getString(R.string.string_506) + (mContext.getString(R.string.space))
                                            + message.getSize() + (mContext.getString(R.string.space)) + mContext.getString(R.string.string_535));
                                    builder.setPositiveButton(R.string.string_578,
                                            new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {


                                            String messageId = message.getMessageId();

                                            String receiverUid = message.getReceiverUid();
                                            message.setDownloading(true);


                                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //notifyItemChanged(viewHolder.getAdapterPosition());

                                                    notifyDataSetChanged();
                                                }
                                            });


                                            download(message.getVideoPath(), message.getThumbnailPath(), mContext.getExternalFilesDir(null)  + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".mp4",
                                                    AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);

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


                                    requestStorageAccessPermission("video", 0);


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


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }

        }


    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")
    private void configureViewHolderLocationReceived(ViewHolderLocationReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {

            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());


            vh2.time.setText(convert24to12hourformat(message.getTS()) +
                    mContext.getString(R.string.space));//todo getString() cannot be applied
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) +
                    mContext.getString(R.string.space));//todo getString() cannot be applied


            if (vh2.mMap != null)

                vh2.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//            final String args[] = message.getPlaceInfo().split("@@");
            String args[] = message.getPlaceInfo().split("@@");

            String LatLng = args[0];

            String[] parts = LatLng.split(",");

            String lat = parts[0].substring(1);
            String lng = parts[1].substring(0, parts[1].length() - 1);

            parts = null;
            args = null;
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
//                                        ActivityOptionsCompat.makeSceneTransitionAnimation((GroupChatMessageScreen) mContext).toBundle());
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

            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(3, -1, message.getPlaceInfo(), null);
                }
            });


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderContactReceived(ViewHolderContactReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {

            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());
            vh2.pic.setVisibility(View.GONE);

            vh2.time.setText(convert24to12hourformat(message.getTS()) +
                    (mContext.getString(R.string.space)));//todo getString cannot be applied
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) +
                    (mContext.getString(R.string.space)));

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

            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(4, -1, message.getContactInfo(), null);
                }
            });


            vh2.contact_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).checkWriteContactPermission(message.getContactInfo());
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
        final GroupChatMessageItem message = mListData.get(position);


        if (message != null) {
            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());
            vh2.playButton.setVisibility(View.VISIBLE);
            vh2.fnf.setVisibility(View.GONE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + (mContext.getString(R.string.space)));


            vh2.time.setText(convert24to12hourformat(message.getTS()) + (mContext.getString(R.string.space)));
            vh2.waveformSeekBar.setSampleFrom(getSampleWave());


            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });


            if (message.getDownloadStatus() == 0) {

                vh2.forward.setVisibility(View.GONE);
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
                                builder.setMessage(mContext.getString(R.string.string_506) + (mContext.getString(R.string.space)) + message.getSize() + (mContext.getString(R.string.space))
                                        + mContext.getString(R.string.string_538));
                                builder.setPositiveButton(R.string.string_578, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        String messageId = message.getMessageId();
                                        message.setDownloading(true);


                                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getAudioPath(), null, mContext.getExternalFilesDir(null)  + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".3gp",
                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);

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


                            requestStorageAccessPermission("audio", 0);
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

//
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


                        vh2.forward.setVisibility(View.VISIBLE);

                        vh2.forward.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                forwardMessage(5, -1, message.getAudioPath(), null);
                            }
                        });

                    } else {


                        vh2.playButton.setVisibility(View.GONE);
                        vh2.fnf.setVisibility(View.VISIBLE);
                        vh2.forward.setVisibility(View.GONE);
                    }
                } else {
                    vh2.forward.setVisibility(View.GONE);

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

            }


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }


    }

    public int[] getSampleWave(){
        int[] dataWaves = new int[]{5,7,9,10,15,3,6,12,20,25,22,17,30,26,19,16,13,2,13};

//        for (Integer i:  dataWaves) {
//
//            dataWaves[i] = new Random().nextInt(dataWaves.length);
//        }

        return dataWaves;
    }



    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderDocumentReceived(final ViewHolderDocumentReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);


        if (message != null) {
            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());
            vh2.fnf.setVisibility(View.GONE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + (mContext.getString(R.string.space)));


            vh2.time.setText(convert24to12hourformat(message.getTS()) + (mContext.getString(R.string.space)));


            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });


            if (message.getDownloadStatus() == 0) {

                vh2.forward.setVisibility(View.GONE);

                vh2.fileName.setText(message.getFileName());

                vh2.fileType.setText(message.getFileType());
                if (message.getFileType().equals(FilePickerConst.PDF)) {

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
                                builder.setMessage(mContext.getString(R.string.string_506) + (mContext.getString(R.string.space)) +
                                        message.getSize() + (mContext.getString(R.string.space)) + mContext.getString(R.string.string_537));
                                builder.setPositiveButton(R.string.string_580, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        // String messageId = message.getMessageId();


                                        message.setDownloading(true);


                                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getDocumentUrl(), null, mContext.getExternalFilesDir(null)
                                                        + ApiOnServer.CHAT_DOWNLOADS_FOLDER + message.getFileName(),
                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);


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


                                requestStorageAccessPermission("document", 0);

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


                        if (message.getFileType().equals(FilePickerConst.PDF)) {

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
                                Uri data ;
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


                        vh2.forward.setVisibility(View.VISIBLE);

                        vh2.forward.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                forwardDocument(9, message.getDocumentUrl(), message.getMimeType(), message.getFileName(), message.getExtension(), null);
                            }
                        });

                    } else {

                        vh2.forward.setVisibility(View.GONE);
                        vh2.fnf.setVisibility(View.VISIBLE);
                        vh2.documentLayout.setVisibility(View.GONE);


                        vh2.fileType.setVisibility(View.GONE);
                    }
                } else {


                    vh2.forward.setVisibility(View.GONE);


                    vh2.documentLayout.setVisibility(View.GONE);

                    vh2.forward.setVisibility(View.GONE);
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

            }


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }

        }


    }


    private void configureViewHolderMessageSent(ViewHolderMessageSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));

            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));

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


            if (message.getDeliveryStatus().equals("1")) {

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
            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(0, -1, message.getTextMessage(), null);
                }
            });
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderImageSent(final ViewHolderImageSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {


            vh2.fnf.setVisibility(View.GONE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));

            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));


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


                                                vh2.forward.setVisibility(View.VISIBLE);

                                                vh2.forward.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {


                                                        forwardMessage(1, -1, message.getImagePath(), null);
                                                    }
                                                });

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
                            vh2.forward.setVisibility(View.GONE);
                            vh2.fnf.setVisibility(View.VISIBLE);


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

                        vh2.forward.setVisibility(View.GONE);
                        vh2.fnf.setVisibility(View.VISIBLE);
                        Glide.clear(vh2.imageView);
                        vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                        vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));


                    }
                } else {


                    vh2.imageView.setImageURI(message.getImageUrl());
                }


            } else {


                vh2.forward.setVisibility(View.GONE);

                /*
                 *
                 *To allow an option to download
                 *
                 */


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
                                builder.setMessage(mContext.getString(R.string.string_506) + (mContext.getString(R.string.space)) +
                                        message.getSize() + (mContext.getString(R.string.space)) + mContext.getString(R.string.string_534));
                                builder.setPositiveButton(R.string.string_580, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        String messageId = message.getMessageId();


                                        message.setDownloading(true);


                                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getImagePath(), message.getThumbnailPath(), mContext.getExternalFilesDir(null)  + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".jpg",
                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);


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


                                requestStorageAccessPermission("image", 0);

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


            if (message.getDeliveryStatus().equals("1")) {

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
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {


            vh2.fnf.setVisibility(View.GONE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));

            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));

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

                                        i.putExtra("videoPath", message.getVideoPath());
                                        mContext.startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext).toBundle());


                                    }
                                }
                            });


                            vh2.forward.setVisibility(View.VISIBLE);

                            vh2.forward.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    forwardMessage(2, -1, message.getVideoPath(), null);
                                }
                            });

                        } else {


                            vh2.forward.setVisibility(View.GONE);
                            Glide.clear(vh2.thumbnail);
                            vh2.fnf.setVisibility(View.VISIBLE);

                            vh2.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                            vh2.thumbnail.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));

                        }
                    } else {
                        vh2.forward.setVisibility(View.GONE);
                        vh2.fnf.setVisibility(View.VISIBLE);
                        Glide.clear(vh2.thumbnail);

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


                vh2.forward.setVisibility(View.GONE);
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
                                builder.setMessage(mContext.getString(R.string.string_506) + (mContext.getString(R.string.space)) + message.getSize() +
                                       (mContext.getString(R.string.space)) + mContext.getString(R.string.string_535));
                                builder.setPositiveButton(R.string.string_578, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String messageId = message.getMessageId();

                                        String receiverUid = message.getReceiverUid();
                                        message.setDownloading(true);


                                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });


                                        download(message.getVideoPath(), message.getThumbnailPath(), mContext.getExternalFilesDir(null)  + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".mp4",
                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);

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


                                requestStorageAccessPermission("video", 0);


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
            if (message.getDeliveryStatus().equals("1")) {

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


    @SuppressWarnings("unchecked,TryWithIdenticalCatches")
    private void configureViewHolderLocationSent(ViewHolderLocationSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));

            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));


            if (vh2.mMap != null) {
                vh2.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
//            final String args[] = message.getPlaceInfo().split("@@");
            String args[] = message.getPlaceInfo().split("@@");
            String LatLng = args[0];

            String[] parts = LatLng.split(",");

            String lat = parts[0].substring(1);
            String lng = parts[1].substring(0, parts[1].length() - 1);

            parts = null;
            args = null;
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
//                                        ActivityOptionsCompat.makeSceneTransitionAnimation((GroupChatMessageScreen) mContext).toBundle());
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


            if (message.getDeliveryStatus().equals("1")) {

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
            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(3, -1, message.getPlaceInfo(), null);
                }
            });


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")

    private void configureViewHolderContactSent(ViewHolderContactSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {

            vh2.pic.setVisibility(View.GONE);

            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));


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


            if (message.getDeliveryStatus().equals("1")) {

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
            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(4, -1, message.getContactInfo(), null);
                }
            });


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }

            vh2.contact_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((GroupChatMessageScreen) mContext).checkWriteContactPermission(message.getContactInfo());
                }
            });
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderAudioSent(final ViewHolderAudioSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {


            vh2.fnf.setVisibility(View.GONE);


            vh2.playButton.setVisibility(View.VISIBLE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));
            vh2.waveformSeekBar.setSampleFrom(getSampleWave());

            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });

            if (message.getDownloadStatus() == 1) {


                try {
                    vh2.download.setVisibility(View.GONE);


                    vh2.progressBar.setVisibility(View.GONE);


                    vh2.cancel.setVisibility(View.GONE);

                    vh2.progressBar2.setVisibility(View.GONE);

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {


                        final File file = new File(message.getAudioPath());


                        if (file.exists()) {
                            MediaPlayer mediaPlayer = new MediaPlayer();
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.setDataSource(mContext, Uri.fromFile(file));

                            vh2.playButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    try {
                                        mediaPlayer.prepare();
                                    } catch (IOException er) {
                                        er.printStackTrace();
                                    }

                                    mediaPlayer.start();
                                    vh2.playButton.setVisibility(View.GONE);
                                    vh2.pauseButton.setVisibility(View.VISIBLE);

                                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {


                                        @Override

                                        public void onCompletion(MediaPlayer mediaPlayer) {

                                            // TODO Auto-generated method stub

                                            mediaPlayer.stop();
                                            vh2.playButton.setVisibility(View.VISIBLE);
                                            vh2.pauseButton.setVisibility(View.GONE);

                                            mediaPlayer = null;

                                        }

                                    });
/*
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

//
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
                                    }*/


                                }
                            });

                            vh2.pauseButton.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    mediaPlayer.stop();
                                    vh2.playButton.setVisibility(View.VISIBLE);
                                    vh2.pauseButton.setVisibility(View.GONE);
                                }
                            });


                            vh2.forward.setVisibility(View.VISIBLE);

                            vh2.forward.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    forwardMessage(5, -1, message.getAudioPath(), null);
                                }
                            });

                        } else {
                            vh2.forward.setVisibility(View.GONE);
                            vh2.playButton.setVisibility(View.GONE);
                            vh2.fnf.setVisibility(View.VISIBLE);

                        }
                    } else {
                        vh2.forward.setVisibility(View.GONE);
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

                } catch (Exception e) {
                    e.printStackTrace();


                }

            } else {
                vh2.forward.setVisibility(View.GONE);
                /*
                 *To allow the option to download
                 */
//                if (message.getDownloadStatus() == 0) {


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
                                builder.setMessage(mContext.getString(R.string.string_506) + (mContext.getString(R.string.space)) +
                                        message.getSize() + (mContext.getString(R.string.space)) + mContext.getString(R.string.string_538));
                                builder.setPositiveButton(R.string.string_578, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        String messageId = message.getMessageId();
                                        message.setDownloading(true);


                                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getAudioPath(), null, mContext.getExternalFilesDir(null)  + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".3gp",


                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);

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


                            requestStorageAccessPermission("audio", 0);
                        }
                    }
                });


                //   }

            }
            if (message.getDeliveryStatus().equals("1")) {

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
                          final String receiverDocid, final GroupChatMessageItem message,
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


                            int replyType = -1;


                            if (message.getMessageType().equals("10")) {


                                replyType = Integer.parseInt(message.getReplyType());
                            }


                            boolean writtenToDisk = writeResponseBodyToDisk(response.body(), filePath,
                                    viewHolder, message.getMessageType(), message.getMessageId(), replyType);


                            message.setDownloading(false);

                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    notifyDataSetChanged();
                                }
                            });


                            if (writtenToDisk) {


                                // deleteFileFromServer(url);


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
                                } else if (type.equals("10")) {
                                    /*
                                     * For reply message
                                     */


                                    switch (Integer.parseInt(message.getReplyType())) {

                                        case 1:
                                            message.setImagePath(filePath);
                                            break;
                                        case 2:
                                            message.setVideoPath(filePath);
                                            break;
                                        case 5:
                                            message.setAudioPath(filePath);
                                            break;
                                        case 7:
                                            message.setImagePath(filePath);
                                            break;
                                        case 9:
                                            message.setDocumentUrl(filePath);
                                            break;

                                    }


                                }


                                ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        notifyDataSetChanged();
                                    }
                                });

                                try {
                                    AppController.getInstance().getDbController().updateDownloadStatusAndPath(receiverDocid,
                                            filePath,
                                            message.getMessageId());

                                } catch (Exception e) {
                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                                ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                                            final RecyclerView.ViewHolder viewHolder, String messageType, final String messageId, int replyType) {


        fileSizeDownloaded = 0;


        if (messageType.equals("1")) {

            try {
                if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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

                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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

        } else if (messageType.equals("10")) {

            switch (replyType) {


                case 1: {


                    try {
                        if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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

                    break;

                }
                case 2: {


                    try {
                        if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                    break;

                }
                case 5: {


                    try {
                        if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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

                    break;
                }
                case 7: {
                    try {
                        if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {

                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                    break;

                }
                case 9: {
                    try {
                        if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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

                    break;
                }

            }


        }


        try {
            // todo change the file location/name according to your needs


            File folder;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                folder = new File(mContext.getExternalFilesDir(null) + "/" + ApiOnServer.APP_NAME);
            } else {
                folder = new File(mContext.getFilesDir().getPath() + "/" + ApiOnServer.APP_NAME);
            }

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


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                                            ((ViewHolderImageSent) viewHolder).progressBar.


                                                    setProgress((int) ((fileSizeDownloaded * 100) / fileSize));


                                        } else {
                                            ((ViewHolderImageReceived) viewHolder).progressBar.


                                                    setProgress((int) ((fileSizeDownloaded * 100) / fileSize));
                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                        } else if (messageType.equals("2")) {


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    try {
                                        if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                                            ((ViewHolderVideoSent) viewHolder).progressBar.setProgress((int)
                                                    ((fileSizeDownloaded * 100) / fileSize));

                                        } else {
                                            ((ViewHolderVideoReceived) viewHolder).progressBar.setProgress((int) ((fileSizeDownloaded * 100) / fileSize));

                                        }
                                    } catch (ArrayIndexOutOfBoundsException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });


                        } else if (messageType.equals("5")) {


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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

                        } else if (messageType.equals("10")) {


                            switch (replyType) {


                                case 1: {


                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                if (mListData.get(viewHolder.getAdapterPosition()).isSelf()) {


                                                    ((ViewHolderImageSent) viewHolder).progressBar.


                                                            setProgress((int) ((fileSizeDownloaded * 100) / fileSize));


                                                } else {
                                                    ((ViewHolderImageReceived) viewHolder).progressBar.


                                                            setProgress((int) ((fileSizeDownloaded * 100) / fileSize));
                                                }
                                            } catch (ArrayIndexOutOfBoundsException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                                    break;
                                }

                                case 2: {


                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                                    break;

                                }
                                case 5: {


                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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
                                    break;

                                }
                                case 7: {


                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                                    break;

                                }
                                case 9: {


                                    ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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

                                    break;
                                }

                            }


                        }


                    } catch (ClassCastException e) {
                        e.printStackTrace();
                    }

                }

                if(messageType.equals("1")) {
                    MediaStore.Images.Media.insertImage(mContext.getContentResolver(), file.getAbsolutePath(),
                            file.getName(), file.getName());
                }

                outputStream.flush();
                return true;
            } catch (ArrayIndexOutOfBoundsException e) {
                return false;
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


                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                    } else if (messageType.equals("10")) {

                        switch (replyType) {

                            case 1: {


                                ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                                break;
                            }
                            case 2: {

                                ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                                break;
                            }
                            case 5: {


                                ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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

                                break;
                            }
                            case 7: {
                                ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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


                                break;
                            }
                            case 9: {

                                ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
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

                                break;
                            }

                        }
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


    private void requestStorageAccessPermission(String type, int k) {

        if (k == 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((GroupChatMessageScreen) mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Snackbar snackbar = Snackbar.make(root, mContext.getString(R.string.string_45, type),
                        Snackbar.LENGTH_INDEFINITE).setAction(mContext.getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions((GroupChatMessageScreen) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                21);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

            } else

            {

                ActivityCompat.requestPermissions((GroupChatMessageScreen) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        21);
            }
        } else {


            if (ActivityCompat.shouldShowRequestPermissionRationale((GroupChatMessageScreen) mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Snackbar snackbar = Snackbar.make(root, mContext.getString(R.string.ForwardPermission, type),
                        Snackbar.LENGTH_INDEFINITE).setAction(mContext.getString(R.string.string_580), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ActivityCompat.requestPermissions((GroupChatMessageScreen) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                41);
                    }
                });


                snackbar.show();


                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);

            } else

            {

                ActivityCompat.requestPermissions((GroupChatMessageScreen) mContext, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        41);
            }


        }

    }


    /*
     * View holders for non-sup specific items
     */

    /*********************************************/
    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderGifReceived(final ViewHolderGifReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {

            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());

            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));
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


                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity) mContext, vh2.gifImage, "image");
                        mContext.startActivity(intent, options.toBundle());

                        vh2.gifImage.setVisibility(View.GONE);


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(8, -1, message.getGifUrl(), null);
                }
            });

            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderGifSent(final ViewHolderGifSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));

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


            if (message.getDeliveryStatus().equals("1")) {

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
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity) mContext, vh2.gifImage, "image");
                        mContext.startActivity(intent, options.toBundle());
                        vh2.gifImage.setVisibility(View.GONE);

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(8, -1, message.getGifUrl(), null);
                }
            });


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")

    private void configureViewHolderStickersSent(final ViewHolderStickerSent vh15, final int position) {
        final GroupChatMessageItem message = mListData.get(position);

        if (message != null) {

            vh15.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh15.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));
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

            if (message.getDeliveryStatus().equals("1")) {

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

            vh15.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(6, -1, message.getStickerUrl(), null);
                }
            });


            if (message.isSelected()) {

                vh15.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh15.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderStickerReceived(final ViewHolderStickerReceived vh16, final int position) {
        final GroupChatMessageItem message = mListData.get(position);


        if (message != null) {


            vh16.senderName.setVisibility(View.VISIBLE);
            vh16.senderName.setText(message.getSenderName());
            vh16.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh16.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));

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


            vh16.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(6, -1, message.getStickerUrl(), null);
                }
            });


            if (message.isSelected()) {

                vh16.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh16.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }

    private void configureViewHolderServerMessage(ViewHolderServerMessage vh14, int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {
            try {
//                if (message.getTextMessage().contains("created") || vh14.getAdapterPosition() == 0) {
//
//
//                    vh14.gap.setVisibility(View.VISIBLE);
//                } else
//                    vh14.gap.setVisibility(View.GONE);

            } catch (Exception e) {
                e.printStackTrace();
            }

            vh14.serverupdate.setText(message.getTextMessage());
        }
    }


    /**
     * Since image size for the doodle is hardcoded as 150dp X 150dp
     */
    @SuppressWarnings("TryWithIdenticalCatches,unchecked")


    private void configureViewHolderDoodleReceived(final ViewHolderDoodleReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {
            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());
            vh2.fnf.setVisibility(View.GONE);


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


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


                                                vh2.forward.setVisibility(View.VISIBLE);

                                                vh2.forward.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {


                                                        forwardMessage(7, -1, message.getImagePath(), null);
                                                    }
                                                });

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


                            vh2.forward.setVisibility(View.GONE);
                            Glide.clear(vh2.imageView);
                            vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                            vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                            vh2.fnf.setVisibility(View.VISIBLE);
                        }

                    } else {

                        vh2.forward.setVisibility(View.GONE);
                        Glide.clear(vh2.imageView);

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

                } else {
                    vh2.forward.setVisibility(View.GONE);

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


                                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //notifyItemChanged(viewHolder.getAdapterPosition());

                                                    notifyDataSetChanged();
                                                }
                                            });

                                            download(message.getImagePath(), message.getThumbnailPath(), mContext.getExternalFilesDir(null)
                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".jpg",
                                                    AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);


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


                                    requestStorageAccessPermission("image", 0);

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


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderDoodleSent(final ViewHolderDoodleSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {


            vh2.fnf.setVisibility(View.GONE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));

            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));


            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });

            if (message.getDownloadStatus() == 1) {


                if (message.getImagePath() != null) {


                    try {
                        vh2.progressBar2.setVisibility(View.GONE);

                        vh2.progressBar.setVisibility(View.GONE);
                        vh2.download.setVisibility(View.GONE);
                        vh2.cancel.setVisibility(View.GONE);

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


                                                vh2.forward.setVisibility(View.VISIBLE);

                                                vh2.forward.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {


                                                        forwardMessage(7, -1, message.getImagePath(), null);
                                                    }
                                                });

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
                            vh2.forward.setVisibility(View.GONE);
                            vh2.fnf.setVisibility(View.VISIBLE);


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

                        vh2.forward.setVisibility(View.GONE);
                        vh2.fnf.setVisibility(View.VISIBLE);
                        Glide.clear(vh2.imageView);
                        vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                        vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));


                    }
                } else {
                    vh2.forward.setVisibility(View.GONE);

                    vh2.imageView.setImageURI(message.getImageUrl());
                }
            } else {
                vh2.forward.setVisibility(View.GONE);

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


                                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getImagePath(), message.getThumbnailPath(), mContext.getExternalFilesDir(null)
                                                        + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".jpg",
                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);


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


                                requestStorageAccessPermission("image", 0);

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

            if (message.getDeliveryStatus().equals("1")) {

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

    @SuppressWarnings("TryWithIdenticalCatches,unchecked")
    private void configureViewHolderDocumentSent(final ViewHolderDocumentSent vh2, int position) {


        final GroupChatMessageItem message = mListData.get(position);


        if (message != null) {


            vh2.fnf.setVisibility(View.GONE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));

            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));


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


                        if (message.getFileType().equals(FilePickerConst.PDF)) {

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
                                Uri data ;
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
                        vh2.forward.setVisibility(View.VISIBLE);

                        vh2.forward.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                forwardDocument(9, message.getDocumentUrl(), message.getMimeType(), message.getFileName(), message.getExtension(), null);
                            }
                        });

                    } else {
                        vh2.forward.setVisibility(View.GONE);

                        vh2.fnf.setVisibility(View.VISIBLE);
                        vh2.documentLayout.setVisibility(View.GONE);


                        vh2.fileType.setVisibility(View.GONE);
                    }
                } else {
                    vh2.forward.setVisibility(View.GONE);
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
                vh2.forward.setVisibility(View.GONE);
                /*
                 *
                 *To allow an option to download
                 *
                 */

                vh2.fileName.setText(message.getFileName());

                vh2.fileType.setText(message.getFileType());
                if (message.getFileType().equals(FilePickerConst.PDF)) {

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

                                        //  String messageId = message.getMessageId();


                                        message.setDownloading(true);


                                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getDocumentUrl(), null, mContext.getExternalFilesDir(null)
                                                        + ApiOnServer.CHAT_DOWNLOADS_FOLDER + message.getFileName(),
                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);


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


                                requestStorageAccessPermission("document", 0);

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


            if (message.getDeliveryStatus().equals("1")) {

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


    private void forwardMessage(int messageType, int replyType, String payload, GroupChatMessageItem chatMessageItem) {
        if (AppController.getInstance().canPublish()) {


            boolean toUpload = false;

            boolean canForward = false;
            switch (messageType) {

                case 0: {
                    canForward = true;


                    break;
                }
                case 1: {
                    toUpload = true;

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (new File(payload).exists()) {
                            canForward = true;
                        }
                    } else {

                        requestStorageAccessPermission("image", 1);

                        return;
                    }

                    break;
                }
                case 2: {
                    toUpload = true;


                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (new File(payload).exists()) {
                            canForward = true;
                        }
                    } else {

                        requestStorageAccessPermission("video", 1);

                        return;
                    }


                    break;

                }
                case 3: {
                    canForward = true;
                    break;
                }
                case 4: {
                    canForward = true;
                    break;
                }
                case 5: {
                    toUpload = true;


                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (new File(payload).exists()) {
                            canForward = true;
                        }
                    } else {

                        requestStorageAccessPermission("audio", 1);

                        return;
                    }


                    break;
                }

                case 6: {
                    canForward = true;
                    break;
                }
                case 7: {
                    toUpload = true;


                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                        if (new File(payload).exists()) {
                            canForward = true;
                        }
                    } else {

                        requestStorageAccessPermission("doodle", 1);

                        return;
                    }


                    break;

                }
                case 8: {
                    canForward = true;
                    break;


                }

                case 10: {

                    switch (replyType) {

                        case 0: {
                            canForward = true;
                            break;
                        }
                        case 1: {
                            toUpload = true;

                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {
                                if (new File(payload).exists()) {
                                    canForward = true;
                                }
                            } else {

                                requestStorageAccessPermission("image", 1);

                                return;
                            }

                            break;
                        }
                        case 2: {
                            toUpload = true;


                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {
                                if (new File(payload).exists()) {
                                    canForward = true;
                                }
                            } else {

                                requestStorageAccessPermission("video", 1);

                                return;
                            }


                            break;

                        }
                        case 3: {
                            canForward = true;
                            break;
                        }
                        case 4: {
                            canForward = true;
                            break;
                        }
                        case 5: {
                            toUpload = true;


                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {
                                if (new File(payload).exists()) {
                                    canForward = true;
                                }
                            } else {

                                requestStorageAccessPermission("audio", 1);

                                return;
                            }


                            break;
                        }

                        case 6: {
                            canForward = true;
                            break;
                        }
                        case 7: {
                            toUpload = true;


                            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    == PackageManager.PERMISSION_GRANTED) {
                                if (new File(payload).exists()) {
                                    canForward = true;
                                }
                            } else {

                                requestStorageAccessPermission("doodle", 1);

                                return;
                            }


                            break;

                        }
                        case 8: {
                            canForward = true;
                            break;


                        }


                    }
                }


            }

            if (canForward) {
                Intent i = new Intent(mContext, ActivityForwardMessage.class);

                i.putExtra("messageType", messageType);


                if (messageType == 10) {

                    i.putExtra("replyType", String.valueOf(replyType));


                    i.putExtra("previousId", chatMessageItem.getPreviousMessageId());
                    i.putExtra("previousType", chatMessageItem.getPreviousMessageType());
                    i.putExtra("previousPayload", chatMessageItem.getPreviousMessagePayload());
                    i.putExtra("previousFrom", chatMessageItem.getPreviousSenderId());
                    if (chatMessageItem.getPreviousSenderId().equals(AppController.getInstance().getUserId()))

                    {
                        i.putExtra("previousReceiverIdentifier", AppController.getInstance().getUserIdentifier());

                    } else {

//                        String contactIdentifier = AppController.getInstance().getDbController().checkContactExists(
//                                AppController.getInstance().getContactsDocId(), chatMessageItem.getPreviousSenderId());
//
//
//                        if (contactIdentifier != null) {
//                            i.putExtra("previousReceiverIdentifier", contactIdentifier);
//
//                        } else {
//                            i.putExtra("previousReceiverIdentifier", chatMessageItem.getPreviousSenderName());
//
//
//                        }
                        i.putExtra("previousReceiverIdentifier", chatMessageItem.getPreviousReceiverIdentifier());
                    }

                    if (chatMessageItem.getPreviousMessageType().equals("9")) {
                        i.putExtra("previousFileType", chatMessageItem.getPreviousFileType());
                    }


                }


                i.putExtra("toUpload", toUpload);

                i.putExtra("payload", payload);


                mContext.startActivity(i);

            } else {

                String str = "";
                switch (messageType) {

                    case 1:


                        str = mContext.getString(R.string.NotFoundImageForward);
                        break;
                    case 2:
                        str = mContext.getString(R.string.NotFoundVideoForward);
                        break;


                    case 5:
                        str = mContext.getString(R.string.NotFoundAudioForward);
                        break;


                    case 7:
                        str = mContext.getString(R.string.NotFoundDoodleForward);
                        break;


                }

                if (root != null) {

                    Snackbar snackbar = Snackbar.make(root, str, Snackbar.LENGTH_SHORT);


                    snackbar.show();
                    View view = snackbar.getView();
                    TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                    txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                }


            }

        } else {


            if (root != null) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_381, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }
    }


    private void forwardDocument(int messageType, String payload, String mimeType, String fileName, String extension, GroupChatMessageItem message) {

        if (AppController.getInstance().canPublish()) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                if (new File(payload).exists()) {


                    Intent i = new Intent(mContext, ActivityForwardMessage.class);

                    i.putExtra("messageType", messageType);


                    if (messageType == 10) {


                        i.putExtra("replyType", "9");

                        i.putExtra("previousId", message.getPreviousMessageId());
                        i.putExtra("previousType", message.getPreviousMessageType());
                        i.putExtra("previousPayload", message.getPreviousMessagePayload());
                        i.putExtra("previousFrom", message.getPreviousSenderId());

                        if (message.getPreviousSenderId().equals(AppController.getInstance().getUserId()))

                        {
                            i.putExtra("previousReceiverIdentifier", AppController.getInstance().getUserIdentifier());

                        } else {

//                            String contactIdentifier = AppController.getInstance().getDbController().checkContactExists(AppController.getInstance().getContactsDocId(), message.getPreviousSenderId());
//
//
//                            if (contactIdentifier != null) {
//                                i.putExtra("previousReceiverIdentifier", contactIdentifier);
//
//                            } else {
//                                i.putExtra("previousReceiverIdentifier", message.getPreviousSenderName());
//
//
//                            }


                            i.putExtra("previousReceiverIdentifier", message.getPreviousReceiverIdentifier());


                        }
                        if (message.getPreviousMessageType().equals("9")) {
                            i.putExtra("previousFileType", message.getPreviousFileType());
                        }


                    }


                    i.putExtra("toUpload", true);

                    i.putExtra("payload", payload);
                    i.putExtra("mimeType", mimeType);
                    i.putExtra("fileName", fileName);
                    i.putExtra("extension", extension);


                    mContext.startActivity(i);


                } else {
                    if (root != null) {

                        Snackbar snackbar = Snackbar.make(root, mContext.getString(R.string.NotFoundDocumentForward), Snackbar.LENGTH_SHORT);


                        snackbar.show();
                        View view = snackbar.getView();
                        TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                        txtv.setGravity(Gravity.CENTER_HORIZONTAL);
                    }

                }
            } else {

                requestStorageAccessPermission("document", 1);


            }


        } else {
            if (root != null) {

                Snackbar snackbar = Snackbar.make(root, R.string.string_382, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        }

    }


    /*
     * View holders for the reply messages
     */


    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderReplyMessageReceived(ViewHolderMessageReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {
            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());
            vh2.previousMessage_head.setText(message.getPreviousSenderName());


            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()

                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());
                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()

                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;
            }


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));
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


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });

            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(10, 0, message.getTextMessage(), message);
                }
            });
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderReplyImageReceived(final ViewHolderImageReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {
            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());
            vh2.previousMessage_head.setText(message.getPreviousSenderName());

            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()

                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;
                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;

            }
            vh2.fnf.setVisibility(View.GONE);


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


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

                                                vh2.forward.setVisibility(View.VISIBLE);
                                                vh2.forward.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {


                                                        forwardMessage(10, 1, message.getImagePath(), message);
                                                    }
                                                });
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


                            vh2.forward.setVisibility(View.GONE);


                            Glide.clear(vh2.imageView);
                            vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                            vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                            vh2.fnf.setVisibility(View.VISIBLE);
                        }

                    } else {

                        vh2.forward.setVisibility(View.GONE);


                        Glide.clear(vh2.imageView);

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

                } else {
                    vh2.forward.setVisibility(View.GONE);

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


                                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //notifyItemChanged(viewHolder.getAdapterPosition());

                                                    notifyDataSetChanged();
                                                }
                                            });

                                            download(message.getImagePath(), message.getThumbnailPath(),
                                                    mContext.getExternalFilesDir(null)  + ApiOnServer.CHAT_DOWNLOADS_FOLDER +
                                                            receiverUid + messageId + ".jpg",
                                                    AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);


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


                                    requestStorageAccessPermission("image", 0);

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


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }


    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderReplyVideoReceived(final ViewHolderVideoReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {
            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());

            vh2.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()

                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;
                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;

            }
            vh2.fnf.setVisibility(View.GONE);


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));

            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


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

                                                                         i.putExtra("videoPath", message.getVideoPath());
                                                                         mContext.startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext).toBundle());


                                                                     }
                                                                 }
                                                             }

                            );
                            vh2.forward.setVisibility(View.VISIBLE);

                            vh2.forward.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    forwardMessage(10, 2, message.getVideoPath(), message);
                                }
                            });

                        } else {
                            vh2.forward.setVisibility(View.GONE);
                            Glide.clear(vh2.thumbnail);
                            vh2.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                            vh2.thumbnail.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));


                            vh2.fnf.setVisibility(View.VISIBLE);


                        }


                    } else {

                        vh2.forward.setVisibility(View.GONE);

                        Glide.clear(vh2.thumbnail);
                        vh2.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                        vh2.thumbnail.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                        vh2.fnf.setVisibility(View.VISIBLE);

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


                } else {

                    vh2.forward.setVisibility(View.GONE);
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


                                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //notifyItemChanged(viewHolder.getAdapterPosition());

                                                    notifyDataSetChanged();
                                                }
                                            });


                                            download(message.getVideoPath(), message.getThumbnailPath(), mContext.getExternalFilesDir(null)  + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".mp4",
                                                    AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);

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


                                    requestStorageAccessPermission("video", 0);


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


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }

            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }


    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")
    private void configureViewHolderReplyLocationReceived(ViewHolderLocationReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {
            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());
            vh2.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()

                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()

                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;
                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;

            }
            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            if (vh2.mMap != null)

                vh2.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//            final String args[] = message.getPlaceInfo().split("@@");
            String args[] = message.getPlaceInfo().split("@@");

            String LatLng = args[0];

            String[] parts = LatLng.split(",");

            String lat = parts[0].substring(1);
            String lng = parts[1].substring(0, parts[1].length() - 1);

            parts = null;
            args = null;
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
//                                        ActivityOptionsCompat.makeSceneTransitionAnimation((GroupChatMessageScreen) mContext).toBundle());
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

            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(10, 3, message.getPlaceInfo(), message);
                }
            });


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }

            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderReplyContactReceived(ViewHolderContactReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {
            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());

            vh2.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;
            }
            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));

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

            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(10, 4, message.getContactInfo(), message);
                }
            });


            vh2.contact_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).checkWriteContactPermission(message.getContactInfo());
                }
            });


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderReplyAudioReceived(final ViewHolderAudioReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);


        if (message != null) {
            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());
            vh2.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;
                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;

            }
            vh2.playButton.setVisibility(View.VISIBLE);
            vh2.fnf.setVisibility(View.GONE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));


            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });


            if (message.getDownloadStatus() == 0) {

                vh2.forward.setVisibility(View.GONE);
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
                                builder.setMessage(mContext.getString(R.string.string_506) + mContext.getString(R.string.space) + message.getSize() +
                                        mContext.getString(R.string.space) + mContext.getString(R.string.string_538));
                                builder.setPositiveButton(R.string.string_578, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        String messageId = message.getMessageId();
                                        message.setDownloading(true);


                                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getAudioPath(), null, mContext.getExternalFilesDir(null)  + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".3gp",
                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);

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


                            requestStorageAccessPermission("audio", 0);
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

//
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


                        vh2.forward.setVisibility(View.VISIBLE);

                        vh2.forward.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                forwardMessage(10, 5, message.getAudioPath(), message);
                            }
                        });

                    } else {


                        vh2.playButton.setVisibility(View.GONE);
                        vh2.fnf.setVisibility(View.VISIBLE);
                        vh2.forward.setVisibility(View.GONE);
                    }
                } else {
                    vh2.forward.setVisibility(View.GONE);

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

            }


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }


    }


    @SuppressWarnings("unchecked,TryWithIdenticalCatches")
    private void configureViewHolderReplyMessageSent(ViewHolderMessageSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {


            vh2.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;
                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;

            }

            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));

            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));

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


            if (message.getDeliveryStatus().equals("1")) {

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


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });


            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(10, 0, message.getTextMessage(), message);
                }
            });
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderReplyImageSent(final ViewHolderImageSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {


            vh2.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()

                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()

                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;
                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;

            }
            vh2.fnf.setVisibility(View.GONE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));

            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));


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


                                                vh2.forward.setVisibility(View.VISIBLE);

                                                vh2.forward.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {


                                                        forwardMessage(10, 1, message.getImagePath(), message);
                                                    }
                                                });

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
                            vh2.forward.setVisibility(View.GONE);
                            vh2.fnf.setVisibility(View.VISIBLE);


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

                        vh2.forward.setVisibility(View.GONE);
                        vh2.fnf.setVisibility(View.VISIBLE);
                        Glide.clear(vh2.imageView);
                        vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                        vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));


                    }
                } else {


                    vh2.imageView.setImageURI(message.getImageUrl());
                }


            } else {


                vh2.forward.setVisibility(View.GONE);

                /*
                 *
                 *To allow an option to download
                 *
                 */


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
                                builder.setMessage(mContext.getString(R.string.string_506) + mContext.getString(R.string.space) + message.getSize() +
                                        mContext.getString(R.string.space) + mContext.getString(R.string.string_534));
                                builder.setPositiveButton(R.string.string_580, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        String messageId = message.getMessageId();


                                        message.setDownloading(true);


                                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getImagePath(), message.getThumbnailPath(), mContext.getExternalFilesDir(null)  + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".jpg",
                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);


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


                                requestStorageAccessPermission("image", 0);

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


            if (message.getDeliveryStatus().equals("1")) {

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


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderReplyVideoSent(final ViewHolderVideoSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {

            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);


                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;
            }
            vh2.previousMessage_head.setText(message.getPreviousSenderName());

            vh2.fnf.setVisibility(View.GONE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));

            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));

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

                                        i.putExtra("videoPath", message.getVideoPath());
                                        mContext.startActivity(i, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) mContext).toBundle());


                                    }
                                }
                            });


                            vh2.forward.setVisibility(View.VISIBLE);

                            vh2.forward.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    forwardMessage(10, 2, message.getVideoPath(), message);
                                }
                            });

                        } else {


                            vh2.forward.setVisibility(View.GONE);
                            Glide.clear(vh2.thumbnail);
                            vh2.fnf.setVisibility(View.VISIBLE);

                            vh2.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                            vh2.thumbnail.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));

                        }
                    } else {
                        vh2.forward.setVisibility(View.GONE);
                        vh2.fnf.setVisibility(View.VISIBLE);
                        Glide.clear(vh2.thumbnail);

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


                vh2.forward.setVisibility(View.GONE);
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
                                builder.setMessage(mContext.getString(R.string.string_506) + mContext.getString(R.string.space) + message.getSize() + mContext.getString(R.string.space) + mContext.getString(R.string.string_535));
                                builder.setPositiveButton(R.string.string_578, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String messageId = message.getMessageId();

                                        String receiverUid = message.getReceiverUid();
                                        message.setDownloading(true);


                                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });


                                        download(message.getVideoPath(), message.getThumbnailPath(), mContext.getExternalFilesDir(null)  + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".mp4",
                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);

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


                                requestStorageAccessPermission("video", 0);


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
            if (message.getDeliveryStatus().equals("1")) {

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


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }

    }


    @SuppressWarnings("unchecked,TryWithIdenticalCatches")
    private void configureViewHolderReplyLocationSent(ViewHolderLocationSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {

            vh2.previousMessage_head.setText(message.getPreviousSenderName());

            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;
                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;

            }
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));

            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));


            if (vh2.mMap != null) {
                vh2.mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
//            final String args[] = message.getPlaceInfo().split("@@");
            String args[] = message.getPlaceInfo().split("@@");
            String LatLng = args[0];

            String[] parts = LatLng.split(",");

            String lat = parts[0].substring(1);
            String lng = parts[1].substring(0, parts[1].length() - 1);

            parts = null;
            args = null;
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
//                                        ActivityOptionsCompat.makeSceneTransitionAnimation((GroupChatMessageScreen) mContext).toBundle());
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


            if (message.getDeliveryStatus().equals("1")) {

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
            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(10, 3, message.getPlaceInfo(), message);
                }
            });


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });

        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")

    private void configureViewHolderReplyContactSent(ViewHolderContactSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {


            vh2.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;
            }

            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + " ");

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


            if (message.getDeliveryStatus().equals("1")) {

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
            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(10, 4, message.getContactInfo(), message);
                }
            });


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderReplyAudioSent(final ViewHolderAudioSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {


            vh2.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;
            }
            vh2.fnf.setVisibility(View.GONE);


            vh2.playButton.setVisibility(View.VISIBLE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));

            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });

            if (message.getDownloadStatus() == 1) {


                try {
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

//
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


                            vh2.forward.setVisibility(View.VISIBLE);

                            vh2.forward.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    forwardMessage(10, 5, message.getAudioPath(), message);
                                }
                            });

                        } else {
                            vh2.forward.setVisibility(View.GONE);
                            vh2.playButton.setVisibility(View.GONE);
                            vh2.fnf.setVisibility(View.VISIBLE);

                        }
                    } else {
                        vh2.forward.setVisibility(View.GONE);
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

                } catch (Exception e) {
                    e.printStackTrace();


                }

            } else {
                vh2.forward.setVisibility(View.GONE);
                /*
                 *To allow the option to download
                 */
//                if (message.getDownloadStatus() == 0) {


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
                                builder.setMessage(mContext.getString(R.string.string_506) + mContext.getString(R.string.space) + message.getSize()
                                        + mContext.getString(R.string.space) + mContext.getString(R.string.string_538));
                                builder.setPositiveButton(R.string.string_578, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        String messageId = message.getMessageId();
                                        message.setDownloading(true);


                                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getAudioPath(), null, mContext.getExternalFilesDir(null)  + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".3gp",


                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);

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


                            requestStorageAccessPermission("audio", 0);
                        }
                    }
                });


                //   }

            }
            if (message.getDeliveryStatus().equals("1")) {

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


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }


    }

    /*
     * View holders for non-sup specific items
     */

    /*********************************************/
    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderReplyGifReceived(final ViewHolderGifReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {
            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());
            vh2.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()

                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;
                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;

            }
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));
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


                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity) mContext, vh2.gifImage, "image");
                        mContext.startActivity(intent, options.toBundle());

                        vh2.gifImage.setVisibility(View.GONE);


                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(10, 8, message.getGifUrl(), message);
                }
            });

            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderReplyGifSent(final ViewHolderGifSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {


            vh2.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;
            }
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));

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


            if (message.getDeliveryStatus().equals("1")) {

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
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity) mContext, vh2.gifImage, "image");
                        mContext.startActivity(intent, options.toBundle());
                        vh2.gifImage.setVisibility(View.GONE);

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            vh2.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(10, 8, message.getGifUrl(), message);
                }
            });


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches")
    private void configureViewHolderReplyStickerReceived(final ViewHolderStickerReceived vh16, final int position) {
        final GroupChatMessageItem message = mListData.get(position);


        if (message != null) {

            vh16.senderName.setVisibility(View.VISIBLE);
            vh16.senderName.setText(message.getSenderName());
            vh16.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh16.previousMessage_iv.setVisibility(View.GONE);
                    vh16.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh16.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh16.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh16.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh16.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh16.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh16.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh16.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh16.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh16.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh16.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh16.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);

                    vh16.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh16.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh16.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh16.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh16.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh16.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh16.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh16.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh16.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh16.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh16.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh16.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh16.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh16.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh16.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh16.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh16.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh16.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh16.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh16.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;
                case 13:

                    vh16.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh16.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh16.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;

            }
            vh16.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh16.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));

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


            vh16.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(10, 6, message.getStickerUrl(), message);
                }
            });


            if (message.isSelected()) {

                vh16.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh16.messageRoot.setBackgroundColor(transparentColor);


            }


            vh16.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches")

    private void configureViewHolderReplyStickersSent(final ViewHolderStickerSent vh15, final int position) {
        final GroupChatMessageItem message = mListData.get(position);

        if (message != null) {


            vh15.previousMessage_head.setText(message.getPreviousSenderName());


            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh15.previousMessage_iv.setVisibility(View.GONE);
                    vh15.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh15.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()

                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh15.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh15.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh15.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh15.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh15.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh15.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh15.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh15.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh15.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh15.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);


                    vh15.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh15.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh15.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh15.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh15.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh15.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh15.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh15.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh15.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh15.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh15.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh15.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh15.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh15.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh15.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh15.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh15.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh15.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh15.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh15.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 13:

                    vh15.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh15.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh15.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;
            }
            vh15.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh15.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));
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

            if (message.getDeliveryStatus().equals("1")) {

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

            vh15.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardMessage(10, 6, message.getStickerUrl(), message);
                }
            });


            if (message.isSelected()) {

                vh15.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh15.messageRoot.setBackgroundColor(transparentColor);


            }


            vh15.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }
    }

    /**
     * Since image size for the doodle is hardcoded as 150dp X 150dp
     */
    @SuppressWarnings("TryWithIdenticalCatches,unchecked")


    private void configureViewHolderReplyDoodleReceived(final ViewHolderDoodleReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {
            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());
            vh2.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()

                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;
            }
            vh2.fnf.setVisibility(View.GONE);


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));
            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


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


                                                vh2.forward.setVisibility(View.VISIBLE);

                                                vh2.forward.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {


                                                        forwardMessage(10, 7, message.getImagePath(), message);
                                                    }
                                                });

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

                            vh2.forward.setVisibility(View.GONE);
                            Glide.clear(vh2.imageView);
                            vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                            vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));
                            vh2.fnf.setVisibility(View.VISIBLE);
                        }

                    } else {

                        vh2.forward.setVisibility(View.GONE);
                        Glide.clear(vh2.imageView);

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

                } else {
                    vh2.forward.setVisibility(View.GONE);

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
                                    builder.setMessage(mContext.getString(R.string.string_506) + mContext.getString(R.string.space) +
                                            message.getSize() + mContext.getString(R.string.space) + mContext.getString(R.string.string_534));
                                    builder.setPositiveButton(R.string.string_580, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id) {


                                            String receiverUid = message.getReceiverUid();

                                            String messageId = message.getMessageId();


                                            message.setDownloading(true);


                                            ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //notifyItemChanged(viewHolder.getAdapterPosition());

                                                    notifyDataSetChanged();
                                                }
                                            });

                                            download(message.getImagePath(), message.getThumbnailPath(), mContext.getExternalFilesDir(null)
                                                            + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".jpg",
                                                    AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);


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


                                    requestStorageAccessPermission("image", 0);

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


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }
    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderReplyDoodleSent(final ViewHolderDoodleSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {


            vh2.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()

                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;
                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;

            }
            vh2.fnf.setVisibility(View.GONE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));

            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));


            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });

            if (message.getDownloadStatus() == 1) {


                if (message.getImagePath() != null) {


                    try {
                        vh2.progressBar2.setVisibility(View.GONE);

                        vh2.progressBar.setVisibility(View.GONE);
                        vh2.download.setVisibility(View.GONE);
                        vh2.cancel.setVisibility(View.GONE);

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


                                                vh2.forward.setVisibility(View.VISIBLE);

                                                vh2.forward.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {


                                                        forwardMessage(10, 7, message.getImagePath(), message);
                                                    }
                                                });

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
                            vh2.forward.setVisibility(View.GONE);
                            vh2.fnf.setVisibility(View.VISIBLE);


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

                        vh2.forward.setVisibility(View.GONE);
                        vh2.fnf.setVisibility(View.VISIBLE);
                        Glide.clear(vh2.imageView);
                        vh2.imageView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.chat_white_circle));
                        vh2.imageView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.color_white));


                    }
                } else {
                    vh2.forward.setVisibility(View.GONE);

                    vh2.imageView.setImageURI(message.getImageUrl());
                }
            } else {
                vh2.forward.setVisibility(View.GONE);

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
                                builder.setMessage(mContext.getString(R.string.string_506) + mContext.getString(R.string.space) +
                                        message.getSize() + mContext.getString(R.string.space) + mContext.getString(R.string.string_534));
                                builder.setPositiveButton(R.string.string_580, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        String messageId = message.getMessageId();


                                        message.setDownloading(true);


                                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getImagePath(), message.getThumbnailPath(), mContext.getExternalFilesDir(null)
                                                        + ApiOnServer.CHAT_DOWNLOADS_FOLDER + receiverUid + messageId + ".jpg",
                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);


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


                                requestStorageAccessPermission("image", 0);

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

            if (message.getDeliveryStatus().equals("1")) {

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


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });
        }
    }

    @SuppressWarnings("TryWithIdenticalCatches,unchecked")

    private void configureViewHolderReplyDocumentReceived(final ViewHolderDocumentReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);


        if (message != null) {
            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());
            vh2.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()

                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;
                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;

            }
            vh2.fnf.setVisibility(View.GONE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));


            vh2.cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {


                    Call<ResponseBody> call = (Call<ResponseBody>) map.get(message.getMessageId());


                    if (call != null)
                        call.cancel();

                }
            });


            if (message.getDownloadStatus() == 0) {

                vh2.forward.setVisibility(View.GONE);

                vh2.fileName.setText(message.getFileName());

                vh2.fileType.setText(message.getFileType());
                if (message.getFileType().equals(FilePickerConst.PDF)) {

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
                                builder.setMessage(mContext.getString(R.string.string_506) + mContext.getString(R.string.space) + message.getSize()
                                        + mContext.getString(R.string.space) + mContext.getString(R.string.string_537));
                                builder.setPositiveButton(R.string.string_580, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        // String messageId = message.getMessageId();


                                        message.setDownloading(true);


                                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getDocumentUrl(), null, mContext.getExternalFilesDir(null)
                                                        + ApiOnServer.CHAT_DOWNLOADS_FOLDER + message.getFileName(),
                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);


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


                                requestStorageAccessPermission("document", 0);

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


                        if (message.getFileType().equals(FilePickerConst.PDF)) {

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


                        vh2.forward.setVisibility(View.VISIBLE);

                        vh2.forward.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                forwardDocument(10, message.getDocumentUrl(), message.getMimeType(), message.getFileName(), message.getExtension(), message);
                            }
                        });

                    } else {

                        vh2.forward.setVisibility(View.GONE);
                        vh2.fnf.setVisibility(View.VISIBLE);
                        vh2.documentLayout.setVisibility(View.GONE);


                        vh2.fileType.setVisibility(View.GONE);
                    }
                } else {


                    vh2.forward.setVisibility(View.GONE);


                    vh2.documentLayout.setVisibility(View.GONE);

                    vh2.forward.setVisibility(View.GONE);
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

            }


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });

        }


    }


    @SuppressWarnings("TryWithIdenticalCatches,unchecked")
    private void configureViewHolderReplyDocumentSent(final ViewHolderDocumentSent vh2, int position) {


        final GroupChatMessageItem message = mListData.get(position);


        if (message != null) {


            vh2.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh2.previousMessage_iv.setVisibility(View.GONE);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }


                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);
                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    vh2.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);


                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh2.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }


                    vh2.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;
                case 13:

                    vh2.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh2.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh2.previousMessage_content.setText(mContext.getString(R.string.Post));

                    break;

            }
            vh2.fnf.setVisibility(View.GONE);


            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));

            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));


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


                        if (message.getFileType().equals(FilePickerConst.PDF)) {

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
                        vh2.forward.setVisibility(View.VISIBLE);

                        vh2.forward.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {


                                forwardDocument(10, message.getDocumentUrl(), message.getMimeType(), message.getFileName(),
                                        message.getExtension(), message);
                            }
                        });

                    } else {
                        vh2.forward.setVisibility(View.GONE);

                        vh2.fnf.setVisibility(View.VISIBLE);
                        vh2.documentLayout.setVisibility(View.GONE);


                        vh2.fileType.setVisibility(View.GONE);
                    }
                } else {
                    vh2.forward.setVisibility(View.GONE);
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
                vh2.forward.setVisibility(View.GONE);
                /*
                 *
                 *To allow an option to download
                 *
                 */

                vh2.fileName.setText(message.getFileName());

                vh2.fileType.setText(message.getFileType());
                if (message.getFileType().equals(FilePickerConst.PDF)) {

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
                                builder.setMessage(mContext.getString(R.string.string_506) + mContext.getString(R.string.space) + message.getSize() +
                                        mContext.getString(R.string.space) + mContext.getString(R.string.string_537));
                                builder.setPositiveButton(R.string.string_580, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {


                                        String receiverUid = message.getReceiverUid();

                                        //  String messageId = message.getMessageId();


                                        message.setDownloading(true);


                                        ((GroupChatMessageScreen) mContext).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                //notifyItemChanged(viewHolder.getAdapterPosition());

                                                notifyDataSetChanged();
                                            }
                                        });

                                        download(message.getDocumentUrl(), null, mContext.getExternalFilesDir(null)
                                                        + ApiOnServer.CHAT_DOWNLOADS_FOLDER + message.getFileName(),
                                                AppController.getInstance().findDocumentIdOfReceiver(receiverUid, ""), message, vh2);


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


                                requestStorageAccessPermission("document", 0);

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

            if (message.getDeliveryStatus().equals("1")) {

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


            vh2.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });

        }


    }
    /*
     * For the message remove feature
     */


    private void configureViewHolderRemoveReceived(ViewHolderRemoveReceived vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {


            vh2.senderName.setVisibility(View.VISIBLE);
            vh2.senderName.setText(message.getSenderName());


            try {
                vh2.message.setText(message.getTextMessage());


            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }

            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));


            if (message.isSelected()) {

                vh2.messageRoot.setBackgroundColor(lightBlueColor);

            } else {

                vh2.messageRoot.setBackgroundColor(transparentColor);


            }

        }
    }

    private void configureViewHolderRemoveSent(ViewHolderRemoveSent vh2, final int position) {
        final GroupChatMessageItem message = mListData.get(position);
        if (message != null) {

            try {
                vh2.message.setText(message.getTextMessage());


            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }

            vh2.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));

            vh2.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));


            String status = message.getDeliveryStatus();

            if (status.equals("3")) {

                vh2.clock.setVisibility(View.GONE);
                vh2.singleTick.setVisibility(View.GONE);

                vh2.doubleTickGreen.setVisibility(View.GONE);
                vh2.doubleTickBlue.setVisibility(View.VISIBLE);

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
     * For the post
     */

    @SuppressWarnings("TryWithIdenticalCatches")

    private void configureViewHolderPostSent(final ViewHolderPostSent vh, final int position) {
        final GroupChatMessageItem message = mListData.get(position);

        if (message != null) {

            vh.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));
            vh.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));
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

            vh.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardPostMessage(13, -1, message.getPostId(),message.getPostTitle(), message.getPostType(),  message.getImagePath(),message);
                }
            });


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
        final GroupChatMessageItem message = mListData.get(position);


        if (message != null) {

            vh.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));

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

            vh.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardPostMessage(13, -1, message.getPostId(),message.getPostTitle(), message.getPostType(),  message.getImagePath(), message);
                }
            });


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
    private void configureViewHolderReplyPostReceived(final ViewHolderPostReceived vh, final int position) {
        final GroupChatMessageItem message = mListData.get(position);


        if (message != null) {

            vh.previousMessage_head.setText(message.getPreviousSenderName());
            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh.previousMessage_iv.setVisibility(View.GONE);
                    vh.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);

                    vh.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;


                case 13:

                    vh.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh.previousMessage_content.setText(mContext.getString(R.string.Post));
                    break;



            }
            vh.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));


            vh.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));
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


            vh.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    forwardPostMessage(10, 13, message.getPostId(),message.getPostTitle(), message.getPostType(),  message.getImagePath(), message);
                }
            });

            vh.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });

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

    private void configureViewHolderReplyPostSent(final ViewHolderPostSent vh, final int position) {
        final GroupChatMessageItem message = mListData.get(position);

        if (message != null) {


            vh.previousMessage_head.setText(message.getPreviousSenderName());


            switch (Integer.parseInt(message.getPreviousMessageType())) {

                case 0:

                    vh.previousMessage_iv.setVisibility(View.GONE);
                    vh.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 1:
                    vh.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .centerCrop()

                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh.previousMessage_content.setText(mContext.getString(R.string.Image));

                    break;

                case 2:
                    vh.previousMessage_iv.setVisibility(View.VISIBLE);

                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh.previousMessage_content.setText(mContext.getString(R.string.Video));

                    break;


                case 3:
                    vh.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(R.drawable.image)


                                .bitmapTransform(new CenterCrop(mContext), new BlurTransformation(mContext, 5))


                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 4:
                    vh.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh.previousMessage_iv.setImageResource(R.drawable.ic_default_profile_pic);


                    vh.previousMessage_content.setText(message.getPreviousMessagePayload());

                    break;

                case 5:

                    vh.previousMessage_iv.setVisibility(View.VISIBLE);


                    vh.previousMessage_iv.setImageResource(R.drawable.ic_play_arrow_black_48px);
                    vh.previousMessage_content.setText(mContext.getString(R.string.Audio));

                    break;


                case 6:

                    vh.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)
                                .centerCrop()

                                .into(vh.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh.previousMessage_content.setText(mContext.getString(R.string.Sticker));

                    break;


                case 7:

                    vh.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh.previousMessage_content.setText(mContext.getString(R.string.Doodle));
                    break;


                case 8:
                    vh.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)

                                .centerCrop()
                                .placeholder(R.drawable.home_grid_view_image_icon)


                                .into(vh.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    vh.previousMessage_content.setText(mContext.getString(R.string.Gif));
                    break;

                case 9:
                    vh.previousMessage_iv.setVisibility(View.VISIBLE);
                    if (message.getPreviousFileType().equals(FilePickerConst.PDF)) {
                        vh.previousMessage_iv.setImageResource(R.drawable.ic_pdf);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.DOC)) {
                        vh.previousMessage_iv.setImageResource(R.drawable.ic_word);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.PPT)) {
                        vh.previousMessage_iv.setImageResource(R.drawable.ic_ppt);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.XLS)) {
                        vh.previousMessage_iv.setImageResource(R.drawable.ic_excel);
                    } else if (message.getPreviousFileType().equals(FilePickerConst.TXT)) {
                        vh.previousMessage_iv.setImageResource(R.drawable.ic_txt);
                    }

                    vh.previousMessage_content.setText(message.getPreviousMessagePayload());


                    break;

                case 13:

                    vh.previousMessage_iv.setVisibility(View.VISIBLE);
                    try {
                        Glide
                                .with(mContext)
                                .load(message.getPreviousMessagePayload())

                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)


                                .placeholder(R.drawable.home_grid_view_image_icon)

                                .centerCrop()
                                .into(vh.previousMessage_iv);

                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                    vh.previousMessage_content.setText(mContext.getString(R.string.Post));
                    break;

            }
            vh.date.setText(findOverlayDate(message.getMessageDateOverlay()) + mContext.getString(R.string.space));
            vh.time.setText(convert24to12hourformat(message.getTS()) + mContext.getString(R.string.space));
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

            if(message.getPostType() == 0){


                vh.playButton.setVisibility(View.GONE);

            }
            else{


                vh.playButton.setVisibility(View.VISIBLE);
            }

            vh.forward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    forwardPostMessage(10, 13, message.getPostId(),message.getPostTitle(), message.getPostType(),  message.getImagePath(), message);
                }
            });

            vh.previousMessage_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ((GroupChatMessageScreen) mContext).scrollToMessage(message.getPreviousMessageId());
                }
            });

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


    private void forwardPostMessage(int messageType, int replyType,String postId,String postTitle, int postType, String postImageUrl, GroupChatMessageItem messageItem){

        /*
         * To forward a post message
         *
         */

        if (AppController.getInstance().canPublish()) {

            Intent i = new Intent(mContext, ActivityForwardMessage.class);

            i.putExtra("messageType", messageType);
            i.putExtra("postId", postId);
            i.putExtra("postType", postType);
            i.putExtra("postTitle", postTitle);
            i.putExtra("toUpload", false);
            i.putExtra("payload", postImageUrl);


            i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

            if (messageType == 10) {

                /*
                 * For the reply message to be forwarded(replied upon a message by a post---13)
                 */

                i.putExtra("replyType", String.valueOf(replyType));
                i.putExtra("previousId", messageItem.getPreviousMessageId());
                i.putExtra("previousType", messageItem.getPreviousMessageType());
                i.putExtra("previousPayload", messageItem.getPreviousMessagePayload());
                i.putExtra("previousFrom", messageItem.getPreviousSenderId());


                if (messageItem.getPreviousSenderId().equals(AppController.getInstance().getUserId()))

                {
                    i.putExtra("previousReceiverIdentifier", AppController.getInstance().getUserIdentifier());

                } else {

                    String contactIdentifier = AppController.getInstance().getDbController().checkFriendExists(
                            AppController.getInstance().getFriendsDocId(), messageItem.getPreviousSenderId());


                    if (contactIdentifier != null) {
                        i.putExtra("previousReceiverIdentifier", contactIdentifier);

                    } else {
                        i.putExtra("previousReceiverIdentifier", messageItem.getPreviousSenderName());

                    }

                }

                if (messageItem.getPreviousMessageType().equals("9")) {
                    i.putExtra("previousFileType", messageItem.getPreviousFileType());
                }


            }

            mContext.startActivity(i);

        } else {


            if (root != null) {

                Snackbar snackbar = Snackbar.make(root, R.string.ForwardPostFailed, Snackbar.LENGTH_SHORT);


                snackbar.show();
                View view = snackbar.getView();
                TextView txtv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
                txtv.setGravity(Gravity.CENTER_HORIZONTAL);
            }
        }

    }
}