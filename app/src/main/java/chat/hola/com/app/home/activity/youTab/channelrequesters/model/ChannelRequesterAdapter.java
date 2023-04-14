package chat.hola.com.app.home.activity.youTab.channelrequesters.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.Constants;
import chat.hola.com.app.Utilities.TypefaceManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.List;
import javax.inject.Inject;

/**
 * <h1>ChannelRequesterAdapter</h1>
 *
 * @author 3embed
 * @since 4/9/2018
 */

public class ChannelRequesterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  private List<DataList> list;
  private TypefaceManager typefaceManager;
  private ClickListner clickListner;
  private Context context;

  @Override
  public int getItemViewType(int position) {
    return list.get(position).isParent() ? 0 : 1;
  }

  public void setListener(ClickListner clickListner) {
    this.clickListner = clickListner;
  }

  @Inject
  public ChannelRequesterAdapter(Context context, List<DataList> list,
      TypefaceManager typefaceManager) {
    this.context = context;
    this.list = list;
    this.typefaceManager = typefaceManager;
  }

  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder2(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.channel_request_item, parent, false));
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
    ViewHolder2 holder2 = (ViewHolder2) holder;
    Glide.with(context)
        .load(list.get(position).getProfilePic().replace("upload/", Constants.PROFILE_PIC_SHAPE))
        .asBitmap()
        .placeholder(context.getResources().getDrawable(R.drawable.profile_one))
        //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
        .signature(new StringSignature(
            AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
        .into(holder2.ivProfilePic);
    holder2.tvUserName.setText(list.get(position).getUserName());
    holder2.tvTime.setText(list.get(position).getChannelName());
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
      clickListner.onRequestAction(getAdapterPosition(), true);
    }

    @OnClick(R.id.btnReject)
    public void reject() {
      clickListner.onRequestAction(getAdapterPosition(), false);
    }
  }
}
