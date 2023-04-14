package chat.hola.com.app.live_stream.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;

import chat.hola.com.app.live_stream.ResponcePojo.Gifts;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 09 August 2019
 */
public class GiftGridAdapter extends RecyclerView.Adapter<GiftGridAdapter.ViewHolderGifts> {
  private ArrayList<Gifts> gifts;
  private Context mContext;
  private ItemSelctListner itemSelctListner;

  public GiftGridAdapter(ArrayList<Gifts> gifts, Context mContext,
                         ItemSelctListner itemSelctListner) {
    this.gifts = gifts;
    this.mContext = mContext;
    this.itemSelctListner = itemSelctListner;
  }

  //    gifts_item
  @NonNull
  @Override
  public ViewHolderGifts onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View v = LayoutInflater.from(mContext).inflate(R.layout.gifts_item, viewGroup, false);
    return new ViewHolderGifts(v);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolderGifts viewHolder, int position) {

    Gifts gift = gifts.get(position);

    viewHolder.tvName.setText(gift.getName());
    viewHolder.tvCoin.setText(gift.getCoin());//+ " " + mContext.getString(R.string.coins));
    try {

      try {
        GlideDrawableImageViewTarget imageViewTarget =
            new GlideDrawableImageViewTarget(viewHolder.ivGift);
        DrawableRequestBuilder<String> thumbnailRequest = Glide.with(mContext)
            .load(gift.getImage())
            .diskCacheStrategy(DiskCacheStrategy.SOURCE)
            .fitCenter();

        Glide.with(mContext)
            .load(gift.getImage())
            .thumbnail(thumbnailRequest)
            .dontAnimate()
            .fitCenter()
            .placeholder(ContextCompat.getDrawable(mContext, R.drawable.ic_default))
            //.signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
            .into(imageViewTarget);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            itemSelctListner.onGiftItemSelect(gift);
          }
        });
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (NullPointerException e) {
        e.printStackTrace();
      }
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  @Override
  public int getItemCount() {
    return gifts.size();
  }

  public class ViewHolderGifts extends RecyclerView.ViewHolder {
    private ImageView ivGift;
    private TextView tvName, tvCoin;

    private ViewHolderGifts(@NonNull View itemView) {
      super(itemView);
      tvName = itemView.findViewById(R.id.tvName);
      tvCoin = itemView.findViewById(R.id.tvCoin);
      ivGift = itemView.findViewById(R.id.ivGift);
    }
  }

  public interface ItemSelctListner {
    void onGiftItemSelect(Gifts data);
  }
}
