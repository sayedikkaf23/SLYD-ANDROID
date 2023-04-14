package chat.hola.com.app.ViewHolders;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import chat.hola.com.app.AppController;


/**
 * Created by moda on 28/07/17.
 */

public class ViewHolderContactCall extends  RecyclerView.ViewHolder {


    public TextView contactName, contactStatus,contactnameIndicatorTv;

    public RelativeLayout frame;
    public ImageView contactImage,iV_star;
    public LinearLayoutCompat initiateVideoCall,initiateAudioCall;
   private AppController appController;
   private Typeface fontMedium,fontRegular,fontBold;

    public ViewHolderContactCall(View view) {
        super(view);
       appController=AppController.getInstance();
       fontMedium=appController.getMediumFont();
       fontRegular=appController.getRegularFont();
       fontBold=appController.getSemiboldFont();
        contactStatus = (TextView) view.findViewById(R.id.contactStatus);
        contactnameIndicatorTv=(TextView)view.findViewById(R.id.contactnameIndicatorTv);

        contactName = (TextView) view.findViewById(R.id.contactName);

        contactImage = (ImageView) view.findViewById(R.id.storeImage2);
        iV_star = (ImageView) view.findViewById(R.id.iV_star);
        initiateVideoCall = (LinearLayoutCompat) view.findViewById(R.id.initiateVideoCall);
        initiateAudioCall = (LinearLayoutCompat) view.findViewById(R.id.initiateAudioCall);

        contactName.setTypeface(fontMedium);
        contactStatus.setTypeface(fontRegular);
        contactnameIndicatorTv.setTypeface(fontBold);

        frame = (RelativeLayout) view.findViewById(R.id.frame);
    }
}
