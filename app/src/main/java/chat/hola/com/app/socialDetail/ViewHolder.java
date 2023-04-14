package chat.hola.com.app.socialDetail;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.PostTitleCustomTextView;
import chat.hola.com.app.Utilities.TypefaceManager;
import com.ezcall.android.R;
import com.volokh.danylo.video_player_manager.ui.VideoPlayerView;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 11/27/2018.
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.llActions)
    public LinearLayout llActions;

    @BindView(R.id.player)
    public VideoPlayerView mPlayer;
    @BindView(R.id.cover)
    public ImageView mCover;
    @BindView(R.id.tvUserName)
    public TextView tvUserName;
    @BindView(R.id.postTitleTv)
    public PostTitleCustomTextView postTitleTv;

    @BindView(R.id.tvLocation)
    public TextView tvLocation;
    @BindView(R.id.tvCategory)
    public TextView tvCategory;
    @BindView(R.id.tvChannel)
    public TextView tvChannel;
    @BindView(R.id.ivProfilePic)
    public ImageView ivProfilePic;
    @BindView(R.id.ibLike)
    public ImageView ibLike;
    @BindView(R.id.ivComment)
    public ImageView ivComment;
//    @BindView(R.id.ivShare)
//    public ImageView ivShare;
    @BindView(R.id.ivView)
    public ImageView ivView;
    @BindView(R.id.tvLikeCount)
    public TextView tvLikeCount;
    @BindView(R.id.tvCommentCount)
    public TextView tvCommentCount;
    @BindView(R.id.tvViewCount)
    public TextView tvViewCount;
    @BindView(R.id.flMediaContainer)
    public RelativeLayout flMediaContainer;
    @BindView(R.id.vBgLike)
    public View vBgLike;
    @BindView(R.id.ivLikeIt)
    public ImageView ivLikeIt;
    @BindView(R.id.ivStarBadge)
    public ImageView ivStarBadge;
    @BindView(R.id.tvMusic)
    public TextView tvMusic;
    @BindView(R.id.tvArtist)
    public TextView tvArtist;

    @BindView(R.id.llMusic)
    public RelativeLayout rlMusic;
    @BindView(R.id.tvDivide)
    public TextView tvDivide;
    //@BindView(R.id.ibFollow)
    //public ToggleButton ibFollow;
    @BindView(R.id.ibMenu)
    public ImageButton ibMenu;
    @BindView(R.id.ivShare)
    public AppCompatImageView ivShare;
    @BindView(R.id.llFabContainer)
    public LinearLayout llFabContainer;

    @BindView(R.id.rlAction)
    public RelativeLayout rlAction;
    @BindView(R.id.tvActionText)
    public TextView tvActionButton;
    @BindView(R.id.tvPrice)
    public TextView tvPrice;

    //    @BindView(R.id.rL_save)
    //    public RelativeLayout rL_save;
    //    @BindView(R.id.tV_saveTo)
    //    public TextView tV_saveTo;
    //    @BindView(R.id.tV_savedView)
    //    public TextView tV_savedView;
    //    @BindView(R.id.ibSaved)
    //    public ImageView ibSaved;
    @BindView(R.id.ivRecord)
    public AppCompatImageView ivRecord;
    @BindView(R.id.ivMusicAnimation)
    public AppCompatImageView ivMusicAnimation;

    @BindView(R.id.ivPlay)
    public AppCompatImageView ivPlay;
    @BindView(R.id.ivEcomShopping)
    public AppCompatImageView ivEcomShopping;

    //    @BindView(R.id.fl_music_one)
    //    public FrameLayout flMusicOne;
    //    @BindView(R.id.fl_music_two)
    //    public FrameLayout flMusicTwo;
    //    @BindView(R.id.fl_music_three)
    //    public FrameLayout flMusicThree;
    //    @BindView(R.id.clMusic)
    //    public CoordinatorLayout clMusic;

    @BindView(R.id.ivMute)
    public AppCompatImageView ivMute;
    @BindView(R.id.ivForward)
    public AppCompatImageView ivForward;
    @BindView(R.id.ivFollowing)
    public AppCompatImageView ivFollowing;
    @BindView(R.id.tvFollow)
    public AppCompatTextView tvFollow;
    @BindView(R.id.rlSendTip)
    RelativeLayout rlSendTip;
    //@BindView(R.id.rlBuyNow)
    //RelativeLayout rlBuyNow;

    @BindView(R.id.rlPaidPostOverlay)
    RelativeLayout rlPaidPostOverlay;

    @BindView(R.id.tvExclusivePost)
    AppCompatTextView tvExclusivePost;

    @BindView(R.id.llUnlock)
    LinearLayout llUnlock;
    @BindView(R.id.tvUnlockCoins)
    AppCompatTextView tvUnlockCoins;
    @BindView(R.id.tvUnlockPlaceHolder)
    AppCompatTextView tvUnlockPlaceHolder;

    @BindView(R.id.llSubscribe)
    LinearLayout llSubscribe;
    @BindView(R.id.tvSubscribePlaceHolder)
    TextView tvSubscribePlaceHolder;
    @BindView(R.id.tvSubscribeCoins)
    TextView tvSubscribeCoins;

    @BindView(R.id.tvSendTip)
    TextView tvSendTip;
    @BindView(R.id.tvTipCollect)
    TextView tvTipCollect;

    //@BindView(R.id.tvBuyNow)
    //TextView tvBuyNow;

    public ViewHolder(View view, TypefaceManager typefaceManager) {
        super(view);
        ButterKnife.bind(this, view);

        if (typefaceManager == null) {
            typefaceManager = new TypefaceManager(AppController.getInstance());
        }

        tvLikeCount.setTypeface(typefaceManager.getRobotoCondensedBold());
        tvViewCount.setTypeface(typefaceManager.getRobotoCondensedBold());
        tvCommentCount.setTypeface(typefaceManager.getRobotoCondensedBold());

        tvUserName.setTypeface(typefaceManager.getRobotoCondensedBold());
        tvLocation.setTypeface(typefaceManager.getRobotoCondensedBold());
        tvFollow.setTypeface(typefaceManager.getRobotoCondensedBold());

        tvChannel.setTypeface(typefaceManager.getRobotoCondensedBold());
        tvCategory.setTypeface(typefaceManager.getRobotoCondensedBold());

        tvMusic.setTypeface(typefaceManager.getRobotoCondensedBold(), Typeface.ITALIC);
        tvArtist.setTypeface(typefaceManager.getRobotoCondensed(), Typeface.ITALIC);

        tvExclusivePost.setTypeface(typefaceManager.getRobotoCondensedBold());
        tvUnlockCoins.setTypeface(typefaceManager.getRobotoCondensedBold());
        tvUnlockPlaceHolder.setTypeface(typefaceManager.getRobotoCondensedBold());

        tvSubscribePlaceHolder.setTypeface(typefaceManager.getRobotoCondensedBold());
        tvSubscribeCoins.setTypeface(typefaceManager.getRobotoCondensedBold());

        tvSendTip.setTypeface(typefaceManager.getRobotoCondensedBold());

        tvActionButton.setTypeface(typefaceManager.getRobotoCondensedBold());
        tvPrice.setTypeface(typefaceManager.getRobotoCondensedBold());
    }
}

