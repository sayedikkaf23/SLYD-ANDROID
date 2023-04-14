package chat.hola.com.app.profileScreen.addChannel;

import chat.hola.com.app.Dialog.CustomProgressDialog;
import chat.hola.com.app.Dialog.ImageSourcePicker;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Module;
import dagger.Provides;

/**
 * Created by ankit on 27/2/18.
 */

//@ActivityScoped
@Module
public class AddChannelUtilModule {

//    @ActivityScoped
//    @Provides
//    CategoryAdapter categoryPicker(TypefaceManager typefaceManager){
//        return new CategoryAdapter(typefaceManager);
//    }
//
//    @ActivityScoped
//    @Provides
//    CategoryDialog categoryDialog(CategoryAdapter categoryPicker, TypefaceManager typefaceManager){
//        return new CategoryDialog(categoryPicker,typefaceManager);
//    }

    @ActivityScoped
    @Provides
    ImageSourcePicker imageSourcePicker(AddChannelActivity activity){
        return new ImageSourcePicker(activity,false, false);
    }

    @ActivityScoped
    @Provides
    CustomProgressDialog customProgressDialog(AddChannelActivity activity,TypefaceManager typefaceManager){
        return new CustomProgressDialog("your Channel being created...",activity,typefaceManager);
    }

}
