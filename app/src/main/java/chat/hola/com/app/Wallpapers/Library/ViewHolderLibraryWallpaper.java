package chat.hola.com.app.Wallpapers.Library;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.ezcall.android.R;

/**
 * Created by moda on 11/10/17.
 */

public class ViewHolderLibraryWallpaper extends RecyclerView.ViewHolder {


    public ImageView wallpaper;


    public ViewHolderLibraryWallpaper(View view) {
        super(view);


        wallpaper = (ImageView) view.findViewById(R.id.wallpaper);

    }
}
