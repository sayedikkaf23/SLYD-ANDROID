package chat.hola.com.app.ui.paymentgateway;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.Utilities.TypefaceManager;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.models.PaymentGateway;
import chat.hola.com.app.ui.adapter.PaymentGatewayAdapter;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>KycUtilModule<h1/>
 *
 * <p>This module provides all utilities which @{@link PaymentGatewaysActivity} and child fragments can @Inject,
 * can add List, Adapters, Custom components, etc</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 05 Dec 2019
 */
@Module
public class PaymentGatewaysUtilModule {

    @ActivityScoped
    @Provides
    List<PaymentGateway> paymentGateways() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    PaymentGatewayAdapter paymentGatewayAdapter(Context context, TypefaceManager typefaceManager, List<PaymentGateway> paymentGateways) {
        return new PaymentGatewayAdapter(context, typefaceManager, paymentGateways);
    }

    @ActivityScoped
    @Provides
    Loader loader(PaymentGatewaysActivity context) {
        return new Loader(context);
    }
}
