package chat.hola.com.app.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezcall.android.R;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by embed on 31/8/16.
 */
public class ViewHolderMessageMissedCall extends RecyclerView.ViewHolder {

    public TextView message;
    public View gap;
    public ImageView image;


    public ViewHolderMessageMissedCall(View view) {
        super(view);

        message = (TextView) view.findViewById(R.id.message);
        gap = view.findViewById(R.id.gap);
        image = (ImageView) view.findViewById(R.id.image);


    }
}
