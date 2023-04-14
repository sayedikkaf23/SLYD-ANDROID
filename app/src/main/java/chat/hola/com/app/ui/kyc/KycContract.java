package chat.hola.com.app.ui.kyc;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import java.util.List;
import java.util.Map;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;

/**
 * <h1>KycContract</h1>
 * <p>contains presenter and view interfaces of {@link KycActivity}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 Dec 2019
 */
public interface KycContract {

    interface View extends BaseView {
        void moveNext();

        void typesOfDocuments(List<String> response);

        void launchCropImage(Uri data);

        void setFrontImage(Bitmap bitmap, String url);

        void setBackImage(Bitmap bitmapToUpload, String uri);
    }

    interface Presenter extends BasePresenter<View> {
        void updateUserDetail(Map<String, Object> params);

        void typesOfDocuments();

        void parseSelectedImage(int requestCode, int resultCode, Intent data);

        void parseCapturedImage(int requestCode, int resultCode, Intent data);

        void parseCropedImage(int requestCode, int resultCode, Intent data, boolean isFront);
    }
}
