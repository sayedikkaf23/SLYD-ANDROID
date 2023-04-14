package chat.hola.com.app.home.trending;

import chat.hola.com.app.dagger.FragmentScoped;
import chat.hola.com.app.home.trending.model.TrendingModel;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>TrendingModule</h1>
 *
 * @author 3Embed
 * @version 1.0
 * @since 18/6/18.
 */

//@FragmentScoped
@Module
public interface TrendingModule {
    @FragmentScoped
    @Binds
    TrendingModel trendingModel(TrendingModel model);

//    @FragmentScoped
//    @Provides
//    ContentAdapter contentAdapter(LandingActivity context, TypefaceManager typefaceManager) {
//        return new ContentAdapter(context, typefaceManager);
//    }

}
