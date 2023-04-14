package chat.hola.com.app.authentication.newpassword;

import java.util.Map;

public interface NewPasswordContract {

    interface View {
        void message(String message);

        /**
         * Show message
         *
         * @param message : string id
         */
        void message(int message);

        /**
         * shows loader
         */
        void showLoader();

        /**
         * hides loader
         */
        void hideLoader();

        void success();

        void passwordMatched();

        void passwordNotMatched();
    }

    interface Presenter {

        void changePassword(Map<String, String> params);

        void  checkPassword(String password);

        void updatePassword(String password);
    }
}
