package chat.hola.com.app.socialDetail;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.OnSwipeTouchListener;
import chat.hola.com.app.Utilities.TextSpannable;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.models.Business;
import chat.hola.com.app.socialDetail.video_manager.BaseVideoItem;
import chat.hola.com.app.socialDetail.video_manager.ClickListner;
import com.ezcall.android.R;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import java.util.HashMap;
import java.util.List;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 11/27/2018.
 */
public class SocialDetailAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final VideoPlayerManager mVideoPlayerManager;
    private List<BaseVideoItem> baseVideoItems;
    int count;
    private List<Data> dataList;
    private Context context;
    private ClickListner clickListner;
    private int height, width;
    private static final DecelerateInterpolator DECCELERATE_INTERPOLATOR =
        new DecelerateInterpolator();
    private static final AccelerateInterpolator ACCELERATE_INTERPOLATOR =
        new AccelerateInterpolator();
    Handler handler = new Handler();
    Runnable runnable;
    private final TypefaceManager typefaceManager;
    private final HashMap<String, Boolean> mPostTitleToggledPositions = new HashMap();
    private final boolean isActivity;
    private final DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();

    public SocialDetailAdapter(VideoPlayerManager mVideoPlayerManager, Context context,
        List<BaseVideoItem> baseVideoItems, List<Data> dataList, int width, int height,
        boolean isActivity) {
        this.mVideoPlayerManager = mVideoPlayerManager;
        this.context = context;
        this.baseVideoItems = baseVideoItems;
        this.dataList = dataList;
        this.height = height;
        this.width = width;
        this.isActivity = isActivity;
        typefaceManager = new TypefaceManager(context);
    }

    public void setDataList(List<BaseVideoItem> baseVideoItems, List<Data> dataList) {
        this.dataList = dataList;
        this.baseVideoItems = baseVideoItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseVideoItem videoItem = baseVideoItems.get(viewType);
        View itemView = videoItem.createView(parent, height, width);

        return new ViewHolder(itemView,typefaceManager);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            RelativeLayout.LayoutParams params =
                (RelativeLayout.LayoutParams) holder.llActions.getLayoutParams();
            if (isActivity) {
                params.setMargins(0, 0, 0, (int) (displayMetrics.density * 13));
            } else {

                params.setMargins(0, 0, 0, (int) (displayMetrics.density * 56));
            }
            holder.llActions.setLayoutParams(params);

            if (AppController.getInstance().isGuest())
            //holder.ibFollow.setVisibility(View.GONE);
            {
                holder.tvFollow.setVisibility(View.GONE);
            }
            //holder.tvLikeCount.setTypeface(typefaceManager.getSemiboldFont());
            //holder.tvViewCount.setTypeface(typefaceManager.getSemiboldFont());
            //holder.tvUserName.setTypeface(typefaceManager.getSemiboldFont());
            //holder.tvLocation.setTypeface(typefaceManager.getMediumFont());
            //holder.tvTitle.setTypeface(typefaceManager.getMediumFont());
            //holder.tvMusic.setTypeface(typefaceManager.getMediumFont());
            //holder.tvCategory.setTypeface(typefaceManager.getSemiboldFont());

            handler.removeCallbacks(runnable);
            holder.postTitleTv.setOnExpandStateChangeListener(mPostTitleToggledPositions::put);
            BaseVideoItem videoItem = baseVideoItems.get(position);
            videoItem.update(position, holder, mVideoPlayerManager, mPostTitleToggledPositions);
            Data data = dataList.get(position);
            boolean isSelf = data.getUserId().equals(AppController.getInstance().getUserId());
            holder.rlSendTip.setVisibility(isSelf ? View.GONE : View.VISIBLE);
            //holder.btnSendTip.setVisibility(isSelf ? View.GONE : View.VISIBLE);

            //holder.ibFollow.setVisibility(data.getId().equals(AppController.getInstance().getUserId()) ? View.GONE : View.VISIBLE);

            if (isSelf) {
                holder.tvFollow.setVisibility(View.GONE);
                holder.ivFollowing.setVisibility(View.INVISIBLE);
            } else {

                boolean isChecked;

                switch (data.getFollowStatus()) {
                    case 0:
                        //public - unfollow
                        //isPrivate = data.getPrivate()==1;
                        isChecked = false;
                        break;
                    case 1:
                        //public - follow
                        //isPrivate = false;
                        isChecked = true;
                        holder.ivFollowing.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.ic_homepage_following));
                        break;
                    case 2:
                        //private - requested
                        //isPrivate = true;
                        isChecked = true;
                        holder.ivFollowing.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.ic_homepage_pending));
                        break;
                    case 3:
                        //private - request
                        //isPrivate = true;
                        isChecked = false;
                        break;
                    default:
                        isChecked = false;
                        break;
                }

                //holder.ibFollow.setTextOn(context.getResources().getString(R.string.following));
                //holder.ibFollow.setTextOff(context.getResources().getString(R.string.follow));
                //holder.ibFollow.setChecked(isChecked);
                //
                //holder.ibFollow.setOnClickListener(
                //    view -> clickListner.follow(holder.ibFollow.isChecked(), data.getId()));
                //holder.ibFollow.setSelected(data.getFollowStatus() != 0);

                if (isChecked) {
                    holder.ivFollowing.setVisibility(View.VISIBLE);
                    holder.tvFollow.setVisibility(View.GONE);
                } else {
                    holder.ivFollowing.setVisibility(View.INVISIBLE);
                    holder.tvFollow.setVisibility(View.VISIBLE);
                }
            }
            holder.ibLike.setSelected(data.isLiked());

            if(data.getTipsAmount()!=null && data.getTipsAmount()>0) {
                holder.tvTipCollect.setVisibility(View.VISIBLE);
                holder.tvTipCollect.setText(Utilities.formatMoney(data.getTipsAmount()));
            }


            holder.rlSendTip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AppController.getInstance().isGuest()) {
                        AppController.getInstance().openSignInDialog(context);
                    } else {
                        clickListner.sendTip(position);
                    }
                }
            });


            //holder.ivRecord.clearAnimation();
            if (data.getMediaType1() == 1) {
                //Video post
                if (data.getDub() != null && data.getDub().getId() != null && !data.getDub()
                    .getId()
                    .isEmpty()) {
                    holder.ivRecord.setVisibility(View.VISIBLE);
                    holder.ivMusicAnimation.setVisibility(View.VISIBLE);
                } else {
                    holder.ivRecord.setVisibility(View.GONE);
                    holder.ivMusicAnimation.setVisibility(View.GONE);
                }

                holder.ivMute.setVisibility(View.VISIBLE);
                holder.ivMute.setSelected(data.isVideoMuted());

                //if (data.isPaused()) {
                //  holder.ivPlay.setVisibility(View.VISIBLE);
                //} else {
                //holder.ivPlay.setVisibility(View.GONE);
                //}
            } else {
                //Image Post
                //holder.ivPlay.setVisibility(View.GONE);
                holder.ivMute.setVisibility(View.GONE);
                holder.ivRecord.setVisibility(View.GONE);
                holder.ivMusicAnimation.setVisibility(View.GONE);
            }

            Business business = data.getBusiness();
            holder.rlAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (business != null && business.getBusinessButtonText() != null) {
                        clickListner.onActionButtonClick(business.getBusinessButtonText(),
                                business.getBusinessUrl());
                    }
                }
            });

            //holder.mCover.setVisibility(data.hideCover() ? View.GONE : View.VISIBLE);

            boolean isChannel = data.getChannelId() != null && !data.getChannelId().isEmpty();
            boolean isBusiness =
                business != null && !business.getBusinessPostType().equalsIgnoreCase("regular");

            //            if (AppController.getInstance().isGuest()) {
            //                AppController.getInstance().openSignInDialog(context);
            //            }

            /* PAID POST*/
            if ( data.getPurchased()) {
                holder.rlPaidPostOverlay.setVisibility(View.GONE);

            } else {
                holder.rlPaidPostOverlay.setVisibility(View.VISIBLE);
                holder.tvUnlockCoins.setText(String.valueOf(data.getPostAmount()));

                holder.llUnlock.setOnClickListener(v -> {
                    if (AppController.getInstance().isGuest()) {
                        AppController.getInstance().openSignInDialog(context);
                    } else {
                        clickListner.payForPost(data, position, false);
                    }
                });

                if (data.getSubscriptionAmount() != null && data.getSubscriptionAmount() > 0) {
                    holder.tvSubscribeCoins.setText(
                        context.getString(R.string.subscribe_for_month, data.getSubscriptionAmount()));
                    holder.llSubscribe.setVisibility(View.VISIBLE);
                    holder.llSubscribe.setOnClickListener(v -> {
                        if (AppController.getInstance().isGuest()) {
                            AppController.getInstance().openSignInDialog(context);
                        } else {
                            clickListner.subscribeProfile(data, position, false);
                        }
                    });
                } else {
                    holder.llSubscribe.setVisibility(View.GONE);
                }
            }


            /* PAID POST END*/

            //comment
            holder.ivComment.setSelected(data.isAllowComments());
            holder.ivComment.setOnClickListener(view -> {
                if (!AppController.getInstance().isGuest()) {
                    clickListner.comment(data, data.getPostId(), position, data.getCommentCount(),
                        data.isAllowComments());
                } else {
                    AppController.getInstance().openSignInDialog(context);
                }
            });

            //forward
            holder.ivForward.setOnClickListener(view -> {
                if (!AppController.getInstance().isGuest()) {

                    if (!data.getPaid()) {
                        clickListner.send(position);
                    } else {
                        Toast.makeText(context, context.getString(R.string.paid_post_can_not_be_share),
                            Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AppController.getInstance().openSignInDialog(context);
                }
            });

            //profile
            holder.ivProfilePic.setOnClickListener(view -> {
                if (!AppController.getInstance().isGuest()) {
                    clickListner.profile(isChannel ? data.getChannelId() : data.getUserId(),
                        isChannel, isBusiness);
                } else {
                    AppController.getInstance().openSignInDialog(context);
                }
            });
            holder.tvUserName.setOnClickListener(view -> {
                if (!AppController.getInstance().isGuest()) {
                    clickListner.profile(isChannel ? data.getChannelId() : data.getUserId(), isChannel,
                            isBusiness);
                } else {
                    AppController.getInstance().openSignInDialog(context);
                }
            });

            //channel
            holder.tvChannel.setOnClickListener(view -> {
                if (!AppController.getInstance().isGuest()) {
                    clickListner.channel(data.getChannelId(), data.getChannelName());
                } else {
                    AppController.getInstance().openSignInDialog(context);
                }
            });

            //category
            holder.tvCategory.setOnClickListener(view -> {
                if (!AppController.getInstance().isGuest()) {
                    clickListner.category(data.getCategoryId(), data.getCategoryName());
                } else {
                    AppController.getInstance().openSignInDialog(context);
                }
            });

            //music
            holder.tvMusic.setOnClickListener(view -> {
                openMusicActivity(data);
            });
            holder.tvArtist.setOnClickListener(view -> {
                openMusicActivity(data);
            });
            //record
            holder.ivMusicAnimation.setOnClickListener(view -> {
                openMusicActivity(data);
            });

            holder.ibLike.setOnClickListener(v -> {
                if (!AppController.getInstance().isGuest()) {
                    holder.ibLike.setSelected(!holder.ibLike.isSelected());
                    like(holder.tvLikeCount, position);
                } else {
                    AppController.getInstance().openSignInDialog(context);
                }
            });

            holder.tvFollow.setOnClickListener(v -> {
                if (!AppController.getInstance().isGuest()) {

                    //holder.ibFollow.setSelected(data.getFollowStatus() == 0);
                    holder.tvFollow.setVisibility(View.GONE);
                    if (data.isPrivateUser() != null) {
                        if (data.isPrivateUser() == 1) {
                            holder.ivFollowing.setImageDrawable(
                                ContextCompat.getDrawable(context, R.drawable.ic_homepage_pending));
                        } else {
                            holder.ivFollowing.setImageDrawable(ContextCompat.getDrawable(context,
                                R.drawable.ic_homepage_following));
                        }
                    } else {
                        //If key is not received in response,it is assumed to be a public user
                        holder.ivFollowing.setImageDrawable(
                            ContextCompat.getDrawable(context, R.drawable.ic_homepage_following));
                    }
                    holder.ivFollowing.setVisibility(View.VISIBLE);
                    follow(true, position);
                } else {
                    AppController.getInstance().openSignInDialog(context);
                }
            });

            holder.ivFollowing.setOnClickListener(v -> {
                if (!AppController.getInstance().isGuest()) {

                    //holder.ibFollow.setSelected(data.getFollowStatus() == 0);
                    holder.tvFollow.setVisibility(View.VISIBLE);
                    holder.ivFollowing.setVisibility(View.INVISIBLE);
                    follow(false, position);
                } else {
                    AppController.getInstance().openSignInDialog(context);
                }
            });

            holder.mPlayer.setOnClickListener(v -> {

                try {
                    if (data.getMediaType1() == 1) {
                        //Video post
                        if (holder.ivPlay.getVisibility() == View.VISIBLE) {
                            //if (data.isPaused()) {

                            holder.mPlayer.start();
                            holder.ivPlay.setVisibility(View.GONE);

                            //if (!holder.mPlayer.getVideoUrlDataSource()
                            //    .equals(Utilities.getModifiedVideoLink(data.getImageUrl1()))) {
                            //  videoItem.setActive(holder.itemView, position);
                            //  if (holder.mPlayer.getDuration() >= data.getSeekTime()) {
                            //    //For this bug
                            //
                            //    //https://github.com/Bilibili/ijkplayer/issues/4349
                            //    holder.mPlayer.seekToPosition(data.getSeekTime());
                            //  }
                            //} else {
                            //
                            //  holder.mPlayer.start();
                            //}
                            //updateVideoPauseStatus(false, position, data.getPostId(), -1);
                        } else {

                            holder.mPlayer.pause();
                            holder.ivPlay.setVisibility(View.VISIBLE);

                            //updateVideoPauseStatus(true, position, data.getPostId(),
                            //    holder.mPlayer.getPosition());
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            //share- more
            holder.ivShare.setOnClickListener(view -> {
                if (!AppController.getInstance().isGuest()) {

                    if ( data.getPurchased()) {
                        clickListner.showCustomShareSheet(data, position,
                            holder.mPlayer.getDuration());
                    } else {
                        Toast.makeText(context,
                            context.getString(R.string.unlock_post_for_custom_actions),
                            Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AppController.getInstance().openSignInDialog(context);
                }
            });

            //likers
            holder.tvLikeCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!AppController.getInstance().isGuest())
                        clickListner.openLikers(data);
                    else
                        AppController.getInstance().openSignInDialog(context);
                }
            });

            //viewer
            holder.tvViewCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!AppController.getInstance().isGuest())
                        clickListner.openViewers(data);
                    else
                        AppController.getInstance().openSignInDialog(context);
                }
            });

            holder.ivView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!AppController.getInstance().isGuest())
                        clickListner.openViewers(data);
                    else
                        AppController.getInstance().openSignInDialog(context);
                }
            });

            //menu
            holder.ibMenu.setOnClickListener(view -> {
                if (!AppController.getInstance().isGuest())
                    clickListner.openMenu(data, holder.ibMenu);
                else
                    AppController.getInstance().openSignInDialog(context);
            });

            //location
            holder.tvLocation.setOnClickListener(view -> {
                if (!AppController.getInstance().isGuest())
                    clickListner.location(data);
                else
                    AppController.getInstance().openSignInDialog(context);
            });

            //      heart animation
            holder.mCover.setOnTouchListener(new OnSwipeTouchListener(context) {
                public void onDoubleTaps() {

                    if (data.getMediaType1() == 0) {
                        if (!AppController.getInstance().isGuest()) {
                            if (!data.isLiked()) {
                                like(holder.tvLikeCount, position);
                            }
                            animatePhotoLike(holder);
                        } else {
                            AppController.getInstance().openSignInDialog(context);
                        }

                    }
                }

                @Override
                public void onSingleTap() {
                }
            });
