package chat.hola.com.app.Status;

import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import chat.hola.com.app.AppController;
import com.ezcall.android.R;


/**
 * Created by moda on 04/08/17.
 */

public class ViewHolderStatus extends RecyclerView.ViewHolder {
    public TextView status;
    public ImageView delete;

    public View seperator;

    public ViewHolderStatus(View view) {
        super(view);
        delete = (ImageView) view.findViewById(R.id.delete);
        status = (TextView) view.findViewById(R.id.statusTv);
        seperator= view.findViewById(R.id.seperator);
        Typeface tf = AppController.getInstance().getMediumFont();
        status.setTypeface(tf, Typeface.NORMAL);


    }
}