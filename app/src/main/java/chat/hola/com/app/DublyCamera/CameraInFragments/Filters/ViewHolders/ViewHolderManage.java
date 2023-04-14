package chat.hola.com.app.DublyCamera.CameraInFragments.Filters.ViewHolders;


import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by moda on 18/05/17.
 */

public class ViewHolderManage extends RecyclerView.ViewHolder {
    public TextView filterName;
    public RelativeLayout manage;


    public ViewHolderManage(View view) {
        super(view);


        filterName = (TextView) view.findViewById(R.id.name);

        manage = (RelativeLayout) view.findViewById(R.id.iv);


    }
}