package chat.hola.com.app.socialDetail.video_manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.CountFormat;
import chat.hola.com.app.Utilities.TagSpannable;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.models.Business;
import chat.hola.com.app.socialDetail.ViewHolder;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bumptech.glide.load.engine.DiskCacheStrategy.NONE;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 11/30/2018.
 */

public class VideoItem extends BaseVideoItem {

    private String POST_TYPE_REGULAR = "Regular";
    //    private final Picasso mImageLoader;

    private final RequestManager mImageLoader;
    private final Data data;
    private long likes;
    private String userId;
    private String postId;
    private String musicId;
    private String channelId;
    private String categoryId;
    private Context context;
    private String placeHolderForLongText, placeHolderForUsernameBegining;

    private DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
    private double screenRatio =
        ((double) displayMetrics.heightPixels / displayMetrics.widthPixels);

    private final TypefaceManager typefaceManager;
    private Animation rotate;
    @SuppressWarnings("unchecked")
    public VideoItem(VideoPlayerManager videoPlayerManager, RequestManager imageLoader, Data data,
        Context context) {
        //    public VideoItem(VideoPlayerManager videoPlayerManager, Picasso imageLoader, Data data, Context context) {
        super(videoPlayerManager);
        mImageLoader = imageLoader;
        this.data = data;
        this.context = context;
        placeHolderForLongText = context.getString(R.string.placeholder_for_long_text);
        placeHolderForUsernameBegining =
            context.getString(R.string.placeholder_for_username_begining);
        typefaceManager = new TypefaceManager(context);
        rotate = AnimationUtils.loadAnimation(context, R.anim.rotate);
    }

    /*
     * Bug Title: need to pull small thumbnail url for profile pic
     * Bug Id: DUBAND001
     * Fix Description: load the image from thumbnail
     * Developer Name: Hardik
     * Fix Date: 6/4/2021
     * */

    @SuppressWarnings("TryWithIdenticalCatches")
    @Override
    public void update(int position, ViewHolder viewHolder, VideoPlayerManager videoPlayerManager,HashMap<String,Boolean> mTogglePositions) {
        viewHolder.mCover.setVisibility(View.VISIBLE);

        try {
            double ratio =
                ((double) Integer.parseInt(data.getImageUrl1Height())) / Integer.parseInt(
                    data.getImageUrl1Width());
            String thumbnailUrl = data.getThumbnailUrl1();

            if (data.getPurchased()) {

                if (!thumbnailUrl.contains("upload/t_media_lib_thumb")) {

                    thumbnailUrl = thumbnailUrl.replace("upload", "upload/t_media_lib_thumb");
                }
            }
            thumbnailUrl =
                Utilities.getModifiedThumbnailLinkForPosts(thumbnailUrl, data.getPurchased());

            if (ratio > screenRatio) {

                try {
                    DrawableRequestBuilder<String> thumbnailRequest = Glide.with(context)
                        .load(thumbnailUrl)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE);

                    mImageLoader.load(
                        Utilities.getCoverImageUrlForPost(data.getImageUrl1(), data.getMediaType1(),
                            data.getPurchased(), thumbnailUrl))
                        .thumbnail(thumbnailRequest)
                        .dontAnimate()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(viewHolder.mCover);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    DrawableRequestBuilder<String> thumbnailRequest = Glide.with(context)
                        .load(thumbnailUrl)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE);

                    mImageLoader.load(
                        Utilities.getCoverImageUrlForPost(data.getImageUrl1(), data.getMediaType1(),
                            data.getPurchased(), thumbnailUrl))
                        .thumbnail(thumbnailRequest)
                        .dontAnimate()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(viewHolder.mCover);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            try {
                String thumbnailUrl = data.getThumbnailUrl1();

                if (data.getPurchased()) {

                    if (!thumbnailUrl.contains("upload/t_media_lib_thumb")) {

                        thumbnailUrl = thumbnailUrl.replace("upload", "upload/t_media_lib_thumb");
                    }
                }

                thumbnailUrl =
                    Utilities.getModifiedThumbnailLinkForPosts(thumbnailUrl, data.getPurchased());
                DrawableRequestBuilder<String> thumbnailRequest = Glide.with(context)
                    .load(thumbnailUrl)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE);

                mImageLoader.load(
                    Utilities.getCoverImageUrlForPost(data.getImageUrl1(), data.getMediaType1(),
                        data.getPurchased(), thumbnailUrl))
                    .thumbnail(thumbnailRequest)
                    .dontAnimate()
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(viewHolder.mCover);
            } catch (IllegalArgumentException ef) {
                e.printStackTrace();
            } catch (NullPointerException ef) {
                e.printStackTrace();
            }
        }

