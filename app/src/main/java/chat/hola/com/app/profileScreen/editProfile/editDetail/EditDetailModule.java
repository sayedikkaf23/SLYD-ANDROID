package chat.hola.com.app.profileScreen.editProfile.editDetail;

import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.profileScreen.editProfile.EditProfileActivity;
import chat.hola.com.app.profileScreen.editProfile.EditProfileContract;
import chat.hola.com.app.profileScreen.editProfile.EditProfilePresenter;
import dagger.Binds;
import dagger.Module;

@Module
public interface EditDetailModule {

    @ActivityScoped
    @Binds
    EditDetailContract.Presenter presenter(EditDetailPresenter presenter);

    @ActivityScoped
    @Binds
    EditDetailContract.View view(EditDetailActivity activity);

}
