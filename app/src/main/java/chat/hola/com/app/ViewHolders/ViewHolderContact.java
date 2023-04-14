package chat.hola.com.app.ViewHolders;

import android.graphics.Typeface;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import chat.hola.com.app.AppController;
import chat.hola.com.app.DublyCamera.dubbing.TimeDownView;


/**
 * Created by moda on 28/07/17.
 */

public class ViewHolderContact extends RecyclerView.ViewHolder {


    public TextView contactName, contactStatus, contactnameIndicatorTv;

    public RelativeLayout frame;
    public ImageView contactImage, iV_star;
    private AppController appController;
    private Typeface fontMedium, fontRegular, fontBold;
    public Button btn_invite;
    public TextView tv_InviteContact;

    public ViewHolderContact(View view) {
        super(view);
        appController = AppController.getInstance();
        fontMedium = appController.getMediumFont();
        fontRegular = appController.getRegularFont();
        fontBold = appController.getSemiboldFont();
        contactStatus = (TextView) view.findViewById(R.id.contactStatus);
        contactnameIndicatorTv = (TextView) view.findViewById(R.id.contactnameIndicatorTv);

        contactName = (TextView) view.findViewById(R.id.contactName);

        contactImage = (ImageView) view.findViewById(R.id.storeImage2);
        iV_star = (ImageView) view.findViewById(R.id.iV_star);
        btn_invite = (Button) view.findViewById(R.id.btn_invite);
        tv_InviteContact = (TextView) view.findViewById(R.id.tv_InviteContact);

        contactName.setTypeface(fontMedium);
        contactStatus.setTypeface(fontRegular);
        contactnameIndicatorTv.setTypeface(fontBold);


        frame = (RelativeLayout) view.findViewById(R.id.frame);
    }
}
