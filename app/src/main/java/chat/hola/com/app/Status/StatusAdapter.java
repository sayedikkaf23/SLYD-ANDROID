package chat.hola.com.app.Status;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import chat.hola.com.app.AppController;

import com.ezcall.android.R;

/**
 * Created by moda on 04/08/17.
 */

public class StatusAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private ArrayList<StatusItem> mListData = new ArrayList<>();

    private Context mContext;


    /**
     * @param mContext  Context
     * @param mListData ArrayList<StatusItem>
     */
    public StatusAdapter(Context mContext, ArrayList<StatusItem> mListData) {


        this.mListData = mListData;


        this.mContext = mContext;


    }

    /**
     * @return number of items
     */
    @Override
    public int getItemCount() {
        return this.mListData.size();
    }


    /**
     * @param position item position
     * @return item viewType
     */
    @Override
    public int getItemViewType(int position) {
        return 1;
    }


    /**
     * @param viewGroup ViewGroup
     * @param viewType  item viewType
     * @return RecyclerView.ViewHolder
     */

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());


        View v = inflater.inflate(R.layout.status_item, viewGroup, false);
        viewHolder = new ViewHolderStatus(v);

        return viewHolder;
    }


    /**
     * @param viewHolder RecyclerView.ViewHolder
     * @param position   item position
     */

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {


        ViewHolderStatus vh2 = (ViewHolderStatus) viewHolder;
        configureViewHolderStatus(vh2, position);

    }


    /**
     * @param vh       ViewHolderStatus
     * @param position item position
     */
    private void configureViewHolderStatus(final ViewHolderStatus vh, int position) {
        final StatusItem status = mListData.get(position);
        if (status != null) {


            if (status.getStatusType() == 0) {

                vh.delete.setVisibility(View.VISIBLE);

            } else {

                vh.delete.setVisibility(View.GONE);


            }
            vh.status.setTypeface(AppController.getInstance().getMediumFont());
            vh.status.setText(status.getStatus());


            vh.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
/*
 * To delete the particular status from the local couchdb
 */

                    AppController.getInstance().getDbController().deleteParticularStatus(AppController.getInstance().getStatusDocId(), status.getStatus());


                    delete(vh.getAdapterPosition());


                }
            });


            if (vh.getAdapterPosition() == 0) {
                vh.seperator.setVisibility(View.GONE);
            } else {
                vh.seperator.setVisibility(View.VISIBLE);
            }
        }
    }

    public void delete(int position) { //removes the particular status
        mListData.remove(position);


        if (position == 0) {


            notifyDataSetChanged();
        } else {
            notifyItemRemoved(position);
        }

    }

}
