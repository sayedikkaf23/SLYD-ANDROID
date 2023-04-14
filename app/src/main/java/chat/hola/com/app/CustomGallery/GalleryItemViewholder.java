package chat.hola.com.app.CustomGallery;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.ezcall.android.R;


/**
 * Handler for the Gallery image view .
 *
 * @since 09/05/17.
 */
public class GalleryItemViewholder extends RecyclerView.ViewHolder {
    GrideSquareImageView thumb_nail;

    GalleryItemViewholder(View itemView) {
        super(itemView);
        thumb_nail = (GrideSquareImageView) itemView.findViewById(R.id.thumb_nail_image);
    }
}