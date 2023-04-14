package chat.hola.com.app.DublyCamera.CameraInFragments.Filters.ViewHolders;


import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by moda on 22/05/17.
 */

public class ViewHolderManageFilter extends RecyclerView.ViewHolder {
    public TextView filterName;
    public ImageView filterImage, check;

    public RelativeLayout overlay;


    public ViewHolderManageFilter(View view) {
        super(view);


        filterName = (TextView) view.findViewById(R.id.tv_username);

        filterImage = (ImageView) view.findViewById(R.id.iv);


        check = (ImageView) view.findViewById(R.id.check);

        overlay = (RelativeLayout) view.findViewById(R.id.overlay);


    }
}