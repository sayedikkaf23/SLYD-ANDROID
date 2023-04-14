package chat.hola.com.app.authentication.forgotpassword;

public interface ForgotPasswordContract {

    interface View {
        void message(String message);

        void success();

        void progress(boolean b);

        /*
         * Bug Title: Forgot password ->Then message form the green box is not showing.
         * Bug Id: DUBAND146
         * Fix Desc: add api
         * Fix Dev: Hardik
         * Fix Date: 11/5/21
         * */
        void emailRegisterd(String email);

        void emailNotRegisterd();
    }

    interface Presenter {
        void forgotPassword(String email);

        void requestOtp(String email);

        /*
        * Bug Title: Forgot password ->Then message form the green box is not showing.
        * Bug Id: DUBAND146
        * Fix Desc: add api
        * Fix Dev: Hardik
        * Fix Date: 11/5/21
         * */
        void verifyIsEmailRegistered(String email);
    }
}
