package chat.hola.com.app.home.activity.youTab;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import chat.hola.com.app.AppController;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TagSpannable;
import chat.hola.com.app.Utilities.TimeAgo;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.activity.youTab.model.Data;
import chat.hola.com.app.home.model.PostData;

/**
 * <h>YouAdapter.class</h>
 * <p> This adapter class is used by {@link YouFrag}.</p>
 *
 * @author 3Embed
 * @since 14/2/18.
 */

public class YouAdapter extends RecyclerView.Adapter<YouAdapter.ViewHolder> {

    private Context context;
    private TypefaceManager typefaceManager;
    private ArrayList<Data> arrayList = new ArrayList<>();
    private OnAdapterClickCallback clickCallback = null;

    public interface OnAdapterClickCallback {
        void onProfilePicClick(String userId);

        void onPostClickCallback(PostData postData, View v);

        void onFollow(String userId);

        void onUnfollow(String userId);
    }

    public void setClickCallback(OnAdapterClickCallback clickCallback) {
        this.clickCallback = clickCallback;
    }

    @Inject
    public YouAdapter(Context context, TypefaceManager typefaceManager) {
        this.context = context;
        this.typefaceManager = typefaceManager;
    }

    public void setData(ArrayList<Data> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    @Override
    public YouAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.you_row, parent, false);
        return new YouAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(YouAdapter.ViewHolder holder, int position) {

        Data data = arrayList.get(position);

        if(data.getProfilePic()!=null || !data.getProfilePic().isEmpty()) {
            Glide.with(context).load(data.getProfilePic())
                    .asBitmap()
                    .signature(new StringSignature(AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                    .centerCrop()
                    .placeholder(R.drawable.chat_attachment_profile_default_image_frame)
                    .into(new BitmapImageViewTarget(holder.ivProfile) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            holder.ivProfile.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        }else{
            Utilities.setTextRoundDrawable(context, data.getFirstName(),data.getLastName(), holder.ivProfile);
        }


        String message = "";
        if (data.getType() == 2 || data.getType() == 5 || data.getType()==7 || data.getType()==9) {
            //type = 2 =liked
            //type = 5 =commented
            //type = 7 = send tip
            //type = 9 = buy post

            message = data.getFirstName() + context.getString(R.string.space) + data.getMessage();

            holder.tbFollow.setVisibility(View.GONE);
            holder.flMedia.setVisibility(View.VISIBLE);
            holder.ibPlay.setVisibility(data.getPostData().getMediaType1() != null && data.getPostData().getMediaType1() == 1 ? View.VISIBLE : View.GONE);

            Glide.with(context).load(data.getPostData().getImageUrl1() != null ? Utilities.getModifiedImageLink(data.getPostData().getImageUrl1()) : "").asBitmap().centerCrop().placeholder(R.drawable.profile_one).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.ivMedia);
        } else if (data.getType() == 3 || data.getType()==8 ) {
            //type = 3 =follow
            //type = 8 =subscribe user


            message = data.getFirstName() + context.getString(R.string.space) + data.getMessage();

            holder.flMedia.setVisibility(View.GONE);
            holder.ibPlay.setVisibility(View.GONE);
            holder.tbFollow.setVisibility(View.VISIBLE);
            holder.tbFollow.setChecked(data.getAmIFollowing());
        } else if(data.getType()==10){
            //type = 10 = sent payment
            message =  data.getMessage();

            holder.flMedia.setVisibility(View.GONE);
            holder.ibPlay.setVisibility(View.GONE);
            holder.tbFollow.setVisibility(View.GONE);
            holder.tbFollow.setChecked(data.getAmIFollowing());
        }

        SpannableString spanString = new SpannableString(message);
        Matcher userMatcher = Pattern.compile("@([A-Za-z0-9_-]+)").matcher(spanString);
        findMatch(spanString, userMatcher);
        holder.tvMessage.setText(spanString);
        holder.tvMessage.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tvRowTime.setText(TimeAgo.getTimeAgo(Long.parseLong(data.getTimeStamp())));

        holder.ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = data.getUserId();
                clickCallback.onProfilePicClick(userId);
            }
        });
    }

    private void findMatch(SpannableString spanString, Matcher matcher) {
        while (matcher.find()) {
            final String tag = matcher.group(0);
            spanString.setSpan(new TagSpannable(context, tag), matcher.start(), matcher.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.flMedia)
        FrameLayout flMedia;
        @BindView(R.id.ibPlay)
        ImageButton ibPlay;
        @BindView(R.id.ivMedia)
        ImageView ivMedia;
        @BindView(R.id.ivProfilePic)
        ImageView ivProfile;
        @BindView(R.id.tvMessage)
        TextView tvMessage;
        @BindView(R.id.tvRowTime)
        TextView tvRowTime;
        @BindView(R.id.tbFollow)
        ToggleButton tbFollow;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvMessage.setTypeface(typefaceManager.getMediumFont());
            tvRowTime.setTypeface(typefaceManager.getMediumFont());
            tbFollow.setOnClickListener(this);
            flMedia.setOnClickListener(this);
            ivProfile.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tbFollow:
                    if (tbFollow.isChecked()) {
                        //follow
                        String userId = arrayList.get(getAdapterPosition()).getUserId();
                        if (userId != null)
                            if (clickCallback != null)
                                clickCallback.onFollow(userId);
                    } else {
                        //unfollow
                        String userId = arrayList.get(getAdapterPosition()).getUserId();
                        if (userId != null)
                            if (clickCallback != null)
                                clickCallback.onUnfollow(userId);
                    }
                    break;

                case R.id.flMedia:
                    if (clickCallback != null) {
                        PostData postData = arrayList.get(getAdapterPosition()).getPostData();
                        if (postData != null) {
                            clickCallback.onPostClickCallback(postData, v);
                        }
                    }
                    break;
                default:
            }
        }
    }

}
