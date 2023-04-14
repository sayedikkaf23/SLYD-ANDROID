package chat.hola.com.app.home.contact;

import android.view.View;
import android.widget.TextView;

import com.ezcall.android.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;

public class TitleViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.title)
    TextView title;


    public TitleViewHolder(@NonNull View itemView, TypefaceManager typefaceManager) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        title.setTypeface(typefaceManager.getBoldFont());
    }

}
