package chat.hola.com.app.home.activity.youTab.channelrequesters;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.home.activity.youTab.channelrequesters.model.ChannelRequesterAdapter;
import chat.hola.com.app.home.activity.youTab.channelrequesters.model.DataList;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>SearchUserUtilModule</h1>
 *
 * @author 3Embed
 * @version 1.0.
 * @since 4/9/2018.
 */
//@ActivityScoped
@Module
public class ChannelRequestersUtilModule {

    @ActivityScoped
    @Provides
    List<DataList> getRequestedChannels() {
        return new ArrayList<DataList>();
    }

//    @ActivityScoped
//    @Provides
//    ClickListner listner(){
//     //   return ClickListner;
//    }

    @ActivityScoped
    @Provides
    ChannelRequesterAdapter channelRequesterAdapter(Activity activity, List<DataList> list, TypefaceManager typefaceManager) {
        return new ChannelRequesterAdapter(activity, list, typefaceManager);
    }

}
