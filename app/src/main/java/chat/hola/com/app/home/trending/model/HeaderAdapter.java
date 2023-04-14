package chat.hola.com.app.home.trending.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezcall.android.R;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.recyclerview.widget.RecyclerView;

import chat.hola.com.app.Utilities.RoundedImageView;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.trending.TrendingFragment;

/**
 * <h> HeaderAdapter.class </h>
 * <p> This Adapter is used by {@link TrendingFragment} headerRecyclerView.</p>
 *
 * @author 3Embed
 * @since 13/2/18.
 */

public class HeaderAdapter extends RecyclerView.Adapter<HeaderAdapter.ViewHolder> {

    private TypefaceManager typefaceManager;
    private Context context;
    private ArrayList<Header> headers = new ArrayList<>();
    private int selectedPos = 0;
    private ClickListner clickListner;

    @Inject
    public HeaderAdapter(Context context, TypefaceManager typefaceManager, ArrayList<Header> headers) {
        this.context = context;
        this.typefaceManager = typefaceManager;
        this.headers = headers;
    }

    public void setListner(ClickListner listner) {
        this.clickListner = listner;
    }

    public void setData(ArrayList<Header> headers) {
        this.headers = headers;
        this.selectedPos = 0;
    }

    @Override
    public HeaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trending_header_item, parent, false);
        return new HeaderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HeaderAdapter.ViewHolder holder, int position) {
        holder.itemView.setSelected(selectedPos == position);
        Header header = headers.get(position);
        //holder.selector.setVisibility(holder.itemView.isSelected() ? View.VISIBLE : View.GONE);
        holder.tvTitle.setText(header.getCategoryName());
        Glide.with(context).load(header.getCategoryActiveIconUrl() != null ? header.getCategoryActiveIconUrl() : "").into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return headers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView ivImage;
        TextView tvTitle;
        RoundedImageView rivImage;
        View selector;

        public ViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            rivImage = (RoundedImageView) itemView.findViewById(R.id.rivImage);
            selector = (View) itemView.findViewById(R.id.selector);
            tvTitle.setTypeface(typefaceManager.getSemiboldFont());
            ivImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);

            if (view.getId() == R.id.ivImage)
                clickListner.onItemClick(headers.get(getAdapterPosition()).getId(), headers.get(getAdapterPosition()).getCategoryName());

        }
    }

    public interface ClickListner {
        void onItemClick(String categoryId, String categoryName);

        void onStarClick();

        void onLiveStreamClick();
    }

}
