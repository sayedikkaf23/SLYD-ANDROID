package chat.hola.com.app.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

public class ViewHolderTransferSent extends RecyclerView.ViewHolder {

    public TextView tV_amount, tV_status, tV_msg, tvDate;
    public RelativeLayout rL_cancel;
    public ImageView pic, ivStatus;

    public ViewHolderTransferSent(@NonNull View itemView) {
        super(itemView);
        tV_amount = (TextView) itemView.findViewById(R.id.tV_amount);
        tV_status = (TextView) itemView.findViewById(R.id.tV_status);
        rL_cancel = (RelativeLayout) itemView.findViewById(R.id.rL_cancel);
        pic = (ImageView) itemView.findViewById(R.id.pic);
        tV_msg = (TextView) itemView.findViewById(R.id.tV_msg);
        tvDate = (TextView) itemView.findViewById(R.id.tvDate);
        ivStatus = (ImageView) itemView.findViewById(R.id.ivStatus);
    }
}
