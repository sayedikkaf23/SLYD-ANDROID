package chat.hola.com.app.stars;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.home.contact.Friend;
import dagger.Module;
import dagger.Provides;

@Module
public class StarUtilModule {
    @ActivityScoped
    @Provides
    List<Friend> friendList() {
        return new ArrayList<Friend>();
    }

    @ActivityScoped
    @Provides
    StarAdapter getStarAdapter(List<Friend> arrayList, StarActivity mContext, TypefaceManager typefaceManager) {
        return new StarAdapter(arrayList, mContext, typefaceManager);
    }
}
