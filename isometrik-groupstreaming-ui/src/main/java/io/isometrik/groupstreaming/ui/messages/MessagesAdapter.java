package io.isometrik.groupstreaming.ui.messages;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import io.isometrik.groupstreaming.ui.R;
import io.isometrik.groupstreaming.ui.copublish.CopublishActivity;
import io.isometrik.groupstreaming.ui.scrollable.ScrollableStreamsActivity;
import java.util.ArrayList;

/**
 * The type Messages adapter.
 */
public class MessagesAdapter extends RecyclerView.Adapter {

  private Context mContext;
  private ArrayList<MessagesModel> messages;

  private final int NORMAL_MESSAGE = 0;
  private final int LIKE_MESSAGE = 1;
  private final int GIFT_MESSAGE = 2;
  private final int REMOVED_MESSAGE = 3;
  private final int PRESENCE_MESSAGE = 4;
  private final int COPUBLISH_REQUEST_MESSAGE = 5;
  private final int COPUBLISH_ACCEPTED_MESSAGE = 6;
  private final int COPUBLISH_ACTION_MESSAGE = 7;

  /**
   * Instantiates a new Messages adapter.
   *
   * @param mContext the m context
   * @param messages the messages
   */
  public MessagesAdapter(Context mContext, ArrayList<MessagesModel> messages) {
    this.mContext = mContext;
    this.messages = messages;
  }

  @Override
  public int getItemViewType(int position) {

    switch (messages.get(position).getMessageItemType()) {

      case 1:
        return LIKE_MESSAGE;
      case 2:
        return GIFT_MESSAGE;
      case 3:
        return REMOVED_MESSAGE;
      case 4:
        return PRESENCE_MESSAGE;
      case 5:
        return COPUBLISH_REQUEST_MESSAGE;
      case 6:
        return COPUBLISH_ACCEPTED_MESSAGE;
      case 7:
        return COPUBLISH_ACTION_MESSAGE;
      default:
        return NORMAL_MESSAGE;
    }
  }

  @Override
  public int getItemCount() {
    return messages.size();
  }

  @NonNull
  @Override
  public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

    switch (viewType) {

      case LIKE_MESSAGE:

        return new MessagesAdapter.MessagesViewHolder(LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.ism_like_message_item, viewGroup, false));

      case GIFT_MESSAGE:

