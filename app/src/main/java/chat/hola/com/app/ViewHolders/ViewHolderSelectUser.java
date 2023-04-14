package chat.hola.com.app.ViewHolders;

import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezcall.android.R;

import chat.hola.com.app.AppController;


/**
 * Created by moda on 19/06/17.
 */


public class ViewHolderSelectUser extends RecyclerView.ViewHolder {
    public TextView userName, email;
    public ImageView userImage;

    public ViewHolderSelectUser(View view) {
        super(view);


        userName = (TextView) view.findViewById(R.id.userName);
        email = (TextView) view.findViewById(R.id.userIdentifier);

        userImage = (ImageView) view.findViewById(R.id.userImage);

        Typeface tf = AppController.getInstance().getRegularFont();
        userName.setTypeface(tf, Typeface.NORMAL);
        email.setTypeface(tf, Typeface.NORMAL);

    }
}
