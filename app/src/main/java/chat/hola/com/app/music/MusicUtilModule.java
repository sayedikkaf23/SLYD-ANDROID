package chat.hola.com.app.music;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.home.model.Data;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>HashtagUtilModule</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */
//@ActivityScoped
@Module
public class MusicUtilModule {

    @ActivityScoped
    @Provides
    List<Data> getMusicdata() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    MusicAdapter musicAdapter(List<Data> dataList, Activity mContext) {
        return new MusicAdapter(dataList, mContext);
    }
}
