package chat.hola.com.app.ui.stripe;


import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;

import com.ezcall.android.R;

import java.util.ArrayList;
import java.util.List;

import chat.hola.com.app.Utilities.Loader;
import chat.hola.com.app.dagger.ActivityScoped;
import chat.hola.com.app.ui.kyc.KycActivity;
import chat.hola.com.app.wallet.transaction.Model.TransactionData;
import dagger.Module;
import dagger.Provides;

/**
 * <h1>KycUtilModule<h1/>
 *
 * <p>This module provides all utilities which @{@link KycActivity} and child fragments can @Inject,
 * can add List, Adapters, Custom components, etc</p>
 *
 * @author : Shaktisinh Jadeja
 * @version : 1.0
 * @company : 3Embed Software Technologies Pvt. Ltd.
 * @since : 05 Dec 2019
 */
@Module
public class AddStripeUtilModule {

    @ActivityScoped
    @Provides
    AlertDialog.Builder builder(AddStripeActivity activity) {
        return new AlertDialog.Builder(activity);
    }

    @ActivityScoped
    @Provides
    List<String> list() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    ArrayAdapter arrayAdapter(KycActivity activity) {
        return new ArrayAdapter(activity, R.layout.custom_select_dialog_singlechoice);
    }

    @ActivityScoped
    @Provides
    List<TransactionData> transactionData() {
        return new ArrayList<>();
    }

    @ActivityScoped
    @Provides
    Loader loader(AddStripeActivity context) {
        return new Loader(context);
    }
}
