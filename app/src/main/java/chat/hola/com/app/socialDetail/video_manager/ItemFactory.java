package chat.hola.com.app.socialDetail.video_manager;

import android.app.Activity;

import com.bumptech.glide.Glide;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;

import java.io.IOException;

import chat.hola.com.app.home.model.Data;

/**
 * <h1></h1>
 *
 * @author DELL
 * @version 1.0
 * @since 11/30/2018.
 */
public class ItemFactory {

    public static BaseVideoItem createItem(Activity activity, VideoPlayerManager<MetaData> videoPlayerManager, Data data) throws IOException {
//        return new VideoItem(videoPlayerManager, Picasso.with(activity), data, activity);


        return new VideoItem(videoPlayerManager, Glide.with(activity), data, activity);
    }
}
