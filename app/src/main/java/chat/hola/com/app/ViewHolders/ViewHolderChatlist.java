package chat.hola.com.app.ViewHolders;

import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import chat.hola.com.app.AppController;
import chat.hola.com.app.Utilities.TypefaceManager;


/**
 * Created by moda on 20/06/17.
 */

public class ViewHolderChatlist extends RecyclerView.ViewHolder {
    public TextView newMessageTime, newMessage, storeName, newMessageDate, newMessageCount, tv_remaining_count;
    public ImageView storeImage, lock, tick, chatSelected,iV_star;
    public RelativeLayout rl, bg_frame;
    public LinearLayout background;
    public ImageView buttonAction;
    private AppController appController;
    private Typeface fontMedium,fontRegular;

    public ImageView profile1,profile2,profile3,profile4,profile5;
    public ViewHolderChatlist(View view) {
        super(view);
         appController=AppController.getInstance();
         fontMedium=appController.getMediumFont();
         fontRegular=appController.getRegularFont();
        tick = (ImageView) view.findViewById(R.id.tick);
        newMessageTime = (TextView) view.findViewById(R.id.newMessageTime);
        newMessage = (TextView) view.findViewById(R.id.newMessage);
        newMessageDate = (TextView) view.findViewById(R.id.newMessageDate);
        storeName = (TextView) view.findViewById(R.id.storeName);
        tv_remaining_count = (TextView) view.findViewById(R.id.tv_remaining_count);
        storeImage = (ImageView) view.findViewById(R.id.storeImage2);


        profile1 = (ImageView) view.findViewById(R.id.profile1);
        profile2 = (ImageView) view.findViewById(R.id.profile2);
        profile3 = (ImageView) view.findViewById(R.id.profile3);
        profile4 = (ImageView) view.findViewById(R.id.profile4);
        profile5 = (ImageView) view.findViewById(R.id.profile5);

        chatSelected = (ImageView) view.findViewById(R.id.chatSelected);
        iV_star = (ImageView) view.findViewById(R.id.iV_star);

        rl = (RelativeLayout) view.findViewById(R.id.rl);
        bg_frame = (RelativeLayout) view.findViewById(R.id.frame);
        background = (LinearLayout) view.findViewById(R.id.ll_delete);
        buttonAction = (ImageView) view.findViewById(R.id.buttonAction);



        newMessageCount = (TextView) view.findViewById(R.id.newMessageCount);
        lock = (ImageView) view.findViewById(R.id.secretLockIv);

        TypefaceManager tfm = new TypefaceManager(view.getContext());

        newMessageCount.setTypeface(tfm.getMediumFont());
        newMessageDate.setTypeface(tfm.getRegularFont());
        newMessageTime.setTypeface(tfm.getRegularFont());
        newMessage.setTypeface(tfm.getRegularFont());
        storeName.setTypeface(tfm.getRegularFont());
    }
}
