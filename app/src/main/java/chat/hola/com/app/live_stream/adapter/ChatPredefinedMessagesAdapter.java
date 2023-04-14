package chat.hola.com.app.live_stream.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ezcall.android.R;

import chat.hola.com.app.live_stream.CallbacksFromChatPredefined;

public class ChatPredefinedMessagesAdapter extends RecyclerView.Adapter {

  public  String[] presetMessages;
    CallbacksFromChatPredefined chatCallback;
    public ChatPredefinedMessagesAdapter(String[] presets_chats, CallbacksFromChatPredefined chatCallback){
        this.presetMessages =presets_chats;
        this.chatCallback=chatCallback;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_preset_item,viewGroup,false);
        return new ChatPreset(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ChatPreset preset= (ChatPreset) viewHolder;
        preset.tvChatPresetItems.setText(presetMessages[i]);
    }

    @Override
    public int getItemCount() {
        return (presetMessages !=null)? presetMessages.length:0;
    }
    public class  ChatPreset extends RecyclerView.ViewHolder{
        TextView tvChatPresetItems;
        public ChatPreset(@NonNull View itemView) {
            super(itemView);
            tvChatPresetItems =itemView.findViewById(R.id.tv_chatPreset_item);
            tvChatPresetItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chatCallback.onPresetChatClicked(presetMessages[getAdapterPosition()]);
                }
            });
        }
    }
}
