package chat.hola.com.app.ViewHolders;
/*
 * Created by moda on 15/04/16.
 */

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ezcall.android.R;
import chat.hola.com.app.Utilities.AdjustableImageView;


/**
 * View holder for media history video recycler view item
 */
public class ViewHolderVideoMedia extends RecyclerView.ViewHolder {


    public AdjustableImageView thumbnail;

    public TextView fnf;

    public  ViewHolderVideoMedia(View view) {
        super(view);
        fnf = (TextView) view.findViewById(R.id.fnf);
        thumbnail = (AdjustableImageView) view.findViewById(R.id.vidshow);
    }
}
