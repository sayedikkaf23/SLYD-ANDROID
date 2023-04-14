package chat.hola.com.app.DublyCamera.deepar;

import android.app.Activity;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Binds;
import dagger.Module;

@Module
public interface DeeparTabCameraModule {
  @ActivityScoped
  @Binds
  DeeparTabCameraContract.Presenter presenter(DeeparTabCameraPresenter presenter);

  @ActivityScoped
  @Binds
  DeeparTabCameraContract.View view(DeeparFiltersTabCameraActivity activity);

  @ActivityScoped
  @Binds
  Activity activity(DeeparFiltersTabCameraActivity activity);
}

