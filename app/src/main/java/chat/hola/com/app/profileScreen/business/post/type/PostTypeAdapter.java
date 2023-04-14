package chat.hola.com.app.profileScreen.business.post.type;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.ezcall.android.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.models.PostTypeResponse;
import chat.hola.com.app.profileScreen.business.post.actionbutton.ViewHolder;

/**
 * <h1>PostTypeAdapter</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 09 September 2019
 */
public class PostTypeAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<PostTypeResponse.PostType> buttonTexts;
    private TypefaceManager typefaceManager;
    private ClickListner clickListner;
    private int lastCheckedPosition = -1;

    public PostTypeAdapter(Context context, List<PostTypeResponse.PostType> buttonTexts, TypefaceManager typefaceManager) {
        this.context = context;
        this.buttonTexts = buttonTexts;
        this.typefaceManager = typefaceManager;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_action_button, parent, false), typefaceManager);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PostTypeResponse.PostType data = buttonTexts.get(position);
        holder.tvText.setText(data.getText());
        holder.rbItem.setChecked(position == lastCheckedPosition);
        holder.rbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lastCheckedPosition = position;
                    if (clickListner != null)
                        clickListner.onItemSelectListner(data.getId(), data.getText());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return buttonTexts.size();
    }

    public void setClickListner(ClickListner clickListner) {
        this.clickListner = clickListner;
    }

    public void setData(List<PostTypeResponse.PostType> dataList, int lastCheckedPosition) {
        this.buttonTexts = dataList;
        this.lastCheckedPosition = lastCheckedPosition;
        notifyDataSetChanged();
    }

    public interface ClickListner {
        void onItemSelectListner(String id, String text);
    }
}
