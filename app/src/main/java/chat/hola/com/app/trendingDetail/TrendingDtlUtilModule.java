package chat.hola.com.app.trendingDetail;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.home.model.Data;
import chat.hola.com.app.trendingDetail.model.TrendingAdapter;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>TrendingDtlUtilModule</h1>
 *
 * @author ankit
 * @version 1.0
 * @since 21/2/18.
 */

//@ActivityScoped
@Module
public class TrendingDtlUtilModule {

    @ActivityScoped
    @Provides
    List<Data> data() {
        return new ArrayList<Data>();
    }

    @ActivityScoped
    @Provides
    TrendingAdapter trendingAdapter(TrendingDetail context, List<Data> data) {
        return new TrendingAdapter(context, data);
    }
}
