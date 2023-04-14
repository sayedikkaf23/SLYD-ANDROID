package chat.hola.com.app.calling.video.call;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;
import chat.hola.com.app.AppController;
import com.appscrip.myapplication.utility.Utility;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.ezcall.android.R;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;

import org.webrtc.EglBase;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoSink;

import static com.appscrip.myapplication.utility.Constants.NO_VALUE;
import static com.appscrip.myapplication.utility.Constants.ONE;
import static com.appscrip.myapplication.utility.Constants.THRICE;
import static com.appscrip.myapplication.utility.Constants.TWO;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {
  private static final String TAG = VideosAdapter.class.getSimpleName();
  private EglBase rootEglBase;
  private ArrayList<CallUserDetails> videoRendererList;
  private HashMap<BigInteger, VideoSink> listOfViews = new HashMap<>();
  private ClickEvent clickEvent;

  public VideosAdapter(EglBase rootEglBase, ArrayList<CallUserDetails> videoRendererList,
      ClickEvent clickEvent) {
    this.clickEvent = clickEvent;
    this.rootEglBase = rootEglBase;
    this.videoRendererList = videoRendererList;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
        .inflate(R.layout.item_video_cell, viewGroup, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
    viewHolder.setIsRecyclable(false); // fix for items fluctuation

    if (videoRendererList.get(i).isToShowPlaceHodler()) {
      viewHolder.svVideoRenderer.setVisibility(View.GONE);
      viewHolder.grpPlaceHolderUI.setVisibility(View.VISIBLE);
      viewHolder.ivGradientColor.setVisibility(View.VISIBLE);
      if (videoRendererList.get(i).getUserDetails() != null) {
        //                Glide.with(viewHolder.ivPlaceHolder.getContext())
        //                        .load(videoRendererList.get(i).getUserDetails().getUserImage())
        //                        .apply(RequestOptions.bitmapTransform(new BlurTransformation()))
        //                        .into(
        //                                new CustomTarget<Drawable>() {
        //                                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        //                                    @Override
        //                                    public void onResourceReady(
        //                                            @NonNull Drawable resource,
        //                                            @Nullable Transition<? super Drawable> transition) {
        //                                        Log.i(TAG, "is to user image loaded " +resource);
        //                                        viewHolder.ivPlaceHolder.setBackground(resource);
        //                                    }
        //
        //                                    @Override
        //                                    public void onLoadCleared(@Nullable Drawable placeholder) {}
        //                                });

        Glide.with(viewHolder.ivPlaceHolder.getContext())
            .load(videoRendererList.get(i).getUserDetails().getUserImage())
            .asBitmap()
            .centerCrop()
            .signature(new StringSignature(
                AppController.getInstance().getSessionManager().getUserProfilePicUpdateTime()))
            //.diskCacheStrategy(DiskCacheStrategy.NONE)
            //.skipMemoryCache(true)
            .into(new BitmapImageViewTarget(viewHolder.ivPlaceHolder) {
              @Override
              protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(
                    viewHolder.ivPlaceHolder.getContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                viewHolder.ivPlaceHolder.setImageDrawable(circularBitmapDrawable);
              }
            });
      }
    } else {
      viewHolder.ivGradientColor.setVisibility(View.GONE);
      //VideoRenderer videoRenderer = new VideoRenderer(viewHolder.svVideoRenderer);
      videoRendererList.get(i).getJanusConnection().videoTrack.addSink(viewHolder.svVideoRenderer);
      listOfViews.put(videoRendererList.get(i).getJanusConnection().handleId,
          viewHolder.svVideoRenderer);
           /* viewHolder.clItemRoot.setOnClickListener(
                    v -> {
                        clickEvent.muteOpponent(
                                videoRendererList.get(i).getJanusConnection().handleId);
                    });*/
    }

    switch (videoRendererList.size()) {
      case ONE:
        Utility.getDeviceConfiguration(viewHolder.svVideoRenderer.getContext(),
            (deviceWidth, deviceHeight) -> {
              viewHolder.svVideoRenderer.getLayoutParams().width = deviceWidth;
              viewHolder.svVideoRenderer.getLayoutParams().height = deviceHeight;
            });
        break;

      case TWO:
      case THRICE:
        Utility.getDeviceConfiguration(viewHolder.svVideoRenderer.getContext(),
            (deviceWidth, deviceHeight) -> {
              if (videoRendererList.get(i).isToShowPlaceHodler()) {
                viewHolder.clItemRoot.getLayoutParams().width = deviceWidth / TWO;
                viewHolder.clItemRoot.getLayoutParams().height = deviceHeight / TWO;
              } else {
                viewHolder.svVideoRenderer.getLayoutParams().width = deviceWidth / TWO;
                viewHolder.svVideoRenderer.getLayoutParams().height = deviceHeight / TWO;
              }
            });
        break;
    }
  }

  @Override
  public int getItemCount() {
    return videoRendererList != null ? videoRendererList.size() : NO_VALUE;
  }

  public VideoSink getLostRemote(BigInteger bigInteger) {
    return listOfViews.get(bigInteger);
  }

  public void setRemoteRenderSize(RecyclerView rvRemoteViews,boolean isPIP) {
    for(int i=0; i<videoRendererList.size(); i++) {
      ViewHolder viewHolder = (ViewHolder) rvRemoteViews.findViewHolderForAdapterPosition(i);
      if (isPIP) {
        assert viewHolder != null;
        Utility.getDeviceConfiguration(viewHolder.svVideoRenderer.getContext(),
                (deviceWidth, deviceHeight) -> {
                  viewHolder.svVideoRenderer.getLayoutParams().width = deviceWidth / 3;
                  viewHolder.svVideoRenderer.getLayoutParams().height = deviceHeight / 3;
                });
      } else {
        assert viewHolder != null;
        Utility.getDeviceConfiguration(viewHolder.svVideoRenderer.getContext(),
                (deviceWidth, deviceHeight) -> {
                  viewHolder.svVideoRenderer.getLayoutParams().width = deviceWidth;
                  viewHolder.svVideoRenderer.getLayoutParams().height = deviceHeight;
                });
      }
    }
    try {
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          notifyDataSetChanged();
        }
      },1000);
    }catch (Exception ignored){
      notifyDataSetChanged();
    }
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    SurfaceViewRenderer svVideoRenderer;
    Group grpPlaceHolderUI;
    AppCompatImageView ivPlaceHolder, ivGradientColor;
    ConstraintLayout clItemRoot;

    ViewHolder(View itemView) {
      super(itemView);
      svVideoRenderer = itemView.findViewById(R.id.svVideoRenderer);
      ivPlaceHolder = itemView.findViewById(R.id.ivPlaceHolder);
      grpPlaceHolderUI = itemView.findViewById(R.id.grpPlaceHolderUI);
      clItemRoot = itemView.findViewById(R.id.clItemRoot);
      ivGradientColor = itemView.findViewById(R.id.ivGradientColor);
      svVideoRenderer.init(rootEglBase.getEglBaseContext(), null);
      svVideoRenderer.setEnableHardwareScaler(true);
    }
  }

  public interface ClickEvent {
    void muteOpponent(BigInteger handleId);
  }
}
