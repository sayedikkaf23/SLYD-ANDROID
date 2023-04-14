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
 * View holder for media history image recycler view item
 */

public class ViewHolderImageMedia extends RecyclerView.ViewHolder {

    public TextView fnf;
    public AdjustableImageView image;


    public ViewHolderImageMedia(View view) {
        super(view);
        fnf = (TextView) view.findViewById(R.id.fnf);
        image = (AdjustableImageView) view.findViewById(R.id.imageView28);
    }
}