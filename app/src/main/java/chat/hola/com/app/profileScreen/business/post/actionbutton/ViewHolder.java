package chat.hola.com.app.profileScreen.business.post.actionbutton;


import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ezcall.android.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;

/**
 * <h1>ViewHolder</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 09 September 2019
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvText)
    public TextView tvText;
    @BindView(R.id.rbItem)
    public RadioButton rbItem;

    public ViewHolder(@NonNull View itemView, TypefaceManager typefaceManager) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        tvText.setTypeface(typefaceManager.getRegularFont());
    }
}
