package chat.hola.com.app.request_star_profile.request_star;

import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Module;
import dagger.Provides;

@Module
public class RequestStarUtilModule {


    @ActivityScoped
    @Provides
    ImageSourcePicker imageSourcePicker(RequestStarProfileActivity activity) {
        return new ImageSourcePicker(activity, true, false);
    }

}
