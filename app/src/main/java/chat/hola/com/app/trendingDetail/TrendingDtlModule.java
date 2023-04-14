package chat.hola.com.app.trendingDetail;

import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * <h1>TrendingDtlModule</h1>
 *
 * @author ankit
 * @version 1.0
 * @since 21/2/18.
 */

@Module
public interface TrendingDtlModule {

    @ActivityScoped
    @Binds
    TrendingDtlContract.Presenter presenter(TrendingDtlPresenter presenter);

    @ActivityScoped
    @Binds
    TrendingDtlContract.View view(TrendingDetail detail);
}
