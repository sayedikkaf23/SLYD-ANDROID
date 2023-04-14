package chat.hola.com.app.profileScreen.business.post.actionbutton;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.ezcall.android.R;

import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.models.ActionButtonResponse;

/**
 * <h1>ActionButtonAdapter</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 09 September 2019
 */
public class ActionButtonAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<ActionButtonResponse.BizButton> buttonTexts;
    private TypefaceManager typefaceManager;
    private ClickListner clickListner;
    private int lastCheckedPosition = -1;

    @Inject
    public ActionButtonAdapter(Context context, List<ActionButtonResponse.BizButton> buttonTexts, TypefaceManager typefaceManager) {
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
        ActionButtonResponse.BizButton data = buttonTexts.get(position);
        holder.tvText.setText(data.getButtonText());
        holder.rbItem.setChecked(position == lastCheckedPosition);
        holder.rbItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    lastCheckedPosition = position;
                    if (clickListner != null)
                        clickListner.onItemSelectListner(data);
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

    public void setData(List<ActionButtonResponse.BizButton> dataList, int pos) {
        this.buttonTexts = dataList;
        lastCheckedPosition = pos;
        notifyDataSetChanged();
    }

    public interface ClickListner {
        void onItemSelectListner(ActionButtonResponse.BizButton bizButton);
    }
}
