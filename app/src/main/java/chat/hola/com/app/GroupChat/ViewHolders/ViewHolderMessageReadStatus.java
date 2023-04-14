package chat.hola.com.app.GroupChat.ViewHolders;

import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezcall.android.R;

import chat.hola.com.app.AppController;

/**
 * Created by moda on 10/11/17.
 */

public class ViewHolderMessageReadStatus extends RecyclerView.ViewHolder {


    public TextView contactName, readAt, deliveredAt;

    public ImageView contactImage;


    public ViewHolderMessageReadStatus(View view) {
        super(view);

        readAt = (TextView) view.findViewById(R.id.readAt);


        contactName = (TextView) view.findViewById(R.id.contactName);


        deliveredAt = (TextView) view.findViewById(R.id.deliveredAt);

        contactImage = (ImageView) view.findViewById(R.id.storeImage2);
        Typeface tf = AppController.getInstance().getRegularFont();

        contactName.setTypeface(tf, Typeface.NORMAL);
        readAt.setTypeface(tf, Typeface.NORMAL);
        deliveredAt.setTypeface(tf, Typeface.NORMAL);
    }
}
