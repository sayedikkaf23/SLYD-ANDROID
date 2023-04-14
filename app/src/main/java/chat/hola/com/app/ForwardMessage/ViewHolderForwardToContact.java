package chat.hola.com.app.ForwardMessage;

import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezcall.android.R;

import chat.hola.com.app.AppController;

/**
 * Created by moda on 30/08/17.
 */

public class ViewHolderForwardToContact extends RecyclerView.ViewHolder {


    public TextView contactName, contactStatus, tv_InviteContact;

    public ImageView contactImage, contactSelected,iV_star;
    public Button btn_invite;


    public ViewHolderForwardToContact(View view) {
        super(view);

        contactStatus = (TextView) view.findViewById(R.id.contactStatus);
        tv_InviteContact = (TextView) view.findViewById(R.id.tv_InviteContact);

        contactName = (TextView) view.findViewById(R.id.contactName);

        contactImage = (ImageView) view.findViewById(R.id.storeImage2);
        contactSelected = (ImageView) view.findViewById(R.id.contactSelected);
        iV_star = (ImageView) view.findViewById(R.id.iV_star);
        btn_invite = (Button) view.findViewById(R.id.btn_invite);
        Typeface tf = AppController.getInstance().getRegularFont();

        contactName.setTypeface(tf, Typeface.NORMAL);
        contactStatus.setTypeface(tf, Typeface.NORMAL);

    }
}
