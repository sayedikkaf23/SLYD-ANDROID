package chat.hola.com.app.home.activity.youTab.followrequest;

import android.content.Context;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import chat.hola.com.app.AppController;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h1>ChannelRequesterAdapter</h1>
 *
 * @author 3embed
 * @since 4/9/2018
 */

public class FollowRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ReuestData> list;
    private TypefaceManager typefaceManager;
    private ItemClickListner clickListner;
    private Context context;

    public void setListener(ItemClickListner clickListner) {
        this.clickListner = clickListner;
    }

    @Inject
    public FollowRequestAdapter(Context context, List<ReuestData> list, TypefaceManager typefaceManager) {
        this.context = context;
        this.list = list;
        this.typefaceManager = typefaceManager;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder2(LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_request_item, parent, false));
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder2 holder2 = (ViewHolder2) holder;
        Glide.with(context).load(list.get(position).getProfilePic()).asBitmap().signature(new StringSignature(
            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime())).centerCrop().into(new BitmapImageViewTarget(holder2.ivProfilePic) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder2.ivProfilePic.setImageDrawable(circularBitmapDrawable);
            }
        });
        holder2.tvUserName.setText(list.get(position).getUserName());
        /*Bug Title:random number displayed with user name
         * Bug Desc:random no caused due to space id
         * Developer name :Ankit K Tiwary
         * Fixed Date:14-April-2021*/
        holder2.tvTime.setText(list.get(position).getFirstName() + context.getString(R.string.space) + list.get(position).getLastName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder2 extends RecyclerView.ViewHolder {
        @BindView(R.id.ivProfilePic)
        ImageView ivProfilePic;
        @BindView(R.id.tvUserName)
        TextView tvUserName;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.btnAccept)
        Button btnAccept;
        @BindView(R.id.btnReject)
        Button btnReject;

        public ViewHolder2(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tvTime.setTypeface(typefaceManager.getRegularFont());
            tvUserName.setTypeface(typefaceManager.getSemiboldFont());
            btnAccept.setTypeface(typefaceManager.getSemiboldFont());
            btnReject.setTypeface(typefaceManager.getSemiboldFont());
        }

        @OnClick(R.id.ivProfilePic)
        public void profile() {
            clickListner.onUserClicked(getAdapterPosition());
        }

        @OnClick(R.id.btnAccept)
        public void accept() {
            clickListner.onRequestAction(getAdapterPosition(), 1);
        }

        @OnClick(R.id.btnReject)
        public void reject() {
            clickListner.onRequestAction(getAdapterPosition(), 0);
        }
    }
}