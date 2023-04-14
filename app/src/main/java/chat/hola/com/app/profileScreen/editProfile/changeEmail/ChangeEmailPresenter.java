package chat.hola.com.app.profileScreen.editProfile.changeEmail;

import android.content.Context;
import android.util.Patterns;

import com.ezcall.android.R;

import javax.inject.Inject;

/**
 * <h>ChangeEmailPresenter</h>
 * @author 3Embed.
 * @since 19/3/18.
 */

public class ChangeEmailPresenter implements ChangeEmailContract.Presenter {

    @Inject
    ChangeEmailContract.View view;
    String emailNotValidMsg = null;
    @Inject
    Context context;
    @Inject
    public ChangeEmailPresenter() {
    }

    @Override
    public void init() {
        view.applyFont();
    }

    @Override
    public boolean isEmailValidated(String email) {
        if(email == null || email.isEmpty()){
            view.showMessage(null,R.string.emailEmptyMsg);
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            view.showMessage(null,R.string.emailEmptyMsg);
            return false;
        }

        return true;
    }

}
