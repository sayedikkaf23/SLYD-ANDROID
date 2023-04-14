package chat.hola.com.app.cameraActivities.ui.view;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ezcall.android.R;

import chat.hola.com.app.cameraActivities.SandriosCamera;
import chat.hola.com.app.cameraActivities.configuration.CameraConfiguration;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Arpit Gandhi
 */
public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageGalleryAdapter.GalleryViewHolder> {

    ArrayList<PickerTile> pickerTiles;
    Context context;
    OnItemClickListener onItemClickListener;

    public ImageGalleryAdapter(Context context, int type) {

        this.context = context;

        pickerTiles = new ArrayList<>();

        if (type == CameraConfiguration.VIDEO) {
            Cursor videoCursor = null;
            try {
                final String[] columns = {MediaStore.Video.VideoColumns.DATA};
                final String orderBy = MediaStore.Video.Media.DATE_ADDED + " DESC";

                videoCursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
                if (videoCursor != null) {
                    int count = 0;
                    while (videoCursor.moveToNext()) {
                        String videoLocation;
                        videoLocation = videoCursor.getString(videoCursor.getColumnIndex(MediaStore.Video.Media.DATA));
                        File videoFile = new File(videoLocation);
                      //  pickerTiles.add(new PickerTile(Uri.fromFile(videoFile)));
                        count++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (videoCursor != null && !videoCursor.isClosed()) {
                    videoCursor.close();
                }
            }
        } else {
            Cursor imageCursor = null;
            try {
                final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
                final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";

                imageCursor = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
                if (imageCursor != null) {
                    int count = 0;
                    while (imageCursor.moveToNext()) {
                        String imageLocation = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        File imageFile = new File(imageLocation);
                      //  pickerTiles.add(new PickerTile(Uri.fromFile(imageFile)));
                        count++;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (imageCursor != null && !imageCursor.isClosed()) {
                    imageCursor.close();
                }
            }

        }
    }

    public ImageGalleryAdapter(Context context) {

        this.context = context;

        pickerTiles = new ArrayList<>();

        Cursor imageCursor = null;
        try {
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
            final String orderBy = MediaStore.Images.Media.DATE_ADDED + " DESC";


            imageCursor = context.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            //imageCursor = sContext.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
            if (imageCursor != null) {
                int count = 0;
                while (imageCursor.moveToNext()) {
                    int type= imageCursor.getType(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    String imageLocation = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    File imageFile = new File(imageLocation);
                    pickerTiles.add(new PickerTile(Uri.fromFile(imageFile),type));
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (imageCursor != null && !imageCursor.isClosed()) {
                imageCursor.close();
            }
        }
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(context, R.layout.image_item, null);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GalleryViewHolder holder, final int position) {

        PickerTile pickerTile = getItem(position);

        Uri uri = pickerTile.getImageUri();
        if (uri != null) {
            int type = pickerTile.getType();//BaseSandriosActivity.getMimeType(context, uri.toString());
            if (type == SandriosCamera.MediaType.PHOTO) {
                holder.videoIndicator.setVisibility(View.GONE);
            } else {
                holder.videoIndicator.setVisibility(View.VISIBLE);
            }
            Glide.with(context)
                    .load(uri)
                    .thumbnail(0.1f)
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(ContextCompat.getDrawable(context, R.drawable.ic_gallery))
                    .error(ContextCompat.getDrawable(context, R.drawable.ic_error))
                    .into(holder.iv_thumbnail);

            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return pickerTiles.size();
    }

    public PickerTile getItem(int position) {
        return pickerTiles.get(position);
    }

    public void setOnItemClickListener(
            OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public static class PickerTile {

        final Uri imageUri;
        private int type;

        PickerTile(@NonNull Uri imageUri, int type) {
            this.imageUri = imageUri;
            this.type = type;
        }

        @Nullable
        public Uri getImageUri() {
            return imageUri;
        }

        @Override
        public String toString() {
            return "ImageTile: " + imageUri;
        }

        public int getType() {
            return type;
        }
    }

    class GalleryViewHolder extends RecyclerView.ViewHolder {
        View videoIndicator;
        ImageView iv_thumbnail;

        GalleryViewHolder(View view) {
            super(view);
            videoIndicator = view.findViewById(R.id.video_indicator);
            iv_thumbnail = (ImageView) view.findViewById(R.id.image);
        }
    }
}