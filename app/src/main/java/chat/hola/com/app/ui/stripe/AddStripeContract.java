package chat.hola.com.app.ui.stripe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import java.util.List;
import java.util.Map;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;
import chat.hola.com.app.models.Country;
import chat.hola.com.app.ui.kyc.KycActivity;

/**
 * <h1>KycContract</h1>
 * <p>contains presenter and view interfaces of {@link KycActivity}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 Dec 2019
 */
public interface AddStripeContract {

    interface View extends BaseView {
        void success();

        void launchCropImage(Uri data);

        void setDocumentImage(Bitmap bitmap, String url);

        void setCountries(List<Country> countries);
    }

    interface Presenter extends BasePresenter<View> {
        void addAccount(Map<String, Object> params);

        void parseSelectedImage(int requestCode, int resultCode, Intent data);

        void parseCapturedImage(int requestCode, int resultCode, Intent data);

        void parseCropedImage(int requestCode, int resultCode, Intent data);

        void getCountry();
    }
}
