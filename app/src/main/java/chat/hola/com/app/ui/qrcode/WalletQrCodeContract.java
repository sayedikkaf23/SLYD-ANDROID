package chat.hola.com.app.ui.qrcode;

import chat.hola.com.app.base.BasePresenter;
import chat.hola.com.app.base.BaseView;

/**
 * <h1>WalletQrCodeContract</h1>
 * <p>contains presenter and view interfaces of {@link WalletQrCodeActivity}</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 06 Dec 2019
 */
public interface WalletQrCodeContract {

    interface View extends BaseView {
        void displayInformation(String qrCode);
    }

    interface Presenter extends BasePresenter<View> {

        void fetchQrCode();
    }
}
