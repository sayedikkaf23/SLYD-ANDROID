package chat.hola.com.app.home.model;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.Utilities.Utilities;
import chat.hola.com.app.home.trending.TrendingFragment;
import chat.hola.com.app.profileScreen.addChannel.AddChannelActivity;
import chat.hola.com.app.profileScreen.channel.Model.ChannelData;

/**
 * <h>ContentAdapter.class</h>
 * <p>
 * This Adapter is used by {@link TrendingFragment} contentRecyclerView.
 *
 * @author 3Embed
 * @since 13/2/18.
 */

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.ViewHolder> {

    private TypefaceManager typefaceManager;
    private Context context;
    ArrayList<ChannelData> channelDataList = new ArrayList<>();
    private AdapterClickCallback clickCallback;
    private Integer POS_TAG = 0;

    public interface AdapterClickCallback {

        void onChannelDetail(int pos);

        void onPostDetail(Data data, int type, View view);

        void onDeleteChannel(String channelId);
    }

    public void setCallback(AdapterClickCallback callback) {
        this.clickCallback = callback;
    }

    @Inject
    public ContentAdapter(Context context, TypefaceManager typefaceManager) {
        this.context = context;
        this.typefaceManager = typefaceManager;
    }

    public void setData(ArrayList<ChannelData> channelDataList, boolean clear) {
        if(clear)this.channelDataList.clear();
        this.channelDataList.addAll(channelDataList);
    }

    @Override
    public ContentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_content_item, parent, false);
        return new ContentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ContentAdapter.ViewHolder holder, int position) {
        try {
            ChannelData channelData = channelDataList.get(position);
            String channelPic = channelData.getChannelImageUrl();
            if (channelPic != null) {
                Glide.with(context)
                        .load(channelPic)
                        .asBitmap()
                        .placeholder(R.drawable.ic_default)
                        .signature(new StringSignature(
                                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.ivItemTopImage);
            }
            String channelName = channelData.getChannelName();
            holder.tvItemTopTitle.setText((channelName == null) ? "Title" : channelName);
            holder.tvSubscribe.setText(R.string.double_inverted_comma);

            holder.ivDelete.setVisibility(channelData.getUserId().equals(AppController.getInstance().getUserId()) ? View.VISIBLE : View.GONE);
            holder.ivArrow.setImageDrawable(channelData.getUserId().equals(AppController.getInstance().getUserId()) ? context.getResources().getDrawable(R.drawable.ic_edit) : context.getResources().getDrawable(R.drawable.ic_next_arrow_icon));

            holder.ivPrivacy.setImageDrawable(channelData.getPrivate()
                    ? context.getResources().getDrawable(R.drawable.ic_lock)
                    :context.getResources().getDrawable(R.drawable.ic_public));

            if (channelData.getPrivate() && !channelData.getUserId().equals(AppController.getInstance().getUserId())) {
                holder.rlData.setVisibility(View.GONE);
                holder.tvViewMore.setVisibility(View.GONE);
                //holder.ivPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_lock));
            } else {
                holder.rlData.setVisibility(View.VISIBLE);
                holder.tvViewMore.setVisibility(View.VISIBLE);
                //holder.ivPrivacy.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_public));
                List<Data> dataList = channelData.getData();
                if (!dataList.isEmpty()) {
                    if (dataList.size() >= 1) {
                        loadImage(holder, dataList.get(0).getMediaType1(), holder.ibPlayOne, Utilities.getModifiedImageLink(dataList.get(0).getImageUrl1()), holder.ivImageOne);
                        //holder.ibPlay.setTag(0);
                    }

                    if (dataList.size() >= 2) {
                        loadImage(holder, dataList.get(1).getMediaType1(), holder.ibPlayTwo, Utilities.getModifiedImageLink(dataList.get(1).getImageUrl1()), holder.ivImageTwo);
                        //holder.ibPlay.setTag(1);
                    }

                    if (dataList.size() >= 3) {
                        loadImage(holder, dataList.get(2).getMediaType1(), holder.ibPlayThree, Utilities.getModifiedImageLink(dataList.get(2).getImageUrl1()), holder.ivImageThree);
                        //holder.ibPlay.setTag(2);
                    }

//                    if (dataList.size() >= 4) {
//                        loadImage(holder, dataList.get(3).getMediaType1(), holder.ibPlayFour, Utilities.getModifiedImageLink(dataList.get(3).getImageUrl1()), holder.ivImageFour);
//                        // holder.ibPlay.setTag(3);
//                    }

//                    if (dataList.size() > 4) {
//                        holder.viewImageFilter.setVisibility(View.VISIBLE);
//                        holder.tvMoreImage.setVisibility(View.VISIBLE);
//                        holder.tvMoreImage.setText("+" + (dataList.size() - 4));
//                    } else {
//                        holder.viewImageFilter.setVisibility(View.GONE);
//                        holder.tvMoreImage.setVisibility(View.GONE);
//                    }
                }
            }
            if(channelData.getData()==null || channelData.getData().isEmpty()){
                holder.rlData.setVisibility(View.GONE);
                holder.tvViewMore.setVisibility(View.GONE);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    private void loadImage(ContentAdapter.ViewHolder holder, int type, ImageButton ibPlay, String mediaUrl, ImageView ivImageView) {

        if (type == 0) {
            //image media
            ibPlay.setVisibility(View.GONE);
        } else {
            //video media
            ibPlay.setVisibility(View.VISIBLE);
        }


        Glide.with(context)
                .load(mediaUrl)
                .asBitmap()
                .signature(new StringSignature(
                        AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivImageView);
    }

    @Override
    public int getItemCount() {
        return channelDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ivArrow)
        ImageView ivArrow;
        @BindView(R.id.ivDelete)
        ImageView ivDelete;
        @BindView(R.id.ivPrivacy)
        ImageView ivPrivacy;
        @BindView(R.id.ivItemTopImage)
        ImageView ivItemTopImage;
        @BindView(R.id.ibPlayOne)
        ImageButton ibPlayOne;
        @BindView(R.id.ibPlayTwo)
        ImageButton ibPlayTwo;
        @BindView(R.id.ibPlayThree)
        ImageButton ibPlayThree;
        //        @BindView(R.id.ibPlayFour)
//        ImageButton ibPlayFour;
        @BindView(R.id.rlData)
        RelativeLayout rlData;
        @BindView(R.id.ivImageOne)
        ImageView ivImageOne;
        @BindView(R.id.ivImageTwo)
        ImageView ivImageTwo;
        @BindView(R.id.ivImageThree)
        ImageView ivImageThree;
        //        @BindView(R.id.ivImageFour)
//        ImageView ivImageFour;
//        @BindView(R.id.viewImgFilter)
//        View viewImageFilter;
        @BindView(R.id.tvItemTitle)
        TextView tvItemTopTitle;
        @BindView(R.id.llRowHeader)
        RelativeLayout llRowHeader;
        @BindView(R.id.llImageContainer)
        LinearLayout llImageContainer;
        @BindView(R.id.tvViewMore)
        TextView tvViewMore;
        @BindView(R.id.tvItemSubscribe)
        TextView tvSubscribe;
        @BindView(R.id.container)
        LinearLayout container;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
//            int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
//            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
//            container.setBackgroundColor(randomAndroidColor);


            tvItemTopTitle.setTypeface(typefaceManager.getSemiboldFont());
            tvSubscribe.setTypeface(typefaceManager.getMediumFont());
            tvViewMore.setTypeface(typefaceManager.getSemiboldFont());

            ivImageOne.setOnClickListener(this);
            ivImageTwo.setOnClickListener(this);
            ivImageThree.setOnClickListener(this);
            tvViewMore.setOnClickListener(this);
            ivArrow.setOnClickListener(this);
            ivDelete.setOnClickListener(this);
            tvViewMore.setOnClickListener(this);
            llRowHeader.setOnClickListener(this);
            ibPlayOne.setOnClickListener(this);
            ibPlayTwo.setOnClickListener(this);
            ibPlayThree.setOnClickListener(this);
//            ibPlayFour.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            try {
                if (channelDataList.size() > 0) {
                    List<Data> dataList = channelDataList.get(getAdapterPosition()).getData();
                    Data data;
                    String channelId;
                    switch (v.getId()) {
                        case R.id.ivDelete:
                            channelId = channelDataList.get(getAdapterPosition()).getId();
                            if (clickCallback != null)
                                clickCallback.onDeleteChannel(channelId);
                            break;
                        case R.id.ivArrow:
                            boolean isMine = (channelDataList.get(getAdapterPosition()).getUserId()).equals(AppController.getInstance().getUserId());
                            if (isMine) {
                                Intent intent = new Intent(context, AddChannelActivity.class);
                                intent.putExtra("isEdit", true);
                                intent.putExtra("data", channelDataList.get(getAdapterPosition()));
                                context.startActivity(intent);
                            } else {
                                channelId = channelDataList.get(getAdapterPosition()).getId();
                                if (channelId != null)
                                    if (clickCallback != null)
                                        clickCallback.onChannelDetail(getAdapterPosition());
                            }
                            break;
                        case R.id.tvViewMore:
                            //detail page.
                            channelId = channelDataList.get(getAdapterPosition()).getId();
                            if (channelId != null)
                                if (clickCallback != null)
                                    clickCallback.onChannelDetail(getAdapterPosition());
                            break;
                        case R.id.llRowHeader:
                            isMine = (channelDataList.get(getAdapterPosition()).getUserId()).equals(AppController.getInstance().getUserId());
                            if (isMine) {
                                Intent intent = new Intent(context, AddChannelActivity.class);
                                intent.putExtra("isEdit", true);
                                intent.putExtra("data", channelDataList.get(getAdapterPosition()));
                                context.startActivity(intent);
                            } else {
                                channelId = channelDataList.get(getAdapterPosition()).getId();
                                if (channelId != null)
                                    if (clickCallback != null)
                                        clickCallback.onChannelDetail(getAdapterPosition());
                            }
                            //detail page.
//                            channelId = channelDataList.get(getAdapterPosition()).getId();
//                            if (channelId != null)
//                                if (clickCallback != null)
//                                    clickCallback.onChannelDetail(getAdapterPosition());
                            break;
                        case R.id.ivImageOne:
                            if (dataList.size() < 1)
                                break;
                            data = dataList.get(0);
                            if (clickCallback != null)
                                clickCallback.onPostDetail(data, data.getMediaType1(), v);
                            break;
                        case R.id.ivImageTwo:
                            if (dataList.size() < 2)
                                break;
                            data = dataList.get(1);
                            if (clickCallback != null)
                                clickCallback.onPostDetail(data, data.getMediaType1(), v);
                            break;
                        case R.id.ivImageThree:
                            if (dataList.size() < 3)
                                break;
                            data = dataList.get(2);
                            if (clickCallback != null)
                                clickCallback.onPostDetail(data, data.getMediaType1(), v);
                            break;
//                        case R.id.ivImageFour:
//                            if (dataList.size() < 4)
//                                break;
//                            data = dataList.get(3);
//                            if (clickCallback != null)
//                                clickCallback.onPostDetail(data, data.getMediaType1(), v);
//                            break;
                        case R.id.ibPlayOne:
                            if (dataList.size() < 1)
                                break;
                            data = dataList.get(0);
                            if (clickCallback != null)
                                clickCallback.onPostDetail(data, data.getMediaType1(), v);
                            break;
                        case R.id.ibPlayTwo:
                            if (dataList.size() < 2)
                                break;
                            data = dataList.get(1);
                            if (clickCallback != null)
                                clickCallback.onPostDetail(data, data.getMediaType1(), v);
                            break;
                        case R.id.ibPlayThree:
                            if (dataList.size() < 3)
                                break;
                            data = dataList.get(2);
                            if (clickCallback != null)
                                clickCallback.onPostDetail(data, data.getMediaType1(), v);
                            break;
//                        case R.id.ibPlayFour:
//                            if (dataList.size() < 4)
//                                break;
//                            data = dataList.get(3);
//                            if (clickCallback != null)
//                                clickCallback.onPostDetail(data, data.getMediaType1(), v);
//                            break;
                        default:
                    }
                }
            } catch (ArrayIndexOutOfBoundsException ignored) {
            }
        }
    }
}