//            holder.ibSaved.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!AppController.getInstance().isGuest()) {
//                        if (data.getBookMarked()) {
//                            holder.ibSaved.setImageDrawable(context.getDrawable(R.drawable.ic_unsaved));
//                            holder.rL_save.setVisibility(View.GONE);
//                        } else {
//                            holder.ibSaved.setImageDrawable(context.getDrawable(R.drawable.ic_saved));
//                            holder.rL_save.setVisibility(View.VISIBLE);
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    holder.rL_save.setVisibility(View.GONE);
//                                }
//                            }, 4000);
//                        }
//                        clickListner.savedClick(position, data.getBookMarked());
//                    } else {
//                        AppController.getInstance().openSignInDialog(context);
//                    }
//                }
//            });
//
//            holder.ibSaved.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (!AppController.getInstance().isGuest()) {
//                        if (data.getBookMarked()) {
//                            holder.rL_save.setVisibility(View.VISIBLE);
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    holder.rL_save.setVisibility(View.GONE);
//                                }
//                            }, 4000);
//                            clickListner.savedLongCick(position, data.getBookMarked());
//                            return true;
//                        } else {
//                            return false;
//                        }
//                    } else {
//                        AppController.getInstance().openSignInDialog(context);
//                        return false;
//                    }
//                }
//            });
//
//            holder.tV_savedView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!AppController.getInstance().isGuest()) {
//                        holder.rL_save.setVisibility(View.GONE);
//                        clickListner.savedViewClick(position, data);
//                    }
//                }
//            });
//
//            holder.tV_saveTo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!AppController.getInstance().isGuest()) {
//                        clickListner.onSaveToCollectionClick(position, data);
//                    }
//                }
//            });

            //holder.ivEcomShopping.setVisibility(data.getProductIds().size()> 0 ? View.VISIBLE : View.GONE);

            holder.ivEcomShopping.setOnClickListener(view -> {
                if(!AppController.getInstance().isGuest()) {
                    clickListner.callProductsApi(data.getProductIds());
                }else {
                    AppController.getInstance().openSignInDialog(context);
                }
            });
