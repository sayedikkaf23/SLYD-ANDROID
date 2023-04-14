package chat.hola.com.app.DublyCamera;

import android.view.View;

import com.ezcall.android.R;

import androidx.recyclerview.widget.RecyclerView;

class ViewHolderProgressGapItem extends RecyclerView.ViewHolder {


     View progressView;

    ViewHolderProgressGapItem(View view) {
        super(view);

        progressView = (View) view.findViewById(R.id.progressView);
    }
}
