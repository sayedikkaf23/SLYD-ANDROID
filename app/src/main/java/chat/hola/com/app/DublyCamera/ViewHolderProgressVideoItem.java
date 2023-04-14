package chat.hola.com.app.DublyCamera;

import android.view.View;

import com.ezcall.android.R;

import androidx.recyclerview.widget.RecyclerView;

class ViewHolderProgressVideoItem extends RecyclerView.ViewHolder {
    View progressView;

    ViewHolderProgressVideoItem(View view) {
        super(view);

        progressView = (View) view.findViewById(R.id.progressView);
    }
}
