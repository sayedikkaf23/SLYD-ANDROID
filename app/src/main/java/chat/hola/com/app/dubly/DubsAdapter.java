package chat.hola.com.app.dubly;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h1>BlockUserAdapter</h1>
 *
 * @author 3embed
 * @version 1.0
 * @since 4/9/2018
 */

public class DubsAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<Dub> dubs;
    private Context context;
    private ClickListner clickListner;
    private TypefaceManager typefaceManager;

    @Inject
    public DubsAdapter(List<Dub> dubs, Activity mContext, TypefaceManager typefaceManager) {
        this.dubs = dubs;
        this.context = mContext;
        this.typefaceManager = typefaceManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.dub_item, parent, false);
        return new ViewHolder(itemView, typefaceManager, clickListner);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final Dub dub = dubs.get(position);
        Glide.with(context).load(dub.getImageUrl()).placeholder(R.drawable.ic_default).into(holder.ivThumbnail);
        holder.tvTitle.setText(dub.getName());
        holder.tvDuration.setText(dub.getDuration());
        holder.ibPlay.setChecked(dub.isPlaying());
        holder.ivLike.setChecked(dub.isMyFavourite() == 1);
        holder.llDubWithIt.setVisibility(dub.isPlaying() ? View.VISIBLE : View.GONE);
        holder.tvPlaybackTime.setVisibility(dub.isPlaying() ? View.VISIBLE : View.GONE);
        holder.llDubWithIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListner.dubWithIt(dub.getName(), position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dubs.size();
    }

    public void setListener(ClickListner clickListner) {
        this.clickListner = clickListner;
    }
}