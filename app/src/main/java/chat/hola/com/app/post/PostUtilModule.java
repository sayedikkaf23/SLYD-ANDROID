package chat.hola.com.app.post;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import chat.hola.com.app.Dialog.ChannelPicker;
import chat.hola.com.app.Utilities.SocialShare;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.location.Address_list_holder;
import chat.hola.com.app.location.Address_list_item_pojo;
import chat.hola.com.app.post.model.AddressAdapter;
import dagger.Module;
import dagger.Provides;

/**
 * Created by ankit on 27/2/18.
 */

@Module
public class PostUtilModule {

    /* @ActivityScoped
     @Provides
     ProgressDialog alertDialog(PostPresenter presenter, TypefaceManager typefaceManager){
         return new ProgressDialog(presenter,typefaceManager);
     }
    */
    @ActivityScoped
    @Provides
    ChannelPicker channelPicker(TypefaceManager typefaceManager) {
        return new ChannelPicker(typefaceManager);
    }

//    @ActivityScoped
//    @Provides
//    CategoryAdapter categoryPicker(TypefaceManager typefaceManager){
//        return new CategoryAdapter(typefaceManager);
//    }

//    @ActivityScoped
//    @Provides
//    CategoryDialog categoryDialog(CategoryAdapter categoryPicker, TypefaceManager typefaceManager){
//        return new CategoryDialog(categoryPicker,typefaceManager);
//    }

    @ActivityScoped
    @Provides
    SocialShare instaShare(PostActivity postActivity) {
        return new SocialShare(postActivity);
    }

//    @ActivityScoped
//    @Provides
//    CallbackManager callbackManager() {
//        return CallbackManager.Factory.create();
//    }
//
//    @ActivityScoped
//    @Provides
//    ShareDialog shareDialog(PostActivity postActivity) {
//        return new ShareDialog(postActivity);
//    }

    @ActivityScoped
    @Provides
    Address_list_holder listHolder() {
        return new Address_list_holder();
    }

    @ActivityScoped
    @Provides
    List<Address_list_item_pojo> list() {
        return new ArrayList<Address_list_item_pojo>();
    }

    @ActivityScoped
    @Provides
    AddressAdapter addressAdapter(List<Address_list_item_pojo> list) {
        return new AddressAdapter(list);
    }

    @ActivityScoped
    @Provides
    AlertDialog.Builder builder(PostActivity activity) {
        return new AlertDialog.Builder(activity);
    }

}
