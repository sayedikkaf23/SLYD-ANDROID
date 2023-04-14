package chat.hola.com.app.DublyCamera;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ezcall.android.R;

import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;

public class ViewHolderDublyFilters extends RecyclerView.ViewHolder {


    public TextView filterName;
    public ImageView filterImage,selectedSign;

    public ViewHolderDublyFilters(View view) {
        super(view);


        filterName = (TextView) view.findViewById(R.id.filterName);
        filterImage= (ImageView) view.findViewById(R.id.iv);
        selectedSign= (ImageView) view.findViewById(R.id.iv_selected_filter);
        Typeface tf = AppController.getInstance().getSemiboldFont();
        filterName.setTypeface(tf, Typeface.BOLD);


    }

}
