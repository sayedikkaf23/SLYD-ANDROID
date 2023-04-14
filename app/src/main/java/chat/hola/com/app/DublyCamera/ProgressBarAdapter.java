package chat.hola.com.app.DublyCamera;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ezcall.android.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ProgressBarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIDEO_ITEM = 0;
    private final int GAP_ITEM = 1;


    private ArrayList<ProgressBarModel> mListData;
    private Context mContext;
    private int density;

    public ProgressBarAdapter(Context mContext, ArrayList<ProgressBarModel> mListData) {
        this.mListData = mListData;
        this.mContext = mContext;
        density = (int) mContext.getResources().getDisplayMetrics().density;
    }


    @Override
    public int getItemCount() {
        return this.mListData.size();
    }


    @Override
    public int getItemViewType(int position) {


        if (mListData.get(position).isVideoItem()) {
            return VIDEO_ITEM;
        } else {
            return GAP_ITEM;
        }


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        View v1;
        switch (viewType) {
            case VIDEO_ITEM:
                v1 = inflater.inflate(R.layout.progressview_video, viewGroup, false);
                viewHolder = new ViewHolderProgressVideoItem(v1);
                break;


            default:
                v1 = inflater.inflate(R.layout.progressview_gap, viewGroup, false);
                viewHolder = new ViewHolderProgressGapItem(v1);


        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {


        switch (viewHolder.getItemViewType()) {


            case VIDEO_ITEM:
                ViewHolderProgressVideoItem vh1 = (ViewHolderProgressVideoItem) viewHolder;

                configureViewHolderProgressVideoItem(vh1, position);

                break;


            default:
                ViewHolderProgressGapItem vh2 = (ViewHolderProgressGapItem) viewHolder;

                configureViewHolderProgressGapItem(vh2, position);


        }
    }


    private void configureViewHolderProgressVideoItem(ViewHolderProgressVideoItem vh1, final int position) {
        final ProgressBarModel progressBarModel = mListData.get(position);


        if (progressBarModel != null) {

            vh1.progressView.getLayoutParams().width = progressBarModel.getProgressViewWidth();


            if (progressBarModel.isPlaying()) {

                vh1.progressView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.rose));
            } else {
                vh1.progressView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.blue));

            }
        }
    }

    private void configureViewHolderProgressGapItem(ViewHolderProgressGapItem vh1, final int position) {
        final ProgressBarModel progressBarModel = mListData.get(position);

        if (progressBarModel != null) {

            vh1.progressView.getLayoutParams().width = density;

        }
    }

}