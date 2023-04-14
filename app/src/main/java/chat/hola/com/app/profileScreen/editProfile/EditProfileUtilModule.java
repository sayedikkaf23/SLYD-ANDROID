package chat.hola.com.app.profileScreen.editProfile;

import chat.hola.com.app.Dialog.CustomProgressDialog;
import chat.hola.com.app.Dialog.DatePickerFragment;
import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.Utilities.App_permission_23;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Module;
import dagger.Provides;

/**
 * <h>EditProfileUtilModule</h>
 * @author 3Embed.
 * @since 27/2/18.
 */

//@ActivityScoped
@Module
public class EditProfileUtilModule {

    @ActivityScoped
    @Provides
    DatePickerFragment datePickerFragment(){
        return new DatePickerFragment();
    }


    @ActivityScoped
    @Provides
    ImageSourcePicker imageSourcePicker(EditProfileActivity activity){
        return new ImageSourcePicker(activity,true, true);
    }

    @ActivityScoped
    @Provides
    CustomProgressDialog customProgressDialog(EditProfileActivity activity, TypefaceManager typefaceManager){
        return new CustomProgressDialog("your profile being edited...",activity,typefaceManager);
    }

    @ActivityScoped
    @Provides
    App_permission_23 provideAppPermission23(EditProfileActivity activity){
        return new App_permission_23(activity);
    }

}