        return new MessagesAdapter.MessagesViewHolder(LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.ism_gift_message_item, viewGroup, false));

      case REMOVED_MESSAGE:

        return new MessagesAdapter.MessagesViewHolder(LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.ism_removed_message_item, viewGroup, false));

      case PRESENCE_MESSAGE:

        return new MessagesAdapter.MessagesViewHolder(LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.ism_presence_message_item, viewGroup, false));

      case COPUBLISH_REQUEST_MESSAGE:

        return new MessagesAdapter.MessagesViewHolder(LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.ism_request_message_item, viewGroup, false));

      case COPUBLISH_ACCEPTED_MESSAGE:

        return new MessagesAdapter.MessagesViewHolder(LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.ism_request_accepted_message_item, viewGroup, false));

      case COPUBLISH_ACTION_MESSAGE:

        return new MessagesAdapter.MessagesViewHolder(LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.ism_request_action_message_item, viewGroup, false));

      default:

        return new MessagesAdapter.MessagesViewHolder(LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.ism_text_message_item, viewGroup, false));
    }
  }

  @Override
  public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

    switch (viewHolder.getItemViewType()) {

      case LIKE_MESSAGE:

        configureViewHolderLikeMessage((MessagesAdapter.MessagesViewHolder) viewHolder, position);
        break;

      case GIFT_MESSAGE:

        configureViewHolderGiftMessage((MessagesAdapter.MessagesViewHolder) viewHolder, position);
        break;

      case REMOVED_MESSAGE:

        configureViewHolderRemoveMessage((MessagesAdapter.MessagesViewHolder) viewHolder, position);
        break;

      case PRESENCE_MESSAGE:

        configureViewHolderPresenceMessage((MessagesAdapter.MessagesViewHolder) viewHolder,
            position);
        break;

      case COPUBLISH_REQUEST_MESSAGE:

        configureViewHolderCopublishRequestedMessage(
            (MessagesAdapter.MessagesViewHolder) viewHolder, position);
        break;

      case COPUBLISH_ACCEPTED_MESSAGE:

        configureViewHolderCopublishAcceptedMessage((MessagesAdapter.MessagesViewHolder) viewHolder,
            position);
        break;

      case COPUBLISH_ACTION_MESSAGE:

        configureViewHolderCopublishActionMessage((MessagesAdapter.MessagesViewHolder) viewHolder,
            position);
        break;

      default:

        configureViewHolderNormalMessage((MessagesAdapter.MessagesViewHolder) viewHolder, position);
        break;
    }
  }

  private void configureViewHolderPresenceMessage(MessagesAdapter.MessagesViewHolder viewHolder,
      final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        viewHolder.tvMessage.setText(message.getMessage());
        viewHolder.tvMessageTime.setText(message.getMessageTime());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void configureViewHolderRemoveMessage(MessagesAdapter.MessagesViewHolder viewHolder,
      final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {
        viewHolder.tvMessageTime.setText(message.getMessageTime());
        viewHolder.tvMessage.setText(message.getMessage());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void configureViewHolderNormalMessage(MessagesAdapter.MessagesViewHolder viewHolder,
      final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {

        viewHolder.tvMessage.setText(message.getMessage());
        viewHolder.tvUserName.setText(message.getSenderName());
        viewHolder.tvMessageTime.setText(message.getMessageTime());

        if (message.isCanRemoveMessage()) {

          viewHolder.ivDeleteMessage.setVisibility(View.VISIBLE);

          viewHolder.ivDeleteMessage.setOnClickListener(v -> {

            if (mContext instanceof CopublishActivity) {

              ((CopublishActivity) mContext).removeMessage(message.getMessageId(),
                  message.getTimestamp());
            } else if (mContext instanceof ScrollableStreamsActivity) {

              ((ScrollableStreamsActivity) mContext).removeMessage(message.getMessageId(),
                  message.getTimestamp());
            }
          });
        } else {
          viewHolder.ivDeleteMessage.setVisibility(View.GONE);
        }
        if (message.isReceivedMessage()) {
          viewHolder.ivDeliveryStatus.setVisibility(View.INVISIBLE);
        } else {

          viewHolder.ivDeliveryStatus.setVisibility(View.VISIBLE);
          if (message.isDelivered()) {

            viewHolder.ivDeliveryStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_ic_done));
          } else {
            viewHolder.ivDeliveryStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_ic_access_time));
          }
        }
        try {
          Glide.with(mContext)
              .load(message.getSenderImage())
              .asBitmap()
              .placeholder(R.drawable.ism_default_profile_image)
              .into(new BitmapImageViewTarget(viewHolder.ivSenderImage) {
                @Override
                protected void setResource(Bitmap resource) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  viewHolder.ivSenderImage.setImageDrawable(circularBitmapDrawable);
                }
              });
        } catch (IllegalArgumentException | NullPointerException e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void configureViewHolderLikeMessage(MessagesAdapter.MessagesViewHolder viewHolder,
      final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {

        viewHolder.tvUserName.setText(message.getSenderName());
        viewHolder.tvMessageTime.setText(message.getMessageTime());

        if (message.isReceivedMessage()) {
          viewHolder.ivDeliveryStatus.setVisibility(View.INVISIBLE);
        } else {

          viewHolder.ivDeliveryStatus.setVisibility(View.VISIBLE);
          if (message.isDelivered()) {

            viewHolder.ivDeliveryStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_ic_done));
          } else {
            viewHolder.ivDeliveryStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_ic_access_time));
          }
        }
        try {
          Glide.with(mContext)
              .load(message.getSenderImage())
              .asBitmap()
              .placeholder(R.drawable.ism_default_profile_image)
              .into(new BitmapImageViewTarget(viewHolder.ivSenderImage) {
                @Override
                protected void setResource(Bitmap resource) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  viewHolder.ivSenderImage.setImageDrawable(circularBitmapDrawable);
                }
              });
        } catch (IllegalArgumentException | NullPointerException e) {
          e.printStackTrace();
        }

        try {
          Glide.with(mContext).load(message.getMessage()).into(viewHolder.ivLike);
        } catch (IllegalArgumentException | NullPointerException e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @SuppressWarnings("all")
  private void configureViewHolderGiftMessage(MessagesAdapter.MessagesViewHolder viewHolder,
      final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {

        viewHolder.tvUserName.setText(message.getSenderName());
        viewHolder.tvMessageTime.setText(message.getMessageTime());
        viewHolder.tvCoinsValue.setText(
            message.getGiftName() + " - " + mContext.getString(R.string.ism_coins,
                String.valueOf(message.getCoinValue())));

        if (message.isReceivedMessage()) {
          viewHolder.ivDeliveryStatus.setVisibility(View.INVISIBLE);
        } else {

          viewHolder.ivDeliveryStatus.setVisibility(View.VISIBLE);
          if (message.isDelivered()) {

            viewHolder.ivDeliveryStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_ic_done));
          } else {
            viewHolder.ivDeliveryStatus.setImageDrawable(
                ContextCompat.getDrawable(mContext, R.drawable.ism_ic_access_time));
          }
        }
        try {
          Glide.with(mContext)
              .load(message.getSenderImage())
              .asBitmap()
              .placeholder(R.drawable.ism_default_profile_image)
              .into(new BitmapImageViewTarget(viewHolder.ivSenderImage) {
                @Override
                protected void setResource(Bitmap resource) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  viewHolder.ivSenderImage.setImageDrawable(circularBitmapDrawable);
                }
              });
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        } catch (NullPointerException e) {
          e.printStackTrace();
        }

        try {
          Glide.with(mContext).load(message.getMessage()).into(viewHolder.ivGift);
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        } catch (NullPointerException e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void configureViewHolderCopublishRequestedMessage(
      MessagesAdapter.MessagesViewHolder viewHolder, final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {

        viewHolder.tvMessage.setText(message.getMessage());
        viewHolder.tvUserName.setText(message.getSenderName());
        viewHolder.tvMessageTime.setText(message.getMessageTime());
        if (message.isInitiator()) {
          viewHolder.rlAccept.setVisibility(View.VISIBLE);
          viewHolder.rlDecline.setVisibility(View.VISIBLE);
          viewHolder.rlAccept.setOnClickListener(v -> {
            if (mContext instanceof CopublishActivity) {
              ((CopublishActivity) mContext).acceptCopublishRequest(message.getSenderId());
            } else if (mContext instanceof ScrollableStreamsActivity) {

              ((ScrollableStreamsActivity) mContext).acceptCopublishRequest(message.getSenderId());
            }
          });

          viewHolder.rlDecline.setOnClickListener(v -> {
            if (mContext instanceof CopublishActivity) {
              ((CopublishActivity) mContext).declineCopublishRequest(message.getSenderId());
            } else if (mContext instanceof ScrollableStreamsActivity) {
              ((ScrollableStreamsActivity) mContext).declineCopublishRequest(message.getSenderId());
            }
          });
        } else {
          viewHolder.rlAccept.setVisibility(View.GONE);
          viewHolder.rlDecline.setVisibility(View.GONE);
        }

        try {
          Glide.with(mContext)
              .load(message.getSenderImage())
              .asBitmap()
              .placeholder(R.drawable.ism_default_profile_image)
              .into(new BitmapImageViewTarget(viewHolder.ivSenderImage) {
                @Override
                protected void setResource(Bitmap resource) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  viewHolder.ivSenderImage.setImageDrawable(circularBitmapDrawable);
                }
              });
        } catch (IllegalArgumentException | NullPointerException e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void configureViewHolderCopublishAcceptedMessage(
      MessagesAdapter.MessagesViewHolder viewHolder, final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {

        viewHolder.tvMessage.setText(message.getMessage());
        viewHolder.tvUserName.setText(message.getSenderName());
        viewHolder.tvMessageTime.setText(message.getMessageTime());
        if (message.isCanJoin()) {
          viewHolder.rlJoin.setVisibility(View.VISIBLE);
          viewHolder.rlJoin.setOnClickListener(v -> {
            if (mContext instanceof CopublishActivity) {
              ((CopublishActivity) mContext).switchProfile();
            } else if (mContext instanceof ScrollableStreamsActivity) {
              ((ScrollableStreamsActivity) mContext).switchProfile();
            }
          });
        } else {
          viewHolder.rlJoin.setVisibility(View.GONE);
        }
        try {
          Glide.with(mContext)
              .load(message.getSenderImage())
              .asBitmap()
              .placeholder(R.drawable.ism_default_profile_image)
              .into(new BitmapImageViewTarget(viewHolder.ivSenderImage) {
                @Override
                protected void setResource(Bitmap resource) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  viewHolder.ivSenderImage.setImageDrawable(circularBitmapDrawable);
                }
              });
        } catch (IllegalArgumentException | NullPointerException e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void configureViewHolderCopublishActionMessage(
      MessagesAdapter.MessagesViewHolder viewHolder, final int position) {

    try {
      MessagesModel message = messages.get(position);
      if (message != null) {

        viewHolder.tvMessage.setText(message.getMessage());
        viewHolder.tvUserName.setText(message.getSenderName());
        viewHolder.tvMessageTime.setText(message.getMessageTime());
        try {
          Glide.with(mContext)
              .load(message.getSenderImage())
              .asBitmap()
              .placeholder(R.drawable.ism_default_profile_image)
              .into(new BitmapImageViewTarget(viewHolder.ivSenderImage) {
                @Override
                protected void setResource(Bitmap resource) {
                  RoundedBitmapDrawable circularBitmapDrawable =
                      RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                  circularBitmapDrawable.setCircular(true);
                  viewHolder.ivSenderImage.setImageDrawable(circularBitmapDrawable);
                }
              });
        } catch (IllegalArgumentException | NullPointerException e) {
          e.printStackTrace();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * The type Messages view holder.
   */
  static class MessagesViewHolder extends RecyclerView.ViewHolder {

    TextView tvUserName, tvMessage, tvMessageTime, tvCoinsValue;

    ImageView ivSenderImage, ivDeleteMessage, ivDeliveryStatus, ivLike, ivGift;

    RelativeLayout rlDecline, rlAccept, rlJoin;

    /**
     * Instantiates a new Messages view holder.
     *
     * @param itemView the item view
     */
    MessagesViewHolder(@NonNull View itemView) {
      super(itemView);
      tvUserName = itemView.findViewById(R.id.tvUserName);
      tvMessage = itemView.findViewById(R.id.tvMessage);
      tvMessageTime = itemView.findViewById(R.id.tvMessageTime);
      tvCoinsValue = itemView.findViewById(R.id.tvCoinValue);
      ivSenderImage = itemView.findViewById(R.id.ivProfilePic);
      ivDeleteMessage = itemView.findViewById(R.id.ivDeleteMessage);
      ivDeliveryStatus = itemView.findViewById(R.id.ivDeliveryStatus);
      ivLike = itemView.findViewById(R.id.ivLike);
      ivGift = itemView.findViewById(R.id.ivGift);
      rlDecline = itemView.findViewById(R.id.rlDecline);
      rlAccept = itemView.findViewById(R.id.rlAccept);
      rlJoin = itemView.findViewById(R.id.rlJoin);
    }
  }
}