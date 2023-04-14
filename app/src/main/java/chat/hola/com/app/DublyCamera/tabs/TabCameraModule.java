package chat.hola.com.app.DublyCamera.tabs;

import android.app.Activity;
import chat.hola.com.app.DublyCamera.live_stream.CameraStreamContract;
import chat.hola.com.app.DublyCamera.live_stream.CameraStreamPresenter;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;


  @Module
  public interface TabCameraModule {
    @ActivityScoped
    @Binds
    CameraStreamContract.Presenter presenter(CameraStreamPresenter presenter);

    @ActivityScoped
    @Binds
    CameraStreamContract.View view(TabCameraActivity activity);

    @ActivityScoped
    @Binds
    Activity activity(TabCameraActivity activity);
  }

