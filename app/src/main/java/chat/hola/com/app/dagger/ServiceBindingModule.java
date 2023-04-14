package chat.hola.com.app.dagger;

import chat.hola.com.app.Service.PostService;
import chat.hola.com.app.Service.ShareService;
import chat.hola.com.app.post.PostModule;
import chat.hola.com.app.poststory.StoryService;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by ankit on 20/2/18.
 */

@Module
public interface ServiceBindingModule {

    @ActivityScoped
    @ContributesAndroidInjector(modules = PostModule.class)
    PostService postService();

    @ActivityScoped
    @ContributesAndroidInjector
    StoryService postStoryService();

    @ActivityScoped
    @ContributesAndroidInjector()
    ShareService shareService();

}
