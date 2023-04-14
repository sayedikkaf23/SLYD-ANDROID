package chat.hola.com.app.authentication.verifyEmail;

public interface VerifyEmailContract {

    interface View {
        void message(String message);

        void success();

        void progress(boolean b);

        void otpVerified();

        void businessEmailVerified();
    }

    interface Presenter {
        void resend(String phone, String email);

        void verify(String otp, String email);

        void verifyBusinessEmail(String otp, String email, boolean isVisible);

        void businessEmailVerificationCode(String email);
    }
}
