package chat.hola.com.app.DublyCamera.live_stream;

import android.app.Activity;

import chat.hola.com.app.DublyCamera.CameraActivity;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

/**
 * Created by moda on 11/20/2018.
 */
@Module
public interface CameraStreamModule {
    @ActivityScoped
    @Binds
    CameraStreamContract.Presenter presenter(CameraStreamPresenter presenter);

    @ActivityScoped
    @Binds
    CameraStreamContract.View view(CameraActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(CameraActivity activity);
}
