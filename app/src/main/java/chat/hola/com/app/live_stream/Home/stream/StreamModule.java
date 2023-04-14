package chat.hola.com.app.live_stream.Home.stream;

import dagger.Binds;
import dagger.Module;

/**
 * Created by moda on 11/20/2018.
 */
@Module
public interface StreamModule {
    @Binds
    StreamContract.Presenter presenter(StreamPresenter presenter);

    @Binds
    StreamContract.View view(StreamActivity activity);

}
