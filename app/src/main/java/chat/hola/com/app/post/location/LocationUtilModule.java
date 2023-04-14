package chat.hola.com.app.post.location;


import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.models.Place;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>LocationUtilModule</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 23 September 2019
 */
@Module
public class LocationUtilModule {
    @ActivityScoped
    @Provides
    List<Place> locations() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    LocationAdapter locationAdapter(List<Place> places, Activity mContext, TypefaceManager typefaceManager) {
        return new LocationAdapter(places, mContext, typefaceManager);
    }
}
