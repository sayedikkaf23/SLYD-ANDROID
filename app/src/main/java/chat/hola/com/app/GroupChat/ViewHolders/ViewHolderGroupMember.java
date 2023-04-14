package chat.hola.com.app.GroupChat.ViewHolders;

import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import chat.hola.com.app.AppController;

/**
 * Created by moda on 22/09/17.
 */

public class ViewHolderGroupMember extends RecyclerView.ViewHolder {


    public TextView contactName, contactStatus;

    public ImageView contactImage,iV_star;

    public RelativeLayout admin_rl;

    public ViewHolderGroupMember(View view) {
        super(view);
        admin_rl = (RelativeLayout) view.findViewById(R.id.admin_rl);
        contactStatus = (TextView) view.findViewById(R.id.contactStatus);

        iV_star = (ImageView) view.findViewById(R.id.iV_star);
        contactName = (TextView) view.findViewById(R.id.contactName);

        contactImage = (ImageView) view.findViewById(R.id.storeImage2);
        Typeface tf = AppController.getInstance().getRegularFont();

        contactName.setTypeface(tf, Typeface.NORMAL);
        contactStatus.setTypeface(tf, Typeface.NORMAL);

    }
}
