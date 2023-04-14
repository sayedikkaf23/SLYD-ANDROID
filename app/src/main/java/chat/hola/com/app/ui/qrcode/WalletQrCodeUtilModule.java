package chat.hola.com.app.ui.qrcode;


import androidx.appcompat.app.AlertDialog;

import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.dagger.ActivityScoped;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>WalletQrCodeUtilModule<h1/>
 *
 * <p>This module provides all utilities which @{@link WalletQrCodeActivity} and child fragments can @Inject,
 * can add List, Adapters, Custom components, etc</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 05 Dec 2019
 */
@Module
public class WalletQrCodeUtilModule {

    @ActivityScoped
    @Provides
    AlertDialog.Builder builder(WalletQrCodeActivity activity) {
        return new AlertDialog.Builder(activity);
    }

    @ActivityScoped
    @Provides
    Loader loader(WalletQrCodeActivity context) {
        return new Loader(context);
    }
}
