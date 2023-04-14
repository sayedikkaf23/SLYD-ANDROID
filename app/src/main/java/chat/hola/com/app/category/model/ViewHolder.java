package chat.hola.com.app.category.model;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.post.model.CategoryData;

/**
 * <h1>ViewHolder</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 5/2/2018.
 */

class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    @BindView(R.id.ivRow)
    ImageView ivRow;
    @BindView(R.id.tvRow)
    TextView tvRow;
    @BindView(R.id.cbRow)
    CheckBox cbRow;
    @BindView(R.id.rlItem)
    RelativeLayout rlItem;
    @Inject
    List<CategoryData> categories;
    private ClickListner clickListner;

    boolean isSelected = false;

    public ViewHolder(View itemView, TypefaceManager typefaceManager, ClickListner clickListner) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.clickListner = clickListner;
        cbRow.setOnClickListener(this);
        rlItem.setOnClickListener(this);
        tvRow.setTypeface(typefaceManager.getMediumFont());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cbRow:
                isSelected = ((CheckBox) v).isChecked();
                if (isSelected) {
                    clickListner.onItemSelected(getAdapterPosition());
                }
                break;
            case R.id.rlItem:
                isSelected = !isSelected;
                cbRow.setChecked(isSelected);
                if (isSelected) {
                    clickListner.onItemSelected(getAdapterPosition());
                }
                break;
        }
    }
}
