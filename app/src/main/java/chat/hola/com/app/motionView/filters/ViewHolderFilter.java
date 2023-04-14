package chat.hola.com.app.motionView.filters;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezcall.android.R;


/**
 * Created by moda on 18/05/17.
 */

public class ViewHolderFilter extends RecyclerView.ViewHolder {
    public TextView filterName;
    public ImageView filterImage, filterSelected;


    public ViewHolderFilter(View view) {
        super(view);


        filterName = (TextView) view.findViewById(R.id.name);

        filterImage = (ImageView) view.findViewById(R.id.iv);
        filterSelected = (ImageView) view.findViewById(R.id.iv_selected_filter);

    }
}