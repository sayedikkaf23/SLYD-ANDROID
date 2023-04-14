package chat.hola.com.app.profileScreen.editProfile.changeEmail;

import chat.hola.com.app.Utilities.BaseView;

/**
 * <h>ChangeEmailContract</h>
 * @author  3Embed.
 * @since 19/3/18.
 */

public class ChangeEmailContract {
    interface View extends BaseView{

        void applyFont();
    }

    interface Presenter {

        void init();

        boolean isEmailValidated(String email);
    }
}
