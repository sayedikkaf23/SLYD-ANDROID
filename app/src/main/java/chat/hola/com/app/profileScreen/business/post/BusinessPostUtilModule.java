package chat.hola.com.app.profileScreen.business.post;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.models.ActionButtonResponse;
import chat.hola.com.app.models.PostTypeResponse;
import chat.hola.com.app.profileScreen.business.post.actionbutton.ActionButtonAdapter;
import chat.hola.com.app.profileScreen.business.post.type.PostTypeAdapter;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>BusinessPostUtilModule</h1>
 *
 * @author DELL
 * @version 1.0
 * @since 09 September 2019
 */
@Module
public class BusinessPostUtilModule {
    @ActivityScoped
    @Provides
    List<ActionButtonResponse.BizButton> buttonTexts() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    List<PostTypeResponse.PostType> postTypes() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    ActionButtonAdapter actionButtonAdapter(Context context, List<ActionButtonResponse.BizButton> buttonTexts, TypefaceManager typefaceManager) {
        return new ActionButtonAdapter(context, buttonTexts, typefaceManager);
    }

    @ActivityScoped
    @Provides
    PostTypeAdapter postTypeAdapter(Context context, List<PostTypeResponse.PostType> buttonTexts, TypefaceManager typefaceManager) {
        return new PostTypeAdapter(context, buttonTexts, typefaceManager);
    }
}