        displayData(viewHolder, data,position,mTogglePositions);
    }

    @Override
    public void playNewVideo(MetaData currentItemMetaData, VideoPlayerView player,
        VideoPlayerManager<MetaData> videoPlayerManager, AppCompatImageView ivRecord,
        boolean newPost) {
        ivRecord.clearAnimation();
        if (data.getMediaType1() == 1) {

            //boolean isSelf = data.getUserId().equals(AppController.getInstance().getUserId());

            //if (isSelf || !data.getPaid() || (data.getPaid() && data.getPurchased())) {
            if (data.getPurchased()) {
                //if (!data.isPaused()) {

                if (newPost) {
                    videoPlayerManager.playNewVideo(currentItemMetaData, player,
                        data.getImageUrl1(), data.isVideoMuted());
                } else {
                    videoPlayerManager.playNewVideo(currentItemMetaData, player,
                        Utilities.getModifiedVideoLink(data.getImageUrl1()), data.isVideoMuted());
                }
                //}
                //else{
                //  videoPlayerManager.stopAnyPlayback(true);
                //}
            } else {
                videoPlayerManager.stopAnyPlayback(true);
            }
            if (data.getDub() != null && data.getDub().getId() != null && !data.getDub()
                .getId()
                .isEmpty()) {
                ivRecord.startAnimation(rotate);
            }
        } else {
            videoPlayerManager.stopAnyPlayback(true);
        }
    }

    @Override
    public void stopPlayback(VideoPlayerManager videoPlayerManager) {
        videoPlayerManager.stopAnyPlayback(true);
    }

    //display data
    @SuppressLint("ClickableViewAccessibility")
    public void displayData(ViewHolder holder, Data data,int position, HashMap<String,Boolean> mTogglePositions) {
        userId = data.getUserId();
        likes = Long.parseLong(data.getLikesCount());
        postId = data.getPostId();
        boolean isChannel = data.getChannelId() != null && !data.getChannelId().isEmpty();

        holder.mPlayer.setRotation(data.getOrientation() == null ? 0 : data.getOrientation());

        /* BUSINESS POST START*/
        Business business = data.getBusiness();
        boolean isBusiness =
            business != null && !business.getBusinessPostType().equalsIgnoreCase("regular");
        holder.rlAction.setVisibility(isBusiness
            && business.getBusinessPostTypeLabel() != null
            && !business.getBusinessPostTypeLabel().equalsIgnoreCase(POST_TYPE_REGULAR)
            ? View.VISIBLE : View.GONE);
        if (isBusiness) {
            if (business.getBusinessButtonColor() != null && !business.getBusinessButtonColor()
                .isEmpty()) {

                holder.rlAction.getBackground()
                    .setColorFilter(Color.parseColor(business.getBusinessButtonColor()),
                        PorterDuff.Mode.SRC_ATOP);
            }
            holder.tvActionButton.setText(business.getBusinessButtonText());
            if (business.getBusinessPrice() != null) {
                String currencySymbol = business.getBusinessCurrency();
                holder.tvPrice.setText(placeHolderForUsernameBegining
                    + " "
                    + currencySymbol
                    + " "
                    + business.getBusinessPrice());
            }
        }
        /* BUSINESS POST END*/

        if (isBusiness) {
            //Allowing maximum 18 characters for userName textview, and
            // length includes one special @ character at begining
            String businessName = business.getBusinessName();

            if (businessName.length() > 17) {
                businessName = placeHolderForUsernameBegining
                    + businessName.substring(0, 13)
                    + placeHolderForLongText;
            } else {
                businessName = placeHolderForUsernameBegining+businessName;
            }

            holder.tvUserName.setText(businessName);
            if(!TextUtils.isEmpty(business.getBusinessProfilePic())) {
                Glide.with(context)
                        .load(business.getBusinessProfilePic())
                        .asBitmap()
                        .centerCrop()
                        .placeholder(R.drawable.default_profile)
                        .signature(new StringSignature(
                                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        //.diskCacheStrategy(DiskCacheStrategy.NONE)
                        //    .skipMemoryCache(true)
                        .into(new BitmapImageViewTarget(holder.ivProfilePic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                holder.ivProfilePic.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            }else{
                Utilities.setTextRoundDrawable(context,business.getBusinessName(),"",holder.ivProfilePic);
            }
        } else {
            String pic = isChannel ? data.getChannelImageUrl() : data.getProfilepic();
            if(!TextUtils.isEmpty(pic)) {
                Glide.with(context)
                        .load(pic)
                        .asBitmap()
                        .signature(new StringSignature(
                                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        .centerCrop()
                        .placeholder(R.drawable.default_profile)
                        //.diskCacheStrategy(DiskCacheStrategy.NONE)
                        //.skipMemoryCache(true)
                        .into(new BitmapImageViewTarget(holder.ivProfilePic) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                holder.ivProfilePic.setImageDrawable(circularBitmapDrawable);
                            }
                        });
            } else {
                Utilities.setTextRoundDrawable(context,
                        isChannel ? data.getChannelName() : data.getFirstName(),
                        isChannel ? "" : data.getLastName(),
                    holder.ivProfilePic);
                //As first name and last name were both null or empty.
                //Utilities.setTextRoundDrawable(context,data.getFirstName(),data.getLastName(),holder.ivProfilePic);
            }
            //Allowing maximum 18 characters for userName textview, and
            // length includes one special @ character at begining
            String userName = isChannel ? data.getChannelName() : data.getUsername();

            if (userName.length() > 17) {
                userName = placeHolderForUsernameBegining
                    + userName.substring(0, 13)
                    + placeHolderForLongText;
            } else {
                userName = placeHolderForUsernameBegining+userName;
            }

            holder.tvUserName.setText(userName);
        }
        //        holder.ivProfilePic.setImageURI(data.getProfilepic());

        holder.ivStarBadge.setVisibility(data.isStar() ? View.VISIBLE : View.GONE);
        //        holder.tvUserName.setText(data.getUsername());
        holder.tvViewCount.setText(CountFormat.format(Long.parseLong(data.getDistinctViews())));
        holder.tvLikeCount.setText(CountFormat.format(likes));
        try {
            holder.tvCommentCount.setText(
                CountFormat.format(Long.parseLong(data.getCommentCount())));
        } catch (NumberFormatException e) {
            holder.tvCommentCount.setText("0");
        }
        //if (AppController.getInstance().getUserId() != null) {
        //    holder.ibFollow.setVisibility(
        //            AppController.getInstance().getUserId().equals(userId) ? View.GONE : View.VISIBLE);
        //}
        //        holder.cbFollow.setChecked(data.getFollowStatus() != 0);
        //        holder.ivLike.setChecked(data.isLiked());

        //music

        if (data.getDub() != null && data.getDub().getId() != null && !data.getDub()
            .getId()
            .isEmpty()) {
            holder.rlMusic.setVisibility(View.VISIBLE);
            holder.tvMusic.setText(data.getDub().getName()+" ");
            holder.tvMusic.setSelected(true);

            if (data.getDub().getArtist() != null) {
                holder.tvArtist.setVisibility(View.VISIBLE);
                holder.tvArtist.setText(
                    context.getString(R.string.music_by, data.getDub().getArtist())+" ");
            } else {
                holder.tvArtist.setVisibility(View.GONE);
            }
            musicId = data.getDub().getId();

            try {
                Glide.with(context)
                    .load(R.raw.music_animation)
                    .asGif()
                    .diskCacheStrategy(NONE)
                    .into(holder.ivMusicAnimation);
            } catch (Exception ignore) {

            }

            try {
                Glide.with(context)
                    .load(data.getDub().getImageUrl())
                    .asBitmap()
                    .signature(new StringSignature(AppController.getInstance()
                        .getSessionManager()
                        .getUserProfilePicUpdateTime()))
                    .centerCrop()
                    .into(new BitmapImageViewTarget(holder.ivRecord) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(),
                                    resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.ivRecord.setImageDrawable(circularBitmapDrawable);
                        }
                    });
            } catch (Exception ignore) {
            }
        } else {
            holder.rlMusic.setVisibility(View.GONE);
        }

        //title
        if (data.getTitle() != null && !data.getTitle().trim().equals("")) {
            holder.postTitleTv.setVisibility(View.VISIBLE);
            SpannableString spanString = new SpannableString(data.getTitle());
            Matcher matcher = Pattern.compile("#([A-Za-z0-9_-]+)").matcher(spanString);
            findMatch(spanString, matcher);
            Matcher userMatcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
            findMatch(spanString, userMatcher);
            holder.postTitleTv.setText(spanString,mTogglePositions,data.getPostId(), typefaceManager);

        } else {
            holder.postTitleTv.setVisibility(View.GONE);
        }

        //location
        if (data.getPlace() != null && !data.getPlace().trim().equals("")) {
            holder.tvLocation.setVisibility(View.VISIBLE);
            holder.tvLocation.setText(data.getPlace());
        } else {
            holder.tvLocation.setVisibility(View.GONE);
        }
        //divide
        //if ((data.getChannelName() != null && !data.getChannelName().isEmpty())
        //    && (data.getCategoryName() != null && !data.getCategoryName().isEmpty())) {
        //    holder.tvDivide.setVisibility(View.VISIBLE);
        //} else {
        //    holder.tvDivide.setVisibility(View.GONE);
        //}

        //category
        //if (data.getCategoryName() != null && !data.getCategoryName().trim().equals("")) {
        //    /**
        //     * Have hidden this intentionally(and not removed) as asked by @rahul sir,since in ui although this field is received but we don't have to show in UI
        //     */
        //    holder.tvCategory.setVisibility(View.GONE);
        //    //holder.tvCategory.setVisibility(View.VISIBLE);
        //    holder.tvCategory.setText(data.getCategoryName());
        //    categoryId = data.getCategoryId();
        //} else {
        //    holder.tvCategory.setVisibility(View.GONE);
        //}

//        if (data.getBookMarked()) {
//            holder.ibSaved.setImageDrawable(context.getDrawable(R.drawable.ic_saved));
//        } else {
//            holder.ibSaved.setImageDrawable(context.getDrawable(R.drawable.ic_unsaved));
//        }
    }

    private void findMatch(SpannableString spanString, Matcher matcher) {
        while (matcher.find()) {
            final String tag = matcher.group(0);
            spanString.setSpan(new TagSpannable(context, tag, R.color.color_white), matcher.start(),
                matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }
}
