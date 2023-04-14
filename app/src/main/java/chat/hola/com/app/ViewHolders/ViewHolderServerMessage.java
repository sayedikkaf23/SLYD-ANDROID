package chat.hola.com.app.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ezcall.android.R;


/**
 * Created by embed on 31/8/16.
 */
public class ViewHolderServerMessage extends RecyclerView.ViewHolder {

    public TextView serverupdate;
    public View gap;


    public ViewHolderServerMessage(View view) {
        super(view);

        serverupdate = (TextView) view.findViewById(R.id.servermessage);
        gap = view.findViewById(R.id.gap);

    }
}
