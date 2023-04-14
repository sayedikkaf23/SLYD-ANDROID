package chat.hola.com.app.GroupChat.ViewHolders;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezcall.android.R;

import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;

/**
 * Created by moda on 19/09/17.
 */

public class ViewHolderSelectedMember extends RecyclerView.ViewHolder {


    public TextView contactName;

    public ImageView contactImage, removeContact, iV_star;


    public ViewHolderSelectedMember(View view) {
        super(view);


        contactName = (TextView) view.findViewById(R.id.contactName);
        iV_star = (ImageView) view.findViewById(R.id.iV_star);
        contactImage = (ImageView) view.findViewById(R.id.storeImage2);
        removeContact = (ImageView) view.findViewById(R.id.contactSelected);

        Typeface tf = AppController.getInstance().getRegularFont();

        contactName.setTypeface(tf, Typeface.NORMAL);


    }
}
