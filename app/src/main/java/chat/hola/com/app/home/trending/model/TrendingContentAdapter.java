package chat.hola.com.app.home.trending.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.home.LandingActivity;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 6/18/2018.
 */

public class TrendingContentAdapter extends RecyclerView.Adapter<TrendingContentAdapter.ViewHolder> {
    private TypefaceManager typefaceManager;
    private Context context;
    private ArrayList<TrendingResponse> content = new ArrayList<>();
    private ClickListner clickListner;
    private TrendingItemAdapter.ClickListner listner;

    public TrendingContentAdapter(LandingActivity context, TypefaceManager typefaceManager, ArrayList<TrendingResponse> data) {
        this.content = data;
        this.context = context;
        this.typefaceManager = typefaceManager;
    }

    public void setViewAllListner(TrendingItemAdapter.ClickListner listner) {
        this.listner = listner;
    }

    public void setPostListner(ClickListner listner) {
        this.clickListner = listner;
    }

    void refresh(ArrayList<TrendingResponse> data) {
        this.content = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_item_content, parent, false);
        return new TrendingContentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TrendingResponse data = content.get(position);
        holder.tvHashTag.setText(data.getHashTag());
        holder.tvPostCount.setText(data.getTotalPosts() + (data.getTotalPosts()>1?context.getString(R.string.posts):context.getString(R.string.text_post)));

        holder.rvItems.setLayoutManager(new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false));
        holder.rvItems.setAdapter(new TrendingItemAdapter(context, data.getData(), data.getTotalPosts(), data.getHashTag(), listner));

        holder.rlHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListner.viewAllHeader(data.getHashTag(), String.valueOf(data.getTotalPosts()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvHashTag)
        TextView tvHashTag;
        @BindView(R.id.tvPostCount)
        TextView tvPostCount;
        @BindView(R.id.rvItems)
        RecyclerView rvItems;
        @BindView(R.id.rlHeader)
        RelativeLayout rlHeader;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ClickListner {
        void viewAllHeader(String hashtag, String totalPosts);
    }
}
