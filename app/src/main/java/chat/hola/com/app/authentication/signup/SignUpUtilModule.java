package chat.hola.com.app.authentication.signup;

import android.app.Activity;

import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.models.Register;
import dagger.Module;
import dagger.Provides;

@Module
public class SignUpUtilModule {

    @ActivityScoped
    @Provides
    Register register() {
        return new Register();
    }

    @ActivityScoped
    @Provides
    ImageSourcePicker imageSourcePicker(Activity activity) {
        return new ImageSourcePicker(activity, true, false);
    }
}
