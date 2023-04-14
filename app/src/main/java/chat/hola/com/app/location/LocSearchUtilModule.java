package chat.hola.com.app.location;

import chat.hola.com.app.Utilities.App_permission_23;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Module;
import dagger.Provides;

/**
 * Created by ankit on 3/4/18.
 */

//@ActivityScoped
@Module
public class LocSearchUtilModule {

    @ActivityScoped
    @Provides
    App_permission_23 provideAppPermission23(Location_Search_Activity activity){
        return new App_permission_23(activity);
    }

}
