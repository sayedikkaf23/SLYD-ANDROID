package chat.hola.com.app.profileScreen.channel;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.profileScreen.channel.Model.ChannelData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.util.ArrayList;

/**
 * <h>ChannelAdapter</h>
 *
 * @author 3Embed.
 * @since 17/2/18.
 */

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.ViewHolder> {

  private static final String TAG = ChannelAdapter.class.getSimpleName();

  private Context context;
  private TypefaceManager typefaceManager;
  ArrayList<ChannelData> channelDataList = new ArrayList<>();
  private ChannelAdapterCallback callback = null;

  public interface ChannelAdapterCallback {
    void onChannelClick();
  }

  public void setCallback(ChannelAdapterCallback callback) {
    this.callback = callback;
  }

  public ChannelAdapter(Context context, TypefaceManager typefaceManager) {
    this.context = context;
    this.typefaceManager = typefaceManager;
  }

  public void setData(ArrayList<ChannelData> channelDataList) {
    this.channelDataList = channelDataList;
    notifyDataSetChanged();
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view =
        LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_row, parent, false);
    return new ChannelAdapter.ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {

    ChannelData channelData = channelDataList.get(position);

    String channelPic = channelData.getChannelImageUrl();
    if (channelPic != null) {
      Glide.with(context).load(channelPic).asBitmap().centerCrop()
          //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
          .signature(new StringSignature(
              AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
          .into(holder.ivItemTopImage);
    }
    String channelName = channelData.getName();
    holder.tvItemTitle.setText((channelName == null) ? "Title" : channelName);
    holder.tvItemSubscribe.setText("");
    //initChildRecycler(holder, channelData.getData());
  }
  //
  //    private void initChildRecycler(ChannelAdapter.ViewHolder holder, ArrayList<Data> postList) {
  //        LinearLayoutManager llm = new LinearLayoutManager(context);
  //        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
  //        holder.recyclerChild.setLayoutManager(llm);
  //        holder.recyclerChild.setHasFixedSize(true);
  //        holder.recyclerChild.setNestedScrollingEnabled(false);
  //        ChannelChildAdapter channelChildAdapter = new ChannelChildAdapter(context);
  //        holder.recyclerChild.setAdapter(channelChildAdapter);
  //        channelChildAdapter.setData(postList);
  //        if (callback != null) {
  //            callback.onChildAdapterSetup(channelChildAdapter);
  //        } else {
  //            Log.w(TAG, "ChannelAdapterCallback can not be null!!");
  //        }
  //
  //    }

  @Override
  public int getItemCount() {
    return channelDataList.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView ivItemTopImage;
    TextView tvItemTitle;
    TextView tvItemSubscribe;
    RecyclerView recyclerChild;

    public ViewHolder(View itemView) {
      super(itemView);
      ivItemTopImage = (ImageView) itemView.findViewById(R.id.ivItemTopImage);
      tvItemTitle = (TextView) itemView.findViewById(R.id.tvItemTitle);
      tvItemSubscribe = (TextView) itemView.findViewById(R.id.tvItemSubscribe);
      recyclerChild = (RecyclerView) itemView.findViewById(R.id.recyclerContentChild);
      tvItemTitle.setTypeface(typefaceManager.getMediumFont());
      tvItemSubscribe.setTypeface(typefaceManager.getMediumFont());
    }

    @Override
    public void onClick(View v) {
      if (callback != null) {
        callback.onChannelClick();
      } else {
        Log.w(TAG, "ChannelAdapterCallback can not be null!!");
      }
    }
  }
}
