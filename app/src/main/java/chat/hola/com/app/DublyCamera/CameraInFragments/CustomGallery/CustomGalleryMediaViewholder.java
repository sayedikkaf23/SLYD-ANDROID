package chat.hola.com.app.DublyCamera.CameraInFragments.CustomGallery;


import android.view.View;

import com.ezcall.android.R;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Handler for the Gallery image view .
 * @since  09/05/17.
 */
public class CustomGalleryMediaViewholder extends RecyclerView.ViewHolder
{ AppCompatImageView video_icon;
    AppCompatImageView thumb_nail;
    CustomGalleryMediaViewholder(View itemView)
    {
        super(itemView);
        video_icon=(AppCompatImageView)itemView.findViewById(R.id.video_icon);
        thumb_nail = (AppCompatImageView ) itemView.findViewById(R.id.thumb_nail_image);
    }
}