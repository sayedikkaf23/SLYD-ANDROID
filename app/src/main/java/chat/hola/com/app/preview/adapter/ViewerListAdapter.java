package chat.hola.com.app.preview.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezcall.android.R;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import chat.hola.com.app.home.stories.model.Viewer;

/**
 * Created by embed on 13/12/18.
 */

public class ViewerListAdapter extends RecyclerView.Adapter<ViewerListAdapter.MyViewHolder>{

    Context mContext;
    List<Viewer> viewerList;

    public ViewerListAdapter(Context mContext, List<Viewer> viewerList) {
        this.mContext = mContext;
        this.viewerList = viewerList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.single_row_viewer_list,null,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tV_userName.setText(viewerList.get(position).getUserName());
        holder.tV_time.setText(getTime(Long.parseLong(viewerList.get(position).getTimestamp())));

        Glide.with(mContext)
                .load(viewerList.get(position).getProfilePic())
                .centerCrop()
                .into(holder.iV_profilePic);
    }

    @Override
    public int getItemCount() {
        return viewerList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView iV_profilePic;
        TextView tV_userName,tV_time;
        public MyViewHolder(View itemView) {
            super(itemView);
            iV_profilePic = (ImageView)itemView.findViewById(R.id.iV_profilePic);
            tV_userName = (TextView)itemView.findViewById(R.id.tV_userName);
            tV_time = (TextView)itemView.findViewById(R.id.tV_time);

        }
    }

    public String getTime(long timestamp){
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        String date = DateFormat.format("hh:mm", cal).toString();
        return date;
    }

}