//mute video button
            holder.ivMute.setOnClickListener(view -> {
                holder.mPlayer.updateMuteState(!data.isVideoMuted());
                holder.ivMute.setSelected(!holder.ivMute.isSelected());

                String postId= data.getPostId();
                if (dataList.get(position).getPostId().equals(postId)) {
                    Data dataToUpdate = dataList.get(position);
                    dataToUpdate.setVideoMuted(!data.isVideoMuted());
                    dataList.set(position, dataToUpdate);
                } else {
                    for (int i = 0; i < dataList.size(); i++) {

                        if (dataList.get(i).getPostId().equals(postId)) {

                            Data dataToUpdate = dataList.get(i);
                            dataToUpdate.setVideoMuted(!data.isVideoMuted());
                            dataList.set(i, dataToUpdate);

                            break;
                        }
                    }
                }
               
            });

            if (position % Constants.AD_IMPRESS_POSITION == 0 && position != 0) {
                clickListner.showAds();
            }

            //            if (data.getTitle().length() > 20)
            //                makeTextViewResizable(holder.tvTitle, 3, "View More", true);
            if (!AppController.getInstance().isGuest()) {
                runnable = () -> {

                    clickListner.view(data);
                };
                handler.postDelayed(runnable, 3000);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateTextureViewScaling(View view, int viewWidth, int viewHeight) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.getLayoutParams();
        params.width = viewWidth;
        params.height = viewHeight;
        view.setLayoutParams(params);
    }

    private void like(TextView view, int position) {
        try {
            Data data = dataList.get(position);
            boolean alreadyLiked = data.isLiked();
            int likes = Integer.parseInt(data.getLikesCount());

            if (alreadyLiked) {
                likes--;
            } else {
                likes++;
            }
            data.setLiked(!alreadyLiked);
            data.setLikesCount(String.valueOf(likes));
            dataList.set(position, data);
            view.setText(String.valueOf(likes));
            clickListner.like(!alreadyLiked, data.getPostId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void follow(boolean follow, int position) {
        try {
            Data data = dataList.get(position);
            String userId = data.getUserId();
            //int followingStatus;
            //if (data.getFollowStatus() != 0) {
            //    followingStatus = 0;
            //    data.setFollowStatus(followingStatus);
            //    clickListner.follow(false, userId);
            //} else {
            //    followingStatus = 1;
            //    data.setFollowStatus(followingStatus);
            //    clickListner.follow(true, userId);
            //}

            data.setFollowStatus(follow ? 1 : 0);

            clickListner.follow(follow, userId);

            dataList.set(position, data);

            updateFollowStatusForPosts(userId, data.getPostId(), follow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return baseVideoItems.size();
    }

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public void updateCommentCount(String count, String postId, int position,
                                   RecyclerView recyclerView) {
        try {
            if (dataList.get(position).getPostId().equals(postId)) {
                Data data = dataList.get(position);
                data.setCommentCount(count);
                dataList.set(position, data);

                try {
                    ((ViewHolder) recyclerView.findViewHolderForAdapterPosition(
                            position)).tvCommentCount.setText(count);
                } catch (Exception e) {
                    notifyItemChanged(position);
                }
            } else {
                for (int i = 0; i < dataList.size(); i++) {

                    if (dataList.get(i).getPostId().equals(postId)) {

                        Data data = dataList.get(position);
                        data.setCommentCount(count);
                        dataList.set(position, data);
                        try {
                            ((ViewHolder) recyclerView.findViewHolderForAdapterPosition(
                                    position)).tvCommentCount.setText(count);
                        } catch (Exception e) {
                            notifyItemChanged(position);
                        }

                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine,
                                             final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1)
                            + " "
                            + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1)
                            + " "
                            + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv,
                        lineEndIndex, expandText, viewMore), TextView.BufferType.SPANNABLE);
            }
        });
    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned,
                                                                            final TextView tv, final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new TextSpannable(false) {
                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        makeTextViewResizable(tv, 3, "View More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);
        }
        return ssb;
    }

    private void updateFollowStatusForPosts(String userId, String postId,
        boolean follow) {//int followingStaus) {

        Data data;
        for (int i = 0; i < dataList.size(); i++) {
            data = dataList.get(i);
            if (data.getUserId().equals(userId)) {

                if (!data.getPostId().equals(postId)) {
                    data.setFollowStatus(follow ? 1 : 0);
                    //data.setFollowStatus(followingStaus);
                    dataList.set(i, data);
                }
            }
        }
    }

    // heart animation
    private void animatePhotoLike(ViewHolder holder) {
        holder.vBgLike.setVisibility(View.VISIBLE);
        holder.ivLikeIt.setVisibility(View.VISIBLE);

        holder.vBgLike.setScaleY(0.1f);
        holder.vBgLike.setScaleX(0.1f);
        holder.vBgLike.setAlpha(1f);
        holder.ivLikeIt.setScaleY(0.1f);
        holder.ivLikeIt.setScaleX(0.1f);

        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator bgScaleYAnim = ObjectAnimator.ofFloat(holder.vBgLike, "scaleY", 0.1f, 1f);
        bgScaleYAnim.setDuration(200);
        bgScaleYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
        ObjectAnimator bgScaleXAnim = ObjectAnimator.ofFloat(holder.vBgLike, "scaleX", 0.1f, 1f);
        bgScaleXAnim.setDuration(200);
        bgScaleXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
        ObjectAnimator bgAlphaAnim = ObjectAnimator.ofFloat(holder.vBgLike, "alpha", 1f, 0f);
        bgAlphaAnim.setDuration(200);
        bgAlphaAnim.setStartDelay(150);
        bgAlphaAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

        ObjectAnimator imgScaleUpYAnim = ObjectAnimator.ofFloat(holder.ivLikeIt, "scaleY", 0.1f, 1f);
        imgScaleUpYAnim.setDuration(300);
        imgScaleUpYAnim.setInterpolator(DECCELERATE_INTERPOLATOR);
        ObjectAnimator imgScaleUpXAnim = ObjectAnimator.ofFloat(holder.ivLikeIt, "scaleX", 0.1f, 1f);
        imgScaleUpXAnim.setDuration(300);
        imgScaleUpXAnim.setInterpolator(DECCELERATE_INTERPOLATOR);

        ObjectAnimator imgScaleDownYAnim = ObjectAnimator.ofFloat(holder.ivLikeIt, "scaleY", 1f, 0f);
        imgScaleDownYAnim.setDuration(300);
        imgScaleDownYAnim.setInterpolator(ACCELERATE_INTERPOLATOR);
        ObjectAnimator imgScaleDownXAnim = ObjectAnimator.ofFloat(holder.ivLikeIt, "scaleX", 1f, 0f);
        imgScaleDownXAnim.setDuration(300);
        imgScaleDownXAnim.setInterpolator(ACCELERATE_INTERPOLATOR);

        animatorSet.playTogether(bgScaleYAnim, bgScaleXAnim, bgAlphaAnim, imgScaleUpYAnim,
                imgScaleUpXAnim);
        animatorSet.play(imgScaleDownYAnim).with(imgScaleDownXAnim).after(imgScaleUpYAnim);

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                resetLikeAnimationState(holder);
            }
        });
        animatorSet.start();
        holder.ibLike.setSelected(true);
    }

    private void resetLikeAnimationState(ViewHolder holder) {
        holder.vBgLike.setVisibility(View.INVISIBLE);
        holder.ivLikeIt.setVisibility(View.INVISIBLE);
    }



    //private void updateVideoPauseStatus(boolean pause, int position, String postId, long seekTime) {
    //  Data data = dataList.get(position);
    //  if (data.getPostId().equals(postId)) {
    //    data.setPaused(pause);
    //    data.setSeekTime(seekTime);
    //    dataList.set(position, data);
    //    //notifyItemChanged(position);
    //  } else {
    //    for (int i = 0; i < dataList.size(); i++) {
    //      data = dataList.get(i);
    //      if (data.getPostId().equals(postId)) {
    //
    //        data.setPaused(pause);
    //        data.setSeekTime(seekTime);
    //        dataList.set(i, data);
    //        //notifyItemChanged(i);
    //        break;
    //      }
    //    }
    //  }
    //}
    private void openMusicActivity(Data data) {
        if (AppController.getInstance().isGuest()) {
            AppController.getInstance().openSignInDialog(context);
        } else {
            clickListner.music(data.getDub().getId(), data.getDub().getName(),
                data.getDub().getPath(), data.getDub().getArtist());
        }
    }
}
